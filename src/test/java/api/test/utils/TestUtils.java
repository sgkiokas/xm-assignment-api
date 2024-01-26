package api.test.utils;

import api.test.dto.Person;
import api.test.helpermodels.Gender;
import com.xm.api.RestClient;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TestUtils {

    private static final String FILM_PLANETS = "planets";
    private static final String BBY = "BBY";
    RestClient restClient = new RestClient();

    public String getFilmWithMinimumPlanets(List<String> films) {
        return films.stream()
                .min(this::comparePlanetsCount)
                .orElse(null);
    }

    public int comparePlanetsCount(String firstFilm, String secondFilm) {
        int firstPlanetCount = getPlanetCount(firstFilm);
        int secondPlanetCount = getPlanetCount(secondFilm);
        return Integer.compare(firstPlanetCount, secondPlanetCount);
    }

    public int getPlanetCount(String film) {
        var filmDetails = restClient.get(film);
        return filmDetails.getBody().jsonPath().getList(FILM_PLANETS).size();
    }

    public boolean isDesiredGender(Person person) {
        Object gender = person.getGender();
        return (Gender.MALE.getGender().equals(gender) || Gender.FEMALE.getGender().equals(gender));
    }

    public double calculateAge(String birthYear) {
        try {
            double timeIdentifier = Double.parseDouble(birthYear.substring(0, birthYear.length() - 3));

            // Assuming BBY is BC and ABY is AD
            return Math.abs(birthYear.endsWith(BBY) ? - timeIdentifier : timeIdentifier);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public String getOldestPerson(Map<String, String> nameAgeMap) {
        Optional<Map.Entry<String, String>> oldestPersonEntry = nameAgeMap.entrySet().stream()
                .max(Comparator.comparingDouble(entry -> calculateAge(entry.getValue())));

        return oldestPersonEntry.map(Map.Entry::getKey).orElse(null);
    }
}
