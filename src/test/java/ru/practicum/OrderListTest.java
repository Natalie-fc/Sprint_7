package ru.practicum;

import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;
import org.junit.Test;
import ru.practicum.steps.OrderSteps;

import static org.hamcrest.Matchers.notNullValue;

public class OrderListTest extends BaseTest {

    private final OrderSteps orderSteps = new OrderSteps();

    @Before
    @Step("Добавляем фильтры логирования RestAssured")
    public void logFilters() {
        io.restassured.RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
    }

    @Test
    @Step("Список заказов")
    @DisplayName("Получение списка заказов")
    @Description("Проверяет, что при GET-запросе возвращается список заказов в теле ответа")
    public void shouldReturnListOfOrders() {
        orderSteps.getOrderList()
                .statusCode(200)
                .body("orders", notNullValue());
    }
}
