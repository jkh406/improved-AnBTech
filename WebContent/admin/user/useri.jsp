<%@ include file= "../checkAdmin.jsp"%>
<%@ include file= "../configHead.jsp"%>
<%@ page 
	language	= "java" 
	import		= "java.sql.*"
	contentType	= "text/HTML;charset=KSC5601"
	errorPage	= "../errorpage.jsp"
%>
<jsp:useBean id="recursion"  class="com.anbtech.admin.db.makeClassTree"/>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />


<%
/* ����� �߰� */

    bean.openConnection();	

	String query = "";
	String caption = "";
	String j		=	request.getParameter("j")==null?"a":request.getParameter("j");		// ���
	String ac_id	=	request.getParameter("ac_id");	// ����� ���Ե� ������ ID
	String au_id	=	request.getParameter("au_id");	// ��� ID
	String rank		=	"";
	String name		=	"";
	String id		=	"";
	String passwd	=	"";
	String email	=	"";
	String office_tel	=	"";
	String hand_tel	=	"";
	String fax		=	"";
	String main_job	=	"";
	String address	=	"";
	String post_no	=	"";
	String home_tel	=	"";
	String enter_day	=	"";
	String regi_date	=	"";
	String access_code  = "";
	String ac_code		= "";
	String code			= "";

	if( j.equals("u")){ // ����� ���� ����
		query = "select * from user_table where au_id = '"+au_id+"'";
		bean.executeQuery(query);
		
		while(bean.next()){
			ac_id		= bean.getData("ac_id");
			rank		= bean.getData("rank");
			name		= bean.getData("name");
			id			= bean.getData("id");
			passwd		= bean.getData("passwd");
			email		= bean.getData("email");
			office_tel	= bean.getData("office_tel");
			hand_tel	= bean.getData("hand_tel");
			fax			= bean.getData("fax");
			main_job	= bean.getData("main_job");
			address		= bean.getData("address");
			post_no		= bean.getData("post_no");
			home_tel	= bean.getData("home_tel");
			enter_day	= bean.getData("enter_day");
			regi_date	= bean.getData("regi_date");
			access_code = bean.getData("access_code");

		}
		caption		 = "����";
	}else if(j.equals("a")){ //����� �߰�
		caption = "���";
	}

%>
<HTML>
<head><title>����ڵ��</title></head>
<link rel="stylesheet" type="text/css" href="../css/style.css">
<BODY leftmargin=0 topmargin=0 oncontextmenu='return false'>
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title">&nbsp;<img src="../images/blet.gif" align="absmiddle"> ����ڵ��</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
				<a href="javascript:checkForm()"><IMG src='../images/bt_save.gif' align='absmiddle' border='0' style='cursor:hand'></a>
				<IMG src='../images/bt_cancel.gif' onclick='history.back()' align='absmiddle' border='0' style='cursor:hand'>
				</TD></TR></TBODY>
		</TABLE></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR></TBODY></TABLE>

<form name="frm1" method="get" action="userp.jsp" style="margin:0">
<!--����� ����-->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
  <TR>
	<TD height=22 colspan="4"><img src="../images/user_info.gif" width="209" height="25" border="0"></TD></TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">�Ҽ�</TD>
    <TD width="85%" height="25" class="bg_04" colspan='3'>
		<select name='ac_id' >
			<%=recursion.viewCombo(0,0)%>
		</select></TD>
<%		if(ac_id !=null && Integer.parseInt(ac_id) > 0){	%>
			<script language='javascript'>
				document.frm1.ac_id.value = '<%=ac_id%>';
			</script>
<%		}	%>	
	</TD>
   
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">����</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="text" name="name" size="10" value="<%=name%>" maxlength="25" class="text_01"></TD>
     <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">����</TD>
    <TD width="35%" height="25" class="bg_04" >
			<select name="rank" >
				<option value=''>����</option>
<%
			query = "select ar_name,ar_code from rank_table order by ar_priorty asc";
			bean.executeQuery(query);
			while(bean.next()){
%>
				<option value='<%=bean.getData("ar_code")%>'><%=bean.getData("ar_name")%></option>
<%		}	%>
		
				</select>
<%		if(rank !=null){	%>
			<script language='javascript'>
				document.frm1.rank.value='<%=rank%>';
			</script>
<%		}	%>	
	</TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">���</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="text" name="id" size="10" value="<%=id%>" maxlength="10" class="text_01"></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">��й�ȣ</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="password" name="passwd" size="15" value="<%=passwd%>" maxlength="15" class="text_01"> * ��Һ��� ����</TD>    
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">�ֿ����</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="text" name="main_job" size="25" value="<%=main_job%>" maxlength="25"></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">���ڿ���</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="text" name="email" size="30" value="<%=email%>" maxlength="30" class="text_01"></TD>    
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">�ٹ�ó ��ȭ��ȣ</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="text" name="office_tel" size="15" value="<%=office_tel%>" maxlength="15" class="text_01"></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">�ٹ�ó �ѽ���ȣ</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="text" name="fax" size="15" value="<%=fax%>" maxlength="15"></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
	<TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">�޴���ȭ��ȣ</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="text" name="hand_tel" size="15" value="<%=hand_tel%>" maxlength="15"></TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">������ȭ��ȣ</TD>
    <TD width="35%" height="25" class="bg_04"><input type="text" name="home_tel" size="15" value="<%=home_tel%>" maxlength="15"></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">���� �ּ�</TD>
    <TD width="85%" height="25" class="bg_04" colspan=3><input type="text" name="address" size="74" value="<%=address%>" maxlength="50"></TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
  <TR>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">�����ȣ</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="text" name="post_no" size="10" value="<%=post_no%>" maxlength="7"> * 467-850 ������ �Է�</TD>
    <TD width="15%" height="25" class="bg_03" background="../images/bg-01.gif">�Ի���</TD>
    <TD width="35%" height="25" class="bg_04" ><input type="text" name="enter_day" size="10" value="<%=enter_day%>" maxlength="8" class="text_01"> * �ݵ�� 20030327 ������ �Է�</TD>
  </TR>
  <TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR></TABLE>
<input type="hidden" name="j" value="<%=j%>">
<input type="hidden" name="au_id" value="<%=au_id%>">
<input type="hidden" name="authorityString" value="NNCCA">
<input type="hidden" name="passwd_change" value="n">
<input type="hidden" name="current_passwd" value="<%=passwd%>">
</BODY>
</HTML>


<script>
<!--
 
function checkForm()
{
	var f = document.frm1;

	if(f.ac_id.value == ""){
			alert("�Ҽ��� �����Ͻʽÿ�.");
			f.ac_id.focus();
			return;
	}
	if(f.rank.value == ""){
			alert("������ �����Ͻʽÿ�.");
			f.rank.focus();
			return;
	}
	if(f.name.value == ""){
			alert("�̸��� �Է��Ͻʽÿ�.");
			f.name.focus();
			return;
	}
	if(f.id.value == ""){
			alert("����� �Է��Ͻʽÿ�.");
			f.id.focus();
			return;
	}
	if(f.passwd.value == ""){
			alert("��й�ȣ�� �Է��Ͻʽÿ�.");
			f.passwd.focus();
			return;
	}
	if(f.office_tel.value == ""){
			alert("�ٹ�ó ��ȭ��ȣ�� �Է��Ͻʽÿ�.");
			f.office_tel.focus();
			return;
	}
	if(f.enter_day.value == ""){
			alert("�Ի����� �Է��Ͻʽÿ�.");
			f.enter_day.focus();
			return;
	}
	if(f.enter_day.value.length != 8){
			alert("��¥�� �߸� �Է��ϼ̽��ϴ�.");
			f.enter_day.focus();
			return;
	}
	if(!isNumber(f.enter_day)){
			alert("��¥�� ������ �ԷµǾ� �մϴ�.");
			f.enter_day.focus();
			return;
	}

	if(f.current_passwd.value != f.passwd.value){
		f.passwd_change.value = "y";
	}

	f.submit();
}

// �Է°��� Ư�� ����(chars)������ �Ǿ��ִ��� üũ
function containsCharsOnly(input,chars) {
	for (var inx = 0; inx < input.value.length; inx++) {
	   if (chars.indexOf(input.value.charAt(inx)) == -1)
		   return false;
	}
	return true;
}
		
// �Է°��� ���ڸ� �ִ��� üũ
function isNumber(input) {
	var chars = "0123456789";
	return containsCharsOnly(input,chars);
}
-->
</script>