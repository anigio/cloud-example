package com.anigio.example.cloudapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

/**
 * Created by Eric.Shang on 2019-10-28.
 */
@SpringBootApplication
@ImportResource({"classpath:applicationContext.xml"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
