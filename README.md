# niubi-commons
个人的一个公共项目,基于 SpringBoot,SpringSecurity,Jpa 开发的功能
## core
* jpa converter
    1. ListStringToBlobConverter 把 list 字符串转为数据库的 Blob 类型的值
        ```java
         @Entity
         public class ExampleEntity {
             @Convert(converter = ListStringToBlobConverter.class)
             private List<String> type;
         }
        ``` 
    2. ListStringToTextConverter 把 list 字符串转为数据库的 varchar 类型的值
        ```java
         @Entity
         public class ExampleEntity {
             @Convert(converter = ListStringToTextConverter.class)
             private List<String> type;
         }
        ``` 
    3. PersistableEnumConverter 把需要自定义枚举值转为数据库的值,插入到数据库中.
        ```java
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
TODO
## web
TODO