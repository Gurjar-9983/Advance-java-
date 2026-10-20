
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Product Management | ShopSphere Admin</title>

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
            gap: 20px;
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

        .heading {
            margin-bottom: 25px;
        }

        .heading h1 { margin-bottom: 8px; }

        .muted { color: #77758a; }

        .layout {
            display: grid;
            grid-template-columns: minmax(280px, 0.8fr) minmax(0, 1.7fr);
            gap: 24px;
            align-items: start;
        }

        .card {
            background: white;
            padding: 24px;
            border-radius: 15px;
            box-shadow: 0 5px 20px rgba(30, 25, 70, 0.05);
            border: 1px solid #eeeaf8;
            margin-bottom: 24px;
        }

        label {
            display: block;
            font-weight: bold;
            font-size: 14px;
            margin: 15px 0 7px;
        }

        input, textarea, select {
            width: 100%;
            padding: 11px 12px;
            border: 1px solid #ddd8eb;
            border-radius: 8px;
            font: inherit;
            background: white;
        }

        textarea {
            min-height: 85px;
            resize: vertical;
        }

        input:focus, textarea:focus, select:focus {
            outline: 2px solid #c9bdff;
            border-color: #6046e8;
        }

        .button {
            display: inline-block;
            padding: 11px 16px;
            border: 0;
            border-radius: 8px;
            background: #6046e8;
            color: white;
            text-decoration: none;
            cursor: pointer;
            font-size: 14px;
            font-weight: bold;
        }

        .button:hover { background: #4d35cb; }

        .button.secondary {
            background: #eeebfa;
            color: #6046e8;
        }

        .button.secondary:hover { background: #e2dcff; }

        .actions {
            display: flex;
            gap: 8px;
            align-items: center;
            flex-wrap: wrap;
        }

        .message {
            padding: 13px 16px;
            border-radius: 8px;
            background: #e8f9ed;
            color: #176b32;
            margin-bottom: 20px;
        }

        .table-wrap { overflow-x: auto; }

        table {
            width: 100%;
            min-width: 650px;
            border-collapse: collapse;
        }

        th, td {
            text-align: left;
            padding: 13px 10px;
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

        .inactive {
            background: #fff0f0;
            color: #a12727;
        }

        .empty {
            color: #77758a;
            padding: 18px 0;
        }

        @media (max-width: 950px) {
            .layout { grid-template-columns: 1fr; }
        }

        @media (max-width: 550px) {
            header { padding: 18px 4%; }
            .card { padding: 17px; }
        }
    </style>
</head>

<body>

<header>
    <h2>ShopSphere Admin</h2>
    <nav>
        <a href="${pageContext.request.contextPath}/admin">Dashboard</a>
        <a href="${pageContext.request.contextPath}/admin/products">Products</a>
        <a href="${pageContext.request.contextPath}/products">View Store</a>
    </nav>
</header>

<main class="container">

    <section class="heading">
        <h1>Product Management</h1>
        <p class="muted">Add products, update prices, manage inventory, and control product availability.</p>
    </section>

    <c:if test="${not empty sessionScope.adminProductMessage}">
        <div class="message">
            <c:out value="${sessionScope.adminProductMessage}"/>
        </div>
        <c:remove var="adminProductMessage" scope="session"/>
    </c:if>

    <div class="layout">

        <section class="card">
            <h2>
                <c:choose>
                    <c:when test="${not empty editProduct}">Edit Product</c:when>
                    <c:otherwise>Add New Product</c:otherwise>
                </c:choose>
            </h2>

            <p class="muted">Fields marked with * are required.</p>

            <form method="post"
                  action="${pageContext.request.contextPath}/admin/products">

                <input type="hidden" name="productId"
                       value="<c:out value='${editProduct.productId}'/>">

                <label for="name">Product Name *</label>
                <input id="name" name="name" type="text"
                       maxlength="200" required
                       value="<c:out value='${editProduct.name}'/>">

                <label for="brand">Brand</label>
                <input id="brand" name="brand" type="text"
                       maxlength="100"
                       value="<c:out value='${editProduct.brand}'/>">

                <label for="description">Description</label>
                <textarea id="description" name="description"><c:out value="${editProduct.description}"/></textarea>

                <label for="price">Price (₹) *</label>
                <input id="price" name="price" type="number"
                       min="0.01" max="99999999.99" step="0.01"
                       required value="<c:out value='${editProduct.price}'/>">

                <label for="discount">Discount (%)</label>
                <input id="discount" name="discount" type="number"
                       min="0" max="100" step="0.01"
                       value="<c:out value='${editProduct.discount}'/>">

                <label for="stock">Stock Quantity *</label>
                <input id="stock" name="stock" type="number"
                       min="0" step="1" required
                       value="<c:out value='${editProduct.stock}'/>">

                <label for="imageUrl">Image URL</label>
                <input id="imageUrl" name="imageUrl" type="url"
                       maxlength="500"
                       value="<c:out value='${editProduct.imageUrl}'/>">

                <label for="status">Product Status *</label>
                <select id="status" name="status" required>
                    <option value="ACTIVE"
                        ${empty editProduct or editProduct.status eq 'ACTIVE' ? 'selected' : ''}>
                        Active
                    </option>
                    <option value="INACTIVE"
                        ${editProduct.status eq 'INACTIVE' ? 'selected' : ''}>
                        Inactive
                    </option>
                </select>

                <div class="actions" style="margin-top:22px">
                    <button class="button" type="submit">
                        <c:choose>
                            <c:when test="${not empty editProduct}">Save Changes</c:when>
                            <c:otherwise>Add Product</c:otherwise>
                        </c:choose>
                    </button>

                    <c:if test="${not empty editProduct}">
                        <a class="button secondary"
                           href="${pageContext.request.contextPath}/admin/products">
                            Cancel
                        </a>
                    </c:if>
                </div>
            </form>
        </section>

        <section class="card">
            <h2>All Products</h2>
            <p class="muted">Select Edit to update a product.</p>

            <div class="table-wrap">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Name</th>
                        <th>Brand</th>
                        <th>Price</th>
                        <th>Stock</th>
                        <th>Status</th>
                        <th>Action</th>
                    </tr>
                    </thead>

                    <tbody>
                    <c:choose>
                        <c:when test="${not empty products}">
                            <c:forEach var="product" items="${products}">
                                <tr>
                                    <td><c:out value="${product.productId}"/></td>
                                    <td><c:out value="${product.name}"/></td>
                                    <td><c:out value="${product.brand}"/></td>
                                    <td>₹<c:out value="${product.price}"/></td>
                                    <td><c:out value="${product.stock}"/></td>
                                    <td>
                                        <span class="badge ${product.status eq 'INACTIVE' ? 'inactive' : ''}">
                                            <c:out value="${product.status}"/>
                                        </span>
                                    </td>
                                    <td>
                                        <a class="button secondary"
                                           href="${pageContext.request.contextPath}/admin/products?edit=${product.productId}">
                                            Edit
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>

                        <c:otherwise>
                            <tr>
                                <td colspan="7" class="empty">No products found.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </section>

    </div>

</main>

</body>
</html>