<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.ew.entity.*"
%>
<jsp:useBean id="recursion" class="com.anbtech.admin.db.makeClassTree"/>

<%
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	EWLinkTable ewLinkTable;
	ExtraWorkHistoryTable table ;

	String division		= request.getParameter("div");

	//리스트 가져오기
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("arry");
	Iterator table_iter = table_list.iterator();
%>


<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../ew/css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<form method="get" name="eForm" action="../servlet/ExtraWorkServlet" style="margin:0">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- 타이틀 및 페이지 정보 -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../ew/images/blet.gif" align="absmiddle"> 정산처리대상</TD>
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
				 	<SELECT name='division' onchange="javascript:go()">
					<%=recursion.viewComboByCode(0,0)%>
					</SELECT>
					<%	if(!division.equals("")){	%>
						<script language='javascript'>
							document.eForm.division.value = '<%=division%>';
						</script>
					<%	}	%>			  
				<IMG src="../ew/images/bt_jungsan.gif" onclick="javascript:go_jungsan()" border="0" align="absmiddle" style="CURSOR: hand">
			  </TD>
			  <TD width='' align='right' style="padding-right:10px"></TD>
			  <TD align=middle width=150></TD>
			  </TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
 
  <TR height=100%><!--리스트-->
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=30 align=middle class='list_title'><input type="checkbox" name="checkbox" onClick="check(document.eForm.checkbox)"></TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>부서명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>직급</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>특근자명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>특근자사번</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=250 align=middle class='list_title'>특근일시</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>특근시간(분)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../ew/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=15></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (ExtraWorkHistoryTable)table_iter.next();

		String o_no				= "" + table.getOno();
		String division_name	= table.getDivisionName();
		String rank_name		= table.getMemberRankName();
		String member_name		= table.getMemberName();
		String member_id		= table.getMemberId();
		String r_sdate			= table.getRsdate();
		String r_stime			= table.getRstime();
		String r_edate			= table.getRedate();
		String r_etime			= table.getRetime();
		r_sdate = r_sdate.substring(0,4) + "-" + r_sdate.substring(4,6) + "-" + r_sdate.substring(6,8);
		r_edate = r_edate.substring(0,4) + "-" + r_edate.substring(4,6) + "-" + r_edate.substring(6,8);
		String result_time = table.getResultTime();
		String pay_by_work = table.getPayByWork();
		String total_time		=	table.getTotalTime();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><input type="checkbox" name="checkbox" value="<%=o_no%>"></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=division_name%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=rank_name%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=member_name%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=member_id%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=r_sdate%> <%=r_stime%> ~ <%=r_edate%> <%=r_etime%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=total_time%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'></td>
			</TR>
			<TR><TD colSpan=15 background="../ew/images/dot_line.gif"></TD></TR>
<%
		no++;
	}
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
		location.href = "../servlet/ExtraWorkServlet?mode=ew_process_list&div="+division;
	}
	
	function go_jungsan(){
		f = document.eForm;

		var division = f.division.value;
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
		   alert("정산할 대상을 먼저 선택하십시오.");
		   return;
		}
		
		if(confirm("선택된 대상에 대해서 정산을 하시겠습니까?")) {
			location.href = "../servlet/ExtraWorkServlet?mode=process_jungsan&ono_plus="+cid_plus+"&div="+division;
		}
	}
//-->
</script>
