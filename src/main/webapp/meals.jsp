<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Meals</title>
    <style type="text/css">
        table {
            border-collapse: collapse;
            text-align: center;
        }

        td, th {
            border: 1px solid black;
            padding: 7px;
        }

        .hide {
            display: none
        }
    </style>
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
            <td class="hide"><c:out value="${meal.id}"/></td>
            <td><javatime:format value="${meal.dateTime}" pattern="yyyy-MM-dd HH:mm"/></td>
            <td><c:out value="${meal.description}"/></td>
            <td><c:out value="${meal.calories}"/></td>
            <form action="meals" method="post" id="edit">
                <td><input class="hide" type="text" name="id" value="<c:out value="${meal.id}"/>"/>
                    <input class="hide" type="text" name="action" value="edit"/>
                    <input type="submit" value="Update"/></td>
            </form>
            <form action="meals" method="post" id="delete">
                <td><input class="hide" type="text" name="id" value="<c:out value="${meal.id}"/>"/>
                    <input class="hide" type="text" name="action" value="delete"/>
                    <input type="submit" value="Del"/></td>
            </form>
        </tr>
    </c:forEach>
</table>
<p><a href="meals?action=add">Add</a></p>
</body>
</html>