package ru.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import ru.practicum.model.Order;
import ru.practicum.steps.OrderSteps;
import static org.apache.http.HttpStatus.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderTests extends BaseTest {

    private final OrderSteps orderSteps = new OrderSteps();
    private final Order order;
    private Integer track;

    public OrderTests(Order order) {
        this.order = order;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getTestData() {
        return Arrays.asList(new Object[][]{
                {new Order("Михаил", "Вальнер", "Ленина, д.2", "4", "+745665478998", 5, "2025-07-10", "Позвонить заранее", Collections.singletonList("BLACK"))},
                {new Order("Михаил", "Вальнер", "Ленина, д.2", "4", "+745665478998", 5, "2025-07-10", "Позвонить заранее", Collections.singletonList("GREY"))},
                {new Order("Михаил", "Вальнер", "Ленина, д.2", "4", "+745665478998", 5, "2025-07-10", "Позвонить заранее", Arrays.asList("BLACK", "GREY"))},
                {new Order("Михаил", "Вальнер", "Ленина, д.2", "4", "+745665478998", 5, "2025-07-10", "Позвонить заранее", Collections.emptyList())},
        });
    }

    @Before
    @Step("Устанавливаем логгеры RestAssured")
    public void logFilters() {
        io.restassured.RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    @Step("Создание заказа с параметрами")
    @DisplayName("Создание заказа с цветом: BLACK, GREY или без цвета")
    @Description("Проверяет, что заказ можно создать с разными значениями цвета и возвращает track")
    public void shouldCreateOrderWithVariousColorOptions() {
        ValidatableResponse response =
        orderSteps
                .createOrder(order)
                .statusCode(SC_CREATED)
                .body("track", notNullValue());
        track = response.extract().path("track");
    }

    @After
    public void tearDown() {
        if (track != null) {
            orderSteps.cancelOrder(track);
        }
    }
}
