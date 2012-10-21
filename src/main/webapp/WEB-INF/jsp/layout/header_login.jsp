<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="/WEB-INF/tld/mlt.tld" prefix="mlt" %>

<script type="text/javascript">	
	$(document).ready(function () {
		$("#myLiveTracker").buttonset();
		$("#headerNav").buttonset();
		$("#headerBack").buttonset();
	});	
</script>

<table style="padding: 0px;border-spacing: 0px;">	
	<tr>
		<td style="white-space: nowrap; text-align:left;height:40px;font-size: large;">
			&nbsp;<spring:message code="login.headline" />&nbsp;
		</td>								
	</tr>	
</table>
