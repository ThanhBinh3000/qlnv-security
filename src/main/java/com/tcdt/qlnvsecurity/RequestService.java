package com.tcdt.qlnvsecurity;

import javax.servlet.http.HttpServletRequest;

public interface RequestService {

	String getClientIp(HttpServletRequest request);

}