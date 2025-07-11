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
import static org.apache.http.HttpStatus.*;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

public class LoginCourierTests extends BaseTest {

    private final CourierSteps courierSteps = new CourierSteps();
    private Courier courier;

    @Before
    public void setUp() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        courier = new Courier();
        courier.setLogin(RandomStringUtils.randomAlphabetic(11));
        courier.setPassword(RandomStringUtils.randomAlphabetic(12));
        courier.setFirstName(RandomStringUtils.randomAlphabetic(10));
        courierSteps.createCourier(courier);
    }

    @Test
    public void shouldLoginCourierTest() {

        courierSteps.loginCourier(courier)
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }

    @Test
    public void shouldNotLoginWithWrongPassword() {
        Courier wrongPasswordCourier = new Courier();
        wrongPasswordCourier.setLogin(courier.getLogin());
        wrongPasswordCourier.setPassword("wrong");
        courierSteps.loginCourier(wrongPasswordCourier)
                .statusCode(SC_NOT_FOUND)
                .body("message", containsString("Учетная запись не найдена"));
    }

    @Test
    public void shouldNotLoginWithoutLogin() {
        Courier noLoginCourier = new Courier();
        noLoginCourier.setLogin("");
        noLoginCourier.setPassword("applebanana");
        courierSteps.loginCourier(noLoginCourier)
                .statusCode(SC_BAD_REQUEST)
                .body("message", containsString("Недостаточно данных для входа"));
    }

    @Test
    public void shouldNotLoginWithoutPassword() {
        Courier noPasswordCourier = new Courier();
        noPasswordCourier.setLogin("natalieee");
        noPasswordCourier.setPassword("");
        courierSteps.loginCourier(noPasswordCourier)
                .statusCode(SC_BAD_REQUEST)
                .body("message", containsString("Недостаточно данных для входа"));
    }

    @Test
    public void shouldNotLoginNonExistentCourier() {
        Courier nonExistentCourier = new Courier();
        nonExistentCourier.setLogin("natalieee" + RandomStringUtils.randomAlphabetic(5));
        nonExistentCourier.setPassword("something");
        courierSteps.loginCourier(nonExistentCourier)
                .statusCode(SC_NOT_FOUND)
                .body("message", containsString("Учетная запись не найдена"));
    }

    @After
    public void tearDown() {
        ValidatableResponse loginResponse = courierSteps.loginCourier(courier);
        if (loginResponse.extract().statusCode() == SC_OK) {
            Integer id = loginResponse.extract().path("id");
            courier.setId(id);

            courierSteps.deleteCourier(courier);
        }
    }
}