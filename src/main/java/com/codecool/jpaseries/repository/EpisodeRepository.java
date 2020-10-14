package com.codecool.jpaseries.repository;

import com.codecool.jpaseries.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;


public interface EpisodeRepository extends JpaRepository<Episode, Long> {

    List<Episode> findByEpisodeNameStartingWithOrReleaseDateBetween(String name, LocalDate from, LocalDate to);

    @Query("SELECT distinct s.series.company from Season s")
    List<String> findAllCompany();
}
