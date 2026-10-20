
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Management | ShopSphere Admin</title>

    <style>
        * { box-sizing: border-box; }

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f5f6fb;
            color: #29243d;
        }

        header {
            background: #6046e8;
            color: white;
            padding: 20px 5%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 15px;
        }

        header h2 { margin: 0; }

        header nav {
            display: flex;
            flex-wrap: wrap;
            gap: 20px;
        }

        header a {
            color: white;
            text-decoration: none;
        }

        .container {
            width: 94%;
            max-width: 1450px;
            margin: 35px auto;
        }

        h1 { margin-bottom: 8px; }

        .muted {
            color: #77758a;
            margin-bottom: 25px;
        }

        .card {
            background: white;
            padding: 25px;
            border-radius: 15px;
            border: 1px solid #eeeaf8;
            box-shadow: 0 5px 20px rgba(30, 25, 70, 0.05);
        }

        .message {
            padding: 14px 16px;
            border-radius: 8px;
            background: #e8f9ed;
            color: #176b32;
            margin-bottom: 20px;
        }

        .table-wrap { overflow-x: auto; }

        table {
            width: 100%;
            min-width: 1050px;
            border-collapse: collapse;
        }

        th, td {
            padding: 14px 12px;
            border-bottom: 1px solid #eeeaf4;
            text-align: left;
            vertical-align: middle;
            font-size: 14px;
        }

        th {
            background: #f8f6ff;
            color: #51476d;
        }

        .badge {
            display: inline-block;
            padding: 6px 10px;
            border-radius: 20px;
            background: #eeeaff;
            color: #6046e8;
            font-size: 12px;
            font-weight: bold;
            white-space: nowrap;
        }

        select {
            padding: 9px;
            border: 1px solid #ddd8eb;
            border-radius: 7px;
            background: white;
            max-width: 145px;
        }

        button {
            padding: 10px 13px;
            border: none;
            border-radius: 7px;
            background: #6046e8;
            color: white;
            cursor: pointer;
            font-weight: bold;
            margin-top: 6px;
        }

        button:hover { background: #4d35cb; }

        .empty {
            padding: 25px;
            text-align: center;
            color: #77758a;
        }

        .footer {
            text-align: center;
            padding: 25px;
            color: #77758a;
        }

        @media (max-width: 600px) {
            header { padding: 18px 4%; }
            .card { padding: 15px; }
        }
    </style>
</head>

<body>

<header>
    <h2>ShopSphere Admin</h2>
    <nav>
        <a href="${pageContext.request.contextPath}/admin">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/products">Products</a>
        <a href="${pageContext.request.contextPath}/admin/orders">Orders</a>
        <a href="${pageContext.request.contextPath}/products">View Store</a>
    </nav>
</header>

<main class="container">

    <h1>Order Management</h1>
    <p class="muted">
        Review customer orders and update their fulfillment status.
    </p>

    <c:if test="${not empty sessionScope.adminOrderMessage}">
        <div class="message">
            <c:out value="${sessionScope.adminOrderMessage}"/>
        </div>
        <c:remove var="adminOrderMessage" scope="session"/>
    </c:if>

    <section class="card">
        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Customer</th>
                    <th>Email</th>
                    <th>Total</th>
                    <th>Payment</th>
                    <th>Payment Status</th>
                    <th>Order Status</th>
                    <th>Update Status</th>
                </tr>
                </thead>

                <tbody>
                <c:choose>
                    <c:when test="${not empty orders}">
                        <c:forEach var="order" items="${orders}">
                            <tr>
                                <td>#<c:out value="${order.orderId}"/></td>
                                <td><c:out value="${order.customerName}"/></td>
                                <td><c:out value="${order.customerEmail}"/></td>
                                <td>₹<c:out value="${order.totalAmount}"/></td>
                                <td><c:out value="${order.paymentMethod}"/></td>
                                <td>
                                    <span class="badge">
                                        <c:out value="${order.paymentStatus}"/>
                                    </span>
                                </td>
                                <td>
                                    <span class="badge">
                                        <c:out value="${order.orderStatus}"/>
                                    </span>
                                </td>
                                <td>
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/admin/orders">
                                        <input type="hidden" name="orderId"
                                               value="<c:out value='${order.orderId}'/>">

                                        <select name="orderStatus" required>
                                            <option value="CONFIRMED"
                                                ${order.orderStatus eq 'CONFIRMED' ? 'selected' : ''}>
                                                Confirmed
                                            </option>
                                            <option value="PACKED"
                                                ${order.orderStatus eq 'PACKED' ? 'selected' : ''}>
                                                Packed
                                            </option>
                                            <option value="SHIPPED"
                                                ${order.orderStatus eq 'SHIPPED' ? 'selected' : ''}>
                                                Shipped
                                            </option>
                                            <option value="DELIVERED"
                                                ${order.orderStatus eq 'DELIVERED' ? 'selected' : ''}>
                                                Delivered
                                            </option>
                                            <option value="CANCELLED"
                                                ${order.orderStatus eq 'CANCELLED' ? 'selected' : ''}>
                                                Cancelled
                                            </option>
                                        </select>

                                        <button type="submit">Update</button>
                                    </form>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>

                    <c:otherwise>
                        <tr>
                            <td colspan="8" class="empty">
                                No orders found.
                            </td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </section>

</main>

<footer class="footer">
    ShopSphere | Java Enterprise E-Commerce Project
</footer>

</body>
</html>