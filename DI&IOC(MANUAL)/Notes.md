# Spring IoC Container, Beans & Annotation-Based Configuration

## 1. Core Idea: Classes Should Not Create Their Own Dependencies

A class should focus only on its own job. For example, `OrderService` should focus on placing an order — it shouldn't be responsible for creating a `PaymentService` object.

**Tightly coupled (bad) design:**
```java
public class OrderService {
    private PaymentService paymentService = new PaymentService();

    public void placeOrder() {
        paymentService.pay();
        System.out.println("Order placed");
    }
}
```
Problem: `OrderService` is tightly coupled to `PaymentService`. Swapping the implementation later means editing `OrderService` itself.

**Better design (dependency passed from outside):**
```java
public class OrderService {
    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void placeOrder() {
        paymentService.pay();
        System.out.println("Order placed");
    }
}
```
Now `OrderService` receives its dependency instead of creating it — this is **Dependency Injection (DI)**.

---

## 2. First-Principles Understanding

Every application needs objects, and those objects often need other objects. Three key questions:
- Who creates these objects?
- Who connects them together?
- Who manages their lifecycle?

**Without Spring:** `main()` creates objects, wires them, and effectively acts as a manual container.

**With Spring:** these responsibilities move to the **Spring IoC container**, which creates objects, connects them, and manages their lifecycle.

This is **IoC — Inversion of Control**. Instead of our code controlling object creation and wiring, Spring controls it.

---

## 3. Basic Project Setup

To use Spring Core with annotation-based configuration:
1. Create a Maven project.
2. Add the `spring-context` dependency.

`spring-context` provides:
- `ApplicationContext`
- Annotation-based configuration
- Component scanning
- Bean creation and dependency injection

---

## 4. What Is a Spring Bean?

A **Spring Bean** is an object managed by the Spring IoC container. Spring is responsible for:
- Creating the object
- Wiring its dependencies
- Managing its lifecycle
- Handing it back when requested

> **Definition:** A Spring Bean is an object whose creation, dependency wiring, and lifecycle are managed by the Spring IoC container.

---

## 5. How Spring Manages Objects: Two Configuration Styles

1. **Annotation-based configuration** — modern, commonly used. Key annotations: `@Component`, `@Configuration`, `@ComponentScan`, `@Autowired`, `@Bean`.
2. **XML-based configuration** — older style, mostly seen in legacy projects, not preferred for new development.

---

## 6. Reflection: Why `Student.class` Matters

`Student.class` does **not** create a `Student` object — it refers to a `Class` object containing metadata about `Student`:
- Class name
- Fields
- Methods
- Constructors
- Annotations present on the class

```java
Class<Student> c = Student.class;
```

Spring relies on this kind of metadata internally. When we write:
```java
new AnnotationConfigApplicationContext(AppConfig.class);
```
we're giving Spring metadata about `AppConfig` so it can read configuration instructions from it.

---

## 7. Telling Spring Which Classes to Manage

Spring doesn't manage every class automatically — we mark eligible classes using `@Component`:

```java
@Component
public class PaymentService {
    public void pay() {
        System.out.println("Payment done");
    }
}
```

`@Component` just marks eligibility. Spring also needs to know **where** to look — that's the job of `@ComponentScan`.

---

## 8. `ApplicationContext`: The Spring IoC Container

```java
ApplicationContext context =
    new AnnotationConfigApplicationContext(AppConfig.class);
```

`ApplicationContext` represents the Spring IoC container. It:
- Reads configuration
- Creates beans
- Resolves dependencies
- Manages bean lifecycle
- Provides beans on request

Note: `ApplicationContext` is an **interface**, so `new ApplicationContext()` is not valid — we must use an implementation such as `AnnotationConfigApplicationContext`.

---

## 9. What Is `AnnotationConfigApplicationContext`?

An implementation of `ApplicationContext` that starts a Spring container using Java annotation-based configuration.

```java
ApplicationContext context =
    new AnnotationConfigApplicationContext(AppConfig.class);
```

This means: start the container, read `AppConfig`, use annotation-based configuration, create and manage beans accordingly.

---

## 10. What Is `AppConfig.class`?

A configuration class, e.g.:

```java
@Configuration
@ComponentScan("in.example")
public class AppConfig {
}
```

This tells Spring: this is a configuration class, scan package `in.example`, find `@Component`-annotated classes, create their beans, and wire dependencies.

---

## 11. What Is `@Configuration`?

Marks a class as containing Spring configuration instructions.

```java
@Configuration
public class AppConfig {
}
```

Such a class can contain `@ComponentScan`, `@Bean` methods, and other configuration instructions.

> **Definition:** `@Configuration` marks a class as a source of bean definitions.

---

## 12. What Is `@ComponentScan`?

Tells Spring where to search for annotated classes.

```java
@Configuration
@ComponentScan("com.example")
public class AppConfig {
}
```

This means: start scanning from `com.example` (and sub-packages), find classes marked `@Component`, `@Service`, `@Repository`, `@Controller`, etc., and register them as beans.

**`@Component` vs `@ComponentScan`:**

| Annotation | Meaning |
|---|---|
| `@Component` | Marks a class as eligible to become a Spring bean |
| `@ComponentScan` | Tells Spring where to search for such classes |

**Without a package name?**
```java
@Configuration
@ComponentScan
public class AppConfig {
}
```
Spring then scans the package where `AppConfig` itself lives, plus its sub-packages.

---

## 13. What Does `getBean()` Mean?

```java
OrderService orderService = context.getBean(OrderService.class);
```

This asks the container: "give me the `OrderService` bean." If Spring already created it, it's returned; otherwise an error occurs.

**Common failure reasons:**
- Class isn't annotated with `@Component`
- Its package isn't covered by `@ComponentScan`
- It isn't registered via `@Bean` or XML

**Example:**
```java
public class Main {
    public static void main(String[] args) {
        ApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class);
        OrderService orderService = context.getBean(OrderService.class);
        orderService.placeOrder();
    }
}
```

---

## 14. Types of Dependency Injection in Spring

1. Constructor injection
2. Field injection
3. Setter injection

---

## 15. Constructor Injection

Dependencies are supplied through the constructor.

```java
@Component
public class OrderService {
    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void placeOrder() {
        paymentService.pay();
        System.out.println("Order placed");
    }
}
```

If a bean has only **one** constructor, `@Autowired` on it is optional — Spring uses it automatically. Writing `@Autowired` explicitly is still valid:

```java
@Component
public class OrderService {
    private final PaymentService paymentService;

    @Autowired
    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

---

## 16. Why Constructor Injection Is Preferred

A constructor runs at object creation time, so it's the natural place to supply required dependencies — the object is fully usable the moment it's created.

**Benefit 1 — Dependency is mandatory:** the object literally cannot be constructed without it.

**Benefit 2 — Allows `final` fields:** `private final PaymentService paymentService;` prevents accidental reassignment, making the class safer.

**Benefit 3 — Easy to test without Spring:**
```java
PaymentService paymentService = new PaymentService();
OrderService orderService = new OrderService(paymentService);
orderService.placeOrder();
```
No container needed just to construct and test the object. The class stays clean and loosely coupled from Spring itself.

---

## 17. Field Injection

```java
@Component
public class OrderService {
    @Autowired
    private PaymentService paymentService;

    public void placeOrder() {
        paymentService.pay();
        System.out.println("Order placed");
    }
}
```

Works via reflection, but generally **not preferred**:
- Dependency is hidden (not visible in constructor signature)
- Hard to test without Spring
- Field can't be `final`
- Object can briefly exist in an incomplete state before injection happens

---

## 18. Setter Injection

```java
@Component
public class OrderService {
    private PaymentService paymentService;

    @Autowired
    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void placeOrder() {
        paymentService.pay();
        System.out.println("Order placed");
    }
}
```

Flow: Spring creates `OrderService` with a no-arg constructor, then calls `setPaymentService()`.

Useful for **optional** or **changeable** dependencies. For mandatory dependencies, constructor injection is preferred.

---

## 19. What Happens Internally When Spring Starts?

```java
new AnnotationConfigApplicationContext(AppConfig.class);
```

**Step 1 — Container starts:** Spring creates an `ApplicationContext`.

**Step 2 — Reads `AppConfig.class`:** identifies it as a configuration class and reads its annotations.

**Step 3 — Processes `@ComponentScan`:** e.g. `@ComponentScan("com.example")` tells Spring to search `com.example` and sub-packages.

**Step 4 — Finds component classes:** any class marked `@Component`, `@Service`, `@Repository`, `@Controller`.

**Step 5 — Creates Bean Definitions:** before making real objects, Spring stores metadata about each bean — a **`BeanDefinition`** — including:
- Bean name
- Bean class
- Scope (e.g. singleton)
- Dependencies
- Creation strategy

A `BeanDefinition` is *not* the object itself — it's a blueprint describing how to create and manage it.

```
BeanDefinition list
│
├── paymentService
└── orderService
```

---

## 20. Why Bean Definitions Come First

Spring manages a whole application, not a single object, so before creating anything it needs to know: which beans exist, their classes, scopes, dependencies, creation strategy, and lifecycle methods. Hence bean definitions are built first, then actual objects.

**Step 6 — Creates bean objects:** beans with no dependencies (like `PaymentService`) are created first.

**Step 7 — Creates `OrderService`:** Spring inspects the constructor, sees it needs a `PaymentService`, and checks whether such a bean already exists in the container (**dependency resolution**). If found, it's passed into the constructor.

**Step 8 — Injects dependencies:** for constructor injection this happens at object-creation time — the object is ready to use immediately after construction.

**Step 9 — Application uses the bean:**
```java
orderService.placeOrder();
```
Output:
```
Payment done
Order placed
```

---

## 21. What If No Matching Bean Exists?

Error such as:
```
No qualifying bean of type 'PaymentService' available
```

**Common causes:**
- Missing `@Component`
- Package not covered by `@ComponentScan`
- No `@Bean` method registering it
- Dependency simply never registered

---

## 22. What If Multiple Matching Beans Exist?

```java
public interface PaymentService {
    void pay();
}

@Component
public class UPIPaymentService implements PaymentService {
    public void pay() { System.out.println("UPI payment done"); }
}

@Component
public class CardPaymentService implements PaymentService {
    public void pay() { System.out.println("Card payment done"); }
}
```

If `OrderService` depends on the `PaymentService` interface, Spring now finds **two** matching beans — a **bean ambiguity problem**. Resolved using `@Primary` or `@Qualifier`.

---

## 23. Using `@Primary`

Marks one implementation as the default choice when multiple candidates exist.

```java
@Primary
@Component
public class UPIPaymentService implements PaymentService {
    public void pay() { System.out.println("UPI payment done"); }
}
```

---

## 24. Using `@Qualifier`

Explicitly picks a specific bean by name.

```java
@Component
public class OrderService {
    private final PaymentService paymentService;

    public OrderService(@Qualifier("cardPaymentService") PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

**Default bean naming:** Spring derives the bean name from the class name with the first letter lowercased (e.g. `CardPaymentService` → `cardPaymentService`). Acronym-starting class names can behave inconsistently, so an explicit custom name is often clearer.

---

## 25. Custom Bean Names with `@Component`

```java
@Component("upi")
public class UPIPaymentService implements PaymentService { }

@Component("card")
public class CardPaymentService implements PaymentService { }
```

```java
public OrderService(@Qualifier("card") PaymentService paymentService) {
    this.paymentService = paymentService;
}
```

---

## 26. `@Qualifier` with Field Injection

```java
@Component
public class OrderService {
    @Autowired
    @Qualifier("upiPaymentService")
    private PaymentService paymentService;

    public void placeOrder() {
        paymentService.pay();
        System.out.println("Order placed");
    }
}
```

---

## 27. `@Qualifier` with Setter Injection

```java
@Component
public class OrderService {
    private PaymentService paymentService;

    @Autowired
    public void setPaymentService(@Qualifier("cardPaymentService") PaymentService paymentService) {
        this.paymentService = paymentService;
    }
}
```

---

## 28. `@Primary` vs `@Qualifier` Together

If both are present, **`@Qualifier` wins** — a specific choice always overrides a default choice.

---

## 29. Why Do We Need `@Bean`?

`@Component` only works on classes we own. For a class from an external library, we can't add `@Component` to it directly:

```java
public class EmailClient {
    private final String apiKey;

    public EmailClient(String apiKey) {
        this.apiKey = apiKey;
    }

    public void sendEmail() {
        System.out.println("Email sent using API key: " + apiKey);
    }
}
```

We still want Spring to manage it — that's what `@Bean` is for.

---

## 30. Another Case: Custom Object Creation

```java
public class User {
    private String name;
    private String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
```

Spring can't guess what values to pass for `name`/`email` — `@Bean` lets us specify exactly how to build it.

---

## 31. What Exactly Does `@Bean` Mean?

Used on a method inside a `@Configuration` class:

```java
@Configuration
public class AppConfig {
    @Bean
    public PaymentService paymentService() {
        return new PaymentService();
    }
}
```

Meaning: call this method, take the returned object, and register it as a Spring-managed bean. For singleton beans, Spring typically creates and stores the object at context startup.

---

## 32. Custom Bean Name with `@Bean`

Default bean name = method name.

```java
@Bean
public PaymentService paymentService() {
    return new PaymentService();
}
// bean name: paymentService
```

Custom name:
```java
@Bean("myPaymentService")
public PaymentService paymentService() {
    return new PaymentService();
}
```
```java
PaymentService paymentService =
    (PaymentService) context.getBean("myPaymentService");
```

---

## 33. `@Bean` with Dependencies

```java
public class OrderService {
    private final PaymentService paymentService;

    public OrderService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public void placeOrder() {
        paymentService.pay();
        System.out.println("Order placed");
    }
}
```

No `@Component` here — both beans created via `@Bean`:

```java
@Configuration
public class AppConfig {
    @Bean
    public PaymentService paymentService() {
        return new PaymentService();
    }

    @Bean
    public OrderService orderService(PaymentService paymentService) {
        return new OrderService(paymentService);
    }
}
```

Spring creates `PaymentService` first, then passes it into `orderService()`. Using method parameters (as above) is cleaner than calling `paymentService()` directly inside `orderService()`, though in a proper `@Configuration` class Spring does ensure singleton behavior either way.

---

## 34. `@Component` vs `@Bean`

| Point | `@Component` | `@Bean` |
|---|---|---|
| Where used | On a class | On a method |
| Style | Automatic detection | Manual registration |
| Best for | Our own classes | External library classes / custom object creation |
| Needs component scanning? | Yes | No, but the configuration class must be loaded |
| Default bean name | Class name based | Method name based |

**In short:** `@Component` = Spring finds the class automatically. `@Bean` = we manually tell Spring how to build the object.

---

## 35. Avoid Registering the Same Bean Twice

Don't register the same object via both `@Component` and `@Bean` unless multiple beans are genuinely intended.

```java
@Component
public class PaymentService { }

@Bean
public PaymentService paymentService() {
    return new PaymentService();
}
```

**Possible outcomes:**
- Treated as two different beans if names differ
- Type-based injection can become ambiguous
- If names match, bean-overriding rules may kick in depending on configuration

**Rule of thumb:** use either `@Component` or `@Bean` for a given bean, not both, unless there's a clear reason.

---

## 36. Dependency Resolution with `@Bean`

Same `@Primary` / `@Qualifier` rules apply:

```java
@Bean
@Primary
public PaymentService upiPaymentService() {
    return new UPIPaymentService();
}
```

```java
@Bean
public OrderService orderService(
        @Qualifier("cardPaymentService") PaymentService paymentService) {
    return new OrderService(paymentService);
}
```

---

## 37. Why Not Put Everything in `main()`?

`main()` should only **start** the application. If it also handles object creation and wiring, it becomes a manual container all over again — defeating the purpose of using Spring.

```java
public class Main {
    public static void main(String[] args) {
        ApplicationContext context =
            new AnnotationConfigApplicationContext(AppConfig.class);
        OrderService orderService = context.getBean(OrderService.class);
        orderService.placeOrder();
    }
}
```

Object creation, wiring, and lifecycle are all delegated to Spring.

---

## 38. `BeanFactory` vs `ApplicationContext`

- **`BeanFactory`** — the basic container interface.
- **`ApplicationContext`** — builds on `BeanFactory`, provides richer features, and is what's commonly used in real applications.

`ApplicationContext` additionally provides:
- Bean creation and dependency injection
- Lifecycle management
- Event publishing
- Internationalization support
- Integration with other Spring features

---

## 39. Final Flow Summary

```java
ApplicationContext context =
    new AnnotationConfigApplicationContext(AppConfig.class);
```

1. Start the IoC container
2. Read `AppConfig.class`
3. Process `@Configuration`
4. Process `@ComponentScan`
5. Scan the given package and sub-packages
6. Find classes marked `@Component` and related annotations
7. Create `BeanDefinition`s
8. Resolve dependencies
9. Create bean objects
10. Inject dependencies
11. Store beans inside the container
12. Return beans when `getBean()` is called

---

## 40. Key Takeaways

- A **Spring Bean** is an object managed by the Spring IoC container.
- **`ApplicationContext`** represents the Spring IoC container in normal applications.
- **`AnnotationConfigApplicationContext`** starts a container using annotation-based configuration.
- **`@Configuration`** marks a class as a source of Spring configuration.
- **`@ComponentScan`** tells Spring where to search for components.
- **`@Component`** marks a class as eligible to become a bean.
- **`getBean()`** asks Spring for an object from the container.
- **Constructor injection** is generally preferred for mandatory dependencies.
- **Field injection** works but isn't recommended for clean design.
- **Setter injection** suits optional/changeable dependencies.
- Spring first creates `BeanDefinition` metadata, then creates actual bean objects.
- No matching bean → error. Multiple matching beans → resolve with `@Primary`/`@Qualifier`.
- **`@Bean`** manually tells Spring how to create an object (method-level); **`@Component`** is automatic (class-level).
- Avoid registering the same bean via both `@Component` and `@Bean` unless intentional.
- `main()` should start the application, not manage all object creation.
- `ApplicationContext` is preferred because it offers a complete set of container features.

---

## Quick Revision Table

| Annotation | Level | Purpose |
|---|---|---|
| `@Component` | Class | Marks a class as a candidate Spring bean |
| `@Configuration` | Class | Marks a class as a source of bean definitions |
| `@ComponentScan` | Class | Tells Spring which package(s) to scan |
| `@Bean` | Method | Manually registers the method's return value as a bean |
| `@Autowired` | Constructor/Field/Setter | Requests dependency injection |
| `@Primary` | Class/Method | Marks the default bean when multiple candidates exist |
| `@Qualifier` | Parameter/Field | Explicitly picks a specific bean by name |

## Common Interview-Style Questions (self-check)

1. What is IoC, and how does Spring implement it?
2. Difference between `BeanFactory` and `ApplicationContext`?
3. Why is constructor injection preferred over field injection?
4. What happens if two beans of the same type exist and neither `@Primary` nor `@Qualifier` is used?
5. What is a `BeanDefinition`, and why does Spring build it before creating objects?
6. When would you use `@Bean` instead of `@Component`?
7. What error do you get when Spring can't find a required bean, and what usually causes it?
