package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository repository) {
        this.repository = repository;
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id, userId);
    }

    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id, userId);
    }

    public List<Meal> getAll(int userId) {
        return repository.getAll(userId);
    }

    public void update(Meal user, int userId) {
        checkNotFoundWithId(repository.save(user, userId), user.getId(), userId);
    }

    public List<MealTo> getAllFilter(int userId, LocalDateTime startTime, LocalDateTime finishTime, int calories) {
        return getFilterByTime(MealsUtil.getTos(getAll(userId), calories), startTime, finishTime);
    }

    private List<MealTo> getFilterByTime(List<MealTo> mealToList, LocalDateTime startTime, LocalDateTime finishTime) {
        if (startTime != null) {
            mealToList = mealToList.stream().filter(e -> e.getDateTime().isAfter(startTime)).collect(Collectors.toList());
        }
        if (finishTime != null) {
            mealToList = mealToList.stream().filter(e -> e.getDateTime().isBefore(finishTime)).collect(Collectors.toList());
        }
        return mealToList;
    }
}