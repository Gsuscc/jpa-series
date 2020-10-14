package com.codecool.jpaseries.repository;

import com.codecool.jpaseries.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SeasonRepositry extends JpaRepository<Season, Long> {



}
