<%@ include file="../admin/configHead.jsp"%>
<%@ page language="java" contentType="text/html;charset=KSC5601" %>
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

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=ks_c_5601-1987">
<meta name="GENERATOR" content="Microsoft FrontPage 4.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<title>새 페이지 5</title>
</head>

<body>

<form name="listForm" action="">
  <select size="15" name="user_list" multiple>
	 <OPTGROUP label='--------------'>
	 <%
		StringTokenizer share = new StringTokenizer(share_id,";");
		while(share.hasMoreTokens()){
			String sh = share.nextToken();
			if(sh.length() > 5) {
				sh = sh.trim()+";";
				out.println("<option value='"+sh+"'>"+sh+"</option>");
			}
		}
	 %>
  </select>
  <br>
  <input type='button' value='선택항목 삭제' onClick='javascript:delSelected();'>
  <br><br>
  <input type='button' value='공문전달' onClick='javascript:transferList();'> 
  <input type='button' value='취소' onClick='javascript:top.close();'>  
</form>

<form name="eForm" method='post'>
	<input type='hidden' name='share_id'>
	<input type='hidden' name='target' value='<%=target%>'>
	<input type='hidden' name='id' value='<%=id%>'>
	<input type='hidden' name='tablename' value='<%=tablename%>'>

	<input type='hidden' name='mode'>
</form>
</body>

</html>

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
