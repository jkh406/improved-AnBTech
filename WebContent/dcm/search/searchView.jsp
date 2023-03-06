<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "ECO ���뺸��"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.dcm.entity.*"
	import="com.anbtech.date.anbDate"
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
<HEAD><title>ECO����</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../dcm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY bgColor=#ffffff  topmargin="5" leftmargin="5" marginheight="0" marginwidth="0">
<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
	<TR>
		<TD width='30%' height="50" align="left" valign="bottom"><img src="../dms/images/logo.jpg" border="0"></TD>
		<TD width='30%' align="middle" class="title2">ECO ������</TD>
		<TD width='30%' align="right" valign="bottom">
			<div id="print" style="position:relative;visibility:visible;">
				<a href='Javascript:winprint();'><img src="../dms/images/bt_print.gif" border="0"></a>
				<a href='Javascript:self.close();'><img src="../dms/images/bt_close.gif" border="0"></a></div></TD></TR>
	<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
	<TR><TD height='10' colspan="3"></TD></TR></TABLE>

<!-- ���� ���� ���� -->
	<TABLE cellSpacing=0 cellPadding=0 width="620" border=0>
	  <TR><td>
		
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
			<!--	<TR><TD height="25" colspan='4'><img src='../dcm/images/title_eco_info.gif' align='absmiddle' alt='ECO  ����'></TD></TR>-->

			<!-- �������� �ۼ� -->
			<TR>
				<TD width="15%" height="25" class="bg_05" align='middle'>ECO NO</td>
				<TD width="35%" height="25" class="bg_06" >&nbsp;<%=ecc.getEcoNo()%></td>
				<TD width="15%" height="25" class="bg_05" align='middle'>��    ��</td>
				<TD width="35%" height="25" class="bg_06" >&nbsp;<%=ecc.getEccSubject()%></td>
			</TR>					 
			<!-- ������ �ۼ� -->
			<TR>
				<TD width="15%" height="25" class="bg_05" align='middle'>���� ����</td>
				<TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getEccReason()%></td>
				<TD width="15%" height="25" class="bg_05" align='middle'>���� ����</td>
			    <TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getEccFactor()%></td>
			</TR>
			<TR>
				<TD width="15%" height="25" class="bg_05" align='middle'>���� ����</td>
				<TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getEccScope()%></td>
				<TD width="15%" height="25" class="bg_05" align='middle'>���� ����</td>
			    <TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getEccKind()%></td>
			</TR>
			<TR>
				<TD width="15%" height="25" class="bg_05" align='middle'>�� ǰ ��</td>
				<TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getPdgCode()%></td>
				<TD width="15%" height="25" class="bg_05" align='middle'>��    ǰ</td>
				<TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getPdCode()%></td>
			</TR>
			<TR>
				<TD width="15%" height="25" class="bg_05" align='middle'>�����߻���(FG)</td>
				<TD width="35%" height="25" class="bg_06">&nbsp;
					<TEXTAREA NAME="fg_code" rows='3' cols='23' readonly><%=ecc.getFgCode()%></TEXTAREA></td>
				<TD width="15%" height="25" class="bg_05" align='middle'>�����߻���ǰ</td>
				<TD width="35%" height="25" class="bg_06">&nbsp;
					<TEXTAREA NAME="part_code" rows='3' cols='23' readonly><%=ecc.getPartCode()%></TEXTAREA></td>
			</TR>
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle'>�����(FG)</td>
			   <TD width="35%" height="25" class="bg_06">&nbsp;
					<TEXTAREA NAME="fg_code" rows='3' cols='23' readonly><% while(model_iter.hasNext()){
						model = (eccModelTable)model_iter.next();
						out.println(model.getFgCode()+" ["+model.getModelCode()+": "+model.getModelName()+"]");
					} %></TEXTAREA></td>
			   <TD width="15%" height="25" class="bg_05" align='middle'>�� �� ��</td>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=anbdt.getSepDate(ecc.getOrderDate(),"-")%></td>
			</TR>  
			<!-- ��������� ���� -->
			<TR>
				<TD height="25" colspan='4'><img src='../dcm/images/title_pre_check.gif' align='absmiddle' alt='��������� ����'></TD>
			</TR>
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle'>���� �з�</td>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=trouble%></td>
			   <TD width="15%" height="25" class="bg_05" align='middle'>�����߻��κ�</td>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=chg_position%></td>
			</TR>
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle'>����׿���</td>
			   <TD width="35%" height="25" class="bg_06">&nbsp;
					<TEXTAREA NAME="condition" rows='4' cols='23' readonly><%=condition%></TEXTAREA></td>
			   <TD width="15%" height="25" class="bg_05" align='middle'>ECO���볻��</td>
			   <TD width="35%" height="25" class="bg_06">&nbsp;
					<TEXTAREA NAME="condition" rows='4' cols='23' readonly><%=solution%></TEXTAREA></td>
			</TR>
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle'>÷�� ����</td>
			   <TD width="35%" height="25" class="bg_06" colspan='3'>&nbsp;
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
			<!--   ��������� ����    -->
			<TR>
				<TD height="25" colspan='4'><img src='../dcm/images/title_after_check.gif' align='absmiddle' alt='��������� ����'></TD>
			</TR>
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle'>���� �з�</td>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=eco_trouble%></td>
			   <TD width="15%" height="25" class="bg_05" align='middle'>�����߻��κ�</td>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=eco_chg_position%></td>
			</TR>
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle'>����׿���</td>
			   <TD width="35%" height="25" class="bg_06">&nbsp;
					<TEXTAREA NAME="solution" rows='4' cols='23' readonly><%=eco_condition%></TEXTAREA></td>
			   <TD width="15%" height="25" class="bg_05" align='middle'>ECO���볻��</td>
			   <TD width="35%" height="25" class="bg_06">&nbsp;
					<TEXTAREA NAME="solution" rows='4' cols='23' readonly><%=eco_solution%></TEXTAREA></td>
			</TR>
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle'>÷�� ����</td>
			   <TD width="35%" height="25" class="bg_06" colspan='3'>&nbsp;
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
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle'>���泻����</td>
			   <TD width="35%" height="25" class="bg_06" colspan=3>&nbsp;<a href='javascript:viewBom()'><img src='../dcm/images/bt_viewbom.gif' border='0' align='absmiddle' alt='BOM ���泻��������'></a></td>
			</TR>
			<TR>
			   <TD width="100%" height="5" colspan='4'>&nbsp;</td>			   
			</TR>
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle'>�� �� ��</td>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getEcrName()%></td>
			   <TD width="15%" height="25" class="bg_05" align='middle'>�� �� ��</td>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getEcrDivName()%></td>
			</TR>
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle'>��ȭ ��ȣ</td>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getEcrTel()%></td>
			   <TD width="15%" height="25" class="bg_05" align='middle'>�� �� ��</td>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=anbdt.getSepDate(ecc.getEcrDate(),"-")%></td>
			</TR> 
			<TR>
			   <TD width="15%" height="25" class="bg_05" align='middle'>����å����</td>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getMgrName()%></td>
			   <TD width="15%" height="25" class="bg_05" align='middle'>��������</td>
			   <TD width="35%" height="25" class="bg_06">&nbsp;<%=ecc.getEcoName()%></td>
			</TR>
			</table>
      </td></TR></table>
<form name="eForm" method="post" style="margin:0">
<input type="hidden" name="mode" value=''>
<input type="hidden" name="gid" value='<%=gid%>'>
<input type="hidden" name="eco_no" value='<%=ecc.getEcoNo()%>'>
<form>

</body></html>

<script language='javascript'>
<!--
//BOM���泻����
function viewBom()
{
	var gid = document.eForm.gid.value;
	var eco_no = document.eForm.eco_no.value;
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

// print
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
// ������������ ���������� �и��� �ʵ��� ó��
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 200 ;
	item_list.style.height = div_h;
}
-->
</script>