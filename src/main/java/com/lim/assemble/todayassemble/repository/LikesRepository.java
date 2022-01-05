package com.lim.assemble.todayassemble.repository;

import com.lim.assemble.todayassemble.domain.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
}
