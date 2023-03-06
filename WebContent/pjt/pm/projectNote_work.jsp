<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "�����߳�� ����� �ֱ� ��������"		
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

	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String pjt_code="",pjt_name="",node_code="",pjtWord="evt_note";
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");
	para = new projectTable();
	Iterator para_iter = para_list.iterator();
	
	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();
		pjt_code = para.getPjtCode();
		pjt_name = para.getPjtName();
		node_code = para.getNodeCode();
		pjtWord = para.getPjtword();
	}
	if(pjtWord.length() <= 1) pjtWord = "evt_note";

	//-----------------------------------
	//	�������� activity��� ã��
	//-----------------------------------
	int sel_no = 0;		//activity���õ� ��ȣ

	com.anbtech.pjt.entity.projectTable act;
	ArrayList act_list = new ArrayList();
	act_list = (ArrayList)request.getAttribute("ACT_List");
	act = new projectTable();
	Iterator act_iter = act_list.iterator();
	int act_cnt = act_list.size();
	String[][] activity = new String[act_cnt][8];
	
	int n=0;
	while(act_iter.hasNext()) {
		act = (projectTable)act_iter.next();
		activity[n][0] = act.getChildNode();	//����ڵ�
		activity[n][1] = act.getNodeName();		//����
		activity[n][2] = act.getUserId();		//����ڻ��
		activity[n][3] = act.getUserName();		//������̸�

		//������
		activity[n][4] = Double.toString(act.getProgress()*100+0.001);	
		activity[n][4] = activity[n][4].substring(0,activity[n][4].indexOf('.')+2);
		
		//��ȹ�Ⱓ
		activity[n][5] = anbdt.getSepDate(act.getPlanStartDate(),"/")+" ~ "+anbdt.getSepDate(act.getPlanEndDate(),"/");

		//�����Ⱓ
		activity[n][6] = anbdt.getSepDate(act.getChgStartDate(),"/")+" ~ "+anbdt.getSepDate(act.getChgEndDate(),"/");

		//�����Ⱓ
		activity[n][7] = anbdt.getSepDate(act.getRstStartDate(),"/")+" ~ "+anbdt.getSepDate(act.getRstEndDate(),"/");

		n++;
	}

	//----------------------------------------------------
	//	�ش����/��� �Էµ� �������뺸��
	//----------------------------------------------------
	String node_name="";
	String user_id="",user_name="",in_date="",evt_content="",evt_note="",evt_issue="";
	
	com.anbtech.pjt.entity.projectTable work;
	ArrayList work_list = new ArrayList();
	work_list = (ArrayList)request.getAttribute("WORK_List");
	work = new projectTable();
	Iterator work_iter = work_list.iterator();

	if(work_iter.hasNext()) {
		work = (projectTable)work_iter.next();
		
		pjt_code=work.getPjtCode();
		pjt_name=work.getPjtName();
		node_code=work.getNodeCode();
		node_name=work.getNodeName();
		user_id=work.getUserId();
		user_name=work.getUserName();
		in_date=work.getInDate();
		evt_content=work.getEvtContent();
		evt_note=work.getEvtNote();
		evt_issue=work.getEvtIssue();
	}

	//-----------------------------------
	//	�������� activity�� ���� �Է��� ��üLIST
	//-----------------------------------
	com.anbtech.pjt.entity.projectTable indate;
	ArrayList indate_list = new ArrayList();
	indate_list = (ArrayList)request.getAttribute("INDATE_List");
	indate = new projectTable();
	Iterator indate_iter = indate_list.iterator();
	int indate_cnt = indate_list.size();
	String[] inputD = new String[indate_cnt];
	
	int d=0;
	while(indate_iter.hasNext()) {
		indate = (projectTable)indate_iter.next();
		inputD[d] = indate.getInDate();		//�����Է���
		d++;
	}
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//�˻��ϱ�
function goSearch()
{
	document.sForm.action='../servlet/projectNoteServlet';
	document.sForm.mode.value='PNT_WV';
	document.sForm.submit();
}
-->
</script>
<BODY topmargin="0" leftmargin="0">
<form name="sForm" method="post" style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--Ÿ��Ʋ-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif"> �ش������ ��������</TD>
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
					<TD align=left width='100%'>������ : <%=pjt_code%> <%=pjt_name%> 
						<select name="pjtWord" style=font-size:9pt;color="black"; onChange='javascript:goSearch();'>  
					<%
						String[] e_code = {"evt_content","evt_note","evt_issue"};
						String[] e_name = {"���೻��","������","�̽�����"};
						String esel = "";
						for(int ti=0; ti<e_code.length; ti++) {
							if(pjtWord.equals(e_code[ti])) esel = "selected";
							else esel = "";
							out.println("<option "+esel+" value='"+e_code[ti]+"'>"+e_name[ti]+"</option>");
						}
					%>
					</select>&nbsp;&nbsp;����̸� :
					<select name="node_code" style=font-size:9pt;color="black"; onChange='javascript:goSearch();'>  
					<%
						String nsel = "";
						for(int ti=0; ti<act_cnt; ti++) {
							if(node_code.equals(activity[ti][0])) { nsel = "selected"; sel_no=ti; }
							else nsel = "";
							out.print("<option "+nsel+" value='"+activity[ti][0]+"'>");
							out.println(activity[ti][0]+" "+activity[ti][1]+"</option>");
						}
					%>
					</select>
						</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
</TABLE>

<!-- ��庰 �������� �ۼ� ���� -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�ۼ���</td>
			   <td width="40%" height="25" class="bg_04">
				<%=user_name%>	[������:<%=activity[sel_no][4]%>%]</td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����Է���</td>
			   <td width="40%" height="25" class="bg_04">
					<select name="in_date" style=font-size:9pt;color="black"; onChange='javascript:goSearch();'> 
					<%
						String dsel = "";
						for(int ti=0; ti<indate_cnt; ti++) {
							if(in_date.equals(inputD[ti])) dsel = "selected";
							else dsel = "";
							out.print("<option "+dsel+" value='"+inputD[ti]+"'>");
							out.println(inputD[ti]+"</option>");
						}
					%>
					</select>
					[<%=activity[sel_no][7]%>]</td>
			</tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">��ȹ�Ⱓ</td>
			   <td width="40%" height="25" class="bg_04"><%=activity[sel_no][5]%></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����Ⱓ</td>
			   <td width="40%" height="25" class="bg_04"><%=activity[sel_no][6]%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			<% 	if(pjtWord.equals("evt_content")) {		%>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">���೻��</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
					<textarea name='evt_content' rows='5' cols='80' readonly><%=evt_content%></textarea></td>
			<% } else if(pjtWord.equals("evt_note")) {  %>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">������</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
					<textarea name='evt_note' rows='5' cols='80' readonly><%=evt_note%></textarea></td>
			<% } else if(pjtWord.equals("evt_issue")) {  %>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�̽�����</td>
			   <td width="90%" height="25" class="bg_04" colspan=3>
					<textarea name='evt_issue' rows='5' cols='80' readonly><%=evt_issue%></textarea></td>
			<% } %>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pjt_code' value='<%=pjt_code%>'>
<input type='hidden' name='pjt_name' value='<%=pjt_name%>'>
<input type='hidden' name='node_code' value='<%=activity[sel_no][0]%>'>
</form>

</body>
</html>
