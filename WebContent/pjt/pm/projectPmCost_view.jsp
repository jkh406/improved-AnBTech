<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "���� ���ߺ�� �󼼳��� ����"		
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
	/*********************************************************************
	 	�ʱ�ȭ ����
	*********************************************************************/
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//����
	com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();			//���ڿ� ó��
	com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat("0,000");		//�������

	String pid="",pjt_code="",pjt_name="",node_code="",node_name="";
	String user_id="",user_name="",cost_type="",node_cost="",exchange="",in_date="",remark="";
	/*********************************************************************
	 	�ش���� ���� ��� �������� �б�
	*********************************************************************/
	com.anbtech.pjt.entity.projectTable cost;									
	ArrayList cost_list = new ArrayList();
	cost_list = (ArrayList)request.getAttribute("NCOST_List");
	cost = new projectTable();
	Iterator cost_iter = cost_list.iterator();
	
	if(cost_iter.hasNext()) {
		cost = (projectTable)cost_iter.next();
		pid = cost.getPid();
		pjt_code = cost.getPjtCode();
		pjt_name = cost.getPjtName();
		node_code = cost.getNodeCode();	
		node_name = cost.getNodeName();	
		user_id = cost.getUserId();
		user_name = cost.getUserName();
		cost_type = cost.getCostType(); 
		node_cost = fmt.DoubleToString(Double.parseDouble(cost.getNodeCost()));	
		exchange = cost.getExchange();
		in_date = cost.getInDate();
		remark = cost.getRemark();
	}
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--

-->
</script>
<BODY topmargin="0" leftmargin="0">

<!-- ��� -->
<TABLE  cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
				<TD valign='middle' class="title"><img src="../pjt/images/blet.gif" align="absmiddle"> ���ߺ�� �󼼺���</TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>	
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--��ư-->
			<TABLE width='100%' cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='5'></TD>
					<TD align=left width=''>
						<a href='javascript:self.close()'>
						<img src='../pjt/images/bt_close.gif' border='0'></a>
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
</TABLE>
<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�� �� ��</td>
			   <td width="40%" height="25" class="bg_04" colspan=3><%=pjt_code%> <%=pjt_name%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">P M ��</td>
			   <td width="40%" height="25" class="bg_04"><%=user_id%>/<%=user_name%></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">����</td>
			   <td width="40%" height="25" class="bg_04"><%=node_code%> <%=node_name%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">����׸�</td>
			   <td width="40%" height="25" class="bg_04">
					<%
						String[] s_code = {"1","2","3","4","5","6"};
						String[] s_name = {"�ΰǺ�","SAMPLE","������","���ڰ��","�԰ݽ��κ�","�ü����ں�"};
						String tsel = "";
						for(int ti=0; ti<s_code.length; ti++) {
							if(cost_type.equals(s_code[ti])) {
								out.println(s_name[ti]);
							}
						}
					%></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�ۼ���</td>
			   <td width="40%" height="25" class="bg_04"><%=in_date%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">����ݾ�</td>
			   <td width="40%" height="25" class="bg_04"><%=node_cost%> [��]</td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">ȯ ��</td>
			   <td width="40%" height="25" class="bg_04"><%=exchange%> [��]</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�� ��</td>
			   <td width="40%" height="25" class="bg_04" colspan=3>
					<textarea name='remark' rows='5' cols='80' readonly><%=remark%></textarea></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>

</body>
</html>
