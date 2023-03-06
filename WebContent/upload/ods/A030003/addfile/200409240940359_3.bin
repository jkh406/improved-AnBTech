<%@ include file="admin/configHead.jsp"%>
<%@ page		
	info= "전자결재 및 개인우편 수신확인"		
	contentType = "text/html; charset=euc-kr" 	
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.gw.entity.*"
	import="com.anbtech.gw.db.*"
%>

<%
	//초기화 선언
	com.anbtech.gw.entity.TableItemCount table;

	//갯수 전달변수
	int APP_ING_CNT = 0;		//미결함 갯수
	int APP_BOX_CNT = 0;		//기결함 갯수
	int REJ_BOX_CNT = 0;		//반려함 갯수
	int SEE_BOX_CNT = 0;		//통보함 갯수 (읽지 않은 문서)
	int POST_CNT = 0;			//도착 편지
	int INFORM_CNT = 0;			//공지사항

	//-----------------------------------
	//	전자결재 수량 파악하기
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Table_List");
	table = new TableItemCount();
	Iterator table_iter = table_list.iterator();
	
	int no = 0;
	while(table_iter.hasNext()){
		table = (TableItemCount)table_iter.next();
		if(no == 0) APP_ING_CNT = table.getAppIngCnt();
		if(no == 1) APP_BOX_CNT = table.getAppBoxCnt();
		if(no == 2) REJ_BOX_CNT = table.getRejBoxCnt();
		if(no == 3) SEE_BOX_CNT = table.getSeeBoxCnt();
		if(no == 4) POST_CNT = table.getPostCnt();
		if(no == 5) INFORM_CNT = table.getInformCnt();
		no++;
	}	

%>

<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<META content="MSHTML 6.00.2800.1170" name=GENERATOR></HEAD>
<Script Language='JavaScript'>
<!--
function chkMsg()
{	
	document.sForm.action ='../servlet/ApprovalInfoServlet?mode=informCnt';
	document.sForm.submit();	
}
//깜빡이는 글자 만들기
function blinkIt()
{
  for(i=0; i<document.all.tags('blink').length; i++)
  {
    s = document.all.tags('blink')[i];
    s.style.visibility = (s.style.visibility == 'visible') ? 'hidden' : 'visible';
  }
}
setInterval('blinkIt()',100)
-->
</Script>
<style type="text/css">
<!--
td {FONT: 8pt/16px Verdana,굴림,Gulim; COLOR: #134AAF; FONT-WEIGHT: bold;}
//-->
</style>

<BODY text=#000000 bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" 
marginwidth="0" onload="setInterval('chkMsg()',30000)" oncontextmenu="return false">   <% // 1sec = 1000 %>

<form name="sForm" method='get'>
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 background="../images/bt_bg.gif">
	<TR align=center><TD width='100%' height=20>
		<table border="0" width="520" cellspacing="0" cellpadding="0" background="../images/bt_bg.gif">
		  <tr>
			<td width="61"><img border="0" src="../images/bt_mail.gif" width="61" height="22"></td>
			<td width="30" align='center' valign='middle'><%=POST_CNT%></td>
			<td width="61"><img border="0" src="../images/bt_gong.gif" width="61" height="22"></td>
			<td width="30" align='center' valign='middle'><%=INFORM_CNT%></td>
			<td width="72"><img border="0" src="../images/bt_mi.gif" width="72" height="22"></td>
			<% if(APP_ING_CNT > 0) {
					out.println("<td width='30' align='center' valign='middle'><blink>"+APP_ING_CNT+"</blink></td>");
				} else {
					out.println("<td width='30' align='center' valign='middle'>"+APP_ING_CNT+"</td>");	
				}
			%>
			<td width="72"><img border="0" src="../images/bt_wan.gif" width="72" height="22"></td>
			<td width="30" align='center' valign='middle'><%=APP_BOX_CNT%></td>
			<td width="72"><img border="0" src="../images/bt_ban.gif" width="72" height="22"></td>
			<td width="30" align='center' valign='middle'><%=REJ_BOX_CNT%></td>
			<td width="72"><img border="0" src="../images/bt_tong.gif" width="72" height="22"></td>
			<td width="30" align='center' valign='middle'><%=SEE_BOX_CNT%></td>
		  </tr>
		</table>
	</TD><TD width='233'><img src='../images/copyright.gif' border='0'></TD></TR></TABLE>
</form>

</BODY>
</HTML>
