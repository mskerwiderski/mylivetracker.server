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

<table>	
	<tr>
		<td style="white-space: nowrap">
			<div id="myLiveTracker" class="mlt-button">
				<input type="radio" 
						id="myLiveTrackerBtn" 
						name="radio"
						onclick="javascript:window.location.href='tracks_overview.do';" 
				/>
				<label for="myLiveTrackerBtn" style="width:130px;">
					<c:out value="${applicationName}" />
				</label>														
			</div>	
		</td>
		<td style="white-space: nowrap; text-align:left">
			<c:choose>				
				<c:when test="${!empty currentUser}">
					<div style="font-weight: bold;margin-left: 10px;">					
					- <spring:message code="layout.userinfo" /> 
						<c:choose>
							<c:when test="${isGuest}">
								<spring:message code="layout.userinfo.guest" />
							</c:when>
							<c:when test="${isAdmin}">
								<spring:message code="layout.userinfo.admin" />
							</c:when>
							<c:when test="${isLoggedInAsAdmin}">
								<spring:message code="layout.userinfo.loggedInAsAdmin" />
							</c:when>
							<c:otherwise>
							</c:otherwise>
						</c:choose>
						<c:out value="${currentUser.username}" /> &nbsp;
					</div>									
				</c:when>									
				<c:otherwise>
					<c:set var="selLanguage">${locale}</c:set>
					&nbsp;&nbsp;<select id="locale" name="locale" class="ui-state-default" 
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
				</c:otherwise>
			</c:choose>				
		</td>
		<td style="width:100%;">			
		</td>
		<td style="text-align:right; white-space: nowrap;">								
			<div id="headerNav" class="mlt-button">								
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
