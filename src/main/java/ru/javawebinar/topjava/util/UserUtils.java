package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.Arrays;
import java.util.List;

public class UserUtils {

    public static final List<User> USERS = Arrays.asList(
            new User("User_1", "us1@mail.ru", "pass", Role.ROLE_ADMIN),
            new User("User_2", "us2@mail.ru", "pass1", Role.ROLE_USER),
            new User("User_3", "us3@mail.ru", "pass2", Role.ROLE_USER),
            new User("User_4", "us4@mail.ru", "pass3", Role.ROLE_USER)
    );

}
