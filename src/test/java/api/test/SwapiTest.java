package api.test;

import api.test.dto.Config;
import api.test.testbreakdown.PersonasRetrieval;
import api.test.testbreakdown.SchemaValidation;
import api.test.testbreakdown.VaderValidation;
import api.test.utils.ConfigRetrieval;
import java.util.Objects;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class SwapiTest {


    private static Config config;
    private final SchemaValidation schemaValidation = new SchemaValidation();
    private final PersonasRetrieval personasRetrieval = new PersonasRetrieval();
    private final VaderValidation vaderValidation = new VaderValidation();


    @BeforeAll
    public static void setUp() {
        config = Objects.requireNonNull(ConfigRetrieval.retrieve());
    }

    @Test
    public void testVaderInformation() {
        vaderValidation.validateVaderInformation(config);
    }

    @Test
    public void testOldestPerson() {
        personasRetrieval.validateOldestPerson(config);
    }

    @Test
    public void testPeopleResourceSchema() {
        schemaValidation.runSchemaValidation(config);
    }
}
