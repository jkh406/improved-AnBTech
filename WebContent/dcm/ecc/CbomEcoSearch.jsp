<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= "���躯�� BOM�� ���� LIST"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import		= "java.util.*"
	import		= "com.anbtech.text.*"
	import		= "com.anbtech.bm.entity.*"
	import		= "com.anbtech.dcm.entity.*"
	import		= "com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.bm.entity.mbomStrTable table;
	com.anbtech.dcm.entity.eccModelTable model;
	com.anbtech.date.anbDate anbdt = new anbDate();

	//-----------------------------------
	//	���� Parameter
	//-----------------------------------
	String fg_code = (String)request.getAttribute("fg_code");
	String model_code = (String)request.getAttribute("model_code");
	String eco_no = (String)request.getAttribute("eco_no");
	String gid = (String)request.getAttribute("gid");
	
	//-----------------------------------
	//	����� LIST
	//-----------------------------------
	ArrayList model_list = new ArrayList();
	model_list = (ArrayList)request.getAttribute("MODEL_List");
	model = new eccModelTable();
	Iterator model_iter = model_list.iterator();

	//-----------------------------------
	//	������ BOM 
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("BOM_List");
	table = new mbomStrTable();
	Iterator table_iter = table_list.iterator();

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' onload='display();' oncontextmenu="return false">
<form name="eForm" method="post" style="margin:0">
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD>
	<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
		<TBODY>
			<TR height=25><!-- Ÿ��Ʋ �� ������ ���� -->
				<TD vAlign=center bgcolor='#DBE7FD' class='bg_05' style="padding-left:5px"  BACKGROUND='../dcm/images/title_bm_bg.gif'>BOM ����</TD></TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
			<TR height=25><!-- ��ư -->
				<TD align=left style="padding-left:5px"> <IMG src='../dcm/images/app_model.gif' border='0' align='absmiddle'>
				<select name="fg_code" style=font-size:9pt;color="black"; onChange='javascript:goSearch();'> 
				<OPTGROUP label='---------------'>
				<%
					String sel = "";
					while(model_iter.hasNext()) {
						model = (eccModelTable)model_iter.next();
						String fcd = model.getFgCode();
						if(fg_code.equals(fcd)) sel = "selected";
						else sel = "";
						out.println("<option "+sel+" value='"+fcd+"'>"+fcd+"</option>");
					} 
				%>
				</select>
				<a href="javascript:checkModel();"><IMG src='../dcm/images/target_model_app.gif' border='0' align='absmiddle' alt='��������'></a>
				</TD></TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>

		<!--����Ʈ-->
			<TR height=100%>
				<TD vAlign=top>
				<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">	
					<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
						<TBODY>
							<TR vAlign=middle height=23>
								<TD noWrap width=30 align=middle class='list_title'>��ȣ</TD>
								<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
								<TD noWrap width=80 align=middle class='list_title'>LEVEL</TD>
								<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
								<TD noWrap width=100 align=middle class='list_title'>��ǰ���ڵ�</TD>
								<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
								<TD noWrap width=100 align=middle class='list_title'>��ǰ���ڵ�</TD>
								<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
								<TD noWrap width=400 align=middle class='list_title'>ǰ��԰�</TD>
								<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
								<TD noWrap width=60 align=middle class='list_title'>LOC NO</TD>
								<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
								<TD noWrap width=60 align=middle class='list_title'>�����ڵ�</TD>
							</TR>
							<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>

	<%  int cnt = 1;
		while(table_iter.hasNext()) {
			table = (mbomStrTable)table_iter.next();
			int lv = Integer.parseInt(table.getLevelNo());
			String space="&nbsp;";
			for(int i=1; i<lv; i++) space += "&nbsp;&nbsp;";
	%>	
						<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
							<TD align=middle height="24" class='list_bg'><%=cnt%></TD>
							<TD><IMG height=1 width=1></TD>
							<TD align=left height="24"><%=space%><%=table.getLevelNo()%></TD>
							<TD><IMG height=1 width=1></TD>
							<TD align=left class='list_bg'><%=table.getParentCode()%></TD>
							<TD><IMG height=1 width=1></TD>
							<TD align=left class='list_bg'><%=table.getChildCode()%></TD>
							<TD><IMG height=1 width=1></TD>
							<TD align=left class='list_bg'>&nbsp;<%=table.getPartSpec()%></TD>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getLocation()%></TD>
							<TD><IMG height=1 width=1></TD>
							<TD align=middle class='list_bg'><%=table.getOpCode()%></TD>
						</TR>
						<TR><TD colSpan=19 background="../bm/images/dot_line.gif"></TD></TR>
	<% 
		cnt++;
		}  //while 

	%>
		</TBODY></TABLE></DIV></TD></TR>
	</TBODY>
</TABLE>
</TD></TR></TABLE>

<input type="hidden" name="mode" value=''>
<input type="hidden" name="gid" value=''>
<input type="hidden" name="pid" value=''>
<input type="hidden" name="eco_no" value='<%=eco_no%>'>
</form>

<DIV id="lding" style="position:absolute;left:95px;top:95px;width:250px;height:150px;visibility:hidden;">
	<img src='../bm/images/loading8.gif' border='0' width='214' height='200'>
</DIV>

</body>
</html>

<script language=javascript>
<!--
//������ ��[FG] BOM����
function goSearch()
{
	var fg_code = document.eForm.fg_code.value;
	var eco_no = '<%=eco_no%>';

	document.all['lding'].style.visibility="visible";	//ó���� �޽��� ���

	//BOM���� ����
	document.eForm.action='../servlet/CbomChangeServlet';
	document.eForm.mode.value='eco_bomlist';
	document.eForm.submit();

	//�Է�â ����
	parent.reg.document.eForm.action='../servlet/CbomChangeServlet';
	parent.reg.document.eForm.mode.value='eco_prechg';
	parent.reg.document.eForm.fg_code.value=fg_code;
	parent.reg.document.eForm.eco_no.value=eco_no;
	parent.reg.document.eForm.gid.value='';
	parent.reg.document.eForm.submit();

	//BOM���泻�� ����
	parent.change.document.eForm.action='../servlet/CbomChangeServlet';
	parent.change.document.eForm.mode.value='eco_chglist';
	parent.change.document.eForm.fg_code.value=fg_code;
	parent.change.document.eForm.eco_no.value=eco_no;
	parent.change.document.eForm.gid.value='';
	parent.change.document.eForm.submit();

}
//�ش�ǰ�� �����ϱ�
function strView(a,b)
{
	var fg_code = document.eForm.fg_code.value;
	var eco_no = '<%=eco_no%>';
	
	parent.reg.document.eForm.action='../servlet/CbomChangeServlet';
	parent.reg.document.eForm.mode.value='eco_prechg';
	parent.reg.document.eForm.fg_code.value=fg_code;
	parent.reg.document.eForm.eco_no.value=eco_no;
	parent.reg.document.eForm.pid.value=a;
	parent.reg.document.eForm.gid.value=b;
	parent.reg.document.eForm.submit();
}
//��������
function checkModel()
{	
	document.onmousedown=dbclick;
	document.all['lding'].style.visibility="visible";	//ó���� �޽��� ���
	document.eForm.action='../servlet/CbomChangeServlet';
	document.eForm.mode.value='eco_bomlist';
	document.eForm.submit();
}

function dbclick(){
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}

// ������������ ���������� �и��� �ʵ��� ó��
function display() { 
	var w = window.screen.width; 
    var h = window.screen.height; 
	//var div_w = w - 430 ; 
	var div_h = h - 507;
	item_list.style.height = div_h;
}
-->
</script>