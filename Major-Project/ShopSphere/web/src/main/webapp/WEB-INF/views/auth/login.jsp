<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign In | ShopSphere</title>
    <style>
        * { box-sizing: border-box; }
        body {
            margin: 0;
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 24px;
            font-family: Arial, sans-serif;
            background: linear-gradient(135deg, #f4f0ff, #ffffff);
            color: #24213b;
        }
        .card {
            width: 100%;
            max-width: 420px;
            padding: 34px;
            background: #fff;
            border-radius: 18px;
            box-shadow: 0 12px 40px rgba(60, 40, 100, .12);
        }
        .brand {
            text-align: center;
            font-size: 29px;
            font-weight: bold;
            color: #7048d9;
        }
        h1 { text-align: center; margin-bottom: 8px; }
        .subtitle { text-align: center; color: #777; margin-bottom: 25px; }
        label { display: block; margin: 16px 0 7px; font-weight: bold; font-size: 14px; }
        input {
            width: 100%;
            padding: 13px;
            border: 1px solid #ddd8e9;
            border-radius: 8px;
            font-size: 15px;
        }
        input:focus { outline: 2px solid #b7a0ff; }
        button {
            width: 100%;
            padding: 13px;
            margin-top: 23px;
            border: 0;
            border-radius: 8px;
            background: #7048d9;
            color: white;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
        }
        button:hover { background: #5832bd; }
        .message { padding: 11px; border-radius: 7px; margin: 15px 0; font-size: 14px; }
        .error { background: #fff0f0; color: #a32222; }
        .success { background: #eaf8ee; color: #216e39; }
        .footer { text-align: center; margin-top: 22px; font-size: 14px; }
        a { color: #7048d9; text-decoration: none; font-weight: bold; }
    </style>
</head>
<body>
<div class="card">
    <div class="brand">ShopSphere</div>
    <h1>Welcome back</h1>
    <p class="subtitle">Sign in to continue shopping.</p>

    <c:if test="${not empty requestScope.success}">
        <div class="message success"><c:out value="${requestScope.success}"/></div>
    </c:if>

    <c:if test="${not empty requestScope.error}">
        <div class="message error"><c:out value="${requestScope.error}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/login">
        <label for="email">Email address</label>
        <input id="email" name="email" type="email"
               maxlength="150" autocomplete="email" required>

        <label for="password">Password</label>
        <input id="password" name="password" type="password"
               autocomplete="current-password" required>

        <button type="submit">Sign In</button>
    </form>

    <div class="footer">
        New to ShopSphere?
        <a href="${pageContext.request.contextPath}/register">Create an account</a>
        <br><br>
        <a href="${pageContext.request.contextPath}/products">Continue shopping</a>
    </div>
</div>
</body>
</html>
