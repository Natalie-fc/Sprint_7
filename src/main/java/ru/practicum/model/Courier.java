package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Courier {
    private String login;
    private String password;
    private String firstName;
    private Integer id;


    public static Courier withoutLogin() {
        Courier courier = new Courier();
        courier.setPassword("12345");
        courier.setFirstName("Natalya");
        return courier;
    }

    public static Courier withoutPassword() {
        Courier courier = new Courier();
        courier.setLogin("vassiliy");
        courier.setFirstName("Vassya");
        return courier;
    }
}
