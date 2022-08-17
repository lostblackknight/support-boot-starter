# 概述

对安全方面的功能增强，基于注解、切面、自定义Jackson的序列化实现。

- [概述](#概述)
  - [敏感信息处理](#敏感信息处理)
    - [快速入门](#快速入门)
    - [教程](#教程)
      - [脱敏的方式](#脱敏的方式)
      - [@Sensitive 注解](#sensitive-注解)
      - [类型脱敏](#类型脱敏)
      - [Tag 脱敏](#tag-脱敏)
      - [敏感信息处理器](#敏感信息处理器)
      - [ResponseEntity 类型的上游敏感信息处理器](#responseentity-类型的上游敏感信息处理器)
      - [ResponseEntity 类型的下游敏感信息处理器](#responseentity-类型的下游敏感信息处理器)
      - [注解脱敏 @Desensitized](#注解脱敏-desensitized)
      - [自定义默认的 @Desensitized 的脱敏规则。](#自定义默认的-desensitized-的脱敏规则)
      - [全局配置](#全局配置)
    - [注意事项和使用建议](#注意事项和使用建议)

## 敏感信息处理

### 快速入门

```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/sensitive")
public class SensitiveController {

    private final UserRepository userRepository;

    @GetMapping("/users/{id}")
    @Sensitive
    public User getUserById(@PathVariable Long id) {
        return userRepository.selectUserByUserId(id);
    }

    @GetMapping("/users")
    @Sensitive
    public List<User> getUsers() {
        return userRepository.selectUsers();
    }

    @GetMapping("/users/user1")
    @Sensitive
    public ResponseEntity<User> getUser1() {
        return ResponseEntity.ok(userRepository.selectUserByUserId(1L));
    }

    @GetMapping("/users/user2")
    @Sensitive("getUser2")
    public ResponseEntity<User> getUser2() {
        return ResponseEntity.ok(userRepository.selectUserByUserId(2L));
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class User {

        private Long userId;

        @Desensitized(DesensitizedType.CHINESE_NAME)
        private String username;

        @Desensitized(DesensitizedType.PASSWORD)
        private String password;

        @Desensitized(DesensitizedType.MOBILE_PHONE)
        private String phone;

        @Desensitized(DesensitizedType.ID_CARD)
        private String idCard;

        @Desensitized(DesensitizedType.ADDRESS)
        private String address;

        @Desensitized(DesensitizedType.BANK_CARD)
        private String bankCard;

        @Desensitized(DesensitizedType.FIXED_PHONE)
        private String fixedPhone;

        private String email;

        @Desensitized(DesensitizedType.CAR_LICENSE)
        private String carLicense;
    }

    @Repository
    public static class UserRepository {

        private final List<User> users = new ArrayList<>();

        @Bean
        public CommandLineRunner sensitiveRunner() {
            return args -> {
                users.add(new User(1L, "张三", "1234sf56", "17669854653",
                        "164653596666583325", "北京市海淀区马连洼街道289号", "11011111222233333256",
                        "09157518479", "duandazhi-jack@gmail.com.cn", "苏D40000"));
                users.add(new User(2L, "李四", "1256sf56", "18936569999",
                        "51343620000320711X", "北京市海淀区马连洼街道289号", "11011111222233333256",
                        "09157518479", "duandazhi-jack@gmail.com.cn", "苏D40000"));
                users.add(new User(3L, "王五", "12345622", "18765656993",
                        "589862326565989119", "北京市海淀区马连洼街道289号", "11011111222233333256",
                        "09157518479", "duandazhi-jack@gmail.com.cn", "苏D40000"));
                users.add(new User(4L, "关羽", "1234sf56", "15658989565",
                        "898956565656565116", "北京市海淀区马连洼街道289号", "11011111222233333256",
                        "09157518479", "duandazhi-jack@gmail.com.cn", "苏D40000"));
                users.add(new User(5L, "刘备", "1234pa56", "14848446565",
                        "898951465632323659", "北京市海淀区马连洼街道289号", "11011111222233333256",
                        "09157518479", "duandazhi-jack@gmail.com.cn", "苏D40000"));
            };
        }

        public User selectUserByUserId(Long id) {
            return users.stream().filter(user -> user.getUserId().equals(id)).findFirst().orElse(null);
        }

        public List<User> selectUsers() {
            return users;
        }
    }
}
```

```shell
# 发送请求
curl http://localhost:8080/sensitive/users/1

# 响应
{
  "userId": 1,
  "username": "张*",
  "password": "********",
  "phone": "176****4653",
  "idCard": "1***************25",
  "address": "北京市海淀区马********",
  "bankCard": "1101 **** **** **** 3256",
  "fixedPhone": "0915*****79",
  "email": "duandazhi-jack@gmail.com.cn",
  "carLicense": "苏D4***0"
}
```

### 教程

#### 脱敏的方式

提供了三种脱敏的方式，以应对不同的情况：

1. 基于返回值的类型，采用注解 + 切面实现。
2. 根据 `tag` 标记，采用注解 + 切面实现。
3. 注解，提供了 @Desensitized 注解，放在属性上，进行脱敏，是通过修改 Jackson 的序列化器实现的。

#### @Sensitive 注解

1. 参数配置。

   ```java
   @Target({ElementType.METHOD})
   @Retention(RetentionPolicy.RUNTIME)
   @Documented
   public @interface Sensitive {
   
       /**
        * 标记，如果返回值的类型相同，但又要进行不同的处理，这时可以通过 tag 来区分
        */
       @AliasFor("tag")
       String value() default "";
   
       /**
        * 标记，如果返回值的类型相同，但又要进行不同的处理，这时可以通过 tag 来区分
        */
       String tag() default "";
   }
   ```

2. 注解放在方法上，表明该方法的返回值要进行脱敏处理。

#### 类型脱敏

1. 例子。

   ```java
   @Bean
   @Order(1)
   public static SensitiveHandler sensitiveHandler1() {
       return new SensitiveHandler() {
           @Override
           public Object handle(Object retValCopy, Object processed, String tag, Map<String, Object> sendMessages, Map<String, Map<String, Object>> messageBroker) throws Throwable {
               if (processed instanceof User) {
                   User user = (User) processed;
                   user.setEmail("my@gamil.com");
               }
               return processed;
           }
       };
   }
   ```

#### Tag 脱敏

1. 例子。

   ```java
   @Bean
   @Order(2)
   public static SensitiveHandler sensitiveHandler2() {
       return new SensitiveHandler() {
           @Override
           public Object handle(Object retValCopy, Object processed, String tag, Map<String, Object> sendMessages, Map<String, Map<String, Object>> messageBroker) throws Throwable {
               // 判断标记
               if ("getUser2".equals(tag)) {
                   User user = (User) processed;
                   String newEmail = null;
                   if (retValCopy instanceof User) {
                       // 判断原始类型是否为 User
                       User valCopy = (User) retValCopy;
                       newEmail = valCopy.getEmail();
                   } else if (retValCopy instanceof ResponseEntity) {
                       // 判断原始类型是否为 ResponseEntity
                       ResponseEntity<LinkedHashMap<String, Object>> copy = (ResponseEntity<LinkedHashMap<String, Object>>) retValCopy;
                       Map<String, Object> body = copy.getBody();
                       newEmail = (String) body.get("email");
                   }
                   if (newEmail != null) {
                       // 获取原始的 Email，再拼接上新的字符串
                       user.setEmail("经过处理后的邮箱: " + newEmail);
                   }
               }
               return processed;
           }
       };
   }
   ```

#### 敏感信息处理器

1. 代码。

   ```java
   public interface SensitiveHandler extends Ordered {
   
       /**
        * 处理返回值
        *
        * @param retValCopy    原来返回值的拷贝
        * @param processed     处理后的返回值
        * @param tag           标记
        * @param sendMessages  发送的消息
        * @param messageBroker 消息代理
        * @return 处理后的结果
        */
       default Object handle(Object retValCopy,
                             Object processed,
                             String tag,
                             Map<String, Object> sendMessages,
                             Map<String, Map<String, Object>> messageBroker) throws Throwable {
           return processed;
       }
   
       @Override
       default int getOrder() {
           return 0;
       }
   }
   ```

2. 排序采用 SpringBoot 的排序机制，相关注意事项请参考[日志](../log/log.md)部分。

#### ResponseEntity 类型的上游敏感信息处理器

1. 代码。

   ```java
   public class ResponseEntityUpStreamSensitiveHandler implements SensitiveHandler {
   
       @Override
       public Object handle(Object retValCopy, Object processed, String tag, Map<String, Object> sendMessages, Map<String, Map<String, Object>> messageBroker) throws Throwable {
           if (processed instanceof ResponseEntity) {
               ResponseEntity<?> responseEntity = (ResponseEntity<?>) processed;
               final Object body = responseEntity.getBody();
               sendMessages.put("handle", true);
               return body;
           }
           return processed;
       }
   
       @Override
       public int getOrder() {
           return Ordered.HIGHEST_PRECEDENCE;
       }
   }
   ```

2. 对于**真正的**数据存放在泛型中的数据，如 `ResponseEntity` 这样的类型，自动配置了上游敏感信息处理器，在上游将响应体中的数据取出来，并通过消息代理发送消息到下游。

#### ResponseEntity 类型的下游敏感信息处理器

1. 代码。

   ```java
   public class ResponseEntityDownStreamSensitiveHandler implements SensitiveHandler {
   
       @Override
       public Object handle(Object retValCopy, Object processed, String tag, Map<String, Object> sendMessages, Map<String, Map<String, Object>> messageBroker) throws Throwable {
           final Map<String, Object> receivedMessages = messageBroker.get(ResponseEntityUpStreamSensitiveHandler.class.getSimpleName());
           if (receivedMessages.get("handle") != null && receivedMessages.get("handle").equals(true)) {
               ResponseEntity<?> responseEntityCopy = (ResponseEntity<?>) retValCopy;
               return ResponseEntity.status(responseEntityCopy.getStatusCode())
                       .headers(responseEntityCopy.getHeaders())
                       .body(processed);
           }
           return processed;
       }
   
       @Override
       public int getOrder() {
           return Ordered.LOWEST_PRECEDENCE;
       }
   }
   ```

2. 同时也自动配置了下游敏感信息处理器，通过消息代理取出上游传过来的消息，再重新将数据封装成 `ResponseEntity` 。

#### 注解脱敏 @Desensitized

1. 提供了默认的注解 @Desensitized，放在属性上，根据传入的脱敏类型进行不同的处理。

   ```java
   @Target({ElementType.FIELD})
   @Retention(RetentionPolicy.RUNTIME)
   @Documented
   public @interface Desensitized {
   
       /**
        * 支持脱敏的类型
        */
       DesensitizedType value();
   }
   ```

   ```java
   public enum DesensitizedType {
   
       /**
        * 中文名
        */
       CHINESE_NAME,
   
       /**
        * 身份证号
        */
       ID_CARD,
   
       /**
        * 座机号
        */
       FIXED_PHONE,
   
       /**
        * 手机号
        */
       MOBILE_PHONE,
   
       /**
        * 地址
        */
       ADDRESS,
   
       /**
        * 电子邮件
        */
       EMAIL,
   
       /**
        * 密码
        */
       PASSWORD,
   
       /**
        * 中国大陆车牌，包含普通车辆、新能源车辆
        */
       CAR_LICENSE,
   
       /**
        * 银行卡
        */
       BANK_CARD
   }
   ```

#### 自定义默认的 @Desensitized 的脱敏规则

   1. 例子。

      ```java
      @Component(SecurityAutoConfiguration.DESENSITIZED_ANNOTATION_SENSITIVE_SERIALIZER_BEAN_NAME)
      public static class CustomerDesensitizedAnnotationSensitiveSerializer extends DesensitizedAnnotationSensitiveSerializer {
      
          @Override
          protected void processAddress(String value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
              gen.writeString(DesensitizedUtil.address(value, 4));
          }
      }
      ```

   2. 注意：务必保证 `DesensitizedAnnotationSensitiveSerializer` 有默认的构造函数。

#### 全局配置

1. 配置项。

   ```properties
   support.security.auto=true
   support.security.sensitive.enable=true
   support.security.sensitive.aspect-order=0
   ```

### 注意事项和使用建议

1. 如果返回值的类型为 `ResponseEntity`，那么他的 body 的类型为 `LinkedHashMap`，再进行转换的时候要注意。
2. 务必保证自定义的 `DesensitizedAnnotationSensitiveSerializer` 有默认的构造函数，推荐使用 @Component 注入，而不是 @Bean 注入。
3. 采用 @Order 排序还是重写 getOrder() 方法，请参阅[日志](../log/log.md)部分。 
4. 通常情况下如果是固定的脱敏规则，采用注解脱敏是最好的选择，如果脱敏和业务挂钩，如规则存放在数据库中，或者只针对返回值中的部分数据进行脱敏，这时候只能采用类型脱敏或者Tag脱敏。
5. 这三种方式可以结合再一起使用，要注意顺序，以免脱敏规则被覆盖，得不到想要的结果。