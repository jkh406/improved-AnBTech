<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkPU01.jsp"%>
<%@ page
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.util.*"
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%!
	com.anbtech.ViewQueryBean bean = new com.anbtech.ViewQueryBean();

	String query = "";
	// 권한설정 저장
	public String savePrivilege(String code_s,String owner_list){
		try {
			bean.openConnection();

			query  = "update prg_privilege set ";
			query += "owner = '"+owner_list+"' ";
			query += "where code_s = '"+code_s+"'";

			bean.executeUpdate(query);
			return "정상적으로 저장되었습니다.";
		}catch (Exception e){
			return "[에러] 저장되지 않았습니다.";
		}
	}
%>

<%
	String code_b	= request.getParameter("code_b")==null?"AP": Hanguel.toHanguel(request.getParameter("code_b"));
	String code_s	= request.getParameter("code_s");
	String name_b	= request.getParameter("name_b")==null?"":Hanguel.toHanguel(request.getParameter("name_b"));
	String owner_list = Hanguel.toHanguel(request.getParameter("list"));
	
	if (code_s != null){
		String save_result = savePrivilege(code_s,owner_list);
%>
		<script language='javascript'>
			alert('<%=save_result%>');
		</script>
<% }
%>

<HTML>
<HEAD><TITLE></TITLE>
<META http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<LINK rel="stylesheet" href="../../admin/css/style.css" type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0">
<FORM>

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="5"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title">&nbsp;<img src="../../admin/images/blet.gif"> 관리자설정</TD></TR></TBODY></TABLE></TD></TR></TABLE>

<%
		query = "select name_b,name_s,code_s,owner,description from prg_privilege where code_b like 'PU%'";
		bean.openConnection();
		bean.executeQuery(query);
		
		while(bean.next()){
			String module_name	= bean.getData("name_b");
			String prg_name		= bean.getData("name_s");
			String prg_code		= bean.getData("code_s");
			String list			= bean.getData("owner");
			String description  = bean.getData("description");
			String name_s		= bean.getData("name_s");
			if(description==null) { description="";}
			StringTokenizer str = new StringTokenizer(list, ";");
			int owner_count = str.countTokens();
			String owner[] = new String[owner_count];

			for(int i=0; i<owner_count; i++){ 
				owner[i] = str.nextToken();
			}
%>


<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD>
		<!-- 테두리 -->		
		<TABLE cellSpacing=0 cellPadding=0 width="100%" height="100%" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA">
		<TR><TD colspan='5'>
		
		<!--내용-->
		<TABLE cellspacing=0 cellpadding=0 width="100%" border=0>
			<TBODY>
			<TR height='25px'>
				<TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;"  BACKGROUND='../images/title_bm_bg.gif' colspan='5'><%=name_s%></TD></TR>
			<TR><TD height='1' bgcolor='#9CA9BA' colspan='5'></TD></TR>
			
			<TR height='25px'>
				<TD vAlign=center colspan='5' style='padding-left:5px;'>
					<IMG src='../../admin/images/bt_save.gif' border='0' align='absmiddle'  style='cursor:hand'  onClick="saveOwner('<%=prg_code%>','<%=code_b%>');">
					<IMG src='../../admin/images/bt_cancel.gif' border='0' align='absmiddle'  style='cursor:hand'  onClick="javascript:history.back();"></TD></TR>
			<TR><TD height='1' bgcolor='#9CA9BA' colspan='5'></TD></TR>

			<TR><TD align='middle' background="../../admin/images/bg-01.gif" width='80' style='COLOR:#4D91DC;FONT WEIGHT : bolder;font-size: 9pt;'>권한자</TD>
				<TD style='padding-left:5px;' width='105'>
			
					<select size="5" name="<%=prg_code%>" multiple style="width:100">
			<%
					for(int i=0; i<owner_count; i++)	{ 
			%>		<option value="<%=owner[i]%>"><%=owner[i]%></option>
			<%		}
			%>
				</select>
				</TD>
				<TD style='padding-left:5px;' width='150' valign='bottom'><INPUT type="text" name="t_<%=prg_code%>" size="13"><br>
						<IMG src='../../admin/images/bt_add.gif' border='0' align='absmiddle'  style='cursor:hand' onClick="addOwner('<%=prg_code%>');">
						<IMG src='../../admin/images/bt_del.gif' border='0' align='absmiddle'  style='cursor:hand'  onClick="dropOwner('<%=prg_code%>');">
				</TD>
				<TD align='middle' background="../../admin/images/bg-01.gif" width='80' style='COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;'>역 할</TD>
				<TD style='padding-left:5px;font-size: 9pt;'><%=description%></TD></Tr>
				</TD></TR></TBODY></TABLE><!--내용끝-->	
		</TD></TR></TABLE><!--테두리 끝-->	
	<TR><TD height='5'></TD></TR>
</TBODY></TABLE>

<%	}
%>
<INPUT type=hidden name='now_code_s'>
</FORM>
</BODY>
</HTML>

<SCRIPT language=javascript>
<!--

function openWin(url){
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
		alert("추가할 사원의 사번을 입력하십시오.");
		return;
	}

	var len = eval("document.forms[0]." + id + ".length")
	for(var i = 0; i < len; i++){
		if(eval("document.forms[0]."+id + ".options["+i+"].value") == new_owner){
			alert(new_owner+"는 이미 등록된 사번입니다.");
			return;
		}
	}

//	document.forms[0].BR01.options[len] = new Option(new_owner, new_owner);
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