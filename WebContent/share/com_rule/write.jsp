<%@ include file= "../../admin/configHead.jsp"%>
<%@ page language="java" 
	contentType="text/html;charset=euc-kr" 
	errorPage="../../adimn/errorpage.jsp"
%>
<%@ page import="java.util.*,com.anbtech.share.entity.*"%>
<%@ page import="com.oreilly.servlet.MultipartRequest"%>
<%@	page import="com.anbtech.admin.SessionLib,com.anbtech.share.entity.*"%>

<%	
	int enableupload	= 5;		// 업로드 개수 지정
	ArrayList arry = new ArrayList();
	
	com.anbtech.share.entity.ShareParameterTable sbpara = new com.anbtech.share.entity.ShareParameterTable();

	sbpara   = (ShareParameterTable)request.getAttribute("sbParameter");
	String category		= sbpara.getCategory();
	String tablename	= sbpara.getTableName();
	String mode			= sbpara.getMode();
	String id			= sbpara.getId();
	String name			= sbpara.getName();
	String categorycombo= sbpara.getCategoryCombo();

	com.anbtech.share.entity.ShareBdTable sbTable = new com.anbtech.share.entity.ShareBdTable();
    sbTable = (ShareBdTable)request.getAttribute("shareBdTable");

	if(category.equals("")) category = sbTable.getCategory();

	page = "1";
	// 수정처리일 경우에 페이지 번호를 넘겨준다.
	if("modify".equals(mode)){
		com.anbtech.share.entity.ShareLinkTable redirect = new com.anbtech.share.entity.ShareLinkTable();;
		redirect = (ShareLinkTable)request.getAttribute("Redirect");
		page = redirect.getViewBoardpage();
	}
%>

<SCRIPT language='JavaScript'>
<%
		int i = 1;
		while(i < enableupload){
			if(i == enableupload-1){
%>
			function fileadd_action<%=i%>() {
				id<%=i%>.innerHTML="<br><INPUT type=file name=attachfile<%=i+1%> size=45 >";
			}
<%			break;
			}
%>
			function fileadd_action<%=i%>() {
				id<%=i%>.innerHTML="<br><INPUT type=file name=attachfile<%=i+1%>  onClick='fileadd_action<%=i+1%>()' size=45 ><FONT id=id<%=i+1%>></FONT>";
			}
<%			i++;
		}
%>

</SCRIPT>
<%

	String file_stat = "";
	if("delete".equals(mode) ||"view".equals(mode)||"modify".equals(mode)) {
	com.anbtech.share.entity.ShareBdTable file = new com.anbtech.share.entity.ShareBdTable();

	ArrayList file_list = new ArrayList();
	file_list = (ArrayList)request.getAttribute("sharefile");
	Iterator file_iter = file_list.iterator();

	i = 1;
	
	while(file_iter.hasNext()){
		file = (ShareBdTable)file_iter.next();
		file_stat = file_stat + "<INPUT class=kissofgod-input type=file name='attachfile"+i+"' size=45> " + file.getFname()+" 삭제! <INPUT type=checkbox name = 'deletefile"+i+"' value='delete'><br>";
		i++;
		}

	} else {
		i=1;
	}
%>

<SCRIPT language='javascript'>
	function checkValue(){
	var myindex=document.writeForm.category[1].selectedIndex;
	if(myindex<1){
		alert('분야를 선택하십시오.');
		return;
	}
	
	if(!document.writeForm.subject.value){
		alert('사규명을 입력하십시오.');
		document.writeForm.subject.focus();
	}else if(!document.writeForm.content.value){
		alert('첨부파일에 대한 설명을 입력하십시오.');
		document.writeForm.content.focus();
	}else{
		document.writeForm.submit();
	}
}

</SCRIPT>

<HTML>
<HEAD><TITLE></TITLE>
<META http-equiv='Content-Type' content='text/HTML; charset=EUC-KR'>
<LINK rel="stylesheet" href="../share/css/style.css" type="text/css">
</HEAD>

<BODY topmargin="0" leftmargin="0">
<FORM method=post name="writeForm" action='../servlet/ShareBdServlet?tablename=<%=tablename%>' enctype='multipart/form-data' style='margin:0'>
	<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
		<TBODY><!-- 제목 -->
			<TR><TD valign='top' height="27">
				<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
					<TBODY>
						<TR bgcolor="#4A91E1"><TD height="2"></TD></TR>
						<TR bgcolor="#BAC2CD">
							<TD valign='middle' class="title"><img src="../share/images/blet.gif" align="absmiddle"> 사규등록</TD></TR>
						<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR></TBODY></TABLE></TD></TR>
			<TR><TD valign='top' height="32"><!-- 버튼 -->
				<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
					<TBODY>
						<TR><TD width=4>&nbsp;</TD>
							<TD align=left width='500'>
							<A href="javascript:checkValue();"><img src="../share/images/bt_save.gif" border="0" align="absmiddle"></a> 
							<A href="javascript:history.go(-1);"><img src="../share/images/bt_cancel.gif" border="0" align="absmiddle"></A></TD></TR></TBODY></TABLE></TD></TR>
			<!-- 내용  -->
			<TR><TD valign='top' height="100%">
				<INPUT TYPE='hidden' NAME='mode' value='<%=mode%>'>
				<INPUT TYPE='hidden' NAME='page' value='<%=page%>'>
			
			<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
				<TR><TD align="center">
			<!--등록정보-->
	<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
		<TBODY>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">사규명</TD>
			<TD width="87%" height="25" class="bg_04" colspan="3"><INPUT type=text name='subject' size=40 value="<%=sbTable.getSubject()%>" class="text_01"></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif"> 사규번호</TD>
				<TD width="37%" height="25" class="bg_04" ><INPUT type='text' name='doc_no' value=<%=sbTable.getDocNo()%>></TD>
				<TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">버젼</TD>
				<TD width="37%" height="25" class="bg_04" ><INPUT type='text' name='ver' size='5' maxlength='5' value=<%=sbTable.getVer()%>></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">분야</TD>
				<TD width="37%" height="25" class="bg_04" >
					<%=categorycombo%>
					<%if(!sbTable.getCategory().equals("") && sbTable.getCategory() != null) {%>
						<script language='javascript'>
							document.writeForm.category.value = '<%=sbTable.getCategory()%>';
						</script>
					<%}%>
				</TD>
			    <TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">주관부서</TD>
				<TD width="37%" height="25" class="bg_04" ><INPUT type='text' name='ac_name' value=<%=sbTable.getAcName()%>></TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">첨부파일설명</TD>
				<TD width="87%" height="25" class="bg_04" colspan="3"><TEXTAREA name=content ROWS=11 COLS=78 class="text_01"><%=sbTable.getContent()%></TEXTAREA></TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">첨부파일</TD>
				<TD width="87%" height="25" class="bg_04" colspan="3">
		   		<%
					if (enableupload > 0){
				%>
		            <%=file_stat%>
				<%
						if(i < enableupload){
				%>
				    <INPUT type='file' name='attachfile<%=i%>' onClick='fileadd_action<%=i%>()' size=45 > 
				            <font id=id<%=i%>></font>
				<%
						}else if(i == enableupload){
				%>
				            <INPUT type='file' name='attachfile<%=i%>' size=45 >
				            <font id='id<%=i%>'></font>
				<%
						}
					}
				%>			
				</TD></TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR>
		<%
				if ("write".equals(mode)) {
		%>		<TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">등록자</TD>
				<TD width="37%" height="25" class="bg_04" ><%=sbTable.getWid()%>/<%=sbTable.getWname()%></TD>
					<INPUT type=hidden name='wid' size=15 maxlength=10 value="<%=id%>">
					<INPUT type=hidden name='wname' size=10 maxlength=10 value="<%=name%>"></TD>

		<%		} else if("modify".equals(mode)){
		%>		<TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">등록자</TD>
				<TD width="37%" height="25" class="bg_04" ><%=sbTable.getWid()%>/<%=sbTable.getWname()%></TD>
				<TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">등록일자</TD>
				<TD width="37%" height="25" class="bg_04" ><%=sbTable.getWdate()%></TD>
			</TR>
			<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR>
			<TR>
				<TD width="13%" height="25" class="bg_03" background="../share/images/bg-01.gif">수정자</TD>
				<TD width="37%" height="25" class="bg_04" ><%=id%>/<%=name%></TD>
					<INPUT type=hidden name='wid' size=15 maxlength=10 value="<%=id%>">
					<INPUT type=hidden name='wname' size=10 maxlength=10 value="<%=name%>"></TD>
		<%		}
		%>	
			</TR>
		<INPUT TYPE='HIDDEN' NAME='no' VALUE='<%=sbTable.getNo()%>'>
		<TR bgcolor=c7c7c7><TD height=1 colspan="4"></TD></TR></TBODY></TABLE></FORM></TD></TR></TABLE>
</TD></TR>
</TBODY>
</TABLE>
</BODY></TABLE>