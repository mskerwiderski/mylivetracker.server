<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="/WEB-INF/tld/mlt.tld" prefix="mlt" %>

<jsp:directive.page import="de.msk.mylivetracker.web.util.WebUtils" />
<% 
	String applicationBaseUrl = WebUtils.getApplicationBaseUrl(request);
	request.setAttribute("applicationBaseUrl", applicationBaseUrl);
%>

<script>	
	function registerUser() {
		document.forms["formUserRegistration"].submit();		
	}
</script>

<div id="divUserRegistration" style="overflow:scroll;height:80%;border-bottom: none;">
<form id="formUserRegistration" name="formUserRegistration" action="registration.do" method="POST" >
<div>
<table>
	<tr>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;User id&nbsp;
		</td>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;<input id='userId' name='userId' value='' size="30">&nbsp;
		</td>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;Last name&nbsp;
		</td>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;<input id='lastName' name='lastName' value='' size="30">&nbsp;
		</td>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;First name&nbsp;
		</td>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;<input id='firstName' name='firstName' value='' size="30">&nbsp;
		</td>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;Email address&nbsp;
		</td>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;<input id='emailAddress' name='emailAddress' value='' size="30">&nbsp;
		</td>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;Language ('en' or 'de')&nbsp;
		</td>
		<td style="width:200px;text-align:left;white-space: nowrap;">
			&nbsp;<input id='language' name='language' value='en' size="10">&nbsp;
		</td>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
	<tr>
		<td colspan="2" style="width:400px;text-align:left;white-space: nowrap;">
			<div class="mlt-button">
				&nbsp;<a onclick='javascript:registerUser();' >
					Register new User
				</a>&nbsp;														
			</div>					
		</td>
		<th style="width:100%;">
			&nbsp;&nbsp;							
		</th>
	</tr>
</table>
</div>
</form>
</div>

