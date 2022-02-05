package com.lim.assemble.todayassemble.accounts.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateAccountsDto {
    private Long targetId;
    private Long checkId;
}
