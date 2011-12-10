<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<%@ page import="de.msk.mylivetracker.web.util.WebUtils" %>
<%@ page import="de.msk.mylivetracker.domain.user.UserWithRoleVo" %>
 
<html> 
<head>
	<meta http-equiv="content-type" content="text/html; charset=UTF-8" />
	<title>MyLiveTracker</title>	
	<tiles:insertAttribute name="style_and_scripts" />
	<style type="text/css">
		body, div, table { font-size: small; font-family: Tahoma,Arial,sans-serif; }
		.mlt-button { font-size: 85%; font-family: Tahoma,Arial,sans-serif; font-weight: bold;}
		.mlt-dialog { font-size: 80%; font-family: Tahoma,Arial,sans-serif; line-height: normal;}
		table { width: 100%; }
		.mlt-header td { border:0px; }
		.mlt-content td { border:1px solid #E6E6E6; }
		.mlt-content th { border:1px solid #E6E6E6; }
		.mlt-footer td { border:0px; font-size: x-small; font-family: Tahoma,Arial,sans-serif; }
	</style>
</head>
<body>

<div id="content" style="width:100%;height:100%" class="mlt-content">
	<tiles:insertAttribute name="content" />	
</div>

</body>
</html>