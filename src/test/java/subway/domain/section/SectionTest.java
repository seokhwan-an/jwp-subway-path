package subway.domain.section;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import subway.domain.station.Station;

class SectionTest {

    @ParameterizedTest
    @MethodSource("createStation")
    @DisplayName("간선에서 정점이 null일 수는 없다.")
    void validate_both_stations_is_null(Station leftStation, Station rightStation) {
        //given
        Long id = 1L;
        int distance = 10;

        // when + then
        assertThatThrownBy(() -> new Section(id, leftStation, rightStation, distance))
            .isInstanceOf(IllegalArgumentException.class);
    }

    private static Stream<Arguments> createStation() {
        return Stream.of(
            Arguments.arguments(new Station(1L, "잠실역"), null),
            Arguments.arguments(null, new Station(1L, "잠실역")),
            Arguments.arguments(null, null)
        );
    }

    @Test
    @DisplayName("두 역과 거리가 주어지면 생성된다.")
    void generate_section_success() {
        //given
        Long id = 1L;
        Station leftStation = new Station(1L, "잠실역");
        Station rightStation = new Station(1L, "강남역");
        int distance = 10;

        // when + then
        assertDoesNotThrow(() -> new Section(id, leftStation, rightStation, distance));
    }
}
