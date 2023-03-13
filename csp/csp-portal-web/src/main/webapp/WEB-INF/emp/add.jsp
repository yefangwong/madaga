<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  History
  --------------------------------------------
  2023/2/1 yfwong inital version.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <link rel="stylesheet" href="resources/css/style.css" type="text/css">
    <title>新增員工</title>
</head>
<body>
    <div id="container">
        <form action="add">
            <div class="align">
                <label>編號</label>
                <input type="text" placeholder="請輸入編號" name="number" />
            </div>
            <div class="align">
                <label>姓名</label>
                <input type="text" placeholder="請輸入姓名" name="name" />
            </div>
            <div class="align">
                <label>性別</label>
                <input type="radio" value="男" name="gender" />男
                <input type="radio" value="女" name="gender" />女
            </div>
            <div class="align">
                <label>年齡</label>
                <input type="text" placeholder="請輸入年齡" name="age" />
            </div>
            <div class="align">
                <label>部門</label>
                <select name="dep.id">
                    <c:forEach var="dep" items="${depList}">
                        <option value="${dep.id}">${dep.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="align">
                <button type="submit">存檔</button>
            </div>
        </form>
    </div>
</body>
</html>
