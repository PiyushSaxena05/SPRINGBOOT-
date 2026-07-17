# Circular Dependency, Bean Scope & Bean Initialization in Spring

## 1. Quick Recap: DI, IoC, Bean

| Concept | Meaning |
|---|---|
| DI (Dependency Injection) | Spring gives a class its required dependency instead of the class creating it itself |
| IoC (Inversion of Control) | Object creation & dependency management is handled by the Spring container |
| Bean | An object created and managed by the Spring IoC container |

Classes annotated with `@Component`, `@Service`, `@Repository`, `@Controller`, or methods annotated with `@Bean` become Spring-managed beans.

---

## 2. @Configuration is also a Component

```java
@Configuration
public class AppConfig {
    @Bean
    public PaymentService paymentService() {
        return new PaymentService();
    }
}
```

`@Configuration` internally contains `@Component` → so a `@Configuration` class is itself picked up during component scanning and registered as a bean.

`@Configuration → @Component → detected & managed by Spring`

---

## 3. Does Spring Create Beans in Random Order?

No. Beans are created based on their dependency chain.

Example:
`OrderController → OrderService → PaymentService → PaymentGateway`

Creation order (bottom-up): `PaymentGateway → PaymentService → OrderService → OrderController`

Spring always creates the dependency first, then the dependent class.

---

## 4. Why This Causes Circular Dependency

If `A` needs `B`, and `B` needs `A`:

```
To create A, I need B.
To create B, I need A.
To create A, I need B.
...
```

There's no clear starting point → circular dependency problem.

---

## 5. What is Circular Dependency?

Two or more classes depending on each other, directly or indirectly.

```java
@Service
public class OrderService {
    private final PaymentService paymentService;
    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}

@Service
public class PaymentService {
    private final OrderService orderService;
    public PaymentService(OrderService orderService) {
        this.orderService = orderService;
    }
}
```

Spring can't decide which one to build first → deadlock-like situation.

---

## 6. Not Just a Spring Problem

Plain Java has the same issue:

```java
public class A {
    private B b;
    public A() { this.b = new B(); }
}

public class B {
    private A a;
    public B() { this.a = new A(); }
}
```

Calling `new A()` → constructor calls keep stacking → `StackOverflowError`.

**Conclusion:** Circular dependency is fundamentally a design problem. Spring just exposes it at bean-creation time.

---

## 7. Why Constructor Injection Fails on Circular Dependency

Rule: an object can't be created until **all** constructor arguments exist.

```
Create OrderService → needs PaymentService
Create PaymentService → needs OrderService
Create OrderService → needs PaymentService
...
```

No starting point → Spring throws `BeanCurrentlyInCreationException` and the app fails to start.

---

## 8. Why Constructor Injection is Still Preferred

Even though it fails on circular deps, constructor injection is still the recommended style because:
- It makes dependencies **explicit and mandatory**.
- `OrderService cannot exist without PaymentService` is communicated clearly by the constructor signature.

The circular *design* is the real problem — not constructor injection itself.

---

## 9 & 10. Setter / Field Injection and Circular Dependency

With setter/field injection, object creation and dependency injection happen in **separate steps**:

```
Step 1: Create empty OrderService object
Step 2: Create empty PaymentService object
Step 3: Inject PaymentService into OrderService
Step 4: Inject OrderService into PaymentService
```

So Spring can create the raw object first, and wire dependencies afterward — which sometimes lets it resolve circular dependencies that constructor injection cannot.

This does **not** make circular dependency good practice — it just means Spring has more flexibility here.

---

## 11. Why It May Work — Early Reference Idea

```java
@Service
public class OrderService {
    @Autowired
    private PaymentService paymentService;
    public OrderService() { System.out.println("OrderService created"); }
}
```

Spring's internal logic (conceptually):
> I created OrderService, it's not fully initialized yet, but I can hold an early reference to it. Now I create PaymentService and give it that early reference. Later, I come back and inject PaymentService into OrderService.

This is the basis of Spring's **early reference mechanism** (backed internally by early bean exposure via ObjectFactory / third-level cache).

---

## 12–13. Normal Flow vs Circular Flow

**Normal (A needs B):**
```
Create B → Inject deps into B → Initialize B → B ready
Create A → Inject B into A → Initialize A → A ready
```

**Circular (A needs B, B needs A):**
```java
A a = new A();     // A exists, not fully wired
B b = new B();     // B exists
b.setA(a);          // B gets early reference of A
a.setB(b);           // A gets B
```

**Early reference** = Spring exposes a not-yet-fully-initialized bean reference so another bean can use it temporarily while resolving the cycle.

---

## 14. Spring Boot Discourages This

Spring Framework *can* resolve some setter/field circular dependencies internally, but **Spring Boot disables this by default from 2.6 onward**.

```properties
spring.main.allow-circular-references=false   # default
```

You *can* set it to `true`, but that's not a real fix — fixing the design is.

---

## 15. Circular Dependency = Design Smell

Ask:
- Why does A need B?
- Why does B also need A?
- Are both classes doing too much?
- Can shared logic move to a third class?
- Can one direction of the dependency be removed?

**Better designs instead of A ↔ B:**
- `OrderService → PaymentService` only (one direction)
- Both depend on a shared `PaymentProcessor` / `PaymentStatusRepository`

---

## 16. Best Fixes

| Problem | Better Design |
|---|---|
| Two services call each other | Move shared logic to a third service |
| One service has too many responsibilities | Split responsibilities |
| Both classes depend on each other's internals | Use an interface or event-based communication |
| Service mixes orchestration + business logic | Create a separate coordinator/orchestrator class |

**Principle:** Avoid circular dependency by designing clear responsibility boundaries.

---

## 17. What is Bean Scope?

Bean scope decides **how many objects** Spring creates for a bean definition, and **how long** they live.

Core scopes: `singleton`, `prototype`
Web scopes: `request`, `session`, `application`, `websocket`

---

## 18–21. Singleton Scope

- Default scope in Spring — exactly **one object per bean definition** in the container.

```java
@Component
public class PaymentService { }
// same as
@Component
@Scope("singleton")
public class PaymentService { }
```

```java
PaymentService p1 = context.getBean(PaymentService.class);
PaymentService p2 = context.getBean(PaymentService.class);
System.out.println(p1 == p2); // true
```

**Important:** Spring singleton ≠ "only one object of this class can ever exist in the JVM." You can still do `new PaymentService()` manually — it's just not Spring-managed.

**Singleton is per bean definition, not per class:**

```java
@Bean
public User createUser() { return new User(); }

@Bean
public User createUser2() { return new User(); }
```

→ 2 bean definitions → 2 separate singleton `User` objects. Singleton = one object per bean name/definition, not one object per class (different from the classic GoF Singleton pattern).

---

## 22–24. Prototype Scope

A **new object every time the bean is requested** from the container.

```java
@Component
@Scope("prototype")
public class OrderRequest {
    public OrderRequest() { System.out.println("OrderRequest created"); }
}
```

```java
OrderRequest r1 = context.getBean(OrderRequest.class);
OrderRequest r2 = context.getBean(OrderRequest.class);
System.out.println(r1 == r2); // false
```

Feels close to plain `new`, but Spring is still creating it (not you manually).

**When to use which:**

| Scope | Best for |
|---|---|
| Singleton | Stateless beans providing behavior (e.g., services: `processPayment()`, `validateOrder()`) |
| Prototype | Stateful beans holding changing/request-specific data |

---

## 25. Prototype Bean Inside a Singleton Bean

**Q:** If a prototype bean is injected into a singleton, does the singleton get a fresh prototype object every time?
**A:** No.

The singleton is created only **once**. At that point, Spring injects **one** prototype instance into it. After that, the singleton keeps reusing that same reference forever.

```java
@Service
public class OrderService {
    private final OrderRequest orderRequest;
    public OrderService(OrderRequest orderRequest) {
        this.orderRequest = orderRequest;
    }
}
```

"Prototype" means a new object every time it's **requested from the container** — not every time a singleton method runs.

---

## 26–29. Web Scopes

| Scope | One object per |
|---|---|
| request | One HTTP request |
| session | One user session |
| application | Whole web app (ServletContext) |
| websocket | One WebSocket session |

Only available in a web-aware `ApplicationContext`.

- **Request scope:** new bean object per HTTP request.
- **Session scope:** new bean object per user session (User A and User B each get their own).
- **Application scope:** one bean shared across the entire web application, tied to the ServletContext.

---

## 30. When Are Beans Actually Created?

| Type | Meaning |
|---|---|
| Eager initialization | Bean created at application startup |
| Lazy initialization | Bean created only when actually needed |

By default: singleton beans → eager; prototype beans → created lazily on request. Lazy behavior can also be forced via `@Lazy`.

---

## 31–32. Eager Initialization

Default behavior for singletons — bean is created as soon as the context starts.

```java
@Component
public class PaymentService {
    public PaymentService() { System.out.println("PaymentService created"); }
}
```

**Why it's the default:** it helps the app **fail fast**. If a bean has a config issue or missing dependency, the app fails immediately at startup instead of failing later when a real user hits that feature. This gives fast failure, early error detection, and predictable startup validation.

---

## 33–34. Lazy Initialization

Bean is **not** created at startup — only when first requested.

```java
@Component
@Lazy
public class ReportService {
    public ReportService() { System.out.println("ReportService created"); }
    public void generateReport() { System.out.println("Report generated"); }
}
```

```java
ReportService reportService = context.getBean(ReportService.class);
reportService.generateReport();
```
```
Application started
ReportService object created
Report generated
```

**Exception:** if an eager singleton depends on a lazy bean, the lazy bean may still get created at startup — because Spring must satisfy the eager bean's dependency.

---

## 35–36. @Lazy on the Injection Point (Proxy Injection)

Instead of marking the whole bean lazy, mark just the **injection point**:

```java
@Component
public class UserService {
    private final EmailService emailService;
    public UserService(@Lazy EmailService emailService) {
        this.emailService = emailService;
        System.out.println("UserService created");
    }
    public void registerUser() {
        System.out.println("User registered");
        emailService.sendEmail();
    }
}
```

Spring injects a **proxy** (placeholder) instead of the real object immediately. The real `EmailService` is only created/fetched when a method is actually called on it.

```
UserService created
Application started
...later, on registerUser():
User registered
EmailService created
Email sent
```

| Usage | Meaning |
|---|---|
| `@Lazy` on the class | Don't create this bean until requested |
| `@Lazy` on the injection point | Inject a proxy; resolve the real bean only when used |

---

## 37–39. Global Lazy Initialization (Spring Boot)

```properties
spring.main.lazy-initialization=true
```
```yaml
spring:
  main:
    lazy-initialization: true
```

Makes **all** beans lazy — reduces startup time, but config errors may surface later, only when the bean is actually used.

Default: `spring.main.lazy-initialization=false`.

**Opt a specific bean out of global laziness:**

```java
@Component
@Lazy(false)
public class ImportantStartupBean { }
```

---

## 40–42. Common Lazy/Eager Combinations

- **Eager bean → Lazy dependency:** the lazy dependency may still get created at startup to satisfy the eager bean, unless `@Lazy` is used on the injection point (proxy instead).
- **Lazy bean → Eager dependency:** the eager dependency is already created at startup; when the lazy bean is finally created, it just gets that existing instance injected.
- **Lazy bean → Lazy dependency:** neither is created at startup. When the first lazy bean is requested, Spring creates it, and may also create its lazy dependency at that time — unless `@Lazy` is on the injection point, in which case a proxy is injected and the real dependency is delayed further.

---

## 43–44. Using @Lazy to Break Constructor-Based Circular Dependency

```java
@Service
public class OrderService {
    private final PaymentService paymentService;
    public OrderService(@Lazy PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}

@Service
public class PaymentService {
    private final OrderService orderService;
    public PaymentService(OrderService orderService) {
        this.orderService = orderService;
    }
}
```

Spring injects a **proxy** for `PaymentService` instead of building the real object immediately — resolving the cycle at startup. The real object is resolved on first actual use.

**Warning:** this is a workaround, not good design. Use it only when:
- You clearly understand the dependency flow
- Refactoring isn't immediately possible
- You're dealing with legacy code
- You need a temporary fix

Long-term: fix the design, separate responsibilities, remove the circular dependency.

---

## 45–46. Bean Lifecycle

```
Bean Definition → Object Creation → Dependency Injection → Initialization → Ready to Use → Destruction
```

**1. Bean Definition** — Spring registers metadata (class, scope, dependencies, lazy/eager, lifecycle hooks) via `@Component`/`@Service`/`@Repository`/`@Controller`/`@Bean`/XML. The actual object doesn't necessarily exist yet.

**2. Object Creation** — Spring calls the constructor (conceptually `new PaymentService()`, though the container does this internally).

**3. Dependency Injection** — Spring wires required dependencies into the object.

**4. Initialization** — Custom setup logic runs: validating config, opening resources, loading data. Hooks: `@PostConstruct`, `InitializingBean`, custom init method.

**5. Ready to Use** — Bean is fully usable by the rest of the app.

**6. Destruction** — On container shutdown, singleton beans are destroyed; cleanup logic runs via `@PreDestroy`, `DisposableBean`, or a custom destroy method.

> Note: for prototype beans, Spring creates and wires the object but does **not** fully manage its destruction the way it does for singletons.

---

## 47. Circular Dependency — Summary

- Happens when 2+ beans depend on each other (A needs B, B needs A).
- Constructor injection usually **fails** on this (needs all args before creation).
- Setter/field injection **may** succeed (object creation and injection are separate steps).
- Still a **design smell** — refactor instead of relying on tricks.

## 48. Scope — Summary

| Scope | Meaning |
|---|---|
| singleton | One object per bean definition in the container |
| prototype | New object every time it's requested |
| request | One object per HTTP request |
| session | One object per user session |
| application | One object per web application/ServletContext |

> Singleton = one object per bean **definition**, not one object per class in the whole JVM.

## 49. Initialization — Summary

| Type | Meaning |
|---|---|
| Eager | Created at startup |
| Lazy | Created only when needed |

- Singleton → eager by default; Prototype → created on request.
- `@Lazy` delays bean creation; on an injection point it injects a proxy and resolves the real dependency later.
- `spring.main.lazy-initialization=true` makes everything lazy app-wide, at the cost of delayed error detection.

## 50. Key Takeaways

1. Spring builds beans based on dependency order.
2. Circular dependency = the dependency order loops back on itself.
3. Constructor-based circular dependency usually fails at startup.
4. Setter/field injection can sometimes let Spring resolve circular dependencies.
5. Spring Boot disables circular references by default (2.6+).
6. Circular dependency is a design smell — fix via refactoring.
7. Singleton = one object per bean definition in the container.
8. Prototype = new object every time it's requested from the container.
9. Web scopes (request/session/application) are for web apps.
10. Eager initialization catches errors early at startup.
11. Lazy initialization delays bean creation until needed.
12. `@Lazy` on an injection point injects a proxy, resolving the real bean later.
13. Bean lifecycle: definition → creation → injection → initialization → ready → destruction.
