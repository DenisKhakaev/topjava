package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealDao {

    Meal getOne(Long id);

    boolean add(Meal meal);

    boolean delete(Long id);

    boolean update(Meal meal);

    List<Meal> getAll();
}
