<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<%@ page import="java.util.List" %>
<%@ page import="org.apache.commons.lang.StringUtils" %>
<%@ page import="de.msk.mylivetracker.Global" %>
<%@ page import="de.msk.mylivetracker.web.options.StrOptionDsc" %>
<%@ page import="de.msk.mylivetracker.web.util.WebUtils" %>
<%@ page import="de.msk.mylivetracker.domain.user.UserWithRoleVo" %>

<html> 
<head>	
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<title>MyLiveTracker</title>		
	<tiles:insertAttribute name="style_and_scripts" />		
	<style type="text/css">
		body, div, table { font-size: small; font-family: Tahoma,Arial,sans-serif; }
		.ui-state-error { border:0px!important; }
		.mlt-button { font-size: 100%; font-family: Tahoma,Arial,sans-serif; font-weight: bold;}
		.mlt-dialog { font-size: 80%; font-family: Tahoma,Arial,sans-serif; line-height: normal;}
		table { width: 100%; }
		.mlt-header td { border:0px; }
		.mlt-content td { border:1px solid #E6E6E6; }
		.mlt-content th { border:1px solid #E6E6E6; }
		.mlt-footer td { border:0px; font-size: x-small; font-family: Tahoma,Arial,sans-serif; }
	
		/* Angaben für alle standardkonformen Browser */
		body
		{
			margin:0;
			padding:0;
		}
		#divContent
		{
			padding-top:42px;
			padding-bottom:1em;
			padding-left:0;
		}
		#divHeader
		{
			position:fixed;
			top:0px; left:0px; right:0px;
			background: #f2f2f2;
			text-align:center;
		}
		#divFooter
		{
			position:fixed;
			bottom:0px; left:0px; right:0px;
			text-align:center;
			background: #f2f2f2;
			padding:3px;
		}
	</style>	
</head>

<body>

<script>
	function changeLanguage() {
		var locale = document.getElementById("locale").value;
		var path = window.location.pathname;
		window.location.href = path + "?locale=" + locale;		
	}
	$(document).ready(function () {					
		$("button, input:submit, input:button, a", ".mlt-button").button();			
	});		
</script>
<%
	String version = Global.getVersion();
	request.setAttribute("version", version);
	
	String applicationName = WebUtils.getApplicationName(request);
	request.setAttribute("applicationName", applicationName);
	String applicationNameWidth = "120px";
	if (StringUtils.contains(applicationName, "Evaluation")) {
		applicationNameWidth = "240px";
	}
	request.setAttribute("applicationNameWidth", applicationNameWidth);
	
	String applicationBaseUrl = WebUtils.getApplicationBaseUrl(request);
	request.setAttribute("applicationBaseUrl", applicationBaseUrl);

	UserWithRoleVo currentUser = WebUtils.getCurrentUserWithRole();
	request.setAttribute("currentUser", currentUser);
	
	if (currentUser != null) {
		Boolean isGuest = currentUser.getRole().equals(UserWithRoleVo.UserRole.Guest);
		request.setAttribute("isGuest", isGuest);
		Boolean isAdmin = currentUser.getRole().equals(UserWithRoleVo.UserRole.Admin);
		request.setAttribute("isAdmin", isAdmin);
		Boolean isLoggedInAsAdmin = currentUser.isLoggedInAsAdmin();
		request.setAttribute("isLoggedInAsAdmin", isLoggedInAsAdmin);		
	}
	
	String timezone = WebUtils.getTimezone(request);
	request.setAttribute("timezone", timezone);
	
	String language = WebUtils.getLanguage(request);
	request.setAttribute("locale", language);
	
	List<StrOptionDsc> userOptsLanguage =
		WebUtils.getUserOptsLanguage(request);
	request.setAttribute("userOptsLanguage", userOptsLanguage);	
%>
<div id="divHeader" class="mlt-header" >
	<tiles:insertAttribute name="header" />
</div>
<div id="divContent" style="width:100%" class="mlt-content" >
	<tiles:insertAttribute name="content" />
</div>
<div id="divFooter" class="mlt-footer">
	<tiles:insertAttribute name="footer" />
</div>
</body>
</html>