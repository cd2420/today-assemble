package com.lim.assemble.todayassemble.likes.repository;

import com.lim.assemble.todayassemble.likes.entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LikesRepository extends JpaRepository<Likes, Long> {
    Optional<Likes> findByAccountsIdAndEventsId(Long accountsId, Long eventId);

    @Query("SELECT l FROM Likes l LEFT JOIN FETCH Accounts a ON l.accounts = a WHERE a.email = :email")
    Optional<List<Likes>> findByAccountsEmail(String email);
}
