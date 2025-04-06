<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <title>Delete coworking</title>
</head>
<body>
<h2>Delete coworking</h2>
<form:form action="cancel-reservation" method="post" modelAttribute="reservation">
    <table>
        <tr>
            <td><form:label path="id">ID reservation:</form:label></td>
            <td><form:input path="id" type="number" required="true"/></td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="submit" value="Cancel"/>
            </td>
        </tr>
    </table>
</form:form>
</body>
</html>
