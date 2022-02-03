package com.lim.assemble.todayassemble.likes.service;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.events.dto.EventsDto;

import java.util.List;

public interface LikesService {
    void manageLikes(Long eventsId, Accounts accounts);

    List<EventsDto> getAccountLikesEventList(Accounts accounts);
}
