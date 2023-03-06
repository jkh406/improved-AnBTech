<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= "ECO AUDIT ���뺸��"		
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
	//ECR���� �б�
	String fname="",sname="",ftype="",fsize = "";						
	String chg_position="",trouble="",condition="",solution="";

	//ECO���� �б�
	String eco_fname="",eco_sname="",eco_ftype="",eco_fsize = "";						
	String eco_chg_position="",eco_trouble="",eco_condition="",eco_solution="";

	String gid = (String)request.getAttribute("gid");
	String msg = (String)request.getAttribute("msg");		//������ �ִ��� �Ǵ�
	//----------------------------------------------------
	//	ECC COM �б�
	//----------------------------------------------------
	com.anbtech.dcm.entity.eccComTable ecc;
	ecc = (eccComTable)request.getAttribute("COM_List");

	//----------------------------------------------------
	//	ECC MODEL �б�
	//----------------------------------------------------
	ArrayList model_list = new ArrayList();
	model_list = (ArrayList)request.getAttribute("MODEL_List");
	com.anbtech.dcm.entity.eccModelTable model = new com.anbtech.dcm.entity.eccModelTable();
	Iterator model_iter = model_list.iterator();

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
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../dcm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//�����Ǵ�
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
	history.back(-1);
}
//��Ϻ���
function goLIST()
{
	document.sForm.action='../servlet/CbomProcessServlet';
	document.sForm.mode.value='audit_list';
	document.sForm.submit();
}
//Audit ����
function auditApp()
{
	var c = confirm('ECO�� �����Ͻðڽ��ϱ�?');
	if(c == false) return;

	var receivers = document.sForm.receivers.value;
	if(receivers == '') { alert('���������ڰ� �Էµ��� �ʾҽ��ϴ�.'); 
		document.sForm.receivers.focus(); return; }

	document.sForm.action='../servlet/CbomProcessServlet';
	document.sForm.mode.value='audit_app';
	document.sForm.submit();
}
//Audit �ݷ�
function auditRej()
{
	var c = confirm('ECO�� �ݷ��Ͻðڽ��ϱ�?');
	if(c == false) return;

	document.sForm.action='../servlet/CbomProcessServlet';
	document.sForm.mode.value='audit_rej';
	document.sForm.submit();
}
//���������� ã��
function searchProxy()
{
	var list = document.sForm.receivers.value; 
	wopen("../dcm/searchFellows.jsp?target=sForm.receivers|"+list,"proxy","520","467","scrollbars=no,toolbar=no,status=no,resizable=no");

}
//�����ǰ ��
function compareItem()
{
	var eco_no = document.sForm.eco_no.value;
	var para = "eco_no="+eco_no;
	wopen("../servlet/CbomChangeServlet?mode=eco_itemcomp&"+para,"proxy","650","400","scrollbars=yes,toolbar=no,status=no,resizable=yes");
}
//BOM���泻����
function viewBom()
{
	var gid = document.sForm.gid.value;
	var eco_no = document.sForm.eco_no.value;
	var para = "gid="+gid+"&eco_no="+eco_no;
	wopen("../servlet/CbomChangeServlet?mode=eco_changebom&"+para,"proxy","870","600","scrollbars=yes,toolbar=no,status=no,resizable=no");
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
</script>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<form name="sForm" method="post" style="margin:0" encType="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--Ÿ��Ʋ-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../dcm/images/blet.gif"> ECO ����ó��</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=30><!--��ư-->
			<TABLE width='100%' cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='50%' style='padding-left:5px;'>
					<a href="javascript:goLIST();"><img src="../dcm/images/bt_list.gif" align='middle' border=0></a>
					<a href="javascript:compareItem();"><img src='../dcm/images/bt_chgitem_chk.gif' border='0' align='absmiddle' alt='�����ǰ��'></a>
					<a href="javascript:auditApp();"><img src='../dcm/images/bt_confirm_ecc.gif' border='0' align='absmiddle' alt='�������'></a>
					<a href="javascript:auditRej();"><img src='../dcm/images/bt_reject_ecc.gif' border='0' align='absmiddle' alt='����ݷ�'></a>
					</TD>
					<TD align=left width='50%'>
					<!--<a href="javascript:searchProxy();">[����ó����]</a>-->
					<!--<a href="javascript:compareItem();">[�����ǰ��]</a>-->
					</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>
<!--����-->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR>
		<TD align="center">
		<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
			<TBODY>
			<!-- ����ó ���� -->
			<!--
			<TR>
				<TD width="13%" height="25" class="bg_03">��������</TD>
			</TR>-->
			<TR><TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">����������</TD>
			    <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="receivers" rows='3' cols='25' readonly></TEXTAREA>
					<a href="javascript:searchProxy();"><img src='../dcm/images/bt_proxy_div.gif' border='0' align='top' alt='����ó'></a>
					</TD>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����ǰ�</TD>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="note" rows='3' cols='35'></TEXTAREA></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD height='5'></TD></TR>
			<!-- �������� �ۼ� -->
			<!--
			<TR>
				<TD height="25" colspan="4"><img src="../dcm/images/basic_info.gif" width="209" height="25" border="0"></TD>
			</TR>-->
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
				<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">ECO NO</TD>
			    <TD width="37%" height="25" class="bg_04" ><%=ecc.getEcoNo()%></TD>
			    <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�� ��</TD>
			    <TD width="37%" height="25" class="bg_04" ><%=ecc.getEccSubject()%></TD>
			</TR>
			<!-- ������ �ۼ� -->
		<!--<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD height="25" colspan="4"><img src="../dcm/images/detailed_info.gif" width="209" height="25" border="0"></TD></TR>
			-->
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�������</TD>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEccReason()%></TD>
				<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">���뱸��</TD>
			    <TD width="37%" height="25" class="bg_04"><%=ecc.getEccFactor()%></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�������</TD>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEccScope()%></TD>
				<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">��������</TD>
			    <TD width="37%" height="25" class="bg_04"><%=ecc.getEccKind()%></TD>
			</TR>
			<!--
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">��ǰ��</TD>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getPdgCode()%></TD>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">��ǰ</TD>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getPdCode()%></TD>
			</TR>-->
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����߻���(FG)</TD>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="fg_code" rows='3' cols='35' readonly><%=ecc.getFgCode()%></TEXTAREA></TD>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����߻���ǰ</TD>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="part_code" rows='3' cols='35' readonly><%=ecc.getPartCode()%></TEXTAREA></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����(FG)</TD>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="fg_code" rows='3' cols='35' readonly><% while(model_iter.hasNext()){
						model = (eccModelTable)model_iter.next();
						out.println(model.getFgCode()+" ["+model.getModelCode()+": "+model.getModelName()+"]");
					} %></TEXTAREA></TD>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">������</TD>
			   <TD width="37%" height="25" class="bg_04" colspan='3'><%=anbdt.getSepDate(ecc.getOrderDate(),"-")%></TD>
			</TR>  
			
			<!-- ECO �ۼ� -->
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
				<TD height="25" colspan="4"><img src='../dcm/images/title_pre_check.gif' align='absmiddle' alt='��������� ����'></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����з�</TD>
			   <TD width="37%" height="25" class="bg_04"><%=trouble%></TD>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����߻��κ�</TD>
			   <TD width="37%" height="25" class="bg_04"><%=chg_position%></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">����׿���</TD>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="condition" rows='4' cols='35' readonly><%=condition%></TEXTAREA></TD>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">ECO���볻��</TD>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="condition" rows='4' cols='35' readonly><%=solution%></TEXTAREA></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">÷������</TD>
			   <TD width="87%" height="25" class="bg_04" colspan='3'>
				<% 
				for(int i=0; i<cnt; i++) {
					out.print("<a href='../dcm/attach_download.jsp?fname="+addFile[i][0]);
					out.print("&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]);
					out.print("&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a>");
					out.println("<br>"); 
				}
				%>		   
			   </TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
				<TD height="25" colspan="4"><img src='../dcm/images/title_after_check.gif' align='absmiddle' alt='��������� ����'></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>			   
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����з�</TD>
			   <TD width="37%" height="25" class="bg_04"><%=eco_trouble%></TD>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�����߻��κ�</TD>
			   <TD width="37%" height="25" class="bg_04"><%=eco_chg_position%></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">����׿���</TD>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="solution" rows='4' cols='35' readonly><%=eco_condition%></TEXTAREA></TD>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">ECO���볻��</TD>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="solution" rows='4' cols='35' readonly><%=eco_solution%></TEXTAREA></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">÷������</TD>
			   <TD width="87%" height="25" class="bg_04" colspan='3'>
				<% 
				for(int i=0; i<eco_cnt; i++) {
					out.print("<a href='../dcm/attach_download.jsp?fname="+eco_addFile[i][0]);
					out.print("&ftype="+eco_addFile[i][1]+"&fsize="+eco_addFile[i][2]);
					out.print("&sname="+eco_addFile[i][3]+"&extend="+eco_bon_path+"'>"+eco_addFile[i][0]+"</a>");
					out.println("<br>"); 
				}
				%>		   
			   </TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">���泻����</TD>
			   <TD width="37%" height="25" class="bg_04" colspan=3><a href='javascript:viewBom()'><img src='../dcm/images/bt_viewbom.gif' border='0' align='absmiddle' alt='BOM ���泻��������'></a></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD height='5'></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�ۼ���</TD>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEcrName()%></TD>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�μ���</TD>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEcrDivName()%></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">��ȭ��ȣ</TD>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEcrTel()%></TD>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">�ۼ���</TD>
			   <TD width="37%" height="25" class="bg_04"><%=anbdt.getSepDate(ecc.getEcrDate(),"-")%></TD>
			</TR> 
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">����å����</TD>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getMgrName()%></TD>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">��������</TD>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEcoName()%></TD>
			</TR> 
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
</TABLE>

<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="pid" size="15" value="<%=ecc.getPid()%>">
<INPUT type="hidden" name="ecc_subject" size="15" value="���躯��: <%=ecc.getEccSubject()%>">
<INPUT type="hidden" name="eco_no" size="15" value="<%=ecc.getEcoNo()%>">
<INPUT type="hidden" name="gid" size="15" value="<%=gid%>">
<INPUT type="hidden" name="user_id" size="15" value="<%=sl.id%>">
<INPUT type="hidden" name="user_name" size="15" value="<%=sl.name%>">
<INPUT type="hidden" name="order_date" size="15" value="<%=ecc.getOrderDate()%>">
<INPUT type="hidden" name="server_url" size="15" value="<%=server_path%>">
</FORM>
</BODY>
</HTML>
