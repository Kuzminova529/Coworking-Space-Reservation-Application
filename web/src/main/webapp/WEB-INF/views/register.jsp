<form action="${pageContext.request.contextPath}/register" method="post">
    <label for="username">Username:</label>
    <input type="text" id="username" name="username" value="${user.username}" required>

    <label for="password">Password:</label>
    <input type="password" id="password" name="password" required>

    <label for="role">Role:</label>
    <select name="role" id="role">
        <option value="CUSTOMER" ${user.role == 'CUSTOMER' ? 'selected' : ''}>Customer</option>
        <option value="ADMIN" ${user.role == 'ADMIN' ? 'selected' : ''}>Admin</option>
    </select>

    <button type="submit">Register</button>
</form>
