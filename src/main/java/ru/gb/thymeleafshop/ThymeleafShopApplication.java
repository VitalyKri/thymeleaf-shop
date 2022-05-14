package ru.gb.thymeleafshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "ru.gb")
public class ThymeleafShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThymeleafShopApplication.class, args);
    }


}
