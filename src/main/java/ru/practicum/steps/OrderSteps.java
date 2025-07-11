package ru.practicum.steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import ru.practicum.model.Order;
import static io.restassured.RestAssured.given;


public class OrderSteps {
    private final String ORDER = "/api/v1/orders";

    @Step("Создание заказа")
    public ValidatableResponse createOrder(Order order) {
        return given()
                .body(order)
                .when()
                .post(ORDER)
                .then();
    }

    @Step("Получение списка заказов")
    public ValidatableResponse getOrderList() {
        return given()
                .when()
                .get(ORDER)
                .then();
    }

    @Step("Отмена заказа по треку: {track}")
    public ValidatableResponse cancelOrder(int track) {
        return given()
                .header("Content-type", "application/json")
                .body("{\"track\": " + track + "}")
                .when()
                .put("/api/v1/orders/cancel")
                .then();
    }
}
