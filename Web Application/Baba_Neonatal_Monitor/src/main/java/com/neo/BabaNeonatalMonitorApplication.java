package com.neo;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.neo.controller.MainController;
@EnableAutoConfiguration
@Configuration
@SpringBootApplication
@ComponentScan(basePackageClasses=MainController.class)
public class BabaNeonatalMonitorApplication extends SpringBootServletInitializer{

	public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
		SpringApplication.run(BabaNeonatalMonitorApplication.class, args);
	}
}