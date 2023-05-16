package subway.application;

import org.springframework.stereotype.Service;
import subway.dao.LineDao;
import subway.domain.line.Line;
import subway.dto.LineRequest;
import subway.dto.LineResponse;
import subway.dto.StationResponse;
import subway.entity.StationEntity;
import subway.repository.LineRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LineService {
    private final LineRepository lineRepository;

    public LineService(final LineRepository lineRepository) {
        this.lineRepository = lineRepository;
    }

    public LineResponse saveLine(LineRequest request) {
        Line persistLine = lineRepository.save(new Line(request.getName(), request.getColor()));
        return LineResponse.of(persistLine);
    }

    public LineResponse readLine(Long id) {
        Line line = lineRepository.findById(id);
        List<StationResponse> stations = line.sortStation().stream()
                .map(station -> new StationResponse(station.getId(), station.getName()))
                .collect(Collectors.toList());

        return new LineResponse(line.getId(), line.getName(), line.getColor(), stations);
    }
}
