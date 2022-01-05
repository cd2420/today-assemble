package com.lim.assemble.todayassemble.mapper;

import com.lim.assemble.todayassemble.domain.entity.Accounts;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountsMapper {

    List<Accounts> findAll();
}
