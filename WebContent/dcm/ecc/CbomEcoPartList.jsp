<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= "���躯�� BOM LIST"		
	contentType = "text/html; charset=euc-kr" 		
	import		= "java.io.*"
	import		= "java.util.*"
	import		= "com.anbtech.text.*"
	import		= "com.anbtech.dcm.entity.*"
	import		= "com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.dcm.entity.eccBomTable table;
	com.anbtech.date.anbDate anbdt = new anbDate();

	//-----------------------------------
	//	���� Parameter
	//-----------------------------------
	String fg_code = (String)request.getAttribute("fg_code");
	String eco_no = (String)request.getAttribute("eco_no");						//���Ժ��� ��ȣ
	String gid = (String)request.getAttribute("gid");							//�����ڵ�
	String msg = (String)request.getAttribute("msg");	if(msg==null) msg="";	//�޽��� ����
	
	//-----------------------------------
	//	����� BOM LIST 
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("CHANGE_List");
	table = new eccBomTable();
	Iterator table_iter = table_list.iterator();

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<script language=javascript>
<!--
//�޽��� ����
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
}
//�ش�ǰ�� �����ϱ�
function strView(a,b)
{
	var fg_code = document.eForm.fg_code.value;
	var eco_no = document.eForm.eco_no.value;

	parent.reg.document.eForm.action='../servlet/CbomChangeServlet';
	parent.reg.document.eForm.mode.value='item_prechg';
	parent.reg.document.eForm.fg_code.value=fg_code;
	parent.reg.document.eForm.eco_no.value=eco_no;
	parent.reg.document.eForm.pid.value=a;
	parent.reg.document.eForm.gid.value=b;
	parent.reg.document.eForm.submit();
}
//���� �˻��ϱ�
function checkModel()
{
	var gid = document.eForm.gid.value;
	var eco_no = document.eForm.eco_no.value;
	var para = "gid="+gid+"&eco_no="+eco_no;
	wopen("../servlet/CbomChangeServlet?mode=eco_fgsearch&"+para,"proxy","450","400","scrollbars=no,toolbar=no,status=no,resizable=yes");
}
//�����ǰ ��
function compareItem()
{
	var eco_no = document.eForm.eco_no.value;
	var para = "eco_no="+eco_no;
	wopen("../servlet/CbomChangeServlet?mode=eco_itemcomp&"+para,"proxy","550","270","scrollbars=no,toolbar=no,status=no,resizable=no");
}

//BOM��� �˻��ϱ�
function checkBOM()
{
	//�˻������ϱ�
	var gid = document.eForm.gid.value;	wopen("../servlet/BomInputServlet?mode=chk_list&gid="+gid,"proxy","550","260","scrollbars=no,toolbar=no,status=no,resizable=no");

}

//â���� ����
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}

// ������������ ���������� �и��� �ʵ��� ó��
function display() { 
	var w = window.screen.width; 
    var h = window.screen.height; 
	//var div_w = w - 430 ; 
	var div_h = h - 515;
	item_list.style.height = div_h;
}
-->
</script>
<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>
<form name="eForm" method="post" style="margin:0">
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD>
	<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
		<TBODY>
			<TR height=25><!-- Ÿ��Ʋ �� ������ ���� -->
				<TD vAlign=center bgcolor='#DBE7FD' class='bg_05' style="padding-left:5px"  BACKGROUND='../bm/images/title_bm_bg.gif'> ����� BOM LIST</TD></TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
			<TR height=25>
				<TD align=left style="padding-left:5px">
					<A href="javascript:checkModel();"><IMG src='../dcm/images/bt_tmodel_search.gif' border='0' align='absmiddle' alt='���𵨰˻�'></a>
					<A href="javascript:compareItem();"><IMG src='../dcm/images/bt_chgitem_chk.gif' border='0' align='absmiddle' alt='�����ǰ��'></a>
					<A href="javascript:checkBOM();"> <IMG src='../dcm/images/bt_bm_chk.gif' border='0' align='absmiddle' alt='BOM�˻�'></a>
				</TD></TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
	<!--����Ʈ-->
	<TR height=100%>
		<TD vAlign=top>
			<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">	
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR vAlign=middle height=23>
					  <TD noWrap width=60 align=middle class='list_title'>�������</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
					  <TD noWrap width=20 align=middle class='list_title'>Level</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
					  <TD noWrap width=80 align=middle class='list_title'>��ǰ���ڵ�</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
					  <TD noWrap width=80 align=middle class='list_title'>��ǰ���ڵ�</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
					  <TD noWrap width=400 align=middle class='list_title'>ǰ��԰�</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
					  <TD noWrap width=60 align=middle class='list_title'>LOC NO</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
					  <TD noWrap width=60 align=middle class='list_title'>�����ڵ�</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
					  <TD noWrap width=200 align=middle class='list_title'>��������</TD>
					  <TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
					  <TD noWrap width=200 align=middle class='list_title'>��Ÿ�ǰ�</TD>
					</TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>

	<%  int cnt = 1;
		while(table_iter.hasNext()) {
			table = (eccBomTable)table_iter.next();
	%>	
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
					  <TD align=left height="23" class='list_bg'>&nbsp;<%=table.getAdTag()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle height="24"><%=table.getLevelNo()%></TD>
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
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><%=table.getEccReason()%></TD>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=left class='list_bg'><%=table.getNote()%></TD>
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
<input type="hidden" name="gid" value='<%=gid%>'>
<input type="hidden" name="pid" value=''>
<input type="hidden" name="eco_no" value='<%=eco_no%>'>
<input type="hidden" name="fg_code" value='<%=fg_code%>'>
</form>

</body>
</html>

