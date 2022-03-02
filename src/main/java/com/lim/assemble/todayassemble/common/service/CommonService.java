package com.lim.assemble.todayassemble.common.service;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;

import javax.servlet.http.HttpServletResponse;

public interface CommonService {

    public void loginWithToken(HttpServletResponse response, Accounts accounts);
}
