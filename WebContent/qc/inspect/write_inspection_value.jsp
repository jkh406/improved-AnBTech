<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.util.*"
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%
	String mode				= request.getParameter("mode");
	String request_no		= request.getParameter("request_no");		//�˻��Ƿڹ�ȣ
	String item_code		= request.getParameter("item_code");		//�˻���ǰ���ڵ�
	String inspection_code	= request.getParameter("inspection_code");	//�˻��׸��ڵ�
	String sampled_quantity	= request.getParameter("sampled_quantity");	//�÷����

	//����Ʈ
	ArrayList inspection_value_list = new ArrayList();
	inspection_value_list = (ArrayList)request.getAttribute("INSPECTION_VALUE_LIST");
	Iterator table_iter = inspection_value_list.iterator();
%>

<HTML><HEAD><TITLE>�˻������</TITLE></HEAD>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onload='display();'>

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
		<TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
			<TD vAlign=top>
			<TABLE cellSpacing=0 cellPadding=5 width="100%" border=0>
				<TBODY>
					<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
					<TR><TD valign='middle' height='32' bgcolor="#73AEEF"><img src="../qc/images/pop_reg_chk_result.gif" hspace="10" alt="�˻������"></TD></TR>
					<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
					</TD></TR>
		<TR bgColor='' height=19><TD></TD></TR>
  
	<!--����Ʈ-->
	<TR height=100%>
		<TD vAlign=top>
		<form method=post name="writeForm" action='QualityCtrlServlet' enctype='multipart/form-data' style="margin:0">
			<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:100%; overflow-x:auto; overflow-y:auto;">	
		    <TABLE cellSpacing=0 cellPadding=0 width="98%" border=0 align=center>
			    <TBODY>
					<TR bgColor=#9DA9B9 height=1><TD colspan=5></TD></TR>
					<TR vAlign=middle height=23>
						  <TD noWrap width=40 align=middle class='list_title'>��ȣ</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
						  <TD noWrap width=100% align=middle class='list_title'>����ġ</TD>
						  <TD noWrap width=6 class='list_title'><IMG src="../qc/images/list_tep.gif"></TD>
						  <TD noWrap width=120 align=middle class='list_title'>����</TD>
					<TR bgColor=#9DA9B9 height=1><TD colspan=5></TD></TR>
<%
			int no = 1;
			String[] values = new String[2];
			while(table_iter.hasNext()){
				values = (String[])table_iter.next();
				String inspection_value = values[0];
				String is_passed = values[1];
%>
					<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
					  <TD align=middle height="24" class='list_bg'><%=no%></td>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><input type='text' size='15' maxlength='15' name='inspection_value_<%=no%>' value='<%=inspection_value%>'></td>
					  <TD><IMG height=1 width=1></TD>
					  <TD align=middle class='list_bg'><input type='radio' name='is_passed_<%=no%>' value='Y' <% if(is_passed.equals("Y")) out.print("checked"); %>>�հ� <input type='radio' name='is_passed_<%=no%>' value='N' <% if(is_passed.equals("N")) out.print("checked"); %>>���հ�</td>
					</TR>
					<TR><TD colSpan=5 background="../qc/images/dot_line.gif"></TD></TR>				
<%				no++;
	}	
%>	
				</TBODY></TABLE></DIV></TD></TR>
				<!--������-->
				<TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
					<IMG src='../qc/images/bt_all_ok.gif' onclick="javascript:pass_all('<%=sampled_quantity%>')" align='absmiddle' style='cursor:hand' alt="�ϰ��հ�">
					<IMG src='../qc/images/bt_all_no.gif' onclick="javascript:fail_all('<%=sampled_quantity%>')" align='absmiddle' style='cursor:hand' alt="�ϰ����հ�">
					<IMG src='../qc/images/bt_save.gif' onclick="javascript:checkForm('<%=sampled_quantity%>')" align='absmiddle' style='cursor:hand'>
					<IMG src='../qc/images/bt_cancel.gif' onClick='javascript:self.close();' align='absmiddle' style='cursor:hand'></TD></TR>
					<TR><TD width="100%" height=1 bgcolor="0C2C55"></TD></TR>
			</TD></TR></TABLE></TBODY></TABLE>
	<input type='hidden' name='mode' value='<%=mode%>'>
	<input type='hidden' name='request_no' value='<%=request_no%>'>
	<input type='hidden' name='item_code' value='<%=item_code%>'>
	<input type='hidden' name='inspection_code' value='<%=inspection_code%>'>
	<input type='hidden' name='inspected_quantity' value='<%=sampled_quantity%>'>
</form>
</BODY>
</HTML>

<script language='javascript'>
//�ʼ� �Է»��� üũ
function checkForm(no){ 
	var f = document.writeForm;

	for(var i=1; i<=no; i++){
		if(eval("f.inspection_value_" + i + ".value;") == ''){
			alert(i + "��° ǰ���� ����ġ�� �Է��Ͻʽÿ�.");
			eval("f.inspection_value_" + i + ".focus();");
			return;
		}

		if(eval("f.inspection_value_" + i + ".value;").indexOf("#") > 0 || eval("f.inspection_value_" + i + ".value;").indexOf("|") > 0){
			alert("����ġ���� Ư������(#,|)�� ������ �� �����ϴ�.");
			eval("f.inspection_value_" + i + ".focus();");
			return;
		}
	}
	f.submit();
}

//�ϰ��հ�ó��
function pass_all(no){ 
	var f = document.writeForm;
	var main_value = f.inspection_value_1.value;
	if(main_value == ''){
		alert("ù��° ǰ���� ����ġ�� �Է��Ͻ� �� �����Ͻʽÿ�.");
		f.inspection_value_1.focus();
		return;
	}

	for(var i=1; i<=no; i++){
		eval("f.inspection_value_" + i + ".value = '" + main_value + "'");
		eval("f.is_passed_" + i + "[0].checked = true");
	}	
}

//�ϰ����հ�ó��
function fail_all(no){ 
	var f = document.writeForm;
	var main_value = f.inspection_value_1.value;
	if(main_value == ''){
		alert("ù��° ǰ���� ����ġ�� �Է��Ͻ� �� �����Ͻʽÿ�.");
		f.inspection_value_1.focus();
		return;
	}

	for(var i=1; i<=no; i++){
		eval("f.inspection_value_" + i + ".value = '" + main_value + "'");
		eval("f.is_passed_" + i + "[1].checked = true");
	}	
}

//�ػ󵵸� ���ؼ� div�� ���̸� ����
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 585;
	item_list.style.height = div_h;
}
</script>