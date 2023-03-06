<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "���μ��� ����"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="com.anbtech.date.anbDate"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//�ʱ�ȭ ����
	com.anbtech.pjt.entity.prsCodeTable table;
	String[][] node = new String[1][2];				//�Է��� ���μ��� �ڳ��� �̸�
	String[][] doc = new String[1][2];				//�Է��� ���⹰ �ڳ��� �̸�
	String[][] save = new String[1][2];				//�ش��忡 ��ϵ� ���⹰ �ڳ��� �̸�
	String title_name = "���ߴܰ�",title_doc="����������";
	int cnt = 0;									//���� ����

	//RD=Reload : ���μ��� ����� Tree Reload�ϱ�
	String RD = request.getParameter("RD"); if(RD == null) RD = "";	
	String comment = request.getParameter("comment"); if(comment == null) comment = "";	//������ ��� ����

	/*********************************************************************
	 	���� ���� �б�
	*********************************************************************/
	String prs_code="",parent_node="",child_node="",level_no="0",type="P",spid="";
	ArrayList base_list = new ArrayList();
	base_list = (ArrayList)request.getAttribute("Base_List");
	table = new prsCodeTable();
	Iterator base_iter = base_list.iterator();

	cnt = base_list.size();
	while(base_iter.hasNext()) {
		table = (prsCodeTable)base_iter.next();
		spid = table.getPid();						//����� �б�� �ʿ�
		prs_code = table.getPrsCode();
		parent_node = table.getParentNode();
		child_node = table.getChildNode();
		level_no = table.getLevelNo();
		type = table.getType();
	}

	/*********************************************************************
	 	�Է��� ���μ��� ����� �д´�.
	*********************************************************************/
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Data_List");
	table = new prsCodeTable();
	Iterator table_iter = table_list.iterator();

	int node_cnt = table_list.size();
	node = new String[node_cnt][2];
	int n = 0;
	while(table_iter.hasNext()) {
		table = (prsCodeTable)table_iter.next();
		
		//���ߴܰ�
		if(level_no.equals("0")) {
			node[n][0] = table.getPhCode();
			node[n][1] = table.getPhName();
			title_name = "���ߴܰ�";
		}
		//�����׸�
		else if(level_no.equals("1")) {
			node[n][0] = table.getStepCode();
			node[n][1] = table.getStepName();
			title_name = "�����׸�";
		}
		//�����׸�
		else if(level_no.equals("2")) {
			node[n][0] = table.getActCode();
			node[n][1] = table.getActName();
			title_name = "�����׸�";
		}
		n++;
	}

	/*********************************************************************
	 	�Է��� �����׸� ���� ��������� �д´�.
	*********************************************************************/
	ArrayList doc_list = new ArrayList();
	doc_list = (ArrayList)request.getAttribute("Doc_List");
	table = new prsCodeTable();
	Iterator doc_iter = doc_list.iterator();

	int doc_cnt = doc_list.size();
	doc = new String[doc_cnt][2];
	int m = 0;
	while(doc_iter.hasNext()) {
		table = (prsCodeTable)doc_iter.next();
		
		doc[m][0] = table.getDocCode();
		doc[m][1] = table.getDocName();
		m++;
	}

	/*********************************************************************
	 	��忡 �Էµ� �����׸� ���� ��������� �д´�.
	*********************************************************************/
	ArrayList save_list = new ArrayList();
	save_list = (ArrayList)request.getAttribute("Save_List");
	table = new prsCodeTable();
	Iterator save_iter = save_list.iterator();

	cnt = save_list.size();
	save = new String[cnt][2];
	int s = 0;
	while(save_iter.hasNext()) {
		table = (prsCodeTable)save_iter.next();
		
		save[s][0] = table.getChildNode();
		save[s][1] = table.getNodeName();
		s++;
	}

	//level_no�� +1 �Ѵ�.
	level_no = Integer.toString(Integer.parseInt(level_no)+1);

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//����� Tree Reload�ϱ�
var RD = '<%=RD%>';
if(RD == "Reload") {
	var url = '../../servlet/prsStandardServlet';

	parent.viewL.document.sForm.action='processDiv_tree.jsp';
	parent.viewL.document.sForm.prs_code.value='<%=prs_code%>';
	parent.viewL.document.sForm.url.value=url;
	parent.viewL.document.sForm.submit();
}
//���������� �������� ���� ���� �˷��ֱ�
var comment = '<%=comment%>';
var len = comment.length;
if(len > 0) {
	if(comment == 'DOC') alert('�ش����� ���⹰�� �־� ������ �� �����ϴ�. ���� ���⹰�� ������ �����Ͻʽÿ�.');
	else alert(comment+'�� �Ϻα����� �ִ³��� ������ �� �����ϴ�.');
}
//���μ��� �Է��ϱ�(sForm)
function writePrs()
{
	var node_cnt = '<%=node_cnt%>'; 
	if(node_cnt == 0) return;

	var prs_code = '<%=prs_code%>';
	if(prs_code.length == 0) return;
	
	var f = document.sForm.prs;
	var node_code = "";
	for(i=1; i<f.length; i++) if(f[i].checked) node_code += f[i].value+",";

	document.sForm.action='../servlet/prsStandardServlet';
	document.sForm.mode.value='STD_WD';
	document.sForm.node_code.value=node_code;
	document.sForm.submit();
}

//���μ��� �����ϱ�(sForm)
function deletePrs()
{
	var node_cnt = '<%=node_cnt%>'; 
	if(node_cnt == 0) return;

	var prs_code = '<%=prs_code%>';
	if(prs_code.length == 0) return;

	var f = document.sForm.prs;
	var node_code = "";
	for(i=1; i<f.length; i++) if(f[i].checked) node_code += f[i].value+",";

	document.sForm.action='../servlet/prsStandardServlet';
	document.sForm.mode.value='STD_DD';
	document.sForm.node_code.value=node_code;
	document.sForm.submit();
}

//���⹰ �Է��ϱ�(dForm)
function writeDoc()
{
	var doc_cnt = '<%=doc_cnt%>'; 
	if(doc_cnt == 0) return;

	var f = document.dForm.doc;
	var node_code = "";
	for(i=1; i<f.length; i++) if(f[i].checked) node_code += f[i].value+",";

	document.dForm.action='../servlet/prsStandardServlet';
	document.dForm.mode.value='SDC_WD';
	document.dForm.node_code.value=node_code;
	document.dForm.submit();
}

//���⹰ �����ϱ�(dForm)
function deleteDoc()
{
	var doc_cnt = '<%=doc_cnt%>'; 
	if(doc_cnt == 0) return;

	var f = document.dForm.doc;
	var node_code = "";
	for(i=1; i<f.length; i++) if(f[i].checked) node_code += f[i].value+",";

	document.dForm.action='../servlet/prsStandardServlet';
	document.dForm.mode.value='SDC_DD';
	document.dForm.node_code.value=node_code;
	document.dForm.submit();
}
//���μ��� �ϰ� �����ϱ�
function prscheck(field) 
{
	var node_cnt = '<%=node_cnt%>'; 
	if(node_cnt == 0) return;

	if (field[0].checked == true) { 
		for (i = 1; i < field.length; i++) { 
			field[i].checked = true;
		} 
	}else { 
		for (i = 1; i < field.length; i++) { 
			field[i].checked = false; 
		} 
	}
} 

//���μ��� ���⹰ �ϰ� �����ϱ�
function doccheck(field) 
{
	var doc_cnt = '<%=doc_cnt%>'; 
	if(doc_cnt == 0) return;

	if (field[0].checked == true) { 
		for (i = 1; i < field.length; i++) { 
			field[i].checked = true;
		} 
	}else { 
		for (i = 1; i < field.length; i++) { 
			field[i].checked = false; 
		} 
	}
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
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif"> <%=title_name%> ��� </TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign='top'>
    <TBODY>
		<TR vAlign=middle height=25>
			<TD noWrap width=50 align=middle class='list_title'>
				<input type=checkbox name=prs onClick="javascript:prscheck(document.sForm.prs);">����</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
			<TD noWrap width=100 align=middle class='list_title'>�� ��</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
			<TD noWrap width=100% align=middle class='list_title'>�� ��</TD>
		</TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
	<% if (node.length == 0) { %>
		<TR vAlign=center height=22>
			 <td colspan='11' align="middle">***** ������ �����ϴ�. ****</td>
		</tr> 
	<% } %>	

	<% 
	if(prs_code.length() > 0) {
		for(int si=0; si<node.length; si++) {
	%>	
		<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			<TD align=middle height="24" class='list_bg'>
				<input type='checkbox' name='prs' value='<%=node[si][0]%>'></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=middle height="24" class='list_bg'><%=node[si][0]%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=left height="24" class='list_bg'>&nbsp;&nbsp;<%=node[si][1]%></TD>
		</TR>
		<TR><TD colSpan=11 background="../pjt/images/dot_line.gif"></TD></TR>
	<% 
		}  //for
	} //if

	%>
	</TBODY>
</TABLE>

<!--��ư-->
<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
	<TBODY>
	<TR>
		<TD width=4>&nbsp;</TD>
		<TD align=left width='400'>
			<a href='javascript:writePrs()'><img src="../pjt/images/bt_add.gif" border='0' align='absmiddle'></a> <a href='javascript:deletePrs()'><img src="../pjt/images/bt_del.gif" border='0' align='absmiddle'></a>
		</TD>
	</TR>
	</TBODY>
</TABLE>

<input type='hidden' name='mode' value=''>
<input type='hidden' name='spid' value='<%=spid%>'>
<input type='hidden' name='pid' value='<%=bean.getID()%>'>
<input type='hidden' name='prs_code' value='<%=prs_code%>'>
<input type='hidden' name='parent_node' value='<%=child_node%>'>
<input type='hidden' name='level_no' value='<%=level_no%>'>
<input type='hidden' name='type' value='<%=type%>'>
<input type='hidden' name='node_code' value=''>
<input type='hidden' name='array_cnt' value='<%=node.length%>'>
</form>

<pre> </pre>

<!-- �ϴ� ���� -->
<% if(level_no.equals("3")) { //���⹰�� �ִ� �����׸�ܰ踸 ����� %>
<form name="dForm" method="post" style="margin:0">
	<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
		<TR>
			<TD height=27><!--Ÿ��Ʋ-->
				<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
					<TBODY>
					<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
					<TR bgcolor="#BAC2CD">
						<TD valign='middle' class="title"><img src="../pjt/images/blet.gif"> ���������� ��� </TD>
					</TR>
					</TBODY>
				</TABLE>
			</TD>
		</TR>
		<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	</TABLE>


	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign='top'>
		<TR>
			<TD width='200' valign='top'> <!-- ��ϵ� ������� ��� -->
				<TABLE cellSpacing=0 cellPadding=5 width="100%" border=0 valign='top'>
				<TBODY>
					<TR vAlign=center bgColor=#FFFFFF height=22>
					<TD>
					<%
						out.println("<pre><font size=2 color=darkblue><b>��ϵ� �������</b></font><br>");
						for(int i=0; i<save.length; i++)
							out.println(save[i][0]+" "+save[i][1]);
						out.println("</pre>");
					%>
					</TD>
					</TR>
				</TBODY>
				</TABLE>
			</TD>
			<TD width='100%'> <!-- ����� ������� ��� -->
				<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign='top'>
				<TBODY>
					<TR vAlign=middle height=25>
						<TD noWrap width=50 align=middle class='list_title'>
							<input type=checkbox name=doc onClick="javascript:prscheck(document.dForm.doc);">����</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
						<TD noWrap width=100 align=middle class='list_title'>�� ��</TD>
						<TD noWrap width=6 class='list_title'><IMG src="../pjt/images/list_tep.gif"></TD>
						<TD noWrap width=100% align=middle class='list_title'>������� �̸�</TD>
					</TR>	
				<% if (doc.length == 0) { %>
					<TR vAlign=center height=22>
						 <td colspan='11' align="middle">***** ���⹰�� �����ϴ�. ****</td>
					</tr> 
				<% } %>	

				<% 
					for(int si=0; si<doc.length; si++) {
				%>	
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
						<TD align=middle height="24" class='list_bg'>
							<input type='checkbox' name='doc' value='<%=doc[si][0]%>'></TD>
						<TD><IMG height=1 width=1></TD>
						<TD align=middle height="24" class='list_bg'><%=doc[si][0]%></TD>
						<TD><IMG height=1 width=1></TD>
						<TD align=left height="24" class='list_bg'>&nbsp;&nbsp;<%=doc[si][1]%></TD>
					</TR>
					<TR><TD colSpan=11 background="../pjt/images/dot_line.gif"></TD></TR>
				<% 
					}  //for
				%>
				</TBODY>
				</TABLE>

				<!--��ư-->
				<table border=0 cellspacing=0 cellpadding=0 width="100%" bgcolor="#ABDDE9">
					<tr>
						<td height=28>
							<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" background='../pjt/img/button_bg.gif' border=0>
							<TBODY>
							<TR>
								<TD width=4>&nbsp;</TD>
								<TD align=left width='400'>
									<a href='javascript:writeDoc()'><img src="../pjt/images/bt_add.gif" border='0' align='absmiddle'></a> <a href='javascript:deleteDoc()'><img src="../pjt/images/bt_del.gif" border='0' align='absmiddle'></a>
								</TD>
							</TR>
							</TBODY>
						</TABLE>
						</td>
					</tr>
				</table>
			</TD>
		</TR>
		<TR bgcolor="gray"><TD height="2" colspan="2"></TD></TR>
	</TABLE>

	<input type='hidden' name='mode' value=''>
	<input type='hidden' name='spid' value='<%=spid%>'>
	<input type='hidden' name='pid' value='<%=bean.getID()%>'>
	<input type='hidden' name='prs_code' value='<%=prs_code%>'>
	<input type='hidden' name='parent_node' value='<%=child_node%>'>
	<input type='hidden' name='level_no' value='<%=level_no%>'>
	<input type='hidden' name='type' value='<%=type%>'>
	<input type='hidden' name='node_code' value=''>
	<input type='hidden' name='array_cnt' value='<%=doc.length%>'>
	</form>
<% } %>


</body>
</html>
