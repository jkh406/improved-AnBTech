<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "�������� FG ã��"		
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
	String gid = (String)request.getAttribute("gid"); if(gid == null) gid = "";
	String parent_code = (String)request.getAttribute("parent_code"); 
	if(parent_code == null) parent_code = "";

	String sel_date = (String)request.getAttribute("sel_date"); 
	if(sel_date.length() ==0) sel_date = anbdt.getDate(0);
	if(sel_date.length() == 8) sel_date = anbdt.getSepDate(sel_date,"/");

	String step = (String)request.getAttribute("step"); if(step.length() ==0) step="M";

	//-----------------------------------
	//	FG ����Ʈ
	//-----------------------------------
	com.anbtech.bm.entity.mbomMasterTable fg;
	ArrayList fg_list = new ArrayList();
	fg_list = (ArrayList)request.getAttribute("FG_List");
	fg = new mbomMasterTable();
	Iterator fg_iter = fg_list.iterator();

	//-----------------------------------
	//	ASSY ����Ʈ
	//-----------------------------------
	com.anbtech.bm.entity.mbomStrTable assy;
	ArrayList assy_list = new ArrayList();
	assy_list = (ArrayList)request.getAttribute("ASSY_List");
	assy = new mbomStrTable();
	Iterator assy_iter = assy_list.iterator();
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
	document.sForm.mode.value='frd_fgsearch';
	document.sForm.gid.value='';
	document.sForm.parent_code.value='';
	document.sForm.level_no.value='0';
	document.sForm.step.value='M';
	document.sForm.submit();

}
//FG�����ϱ�
function selectFG()
{
	//�𵨼�������
	var a = document.sForm.fg_code.selectedIndex;
	if(a != -1) {
		var fg = document.sForm.fg_code.options[a].value.split("|"); 
		var gid = fg[0];
		var model_code = fg[1];
	}

	//��������
	var sel_date = document.sForm.sel_date.value;

	//���ó��� ����
	document.sForm.action='../servlet/BomShowServlet';
	document.sForm.mode.value='frd_fgsearch';
	document.sForm.gid.value=gid;
	document.sForm.level_no.value='0';
	document.sForm.parent_code.value=model_code;
	document.sForm.sel_date.value=sel_date;
	document.sForm.step.value='M';
	document.sForm.submit();
}
//BOM ��ȸ
function goView()
{
	//�𵨼�������
	var a = document.sForm.fg_code.selectedIndex; 
	if(a != -1) {
		var fg = document.sForm.fg_code.options[a].value.split("|"); 
		var gid = fg[0];
		var model_code = fg[1];
		var org_model_code = fg[1];
	}

	//ASSY��������
	var level_no = "0";
	var b = document.sForm.assy_code.selectedIndex;
	if(b != -1) {
		var assy_code = document.sForm.assy_code.options[b].value;
		var assy_lvn = assy_code.split("|");
		level_no = assy_lvn[0];
		if(b != 0) model_code = assy_lvn[1];
		
	}
	
	//�ٴܰ� �Ǵ� �ܴܰ� ���ó���
	var step = "";
	step = document.sForm.ck.value;

	//��������
	var sel_date = document.sForm.sel_date.value;

	//������ �б��ϱ�
	parent.list.document.all['lding'].style.visibility="visible";	//ó���� �޽��� ���
	document.all['saving'].style.visibility="visible";				//�޴���ư Disable 

	//TREE ���� [��ü]
	parent.tree.document.sForm.action='../servlet/BomShowServlet';
	parent.tree.document.sForm.mode.value='frd_tree';
	parent.tree.document.sForm.gid.value=gid;
	parent.tree.document.sForm.level_no.value="0";
	parent.tree.document.sForm.parent_code.value=org_model_code;
	parent.tree.document.sForm.sel_date.value=sel_date;
	parent.tree.document.sForm.url.value="../servlet/BomShowServlet";
	parent.tree.document.sForm.submit();

	//LIST ���� [�ش�ASSY]
	parent.list.document.sForm.action='../servlet/BomShowServlet';
	parent.list.document.sForm.mode.value='frd_text';
	parent.list.document.sForm.gid.value=gid;
	parent.list.document.sForm.level_no.value=level_no;
	parent.list.document.sForm.parent_code.value=model_code;
	parent.list.document.sForm.step.value=step;
	parent.list.document.sForm.sel_date.value=sel_date;
	parent.list.document.sForm.submit();
}


//��ȿ���� ã��
function OpenCalendar(FieldName) {
	var strUrl = "../bm/Calendar.jsp?FieldName=" + FieldName;
	wopen(strUrl,"open_calnedar",'180','260','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//BOM����Ʈ
function goReport()
{
	//�𵨼�������
	var a = document.sForm.fg_code.selectedIndex; 
	if(a != -1) {
		var fg = document.sForm.fg_code.options[a].value.split("|"); 
		var gid = fg[0];
		var model_code = fg[1];
	} else { alert('FG�ڵ尡 �����ϴ�. ���� FG �ڵ带 �˻��Ͻʽÿ�.'); return; }

	//��������
	var sel_date = document.sForm.sel_date.value;

	//��������
	var bundle = "8"; 

	var para = "gid="+gid+"&parent_code="+model_code+"&level_no=0&sel_date="+sel_date+"&bundle="+bundle;
	wopen("../servlet/BomShowServlet?mode=report_n&"+para,"proxy","870","580","scrollbars=no,toolbar=no,status=no,resizable=no");
}
//��������Ʈ
function goExcel()
{
	//�𵨼�������
	var a = document.sForm.fg_code.selectedIndex; 
	if(a != -1) {
		var fg = document.sForm.fg_code.options[a].value.split("|"); 
		var gid = fg[0];
		var model_code = fg[1];
	} else { alert('FG�ڵ尡 �����ϴ�. ���� FG �ڵ带 �˻��Ͻʽÿ�.'); return; }

	//��������
	var sel_date = document.sForm.sel_date.value;

	//��������
	var bundle = "8"; 

	var para = "gid="+gid+"&parent_code="+model_code+"&level_no=0&sel_date="+sel_date+"&bundle="+bundle;
	wopen("../servlet/BomShowServlet?mode=report_excel&"+para,"proxy","870","580","scrollbars=no,toolbar=yes,status=yes,resizable=yes,menubar=yes");
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
</SCRIPT>

<LINK href="../bm/css/style.css" rel=stylesheet>
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>
<FORM name="sForm" method="post" style="margin:0">

<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=0 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD>	
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="center">
		<TBODY>
			<TR height="25"><!--��ư �� ����¡-->
					<TD vAlign="BOTTOM" align='left' width='19%' style='padding-left:5px'>
						<INPUT type='text' name='sWord' value='<%=sWord%>' size='11' onfocus="javascript:empty();">
					<A href='javascript:goSearch();'><img src='../bm/images/bt_search.gif' align='absmiddle' border=0></a></TD>
					<TD width='45%' vAlign="BOTTOM"  align='center'>
						<SELECT name="fg_code" style='font-size:9pt;color="black";' onChange='javascript:selectFG();'> 
						<OPTGROUP label='---------'>
				<%
						String sel = "";
						while(fg_iter.hasNext()) {
							fg = (mbomMasterTable)fg_iter.next(); 
							if(gid.equals(fg.getPid())) sel = "selected";
							else sel = "";
							out.print("<option "+sel+" value='"+fg.getPid()+"|"+fg.getModelCode()+"'>");
							out.println(fg.getFgCode()+"</option>");
						} 
				%>		</SELECT>
				
						<SELECT name="assy_code" style='font-size:9pt;color="black";'> 
						<OPTGROUP label='---------'>
						<OPTION value='0|0'>0: ��ü����</option>
					<%
							String asel = "";
							while(assy_iter.hasNext()) {
								assy = (mbomStrTable)assy_iter.next(); 
								if(parent_code.equals(assy.getParentCode())) asel = "selected";
								else asel = "";
								out.print("<option "+asel+" value='"+assy.getLevelNo()+"|"+assy.getParentCode()+"'>");
								out.println(assy.getLevelNo()+": "+assy.getParentCode()+"</option>");
						} 
					%>	</SELECT>
					
						<SELECT name='ck' style='font-size:9pt;color="black";'>
							<OPTION value='M'>�ٴܰ�</OPTION>
							<OPTION value='S'>�ܴܰ�</OPTION>

				<% if(step.equals("M")) {%>
						<SCRIPT language='javascript'>
							document.sForm.ck.value='M';
						</SCRIPT>
				<% } else { %>
						<SCRIPT language='javascript'>
							document.sForm.ck.value='S';
						</SCRIPT>	
						<% }%>
						</SELECT>

						<A href='javascript:goView();'><img src='../bm/images/bt_sel.gif' align='absmiddle' border=0></a>
					</TD>
					<TD width='33%' vAlign="BOTTOM"  align='right' style='padding-right:5px'>
						<IMG src='../bm/images/valid_date.gif' align='absmiddle' border='0'>
						<INPUT type='text' name='sel_date' value='<%=sel_date%>' size='9'  style='font-size:9pt;color="black";' readonly>
						<A Href="Javascript:OpenCalendar('sel_date');"><img src="../bm/images/bt_calendar.gif" border="0" align='absmiddle'></A>
					<!-- bomFrdText.jsp���� ȣ���Ͽ� ����ϵ��� ��ư�� �Ű���.
					<a href='javascript:goReport();'><img src='../bm/iamges/bt_reportp.gif' border='0' align='absmiddle' alt='����'></a>
					<a href='javascript:goExcel();'><img src='../bm/iamges/bt_excellp.gif' border='0' align='absmiddle' alt='����'></a>
					-->
						<a href="javascript:Display_tip(1, 'show', true);"  onfocus="this.blur();"><img src="../bm/images/bt_print.gif" border="0" align="absmiddle" onMouseout="Display_tip(1, 'hide', false)"></a>

					<div id="tip1" style="display:none;position:absolute;z-index:3;margin-top:-2px;margin-left:-150px" onMouseout="Display_tip(1, 'hide', false)" onMouseover="Display_tip(1, 'hide', true)">
						<TABLE border="0" cellpadding="0" cellspacing="0" width="150" style="border:#999999 solid 1px" bgcolor="#f6f6f6">
							<TR><TD valign='center' align='middle'>
								<A href="javascript:goReport();"><IMG src="../bm/iamges/bt_reportp.gif" border="0" width="3" align="absmiddle">�������</A></TD>
								<TD valign='center' align='middle'><IMG src="../bm/images/list_tep.gif" border="0" ></TD>
								<TD align='center' align='left'>
								<A href="javascript:goExcel();"><img src="../bm/iamges/bt_excellp.gif" border="0" width="3"  align="absmiddle">Excell���</A></TD></TR>
						</TABLE></DIV>			
				</TD></TR></TBODY></TABLE>
</TD></TR></TABLE>
<INPUT type='hidden' name='sItem' value='fg_code'>
<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="gid" size="15" value="">
<INPUT type="hidden" name="parent_code" size="15" value="">
<INPUT type="hidden" name="level_no" size="15" value="">
<INPUT type="hidden" name="step" size="15" value="">
</FORM>

<DIV id="saving" style="position:absolute;left:0px;top:0px;width:250px;height:15px;visibility:hidden;">
<TABLE width="700" border="0" cellspacing=1 cellpadding=1 bgcolor="">
	<TR><td height="15" align="center" valign="middle"></TD></TR>
</TABLE>
</DIV>
</BODY>
</HTML>

<SCRIPT language='javascript'>
flag=true;

function Display_tip(index , view, flag) {

	if(view == 'show'){
		if(flag) {
			if (navigator.userAgent.indexOf("MSIE") != -1) {		
				document.getElementById('Tip' + index).style.display='';
				flag = false;
			}else {
				location.href = "";
			}		
		} else {
			document.getElementById('Tip' + index).style.display='none';
			flag = true;		
		}
	} else if(view == 'hide'){
		if (flag){
			document.getElementById('Tip' + index).style.display ='';
		}else{
			document.getElementById('Tip' + index).style.display = "none";	
		}
	}
}

// �˻� field �ʱ�ȭ
function empty(){
	document.sForm.sWord.value="";
}
</SCRIPT>