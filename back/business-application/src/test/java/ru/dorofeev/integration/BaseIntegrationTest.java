package ru.dorofeev.integration;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import ru.dorofeev.ApplicationTest;
import ru.dorofeev.application.Application;

@ActiveProfiles("test")
@Import(ApplicationTest.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class BaseIntegrationTest {

}
