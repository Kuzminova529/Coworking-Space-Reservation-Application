<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
    <title>Make reservation</title>
</head>
<body>
<h2>Make reservation</h2>

<form:form action="make-reservation" method="post" modelAttribute="reservation">
    <table>
        <tr>
            <td><form:label path="coworkingSpaceId">Coworking space:</form:label></td>
            <td>
                <form:select path="coworkingSpaceId">
                    <form:options items="${coworkingSpaces}" itemValue="id" itemLabel="id" />
                </form:select>
            </td>
        </tr>

        <tr>
            <td><form:label path="reservationName">Reservation name:</form:label></td>
            <td><form:input path="reservationName" required="true" /></td>
        </tr>

        <tr>
            <td><form:label path="startDateTime">Start time:</form:label></td>
            <td><form:input path="startDateTime" type="datetime-local" required="true" /></td>
        </tr>

        <tr>
            <td><form:label path="endDateTime">End time:</form:label></td>
            <td><form:input path="endDateTime" type="datetime-local" required="true" /></td>
        </tr>

        <tr>
            <td colspan="2">
                <input type="submit" value="Create reservation" />
            </td>
        </tr>
    </table>
</form:form>

</body>
</html>
