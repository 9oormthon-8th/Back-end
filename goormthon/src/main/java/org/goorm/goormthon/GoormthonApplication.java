package org.goorm.goormthon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class GoormthonApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoormthonApplication.class, args);
	}

}
