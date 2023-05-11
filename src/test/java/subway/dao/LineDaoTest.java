package subway.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static subway.data.LineFixture.LINE_2;

import java.util.Optional;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineEntity;

@JdbcTest
class LineDaoTest {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    DataSource dataSource;

    LineDao lineDao;

    @BeforeEach
    void setUp() {
        lineDao = new LineDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("노선을 최초 등록한다.")
    void insert() {
        // given

        // when
        LineEntity result = lineDao.insert(LINE_2);

        // then
        assertThat(result.getName()).isEqualTo(LINE_2.getName());
        assertThat(result.getColor()).isEqualTo(LINE_2.getColor());
    }

    @Test
    @DisplayName("노선을 이름으로 찾는다.")
    void findByName() {
        // given
        LineEntity insertedLine = lineDao.insert(LINE_2);

        // when
        Optional<LineEntity> result = lineDao.findByName(insertedLine.getName());

        // then
        assertThat(result.get().getName()).isEqualTo(insertedLine.getName());
        assertThat(result.get().getColor()).isEqualTo(insertedLine.getColor());
    }
}