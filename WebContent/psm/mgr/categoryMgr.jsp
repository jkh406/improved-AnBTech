<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "category ����"		
	contentType = "text/html; charset=KSC5601" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />
<%
	//--------------------
	//	�ʱ�ȭ ����
	//--------------------
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
	bean.openConnection();

	/*********************************************************************
	 	���ε����� �ޱ�
	*********************************************************************/
	String query = "",msg="";
	String mode = request.getParameter("mode"); if(mode == null) mode = "";
	String pid = request.getParameter("pid"); if(pid == null) pid = ""; 
	String korea_name = Hanguel.toHanguel(request.getParameter("korea_name"));
	if(korea_name == null) korea_name=""; 
	String english_name = Hanguel.toHanguel(request.getParameter("english_name"));
	if(english_name == null) english_name=""; 
	String key_word = Hanguel.toHanguel(request.getParameter("key_word"));
	if(key_word == null) key_word=""; else key_word = key_word.toUpperCase();
	String comp_no = Hanguel.toHanguel(request.getParameter("comp_no"));
	if(comp_no == null) comp_no=""; 
	String comp_korea = Hanguel.toHanguel(request.getParameter("comp_korea"));
	if(comp_korea == null) comp_korea=""; 
	String comp_english = Hanguel.toHanguel(request.getParameter("comp_english"));
	if(comp_english == null) comp_english="����"; 
	
	/*********************************************************************
	 	����ϱ�
	*********************************************************************/
	if(mode.equals("ADD")) {
		//��ϵ� �������� �Ǵ��ϱ�
		String sts = "";	//����ڹ�ȣ �ߺ��˻�
		query = "select * from psm_category where comp_no='"+comp_no+"'";
		bean.executeQuery(query);
		if(bean.next()) sts = bean.getData("comp_no");
		
		String kname="";	//��ü�� �ߺ��˻� (english_name�� ������ ��츸)
		query = "select * from psm_category where comp_korea='"+comp_korea+"'";
		bean.executeQuery(query);
		if(bean.next()) kname = bean.getData("comp_korea");

		//���ǿ����� ����ϱ�
		if(sts.equals(comp_no)) { msg = "�̹� ��ϵ� �����Դϴ�."; }
		else if(kname.equals(comp_korea) && comp_english.equals("����")) { msg = "��ü���� �����մϴ�."; }
		else {
			query = "insert into psm_category(pid,korea_name,english_name,key_word,comp_no,comp_korea,";
			query += "comp_english) values('";
			query += anbdt.getID()+"','"+korea_name+"','"+english_name+"','"+key_word+"','";
			query += comp_no+"','"+comp_korea+"','"+comp_english+"')";
			bean.execute(query);
		}
	}
	/*********************************************************************
	 	�����ϱ�
	*********************************************************************/
	if(mode.equals("MOD")) {
		query = "update psm_category set korea_name='"+korea_name+"',english_name='"+english_name+"',";
		query += "key_word='"+key_word+"',comp_no='"+comp_no+"',comp_korea='"+comp_korea+"',";
		query += "comp_english='"+comp_english+"' where pid='"+pid+"'";
		bean.execute(query);

		pid=korea_name=english_name=key_word=comp_no=comp_korea="";
		comp_english="����";
	}
	/*********************************************************************
	 	�����ϱ�
	*********************************************************************/
	if(mode.equals("DEL")) {
		query = "delete from psm_category where pid='"+pid+"'";
		bean.execute(query);

		pid=korea_name=english_name=key_word=comp_no=comp_korea="";
		comp_english="����";
	}
	/*********************************************************************
	 	���뺸��
	*********************************************************************/
	if(mode.equals("VIEW")) {
		query = "select * from psm_category where pid='"+pid+"'";
		bean.executeQuery(query);
		if(bean.next()) {
			pid = bean.getData("pid");
			korea_name = bean.getData("korea_name");
			english_name = bean.getData("english_name");
			key_word = bean.getData("key_word");
			comp_no = bean.getData("comp_no");
			comp_korea = bean.getData("comp_korea");
			comp_english = bean.getData("comp_english");
		}
	}
	
	/*********************************************************************
	 	CATEGORY LIST��������
	*********************************************************************/
	query = "SELECT count(*) FROM psm_category";
	bean.executeQuery(query);
	bean.next();
	int cnt = Integer.parseInt(bean.getData(1));
	String[][] data = new String[cnt][7];

	query = "SELECT * FROM psm_category order by comp_korea asc";
	bean.executeQuery(query);
	int n=0;
	while(bean.next()) {
		data[n][0] = bean.getData("pid");
		data[n][1] = bean.getData("korea_name");
		data[n][2] = bean.getData("english_name");
		data[n][3] = bean.getData("key_word");
		data[n][4] = bean.getData("comp_no");
		data[n][5] = bean.getData("comp_korea");
		data[n][6] = bean.getData("comp_english");
		n++;
	} //while

%>
<HTML>
<HEAD><TITLE>CATEGORY ����</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
</HEAD>

<BODY topmargin="0" link="darkblue" alink="blue" vlink="blue" leftmargin="0" oncontextmenu="return false">
<form name="sForm" method="post" style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--Ÿ��Ʋ-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../images/blet.gif"> ī�װ�����</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=27><!--��ư-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%'>
					<a href="javascript:write();"><img src="../images/bt_reg.gif" border=0></a>
					<a href="javascript:contentModify();"><img src="../images/bt_modify.gif" border=0></a>
					<a href="javascript:contentDelete();"><img src="../images/bt_del.gif" border=0></a>
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='2' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr><!--�⺻���� -->
				<td height="25" colspan="4"><img src="../images/basic_info.gif" width="209" height="25" border="0"></td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">ī�װ��ѱ۸�</td>
			   <td width="37%" height="25" class="bg_04">
					<input type="text" name="korea_name" maxlength='50' value="<%=korea_name%>" size="20"></td>
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��ü(�ѱ�)��</td>
			   <td width="37%" height="25" class="bg_04">
					<input type="text" name="comp_korea" value="<%=comp_korea%>" size="20" onclick="javascript:checkEdit();">
					<a href="javascript:selectComp();"><img src="../images/bt_search.gif" border="0" align='absmiddle'></a></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">ī�װ�������</td>
			   <td width="37%" height="25" class="bg_04">
					<input type="text" name="english_name" maxlength='50' value="<%=english_name%>" size="20"></td>
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��ü(����)��</td>
			   <td width="37%" height="25" class="bg_04">
					<input type="text" name="comp_english" value="<%=comp_english%>" size="30" readonly></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">�������(4��)</td>
			   <td width="37%" height="25" class="bg_04">
					<input type="text" name="key_word" maxlength='4' value="<%=key_word%>" size="20"></td>
			   <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">��ü����ڹ�ȣ</td>
			   <td width="37%" height="25" class="bg_04">
					<input type="text" name="comp_no" value="<%=comp_no%>" size="30" onclick="javascript:checkEdit();"></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
	<TR><TD height='5'></TD></TR>
</table>
<input type="hidden" name="mode" value=''>
<input type="hidden" name="pid" value='<%=pid%>'>
</form> 

<!-- ����Ʈ ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign='top'>
    <TBODY>
		<TR bgColor=#9DA9B9 height=2><TD colspan=19></TD></TR>
		<TR vAlign=middle height=25>
			<TD noWrap width=20 align=middle class='list_title'>#</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=120 align=middle class='list_title'>ī�װ��ѱ۸�</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=120 align=middle class='list_title'>ī�װ�������</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=80 align=middle class='list_title'>ī�װ����</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=150 align=middle class='list_title'>��ü�ѱ۸�</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=150 align=middle class='list_title'>��ü������</TD>
			<TD noWrap width=6 class='list_title'><IMG src="../images/list_tep.gif"></TD>
			<TD noWrap width=100% align=middle class='list_title'>��ü����ڹ�ȣ</TD>
		</TR>
		<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
	<% if (cnt == 0) { %>
		<TR vAlign=center height=22>
			 <td colspan='15' align="middle">***** ������ �����ϴ�. ****</td>
		</tr> 
	<% } %>	

	<% 
		for(int i=0; i<data.length; i++) {
	%>	
		<form name="vForm" method="post" style="margin:0">
		<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			<TD align=middle height="24" class='list_bg'><%=i+1%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=left height="24" class='list_bg'><a href="javascript:contentView('<%=data[i][0]%>');"><%=data[i][1]%></a></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=left height="24" class='list_bg'>&nbsp;&nbsp;<%=data[i][2]%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=left height="24" class='list_bg'>&nbsp;&nbsp;<%=data[i][3]%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=left height="24" class='list_bg'>&nbsp;&nbsp;<%=data[i][5]%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=left height="24" class='list_bg'>&nbsp;&nbsp;<%=data[i][6]%></TD>
			<TD><IMG height=1 width=1></TD>
			<TD align=left height="24" class='list_bg'>&nbsp;&nbsp;<%=data[i][4]%></TD>
		</TR>
		<TR><TD colSpan=15 background="../images/dot_line.gif"></TD></TR>
	<% 
		}  //for

	%>
	</TBODY>
</TABLE>

<input type="hidden" name="mode" value=''>
<input type="hidden" name="pid" value=''>
</form>
</body>
</html>

<script language=javascript>
<!--
var msg = '<%=msg%>';
if(msg.length != 0) {alert(msg); }
//����ϱ�
function write()
{
	var f = document.sForm;

	var korea_name = f.korea_name.value;
	if(korea_name == '') { alert('ī�װ� �ѱ۸��� �Է��Ͻʽÿ�.'); f.korea_name.focus(); return; }
	var english_name = f.english_name.value;
	if(english_name == '') { alert('ī�װ� �������� �Է��Ͻʽÿ�.'); f.english_name.focus(); return; }
	var key_word = f.key_word.value;
	if(key_word == '') { alert('ī�װ� ���������� �Է��Ͻʽÿ�.'); f.key_word.focus(); return; }
	var comp_korea = f.comp_korea.value;
	if(comp_korea == '') { alert('��ü������ �Է��Ͻʽÿ�.'); return; }
	var comp_no = f.comp_no.value;
	if(comp_no == '') { alert('��ü������ �Է��Ͻʽÿ�.'); return; }

	var pid = f.pid.value;
	if(pid != '') { alert('������ư�� �̿��Ͻʽÿ�.'); return; }

	document.sForm.action='categoryMgr.jsp';
	document.sForm.mode.value='ADD';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//��������ϱ�
function contentModify()
{
	var f = document.sForm;

	var korea_name = f.korea_name.value;
	if(korea_name == '') { alert('ī�װ� �ѱ۸��� �Է��Ͻʽÿ�.'); f.korea_name.focus(); return; }
	var english_name = f.english_name.value;
	if(english_name == '') { alert('ī�װ� �������� �Է��Ͻʽÿ�.'); f.english_name.focus(); return; }
	var key_word = f.key_word.value;
	if(key_word == '') { alert('ī�װ� ���������� �Է��Ͻʽÿ�.'); f.key_word.focus(); return; }
	var comp_korea = f.comp_korea.value;
	if(comp_korea == '') { alert('��ü������ �Է��Ͻʽÿ�.'); return; }
	var comp_no = f.comp_no.value;
	if(comp_no == '') { alert('��ü������ �Է��Ͻʽÿ�.'); return; }

	var pid = f.pid.value;
	if(pid == '') { alert('��Ϲ�ư�� �̿��Ͻʽÿ�.'); return; }

	document.sForm.action='categoryMgr.jsp';
	document.sForm.mode.value='MOD';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//��������ϱ�
function contentDelete()
{
	d = confirm("�����Ͻðڽ��ϱ�?");
	if(d == false) return;

	document.sForm.action='categoryMgr.jsp';
	document.sForm.mode.value='DEL';
	document.onmousedown=dbclick;
	document.sForm.submit();
}

//���뺸��
function contentView(pid)
{
	document.vForm.action='categoryMgr.jsp';
	document.vForm.mode.value='VIEW';
	document.vForm.pid.value=pid;
	document.onmousedown=dbclick;
	document.vForm.submit();
}
//��ü���� ã��
function selectComp()
{
	var strUrl = "../searchCompany.jsp?target=sForm.comp_no/sForm.comp_korea/sForm.comp_english";
	newWIndow = wopen(strUrl, "PsmCompInot", "260", "380", "scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//���� �������� (�����ΰ�츸 ��������)
function checkEdit()
{
	var chk = document.sForm.comp_english.value;
	if(chk != "����") document.sForm.comp_english.focus();
}
//â
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
//������ ó���� ��ư����
function dbclick()
{
	if(event.button==1) alert("���� �۾��� ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.");
}
-->
</script>