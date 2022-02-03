package com.lim.assemble.todayassemble.likes.service;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;

public interface LikesService {
    void manageLikes(Long eventsId, Accounts accounts);
}
