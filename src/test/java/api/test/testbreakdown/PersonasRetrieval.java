package api.test.testbreakdown;

import static org.junit.jupiter.api.Assertions.assertEquals;

import api.test.SwapiTest;
import api.test.dto.Config;
import api.test.dto.People;
import api.test.dto.Person;
import api.test.helpermodels.Constants;
import api.test.helpermodels.Validations;
import api.test.utils.TestUtils;
import com.xm.api.Endpoints;
import com.xm.api.RestClient;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class PersonasRetrieval {
    private static final Logger logger = LoggerFactory.getLogger(SwapiTest.class);
    RestClient restClient = new RestClient();
    TestUtils testUtils = new TestUtils();

    public void validateOldestPerson(Config config) {
        /*
            Since we want the persons only, we exclude robots and other creatures, we check the gender to be
            male/female. Also, we have the birth_year BBY and ABY (BC and AD equivalent). Note that we might
            have an unknown value
         */
        int pageIndex = 1;
        Map<String, String> nameAndAgeMap = new HashMap<>();

        while (pageIndex < Constants.MAX_PAGE_LIMIT) {
            var pageResults = restClient.get(config.getBaseUrl(),
                    String.format(Endpoints.PEOPLE_WITH_PAGINATION.getEndpoint(), pageIndex));

            assertEquals(HttpStatus.SC_OK, pageResults.getStatusCode());

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

        logger.info("The oldest person appeared in all films seems to be {}", oldestPersonInAllFilms);
        assertEquals(Validations.YODA.getName(), oldestPersonInAllFilms);
    }
}
