<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "��� �۾����� ����Ʈ"		
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
	String pjt_code="",pjt_name="",node_code="",pjtWord="",sItem="",sWord="";
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");

	para = new projectTable();
	Iterator para_iter = para_list.iterator();

	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();
		pjt_code = para.getPjtCode();				//������Ʈ�ڵ�
		pjt_name = para.getPjtName();				//������Ʈ��
		node_code = para.getNodeCode();				//����ڵ�
		pjtWord = para.getPjtword();				//�ְ�[W]/����[M] ����
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

	String[][] event = new String[evt_cnt][6];
	int e = 0;
	while(evt_iter.hasNext()) {
		evt = (projectTable)evt_iter.next();
		event[e][0] = evt.getUserName();	if(event[e][0] == null) event[e][0] = "";	
		event[e][1] = evt.getInDate();		if(event[e][1] == null) event[e][1] = "";		
		event[e][2] = evt.getWmType();		if(event[e][2] == null) event[e][2] = "";	
		event[e][3] = evt.getEvtContent();  if(event[e][3] == null) event[e][3] = "";	
			if(event[e][3].length() > 30) event[e][3] = event[e][3].substring(0,30) + "...";
		event[e][4] = evt.getView()+" "+evt.getModify()+" "+evt.getDelete();
			if(event[e][4] == null) event[e][4] = "";	
		event[e][5] = Double.toString(evt.getProgress()*100);
		e++;
	}
	//���� �ֱٿ� �Է��� ���� ���ϱ� (���ϳ��� �Է� ��������)
	String lred = "";
	if(evt_cnt > 0)
		lred = str.repWord(event[0][1],"-","");				//�������� �Է��� ����

	String tpage = request.getParameter("Tpage"); if(tpage == null) tpage = "1";
	String cpage = request.getParameter("Cpage"); if(cpage == null) cpage = "1";
	int Tpage = Integer.parseInt(tpage);
	int Cpage = Integer.parseInt(cpage);

	//----------------------------------------------------
	//	�ش���� Activity����Ʈ �б� : pjt_schedule
	//----------------------------------------------------
	String psd="",csd="";	//��ȹ������,����������
	String user_id="",user_name="",pjt_node_mbr="",node_status="",red="";
	com.anbtech.pjt.entity.projectTable act;
	ArrayList act_list = new ArrayList();
	act_list = (ArrayList)request.getAttribute("ACT_List");
	act = new projectTable();
	Iterator act_iter = act_list.iterator();
	int act_cnt = act_list.size();

	String[][] activity = new String[act_cnt][8];
	int a = 0;
	while(act_iter.hasNext()) {
		act = (projectTable)act_iter.next();
		activity[a][0] = act.getPjtCode();	
		activity[a][1] = act.getChildNode();			
		activity[a][2] = act.getNodeName();	

		activity[a][3] = act.getUserId();			user_id += activity[a][3]+"|";
		activity[a][4] = act.getUserName();			user_name += activity[a][4]+"|";
		activity[a][5] = act.getPjtNodeMbr();		pjt_node_mbr += activity[a][5].trim()+"|"; 
		activity[a][6] = act.getNodeStatus();		node_status += activity[a][6]+"|";
		activity[a][7] = act.getRstEndDate();		red += activity[a][7]+"|";

		//�����Է°��� �Ǵ�Ű ����
		if(node_code.equals(activity[a][1])) {
			psd = act.getPlanStartDate();	if(psd == null) psd = "";
			csd = act.getChgStartDate();	if(csd == null) csd = "";
		}
		a++;
	}
	pjt_node_mbr = str.repWord(pjt_node_mbr,";","/");
	pjt_node_mbr = str.repWord(pjt_node_mbr,"\r","");
	pjt_node_mbr = str.repWord(pjt_node_mbr,"\n","");

	String tsd = anbdt.getDateNoformat();		//���ó��� : �����Է°��ɿ��� ���ϱ� ����

	//----------------------------------------------------
	//	��� ���ο�û �˷��ֱ�
	//----------------------------------------------------
	String ReqApp = request.getParameter("ReqApp"); if(ReqApp == null) ReqApp = "";

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//�����ο�û �޽��� �˷��ֱ�
var RA = '<%=ReqApp%>';
if(RA == 'Y') { alert('���Ϸ� ���ο�û�� �Ǿ����ϴ�.'); }
else if(RA == 'A') { alert('���οϷ�� ���� �Է��� �� �����ϴ�.'); }

//��弱���ϱ�
function goNode()
{
	//���õ� sel : pjt_code|node_code
	var node = document.nForm.node.value;
	var pjtnode = node.split('|');

	document.nForm.action='../servlet/projectEventServlet';
	document.nForm.mode.value='PSM_EL';
	document.nForm.pjt_code.value=pjtnode[0];
	document.nForm.node_code.value=pjtnode[1];
	document.nForm.submit();

}
//������ �̵��ϱ�
function goPage(a) 
{
	document.sForm.action='../servlet/projectEventServlet';
	document.sForm.mode.value='PSM_EL';
	document.sForm.page.value=a;
	document.sForm.submit();
}
//�˻��ϱ�
function goSearch()
{
	document.sForm.action='../servlet/projectEventServlet';
	document.sForm.mode.value='PSM_EL';
	document.sForm.page.value='1';
	document.sForm.submit();
}
//�ش��� �����Է��ϱ�
function eventWrite()
{
	//���õ� sel : pjt_code|node_code
	var node = document.nForm.node.value;
	var pjtnode = node.split('|');

	//������ ���ؽ� ��ȣ
	var num = document.nForm.node.selectedIndex;
	
	//������ �Ǵ��ϱ� : �������� ��常 �Է°���
	var status = '<%=node_status%>';
	var node_status = status.split('|');		//������
	var red = '<%=red%>';
	var node_red = red.split('|');				//���������

	//�Ϸ���� ���δ���� �˻�
	if(node_status[num] == '2') { alert('�Ϸ�� ���� ������ �Է��� �� �����ϴ�.'); return; }
	else if(node_status[num] == '11') {
		alert('���δ�� ���� ������ �Է��� �� �����ϴ�.'); return;
	}

	//������ڹ� ���߸������ �Է°���
	var login_id = '<%=login_id%>';			//������� ��ü
	var uid = '<%=user_id%>';
	var user_id = uid.split('|');
	var uid_tag = 'N';
	if(login_id.indexOf(user_id[num]) != -1) uid_tag = 'Y';		//����������� �Ǵ�

	var mbr = '<%=pjt_node_mbr%>';			//���߸�� ��ü
	var mbr_id = mbr.split('|');
	var pjt_mbr_id = mbr_id[num];			//��尳�߸��
	var mid_cnt = 0;
	for(i=0; i<pjt_mbr_id.length; i++) { if(pjt_mbr_id.charAt(i) == '/') mid_cnt++; }

	var mbr_tag = 'N';
	if(pjt_mbr_id.length > 0) {
		var mid = pjt_mbr_id.split('/');
		for(i=0; i<mid_cnt; i++) { if(login_id.indexOf(mid[i]) != -1) mbr_tag = 'Y'; }
	}

	if((uid_tag == 'N') && (mbr_tag == 'N')){ 
		alert('����Է��� ��尳�߸������ �Է��� �� �ֽ��ϴ�.'); return; 
	} 

	//����۾����ð� ���� ��� ��ȹ������ �Ǵ� ������������ ���Ͽ� �Է°��ɿ��� �Ǵ��ϱ�
	//�������� ������ ��ȹ����, ������ �������� ������
	var tsd = '<%=tsd%>';			//����
	var lred = '<%=lred%>';			//���� �����Է��� ���� 
	var psd = '<%=psd%>';			//��ȹ������
	var csd = '<%=csd%>';			//����������
	var ssd = psd;
	if(csd.length != 0) ssd = csd;	//������ 
	if(node_status[num] == '0') {	
		if(ssd > tsd) { 
			var cmt = '�����Է��� ���������ð� �ְų�, ��ȹ or ������������ ���Ϻ��� ũ�ų� ������ �����մϴ�.\n\n';
			cmt += '�����Է°��� ������ : '+ssd.substring(0,4)+'/'+ssd.substring(4,6)+'/'+ssd.substring(6,8);
			alert(cmt); return; 
		};
	}

	if(tsd == lred) { alert('�������ڿ� �ߺ����� ����/������ �Է��� �� �����ϴ�.'); return; }
	
	document.nForm.action='../servlet/projectEventServlet';
	document.nForm.mode.value='PSM_EWV';
	document.nForm.pjt_code.value=pjtnode[0];
	document.nForm.node_code.value=pjtnode[1];
	document.nForm.node_status.value=node_status[num];
	document.nForm.submit();
}
//�ش��� �Ϸ� ���ο�û�غ�
function nodeAppReq()
{
	//���õ� sel : pjt_code|node_code
	var node = document.nForm.node.value;
	var pjtnode = node.split('|');

	//������ ���ؽ� ��ȣ
	var num = document.nForm.node.selectedIndex;
	
	//������� �Ϸ���ο�û ����
	var login_id = '<%=login_id%>';			//������� ��ü
	var uid = '<%=user_id%>';
	var user_id = uid.split('|');
	var uid_tag = 'N';
	if(login_id.indexOf(user_id[num]) != -1) uid_tag = 'Y';		//����������� �Ǵ�

	if(uid_tag == 'N'){ 
		alert('������ڸ��� �Ϸ���ο�û�� �� �� �ֽ��ϴ�.'); return; 
	} 

	//������ �Ǵ��ϱ� : �������� ��常 �Է°���
	var status = '<%=node_status%>';
	var node_status = status.split('|');		//��� ����
	var red = '<%=red%>';
	var node_red = red.split('|');				//���������

	if(node_status[num] == '0') { alert('������ ���� �Ϸ���ο�û�� �� �� �����ϴ�.'); return; }
	else if(node_status[num] == '2') { alert('�Ϸ�� ���� �Ϸ���ο�û�� �� �� �����ϴ�.'); return; }
	else if(node_status[num] == '11') {
		alert('���δ�� ���� ���ο�û�� �� �� �����ϴ�.'); return;
	}

	//�������� �ߺ� �Է� �˻�
	var tsd = '<%=tsd%>';			//����
	var lred = '<%=lred%>';			//���� �����Է��� ���� 
	if(tsd == lred) { alert('�������ڿ� �ߺ����� ����/������ �Է��� �� �����ϴ�.'); return; }

	document.nForm.action='../servlet/projectEventServlet';
	document.nForm.mode.value='PSM_EAV';
	document.nForm.pjt_code.value=pjtnode[0];
	document.nForm.node_code.value=pjtnode[1];
	document.nForm.node_status.value=node_status[num];
	document.nForm.submit();
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
//�ش����� �����̷� ����[�����ۼ�/���ϼ���]
function eventModify(pid)
{
	document.aForm.action='../servlet/projectEventServlet';
	document.aForm.mode.value='PSM_EMV';
	document.aForm.pid.value=pid;
	document.aForm.submit();
}
//�ش����� �����̷� ����[�����ۼ�/���ϻ���]
function eventDelete(pid)
{
	document.aForm.action='../servlet/projectEventServlet';
	document.aForm.mode.value='PSM_ED';
	document.aForm.pid.value=pid;
	document.aForm.submit();
}
-->
</script>
<BODY topmargin="0" leftmargin="0">

<!-- ����Ʈ -->
<TABLE  cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
				<TD valign='middle' class="title"><img src="../pjt/images/blet.gif" align="absmiddle"> ������ �̷����� &nbsp;&nbsp;&nbsp;&nbsp;[������: <%=pjt_code%> <%=pjt_name%>]</TD>
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
				<TD width='2%'>&nbsp;</TD>
				<TD align=left width='53%'>
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
					<a href='Javascript:goSearch();'><img src='../pjt/images/bt_search3.gif' border='0' align='absmiddle'></a> <a href='javascript:eventWrite()'><b>�����ۼ�</b></a> 	<a href='javascript:nodeAppReq()'><b>���ο�û</b></a>
					<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
					<input type="hidden" name="mode" size="15" value="">
					<input type="hidden" name="pid" size="15" value="">
					<input type="hidden" name="page" size="15" value="">
					<input type="hidden" name="pjt_code" size="15" value="<%=pjt_code%>">
					<input type="hidden" name="pjt_name" size="15" value="<%=pjt_name%>">
					<input type="hidden" name="node_code" size="15" value="<%=node_code%>">
					<input type="hidden" name="user_id" size="15" value="<%=user_id%>">
					<input type="hidden" name="user_name" size="15" value="<%=user_name%>">
					<input type="hidden" name="pjt_node_mbr" size="15" value="<%=pjt_node_mbr%>">
					<input type="hidden" name="node_status" size="15" value="<%=node_status%>">
					</form> 
					</TD>
				<TD align=left width='25%'>
					<form name="nForm" method="post" style="margin:0">
					<select name="node" style=font-size:9pt;color="black"; onChange='javascript:goNode();'>  
					<%	
						String asel = "";
						for(int ai=0; ai<act_cnt; ai++) {
							if(node_code.equals(activity[ai][1])) asel = "selected";
							else asel = "";
							out.println("<option "+asel+" value='"+activity[ai][0]+"|"+activity[ai][1]+"'>");
							out.println(activity[ai][1]+" "+activity[ai][2]+"</option>");
						}
					%>
					</select>
					<input type="hidden" name="mode" size="15" value="">
					<input type="hidden" name="sItem" size="15" value="evt_content">
					<input type="hidden" name="sWord" size="15" value="">
					<input type="hidden" name="pjtWord" size="15" value="">
					<input type="hidden" name="pjt_code" size="15" value="">
					<input type="hidden" name="pjt_name" size="15" value="<%=pjt_name%>">
					<input type="hidden" name="node_code" size="15" value="">

					<input type="hidden" name="user_id" size="15" value="">
					<input type="hidden" name="user_name" size="15" value="">
					<input type="hidden" name="pjt_node_mbr" size="15" value="">
					<input type="hidden" name="pjt_status" size="15" value="">
					<input type="hidden" name="node_status" size="15" value="">
					</form>
					</TD>
				<TD width='15%' align='right' style="padding-right:10px">
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
				<TD align=left height="24" class='list_bg'><%=event[i][1]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height="24" class='list_bg'>&nbsp;<%=event[i][5]%>%</TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=left height="24" class='list_bg'>&nbsp;<%=event[i][3]%></TD>
				<TD><IMG height=1 width=1></TD>
				<TD align=middle height="24" class='list_bg'>&nbsp;<%=event[i][4]%></TD>
			</TR>
			<TR><TD colSpan=11 background="../pjt/images/dot_line.gif"></TD></TR>
		<% 
			}  //for 

		%>
			<input type="hidden" name="mode" size="15" value="">
			<input type="hidden" name="pid" size="15" value="">
			<input type="hidden" name="page" size="15" value="">
			<input type="hidden" name="sItem" size="15" value="<%=sItem%>">
			<input type="hidden" name="sWord" size="15" value="<%=sWord%>">
			<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">

			<input type="hidden" name="pjt_code" size="15" value="<%=pjt_code%>">
			<input type="hidden" name="pjt_name" size="15" value="<%=pjt_name%>">
			<input type="hidden" name="node_code" size="15" value="<%=node_code%>">
			<input type="hidden" name="user_id" size="15" value="<%=user_id%>">
			<input type="hidden" name="user_name" size="15" value="<%=user_name%>">
			<input type="hidden" name="pjt_node_mbr" size="15" value="<%=pjt_node_mbr%>">
			<input type="hidden" name="node_status" size="15" value="">
			</form>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	</TBODY>
</TABLE>

</body>
</html>
