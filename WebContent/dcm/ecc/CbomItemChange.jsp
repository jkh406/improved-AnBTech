<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= "BOM ǰ�񺯰� ����[����/����]"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import		= "java.util.*"
	import		= "com.anbtech.text.*"
	import		= "com.anbtech.dcm.entity.*"
	import		= "com.anbtech.bm.entity.*"
	import		= "com.anbtech.date.anbDate"
	import		= "com.anbtech.text.StringProcess"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//----------------------------------------------------
	//	parameter ����
	//----------------------------------------------------
	String pid="",chg_id="",chg_name="";
	
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();
	String todate = anbdt.getDateNoformat();				//���ó���

	//----------------------------------------------------
	// 	������ login �˾ƺ���
	//----------------------------------------------------
	chg_id = sl.id; 		//������ login id
	
	String[] idColumn = {"a.id","a.name","b.code","b.ac_code","b.ac_name","a.office_tel"};
	bean.setTable("user_table a,class_table b");			
	bean.setColumns(idColumn);
	bean.setOrder("a.id ASC");	
	String item_data = "where (a.id ='"+chg_id+"' and a.ac_id = b.ac_id)";
	bean.setSearchWrite(item_data);
	bean.init_write();

	if(bean.isAll()) {
		chg_name = bean.getData("name");			//������ ��
	} //while
	
	//----------------------------------------------------
	//	�Ķ���� �б�
	//----------------------------------------------------
	String fg_code = (String)request.getAttribute("fg_code");			//FG code
	String eco_no = (String)request.getAttribute("eco_no");				//���Ժ��� ��ȣ
	String gid = (String)request.getAttribute("gid");					//�����ڵ�
	String adtag = "";

	//----------------------------------------------------
	//	���躯�� �����׸�[F01:��������] ������ �б�
	//----------------------------------------------------
	com.anbtech.bm.entity.mbomEnvTable ecr;
	ArrayList ecr_list = new ArrayList();
	ecr_list = (ArrayList)request.getAttribute("ECR_List");
	ecr = new mbomEnvTable();
	Iterator ecr_iter = ecr_list.iterator();

	//----------------------------------------------------
	//	������ �ش�ǰ�� ���� �б�
	//----------------------------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ITEM_List");
	com.anbtech.dcm.entity.eccBomTable table;
	table = new eccBomTable();
	Iterator table_iter = table_list.iterator();

	String[][] ecc_bom = new String[2][10];
	for(int i=0; i<2; i++) for(int j=0; j<10; j++) ecc_bom[i][j] = "";

	int n=0;
	while(table_iter.hasNext()){
		table = (eccBomTable)table_iter.next();
		ecc_bom[n][0] = table.getPid();
		ecc_bom[n][1] = table.getGid();
		ecc_bom[n][2] = table.getParentCode();
		ecc_bom[n][3] = table.getChildCode();
		ecc_bom[n][4] = table.getPartSpec();
		ecc_bom[n][5] = table.getLocation();
		ecc_bom[n][6] = table.getOpCode();
		ecc_bom[n][7] = table.getAdTag();
		ecc_bom[n][8] = table.getEccReason();
		ecc_bom[n][9] = table.getNote();
		n++;
	}
	adtag = ecc_bom[0][7];

	//adtag='A'�̸� �迭0 ->�迭1�� �����͸� �ű��.
	if(ecc_bom[0][7].equals("A")) {
		for(int k=0; k<10; k++) ecc_bom[1][k] = ecc_bom[0][k];
		for(int k=0; k<10; k++) ecc_bom[0][k] = "";
		adtag = ecc_bom[1][7];
	}
	
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../dcm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' onload='display();' oncontextmenu='return false'>
<FORM name="eForm" method="post" style="margin:0">
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
<TR><TD>
	<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
		<TBODY>
			<TR height='25'>
				<TD vAlign=center bgcolor='#DBE7FD' class='bg_05' style="padding-left:5px"  BACKGROUND='../bm/images/title_bm_bg.gif'> ����BOM �����۾�</TD></TR>
			</TD></TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=21></TD></TR>
			<TR height=25>
				<TD align=left width='80%' style="padding-left:5px">
					<a href="javascript:undoItem();"><img src='../dcm/images/bt_undo.gif' alt='UNDO' align='absmiddle' border='0'></a>
				<% if(!ecc_bom[0][7].equals("D")) { %>
					<a href="javascript:changeItem();"><img src="../dcm/images/bt_modify.gif" alt='����' align='absmiddle' border='0'></a>
					<a href="javascript:searchItems();"><img src='../dcm/images/bt_item_chg.gif' alt='��ǰ�˻�' align='absmiddle' border='0'></a>
				<% } %>
					<a href="javascript:newAddItem();"><img src="../dcm/images/bt_add_new2.gif" align='absmiddle' border='0'></a>
							</TD></TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=21></TD></TR>
	<!--����Ʈ-->
			<TR height=100%>
				<TD vAlign=top>
				<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">	
				<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
					<TBODY>
						<tr>
							<td width='100' height="25" class='list_title'></td>
							<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
							<td width='100' height="25" class='list_title' align='middle'>��ǰ���ڵ�</td>
							<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
							<td width='100' height="25" class='list_title' align='middle'>��ǰ���ڵ�</td>
							<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
							<td width='400' height="25" class='list_title' align='middle'>ǰ��԰�</td>
							<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
							<td width='60' height="25" class='list_title' align='middle'>LOC</td>
							<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
							<td width='60' height="25" class='list_title' align='middle'>OPC</td>
							<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
							<td width='200' height="25" class='list_title' align='middle'>��������</td>
							<TD noWrap width=6 class='list_title'><IMG src="../bm/images/list_tep.gif"></TD>
							<td width='200' height="25" class='list_title' align='middle'>��Ÿ�ǰ�</td>
						<TR bgColor=#9DA9B9 height=1><TD colspan=15></TD></TR>
						<tr>
							<td width='100' height="25" class='list_title' align='middle'><%=ecc_bom[0][7]%></td>
							<TD noWrap width=1 class='list_title'></TD>
							<td width="100" height="25" class="bg_04" align='middle'><%=ecc_bom[0][2]%></td>
							<TD><IMG height=1 width=1></TD>
							<td width="100" height="25" class="bg_04" align='middle'><%=ecc_bom[0][3]%></td>
							<TD><IMG height=1 width=1></TD>
							<td width="400" height="25" class="bg_04" align='middle'><%=ecc_bom[0][4]%></td>
						    <TD><IMG height=1 width=1></TD>
							<td width="60" height="25" class="bg_04" align='middle'><%=ecc_bom[0][5]%></td>
						    <TD><IMG height=1 width=1></TD>
							<td width="60" height="25" class="bg_04" align='middle'><%=ecc_bom[0][6]%></td>
						    <TD><IMG height=1 width=1></TD>
							<td width="200" height="25" class="bg_04" align='middle'></td>
						    <TD><IMG height=1 width=1></TD>
							<td width="200" height="25" class="bg_04" align='middle'></td>
						</tr>
						<tr bgcolor="c7c7c7"><td height=1 colspan="15"></td></tr>
						<tr>
							<td width='100' height="25" class='list_title' align='middle'><%=ecc_bom[1][7]%></td>
							<TD noWrap width=1 class='list_title'></TD>
							<td width="100" height="25" class="bg_04" align='middle'><%=ecc_bom[1][2]%></td>
						    <TD><IMG height=1 width=1></TD>
							<td width="100" height="25" class="bg_04" align='middle'>
								<INPUT type='text' name='child_code' value='<%=ecc_bom[1][3]%>' size='10' class="text_01"></td>
							<TD><IMG height=1 width=1></TD>
							<td width="400" height="25" class="bg_04" align='middle'>
								<INPUT type='text' name='part_spec' value='<%=ecc_bom[1][4]%>' size='30'></td>
							<TD><IMG height=1 width=1></TD>
							<td width="60" height="25" class="bg_04" align='middle'>
								<INPUT type='text' name='location' value='<%=ecc_bom[1][5]%>' size='5'></td>
							<TD><IMG height=1 width=1></TD>
							<td width="60" height="25" class="bg_04" align='middle'>
								<INPUT type='text' name='op_code' value='<%=ecc_bom[1][6]%>' size='5'></td>
							<TD><IMG height=1 width=1></TD>
							<td width="200" height="25" class="bg_04" align='middle'>
				
								<select name="ecc_reason"> 
								<OPTGROUP label='---------------'>
					<%
							String rsel="";
							while(ecr_iter.hasNext()) {
								ecr = (mbomEnvTable)ecr_iter.next(); 
								if(ecc_bom[1][8].equals(ecr.getSpec())) rsel = "selected";
								else rsel = "";
								out.print("<option "+rsel+" value='"+ecr.getSpec()+"'>");
								out.println(ecr.getSpec()+"</option>");
							} 
					%></select></td>
							<TD><IMG height=1 width=1></TD>
							<td width="200" height="25" class="bg_04" align='middle'>
								<INPUT type='text' NAME="note" size='20' value='<%=ecc_bom[1][9]%>'></td>
						</tr> 
						<TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR>
		</TBODY></TABLE></DIV></TD></TR>
	</TBODY></TABLE>
</TD></TR></TABLE>

<INPUT type='hidden' name='mode' value=''>
<INPUT type='hidden' name='cpid' value='<%=ecc_bom[0][0]%>'>			<% //��ϵ� PID %>
<INPUT type='hidden' name='pid' value='<%=ecc_bom[1][0]%>'>				<% //����� PID %>
<INPUT type='hidden' name='gid' value='<%=gid%>'>
<INPUT type='hidden' name='eco_no' value='<%=eco_no%>'>
<INPUT type='hidden' name='fg_code' value='<%=fg_code%>'>
<INPUT type='hidden' name='adtag' value='<%=adtag%>'>
<INPUT type='hidden' name='chg_id' value='<%=chg_id%>'>
<INPUT type='hidden' name='chg_name' value='<%=chg_name%>'>
</FORM>
</BODY>
</HTML>

<script language=javascript>
<!--
//�����ϱ�
function changeItem()
{
	var f = document.eForm;

	var child_code = f.child_code.value;
	if(child_code == '') { alert('�ű� ��ǰ���ڵ尡 �Էµ��� �ʾҽ��ϴ�.'); f.child_code.focus(); return; }

	var ecc_reason = f.ecc_reason.value;
	if(ecc_reason == '') { alert('���������� �Էµ��� �ʾҽ��ϴ�.'); f.ecc_reason.focus(); return; }

	document.eForm.action='../servlet/CbomChangeServlet';
	document.eForm.mode.value='item_change';
	document.eForm.submit();
}
//���泻�� ����ϱ� UNDO
function undoItem()
{
	document.eForm.action='../servlet/CbomChangeServlet';
	document.eForm.mode.value='item_undo';
	document.eForm.submit();
}
//�߻���ǰ ã��
function searchItems()
{
	var strUrl = "../cm/openItemInfoWindow.jsp?item_code=child_code&item_name=part_name&item_type=item_type&item_desc=part_spec&item_unit=qty_unit";
	wopen(strUrl,"open_part",'820','425','scrollbars=no,toolbar=no,status=no,resizable=no');

}
//�űԵ�� �޴��� ���ư���
function newAddItem()
{
	document.eForm.action='../servlet/CbomChangeServlet';
	document.eForm.mode.value='eco_prechg';
	document.eForm.pid.value='';
	document.eForm.submit();
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
	var div_h = h - 670;
	item_list.style.height = div_h;
}
-->
</script>