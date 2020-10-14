package com.codecool.jpaseries.repository;

import com.codecool.jpaseries.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeriesRepository extends JpaRepository<Series , Long> {

    @Query("update Series s set s.company = 'UNIVERSAL' where s.id in " +
    "(select e.season.series.id from Episode e WHERE e.episodeName like :name)")
    @Modifying(clearAutomatically = true)
    int updateAllToModified(@Param("name") String name);

}
