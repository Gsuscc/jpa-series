package com.codecool.jpaseries.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Episode {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String episodeName;

    private LocalDate releaseDate;

    @ElementCollection
    @Singular
    private List<String> actors;

    @ManyToOne(cascade = CascadeType.ALL)
    private Season season;

    @Transient
    private long age;

    public void calculateAge(){
        if (releaseDate != null){
            age = ChronoUnit.YEARS.between(releaseDate, LocalDate.now());
        }
    }






}
