<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "������ FG�ڵ� ã��"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.util.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String sWord = (String)request.getAttribute("sWord"); if(sWord == null) sWord = "";
	String gid = (String)request.getAttribute("gid"); if(gid == null) gid = "";

	//-----------------------------------
	//	������ �� ����Ʈ
	//-----------------------------------
	com.anbtech.bm.entity.mbomMasterTable fg;
	ArrayList fg_list = new ArrayList();
	fg_list = (ArrayList)request.getAttribute("FG_List");
	fg = new mbomMasterTable();
	Iterator fg_iter = fg_list.iterator();

	//-----------------------------------
	//	������ ASSY ����Ʈ
	//-----------------------------------
	com.anbtech.bm.entity.mbomStrTable assy;
	ArrayList assy_list = new ArrayList();
	assy_list = (ArrayList)request.getAttribute("ASSY_List");
	assy = new mbomStrTable();
	Iterator assy_iter = assy_list.iterator();
	

%>
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'  oncontextmenu="return false">
<FORM name="sForm" method="post" style="margin:0" encType="multipart/form-data" onSubmit='javascript:goSearch();return false;'>

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 >
	<TR><TD align='left' width='50%'><!-- Ȯ��BOM�˻� -->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
			 	<TBODY>
				<TR height=25><TD vAlign="center" class='bg_05' style='padding-left:5px;' background='../bm/images/title_bm_bg.gif'>������:Ȯ��BOM F/G�ڵ�</TD></TR>
				<TR height=25><TD align='left' style='padding-left:5px;' bgcolor=''>
					<INPUT type='text' name='sWord' value='<%=sWord%>' size='12' onClick="document.sForm.sWord.value=''"> <a href='javascript:goSearch();'><img src='../bm/images/bt_search.gif' align='absmiddle' border=0></a></TD></TR>
				<TR><TD style='padding-left:5px;'>
					<select size="6" name="fg" multiple onChange='javascript:selectFG();'> 
					<OPTGROUP label='----------------------'>
					<%
						String sel = "";
						while(fg_iter.hasNext()) {
							fg = (mbomMasterTable)fg_iter.next(); 
							if(gid.equals(fg.getPid())) sel = "selected";
							else sel = "";
							out.print("<option "+sel+" value='"+fg.getPid()+"|"+fg.getFgCode());
							out.print("|"+fg.getModelCode()+"'>");
							out.println(fg.getModelCode()+": "+fg.getFgCode()+"</option>");
						} 
					%></select></TD></TR></TBODY></TABLE></TD>
			<TD align='left' width='50%'><!--������ ASSY-->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
			 	<TBODY>
				<TR height=25><TD vAlign="center" class='bg_05' style='padding-left:5px;' background='../bm/images/title_bm_bg.gif'>������:ASSY�ڵ�</TD></TR>
				<TR height=25><TD align='left' style='padding-left:5px;' bgcolor=''>
					<a href='javascript:copyPart();'><img src='../bm/images/bt_copy.gif' align='absmiddle' border=0 alt='Copy'></a></TD></TR>
				<TR><TD  style='padding-left:5px;'>
					<select size="6" name="assy" multiple> 
					<OPTGROUP label='----------------'>
					<%
					while(assy_iter.hasNext()) {
						assy = (mbomStrTable)assy_iter.next(); 
						if(!assy.getLevelNo().equals("1")) {	
							out.print("<option value='"+assy.getGid()+"|"+assy.getLevelNo()+"|"+assy.getParentCode()+"'>");
							out.println(assy.getLevelNo()+": "+assy.getParentCode()+"</option>");
						}
					} 
					%></select></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<INPUT type='hidden' name='sItem' value='fg_code'>
<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="gid" size="15" value="">
</FORM>

<DIV id="lding" style="position:absolute;left:180px;top:50px;width:250px;height:100px;visibility:hidden;">
		<img src='../bm/images/loading8.gif' border='0' width='214' height='200'></DIV>
<DIV id="saving" style="position:absolute;left:0px;top:0px;width:250px;height:100px;visibility:hidden;">
	<TABLE width="400" border="0" cellspacing=1 cellpadding=1 bgcolor="">
		<TR><td height="50" align="center" valign="middle"></TD></TR></TABLE></DIV>

</BODY>
</HTML>

<script language=javascript>
<!--
//�˻��ϱ�
function goSearch()
{
	var sWord = document.sForm.sWord.value;
	if(sWord.length < 4) { 
		alert('�˻��� FG�ڵ带 4���̻� �Է��Ͻʽÿ�.'); 
		document.sForm.sWord.focus();
		return; 
	}

	document.sForm.action='../servlet/BomBillModifyServlet';
	document.sForm.mode.value='cpy_fgsearch';
	document.sForm.submit();
	
}
//FG�ڵ� �����ϱ�
function selectFG()
{
	var a = document.sForm.fg.selectedIndex;
	if(a != -1) {
		var fg = document.sForm.fg.options[a].value; 
		var list = fg.split("|");
	}

	document.sForm.action='../servlet/BomBillModifyServlet';
	document.sForm.mode.value='cpy_fgsearch';
	document.sForm.gid.value=list[0];
	document.sForm.submit();
}
//�����ϱ�
function copyPart()
{
	//�𵨼�������
	var a = document.sForm.fg.selectedIndex;
	if(a != -1) {
		var fg = document.sForm.fg.options[a].value; 
		var list = fg.split("|");
	} else { alert('FG�ڵ带 �����Ͻÿ�'); return; }
		
	//ASSY��������
	var b = document.sForm.assy.selectedIndex;
	if(b != -1) {
		var assy = document.sForm.assy.options[b].value; 
		var list = assy.split("|");
	} else { alert('������ ASSY�ڵ带 �����Ͻÿ�'); return; } 

	//������ �����ϱ�
	parent.list.document.all['lding'].style.visibility="visible";	//ó���� �޽��� ���
	document.all['saving'].style.visibility="visible";				//�޴���ư Disable [���� �ڽ�]
	parent.paste.document.all['saving'].style.visibility="visible";	//�޴���ư Disable [���̱�]

	//�����ϱ�
	parent.list.document.sForm.action='../servlet/BomBillModifyServlet';
	parent.list.document.sForm.mode.value='cpy_list';
	parent.list.document.sForm.gid.value=list[0];
	parent.list.document.sForm.level_no.value=list[1];
	parent.list.document.sForm.parent_code.value=list[2];
	parent.list.document.sForm.submit();
}
-->
</script>

