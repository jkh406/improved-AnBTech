<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "�������� ��ǰ ã��"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.util.*"
	import="com.anbtech.bm.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();

	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String sWord = (String)request.getAttribute("sWord"); if(sWord == null) sWord = "";
	String parent_code = (String)request.getAttribute("parent_code"); 
	if(parent_code == null) parent_code = "";
	String sel_date = (String)request.getAttribute("sel_date"); 
	if(sel_date.length() ==0) sel_date = anbdt.getDate(0);
	if(sel_date.length() == 8) sel_date = anbdt.getSepDate(sel_date,"/");

	//-----------------------------------
	//	��ǰ ����Ʈ
	//-----------------------------------
	com.anbtech.bm.entity.mbomStrTable component;
	ArrayList component_list = new ArrayList();
	component_list = (ArrayList)request.getAttribute("COMPONENT_List");
	component = new mbomStrTable();
	Iterator component_iter = component_list.iterator();
	
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<SCRIPT language=javascript>
<!--
//�˻��ϱ�
function goSearch()
{
	var sWord = document.sForm.sWord.value; 
	if(sWord.length < 4) {
		alert('�˻��� ǰ���ڵ�� 4�� �̻� �Է��ؾ� �մϴ�.'); return;
	}

	document.sForm.action='../servlet/BomShowServlet';
	document.sForm.mode.value='rev_fgsearch';
	document.sForm.submit();
}
//��ǰ �����ϱ�
function selPart()
{
	//ǰ��������
	var a = document.sForm.part_code.selectedIndex; 
	if(a != -1) {
		var part_code = document.sForm.part_code.options[a].value; 
	}
	if(a == 0) return;

	//��������
	var sel_date = document.sForm.sel_date.value;

	//������ �б��ϱ�
	parent.list.document.all['lding'].style.visibility="visible";	//ó���� �޽��� ���
	document.all['saving'].style.visibility="visible";				//�޴���ư Disable

	//TREE ����
	parent.tree.document.sForm.action='../servlet/BomShowServlet';
	parent.tree.document.sForm.mode.value='rev_tree';
	parent.tree.document.sForm.child_code.value=part_code;
	parent.tree.document.sForm.sel_date.value=sel_date;
	parent.tree.document.sForm.submit();

	//LIST ����
	parent.list.document.sForm.action='../servlet/BomShowServlet';
	parent.list.document.sForm.mode.value='rev_text';
	parent.list.document.sForm.child_code.value=part_code;
	parent.list.document.sForm.sel_date.value=sel_date;
	parent.list.document.sForm.submit();
}
//��ȿ���� ã��
function OpenCalendar(FieldName) {
	var strUrl = "../bm/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 25;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

// �˻� field �ʱ�ȭ
function empty(){
	document.sForm.sWord.value="";
}

-->
</SCRIPT>
<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' oncontextmenu="return false">
<FORM name="sForm" method="post" style="margin:0">
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=0 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD>	
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="BOTTOM">
	<TBODY>
		<TR height=25><!--��ư �� ����¡-->
			<TD vAlign="BOTTOM" width='100%' style='padding-left:5px'>
				<INPUT type='text' name='sWord' value='<%=sWord%>' size='13' align='absmiddle'  onfocus="javascript:empty();">
				<a href='javascript:goSearch();'><IMG src='../bm/images/bt_search.gif' align='absmiddle' border=0></a>
			
				<SELECT name="part_code" style=font-size:9pt;color="black"; align='absmiddle' style='padding-left:5px'> 
				<OPTGROUP label='---------------'>
			<%	
				//�˻�(sWord)�� �ְ�, �˻������ ������ : BOM������ ������ �ִ� ������ ��ǰ�� ���
				if((sWord.length() > 4) && (component_list.size() == 0)) {
						out.print("<option value='"+sWord+"'>"+sWord+"</option>");
				} else out.print("<option value=''>�ش�ǰ����</option>");

				//�˻������ ����Ѵ�. : Item Maste DB���� Assy or ��ǰ�ڵ带 �˻��� ���
				String sel = "";
				while(component_iter.hasNext()) {
					component = (mbomStrTable)component_iter.next(); 
					if(parent_code.equals(component.getParentCode())) sel = "selected";
					else sel = "";
					out.print("<option "+sel+" value='"+component.getParentCode()+"'>");
					out.println(component.getParentCode()+"</option>");
				} 
					%></SELECT>
					<a href='javascript:selPart();'><IMG src='../bm/images/bt_sel.gif' align='absmiddle' border=0></a>
				    <IMG src='../bm/images/valid_date.gif' align='absmiddle' border='0'>
					<INPUT type='text' name='sel_date' value='<%=sel_date%>' size='9'>
					<A Href="Javascript:OpenCalendar('sel_date');"><IMG src="../bm/images/bt_calendar.gif" border="0" align='absmiddle'></A>
				</TD></TR></TBODY></TABLE>
</TABLE>

<INPUT type="hidden" name="mode" size="15" value="">
</FORM>

<DIV id="saving" style="position:absolute;left:0px;top:0px;width:250px;height:10px;visibility:hidden;">
<TABLE width="700" border="0" cellspacing=1 cellpadding=1 bgcolor="">
	<TR><TD height="10" align="center" valign="middle">
	</TD> 
	</TR>
</TABLE>
</DIV>

</td></tr></TABLE>
</BODY>
</HTML>

