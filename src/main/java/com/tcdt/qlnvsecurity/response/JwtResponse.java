package com.tcdt.qlnvsecurity.response;

import java.util.List;

import lombok.Data;

@Data
public class JwtResponse {
	private Long id;
	private String username;
	private String type = "Bearer";
	private String token;
	private List<String> roles;

}
