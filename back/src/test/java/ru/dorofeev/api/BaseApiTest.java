package ru.dorofeev.api;

import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.dorofeev.ApplicationTest;
import ru.dorofeev.application.Application;

@Testcontainers
@ActiveProfiles("test")
@Import(ApplicationTest.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class BaseApiTest {

}
