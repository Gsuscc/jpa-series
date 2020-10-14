package com.codecool.jpaseries.repository;

import com.codecool.jpaseries.Company;
import com.codecool.jpaseries.entity.Episode;
import com.codecool.jpaseries.entity.Season;
import com.codecool.jpaseries.entity.Series;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@DataJpaTest
@ActiveProfiles("test")
class AllRepositoryTest {

    @Autowired
    private EpisodeRepository episodeRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SeasonRepositry seasonRepositry;

    @Autowired
    private SeriesRepository seriesRepistory;

    @Test
    public void saveOnSimple() {
        Episode universal = Episode.builder()
                .episodeName("Episode2")
                .build();
        episodeRepository.save(universal);

        List<Episode> episodes = episodeRepository.findAll();
        assertThat(episodes).hasSize(1);
    }

    @Test
    public void saveUniqueFieldTwice() {
        Episode universal = Episode.builder()
                .episodeName("Episode2")
                .build();
        episodeRepository.save(universal);

        Episode episode2 = Episode.builder()
                .episodeName("Episode2")
                .build();

        assertThrows(DataIntegrityViolationException.class, () -> {
            episodeRepository.saveAndFlush(episode2);
        });
    }

    @Test()
    public void nameShouldNotBeNull() {
        Episode universal = Episode.builder()
                .build();
        assertThrows(DataIntegrityViolationException.class, () -> {
            episodeRepository.save(universal);
        });
    }

    @Test
    public void transientIsNotSaved(){
        Episode episode2 = Episode.builder()
                .episodeName("Episode2")
                .releaseDate(LocalDate.of(1999,10,11))
                .build();
        episode2.calculateAge();
        assertThat(episode2.getAge()).isGreaterThanOrEqualTo(21);

        episodeRepository.save(episode2);
        entityManager.clear();

        List<Episode> episodes = episodeRepository.findAll();
        assertThat(episodes).allMatch(episode -> episode.getAge() == 0L);

    }

    @Test
    public void EpisodesArePersistantAndDeletedWithNewSeason (){
        Set<Episode> episodeSet = IntStream.range(1, 10)
                .boxed()
                .map(integer -> Episode.builder().episodeName("Episode" + integer).build())
                .collect(Collectors.toSet());

        Season first_season = Season.builder()
                .episodes(episodeSet)
                .numberOfEpisodes(episodeSet.size())
                .title("First Season")
                .build();

        seasonRepositry.save(first_season);

        assertThat(episodeRepository.findAll())
                .hasSize(9)
                .anyMatch(episode -> episode.getEpisodeName().equals("Episode9"));

        seasonRepositry.deleteAll();

        assertThat(episodeRepository.findAll()).hasSize(0);
    }

    @Test
    public void findByNameStartingWithOrReleaseDateBetween (){
        Episode episode2 = Episode.builder()
                .episodeName("FEpisode2")
                .build();

        Episode episode3 = Episode.builder()
                .episodeName("FEpisode3")
                .build();


        Episode episode5 = Episode.builder()
                .episodeName("Episode5")
                .releaseDate(LocalDate.of(2000,10,11))
                .build();

        Episode episode6 = Episode.builder()
                .episodeName("FEpisode6")
                .build();

        Episode episode4 = Episode.builder()
                .episodeName("Episode4")
                .releaseDate(LocalDate.of(2003,10,11))
                .build();

        episodeRepository.saveAll(Lists.newArrayList(episode2,episode3, episode4, episode5, episode6));

        List<Episode> e = episodeRepository.findByEpisodeNameStartingWithOrReleaseDateBetween("E",
                LocalDate.of(2000, 1, 1),
                LocalDate.of(2001, 1, 1));

        assertThat(e).containsExactlyInAnyOrder(episode5, episode4);
    }
    @Test
    public void findAllCountry(){
        Episode episode = Episode.builder()
                .episodeName("Episode1")
                .season(Season.builder().title("first").series(Series.builder().company(Company.PIXAR).build()).build())
                .build();
        Episode episode1 = Episode.builder()
                .episodeName("Episode2")
                .season(Season.builder().title("second").series(Series.builder().company(Company.DISNEY).build()).build())
                .build();
        Episode episode2 = Episode.builder()
                .episodeName("Episode3")
                .season(Season.builder().title("third").series(Series.builder().company(Company.MGM).build()).build())
                .build();
        Episode episode3 = Episode.builder()
                .episodeName("Episode4")
                .season(Season.builder().title("fourth").series(Series.builder().company(Company.PIXAR).build()).build())
                .build();

        episodeRepository.saveAll(Lists.newArrayList(episode, episode1, episode2,episode3));

        List<String> allCompany = episodeRepository.findAllCompany();

        assertThat(allCompany)
                .hasSize(3)
                .containsOnlyOnce("PIXAR", "DISNEY", "MGM");
    }
    @Test
    public void updateAllCompany(){
        Series pixar = Series.builder().company(Company.PIXAR).build();
        Series mgm = Series.builder().company(Company.MGM).build();
        Series disney = Series.builder().company(Company.DISNEY).build();
        Season first = Season.builder().title("first").build();
        first.setSeries(pixar);
        Episode episode1 = Episode.builder().episodeName("Episode1").build();
        episode1.setSeason(first);

        episodeRepository.save(episode1);
        seriesRepistory.save(mgm);
        seriesRepistory.save(disney);

        assertThat(seriesRepistory.findAll()).hasSize(3).noneMatch(series -> series.getCompany().equals(Company.UNIVERSAL));

        int updatedRows = seriesRepistory.updateAllToModified("Episode1");

        assertThat(updatedRows).isEqualTo(1);

        assertThat(seriesRepistory.findAll()).hasSize(3).anyMatch(series -> series.getCompany().equals(Company.UNIVERSAL));


    }


}