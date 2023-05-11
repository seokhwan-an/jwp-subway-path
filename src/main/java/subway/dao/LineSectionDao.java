package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.entity.LineSectionEntity;

@Repository
public class LineSectionDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;


    public LineSectionDao(final JdbcTemplate jdbcTemplate, final DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("line_section")
            .usingGeneratedKeyColumns("id");
    }

    public LineSectionEntity insert(final LineSectionEntity lineSectionEntity) {
        Map<String, Object> params = new HashMap<>();
        params.put("line_id", lineSectionEntity.getLineId());
        params.put("section_id", lineSectionEntity.getSectionId());

        Long lineSectionId = insertAction.executeAndReturnKey(params).longValue();
        return new LineSectionEntity(lineSectionId, lineSectionEntity.getLineId(), lineSectionEntity.getSectionId());
    }

    public void deleteBySectionId(final Long sectionId) {
        String sql = "DELETE FROM LINE_SECTION WHERE section_id = ?";
        jdbcTemplate.update(sql, sectionId);
    }

    public void deleteByLineId(final Long lineId) {
        String sql = "DELETE FROM LINE_SECTION WHERE line_id = ?";
        jdbcTemplate.update(sql, lineId);
    }

    public List<LineSectionEntity> findByLineId(final Long id) {
        String sql =
            "SELECT ID, LINE_ID, SECTION_ID FROM LINE_SECTION WHERE LINE_ID = ?";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            return new LineSectionEntity(
                rs.getLong("ID"),
                rs.getLong("LINE_ID"),
                rs.getLong("SECTION_ID"));
        }, id);
    }
}
