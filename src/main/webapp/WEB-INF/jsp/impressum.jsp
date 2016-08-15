<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>

<div id="divImpressum" style="height:100%;overflow:auto;">
<table>
	<tr>
		<td colspan="2" class="ui-widget-header">
			&nbsp;Impressum (Angaben gem&auml;&szlig; / Information according to &sect;5 TMG):&nbsp;						
		</td>		
	</tr>
	<tr>
		<td style="width:5px;border:none;" class="ui-widget-content">&nbsp;</td>
		<td style="border:none;" class="ui-widget-content">
			&nbsp;<img style="vertical-align: middle;" height="75%" src="img/others/impressum-address.jpg" />&nbsp;
		</td>
	</tr>
	<tr>
		<td colspan="2" style="height:5px; border:none" />				
	</tr>
	<tr>
		<td colspan="2" style="border:none;" class="ui-widget-header">
			&nbsp;Haftungsbeschr&auml;nkung&nbsp;						
		</td>
	</tr>
	<tr>		
		<td style="width:5px;border:none;" class="ui-widget-content">&nbsp;</td>	
		<td style="border:none;" class="ui-widget-content">
			Die Inhalte und Funktionalit&auml;ten dieser Website werden mit 
			gr&ouml;&beta;tm&ouml;glicher Sorgfalt erstellt und bereitgestellt. 
			Der Anbieter übernimmt jedoch keine Gew&auml;hr für die Richtigkeit, 
			Vollst&auml;ndigkeit und Aktualit&auml;t der bereitgestellten Inhalte
			und Funktionalit&auml;ten. Die Nutzung der Inhalte und Funktionalit&auml;ten 
			dieser Website erfolgt auf eigene Gefahr des Nutzers. Mit der reinen Nutzung 
			der Website des Anbieters kommt keinerlei Vertragsverh&auml;ltnis zwischen 
			dem Nutzer und dem Anbieter zustande.&nbsp;						
		</td>
	</tr>	
	<tr>
		<td colspan="2" style="height:5px; border:none" />				
	</tr>
	<tr>
		<td colspan="2" style="border:none;" class="ui-widget-header">
			&nbsp;Externe Links&nbsp;						
		</td>		
	</tr>
	<tr>
		<td style="width:5px;border:none;" class="ui-widget-content">&nbsp;</td>
		<td style="border:none;" class="ui-widget-content">
			Diese Website enth&auml;lt Verkn&uuml;pfungen zu Websites Dritter (sog. externe Links). 
			Diese Websites unterliegen der Haftung der jeweiligen Betreiber. Der Anbieter hat bei der 
			erstmaligen Verkn&uuml;pfung der externen Links die fremden Inhalte daraufhin &uuml;berpr&uuml;ft, 
			ob etwaige Rechtsverst&ouml;&beta;e bestehen. Zu dem Zeitpunkt waren keine Rechtsverst&ouml;&beta;e 
			ersichtlich. Der Anbieter hat keinerlei Einfluss auf die aktuelle und zuk&auml;nftige 
			Gestaltung und auf die Inhalte der verkn&uuml;pften Seiten. Das Setzen von externen Links 
			bedeutet nicht, dass sich der Anbieter die hinter dem Verweis oder Link liegenden Inhalte 
			zu Eigen macht. Eine st&auml;ndige Kontrolle der externen Links ist f&uuml;r den Anbieter 
			ohne konkrete Hinweise auf Rechtsverst&ouml;&beta;e nicht zumutbar. Bei Kenntnis von
			Rechtsverst&ouml;&beta;en werden jedoch derartige externe Links unverz&uuml;glich gel&ouml;scht.&nbsp;						
		</td>		
	</tr>	
	<tr>
		<td colspan="2" style="height:5px; border:none" />				
	</tr>
	<tr>
		<td colspan="2" style="border:none;" class="ui-widget-header">
			&nbsp;Urheber- und Leistungsschutzrechte&nbsp;						
		</td>		
	</tr>	
	<tr>
		<td style="width:5px;border:none;" class="ui-widget-content">&nbsp;</td>
		<td style="border:none;" class="ui-widget-content">
			Die auf dieser Website ver&ouml;ffentlichten Inhalte unterliegen dem deutschen
			Urheber- und Leistungsschutzrecht. Jede vom deutschen Urheber- und Leistungsschutzrecht 
			nicht zugelassene Verwertung bedarf der vorherigen schriftlichen Zustimmung des Anbieters 
			oder jeweiligen Rechteinhabers. Dies gilt insbesondere für Vervielfältigung, Bearbeitung, 
			&Uuml;bersetzung, Einspeicherung, Verarbeitung bzw. Wiedergabe von Inhalten in Datenbanken 
			oder anderen elektronischen Medien und Systemen. Inhalte und Rechte Dritter sind dabei als 
			solche gekennzeichnet. Die unerlaubte Vervielf&auml;ltigung oder Weitergabe einzelner 
			Inhalte oder kompletter Seiten ist nicht gestattet und strafbar. Lediglich die Herstellung 
			von Kopien und Downloads für den pers&ouml;nlichen, privaten und nicht kommerziellen 
			Gebrauch ist erlaubt. Die Darstellung dieser Website in fremden Frames ist nur mit 
			schriftlicher Erlaubnis zul&auml;ssig.&nbsp;						
		</td>		
	</tr>
	<tr>
		<td colspan="2" style="height:5px; border:none" />				
	</tr>
	<tr>
		<td colspan="2" style="border:none;" class="ui-widget-header">
			&nbsp;Datenschutz&nbsp;						
		</td>		
	</tr>	
	<tr>
		<td style="width:5px;border:none;" class="ui-widget-content">&nbsp;</td>
		<td style="border:none;" class="ui-widget-content">			
			Hinsichtlich der pers&ouml;nlichen Daten (= Zugangsdaten, Trackingdaten) des Nutzers
			weist der Anbieter gem&auml;&beta; &sect; 33 BDSG darauf hin, dass diese nach Ma&beta;gabe 
			der anwendbaren Datenschutzbestimmungen gespeichert und	&uuml;bertragen werden.	
			Ihre pers&ouml;nlichen Daten werden absolut vertraulich behandelt und an 
			Dritte nur weitergeleitet, sofern es f&uuml;r die Funktionalit&auml;t dieser Website 
			notwendig ist. Der Nutzer ist damit einverstanden, dass die von ihm im Rahmen der Nutzung dieser Website
			erhobenen Daten vom Anbieter gespeichert und unter Beachtung des Vorstehenden genutzt werden.&nbsp;					
		</td>
	</tr>
	<tr>
		<td style="width:5px;border:none;" class="ui-widget-content">&nbsp;</td>
		<td style="border:none;" class="ui-widget-content">
			Der Anbieter weist ausdr&uuml;cklich darauf hin, dass die Daten&uuml;bertragung
			im Internet (z.B. beim &Uuml;bertragen der Positionsdaten an MyLiveTracker) Sicherheitsl&uuml;cken 
			aufweisen und nicht l&uuml;ckenlos vor dem Zugriff durch Dritte gesch&uuml;tzt werden kann.&nbsp;						
		</td>		
	</tr>
	<tr>
		<td style="width:5px;border:none;" class="ui-widget-content">&nbsp;</td>
		<td style="border:none;" class="ui-widget-content">
			Die Verwendung der Kontaktdaten des Impressums zur gewerblichen Werbung ist 
			ausdr&uuml;cklich nicht erw&uuml;nscht, es sei denn der Anbieter hatte zuvor seine 
			schriftliche Einwilligung erteilt oder es besteht bereits eine Gesch&auml;ftsbeziehung. 
			Der Anbieter und alle auf dieser Website genannten Personen widersprechen hiermit 
			jeder kommerziellen Verwendung und Weitergabe ihrer Daten.&nbsp;						
		</td>		
	</tr>
	<tr>
		<td colspan="2" style="height:5px; border:none;" />				
	</tr>
	<tr>
		<td colspan="2" style="border:none;" class="ui-widget-content">
			Quelle: Disclaimer nach einer 
			<a href="http://www.juraforum.de/disclaimer_muster/" target="_blank">Vorlage</a> 
			u.a. von <a href="http://www.juraforum.de/" target="_blank">Juraforum.de</a> &amp; 
			<a href="http://www.experten-branchenbuch.de/" target="_blank">Experten-Branchenbuch.de</a>&nbsp;						
		</td>		
	</tr>
</table>	
</div>

