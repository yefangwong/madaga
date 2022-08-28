<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
<title><sitemesh:write property='title' /></title>
<!-- Common Kendo UI CSS for web widgets and widgets for data visualization. -->
<link rel="stylesheet" href="<c:url value='/styles/kendo/kendo.common.min.css' />" />
<!-- Default Kendo UI theme CSS for web widgets and widgets for data visualization. -->
<link rel="stylesheet" href="<c:url value='/styles/kendo/kendo.default.min.css' />" />
<!-- Kendo UI Hybrid CSS. Include only if you will use the mobile devices features. -->
<link rel="stylesheet" href="<c:url value='/styles/kendo/kendo.default.mobile.min.css' />" />
<script>
 	function getBaseUrl() {
 		return '${webapps.contextPath}';
 	}
</script>
<!-- jQuery JavaScript -->
<script src="<c:url value='/scripts/jquery/jquery-1.12.4.min.js' />"></script>
<!-- Angular JavaScript -->
<script src="<c:url value='/scripts/angular/angular.min.js' />"></script>
<!-- Kendo UI combined JavaScript -->
<script src="<c:url value='/scripts/kendo/kendo.all.min.js' />"></script>
<sitemesh:write property='head' />
</head>
<body>
	<sitemesh:write property='body' />
</body>
</html>