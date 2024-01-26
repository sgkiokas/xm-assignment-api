package api.test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import api.test.dto.Film;
import api.test.dto.People;
import api.test.dto.Person;
import api.test.helpermodels.Constants;
import api.test.helpermodels.Validations;
import api.test.utils.TestUtils;
import com.xm.api.Endpoints;
import com.xm.api.RestClient;
import com.xm.utils.BaseUrls;
import io.restassured.module.jsv.JsonSchemaValidator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Test;

public class SwapiTest {

    RestClient restClient = new RestClient();
    TestUtils testUtils = new TestUtils();
    Logger logger = LogManager.getLogger();

    @Test
    public void testVaderInformation() {
        var vader = restClient.get(BaseUrls.SWAPI.getBaseUrl(),
                String.format(Endpoints.PEOPLE.getEndpoint(), Validations.VADER));
        Person vaderResponseBody = vader.getBody().as(People.class).getResults().get(0);

        assertAll(() -> {
            assertThat(vader.getStatusCode()).isEqualTo(HttpStatus.SC_OK);
            assertThat(vaderResponseBody.getName()).contains(Validations.VADER.getName());
        });

        List<String> vaderFilms = vaderResponseBody.getFilms();
        var vaderFilmWithLeastPlanets = restClient.get(testUtils
                .getFilmWithMinimumPlanets(vaderFilms))
                .getBody()
                .as(Film.class);

        var filmTitle = vaderFilmWithLeastPlanets.getTitle();
        assertThat(filmTitle).isNotNull();

        List<String> filmStarships = vaderFilmWithLeastPlanets.getStarships();

        // according to the assignment's description, we assume that we have only 1 starship
        Optional<String> firstStarship = vaderResponseBody.getStarships()
                .stream()
                .findFirst()
                .map(Object::toString);

        String vaderStarship = firstStarship.orElse("No Starship Found");
        assertTrue(filmStarships.contains(vaderStarship));
    }

    @Test
    public void testOldestPerson() {
        /*
            Since we want the persons only, we exclude robots and other creatures, we check the gender to be
            male/female. Also, we have the birth_year BBY and ABY (BC and AD equivalent). Note that we might have an unknown value
         */
        int pageIndex = 1;
        Map<String, String> nameAndAgeMap = new HashMap<>();

        while (pageIndex < Constants.MAX_PAGE_LIMIT) {
            var pageResults = restClient.get(BaseUrls.SWAPI.getBaseUrl(),
                    String.format(Endpoints.PEOPLE_WITH_PAGINATION.getEndpoint(), pageIndex));

            assertThat(pageResults.getStatusCode()).isEqualTo(HttpStatus.SC_OK);

            List<Person> peopleList = pageResults.getBody().as(People.class).getResults();

            nameAndAgeMap.putAll(peopleList.stream()
                    .filter(testUtils::isDesiredGender)
                    .filter(person -> !Constants.UNKNOWN_BIRTH_YEAR.equals(person.getBirth_year()))
                    .collect(Collectors.toMap(
                            Person::getName,
                            Person::getBirth_year)
                    ));

            String nextPage = pageResults.getBody().as(People.class).getNext();
            if (nextPage == null) {
                break;
            }

            pageIndex++;
        }

        String oldestPersonInAllFilms = testUtils.getOldestPerson(nameAndAgeMap);

        assertThat(oldestPersonInAllFilms).isEqualTo(Validations.YODA.getName());
        logger.info("The oldest person appeared in all films is {}", oldestPersonInAllFilms);
    }

    @Test
    public void testPeopleResourceSchema() {
        /*
           What is suggested in the documentation (fetching the schema via /api/<resource>/schema) is not working. So,
           curling /people/schema/ to get the schema is not an option. Hence, the schemas were generated from responses
           of the API.
         */
        var allPeople = restClient.get(BaseUrls.SWAPI.getBaseUrl(), Endpoints.ALL_PEOPLE.getEndpoint());
        allPeople
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/peopleSchema.json"));

        // picking the first person
        int personId = 1;
        var singlePerson = restClient.get(BaseUrls.SWAPI.getBaseUrl(),
                String.format(Endpoints.PEOPLE_ID.getEndpoint(), personId));

        singlePerson
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/peopleIdSchema.json"));
    }
}
