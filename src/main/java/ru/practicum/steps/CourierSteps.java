package ru.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.practicum.model.Courier;

import static io.restassured.RestAssured.given;

public class CourierSteps {

    private static String COURIER = "/api/v1/courier/";
    private static String LOGIN_COURIER = "/api/v1/courier/login";

    @Step("Создание курьера с логином: {courier.login}")
    public ValidatableResponse createCourier(Courier courier) {

        return given()
                .body(courier)
                .when()
                .post(COURIER)
                .then();
    }

    @Step("Авторизация курьера с логином: {courier.login}")
    public ValidatableResponse loginCourier(Courier courier) {

        return given()
                .body(courier)
                .when()
                .post(LOGIN_COURIER)
                .then();
    }

    @Step("Удаление курьера с ID: {courier.id}")
    public ValidatableResponse deleteCourier(Courier courier) {
        return given()
                .pathParams("id", courier.getId())
                .when()
                .delete(COURIER + "{id}")
                .then();

    }
}
