package com.pub;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class PubApplication {

	public static void main(String[] args) {
		//TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		SpringApplication.run(PubApplication.class, args);
	}

}
