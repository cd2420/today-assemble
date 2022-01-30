package com.lim.assemble.todayassemble.events.dto;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import lombok.Data;

@Data
public class UpdateEventsDto {

    private UpdateEventsContentsReq updateEventsContentsReq;

    private Accounts accounts;

    public UpdateEventsDto(UpdateEventsContentsReq updateEventsContentsReq, Accounts accounts) {
        this.updateEventsContentsReq = updateEventsContentsReq;
        this.accounts = accounts;
    }
}
