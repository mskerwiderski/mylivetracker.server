<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="/WEB-INF/tld/mlt.tld" prefix="mlt" %>

<table>
	<tr>
		<td style="width:40%;">
			&nbsp;<spring:message code="options.timezone" />&nbsp;
		</td>
		<td style="width:30%;">
			<c:set var="selTimeZone">${optionsCmd.userOptions.timeZone}</c:set>
			&nbsp;<select id="userOptions.timeZone" name="userOptions.timeZone" 
				class="text ui-widget-content ui-corner-all" > 
					<c:forEach var="commonsOptsTimeZone" items="${optionsCmd.commonsOptsTimeZone}">
  						<c:choose>
    						<c:when test="${commonsOptsTimeZone.value == selTimeZone}">
						    	<option value="${commonsOptsTimeZone.value}" selected>
						        	<spring:message code="${commonsOptsTimeZone.label}" />
						      	</option>
						    </c:when> 
					    	<c:otherwise>
					      		<option value="${commonsOptsTimeZone.value}" >
						        	<spring:message code="${commonsOptsTimeZone.label}" />
						      	</option>
						    </c:otherwise>
						</c:choose>
					</c:forEach>
			</select>&nbsp;
		</td>
		<td>&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;<spring:message code="options.language" />&nbsp;
		</td>
		<td>				
			<c:set var="selLanguage">${optionsCmd.userOptions.language}</c:set>
			&nbsp;<select id="userOptions.language" name="userOptions.language" 
				class="text ui-widget-content ui-corner-all"> 
				<c:forEach var="language" items="${optionsCmd.userOptsLanguage}">
 						<c:choose>
   						<c:when test="${language.value == selLanguage}">
					    	<option value="${language.value}" selected>
					        	<spring:message code="${language.label}" />
					      	</option>
					    </c:when>
				    	<c:otherwise>
				      		<option value="${language.value}" >
					        	<spring:message code="${language.label}" />
					      	</option>
					    </c:otherwise>
					</c:choose>
				</c:forEach>
			</select>&nbsp;
		</td>			
		<td>
			&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;<spring:message code="options.geocoding" />&nbsp;
		</td>
		<td>		
			<table>
				<tr><td style="border:none;">					
					&nbsp;<b><spring:message code="options.geocoding.onoff" /></b>&nbsp;									
					<c:set var="selGeocoderMode">${optionsCmd.userOptions.geocoderMode}</c:set>
					<select id="userOptions.geocoderMode" name="userOptions.geocoderMode" 
						class="text ui-widget-content ui-corner-all"> 
						<c:forEach var="geocoderMode" items="${optionsCmd.userOptsGeocoder}"> 
		 						<c:choose>
		   						<c:when test="${geocoderMode.value == selGeocoderMode}">
							    	<option value="${geocoderMode.value}" selected>
							        	<spring:message code="${geocoderMode.label}" />
							      	</option>
							    </c:when>
						    	<c:otherwise>
						      		<option value="${geocoderMode.value}" >
							        	<spring:message code="${geocoderMode.label}" />
							      	</option>
							    </c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td></tr>
				<tr><td style="border:none;">		
					&nbsp;<b><spring:message code="options.geocoding.language" /></b>&nbsp;
					<c:set var="selGeocoderLanguage">${optionsCmd.userOptions.geocoderLanguage}</c:set>
					<select id="userOptions.geocoderLanguage" name="userOptions.geocoderLanguage" 
						class="text ui-widget-content ui-corner-all"> 
						<c:forEach var="language" items="${optionsCmd.userOptsLanguage}">
		 						<c:choose>
		   						<c:when test="${language.value == selGeocoderLanguage}">
							    	<option value="${language.value}" selected>
							        	<spring:message code="${language.label}" />
							      	</option>
							    </c:when>
						    	<c:otherwise>
						      		<option value="${language.value}" >
							        	<spring:message code="${language.label}" />
							      	</option>
							    </c:otherwise>
							</c:choose>
						</c:forEach>
					</select>
				</td></tr>	
			</table>
		</td>			
		<td>
			&nbsp;
		</td>
	</tr>	
	<tr>
		<td>
			&nbsp;<spring:message code="options.scale.unit" />&nbsp;
		</td>
		<td>				
			<c:set var="selScaleUnit">${optionsCmd.userOptions.scaleUnit}</c:set>
			&nbsp;<select id="userOptions.scaleUnit" name="userOptions.scaleUnit" 
				class="text ui-widget-content ui-corner-all"> 
				<c:forEach var="language" items="${optionsCmd.userOptsLanguage}">
 						<c:choose>
   						<c:when test="${language.value == selScaleUnit}">
					    	<option value="${language.value}" selected>
					        	<spring:message code="${language.label}" />
					      	</option>
					    </c:when>
				    	<c:otherwise>
				      		<option value="${language.value}" >
					        	<spring:message code="${language.label}" />
					      	</option>
					    </c:otherwise>
					</c:choose>
				</c:forEach>
			</select>&nbsp;
		</td>			
		<td>
			&nbsp;
		</td>
	</tr>	
	<tr>
		<td>
			&nbsp;<spring:message code="options.track.defname" />&nbsp;
		</td>
		<td>
			&nbsp;<form:input path="userOptions.defTrackName"
			size="40" cssClass="text ui-widget-content ui-corner-all"
			/>&nbsp;												
		</td>
		<td>
			&nbsp;
		</td>			
	</tr>	
	<tr>
		<td>
			&nbsp;<spring:message code="options.track.defstatus" />&nbsp;
		</td>
		<td>				
			<c:set var="selDefTrackReleaseStatus">${optionsCmd.userOptions.defTrackReleaseStatus}</c:set>
			&nbsp;<select id="userOptions.defTrackReleaseStatus" name="userOptions.defTrackReleaseStatus" 
				class="text ui-widget-content ui-corner-all"> 
				<c:forEach var="defTrackReleaseStatus" items="${optionsCmd.trackOptsReleaseStatus}">
 						<c:choose>
   						<c:when test="${defTrackReleaseStatus.value == selDefTrackReleaseStatus}">
					    	<option value="${defTrackReleaseStatus.value}" selected>
					        	<spring:message code="${defTrackReleaseStatus.label}" />
					      	</option>
					    </c:when> 
				    	<c:otherwise>
				      		<option value="${defTrackReleaseStatus.value}" >
					        	<spring:message code="${defTrackReleaseStatus.label}" />
					      	</option>
					    </c:otherwise>
					</c:choose>
				</c:forEach>
			</select>&nbsp;				
		</td>
		<td>
			&nbsp;
		</td>			
	</tr>	
	<tr>
		<td>
			&nbsp;<spring:message code="options.track.autoclose" />&nbsp;
		</td>
		<td>				
			<c:set var="selTrackAutoClose">${optionsCmd.userOptions.trackAutoClose}</c:set>
			&nbsp;<select id="userOptions.trackAutoClose" name="userOptions.trackAutoClose" 
				class="text ui-widget-content ui-corner-all"> 
				<c:forEach var="trackAutoClose" items="${optionsCmd.trackOptsAutoClose}">
 						<c:choose>
   						<c:when test="${trackAutoClose.value == selTrackAutoClose}">
					    	<option value="${trackAutoClose.value}" selected>
					        	<spring:message code="${trackAutoClose.label}" />
					      	</option>
					    </c:when>
				    	<c:otherwise>
				      		<option value="${trackAutoClose.value}" >
					        	<spring:message code="${trackAutoClose.label}" />
					      	</option>
					    </c:otherwise>
					</c:choose>
				</c:forEach>
			</select>&nbsp;
		</td>
		<td>
			&nbsp;
		</td>					
	</tr>		
	<tr>
		<td>
			&nbsp;<spring:message code="options.recent.track.access" />&nbsp;
		</td>
		<td>
			<table>
				<tr>
					<td style="border:none;">
						&nbsp;<form:checkbox path="userOptions.recTrAccEnabled"
						cssClass="text ui-widget-content ui-corner-all"
						/>&nbsp;
						<spring:message code="options.recent.track.access.enabled" />
					</td>
				</tr>
				<tr>
					<td style="border:none;">
						&nbsp;<form:checkbox path="userOptions.recTrAccPrivTrEnabled"
						cssClass="text ui-widget-content ui-corner-all"
						/>&nbsp;
						<spring:message code="options.recent.track.access.private.tracks.accessable" />
					</td>
				</tr>
				<tr>				
					<td style="border:none;white-space: nowrap;vertical-align: middle">
						<table>
							<tr>										
								<td style="border:none;font-size:small;" colspan="2">
									<b><spring:message code="options.recent.track.access.code" /></b>
								</td>
							</tr>
							<tr>	
								<td style="border:none;vertical-align: middle;">
									<form:input path="userOptions.recTrAccCode"
										readonly="true" cssClass="text ui-widget-content ui-corner-all"
										size="40" style="background: #ebebeb"							 
									/>
								</td>
								<td style="border:none;vertical-align: middle;">		
									<div class="mlt-button" style="vertical-align: middle;">								
										<a href="#" onclick="javscript:actionExecute('RenewRecTrAccCode');">
											<spring:message code="options.recent.track.access.code.renew" />
										</a>
									</div>
								</td>								
							</tr>
						</table>																												
					</td>
				</tr>
			</table>					
		</td>
		<td>
			&nbsp;
		</td>		
	</tr>			
	<tr>
		<td>
			&nbsp;<spring:message code="options.guest.access" />&nbsp;
		</td>
		<td>
			<table>
				<tr>
					<td style="border:none;">
						&nbsp;<form:checkbox path="userOptions.guestAccEnabled"
							cssClass="text ui-widget-content ui-corner-all"
						/>&nbsp;
						<spring:message code="options.guest.access.enabled" />
					</td>
				</tr>
				<tr>
					<td style="border:none;">
						&nbsp;<form:checkbox path="userOptions.guestAccClosedTrEnabled"
							cssClass="text ui-widget-content ui-corner-all"					
						/>&nbsp;
						<spring:message code="options.guest.access.closed.tracks.accessable" />
					</td>
				</tr>
				<tr>
					<td style="border:none;">
						&nbsp;<form:checkbox path="userOptions.guestAccPrivTrEnabled"
							cssClass="text ui-widget-content ui-corner-all"					
						/>&nbsp;
						<spring:message code="options.guest.access.private.tracks.accessable" />
					</td>
				</tr>
				<tr>
					<td style="border:none;">		
						<table>
							<tr>										
								<td style="border:none;font-size:small;" colspan="2">
									<b><spring:message code="options.guest.access.password" /></b>
								</td>
							</tr>
							<tr>	
								<td style="border:none;vertical-align: middle;">
									<form:input path="userOptions.guestAccPassword"
										readonly="true" 	cssClass="text ui-widget-content ui-corner-all" 
										size="40" style="background: #ebebeb" />
								</td>
								<td style="border:none;vertical-align: middle;">		
									<div class="mlt-button" style="vertical-align: middle;">								
										<a href="#" onclick="javscript:actionExecute('RenewGuestAccPassword');">
											<spring:message code="options.guest.access.password.renew" />
										</a>
									</div>
								</td>								
							</tr>
						</table>														
					</td>
				</tr>
			</table>				
		</td>
		<td>
			&nbsp;
		</td>					
	</tr>
	<tr>
		<td>
			&nbsp;<spring:message code="options.track.route" />&nbsp;
		</td>
		<td>
			&nbsp;<form:input path="userOptions.trackRouteColor"
				cssClass="color text ui-corner-all" readonly="true"
				cssStyle="width:100px" 														 
			/>&nbsp;														
			<c:set var="selTrackRouteWidth">${optionsCmd.userOptions.trackRouteWidth}</c:set>
			<select id="userOptions.trackRouteWidth" name="userOptions.trackRouteWidth" 
				class="text ui-widget-content ui-corner-all"> 
				<c:forEach var="trackRouteWidth" items="${optionsCmd.trackRouteOptsWidth}">
 						<c:choose>
   						<c:when test="${trackRouteWidth.value == selTrackRouteWidth}">
					    	<option value="${trackRouteWidth.value}" selected>
					        	<spring:message code="${trackRouteWidth.label}" />
					      	</option>
					    </c:when>
				    	<c:otherwise>
				      		<option value="${trackRouteWidth.value}" >
					        	<spring:message code="${trackRouteWidth.label}" />
					      	</option>
					    </c:otherwise>
					</c:choose>
				</c:forEach>
			</select>&nbsp;
			<c:set var="selTrackRouteOpacity">${optionsCmd.userOptions.trackRouteOpacity}</c:set>
			<select id="userOptions.trackRouteOpacity" name="userOptions.trackRouteOpacity" 
				class="text ui-widget-content ui-corner-all"> 
				<c:forEach var="trackRouteOpacity" items="${optionsCmd.trackRouteOptsOpacity}">
 						<c:choose>
   						<c:when test="${trackRouteOpacity.value == selTrackRouteOpacity}">
					    	<option value="${trackRouteOpacity.value}" selected>
					        	<spring:message code="${trackRouteOpacity.label}" />
					      	</option>
					    </c:when>
				    	<c:otherwise>
				      		<option value="${trackRouteOpacity.value}" >
					        	<spring:message code="${trackRouteOpacity.label}" />
					      	</option>
					    </c:otherwise>
					</c:choose>
				</c:forEach>
			</select>&nbsp;
		</td>
		<td>
			&nbsp;
		</td>
	</tr>		
	<tr>
		<td rowspan="2">
			&nbsp;<spring:message code="options.home.location" />&nbsp;
		</td>
		<td>
			<table> 
				<tr>
					<td style="border:none;">	
						&nbsp;<spring:message code="options.home.location.country" />&nbsp;									
					</td>
					<td colspan="2" style="border:none;">
						<form:input path="userOptions.homeLocCountry"
							size="35" cssClass="text ui-widget-content ui-corner-all"								 
						/>
					</td>
				</tr>
				<tr>
					<td style="border:none;">	
						&nbsp;<spring:message code="options.home.location.city" />&nbsp;									
					</td>
					<td colspan="2" style="border:none;">
						<form:input path="userOptions.homeLocCity"
							size="35" cssClass="text ui-widget-content ui-corner-all"								 
						/>
					</td>
				</tr>
				<tr>
					<td style="border:none;">	
						&nbsp;<spring:message code="options.home.location.streetAndNumber" />&nbsp;									
					</td>
					<td colspan="2" style="border:none;white-space: nowrap;">
						<form:input path="userOptions.homeLocStreet"
							size="25" cssClass="text ui-widget-content ui-corner-all"								 
						/>
						<form:input path="userOptions.homeLocHousenumber"
							size="5" cssClass="text ui-widget-content ui-corner-all"								 
						/>
					</td>
				</tr>
			</table>
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="userOptions.homeLocAddress" />&nbsp;
		</td>			
	</tr>
	<tr>
		<td>
			<table> 
				<tr>
					<td style="border:none;white-space: nowrap;width:25%">
						&nbsp;<spring:message code="options.home.location.position.latitude" />
					</td>
					<td style="border:none;white-space: nowrap;width:25%">
						&nbsp;<form:input path="homeLocLatitudeStr"
							size="15" cssClass="text ui-widget-content ui-corner-all"								 
						/>
					</td>
					<td rowspan="2" style="border:none;text-align:center;width:50%">	
						<div class="mlt-button">	
							<a href="#" onclick="javscript:actionExecute('CheckHomeLocation');">
								<spring:message code="options.home.location.check" />
							</a>								
						</div>									
					</td>
				</tr>
				<tr>	
					<td style="border:none;white-space: nowrap;">
						&nbsp;<spring:message code="options.home.location.position.longitude" />	
					</td>
					<td style="border:none;white-space: nowrap;">
						&nbsp;<form:input path="homeLocLongitudeStr"
							size="15" cssClass="text ui-widget-content ui-corner-all"								 
						/>
					</td>
				</tr>
			</table>
		</td>
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="userOptions.homeLocLatitude" />&nbsp;		
		</td>			
	</tr>			
	<tr>
		<td>
			&nbsp;<spring:message code="options.save.reset" />&nbsp;
		</td>
		<td style="white-space: nowrap;">
			<div class="mlt-button">
				&nbsp;<a href="#" onclick="javascript:actionExecute('SaveAllOptions');">
					<spring:message code='options.save.button' />
				</a>												
				<a href="#" onclick="javascript:actionExecute('ResetAllOptions');">
					<spring:message code='options.reset.button' />
				</a>&nbsp;														
			</div>				
		</td>
		<c:choose>
			<c:when test="${!empty optionsCmd.infoMessage && optionsCmd.currentTabId eq 1}">
				<td class="ui-state-highlight">
					&nbsp;<c:out escapeXml="false" value="${optionsCmd.infoMessage}" />&nbsp;						
				</td>		
			</c:when>
			<c:otherwise>
				<td>&nbsp;</td>
			</c:otherwise>
		</c:choose>						
	</tr>
</table>				
