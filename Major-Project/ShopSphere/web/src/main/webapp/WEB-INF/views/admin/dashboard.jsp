
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard | ShopSphere</title>

    <style>
        * {
            box-sizing: border-box;
        }

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
            align-items: center;
            justify-content: space-between;
            flex-wrap: wrap;
            gap: 15px;
        }

        header h2 {
            margin: 0;
        }

        header nav {
            display: flex;
            gap: 22px;
            flex-wrap: wrap;
        }

        header a {
            color: white;
            text-decoration: none;
        }

        .container {
            width: 94%;
            max-width: 1400px;
            margin: 35px auto;
        }

        .welcome {
            margin-bottom: 30px;
        }

        .welcome h1 {
            font-size: 30px;
            margin-bottom: 8px;
        }

        .welcome p {
            color: #77758a;
        }

        .stats {
            display: grid;
            grid-template-columns: repeat(4, minmax(0, 1fr));
            gap: 20px;
            margin-bottom: 32px;
        }

        .stat-card {
            background: white;
            padding: 24px;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(30, 25, 70, 0.05);
            border: 1px solid #eeeaf8;
        }

        .stat-label {
            color: #77758a;
            font-size: 14px;
            margin-bottom: 15px;
        }

        .stat-number {
            color: #6046e8;
            font-size: 30px;
            font-weight: bold;
        }

        .section {
            background: white;
            padding: 25px;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(30, 25, 70, 0.05);
            border: 1px solid #eeeaf8;
            margin-bottom: 30px;
        }

        .section h2 {
            margin-top: 0;
            margin-bottom: 20px;
        }

        .table-wrap {
            width: 100%;
            overflow-x: auto;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            min-width: 650px;
        }

        th, td {
            padding: 14px 12px;
            text-align: left;
            border-bottom: 1px solid #eeeaf4;
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
        }

        .low-stock {
            background: #fff0e6;
            color: #a14e00;
        }

        .footer {
            color: #77758a;
            text-align: center;
            padding: 20px;
        }

        @media (max-width: 950px) {
            .stats {
                grid-template-columns: repeat(2, minmax(0, 1fr));
            }
        }

        @media (max-width: 550px) {
            header {
                padding: 18px 4%;
            }

            header nav {
                gap: 14px;
            }

            .stats {
                grid-template-columns: 1fr;
            }

            .section {
                padding: 16px;
            }

            .welcome h1 {
                font-size: 25px;
            }
        }
    </style>
</head>

<body>

<header>
    <h2>ShopSphere Admin</h2>

    <nav>
        <a href="${pageContext.request.contextPath}/admin">Dashboard</a>
        <a href="${pageContext.request.contextPath}/products">View Store</a>
        <a href="${pageContext.request.contextPath}/orders">My Orders</a>
    </nav>
</header>

<main class="container">

    <section class="welcome">
        <h1>Dashboard Overview</h1>
        <p>Monitor your store, customers, orders, and inventory.</p>
    </section>

    <section class="stats">

        <div class="stat-card">
            <div class="stat-label">Total Products</div>
            <div class="stat-number">
                <c:out value="${productCount}"/>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-label">Registered Customers</div>
            <div class="stat-number">
                <c:out value="${customerCount}"/>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-label">Total Orders</div>
            <div class="stat-number">
                <c:out value="${orderCount}"/>
            </div>
        </div>

        <div class="stat-card">
            <div class="stat-label">Low-Stock Products</div>
            <div class="stat-number">
                <c:out value="${lowStockCount}"/>
            </div>
        </div>

    </section>

    <section class="section">
        <h2>Recent Orders</h2>

        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Customer</th>
                    <th>Total</th>
                    <th>Payment</th>
                    <th>Payment Status</th>
                    <th>Order Status</th>
                    <th>Date</th>
                </tr>
                </thead>

                <tbody>
                <c:choose>
                    <c:when test="${not empty recentOrders}">
                        <c:forEach var="order" items="${recentOrders}">
                            <tr>
                                <td>#<c:out value="${order.orderId}"/></td>
                                <td><c:out value="${order.customerName}"/></td>
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
                                <td><c:out value="${order.createdAt}"/></td>
                            </tr>
                        </c:forEach>
                    </c:when>

                    <c:otherwise>
                        <tr>
                            <td colspan="7">No orders have been placed yet.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </section>

    <section class="section">
        <h2>Inventory Overview</h2>

        <div class="table-wrap">
            <table>
                <thead>
                <tr>
                    <th>Product ID</th>
                    <th>Product Name</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Status</th>
                </tr>
                </thead>

                <tbody>
                <c:choose>
                    <c:when test="${not empty products}">
                        <c:forEach var="product" items="${products}">
                            <tr>
                                <td><c:out value="${product.productId}"/></td>
                                <td><c:out value="${product.name}"/></td>
                                <td>₹<c:out value="${product.price}"/></td>
                                <td>
                                    <span class="badge ${product.stock le 5 ? 'low-stock' : ''}">
                                        <c:out value="${product.stock}"/>
                                    </span>
                                </td>
                                <td><c:out value="${product.status}"/></td>
                            </tr>
                        </c:forEach>
                    </c:when>

                    <c:otherwise>
                        <tr>
                            <td colspan="5">No products found.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </section>

</main>

<footer class="footer">
    ShopSphere Admin Dashboard | Java Enterprise E-Commerce Project
</footer>

</body>
</html>