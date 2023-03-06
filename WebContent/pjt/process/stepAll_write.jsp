<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "������� �����׸�(step) �űԵ��"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.util.normalFormat"
	import="java.sql.Connection"
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
	 	���μ��� ���ߴܰ� ��üList
	*********************************************************************/
	String[] phaseColumn = {"ph_code","ph_name"};
	bean.setTable("prs_phase");		
	bean.setColumns(phaseColumn);
	query = " where type='P' order by ph_code ASC";
	bean.setSearchWrite(query);
	bean.init_write();

	int cnt = bean.getTotalCount();
	String[][] phase = new String[cnt][2];
	int n=0;
	while(bean.isAll()) {
		phase[n][0] = bean.getData("ph_code");
		phase[n][1] = bean.getData("ph_name");
		n++;
	}

	/*********************************************************************
	 	����ó���� ����Ǵ� step�ڵ� ã��
	*********************************************************************/
	String ph_code = request.getParameter("ph_code");
	if(ph_code == null) ph_code = "S01";

	String[] stepColumn = {"ph_code","step_code"};
	bean.setTable("prs_step");		
	bean.setColumns(stepColumn);
	query = " where type='P' and ph_code='"+ph_code+"' order by step_code DESC";
	bean.setSearchWrite(query);
	bean.init_write();

	String step_code = "N00";
	if(bean.isAll()) step_code = bean.getData("step_code");
	String tmp_step_code = step_code;	//�ӽ� ����

	if(step_code.length() == 3) {		//���ĺ� Ȯ���ڵ尡 �ƴϸ�... +1�� ����
		int num = Integer.parseInt(step_code.substring(1,3))+1;
		normalFormat fmt = new com.anbtech.util.normalFormat("00");				
		step_code = "N"+fmt.toDigits(num);
	}

	/*********************************************************************
	 	����ó���� ����Ǵ� step�ڵ��� ����� �� �ִ��� �Ǵ��ϱ�
		(���� Step Code�� �Ϻα����� �����Ǿ��� Ȯ���ϱ�)
	*********************************************************************/
	com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
	Connection con = connMgr.getConnection("mssql");
	com.anbtech.pjt.db.prsStepDAO stepDAO = new com.anbtech.pjt.db.prsStepDAO(con);

	//�Ϻα������� ���Ǿ��� �Ǵ��ϱ�
	String use = "N";					//�Ϻα����� �����Ǿ��� Ȯ���ϱ� (Y:������  N:�����ȵ�)
	if(tmp_step_code.length() == 3)		//���ĺ� Ȯ���ڵ尡 ���°�� (��,N04A N04B ...)
		use = stepDAO.useStepAtActivity (step_code,"P");		//P:�������, �μ��ڵ�:�μ�����

	//�ٽ� step code�����ϱ�
	if(use.equals("Y")) {											//����
		step_code = stepDAO.searchStepCode(ph_code,tmp_step_code,"P");
	}
	else if(use.equals("N") && (tmp_step_code.length() == 4)) {		//���ĺ� Ȯ���ڵ尡 �ִ°��� ������
		step_code = stepDAO.searchStepCode(ph_code,tmp_step_code,"P");
	}
	connMgr.freeConnection("mssql",con);

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
	rtn = document.eForm.ph_code.value;
	if(rtn.length == 0) { alert('���ߴܰ��̸��� �����Ͻʽÿ�.');  return; }
	rtn = document.eForm.step_name.value;
	if(rtn.length == 0) { alert('�����׸��̸��� �����Ͻʽÿ�.');  return; }

	//ó���� �޽��� ���
	document.all['lding'].style.visibility="visible";

	document.eForm.action='../../servlet/prsCodeServlet';
	document.eForm.mode.value='STP_WA';	
	document.eForm.submit();
}
//���ߴܰ�� �����Ͽ� �����׸��ڵ� �űԻ���
function selectPhCode()
{
	document.eForm.action="stepAll_write.jsp";
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
					<TD valign='middle' class="title"><img src="../images/blet.gif">������� �����׸�[Step]���</TD>
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
				<select name='ph_code' onChange="javascript:selectPhCode();">
				<% 
					String sel = "";
					for(int i=0; i<cnt; i++) {
						if(ph_code.equals(phase[i][0])) sel = " selected ";
						else sel = "";
						out.println("<OPTION value='"+phase[i][0]+"' "+sel+">"+phase[i][0]+" "+phase[i][1]+"</OPTION>");
					}
				%>
				</select>
				</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�����׸��ڵ�</td>
			   <td width="80%" height="25" class="bg_04"><%=step_code%>
					<input type='hidden' name='step_code' value='<%=step_code%>'></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="20%" height="25" class="bg_03" background="../images/bg-01.gif">�����׸��̸�</td>
			   <td width="80%" height="25" class="bg_04">
					<input type='text' name='step_name' value=''></td>
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
<input type='hidden' name='type' value='P'>
<input type='hidden' name='tag' value='A'>    <%//A:�������, D:�μ�����%>
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
