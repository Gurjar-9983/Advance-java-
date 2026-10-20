<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Confirmation | ShopSphere</title>
    <style>
        * { box-sizing: border-box; }
        body {
            margin: 0;
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
            font-family: Arial, sans-serif;
            background: #f5f7fb;
            color: #1f2937;
        }
        .card {
            width: 100%;
            max-width: 580px;
            padding: 35px;
            background: white;
            border: 1px solid #e5e7eb;
            border-radius: 16px;
            text-align: center;
            box-shadow: 0 8px 28px rgba(0,0,0,.05);
        }
        .check {
            width: 70px;
            height: 70px;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 auto 20px;
            border-radius: 50%;
            background: #eaf8ee;
            color: #16803d;
            font-size: 38px;
        }
        h1 { margin-bottom: 10px; }
        .muted { color: #6b7280; line-height: 1.6; }
        .details {
            margin: 25px 0;
            padding: 20px;
            background: #f9fafb;
            border-radius: 10px;
            text-align: left;
        }
        .row {
            display: flex;
            justify-content: space-between;
            gap: 15px;
            padding: 10px 0;
            border-bottom: 1px solid #e5e7eb;
        }
        .row:last-child { border-bottom: 0; }
        .label { color: #6b7280; }
        .value { font-weight: bold; text-align: right; overflow-wrap: anywhere; }
        .button {
            display: inline-block;
            margin: 8px 5px;
            padding: 13px 18px;
            border-radius: 8px;
            background: #4f46e5;
            color: white;
            text-decoration: none;
        }
        .secondary { background: #eef2ff; color: #4f46e5; }
        @media (max-width: 450px) {
            .card { padding: 22px; }
            .row { flex-direction: column; gap: 5px; }
            .value { text-align: left; }
        }
    </style>
</head>
<body>
<div class="card">
    <div class="check">&#10003;</div>
    <h1>Order placed successfully!</h1>
    <p class="muted">
        Thank you for shopping with ShopSphere.
        Your order has been recorded.
    </p>

    <div class="details">
        <div class="row">
            <span class="label">Order number</span>
            <span class="value">#<c:out value="${orderId}"/></span>
        </div>
        <div class="row">
            <span class="label">Order total</span>
            <span class="value">₹<c:out value="${totalAmount}"/></span>
        </div>
        <div class="row">
            <span class="label">Payment method</span>
            <span class="value"><c:out value="${paymentMethod}"/></span>
        </div>
        <div class="row">
            <span class="label">Payment status</span>
            <span class="value"><c:out value="${paymentStatus}"/></span>
        </div>
        <div class="row">
            <span class="label">Order status</span>
            <span class="value"><c:out value="${orderStatus}"/></span>
        </div>
        <div class="row">
            <span class="label">Order date</span>
            <span class="value"><c:out value="${createdAt}"/></span>
        </div>
    </div>

    <a class="button" href="${pageContext.request.contextPath}/products">
        Continue Shopping
    </a>
    <a class="button secondary"
       href="${pageContext.request.contextPath}/orders">
        My Orders
    </a>
</div>
</body>
</html>
