package com.lim.assemble.todayassemble.repository;

import com.lim.assemble.todayassemble.domain.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagsRepository extends JpaRepository<Tags, Long> {
}
