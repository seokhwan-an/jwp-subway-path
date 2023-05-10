package subway.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import subway.domain.line.Line;
import subway.domain.section.Sections;
import subway.domain.station.Stations;

@Repository
public class LineDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert insertAction;

    private RowMapper<Line> rowMapper = (rs, rowNum) ->
        new Line(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("color"),
            new Stations(List.of()),
            new Sections(List.of())
        );

    public LineDao(JdbcTemplate jdbcTemplate, DataSource dataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.insertAction = new SimpleJdbcInsert(dataSource)
            .withTableName("line")
            .usingGeneratedKeyColumns("id");
    }

    public Line insert(Line line) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", line.getId());
        params.put("name", line.getName());
        params.put("color", line.getColor());
        params.put("upbound_station_id", null);
        params.put("downbound_station_id", null);

        Long lineId = insertAction.executeAndReturnKey(params).longValue();
        return new Line(lineId, line.getName(), line.getColor(), new Stations(List.of()), new Sections(List.of()));
    }

    public List<Line> findAll() {
        String sql = "select id, name, color from LINE";
        return jdbcTemplate.query(sql, rowMapper);
    }

    public Line findById(Long id) {
        String sql = "select id, name, color from LINE WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, rowMapper, id);
    }

    public Optional<Line> findByName(final String name) {
        String sql = "SELECT id, name, color FROM LINE WHERE name = ?";
        try {
            return Optional.of(jdbcTemplate.queryForObject(sql, rowMapper , name));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public void update(Line newLine) {
        String sql = "update LINE set  upbound_station_id= ?, downbound_station_id = ? where id = ?";
        jdbcTemplate.update(sql, new Object[]{newLine.getUpBoundStation().getId(), newLine.getDownBoundStation().getId(), newLine.getId()});
    }

    public void deleteById(Long id) {
        jdbcTemplate.update("delete from Line where id = ?", id);
    }
}
