<%@ include file= "configHead.jsp"%>
<%@ page
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.util.*" 
	contentType	= "text/html;charset=KSC5601"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%!
	com.anbtech.ViewQueryBean bean = new com.anbtech.ViewQueryBean();

	// ���Ѽ��� ����
	public String savePrivilege(String code_s,String owner_list){
		try {
			bean.openConnection();

			String query = "update prg_privilege set ";
			query += "owner = '"+owner_list+"' ";
			query += "where code_s = '"+code_s+"'";

			bean.executeUpdate(query);
			return "���������� ����Ǿ����ϴ�.";
		}catch (Exception e){
			return "[����] ������� �ʾҽ��ϴ�.";
		}
	}
%>

<%
	String code_b	= request.getParameter("code_b")==null?"AP": Hanguel.toHanguel(request.getParameter("code_b"));
	String code_s	= request.getParameter("code_s");
	String name_b	= request.getParameter("name_b")==null?"���ڰ�����(AP)":Hanguel.toHanguel(request.getParameter("name_b"));
	String owner_list = Hanguel.toHanguel(request.getParameter("list"));
	
	
	if (code_s != null){
		String save_result = savePrivilege(code_s,owner_list);
%>
		<script language='javascript'>
			alert('<%=save_result%>');
		</script>

<%      
	}
%>



<html>
<head><title> ��⺰ ������ ����</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../admin/css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<form >
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title">&nbsp;<img src="../admin/images/blet.gif"> ��⺰ ������ ����</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300>
					<select name=ListModule onChange="javascript:changeModule();">
<%
						String query = "select distinct code_b,name_b from prg_privilege";
						bean.openConnection();
						bean.executeQuery(query);
						while(bean.next()){
%>
							<option value="<%=bean.getData("code_b")%>"><%=bean.getData("name_b")%></option>
<%	}
%>
					</select>&nbsp;&nbsp;��� ����� �����ϼ���.
				
			  </TD></TR></TBODY></TABLE></TD></TR>
	<script language='javascript'>
		document.forms[0].ListModule.value = '<%=code_b%>';
	</script>
			  <TR><TD height='1' bgcolor='#9CA9BA'></TD></TR></TABLE>

<!--����-->

<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr><td align="center">
    <!--�⺻����-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
	<!--  	<tr><td width="100%" height="25" colspan='2'><%=name_b%> ����</td></tr>
		<tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>-->
<%
	query = "select name_b,name_s,code_s,owner from prg_privilege where code_b like '"+code_b+"%'";
	bean.openConnection();
	bean.executeQuery(query);
	while(bean.next()){
		String module_name	= bean.getData("name_b");
		String prg_name		= bean.getData("name_s");
		String prg_code		= bean.getData("code_s");
		String list			= bean.getData("owner");

		StringTokenizer str = new StringTokenizer(list, ";");
		int owner_count = str.countTokens();
		String owner[] = new String[owner_count];

		for(int i=0; i<owner_count; i++){ 
			owner[i] = str.nextToken();
		}
%>
		<tr><td width="20%" height="25" class="bg_03" background="../admin/images/bg-01.gif"><%=prg_name%></td>
			<td width="80%" height="25" class="bg_04">
			<table border="0" cellpadding="0" cellspacing="0" width="100%">
		<tr><td width="20%" rowspan="3">
			<select size="5" name="<%=prg_code%>" multiple style="width:100">
			<%	for(int i=0; i<owner_count; i++){ %>
				  <option value="<%=owner[i]%>"><%=owner[i]%></option>
			<%	}	%>
			</select></td>
			<td width="80%">
			<input type="text" name="t_<%=prg_code%>" size="10"> <IMG src='../admin/images/bt_add.gif' border='0' align='absmiddle'  style='cursor:hand' onClick="addOwner('<%=prg_code%>');">
			</td></tr>
		<tr><td width="80%">
			<IMG src='../admin/images/bt_del_sel.gif' border='0' align='absmiddle'  style='cursor:hand'  onClick="dropOwner('<%=prg_code%>');"></td></tr>
		<tr><td width="80%">
			<IMG src='../admin/images/bt_save.gif' border='0' align='absmiddle'  style='cursor:hand'  onClick="saveOwner('<%=prg_code%>','<%=code_b%>');"> * �����ư�� Ŭ���Ͽ� �����ؾ� ��������� ����˴ϴ�.</td></tr>
	</table>
	 <tr bgcolor=c7c7c7><td height=1 colspan="2"></td></tr>
</td></tr>
<%	}%>
</table>
<input type=hidden name='now_code_s'>
</form>
</body>
</html>

<script language=javascript>
//var owner_list = "";

function openWin(url)
{
	window.open(url,'open','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

function changeModule(){ 
	var f = document.forms[0];
	var len = f.ListModule.length;
	var code_b;
	var name_b;
	for(i=0;i<len;i++){
		if(f.ListModule.options[i].selected){
		    code_b = f.ListModule.options[i].value;
			name_b = f.ListModule.options[i].text;
		}
	}
	location.href="settingPrivilege.jsp?code_b="+code_b+"&name_b="+name_b;
}

function addOwner(id)
{
	
	var new_owner = eval("document.forms[0].t_"+id + ".value");

	if(new_owner == ""){
		alert("�߰��� ����� ����� �Է��Ͻʽÿ�.");
		return;
	}

	var len = eval("document.forms[0]." + id + ".length")
	for(var i = 0; i < len; i++){
		if(eval("document.forms[0]."+id + ".options["+i+"].value") == new_owner){
			alert(new_owner+"�� �̹� ��ϵ� ����Դϴ�.");
			return;
		}
	}

//	document.forms[0].CM01.options[len] = new Option(new_owner, new_owner);
//	eval("document.forms[0]." + id + ".options[" + len + "] = new Option("+new_owner+","+new_owner+")"); 

	eval("document.forms[0]." + id + ".options[" + len + "] = new Option(document.forms[0].t_"+id + ".value,document.forms[0].t_"+id + ".value)"); 

}

function dropOwner(id)
{
    var len = eval("document.forms[0]." + id + ".length")

	for (i=len-1;i>=0 ;i--)
    {
        if(eval("document.forms[0]."+id+".options["+i+"].selected == true"))
                eval("document.forms[0]."+id+".options["+i+"] = null;");
    }
}

function saveOwner(id,cb)
{
	var len = eval("document.forms[0]." + id + ".length")
	var owner_list = "";
	for(var i = 0; i < len; i++){
		owner_list += eval("document.forms[0]."+id + ".options["+i+"].value") + ";";
	}
//alert("id"+id+"nb"+nb+"cb"+cb);
	location.href="settingPrivilege.jsp?code_s="+id+"&list="+owner_list+"&code_b="+cb;
}

</script>