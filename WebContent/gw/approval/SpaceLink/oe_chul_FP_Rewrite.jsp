<%@ include file="../../../admin/configHead.jsp"%>
<%@ page		
	info= "외출계 작성"		
	contentType = "text/html; charset=euc-kr"
	errorPage = "../../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.oreilly.servlet.MultipartRequest"
	import="com.anbtech.file.textFileReader"
	import="com.anbtech.util.normalFormat"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// 공통 변수
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	textFileReader text = new com.anbtech.file.textFileReader();			//파일 다루기
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//출력형식

	String filepath =  upload_path+"/es/"+login_id+"/addfile";
	int maxFileSize = 10;
	com.oreilly.servlet.MultipartRequest multi;
	multi = new com.oreilly.servlet.MultipartRequest(request,filepath,maxFileSize*1024*1024,"euc-kr");

	String query = "";
	String writer_id = "";			//등록자(대리등록일수도 있음) 사번
	String writer_name = "";		//등록자(대리등록일수도 있음) 이름

	String user_id = "";			//해당자 사번
	String user_name = "";			//해당자 명
	String rank_code = "";			//해당자 직급code
	String user_rank = "";			//해당자 직급
	String div_id = "";				//해당자 부서명 관리코드
	String div_name = "";			//해당자 부서명
	String div_code = "";			//해당자 부서코드

	String fname = "";				//첨부파일명
	String sname = "";				//첨부파일 저장명
	String ftype = "";				//첨부파일Type
	String fsize = "";				//첨부파일Size
	int attache_cnt = 4;			//첨부파일 최대갯수 (미만)
	String bon_path = "";			//본문path

	//전달받은 대상자
	user_id = multi.getParameter("user_id")==null?login_id:multi.getParameter("user_id");
	user_name = multi.getParameter("user_name")==null?"":multi.getParameter("user_name");

	/*********************************************************************
	 	등록자(login) 알아보기
	*********************************************************************/	
	String[] Column = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(Column);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		writer_id = login_id;							//등록자 사번
		writer_name = bean.getData("name");				//등록자 명
	} //while

	/*********************************************************************
	 	해당자 정보 알아보기 (대상자)
	*********************************************************************/	
	String[] idColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(idColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+user_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		user_name = bean.getData("name");				//해당자 명
		rank_code = bean.getData("ar_code");			//해당자 직급 code
		user_rank = bean.getData("ar_name");			//해당자 직급
		div_id = bean.getData("ac_id");					//해당자 부서명 관리코드
		div_name = bean.getData("ac_name");				//해당자 부서명 
		div_code = bean.getData("ac_code");				//해당자 부서코드
	} //while

	/*********************************************************************
	 	외출관리코드 등록여부 확인하기(yangsic_env)
	*********************************************************************/	
	String doc_oechul = "OT_001";

	/*********************************************************************
	 	내부 데이터 처리
	*********************************************************************/
	String line = multi.getParameter("doc_app_line"); if(line == null) line = "";	//결재선
	String dest = multi.getParameter("dest"); if(dest == null) dest = "";			//목적지
	String purpose = multi.getParameter("purpose"); if(purpose == null) purpose = "";//사유
	String StartTime = multi.getParameter("hdStartTime");							//시작시간
	String EndTime = multi.getParameter("hdEndTime");								//종료시간
	String fellow_names = multi.getParameter("fellow_names"); 
		if(fellow_names == null) fellow_names = "";									//동행자 사번/이름;
	String traffic_way = multi.getParameter("traffic_way"); 
		if(traffic_way == null) traffic_way = "";									//교통편
	String rec = multi.getParameter("doc_receiver"); if(rec == null) rec = "";		//인수인계자
	String tel = multi.getParameter("doc_tel"); if(tel == null) tel = "";			//긴급연락처

	//시작 년도
	String year = anbdt.getYear();			
	int syear = Integer.parseInt(year);
	int ey = syear + 5;
	String sel_syear = multi.getParameter("doc_syear"); if(sel_syear == null) sel_syear = year;
	//시작 월
	String month = anbdt.getMonth();
	String sel_smonth = multi.getParameter("doc_smonth");	
	if(sel_smonth == null) sel_smonth = month;
	//시작 일
	String dates = anbdt.getDates();
	String sel_sdate = multi.getParameter("doc_sdate");	
	if(sel_sdate == null) sel_sdate = dates;
	int maxdates = anbdt.getDateMaximum(Integer.parseInt(sel_syear),Integer.parseInt(sel_smonth),1);
		
	/*********************************************************************
	 	view.jsp로 or 삭제후 자체적으로 부터 받은 첨부파일 정보
	*********************************************************************/	
	//첨부파일 개별로 읽기 from oe_chul_FP_view.jsp
	String doc_id = multi.getParameter("doc_id");
	fname = multi.getParameter("fname"); if(fname == null) fname = "";			
	sname = multi.getParameter("sname"); if(sname == null) sname = "";			
	ftype = multi.getParameter("ftype"); if(ftype == null) ftype = "";			
	fsize = multi.getParameter("fsize"); if(fsize == null) fsize = "";			
	bon_path = multi.getParameter("bon_path"); if(bon_path == null) bon_path = "";

	//첨부파일 개별로 읽기
	int a_cnt = 0;
	for(int a=0; a<fname.length(); a++) if(fname.charAt(a) == '|') a_cnt++;

	String[][] addFile = new String[a_cnt][5];
	for(int a=0; a<a_cnt; a++) for(int b=0; b<5; b++) addFile[a][b]="";

	if(fname.length() != 0) {
		StringTokenizer f = new StringTokenizer(fname,"|");		//파일명 담기
		int m = 0;
		while(f.hasMoreTokens()) {
			addFile[m][0] = f.nextToken();
			addFile[m][0] = addFile[m][0].trim(); 
			if(addFile[m][0] == null) addFile[m][0] = "";
			m++;
		}
		StringTokenizer t = new StringTokenizer(ftype,"|");		//파일type 담기
		m = 0;
		while(t.hasMoreTokens()) {
			addFile[m][1] = t.nextToken();
			addFile[m][1] = addFile[m][1].trim();
			if(addFile[m][1] == null) addFile[m][1] = "";
			m++;
		}
		StringTokenizer s = new StringTokenizer(fsize,"|");		//파일크기 담기
		m = 0;
		while(s.hasMoreTokens()) {
			addFile[m][2] = s.nextToken();
			addFile[m][2] = addFile[m][2].trim();
			if(addFile[m][2] == null) addFile[m][2] = "";
			m++;
		}
		StringTokenizer o = new StringTokenizer(sname,"|");		//저장파일 담기
		m = 0;
		int no = 1;
		while(o.hasMoreTokens()) {
			addFile[m][3] = o.nextToken();	
			addFile[m][4] = addFile[m][3];
			addFile[m][3] = addFile[m][3].trim() + ".bin";			//저장파일명
			addFile[m][4] = addFile[m][4].trim();					//TEMP 저장파일명
			if(addFile[m][3] == null) addFile[m][3] = "";
			m++;
			no++;
		}
	}

	//-----------------------------
	//자체 첨부파일 삭제를 위해
	//-----------------------------
	String old_id = multi.getParameter("old_id"); if(old_id == null) old_id = doc_id;	//최초로 넘겨받을때만
	String req = multi.getParameter("req"); if(req == null) req = "";
	String ext = multi.getParameter("ext"); if(ext == null) ext = "";		//삭제할 첨부파일 번호

	if(req.equals("ADD_DEL")) {
		fname = ftype = fsize = sname = "";
		int del_ext = Integer.parseInt(ext) - 1;
		String update = "update geuntae_master set ";
		
		for(int a=0; a<a_cnt; a++) {
			if(a == del_ext) {	//내용을 제외한 
				fname += " |";
				ftype += " |";
				fsize += " |";
				sname += " |";
			}
			else {
				fname += addFile[a][0] + " |";
				ftype += addFile[a][1] + " |";
				fsize += addFile[a][2] + " |";
				sname += addFile[a][4] + " |";
			}
		} 
		//Tabel update 하기
		update += "fname='"+fname+"',sname='"+sname+"',ftype='"+ftype+"',fsize='"+fsize+"' where gt_id="+old_id;
		bean.execute(update);		//해당내용 update하기

		//첨부파일 삭제하기
		String delfilename = filepath + "/" + addFile[del_ext][3];
		text.delFilename(delfilename);	//해당 파일삭제 하기

		//삭제된 첨부파일 배열번호 clear 하기
		addFile[del_ext][0]=addFile[del_ext][1]=addFile[del_ext][2]=addFile[del_ext][3]="";

	}

%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0">
<form action="oe_chul_FP_Rewrite.jsp" name="eForm" method="post" encType="multipart/form-data" style="margin:0">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> 외출계 작성</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=300>
				<a href="Javascript:eleApprovalManagerLineSelect();"><img src="../../images/bt_sel_line.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:eleApprovalRequest();"><img src="../../images/bt_sangsin.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:eleApprovalTemp();"><img src="../../images/bt_save_tmp.gif" border="0" align="absmiddle"></a>
				<a href="Javascript:history.go(-1);"><img src="../../images/bt_cancel.gif" border="0" align="absmiddle"></a></TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 결재 정보 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">결<br>재<br>선</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=line%></TEXTAREA></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">결<p>재</TD>
		<TD noWrap width=36% align=left><!-- 결재칸-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">기안자</TD>
					<TD noWrap width=80 align=middle class="bg_07">검토자</TD>
					<TD noWrap width=80 align=middle class="bg_07">승인자</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD>
					<TD noWrap width=80 align=middle class="bg_06">&nbsp;</TD></TR>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07">&nbsp;<img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>	<!-- 결재칸 끝 -->	
		</TD></TR>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr></TBODY></TABLE>
<TABLE><TR><TD width="5"></TD></TR></TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
    <!--기본정보-->
	<table cellspacing=0 cellpadding=2 width="100%" border=0>
	   <tbody>
         <tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">소속부서</td>
           <td width="37%" height="25" class="bg_04"><%=div_name%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">대상자</td>
           <td width="37%" height="25" class="bg_04"><input size="10" type="text" name="user_name" value='<%=user_name%>' class="text_01" readonly> <a href="Javascript:searchUser();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">외출일자</td>
           <td width="37%" height="25" class="bg_04">
			<%
				//시작 년도						
				out.println("&nbsp;<SELECT NAME='doc_syear'>");
				for(int iy = syear; iy < ey; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_syear)) sel = "selected";
					else sel = "";
					out.println("<option "+sel+" value='"+iy+"'>"+iy);
				}
				out.println("</SELECT>년"); 

				//시작 월
				out.println("<SELECT NAME='doc_smonth'>");
				for(int iy = 1; iy < 13; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_smonth)) sel = "selected";
					else sel = "";
					out.println("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
				}
				out.println("</SELECT>월"); 

				//시작 일
				out.println("<SELECT NAME='doc_sdate'>");
				for(int iy = 1; iy <= maxdates; iy++) {
					String sel = "";
					if(iy == Integer.parseInt(sel_sdate)) sel = "selected";
					else sel = "";
					out.println("<option "+sel+" value='"+fmt.toDigits(iy)+"'>"+fmt.toDigits(iy));
				}
				out.println("</SELECT>일");
		   %>		   
		   </td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">외출시간</td>
           <td width="37%" height="25" class="bg_04">
				<SELECT NAME="hdStartTime" CLASS="etc">
				<%
				String Shours = StartTime.substring(0,2);	//시작시
				String Smins = StartTime.substring(3,5);	//시작분
				String Ehours = EndTime.substring(0,2);		//종료시
				String Emins = EndTime.substring(3,5);		//종료분

				String[] asHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String msSEL = "";
					for(int asH=0; asH<24; asH++){
						if(asH == Integer.parseInt(Shours)) msSEL = "SELECTED"; else msSEL="";
						if(Smins.equals("00")) {
							out.println("<OPTION " + msSEL + ">" + asHour[asH] + ":" + "00");
							out.println("<OPTION>" + asHour[asH] + ":" + "30");
						} else {
							out.println("<OPTION>" + asHour[asH] + ":" + "00");
							out.println("<OPTION " + msSEL + ">" + asHour[asH] + ":" + "30");
						}
					}
					out.println("</SELECT> ~ ");
				%>
				<SELECT NAME="hdEndTime" CLASS="etc">
				<%
				String[] aeHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
					String meSEL = "";
					for(int aeH=0; aeH<24; aeH++){
						if(aeH == Integer.parseInt(Ehours)) meSEL = "SELECTED"; else meSEL="";
						if(Emins.equals("00")) {
							out.println("<OPTION " + meSEL + ">" + aeHour[aeH] + ":" + "00");
							out.println("<OPTION>" + aeHour[aeH] + ":" + "30");
						} else {
							out.println("<OPTION>" + aeHour[aeH] + ":" + "00");
							out.println("<OPTION " + meSEL + ">" + aeHour[aeH] + ":" + "30");
						}
					}
				%></SELECT>		   
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">행선지</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="dest" value='<%=dest%>' size="20" maxlength="30" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">교통편</td>
           <td width="37%" height="25" class="bg_04">
					<select name='traffic_way'">
				<% 
					out.println("<OPTION value='버스'>버스</OPTION>");
					out.println("<OPTION value='기차'>기차</OPTION>");
					out.println("<OPTION value='항공기'>항공기</OPTION>");
					out.println("<OPTION value='자가운전'>자가운전</OPTION>");
					out.println("<OPTION value='회사차'>회사차</OPTION>");
					out.println("<OPTION value='도보'>도보</OPTION>");
					out.println("<OPTION value='기타'>기타</OPTION>");
				%>
					</select>		   
		   </td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">외출사유</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="purpose" value='<%=purpose%>' size="20" maxlength="30" class="text_01"></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">동행자</td>
           <td width="37%" height="25" class="bg_04"><TEXTAREA NAME="fellow_names" rows=1 cols=16 readOnly><%=fellow_names%></TEXTAREA> <a href="Javascript:searchProxy();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">업무인수자</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="doc_receiver" value='<%=rec%>' readOnly> <a href="Javascript:jobReceiver();"><img src="../../images/bt_search.gif" border="0" align="absmiddle"></a></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">긴급연락처</td>
           <td width="37%" height="25" class="bg_04"><input type="text" name="doc_tel" value='<%=tel%>' size="15" maxlength="20" class="text_01"></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">신청일자</td>
           <td width="87%" height="25" colspan="3" class="bg_04"><%=anbdt.getYear()%> 년 <%=anbdt.getMonth()%> 월 <%=anbdt.getDates()%>일</td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">첨부파일</td>
           <td width="87%" height="25" colspan="3" class="bg_04">
			<% 
			for(int a=0,no=1; a<a_cnt; a++,no++) {	//기존내용 보여주기
				if(addFile[a][0].length() == 0) {		//내부적으로 삭제된 첨부일때
					out.println("<input type=file name=attachfile"+no+" size=60><br>");
				} else {
					out.println("&nbsp;<a href='attach_download.jsp?fname="+addFile[a][0]+"&ftype="+addFile[a][1]+"&fsize="+addFile[a][2]+"&sname="+addFile[a][3]+"&extend="+bon_path+"'>"+addFile[a][0]+"</a>");
					out.println("<a href=javascript:attachDel('"+no+"')>[삭제]<a><br>"); 
				}
			} 
			a_cnt++;		//attachefile의 번호를 순서를 위해
			if(a_cnt < attache_cnt) {							//첨부파일 추가하기
				for(int a=a_cnt; a<attache_cnt; a++) {
					out.println("<input type=file name=attachfile"+a+" size=60><br>");
				}
			} 
			%>
		   </td></tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>

<input type="hidden" name="div_id" value='<%=div_id%>'>
<input type="hidden" name="div_code" value='<%=div_code%>'>
<input type="hidden" name="div_name" value='<%=div_name%>'>
<input type="hidden" name="user_id" value='<%=user_id%>'>
<input type="hidden" name="user_code" value='<%=rank_code%>'>
<input type="hidden" name="user_rank" value='<%=user_rank%>'>
<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_sub' value=''>
<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='old_id' value='<%=old_id%>'>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
<input type='hidden' name='doc_oechul' value='<%=doc_oechul%>'>

<% // 내부 데이터 처리 %>
<input type='hidden' name='req' value=''>
<input type='hidden' name='ext' value=''>
<input type='hidden' name='bon_path' value='<%=bon_path%>'>
<input type='hidden' name='fname' value='<%=fname%>'>
<input type='hidden' name='sname' value='<%=sname%>'>
<input type='hidden' name='ftype' value='<%=ftype%>'>
<input type='hidden' name='fsize' value='<%=fsize%>'>

<input type='hidden' name='upload_path' value='<%=upload_path%>'>
<input type='hidden' name='attache_cnt' value='<%=attache_cnt%>'>
</form>
</body>
</html>


<script language=javascript>
<!--
//첨부파일 삭제하기
function attachDel(a)
{
	document.eForm.action='oe_chul_FP_Rewrite.jsp';
	document.eForm.req.value='ADD_DEL';	
	document.eForm.ext.value=a;	
	document.eForm.submit();
}
//동행자 찾기
function searchProxy()
{
	fellows = document.eForm.fellow_names.value; 
	wopen("searchFellows.jsp?target=eForm.fellow_names&fellows="+fellows,"proxy","520","467","scrollbars=no,toolbar=no,status=no,resizable=no");

}
//대상자 찾기
function searchUser()
{
	wopen("searchUser.jsp?target=eForm.user_id/eForm.user_name","user","250","380","scrollbars=auto,toolbar=no,status=no,resizable=no");

}
//업무인수자
function jobReceiver()
{
	wopen("searchName.jsp?target=eForm.doc_receiver","proxy","250","380","scrollbars=auto,toolbar=no,status=no,resizable=no");

}
//출력하기
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}

//창띄우기 공통
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}

//결재선 지정지정 
function eleApprovalManagerLineSelect()
{
	var target="eForm.doc_app_line&anypass=Y" 
	wopen("../eleApproval_Share.jsp?target="+target,"eleA_app_search_select","520","467","scrollbars=no,toolbar=no,status=no,resizable=no");
}

//결재 상신 
function eleApprovalRequest()
{
	if (eForm.doc_app_line.value =="") { alert("결재선을 입력하십시요."); return; }
	else if(eForm.dest.value == "") { alert("행선지을 입력하십시요."); return;}
	else if(eForm.purpose.value == "") { alert("사유를 입력하십시요."); return;}
	else if(eForm.doc_tel.value == "") { alert("긴급연락처를 입력하십시요."); return;}
	
	 //결재선 검사
	data = eForm.doc_app_line.value;		//결재선 내용
	s = 0;								//substring시작점
	e = data.length;					//문자열 길이
	decision = agree = 0;
	for(j=0; j<e; j++){
		ocnt = data.indexOf("\n");
		j += ocnt + 1;

		rstr = data.substring(s,ocnt);
		if(rstr.indexOf("승인") != -1) decision++;
		if(rstr.indexOf("협조") != -1) agree++;
	
		if(rstr.length == 0) j = e;
		data = data.substring(ocnt+1,e);
	}
	if(decision == 0) { alert("승인자가 빠졌습니다"); return; }
	
	//제목구하기
	var purpose = document.eForm.purpose.value;
	var doc_sub = "외출계 : "+purpose;

	document.onmousedown=dbclick;  // 더블클릭 check

	//일괄합의 결재상신진행
	document.eForm.action='../../../servlet/GeunTaeServlet';
	document.eForm.mode.value='R_OE_CHUL';	
	document.eForm.app_mode.value='REQ';	
	document.eForm.doc_sub.value=doc_sub;
	document.eForm.submit();
}

//결재 임시보관
function eleApprovalTemp()
{
	//제목구하기
	var purpose = document.eForm.purpose.value;
	var doc_sub = "외출계 : "+purpose;

	document.onmousedown=dbclick;  // 더블클릭 check

	document.eForm.action="../../../servlet/GeunTaeServlet";
	document.eForm.mode.value='R_OE_CHUL_TMP';
	document.eForm.app_mode.value='TMP';	
	document.eForm.doc_sub.value=doc_sub;
	document.eForm.submit();
	
}

// 더블클릭 방지
function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
-->
</script>
