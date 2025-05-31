package ru.dorofeev.api;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.commons.collections4.MapUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.dorofeev.ApplicationTest;
import ru.dorofeev.application.Application;

import java.util.Map;
import java.util.Objects;

@Testcontainers
@ActiveProfiles("test")
@Import(ApplicationTest.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
        classes = Application.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
public class BaseApiTest {

    @LocalServerPort
    private Integer port;

    public RequestSpecification requestSpecification;

    @BeforeAll
    public void initialize() {
        requestSpecification = new RequestSpecBuilder()
                .setBaseUri("http://localhost:" + port)
                .setContentType(MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @BeforeEach
    public void setFilterAllure() {
        RestAssured.filters(new AllureRestAssured());
    }

    public static ResponseSpecification specOkAndJsonType = new ResponseSpecBuilder()
            .expectContentType(MediaType.APPLICATION_JSON_VALUE)
            .expectStatusCode(HttpStatus.OK.value())
            .build();

    public static ResponseSpecification specBadRequestAndJsonType = new ResponseSpecBuilder()
            .expectContentType(MediaType.APPLICATION_JSON_VALUE)
            .expectStatusCode(HttpStatus.BAD_REQUEST.value())
            .build();

    public static ResponseSpecification specInternalServerErrorAndJsonType = new ResponseSpecBuilder()
            .expectContentType(MediaType.APPLICATION_JSON_VALUE)
            .expectStatusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .build();

    /**
     * Формирование запроса к API с логированием параметров.
     *
     * @param requestSpecification спецификация запроса.
     * @param path                 путь endpoint.
     * @param httpMethod           объект типа {@link HttpMethod}.
     * @param queryParameters      параметры запроса.
     * @param requestBody          тело запроса.
     * @return объект типа {@link Response}.
     */
    public Response makeRequest(
            @NotNull RequestSpecification requestSpecification,
            @NotNull String path,
            @NotNull HttpMethod httpMethod,
            @Nullable Map<String, Object> queryParameters,
            @Nullable Object requestBody
    ) {
        RequestSpecification currentRequest = RestAssured
                .given()
                .spec(requestSpecification)
                .basePath(path);

        if (MapUtils.isNotEmpty(queryParameters)) {
            currentRequest.queryParams(queryParameters);
        }

        if (Objects.nonNull(requestBody)) {
            currentRequest.body(requestBody);
        }

        currentRequest
                .log()
                .all()
                .when();

        if (HttpMethod.GET.equals(httpMethod)) {
            return currentRequest.get();
        } else if (HttpMethod.POST.equals(httpMethod)) {
            return currentRequest.post();
        } else if (HttpMethod.PUT.equals(httpMethod)) {
            return currentRequest.post();
        } else if (HttpMethod.DELETE.equals(httpMethod)) {
            return currentRequest.post();
        } else {
            throw new IllegalArgumentException(String.format("Передан неизвестный http-метод: '{%s}'!", httpMethod.name()));
        }
    }

    /**
     * Сформировать ответ указанной модели с логированием тела.
     *
     * @param response              объект типа {@link Response}.
     * @param responseSpecification спецификация ответа.
     * @param responseClass         модель ответа.
     * @return объект, приведенный к <T>.
     */
    public <T> T handleResponse(Response response, ResponseSpecification responseSpecification, Class<T> responseClass) {
        return response
                .then()
                .log()
                .body()
                .spec(responseSpecification)
                .extract()
                .as(responseClass);
    }
}
