package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
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

    public List<MealTo> getAllFilter(int userId, LocalDate startDate, LocalTime startTime, LocalDate finishDate, LocalTime finishTime, int calories) {
        return getFilterByTime(MealsUtil.getTos(getAll(userId), calories), startDate, startTime, finishDate, finishTime);
    }

    private List<MealTo> getFilterByTime(List<MealTo> mealToList, LocalDate startDate, LocalTime startTime, LocalDate finishDate, LocalTime finishTime) {
        LocalDate finalStartDate = startDate == null ? LocalDate.MIN : startDate;
        LocalTime finalStartTime = startTime == null ? LocalTime.MIN : startTime;
        LocalDate finalFinishDate = finishDate == null ? LocalDate.MAX : finishDate;
        LocalTime finalFinishTime = finishTime == null ? LocalTime.MAX : finishTime;
        return mealToList.stream()
                .filter(meal -> DateTimeUtil.isBetweenDate(meal.getDateTime(), finalStartDate, finalFinishDate, finalStartTime, finalFinishTime))
                .collect(Collectors.toList());
    }
}