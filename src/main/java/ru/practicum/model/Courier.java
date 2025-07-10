package ru.practicum.model;


public class Courier {
    private String login;
    private String password;
    private String firstName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    private Integer id;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

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
