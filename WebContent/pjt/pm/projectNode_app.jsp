<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "����۾���Ȳ��ȸ[����/�ݷ�]"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%

	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();

	//---------------------------------------------------------
	//	�Ķ���� �ޱ�
	//--------------------------------------------------------
	String pjtWord="",sItem="",sWord="",remark="";
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");

	para = new projectTable();
	Iterator para_iter = para_list.iterator();

	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();
		remark = para.getRemark();					//PM�ǰ�
		pjtWord = para.getPjtword();				//�����߰�����
		sItem = para.getSitem();		
		sWord = para.getSword();
	}

	//----------------------------------------------------
	//	�ش����/����� �������� LIST�б� : pjt_event
	//----------------------------------------------------
	com.anbtech.pjt.entity.projectTable evt;
	ArrayList evt_list = new ArrayList();
	evt_list = (ArrayList)request.getAttribute("EVENT_List");
	evt = new projectTable();
	Iterator evt_iter = evt_list.iterator();
	int evt_cnt = evt_list.size();

	String[][] event = new String[evt_cnt][7];
	int e = 0;
	while(evt_iter.hasNext()) {
		evt = (projectTable)evt_iter.next();
		event[e][0] = evt.getUserName();	if(event[e][0] == null) event[e][0] = "";	
		event[e][1] = evt.getInDate();		if(event[e][1] == null) event[e][1] = "";		
		event[e][2] = evt.getWmType();		if(event[e][2] == null) event[e][2] = "";	
		event[e][3] = evt.getEvtContent();  if(event[e][3] == null) event[e][3] = "";	
			if(event[e][3].length() > 30) event[e][3] = event[e][3].substring(0,30) + "...";
		event[e][4] = evt.getView();
			if(event[e][4] == null) event[e][4] = "";	
		event[e][5] = Double.toString(evt.getProgress()*100);
		event[e][6] = evt.getPid();
		e++;
	}

	String tpage = request.getParameter("Tpage"); if(tpage == null) tpage = "1";
	String cpage = request.getParameter("Cpage"); if(cpage == null) cpage = "1";
	int Tpage = Integer.parseInt(tpage);
	int Cpage = Integer.parseInt(cpage);

	//----------------------------------------------------
	//	�ش���� Activity Node ����Ʈ �б� : pjt_schedule
	//----------------------------------------------------
	String pid="",pjt_code="",pjt_name="",parent_node="",child_node="";
	String level_no="",node_name="",user_id="",user_name="",pjt_node_mbr="";
	String plan_cnt="",chg_cnt="",rst_cnt="",node_status="",progress="";
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

		rsd = sch.getRstStartDate();	if(rsd == null) rsd = "";
		if(rsd.length() != 0) drsd = rsd.substring(0,4)+"/"+rsd.substring(4,6)+"/"+rsd.substring(6,8);
		else drsd = "";
		red = sch.getRstEndDate();	if(red == null) red = "";
		if(red.length() != 0) dred = red.substring(0,4)+"/"+red.substring(4,6)+"/"+red.substring(6,8);
		else dred = "";

		plan_cnt = Integer.toString(sch.getPlanCnt());
		chg_cnt = Integer.toString(sch.getChgCnt());
		rst_cnt = Integer.toString(sch.getResultCnt());
		node_status = sch.getNodeStatus();	if(node_status == null) node_status = "0";
		
		progress = Double.toString(sch.getProgress()*100+0.001);
		progress = progress.substring(0,progress.indexOf('.')+2);

	}
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//������ �̵��ϱ�
function goPage(a) 
{
	var remark = document.bForm.remark.value;

	document.sForm.action='../servlet/projectNodeAppServlet';
	document.sForm.mode.value='PSN_AV';
	document.sForm.page.value=a;
	document.sForm.remark.value=remark;
	document.sForm.submit();
}
//�˻��ϱ�
function goSearch()
{
	var remark = document.bForm.remark.value;

	document.sForm.action='../servlet/projectNodeAppServlet';
	document.sForm.mode.value='PSN_AV';
	document.sForm.page.value='1';
	document.sForm.remark.value=remark;
	document.sForm.submit();
}
//�ش����� �����̷� �󼼺���
function eventView(pid,wm_type)
{
	sParam = "strSrc=../servlet/projectEventServlet&pid="+pid+"&mode=PSM_EV";
	if((wm_type == 'A') || (wm_type == 'R')) {	
		showModalDialog("../pjt/modalFrm.jsp?"+sParam,"","dialogWidth:720px;dialogHeight:640px;resizable=0");
	} else { 
		showModalDialog("../pjt/modalFrm.jsp?"+sParam,"","dialogWidth:720px;dialogHeight:520px;resizable=0");
	}
}
//�ش����� ���� ��������
function eventModify(pid)
{
	alert('�����Է¸�⿡�� �����մϴ�.'); 
}
//�ش����� ���� ��������
function eventDelete(pid)
{
	alert('�����Է¸�⿡�� �����մϴ�.'); 
}
//�������ϱ�
function nodeApproval()
{
	//���°˻�
	var progress = '<%=progress%>';
	var node_status = '<%=node_status%>';
	if(node_status == '2') { alert('�̹� ���ε� ����Դϴ�.'); return; }
	else if((progress != '100.0') && (node_status == '1')) {alert('���ο�û�� ��尡 �ƴմϴ�.'); return; }
	else if((progress == '100.0') && (node_status == '1')) {alert('���ο�û�� ��尡 �ƴմϴ�.'); return; }
	
	//�ǰ��Է¿���
	var remark = document.bForm.remark.value;
	if(remark < 2) { alert('������ �ǰ��� �����ϴ�. \n\n �ǰ��Է��� �ٽ� �����Ͻʽÿ�.'); return; }

	document.bForm.action='../servlet/projectNodeAppServlet';
	document.bForm.mode.value='PSN_A';
	document.bForm.page.value='1';
	document.bForm.node_status.value='2';
	document.bForm.submit();

}
//������ �ݷ��ϱ�
function nodeReject()
{
	//���°˻�
	var progress = '<%=progress%>';
	var node_status = '<%=node_status%>';
	if(node_status == '2') { alert('�̹� ���ε� ����Դϴ�.'); return; }
	else if((progress != '100.0') && (node_status == '1')) {alert('���ο�û�� ��尡 �ƴմϴ�.'); return; }
	else if((progress == '100.0') && (node_status == '1')) {alert('���ο�û�� ��尡 �ƴմϴ�.'); return; }

	//�ǰ��Է¿���
	var remark = document.bForm.remark.value;
	if(remark < 10) { alert('���ݷ� �ǰ��� �����ϴ�. \n\n �ǰ��Է��� �ٽ� �����Ͻʽÿ�.'); return; }

	document.bForm.action='../servlet/projectNodeAppServlet';
	document.bForm.mode.value='PSN_R';
	document.bForm.page.value='1';
	document.bForm.node_status.value='1';
	document.bForm.submit();
}
-->
</script>
<BODY topmargin="0" leftmargin="0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--Ÿ��Ʋ-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif"> ��� �⺻����</TD>
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
					<TD align=left width=100%><a href='javascript:nodeApproval()'><b>������</b></a>&nbsp;&nbsp;&nbsp;&nbsp;
					<a href='javascript:nodeReject()'><b>���ݷ�</b></a>
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
	<tr><form name="bForm" method="post" style="margin:0">
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����̸�</td>
			   <td width="40%" height="25" class="bg_04"><%=pjt_code%> <%=pjt_name%></td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">����̸�</td>
				<td width="40%" height="25" class="bg_04"><%=child_node%> <%=node_name%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�� �� ��</td>
			   <td width="40%" height="25" class="bg_04"><%=pjt_code%> <%=pjt_name%></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">���߸��</td>
			   <td width="40%" height="25" class="bg_04"><%=pjt_node_mbr%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">��ȹ�Ⱓ</td>
			   <td width="40%" height="25" class="bg_04"><%=dpsd%>&nbsp;&nbsp; ~ &nbsp;&nbsp;<%=dped%></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����Ⱓ</td>
			   <td width="40%" height="25" class="bg_04"><%=drsd%>&nbsp;&nbsp; ~ &nbsp;&nbsp;<%=dced%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����Ⱓ</td>
			   <td width="40%" height="25" class="bg_04"><%=drsd%>&nbsp;&nbsp; ~ &nbsp;&nbsp;<%=dred%></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">������</td>
			   <td width="40%" height="25" class="bg_04">
			   <%
					String status = "";
					if(node_status.equals("")) status = "�̵��";
					else if(node_status.equals("0")) status = "������";
					else if(node_status.equals("1")) status = "������";
					else if(node_status.equals("2")) status = "�Ϸ�";
					else if(node_status.equals("3")) status = "DROP";
					else if(node_status.equals("4")) status = "HOLD";
					else if(node_status.equals("5")) status = "SKIP";
								
					if(progress.equals("100.0") && (node_status.equals("11"))) status="���δ����";

					out.print("<font color=blue>"+status+"</font>"); 
			%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">PM�ǰ�</td>
				<td width="90%" height="25" class="bg_04" colspan=3>
					<textarea name='remark' rows='7' cols='80' value=''><%=remark%></textarea>
				</td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type="hidden" name="mode" size="15" value="">
<input type="hidden" name="spid" size="15" value="<%=event[0][6]%>"> <% //����/�ݷ�ID : pjt_event %>
<input type="hidden" name="pid" size="15" value="<%=pid%>">			 <% //pjt_schedule�� �ݿ� %>
<input type="hidden" name="page" size="15" value="">
<input type="hidden" name="sItem" size="15" value="<%=sItem%>">
<input type="hidden" name="sWord" size="15" value="<%=sWord%>">
<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
<input type="hidden" name="pjt_code" size="15" value="<%=pjt_code%>">
<input type="hidden" name="pjt_name" size="15" value="<%=pjt_name%>">
<input type="hidden" name="parent_node" size="15" value="<%=parent_node%>">
<input type="hidden" name="child_node" size="15" value="<%=child_node%>">
<input type="hidden" name="node_name" size="15" value="<%=node_name%>">
<input type="hidden" name="user_id" size="15" value="<%=user_id%>">
<input type="hidden" name="user_name" size="15" value="<%=user_name%>">
<input type="hidden" name="plan_start_date" size="15" value="<%=psd%>">
<input type="hidden" name="plan_end_date" size="15" value="<%=ped%>">
<input type="hidden" name="chg_start_date" size="15" value="<%=csd%>">
<input type="hidden" name="chg_end_date" size="15" value="<%=ced%>">
<input type="hidden" name="rst_start_date" size="15" value="<%=rsd%>">
<input type="hidden" name="rst_end_date" size="15" value="<%=red%>">
<input type="hidden" name="pjt_node_mbr" size="15" value="<%=pjt_node_mbr%>">
<input type="hidden" name="node_status" size="15" value="">
</form>	

<!-- ����Ʈ -->
<TABLE  cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
				<TD valign='middle' class="title"><img src="../pjt/images/blet.gif" align="absmiddle"> ������ �̷�����</TD>
				<TD style="padding-right:10px" align='right' valign='middle'><%=Cpage%>/<%=Tpage%> <img src="../pjt/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=32><!--��ư �� ����¡-->
		<TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
				<TD width=4>&nbsp;</TD>
				<TD align=left width='400'>
					<form name="sForm" method="post" style="margin:0">
					<select name="pjtWord" style=font-size:9pt;color="black"; onChange='javascript:goSearch();'>  
					<%
						String[] s_code = {"","W","M","N","A","R"};
						String[] s_name = {"��ü","�ְ�����","��������","���ο�û","������","���ιݷ�"};
						String tsel = "";
						for(int ti=0; ti<s_code.length; ti++) {
							if(pjtWord.equals(s_code[ti])) tsel = "selected";
							else tsel = "";
							out.println("<option "+tsel+" value='"+s_code[ti]+"'>"+s_name[ti]+"</option>");
						}
					%>
					</select>
					<select name="sItem" style=font-size:9pt;color="black";>  
					<%
						String[] sitems = {"evt_content","evt_note","evt_issue"};
						String[] snames = {"�ֿ䳻��","������","�̽�����"};
						String sel = "";
						for(int si=0; si<sitems.length; si++) {
							if(sItem.equals(sitems[si])) sel = "selected";
							else sel = "";
							out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
						}
					%>
					</select>
					<input type="text" name="sWord" size="15" value="<%=sWord%>">
					<a href='Javascript:goSearch();'><img src='../pjt/images/bt_search3.gif' border='0' align='absmiddle'></a>

					<input type="hidden" name="mode" size="15" value="">
					<input type="hidden" name="pid" size="15" value="<%=pid%>"> 
					<input type="hidden" name="page" size="15" value="">
					<input type="hidden" name="pjt_code" size="15" value="<%=pjt_code%>">
					<input type="hidden" name="pjt_name" size="15" value="<%=pjt_name%>">
					<input type="hidden" name="parent_node" size="15" value="<%=parent_node%>">
					<input type="hidden" name="child_node" size="15" value="<%=child_node%>">
					<input type="hidden" name="user_id" size="15" value="<%=user_id%>">
					<input type="hidden" name="user_name" size="15" value="<%=user_name%>">
					<input type="hidden" name="pjt_node_mbr" size="15" value="<%=pjt_node_mbr%>">
					<input type="hidden" name="node_status" size="15" value="<%=node_status%>">
					<input type="hidden" name="remark" size="15" value="">
					</form>
				</TD>
				<TD width='' align='right' style="padding-right:10px">
					<%	if (Cpage <= 1) {	%>		
						<img src='../pjt/images/bt_previous.gif' border='0' align='absmiddle'>
					 <%	} else 	{	%>		
						<a href='javascript:goPage(<%=Cpage-1%>)'>
						<img src='../pjt/images/bt_previous.gif' border='0' align='absmiddle'></a>

					 <%	} if ((Cpage != Tpage) && (Tpage != -1 )) { %>		
							<a href='javascript:goPage(<%=Cpage+1%>)'>
							<img src='../pjt/images/bt_next.gif' border='0' align='absmiddle'></a> 		
					 <%	} else 	{  %>		
							<img src='../pjt/images/bt_next.gif' border='0' align='absmiddle'>
					 <%	} %>
				</TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
	<!--����Ʈ-->
	<TR height=100%>
		<TD vAlign=top>
		<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
			<TBODY>
			<TR vAlign=middle height=25>
				<TD noWrap width=60 align=middle class='list_title'>����</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=60 align=middle class='list_title'>�ۼ���</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=100 align=middle class='list_title'>�ۼ���</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=50 align=middle class='list_title'>������</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=100% align=middle class='list_title'>�ֿ䳻��</TD>
				<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
				<TD noWrap width=80 align=middle class='list_title'>View</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
		<% if (evt_list.size() == 0) { %>
			<TR vAlign=center height=22>
				 <td colspan='11' align="middle">***** ������ �����ϴ�. ****</td>
			</tr> 
		<% } %>	

		<% 
			String type = "";
			for(int i=0; i<evt_cnt; i++) {
				if(event[i][2].equals("W")) type = "�ְ�����";
				else if(event[i][2].equals("M")) type = "��������";
				else if(event[i][2].equals("N")) type = "���ο�û";
				else if(event[i][2].equals("A")) type = "������";
				else if(event[i][2].equals("R")) type = "���ιݷ�";
		%>
			<form name="aForm" method="post" style="margin:0">
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				<TD align=middle height="24" class='list_bg'><%=type%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=event[i][0]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=event[i][1]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height="24" class='list_bg'>&nbsp;<%=event[i][5]%>%</TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height="24" class='list_bg'>&nbsp;<%=event[i][3]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'><%=event[i][4]%></TD>
			</TR>
			<TR><TD colSpan=11 background="../pjt/images/dot_line.gif"></TD></TR>
		<% 
			}  //while 

		%>
			<input type="hidden" name="mode" size="15" value="">
			<input type="hidden" name="pid" size="15" value="">
			<input type="hidden" name="page" size="15" value="">
			<input type="hidden" name="sItem" size="15" value="<%=sItem%>">
			<input type="hidden" name="sWord" size="15" value="<%=sWord%>">
			<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">

			<input type="hidden" name="pjt_code" size="15" value="<%=pjt_code%>">
			<input type="hidden" name="pjt_name" size="15" value="<%=pjt_name%>">
			<input type="hidden" name="parent_node" size="15" value="<%=parent_node%>">
			<input type="hidden" name="child_node" size="15" value="<%=child_node%>">
			<input type="hidden" name="user_id" size="15" value="<%=user_id%>">
			<input type="hidden" name="user_name" size="15" value="<%=user_name%>">
			<input type="hidden" name="pjt_node_mbr" size="15" value="<%=pjt_node_mbr%>">
			<input type="hidden" name="node_status" size="15" value="<%=node_status%>">
			<input type="hidden" name="remark" size="15" value="">
			</form>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	</TBODY>
</TABLE>

</body>
</html>
