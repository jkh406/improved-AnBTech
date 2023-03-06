<%@ include file="../../admin/configPopUp.jsp"%>
<%@ include file= "../../admin/chk/chkES02.jsp"%>
<%@ page		
	info		= "근태일수 수정"		
	contentType = "text/html; charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.io.*"
	import		= "java.util.*"
	import		= "java.sql.*"
	import		= "com.anbtech.date.anbDate"
	import		= "com.anbtech.util.normalFormat"
	import		= "com.anbtech.text.StringProcess"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// 공통 변수
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//출력형식
	StringProcess str = new com.anbtech.text.StringProcess();				//문자열 다루기

	//출장신청서 내용관련
	String msg = "";
	String query = "";
	String user_id = "";			//대상자 id
	String user_name = "";			//대상자 명
	String fellow_names = "";		//동행자	 사번/이름;
	String syear = "";				//시작 년
	String smonth = "";				//    월
	String sdate = "";				//    일
	String edyear = "";				//종료 년
	String edmonth = "";			//    월
	String eddate = "";				//    일
	String hd_var = "";				//근태 코드
	String hd_name = "";			//근태 이름
	String search_cnt = "";			//해당구분의 수량
	String gt_id = "";				//관리코드 //전달받은 from geuntae_master
	String[][] fellows;				//사번, 이름
	String search_yy = "";			//대상년도
	String search_mm = "";			//대상월
	String search_mm_col = "";		//대상월 컬럼명
	String search_id = "";			//해당자 사번
	String mod_id = "";				//수정할 관리코드 to geuntae_count
	/*********************************************************************
	 	근태 관리코드 출력하기(yangsic_env)
	*********************************************************************/	
	String[] ysColumn = {"ys_name","ys_value"};
	bean.setTable("yangsic_env");			
	bean.setColumns(ysColumn);
	bean.setOrder("ys_name ASC");	
	query = "where (ys_name like 'BT_%') or (ys_name like 'OT_%') or(ys_name like 'HD_%')";
	bean.setSearchWrite(query);
	bean.init_write();

	int cnt = bean.getTotalCount();
	String[][] items = new String[cnt][2];
	int n = 0;
	while(bean.isAll()) {
		items[n][0] = bean.getData("ys_name");		//관리코드
		items[n][1] = bean.getData("ys_value");		//관리항목
		//out.println(items[n][0] + " : " + items[n][1] + "<br>");
		n++;
	} //while

	/*********************************************************************
	// 	근태 마스터 정보 알아보기
	*********************************************************************/
	gt_id = request.getParameter("gt_id");	if(gt_id == null) gt_id = "";
	String[] Column = {"user_id","user_name","fellow_names","u_year","u_month","u_date",
						"tu_year","tu_month","tu_date","hd_var"};
	bean.setTable("geuntae_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (gt_id ='"+gt_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		user_id = bean.getData("user_id");			//작성자 사번
		user_name = bean.getData("user_name");		//작성자 명
		fellow_names = bean.getData("fellow_names");//동행자 사번/이름
		syear = bean.getData("u_year");				//시작 년
		smonth = fmt.toDigits(Integer.parseInt(bean.getData("u_month")));			//    월
		sdate = fmt.toDigits(Integer.parseInt(bean.getData("u_date")));				//    일
		edyear = bean.getData("tu_year"); if(edyear.length() == 0) edyear = syear;	//종료 년
		String edm = bean.getData("tu_month"); if(edm.length() == 0) edm = smonth;
		edmonth = fmt.toDigits(Integer.parseInt(edm));								//    월
		String edd = bean.getData("tu_date"); if(edd.length() == 0) edd = sdate;
		eddate = fmt.toDigits(Integer.parseInt(edd));								//    일
		hd_var = bean.getData("hd_var");			// 근태 관리 코드
	} //while
	//근태 종류 찾기
	for(int x=0; x<items.length; x++) if(items[x][0].equals(hd_var)) hd_name = items[x][1];
	
	//대상자 찾기
	if(fellow_names == null) fellow_names = "";
	if(fellow_names.length() != 0) {
		fellow_names = fellow_names.trim();
		StringTokenizer f = new StringTokenizer(fellow_names,";");
		int fcnt = f.countTokens() + 1;
		fellows = new String[fcnt][2];
		fellows[0][0] = user_id;
		fellows[0][1] = user_name;
		int i = 1;
		while(f.hasMoreTokens()) {
			String fn = f.nextToken();
			fn = fn.trim();
			StringTokenizer in = new StringTokenizer(fn,"/");
			int j = 0;
			while(in.hasMoreTokens()) {
				if(j == 0) fellows[i][0] = in.nextToken();
				else fellows[i][1] = in.nextToken();
				j++;
			}
			i++;
		}
	}
	else {
		fellows = new String[1][2];
		fellows[0][0] = user_id;
		fellows[0][1] = user_name;
	}

	//대상년도 찾기
	search_yy = request.getParameter("search_yy"); if(search_yy == null) search_yy = syear;
	int tyi = Integer.parseInt(edyear) - Integer.parseInt(syear) + 1;
	int[] tyear = new int[tyi];
	for(int x=0; x<tyi; x++) tyear[x] = Integer.parseInt(syear) + x;
	//for(int x=0; x<tyear.length; x++) out.println("tyear : "+ tyear[x] + "<br>");
	//대상월 찾기
	int[] tmon ;
	if(tyi == 1) {		//시작과 끝이 같은년도
		int tmi = Integer.parseInt(edmonth) - Integer.parseInt(smonth) + 1;
		tmon = new int[tmi];
		for(int x=0; x<tmi; x++) tmon[x] = Integer.parseInt(smonth) + x;
		//for(int x=0; x<tmon.length; x++) out.println("tmon : "+ tmon[x] + "<br>");
	}
	else { //시작과 끝 년도가 다른경우
		if(search_yy.equals(syear)) {				//시작년도
			int tmi = 13 - Integer.parseInt(smonth);
			tmon = new int[tmi];
			//out.println("tmi : " + tmi + "<br>");
			for(int x=Integer.parseInt(smonth),y=0; x<13; x++,y++) tmon[y] = x; 
		} else if (search_yy.equals(edyear)) {		//끝년도
			int tmi = Integer.parseInt(edmonth);
			tmon = new int[tmi];
			for(int x=1,y=0; x<=tmi; x++,y++) tmon[y] = x;  
		} else {									//중간년도
			int tmi = 12;
			tmon = new int[tmi];
			for(int x=1,y=0; x<=tmi; x++,y++) tmon[y] = x; 
		}
		//for(int x=0; x<tmon.length; x++) out.println("tmon : "+ tmon[x] + "<br>");
	}

	/*********************************************************************
	// 	근태 수량 정보 알아보기
	*********************************************************************/
	String[] cColumn = {"gt_id","jan1","feb2","mar3",
						"apr4","may5","jun6","jul7","aug8","sep9","oct10","nov11","dec12"};
	search_mm = request.getParameter("search_mm"); if(search_mm == null) search_mm = smonth;
	String pass = "0";
	for(int x=0; x<tmon.length; x++) {
		int sm = Integer.parseInt(search_mm);
		if(tmon[x] == sm) pass = "1";
	}
	if(pass.equals("0")) search_mm = fmt.toDigits(tmon[0]);

	if(search_mm.equals("01")) search_mm_col="jan1";
	else if(search_mm.equals("02")) search_mm_col = "feb2";
	else if(search_mm.equals("03")) search_mm_col = "mar3";
	else if(search_mm.equals("04")) search_mm_col = "apr4";
	else if(search_mm.equals("05")) search_mm_col = "may5";
	else if(search_mm.equals("06")) search_mm_col = "jun6";
	else if(search_mm.equals("07")) search_mm_col = "jul7";
	else if(search_mm.equals("08")) search_mm_col = "aug8";
	else if(search_mm.equals("09")) search_mm_col = "sep9";
	else if(search_mm.equals("10")) search_mm_col = "oct10";
	else if(search_mm.equals("11")) search_mm_col = "nov11";
	else if(search_mm.equals("12")) search_mm_col = "dec12";
	search_id = request.getParameter("search_id"); if(search_id == null) search_id = user_id;

	bean.setTable("geuntae_count");			
	bean.setColumns(cColumn);
	query = "where (hd_var ='"+hd_var+"') and (user_id ='"+search_id+"') and (thisyear='"+search_yy+"')";
	bean.setSearchWrite(query);
	bean.init_write();
	while(bean.isAll()) {
		mod_id = bean.getData("gt_id");
		search_cnt = bean.getData(search_mm_col);
	}

	/*********************************************************************
	// 	수정하기
	*********************************************************************/
	String req = request.getParameter("req"); if(req == null) req = "";
	if(req.equals("mod")) {
		search_cnt = request.getParameter("search_cnt");
		query = "update geuntae_count set "+search_mm_col+"='"+search_cnt;
		query += "' where gt_id='"+mod_id+"'";
		bean.execute(query);
		msg = "수정";
	}
%>

<script language=javascript>
<!--
var msg = '<%=msg%>'
if(msg.length != 0) self.close();


//대상자 선택
function selectUser()
{
	document.eForm.action="modifyHyuGaInfo.jsp";
	document.eForm.submit();
}
//대상년도
function selectYear()
{
	document.eForm.action="modifyHyuGaInfo.jsp";
	document.eForm.submit();

	document.eForm.action="modifyHyuGaInfo.jsp";
	document.eForm.submit();
	
}
//대상월
function selectMonth()
{
	document.eForm.action="modifyHyuGaInfo.jsp";
	document.eForm.submit();
}
//수정하기
function cntModify()
{
	document.eForm.action="modifyHyuGaInfo.jsp";
	document.eForm.req.value="mod";
	document.eForm.submit();
}
-->
</script>

<HTML><HEAD><TITLE>개인월별근태수량 수정</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--타이틀-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_geuntae_m.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
	
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody><form name="eForm" action="modifyHyuGaInfo.jsp" method="post" style="margin:0">
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_01" background="../images/bg-01.gif">대상자</td>
           <td width="70%" height="25" class="bg_04">
			<select name="search_id" width=50 onChange="javascript:selectUser();">
		<%
			for(int i=0; i<fellows.length; i++) {
				if(fellows[i][0].equals(search_id))
					out.println("<option selected value='"+fellows[i][0]+"'>"+fellows[i][1]+"</option>");
				else 
					out.println("<option value='"+fellows[i][0]+"'>"+fellows[i][1]+"</option>");
			}
		%></select>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
           <td width="30%" height="25" class="bg_01" background="../images/bg-01.gif">구분</td>
           <td width="70%" height="25" class="bg_04"><%=hd_name%></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
           <td width="30%" height="25" class="bg_01" background="../images/bg-01.gif">적용년월</td>
           <td width="70%" height="25" class="bg_04">
			<select name="search_yy" width=50 onChange="javascript:selectYear();">
		<%
			for(int i=0; i<tyear.length; i++) {
				if(tyear[i] == Integer.parseInt(search_yy))
					out.println("<option selected value='"+tyear[i]+"'>"+tyear[i]+"</option>");
				else
					out.println("<option value='"+tyear[i]+"'>"+tyear[i]+"</option>");
			}
		%>	</select>	   
			<select name="search_mm" width=50 onChange="javascript:selectMonth();">
		<%
			for(int i=0; i<tmon.length; i++) {
				if(tmon[i] == Integer.parseInt(search_mm))
					out.println("<option selected value='"+fmt.toDigits(tmon[i])+"'>"+fmt.toDigits(tmon[i])+"</option>");
				else 
					out.println("<option value='"+fmt.toDigits(tmon[i])+"'>"+fmt.toDigits(tmon[i])+"</option>");
			}
		%>	</select>	   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
           <td width="30%" height="25" class="bg_01" background="../images/bg-01.gif">수량</td>
           <td width="70%" height="25" class="bg_04"><input type='text' name='search_cnt' value='<%=search_cnt%>' size="5"></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table>
<input type='hidden' name='req' value=''>
<input type='hidden' name='gt_id' value='<%=gt_id%>'>		 
</form>

	<!--꼬릿말-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="Javascript:cntModify();"><img src='../images/bt_modify.gif' align='absmiddle' border='0'></a> <a href="Javascript:self.close();"><img src='../images/bt_close.gif' align='absmiddle' border='0'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>