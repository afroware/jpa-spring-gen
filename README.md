# Jpa Spring Generator [![Build Status](https://travis-ci.org/afroware/jpa-spring-generator.svg?branch=master)](https://travis-ci.org/afroware/jpa-spring-generator) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.afroware/jpa-spring-generator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.afroware/jpa-spring-generator)

Spring Data Generator for JPA repositories and managers.

## Features ##
* Generate in Runtime
* Generate by Plugin
* Generate repositories for JPA Entities
* Generate managers for JPA Entities
* EntityScan wrapped annotation
* Overwrite option

## Dependencies ##

* **Java 8**
* **Spring data JPA**

## Generate in Runtime ##
Download the jar through Maven:

```xml
<dependency>
  <groupId>com.afroware</groupId>
  <artifactId>jpa-spring-generator</artifactId>
  <version>1.1.3</version>
</dependency>
```

The simple Spring Data JPA configuration with Java-Config looks like this: 
```java
@SDGenerator(
        entityPackage = "com.example.model",
        repositoryPackage = "com.example.repositories",
        repositorySuperClass = "CrudRepository",
        managerPackage = "com.example.managers",
        repositoryPostfix = "Repository",
        managerPostfix = "Manager",
        onlyAnnotations = false,
        debug = false,
        overwrite = false
)
@SpringBootApplication
public class AppConfig {
    
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
    
}
```

| Attribute | Required | Default | Description |
|----------|:-------------:|:------:|------------|
| entityPackage |  No | [ ] | Entity scan package |
| repositoryPackage | No | "" | Package where the repositories will be generated |
| managerPackage | No | "" | Package where the managers will be generated | 
| repositorySuperClass | No | "JpaRepository"| Super Class Repository To Extends From .
| repositoryPostfix | No | "Repository" | Postfix for repositories. example: Account**Repository** |
| managerPostfix | No | "Manager" | Postfix for managers. example: Account**Manager** |
| onlyAnnotations | No | false | Scan only classes annotated with @SDGenerate or @SDNoGenerate |
| debug | No | false | Enable debug log |
| overwrite | No | false | Overwrite existing files |

## Generate by Plugin ##
Download the jar through Maven:
```xml
<build>
	<plugins>
		<plugin>
			<groupId>com.afroware</groupId>
			<artifactId>jpa-spring-generator</artifactId>
			<version>1.1.3</version>
			<configuration>
				<entity-package>
				    <param>com.example.model</param>
				</entity-package>
				<repository-package>com.example.repository</repository-package>
				<repository-postfix>Repository</repository-postfix>
				<manager-package>com.example.managers</manager-package>
				<manager-postfix>Manager</manager-postfix>
				<only-annotations>false</only-annotations>
				<overwrite>false</overwrite>
			</configuration>
		</plugin>
	</plugins>
</build>
```

| Attribute | Required | Default | Description |
|----------|:-------------:|:------:|------------|
| entity-package |  Yes | [ ] | Entity scan package |
| repository-package | Yes | "" | Package where the repositories will be generated |
| repository-superclass | No | "JpaRepository"| Super Class Repository To Extends From .
| manager-package | Yes | "" | Package where the managers will be generated | 
| repository-postfix | No | "Repository" | Postfix for repositories. example: Account**Repository** |
| manager-postfix | No | "Manager" | Postfix for managers. example: Account**Manager** |
| onlyAnnotations | No | false | Scan only classes annotated with @SDGenerate or @SDNoGenerate |
| overwrite | No | false | Overwrite existing files |

#### Generate repositories (terminal)
```
$ mvn jpa-spring-generator:repositories
```
#### Generate managers (terminal)
```
$ mvn jpa-spring-generator:managers
```

## Example ##

Sample entity in `com.example.model`

```java
@Entity
//@SDGenerate   ->  Optional: Include to classes scan
//@SDNoGenerate   ->  Optional: Exclude to classes scan
public class Account {

    @Id
    @GeneratedValue
    private Integer id;
    private String firstname;
    private String lastname;
       
    // Getters and setters
    // (Firstname, Lastname)-constructor and noargs-constructor
    // equals / hashcode
}
```

Generate a repository interface example in `com.example.repositories`:

```java
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
}
```

Generate a manager class example in `com.example.managers`:

```java
@Component
public class AccountManager {
    
    @Autowired
    public AccountManager(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
   
    private AccountRepository accountRepository;
}
```

## Notes ##

* The overwrite option delete the existing file to regenerate

License
----

MIT
