<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<table class="mlt-footer">
	<tr>
		<td class="ui-widget-content">
			&nbsp;
			<img style="vertical-align: middle;width: 50px;" 
				src="img/logomaker/logo_150_73.png" />
			&copy; 2009 by 			
			<a href="mailto:support@mylivetracker.de">MyLiveTracker.de</a> 
			&nbsp;&#8226;&nbsp;
			<c:out value="${version}" />
			&nbsp;&#8226;&nbsp;
			<spring:message code="layout.timezone" text="Timezone"/>: <c:out value="${timezone}"/>
			&nbsp;&#8226;&nbsp;
			<a href="http://forum.mylivetracker.info">MyLiveTracker Forum</a>
			&nbsp;&#8226;&nbsp;
			<a href="credits.sec"><spring:message code="layout.credits"/></a>
			&nbsp;&#8226;&nbsp;
			<a href="impressum.sec"><spring:message code="layout.impressum"/></a>
			&nbsp;				
		</td>				
	</tr>
</table>