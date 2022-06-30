package com.example.demo;

import com.example.demo.student.Student;
import com.example.demo.student.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Configuration
public class StudentConfig {

    @Bean
    CommandLineRunner commandLineRunner(StudentRepository studentRepository){
        return args -> {
            Student maikon = new Student(
                    "Maikon",
                    "maikon@mail.com",
                    LocalDate.of(1987, Month.AUGUST, 10));

            Student maria = new Student(
                    "Maria",
                    "maria@mail.com",
                    LocalDate.of(1988, Month.JANUARY, 2));

            studentRepository.saveAll(List.of(maikon, maria));
        };

    }
}
