<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout | ShopSphere</title>
    <style>
        * { box-sizing: border-box; }
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f5f7fb;
            color: #1f2937;
        }
        header {
            background: white;
            border-bottom: 1px solid #e5e7eb;
            padding: 20px 6%;
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .logo {
            color: #4f46e5;
            font-size: 25px;
            font-weight: bold;
            text-decoration: none;
        }
        header a:last-child { color: #374151; text-decoration: none; }
        main { max-width: 850px; margin: 35px auto; padding: 0 20px; }
        h1 { margin-bottom: 8px; }
        .subtitle { color: #6b7280; margin-bottom: 28px; }
        .panel {
            background: white;
            border: 1px solid #e5e7eb;
            border-radius: 12px;
            padding: 24px;
            margin-bottom: 22px;
        }
        h2 { margin-top: 0; font-size: 21px; }
        .address {
            display: flex;
            gap: 12px;
            padding: 15px;
            margin: 12px 0;
            border: 1px solid #e5e7eb;
            border-radius: 9px;
            cursor: pointer;
        }
        .address:has(input:checked) {
            border-color: #4f46e5;
            background: #f5f3ff;
        }
        .address input { margin-top: 5px; }
        .address strong { display: block; margin-bottom: 6px; }
        .muted { color: #6b7280; font-size: 14px; line-height: 1.5; }
        .payment {
            display: flex;
            gap: 12px;
            align-items: flex-start;
            padding: 15px;
            border: 1px solid #e5e7eb;
            border-radius: 9px;
            margin: 12px 0;
        }
        button, .button {
            display: inline-block;
            padding: 13px 20px;
            background: #4f46e5;
            color: white;
            border: 0;
            border-radius: 8px;
            font-size: 15px;
            font-weight: bold;
            text-decoration: none;
            cursor: pointer;
        }
        .error {
            background: #fff0f0;
            color: #a32222;
            padding: 13px;
            border-radius: 8px;
            margin-bottom: 20px;
        }
        .warning {
            background: #fff8e6;
            color: #805b00;
            padding: 12px;
            border-radius: 8px;
            font-size: 14px;
            margin-top: 14px;
        }
        .empty {
            text-align: center;
            background: white;
            border-radius: 12px;
            padding: 35px;
        }
        @media (max-width: 550px) {
            header { padding: 16px 5%; }
            .panel { padding: 18px; }
        }
    </style>
</head>
<body>
<header>
    <a class="logo" href="${pageContext.request.contextPath}/products">ShopSphere</a>
    <a href="${pageContext.request.contextPath}/cart">Back to Cart</a>
</header>

<main>
    <h1>Checkout</h1>
    <p class="subtitle">Choose your delivery address and payment method.</p>

    <c:if test="${not empty sessionScope.checkoutError}">
        <div class="error">
            <c:out value="${sessionScope.checkoutError}"/>
        </div>
        <c:remove var="checkoutError" scope="session"/>
    </c:if>

    <c:choose>
        <c:when test="${empty addresses}">
            <section class="empty">
                <h2>No delivery address found</h2>
                <p class="muted">
                    You need a saved delivery address before placing an order.
                </p>
                <a class="button"
                   href="${pageContext.request.contextPath}/address">Add Delivery Address</a>
            </section>
        </c:when>

        <c:otherwise>
            <form method="post"
                  action="${pageContext.request.contextPath}/checkout">

                <section class="panel">
                    <h2>1. Delivery Address</h2>

                    <c:forEach var="address" items="${addresses}">
                        <label class="address">
                            <input type="radio" name="addressId"
                                   value="<c:out value='${address.addressId}'/>"
                                   required>
                            <span>
                                <strong><c:out value="${address.addressType}"/></strong>
                                <c:out value="${address.addressLine}"/><br>
                                <c:out value="${address.city}"/>,
                                <c:out value="${address.state}"/>
                                - <c:out value="${address.pincode}"/>
                            </span>
                        </label>
                    </c:forEach>
                </section>

                <section class="panel">
                    <h2>2. Payment Method</h2>

                    <label class="payment">
                        <input type="radio" name="paymentMethod"
                               value="COD" required>
                        <span>
                            <strong>Cash on Delivery</strong><br>
                            <span class="muted">
                                Pay when your order arrives.
                            </span>
                        </span>
                    </label>

                    <label class="payment">
                        <input type="radio" name="paymentMethod"
                               value="MOCK_ONLINE" required>
                        <span>
                            <strong>Demo Online Payment</strong><br>
                            <span class="muted">
                                For academic demonstration only.
                            </span>
                        </span>
                    </label>

                    <div class="warning">
                        Demo online payment does not process real money.
                        Do not enter bank or card details.
                    </div>
                </section>

                <button type="submit">Place Order</button>
            </form>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
