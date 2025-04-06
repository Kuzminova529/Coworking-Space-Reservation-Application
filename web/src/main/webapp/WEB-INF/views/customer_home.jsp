<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>About Reservation App</title>
</head>
<body>
<h1>Welcome to Reservation App! Customer</h1>
<p>Reservation App is a modern web application designed to manage bookings of workspaces in coworking spaces. With it, you can:</p>
<ul>
    <li><a href="${pageContext.request.contextPath}/customer/browse-spaces">Browse available spaces</a></li>
    <li><a href="${pageContext.request.contextPath}/customer/make-reservation">Make a reservation</a></li>
    <li><a href="${pageContext.request.contextPath}/customer/cancel-reservation">Cancel reservation</a></li>
    <li><a href="${pageContext.request.contextPath}/customer/my-reservations">View my reservations</a></li>
</ul>
<p>The application is developed using Java technologies, including servlets and JSP, ensuring stability and high performance. You can easily manage your bookings and find suitable workspaces.</p>
<p>Start using Reservation App today and optimize your workspace booking process!</p>
</body>
</html>
