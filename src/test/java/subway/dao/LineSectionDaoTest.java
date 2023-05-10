package subway.dao;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import subway.entity.LineSection;

@JdbcTest
class LineSectionDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private DataSource dataSource;
    private LineSectionDao lineSectionDao;

    @BeforeEach
    void setUp() {
        lineSectionDao = new LineSectionDao(jdbcTemplate, dataSource);
    }

    @Test
    @DisplayName("Line_Section 테이블 삽입")
    void line_section_insert() {
        // given
        LineSection lineSection = new LineSection(1L, 1L);

        // when
        LineSection result = lineSectionDao.insert(lineSection);

        // then
        assertThat(result.getSectionId()).isEqualTo(lineSection.getSectionId());
        assertThat(result.getLineId()).isEqualTo(lineSection.getLineId());
    }
}