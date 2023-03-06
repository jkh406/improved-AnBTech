<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.ew.entity.*,com.anbtech.admin.entity.*"
%>
<jsp:useBean id="recursion" class="com.anbtech.admin.db.makeClassTree"/>
<%!
	ExtraWorkHistoryTable ewHistoryTable;
	UserInfoTable table;
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
%>

<%
	String year = request.getParameter("y");
	String division = request.getParameter("div");

	//리스트 가져오기
	ArrayList arry = new ArrayList();
	arry = (ArrayList)request.getAttribute("ew_array");
	Iterator itr = arry.iterator();

%>

<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../ew/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<form method='get' name='eForm' >
<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 oncontextmenu="return false">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../ew/images/blet.gif" align="absmiddle"> 부서별 특근 신청현황</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--버튼 및 페이징-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
				<SELECT name='division' onChange="go()"><%=recursion.viewComboByCode(0,0)%></SELECT>
				<%	if(!division.equals("")){	%>
					<script language='javascript'>
						document.eForm.division.value = '<%=division%>';
					</script>
				<%	}	%>
				<IMG src="../ew/images/bt_sangsin.gif" onclick="javascript:go_approval()" style="CURSOR: hand" align="absmiddle" alt="선택항목 상신"></TD>
			  <TD width='' align='right' style="padding-right:10px"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=30 align=middle class='list_title'><input type="checkbox" name="checkbox" onClick="check(document.eForm.checkbox)"></TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>신청자명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>신청자사번</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>신청자부서명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>특근시작일시</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>특근마침일시</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=160 align=middle class='list_title'>특근사유</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>신청일자</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR>
<%		
		while(itr.hasNext()) {
			ewHistoryTable = (ExtraWorkHistoryTable)itr.next();

			String o_no				= ""+ewHistoryTable.getOno();
			String member_id		= ewHistoryTable.getMemberId();
			String member_name		= ewHistoryTable.getMemberName();
			String division_name	= ewHistoryTable.getDivisionName();
			String w_sdate			= ewHistoryTable.getWsdate();
			w_sdate = w_sdate.substring(0,4) + "-" + w_sdate.substring(4,6) + "-" + w_sdate.substring(6,8);
			String w_stime			= ewHistoryTable.getWstime();
			String w_edate			= ewHistoryTable.getWedate();
			w_edate = w_edate.substring(0,4) + "-" + w_edate.substring(4,6) + "-" + w_edate.substring(6,8);
			String w_etime			= ewHistoryTable.getWetime();
			String duty				= ewHistoryTable.getDuty();
			String c_date			= ewHistoryTable.getCdate();
			c_date = c_date.substring(0,4) + "-" + c_date.substring(4,6) + "-" + c_date.substring(6,8);
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><input type="checkbox" name="checkbox" value="<%=o_no%>"></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=member_name%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=member_id%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=division_name%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=w_sdate%> <%=w_stime%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=w_edate%> <%=w_etime%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style="padding-left:5px"><%=duty%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=c_date%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'></td>
			</TR>
			<TR><TD colSpan=17 background="../ew/images/dot_line.gif"></TD></TR>
<%	}
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>
</form>
</body>
</html>

<script language="javascript">
<!--
	var checkflag = false; 

	function check(field) { 
		if (checkflag == false) { 
			for (i = 0; i < field.length; i++) { 
			field[i].checked = true; 
			} 
		checkflag = true; 
		}else { 
			for (i = 0; i < field.length; i++) { 
			field[i].checked = false; 
			} 
		checkflag = false; 
		} 
	}
	
	function go(){
		var f = document.eForm;
		var division = f.division.value;
		location.href = "../servlet/ExtraWorkServlet?mode=ewReqList&div="+division;
	}
	
	function go_approval(){
	
		f = document.eForm;
		var cid_plus="";
		var s_count = 0;
		var checkflag = false; 

		for(i=0;i<f.length;i++){
			var no = "no"+i;
			
			if(f[i].checked){
				if(f[i].value!="undefined" && f[i].value!="on") {
					cid_plus += f[i].value+";";
					s_count ++;}
			}
		}
		
		if(s_count == 0){
		   alert("결재상신할 대상을 먼저 선택하십시오.");
		   return;
		}
		
		if(confirm("선택된 신청건을 전자결재 상신하시겠습니까?")) {
			location.href = "../gw/approval/module/extrawork_FP_App.jsp?ono_plus=" + cid_plus + "";
		}
	}
//-->
</script>
