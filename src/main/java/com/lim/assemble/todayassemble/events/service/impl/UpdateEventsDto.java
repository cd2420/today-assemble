package com.lim.assemble.todayassemble.events.service.impl;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import com.lim.assemble.todayassemble.events.dto.UpdateEventsReq;
import lombok.Data;

@Data
public class UpdateEventsDto {

    private UpdateEventsReq updateEventsReq;

    private Accounts accounts;

    public UpdateEventsDto(UpdateEventsReq updateEventsReq, Accounts accounts) {
        this.updateEventsReq = updateEventsReq;
        this.accounts = accounts;
    }
}
