package com.tcdt.qlnvsecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EntityScan(basePackages = {"com.tcdt.qlnvsecurity.table" })
public class QlnvSecurityApplication {
	public static void main(String[] args) {
		SpringApplication.run(QlnvSecurityApplication.class, args);
	}

}
