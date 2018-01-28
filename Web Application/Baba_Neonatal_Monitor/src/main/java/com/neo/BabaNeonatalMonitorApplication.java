package com.neo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import com.neo.controller.MainController;

@SpringBootApplication
@ComponentScan(basePackageClasses=MainController.class)
public class BabaNeonatalMonitorApplication extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(BabaNeonatalMonitorApplication.class, args);
	}
}
