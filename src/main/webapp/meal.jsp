<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<html>
<head>
    <title>Add new Meal</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<form method="POST" action='meals' name="frmAddUser">
    User ID : <input type="text" readonly="readonly" name="id" value="<c:out value="${meal.id}" />"/>
    <br/>
    Date : <input type="date" name="date" value="<javatime:format value="${meal.dateTime}" pattern="yyyy-MM-dd"/>"
                  placeholder="yyyy-MM-dd" required/>
    <br/>
    Description: <input type="text" name="description" value="<c:out value="${meal.description}" />"/>
    <br/>
    Calories : <input type="number" name="calories" value="<c:out value="${meal.calories}" />"/>
    <br/>
    <input type="submit" value="Submit"/>
</form>
<p><a href="meals?action=add"></a></p>
</body>
</html>