<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "���� Template LIST"		
	contentType = "text/html; charset=euc-kr" 
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.util.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.bm.entity.mbomEnvTable table;
	com.anbtech.date.anbDate anbdt = new anbDate();

	//----------------------------------------------------
	//	�Ķ���� �б�
	//----------------------------------------------------
	String msg = (String)request.getAttribute("msg"); 
	int tg = msg.indexOf("������");		//������ �ִ��� �Ǵ��ϱ�

	String model_code = (String)request.getAttribute("model_code"); 

	//----------------------------------------------------
	//	�Է�/���� ���� �б�
	//----------------------------------------------------
	String pid="",gid="",parent_code="",child_code="",level_no="0";
	com.anbtech.bm.entity.mbomStrTable item;
	item = (mbomStrTable)request.getAttribute("ITEM_List");

	pid = item.getPid();
	gid = item.getGid();
	parent_code = item.getParentCode();
	child_code = item.getChildCode();
	level_no = item.getLevelNo();
	
	//-----------------------------------
	//	������� BOM ����Ʈ
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("TEMP_List");
	table = new mbomEnvTable();
	Iterator table_iter = table_list.iterator();

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<script language=javascript>
<!--
//�޽��� �����ϱ�
var msg = '<%=msg%>';
if(msg.length > 0) {
	alert(msg);
}

//�������ø� BOM���� ����ϱ� 
function regTemplate() 
{
	var msg = document.eForm.msg.value;
	if(msg.length != 0) { alert(msg); return;}

	var parent_code = document.eForm.parent_code.value;
	if(parent_code == '') { return;}

	var f = document.eForm.checkbox; 
	var s_count = 0;
	var tag = "";
    for(i=0;i<f.length;i++){
		if(f[i].checked){
			tag = f[i].value;
			s_count++;
		}
    }
	
    if(s_count == 0){
	   alert("�������ø��� ������ ��, �����ϼ���.");
	   return;
    } else if(s_count > 1) {
		alert("�ϳ��� ������ ��, �����ϼ���.");
	   return;
	}
	
	document.eForm.action='../servlet/BomTemplateServlet';
	document.eForm.mode.value='temp_write';
	document.eForm.tag.value=tag;
	document.eForm.submit();
}

-->
</script>

<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>
<FORM name="eForm" method="post" style="margin:0" encType="multipart/form-data">

<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD>	
	<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=25><!-- Ÿ��Ʋ -->
			<TD vAlign="center" class="bg_05" style="padding-left:5px" background='../bm/images/title_bm_bg.gif'>���� TEMPLATE LIST</TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<TR height=25><!-- ��ư �� �˻�-->
			<TD vAlign="center" style="padding-left:5px" bgcolor=''>
				<a href='Javascript:regTemplate();'><img src='../bm/images/bt_reg.gif' border='0' align='absmiddle'></a></TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<!--����Ʈ-->
		<TR height=100%>
			<TD vAlign=top>
			<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">	
				<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
				<TBODY>
					<TR vAlign=middle height=25>
						<TD noWrap width=30 align=middle class='list_title'>No</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						<TD noWrap width=80 align=middle class='list_title'>�����ڵ�</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						<TD noWrap width=300 align=middle class='list_title'>��������</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>�����ڵ�</TD></TR>
					<TR bgColor=#9DA9B9 height=1><TD colspan=10></TD></TR>
	<%  
		while(table_iter.hasNext()) {
			table = (mbomEnvTable)table_iter.next();
	%>	
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle height="24" class='list_bg'>
							<INPUT type="checkbox" name="checkbox" value='<%=table.getTag()%>'></TD>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle height="24">&nbsp;<%=table.getMCode()%></TD>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'>&nbsp;<%=table.getSpec()%></TD>
						<TD><IMG height=1 width=1></TD>
						<TD align=left class='list_bg'>&nbsp;<%=table.getTag()%></TD>
					</TR>
					<TR><TD colSpan=10 background="../bm/images/dot_line.gif"></TD></TR>
	<% 	}  //while 	%>
			</TBODY></TABLE></DIV></TD></TR>
			</TBODY></TABLE>
</TD></TR></TABLE>
<INPUT type="hidden" name="mode" value=''>
<INPUT type='hidden' name='pid' value='<%=pid%>'>
<INPUT type='hidden' name='gid' value='<%=gid%>'>
<INPUT type='hidden' name='parent_code' value='<%=child_code%>'>
<INPUT type='hidden' name='level_no' value='<%=level_no%>'>
<INPUT type="hidden" name="flag" value=''>
<INPUT type="hidden" name="tag" value=''>
<INPUT type="hidden" name="model_code" value='<%=model_code%>'>

<% if(tg != -1) out.println("<INPUT type='hidden' name='msg' value='"+msg+"'>");
   else out.println("<INPUT type='hidden' name='msg' value=''>");
%>
</FORM>
</BODY>
</HTML>

<SCRIPT language='javascript'>
<!--
// ������������ ���������� �и��� �ʵ��� ó��
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	
	var c_h = this.document.body.scrollHeight; // ���� �������� ũ��
	//var div_h = h - 672;
	var div_h = c_h - 57;
	item_list.style.height = div_h;
}
-->
</SCRIPT>