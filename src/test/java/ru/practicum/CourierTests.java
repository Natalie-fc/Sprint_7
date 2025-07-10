package ru.practicum;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.practicum.model.Courier;
import ru.practicum.steps.CourierSteps;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;


public class CourierTests extends BaseTest {

    private final CourierSteps courierSteps = new CourierSteps();
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        courier = new Courier();
        courier.setLogin(RandomStringUtils.randomAlphabetic(11));
        courier.setPassword(RandomStringUtils.randomAlphabetic(12));
        courier.setFirstName(RandomStringUtils.randomAlphabetic(10));
    }

    @Test
    public void shouldCreateCourierTest() {

        courierSteps
                .createCourier(courier)
                .statusCode(201)
                .body("ok", is(true));
    }

    @Test
    public void shouldNotAllowToCreateDuplicateCourier() {

        courierSteps
                .createCourier(courier)
                .statusCode(201);
        courierSteps
                .createCourier(courier)
                .statusCode(409)
                .body("message", containsString("Этот логин уже используется"));
    }

    @Test
    public void shouldNotCreateCourierWithoutLogin() {
        Courier courierWithoutLogin = Courier.withoutLogin();

        courierSteps.createCourier(courierWithoutLogin)
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для создания учетной записи"));
    }

    @Test
    public void shouldNotCreateCourierWithoutPassword() {
        Courier courierWithoutPassword = Courier.withoutPassword();

        courierSteps.createCourier(courierWithoutPassword)
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для создания учетной записи"));
    }

    @After
    public void tearDown() {
        ValidatableResponse loginResponse = courierSteps.loginCourier(courier);

        if (loginResponse.extract().statusCode() == 200) {
            Integer id = loginResponse.extract().path("id");

            courier.setId(id);
            courierSteps.deleteCourier(courier);
        }
    }
}

