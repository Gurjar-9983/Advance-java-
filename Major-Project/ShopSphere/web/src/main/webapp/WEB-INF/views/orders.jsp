
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Orders | ShopSphere</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f5f6fb;
            color: #24243a;
        }

        header {
            background: #6046e8;
            color: white;
            padding: 20px 7%;
            display: flex;
            align-items: center;
            justify-content: space-between;
            flex-wrap: wrap;
            gap: 15px;
        }

        header h2 {
            margin: 0;
        }

        header a {
            color: white;
            text-decoration: none;
            margin-left: 20px;
        }

        .container {
            width: min(1100px, 92%);
            margin: 40px auto;
        }

        .heading {
            margin-bottom: 28px;
        }

        .heading h1 {
            margin-bottom: 8px;
            font-size: 32px;
        }

        .heading p {
            color: #77758a;
        }

        .order-card {
            background: white;
            border-radius: 14px;
            padding: 25px;
            margin-bottom: 20px;
            box-shadow: 0 5px 22px rgba(30, 25, 70, 0.06);
            border: 1px solid #eeeaf8;
        }

        .order-top {
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 15px;
            padding-bottom: 18px;
            border-bottom: 1px solid #eeeaf4;
        }

        .order-number {
            font-size: 20px;
            font-weight: bold;
        }

        .order-date {
            margin-top: 8px;
            color: #77758a;
            font-size: 14px;
        }

        .status {
            display: inline-block;
            border-radius: 20px;
            padding: 8px 13px;
            background: #eeeaff;
            color: #6046e8;
            font-size: 13px;
            font-weight: bold;
        }

        .order-details {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            gap: 20px;
            padding: 22px 0;
        }

        .detail-label {
            color: #77758a;
            font-size: 13px;
            margin-bottom: 8px;
        }

        .detail-value {
            font-weight: bold;
            font-size: 16px;
            overflow-wrap: anywhere;
        }

        .payment-status {
            color: #a16a00;
        }

        .order-total {
            color: #6046e8;
            font-size: 20px;
        }

        .empty {
            text-align: center;
            background: white;
            padding: 55px 25px;
            border-radius: 14px;
            box-shadow: 0 5px 22px rgba(30, 25, 70, 0.06);
        }

        .empty h2 {
            margin-bottom: 12px;
        }

        .empty p {
            color: #77758a;
            line-height: 1.7;
        }

        .button {
            display: inline-block;
            background: #6046e8;
            color: white;
            text-decoration: none;
            padding: 12px 20px;
            border-radius: 8px;
            margin-top: 15px;
        }

        .button:hover {
            background: #4d35cb;
        }

        @media (max-width: 650px) {
            header {
                padding: 18px 4%;
            }

            header a {
                margin-left: 8px;
            }

            .order-details {
                grid-template-columns: 1fr;
                gap: 18px;
            }

            .heading h1 {
                font-size: 27px;
            }
        }
    </style>
</head>

<body>

<header>
    <h2>ShopSphere</h2>

    <nav>
        <a href="${pageContext.request.contextPath}/products">Products</a>
        <a href="${pageContext.request.contextPath}/cart">My Cart</a>
        <a href="${pageContext.request.contextPath}/address">Addresses</a>
    </nav>
</header>

<main class="container">

    <div class="heading">
        <h1>My Orders</h1>
        <p>View your purchases and check their current status.</p>
    </div>

    <c:choose>
        <c:when test="${not empty orders}">

            <c:forEach var="order" items="${orders}">

                <article class="order-card">

                    <div class="order-top">
                        <div>
                            <div class="order-number">
                                Order #<c:out value="${order.orderId}"/>
                            </div>

                            <div class="order-date">
                                Placed on:
                                <c:out value="${order.createdAt}"/>
                            </div>
                        </div>

                        <span class="status">
                            <c:out value="${order.orderStatus}"/>
                        </span>
                    </div>

                    <div class="order-details">

                        <div>
                            <div class="detail-label">Order Total</div>
                            <div class="detail-value order-total">
                                ₹<c:out value="${order.totalAmount}"/>
                            </div>
                        </div>

                        <div>
                            <div class="detail-label">Payment Method</div>
                            <div class="detail-value">
                                <c:out value="${order.paymentMethod}"/>
                            </div>
                        </div>

                        <div>
                            <div class="detail-label">Payment Status</div>
                            <div class="detail-value payment-status">
                                <c:out value="${order.paymentStatus}"/>
                            </div>
                        </div>

                    </div>

                </article>

            </c:forEach>

        </c:when>

        <c:otherwise>

            <section class="empty">
                <h2>No orders yet</h2>

                <p>
                    You haven't placed any orders.
                    Explore our products and find something you love.
                </p>

                <a class="button"
                   href="${pageContext.request.contextPath}/products">
                    Start Shopping
                </a>
            </section>

        </c:otherwise>
    </c:choose>

    <a class="button"
       href="${pageContext.request.contextPath}/products">
        Continue Shopping
    </a>

</main>

</body>
</html>