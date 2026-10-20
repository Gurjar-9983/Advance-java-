<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Account | ShopSphere</title>
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
            max-width: 440px;
            padding: 32px;
            background: white;
            border-radius: 18px;
            box-shadow: 0 12px 40px rgba(60, 40, 100, 0.12);
        }
        .brand {
            text-align: center;
            font-size: 28px;
            font-weight: bold;
            color: #6c43d9;
        }
        h1 { text-align: center; font-size: 25px; margin-bottom: 8px; }
        .subtitle { text-align: center; color: #777; margin-bottom: 25px; }
        label { display: block; margin: 15px 0 7px; font-weight: bold; font-size: 14px; }
        input {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd8e9;
            border-radius: 8px;
            font-size: 15px;
        }
        input:focus { outline: 2px solid #b7a0ff; border-color: #7650dc; }
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
        .error {
            background: #fff0f0;
            color: #a32222;
            padding: 11px;
            border-radius: 7px;
            margin: 15px 0;
            font-size: 14px;
        }
        .footer { text-align: center; margin-top: 20px; font-size: 14px; }
        a { color: #7048d9; text-decoration: none; font-weight: bold; }
    </style>
</head>
<body>
<div class="card">
    <div class="brand">ShopSphere</div>
    <h1>Create your account</h1>
    <p class="subtitle">Join us and start shopping.</p>

    <c:if test="${not empty requestScope.error}">
        <div class="error"><c:out value="${requestScope.error}"/></div>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/register">
        <label for="name">Full name</label>
        <input id="name" name="name" type="text"
               maxlength="100" autocomplete="name" required>

        <label for="email">Email address</label>
        <input id="email" name="email" type="email"
               maxlength="150" autocomplete="email" required>

        <label for="mobile">Mobile number (optional)</label>
        <input id="mobile" name="mobile" type="tel"
               pattern="[0-9]{10}" maxlength="10"
               autocomplete="tel" placeholder="10-digit mobile number">

        <label for="password">Password</label>
        <input id="password" name="password" type="password"
               minlength="8" maxlength="72"
               autocomplete="new-password" required>

        <label for="confirmPassword">Confirm password</label>
        <input id="confirmPassword" name="confirmPassword" type="password"
               minlength="8" maxlength="72"
               autocomplete="new-password" required>

        <button type="submit">Create Account</button>
    </form>

    <div class="footer">
        Already registered?
        <a href="${pageContext.request.contextPath}/login">Sign in</a>
    </div>
</div>
</body>
</html>

