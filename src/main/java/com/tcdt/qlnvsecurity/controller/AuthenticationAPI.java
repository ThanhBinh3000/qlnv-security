package com.tcdt.qlnvsecurity.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tcdt.qlnvsecurity.repository.UserInfoRepository;
import com.tcdt.qlnvsecurity.request.LoginRequest;
import com.tcdt.qlnvsecurity.response.BaseResponse;
import com.tcdt.qlnvsecurity.table.UserInfo;
import com.tcdt.qlnvsecurity.util.Contains;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author hoanglb
 * @version 0.01
 */
@RestController
@RequestMapping("/usr/authentication")
@Api(tags = "Get token test", value = "Authentication API", description = "Dùng để lấy token, sau này sẽ xóa đi ")
public class AuthenticationAPI {
	@Autowired
	private UserInfoRepository userInfoRepository;

	static final long EXPIRATIONTIME = 36_000_000; // 10 phut 36_000_000

	static final String SECRET = "Teca15DuyTan";

	static final String TOKEN_PREFIX = "Bearer";

	static final String HEADER_STRING = "Authorization";

	@ApiOperation(value = "Authenticated User Login")
	@PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<BaseResponse> login(@Valid @RequestBody LoginRequest objReq) {
		BaseResponse resp = new BaseResponse();
		try {
			UserInfo user = userInfoRepository.findByUserIgnoreCase(objReq.getUsername());
			if (user == null)
				throw new Exception("Tài khoản không tồn tại");

			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			if (bCryptPasswordEncoder.matches(decodeValue(objReq.getPassword()), user.getPassword()))
				throw new Exception("Tên tài khoản hoặc mật khẩu không chính xác");

			String JWT = Jwts.builder().setSubject(objReq.getUsername()).setIssuer(user.getDvql())
					.claim("role", "[{a:'b'}]").setExpiration(new Date(System.currentTimeMillis() + EXPIRATIONTIME))
					.signWith(SignatureAlgorithm.HS512, SECRET).compact();

			resp.setData(TOKEN_PREFIX + " " + JWT);
			resp.setStatusCode(Contains.RESP_SUCC);

		} catch (Exception e) {
			// TODO: handle exception
			resp.setStatusCode(Contains.RESP_FAIL);
			resp.setMsg(e.getMessage());
		}
		return ResponseEntity.ok(resp);
	}

	// Decodes a URL encoded string using `UTF-8`
	public static String decodeValue(String value) {
		try {
			return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
		} catch (UnsupportedEncodingException ex) {
			throw new RuntimeException(ex.getCause());
		}
	}
}