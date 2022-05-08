package com.tcdt.qlnvsecurity.service;

import com.tcdt.qlnvsecurity.repository.QlnvDmDonviRepository;
import com.tcdt.qlnvsecurity.table.QlnvDmDonvi;
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

	@Autowired
	QlnvDmDonviRepository qlnvDmDonviRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserInfo user = userRepository.findByUsername(username);
		if (user == null) {
			throw new UsernameNotFoundException(username);
		}
		QlnvDmDonvi dvi = qlnvDmDonviRepository.findByMaDvi(user.getDvql());
		user.setMaQd(dvi.getMaQd());
		user.setMaTr(dvi.getMaTr());
		user.setMaKhqlh(dvi.getMaKhqlh());
		user.setMaKtbq(dvi.getMaKtbq());
		user.setMaTckt(dvi.getMaTckt());
		return new CustomUserDetails(user);
	}
	
	public Iterable<UserAction> findAll() {
		return userActionRepository.findAll();
	}
	
	public void saveUserHistory(UserHistory userHistory) {
		userHistoryRepository.save(userHistory);
	}

}
