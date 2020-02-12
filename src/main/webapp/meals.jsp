<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Meals</title>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<table>
    <caption>Meals</caption>
    <tr>
        <th class="hide">Id</th>
        <th>Date</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan="2">Actions</th>
    </tr>
    <c:forEach var="meal" items="${listMeals}">
        <tr style="color: ${meal.excess ? 'red': 'green'}">
            <td class="hide">${meal.id}</td>
            <td><javatime:format value="${meal.dateTime}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
            <form action="meals" method="post" id="delete">
                <td><input class="hide" type="text" name="id" value="${meal.id}"/>
                    <input class="hide" type="text" name="action" value="delete"/>
                    <input type="submit" value="Delete"/></td>
            </form>
        </tr>
    </c:forEach>
</table>
<p><a href="meals?action=add">Add</a></p>
</body>
</html>