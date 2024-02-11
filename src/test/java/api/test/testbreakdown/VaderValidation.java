package api.test.testbreakdown;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import api.test.SwapiTest;
import api.test.dto.Config;
import api.test.dto.Film;
import api.test.dto.People;
import api.test.dto.Person;
import api.test.helpermodels.Validations;
import api.test.utils.TestUtils;
import com.xm.api.Endpoints;
import com.xm.api.RestClient;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class VaderValidation {
    private static final Logger logger = LoggerFactory.getLogger(SwapiTest.class);
    RestClient restClient = new RestClient();
    TestUtils testUtils = new TestUtils();

    public void validateVaderInformation(Config config) {
        var vader = restClient.get(config.getBaseUrl(),
                String.format(Endpoints.PEOPLE.getEndpoint(), Validations.VADER));
        Person vaderResponseBody = vader.getBody().as(People.class).getResults().get(0);

        logger.info("The following persona has been found looking for Vader: {}", vaderResponseBody);

        assertAll(() -> {
            assertEquals(HttpStatus.SC_OK, vader.getStatusCode());
            assertTrue(vaderResponseBody.getName().contains(Validations.VADER.getName()));
        });

        List<String> vaderFilms = vaderResponseBody.getFilms();
        var vaderFilmWithLeastPlanets = restClient.get(testUtils
                        .getFilmWithMinimumPlanets(vaderFilms))
                .getBody()
                .as(Film.class);

        var filmTitle = vaderFilmWithLeastPlanets.getTitle();

        logger.info("The film title in which Vader played and has the least planets {}", filmTitle);
        assertNotNull(filmTitle);

        List<String> filmStarships = vaderFilmWithLeastPlanets.getStarships();

        // according to the assignment's description, we assume that we have only 1 starship
        Optional<String> firstStarship = vaderResponseBody.getStarships()
                .stream()
                .findFirst()
                .map(Object::toString);

        String vaderStarship = firstStarship.orElse("No Starship Found");

        logger.info("It seems that Vader's starship is {}", vaderStarship);
        assertTrue(filmStarships.contains(vaderStarship));
    }
}
