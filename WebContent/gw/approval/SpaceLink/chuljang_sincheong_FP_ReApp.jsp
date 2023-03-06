<%@ include file="../../../admin/configHead.jsp"%>
<%@ 	page		
	info= "출장신청서 2차부서 결재상신"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.util.normalFormat"
	import="com.anbtech.text.*"
	import="com.anbtech.gw.entity.*"
	import="com.anbtech.gw.db.*"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// 공통 변수
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//출력형식
	normalFormat money = new com.anbtech.util.normalFormat("#,###");		//출력형식 (비용)
	StringProcess str = new com.anbtech.text.StringProcess();				//문자열 다루기
	AppMasterDetailDAO masterDAO = new com.anbtech.gw.db.AppMasterDetailDAO(); //전자결재내용 & 결재선

	//출장신청서 내용관련
	String query = "";
	String div_name = "";			//부서명
	String prj_code = "";			//project code
	String user_id = "";			//대상자 id
	String user_name = "";			//대상자 명
	String fellow_names = "";		//동행자	 사번/이름;
	String f_names = "";			//동행자	 이름,
	String bistrip_kind = "";		//국내/국외 구분
	String bistrip_country = "";	//국가명
	String bistrip_city = "";		//도시명
	String traffic_way = "";		//교통편
	String purpose = "";			//사유
	String syear = "";				//시작 년
	String smonth = "";				//    월
	String sdate = "";				//    일
	String edyear = "";				//종료 년
	String edmonth = "";			//    월
	String eddate = "";				//    일
	String rec = "";				//인수인계자
	String tel = "";				//긴급연락처
	String bank_no = "";			//계좌번호
	String receiver_id = "";		//출장비 영수인 id
	String receiver_name = "";		//출장비 영수인 명
	String doc_date = "";			//작성 년월일
	String wyear = "";				//작성년
	String wmonth = "";				//	  월
	String wdate = "";				//	  일
	int period_n = 0;				//from ~ to 기간 : 박
	int period = 0;					//from ~ to 기간 : 일

	//결재선 관련
	String doc_id = "";				//전자결재 관리번호
	String link_id = "";			//관련문서 관리번호
	String line="";					//읽은문서 결재선
	String r_line = "";				//재작성으로 넘겨주기
	String vdate = "";				//검토자 검토 일자
	String ddate = "";				//승인자 승인 일자
	String wid = "";				//기안자사번
	String vid = "";				//검토자사번
	String did = "";				//승인자사번
	String wname = "";				//기안자
	String vname = "";				//검토자
	String dname = "";				//승인자
	String PROCESS = "";			//PROCESS
	String doc_ste = "";			//doc_ste

	//2차측 결재 관련
	String line2="";				//읽은문서 결재선
	String writer_id = "";			//등록자 사번
	String writer_name = "";		//등록자 명

	/*********************************************************************
	 	등록자(login) 알아보기
	*********************************************************************/	
	String[] uColumn = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code"};
	bean.setTable("user_table a,class_table b,rank_table c");			
	bean.setColumns(uColumn);
	bean.setOrder("a.id ASC");	
	query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		writer_id = login_id;							//등록자 사번
		writer_name = bean.getData("name");				//등록자 명
	} //while

	//*********************************************************************
	// 2차 결재상신할 관련문서 관리번호 내용 받기
	//*********************************************************************
	link_id = request.getParameter("link_id");	if(link_id == null) link_id = "";	//관련문서 관리번호

	//*********************************************************************
	// 1차 주관부서 결재선 내용 받기
	//*********************************************************************
	String cmt = "";
	masterDAO.getTable_MasterPid(link_id);	
	TableAppLine app = new com.anbtech.gw.entity.TableAppLine();	
	ArrayList table_line = new ArrayList();				//결재선
	table_line = masterDAO.getTable_line();		
	Iterator line_iter = table_line.iterator();
	while(line_iter.hasNext()) {
		app = (TableAppLine)line_iter.next();

		//결재선
		cmt = app.getApComment(); if(cmt == null) cmt = "";
		if(cmt.length() != 0) {
			cmt = "\r    "+cmt; 
		}
										
		if(app.getApStatus().equals("기안")) {
			wname = app.getApName();	//기안자
			wid = app.getApSabun();	//기안자 사번
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"\r";
		}
		if(app.getApStatus().equals("검토"))  {
			vname = app.getApName();	//검토자
			vid = app.getApSabun();	//검토자 사번
			vdate = app.getApDate();	//검토자 검토일자 (있으면 결재하고 없으면 결재않됨)
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"\r";
		}
		if(app.getApStatus().equals("승인"))  {
			dname = app.getApName();	//승인자
			did = app.getApSabun();		//승인자 사번
			ddate = app.getApDate();	//승인자 승인일자 (있으면 결재하고 없으면 결재않됨)\
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"\r";
		}
		if(app.getApStatus().equals("통보"))  {	
			line += app.getApStatus()+" "+app.getApName()+"("+app.getApRank()+")"+app.getApDivision()+" "+app.getApDate()+" "+cmt+"\r";
		}
	}

	/*********************************************************************
	 	출장비 항목 정보
	*********************************************************************/	
	String[] csColumn = {"code","code_name"};
	bean.setTable("system_minor_code");			
	bean.setColumns(csColumn);
	bean.setClear();
	bean.setOrder("code DESC");	
	query = "WHERE type = 'BT_COST'";
	bean.setSearchWrite(query);
	bean.init_write();

	int cnt = bean.getTotalCount();
	String[][] btrip = new String[cnt][5];

	int i = 0;
	while(bean.isAll()) {
		btrip[i][0] = bean.getData("code");				//출장관리코드
		btrip[i][1] = bean.getData("code_name");		//출장관리명
		i++;
	} //while

	//출장비용 찾기
	int sum = 0;			//신청비용 합계
	int ep_sum = 0;			//지급비용 합계
	String[] costColumn = {"gt_id","at_var","gt_cost","cost_cont","ep_cost"};
	bean.setTable("geuntae_account");
	bean.setColumns(costColumn);
	bean.setOrder("at_var ASC");
	for(int c=0; c<i; c++) {
		bean.setSearch("gt_id",link_id,"at_var",btrip[c][0]);
		bean.init_unique();
		if(bean.isAll()) {
			btrip[c][2] = bean.getData("gt_cost");		//출장금액
			btrip[c][3] = bean.getData("cost_cont");	//출장금액 내역
			btrip[c][4] = bean.getData("ep_cost");		//지급금액 내역
			//비용합계 계산하기
			btrip[c][2] = str.repWord(btrip[c][2],",","");
			sum += Integer.parseInt(btrip[c][2]);
			btrip[c][4] = str.repWord(btrip[c][4],",","");
			ep_sum += Integer.parseInt(btrip[c][4]);
		}
		else {
			btrip[c][2] = "000";
			btrip[c][3] = "";
			btrip[c][4] = "000";
		}

	}


	/*********************************************************************
	// 	출장 정보 알아보기
	*********************************************************************/	
	String[] Column = {"ac_name","user_id","user_name","fellow_names","prj_code","gt_purpose","u_year","u_month","u_date",
						"tu_year","tu_month","tu_date","gt_dest","country_class","gt_country",
						"traffic_way","receiver_id","receiver_name","proxy","em_tel","bank_no","in_date"};
	bean.setTable("geuntae_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (gt_id ='"+link_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		div_name = bean.getData("ac_name");			//부서명
		user_id = bean.getData("user_id");			//작성자 사번
		user_name = bean.getData("user_name");		//작성자 명
		fellow_names = bean.getData("fellow_names");//동행자 사번/이름
		prj_code = bean.getData("prj_code");		//project code
		purpose = bean.getData("gt_purpose");		//사유
		syear = bean.getData("u_year");				//시작 년
		smonth = fmt.toDigits(Integer.parseInt(bean.getData("u_month")));			//    월
		sdate = fmt.toDigits(Integer.parseInt(bean.getData("u_date")));				//    일
		edyear = bean.getData("tu_year");			//종료 년
		edmonth = fmt.toDigits(Integer.parseInt(bean.getData("tu_month")));			//    월
		eddate = fmt.toDigits(Integer.parseInt(bean.getData("tu_date")));			//    일
		bistrip_city = bean.getData("gt_dest");		//출장지 도시명
		bistrip_kind = bean.getData("country_class");	//국내국외구분
		bistrip_country = bean.getData("gt_country");	//국가명
		traffic_way = bean.getData("traffic_way");		//교통편
		receiver_id = bean.getData("receiver_id");		//영수인 id
		receiver_name = bean.getData("receiver_name");	//영수인명
		rec = bean.getData("proxy");				//인수인계자
		tel = bean.getData("em_tel");				//긴급연락처
		bank_no = bean.getData("bank_no");			//계좌번호
		doc_date = bean.getData("in_date");			//작성년월일
	} //while

	//기간구하기
	period_n = anbdt.getPeriodDate(Integer.parseInt(syear),Integer.parseInt(smonth),Integer.parseInt(sdate),Integer.parseInt(edyear),Integer.parseInt(edmonth),Integer.parseInt(eddate));
	period = period_n + 1;

	//작성년월일 구하기
	wyear = doc_date.substring(0,4);		//작성년
	wmonth = doc_date.substring(5,7);		//	  월
	wdate = doc_date.substring(8,10);		//	  일

	//동행자 이름만 구하기
	StringTokenizer names = new StringTokenizer(fellow_names,";");
	while(names.hasMoreTokens()) {
		String nms = names.nextToken();
		if(nms.length() < 3) break;
		
		StringTokenizer name = new StringTokenizer(nms,"/");
		int nm = 0;
		while(name.hasMoreTokens()) {
			String n = name.nextToken();
			if(nm == 1) f_names += n + ",";
			nm++;
			if(nm > 2) break;
		}
	}
	if(f_names.length() != 0) f_names = f_names.substring(0,f_names.length()-1);

	/*********************************************************************
	 	내부 데이터 처리
	*********************************************************************/	
	line2 = request.getParameter("doc_app_line"); if(line2 == null) line2 = "";	//결재선
%>

<html>
<head>
<meta http-equiv="Content-Language" content="euc-kr">
<title>출장신청서(통제부서용)</title>
<link rel="stylesheet" href="../../css/style.css" type="text/css">
</head>
<style type="text/css">
<!--
.money {
	BORDER-RIGHT: #a4a4a4 1px solid; BORDER-TOP: #a4a4a4 1px solid; BORDER-LEFT: #a4a4a4 1px solid; BORDER-BOTTOM: #a4a4a4 1px solid; TEXT-ALIGN: right
}
-->
</style>

<BODY leftmargin='0' topmargin='0' marginwidth='0' marginheight='0'>
<form action="chuljang_sincheong_FP_ReApp.jsp" name="eForm" method="post" encType="multipart/form-data">

<table border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../../images/blet.gif"> 출장신청서 [집행부서]</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<a href="Javascript:eleApprovalManagerLineSelect();"><img src='../../images/bt_sel_line.gif' align='middle' border='0'></a> 
					<a href="Javascript:eleApprovalRequest();"><img src='../../images/bt_sangsin.gif' align='middle' border='0'></a> 
					<a href='Javascript:winprint();'><img src='../../images/bt_print.gif' align='middle' border='0'></a> 
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- 결재 정보 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">메<p>모</TD>
		<TD noWrap width=60% align=left><TEXTAREA NAME="" rows=6 cols=66 readOnly style="border:0"><%=line%></TEXTAREA></TD>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">결<p>재</TD>
		<TD noWrap width=40% align=left><!-- 결재칸-->
			<TABLE cellSpacing=1 cellPadding=0 width="100%" border=0>
			<TBODY>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07">기안자</TD>
					<TD noWrap width=80 align=middle class="bg_07">검토자</TD>
					<TD noWrap width=80 align=middle class="bg_07">승인자</TD></TR>
				<TR vAlign=middle height=50>
					<TD noWrap width=80 align=middle class="bg_06">
						<img src="../../../gw/approval/sign/<%=wid%>.gif" width=60 height=50 align="center"><img src='' width='0' height='0'>					
					</TD>
					<TD noWrap width=80 align=middle class="bg_06">
					<% //검토자 승인자 싸인 표시하기 (단, 반려문서가 아닌경우만)
						if(vdate.length() == 0)	{//검토자
							if(ddate.length() == 0) out.println("&nbsp;");
							else out.println("전결");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + vid + ".gif' width=60 height=50 align='center'>");
						}
					%>												
					</TD>
					<TD noWrap width=80 align=middle class="bg_06">
					<%
						if(ddate.length() == 0)	{//승인자
							out.println("&nbsp;");
						} else {
							out.println("<img src='../../../gw/approval/sign/" + did + ".gif' width=60 height=50 align='center'>");
						}
					%>						
					</TD></TR>
				<TR vAlign=middle height=21>
					<TD noWrap width=80 align=middle class="bg_07"><%=wname%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=vname%><img src='' width='0' height='0'></TD>
					<TD noWrap width=80 align=middle class="bg_07"><%=dname%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE>	<!-- 결재칸 끝 -->	
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
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">출장자</td>
           <td width="37%" height="25" class="bg_04"><%=user_name%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">시작일</td>
           <td width="37%" height="25" class="bg_04"><%=syear%>년 <%=smonth%>월 <%=sdate%>일</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">종료일</td>
           <td width="37%" height="25" class="bg_04"><%=edyear%>년 <%=edmonth%>월 <%=eddate%>일</td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">출장일수</td>
           <td width="37%" height="25" class="bg_04"><%=period_n%>박 <%=period%> 일간</td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">출장지</td>
           <td width="37%" height="25" class="bg_04"><%=bistrip_kind%> : <%=bistrip_city%> <%=bistrip_country%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">출장목적</td>
           <td width="37%" height="25" class="bg_04"><%=purpose%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">교통편</td>
           <td width="37%" height="25" class="bg_04"><%=traffic_way%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">프로젝트코드</td>
           <td width="37%" height="25" class="bg_04"><%=prj_code%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">동행자</td>
           <td width="37%" height="25" class="bg_04"><%=f_names%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">업무인수자</td>
           <td width="37%" height="25" class="bg_04"><%=rec%></td>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">긴급연락처</td>
           <td width="37%" height="25" class="bg_04"><%=tel%></td></tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">출 장 비<br>청 구</td>
           <td width="87%" height="25" class="bg_04" colspan="3">
		   <%
				out.print("<TABLE cellSpacing=2 cellPadding=0 width=90% border=0 bordercolordark=white bordercolorlight=#9CA9BA>");
				out.print("<tr><td height=23 class=bg_05 width='15%' align='center'>항목</td>"); 
				out.print("<td class=bg_05 width='20%' align='center'>청구금액(원)</td>"); 
				out.print("<td class=bg_05 width='20%' align='center'>지급금액(원)</td>"); 
				out.print("<td class=bg_05 width='45%' align='center'>청구금액산출내역</td></tr>");

				for(int n=1,p=0; p < cnt; n++,p++) {
					out.print("<tr><td class=bg_07 align='center'><input type='hidden' name='code"+n+"' value='"+btrip[p][0]+"'>");
					out.print(btrip[p][1]+"</td>");
					out.print("<td class=bg_07><input class='money' size=15 type='text' name='cost"+n+"' value='"+money.StringToString(btrip[p][2])+"' style='text-align:right;' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this);' readonly></td>");//청구금액(금액)
					out.print("<td class=bg_07><input class='money' size=15 type='text' name='ep_cost"+n+"' value='"+money.StringToString(btrip[p][2])+"' style='text-align:right;' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this);'></td>");//지급금액(금액)
					out.print("<td class=bg_07><input size=40 type='text' name='cont"+n+"' value='"+btrip[p][3]+"' readonly></td></tr>");
				}
				out.print("<tr><td class=bg_07 align='center'><b>합계</b></td><td class=bg_07 align='right'> &nbsp;"+money.toDigits(sum)+"&nbsp;</td>");
				out.print("<td class=bg_07><input class='money' size=15 type='text' name='ep_sum' value='"+money.toDigits(sum)+"' readonly></td></tr>");
				out.print("</table>");
		   %>
		   </td>
         </tr>
         <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">신청일자</td>
           <td width="37%" height="25" class="bg_04"><%=wyear%>년 <%=wmonth%>월 <%=wdate%>일</td>
		   <td width="13%" height="25" class="bg_03" background="../../images/bg-01.gif">계좌번호</td>
           <td width="37%" height="25" class="bg_04"><%=bank_no%></td>
		 </tr>
		 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
         <tr><td height=10 colspan="4"></td></tr></tbody></table>

</td></tr></table>

<!-- 2차 결재 정보 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
<TBODY>
	<tr bgcolor="c7c7c7"><td height=1 colspan="6"></td></tr>
	<TR vAlign=middle height=23>
		<TD noWrap width=40 align=middle class="bg_03" background="../../images/bg-01.gif">결<br>재<br>선</TD>
		<TD noWrap width=64% align=left><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=line2%></TEXTAREA></TD>
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
<input type='hidden' name='doc_id' value='<%=bean.getID()%>'>
<input type='hidden' name='link_id' value='<%=link_id%>'>
<input type='hidden' name='date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='doc_sub' value='프로젝트 <%=prj_code%> 에 대한 출장비 지급처리 요청 [<%=user_name%>]'>
<input type='hidden' name='doc_per' value='1'>
<input type='hidden' name='doc_sec' value='GENER'>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='app_mode' value=''>
<input type='hidden' name='writer_id' value='<%=writer_id%>'>
<input type='hidden' name='writer_name' value='<%=writer_name%>'>
<input type='hidden' name='period' value='<%=period%>'>
<input type='hidden' name='account_cnt' value='<%=i%>'>
</form>  

</body>
</html>

<script language=javascript>
<!--
//결재선 지정지정 
function eleApprovalManagerLineSelect()
{
	var target="eForm.doc_app_line&anypass=Y" 
	wopen("../eleApproval_Share.jsp?target="+target,"eleA_app_search_select","520","467","scrollbars=no,toolbar=no,status=no,resizable=no");
	
}

//결재 상신 
function eleApprovalRequest()
{
	if (document.eForm.doc_app_line.value =="") { alert("결재선을 입력하십시요."); return; }
	
	 //결재선 검사
	data = document.eForm.doc_app_line.value;		//결재선 내용
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

	document.onmousedown=dbclick;

	//일괄합의 결재상신진행
	document.eForm.action='../../../servlet/GeunTaeServlet';
	document.eForm.mode.value='CHULJANG_SINCHEONG_SEC';	
	document.eForm.app_mode.value='REQ';	
	document.eForm.submit();

}

//출력하기
function winprint()
{
	document.all['print'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
}
//닫기
function winClose()
{
	window.returnValue='';
	self.close();
}
//창띄우기 공통
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}
//금액 천단위에 콤마넣기
function InputMoney(input){
	str = input.value;
	str = unComma(str);
	str = Comma(str);
	input.value = str;

	sumMoney();
}
//총액표시하기
function sumMoney(){
	var n = '<%=cnt%>';
	var ep_sum = 0;
	for(i=0,j=1; i<n; i++,j++) {
		ep_sum += unCommaObj(eval("document.eForm.ep_cost"+j+".value"));
	}
	document.eForm.ep_sum.value=Comma(ep_sum);
}
function isNumObj(obj)
{
	for (var i = 0; i < obj.value.length ; i++){
		chr = obj.value.substr(i,1);		
		chr = escape(chr);
		key_eg = chr.charAt(1);
		if (key_eg == 'u'){
			key_num = chr.substr(i,(chr.length-1));			
			if((key_num < "AC00") || (key_num > "D7A3")) { 
				event.returnValue = false;
			} 			
		}
	}
	if (event.keyCode >= 48 && event.keyCode <= 57) {
		
	} else {
		event.returnValue = false;
	}
}
function Comma(num) {
	re = /(\d+)/;
	if(re.test(num)){ 
		re.exec(num); num = RegExp.$1; 
		re = /(\d+)(\d{3})/;
		while(re.test(num)){ num = num.replace(re,"$1,$2"); }
	}
    return (num);
}
function unComma(str) {
	return str.replace(/,/g,"");
}

//obj로받아 콤마 없애기
function unCommaObj(input) {
   var inputString = new String;
   var outputString = new String;
   var outputNumber = new Number;
   var counter = 0;
   if (input == '')
   {
	return 0
   }
   inputString=input;
   outputString='';
   for (counter=0;counter <inputString.length; counter++)
   {
      outputString += (inputString.charAt(counter) != ',' ?inputString.charAt(counter) : '');
   }
   outputNumber = parseFloat(outputString);
   return (outputNumber);
}

// 더블클릭 방지
function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
-->
</script>