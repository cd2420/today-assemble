package com.lim.assemble.todayassemble.email.factory;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.common.type.EmailsType;

public interface EmailForm {

    public void setToken(Accounts accounts);

    public EmailsType getEmailType();

    public String getPayLoad();

    public String getSubject();

    public String getToken();

    public String getType();

    public String getLinkName();

}
