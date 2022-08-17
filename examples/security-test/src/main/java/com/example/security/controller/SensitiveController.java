package com.example.security.controller;

import com.sitech.crmbcc.support.annotation.security.Desensitized;
import com.sitech.crmbcc.support.annotation.security.Sensitive;
import com.sitech.crmbcc.support.enums.security.DesensitizedType;
import com.sitech.crmbcc.support.handler.security.SensitiveHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chensixiang (chensixiang1234@gmail.com)
 * @since 2022/8/17 14:00
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sensitive")
public class SensitiveController {

    private final UserRepository userRepository;

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
