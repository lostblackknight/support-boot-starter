package com.sitech.crmbcc.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@SpringBootApplication
@RestController
public class SupportBootStarterApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupportBootStarterApplication.class, args);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        System.out.println(requestAttributes);
        return ResponseEntity.ok("ok");
    }

}
