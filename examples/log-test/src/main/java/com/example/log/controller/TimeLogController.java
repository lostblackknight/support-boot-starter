package com.example.log.controller;

import com.sitech.crmbcc.support.annotation.log.TimeLog;
import com.sitech.crmbcc.support.handler.log.LogHandler;
import com.sitech.crmbcc.support.model.log.LogModel;
import com.sitech.crmbcc.support.model.log.TimeLogModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chensixiang (chensixiang1234@gmail.com)
 * @since 2022/8/17 8:54
 */
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
}
