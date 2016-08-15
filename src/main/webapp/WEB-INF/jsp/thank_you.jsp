<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<div id="divThankYou" style="height:100%;overflow:auto;">
<table>
	<tr>
		<td colspan="2" class="ui-widget-header">
			&nbsp;<spring:message  code="thankyou.headline" />&nbsp;						
		</td>		
	</tr>
	<tr>
		<td style="width:5px;border:none;" class="ui-widget-content">&nbsp;</td>
		<td style="border:none;" class="ui-widget-content">
			&nbsp;<spring:message  code="thankyou.message" />&nbsp;
		</td>
	</tr>
	
</table>	
</div>

