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

<div id="divLogin" style="height:100%;overflow:auto;">
<table>   	
    <tr style="text-align:left">
    	<td>&nbsp;<img style="vertical-align: middle;" 
    		src="img/logo_150_73.png" height="50px" 
    		/>&nbsp;</td>
    	<td>
    		&nbsp;<spring:message code="login.intro" />&nbsp;    		
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
		<td style="height:30px;width:100px;font-weight: bold">
			&nbsp;<spring:message code="login.please" />&nbsp;
		</td>
		<c:choose>
			<c:when test="${!empty msg}">
				<td class="ui-state-error" colspan="3">
					&nbsp;<c:out value="${msg}" />&nbsp;						
				</td>		
			</c:when>
			<c:otherwise>
				<td colspan="3">&nbsp;</td>
			</c:otherwise>
		</c:choose>
		<td rowspan="3">    		
    		&nbsp;
    		<SCRIPT charset="utf-8" type="text/javascript" src="http://ws.amazon.de/widgets/q?rt=tf_sw&ServiceVersion=20070822&MarketPlace=DE&ID=V20070822/DE/alemicade-21/8002/1ac64fd1-f6a9-446a-9eb6-f74984731d3b"> </SCRIPT> <NOSCRIPT><A HREF="http://ws.amazon.de/widgets/q?rt=tf_sw&ServiceVersion=20070822&MarketPlace=DE&ID=V20070822%2FDE%2Falemicade-21%2F8002%2F1ac64fd1-f6a9-446a-9eb6-f74984731d3b&Operation=NoScript">Amazon.de Widgets</A></NOSCRIPT>
    		&nbsp;
    	</td>
	</tr>	
    <tr>
    	<td style="width:100px">&nbsp;<spring:message code="login.username" />&nbsp;</td>
    	<td style="width:230px;text-align:center">
    		&nbsp;<input tabindex="1" style="width: 220px;" type='text' id='inputPlainUserId' name='inputPlainUserId' value=''>&nbsp;
    	</td>
    	<td style="width:140px;text-align:center">
    		<div class="mlt-button">
				&nbsp;<a href="#" style="width:120px;" 
					onclick="javascript:submitLoginData();" >
					<spring:message code="login.login" />
				</a>&nbsp;														
			</div>    			    				
    	</td>
    	<td rowspan="2" style="width:140px;text-align:center;">    		
    		&nbsp;<div style="height:30px; margin-top: -15px; margin-left: 2px;"><spring:message code="login.paypal.button" /></div>&nbsp;
    	</td>      	  	
    </tr>
    <tr>
    	<td style="width:100px">&nbsp;<spring:message code="login.password" />&nbsp;</td>
    	<td style="width:230px;text-align:center">
    		&nbsp;<input tabindex="2" style="width: 220px;" type='password' id='inputPlainPassword' name='inputPlainPassword'/>&nbsp;
    	</td>
    	<td style="width:140px;text-align:center">
    		<div class="mlt-button">
				&nbsp;<a href="#" style="width:120px;" 
					onclick="javascript:resetLoginFields();" >
					<spring:message code="login.reset" />
				</a>&nbsp;														
			</div>    		
    	</td>        	    
   	</tr>
</table>
<hr/>
<script>usernameFocus();</script>
<div style="height:5px"></div>
<div id="accordion">
<h3><a href="#"><spring:message code="login.features" /></a></h3>
<div>
<table>   	
    <tr style="text-align:left">
    	<td>&nbsp;&bull;&nbsp;<spring:message code="login.feature1" />&nbsp;</td>
    </tr>
    <tr style="text-align:left">
    	<td>&nbsp;&bull;&nbsp;<spring:message code="login.feature2" />&nbsp;</td>
    </tr>
    <tr style="text-align:left">
    	<td>&nbsp;&bull;&nbsp;<spring:message code="login.feature3" />&nbsp;</td>
    </tr>
    <tr style="text-align:left">
    	<td>&nbsp;&bull;&nbsp;<spring:message code="login.feature4" />&nbsp;</td>
    </tr>    	
</table>
</div>
<h3><a href="#"><spring:message code="login.supported.clients" /></a></h3>
<div>
<table>   	   
    <tr style="text-align:left;">
    	<td nowrap="nowrap" style="width:400px;">&nbsp;&bull;&nbsp;Apple iPhone via <a href="http://itunes.apple.com/us/app/sendlocation/id377724446?mt=8">Send Location</a></td>
    	<td nowrap="nowrap" style="width:400px;">&nbsp;&bull;&nbsp;Nokia S60 Mobiles via <a href="http://www.aspicore.com/en/tuotteet_tracker.asp?tab=2&sub=1">Aspicores GSM Tracker ab V3</a>&nbsp;</td>
    	<td style="width:100%;">&nbsp;</td>    	    	
    </tr>
    <tr style="text-align:left">
    	<td nowrap="nowrap" style="width:400px;">&nbsp;&bull;&nbsp;Apple iPhone via <a href="http://itunes.apple.com/gb/app/id413656652?mt=8&ls=1">YourTracks</a></td>
    	<td nowrap="nowrap" style="width:400px;">&nbsp;&bull;&nbsp;Nokia S60 Mobiles via <a href="http://www.afischer-online.de/sos/AFTrack/aftrack_de.html">A. Fischers AFTrack</a>&nbsp;</td>
    	<td style="width:100%;">&nbsp;</td>    	    	    	
    </tr>
    <tr style="text-align:left">
    	<td nowrap="nowrap" style="width:400px;">&nbsp;&bull;&nbsp;GSM Tracker <a href="http://www.itakka.com">Tk102-2 (V3, V6 und V7)</a></td>
    	<td nowrap="nowrap" style="width:400px;">&nbsp;&bull;&nbsp;GSM Tracker <a href="http://www.gpsvision.de">Tk5000</a>&nbsp;</td>
    	<td style="width:100%;">&nbsp;</td>    	
    </tr>
    <tr style="text-align:left">
    	<td nowrap="nowrap" style="width:400px;">&nbsp;&bull;&nbsp;Meiligao GSM Tracker <a href="http://www.tracker-shop.eu">VT310, GT60, GT30X</a>&nbsp;</td>
    	<td nowrap="nowrap" style="width:400px;">&nbsp;&bull;&nbsp;BlackBerry via <a href="http://begps.de">brandsemotion beGPS</a>&nbsp;</td>
    	<td style="width:100%;">&nbsp;</td>    	
    </tr>  
    <tr style="text-align:left">
    	<td nowrap="nowrap" style="width:400px;">&nbsp;&bull;&nbsp;Android Smartphone <a href="https://market.android.com/details?id=com.wiebej.gps2opengts">GPS2OpenGTS Pro</a>&nbsp;</td>
    	<td nowrap="nowrap" style="width:400px;">&nbsp;&bull;&nbsp;Android Smartphone <a href="https://market.android.com/details?id=de.msk.mylivetracker.client.android">MyLiveTracker</a>&nbsp;</td>
    	<td style="width:100%;">&nbsp;</td>    	
    </tr>      
</table>
</div>
<h3><a href="#"><spring:message code="login.twitter" /></a></h3>
<div>
<table>	
	<tr>
    	<td nowrap="nowrap" style="width:400px;">
			&nbsp;<a href="http://www.twitter.com/MyLiveTrackerDe"
				style="text-decoration: none;">
				<img style="vertical-align: middle;" 
					src="http://twitter-badges.s3.amazonaws.com/twitter-a.png" 
					alt="MyLiveTrackerDe@twitter.com"/>
			</a>&nbsp;					
		</td>	
    <tr>
		<td nowrap="nowrap" style="width:100%;font-size: small;">		
			<c:forEach items="${twitterMessages}" var="twitterMessage">				
			&nbsp;&bull;&nbsp;<c:out value="${twitterMessage.timestamp}" />:&nbsp;<c:out value="${twitterMessage.message}" /><br>			
			</c:forEach>
			&nbsp;&bull;&nbsp;<a style="text-decoration: none;font-style:italic;color:blue;" 
				href="http://twitter.com/MyLiveTrackerDe"><spring:message code="login.twitter.more" /></a>
		</td>		
	</tr>
</table>	
</div>
</div>
</div>