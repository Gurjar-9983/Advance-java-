
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Addresses | ShopSphere</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f7f7fc;
            color: #29243d;
        }

        header {
            background: #6c4be8;
            color: white;
            padding: 18px 7%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            flex-wrap: wrap;
            gap: 12px;
        }

        header h2 {
            margin: 0;
        }

        header a {
            color: white;
            text-decoration: none;
            margin-left: 18px;
        }

        .container {
            width: min(1100px, 92%);
            margin: 35px auto;
        }

        .layout {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 25px;
        }

        .card {
            background: white;
            padding: 26px;
            border-radius: 16px;
            box-shadow: 0 5px 22px rgba(40, 30, 80, 0.07);
        }

        h1 {
            margin-bottom: 8px;
        }

        .subtitle {
            color: #77718a;
            margin-bottom: 25px;
        }

        label {
            display: block;
            margin: 15px 0 7px;
            font-weight: bold;
        }

        input, select {
            width: 100%;
            padding: 12px;
            border: 1px solid #dedbea;
            border-radius: 8px;
            font-size: 15px;
        }

        input:focus, select:focus {
            outline: 2px solid #c8baff;
            border-color: #6c4be8;
        }

        button {
            width: 100%;
            margin-top: 22px;
            padding: 13px;
            border: none;
            border-radius: 8px;
            background: #6c4be8;
            color: white;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
        }

        button:hover {
            background: #5435c9;
        }

        .message {
            padding: 12px;
            margin-bottom: 16px;
            border-radius: 8px;
        }

        .success {
            background: #e8f9ed;
            color: #176b32;
        }

        .error {
            background: #fff0f0;
            color: #a12727;
        }

        .address {
            border: 1px solid #e8e4f3;
            border-radius: 12px;
            padding: 18px;
            margin-bottom: 15px;
        }

        .badge {
            display: inline-block;
            background: #eee9ff;
            color: #6041d5;
            border-radius: 20px;
            padding: 5px 10px;
            font-size: 12px;
            font-weight: bold;
        }

        .empty {
            color: #77718a;
            line-height: 1.7;
        }

        .checkout-link {
            display: inline-block;
            margin-top: 10px;
            padding: 12px 18px;
            border-radius: 8px;
            background: #201b31;
            color: white;
            text-decoration: none;
        }

        @media (max-width: 750px) {
            .layout {
                grid-template-columns: 1fr;
            }

            header {
                padding: 18px 4%;
            }

            header a {
                margin-left: 8px;
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
        <a href="${pageContext.request.contextPath}/address">My Addresses</a>
    </nav>
</header>

<main class="container">

    <h1>My Delivery Addresses</h1>
    <p class="subtitle">Manage your saved addresses for easy checkout.</p>

    <c:if test="${not empty sessionScope.addressSuccess}">
        <div class="message success">
            <c:out value="${sessionScope.addressSuccess}"/>
        </div>
        <c:remove var="addressSuccess" scope="session"/>
    </c:if>

    <c:if test="${not empty sessionScope.addressError}">
        <div class="message error">
            <c:out value="${sessionScope.addressError}"/>
        </div>
        <c:remove var="addressError" scope="session"/>
    </c:if>

    <div class="layout">

        <section class="card">
            <h2>Add New Address</h2>
            <p class="subtitle">Enter your delivery details below.</p>

            <form method="post"
                  action="${pageContext.request.contextPath}/address">

                <label for="addressLine">Full Address</label>
                <input id="addressLine"
                       name="addressLine"
                       type="text"
                       maxlength="255"
                       placeholder="House number, street, area"
                       required>

                <label for="city">City</label>
                <input id="city"
                       name="city"
                       type="text"
                       maxlength="100"
                       placeholder="Enter city"
                       required>

                <label for="state">State</label>
                <input id="state"
                       name="state"
                       type="text"
                       maxlength="100"
                       placeholder="Enter state"
                       required>

                <label for="pincode">PIN Code</label>
                <input id="pincode"
                       name="pincode"
                       type="text"
                       inputmode="numeric"
                       pattern="[0-9]{6}"
                       maxlength="6"
                       placeholder="6-digit PIN code"
                       required>

                <label for="addressType">Address Type</label>
                <select id="addressType" name="addressType" required>
                    <option value="HOME">Home</option>
                    <option value="WORK">Work</option>
                    <option value="OTHER">Other</option>
                </select>

                <button type="submit">Save Address</button>
            </form>
        </section>

        <section class="card">
            <h2>Saved Addresses</h2>

            <c:choose>
                <c:when test="${not empty addresses}">
                    <c:forEach var="address" items="${addresses}">
                        <div class="address">
                            <span class="badge">
                                <c:out value="${address.addressType}"/>
                            </span>

                            <p>
                                <strong>
                                    <c:out value="${address.addressLine}"/>
                                </strong>
                            </p>

                            <p>
                                <c:out value="${address.city}"/>,
                                <c:out value="${address.state}"/>
                                - <c:out value="${address.pincode}"/>
                            </p>
                        </div>
                    </c:forEach>

                    <a class="checkout-link"
                       href="${pageContext.request.contextPath}/checkout">
                        Continue to Checkout
                    </a>
                </c:when>

                <c:otherwise>
                    <p class="empty">
                        You haven't saved any addresses yet.
                        Add your first delivery address using the form.
                    </p>
                </c:otherwise>
            </c:choose>
        </section>

    </div>

</main>

</body>
</html>