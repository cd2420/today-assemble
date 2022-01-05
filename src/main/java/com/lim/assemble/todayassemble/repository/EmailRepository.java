package com.lim.assemble.todayassemble.repository;

import com.lim.assemble.todayassemble.domain.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
