<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "�۾� ���� �ϱ�"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();

	//---------------------------------------------------------
	//	�Ķ���� �ޱ�
	//--------------------------------------------------------
	String pjt_code="",pjtWord="0",sItem="",sWord="",parent_node="",child_node="",chg_note="";;
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");
	para = new projectTable();
	Iterator para_iter = para_list.iterator();

	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();
		pjt_code = para.getPjtCode();
		pjtWord = para.getPjtword();
		sItem = para.getSitem();
		sWord = para.getSword();
		parent_node = para.getParentNode();
		child_node = para.getChildNode();
		chg_note = para.getChgNote();				//�����������
	}

	//----------------------------------------------------
	//	�ش���� ��ȹ��/������ �� �������� ã�� [�⺻�������� ã��]
	//----------------------------------------------------
	String plan_sd="",plan_ed="",change_sd="",change_ed="";	//��ȹ����,������, ��������,������
	String pjt_status = "";									//�����������
	com.anbtech.pjt.entity.projectTable gen;
	ArrayList gen_list = new ArrayList();
	gen_list = (ArrayList)request.getAttribute("GEN_List");
	gen = new projectTable();
	Iterator gen_iter = gen_list.iterator();

	if(gen_iter.hasNext()) {
		gen = (projectTable)gen_iter.next();
		plan_sd = gen.getPlanStartDate();		if(plan_sd == null) plan_sd = "";		//��ȹ������
		plan_ed = gen.getPlanEndDate();			if(plan_ed == null) plan_ed = "";		//��ȹ������
		change_sd = gen.getChgStartDate();		if(change_sd == null) change_sd = "";	//����������
		change_ed = gen.getChgEndDate();		if(change_ed == null) change_ed = "";	//����������
		pjt_status = gen.getPjtStatus();
	}

	//------------------------------------------------------------
	//	�ش� ������ ��� LIST
	//------------------------------------------------------------
	com.anbtech.pjt.entity.projectTable man;
	ArrayList man_list = new ArrayList();
	man_list = (ArrayList)request.getAttribute("MAN_List");
	man = new projectTable();
	Iterator man_iter = man_list.iterator();
	int man_cnt = man_list.size();

	String[][] member = new String[man_cnt][4];
	String pjt_member = "";		//���߸�������� ����
	int p = 0;
	while(man_iter.hasNext()) {
		man = (projectTable)man_iter.next();
		member[p][0] = man.getPjtMbrType();
		member[p][1] = man.getPjtMbrId();
		member[p][2] = man.getPjtMbrName();
		member[p][3] = man.getPjtMbrJob();
		pjt_member += member[p][0]+"|"+member[p][1]+"|"+member[p][2]+"|"+member[p][3]+";";
		p++;
	}

	//-----------------------------------------------------------
	//	�ش� ������ ��� ������ [������ �Է�����]
	//-----------------------------------------------------------
	String pid="",pjt_name="",level_no="",node_name="",user_id="",user_name="",pjt_node_mbr="";
	String plan_cnt="",chg_cnt="",rst_cnt="",node_status="",remark="";
	String psd="",ped="",csd="",ced="",rsd="",red="";		//DB���� ��ȹ��,������,�Ϸ���
	String dpsd="",dped="",dcsd="",dced="",drsd="",dred="";	//ȭ����¿�
	com.anbtech.pjt.entity.projectTable sch;
	ArrayList sch_list = new ArrayList();
	sch_list = (ArrayList)request.getAttribute("NODE_List");
	sch = new projectTable();
	Iterator sch_iter = sch_list.iterator();

	if(sch_iter.hasNext()) {
		sch = (projectTable)sch_iter.next();

		pid = sch.getPid();
		pjt_code = sch.getPjtCode();
		pjt_name = sch.getPjtName();
		parent_node = sch.getParentNode();
		child_node = sch.getChildNode();
		level_no = sch.getLevelNo();
		node_name = sch.getNodeName();
		user_id = sch.getUserId();					if(user_id == null) user_id = "";
		user_name = sch.getUserName();				if(user_name == null) user_name = "";
		pjt_node_mbr = sch.getPjtNodeMbr();			if(pjt_node_mbr == null) pjt_node_mbr = "";

		psd = sch.getPlanStartDate();	if(psd == null) psd = "";
		if(psd.length() != 0) dpsd = psd.substring(0,4)+"/"+psd.substring(4,6)+"/"+psd.substring(6,8);
		else dpsd = "";
		ped = sch.getPlanEndDate();	if(ped == null) ped = "";
		if(ped.length() != 0) dped = ped.substring(0,4)+"/"+ped.substring(4,6)+"/"+ped.substring(6,8);
		else dped = "";

		csd = sch.getChgStartDate();	if(csd == null) csd = "";
		if(csd.length() != 0) dcsd = csd.substring(0,4)+"/"+csd.substring(4,6)+"/"+csd.substring(6,8);
		else dcsd = "";
		ced = sch.getChgEndDate();	if(ced == null) ced = "";
		if(ced.length() != 0) dced = ced.substring(0,4)+"/"+ced.substring(4,6)+"/"+ced.substring(6,8);
		else dced = "";

		rsd = sch.getRstStartDate();	if(rsd == null) rsd = anbdt.getDate(0);
		if(rsd.length() != 0) drsd = rsd.substring(0,4)+"/"+rsd.substring(4,6)+"/"+rsd.substring(6,8);
		else drsd = anbdt.getDate(0);
		red = sch.getRstEndDate();	if(red == null) red = "";
		if(red.length() != 0) dred = red.substring(0,4)+"/"+red.substring(4,6)+"/"+red.substring(6,8);
		else dred = "";

		plan_cnt = Integer.toString(sch.getPlanCnt());
		chg_cnt = Integer.toString(sch.getChgCnt());
		rst_cnt = Integer.toString(sch.getResultCnt());
		node_status = sch.getNodeStatus();	if(node_status == null) node_status = "0";
		remark = sch.getRemark();			if(remark == null) remark = "";
	}
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//�������泻�� �����ϱ�
//�۾����� �Է��ϱ�
function orderJob()
{
	var psd = '<%=psd%>';								//��ȹ�Ⱓ ������
	var ped = '<%=ped%>';								//��ȹ�Ⱓ ������
	var csd = document.eForm.chg_start_date.value;		//�����Ⱓ ������
	for(i=0;i<2;i++) csd = csd.replace('/','');	
	var ced = document.eForm.chg_end_date.value;		//�����Ⱓ �Ϸ���
	for(i=0;i<2;i++) ced = ced.replace('/','');	
	if(csd > ced) { alert('�����Ⱓ�� �����ϰ� �Ϸ��� �Է¿� �߸��ֽ��ϴ�.'); return; } 

	var rsd = document.eForm.rst_start_date.value;		//�����Ⱓ ������
	for(i=0;i<2;i++) rsd = rsd.replace('/','');	
	var red = document.eForm.rst_end_date.value;		//�����Ⱓ �Ϸ���
	for(i=0;i<2;i++) red = red.replace('/','');	

	//�����Ⱓ �������� ��ȹ�Ⱓ��[�Ǵ� �����Ⱓ��]�� ���ԵǴ��� �˻�
	if((csd.length == 0) && (ced.length == 0)) {		//�����Ⱓ�� ������[��ȹ�Ⱓ���� �˻�]
		if((psd > rsd) || (ped < rsd)) { 
			alert('��ȹ�Ⱓ���� ��� �۾����÷� �����Ⱓ�� �Է��� �����Ͻʽÿ�.'); return;}
	} else {											//�����Ⱓ�� ������[�����Ⱓ���� �˻�]
		if((csd > rsd) || (ced < rsd)) { 
			alert('������������ ��� �����Ⱓ�Դϴ�. Ȯ���� �����Ͻʽÿ�.'); return;}
	}
	
	//�����Ⱓ�� �Է������� �⺻������ ��ȹ���� �� ���������� ���Ͽ� �޽��� �˷��ֱ�
	var plan_sd='<%=plan_sd%>';	
	var plan_ed='<%=plan_ed%>';
	var change_sd='<%=change_sd%>';
	var change_ed='<%=change_ed%>';

	var pnsd = plan_sd.substring(0,4)+'/'+plan_sd.substring(4,6)+'/'+plan_sd.substring(6,8);
	var pned = plan_ed.substring(0,4)+'/'+plan_ed.substring(4,6)+'/'+plan_ed.substring(6,8);
	var cned = change_sd.substring(0,4)+'/'+change_sd.substring(4,6)+'/'+change_sd.substring(6,8);
	var cned = change_ed.substring(0,4)+'/'+change_ed.substring(4,6)+'/'+change_ed.substring(6,8);

	if((csd.length != 0) && (ced.length != 0)) {		//�����Ⱓ�� �ٽ� �Է�������
		if(change_sd.length == 0) {		//������ȹ�� �Էµ��� ������ ��ȹ�ϰ� ��
			if(csd < plan_sd) { 
				alert('���� �⺻������ ��ȹ�����Ͽ��� ��� �����Դϴ�.\n\n[��ȹ�Ⱓ: '+pnsd+' ~ '+pned+']'); return; }
			if(ced > plan_ed) { 
				alert('���� �⺻������ ��ȹ�����Ͽ��� ��� �����Դϴ�.\n\n[��ȹ�Ⱓ: '+pnsd+' ~ '+pned+']'); return; }
		} else {						//������ȹ�� �Էµ� ���
			if(csd < change_sd) { 
				alert('���� �⺻������ ���������Ͽ��� ��� �����Դϴ�.\n\n[�����Ⱓ: '+cned+' ~ '+cned+']'); return; }
			if(ced > change_ed) { 
				alert('���� �⺻������ ���������Ͽ��� ��� �����Դϴ�.\n\n[�����Ⱓ: '+cned+' ~ '+cned+']'); return; }
		}
	}

	//�۾����û��� �Է¿��� Ȯ��
	var order = document.eForm.remark.value;
	if(order.length < 4) { alert('4���̻��� �۾����û����� �ʿ��մϴ�. ���û��� �Է��� �ٽ� �����Ͻʽÿ�.'); return; }
	
	//������ �����ϰ� �Է��� �����ϰ��� ���Ͽ� �ٸ���� ������� �Է¿���Ȯ��
	var db_csd = '<%=csd%>';		//DB ��ϵ� ���������
	var db_ced = '<%=ced%>';		//DB ��ϵ� ����������
	var chg_note = document.eForm.chg_note.value; 
	if((csd != db_csd) || (ced != db_ced)) {
		if(chg_note.length < 4) {alert('4���̻��� �������� ������ �Է��Ͻʽÿ�.'); return; }
	}


	//�Է�ó��
	document.eForm.action='../servlet/projectSchServlet';
	document.eForm.mode.value='PSC_J';
	document.eForm.plan_start_date.value=psd;
	document.eForm.plan_end_date.value=ped;
	document.eForm.chg_start_date.value=csd;
	document.eForm.chg_end_date.value=ced;
	document.eForm.rst_start_date.value=rsd;
	document.eForm.rst_end_date.value=red;
	document.eForm.submit();
}

//���� �Է��ϱ�
function OpenCalendar(FieldName) {
	var strUrl = "../pjt/Calendar.jsp?FieldName=" + FieldName;
	newWIndow = window.open(strUrl, "Calendar", "width=0, height=0");
}
-->
</script>
<BODY topmargin="0" leftmargin="0">
<form name="eForm" method="post" style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--Ÿ��Ʋ-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif"> �ش���� ����۾�����</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--��ư-->
			<TABLE cellSpacing=0 cellPadding=0>
				<TBODY>
				<TR>
					<TD align=left width=5></TD>
					<TD align=left width=100%>&nbsp;&nbsp;�����ڵ� : <%=pjt_code%> 
				&nbsp;&nbsp;&nbsp;&nbsp;������ : <%=pjt_name%>
					<% if(!node_status.equals("2")) {		//�Ϸ���°� �ƴϸ� %>
						&nbsp;&nbsp;&nbsp;&nbsp;<a href="javascript:orderJob();"><B>�۾�����</B></a>
					<% } %>
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><form name="eForm" method="post" style="margin:0">
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">����̸�</td>
			   <td width="90%" height="25" class="bg_04"><%=child_node%> <%=node_name%> &nbsp;&nbsp;&nbsp;&nbsp;
				<%
					String status = "";
					if(node_status.equals("")) status = "�̵��";
					else if(node_status.equals("0")) status = "������";
					else if(node_status.equals("1")) status = "������";
					else if(node_status.equals("2")) status = "�Ϸ�";
					else if(node_status.equals("3")) status = "DROP";
					else if(node_status.equals("4")) status = "HOLD";
					else if(node_status.equals("5")) status = "SKIP";
					out.print("<font color=blue>[������ : "+status+"]</font>"); 
				%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�� �� ��</td>
			   <td width="90%" height="25" class="bg_04">
				<%
					String[] grade_no = {"B","C","D","E","F","G"};
					String[] grade_name = {"��� PL","���� PL","SUB-PL","���㰳��","��������","�ܺ��η�"};
					
					out.print(user_name+" [");					//�̸�
					for(int i=0; i<man_cnt; i++) {
						if(user_id.equals(member[i][1])) {	//����� ������
							for(int g=0; g<grade_no.length; g++) {	//��å:������
								if(member[i][0].equals(grade_no[g]))out.print(grade_name[g]+" : ");
							}
							out.println(member[i][3] + "]");
						}
					}
				%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">���߸��</td>
			   <td width="90%" height="25" class="bg_04">
			   <%
					out.println("<textarea rows=4 name='' value='"+pjt_node_mbr+"' readonly>"+pjt_node_mbr+"</textarea>");
				%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">��ȹ�Ⱓ</td>
			   <td width="90%" height="25" class="bg_04">
					<input type='text' name='plan_start_date' value='<%=dpsd%>' size=10 readonly>
					&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='plan_end_date' value='<%=dped%>' size=10 readonly></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����Ⱓ</td>
			   <td width="90%" height="25" class="bg_04">
				<% if(node_status.equals("0")) {		//������ %>
					<input type='text' name='chg_start_date' value='<%=dcsd%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('chg_start_date');"><img src="../pjt/img/calendar.gif" border="0" valign='absbottom'></A>&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='chg_end_date' value='<%=dced%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('chg_end_date');"><img src="../pjt/img/calendar.gif" border="0" valign='absbottom'></A>
				<% } else {								//�������� %>
					<input type='text' name='chg_start_date' value='<%=dcsd%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('chg_start_date');"><img src="../pjt/img/calendar.gif" border="0" valign='absbottom'></A>&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='chg_end_date' value='<%=dced%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('chg_end_date');"><img src="../pjt/img/calendar.gif" border="0" valign='absbottom'></A>
				<% } %></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����Ⱓ</td>
			   <td width="90%" height="25" class="bg_04">
					<input type='text' name='rst_start_date' value='<%=drsd%>' size=10 readonly>
					&nbsp;&nbsp; ~ &nbsp;&nbsp;
					<input type='text' name='rst_end_date' value='<%=dred%>' size=10 readonly>
					&nbsp;&nbsp;&nbsp;&nbsp;[�����������Ҷ� ������������ ��ϵǸ� �Ϸ����� ����ڰ� �Է��մϴ�.]</td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�۾����û���</td>
			   <td width="90%" height="25" class="bg_04">
					<textarea rows=10 cols=60 name='remark' value='<%=remark%>'><%=remark%></textarea></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<% if(!node_status.equals("2")){				//������:���Ϸᰡ�ƴ�	%>
				<tr>
				   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">��������<br>�� ��</td>
				   <td width="90%" height="25" class="bg_04">
						<textarea rows=3 cols=60 name='chg_note' value=''><%=chg_note%></textarea></td>
				</tr>
				<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<% } %>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value='<%=pid%>'>
<input type='hidden' name='pjt_code' value='<%=pjt_code%>'>
<input type='hidden' name='pjt_name' value='<%=pjt_name%>'>
<input type='hidden' name='pjtWord' value='1'>
<input type='hidden' name='sItem' value='<%=sItem%>'>
<input type='hidden' name='sWord' value='<%=sWord%>'>

<input type='hidden' name='parent_node' value='<%=parent_node%>'>
<input type='hidden' name='child_node' value='<%=child_node%>'>
<input type='hidden' name='node_name' value='<%=node_name%>'>
<input type='hidden' name='node_status' value='<%=node_status%>'>
<input type='hidden' name='user_id' value='<%=user_id%>'>
<input type='hidden' name='user_name' value='<%=user_name%>'>
</form>

</body>
</html>
