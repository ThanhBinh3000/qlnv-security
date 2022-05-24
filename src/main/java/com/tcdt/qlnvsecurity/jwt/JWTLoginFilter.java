package com.tcdt.qlnvsecurity.jwt;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.tcdt.qlnvsecurity.request.LoginRequest;
import com.tcdt.qlnvsecurity.response.BaseResponse;
import com.tcdt.qlnvsecurity.response.JwtResponse;
import com.tcdt.qlnvsecurity.table.UserInfo;
import com.tcdt.qlnvsecurity.util.*;

public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
	public JWTLoginFilter(String url, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(url));
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		LoginRequest creds = new ObjectMapper().readValue(request.getInputStream(), LoginRequest.class);
		request.setAttribute("username", creds.getUsername());
		request.setAttribute("password", creds.getPassword());
		/**
		 * truyen pass word rong de bo qua kiem tra password
		 */
		return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(creds.getUsername(), "", Collections.emptyList()));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();
		System.out.println("JWTLoginFilter.successfulAuthentication:");
		response.setHeader("Content-type", "application/json; charset=utf-8");
		boolean checkLogin = true;
		if (customUserDetails.user.getSysType().equals(Contains.TYPE_USER_BACKEND)) {
			checkLogin = checkPasswordFrontEnd(request, customUserDetails.user);
		} else {
			checkLogin = checkPasswordFrontEnd(request, customUserDetails.user);
		}

		BaseResponse resp = new BaseResponse();
		if (checkLogin) {
			List<String> roles = customUserDetails.getAuthorities().stream().map(item -> item.getAuthority())
					.collect(Collectors.toList());
			
			TokenAuthenticationService.addAuthentication(response, authResult.getName(),customUserDetails.getCapDvi(), customUserDetails.getDvql(), customUserDetails.getMaQd(),
					customUserDetails.getMaTr(),customUserDetails.getMaKhqlh(),customUserDetails.getMaKtbq(),customUserDetails.getMaTckt(),roles, customUserDetails.getTenDvi());

			String authorizationString = response.getHeader(TokenAuthenticationService.HEADER_STRING);

			System.out.println("Authorization String=" + authorizationString);

			
			
			JwtResponse jwtResponse = new JwtResponse();
			jwtResponse.setId(customUserDetails.user.getId());
			jwtResponse.setUsername(customUserDetails.getUsername());
			jwtResponse.setToken(authorizationString);
			//jwtResponse.setRoles(roles);
			resp.setData(jwtResponse);
			resp.setStatusCode(Contains.RESP_SUCC);
		} else {
			resp.setMsg("Tên đăng nhập hoặc mật khẩu không chính xác");
			resp.setStatusCode(Contains.RESP_FAIL);
		}
		response.getWriter().print(new Gson().toJson(resp));
	}

	/**
	 * kiem tra password user dang nhap doi voi dang nhap tu frontend
	 */
	public boolean checkPasswordFrontEnd(HttpServletRequest request, UserInfo UserInfo)
			throws IOException, ServletException {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

		if (bCryptPasswordEncoder.matches(decodeValue(request.getAttribute("password").toString()),
				UserInfo.getPassword())) {
			return true;
		}
		return false;
	}

	/**
	 * kiem tra user dang nhap tren ldap (qua SOAP) voi user dang nhap la backend
	 * 
	 * @param user
	 */
	public boolean checkLdap(HttpServletRequest request, String region) {
		return true;
	}

	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		response.setHeader("Content-type", "application/json; charset=utf-8");
		BaseResponse resp = new BaseResponse();
		resp.setMsg("Tên đăng nhập hoặc mật khẩu không chính xác");
		resp.setStatusCode(Contains.RESP_FAIL);
		response.getWriter().print(new Gson().toJson(resp));
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