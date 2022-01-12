package com.tcdt.qlnvsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
@EnableDiscoveryClient
@EntityScan(basePackages = { "com.tcdt.qlnvsecurity.table" })
public class QlnvSecurityApplication {
	public static void main(String[] args) {
		SpringApplication.run(QlnvSecurityApplication.class, args);
	}

}
