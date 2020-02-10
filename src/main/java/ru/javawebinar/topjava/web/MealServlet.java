package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDao;
import ru.javawebinar.topjava.dao.MealDaoMemory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger log = getLogger(UserServlet.class);
    private static String INSERT_OR_EDIT = "/meal.jsp";
    private static String LIST_MEAL = "/meals.jsp";
    private MealDao mealDao;

    @Override
    public void init(ServletConfig config) throws ServletException {
        log.debug("Init meal servlet");
        super.init(config);
        mealDao = new MealDaoMemory();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");

        String forward = "";
        String action = request.getParameter("action");

        if (action.equalsIgnoreCase("delete")) {
            Long mealId = Long.parseLong(request.getParameter("id"));
            mealDao.delete(mealId);
            forward = LIST_MEAL;
            request.setAttribute("listMeals", getMealToListSortByDate());
        } else if (action.equalsIgnoreCase("edit")) {
            forward = INSERT_OR_EDIT;
            Long mealId = Long.parseLong(request.getParameter("id"));
            Meal meal = mealDao.getOne(mealId);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("listMeals")) {
            forward = LIST_MEAL;
            request.setAttribute("listMeals", getMealToListSortByDate());
        } else if (action.equalsIgnoreCase("add")) {
            forward = INSERT_OR_EDIT;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String mealId = req.getParameter("id");
        Meal meal;
        LocalDateTime date = LocalDate.parse(req.getParameter("date")).atStartOfDay();
        if (mealId == null || mealId.isEmpty()) {
            meal = new Meal(null, date, req.getParameter("description"),
                    Integer.parseInt(req.getParameter("calories")));
            mealDao.add(meal);
        } else {
            meal = new Meal(Long.parseLong(mealId), date, req.getParameter("description"),
                    Integer.parseInt(req.getParameter("calories")));
            mealDao.update(meal);
        }
        RequestDispatcher view = req.getRequestDispatcher(LIST_MEAL);
        req.setAttribute("listMeals", getMealToListSortByDate());
        view.forward(req, resp);
    }

    private List<MealTo> getMealToListSortByDate() {
        List<MealTo> mealToList = MealsUtil.filteredByStreams(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
        return mealToList == null ? mealToList : mealToList.stream().sorted(Comparator.comparing(MealTo::getDateTime))
                .collect(Collectors.toList());
    }
}
