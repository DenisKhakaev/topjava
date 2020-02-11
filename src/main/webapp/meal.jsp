<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Add new Meal</title>
    <style>
        .hide {
            display: none
        }</style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<form method="POST" action='meals' name="addMeals">
    <input class="hide" type="text" readonly="readonly" name="id" value="${meal.id}"/>
    Date : <input type="datetime-local" name="date" value="${meal.dateTime}" required/>
    <br/>
    Description: <input type="text" name="description" value="${meal.description}"/>
    <br/>
    Calories : <input type="number" name="calories" value="${meal.calories}"/>
    <br/>
    <input class="hide" type="text" name="action" value="${meal eq null?'add':'update'}"/>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>