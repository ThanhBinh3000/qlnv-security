package com.tcdt.qlnvsecurity.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.tcdt.qlnvsecurity.table.Role;
import com.tcdt.qlnvsecurity.table.UserInfo;

import lombok.Data;

@Data
public class CustomUserDetails implements UserDetails {
	private static final long serialVersionUID = 1L;

	UserInfo user;

	public CustomUserDetails(UserInfo user2) {
		this.user = user2;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();		
		for(Role role:user.getRoles()) {
			List<GrantedAuthority> subAu = role.getPermisstion().stream()
					.map(per -> new SimpleGrantedAuthority(per.getCode())).collect(Collectors.toList());
			authorities.addAll(subAu);
		}
		return authorities;
	}

	// mac dinh ko check password de la ""
	@Override
	public String getPassword() {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder.encode("");
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		if (user.getStatus().equals("01")) {
			return true;
		} else {
			return false;
		}
	}

	public long getDvql() {
		return user.getDvql();
	}

}
