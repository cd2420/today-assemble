package com.lim.assemble.todayassemble.likes.repository;

import com.lim.assemble.todayassemble.likes.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikesRepository extends JpaRepository<Likes, Long> {
}
