<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "����˻� �˻�"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
%>
<%
	//�Ķ���� �ޱ�
	String chg_position = Hanguel.toHanguel(request.getParameter("chg_position"))==null?"":Hanguel.toHanguel(request.getParameter("chg_position"));
	String trouble = Hanguel.toHanguel(request.getParameter("trouble"))==null?"":Hanguel.toHanguel(request.getParameter("trouble"));
	String condition = Hanguel.toHanguel(request.getParameter("condition"))==null?"":Hanguel.toHanguel(request.getParameter("condition"));
	String solution = Hanguel.toHanguel(request.getParameter("solution"))==null?"":Hanguel.toHanguel(request.getParameter("solution"));
	

%>
<HTML>
<HEAD><TITLE>ECO �󼼰˻�(����)</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<form name="eForm" method="post" style="margin:0" onSubmit='javascript:goSearch();'>
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center">
		<!--Ÿ��Ʋ-->
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
				<TR><TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_eco_dsearch.gif" border='0' align='absmiddle' alt='ECO �󼼰˻�'></TD></TR>
				<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY>
		</TABLE>
	<TR><TD height=35><!--��ư-->
		<TABLE cellSpacing=0 cellPadding=0 border=0>
			<TBODY>
				<TR><TD align=left style='padding-left:12px'>
					<SELECT name=kind_search onChange='javascript:goSearchPage();'>
						<OPTION value='searchContent'>ECO ����˻�</OPTION>						
						<OPTION value='searchBase'>�⺻����</OPTION>	
						<OPTION value='searchCondition'>ECO ��������</OPTION>
						<OPTION value='searchContent'>ECO ����˻�</OPTION>						
											
					</SELECT>
						<!--<A href="javascript:goSearch();"><IMG src='../images/bt_search3.gif' border='0' align='absmiddle'></a>-->
				</TD></TR></TBODY></TABLE></TD>
		</TR>
	<TR><TD align='middle'>
		<TABLE cellspacing=0 cellpadding=2 width="94%" border=0>
			<tbody>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
         		<TR><TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�������</TD>
					<TD width="80%" height="25" class="bg_04"  colspan=3>
						<input type="text" name="condition" value="<%=condition%>" size="30"></td>
				</TR>
				
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">ECO���볻��</TD>
					<TD width="80%" height="25" class="bg_04" colspan=3>
					<input type="text" name="solution" value="<%=solution%>" size="30">
					</TD>
				</TR>
				
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�������</TD>
					<TD width="80%" height="25" class="bg_04">
					<input type="text" name="chg_position" value="<%=chg_position%>" size="30"></TD>
				</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�����з�</TD>
					<TD width="80%" height="25" class="bg_04">
						<select name="trouble"> 
					<OPTGROUP label='---------------'>
					<option value=''></option>
					<%
						String sel = "";
						String[] trb_data = {"��ǰ","����","����","����"};
						for(int i=0; i<trb_data.length; i++) {
							if(trouble.equals(trb_data[i])) sel = "selected";
							else sel = "";
							out.print("<option "+sel+" value='"+trb_data[i]+"'>"+trb_data[i]+"</option>");
						} 
					%></select></TD>
				</TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD height=99 colspan="2"></TD></TR></TBODY></TABLE></TD></TR>

			<!--������-->
			<TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
				<TBODY>
					<TR>
						<TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><INPUT type='image' src='../images/bt_search3.gif' onfocus='blur()' align='absmiddle'> <a href='javascript:self.close()'><img src='../images/bt_close.gif' border='0' align='absmiddle'></a></TD>
					</TR>
					<TR><TD width="100%" height=1 bgcolor="0C2C55"></TD></TR>
	        </TBODY></TABLE></FORM>
		</TD></TR>
</TABLE>
</BODY>
</HTML>

<script language=javascript>
<!--
//�󼼰˻� ����б�
function goSearchPage(){
	var kindOfSearch = document.eForm.kind_search.value;
	document.eForm.action = eval("'"+kindOfSearch+".jsp"+"'");
	document.eForm.submit();
}

//�˻������ϱ�
function goSearch()
{
	var f = document.eForm;

	var chg_position	= f.chg_position.value;
	var trouble			= f.trouble.value;
	var condition		= f.condition.value;
	var solution		= f.solution.value;

	var para = "&chg_position="+chg_position+"&trouble="+trouble+"&condition="+condition+"&solution="+solution;

	opener.location.href = "../../servlet/CbomHistoryServlet?mode=sch_content"+para;
	self.close();

}

//��ȿ���� ã��
function OpenCalendar(FieldName) {
	var strUrl = "../Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,'search_detail','1','1','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//â���� ����
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}
-->
</script>