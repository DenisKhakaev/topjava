package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData {

    public static final LocalDateTime LOCAL_DATE_TIME = LocalDateTime.of(2020, Month.FEBRUARY, 24, 19, 30);
    public static final int MEAL_ID = 1;
    public static final int MEAL_ID_2 = 2;
    public static final Meal MEAL = new Meal(2, LOCAL_DATE_TIME, "Завтрак", 350);
    public static final Meal MEAL_1 = new Meal(1, LocalDateTime.parse("2020-02-24T19:30"), "Завтрак", 350);
    public static final Meal MEAL_2 = new Meal(2, LocalDateTime.parse("2020-02-24T20:30:00"), "Обед", 500);
    public static final Meal MEAL_3 = new Meal(3, LocalDateTime.parse("2020-02-24T20:35:00"), "Пельмени", 800);
    public static final Meal MEAL_4 = new Meal(4, LocalDateTime.parse("2020-02-24T20:40:00"), "Суп", 330);
    public static final Meal MEAL_5 = new Meal(5, LocalDateTime.parse("2020-02-24T22:30:00"), "Яблоко", 50);
    public static final Meal MEAL_6 = new Meal(6, LocalDateTime.parse("2020-02-26T22:30:00"), "Груша", 50);

    public static Meal getNew() {
        return new Meal(null, LOCAL_DATE_TIME.plusHours(1), "Обед", 500);
    }

    public static Meal getUpdate() {
        Meal updated = new Meal(MEAL);
        updated.setDateTime(LOCAL_DATE_TIME.plusDays(2));
        updated.setDescription("Второй завтрак");
        updated.setCalories(200);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "id");
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}
