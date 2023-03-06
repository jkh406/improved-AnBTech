<%@ include file="../../admin/configHead.jsp"%>
<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage	= "../../admin/errorpage.jsp" 
%>
<%@ page import="java.util.*,com.anbtech.text.Hanguel"%>

<%	
	com.anbtech.am.entity.AsCategoryTable asCategoryTable;

	String div = request.getParameter("div");
	String readonly="";
	String mode="";
	String caption="";
	String c_no = "";			
	String ct_id = "";		
	String ct_level = "";	
	String ct_parent = "";	
	String ct_word = "";		
	String ct_name = "";    
	String dc_percent = ""; 
	String apply_dc = "";
	
	if(div.equals("f")){
		
		caption ="대분류추가";
	
	} else if(div.equals("a")) {
	
		caption ="분류추가";
		asCategoryTable = new com.anbtech.am.entity.AsCategoryTable();

		asCategoryTable=(com.anbtech.am.entity.AsCategoryTable)request.getAttribute("asCtTableM");
		c_no = ""+asCategoryTable.getCno();
		ct_id = asCategoryTable.getCtId();
		ct_level = ""+Integer.parseInt(asCategoryTable.getCtLevel());
		ct_parent = asCategoryTable.getCtParent();
	
	} else if(div.equals("m") || div.equals("d")){
		
		caption ="분류수정";
		
			if(div.equals("d")) { 
				 caption = "분류삭제";
				 readonly = "readonly";
			}
		
		asCategoryTable = new com.anbtech.am.entity.AsCategoryTable();
	   
		asCategoryTable=(com.anbtech.am.entity.AsCategoryTable)request.getAttribute("asCtTableM");
		c_no = ""+asCategoryTable.getCno();
		ct_id = asCategoryTable.getCtId();
		ct_level = ""+Integer.parseInt(asCategoryTable.getCtLevel());
		ct_parent = asCategoryTable.getCtParent();
		ct_word = asCategoryTable.getCtWord();		
		ct_name = asCategoryTable.getCtName();    
		dc_percent = asCategoryTable.getDcPercent(); 
		apply_dc = asCategoryTable.getApplyDc();
	}

%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../am/css/style.css" type="text/css">
</head>

<form name="frm1" method="post"  action='../servlet/AssetServlet' enctype='multipart/form-data' style="margin:0">
<BODY topmargin="0" leftmargin="0">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../am/images/blet.gif"> <%=caption%></TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
			  <% 
					if(div.equals("f") || div.equals("a")) { 
			  %>		<IMG src='../am/images/bt_save.gif' onclick="javascript:input_category()" style='cursor:hand' align='middle'>
						<IMG src='../am/images/bt_cancel.gif' onclick="javascript:history.go(-1)" style='cursor:hand' align='middle'>

			  <% }  else if(div.equals("m")) {
			  %>		<IMG src='../am/images/bt_save.gif'  onclick="javascript:input_category()" style='cursor:hand' align='middle'>
						<IMG src='../am/images/bt_cancel.gif'  onclick="javascript:history.back()" style='cursor:hand' align='middle'>

			  <% }  else if(div.equals("d")) {
			  %>        <IMG src='../am/images/bt_del.gif'  onclick="javascript:delete_ct()" style='cursor:hand' align='middle'>
						<IMG src='../am/images/bt_cancel.gif'  onclick="javascript:history.go(-1)" style='cursor:hand' align='middle'>
			  <% } else if(div.equals("v")) {
			  %>        <IMG src='../am/images/bt_cancel.gif'  onclick="javascript:history.go(-1)" style='cursor:hand' align='middle'>
			  <% } 
			  %>
			</TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--기본정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">분류명</td>
           <td width="37%" height="25" class="bg_04"><input type='text' name='ct_name' value='<%=ct_name%>' <%=readonly%>></td>
           <td width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">코드명</td>
           <td width="37%" height="25" class="bg_04"><input type='text' size=8 maxlength=8 name='ct_word' value='<%=ct_word%>' <%=readonly%>></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">감가적용여부</td>
           <td width="37%" height="25" class="bg_04">
				<SELECT name="apply_dc">
					<OPTION value="y" selected>적용</OPTION>
					<OPTION value="n">미적용</OPTION>
			
					<script language="javascript">
						document.frm1.apply_dc.value = '<%=apply_dc%>';
					</script>
				</SELECT>
			</td>
           <td width="13%" height="25" class="bg_03" background="../am/images/bg-01.gif">자산 감가비율</td>
           <td width="37%" height="25" class="bg_04"><input type='text' name='dc_percent'  size=5 value='<%=dc_percent%>' <%=readonly%>>&nbsp;%</td></tr>
		   <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>

		    <input type=hidden name='div' value='<%=div%>'>
			<input type=hidden name='c_no' value='<%=c_no%>'>
			<input type=hidden name='ct_id' value='<%=ct_id%>'>
			<input type=hidden name='ct_level' value='<%=ct_level%>'>
			<input type=hidden name='ct_parent' value='<%=ct_parent%>'>
			<input type=hidden name='mode' value='category_manage'>

</form>
</td></tr></table>
</body>
</html>

<script language="javascript">

function input_category(){
		
		var f=document.frm1;
	
		if(f.ct_name.value=="")
		{
			alert("분류 이름을 기입하십시요.")
			return
		}

		if(f.ct_word.value==""){
			alert("분류 코드를 기입하십시요.");
			return;
		}

		if(f.apply_dc.value == "y") {
			
			if(f.dc_percent.value==""){
			alert("분류 감가비율을 기입하십시요.");
			return;
			}
		} 

		document.frm1.submit(); 
}

// 분류 삭제
function delete_ct(){

		var f=document.frm1
	
		var ct_id = f.ct_id.value;
		var c_no = f.c_no.value;

		if(confirm('삭제 하시겠습니까?')) { 
			location.href="../servlet/AssetServlet?mode=category_manage&div=d&ct_id="+ct_id+"&c_no="+c_no;
		} else {
		  return false;
		}
}

// 
function setting(div){
		var f=document.frm1;
			
		var apply=f.apply_dc.value;
		if(div=="m" || div=="a") {
		location.href="../servlet/AssetServlet?mode=category_info_view&div="+div+"&ct_id="+ct_id+"&c_no="+c_no+"&apply_dc="+apply_dc;
		} else {
		location.href="../servlet/AssetServlet?mode=category_info_view&div="+div+"&apply_dc="+apply;
		}

}

function go_list(){
		location.href="../servlet/AssetServlet?mode=category_list";
}
</script>