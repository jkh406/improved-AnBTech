<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "���̱��� FG�ڵ� ã��"		
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
	//	���̱��� �� ����Ʈ
	//-----------------------------------
	com.anbtech.bm.entity.mbomMasterTable fg;
	ArrayList fg_list = new ArrayList();
	fg_list = (ArrayList)request.getAttribute("FG_List");
	fg = new mbomMasterTable();
	Iterator fg_iter = fg_list.iterator();

	//-----------------------------------
	//	���̱��� ASSY ����Ʈ
	//-----------------------------------
	com.anbtech.bm.entity.mbomStrTable assy;
	ArrayList assy_list = new ArrayList();
	assy_list = (ArrayList)request.getAttribute("ASSY_List");
	assy = new mbomStrTable();
	Iterator assy_iter = assy_list.iterator();
	

%>

<SCRIPT language=javascript>
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
	document.sForm.mode.value='pst_fgsearch';
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

	//������� ���� ���̱� ������� �Ǵ��ϱ�
	var ca = parent.copy.document.sForm.fg.selectedIndex; 
	if(ca != -1) {
		var cfg = parent.copy.document.sForm.fg.options[ca].value; 
		var clist = cfg.split("|");
		if(list[0] == clist[0]) { alert('���� ��(FG�ڵ�)������ ����/���̱⸦ �� �� �����ϴ�.'); return; }
	} else { alert('�����ϱ⸦ ���� �����Ͻʽÿ�.'); return; }

	document.sForm.action='../servlet/BomBillModifyServlet';
	document.sForm.mode.value='pst_fgsearch';
	document.sForm.gid.value=list[0];
	document.sForm.submit();
}
//���̱��ϱ�
function pastePart()
{
	//������¸� ã�´�.
	var status = parent.list.document.sForm.status.value;
	if(status == 'PASTE') { alert('�����ϱ⸦ ������ ���̱⸦ �Ͻʽÿ�.'); return; }

	//�����ϱ⸦ �����ߴ����� �Ǵ��Ѵ�.
	if(status == 'COPY') {
		var pcnt = parent.list.document.eForm.pcnt.value; 
		if(pcnt == '1') { alert('���̱��� ��ǰ�� �����ϴ�.'); return; }
	}

	//�𵨼�������
	var a = document.sForm.fg.selectedIndex;
	if(a != -1) {
		var fg = document.sForm.fg.options[a].value; 
		var list = fg.split("|");
		var gid = list[0];
		var ccd = list[1];
		var model_code = list[3];
	}
	else { alert('���̱��� ���� �����Ͻʽÿ�.'); return; }

	//������� ���� ��ǰ�� �ڵ��� ���̱� ������� �Ǵ��ϱ�
	var pdg_code = parent.list.document.sForm.pdg_code.value;
	var chk_fg = document.sForm.fg.options[0].value.split("|"); 
	var pdg_ccd = chk_fg[2];
	if(pdg_code != pdg_ccd){
		alert('���� ��ǰ���� ������ ���̱⸦ �� �� �ֽ��ϴ�.'); return;  
	}

	//ASSY��������
	var b = document.sForm.assy.selectedIndex;
	if(b != -1) {
		var assy = document.sForm.assy.options[b].value; 
		var list = assy.split("|");
		var ccd = list[2]; 
	} else { alert('���̱��� ASSY�ڵ带 �����Ͻÿ�'); return; } 

	var a = confirm('���̱⸦ �����մϴ�. ����ϱ�ڽ��ϱ�?');
	if(a == false) return;

	//������ ���̱��ϱ�
	parent.list.document.all['lding'].style.visibility="visible";	//ó���� �޽��� ���
	document.all['saving'].style.visibility="visible";				//�޴���ư Disable [���̱� �ڽ�]
	parent.copy.document.all['saving'].style.visibility="visible";	//�޴���ư Disable [����]

	//Assy������ ���̱�
	parent.list.document.eForm.action='../servlet/BomBillModifyServlet';
	parent.list.document.eForm.mode.value='pst_write';
	parent.list.document.eForm.gid.value=list[0];
	parent.list.document.eForm.ln0.value=eval(list[1]-1);	//�𵨼��ýÿ� �Ȱ��� ��Ģ�� ���߱� ���� -1
	parent.list.document.eForm.pc0.value=list[2];
	parent.list.document.eForm.cc0.value=ccd;
	parent.list.document.eForm.parent_code.value=model_code;
	parent.list.document.eForm.submit();
	
}
-->
</script>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' oncontextmenu="return false">
<FORM name="sForm" method="post" style="margin:0" encType="multipart/form-data" onSubmit='javascript:goSearch();return false;'>

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 >
	<TR><TD align='left' width='50%'><!-- �������BOM�˻� -->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
		 	<TBODY>
				<TR height=25><TD vAlign="center" class='bg_05' style='padding-left:5px;' background='../bm/images/title_bm_bg.gif'>���̱���:F/G�ڵ�</TD></TR>
				<TR height=25><TD align='left' style='padding-left:5px;' bgcolor=''>
					<INPUT type='text' name='sWord' value='<%=sWord%>' size='12' onClick="document.sForm.sWord.value=''">
					<a href='javascript:goSearch();'><img src='../bm/images/bt_search.gif' align='absmiddle' border=0></TD></TR>
				<TR><TD style='padding-left:5px;'>
					<SELECT size="6" name="fg" multiple onChange='javascript:selectFG();'> 
					<OPTGROUP label='----------------------'>
					<%
						String sel = "";
						while(fg_iter.hasNext()) {
							fg = (mbomMasterTable)fg_iter.next(); 
							if(gid.equals(fg.getPid())) sel = "selected";
							else sel = "";
							out.print("<option "+sel+" value='"+fg.getPid()+"|"+fg.getFgCode());
							out.print("|"+fg.getPdgCode()+"|"+fg.getModelCode()+"'>");
							out.println(fg.getModelCode()+": "+fg.getFgCode()+"</option>");
						} 
					%>
					</SELECT></TD></TR></TBODY></TABLE></TD>
		
			<TD align='left' width='50%'><!--���̱���ASSY-->
			<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
			<TBODY>
				<TR height=25><TD vAlign="center" class='bg_05' style='padding-left:5px;' background='../bm/images/title_bm_bg.gif'>���̱���:ASSY�ڵ�</TD></TR>
				<TR height=25><TD align='left' style='padding-left:5px;' bgcolor=''>
					<a href='javascript:pastePart();'><img src='../bm/images/bt_paste.gif' border=0 alt='���̱�' align='absmiddle'></a></TD></TR>
				<TR><TD style='padding-left:5px;'>
					<SELECT size="6" name="assy" multiple>
					<OPTGROUP label='----------------'>
					<%
						while(assy_iter.hasNext()) {
							assy = (mbomStrTable)assy_iter.next(); 
							out.print("<option value='"+assy.getGid()+"|"+assy.getLevelNo()+"|"+assy.getParentCode()+"'>");
							out.println(assy.getLevelNo()+": "+assy.getParentCode()+"</option>");
						} 
					%>
					</SELECT></TD></TR>
					
					</TBODY></TABLE></TD>
			</TR></TABLE>

<INPUT type='hidden' name='sItem' value='fg_code'>
<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="gid" size="15" value="">
</FORM>

<DIV id="lding" style="position:absolute;left:180px;top:180px;width:250px;height:100px;visibility:hidden;">
		<img src='../bm/images/loading8.gif' border='0' width='214' height='200'>
</DIV>

<DIV id="saving" style="position:absolute;left:0px;top:0px;width:250px;height:100px;visibility:hidden;">
	<TABLE width="400" border="0" cellspacing=1 cellpadding=1 bgcolor="">
		<TR><TD height="50" align="center" valign="middle"></TD></TR></TABLE>
</DIV>

</BODY>
</HTML>
