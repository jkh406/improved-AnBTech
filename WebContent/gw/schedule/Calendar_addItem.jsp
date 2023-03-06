<%@ include file="../../admin/configPopUp.jsp"%>

<%@ page		
	info= "항목 저장하기"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="java.util.StringTokenizer"
%>
<%@	page import="com.anbtech.text.Hanguel" 								%>
<%@	page import="com.anbtech.text.StringProcess"		%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

<%
	String id = "";				//접속자 ID
	String items = "";			//개인일정항목
	String comm_items = "";		//공통일정항목
	String NY = "";				//신규등록 인지 편집인지
	
	/********************************************************************
		new 연산자 생성하기
	*********************************************************************/
	StringProcess str = new com.anbtech.text.StringProcess();				//문자,문자열에 관련된 연산자

	/*********************************************************************
	 	기안자 login 알아보기
	*********************************************************************/
	id = login_id;

	/*****************************************************
	// 공통 일정항목을 찾는다.
	*****************************************************/
	String[] commColumns = {"id","item","nlist"};
	String comm_data = "where item='CIT'";
	bean.setTable("CALENDAR_COMMON");
	bean.setColumns(commColumns);
	bean.setSearchWrite(comm_data);
	bean.init_write();
	
	comm_items = "";					//공통 항목LIST (구분자 : ';')
	while(bean.isAll()) {
		comm_items += bean.getData("nlist");
	}	

	/*********************************************************************
	 	항목추가/삭제 실행하기
	*********************************************************************/
	NY = Hanguel.toHanguel(request.getParameter("NY"));
	items = Hanguel.toHanguel(request.getParameter("Ritems"));
	String REQ = request.getParameter("req"); //요청사항
	if(REQ != null) {
		//삭제 요청시 처리하기
		if(REQ.equals("DEL")) {			
			String delD = Hanguel.toHanguel(request.getParameter("items"));	//삭제할 일정항목
			if(delD != null) {
				String upitems = str.repWord(items,delD+";","");
				String delData = "update calendar_common set nlist='" + upitems + "' where id='" + id + "' and item='IIT'";
				bean.execute(delData);
			} //if (del)

		//추가 요청시 처리하기
		} else if (REQ.equals("ADD")) { //추가
			String addD = Hanguel.toHanguel(request.getParameter("add_item"));	//추가할 일정항목
			if(addD != null) {
				String additems = "";	//추가할 일정항목
				String addData = "";	//query 문장
				//신규추가
				if(NY.equals("new")) {
					additems = addD;
					addData = "insert into calendar_common (pid,id,item,nlist) values('"+bean.getID()+"',";
					addData += "'"+id+"','IIT','"+additems+"')";
				//update
				} else {
					additems = items+addD;
					addData = "update calendar_common set nlist='" + additems + "' where id='" + id + "' and item='IIT'";
				}
				bean.execute(addData);
			}//if (add)
		} //if (del/add)
	} //if (전체)
	
	/*****************************************************
	//개인 일정항목 가져오기
	*****************************************************/
	String[] itemColumns = {"nlist"};
	String item_data = "where item='IIT' and id='" + id + "' order by nlist DESC"; 
	bean.setTable("CALENDAR_COMMON");
	bean.setColumns(itemColumns);
	bean.setSearchWrite(item_data);
	bean.init_write();
	
	items = "";							//개인 일정항목LIST (구분자 : ';')
	NY = "";							//신규 clear
	if(bean.isEmpty()) { 
		NY = "new";						//신규등록임
		items = "";						//내용이 없음
	} else {
		while(bean.isAll()) items += bean.getData("nlist");
	}	

%>
<HTML><HEAD><TITLE>일정항목 관리</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_sch_i.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody><form name="sForm" action="Calendar_addItem.jsp" method="post" style="margin:0">
         <tr><td height=20 colspan="4"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
         <tr>
           <td width="40%" height="25" class="bg_01" background="../images/bg-01.gif">
				<table border="0">
				<tr><td><b>일정항목명</b><br><input type="text" name="add_item" size="15"></td></tr>
				<tr><td height="30" align="center"><a href="javascript:addItem();"><img src='../images/bt_reg.gif' border='0' align='absmiddle'></a> <a href="javascript:delItem();"><img src='../images/bt_del.gif' border='0' align='absmiddle'></a></td></tr></table>
		   </td>
           <td width="60%" height="25" colspan="3" class="bg_02">
				<select name="items" size="5">
				<OPTGROUP label='----------------'>
				<% //저장된 개인일정항목 불러오기
					StringTokenizer strs = new StringTokenizer(items,";");
					while(strs.hasMoreTokens()) {
						String item=strs.nextToken();
						out.println("<option value='"+item+"'>" + item + "</option>");
					}
				%>
				</select>
				<input type="hidden" name="req">
				<input type='hidden' name='Ritems' value='<%=items%>'>
				<input type='hidden' name='NY' value='<%=NY%>'>
				</form></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=20 colspan="4"></td></tr></tbody></table>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8" style='padding-right:10px'><a href="javascript:closeItem();"><img src="../images/close.gif" border="0" align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>

<Script language = "Javascript">
 <!-- 
//항목 삭제하기 
function delItem()
{
	var num = document.sForm.items.selectedIndex;
	if(num < 0){
		alert("삭제할 항목을 선택해 주십시오.");
		return;
	}
	if(!confirm("삭제하시겠습니까?"))
		return;
	document.sForm.action="Calendar_addItem.jsp";
	document.sForm.req.value="DEL";
	document.sForm.items.options[num].value;
	document.sForm.submit();
}

//항목 추가하기 
function addItem()
{
	var data = document.sForm.add_item.value;
	var comm = '<%=comm_items%>';
	var indi = '<%=items%>';
	if(data == ""){
		alert("추가할 항목을 입력해 주십시오.");
		return;
	} else if(comm.indexOf(data) != -1){
		alert("공통 일정항목으로 이미등록된 내용입니다.");
		return;
	} else if(indi.indexOf(data) != -1){
		alert("개인 일정항목으로 이미등록된 내용입니다.");
		return;
	}
	document.sForm.action="Calendar_addItem.jsp";
	document.sForm.req.value="ADD";
	document.sForm.add_item.value=data+';';
	document.sForm.submit();
}

//닫기
function closeItem()
{
	opener.location.reload();
	self.close();
}
-->
</Script>