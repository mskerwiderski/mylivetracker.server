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
	});	
</script>

<table style="padding: 0px;border-spacing: 0px;">	
	<tr>
		<td style="white-space: nowrap; text-align:left">
			<table style="padding: 0px;border-spacing: 0px;">
				<tr>
					<td style="padding: 0px;border-spacing: 0px;font-weight: bold; white-space:nowrap;">
						<spring:message code="layout.loggedin.user"
							argumentSeparator="," 
							arguments="${currentUser.masterData.firstName},${currentUser.masterData.lastName},${currentUser.userId}"
						/>
					</td>
				</tr>
				<tr>	
					<td style="padding: 0px;border-spacing: 0px;white-space:nowrap;font-size: small;">
						<c:choose>
							<c:when test="${isGuest}">
								<spring:message code="layout.loggedin.as.guest" />
							</c:when>
							<c:when test="${isLoggedInAsAdmin}">
								<spring:message 
									code="layout.loggedin.as.admin" 
									arguments="${currentUser.adminUsername}" 
								/>
							</c:when>
							<c:otherwise>
								&nbsp;
							</c:otherwise>
						</c:choose>
					</td>
				</tr>
			</table>			
		</td>
		<td style="width:100%;">			
		</td>
		<td style="text-align:right; white-space: nowrap;">								
			<div id="headerNav" class="mlt-button" >								
				<security:authorize ifAnyGranted="Admin,User,Guest">
					<input type="radio" 
						id="tracksOverview" 
						name="radio"						
						onclick="javascript:window.location.href='tracks_overview.do';" 
						/>
					<label for="tracksOverview" style="width: 125px;" >
						<spring:message code="layout.overview" text="Overview" />
					</label>
				</security:authorize>
				<security:authorize ifAnyGranted="Admin,User">			
					<input type="radio"  
						id="options" 
						name="radio"						
						onclick="javascript:window.location.href='options.do';" 
						/>
					<label for="options" style="width: 125px;">
						<spring:message code="layout.options" text="Options" />
					</label>
				</security:authorize>
				<security:authorize ifAnyGranted="Admin">
					<input type="radio" 
						id="admin" 
						name="radio"
						onclick="javascript:window.location.href='admin.do';" 
						/>
					<label for="admin" style="width: 125px;" >
						<spring:message code="layout.admin" text="Admin" />
					</label>
				</security:authorize>
				<security:authorize ifAnyGranted="Admin,User,Guest">
					<input type="radio" 
						id="logout" 
						name="radio"
						onclick="javascript:window.location.href='logout';" 
						/>
					<label for="logout" style="width: 125px;" >
						<spring:message code="layout.logout" text="Logout" />
					</label>
				</security:authorize>							
			</div>						
		</td>								
	</tr>	
</table>
