<%@ include file="../admin/configPopUp.jsp"%>
<%@ page language="java"
    contentType="text/html;charset=KSC5601" 
	errorPage = "../admin/errorpage.jsp" 
%>
<%@ page import="java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel"%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	String target = request.getParameter("target");
	String id = request.getParameter("id");	if(id == null) id = "";
	String tablename = request.getParameter("tablename"); if(tablename == null) tablename = "";
	String mode = Hanguel.toHanguel(request.getParameter("mode"));
	String query = "";
	/*********************************************************************
	 	사외공문 공유자 지정시 필요한 부서코드 찾기
	*********************************************************************/
	String ac_code = "";
	String[] idColumn = {"b.ac_code"};
	bean.setTable("user_table a,class_table b");			
	bean.setColumns(idColumn);
	query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id)";
	bean.setSearchWrite(query);
	bean.init_write();

	if(bean.isAll()) {
		ac_code = bean.getData("ac_code");				//해당자 부서코드
	} //while

	/*********************************************************************
	 	공지정보의 공유자 찾기
		mode : IDR_S, ODR_S [부서원으로 mail_add을 읽고]
		mode : ODR_G [부서명을 읽는다]
	*********************************************************************/	
	String share_id = "";		//mode에따라 읽기
	String read_col = "";		//읽을 컬럼명
	if(mode.equals("IDR_S") || mode.equals("ODR_S")) read_col = "mail_add";
	else if(mode.equals("ODR_G")) read_col = "receive";
	
	//사내공문 공유자 지정
	if(mode.equals("IDR_S")) {
		String[] aColumn = {"mail_add"};
		bean.setTable(tablename);			
		bean.setColumns(aColumn);
		query = "where id ='"+id+"'";
	}
	//사외공문 공유자 지정
	else if(mode.equals("ODR_S")) {
		String[] bColumn = {"a.mail_add"};
		bean.setTable("OutDocShare_receive a,OutDocument_receive b");			
		bean.setColumns(bColumn);
		query = "where a.id ='"+id+"' and a.id = b.id and a.ac_code='"+ac_code+"'";
	}
	//사외공문 배포자[부서장] 지정 
	else if(mode.equals("ODR_G")) {
		 String[] cColumn = {"receive"};
		bean.setTable(tablename);			
		bean.setColumns(cColumn);
		query = "where id ='"+id+"'";
	}
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		share_id += bean.getData(read_col);		//mode별에 따라 읽기
	} //while
	
%>


<HTML>
<HEAD>
<META content="text/html;charset=euc-kr" http-equiv=content-type>
<LINK href="css/style.css" rel=stylesheet>
</HEAD>

<BODY topmargin="0" leftmargin=5 marginwidth=0>

<!-- 상단여백 -->
<TABLE><TR><TD height='5'></TD></TR></TABLE>
<!-- 테두리 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="120" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TR><TD>

	<!--본문-->
	<TABLE border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="223" align='center'>
		<TR height='25px;'>
			<TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;" BACKGROUND='images/title_bm_bg.gif'>
			<font color="#4D91DC"><b>선택된 임직원 리스트</b></font></td></TR>
		<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
		<TR><TD height='4'></TD></TR>
		<TR><TD width="100%" height="130" valign="top" align='middle'>
			<!-- 공유 리스트 시작-->
			<form name="listForm" method="post" style="margin:0">
			<select name="user_list" multiple size="8">
			<OPTGROUP label='--------------------'>
		<%
				StringTokenizer share = new StringTokenizer(share_id,";");
			while(share.hasMoreTokens()){
			String sh = share.nextToken();
			if(sh.length() > 5) {
				sh = sh.trim()+";";
				out.println("<option value='"+sh+"'>"+sh+"</option>");
			}
		}
		%>	</select></FORM></TD></TR>
		<TR><TD width="100%" height="25" valign='top' style='padding-left:10px;'>
				<a href='javascript:delSelected();'><img src='images/bt_del_sel.gif' border='0'></a></td></TR>
	</TABLE>

</TD></TR></TABLE><!--테두리 끝-->

<!-- 중간여백 -->
<TABLE><TR><TD height='7px;'></TD></TR></TABLE>

<!-- 테두리 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" height="70" border=1 bordercolordark="#FFFFFF" bordercolorlight="#9CA9BA"><TR><TD colspan='5'>	

	<!-- 내용 -->
	<TABLE border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="223" align='left'>
		<TR height='25px;'><TD vAlign=center bgcolor='#DBE7FD' style="padding-left:5px;COLOR:#4D91DC;FONT WEIGHT:bolder;font-size:9pt;"  BACKGROUND='images/title_bm_bg.gif' colspan='5'><font color="#4D91DC"><b>공문 전달대상 선택방법</b></font></td></TR>
		<TR><TD height='1' bgcolor='#9CA9BA'></TD></TR>
		<TR><TD width="100%" height="100" valign="middle" bgcolor="F5F5F5" style='padding-left:5px;padding-right:5px;'>
			<font color="565656">
				1. 전달대상 임직원을 검색화면에서<br>&nbsp;&nbsp;&nbsp;&nbsp;검색한다.<br>
				2. 검색된 임직원의 이름을 클릭하여<br>&nbsp;&nbsp;&nbsp;&nbsp;리스트에 추가한다.<br>
				3. 선택완료 버튼을 눌러 종료한다.	
			</font>
			</TD></TR></TABLE>
<form name="eForm" method='post' style="margin:0">
	<input type='hidden' name='share_id'>
	<input type='hidden' name='target' value='<%=target%>'>
	<input type='hidden' name='id' value='<%=id%>'>
	<input type='hidden' name='tablename' value='<%=tablename%>'>

	<input type='hidden' name='mode'>
</form>
</TD></TR></TABLE><!--테두리-->

</BODY>
</HTML>



<script language="javascript">

//리스트에서 항목 삭제하기
function delSelected()
{
	var num = document.listForm.user_list.selectedIndex;
	if(num < 0){
		alert("삭제할 사람을 선택해 주십시오.");
		return;
	}

	var Frm = document.listForm.user_list;
	var len = Frm.length;
	for (i=len-1;i>=0 ;i--) {
        if(Frm.options[i].selected == true) Frm.options[i] = null;
    }	
}
//리스트에 있는 내용 opener 에 보내기
function transferList()
{
	var from = document.listForm.user_list;

	var user_list = "";
	for(i=0;i<from.length;i++) {
		user_list += from.options[i].value + "\n";
	} //for
	
	var mode = '<%=mode%>';
	if(mode == "IDR_S") {			//사내수신문서 공지전달[공유]
		document.eForm.action='../servlet/InDocumentRecServlet';
		document.eForm.share_id.value = user_list;
		document.eForm.mode.value='IDR_S';
		document.eForm.submit();
	} else if (mode == "ODR_S") {	//사외수신문서 공지전달[공유]
		document.eForm.action='../servlet/OutDocumentRecServlet';
		document.eForm.share_id.value = user_list;
		document.eForm.mode.value='ODR_S';
		document.eForm.submit();
	} else if (mode == "ODR_G") {	//사외수신문서 배포[부서]
		document.eForm.action='../servlet/OutDocumentRecServlet';
		document.eForm.share_id.value = user_list;
		document.eForm.mode.value='ODR_G';
		document.eForm.submit();
	}
}

function addUsers(item) // item == usr|A030003/대리/박동렬 , div|34/통신연구실
{
    var fromField=item.split("|");			//사용자(usr) or 부서(div) 구분	
    var type =  fromField[0];				//usr or div
	var user = fromField[1];				//나머지

	var fromField2=user.split("/");			// 사번/직급/이름
	var id;
	var name;
	var rank;

	var mode = '<%=mode%>';					//전달자 부서명인지 부서원인지 판단

	if(type == "usr"){	id = fromField2[0]; 	rank = fromField2[1]; 	name = fromField2[2];}
	else{				id = fromField2[0];		rank = "";				name = fromField2[1];}
	user = id + "/" + name + ";";

	if((mode == "IDR_S") || (mode == "ODR_S")) {	
		if(type == "div") { alert("부서원을 선택하십시요."); 	return; }
	} else if(mode == "ODR_G") {						
		if(type == "usr") { alert("부서명을 선택하십시요."); 	return; }
	}

	var where_list = document.listForm.user_list;

	//중복 추가할 수 없게 처리
	var length = where_list.length;
	for(j=0;j<length;j++) {	
		if(where_list.options[j].value == user) {
			alert('[중복]이미 추가된 항목입니다.');
			return;
		}
	}
	//리스트에 추가
	var option0 = new Option(id+"/"+name+";",id+"/"+name+";");	//text,value
	where_list.options[length] = option0;
}

//-->
</script>
