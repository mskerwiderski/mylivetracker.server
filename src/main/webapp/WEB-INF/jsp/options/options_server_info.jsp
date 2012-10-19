<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<%@ taglib uri="/WEB-INF/tld/mlt.tld" prefix="mlt" %>

<table>
<tr><td style="border:none;">
<table>
	<tr>
		<th style="white-space:nowrap;text-align:left;" class="ui-widget-header">
			&nbsp;Server DNS Name&nbsp;
		</th>
		<td style="white-space:nowrap;text-align:left;" class="ui-widget-content">
			&nbsp;<c:out value="${optionsCmd.serverInfo.dnsName}" />&nbsp;
		</td>
		<td style="width:100%;border:none;">
			&nbsp;
		</td>
	</tr>
	<tr>
		<th style="white-space:nowrap;text-align:left;" class="ui-widget-header">
			&nbsp;Server IP Address&nbsp;
		</th>
		<td style="white-space:nowrap;text-align:left;" class="ui-widget-content">
			&nbsp;<c:out value="${optionsCmd.serverInfo.ipAddress}" />&nbsp;
		</td>
		<td style="width:100%;border:none;">
			&nbsp;
		</td>
	</tr>
</table>
</td></tr>
<tr><td style="border:none;"><hr/>
<table>
	<tr>
		<th colspan="5" style="white-space: nowrap;text-align:left;" class="ui-widget-header">
			&nbsp;TCP Servers&nbsp;
		</th>
		<th style="width:100%;border:none;">&nbsp;</th>
	</tr>
	<tr>
		<th style="white-space: nowrap;text-align:left;" class="ui-widget-header">&nbsp;Name&nbsp;</th>
		<th style="white-space: nowrap;text-align:left;" class="ui-widget-header">&nbsp;Running&nbsp;</th>
		<th style="white-space: nowrap;text-align:left;" class="ui-widget-header">&nbsp;Port&nbsp;</th>
		<th style="white-space: nowrap;text-align:left;" class="ui-widget-header">&nbsp;Config&nbsp;</th>
		<th style="white-space: nowrap;text-align:left;" class="ui-widget-header">&nbsp;Supported Devices&nbsp;</th>
		<th style="width:100%;border:none;">&nbsp;</th>
	</tr>	
	<c:forEach items="${optionsCmd.serverInfo.tcpServers}" var="tcpServer">
		<tr>
			<td style="white-space: nowrap;vertical-align:top;">
				<table>
					<tr>
						<td style="white-space:nowrap;border:none;" class="ui-widget-content">
							&nbsp;<c:out value="${tcpServer.name}" />&nbsp;
						</td>
					</tr>
				</table>
			</td>
			<td style="white-space: nowrap;vertical-align:top;">
				<table>
					<tr>
						<td style="white-space:nowrap;border:none;" class="ui-widget-content">
							&nbsp;<c:out value="${tcpServer.running}" />&nbsp;
						</td>
					</tr>
				</table>
			</td>		
			<td style="white-space: nowrap;vertical-align:top;">
				<table>
					<tr>	
						<td style="white-space: nowrap;border:none;" class="ui-widget-content">
							&nbsp;<c:out value="${tcpServer.tcpServerConfig.listenPort}" />&nbsp;
						</td>
					</tr>
				</table>
			</td>			
			<td style="white-space: nowrap;vertical-align:top;">
				<table>
					<tr>
						<td style="white-space:nowrap;border:none;" class="ui-widget-content">
							&nbsp;supportsMultipleRecordsPerReception&nbsp;
						</td>
						<td style="white-space:nowrap;border:none;" class="ui-widget-content">
							=
						</td>	
						<td style="white-space:nowrap;border:none;" class="ui-widget-content">
							&nbsp;<c:out value="${tcpServer.socketProcessorConfig.supportsMultipleRecordsPerReception}" />&nbsp;
						</td>
					</tr>
					<tr>
						<td style="white-space:nowrap;border:none;" class="ui-widget-content">
							&nbsp;socketReadTimeoutInMSecs&nbsp;
						</td>
						<td style="white-space:nowrap;border:none;" class="ui-widget-content">
							=
						</td>	
						<td style="white-space:nowrap;border:none;" class="ui-widget-content">
							&nbsp;<c:out value="${tcpServer.socketProcessorConfig.socketReadTimeoutInMSecs}" />&nbsp;
						</td>
					</tr>
					<tr>
						<td style="white-space:nowrap;border:none;" class="ui-widget-content">
							&nbsp;maxDataStringLengthInBytes&nbsp;
						</td>
						<td style="white-space:nowrap;border:none;" class="ui-widget-content">
							=
						</td>	
						<td style="white-space:nowrap;border:none;" class="ui-widget-content">
							&nbsp;<c:out value="${tcpServer.socketProcessorConfig.maxDataStringLengthInBytes}" />&nbsp;
						</td>
					</tr>
					<tr>
						<td style="white-space:nowrap;border:none;" class="ui-widget-content">
							&nbsp;connectionTimeoutInMSecs&nbsp;
						</td>	
						<td style="white-space:nowrap;border:none;" class="ui-widget-content">
							=
						</td>
						<td style="white-space:nowrap;border:none;" class="ui-widget-content">
							&nbsp;<c:out value="${tcpServer.socketProcessorConfig.connectionTimeoutInMSecs}" />&nbsp;
						</td>
					</tr>
				</table>
			</td>
			<td style="white-space: nowrap;vertical-align:top;" class="ui-widget-content">
				<table>
					<c:forEach items="${tcpServer.dataPacketCreator.dataInterpreters}" var="tcpInterpreter">
						<tr><td style="white-space:nowrap;;border:none;" class="ui-widget-content">
							&nbsp;<c:out value="${tcpInterpreter.name}" />&nbsp;
						</td></tr>
					</c:forEach>
				</table>
			</td>
			<td style="border:none;">
				&nbsp;
			</td>
		</tr>	
	</c:forEach>
</table>	
</td></tr>
<tr><td style="border:none;"><hr/>
<table>
	<tr>
		<th colspan="5" style="white-space: nowrap;text-align:left;" class="ui-widget-header">
			&nbsp;UDP Servers&nbsp;
		</th>
		<th style="width:100%;border:none;">&nbsp;</th>
	</tr>
	<tr>
		<th style="white-space: nowrap;text-align:left;" class="ui-widget-header">&nbsp;Name&nbsp;</th>
		<th style="white-space: nowrap;text-align:left;" class="ui-widget-header">&nbsp;Running&nbsp;</th>
		<th style="white-space: nowrap;text-align:left;" class="ui-widget-header">&nbsp;Port&nbsp;</th>
		<th style="white-space: nowrap;text-align:left;" class="ui-widget-header">&nbsp;Config&nbsp;</th>
		<th style="white-space: nowrap;text-align:left;" class="ui-widget-header">&nbsp;Supported Devices&nbsp;</th>
		<th style="width:100%;border:none;">&nbsp;</th>
	</tr>	
	<c:forEach items="${optionsCmd.serverInfo.udpServers}" var="udpServer">
		<tr>
			<td style="white-space: nowrap;vertical-align:top;">
				<table>
					<tr>
						<td style="white-space:nowrap;border:none;" class="ui-widget-content">
							&nbsp;<c:out value="${udpServer.name}" />&nbsp;
						</td>
					</tr>
				</table>
			</td>
			<td style="white-space: nowrap;vertical-align:top;">
				<table>
					<tr>
						<td style="white-space:nowrap;border:none;" class="ui-widget-content">
							&nbsp;<c:out value="${udpServer.running}" />&nbsp;
						</td>
					</tr>
				</table>
			</td>		
			<td style="white-space: nowrap;vertical-align:top;">
				<table>
					<tr>	
						<td style="white-space: nowrap;border:none;" class="ui-widget-content">
							&nbsp;<c:out value="${udpServer.udpServerConfig.listenPort}" />&nbsp;
						</td>
					</tr>
				</table>
			</td>	
			<td style="white-space: nowrap;vertical-align:top;">
				<table>
					<tr>	
						<td style="white-space: nowrap;border:none;" class="ui-widget-content">
							&nbsp;./.&nbsp;
						</td>
					</tr>
				</table>
			</td>		
			<td style="white-space: nowrap;vertical-align:top;" class="ui-widget-content">
				<table>
					<c:forEach items="${udpServer.dataPacketCreator.dataInterpreters}" var="udpInterpreter">
						<tr><td style="white-space:nowrap;;border:none;" class="ui-widget-content">
							&nbsp;<c:out value="${udpInterpreter.name}" />&nbsp;
						</td></tr>
					</c:forEach>
				</table>
			</td>
			<td style="border:none;">
				&nbsp;
			</td>
		</tr>	
	</c:forEach>
</table>	
</td></tr>
<tr><td style="border:none;"><hr/>
<table>
	<tr>
		<th colspan="5" style="white-space: nowrap;text-align:left;" class="ui-widget-header">
			&nbsp;HTTP Servers&nbsp;
		</th>
		<th style="width:100%;border:none;">&nbsp;</th>
	</tr>
	<tr>
		<th style="white-space: nowrap;text-align:left;" class="ui-widget-header">&nbsp;Name&nbsp;</th>
		<th style="white-space: nowrap;text-align:left;" class="ui-widget-header">&nbsp;Running&nbsp;</th>
		<th style="white-space: nowrap;text-align:left;" class="ui-widget-header">&nbsp;Port&nbsp;</th>
		<th style="white-space: nowrap;text-align:left;" class="ui-widget-header">&nbsp;Config&nbsp;</th>
		<th style="white-space: nowrap;text-align:left;" class="ui-widget-header">&nbsp;Supported Devices&nbsp;</th>
		<th style="width:100%;border:none;">&nbsp;</th>
	</tr>	
	<c:forEach items="${optionsCmd.serverInfo.httpServerDscs}" var="httpServerDsc">
		<tr>
			<td style="white-space: nowrap;vertical-align:top;">
				<table>
					<tr>
						<td style="white-space:nowrap;border:none;" class="ui-widget-content">
							&nbsp;<c:out value="${httpServerDsc.server.name}" />&nbsp;
						</td>
					</tr>
				</table>
			</td>
			<td style="white-space: nowrap;vertical-align:top;">
				<table>
					<tr>
						<td style="white-space:nowrap;border:none;" class="ui-widget-content">
							&nbsp;<c:out value="${httpServerDsc.server.running}" />&nbsp;
						</td>
					</tr>
				</table>
			</td>		
			<td style="white-space: nowrap;vertical-align:top;">
				<table>
					<tr>	
						<td style="white-space: nowrap;border:none;" class="ui-widget-content">
							&nbsp;<c:out value="${httpServerDsc.server.listenPort}" />&nbsp;
						</td>
					</tr>
				</table>
			</td>	
			<td style="white-space: nowrap;vertical-align:top;">
				<table>
					<tr>	
						<td style="white-space: nowrap;border:none;" class="ui-widget-content">
							&nbsp;./.&nbsp;
						</td>
					</tr>
				</table>
			</td>		
			<td style="white-space: nowrap;vertical-align:top;" class="ui-widget-content">
				<table>
					<c:forEach items="${httpServerDsc.dscs}" var="httpInterpreterDsc">
						<tr>
							<td style="white-space:nowrap;;border:none;" class="ui-widget-content">
								&nbsp;<c:out value="${httpInterpreterDsc.interpreter.name}" />&nbsp;
							</td>
							<td style="white-space:nowrap;;border:none;" class="ui-widget-content">
								&rarr;
							</td>
							<td style="white-space:nowrap;;border:none;" class="ui-widget-content">
								&nbsp;<c:out value="${httpInterpreterDsc.url}" />&nbsp;
							</td>
						</tr>
					</c:forEach>
				</table>
			</td>
			<td style="border:none;">
				&nbsp;
			</td>
		</tr>	
	</c:forEach>
</table>	
</td></tr>
</table>