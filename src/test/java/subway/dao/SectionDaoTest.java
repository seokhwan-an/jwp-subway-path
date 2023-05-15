package subway.dao;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;
import subway.entity.SectionEntity;
import subway.entity.StationEntity;

import javax.sql.DataSource;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.data.LineFixture.LINE2_ENTITY;

@JdbcTest
class SectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource dataSource;

    private SectionDao sectionDao;
    private StationDao stationDao;
    private LineDao lineDao;

    @BeforeEach
    void setUp() {
        sectionDao = new SectionDao(jdbcTemplate, dataSource);
        stationDao = new StationDao(jdbcTemplate, dataSource);
        lineDao = new LineDao(jdbcTemplate, dataSource);
    }

    @AfterEach
    void clear() {
        jdbcTemplate.execute("DELETE FROM SECTION");
        jdbcTemplate.execute("DELETE FROM STATION");
        jdbcTemplate.execute("DELETE FROM line");
    }

    @Test
    @DisplayName("구간 정보를 저장한다.")
    void section_data_insert() {
        // given
        LineEntity insertedLine = lineDao.insert(LINE2_ENTITY);

        StationEntity jamsil = new StationEntity("잠실", insertedLine.getId());
        StationEntity seolleung = new StationEntity("선릉", insertedLine.getId());
        StationEntity insertedJamsil = stationDao.insert(jamsil);
        StationEntity insertedSeolleung = stationDao.insert(seolleung);

        SectionEntity jamsilSeolleung = new SectionEntity(insertedJamsil.getId(), insertedSeolleung.getId(), insertedLine.getId(), 10);

        // when
        SectionEntity result = sectionDao.insert(jamsilSeolleung);

        // then
        assertThat(result.getLeftStationId()).isEqualTo(jamsilSeolleung.getLeftStationId());
        assertThat(result.getRightStationId()).isEqualTo(jamsilSeolleung.getRightStationId());
        assertThat(result.getLineId()).isEqualTo(jamsilSeolleung.getLineId());
        assertThat(result.getDistance()).isEqualTo(jamsilSeolleung.getDistance());
    }

    @Test
    @DisplayName("구간 정보를 불러온다.")
    void station_data_load() {
        // given
        LineEntity insertedLine = lineDao.insert(LINE2_ENTITY);

        StationEntity jamsil = new StationEntity("잠실", insertedLine.getId());
        StationEntity seolleung = new StationEntity("선릉", insertedLine.getId());
        StationEntity insertedJamsil = stationDao.insert(jamsil);
        StationEntity insertedSeolleung = stationDao.insert(seolleung);

        SectionEntity jamsilSeolleung = new SectionEntity(insertedJamsil.getId(), insertedSeolleung.getId(), insertedLine.getId(), 10);
        SectionEntity insertedJamsilSeolleung = sectionDao.insert(jamsilSeolleung);

        // when
        List<SectionEntity> result = sectionDao.findByLineId(insertedLine.getId());

        // then
        assertThat(result.get(0).getLeftStationId()).isEqualTo(insertedJamsilSeolleung.getLeftStationId());
        assertThat(result.get(0).getRightStationId()).isEqualTo(insertedJamsilSeolleung.getRightStationId());
        assertThat(result.get(0).getLineId()).isEqualTo(insertedJamsilSeolleung.getLineId());
        assertThat(result.get(0).getDistance()).isEqualTo(insertedJamsilSeolleung.getDistance());
    }
}