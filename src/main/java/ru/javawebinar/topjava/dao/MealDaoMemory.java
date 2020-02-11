package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class MealDaoMemory implements MealDao {

    private ConcurrentMap<Long, Meal> meals = new ConcurrentHashMap<>();
    private AtomicLong counter = new AtomicLong();

    @Override
    public Meal getOne(Long id) {
        return meals.get(id);
    }

    @Override
    public boolean add(Meal meal) {
        Long id = counter.addAndGet(1L);
        return meals.put(id, new Meal(id, meal.getDateTime(), meal.getDescription(), meal.getCalories())) != null;
    }

    @Override
    public boolean delete(Long id) {
        return meals.remove(id) != null;
    }

    @Override
    public boolean update(Meal meal) {
        return meals.replace(meal.getId(), meal) != null;
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(meals.values());
    }
}
