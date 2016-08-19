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
		$("#myLiveTracker").controlgroup();
		$("#headerNav").controlgroup();
		$("#headerBack").controlgroup();
	});	
</script>

<table style="padding: 0px;border-spacing: 0px;">	
	<tr>
		<td style="white-space: nowrap; text-align:left;height:40px;">
			<table style="padding: 0px;border-spacing: 0px;">
				<c:choose>
					<c:when test="${empty currentUser}">
						<tr>
							<td>
								&nbsp;<img style="vertical-align: middle;width: 65px;" 
									src="img/logomaker/logo_150_73.png" />&nbsp;
							</td>
							<td style="width:100%">
								&nbsp;
							</td>
						</tr>
					</c:when>
					<c:when test="${isGuest}">
						<tr>
							<td>&nbsp;</td>
							<td>
								&nbsp;<img style="vertical-align: middle;width: 65px;" 
									src="img/logomaker/logo_150_73.png" />&nbsp;
							</td>
							<td style="padding: 0px;border-spacing: 0px;font-weight: bold; white-space:nowrap;">
								<spring:message code="layout.loggedin.as.guest" />&nbsp;<spring:message code="layout.loggedin.user"
									argumentSeparator="," 
									arguments="${currentUser.masterData.firstName},${currentUser.masterData.lastName},${currentUser.userId}"
								/>
							</td>
						</tr>
					</c:when>
					<c:when test="${isLoggedInAsAdmin}">
						<tr>
							<td>&nbsp;</td>
							<td>
								&nbsp;<img style="vertical-align: middle;width: 65px;" 
									src="img/logomaker/logo_150_73.png" />&nbsp;
							</td>
							<td style="padding: 0px;border-spacing: 0px;font-weight: bold; white-space:nowrap;">
								&nbsp;<spring:message code="layout.loggedin.as.admin" 
									arguments="${currentUser.adminUsername}" />&nbsp;<spring:message code="layout.loggedin.user"
									argumentSeparator="," 
									arguments="${currentUser.masterData.firstName},${currentUser.masterData.lastName},${currentUser.userId}"
								/>
							</td>
						</tr>
					</c:when>
					<c:otherwise>
						<tr>
							<td>
								&nbsp;<img style="vertical-align: middle;width: 65px;" 
									src="img/logomaker/logo_150_73.png" />&nbsp;
							</td>
							<td style="padding: 0px;border-spacing: 0px;font-weight: bold; white-space:nowrap;">
								&nbsp;<spring:message code="layout.loggedin.user"
									argumentSeparator="," 
									arguments="${currentUser.masterData.firstName},${currentUser.masterData.lastName},${currentUser.userId}"
								/>&nbsp;
							</td>
							<td>
								<div style="margin-bottom: -15px">
								<spring:message code="common.paypal.button" />
								</div>
							</td>
						</tr>
					</c:otherwise>
				</c:choose>
			</table>			
		</td>
		<td style="text-align:right; white-space: nowrap;width:100%">		
			<div id="headerNav" class="mlt-button">			
				<c:choose>
					<c:when test="${empty currentUser}">
						<div id="headerNav" class="mlt-button">
							<button class="mlt-button" style="width: 125px;" onclick="javascript:window.location.href='tracks_overview.do';">
								<spring:message code="layout.back" text="Back" />
							</button>
						</div>
					</c:when>
				</c:choose>
				<security:authorize ifAnyGranted="Admin,User,Guest">
					<button class="mlt-button" style="width: 125px;" onclick="javascript:window.location.href='tracks_overview.do';">
						<spring:message code="layout.overview" text="Overview" />
					</button>
				</security:authorize>
				<security:authorize ifAnyGranted="Admin,User">
					<button class="mlt-button" style="width: 125px;" onclick="javascript:window.location.href='options.do';">
						<spring:message code="layout.options" text="Options" />
					</button>			
				</security:authorize>
				<security:authorize ifAnyGranted="Admin">
					<button class="mlt-button" style="width: 125px;" onclick="javascript:window.location.href='admin.do';">
						<spring:message code="layout.admin" text="Admin" />
					</button>
				</security:authorize>
				<security:authorize ifAnyGranted="Admin">
					<button class="mlt-button" style="width: 125px;" onclick="javascript:window.location.href='registration.do';">
						<spring:message code="layout.registration" text="Registration" />
					</button>
				</security:authorize>
				<security:authorize ifAnyGranted="Admin,User,Guest">
					<button class="mlt-button" style="width: 125px;" onclick="javascript:window.location.href='logout';">
						<spring:message code="layout.logout" text="Logout" />
					</button>
				</security:authorize>		
			</div>						
		</td>	
		<td>&nbsp;</td>							
	</tr>	
</table>
