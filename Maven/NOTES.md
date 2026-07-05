# 📦 Maven Deep Dive 

---

## 📑 Table of Contents

- [1. The Basic Journey of Java Code](#1-the-basic-journey-of-java-code)
- [2. What Happens in a Real Java Project?](#2-what-happens-in-a-real-java-project)
- [3. What is a JAR File?](#3-what-is-a-jar-file)
- [4. Why Do We Need a JAR File?](#4-why-do-we-need-a-jar-file)
- [5. Library vs Application](#5-library-vs-application)
- [6. Reason 2: To Use External Libraries](#6-reason-2-to-use-external-libraries)
- [7. Library JAR vs Spring Boot Executable JAR](#7-library-jar-vs-spring-boot-executable-jar)
- [8. External Libraries and Classpath](#8-external-libraries-and-classpath)
- [9. What is Classpath?](#9-what-is-classpath)
- [10. Where Maven Comes Into the Picture](#10-where-maven-comes-into-the-picture)
- [11. What is Maven?](#11-what-is-maven)
- [12. What Maven Does Practically](#12-what-maven-does-practically)
- [13. Maven and IDEs](#13-maven-and-ides)
- [14. Convention Over Configuration](#14-convention-over-configuration)
- [15. Standard Maven Project Structure](#15-standard-maven-project-structure)
- [16. pom.xml — The Most Important File](#16-the-most-important-file-in-maven-pomxml)
- [17. Basic Structure of pom.xml](#17-basic-structure-of-pomxml)
- [18. modelVersion](#18-modelversion)
- [19. Maven Coordinates](#19-maven-coordinates)
- [20. What is SNAPSHOT in Maven?](#20-what-is-snapshot-in-maven)
- [21. packaging Tag](#21-packaging-tag)
- [22. properties Tag](#22-properties-tag)
- [23. dependencies Tag](#23-dependencies-tag)
- [24. Dependency Example: Hibernate](#24-dependency-example-hibernate)
- [25. Dependency Example: Spring Boot Starter](#25-dependency-example-spring-boot-starter)
- [26. Transitive Dependencies](#26-transitive-dependencies)
- [27. Maven Plugins](#27-maven-plugins)
- [28. Where Does Maven Download JAR Files From?](#28-where-does-maven-download-jar-files-from)
- [29. Types of Maven Repositories](#29-types-of-maven-repositories)
- [30. Local Repository](#30-local-repository)
- [31. What Happens During mvn install?](#31-what-happens-during-mvn-install)
- [32. Maven Central Repository](#32-maven-central-repository)
- [33. Remote Repository](#33-remote-repository)
- [34. Why Companies Use Private Repositories](#34-why-companies-use-private-repositories)
- [35. How Maven Searches for a Dependency](#35-how-maven-searches-for-a-dependency)
- [36. What is settings.xml?](#36-what-is-settingsxml)
- [37. Why Deleting .m2/repository Sometimes Fixes Errors](#37-why-deleting-m2repository-sometimes-fixes-dependency-errors)
- [38. Maven Lifecycle](#38-maven-lifecycle)
- [39. The Most Important Rule of Maven Lifecycle](#39-the-most-important-rule-of-maven-lifecycle)
- [40. The Three Main Maven Lifecycles](#40-the-three-main-maven-lifecycles)
- [41. Clean Lifecycle](#41-clean-lifecycle)
- [42. Default Lifecycle](#42-default-lifecycle)
- [43. Important Default Lifecycle Phases](#43-important-default-lifecycle-phases)
- [44–50. Lifecycle Phases Explained](#44-validate-phase)
- [51. Site Lifecycle](#51-site-lifecycle)
- [52. mvn clean install Explained](#52-what-happens-when-we-run-mvn-clean-install)
- [53. Maven Archetypes](#53-maven-archetypes)

---

## 1. The Basic Journey of Java Code

Before understanding Maven, we first need to understand the basic flow of a Java program:

```
Hello.java --(compiled by javac)--> Hello.class --(run by JVM)--> Output
```

At the core Java level:
- **`.java` file** → source code written by the developer
- **`.class` file** → compiled bytecode understood by the JVM

> Java code is not directly executed as `.java` files. First, it is compiled into `.class` files, and then the JVM runs those `.class` files.

---

## 2. What Happens in a Real Java Project?

A real project usually has many files, e.g.:

```
User.java
Order.java
Payment.java
EmailService.java
InvoiceGenerator.java
Main.java
```

After compilation, each becomes a `.class` file:

```
User.class
Order.class
Payment.class
EmailService.class
InvoiceGenerator.class
Main.class
```

Sharing many separate `.class` files is impractical because:
- Files can get misplaced
- Package/folder structure can break
- Some files may be missed
- Resource files may not be included
- Running the project becomes confusing

👉 Java needed a standard way to bundle compiled code and related files together — that bundle is called a **JAR file**.

---

## 3. What is a JAR File?

**JAR = Java Archive**

Think of it like a **ZIP file designed specifically for Java**.

A JAR file can contain:
- `.class` files
- folders/packages
- images
- properties files
- configuration files
- metadata

> A JAR file is a standard way to package Java compiled code and related resources into one file.

---

## 4. Why Do We Need a JAR File?

### Reason 1: To Share Java Code Easily

Instead of sending all compiled `.class` files separately, you package them into a single file:

```
calculator.jar
```

Another developer can simply add this JAR to their project and use the compiled code. This is how most Java libraries are shared.

---

## 5. Library vs Application

| Term | Meaning |
|------|---------|
| **Library** | Code created for other developers to use inside their projects. Usually does not run independently. |
| **Application** | Code created for the end user. Runs as a complete program, usually with a `main()` method or entry point. |

**Example:** `calculator.jar`
- If it only provides reusable calculator functions → **library JAR**
- If it contains a complete runnable program → **application JAR**

---

## 6. Reason 2: To Use External Libraries

When we use libraries like:
- Spring Core
- MySQL Connector
- Hibernate
- Jackson

...we are using someone else's JAR files.

> A library is often just compiled Java code packaged as a JAR file.

---

## 7. Library JAR vs Spring Boot Executable JAR

| Type | Description |
|------|-------------|
| **Normal library JAR** | Usually contains your compiled code, **not** all external dependency JARs inside it. Dependency info is managed via `pom.xml`. |
| **Spring Boot executable JAR** | Contains application code **+** required dependencies, making it runnable as one standalone JAR. |

This is why Spring Boot output is often called a **fat JAR** or **executable JAR**.

---

## 8. External Libraries and Classpath

```
Your Java code + External JAR file
            ↓
     Compiled together
            ↓
        Run by JVM
```

Java must know where to find required classes during compile time and runtime — this is handled via the **classpath**.

---

## 9. What is Classpath?

Java searches for classes in:
- Your own project classes
- External JAR files
- Other configured class locations

> Classpath tells Java **where to search** for required classes.

---

## 10. Where Maven Comes Into the Picture

For a single external JAR, manual download works fine. But real projects need many third-party libraries, raising questions like:
- Where to download JARs from?
- Which version to use?
- What if one JAR depends on another?
- What if teammates use different versions?
- What if a required JAR is missing?
- What if version 1 works but version 2 breaks?

**Maven's promise:**
> "Do not manually download and manage JAR files. Tell me what dependency your project needs, and I will download and manage the required JARs for you."

---

## 11. What is Maven?

**Maven** is a project management and build automation tool for Java projects — and it's **independent of Spring**.

You can use Maven with:
- Core Java projects
- Spring / Spring Boot projects
- Hibernate projects
- Many other Java-based projects

> Maven helps manage project structure, dependencies, builds, packaging, and configuration in a standard way.

---

## 12. What Maven Does Practically

1. Gives a standard project structure
2. Compiles Java code
3. Runs tests
4. Creates JAR/WAR files
5. Downloads external JAR libraries
6. Manages dependency versions
7. Uses plugins to perform build-related tasks

---

## 13. Maven and IDEs

Maven works via CLI:

```bash
mvn clean install
```

...and is supported directly by IDEs like **IntelliJ IDEA**, **Eclipse**, and **VS Code**, giving a consistent structure across all tools.

---

## 14. Convention Over Configuration

Maven follows **Convention over Configuration** — it assumes a standard project layout, so you don't need to configure everything manually.

Example:
- Main code → `src/main/java`
- Test code → `src/test/java`

---

## 15. Standard Maven Project Structure

```
my-maven-project/
│
├── pom.xml
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   └── resources/
│   │
│   └── test/
│       ├── java/
│       └── resources/
│
└── target/
```

| Folder/File | Purpose |
|-------------|---------|
| `pom.xml` | Project metadata, dependencies, plugins, build config |
| `src/main/java` | Main application source code |
| `src/main/resources` | Non-Java files (properties, YAML, templates, static files) |
| `src/test/java` | Unit/integration test code |
| `src/test/resources` | Test-only resources/config |
| `target/` | Auto-generated build output (`.class` files, JAR/WAR, reports) — safe to delete |

---

## 16. The Most Important File in Maven: pom.xml

**POM = Project Object Model**

`pom.xml` tells Maven:
- What the project is
- Which version it has
- What type of output to create
- Which external libraries are needed
- Which plugins to use
- Whether there's a parent configuration

---

## 17. Basic Structure of pom.xml

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xsi="http://www.w3.org/2001/XMLSchema-instance"
         schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.coderarmy</groupId>
    <artifactId>calculator-app</artifactId>
    <version>1.0.0</version>

</project>
```

---

## 18. modelVersion

```xml
<modelVersion>4.0.0</modelVersion>
```

Tells Maven which POM model version this file follows. Most projects use `4.0.0`.

---

## 19. Maven Coordinates

```xml
<groupId>com.coderarmy</groupId>
<artifactId>calculator-app</artifactId>
<version>1.0.0</version>
```

| Tag | Meaning |
|-----|---------|
| `groupId` | Organization/company/domain that owns the project |
| `artifactId` | Name of the project or module |
| `version` | Version being built |

Together these are called **Maven coordinates**, used to uniquely identify a project or dependency.

---

## 20. What is SNAPSHOT in Maven?

```xml
<version>1.0.0-SNAPSHOT</version>
```

| Version | Meaning |
|---------|---------|
| `1.0.0` | Stable release version |
| `1.0.0-SNAPSHOT` | Development version (not final) |

---

## 21. `<packaging>` Tag

```xml
<packaging>jar</packaging>
```

| Packaging | Meaning |
|-----------|---------|
| `jar` | Creates a JAR file |
| `war` | Creates a WAR file (traditional web apps) |
| `pom` | Used for parent/aggregator projects |

> Default packaging (if not specified) is `jar`.

---

## 22. `<properties>` Tag

```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
</properties>
```

Defines reusable values (Java version, encoding, etc.), keeping the POM clean.

---

## 23. `<dependencies>` Tag

```xml
<dependencies>
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
        <version>8.x.x</version>
    </dependency>
</dependencies>
```

Each dependency is identified by `groupId + artifactId + version`. Maven downloads the required JAR from a repository.

---

## 24. Dependency Example: Hibernate

```xml
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-core</artifactId>
    <version>6.x.x</version>
</dependency>
```

Maven downloads Hibernate Core **and** manages its supporting dependencies.

---

## 25. Dependency Example: Spring Boot Starter

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

A **starter** brings a whole group of related dependencies needed for a feature (e.g., web development).

---

## 26. Transitive Dependencies

```
Your project needs A
A needs B
B needs C
```

Adding **A** automatically pulls in **B** and **C** too.

> Transitive dependencies = dependencies of your dependencies.

This is one of Maven's biggest advantages — no manual hunting for every required JAR.

---

## 27. Maven Plugins

| Concept | Used For |
|---------|----------|
| **Dependency** | Used **by** your application code |
| **Plugin** | Used **by Maven** during the build process |

Plugins can help with: compiling code, running tests, creating JARs, generating reports, running Spring Boot apps, and packaging.

---

## 28. Where Does Maven Download JAR Files From?

From **Maven repositories** — storage locations for artifacts such as JARs, WARs, POM files, and plugins.

---

## 29. Types of Maven Repositories

1. **Local Repository**
2. **Maven Central Repository**
3. **Remote Repository**

---

## 30. Local Repository

A folder on your own machine:

| OS | Default Location |
|----|-------------------|
| Mac/Linux | `~/.m2/repository` |
| Windows | `C:\Users\<username>\.m2\repository` |

**Jobs:**
1. Caches downloaded dependencies
2. Stores your own locally installed artifacts

> First build is slower (downloading); later builds are faster (cached).

---

## 31. What Happens During `mvn install`?

```bash
mvn install
```

Builds the project and stores the final artifact in the local repo:

```
~/.m2/repository/com/example/MavenDemo/1.0/
```

This lets other **local** Maven projects use that artifact.

---

## 32. Maven Central Repository

A huge public repository hosting libraries like:
- Spring
- Hibernate
- Jackson
- JUnit
- MySQL Connector

If a dependency isn't cached locally, Maven typically fetches it from **Maven Central**.

---

## 33. Remote Repository

Any Maven repository **not** on your local machine — accessible via internet or company network.

**Examples:** Maven Central, company Nexus, company Artifactory, GitHub Packages.

---

## 34. Why Companies Use Private Repositories

For internal-only libraries, companies use:
- **Nexus**
- **Artifactory**
- **GitHub Packages**

Example configuration:

```xml
<repositories>
    <repository>
        <id>company-repo</id>
        <url>https://repo.company.com/maven2</url>
    </repository>
</repositories>
```

---

## 35. How Maven Searches for a Dependency

```xml
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.17.0</version>
</dependency>
```

**Resolution flow:**

1. Read `pom.xml`
2. Identify required dependency
3. Check local repository first
4. If found → use it
5. If not found → check remote repositories
6. Download JAR + POM
7. Store in local repository
8. Use in project build

---

## 36. What is settings.xml?

Maven's **user-level** configuration file, located at:

```
~/.m2/settings.xml
```

Can contain:
- Repository credentials
- Proxy settings
- Mirror configuration
- Custom local repository location

| File | Scope |
|------|-------|
| `pom.xml` | Project-level configuration |
| `settings.xml` | User/machine-level configuration |

---

## 37. Why Deleting `.m2/repository` Sometimes Fixes Dependency Errors

Common causes of corrupted local artifacts:
- Download failed midway
- Internet disconnected during download
- Partial JAR saved
- Inconsistent metadata

**Fix:** Delete the problematic dependency folder (or entire `.m2/repository`) to force Maven to re-download it.

> ⚠️ Deleting the entire `.m2/repository` means **all** dependencies must be downloaded again.

---

## 38. Maven Lifecycle

```
Check project → Compile code → Run tests → Create JAR → Store/Deploy artifact
```

> A Maven lifecycle is a standard sequence of steps Maven follows to build and manage a project.

---

## 39. The Most Important Rule of Maven Lifecycle

> When you run a Maven phase, Maven automatically runs **all earlier phases** of that lifecycle first.

**Example:**

```bash
mvn package
# runs: validate → compile → test → package

mvn install
# runs: validate → compile → test → package → verify → install
```

---

## 40. The Three Main Maven Lifecycles

| Lifecycle | Purpose |
|-----------|---------|
| **Clean** | Cleans old build output |
| **Default** | Builds, tests, packages, installs, and deploys the project |
| **Site** | Generates project documentation and reports |

---

## 41. Clean Lifecycle

```bash
mvn clean
```

Removes old build output — usually deletes the `target` folder for a fresh build.

---

## 42. Default Lifecycle

The main build lifecycle:

```
validate → compile → test → package → verify → install → deploy
```

---

## 43. Important Default Lifecycle Phases

| Phase | Meaning |
|-------|---------|
| `validate` | Checks whether the project is valid |
| `compile` | Compiles `.java` files into `.class` files |
| `test` | Runs test cases |
| `package` | Creates JAR/WAR file |
| `verify` | Performs additional checks on packaged output |
| `install` | Stores artifact in local `.m2` repository |
| `deploy` | Uploads artifact to a remote Maven repository |

---

### 44. `validate` Phase

```bash
mvn validate
```

Checks:
- Is `pom.xml` present and readable?
- Are required project details available?
- Is the project structure valid enough to continue?

---

### 45. `compile` Phase

```bash
mvn compile
```

Flow:

```
src/main/java → target/classes
```

Compiles Java source files into `.class` files.

---

### 46. `test` Phase

```bash
mvn test
```

Compiles and runs tests from `src/test/java` to verify the code works before packaging.

---

### 47. `package` Phase

```bash
mvn package
```

Creates the final packaged output, e.g.:

```
target/MavenDemo-1.0.jar
```

(Or a WAR file for web applications.)

---

### 48. `verify` Phase

```bash
mvn verify
```

In larger projects, may include:
- Integration tests
- Code quality checks
- Coverage checks
- Extra validation

---

### 49. `install` Phase

```bash
mvn install
```

Builds the project and stores the final artifact at:

```
~/.m2/repository
```

Useful when another **local** project needs this as a dependency.

---

### 50. `deploy` Phase

```bash
mvn deploy
```

Uploads the final artifact to a **remote** Maven repository (Nexus, Artifactory, GitHub Packages, etc.).

> 📌 Note: This is different from pushing code to GitHub. GitHub stores *source code*; a Maven remote repository stores *built artifacts* (like JARs) for other projects to consume.

---

## 51. Site Lifecycle

```bash
mvn site
```

Generates project documentation and reports, output typically placed in:

```
target/site
```

---

## 52. What Happens When We Run `mvn clean install`?

```bash
mvn clean install
```

Runs **two lifecycles together**:

```
clean  →  deletes old build output (target folder)
install → validate → compile → test → package → verify → install
```

**In short:**
> Clean old output + Build the project again + Run tests + Package the project + Install the artifact into the local `.m2` repository

---

## 53. Maven Archetypes

A **Maven archetype** is a project template — a ready-made structure for creating new projects quickly.

```
Template → project structure + basic configuration + boilerplate files
```

> Modern Spring Boot projects are usually created via **Spring Initializr**, but Maven archetypes remain a useful concept to understand.

---

## ⚡ Quick Command Cheat Sheet

| Command | What it does |
|---------|---------------|
| `mvn validate` | Validate the project structure |
| `mvn compile` | Compile source code |
| `mvn test` | Run tests |
| `mvn package` | Build JAR/WAR |
| `mvn verify` | Run additional checks on the package |
| `mvn install` | Install artifact to local `.m2` repo |
| `mvn deploy` | Push artifact to remote repo |
| `mvn clean` | Delete `target/` folder |
| `mvn clean install` | Clean + full build + install |
| `mvn clean install -DskipTests` | Same as above, but skip running tests |
| `mvn dependency:tree` | View the full dependency tree (great for spotting version conflicts) |
| `mvn -v` | Check installed Maven & Java version |

---

## 🛠️ Common Errors & Quick Fixes

| Problem | Likely Fix |
|---------|------------|
| `Could not resolve dependencies` | Check internet connection, or delete the corrupted folder inside `.m2/repository` and rebuild |
| `Cannot find symbol` after adding a dependency | Reload/re-import the Maven project in your IDE |
| Two dependencies pulling different versions of the same library | Run `mvn dependency:tree` to find the conflict, then explicitly declare the version you want |
| Build works on your machine but not a teammate's | Compare `pom.xml` versions and check if `settings.xml` differs (mirrors/proxies) |
| Old code still runs after changes | Run `mvn clean` first — stale `.class` files in `target/` can cause this |

---

## ✅ Good Practices Worth Remembering

- Pin exact dependency versions in production projects — avoid version ranges like `[1.0,)`, they can silently break builds.
- Use `<dependencyManagement>` in a parent POM for multi-module projects to keep versions consistent across modules.
- Run `mvn dependency:tree` before adding a new library to check if it conflicts with something you already have.
- Don't commit the `target/` folder to Git — add it to `.gitignore`, since Maven regenerates it every build.
- Prefer the Maven wrapper (`./mvnw`) in shared/team projects so everyone builds with the same Maven version without installing it globally.

---

