<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Products | ShopSphere</title>

    <style>
        * {
            box-sizing: border-box;
        }

        body {
            margin: 0;
            font-family: Arial, Helvetica, sans-serif;
            background: #f5f7fb;
            color: #1f2937;
        }

        header {
            background: #ffffff;
            padding: 18px 6%;
            display: flex;
            justify-content: space-between;
            align-items: center;
            border-bottom: 1px solid #e5e7eb;
            gap: 20px;
            flex-wrap: wrap;
        }

        .logo {
            color: #4f46e5;
            font-size: 25px;
            font-weight: bold;
            text-decoration: none;
        }

        nav a {
            color: #374151;
            text-decoration: none;
            margin-left: 20px;
        }

        .hero {
            padding: 42px 6% 24px;
        }

        .hero h1 {
            margin-bottom: 8px;
            font-size: 32px;
        }

        .hero p {
            color: #6b7280;
        }

        .search-form {
            display: flex;
            gap: 10px;
            margin-top: 24px;
            max-width: 600px;
        }

        .search-form input {
            flex: 1;
            min-width: 0;
            padding: 13px;
            border: 1px solid #d1d5db;
            border-radius: 8px;
            font-size: 15px;
        }

        button {
            border: 0;
            background: #4f46e5;
            color: white;
            padding: 13px 20px;
            border-radius: 8px;
            cursor: pointer;
        }

        .products-section {
            padding: 12px 6% 50px;
        }

        .product-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 24px;
        }

        .product-card {
            background: #ffffff;
            border: 1px solid #e5e7eb;
            border-radius: 12px;
            padding: 22px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04);
        }

        .product-image {
            height: 145px;
            display: flex;
            justify-content: center;
            align-items: center;
            background: #eef2ff;
            border-radius: 9px;
            margin-bottom: 18px;
            color: #4f46e5;
            font-size: 42px;
        }

        .brand {
            color: #6b7280;
            font-size: 13px;
        }

        .product-card h2 {
            font-size: 18px;
            margin: 8px 0;
        }

        .description {
            color: #6b7280;
            font-size: 14px;
            min-height: 40px;
            line-height: 1.5;
        }

        .price {
            font-size: 21px;
            font-weight: bold;
            margin: 16px 0 8px;
        }

        .stock {
            color: #047857;
            font-size: 13px;
        }

        .empty-state {
            background: #ffffff;
            border-radius: 12px;
            padding: 35px;
            text-align: center;
            color: #6b7280;
        }

        footer {
            padding: 24px;
            text-align: center;
            color: #6b7280;
            border-top: 1px solid #e5e7eb;
            background: white;
        }

        @media (max-width: 600px) {
            header {
                padding: 16px 5%;
            }

            nav a {
                margin-left: 10px;
            }

            .hero h1 {
                font-size: 27px;
            }

            .search-form button {
                padding: 12px;
            }
        }
    </style>
</head>

<body>

<header>
    <a class="logo" href="${pageContext.request.contextPath}/products">
        ShopSphere
    </a>

    <nav>
        <a href="${pageContext.request.contextPath}/products">Products</a>
        <a href="${pageContext.request.contextPath}/cart">My Cart</a>
        <a href="#catalog">Catalog</a>
    </nav>
</header>

<main>
    <section class="hero">
        <h1>Discover Your Next Favourite</h1>
        <p>Explore our collection of everyday essentials and tech accessories.</p>

        <form class="search-form"
              action="${pageContext.request.contextPath}/products"
              method="get">
            <input
                type="search"
                name="q"
                placeholder="Search products or brands..."
                value="<c:out value='${searchKeyword}'/>"
                aria-label="Search products">

            <button type="submit">Search</button>
        </form>

        <c:if test="${param.added == '1'}">
            <p style="color:#047857;font-weight:bold;">
                Product added to your cart successfully!
                <a href="${pageContext.request.contextPath}/cart">View Cart</a>
            </p>
        </c:if>
    </section>

    <section class="products-section" id="catalog">
        <h2>Our Products</h2>

        <c:choose>
            <c:when test="${not empty products}">
                <div class="product-grid">

                    <c:forEach var="product" items="${products}">
                        <article class="product-card">
                            <div class="product-image" aria-hidden="true">
                                &#128722;
                            </div>

                            <div class="brand">
                                <c:out value="${product.brand}"/>
                            </div>

                            <h2>
                                <c:out value="${product.name}"/>
                            </h2>

                            <p class="description">
                                <c:out value="${product.description}"/>
                            </p>

                            <div class="price">
                                ₹<c:out value="${product.price}"/>
                            </div>

                            <div class="stock">
                                <c:choose>
                                    <c:when test="${product.stock > 0}">
                                        In stock: <c:out value="${product.stock}"/>
                                    </c:when>
                                    <c:otherwise>
                                        Out of stock
                                    </c:otherwise>
                                </c:choose>
                            </div>

                            <c:choose>
                                <c:when test="${product.stock > 0}">
                                    <form method="post"
                                          action="${pageContext.request.contextPath}/cart"
                                          style="margin-top:16px;display:flex;gap:8px;align-items:center;flex-wrap:wrap;">
                                        <input type="hidden" name="action" value="add">
                                        <input type="hidden" name="productId"
                                               value="<c:out value='${product.productId}'/>">
                                        <label for="qty-${product.productId}">Qty</label>
                                        <input id="qty-${product.productId}"
                                               type="number" name="quantity"
                                               value="1" min="1"
                                               max="<c:out value='${product.stock}'/>"
                                               style="width:65px;padding:10px;border:1px solid #d1d5db;border-radius:7px;">
                                        <button type="submit">Add to Cart</button>
                                    </form>
                                </c:when>
                                <c:otherwise>
                                    <button type="button" disabled
                                            style="margin-top:16px;background:#9ca3af;cursor:not-allowed;">
                                        Out of Stock
                                    </button>
                                </c:otherwise>
                            </c:choose>
                        </article>
                    </c:forEach>

                </div>
            </c:when>

            <c:otherwise>
                <div class="empty-state">
                    <h3>No products found</h3>
                    <p>Try another search term or check back later.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</main>

<footer>
    &copy; 2026 ShopSphere. All rights reserved.
</footer>

</body>
</html>
