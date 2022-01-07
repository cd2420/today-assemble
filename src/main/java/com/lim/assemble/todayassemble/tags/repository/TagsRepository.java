package com.lim.assemble.todayassemble.tags.repository;

import com.lim.assemble.todayassemble.tags.entity.Tags;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagsRepository extends JpaRepository<Tags, Long> {
}
