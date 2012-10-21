<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<script type="text/javascript">
	function submitLoginData() {
		document.forms["formLogin"].elements["plainUserId"].value = 
			document.getElementById("inputPlainUserId").value;
		document.forms["formLogin"].elements["plainPassword"].value = 
			document.getElementById("inputPlainPassword").value;
		document.forms["formLogin"].submit();
	}
		
	function usernameFocus() {
		document.getElementById("inputPlainUserId").focus();
	}	
	function checkEnter(e) {
	  	var keyCode=(e)? e.which :event.keyCode;
	  	if(keyCode==13) {
	  		submitLoginData();
	  	}
	}
	function resetLoginFields() {
		document.forms["formLogin"].reset();
		document.getElementById("inputPlainUserId").value = "";
		document.getElementById("inputPlainPassword").value = "";
		usernameFocus();
	}
	
	document.onkeypress=checkEnter;		
	
	$(function() {
		$("#accordion").accordion({ active: 0 });				 	
	});		
</script>

<div id="divLogin">
<hr/>
<table>
    <tr style="text-align:left">
    	<td style="border:none;">&nbsp;<img style="vertical-align: middle;" 
    		src="img/logomaker/logo_150_73.png" height="50px" 
    		/>&nbsp;&nbsp;</td>
    	<td rowspan="2" style="border:none;" class="ui-widget-content">
    		<spring:message code="login.intro" />    		
    	</td>
    </tr>
    <tr>
    	<td style="border:none;text-align:center;">
    		<c:set var="selLanguage">${locale}</c:set>
			<select id="locale" name="locale" class="ui-state-default" 
				style="vertical-align: middle" onchange="javascript:changeLanguage();"> 
				<c:forEach var="language" items="${userOptsLanguage}">
					<c:choose>
   						<c:when test="${language.value == selLanguage}">
					    	<option value="${language.value}" selected>
					        	<spring:message code="${language.label}" />
					      	</option>
					    </c:when>
				    	<c:otherwise>
				      		<option value="${language.value}">
					        	<spring:message code="${language.label}" />
					      	</option>
					    </c:otherwise>
					</c:choose>
				</c:forEach>
			</select>
    	</td>
    </tr>
</table>
<hr/>
<div style="height:0px;overflow:hidden">
<form id="formLogin" name="formLogin" action="login" method="POST" >
	<input type='hidden' name='plainUserId' value='' >
	<input type='hidden' name='plainPassword' value='' >
</form>
</div>
<table> 
	<tr>
		<td style="height:30px;width:100px;border:none;" 
			class="ui-widget-header">
			&nbsp;<spring:message code="login.please" />&nbsp;
		</td>
		<c:choose>
			<c:when test="${!empty loginCmd.loginResultMessage}">
				<td class="ui-state-error ui-widget-header" colspan="3" style="border:none;">
					&nbsp;<c:out value="${loginCmd.loginResultMessage}" />&nbsp;					
				</td>		
			</c:when>
			<c:otherwise>
				<td colspan="3" class="ui-widget-header" style="border:none;">&nbsp;</td>
			</c:otherwise>
		</c:choose>
	</tr>	
    <tr>
    	<td style="height:30px;width:100px;border:none;" class="ui-widget-content">&nbsp;<spring:message code="login.username" />&nbsp;</td>
    	<td style="width:230px;border:none;text-align:center">
    		&nbsp;<input tabindex="1" style="width: 220px;" type='text'
    			class="text ui-widget-content ui-corner-all" 
    			id='inputPlainUserId' name='inputPlainUserId' value=''>&nbsp;
    	</td>
    	<td style="width:150px;text-align:center;border:none;">
    		<div class="mlt-button">
				&nbsp;<a href="#" style="width:120px;" 
					onclick="javascript:submitLoginData();" >
					<spring:message code="login.login" />
				</a>&nbsp;														
			</div>    			    				
    	</td>
    	<td rowspan="2" style="height:60px;border:none;" class="ui-widget-content">
    		<spring:message code="login.paypal.button" />
    	</td>   	  	
    </tr>
    <tr>
    	<td style="height:30px;width:100px;border:none;" class="ui-widget-content">&nbsp;<spring:message code="login.password" />&nbsp;</td>
    	<td style="width:230px;border:none;text-align:center">
    		&nbsp;<input tabindex="2" style="width: 220px;" type='password'
    			class="text ui-widget-content ui-corner-all" 
    			id='inputPlainPassword' name='inputPlainPassword'/>&nbsp;
    	</td>
    	<td style="width:140px;text-align:center;border:none;">
    		<div class="mlt-button">
				&nbsp;<a href="#" style="width:120px;" 
					onclick="javascript:resetLoginFields();" >
					<spring:message code="login.reset" />
				</a>&nbsp;														
			</div>    		
    	</td>        	    
   	</tr>
   	<tr>
   		<td colspan="3" style="border:none;white-space: nowrap;" class="ui-widget-content">
   			&nbsp;<spring:message code="login.demo" arguments="${loginCmd.autoLoginUrlForDemoGuest}"/>&nbsp;
   		</td>
   	</tr>
</table>
<script>usernameFocus();</script>
<hr/>
<table>
	<tr>
		<th colspan="3" class="ui-widget-header" style="height:30px;text-align:left;border:none;white-space: nowrap;">&nbsp;<spring:message code="login.supported.clients" />&nbsp;</th>
	</tr>   	   
    <tr style="text-align:left;">
    	<td style="border:none;white-space: nowrap;" class="ui-widget-content">
    		&nbsp;&bull;&nbsp;Apple iPhone via <a target="_blank" href="http://itunes.apple.com/app/sendlocation/id377724446">Send Location</a>
    		&nbsp;&nbsp;
    	</td>
    	<td style="border:none;white-space: nowrap;" class="ui-widget-content">
    		&nbsp;&bull;&nbsp;Nokia S60 Mobiles via <a target="_blank" href="http://www.aspicore.com/en/tuotteet_tracker.asp?tab=2&sub=1">Aspicores GSM Tracker ab V3</a>
    	</td>
    	<td style="border:none;width:100%;">&nbsp;</td>    	    	
    </tr>
    <tr style="text-align:left">
    	<td style="border:none;white-space: nowrap;" class="ui-widget-content">
    		&nbsp;&bull;&nbsp;Apple iPhone via <a target="_blank" href="http://itunes.apple.com/app/id413656652">YourTracks</a>
    		&nbsp;&nbsp;
    	</td>
    	<td style="border:none;white-space: nowrap;" class="ui-widget-content">
    		&nbsp;&bull;&nbsp;Nokia S60 Mobiles via <a target="_blank" href="http://www.afischer-online.de/sos/AFTrack/aftrack_de.html">A. Fischers AFTrack</a>
    	</td>
    	<td style="border:none;width:100%;">&nbsp;</td>    	    	    	
    </tr>
    <tr style="text-align:left">
    	<td style="border:none;white-space: nowrap;" class="ui-widget-content">
    		&nbsp;&bull;&nbsp;GSM Tracker <a target="_blank" href="http://www.xexun.com/ProductDetail.aspx?id=102">Xexun TK102-2</a>
    		&nbsp;&nbsp;
    	</td>
    	<td style="border:none;white-space: nowrap;" class="ui-widget-content">
    		&nbsp;&bull;&nbsp;GSM Tracker 
    		<a target="_blank" href="http://www.gpsvision.de/index.php/produkte/gps-tracker-tk5000">Incutex TK5000</a>&nbsp;/&nbsp;<a target="_blank" href="http://www.wondeproud.com/product_3_2_1.html">Wonde Proud SPT10</a>
    	</td>
    	<td style="border:none;width:100%;">&nbsp;</td>    	
    </tr>
    <tr style="text-align:left">
    	<td style="border:none;white-space: nowrap;" class="ui-widget-content">
    		&nbsp;&bull;&nbsp;Meiligao GSM Tracker <a target="_blank" href="http://www.meitrack.net/products/gps-personal-tracker">VT310, GT60, GT30X</a>
    		&nbsp;&nbsp;
    	</td>
    	<td style="border:none;white-space: nowrap;" class="ui-widget-content">
    		&nbsp;&bull;&nbsp;BlackBerry via <a target="_blank" href="http://www.brandsemotion.de/Produkte/beGPS/">brandsemotion beGPS</a>
    	</td>
    	<td style="border:none;width:100%;">&nbsp;</td>    	
    </tr>  
    <tr style="text-align:left">
    	<td style="border:none;white-space: nowrap;" class="ui-widget-content">
    		&nbsp;&bull;&nbsp;Android Smartphone <a target="_blank" href="https://play.google.com/store/apps/details?id=com.wiebej.gps2opengts">GPS2OpenGTS Pro</a>
    		&nbsp;&nbsp;
    	</td>
    	<td style="border:none;white-space: nowrap;" class="ui-widget-content">
    		&nbsp;&bull;&nbsp;Android Smartphone <a target="_blank" href="https://play.google.com/store/apps/details?id=de.msk.mylivetracker.client.android">MyLiveTracker</a>
    	</td>
    	<td style="border:none;width:100%;">&nbsp;</td>    	
    </tr>      
</table>
<hr/>
<table>
	<tr>
		<th colspan="2" class="ui-widget-header" style="height:30px;text-align:left;border:none;white-space: nowrap;">&nbsp;<spring:message code="login.twitter" />&nbsp;</th>
	</tr>  
	<c:forEach items="${loginCmd.twitterMessages}" var="twitterMessage">
		<tr>
			<td style="border:none;white-space: nowrap;" class="ui-widget-content">
			&nbsp;&nbsp;&bull;&nbsp;<c:out value="${twitterMessage.timestamp}"/>:&nbsp;<c:out value="${twitterMessage.message}"/>
			</td>
			<td style="border:none;width:100%;">&nbsp;</td>
		</tr>
    </c:forEach>
	<tr>
		<td style="border:none;white-space: nowrap;" class="ui-widget-content">
		&nbsp;&nbsp;&bull;&nbsp;<a style="text-decoration: none;font-style:italic;color:blue;" 
		href="http://twitter.com/MyLiveTrackerDe"><spring:message code="login.twitter.more"/></a>
		</td>
		<td style="border:none;width:100%;">&nbsp;</td>
	</tr>
</table>
</div>
