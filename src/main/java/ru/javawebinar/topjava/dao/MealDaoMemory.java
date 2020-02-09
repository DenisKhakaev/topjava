package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.LongAdder;

public class MealDaoMemory implements MealDao {

    private LongAdder counter = new LongAdder();
    private ConcurrentMap<Long, Meal> meals = new ConcurrentHashMap<>();

    @Override
    public Meal getOne(Long id) {
        return meals.get(id);
    }

    @Override
    public boolean add(Meal meal) {
        counter.increment();
        return meals.put(counter.sum(), new Meal(counter.sum(), meal.getDateTime(), meal.getDescription(), meal.getCalories())) != null;
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
