# 概述

对日志功能的增强，采用自动配置，基于注解和切面实现。

- [概述](#概述)
  - [请求日志](#请求日志)
    - [快速入门](#快速入门)
    - [教程](#教程)
      - [@RequestLog 注解](#requestlog-注解)
      - [默认的请求日志处理器](#默认的请求日志处理器)
      - [请求日志模型](#请求日志模型)
      - [全局配置](#全局配置)
      - [添加自定义 RequestLogHandler](#添加自定义-requestloghandler)
      - [覆盖默认的请求日志处理器](#覆盖默认的请求日志处理器)
    - [注意事项和使用建议](#注意事项和使用建议)
  - [耗时日志](#耗时日志)
    - [快速入门](#快速入门-1)
    - [教程](#教程-1)
      - [@TimeLog 注解](#timelog-注解)
      - [默认的耗时日志处理器](#默认的耗时日志处理器)
      - [耗时日志模型](#耗时日志模型)
      - [全局配置](#全局配置-1)
      - [添加自定义 TimeLogHandler](#添加自定义-timeloghandler)
      - [覆盖默认的耗时日志处理器](#覆盖默认的耗时日志处理器)
    - [注意事项和使用建议](#注意事项和使用建议-1)
    - [问题](#问题)

## 请求日志

### 快速入门

```java
@RestController
@RequiredArgsConstructor
@RequestMapping("/requestLog")
public class RequestLogController {

    private final StudentRepository studentRepository;

    @RequestLog
    @GetMapping("/students/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentRepository.selectStudentById(id);
    }

    @RequestLog
    @GetMapping("/students")
    public List<Student> getStudentList() {
        return studentRepository.selectStudentList();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Student {
        private Long id;
        private String name;
        private String number;
        private String gender;
        private Integer age;
    }

    @Repository
    public static class StudentRepository {

        private final List<Student> students = new ArrayList<>();

        @Bean
        public CommandLineRunner requestLogRunner() {
            return args -> {
                students.add(new Student(1L, "Alice", "000001", "female", 18));
                students.add(new Student(2L, "Aimer", "000002", "female", 18));
                students.add(new Student(3L, "Jon", "000003", "male", 20));
                students.add(new Student(4L, "Jmeter", "000004", "male", 21));
                students.add(new Student(5L, "Jack", "000005", "female", 20));
            };
        }

        public Student selectStudentById(Long id) {
            return students.stream().filter(student -> student.getId().equals(id)).findFirst().orElse(null);
        }

        public List<Student> selectStudentList() {
            return students;
        }
    }
}
```

```shell
# 发送请求
curl http://localhost:8080/requestLog/students/1

# 控制台打印结果
2022-08-17 09:55:24.619  INFO 12032 --- [nio-8080-exec-5] c.s.c.s.h.log.DefaultRequestLogHandler   : {"ipAddress":"127.0.0.1","method":"GET","requestURI":"/requestLog/students/1","requestParam":{"id":1},"responseParam":{"id":1,"name":"Alice","number":"000001","gender":"female","age":18},"className":"com.example.log.controller.RequestLogController","methodName":"getStudent","totalTime":"1ms"}
```

### 教程

#### @RequestLog 注解

1. 参数配置。

   ```java
   @Target({ElementType.METHOD, ElementType.TYPE})
   @Retention(RetentionPolicy.RUNTIME)
   @Documented
   public @interface RequestLog {
   
       /**
        * 描述
        */
       @AliasFor("description")
       String value() default "";
   
       /**
        * 描述
        */
       String description() default "";
   
       /**
        * 是否展示 IP 地址
        */
       boolean showIpAddress() default true;
   
       /**
        * 是否展示请求的方法的类型
        */
       boolean showMethod() default true;
   
       /**
        * 是否展示请求的 URI
        */
       boolean showRequestURI() default true;
   
       /**
        * 是否展示请求参数
        */
       boolean showRequestParam() default true;
   
       /**
        * 是否展示响应参数
        */
       boolean showResponseParam() default true;
   
       /**
        * 是否展示请求的类的名称
        */
       boolean showClassName() default true;
   
       /**
        * 是否展示请求的方法的名称
        */
       boolean showMethodName() default true;
   
       /**
        * 是否展示请求的耗时
        */
       boolean showTotalTime() default true;
   }
   ```

2. 注意事项。

   - 一个类上和当前类的方法上都有该注解，采用就近原则，采用方法上的配置，描述的属性会进行组合。
     例如：如果类上的描述为 TestController， 方法上的描述为 Hello， 最终描述为 TestController | Hello。

   - 如果所有属性都不展示，且包括描述为 ""，将不会调用 DefaultRequestLogHandler 中的 DefaultRequestLogHandler.handle(LogModel) 方法 和 DefaultRequestLogHandler.errorHandle(LogModel) 方法，即不会打印日志。

   - 如果方法在执行期间发生异常，将永远不会打印响应参数和耗时。

#### 默认的请求日志处理器

1. 直接使用 slf4j 打印日志。

   ```java
   @Slf4j
   public class DefaultRequestLogHandler implements LogHandler {
   
       private final ObjectMapper objectMapper;
   
       private final LogProperties.RequestLog requestLogProperties;
   
       public DefaultRequestLogHandler(ObjectMapper objectMapper, LogProperties.RequestLog requestLogProperties) {
           this.objectMapper = objectMapper;
           this.requestLogProperties = requestLogProperties;
       }
   
       @Override
       public void handle(LogModel model) throws Throwable {
            // 判断是否为 RequestLogModel
           if (model instanceof RequestLogModel) {
               // 打印日志
               log.info(objectMapper.writeValueAsString(model));
           }
       }
   
       @Override
       public void errorHandle(LogModel model) throws Throwable {
           // 判断是否为 RequestLogModel
           if (model instanceof RequestLogModel) {
               // 打印错误日志
               log.error(objectMapper.writeValueAsString(model));
           }
       }
   
       @Override
       public int getOrder() {
           return requestLogProperties.getDefaultLogHandlerOrder();
       }
   }
   ```

#### 请求日志模型

1. 请求日志模型与 @RequestLog 中的参数保持一致。

   ```java
   @Data
   @JsonInclude(JsonInclude.Include.NON_NULL)
   public class RequestLogModel implements LogModel {
   
       /**
        * 描述
        */
       private String description;
   
       /**
        * IP 地址
        */
       private String ipAddress;
   
       /**
        * 请求的方法的类型
        */
       private String method;
   
       /**
        * 请求的 URI
        */
       private String requestURI;
   
       /**
        * 请求参数
        */
       private Map<Object, Object> requestParam;
   
       /**
        * 响应参数
        */
       private Object responseParam;
   
       /**
        * 请求的类的名称
        */
       private String className;
   
       /**
        * 请求的方法的名称
        */
       private String methodName;
   
       /**
        * 请求的耗时
        */
       private String totalTime;
   }
   ```

#### 全局配置

1. 配置项。

   ```properties
   support.log.auto=true
   support.log.request.enable=true
   support.log.request.aspect-order=0
   support.log.request.default-log-handler-order=0
   support.log.request.global.show-class-name=true
   support.log.request.global.show-method-name=true
   support.log.request.global.show-ip-address=true
   support.log.request.global.show-method=true
   support.log.request.global.show-request-param=true
   support.log.request.global.show-request-u-r-i=true
   support.log.request.global.show-response-param=true
   support.log.request.global.show-total-time=true
   ```

2. 注意 `support.log.request.global`配置会进行全局覆盖，会使 @RequestLog 中的配置失效，使用时要小心。

#### 添加自定义 RequestLogHandler

1. 例子

   ```java
   @Bean
   @Order(1)
   public LogHandler myRequestLogHandler1() {
       return new LogHandler() {
           @Override
           public void handle(LogModel model) throws Throwable {
               if (model instanceof RequestLogModel) {
                   System.out.println("myRequestLogHandler1.handle" + model);
               }
           }
   
           @Override
           public void errorHandle(LogModel model) throws Throwable {
               if (model instanceof RequestLogModel) {
                   System.out.println("myRequestLogHandler1.errorHandle" + model);
               }
           }
       };
   }
   
   @Bean
   public LogHandler myRequestLogHandler2() {
       return new LogHandler() {
           @Override
           public void handle(LogModel model) throws Throwable {
               if (model instanceof RequestLogModel) {
                   System.out.println("myRequestLogHandler2.handle" + model);
               }
           }
   
           @Override
           public void errorHandle(LogModel model) throws Throwable {
               if (model instanceof RequestLogModel) {
                   System.out.println("myRequestLogHandler2.errorHandle" + model);
               }
           }
   
           @Override
           public int getOrder() {
               return 2;
           }
       };
   }
   
   @Component
   public static class MyRequestLogHandler3 implements LogHandler {
   
       @Override
       public void handle(LogModel model) throws Throwable {
           if (model instanceof RequestLogModel) {
               System.out.println("MyRequestLogHandler3.handle" + model);
           }
       }
   
       @Override
       public void errorHandle(LogModel model) throws Throwable {
           if (model instanceof RequestLogModel) {
               System.out.println("MyRequestLogHandler3.errorHandle" + model);
           }
       }
   
       @Override
       public int getOrder() {
           return 3;
       }
   }
   
   @Bean
   @Order(4)
   public static LogHandler myRequestLogHandler4() {
       return new LogHandler() {
           @Override
           public void handle(LogModel model) throws Throwable {
               if (model instanceof RequestLogModel) {
                   System.out.println("myRequestLogHandler4.handle" + model);
               }
           }
   
           @Override
           public void errorHandle(LogModel model) throws Throwable {
               if (model instanceof RequestLogModel) {
                   System.out.println("myRequestLogHandler4.errorHandle" + model);
               }
           }
       };
   }
   ```

2. 处理器排序的实现由 Spring 提供，使用 @Order 注解 或者重写 getOrder() 方法，数字越小，优先级越高，默认设置为0。

3. 注意：如果 Controller 和 Handler (且 Handler 使用 @Bean 注入)放在一起，必须使用 static 修饰。

4. 注意：使用 @Component 注入，@Order 会失效，使用重写 getOrder() 方法排序。

#### 覆盖默认的请求日志处理器

1. 例子

   ```java
   @Bean(LogAutoConfiguration.REQUEST_LOG_HANDLER_BEAN_NAME)
   public LogHandler requestLogHandler() {
       return new LogHandler() {
           @Override
           public void handle(LogModel model) throws Throwable {
               if (model instanceof RequestLogModel) {
                   // 覆盖默认配置
               }
           }
   
           @Override
           public void errorHandle(LogModel model) throws Throwable {
               if (model instanceof RequestLogModel) {
                   // 覆盖默认配置
               }
           }
       };
   }
   ```

### 注意事项和使用建议

1. @Bean 注入 Handler，要进行排序，建议使用 @Order 注解。
2. @Component 注入 Handler，要进行排序，务必重写 getOrder() 方法排序。
3. 如果 Controller 和 Handler (且 Handler 使用 @Bean 注入)放在一起，必须使用 static 修饰。
4. `support.log.request.global`配置会进行全局覆盖，会使 @RequestLog 中的配置失效，使用时要小心。
5. 自定义请求日志处理器中，如果要进行复杂的操作，如写请求日志到数据库、发送请求日志到消息队列、使用 ELK 技术栈处理日志等，建议使用异步的方式去操作。

## 耗时日志

### 快速入门

```java

@RestController
@RequiredArgsConstructor
@RequestMapping("/timeLog")
public class TimeLogController {

    private final StudentRepository studentRepository;

    @TimeLog
    @GetMapping("/students/{id}")
    public Student getStudent(@PathVariable Long id) {
        return studentRepository.selectStudentById(id);
    }

    @TimeLog
    @GetMapping("/students")
    public List<Student> getStudentList() {
        return studentRepository.selectStudentList();
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Student {
        private Long id;
        private String name;
        private String number;
        private String gender;
        private Integer age;
    }

    @Repository
    public static class StudentRepository {

        private final List<Student> students = new ArrayList<>();

        @Bean
        public CommandLineRunner timeLogRunner() {
            return args -> {
                students.add(new Student(1L, "Alice", "000001", "female", 18));
                students.add(new Student(2L, "Aimer", "000002", "female", 18));
                students.add(new Student(3L, "Jon", "000003", "male", 20));
                students.add(new Student(4L, "Jmeter", "000004", "male", 21));
                students.add(new Student(5L, "Jack", "000005", "female", 20));
            };
        }

        public Student selectStudentById(Long id) {
            return students.stream().filter(student -> student.getId().equals(id)).findFirst().orElse(null);
        }

        public List<Student> selectStudentList() {
            return students;
        }
    }
}
```

```shell
# 发送请求
curl http://localhost:8080/timeLog/students/1

# 控制台打印结果
2022-08-17 10:45:41.718  INFO 16728 --- [nio-8080-exec-9] c.s.c.s.h.log.DefaultTimeLogHandler      : {"className":"com.example.log.controller.TimeLogController","methodName":"getStudent","startTime":"2022-08-17 10:45:41","endTime":"2022-08-17 10:45:41","totalTime":"0ms"}
```

### 教程

#### @TimeLog 注解

1. 参数配置。

   ```java
   @Target(ElementType.METHOD)
   @Retention(RetentionPolicy.RUNTIME)
   @Documented
   public @interface TimeLog {
   
       /**
        * 描述
        */
       @AliasFor("description")
       String value() default "";
   
       /**
        * 描述
        */
       String description() default "";
   
       /**
        * 是否展示类的名称
        */
       boolean showClassName() default true;
   
       /**
        * 是否展示方法的名称
        */
       boolean showMethodName() default true;
   
       /**
        * 是否展示开始时间
        */
       boolean showStartTime() default true;
   
       /**
        * 是否展示结束时间
        */
       boolean showEndTime() default true;
   
       /**
        * 是否展示耗时
        */
       boolean showTotalTime() default true;
   }
   ```

2. 注意事项。

   - 如果所有属性都不展示，且包括描述为 ""，将不会调用 DefaultTimeLogHandler 中的 DefaultTimeLogHandler.handle(LogModel) 方法 和 DefaultTimeLogHandler.errorHandle(LogModel) 方法，即不会打印日志。
   - 如果方法在执行期间发生异常，将永远不会打印耗时和结束时间。

#### 默认的耗时日志处理器

1. 直接使用 slf4j 打印日志。

   ```java
   @Slf4j
   public class DefaultTimeLogHandler implements LogHandler {
   
       private final ObjectMapper objectMapper;
   
       private final LogProperties.TimeLog timeLog;
   
       public DefaultTimeLogHandler(ObjectMapper objectMapper, LogProperties.TimeLog timeLog) {
           this.objectMapper = objectMapper;
           this.timeLog = timeLog;
       }
   
       @Override
       public void handle(LogModel model) throws Throwable {
           // 判断是否为 TimeLogModel
           if (model instanceof TimeLogModel) {
               // 打印日志
               log.info(objectMapper.writeValueAsString(model));
           }
       }
   
       @Override
       public void errorHandle(LogModel model) throws Throwable {
           // 判断是否为 TimeLogModel
           if (model instanceof TimeLogModel) {
               // 打印日志
               log.error(objectMapper.writeValueAsString(model));
           }
       }
   
       @Override
       public int getOrder() {
           return timeLog.getDefaultLogHandlerOrder();
       }
   }
   ```

#### 耗时日志模型

1. 耗时日志模型与 @TimeLog 中的参数保持一致。

   ```java
   @Data
   @JsonInclude(JsonInclude.Include.NON_NULL)
   public class TimeLogModel implements LogModel {
   
       /**
        * 描述
        */
       private String description;
   
       /**
        * 类的名称
        */
       private String className;
   
       /**
        * 方法的名称
        */
       private String methodName;
   
       /**
        * 开始时间
        */
       private Date startTime;
   
       /**
        * 结束时间
        */
       private Date endTime;
   
       /**
        * 耗时
        */
       private String totalTime;
   }
   ```

#### 全局配置

1. 配置项。

   ```yaml
   support.log.auto=true
   support.log.time.enable=true
   support.log.time.aspect-order=0
   support.log.time.default-log-handler-order=0
   support.log.time.global.show-class-name=true
   support.log.time.global.show-method-name=true
   support.log.time.global.show-start-time=true
   support.log.time.global.show-end-time=true
   support.log.time.global.show-total-time=true
   ```

2. 注意 `support.log.time.global`配置会进行全局覆盖，会使 @TimeLog 中的配置失效，使用时要小心。

#### 添加自定义 TimeLogHandler

1. 例子

   ```java
   @Bean
   @Order(1)
   public LogHandler myTimeLogHandler1() {
       return new LogHandler() {
           @Override
           public void handle(LogModel model) throws Throwable {
               if (model instanceof TimeLogModel) {
                   System.out.println("myTimeLogHandler1.handle" + model);
               }
           }
   
           @Override
           public void errorHandle(LogModel model) throws Throwable {
               if (model instanceof TimeLogModel) {
                   System.out.println("myTimeLogHandler1.errorHandle" + model);
               }
           }
       };
   }
   
   @Bean
   public LogHandler myTimeLogHandler2() {
       return new LogHandler() {
           @Override
           public void handle(LogModel model) throws Throwable {
               if (model instanceof TimeLogModel) {
                   System.out.println("myTimeLogHandler2.handle" + model);
               }
           }
   
           @Override
           public void errorHandle(LogModel model) throws Throwable {
               if (model instanceof TimeLogModel) {
                   System.out.println("myTimeLogHandler2.errorHandle" + model);
               }
           }
   
           @Override
           public int getOrder() {
               return 2;
           }
       };
   }
   
   @Component
   public static class MyTimeLogHandler3 implements LogHandler {
   
       @Override
       public void handle(LogModel model) throws Throwable {
           if (model instanceof TimeLogModel) {
               System.out.println("MyTimeLogHandler3.handle" + model);
           }
       }
   
       @Override
       public void errorHandle(LogModel model) throws Throwable {
           if (model instanceof TimeLogModel) {
               System.out.println("MyTimeLogHandler3.errorHandle" + model);
           }
       }
   
       @Override
       public int getOrder() {
           return 3;
       }
   }
   
   @Bean
   @Order(4)
   public static LogHandler myTimeLogHandler4() {
       return new LogHandler() {
           @Override
           public void handle(LogModel model) throws Throwable {
               if (model instanceof TimeLogModel) {
                   System.out.println("myTimeLogHandler4.handle" + model);
               }
           }
   
           @Override
           public void errorHandle(LogModel model) throws Throwable {
               if (model instanceof TimeLogModel) {
                   System.out.println("myTimeLogHandler4.errorHandle" + model);
               }
           }
       };
   }
   ```

2. 处理器排序的实现由 Spring 提供，使用 @Order 注解 或者重写 getOrder() 方法，数字越小，优先级越高，默认设置为0。

3. 注意：如果 Controller 和 Handler (且 Handler 使用 @Bean 注入)放在一起，必须使用 static 修饰。

4. 注意：使用 @Component 注入，@Order 会失效，使用重写 getOrder() 方法排序。

#### 覆盖默认的耗时日志处理器

1. 例子

   ```java
   @Bean(LogAutoConfiguration.TIME_LOG_HANDLER_BEAN_NAME)
   public LogHandler timeLogHandler() {
       return new LogHandler() {
           @Override
           public void handle(LogModel model) throws Throwable {
               if (model instanceof TimeLogModel) {
                   // 覆盖默认配置
               }
           }
   
           @Override
           public void errorHandle(LogModel model) throws Throwable {
               if (model instanceof TimeLogModel) {
                   // 覆盖默认配置
               }
           }
       };
   }
   ```

### 注意事项和使用建议

1. @Bean 注入 Handler，要进行排序，建议使用 @Order 注解。
2. @Component 注入 Handler，要进行排序，务必重写 getOrder() 方法排序。
3. 如果 Controller 和 Handler (且 Handler 使用 @Bean 注入)放在一起，必须使用 static 修饰。
4. `support.log.time.global`配置会进行全局覆盖，会使 @TimeLog 中的配置失效，使用时要小心。
5. 自定义耗时日志处理器中，如果要进行复杂的操作，如写耗时日志到数据库、发送耗时日志到消息队列、使用 ELK 技术栈处理日志等，建议使用异步的方式去操作。

### 问题

1. 如何修改耗时日志中的日期格式

   ```yaml
   spring:
     jackson:
       date-format: yyyy-MM-dd HH:mm:ss
       time-zone: GMT+8
   ```
