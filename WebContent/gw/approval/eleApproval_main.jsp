<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "���ڰ��� ����"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.gw.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//�ʱ�ȭ ����
//	com.anbtech.gw.db.AppProcessMasterDAO masterDAO = new com.anbtech.gw.db.AppProcessMasterDAO();
	com.anbtech.gw.entity.TableAppMaster table;
	com.anbtech.date.anbDate anbdt = new anbDate();
	String PROCESS_NAME = "";			//���ڰ����� �̸�
	String Message = "";
	String STATE = "";
	String pageNo = "";

	//-----------------------------------
	//	���ڰ��� ���� & ��ü ���� �ľ��ϱ�
	//-----------------------------------
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("Table_List");
	table = new TableAppMaster();
	Iterator table_iter = table_list.iterator();

	String PROCESS = request.getParameter("PROCESS");
	String RTpage = request.getParameter("Tpage");			//��ü������ ��
	int Tpage = Integer.parseInt(RTpage);
	String RCpage = request.getParameter("Cpage");			//���������� ��
	int Cpage = Integer.parseInt(RCpage);

	if(PROCESS.equals("APP_ING")) PROCESS_NAME = "�̰���";
	else if(PROCESS.equals("ASK_ING")) PROCESS_NAME = "������";
	else if(PROCESS.equals("APP_BOX")) PROCESS_NAME = "�����";
	else if(PROCESS.equals("APP_GEN")) PROCESS_NAME = "����� (�Ϲݹ���)";
	else if(PROCESS.equals("APP_SER")) PROCESS_NAME = "����� (������)";
	else if(PROCESS.equals("REJ_BOX")) PROCESS_NAME = "�ݷ���";
	else if(PROCESS.equals("TMP_BOX")) PROCESS_NAME = "������";
	else if(PROCESS.equals("SEE_BOX")) PROCESS_NAME = "�뺸��";
	else if(PROCESS.equals("DEL_BOX")) PROCESS_NAME = "������";
	else PROCESS_NAME = "����� (����İ���)";

	String search_item = Hanguel.toHanguel(request.getParameter("sItem")); 
	if(search_item == null) search_item = "app_subj";
	String search_word = Hanguel.toHanguel(request.getParameter("sWord"));
	if(search_word == null) search_word = "";

	//-------------------------------------------
	// ������ �븮������ ������ �ִ��� �Ǵ�
	// attorney_yn : Y[������ ����], N[������ ����]
	//-------------------------------------------
	String attorney_yn = Hanguel.toHanguel(request.getParameter("attorney_yn")); 
	if(attorney_yn == null) attorney_yn = "N";			

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<script language=javascript>
<!--
var prs = '<%=PROCESS%>';
if(prs == "TMP_BOX") parent.menu.location.reload();
//������ �̵��ϱ�
function goPage(a) {
	document.sForm.action='../servlet/ApprovalMenuServlet';
	document.sForm.mode.value='<%=PROCESS%>';
	document.sForm.page.value=a;
	document.sForm.submit();
}
//�˻��ϱ�
function goSearch()
{
	//num = sForm.sItem.selectedIndex;
	//s_item = sForm.sItem.options[num].value;
	document.sForm.action ='../servlet/ApprovalMenuServlet'
	document.sForm.mode.value='<%=PROCESS%>';
	document.sForm.page.value=1;
	document.sForm.submit();
}

//���繮�� ����
function eleApprovalView(a,b)
{
	//�̰����̸鼭 �븮�����ڰ� �����Ǿ������� ����
	var PROCESS = '<%=PROCESS%>';
	var ATTORNEY = '<%=attorney_yn%>';
	if((PROCESS == 'APP_ING') && (ATTORNEY == 'Y')) {
		alert('������ �븮�����ڰ� �����Ǿ��ֽ��ϴ�.\n\n �븮�����ڸ� ������ �����Ͻʽÿ�.'); 
		return; 
	}

	//�̰Ṯ��
	//if(PROCESS == 'APP_ING') {
		location.href ='../servlet/ApprovalDetailServlet?mode='+b+'&PID='+a;
	//} 
	//�׿ܹ���
	//else {
//		sParam = "strSrc=ApprovalDetailServlet&title=APPROVAL&PID="+a+"&mode="+b;
		//var rval = showModalDialog("../gw/approval/modalFrm.jsp?"+sParam,"","dialogWidth:750px;dialogHeight:750px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=no;resizable=0");

		//wopen('../servlet/ApprovalDetailServlet?PID='+a+'&mode='+b,'view_doc','730','600','scrollbars=yes,toolbar=no,status=no,resizable=no');
/*
		if(rval == "RL"){
			parent.menu.location.href="../servlet/ApprovalInitServlet?mode=menu";
			parent.view.location.href="../servlet/ApprovalMenuServlet?mode="+PROCESS;
		}
*/
	//} 
}

//�ӽ����幮�� ����
function pidDelete(a)
{
	d = confirm("�����Ͻðڽ��ϱ�?");
	if(d == false) return;
	
	//����
	document.sForm.action ='../servlet/ApprovalDeleteServlet';
	document.sForm.pid.value=a;
	document.sForm.submit();
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
-->
</script>
<LINK href="../gw/css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0'>

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../gw/images/blet.gif" align="absmiddle">
				 <%
					 if((PROCESS == null) || (PROCESS.equals("APP_ING")))	//�̰���
						out.println("�̰���");
					 else if (PROCESS.equals("ASK_ING"))					//������
						out.println("������");
					 else if (PROCESS.equals("REJ_BOX"))					//�ݷ���
						out.println("�ݷ���");
					 else if (PROCESS.equals("TMP_BOX"))					//�ӽ���
						out.println("������");
					 else if (PROCESS.equals("SEE_BOX"))					//�뺸��
						out.println("�뺸��");
					 else if (PROCESS.equals("DEL_BOX"))					//������
						out.println("������");
					else if (PROCESS.equals("APP_BOX"))						//�����
						out.println("�����");
					 else 
						out.println("������");
				  %>
			  </TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><%=Cpage%>/<%=Tpage%> <img src="../gw/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='600'>
				<form name="sForm" method="post" style="margin:0">
					<%	//������ �����˻������� �߰� [��������,�⵵����]
						int byear=Integer.parseInt(anbdt.getYear())-1; //1����
						if (PROCESS.equals("DEL_BOX")) {  
						out.println("<input type='text' name='syear' size='5' value='"+byear+"'>");	
						
						out.println("<select name='flag' style='font-size:9pt;';>"); 
						String[] dflg = {"","HYU_GA","OE_CHUL","CHULJANG_SINCHEONG","BOGO","CHULJANG_BOGO","GIAN","MYEONGHAM","SAYU","HYEOPJO","GUIN","GYOYUK_ILJI","BAE_CHA","AKG","TD","SERVICE","ODT","IDS","ODS","ASSET","EST","EWK","BOM","DCM","PCR","ODR","PWH","TGW"};
						String[] dname = {"��ü","��(��)����","�����","�����û��","����","���庸��","��ȼ�","���Խ�û��","������","������","�����Ƿڼ�","��������","������û��","���ο�","�������","������","��������","�系����","��ܰ���","�ڻ����","��������","Ư�ٰ���","BOM����","���躯��","���ſ�û","���ֿ�û","�����԰�","��ǰ���"};
						for(int di=0; di<dflg.length; di++) {
							out.println("<option value='"+dflg[di]+"'>"+dname[di]+"</option>");
						}
						out.println("</select>");
				   } %>

					<select name="sItem" style="font-size:9pt;";>  
					<%
						String[] sitems = {"app_subj","writer_name","write_date"};
						String[] snames = {"����","�ۼ���","�ۼ���"};
						String sel = "";
						for(int si=0; si<sitems.length; si++) {
							if(search_item.equals(sitems[si])) sel = "selected";
							else sel = "";
							out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
						}
					%>
					</select>
					<input type="text" name="sWord" size="15" value="<%=search_word%>">
					<a href='Javascript:goSearch();'><img src='../gw/images/bt_search.gif' border='0' align='absmiddle'></a>
					<input type="hidden" name="mode" value="">	
					<input type="hidden" name="page" value="<%=Cpage%>">
					<input type="hidden" name="pid" value="">
					<input type="hidden" name="PID" value=""></form></TD>
			  <TD width='' align='right' style="padding-right:10px">
					<%	if (Cpage <= 1) {	%>		
						<img src='../gw/images/bt_previous.gif' border='0' align="absmiddle">
					 <%	} else 	{	%>		
						<a href='javascript:goPage(<%=Cpage-1%>)'>
						<img src='../gw/images/bt_previous.gif' border='0' align="absmiddle"></a>

					 <%	} if ((Cpage != Tpage) && (Tpage != -1 )) { %>		
							<a href='javascript:goPage(<%=Cpage+1%>)'>
							<img src='../gw/images/bt_next.gif' border='0' align="absmiddle"></a> 		
					 <%	} else 	{  %>		
							<img src='../gw/images/bt_next.gif' border='0' align="absmiddle">
					 <%	} %></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
<!--����Ʈ-->
  <TR height=100%>
    <TD vAlign=top>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=40 align=middle class='list_title'>��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gw/images/list_tep.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gw/images/list_tep.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gw/images/list_tep.gif"></TD>
			  <TD noWrap width=40 align=middle class='list_title'>÷��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gw/images/list_tep.gif"></TD>
			  <TD noWrap width=120 align=middle class='list_title'>�����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gw/images/list_tep.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>����Ͻ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../gw/images/list_tep.gif"></TD>
			  <TD noWrap width=40 align=middle class='list_title'>���</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
	<% 
		int count=1;
		while(table_iter.hasNext()) {
			table = (TableAppMaster)table_iter.next();		
	%>	
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=count%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle height="24" class='list_bg'><%=table.getAmAppStatus()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=left class='list_bg' style="padding-left:3px"><%=table.getAmAppSubj()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg' style="padding-left:3px"><%=table.getAmAddCounter()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getAmWriterName()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=table.getAmWriteDate()%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'>
	<% if (PROCESS.equals("TMP_BOX") || PROCESS.equals("REJ_BOX"))	{	//�ӽ���,�ݷ��� %>
				<a href=javascript:pidDelete('<%=table.getAmPid()%>');><img src='../gw/images/lt_del.gif' border='0' align='absmiddle'></a>
    <% } %>
			  </TD></TR>
			<TR><TD colSpan=13 background="../gw/images/dot_line.gif"></TD></TR>
	<% 
			count++;
		}  //while 

	%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE>


</body>
</html>