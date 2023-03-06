<%@ include file="../../../admin/configPopUp.jsp"%>
<%@ page language="java" 
	import="java.sql.*,com.anbtech.text.Hanguel" 
	contentType = "text/html; charset=euc-kr"
	errorPage="../../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();	//일자
	com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();	//문자열처리
	String byear=anbdt.getYear();

	//전달받은 파라미터
	String target_id = request.getParameter("target_id");
	String target_name = request.getParameter("target_name");

	//내부처리 검색제목
	String syear = request.getParameter("syear") == null?byear:Hanguel.toHanguel(request.getParameter("syear"));
	String subject = request.getParameter("subject") == null?"":Hanguel.toHanguel(request.getParameter("subject"));
	String sItem = request.getParameter("sItem") == null?"GIAN":Hanguel.toHanguel(request.getParameter("sItem"));

	//찾기
	bean.openConnection();
	String query = "select pid,app_subj,writer,writer_name from app_master ";
	query += "where app_subj like '%"+subject+"%' and flag ='"+sItem+"' ";
	query += "and write_date like '%"+syear+"%' ";
	query += "and app_state='APS' order by pid asc";
	bean.executeQuery(query);
%>
<HTML><HEAD><TITLE>관련문서 찾기</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload='display();'>
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center"><!--타이틀-->
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
				<TR><TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../../images/pop_base_sel.gif" border='0' align='absmiddle' alt='관련문서찾기'></TD></TR>
				<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
	<TR><TD height=35><!--버튼-->
		<form name="eForm" action="searchRefDocument.jsp" method="post" style="margin:0">
		<TABLE cellSpacing=0 cellPadding=0 border=0>
			<TBODY>
				<TR><TD align=left style='padding-left:12px'>
					<input type='text' name='syear' size='5' value='<%=syear%>'>
					<select name='sItem'>
						<OPTION value='GIAN'>기안서</OPTION>
					</select>
					<input type="text" name="subject" size="15"> 
					<a href='javascript:document.eForm.submit();'><img src='../../images/bt_search3.gif' border='0' align='absmiddle'></a>
					<input type="hidden" name="target_id" size="15" value='<%=target_id%>'>
					<input type="hidden" name="target_name" size="15" value='<%=target_name%>'>
				</TD></TR></TBODY></TABLE></form></TD></TR>
				
	<TR><TD height='203'>
	<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">	
		<TABLE  cellSpacing=0 cellPadding=0 width="98%" border=0 align='center'>
			<tbody>
				<TR bgColor=#9DA9B9 height=1><TD colspan=5></TD></TR>
         		<TR height=23>  
					  <TD noWrap width=100% align=middle class='list_title'>제목</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../../images/list_tep2.gif"></TD>
					  <TD noWrap width=150 align=middle class='list_title'>기안자</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../../images/list_tep2.gif"></TD>
					  <TD noWrap width=60 align=middle class='list_title'>비고</TD>
				</TR>
				<TR bgColor=#9DA9B9 height=1><TD colspan=5></TD></TR>
<%
		String sub = "";
		
		while(bean.next()){
		sub = str.repWord(bean.getData("app_subj"),">",")");
		sub = str.repWord(sub,"<","(");
		sub = str.repWord(sub,"\"","");

		String sel = "<a href=\"javascript:opener."+target_id+".value='"+bean.getData("pid")+"';opener."+target_name+".value='"+sub+"';self.close();\"><IMG src='../../images/lt_sel.gif' align='absmiddle' border='0' alt='선택'></a>";
%>

				<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=left height="24" class='list_bg' style='padding-left:5;'><%=bean.getData("app_subj")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=center class='list_bg' ><%=bean.getData("writer")%>/<%=bean.getData("writer_name")%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=sel%></td>
				</TR>
				<TR><TD colSpan=5 background="../../images/dot_line.gif"></TD></TR>
<%			}
%>			
			</TBODY></TABLE></DIV></TD></TR>

			<!--꼬릿말-->
			<TR><TD>
				<TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
				<TBODY>
					<TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
						<a href='javascript:self.close()'><img src='../../images/bt_close.gif' border='0' align='absmiddle'></a></TD>
					</TR>
					<TR><TD width="100%" height=1 bgcolor="0C2C55"></TD></TR>
	        </TBODY></TABLE></TD></TR>

</TD></TR></FORM>
</TABLE>
</BODY>
</HTML>

<script language="javascript">
<!--

function returnSelected()
{
	var num = document.eForm.pList.selectedIndex;
	var code = document.eForm.pList.options[num].value;
	var name = document.eForm.pList.options[num].text;

	var sel = confirm("프로젝트명 '"+name+"'를 선택하시겠습니까?");

	if(sel){
		window.returnValue = code +"|" + name;
		self.close();
	}
}

// 페이지정보가 오른쪽으로 밀리지 않도록 처리
function display() { 
	var w = window.screen.width; 
    var h = window.screen.height; 
	
	var div_h = h - 595;
	item_list.style.height = div_h;
}	
-->
</script>
