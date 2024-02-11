package api.test.testbreakdown;

import api.test.dto.Config;
import com.xm.api.Endpoints;
import com.xm.api.RestClient;
import io.restassured.module.jsv.JsonSchemaValidator;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SchemaValidation {
    RestClient restClient = new RestClient();

    public void runSchemaValidation(Config config) {
        /*
           What is suggested in the documentation (fetching the schema via /api/<resource>/schema) is not working. So,
           curling /people/schema/ to get the schema is not an option. Hence, the schemas were generated from responses
           of the API.
         */
        var allPeople = restClient.get(config.getBaseUrl(), Endpoints.ALL_PEOPLE.getEndpoint());
        allPeople
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/peopleSchema.json"));

        // picking the first person
        int personId = 1;
        var singlePerson = restClient.get(config.getBaseUrl(),
                String.format(Endpoints.PEOPLE_ID.getEndpoint(), personId));

        singlePerson
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/peopleIdSchema.json"));
    }
}
