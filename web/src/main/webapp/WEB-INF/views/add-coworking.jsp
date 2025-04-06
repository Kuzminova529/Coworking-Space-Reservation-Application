<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <title>Add new coworking space</title>
</head>
<body>
<h2>Add new coworking space</h2>
<form:form action="add-coworking" method="post" modelAttribute="coworkingSpace">
    <table>
        <tr>
            <td><form:label path="type">Type:</form:label></td>
            <td>
                <form:select path="type">
                    <form:options items="${coworkingSpaceType}" itemValue="name" itemLabel="type"/>
                </form:select>
            </td>
        </tr>
        <tr>
            <td><form:label path="price">Price:</form:label></td>
            <td><form:input path="price" required="true"/></td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="Add"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>
