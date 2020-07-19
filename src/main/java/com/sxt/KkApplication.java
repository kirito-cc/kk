package com.sxt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = {"com.sxt.sys.mapper"})
public class KkApplication {

	public static void main(String[] args) {
		SpringApplication.run(KkApplication.class, args);
	}

}
