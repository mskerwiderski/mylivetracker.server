<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<jsp:directive.page import="de.msk.mylivetracker.web.util.WebUtils" />
<% 
	String applicationBaseUrl = WebUtils.getApplicationBaseUrl(request);
	request.setAttribute("applicationBaseUrl", applicationBaseUrl);
%>

<div id="divCredits" style="height:100%;overflow:auto;">
<table>
	<tr>
		<td class="ui-widget-header">
			&nbsp;<b>Danksagungen:</b>&nbsp;						
		</td>		
	</tr>
	<tr>
		<td style="height:5px; border:none;" />				
	</tr>
	<tr>
		<td class="ui-widget-header">
			&nbsp;<b>Themes</b>&nbsp;						
		</td>		
	</tr>
	<tr>
		<td class="ui-widget-content">
			&nbsp;Alle von MyLiveTracker verwendeten Themes werden nach bestem Wissen lizenzgerecht verwendet.&nbsp;<br>
			&nbsp;Vielen Dank an die Designer und Hersteller für die zur Verfügung gestellten professionellen Themes!&nbsp;<br>
			&nbsp;Folgende Themes werden von MyLiveTracker verwendet:&nbsp;<br><br>
			<table>
				<tr>
					<th class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;Theme&nbsp;</th>
					<th class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;Designer / Hersteller / Quelle&nbsp;</th>
					<th class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;Lizenz&nbsp;</th>
					<th class="ui-widget-content" style="width:100%;border: none;"></th>
				</tr>
				<tr>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;Aristo Theme for JQuery UI&nbsp;</td>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;<a href="http://taitems.github.com/Aristo-jQuery-UI-Theme/">Tait Brown</a>&nbsp;</td>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;<a href="http://creativecommons.org/licenses/by-sa/3.0/de/">Attribution-ShareAlike 3.0 Unported (CC BY-SA 3.0)</a>&nbsp;</td>
					<td class="ui-widget-content" style="border: none;"></td>
				</tr>
			</table>	
		</td>
	</tr>	
	<tr>
		<td style="height:5px; border:none;" />				
	</tr>
	<tr>
		<td class="ui-widget-header">
			&nbsp;<b>Icons</b>&nbsp;						
		</td>		
	</tr>
	<tr>
		<td class="ui-widget-content">
			&nbsp;Alle von MyLiveTracker verwendeten Icons werden nach bestem Wissen lizenzgerecht verwendet.&nbsp;<br>
			&nbsp;Vielen Dank an die Designer und Hersteller für die zur Verfügung gestellten professionellen Icon Sammlungen!&nbsp;<br>
			&nbsp;Folgende Icon Sammlungen werden von MyLiveTracker verwendet:&nbsp;<br><br>
			<table>
				<tr>
					<th class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;Icon Sammlung&nbsp;</th>
					<th class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;Designer / Hersteller / Quelle&nbsp;</th>
					<th class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;Lizenz&nbsp;</th>
					<th class="ui-widget-content" style="width:100%;border: none;"></th>
				</tr>
				<tr>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;Crystal Clear Icon Set&nbsp;</td>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;<a href="http://www.everaldo.com/">Everaldo Coelho</a>&nbsp;</td>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;<a href="http://www.gnu.org/licenses/lgpl-3.0.de.html">GNU Lesser General Public License (LGPL)</a>&nbsp;</td>
					<td class="ui-widget-content" style="border: none;"></td>
				</tr>
				<tr>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;Farm-fresh Icon Set&nbsp;</td>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;<a href="http://www.fatcow.com/">FatCow Web Hosting</a>&nbsp;</td>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;<a href="http://creativecommons.org/licenses/by/3.0/de/">Creative Commons (Attribution 3.0 United States)</a>&nbsp;</td>
					<td class="ui-widget-content" style="border: none;"></td>
				</tr>
				<tr>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;Gloss Basic Icon Set&nbsp;</td>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;<a href="http://www.momentumdesignlab.com/">Momentum</a>&nbsp;</td>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;<a href="http://creativecommons.org/licenses/by-sa/3.0/de/">Attribution-ShareAlike 3.0 Unported (CC BY-SA 3.0)</a>&nbsp;</td>
					<td class="ui-widget-content" style="border: none;"></td>
				</tr>
				<tr>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;LED Icon Set&nbsp;</td>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;<a href="http://led24.de/iconset/">LED 24&nbsp;&#8226;&nbsp;Idealux GmbH</a>&nbsp;</td>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;<a href="http://creativecommons.org/licenses/by-sa/3.0/de/">Attribution-ShareAlike 3.0 Unported (CC BY-SA 3.0)</a>&nbsp;</td>
					<td class="ui-widget-content" style="border: none;"></td>
				</tr>
				<tr>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;vertical-align:middle;">&nbsp;<img style="vertical-align: middle;" src="img/map/miclogo-88x31.gif" />&nbsp;</td>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;<a href="http://mapicons.nicolasmollet.com">Nicolas Mollet</a>&nbsp;</td>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;<a href="http://creativecommons.org/licenses/by-sa/3.0/de/">Attribution-ShareAlike 3.0 Unported (CC BY-SA 3.0)</a>&nbsp;</td>
					<td class="ui-widget-content" style="border: none;"></td>
				</tr>
				<tr>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;Nuvola Icon Set&nbsp;</td>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;<a href="http://www.icon-king.com/">David Vignoni</a>&nbsp;</td>
					<td class="ui-widget-content" style="border:none;white-space:nowrap;text-align:left;">&nbsp;<a href="http://www.gnu.org/licenses/lgpl-3.0.de.html">GNU Lesser General Public License (LGPL)</a>&nbsp;</td>
					<td class="ui-widget-content" style="border: none;"></td>
				</tr>
			</table>
		</td>		
	</tr>
	<tr>
		<td style="height:5px; border:none;" />				
	</tr>
	<tr>
		<td class="ui-widget-header">
			&nbsp;<b>Frameworks</b>&nbsp;						
		</td>		
	</tr>
	<tr>
		<td class="ui-widget-content">
			&nbsp;Alle von MyLiveTracker verwendeten Frameworks werden nach bestem Wissen lizenzgerecht verwendet.&nbsp;<br>
			&nbsp;Vielen Dank an die Entwickler und Hersteller für die zur Verfügung gestellten professionellen Frameworks!&nbsp;<br>
			&nbsp;Folgende Frameworks werden von MyLiveTracker verwendet:&nbsp;<br><br>
			<table>
				<tr>
					<th class="ui-widget-content" style="border: none;white-space:nowrap;text-align:left;">&nbsp;Framework&nbsp;</th>
					<th class="ui-widget-content" style="border: none;white-space:nowrap;text-align:left;">&nbsp;Entwickler / Hersteller / Quelle&nbsp;</th>
					<th class="ui-widget-content" style="border: none;white-space:nowrap;text-align:left;">&nbsp;Lizenz&nbsp;</th>
					<th class="ui-widget-content" style="width:100%;border: none;"></th>
				</tr>
				<tr>
					<td class="ui-widget-content" style="border: none;white-space:nowrap;text-align:left;">&nbsp;Spring Framework&nbsp;</td>
					<td class="ui-widget-content" style="border: none;white-space:nowrap;text-align:left;">&nbsp;<a href="http://www.springsource.org/spring-framework">SpringSource</a>&nbsp;</td>
					<td class="ui-widget-content" style="border: none;white-space:nowrap;text-align:left;">&nbsp;<a href="http://www.apache.org/licenses/LICENSE-2.0">Apache License, Version 2.0</a>&nbsp;</td>
					<td class="ui-widget-content" style="border: none;"></td>
				</tr>
			</table>	
		</td>
	</tr>	
</table>	
</div>

