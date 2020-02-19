package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.web.meal.MealRestController;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Objects;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    private MealRestController repository;
    private LocalDate startDate;
    private LocalTime startTime;
    private LocalDate finishDate;
    private LocalTime finishTime;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {
            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));
            repository = appCtx.getBean(MealRestController.class);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String id = request.getParameter("id");

        if (id != null) {
            Meal meal = new Meal(id.isEmpty() ? null : Integer.valueOf(id),
                    LocalDateTime.parse(request.getParameter("dateTime")),
                    request.getParameter("description"),
                    Integer.parseInt(request.getParameter("calories")), SecurityUtil.authUserId());

            if (meal.isNew()) {
                log.info("Create {}", meal);
                repository.create(meal, SecurityUtil.authUserId());
            } else {
                log.info("Update {}", meal);
                repository.update(meal, SecurityUtil.authUserId());
            }
        } else {
            startTime = getStartTime(request);
            startDate = getStartDate(request);
            finishDate = getFinishDate(request);
            finishTime = getFinishTime(request);
        }
        response.sendRedirect("meals");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        request.setAttribute("startTime", startTime);
        request.setAttribute("startDate", startDate);
        request.setAttribute("finishDate", finishDate);
        request.setAttribute("finishTime", finishTime);
        switch (action == null ? "all" : action) {
            case "delete":
                int id = getId(request);
                log.info("Delete (id, userId) {}", id, SecurityUtil.authUserId());
                repository.delete(id);
                response.sendRedirect("meals");
                break;
            case "create":
            case "update":
                final Meal meal = "create".equals(action) ?
                        new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000, SecurityUtil.authUserId()) :
                        repository.get(getId(request));
                request.setAttribute("meal", meal);
                request.getRequestDispatcher("/mealForm.jsp").forward(request, response);
                break;
            case "all":
            default:
                log.info("getAllByUser {}", SecurityUtil.authUserId());
                request.setAttribute("meals",
                        repository.getAllFilter(startDate, startTime, finishDate, finishTime, SecurityUtil.authUserCaloriesPerDay()));
                request.getRequestDispatcher("/meals.jsp").forward(request, response);
                break;
        }
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }

    private LocalDate getStartDate(HttpServletRequest request) {
        String start = request.getParameter("startDate");
        if (start != null && !start.isEmpty()) {
            return LocalDate.parse(start);
        }
        return null;
    }

    private LocalDate getFinishDate(HttpServletRequest request) {
        String finish = request.getParameter("finishDate");
        if (finish != null && !finish.isEmpty()) {
            return LocalDate.parse(finish);
        }
        return null;
    }

    private LocalTime getStartTime(HttpServletRequest request) {
        String start = request.getParameter("startTime");
        if (start != null && !start.isEmpty()) {
            return LocalTime.parse(start);
        }
        return null;
    }

    private LocalTime getFinishTime(HttpServletRequest request) {
        String finish = request.getParameter("finishTime");
        if (finish != null && !finish.isEmpty()) {
            return LocalTime.parse(finish);
        }
        return null;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalDate getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(LocalDate finishDate) {
        this.finishDate = finishDate;
    }

    public LocalTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalTime finishTime) {
        this.finishTime = finishTime;
    }
}
