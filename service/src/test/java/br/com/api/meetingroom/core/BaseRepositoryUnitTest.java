package br.com.api.meetingroom.core;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
public class BaseRepositoryUnitTest {

    @Autowired
    private Flyway flyway;

    @BeforeEach
    void setup() {
        setupFlyway();
    }

    private void setupFlyway() {
        flyway.clean();
        flyway.migrate();
    }


}
