package com.lim.assemble.todayassemble.email.repository;

import com.lim.assemble.todayassemble.email.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailRepository extends JpaRepository<Email, Long> {
}
