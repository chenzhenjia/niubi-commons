# niubi-commons
个人的一个公共项目,基于 SpringBoot,SpringSecurity,Jpa 开发的功能.
## core
### 在项目中引用
由于现在是 snapshots 的,所以先配置 maven 的地址为 `https://oss.sonatype.org/content/repositories/snapshots`
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
compile group: 'dev.niubi.commons', name: 'security', version: 'last-version'
```
### 使用
* permission
1. 实现 PermissionService 
在 loadByUsername 中查询数据库根据用户名获取当前用户的权限,建议做缓存处理
```java
@Component
public class CachePermissionService implements PermissionService {
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "permission", key = "#username")
    public Set<String> loadByUsername(String username) {
        // TODO query database load user permission
        return Collections.emptySet();
    }
}
```
2. 配置 SpringSecurity 的 GlobalMethodSecurity
```java
import dev.niubi.commons.security.permission.PermissionMethodSecurityConfiguration;

@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class GlobalMethodConfiguration extends PermissionMethodSecurityConfiguration {
    private PermissionService permissionService;

    @Resource
    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    protected PermissionVoter permissionVoter() {
        return new PermissionVoter(permissionService);
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