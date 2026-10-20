<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Your Shopping Cart | ShopSphere</title>
    <style>
        * { box-sizing: border-box; }
        body {
            margin: 0;
            font-family: Arial, sans-serif;
            background: #f5f7fb;
            color: #1f2937;
        }
        header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 15px;
            flex-wrap: wrap;
            padding: 20px 6%;
            background: white;
            border-bottom: 1px solid #e5e7eb;
        }
        .logo {
            font-size: 25px;
            font-weight: bold;
            color: #4f46e5;
            text-decoration: none;
        }
        nav a {
            margin-left: 18px;
            color: #374151;
            text-decoration: none;
        }
        main { max-width: 1050px; margin: 35px auto; padding: 0 20px; }
        h1 { margin-bottom: 8px; }
        .subtitle { color: #6b7280; margin-bottom: 28px; }
        .message {
            padding: 13px;
            margin: 14px 0;
            border-radius: 8px;
            background: #eaf8ee;
            color: #216e39;
        }
        .error { background: #fff0f0; color: #a32222; }
        .cart-layout {
            display: grid;
            grid-template-columns: minmax(0, 1fr) 300px;
            gap: 24px;
            align-items: start;
        }
        .items, .summary, .empty {
            background: white;
            border: 1px solid #e5e7eb;
            border-radius: 12px;
            padding: 22px;
        }
        .item {
            display: grid;
            grid-template-columns: minmax(0, 1fr) auto;
            gap: 16px;
            padding: 18px 0;
            border-bottom: 1px solid #e5e7eb;
        }
        .item:first-child { padding-top: 0; }
        .item:last-child { border-bottom: 0; padding-bottom: 0; }
        .item h2 { font-size: 17px; margin: 0 0 7px; }
        .muted { color: #6b7280; font-size: 14px; }
        .price { font-weight: bold; margin: 8px 0; }
        form { display: inline-flex; gap: 7px; align-items: center; flex-wrap: wrap; }
        input[type=number] {
            width: 70px;
            padding: 8px;
            border: 1px solid #d1d5db;
            border-radius: 6px;
        }
        button, .button {
            display: inline-block;
            border: 0;
            border-radius: 7px;
            padding: 10px 13px;
            background: #4f46e5;
            color: white;
            text-decoration: none;
            cursor: pointer;
            font-size: 14px;
        }
        .remove { background: #fff0f0; color: #a32222; }
        .summary h2 { margin-top: 0; }
        .total {
            display: flex;
            justify-content: space-between;
            gap: 12px;
            padding: 16px 0;
            margin: 12px 0;
            border-top: 1px solid #e5e7eb;
            font-size: 18px;
            font-weight: bold;
        }
        .checkout { width: 100%; text-align: center; padding: 13px; }
        .continue { display: inline-block; margin-top: 18px; color: #4f46e5; text-decoration: none; }
        .empty { text-align: center; padding: 45px 20px; }
        @media (max-width: 750px) {
            .cart-layout { grid-template-columns: 1fr; }
        }
        @media (max-width: 450px) {
            .item { grid-template-columns: 1fr; }
            header { padding: 16px 5%; }
        }
    </style>
</head>
<body>
<header>
    <a class="logo" href="${pageContext.request.contextPath}/products">ShopSphere</a>
    <nav>
        <a href="${pageContext.request.contextPath}/products">Continue shopping</a>
        <a href="${pageContext.request.contextPath}/cart">My Cart</a>
    </nav>
</header>

<main>
    <h1>Your Shopping Cart</h1>
    <p class="subtitle">Review your items before checkout.</p>

    <c:if test="${param.added == '1'}">
        <div class="message">Product added to your cart successfully.</div>
    </c:if>
    <c:if test="${param.updated == '1'}">
        <div class="message">Cart quantity updated successfully.</div>
    </c:if>
    <c:if test="${param.removed == '1'}">
        <div class="message">Product removed from your cart.</div>
    </c:if>

    <c:if test="${not empty sessionScope.cartError}">
        <div class="message error"><c:out value="${sessionScope.cartError}"/></div>
        <c:remove var="cartError" scope="session"/>
    </c:if>

    <c:choose>
        <c:when test="${not empty cartItems}">
            <div class="cart-layout">
                <section class="items">
                    <c:forEach var="item" items="${cartItems}">
                        <article class="item">
                            <div>
                                <h2><c:out value="${item.name}"/></h2>
                                <div class="muted"><c:out value="${item.brand}"/></div>
                                <div class="price">
                                    ₹<c:out value="${item.price}"/> each
                                </div>
                                <div class="muted">
                                    Available stock: <c:out value="${item.stock}"/>
                                </div>
                                <p>
                                    Subtotal:
                                    <strong>₹<c:out value="${item.subtotal}"/></strong>
                                </p>

                                <form method="post" action="${pageContext.request.contextPath}/cart">
                                    <input type="hidden" name="action" value="update">
                                    <input type="hidden" name="productId" value="<c:out value='${item.productId}'/>">
                                    <label>
                                        <span class="muted">Quantity</span>
                                        <input type="number" name="quantity"
                                               min="1" max="100"
                                               value="<c:out value='${item.quantity}'/>"
                                               required>
                                    </label>
                                    <button type="submit">Update</button>
                                </form>

                                <form method="post" action="${pageContext.request.contextPath}/cart">
                                    <input type="hidden" name="action" value="remove">
                                    <input type="hidden" name="productId" value="<c:out value='${item.productId}'/>">
                                    <button type="submit" class="remove">Remove</button>
                                </form>
                            </div>
                        </article>
                    </c:forEach>
                </section>

                <aside class="summary">
                    <h2>Order Summary</h2>
                    <div class="muted">Items: <c:out value="${cartItems.size()}"/></div>
                    <div class="total">
                        <span>Total</span>
                        <span>₹<c:out value="${cartTotal}"/></span>
                    </div>
                    <p class="muted">Shipping and any applicable discounts will be calculated at checkout.</p>
                    <a class="button checkout"
                       href="${pageContext.request.contextPath}/checkout">Proceed to Checkout</a>
                </aside>
            </div>
        </c:when>
        <c:otherwise>
            <section class="empty">
                <h2>Your cart is empty</h2>
                <p class="muted">Explore our products and add something you love.</p>
                <a class="button" href="${pageContext.request.contextPath}/products">Browse Products</a>
            </section>
        </c:otherwise>
    </c:choose>

    <a class="continue" href="${pageContext.request.contextPath}/products">← Continue shopping</a>
</main>
</body>
</html>
