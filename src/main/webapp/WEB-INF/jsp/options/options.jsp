<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<script type="text/javascript">
	function actionExecute(actionExecutor) {
		document.forms["optionsForm"].elements["actionExecutor"].value = actionExecutor;
		document.forms["optionsForm"].method = "post";
		document.forms["optionsForm"].submit();
	}	
	$(function() {
		$("#divOptions").tabs();
		$("#divOptions").tabs( 'select' , <c:out value='${optionsCmd.currentTabId}' />)

		$('#divOptions').tabs({
		   	select: function(event, ui) {			   						
				document.forms["optionsForm"].elements["currentTabId"].value = ui.index; 
			}
		});
	});
	
</script>
	
<div id="divOptions">
	<form:form id="optionsForm" name="optionsForm" 
		commandName="optionsCmd">
	
	<form:hidden path="actionExecutor"/>
	<form:hidden path="currentTabId"/>
	
	<ul>
		<li><a href="#tabUserMasterData"><spring:message code="masterdata.tab" /></a></li>
		<li><a href="#tabUserOptions"><spring:message code="options.tab" /></a></li>
		<li><a href="#tabSenderMaintenance"><spring:message code="sendermaintenance.tab" /></a></li>
		<li><a href="#tabStatusPage"><spring:message code="statuspage.tab" /></a></li>
		<li><a href="#tabMaps"><spring:message code="maps.tab" /></a></li>
		<li><a href="#tabEmergency"><spring:message code="emergency.tab" /></a></li>
		<li><a href="#tabServerInfo"><spring:message code="serverinfo.tab" /></a></li>
	</ul>

	<div id="tabUserMasterData" style="overflow:scroll;height:80%;border-bottom: none;">
		<tiles:insertAttribute name="user-master-data" />
	</div>
	<div id="tabUserOptions" style="overflow:scroll;height:80%;border-bottom: none;">	
		<tiles:insertAttribute name="user-options" />
	</div>	
	<div id="tabSenderMaintenance" style="overflow:scroll;height:80%;border-bottom: none;">
		<tiles:insertAttribute name="sender-maintenance" />
	</div>
	<div id="tabStatusPage" style="overflow:scroll;height:80%;border-bottom: none;">
		<tiles:insertAttribute name="status-page" />
	</div>
	<div id="tabMaps" style="overflow:scroll;height:80%;border-bottom: none;">
		<tiles:insertAttribute name="maps" />
	</div>
	<div id="tabEmergency" style="overflow:scroll;height:80%;border-bottom: none;">
		<tiles:insertAttribute name="emergency" />
	</div>
	<div id="tabServerInfo" style="overflow:scroll;height:80%;border-bottom: none;">
		<tiles:insertAttribute name="server-info" />
	</div>	
	</form:form>
</div>



