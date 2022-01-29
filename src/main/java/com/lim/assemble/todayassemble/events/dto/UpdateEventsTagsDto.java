package com.lim.assemble.todayassemble.events.dto;

import com.lim.assemble.todayassemble.accounts.entity.Accounts;
import lombok.Data;

@Data
public class UpdateEventsTagsDto {

    private UpdateEventsTagsReq updateEventsTagsReq;
    private Accounts accounts;

    public UpdateEventsTagsDto(UpdateEventsTagsReq updateEventsTagsReq, Accounts accounts) {
        this.updateEventsTagsReq = updateEventsTagsReq;
        this.accounts = accounts;
    }
}
