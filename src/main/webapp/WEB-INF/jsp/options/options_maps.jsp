<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<script type="text/javascript">
	function saveAllMaps() {
		actionExecute('SaveAllMaps');
	}
	function resetAllMaps() {
		actionExecute('ResetAllMaps');
	}
	function selectAllMaps(count) {
		for (i=0; i< count; i++) {
			document.getElementById("userOptions.mapsUsed.mapsUsed[" + i + "]1").checked = true;
		}
	}
	function deselectAllMaps(count) {
		for (i=0; i< count; i++) {
			document.getElementById("userOptions.mapsUsed.mapsUsed[" + i + "]1").checked = false;
		}
	}
</script>

<table>
	
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="maps.supportedMaps" />&nbsp;
		</td>
		<td>
			<table>
			<c:forEach var="supportedMap" items="${optionsCmd.supportedMaps}" varStatus="mapIdStatus">
				<tr>
					<td style="border:none;">
						&nbsp;<form:checkbox path="userOptions.mapsUsed.mapsUsed[${mapIdStatus.count-1}]"
						cssClass="text ui-widget-content ui-corner-all"
						/>&nbsp;<spring:message code="${supportedMap.label}" />&nbsp;
					</td>
				</tr>									
			</c:forEach>
			</table>
			<div class="mlt-button">
			&nbsp;<a href="#" onclick="javascript:selectAllMaps('<c:out value='${optionsCmd.supportedMaps.size()}'/>');">
				<spring:message code='maps.select.all' />
			</a>&nbsp;
			<a href="#" onclick="javascript:deselectAllMaps('<c:out value='${optionsCmd.supportedMaps.size()}'/>');">
				<spring:message code='maps.deselect.all' />
			</a>&nbsp;
			</div>
		</td>	
		<td>
			&nbsp;&nbsp;	
		</td>			
	</tr>
	<tr>
		<td>
			&nbsp;<spring:message code="maps.defaultMap" />&nbsp;
		</td>
		<td style="width:30%;">
			<c:set var="selDefMapId">${optionsCmd.userOptions.mapsUsed.defMapId}</c:set>
			&nbsp;<select id="userOptions.mapsUsed.defMapId" name="userOptions.mapsUsed.defMapId"
				class="text ui-widget-content ui-corner-all" > 
					<c:forEach var="supportedMap" items="${optionsCmd.supportedMaps}">
  						<c:choose>
    						<c:when test="${supportedMap.value eq selDefMapId}">
						    	<option value="${supportedMap.value}" selected>
						        	<spring:message code="${supportedMap.label}" />
						      	</option>
						    </c:when> 
					    	<c:otherwise>
						      	<option value="${supportedMap.value}" >
						        	<spring:message code="${supportedMap.label}" />
						      	</option>
						    </c:otherwise>
						</c:choose>
					</c:forEach>
			</select>&nbsp;		
		</td>					
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="userOptions.mapsUsed.defMapId" />&nbsp;
		</td>
	</tr>
	<tr>
		<td>
			&nbsp;<spring:message code="maps.routesUsed" />&nbsp;
		</td>
		<td>
			<form:textarea cssClass="text ui-widget-content ui-corner-all"
				style="margin-left: 4px;width:98%" rows="4"
				path="userOptions.routesUsed.routesUsedStr"/>
		</td>	
		<td>
			&nbsp;<form:errors cssClass="ui-state-error"  
				path="userOptions.routesUsed.routesUsedStr" />&nbsp;	
		</td>	
	</tr>
	<tr>
		<td style="width:30%;">
			&nbsp;<spring:message code="maps.save.reset" />&nbsp;
		</td>
		<td style="width:30%;white-space: nowrap;">
			<div class="mlt-button">
				&nbsp;<a href="#" onclick="javascript:saveAllMaps();">
					<spring:message code='maps.save.button' />
				</a>												
				<a href="#" onclick="javascript:resetAllMaps();">
					<spring:message code='maps.reset.button' />
				</a>&nbsp;														
			</div>				
		</td>
		<c:choose>
			<c:when test="${!empty optionsCmd.infoMessage && optionsCmd.currentTabId eq 4}">
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
