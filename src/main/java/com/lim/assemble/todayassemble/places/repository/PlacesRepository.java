package com.lim.assemble.todayassemble.places.repository;

import com.lim.assemble.todayassemble.places.entity.Places;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlacesRepository extends JpaRepository<Places, Long> {
}
