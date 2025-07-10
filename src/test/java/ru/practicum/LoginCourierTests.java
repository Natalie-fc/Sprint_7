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
    }

    @Test
    public void shouldLoginCourierTest() {

        courierSteps.createCourier(courier);
        courierSteps.loginCourier(courier)
                .statusCode(200)
                .body("id", notNullValue());
    }

    @Test
    public void shouldNotLoginWithWrongPassword() {
        courierSteps.createCourier(courier);
        courier.setPassword("wrong");
        courierSteps.loginCourier(courier)
                .statusCode(404)
                .body("message", containsString("Учетная запись не найдена"));
    }

    @Test
    public void shouldNotLoginWithoutLogin() {
        courier.setLogin("");
        courierSteps.loginCourier(courier)
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для входа"));
    }

    @Test
    public void shouldNotLoginWithoutPassword() {
        courier.setPassword("");
        courierSteps.loginCourier(courier)
                .statusCode(400)
                .body("message", containsString("Недостаточно данных для входа"));
    }

    @Test
    public void shouldNotLoginNonExistentCourier() {
        courier.setLogin("natalieee" + RandomStringUtils.randomAlphabetic(5));
        courier.setPassword("something");
        courierSteps.loginCourier(courier)
                .statusCode(404)
                .body("message", containsString("Учетная запись не найдена"));
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