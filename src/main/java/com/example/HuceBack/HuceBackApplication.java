package com.example.HuceBack;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@SpringBootApplication
public class HuceBackApplication {
	@Bean // đánh dấu đây là bean để thêm vào container
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
//	@Bean
//	public JavaMailSender javaMailSender() {
//		return new JavaMailSenderImpl();
//	}
	public static void main(String[] args) {
		SpringApplication.run(HuceBackApplication.class, args);
	}

}
