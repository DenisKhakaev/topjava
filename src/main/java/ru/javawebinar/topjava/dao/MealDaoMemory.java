package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MealDaoMemory implements MealDao {

    private ConcurrentMap<Long, Meal> meals;
    private AtomicLong counter;

    public MealDaoMemory() {
        counter = new AtomicLong();
        meals = new ConcurrentHashMap(MealsUtil.getStartListMeals().stream()
                .collect(Collectors.toMap(Meal::getId, meal -> meal)));
        counter.set(((ConcurrentHashMap<Long, Meal>) meals).mappingCount());
    }

    @Override
    public Meal getOne(Long id) {
        return meals.get(id);
    }

    @Override
    public boolean add(Meal meal) {
        Long id = counter.incrementAndGet();
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
