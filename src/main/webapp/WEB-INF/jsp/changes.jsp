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

<div id="divChanges" style="height:100%;overflow:auto;">
<table>
	<tr>
		<td colspan="2">
			&nbsp;<b>Documentation of Changes since 26.02.2010 (only available in English):</b>&nbsp;						
		</td>		
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;						
		</td>		
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;<b><i>Release v1.3.0 (18.11.2011)</i></b>&nbsp;						
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;00&nbsp;</td>
		<td>
			&nbsp;New features:&nbsp;<br>
			&nbsp;* Automatic sender detection added: Now you can see the detected sender type under sender maintenance.&nbsp;<br>
			&nbsp;* New sender type added: Coban GPS 103a.&nbsp;<br>			
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;01&nbsp;</td>
		<td>
			&nbsp;Bugfixes:&nbsp;<br>
			&nbsp;* fixed: Map view fails on internet explorer.&nbsp;<br>
			&nbsp;* fixed: Some sort of email adresses were not accepted by the server (e.g. if an email-address contains a '-' char).&nbsp;<br>
			&nbsp;* fixed: One demo track was not working correctly (due to special char issues in address info strings).&nbsp;<br>			
		</td>
	</tr>		
	<tr>
		<td colspan="2">
			&nbsp;						
		</td>		
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;<b><i>Release v1.2.0 (24.09.2011)</i></b>&nbsp;						
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;00&nbsp;</td>
		<td>
			&nbsp;Time zone features integrated:&nbsp;<br>
			&nbsp;* User can specifiy his own time zone.&nbsp;<br>
			&nbsp;* Time zone can/must be specified for every sender.&nbsp;<br>			
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;01&nbsp;</td>
		<td>
			&nbsp;JSON-RPC services are provided:&nbsp;<br>
			&nbsp;* This is a service for remote server calls in the future (e.g. for third party apps and smartphone apps)&nbsp;<br>
			&nbsp;* Available: RPC 'rpc.json' for automatic linking of an Android with MyLiveTracker portal using MyLiveTracker app.&nbsp;<br>
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;02&nbsp;</td>
		<td>
			&nbsp;New uploaders:&nbsp;<br>
			&nbsp;* Simple HTTP-protocol implemented (only parameters ID, LAT, LON are supported).&nbsp;<br>
			&nbsp;* MyLiveTrackers secure TCP-protocol implemented (used by MyLiveTrackers Android app since v1.2.0).&nbsp;<br>
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;03&nbsp;</td>
		<td>
			&nbsp;New TCP server at port 51395: Multiple positions per data transfer are now supported:&nbsp;<br>
			&nbsp;* This is very useful, if a tracker is able to buffer position data, while not connected to a mobile network.&nbsp;<br>
			&nbsp;* Positions in one data packet must be separated by CR and/or LF characters.&nbsp;<br>
			&nbsp;* Only supported for MyLiveTracker app, Tk102, Tk5000.&nbsp;<br>
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;04&nbsp;</td>
		<td>
			&nbsp;Uploader improvements:&nbsp;<br>
			&nbsp;* All senders: time zone must be specified for senders timestamps.&nbsp;<br>
			&nbsp;* Incutex Tk5000: position is skipped as invalid, if satellites count is less than 5.&nbsp;<br>
			&nbsp;* Incutex Tk5000: emergency signals  are now processed on server-side.&nbsp;<br>
			&nbsp;* Incutex Tk5000: low battery signals are now processed on server-side.&nbsp;<br>
			&nbsp;* All regarding senders: if a phone number is uploaded, it will be displayed on the tracks overview page.&nbsp;<br>
			&nbsp;* All regarding senders: usage of senders mileage for track distance calculation can be switched off (under sender maintenance with the switch 'IgnoreSenderMileage').&nbsp;<br>
			&nbsp;* All regarding senders: usage of position validity check can be switched off (under sender maintenance with the switch 'IgnoreLocationValidFlag').&nbsp;<br>
			&nbsp;* All regarding senders: usage of senders timestamp as the received timestamp can be switched on (under sender maintenance with the switch 'UseTimestampAsReceived').&nbsp;<br>
			&nbsp;* iPhone SendLocation: position is skipped as invalid, if vacc or hacc parameter is less or equals than 0.&nbsp;<br>
			&nbsp;* MyLiveTracker HTTP-protocol: messages and battery info are now processed on server-side.&nbsp;<br>			
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;05&nbsp;</td>
		<td>
			&nbsp;Small new things:&nbsp;<br>			
			&nbsp;* tracking via maps: average speed without pauses added (only used by MyLiveTracker App).&nbsp;<br>
			&nbsp;* tracking via maps: current track can be hidden.&nbsp;<br>
			&nbsp;* geocoder can be switched on/off (under options).&nbsp;<br>		
			&nbsp;* new demo tracks with more details added.&nbsp;<br>	
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;06&nbsp;</td>
		<td>
			&nbsp;Bugfixes:&nbsp;<br>
			&nbsp;* fixed: gpx export files are now compatible with MapSource and BaseCamp.&nbsp;<br>
			&nbsp;* fixed: sender entries which are marked as not activated cannot be used.&nbsp;<br>
			&nbsp;* fixed: sender entries can be deleted.&nbsp;<br>
			&nbsp;* fixed: sender with an empty sender id or name cannot be registered.&nbsp;<br>
			&nbsp;* fixed: more than the maximum allowed number of senders cannot be registered.&nbsp;<br>
			&nbsp;* fixed: track overview can handle tracks, recorded by already deleted senders.&nbsp;<br>
			&nbsp;* fixed: correct timezone is used in gpx export. regarding to gpx spec, timestamps are based on utc timezone.&nbsp;<br>
			&nbsp;* fixed: if only one sender is registered, the "show tracks of all senders" option is available on tracks overview.&nbsp;<br>
			&nbsp;* and many more minor bugfixes and improvements&nbsp;<br>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;						
		</td>		
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;<b><i>Release v1.1.01.00 (21.05.2011)</i></b>&nbsp;						
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;00&nbsp;</td>
		<td>
			&nbsp;Export features integrated (gpx and kml).&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;01&nbsp;</td>
		<td>
			&nbsp;Serverside track search feature integrated.&nbsp;<br>								
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;02&nbsp;</td>
		<td>
			&nbsp;Better error handling implemented.&nbsp;<br>								
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;03&nbsp;</td>
		<td>
			&nbsp;Some bugs fixed (e.g. change password bug).&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;						
		</td>		
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;<b><i>Release v1.1.00.05 (23.04.2011)</i></b>&nbsp;						
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;00&nbsp;</td>
		<td>
			&nbsp;From now we are out of beta phase.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;01&nbsp;</td>
		<td>
			&nbsp;Support for MyLiveTracker Android App.&nbsp;<br>
			&nbsp;If you are interested in using MyLiveTracker app, please contact support.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;02&nbsp;</td>
		<td>
			&nbsp;Cardiac Function support added.&nbsp;<br>
			&nbsp;MyLiveTracker Android App supports heartrate transmission via ANT+.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;03&nbsp;</td>
		<td>
			&nbsp;UDP support for GSMTracker added.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;04&nbsp;</td>
		<td>
			&nbsp;Improvements to tracker support.&nbsp;<br>
			&nbsp;Now trackers additional information is used if sent (e.g. distance, runtime, ...).&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;05&nbsp;</td>
		<td>
			&nbsp;New version of login page (logo added).&nbsp;<br>					
		</td>
	</tr>	
	<tr>
		<td style="width:30px;text-align: center">&nbsp;06&nbsp;</td>
		<td>
			&nbsp;New version of tracks overview page.&nbsp;<br>					
			&nbsp;Now auto refresh can be configured.&nbsp;<br>
			&nbsp;New track table implemented (faster than the old one, with search capabilities).&nbsp;<br>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;						
		</td>		
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;<b><i>Release v1.0.01.06 beta (12.03.2011)</i></b>&nbsp;						
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;00&nbsp;</td>
		<td>
			&nbsp;Support for Android devices via GPS2OpenGTS Pro.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;01&nbsp;</td>
		<td>
			&nbsp;New version of login page (Facebook Like button, Link to twitter-thread added).&nbsp;<br>					
		</td>
	</tr>	
	<tr>
		<td colspan="2">
			&nbsp;						
		</td>		
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;<b><i>Release v1.0.01.05 beta (11.03.2011)</i></b>&nbsp;						
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;00&nbsp;</td>
		<td>
			&nbsp;Track is created even no valid positions has been uploaded by the tracker. See also 'data received' info in tracks overview.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;01&nbsp;</td>
		<td>
			&nbsp;Aspicores GSMTracker GpsGate protocol is supported.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;02&nbsp;</td>
		<td>
			&nbsp;Some bugfixes, e.g. character encoding for german umlauts while displaying single track.&nbsp;<br>					
		</td>
	</tr>	
	<tr>
		<td style="width:30px;text-align: center">&nbsp;03&nbsp;</td>
		<td>
			&nbsp;Documentation updated.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;04&nbsp;</td>
		<td>
			&nbsp;Extended functionalities for administration integrated (e.g. sender queues can now be stopped by administrator).&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;						
		</td>		
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;<b><i>Release v1.0.01.03 beta (06.03.2011)</i></b>&nbsp;						
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;00&nbsp;</td>
		<td>
			&nbsp;Emergency SMS transport implemented.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;01&nbsp;</td>
		<td>
			&nbsp;Support for iPhone devices via YourTracks (ecotec).&nbsp;<br>					
		</td>
	</tr>	
	<tr>
		<td style="width:30px;text-align: center">&nbsp;02&nbsp;</td>
		<td>
			&nbsp;Support for BlackBerry devices via beGPS (brandsemotion).&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;03&nbsp;</td>
		<td>
			&nbsp;Many bugfixes and better performance for processing incoming positions.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;						
		</td>		
	</tr>	
	<tr>
		<td colspan="2">
			&nbsp;<b><i>Release v1.0.00.10 beta (18.01.2011)</i></b>&nbsp;						
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;00&nbsp;</td>
		<td>
			&nbsp;Detailed documentation is now available.&nbsp;<br>					
		</td>
	</tr>	
	<tr>
		<td style="width:30px;text-align: center">&nbsp;01&nbsp;</td>
		<td>
			&nbsp;OpenStreetMap integrated.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;02&nbsp;</td>
		<td>
			&nbsp;Language settings for Geocoding service integrated.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;03&nbsp;</td>
		<td>
			&nbsp;Language settings for units integrated.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;04&nbsp;</td>
		<td>
			&nbsp;Status page settings redesigned.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;05&nbsp;</td>
		<td>
			&nbsp;Via click on a track on map, status info (timestamp, address) is displayed.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;06&nbsp;</td>
		<td>
			&nbsp;Positioning modes integrated: 'None', 'FlyToView', 'FlyToCurrentPosition'.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;07&nbsp;</td>
		<td>
			&nbsp;Meiligao devices are supported (e.g. VT310, GT60, GT30X).&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;08&nbsp;</td>
		<td>
			&nbsp;Support for iPhone/iOS4 via SendLocation.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;09&nbsp;</td>
		<td>
			&nbsp;Support for GSM Tracker TCP-Uploading.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;						
		</td>		
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;<b><i>Release v0.9.00.00 beta (18.09.2010)</i></b>&nbsp;						
		</td>
	</tr>	
	<tr>
		<td style="width:30px;text-align: center">&nbsp;00&nbsp;</td>
		<td>
			&nbsp;Performance improvements to Frontend.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;01&nbsp;</td>
		<td>
			&nbsp;Reset Track functionality added.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;02&nbsp;</td>
		<td>
			&nbsp;Status Page tab under Options implemented to configure status info/google maps windows for embedding in third party applications.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;03&nbsp;</td>
		<td>
			&nbsp;Live Tracking for Google Maps implemented.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;...&nbsp;</td>
		<td>
			&nbsp;And many more improvements...&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;						
		</td>		
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;<b><i>Release v0.5.01.00 beta (13.08.2010)</i></b>&nbsp;						
		</td>
	</tr>	
	<tr>
		<td style="width:30px;text-align: center">&nbsp;00&nbsp;</td>
		<td>
			&nbsp;Design improvements, better performance and bugfixes to Status Widget.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;						
		</td>		
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;<b><i>Release v0.5.00.00 beta (01.08.2010)</i></b>&nbsp;						
		</td>
	</tr>	
	<tr>
		<td style="width:30px;text-align: center">&nbsp;00&nbsp;</td>
		<td>
			&nbsp;Design improvements to Status Widget.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;01&nbsp;</td>
		<td>
			&nbsp;Tracks which are older than 4 weeks are automatically deleted.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;02&nbsp;</td>
		<td>
			&nbsp;Logging to database feature implemented for better user support.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;03&nbsp;</td>
		<td>
			&nbsp;Bugfixes.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;						
		</td>		
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;<b><i>Release v0.4.02.00 beta (30.04.2010)</i></b>&nbsp;						
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;00&nbsp;</td>
		<td>
			&nbsp;Performance improvements loading track list (factor 35 faster now).&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;01&nbsp;</td>
		<td>
			&nbsp;New jquery-ui version integrated.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;02&nbsp;</td>
		<td>
			&nbsp;I18n in tracks-overview completed (table header names).&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;03&nbsp;</td>
		<td>
			&nbsp;Evaluation mode integrated (for companies who likes to run
			and test MyLiveTracker on their own server). In evaluation mode, 
			there are following restrictions:<br>
			* only 1 user is allowed.<br>
			* only 3 senders are allowed.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;04&nbsp;</td>
		<td>
			&nbsp;Device Jublee Phone Locator integrated.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;						
		</td>		
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;<b><i>Release v0.4.01.00 beta (17.04.2010)</i></b>&nbsp;						
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;00&nbsp;</td>
		<td>
			&nbsp;Device Tk5000 integrated.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;						
		</td>		
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;<b><i>Release v0.4.00.00 beta (14.03.2010)</i></b>&nbsp;						
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;00&nbsp;</td>
		<td>
			&nbsp;Layout improvements and layout bugfixes.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;01&nbsp;</td>
		<td>
			&nbsp;Bugfix in Tk102 Server (Port 41395).&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;02&nbsp;</td>
		<td>
			&nbsp;I18n for English and German completed.&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;03&nbsp;</td>
		<td>
			&nbsp;User Data tab (under Options).<br>
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;04&nbsp;</td>
		<td>
			&nbsp;Sender tab added (under Options).<br>
		</td>
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;						
		</td>		
	</tr>
	<tr>
		<td colspan="2">
			&nbsp;<b><i>Release v0.3.00.00 beta (28.02.2010)</i></b>&nbsp;						
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;00&nbsp;</td>
		<td>
			&nbsp;Glopus integration initially done.&nbsp;<br>
			&nbsp;* Placeholder 'Name' is used as Sender Id.&nbsp;<br>
			&nbsp;* Placeholder 'Comment' is used as Track name.&nbsp;<br>
			&nbsp;* Supported tracking values: Latitude ('Lat'), Longitude ('Long') and Speed ('Speed').&nbsp;<br>					
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;01&nbsp;</td>
		<td>
			&nbsp;Tk102 integration improvements for old and new versions:&nbsp;<br>
			&nbsp;* Bugfixes (e.g. clean up timed out connections to prevent blocking new incoming data.&nbsp;<br>
			&nbsp;* Old versions of Tk102 should use port 31395.&nbsp;<br>
			&nbsp;* New versions of Tk102 should use port 41395.&nbsp;<br>
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;02&nbsp;</td>
		<td>
			&nbsp;New option 'Default track name' available:&nbsp;<br>
			&nbsp;* New tracks (automatically or manually created) get intially the default track name.&nbsp;<br>			
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;03&nbsp;</td>
		<td>
			&nbsp;New live tracking option 'Fly to view' available:&nbsp;<br>
			&nbsp;* If 'On' Google Earth will automatically zoom to positions area after every update (old behaviour).&nbsp;<br>
			&nbsp;* If 'Off' Google Earth will not zoom to positions area.&nbsp;<br>			
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;04&nbsp;</td>
		<td>
			&nbsp;Current speed is evaluated and shown on tracks overview if supported by sender.&nbsp;<br>						
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;05&nbsp;</td>
		<td>
			&nbsp;Tk102 status values:&nbsp;<br>						
			&nbsp;* If SOS-button is pressed, a sos message is displayed on tracks overview.&nbsp;<br>
			&nbsp;* If battery voltags is low, a warning message is displayed on tracks overview.&nbsp;<br>
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;06&nbsp;</td>
		<td>
			&nbsp;Confirmation is required if you want to remove a track.&nbsp;<br>									
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;07&nbsp;</td>
		<td>
			&nbsp;This Changes page integrated.&nbsp;<br>									
		</td>
	</tr>
	<tr>
		<td style="width:30px;text-align: center">&nbsp;08&nbsp;</td>
		<td>
			&nbsp;Bugfixes overall.&nbsp;<br>									
		</td>
	</tr>
</table>	
</div>

