package com.example.employee_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;



@SpringBootApplication(
		//❌ “Do NOT apply default security rules”
		//❌ “Do NOT protect my APIs”
		//❌ “Do NOT create login/password”
//		exclude = {
//				org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class
//		}
)
public class EmployeeManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmployeeManagementApplication.class, args);
	}

}
