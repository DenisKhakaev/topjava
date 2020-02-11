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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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
        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");

        if (action != null) {
            String forward;
            switch (action) {
                case ("add"):
                    forward = INSERT_OR_EDIT;
                    break;
                case ("update"):
                    forward = INSERT_OR_EDIT;
                    request.setAttribute("meal", mealDao.getOne(getId(request)));
                    break;
                default:
                    request.setAttribute("listMeals", getMealToList());
                    forward = LIST_MEAL;
                    break;
            }
            request.getRequestDispatcher(forward).forward(request, response);
        } else {
            request.setAttribute("listMeals", getMealToList());
            request.getRequestDispatcher(LIST_MEAL).forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String action = Objects.requireNonNull(request.getParameter("action"));

        switch (action) {
            case ("delete"):
                mealDao.delete(getId(request));
                request.setAttribute("listMeals", getMealToList());
                break;
            case ("update"):
                mealDao.update(new Meal(getId(request), getDate(request), getDescription(request), getCalories(request)));
                request.setAttribute("listMeals", getMealToList());
                break;
            case ("add"):
                mealDao.add(new Meal(null, getDate(request), getDescription(request), getCalories(request)));
                request.setAttribute("listMeals", getMealToList());
                break;
            default:
                request.setAttribute("listMeals", getMealToList());
        }
        request.getRequestDispatcher(LIST_MEAL).forward(request, response);
    }

    private List<MealTo> getMealToList() {
        return MealsUtil.filteredByStreams(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
    }

    private Long getId(HttpServletRequest request) {
        return Long.parseLong(request.getParameter("id"));
    }

    private LocalDateTime getDate(HttpServletRequest request) {
        return LocalDateTime.parse(request.getParameter("date"));
    }

    private String getDescription(HttpServletRequest request) {
        return request.getParameter("description");
    }

    private Integer getCalories(HttpServletRequest request) {
        return Integer.parseInt(request.getParameter("calories"));
    }
}
