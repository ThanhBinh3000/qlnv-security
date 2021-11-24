package com.tcdt.qlnvsecurity.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginRequest {
	@ApiModelProperty(example = "admin")
	private String username;
	@ApiModelProperty(example = "123456")
	private String password;

}