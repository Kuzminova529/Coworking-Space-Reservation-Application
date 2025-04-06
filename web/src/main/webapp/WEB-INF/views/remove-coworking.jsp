<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
  <title>Delete coworking</title>
</head>
<body>
<h2>Delete coworking</h2>
<form:form action="remove-coworking" method="post" modelAttribute="coworkingSpace">
  <table>
    <tr>
      <td><form:label path="id">ID coworking space:</form:label></td>
      <td><form:input path="id" type="number" required="true"/></td>
    </tr>
    <tr>
      <td colspan="2">
        <input type="submit" value="Delete"/>
      </td>
    </tr>
  </table>
</form:form>
</body>
</html>
