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
    <title>員工展示</title>
</head>
<body>
    <div id="container">
        <form id="search" action="search" method="post">
            <div class="align">
                <input type="text" name="number" placeholder="編號" value="${c.number}" />
            </div>
            <div class="align">
                <input type="text" name="name" placeholder="姓名" value="${c.name}" />
            </div>
            <div class="align">
                <select name="gender" id="">
                    <option value="">性別</option>
                    <option value="男" <c:if test="${c.gender == '男'}"> selected</c:if>>男</option>
                    <option value="女" <c:if test="${c.gender == '女'}"> selected</c:if>>女</option>
                </select>
            </div>
            <div class="align">
                <input type="text" name="age" placeholder="年齡" value="${c.age != null ? c.age : ''}">
            </div>
            <div class="align">
                <select name="dep.id">
                    <option value="">部門</option>
                    <c:forEach items="${depList}" var="dep">
                        <option value="${dep.id}" <c:if test="${dep.id == c.dep.id}">selected</c:if>>
                            ${dep.name}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <div class="align">
                <button type="submit">搜尋</button>
            </div>
        </form>
        <table id="data" border="1">
            <tr>
                <th>編號</th>
                <th>性名</th>
                <th>姓別</th>
                <th>年齡</th>
                <th>部門</th>
            </tr>
            <c:forEach items="list" var="data">
                <tr>
                    <td>${data.number}</td>
                    <td>${data.name}</td>
                    <td>${data.gender}</td>
                    <td>${data.age}</td>
                    <td>${data.dep.name}</td>
                </tr>
            </c:forEach>
        </table>
        <button type="button" id="add">新增</button>
        <button type="button" id="update">修改</button>
        <button type="button" id="delete">刪除</button>
    </div>
</body>
</html>
