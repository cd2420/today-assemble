package com.lim.assemble.todayassemble.likes.dto;

import com.lim.assemble.todayassemble.accounts.dto.AccountsDto;
import com.lim.assemble.todayassemble.likes.entity.Likes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikesAccountsDto {

    private AccountsDto accountsDto;

    public static Set<LikesAccountsDto> returnLikesDtoSet(Set<Likes> likesSet) {
        if (likesSet == null) {
            return new HashSet<>();
        }

        return likesSet.stream()
                        .map((likesDto) -> {
                            return new LikesAccountsDto(
                                    AccountsDto.builder()
                                    .id(likesDto.getAccounts().getId())
                                    .build()
                            );
                        })
                        .collect(Collectors.toSet());
    }


}
