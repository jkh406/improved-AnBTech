<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "�μ����� �����׸�(activity) �űԵ��"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.util.normalFormat"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%			
	//-----------------------------------
	// �ۼ��� ���� ��������
	//-----------------------------------
	String user_id = "";			//�ۼ��� ���
	String user_name = "";			//�ۼ��� �̸�
	String user_rank = "";			//�ۼ��� ����
	String div_id = "";				//�ۼ��� �μ��� �����ڵ�
	String div_name = "";			//�ۼ��� �μ���
	String div_code = "";			//�ۼ��� �μ��ڵ�
	String code = "";				//�ۼ��� �μ�Tree �����ڵ�

	/*********************************************************************
	 	�ش��� ���� �˾ƺ��� (�����) : ����� ���� [����]
	*********************************************************************/
	String[] Column = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code","b.code"};
	bean.setTable("user_table a,class_table b,rank_table c");		
	bean.setColumns(Column);
	bean.setOrder("a.id ASC");	
	String query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		user_id = login_id;								//����� ���
		user_name = bean.getData("name");				//����� ��
		user_rank = bean.getData("ar_name");			//����� ����
		div_id = bean.getData("ac_id");					//����� �μ��� �����ڵ�
		div_name = bean.getData("ac_name");				//����� �μ��� 
		div_code = bean.getData("ac_code");				//����� �μ��ڵ�
		code = bean.getData("code");					//�ۼ��� �μ�Tree �����ڵ�
	} //while

	/*********************************************************************
	 	���μ��� ���ߴܰ�[phase] ��üList
	*********************************************************************/
	String[] phaseColumn = {"ph_code","ph_name"};
	bean.setTable("prs_phase");		
	bean.setColumns(phaseColumn);
	query = " where type='"+div_code+"' order by ph_code ASC";
	bean.setSearchWrite(query);
	bean.init_write();

	int pcnt = bean.getTotalCount();
	String[][] phase = new String[pcnt][2];
	int p=0;
	while(bean.isAll()) {
		phase[p][0] = bean.getData("ph_code");
		phase[p][1] = bean.getData("ph_name");
		p++;
	}

	/*********************************************************************
	 	���μ��� �����׸�[step] ��üList
	*********************************************************************/
	String ph_code = request.getParameter("ph_code");
	if(ph_code == null) ph_code = "D01";

	String[] stepColumn = {"step_code","step_name"};
	bean.setTable("prs_step");		
	bean.setColumns(stepColumn);
	query = " where type='"+div_code+"' and ph_code='"+ph_code+"' order by step_code ASC";
	bean.setSearchWrite(query);
	bean.init_write();

	int scnt = bean.getTotalCount();
	if(scnt == 0) {
		out.println("<script>");
		out.println("alert('�ش�Ǵ� �����׸񳻿밡 �����ϴ�. ���� ����� �����Ͻʽÿ�.');");
		out.println("history.back();");
		out.println("</script>");
		return;
	}
	String[][] step = new String[scnt][2];
	int s=0;
	while(bean.isAll()) {
		step[s][0] = bean.getData("step_code");
		step[s][1] = bean.getData("step_name");
		s++;
	}
	/*********************************************************************
	 	����ó���� ����Ǵ� activity�ڵ� ã��
	*********************************************************************/
	String step_code = request.getParameter("step_code");
	if(step_code == null) step_code = "N01";

	//step������ phase���ý� ph_code clear
	String save_ph_code = request.getParameter("save_ph_code");	
	if(save_ph_code == null) save_ph_code = "C";
	if(save_ph_code.equals("C")) step_code = step[0][0];

	String[] actColumn = {"ph_code","step_code","act_code"};
	bean.setTable("prs_activity");		
	bean.setColumns(actColumn);
	query = " where type='"+div_code+"' and ph_code='"+ph_code+"' and step_code='"+step_code+"' order by act_code DESC";
	bean.setSearchWrite(query);
	bean.init_write();

	String act_code = step_code+"00";
	if(bean.isAll()) act_code = bean.getData("act_code");

	//�Ϻα��� ������ �űԷ� step code�� ���ĺ��� �߰��� ��� ��,N01A N03B ....
	int num = 0;
	if(act_code.length() == 5) num = Integer.parseInt(act_code.substring(3,5))+1;
	else num = Integer.parseInt(act_code.substring(4,6))+1;

	normalFormat fmt = new com.anbtech.util.normalFormat("00");				
	act_code = step_code+fmt.toDigits(num);

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//����ϱ�
function sendSave()
{
	//��ϰ˻�
	var rtn = '';
	rtn = document.eForm.act_name.value;
	if(rtn.length == 0) { alert('�����׸��̸��� ����Ͻʽÿ�.');  return; }

	//ó���� �޽��� ���
	document.all['lding'].style.visibility="visible";

	document.eForm.action='../../servlet/prsCodeServlet';
	document.eForm.mode.value='ACT_WD';	
	document.eForm.submit();
}
//���ߴܰ�� �����Ͽ� �����׸��ڵ� �űԻ���
function selectPhCode()
{
	document.eForm.action="activityDiv_write.jsp";
	document.eForm.save_ph_code.value="C";
	document.eForm.submit();
}
//�����׸�� �����Ͽ� �����׸��ڵ� �űԻ���
function selectStepCode()
{
	document.eForm.action="activityDiv_write.jsp";
	document.eForm.submit();
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
					<TD valign='middle' class="title"><img src="../images/blet.gif">�μ����� �����׸�[Activity]���</TD>
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
					<TD align=left width=200><a href="javascript:sendSave();"><img src="../images/bt_add.gif" border="0"></a><a href="javascript:history.go(-1)"><img src="../images/bt_cancel.gif" border="0"></a>
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
</TABLE>

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">���ߴܰ��̸�</td>
			   <td width="80%" height="25" class="bg_04">
					<input type='hidden' name='save_ph_code' value=''>
					<select name='ph_code' onChange="javascript:selectPhCode();">
					<% 
					String psel = "";
					for(int i=0; i<pcnt; i++) {
						if(ph_code.equals(phase[i][0])) psel = " selected ";
						else psel = "";
						out.println("<OPTION value='"+phase[i][0]+"' "+psel+">"+phase[i][0]+" "+phase[i][1]+"</OPTION>");
					}
					%>
					</select>
				</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�����׸��̸�</td>
			   <td width="80%" height="25" class="bg_04">	
			    <select name='step_code' onChange="javascript:selectStepCode();">
				<% 
					String sel = "";
					for(int i=0; i<scnt; i++) {
						if(step_code.equals(step[i][0])) sel = " selected ";
						else sel = "";
						out.println("<OPTION value='"+step[i][0]+"' "+sel+">"+step[i][0]+" "+step[i][1]+"</OPTION>");
					}
				%>
				</select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�����׸��ڵ�</td>
			   <td width="80%" height="25" class="bg_04"><%=act_code%>
					<input type='hidden' name='act_code' value='<%=act_code%>'></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�����׸��̸�</td>
			   <td width="80%" height="25" class="bg_04">
					<input type='text' size=30 name='act_name' value=''></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value='<%=bean.getID()%>'>
<input type='hidden' name='type' value='<%=div_code%>'>
<input type='hidden' name='tag' value='D'>    <%//A:�������, D:�μ�����%>
</form>

<div id="lding" style="position:absolute;left:180px;top:300px;width:300px;height:100px;visibility:hidden;">
<table width="400" border="1" cellspacing=1 cellpadding=1 bgcolor="#ABDDE9">
	<tr><td height="50" align="center" valign="middle" class='subB'>
		<marquee behavior="alternate">������ ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.</marquee>
	</td> 
	</tr>
</table>
</div>

</body>
</html>
