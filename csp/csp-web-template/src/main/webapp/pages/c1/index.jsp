<%@ page language="java" pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div>
	<h1>Hello, Everyone!</h1>
</div>
<!-- HTML element from which the DatePicker would be initialized -->
<input id="datepicker" />
<script>
$(function() {
    // Initialize the Kendo UI DatePicker by calling the kendoDatePicker jQuery plugin
    $("#datepicker").kendoDatePicker();
});
</script>