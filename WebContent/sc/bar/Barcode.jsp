<%@ page		
	info= "바코드 MSCOMM32통신"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<html>
<head>
<title>COM Port Communications</title>
<SCRIPT LANGUAGE=javascript>
<!--
//SEND COM PORT Open / Close
function ComPortButton_onclick(){
    if(Frm_Main.MSComm1.PortOpen==false){
         Frm_Main.MSComm1.Settings = "28800,N,8,1"
         Frm_Main.MSComm1.InputLen = 1
         Frm_Main.MSComm1.CommPort = Frm_Main.PortNumberDisplay.value;
         Frm_Main.MSComm1.PortOpen=true;
         Frm_Main.ComPortButton.value="Open";
    }
    else{
         Frm_Main.MSComm1.PortOpen=false;
         Frm_Main.ComPortButton.value="Close";
    }
		 
}
//RECEIVE COM PORT Open / Close
function ComPortButton_onclick2(){
    if(Frm_Main.MSComm2.PortOpen==false){
         Frm_Main.MSComm2.Settings = "28800,N,8,1"
         Frm_Main.MSComm2.InputLen = 1
         Frm_Main.MSComm2.CommPort = Frm_Main.PortNumberDisplay2.value;
         Frm_Main.MSComm2.PortOpen=true;
         Frm_Main.ComPortButton2.value="Open";
    }
    else{
         Frm_Main.MSComm2.PortOpen=false;
         Frm_Main.ComPortButton2.value="Close";
    }
}

//Send Data
function SendButton_onclick() {
    if(Frm_Main.MSComm1.PortOpen==true){
         Frm_Main.MSComm1.Output=Frm_Main.SendDisplay.value + String.fromCharCode(13)
    }
    else{
         alert("You need to open the COM port first");
    }
}

//Receive Data
function MSComm2_OnComm() {

	switch (Frm_Main.MSComm1.CommEvent) {
	   case "comEventBreak" :
			alert("A Break was received");
			break;
	   case "comEventCDTO" :
			alert("CD (RSSD) Timeout");
			break;
	   case "comEventCTSTO" :
			alert("CTS Timeout");
			break;
	   case "comEventDSRTO" :
			alert("DSR Timeout");
			break;
	   case "comEventFrame" :
			alert("Framing Error");
			break;
	   case "comEventOverrun" :
			alert("Data Lost");
			break;
	   case "comEventRxOver" :
			alert("Receive buffer overflow");
			break;
	   case "comEventRxParity" :
			alert("Parity Error");
			break;
	   case "comEventTxFull" :
			alert("Tramsmit buffer full");
			break;
	   case "comEventDCB" :
			alert("Unexpected error retriving DCB");
			break;
	   case "comEvCD" :
			alert("Change in the CD line");
			break;
	   case "comEvCTS" :
			alert("Change in the CTS line");
			break;
	   case "comEvDSR" :
			alert("Change in the DSR line");
			break;
	   case "comEvRing" :
			alert("Change in the Ring Indicator");
			break;
	   case "comEvReceive" :
		    InputString = Frm_Main.MSComm2.Input;
			Frm_Main.ReceiveDisplay.value = Frm_Main.ReceiveDisplay.value + InputString;

			alert("Received RThreshold # of chars");
			break;
	   case "comEvSend" :
			alert("There are SThreshold mumber of characters in the transmit buffer");
			break;
	   case "comEvEOF" :
			alert("An EOF character was found in the input stream");
			break;
	   default :
		  alert("Sorry, Ready");
	} 
}
</SCRIPT>
<SCRIPT LANGUAGE=javascript FOR='MSComm2' EVENT='OnComm'>
	MSComm2_OnComm()
</SCRIPT>

<body LANGUAGE='javascript'>
<form id="Frm_Main" method="post">
<P>This web page communicates over a COM port : Send Port.
<OBJECT id='MSComm1'  
		style="visibility:hidden;"
		classid='clsid:648A5600-2C6E-101B-82B6-000000000014'>
		<PARAM NAME="RThreshold" VALUE="1">
		<PARAM NAME="InputMode" VALUE="comInputModeText">
</OBJECT>

<P>SEND COM Port Number 
<SELECT NAME="PortNumberDisplay" id='PortNumberDisplay' style="WIDTH: 41px" SIZE="1">
	<OPTION VALUE="1" SELECTED>1
	<OPTION VALUE="2">2
	<OPTION VALUE="3">3
	<OPTION VALUE="4">4
	</OPTION>
</SELECT> 

<INPUT language='javascript' id='ComPortButton' style="LEFT: 340px; TOP: 33px" onclick="return ComPortButton_onclick()" type='button' value='READY' name='ComPortButton'>
<INPUT id='SendButton' name='SendButton' style="LEFT: 68px; TOP: 110px" type='button' value="Send" LANGUAGE ="javascript" onclick="return SendButton_onclick()">&nbsp; 

<P>
<INPUT id='SendDisplay'><BR>

<p>
<P>This web page communicates over a COM port : Receive Port.
<OBJECT id='MSComm2'  
		style="visibility:hidden;"
		classid='clsid:648A5600-2C6E-101B-82B6-000000000014'>
		<PARAM NAME="RThreshold" VALUE="1">
		<PARAM NAME="InputMode" VALUE="comInputModeText">
</OBJECT>

<P>RECEIVE COM Port Number 
<SELECT NAME="PortNumberDisplay2" id='PortNumberDisplay2' style="WIDTH: 41px" SIZE="1">
	<OPTION VALUE="1">1
	<OPTION VALUE="2" SELECTED>2
	<OPTION VALUE="3">3
	<OPTION VALUE="4">4
	</OPTION>
</SELECT> 

<INPUT language='javascript' id='ComPortButton2' style="LEFT: 340px; TOP: 33px" onclick="return ComPortButton_onclick2()" type='button' value='READY' name='ComPortButton2'>

<P>
<TEXTAREA id='ReceiveDisplay' style="WIDTH: 302px; HEIGHT: 150px" rows='5' cols='33'></TEXTAREA></P>
</P>
</form>      
</body>
</html>

