package com.codecool.jpaseries;

import com.codecool.jpaseries.entity.Episode;
import com.codecool.jpaseries.entity.Season;
import com.codecool.jpaseries.entity.Series;
import com.codecool.jpaseries.repository.EpisodeRepository;
import com.codecool.jpaseries.repository.SeasonRepositry;
import com.codecool.jpaseries.repository.SeriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
public class JpaSeriesApplication {

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private SeasonRepositry seasonRepositry;

    @Autowired
    private SeriesRepository seriesRepository;

    public static void main(String[] args) {
        SpringApplication.run(JpaSeriesApplication.class, args);
    }

    @Bean
    @Profile("production")
    public CommandLineRunner init() {
        return args -> {
//            Episode episode1 = Episode.builder()
//                    .episodeName("Episode1")
//                    .releaseDate(LocalDate.of(1999, 10, 10))
//                    .actor("John")
//                    .actor("Jeff")
//                    .actor("Bill")
//                    .build();
//
//            Episode episode2 = Episode.builder()
//                    .episodeName("Episode2")
//                    .releaseDate(LocalDate.of(2010, 10, 10))
//                    .build();
//
//            Episode episode3 = Episode.builder()
//                    .episodeName("Episode3")
//                    .releaseDate(LocalDate.of(2001, 10, 10))
//                    .build();
//
//            Episode episode4 = Episode.builder()
//                    .episodeName("Episode4")
//                    .releaseDate(LocalDate.of(1901, 10, 10))
//                    .build();
//
//
//            Season firstSeason = Season.builder()
//                    .title("FirstSeason")
//                    .episode(episode1)
//                    .episode(episode2)
//                    .build();
//            Season secondSeason = Season.builder()
//                    .title("secondSeason")
//                    .episode(episode3)
//                    .episode(episode4)
//                    .build();
//
//
//            Series series = Series.builder()
//                    .name("Best Series")
//                    .company(Company.PIXAR)
//                    .season(firstSeason)
//                    .season(secondSeason)
//                    .build();
//
//            episode1.setSeason(firstSeason);
//            episode2.setSeason(firstSeason);
//            episode3.setSeason(secondSeason);
//            episode4.setSeason(secondSeason);
//            firstSeason.setSeries(series);
//            secondSeason.setSeries(series);
//
//            seriesRepository.save(series);


            Series game_of_jpa = Series.builder()
                    .name("Game of Jpa")
                    .seasons(getSeasons())
                    .build();
            seriesRepository.save(game_of_jpa);
        };
    }

    private List<Season> getSeasons() {
        return IntStream.range(1, 11)
                .boxed()
                .map(this::getSeason)
                .collect(Collectors.toList());
    }

    private Season getSeason(int integer) {
        return Season.builder()
                .episodes(getEpisodes(integer))
                .title("Season" + integer)
                .build();
    }

    private List<Episode> getEpisodes(int integer) {
        return IntStream.range(1, 10)
                .boxed()
                .map(integer1 -> getEpisode(integer1, integer))
                .collect(Collectors.toList());
    }

    private Episode getEpisode(int integer1, int num) {
        return Episode.builder()
                .episodeName("Episode" + integer1 + "-" + num)
                .build();
    }

}
