<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= "���� ������ ����å����/����� ���躯�� ���뺸��"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import		= "java.io.*"
	import		= "java.util.*"
	import		= "com.anbtech.text.*"
	import		= "com.anbtech.dcm.entity.*"
	import		= "com.anbtech.date.anbDate"
%>
<%
	//----------------------------------------------------
	//	�ʱ�ȭ��
	//----------------------------------------------------
	com.anbtech.date.anbDate anbdt = new anbDate();
	String ecc_status = "";

	//ECR���� �б�
	String fname="",sname="",ftype="",fsize = "";						
	String chg_position="",trouble="",condition="",solution="";

	//ECO���� �б�
	String eco_fname="",eco_sname="",eco_ftype="",eco_fsize = "";						
	String eco_chg_position="",eco_trouble="",eco_condition="",eco_solution="";

	String v_list = (String)request.getAttribute("v_list"); if(v_list == null) v_list = "ecc_rilist";
	
	//----------------------------------------------------
	//	ECC COM �б�
	//----------------------------------------------------
	com.anbtech.dcm.entity.eccComTable ecc;
	ecc = (eccComTable)request.getAttribute("COM_List");
	ecc_status = ecc.getEccStatus();						//������°˻�

	//----------------------------------------------------
	//	ECC REQ �б�
	//----------------------------------------------------
	com.anbtech.dcm.entity.eccReqTable ecr;
	ecr = (eccReqTable)request.getAttribute("REQ_List");

	//----------------------------------------------------
	//	ECC ORD �б�
	//----------------------------------------------------
	com.anbtech.dcm.entity.eccOrdTable eco;
	eco = (eccOrdTable)request.getAttribute("ORD_List");

	//----------------------------------------------------
	//	÷������ �б�
	//----------------------------------------------------
	//ECR�� ������ ������
	fname = ecr.getFname();							//÷�����ϸ�
	sname = ecr.getSname();							//÷������ �����
	ftype = ecr.getFtype();							//÷������Type
	fsize = ecr.getFsize();							//÷������Size
	chg_position = ecr.getChgPosition();
	trouble = ecr.getTrouble();
	condition = ecr.getCondition();
	solution = ecr.getSolution();
	String bon_path = "/dcm/"+ecc.getEcrId();		//÷������ ���� Ȯ��path
	 
	//ECR ÷������ ������ �б�
	int cnt = 0;
	for(int i=0; i<fname.length(); i++) if(fname.charAt(i) == '|') cnt++;

	String[][] addFile = new String[cnt][5];
	for(int i=0; i<cnt; i++) for(int j=0; j<5; j++) addFile[i][j]="";

	if(fname.length() != 0) {
		StringTokenizer f = new StringTokenizer(fname,"|");		//���ϸ� ���
		int m = 0;
		while(f.hasMoreTokens()) {
			addFile[m][0] = f.nextToken();
			addFile[m][0] = addFile[m][0].trim(); 
			if(addFile[m][0] == null) addFile[m][0] = "";
			m++;
		}
		StringTokenizer t = new StringTokenizer(ftype,"|");		//����type ���
		m = 0;
		while(t.hasMoreTokens()) {
			addFile[m][1] = t.nextToken();
			addFile[m][1] = addFile[m][1].trim();
			if(addFile[m][1] == null) addFile[m][1] = "";
			m++;
		}
		StringTokenizer s = new StringTokenizer(fsize,"|");		//����ũ�� ���
		m = 0;
		while(s.hasMoreTokens()) {
			addFile[m][2] = s.nextToken();
			addFile[m][2] = addFile[m][2].trim();
			if(addFile[m][2] == null) addFile[m][2] = "";
			m++;
		}
		StringTokenizer o = new StringTokenizer(sname,"|");		//�������� ���
		m = 0;
		int no = 1;
		while(o.hasMoreTokens()) {
			addFile[m][3] = o.nextToken();	
			addFile[m][4] = addFile[m][3];
			addFile[m][3] = addFile[m][3].trim() + ".bin";			//�������ϸ�
			addFile[m][4] = addFile[m][4].trim();					//TEMP �������ϸ�
			if(addFile[m][3] == null) addFile[m][3] = "";
			m++;
			no++;
		}
	}

	//ECO�� ������ ������
	eco_fname = eco.getFname();						//÷�����ϸ�
	eco_sname = eco.getSname();						//÷������ �����
	eco_ftype = eco.getFtype();						//÷������Type
	eco_fsize = eco.getFsize();						//÷������Size
	eco_chg_position = eco.getChgPosition();
	eco_trouble = eco.getTrouble();
	eco_condition = eco.getCondition();
	eco_solution = eco.getSolution();
	String eco_bon_path = "/dcm/"+ecc.getEcoId();	//÷������ ���� Ȯ��path

	//ECO ÷������ ������ �б�
	int eco_cnt = 0;
	for(int i=0; i<eco_fname.length(); i++) if(eco_fname.charAt(i) == '|') eco_cnt++;

	String[][] eco_addFile = new String[eco_cnt][5];
	for(int i=0; i<eco_cnt; i++) for(int j=0; j<5; j++) eco_addFile[i][j]="";

	if(eco_fname.length() != 0) {
		StringTokenizer eco_f = new StringTokenizer(eco_fname,"|");		//���ϸ� ���
		int eco_m = 0;
		while(eco_f.hasMoreTokens()) {
			eco_addFile[eco_m][0] = eco_f.nextToken();
			eco_addFile[eco_m][0] = eco_addFile[eco_m][0].trim(); 
			if(eco_addFile[eco_m][0] == null) eco_addFile[eco_m][0] = "";
			eco_m++;
		}
		StringTokenizer eco_t = new StringTokenizer(eco_ftype,"|");		//����type ���
		eco_m = 0;
		while(eco_t.hasMoreTokens()) {
			eco_addFile[eco_m][1] = eco_t.nextToken();
			eco_addFile[eco_m][1] = eco_addFile[eco_m][1].trim();
			if(eco_addFile[eco_m][1] == null) eco_addFile[eco_m][1] = "";
			eco_m++;
		}
		StringTokenizer eco_s = new StringTokenizer(eco_fsize,"|");		//����ũ�� ���
		eco_m = 0;
		while(eco_s.hasMoreTokens()) {
			eco_addFile[eco_m][2] = eco_s.nextToken();
			eco_addFile[eco_m][2] = eco_addFile[eco_m][2].trim();
			if(eco_addFile[eco_m][2] == null) eco_addFile[eco_m][2] = "";
			eco_m++;
		}
		StringTokenizer eco_o = new StringTokenizer(eco_sname,"|");		//�������� ���
		eco_m = 0;
		int eco_no = 1;
		while(eco_o.hasMoreTokens()) {
			eco_addFile[eco_m][3] = eco_o.nextToken();	
			eco_addFile[eco_m][4] = eco_addFile[eco_m][3];
			eco_addFile[eco_m][3] = eco_addFile[eco_m][3].trim() + ".bin";			//�������ϸ�
			eco_addFile[eco_m][4] = eco_addFile[eco_m][4].trim();					//TEMP �������ϸ�
			if(eco_addFile[eco_m][3] == null) eco_addFile[eco_m][3] = "";
			eco_m++;
			eco_no++;
		}
	}

%>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../dcm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<form name="sForm" method="post" style="margin:0" encType="multipart/form-data">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--Ÿ��Ʋ-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR  bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR  bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../dcm/images/blet.gif"> ���躯������</TD>
				</TR>
				</TBODY></TABLE></TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=30><!--��ư-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px;'>
					<a href="javascript:goLIST();"><img src="../dcm/images/bt_list.gif" border=0 align='absmiddle'></a>
					<% if(ecc_status.equals("3")) {		//������� å���� �϶� %> 
						<a href="javascript:changeMgr();"><img src="../dcm/images/bt_chg_mgr.gif" border=0 alt='å���ں���' align='absmiddle'></a>
						<a href="javascript:setUser();"><img src="../dcm/images/bt_set_user.gif" border=0 alt='���������' align='absmiddle'></a>
						<a href="javascript:rejectECC();"><img src="../dcm/images/bt_reject_ecc.gif" border=0 alt='����ݷ�' align='absmiddle'></a>
					<% } else if(ecc_status.equals("4")) { //������� ����� �϶� %> 
						<a href="javascript:writeECO();"><img src="../dcm/images/bt_write_eco.gif" border=0 alt='ECO�ۼ�' align='absmiddle'></a>
						<a href="javascript:rejectECC();"><img src="../dcm/images/bt_reject_ecc.gif" border=0 alt='����ݷ�' align='absmiddle'></a>
					<% } else if(ecc_status.equals("0") || ecc_status.equals("5")) { //�ݷ��϶� %> 
						<a href="javascript:modifyECC();"><img src="../dcm/images/bt_save.gif" border=0 alt='���躯�����' align='absmiddle'></a>
					<% } %>
					</TD>
				</TR></TBODY></TABLE></TD>
	</TR>
</TABLE>

<!--����-->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR>
		<TD align="center">
		<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
			<TBODY>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
				<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">ECO NO</td>
				<TD width="37%" height="25" class="bg_04" ><%=ecc.getEcoNo()%></td>
				<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�� ��</td>
				<TD width="37%" height="25" class="bg_04" ><%=ecc.getEccSubject()%></td>			  
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�������</td>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEccReason()%></td>
				<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">���뱸��</td>
			    <TD width="37%" height="25" class="bg_04"><%=ecc.getEccFactor()%></td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�������</td>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEccScope()%></td>
				<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">��������</td>
			    <TD width="37%" height="25" class="bg_04"><%=ecc.getEccKind()%></td>
			</TR>
			<!--
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">��ǰ��</td>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getPdgCode()%></td>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">��ǰ</td>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getPdCode()%></td>
			</TR>-->
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����߻���(FG)</td>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT NAME="fg_code" rows='4' value='<%=ecc.getFgCode()%>' size='12' readonly></td>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">������</td>
			   <TD width="87%" height="25" class="bg_04" colspan='3'><%=anbdt.getSepDate(ecc.getOrderDate(),"-")%></td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			    <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����߻���ǰ</td>
			    <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="part_code" rows='4' cols='30' readonly><%=ecc.getPartCode()%></TEXTAREA>
					<a href="Javascript:searchItems();"><img src="../dcm/images/bt_search.gif" border="0" align="top"></a></td>
			</TR>
			<!-- ECO �ۼ� -->
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR><TD height="25" colspan="4"><img src='../dcm/images/title_pre_check.gif' align='absmiddle' alt='��������� ����'></td></TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����з�</td>
			   <TD width="37%" height="25" class="bg_04"><%=trouble%></td>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����߻��κ�</td>
			   <TD width="37%" height="25" class="bg_04"><%=chg_position%></td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">����׿���</td>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="condition" rows='4' cols='38' readonly><%=condition%></TEXTAREA></td>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">ECO���볻��</td>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="condition" rows='4' cols='38' readonly><%=solution%></TEXTAREA></td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">÷������</td>
			   <TD width="37%" height="25" class="bg_04" colspan='3'>
				<% 
				for(int i=0; i<cnt; i++) {
					out.print("<a href='../dcm/attach_download.jsp?fname="+addFile[i][0]);
					out.print("&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]);
					out.print("&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a>");
					out.println("<br>"); 
				}
				%>		   
			   </td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR><TD height="25" colspan="4"><img src='../dcm/images/title_after_check.gif' align='absmiddle' alt='��������� ����'></td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����з�</td>
			   <TD width="37%" height="25" class="bg_04"><%=eco_trouble%></td>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����߻��κ�</td>
			   <TD width="37%" height="25" class="bg_04"><%=eco_chg_position%></td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">����׿���</td>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="solution" rows='4' cols='38' readonly><%=eco_condition%></TEXTAREA></td>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">ECO���볻��</td>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="solution" rows='4' cols='38' readonly><%=eco_solution%></TEXTAREA></td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">÷������</td>
			   <TD width="37%" height="25" class="bg_04" colspan='3'>
				<% 
				for(int i=0; i<eco_cnt; i++) {
					out.print("<a href='../dcm/attach_download.jsp?fname="+eco_addFile[i][0]);
					out.print("&ftype="+eco_addFile[i][1]+"&fsize="+eco_addFile[i][2]);
					out.print("&sname="+eco_addFile[i][3]+"&extend="+eco_bon_path+"'>"+eco_addFile[i][0]+"</a>");
					out.println("<br>"); 
				}
				%>		   
			   </td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR><TD height='5'></TD></TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�ۼ���</td>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEcrName()%></td>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�μ���</td>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEcrDivName()%></td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">��ȭ��ȣ</td>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEcrTel()%></td>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�ۼ���</td>
			   <TD width="37%" height="25" class="bg_04"><%=anbdt.getSepDate(ecc.getEcrDate(),"-")%></td>
			</TR> 
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">����å����</td>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getMgrName()%></td>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">��������</td>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEcoName()%>
			   <% if(ecc_status.equals("3")) {		//������� å���� �϶� %> 
					<INPUT type="text" name="eco_name" value="" size="20" class="text_01" readonly><a href="Javascript:checkUser();"><img src="../dcm/images/bt_search.gif" border="0" align="absmiddle"></a>
			   <% } %>
			   </td>
			</TR></TBODY></TABLE>
		</TD>
	</TR>
</TABLE>

<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="pid" size="15" value="<%=ecc.getPid()%>">
<INPUT type="hidden" name="v_list" size="15" value="<%=v_list%>"> 
<INPUT type="hidden" name="eco_id" size="15" value="">
<INPUT type="hidden" name="mgr_id" size="15" value="<%=ecc.getMgrId()%>">
<INPUT type="hidden" name="mgr_name" size="15" value="<%=ecc.getMgrName()%>">
<INPUT type="hidden" name="note" size="15" value="">
</FORM>
</BODY>
</HTML>

<script language=javascript>
<!--
//��Ϻ���
function goLIST()
{
	var v_list = document.sForm.v_list.value;
	document.sForm.action='../servlet/CbomProcessServlet';
	document.sForm.mode.value=v_list;
	document.sForm.submit();
}
//å����:å���ں���
function changeMgr()
{
	var para = "pid=<%=ecc.getPid()%>&ecr_id=<%=ecc.getEcrId()%>&ecr_name=<%=ecc.getEcrName()%>";	wopen("../dcm/ecc/mgrChange.jsp?"+para,"mgrChange","500","220","scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//å����:����� ����
function setUser()
{
	var f = document.sForm;
	var eco_name = f.eco_name.value;
	if(eco_name == '') { alert('����������ڰ� �Էµ��� �ʾҽ��ϴ�.'); f.eco_name.focus(); return; }
	
	var note = "������� ����ڰ� �����Ǿ����ϴ�.";

	document.sForm.action='../servlet/CbomProcessServlet';
	document.sForm.mode.value='eco_assign';
	document.sForm.note.value=note;
	document.sForm.submit();
}
//�����:ECO�ۼ�
function writeECO()
{
	document.sForm.action='../servlet/CbomProcessServlet';
	document.sForm.mode.value='ecc_premodify';
	document.sForm.submit();
}
//�ݷ��� ���躯�� �����ϱ�
function modifyECC()
{
	if(confirm("���躯�� ����ó���� �Ͻðڽ��ϱ�?")) {
		document.sForm.action='../servlet/CbomProcessServlet';
		document.sForm.mode.value='ecc_premodify';
		document.sForm.submit();
	} else {
		return;
	}
}
//����ݷ�
function rejectECC()
{
	if(confirm("����ݷ� ó���� �Ͻðڽ��ϱ�?")) {
		para = "pid=<%=ecc.getPid()%>&ecr_id=<%=ecc.getEcrId()%>&ecr_name=<%=ecc.getEcrName()%>";
		para += "&mgr_id=<%=ecc.getMgrId()%>&mgr_name=<%=ecc.getMgrName()%>";	wopen("../dcm/ecc/mgrEccReject.jsp?"+para,"mgrChange","450","190","scrollbar=yes,toolbar=no,status=no,resizable=no");
	} else {
		return;
	}
}
//��������
function checkUser()
{
	wopen("../dcm/searchName.jsp?target=sForm.eco_id/sForm.eco_name","proxy","250","380","scrollbar=yes,toolbar=no,status=no,resizable=no");

}
//â���� ����
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}

//�����߻���ǰ ã��
function searchItems()
{
	//��ǥ FG�ڵ常 �����Ѵ�:ù��°�� ��ǥFG�� ����
	var fg_list = document.sForm.fg_code.value;
	var fgc = fg_list.split('\n');
	var fg_code = fgc[0];
	if(fg_code.length == 0) { alert('���� �߻���(FG)�� ���� �Է��Ͻʽÿ�.'); return; }
	wopen("../servlet/CbomBaseInfoServlet?mode=pl_list&fg_code="+fg_code,"proxy","700","400","scrollbars=auto,toolbar=no,status=no,resizable=yes");

}
-->
</script>