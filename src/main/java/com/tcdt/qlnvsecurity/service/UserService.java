package com.tcdt.qlnvsecurity.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tcdt.qlnvsecurity.jwt.CustomUserDetails;
import com.tcdt.qlnvsecurity.repository.UserActionRepository;
import com.tcdt.qlnvsecurity.repository.UserHistoryRepository;
import com.tcdt.qlnvsecurity.repository.UserInfoRepository;
import com.tcdt.qlnvsecurity.table.UserAction;
import com.tcdt.qlnvsecurity.table.UserHistory;
import com.tcdt.qlnvsecurity.table.UserInfo;

@Service
public class UserService implements UserDetailsService {
	@Autowired
	private UserInfoRepository userRepository;
	@Autowired
	UserHistoryRepository userHistoryRepository;

	@Autowired
	UserActionRepository userActionRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfo user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		return new CustomUserDetails(user);
	}
	
	public Iterable<UserAction> findAll() {
		return userActionRepository.findAll();
	}
	
	public void saveUserHistory(UserHistory userHistory) {
		userHistoryRepository.save(userHistory);
	}

}
