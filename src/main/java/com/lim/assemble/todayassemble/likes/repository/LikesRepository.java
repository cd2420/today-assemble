package com.lim.assemble.todayassemble.likes.repository;

import com.lim.assemble.todayassemble.likes.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByAccountsIdAndEventsId(Long accountsId, Long eventId);
}
