# niubi-commons
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](LICENSE)

个人的一个开源项目项目,基于 SpringBoot,SpringSecurity,Jpa 开发的功能.
有 jpa 的 converter,还有 Security 的基于注解的接口权限拦截
## core
### 在项目中引用
由于现在是 snapshots 的,所以先配置 maven 的地址为 `https://oss.sonatype.org/content/repositories/snapshots`

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/dev.niubi.commons/core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/dev.niubi.commons/core)
* maven
```xml
<dependency>
  <groupId>dev.niubi.commons</groupId>
  <artifactId>core</artifactId>
  <version>last-version</version>
</dependency>
```
* gradle
```groovy
compile group: 'dev.niubi.commons', name: 'core', version: 'last-version'
```
### 使用
* jpa converter
    1. `ListStringToBlobConverter` 把 list 字符串转为数据库的 Blob 类型的值
        ```java
        import dev.niubi.commons.core.data.converter.ListStringToBlobConverter;
        @Entity
        public class ExampleEntity {
             @Convert(converter = ListStringToBlobConverter.class)
             private List<String> type;
        }
        ``` 
    2. `ListStringToTextConverter` 把 list 字符串转为数据库的 varchar 类型的值
        ```java
        import dev.niubi.commons.core.data.converter.ListStringToTextConverter;
        @Entity
        public class ExampleEntity {
            @Convert(converter = ListStringToTextConverter.class)
            private List<String> type;
        }
        ``` 
    3. `PersistableEnumConverter` 把需要自定义枚举值转为数据库的值,插入到数据库中.
        ```java
        import dev.niubi.commons.core.data.converter.PersistableEnumConverter;
        
        public enum Type implements PersistableEnum<Integer> {
          MALE(1),
          FEMALE(2);
          private final Integer value;
        
          MenuType(int value) {
              this.value = value;
          }
        
          public Integer getValue() {
              return value;
          }
        
          public static class Converter extends PersistableEnumConverter<Type, Integer> {
        
          }
        }
        
        @Entity
        public class ExampleEntity {
          @Convert(converter = Type.Converter.class)
          private Type type;
        }
        ```
## security
### 在项目中引用
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/dev.niubi.commons/security/badge.svg)](https://maven-badges.herokuapp.com/maven-central/dev.niubi.commons/security)
* maven
```xml
<dependency>
  <groupId>dev.niubi.commons</groupId>
  <artifactId>security</artifactId>
  <version>last-version</version>
</dependency>
```
* gradle
```groovy
implementation('dev.niubi.commons:security:last-version')
```
* kotlin dsl
```kotlin
implementation("dev.niubi.commons:security:last-version")
```
### 使用
* permissions
1. 配置 SpringSecurity 的 GlobalMethodSecurity
```java
import dev.niubi.commons.security.permissions.EnablePermissions;
import dev.niubi.commons.security.permissions.UsernamePermissionsVoter;

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
@EnablePermissions
public class GlobalMethodConfiguration{
    @Bean
    public UsernamePermissionsVoter usernamePermissionsVoter(){
        // 自定义根据用户名加载权限的加载器，建议缓存权限
        return new UsernamePermissionsVoter(username -> {
            return new PermissionsContextImpl(Collections.emptySet());
        });
    }

}
```
3. 使用
直接在 controller 类上或者 方法上
```java
@RestController
@RequestMapping("admin/user")
@Slf4j
@AllArgsConstructor
public class AdminUserCtrl {

    @PostMapping
    @Permission(
      tag = "添加管理员",
      value = {"admin:user:post"}
    )
    public Response<?> add(@RequestBody @Validated AdminUserIn addIn) {
        return Response.ok();
    }

    @DeleteMapping({"{id}"})
    @Permission(
      tag = "删除管理员",
      value = {"admin:user:delete:id"}
    )
    public Response<?> delete(@PathVariable("id") Long id) {
        return Response.ok();
    }

    @PutMapping({"{id}"})
    @Permission(
      tag = "修改管理员",
      value = {"admin:user:put:id"}
    )
    public Response<?> update(@PathVariable("id") Long id) {
        return Response.ok();
    }

    @GetMapping
    @Permission(
      tag = "获取管理员列表",
      value = {"admin:user:get"}
    )
    public Response<Page<?>> page(Pageable pageable) {
        return Response.ok();
    }

    @GetMapping({"{id}"})
    @Permission(
      tag = "获取管理员详情",
      value = {"admin:user:get:id"}
    )
    public Response<?> get(@PathVariable("id") Long id) {
        return Response.ok();
    }
}
```
如果不想用户访问需要权限的话,则不需要在方法或者类上加上权限.
这样就可以实现用户接口的权限管理
## web
TODO