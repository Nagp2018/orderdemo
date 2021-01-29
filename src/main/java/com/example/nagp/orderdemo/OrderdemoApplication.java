package com.example.nagp.orderdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableAutoConfiguration
public class OrderdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderdemoApplication.class, args);
	}

}
