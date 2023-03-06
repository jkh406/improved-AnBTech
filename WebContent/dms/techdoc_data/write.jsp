<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		= "java.util.*,com.anbtech.dms.entity.*"
	errorPage	= "../../admin/errorpage.jsp"
%>
<%!
	MasterTable master;
	TechDocTable techdoc;
	LinkUrl	redirect;
	DmsEnvTable dmsenv;
%>

<%
	/*********************
	 * 등록자 정보(로긴정보)
	**********************/
	String id = sl.id; 				//ID
	String name = sl.name;			//이름
	String division = sl.division;	//부서명

	/**********
	 * 등록일자
	***********/
	java.util.Date now = new java.util.Date();
	java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
	String regi_date 	= vans.format(now);

	/**********
	 * 파라미터
	***********/
	String mode = request.getParameter("mode");
	String no = request.getParameter("no");
	String data_id = request.getParameter("d_id");
	String category = request.getParameter("category");

	/******************************
	 * master_data 테이블내용 가져오기
	 ******************************/
	master = new MasterTable();
	master = (MasterTable)request.getAttribute("MasterData");
	String subject			= master.getSubject()==null?"":master.getSubject();
	String writer			= master.getWriter()==null?"":master.getWriter();
	String category_info	= master.getWhereCategory();
	String doc_no			= master.getDocNo() == null?"문서등록시 자동생성":master.getDocNo();
	String model_code		= master.getModelCode();
	String model_name		= master.getModelName();
	String pjt_code			= master.getPjtCode();
	String pjt_name			= master.getPjtName();
	String node_code		= master.getNodeCode();
	String node_name		= master.getNodeName();
	String model_info		= "";

	if(!model_code.equals("") && model_code != null){
		com.anbtech.gm.business.makeGoodsTreeItems mt = new com.anbtech.gm.business.makeGoodsTreeItems();
		model_info = mt.getGoodsClassStrByModelCode(0,model_code,"") + model_name;
	}

	/******************************
	 * techdoc_data 테이블내용 가져오기
	 ******************************/
	techdoc = new TechDocTable();
	techdoc = (TechDocTable)request.getAttribute("techdoc_data");
	String where_from = techdoc.getWhereFrom()==null?"자체제작":techdoc.getWhereFrom();
	String security_level = techdoc.getSecurityLevel()==null?"3":techdoc.getSecurityLevel();
	String save_period = techdoc.getSavePeriod()==null?"3":techdoc.getSavePeriod();
	String written_lang = techdoc.getWrittenLang()==null?"KOR":techdoc.getWrittenLang();
	String doc_type = techdoc.getDocType()==null?"FILE":techdoc.getDocType();
	String data_no = techdoc.getAncestor()==null?"":techdoc.getAncestor();
	String modify_info = techdoc.getModifyHistory()==null?"":techdoc.getModifyHistory();
	String why_revision = techdoc.getWhyRevision()==null?"":techdoc.getWhyRevision();
	String preview = techdoc.getPreview()==null?"":techdoc.getPreview();
	String category_id = techdoc.getCategoryId()==null?"":techdoc.getCategoryId();
	String save_url = techdoc.getSaveUrl()==null?"NA":techdoc.getSaveUrl();
	String copy_num = techdoc.getCopyNum()==null?"NA":techdoc.getCopyNum();
	String writer_s = techdoc.getWriterS()==null?"":techdoc.getWriterS();
	String version = techdoc.getVerCode() == null?"1.0":techdoc.getVerCode();

	/******************************
	 * 각종 링크문자열 가져오기
	 ******************************/
	redirect = new LinkUrl();
	redirect = (LinkUrl)request.getAttribute("RedirectInWrite");
	String link_change_category = redirect.getLinkChangeCategory();
	String input_hidden = redirect.getInputHidden();

	/******************************
	 * 현재분류에 대한 환경 가져오기
	 ******************************/
	dmsenv = new DmsEnvTable();
	dmsenv = (DmsEnvTable)request.getAttribute("DmsEnv");
	String enable_project	= dmsenv.getEnableProject();	//프로젝트 관리여부(y/n)
	String enable_model		= dmsenv.getEnableModel();		//관련모델명 관리여부(y/n)
	String enable_revision	= dmsenv.getEnableRevision();	//리비젼 관리여부(y/n)

	//신규입력시에는 보안등급과 보존기간을 디폴트로 세팅한다.
	if(mode.equals("write")){
		security_level = dmsenv.getSecurityLevel();
		save_period = dmsenv.getSavePeriod();
	}

	/******************************
	 * 페이지 타이틀 문자열 생성
	 ******************************/
	String title = "";
	String reg_type = "";
	if(mode.equals("write")){
		title = "신규문서등록";
		reg_type = "문서 등록";
	}else if(mode.equals("modify")){
		title = "문서등록정보수정";
		reg_type = "문서 수정";
	}else if(mode.equals("revision")){
		title = "문서리비젼";
		reg_type = "문서 리비젼";
	}


	int ref_count		= 5;	// 최대 참조문서 개수 지정
	int enableupload	= 4;	// 업로드 개수 지정
%>

<script language=JavaScript>
<!--
<%
	int i = 1;

	while(i < enableupload){
		if(i == enableupload-1){
%>

		function fileadd_action<%=i%>() {
		id<%=i%>.innerHTML="<br><input type=file name=attachfile<%=i+1%> size=50>"
		}
	<%
		 break;
		}
	%>
	function fileadd_action<%=i%>() {
		id<%=i%>.innerHTML="<br><input type=file name=attachfile<%=i+1%>  onClick='fileadd_action<%=i+1%>()' size=50><font id=id<%=i+1%>></font>"
	}
<%
	i++;
	}
%>
//-->
</script>

<%
	TechDocTable file = new TechDocTable();

	ArrayList file_list = new ArrayList();
	file_list = (ArrayList)request.getAttribute("File_List");
	Iterator file_iter = file_list.iterator();

	i = 1;

	String file_stat = "";
	while(file_iter.hasNext()){
		file = (TechDocTable)file_iter.next();
		file_stat = file_stat + "<input type=file name='attachfile"+i+"' size=50>" + file.getFileName()+" 삭제! <input type=checkbox name = 'deletefile"+i+"' value='delete'><br>";
		i++;
	}
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../dms/css/style.css" type="text/css">
<STYLE>
	.expanded {color:black;}
	.collapsed {display:none;}
</STYLE>
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../dms/images/blet.gif"> <%=title%></TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300><a href="javascript:checkForm();"><img src="../dms/images/bt_save.gif" border="0" align="absmiddle"></a> <a href="javascript:history.go(-1);"><img src="../dms/images/bt_cancel.gif" border="0" align="absmiddle"></a></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<form method=post name="writeForm" action='../servlet/AnBDMS?tablename=techdoc_data&upload_size=50' enctype='multipart/form-data' style="margin:0">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--기본정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">등록구분</td>
           <td width="37%" height="25" class="bg_04"><b><%=reg_type%></b><input type="hidden" name="mode" value="<%=mode%>"></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">문서출처</td>
           <td width="37%" height="25" class="bg_04">
		   <%
				//선택된 문서분류가 자체제작기술문서(코드:10101로 시작)일 경우는 자체제작문서로 고정
				//외부입수기술자료(코드:10102로 시작)일 경우에는 입력받도록 한다.
				if(category.indexOf("10101") == 0) out.print("<input type='text' name='where_from' value='자체제작' size='7' readOnly>");
				else if(category.indexOf("10102") == 0) out.print("<input type='text' name='where_from' value='' size='20'>");
		   %>
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">문서번호</td>
           <td width="37%" height="25" class="bg_04"><%=doc_no%></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">문서버젼</td>
           <td width="37%" height="25" class="bg_04"><input type=text name=ver_code size=4 readOnly value="<%=version%>"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">문서분류</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<%=category_info%><input type="hidden" name="category" value="<%=category%>">
				<% if(version.equals("1.0")){ //최초 버젼인 경우만 카테고리 변경 가능	%>				
					<a href='<%=link_change_category%>'><img src="../dms/images/bt_search.gif" border="0" align="absmiddle"></a>
				<%	}	%>
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
<%	if(enable_project.equals("y")){	//프로젝트 관리할때만 출력	%>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">과제이름</td>
           <td width="37%" height="25" class="bg_04" colspan='3'>
				<input type='text' name="pjt_code" value="<%=pjt_code%>" size="15" readOnly class="text_01"> <input type=text name="pjt_name" size='20' value="<%=pjt_name%>" readOnly class="text_01">
				<a href='javascript:pjt_search();'><img src="../dms/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">문서종류</td>
           <td width="37%" height="25" class="bg_04" colspan='3'>
				<input type='text' name="node_code" value="<%=node_code%>" size="15" readOnly> <input type=text name="node_name" size='20' value="<%=node_name%>" readOnly> <a href='javascript:node_search();'><img src="../dms/images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
<%	}	%>
<%	if(enable_model.equals("y")){ //관련모델관리할때만 출력	%>				
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">관련모델명</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<input type="text" name="model_code" value="<%=model_code%>" size="15" readOnly class="text_01"> <input type=text name="model_name" size='20' value="<%=model_name%>" readOnly class="text_01"> <a href='javascript:m_search();'><img src="../dms/images/bt_search.gif" border="0" align="absmiddle"></a>
						   
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
<%	}	%>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">문서제목</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><input type=text name=subject size=50 value="<%=subject%>" maxlength="50" class="text_01"></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">작성자(입수자)</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
					<input type=text name="writer_info" size="15" readOnly value="<%=writer_s%>" class="text_01">
					<a href='javascript:searchUser();'><img src="../dms/images/bt_search.gif" border="0" align="absmiddle"></a>
					<input type="checkbox" name="writer_same" value="same" onClick="selWriterSame();">등록자와 동일
					<input type="hidden" name="writer" value="<%=writer%>">		   		   
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">문서첨부</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
				<%
					if (enableupload > 0){
				%>
		            <%=file_stat%>
				<%
						if(i < enableupload){
				%>
				            <input type=file name=attachfile<%=i%> onClick='fileadd_action<%=i%>()' size="50">
				            <font id=id<%=i%>></font>
				<%
						}else if(i == enableupload){
				%>
				            <input type=file name=attachfile<%=i%> size="50">
				            <font id=id<%=i%>></font>
				<%
						}
					}
				%>		   
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">등록자</td>
           <td width="37%" height="25" class="bg_04"><%=id%>/<%=name%>
					<input type="hidden" name="register_info" value="<%=id%>/<%=name%>">
					<input type="hidden" name="register" value="<%=id%>">		   
		   </td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">등록일</td>
           <td width="37%" height="25" class="bg_04"><%=regi_date%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

    <!--기본정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">문서요약</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="7" name="preview" cols="93" class="text_01"><%=preview%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr></tbody></table>
<div id="d_why_revision" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">리비젼사유</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="5" name="why_revision" cols="93" class="text_01"><%=why_revision%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr></tbody></table></div>
<div id="d_modify_history" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">수정이력</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="3" name="modify_history" cols="93"></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr></tbody></table></div>
<div id="d_modify_info" class="collapsed">
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">수정사항</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><textarea rows="5" name="modify_info" cols="93"><%=modify_info%></textarea></td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr></tbody></table></div>
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">보안등급</td>
           <td width="37%" height="25" class="bg_04">
					<select size="1" name="security_level" onFocus="selSecurityLevel();">
					<option value="1">1급</option>
					<option value="2">2급</option>
					<option value="3">3급</option>
					<option value="4">대외비</option>
					<option value="5">일반</option>
					</select>		   
		   </td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">보존기간</td>
           <td width="37%" height="25" class="bg_04">
					<select size="1" name="save_period" onFocus="selSavePeriod();">
					<option value="1">1년</option>
					<option value="3">3년</option>
					<option value="5">5년</option>
					<option value="10">10년</option>
					<option value="0">영구</option>
					</select>		   
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">작성언어</td>
           <td width="37%" height="25" class="bg_04">
					<select size="1" name="written_lang">
					<option value="KOR">한국어</option>
					<option value="ENG">영어</option>
					<option value="JPN">일본어</option>
					<option value="CHN">중국어</option>
					<option value="DAU">독일어</option>
					<option value="ETC">기타</option>
					</select>		   
		   </td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">문서타입</td>
           <td width="37%" height="25" class="bg_04">
					<select name="doc_type" onChange="javascript:selDocType();">
					<option value="FILE">파일형태</option>
					<option value="BOOK">책자형태</option>
					<option value="SHEET">SHEET형태</option>
					<option value="CD">CD(DVD)형태</option>
					<option value="TAPE">테이프형태</option>
					<option value="FILM">필름형태</option>
					<option value="ETC">기타</option>
					</select>		   
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">보관위치</td>
           <td width="37%" height="25" class="bg_04"><input type=text name="save_url" size=35  value="<%=save_url%>" readOnly></td>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">문서건수</td>
           <td width="37%" height="25" class="bg_04"><input type=text name="copy_num" size=5  value="<%=copy_num%>" readOnly></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../dms/images/bg-01.gif">참조자료</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
					<table border=0>
					  <tr>
						<td align=left width=52%>참조자료명</td>
						<td align=left width=23%>저자명</td>
						<td align=left width=15%>출판사</td>
						<td align=right width=10%>출판년도</td>
					  </tr>
					  <tr><td colspan='4'>
						<%
							for(int j=1; j<ref_count; j++){
								String view_str = "collapsed";
								if(j==1) view_str = "expanded";
						%>
								<div id="layer<%=j%>" class="<%=view_str%>">
								<input type=text name=ref_subject<%=j%> size="45" onFocus='add_action<%=j%>()'> <input type=text name=ref_writer<%=j%> size=20> <input type=text name=ref_press_name<%=j%> size=15> <input type=text name=ref_press_year<%=j%> size="4"><br></div>
						<%
							}
						%>
								<div id="layer<%=ref_count%>" class="collapsed">
								<input type=text name=ref_subject<%=ref_count%> size="45"> <input type=text name=ref_writer<%=ref_count%> size=20> <input type=text name=ref_press_name<%=ref_count%> size=15> <input type=text name=ref_press_year<%=ref_count%> size="4"></div>
						<input type='hidden' name='ref_count' value='<%=ref_count%>'>
						<input type='hidden' name='reference'></td>
					  </tr>
					</table>		   
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>
  <input type="hidden" name="tablename" value="techdoc_data">
  <input type="hidden" name="d_id" value="<%=data_id%>">
  <input type="hidden" name="no" value="<%=no%>">
  <input type="hidden" name="doc_no" value="<%=doc_no%>">
<%=input_hidden%>
</form>
</td></tr></table>
</body>
</html>


<script language=JavaScript>
<!--
<%

	for(int k=1; k<ref_count; k++){
%>

		function add_action<%=k%>() {
			show('layer<%=k+1%>');
<%
		int w = ref_count;
		while(w > k+1){
%>
			hide('layer<%=w%>');
<%
			w--;
		}
%>
		}
<%
	}
%>

	//참조자료 리스트 가져와서 뿌려주기
	var f = document.writeForm;
<%
	TechDocTable ref = new TechDocTable();

	ArrayList ref_list = new ArrayList();
	ref_list = (ArrayList)request.getAttribute("Reference_List");
	Iterator ref_iter = ref_list.iterator();

	int c = 1;

	while(ref_iter.hasNext()){
		ref = (TechDocTable)ref_iter.next();
		String ref_subject = ref.getRefSubject();
		String ref_writer = ref.getRefWriter();
		String ref_press_name = ref.getRefPressName();
		String ref_press_year = ref.getRefPressYear();
%>
		f.ref_subject<%=c%>.value = '<%=ref_subject%>';
		f.ref_writer<%=c%>.value = '<%=ref_writer%>';
		f.ref_press_name<%=c%>.value = '<%=ref_press_name%>';
		f.ref_press_year<%=c%>.value = '<%=ref_press_year%>';
<%
		c++;
	}
%>
//-->
</script>


<script language='javascript'>
<!--
<%	if(!security_level.equals("")){	%>
		document.writeForm.security_level.value = '<%=security_level%>';
<%	}	%>

<%	if(!save_period.equals("")){	%>
		document.writeForm.save_period.value = '<%=save_period%>';
<%	}	%>

<%	if(!written_lang.equals("")){	%>
		document.writeForm.written_lang.value = '<%=written_lang%>';
<%	}	%>

<%	if(!doc_type.equals("")){	%>
		document.writeForm.doc_type.value = '<%=doc_type%>';
<%	}	%>
<%	if(!doc_type.equals("FILE")){	%>
		document.writeForm.save_url.readOnly = false;
		document.writeForm.copy_num.readOnly = false;
<%	}	%>

//수정 모드일 경우
<%	if(mode.equals("modify")){	%>

	var f = document.writeForm;
	show('d_why_revision');
//	show('d_modify_info');
//	show('d_modify_history');
//	f.modify_history.readOnly = 'true';
//	f.modify_info.readOnly = 'false';
//	f.why_revision.readOnly = 'true';


<%	
	//기존 참조자료 항목을 보임처리한다.
	for(int r=1; r<c-1; r++){
		out.print("add_action"+r+"();");
	}
}	
%>

//리비젼 모드일 경우
<%	if(mode.equals("revision")){	%>
	var f = document.writeForm;
	f.subject.readOnly = 'true';
	show('d_why_revision');
	f.modify_info.readOnly = 'true';
<%	}	%>

//필수 입력사항 체크
function checkForm(){
	var f = document.writeForm;

	if(f.where_from.value.length < 3){
		alert("문서출처를 입력하십시오.");
		f.where_from.focus();
		return;
	}

<% if(enable_project.equals("y")){ %>
	if(f.pjt_code.value == ''){
		alert("관련과제를 찾아서 선택하십시오.");
		return;
	}
/*	if(f.node_code.value == ''){
		alert("문서종류를 선택하십시요.");
		return;
	}
*/
<% }	%>

<% if(enable_model.equals("y")){ %>
	if(f.model_code.value == '10'){
		alert("관련모델을 찾아서 선택하십시오.");
		return;
	}
<%	}	%>

	if(f.subject.value.length < 5){
		alert("문서 제목을 입력하십시오.(5자 이상)");
		f.subject.focus();
		return;
	}

	if(f.ver_code.value == ''){
		alert("문서버젼을 입력하십시오.");
		return;
	}

	if(f.writer.value == ''){
		alert("작성자(or 입수자)를 선택하십시오.");
		return;
	}

<%	if(mode.equals("write") || mode.equals("modify")){	%>
	if(f.preview.value.length < 30){
		alert("첨부문서의 주요 내용을 30자 이상으로 요약하여 입력하십시오.");
		f.preview.focus();
		return;
	}
<%	}	%>

<%	if(mode.equals("modify")){	%>
/* 수정이력 남기는 것이 효용성이 없어 주석 처리함. 04/01/30
	if(f.modify_info.value.length < 5){
		alert("수정이력을 남기기 위해 수정사항을 요약하여 입력하십시요.");
		f.modify_info.focus();
		return;
	}
*/
<%	}	%>

<%	if(mode.equals("revision")){	%>
	if(f.why_revision.value.length < 10){
		alert("리비젼이력을 남기기 위해 변경사항을 요약하여 입력하십시오.");
		f.why_revision.focus();
		return;
	}
<%	}	%>

	if(f.doc_type.value != "FILE"){
		if(f.save_url.value == ''){
			alert("문서보관위치를 입력하십시오.");
			f.save_url.focus();
			return;
		}
		if(f.copy_num.value == ''){
			alert("문서건수를 입력하십시오.");
			f.copy_num.focus();
			return;
		}
	}


	//참조자료를 하나 이상 입력할 경우 제목은 반드시 입력하도록 한다.
<%	for(int m=1; m<=ref_count; m++){	%>
		if ((f.ref_writer<%=m%>.value.length > 0 || f.ref_press_name<%=m%>.value.length > 0 || f.ref_press_year<%=m%>.value.length > 0) && f.ref_subject<%=m%>.value.length < 1){
			alert("참조자료 제목은 반드시 입력하셔야 합니다.");
			f.ref_subject<%=m%>.focus();
			return;
		}
		var ref<%=m%> = '';
		var ref_subject<%=m%> = f.ref_subject<%=m%>.value;
		var ref_writer<%=m%> = f.ref_writer<%=m%>.value;
		var ref_press_name<%=m%> = f.ref_press_name<%=m%>.value;
		var ref_press_year<%=m%> = f.ref_press_year<%=m%>.value;


		ref<%=m%> = ref_subject<%=m%> + "|" + ref_writer<%=m%> + "|" + ref_press_name<%=m%> + "|" + ref_press_year<%=m%>;
<%	}	%>


	var ref = '';

<%	for(int n=1; n<=ref_count; n++){	%>
		ref += ref<%=n%> + "^";
<%	}	%>
	
	f.reference.value = ref;

	document.onmousedown=dbclick;
	f.submit();

}

function dbclick() 
{
    if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오."); 	
}

//카테고리 변경
function modify_category(tablename,mode,category,searchscope,searchword,page,no,data_id,ver) {
	var sParam = "src=modify_category.jsp&frmWidth=290&frmHeight=310&title=change_category&category="+category;
	
	var sRtnValue = showModalDialog("../dms/modalFrm.jsp?"+sParam,"change_category","dialogWidth:296px;dialogHeight:335px;toolbar=0;location=0;directories=0;status=0;menuBar=0;scrollBars=0;resizable=0");
	
	if (typeof sRtnValue != "undefined" && sRtnValue != "")
	{
		if(sRtnValue == '1' || sRtnValue == '101'){
			alert("선택된 분류단계에는 문서를 등록하실 수 없습니다.");
			return;
		}
/*
		if(sRtnValue.indexOf("102") == 0){
			alert("선택된 분류단계에는 문서를 등록하실 수 없습니다.");
			return;
		}
*/
		sParam = "&tablename="+tablename+"&category="+sRtnValue+"&searchscope="+searchscope+"&searchword="+searchword;
		sParam = sParam + "&page="+page+"&no="+no+"&d_id="+data_id+"&ver="+ver;
		location.href = "../servlet/AnBDMS?mode="+mode + sParam;
	}

	//wopen('../dms/modify_category.jsp?category='+category+'&tablename='+tablename+'searchscope='+searchscope+'&searchword='+searchword+'&page='+page+'&no='+no+'&d_id='+data_id+'&ver='+ver,'change_category','300','305','scrollbars=no,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//모델 찾기
function m_search()
{
	var url = "../gm/openModelInfoWindow.jsp?fname=writeForm&one_class=pdg_code&one_name=pdg_name&two_class=pd_code&two_name=blank&three_class=modelg_code&three_name=modelg_name&four_class=model_code&four_name=model_name&fg_code=fg_code";
	wopen(url,"SEARCH_MODEL",'800','400','scrollbars=yes,toolbar=no,status=no,resizable=no');

}

//과제코드 찾기
function pjt_search()
{
	wopen('../servlet/PsmProcessServlet?mode=search_project&target=writeForm.pjt_code/writeForm.pjt_name','search_pjt','400','220','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//해당과제 문서[노드]코드 찾기
function node_search()
{
	var pjt_code = document.writeForm.pjt_code.value;
	if(pjt_code.length == 0) { alert('과제이름을 먼저 선택하십시요.'); return; }
	wopen('../servlet/projectPmDocumentServlet?mode=PDT_NL&pjt_code='+pjt_code,'search_node','400','220','scrollbars=no,toolbar=no,status=no,resizable=no');
}

// 작성자가 등록자와 동일할 때의 처리
function selWriterSame(){
	var f = document.writeForm;

	if(f.writer_same.checked){
		f.writer_info.value = f.register_info.value;
		f.writer.value = f.register.value;
//		f.writer_search.disabled = true;
	}else{
		f.writer_info.value = "";
		f.writer.value = "";
//		f.writer_search.disabled = false;
	}
}

//대상자 찾기
function searchUser()
{

	wopen("../dms/searchUser.jsp","search_user","250","380","scrollbar=yes,toolbar=no,status=no,resizable=no");

}

// 문서형태 선택시 취할 액션 정의
function selDocType(){
	var f = document.writeForm;

	if(f.doc_type.value == 'FILE'){
		f.save_url.value = 'NA';
		f.copy_num.value = 'NA';
		f.save_url.readOnly = true;
		f.copy_num.readOnly = true;
	}else{
		f.save_url.value = '';
		f.copy_num.value = '';
		f.save_url.readOnly = false;
		f.copy_num.readOnly = false;	
	}
}

// 보안등급 변경 시 경고 메시지 출력
function selSecurityLevel(){
	alert("보안등급은 문서종류에 맞게 자동 설정되어 있습니다.\n변경하시려면 문서관리자와 협의후 진행하십시요.");
}

// 보존년한 변경 시 경고 메시지 출력
function selSavePeriod(){
	alert("보존년한은 문서종류에 맞게 자동 설정되어 있습니다.\n변경하시려면 문서관리자와 협의후 진행하십시요.");
}


// 선택된 레이어를 숨김
function hide( menuname )
{
  if (navigator.appName =="Netscape" ) {
	  document.layers[menuname].visibility="hide";
  } else {
	  document.all[menuname].className="collapsed"
   }
}

// 선택된 레이이를 보여줌
function show( menuname )
{
  if (navigator.appName =="Netscape" ) {
	   document.layers[menuname].visibility="show";
  } else {
	   document.all[menuname].className="expanded"
  }
}
//-->
</script>