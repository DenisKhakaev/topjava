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
        mealDao.add(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500));
        mealDao.add(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000));
        mealDao.add(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500));
        mealDao.add(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100));
        mealDao.add(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000));
        mealDao.add(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500));
        mealDao.add(new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("redirect to meals");
        request.setCharacterEncoding("UTF-8");
        String action;
        action = Objects.requireNonNull(request.getParameter("action"));
        String forward;
        switch (action) {
            case ("add"):
                forward = INSERT_OR_EDIT;
                break;
            default:
                request.setAttribute("listMeals", getMealToList());
                forward = LIST_MEAL;
                break;
        }

        RequestDispatcher view = request.getRequestDispatcher(forward);
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String mealId = Objects.requireNonNull(req.getParameter("id"));
        String action = Objects.requireNonNull(req.getParameter("action"));

        Meal meal;
        String forward;
        LocalDateTime date;
        switch (action) {
            case ("delete"):
                forward = LIST_MEAL;
                mealDao.delete(Long.parseLong(mealId));
                req.setAttribute("listMeals", getMealToList());
                break;
            case ("edit"):
                forward = INSERT_OR_EDIT;
                meal = mealDao.getOne(Long.parseLong(mealId));
                req.setAttribute("meal", meal);
                break;
            case ("update"):
                forward = LIST_MEAL;
                if (mealId.equals("") || mealId.isEmpty()) {
                    date = LocalDateTime.parse(req.getParameter("date"));
                    meal = new Meal(null, date, req.getParameter("description"),
                            Integer.parseInt(req.getParameter("calories")));
                    mealDao.add(meal);
                    req.setAttribute("listMeals", getMealToList());
                } else {
                    date = LocalDateTime.parse(req.getParameter("date"));

                    meal = new Meal(Long.parseLong(mealId), date, req.getParameter("description"),
                            Integer.parseInt(req.getParameter("calories")));
                    mealDao.update(meal);
                    req.setAttribute("listMeals", getMealToList());
                    break;
                }
            default:
                forward = LIST_MEAL;
                req.setAttribute("listMeals", getMealToList());
        }

        RequestDispatcher view = req.getRequestDispatcher(forward);
        view.forward(req, resp);
    }

    private List<MealTo> getMealToList() {
        return MealsUtil.filteredByStreams(mealDao.getAll(), LocalTime.MIN, LocalTime.MAX, 2000);
    }
}
