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
<!-- Fix CVE-2015-9251(6.1), CVE-2019-11358(6.1), CVE-2020-11023(6.1), CVE-2020-11022(6.1) Begin -->
<script src="<c:url value='/scripts/jquery/jquery-3.0.0.min.js' />"></script>
<!-- Fix CVE-2015-9251(6.1), CVE-2019-11358(6.1), CVE-2020-11023(6.1), CVE-2020-11022(6.1) End -->
<!-- Angular JavaScript -->
<!-- Fix CVE-2020-7676(5.4), Prototype pollution(6.9) Begin -->
<!--<script src="<c:url value='/scripts/angular/angular.min.js' />"></script>-->
<!-- Fix CVE-2020-7676(5.4), Prototype pollution(6.9) End -->
<!-- Kendo UI combined JavaScript -->
<script src="<c:url value='/scripts/kendo/kendo.all.min.js' />"></script>
<sitemesh:write property='head' />
</head>
<body>
	<sitemesh:write property='body' />
</body>
</html>