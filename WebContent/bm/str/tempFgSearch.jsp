<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "��Ȯ�� BOM FG�ڵ� �˻� LIST [������ ����]"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String gid = (String)request.getAttribute("gid"); 	if(gid == null) gid = "";
	String sItem = (String)request.getAttribute("sItem"); if(sItem == null) sItem = "pid";
	String sWord = (String)request.getAttribute("sWord"); if(sWord == null) sWord = "";

	//-----------------------------------
	//	��Ȯ�� BOM ����Ʈ
	//-----------------------------------
	com.anbtech.bm.entity.mbomMasterTable xbom;
	ArrayList xbom_list = new ArrayList();
	xbom_list = (ArrayList)request.getAttribute("XBOM_List");
	xbom = new mbomMasterTable();
	Iterator xbom_iter = xbom_list.iterator();
	int cnt = xbom_list.size();

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'  oncontextmenu="return false"  onload='display()'>
<FORM name="sForm" method="post" style="margin:0" encType="multipart/form-data"  onSubmit='javascript:goSearch();return false;'>
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD>	
	<TABLE height="100%" cellSpacing=0 cellPadding=1 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=25><TD vAlign='center' style='padding-left:5px' class='bg_05' background='../bm/images/title_bm_bg.gif'>��Ȯ�� BOM ã��</TD></TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
		<TR height=25>
			<TD vAlign=top align=left style="padding-left:5px" bgcolor=''>
				<SELECT name="sItem" style=font-size:9pt;color="black";>  
				<%
					String[] sitems = {"model_code","model_name","fg_code"};
					String[] snames = {"���ڵ�","�𵨸�","FG�ڵ�"};
					String sel = "";
					for(int si=0; si<sitems.length; si++) {
						if(sItem.equals(sitems[si])) sel = "selected";
						else sel = "";
						out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
					}
				%>
				</SELECT>
				<INPUT type="text" name="sWord" size="10" value="<%=sWord%>">
				<input type='image' src="../bm/images/bt_search3.gif" border="0" align="absmiddle">
			</TD></TR>
		<TR height=100%><TD vAlign=top>
			<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">
			<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR bgColor=#9DA9B9 height=1><TD colspan=4></TD></TR>
				<TR vAlign=middle height=23>
					<TD noWrap width=90 align=middle class='list_title'>���ڵ�</TD>
					<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep2.gif"></TD>
					<TD noWrap width=90 align=middle class='list_title'>F/G�ڵ�</TD></TR>
				<TR bgColor=#9DA9B9 height=1><TD colspan=4></TD></TR>
<%
	while(xbom_iter.hasNext()) {
		xbom = (mbomMasterTable)xbom_iter.next(); 
%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle class='list_bg'  height=24><%=xbom.getModelCode()%></td>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle class='list_bg'><%=xbom.getFgCode()%></td>
					</TR>
					<TR><TD colspan=4 background="../bm/images/dot_line.gif"></TD></TR>
<%	}
%>				</TBODY></TABLE></DIV></TD></TR></TBODY></TABLE>
</TD></TR></TABLE>

<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="gid" size="15" value="">
<INPUT type="hidden" name="permit" size="15" value="Y">
</FORM>

<DIV id="lding" style="position:absolute;left:10px;top:80px;width:250px;height:100px;visibility:hidden;">
		<img src='../bm/images/loading8.gif' border='0' width='214' height='200'>
</DIV>

</BODY>
</HTML>

<SCRIPT language=javascript>
<!--
//�˻��ϱ�
function goSearch()
{
	document.sForm.action='../servlet/BomTemplateServlet';
	document.sForm.mode.value='temp_search';
	document.sForm.submit();
}
//BOM TEXT,����غ� ����
function goBranch(gid,parent_code,fg_code)
{
	//������ �а� �ִ��� �Ǵ��Ͽ� �а� ������ return�ϱ�
	//������ ������ �����ʿ��� permit�� Y�� �Ѵ�.(from plPartList.jsp)
	var permit = document.sForm.permit.value;
	if(permit == 'N') return;
	else document.sForm.permit.value='N';

	//������ �б��ϱ�
	//parent.tree.document.all['lding'].style.visibility="visible";	//ó���� �޽��� ���

	//BOM TEXT
	parent.tree.document.sForm.action='../servlet/BomTemplateServlet';
	parent.tree.document.sForm.mode.value='temp_list';
	parent.tree.document.sForm.gid.value=gid;
	parent.tree.document.sForm.model_code.value=parent_code;
	parent.tree.document.sForm.submit();

	//����غ��ϱ�
	parent.reg.document.eForm.action='../servlet/BomTemplateServlet';
	parent.reg.document.eForm.mode.value='temp_prewrite';
	parent.reg.document.eForm.gid.value=gid;
	parent.reg.document.eForm.model_code.value=parent_code;
	parent.reg.document.eForm.submit();

}

// ������������ ���������� �и��� �ʵ��� ó��
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	
	var c_h = this.document.body.scrollHeight; // ���� �������� ũ��
	//var div_h = h - 528 ;
	var div_h = c_h - 56;
	item_list.style.height = div_h;
}
-->
</script>