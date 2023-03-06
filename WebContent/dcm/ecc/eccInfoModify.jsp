<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= "ECR부터 시작된 ECO 수정하기"		
	contentType = "text/html; charset=euc-kr" 		
	import		= "java.io.*"
	import		= "java.util.*"
	import		= "com.anbtech.text.*"
	import		= "com.anbtech.dcm.entity.*"
	import		= "com.anbtech.bm.entity.*"
	import		= "java.sql.Connection"
	import		= "com.anbtech.date.anbDate"
	import		= "com.anbtech.text.StringProcess"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//----------------------------------------------------
	//	parameter 선언
	//----------------------------------------------------
	String pid="",ecr_id="",ecr_name="",ecr_code="",ecr_div_code="",ecr_div_name="",ecr_tel="";
	
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();
	String todate = anbdt.getDateNoformat();				//오늘날자

	//----------------------------------------------------
	// 	접속자 login 알아보기
	//----------------------------------------------------
	ecr_id = sl.id; 		//접속자 login id
	
	String[] idColumn = {"a.id","a.name","b.code","b.ac_code","b.ac_name","a.office_tel"};
	bean.setTable("user_table a,class_table b");			
	bean.setColumns(idColumn);
	bean.setOrder("a.id ASC");	
	String item_data = "where (a.id ='"+ecr_id+"' and a.ac_id = b.ac_id)";
	bean.setSearchWrite(item_data);
	bean.init_write();

	if(bean.isAll()) {
		ecr_name = bean.getData("name");			//접속자 명
		ecr_code = bean.getData("code");			//접속자 부서관리코드
		ecr_div_code = bean.getData("ac_code");		//접속자 부서코드
		ecr_div_name = bean.getData("ac_name");		//접속자 부서명
		ecr_tel = bean.getData("office_tel");		//접속자 전화번호
	} //while

	//----------------------------------------------------
	//	ECC COM 읽기
	//----------------------------------------------------
	com.anbtech.dcm.entity.eccComTable ecc;
	ecc = (eccComTable)request.getAttribute("COM_List");
	
	//----------------------------------------------------
	//	ECC ORD 읽기
	//----------------------------------------------------
	com.anbtech.dcm.entity.eccOrdTable eco;
	eco = (eccOrdTable)request.getAttribute("ORD_List");

	//----------------------------------------------------
	//	설계변경 선택항목[F01:변경사유] 데이터 읽기
	//----------------------------------------------------
	com.anbtech.bm.entity.mbomEnvTable ecr;
	ArrayList ecr_list = new ArrayList();
	ecr_list = (ArrayList)request.getAttribute("ECR_List");
	ecr = new mbomEnvTable();
	Iterator ecr_iter = ecr_list.iterator();

	//----------------------------------------------------
	//	설계변경 선택항목[F02:적용구분] 데이터 읽기
	//----------------------------------------------------
	com.anbtech.bm.entity.mbomEnvTable ecf;
	ArrayList ecf_list = new ArrayList();
	ecf_list = (ArrayList)request.getAttribute("ECF_List");
	ecf = new mbomEnvTable();
	Iterator ecf_iter = ecf_list.iterator();

	//----------------------------------------------------
	//	설계변경 선택항목[F03:적용범위] 데이터 읽기
	//----------------------------------------------------
	com.anbtech.bm.entity.mbomEnvTable ecs;
	ArrayList ecs_list = new ArrayList();
	ecs_list = (ArrayList)request.getAttribute("ECS_List");
	ecs = new mbomEnvTable();
	Iterator ecs_iter = ecs_list.iterator();

	//----------------------------------------------------
	//	설계변경 선택항목[F04:업무구분] 데이터 읽기
	//----------------------------------------------------
	com.anbtech.bm.entity.mbomEnvTable eck;
	ArrayList eck_list = new ArrayList();
	eck_list = (ArrayList)request.getAttribute("ECK_List");
	eck = new mbomEnvTable();
	Iterator eck_iter = eck_list.iterator();

	//----------------------------------------------------
	//	첨부파일 읽기
	//----------------------------------------------------
	String bon_path = "/dcm/"+ecc.getEcrId();	//첨부파일 저장 확장path
	String fname = eco.getFname();			//첨부파일명
	String sname = eco.getSname();			//첨부파일 저장명
	String ftype = eco.getFtype();			//첨부파일Type
	String fsize = eco.getFsize();			//첨부파일Size
	int attache_cnt = 4;					//첨부파일 최대갯수 (미만)
	

	//첨부파일 개별로 읽기
	int cnt = 0;
	for(int i=0; i<fname.length(); i++) if(fname.charAt(i) == '|') cnt++;

	String[][] addFile = new String[cnt][5];
	for(int i=0; i<cnt; i++) for(int j=0; j<5; j++) addFile[i][j]="";

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

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../dcm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//수정하기하기
function saveModify()
{
	var f = document.eForm;

	var ecc_subject = f.ecc_subject.value;
	if(ecc_subject == '') { alert('설계변경제목이 입력되지 않았습니다.'); f.ecc_subject.focus(); return; }
	
	var fg_code = f.fg_code.value;
	if(fg_code == '') { alert('문제 발생모델(FG코드)이 입력되지 않았습니다.'); f.fg_code.focus(); return; }

	var order_date = f.order_date.value;
	if(order_date == '') { alert('적용일이 입력되지 않았습니다.'); f.order_date.focus(); return; }

	var chg_position = f.chg_position.value;
	if(chg_position == '') { alert('문제발생부분이 입력되지 않았습니다.'); f.chg_position.focus(); return; }
	
	var condition = f.condition.value;
	if(condition == '') { alert('원인및현상이 입력되지 않았습니다.'); f.condition.focus(); return; }

	var solution = f.solution.value;
	if(solution == '') { alert('ECO적용 내용이 입력되지 않았습니다.'); f.solution.focus(); return; }
	
	//일자에서 문자 특수문자 삭제하기
	var order_date = document.eForm.order_date.value;
	for(i=0; i<2; i++) order_date = order_date.replace('/','');

	var todate = '<%=todate%>';
	if(todate > order_date) { alert('적용일은 오늘일자보다 큰 일자를 선택하십시오.'); f.order_date.focus(); return; }

	//데이터 저장하기
	document.all['lding'].style.visibility="visible";	//처리중 메시지 출력

	document.eForm.action='../servlet/CbomProcessServlet';
	document.eForm.mode.value='ecc_modify';
	document.eForm.order_date.value=order_date;
	document.eForm.submit();
}
//문제발생부품 찾기
function searchItems()
{
	//대표 FG코드만 선택한다:첫번째를 대표FG로 간주
	var fg_list = document.eForm.fg_code.value;
	var fgc = fg_list.split('\n');
	var fg_code = fgc[0];
	if(fg_code.length == 0) { alert('문제 발생모델(FG)을 먼저 입력하십시요.'); return; }
	wopen("../servlet/CbomBaseInfoServlet?mode=pl_list&fg_code="+fg_code,"proxy","700","400","scrollbars=no,toolbar=no,status=no,resizable=yes");

}
//삭제하기
function delECC()
{
	document.eForm.action='../servlet/CbomProcessServlet';
	document.eForm.mode.value='ecc_delete';
	document.eForm.submit();
}
//ECR 내용보기
function goECR()
{
	var pid = document.eForm.pid.value;	wopen("../servlet/CbomProcessServlet?mode=req_view&pid="+pid,"ecrview","700","360","scrollbars=yes,toolbar=no,status=no,resizable=yes");

}
//BOM변경작업
function chgBOM()
{
	//대표모델이 있는지 검사
	var fg_list = document.eForm.fg_code.value;
	var fgc = fg_list.split('\n');
	var fg_code = fgc[0];
	if(fg_code.length == 0) { alert('문제 발생모델(FG)을 먼저 입력하십시요.'); return; }

	//변경내용 저장했는지 확인하기
	if(!confirm('변경내용을 저장하셨습니까? 계속진행하려면 확인을 선택하십시오')) { return; }

	//BOM변경작업이 가능한지 판단하기
	var status = '<%=ecc.getEccStatus()%>';
	if(status != '6') { 
		alert('BOM변경작업을 할 수 없는 상태입니다. 먼저 내용을 수정후 진행하십시오.'); return;
	}

	var para = "fg_code="+fg_code+"&eco_no=<%=ecc.getEcoNo()%>";
	document.eForm.action='../dcm/ecc/CbomFrame.jsp?'+para;
	document.eForm.submit();
}
//유효일자 찾기
function OpenCalendar(FieldName) {
	var strUrl = "../dcm/Calendar.jsp?FieldName=" + FieldName;
	newWIndow = window.open(strUrl, "Calendar", "width=0, height=0");
}

//창띄우기 공통
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}
-->
</script>
<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<form name="eForm" method="post" style="margin:0" encType="multipart/form-data">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../dcm/images/blet.gif"> ECO 작성</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=30><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%'>
					<a href="javascript:saveModify();"><img src="../dcm/images/bt_reg.gif" border=0></a>
					<a href="javascript:delECC();"><img src="../dcm/images/bt_del.gif" border=0></a>
					<a href="javascript:chgBOM();"><img src="../dcm/images/bt_bom_modify.gif" border='0' alt='BOM편집'></a>
					</TD>
				</TR></TBODY></TABLE></TD></TR>
	<TR><TD height='2' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--내용-->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR>
		<TD align="center">
		<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
			<TBODY>
			 <!-- 기초정보 작성 -->
			<TR>
				<TD height="25" colspan="4"><img src="../dcm/images/basic_info.gif" width="209" height="25" border="0"></TD></TR>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">제 목</TD>
			   <TD width="87%" height="25" class="bg_04" colspan=3><%=ecc.getEccSubject()%>
					<INPUT type="hidden" name="ecc_subject" value="<%=ecc.getEccSubject()%>" size="80" class="bg_05"></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">작성자</TD>
			   <TD width="37%" height="25" class="bg_04"><%=ecr_name%>
					<INPUT type="hidden" name="ecr_name" value="<%=ecr_name%>" size="20">
					<INPUT type="hidden" name="ecr_div_name" value="<%=ecr_div_name%>" size="30"></TD>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">ECO NO</TD>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEcoNo()%></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">전화번호</TD>
			   <TD width="37%" height="25" class="bg_04"><%=ecr_tel%>
					<INPUT type="hidden" name="ecr_tel" value="<%=ecr_tel%>" size="20"></TD>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">작성일</TD>
			   <TD width="37%" height="25" class="bg_04"><%=anbdt.getDate()%>
					<INPUT type="hidden" name="ecr_date" value="<%=ecc.getEcrDate()%>" size="30"></TD>
			</TR> 
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">검토책임자</TD>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getMgrName()%></TD>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">검토담당자</TD>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEcoName()%></TD>
			</TR>
			<!-- 상세정보 작성 -->
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
				<TD height="25" colspan="4"><img src="../dcm/images/detailed_info.gif" width="209" height="25" border="0"></TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">변경사유</TD>
			   <TD width="37%" height="25" class="bg_04">
					<SELECT name="ecc_reason" class="bg_05"> 
					<OPTGROUP label='---------------'>
					<%
						String rsel="",ecc_reason=ecc.getEccReason();
						while(ecr_iter.hasNext()) {
							ecr = (mbomEnvTable)ecr_iter.next(); 
							if(ecc_reason.equals(ecr.getSpec())) rsel = "selected";
							else rsel = "";
							out.print("<option "+rsel+" value='"+ecr.getSpec()+"'>");
							out.println(ecr.getSpec()+"</option>");
						} 
					%></select></TD>
				<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">적용구분</TD>
			    <TD width="37%" height="25" class="bg_04">
					<select name="ecc_factor" class="bg_05"> 
					<OPTGROUP label='---------------'>
					<%
						String fsel="",ecc_factor=ecc.getEccFactor();
						while(ecf_iter.hasNext()) {
							ecf = (mbomEnvTable)ecf_iter.next(); 
							if(ecc_factor.equals(ecf.getSpec())) fsel = "selected";
							else fsel = "";
							out.print("<option "+fsel+" value='"+ecf.getSpec()+"'>");
							out.println(ecf.getSpec()+"</option>");
						} 
					%></select></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">적용범위</TD>
			   <TD width="37%" height="25" class="bg_04">
					<select name="ecc_scope" class="bg_05"> 
					<OPTGROUP label='---------------'>
					<%
						String ssel="",ecc_scope=ecc.getEccScope();
						while(ecs_iter.hasNext()) {
							ecs = (mbomEnvTable)ecs_iter.next(); 
							if(ecc_scope.equals(ecs.getSpec())) ssel = "selected";
							else ssel = "";
							out.print("<option "+ssel+" value='"+ecs.getSpec()+"'>");
							out.println(ecs.getSpec()+"</option>");
						} 
					%></select></TD>
				<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">업무구분</TD>
			    <TD width="37%" height="25" class="bg_04"><%=ecc.getEccKind()%>
					<INPUT type="hidden" name="ecc_kind" value="<%=ecc.getEccKind()%>" size="30"></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">제품군</TD>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getPdgCode()%>
					<INPUT type='hidden' name='pdg_code' value='<%=ecc.getPdgCode()%>' size='20' class="bg_05" readonly></TD>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">제품</TD>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getPdCode()%>
					<INPUT type='hidden' name='pd_code' value='<%=ecc.getPdCode()%>' size='20' class="bg_05" readonly></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">문제발생모델(FG)</TD>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="fg_code" rows='4' cols='40' class="bg_05" readonly><%=ecc.getFgCode()%></TEXTAREA></TD>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">문제발생부품<br><a href="Javascript:searchItems();"><img src="../dcm/images/bt_search.gif" border="0" align="absmiddle"></a></TD>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="part_code" rows='4' cols='40' readonly><%=ecc.getPartCode()%></TEXTAREA></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">적용일</TD>
			   <TD width="87%" height="25" class="bg_04" colspan='3'>
					<INPUT type="text" name="order_date" value="<%=anbdt.getSepDate(ecc.getOrderDate(),"/")%>" size="20" class="bg_05" readonly><A Href="Javascript:OpenCalendar('order_date');"><img src="../dcm/images/bt_calendar.gif" border="0" align='absmiddle'></A></TD>
			</TR> 
			<!-- ECO 작성 -->
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
				<TD height="25" colspan="4">ECO내용 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
					<a href='Javascript:goECR();'>[ECR내용]</a> </TD></TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">문제분류</TD>
			   <TD width="37%" height="25" class="bg_04">
					<select name="trouble" class="bg_05"> 
					<OPTGROUP label='---------------'>
					<%
						String[] trb_data = {"부품","동작","공정","성능"};
						String tsel="",trouble=eco.getTrouble();
						for(int i=0; i<trb_data.length; i++) {
							if(trouble.equals(trb_data[i])) tsel = "selected";
							else tsel = "";
							out.print("<option "+tsel+" value='"+trb_data[i]+"'>");
							out.println(trb_data[i]+"</option>");
						} 
					%></select>
					</TD>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">문제발생부분</TD>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT type='text' name='chg_position' value='<%=eco.getChgPosition()%>' size='20' class="bg_05"></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">현상및원인</TD>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="condition" rows='4' cols='40' class="bg_05"><%=eco.getCondition()%></TEXTAREA></TD>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">ECO적용내용</TD>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="solution" rows='4' cols='40' class="bg_05"><%=eco.getSolution()%></TEXTAREA></TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">첨부파일</TD>
			   <TD width="87%" height="25" class="bg_04" colspan="3">
				<% 
				for(int i=0,no=1; no<attache_cnt; i++,no++) {
					out.println("<INPUT type='file' name='attachfile"+no+"' size='60'>");
					if((i<cnt) && (addFile[i][0].length() != 0)) {		//등록된 첨부가 있을때
						out.print("&nbsp;<INPUT type='checkbox' name='delfile"+no+"' value='"+addFile[i][3]+"'>");
						out.print("<a href='../dcm/attach_download.jsp?fname="+addFile[i][0]);
						out.print("&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]);
						out.print("&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a>");
						out.println("<br>"); 
					}
				}
				%>		   
			   </TD>
			</TR>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
</TABLE>
<INPUT type='hidden' name='mode' value=''>
<INPUT type='hidden' name='pid' value='<%=ecc.getPid()%>'>
<INPUT type='hidden' name='eco_no' value='<%=ecc.getEcoNo()%>'>
<INPUT type='hidden' name='mgr_id' value='<%=ecc.getMgrId()%>'>
<INPUT type='hidden' name='ecr_id' value='<%=ecr_id%>'>
<INPUT type='hidden' name='ecr_code' value='<%=ecr_code%>'>
<INPUT type='hidden' name='ecr_div_code' value='<%=ecr_div_code%>'>
<INPUT type='hidden' name='eco_id' value='<%=ecr_id%>'>
<INPUT type='hidden' name='eco_name' value='<%=ecr_name%>'>
<INPUT type='hidden' name='eco_code' value='<%=ecr_code%>'>
<INPUT type='hidden' name='eco_div_code' value='<%=ecr_div_code%>'>
<INPUT type='hidden' name='eco_div_name' value='<%=ecr_div_name%>'>
<INPUT type='hidden' name='eco_tel' value='<%=ecr_tel%>'>
<INPUT type='hidden' name='ecc_status' value='<%=ecc.getEccStatus()%>'>
<INPUT type='hidden' name='attache_cnt' value='<%=attache_cnt%>'>
<INPUT type='hidden' name='fname' value='<%=fname%>'>
<INPUT type='hidden' name='sname' value='<%=sname%>'>
<INPUT type='hidden' name='ftype' value='<%=ftype%>'>
<INPUT type='hidden' name='fsize' value='<%=fsize%>'>

</FORM>
<DIV id="lding" style="position:absolute;left:180px;top:300px;width:300px;height:100px;visibility:hidden;">
		<IMG src='../bm/images/loading8.gif' border='0' width='214' height='200'>
</DIV>
</BODY>
</HTML>
