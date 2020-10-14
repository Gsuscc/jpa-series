package com.codecool.jpaseries.entity;


import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Season {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private int numberOfEpisodes;

    @ManyToOne(cascade = CascadeType.PERSIST)
    private Series series;

    @Singular
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "season", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    private Set<Episode> episodes;


}
