<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "�������� �˻�"		
	contentType = "text/html; charset=euc-kr" 		 
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.bm.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>

<%	
	com.anbtech.date.anbDate anbdt = new anbDate();	
	com.anbtech.bm.entity.mbomEnvTable ecr = new mbomEnvTable();
	com.anbtech.bm.entity.mbomEnvTable ecf = new mbomEnvTable();
	com.anbtech.bm.entity.mbomEnvTable ecs = new mbomEnvTable();
	com.anbtech.bm.entity.mbomEnvTable eck = new mbomEnvTable();

	//connection open
	com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
	Connection con = connMgr.getConnection("mssql");
	com.anbtech.dcm.db.CbomModifyDAO cmodDAO = new com.anbtech.dcm.db.CbomModifyDAO(con);	

	//�����׸� �б�
	ArrayList ecr_list = new ArrayList();
	ecr_list = cmodDAO.getEccItem("F01");			//[F01:��������]
	Iterator ecr_iter = ecr_list.iterator();

	ArrayList ecf_list = new ArrayList();
	ecf_list = cmodDAO.getEccItem("F02");			//[F02:���뱸��]
	Iterator ecf_iter = ecf_list.iterator();

	ArrayList ecs_list = new ArrayList();
	ecs_list = cmodDAO.getEccItem("F03");			//[F03:�������]
	Iterator ecs_iter = ecs_list.iterator();

	ArrayList eck_list = new ArrayList();
	eck_list = cmodDAO.getEccItem("F04");			//[F04:��������]
	Iterator eck_iter = eck_list.iterator();

	//connection close
	connMgr.freeConnection("mssql",con);

	//�Ķ���� �ޱ�
	String ecc_reason = Hanguel.toHanguel(request.getParameter("ecc_reason"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_reason"));
	String ecc_factor = Hanguel.toHanguel(request.getParameter("ecc_factor"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_factor"));
	String ecc_scope = Hanguel.toHanguel(request.getParameter("ecc_scope"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_scope"));
	String ecc_kind = Hanguel.toHanguel(request.getParameter("ecc_kind"))==null?"":Hanguel.toHanguel(request.getParameter("ecc_kind"));
	String pdg_code = Hanguel.toHanguel(request.getParameter("pdg_code"))==null?"":Hanguel.toHanguel(request.getParameter("pdg_code"));
	String pd_code = Hanguel.toHanguel(request.getParameter("pd_code"))==null?"":Hanguel.toHanguel(request.getParameter("pd_code"));
	String e_fg_code = Hanguel.toHanguel(request.getParameter("e_fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("e_fg_code"));
	String a_fg_code = Hanguel.toHanguel(request.getParameter("a_fg_code"))==null?"":Hanguel.toHanguel(request.getParameter("a_fg_code"));
	String part_code = Hanguel.toHanguel(request.getParameter("part_code"))==null?"":Hanguel.toHanguel(request.getParameter("part_code"));
	String item_code = Hanguel.toHanguel(request.getParameter("item_code"))==null?"":Hanguel.toHanguel(request.getParameter("item_code"));
%>

<HTML>
<HEAD><TITLE>ECO �󼼰˻�(���� ����)</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet type="text/css">
</HEAD>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<form name="eForm" method="post" style="margin:0"  onSubmit='javascript:goSearch();'>

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
						<OPTION value='searchCondition'>ECO ��������</OPTION>
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
				<TR><TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">��ǰ��</TD>
					<TD width="30%" height="25" class="bg_04">
					<input type="text" name="pdg_code" value="<%=pdg_code%>" size="15"></TD>
					<TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">��ǰ</TD>
					<TD width="30%" height="25" class="bg_04">
					<input type="text" name="pd_code" value="<%=pd_code%>" size="15"></TD>
				</TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
         		<TR><TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�������</TD>
					<TD width="80%" height="25" class="bg_04"  colspan=3>
						<select name="ecc_reason"> 
						<OPTGROUP label='--------------'>
						<option value=''></option>
						<%
							String rsel = "";
							while(ecr_iter.hasNext()) {
								ecr = (mbomEnvTable)ecr_iter.next(); 
								if(ecc_reason.equals(ecr.getSpec())) rsel = "selected";
								else rsel = "";
								out.print("<option "+rsel+" value='"+ecr.getSpec()+"'>"+ecr.getSpec()+"</option>");
							} 
						%></select>
				</TR>
				
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">���뱸��</TD>
					<TD width="80%" height="25" class="bg_04" colspan=3>
					<select name="ecc_factor"> 
					<OPTGROUP label='---------------'>
					<option value=''></option>
					<%
						String fsel = "";
						while(ecf_iter.hasNext()) {
							ecf = (mbomEnvTable)ecf_iter.next(); 
							if(ecc_factor.equals(ecf.getSpec())) fsel = "selected";
							else fsel = "";
							out.print("<option "+fsel+" value='"+ecf.getSpec()+"'>"+ecf.getSpec()+"</option>");
						} 
					%></select>
					</TD>
				</TR>
				
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�������</TD>
					<TD width="80%" height="25" class="bg_04" colspan=3>
					<select name="ecc_scope"> 
					<OPTGROUP label='---------------'>
					<option value=''></option>
					<%
						String ssel = "";
						while(ecs_iter.hasNext()) {
							ecs = (mbomEnvTable)ecs_iter.next(); 
							if(ecc_scope.equals(ecs.getSpec())) ssel = "selected";
							else ssel = "";
							out.print("<option "+ssel+" value='"+ecs.getSpec()+"'>"+ecs.getSpec()+"</option>");
						} 
					%></select></TD>
				</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">��������</TD>
					<TD width="80%" height="25" class="bg_04" colspan=3>
						<select name="ecc_kind"> 
						<OPTGROUP label='---------------'>
						<option value=''></option>
					<%
						String ksel = "";
						while(eck_iter.hasNext()) {
							eck = (mbomEnvTable)eck_iter.next(); 
							if(ecc_kind.equals(eck.getSpec())) ksel = "selected";
							else ksel = "";
							out.print("<option "+ksel+" value='"+eck.getSpec()+"'>"+eck.getSpec()+"</option>");
						} 
					%></select></TD>
				</TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�����߻���</TD>
					<TD width="80%" height="25" class="bg_04" colspan=3>
						FG <input type="text" name="e_fg_code" value="<%=e_fg_code%>" size="12">
						��ǰ <input type="text" name="part_code" value="<%=part_code%>" size="12">
					</TD>
				</TR>
			
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�����</TD>
					<TD width="80%" height="25" class="bg_04" colspan=3>
						FG <input type="text" name="a_fg_code" value="<%=a_fg_code%>" size="12">
						��ǰ <input type="text" name="item_code" value="<%=item_code%>" size="12"></td>
				</TR>

				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD height=20 colspan="2"></TD></TR></TBODY></TABLE></TD></TR>

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

	var ecc_reason	= f.ecc_reason.value;
	var pdg_code	= f.pdg_code.value;
	var ecc_factor	= f.ecc_factor.value;
	var pd_code		= f.pd_code.value;
	var ecc_scope	= f.ecc_scope.value;
	var e_fg_code	= f.e_fg_code.value;
	var part_code	= f.part_code.value;
	var ecc_kind	= f.ecc_kind.value;
	var a_fg_code	= f.a_fg_code.value;
	var item_code	= f.item_code.value;
	
	var para = "&ecc_reason="+ecc_reason+"&pdg_code="+pdg_code+"&ecc_factor="+ecc_factor+"&pd_code="+pd_code+"&ecc_scope="+ecc_scope+"&e_fg_code="+e_fg_code+"&part_code="+part_code+"&ecc_kind="+ecc_kind+"&a_fg_code="+a_fg_code+"&item_code="+item_code;

	opener.location.href = "../../servlet/CbomHistoryServlet?mode=sch_condition"+para;
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