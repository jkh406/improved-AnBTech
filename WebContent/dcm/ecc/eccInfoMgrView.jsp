<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= "개인 수신함 검토책임자/담당자 설계변경 내용보기"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import		= "java.io.*"
	import		= "java.util.*"
	import		= "com.anbtech.text.*"
	import		= "com.anbtech.dcm.entity.*"
	import		= "com.anbtech.date.anbDate"
%>
<%
	//----------------------------------------------------
	//	초기화값
	//----------------------------------------------------
	com.anbtech.date.anbDate anbdt = new anbDate();
	String ecc_status = "";

	//ECR정보 읽기
	String fname="",sname="",ftype="",fsize = "";						
	String chg_position="",trouble="",condition="",solution="";

	//ECO정보 읽기
	String eco_fname="",eco_sname="",eco_ftype="",eco_fsize = "";						
	String eco_chg_position="",eco_trouble="",eco_condition="",eco_solution="";

	String v_list = (String)request.getAttribute("v_list"); if(v_list == null) v_list = "ecc_rilist";
	
	//----------------------------------------------------
	//	ECC COM 읽기
	//----------------------------------------------------
	com.anbtech.dcm.entity.eccComTable ecc;
	ecc = (eccComTable)request.getAttribute("COM_List");
	ecc_status = ecc.getEccStatus();						//진행상태검사

	//----------------------------------------------------
	//	ECC REQ 읽기
	//----------------------------------------------------
	com.anbtech.dcm.entity.eccReqTable ecr;
	ecr = (eccReqTable)request.getAttribute("REQ_List");

	//----------------------------------------------------
	//	ECC ORD 읽기
	//----------------------------------------------------
	com.anbtech.dcm.entity.eccOrdTable eco;
	eco = (eccOrdTable)request.getAttribute("ORD_List");

	//----------------------------------------------------
	//	첨부파일 읽기
	//----------------------------------------------------
	//ECR의 내용을 읽을때
	fname = ecr.getFname();							//첨부파일명
	sname = ecr.getSname();							//첨부파일 저장명
	ftype = ecr.getFtype();							//첨부파일Type
	fsize = ecr.getFsize();							//첨부파일Size
	chg_position = ecr.getChgPosition();
	trouble = ecr.getTrouble();
	condition = ecr.getCondition();
	solution = ecr.getSolution();
	String bon_path = "/dcm/"+ecc.getEcrId();		//첨부파일 저장 확장path
	 
	//ECR 첨부파일 개별로 읽기
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

	//ECO의 내용을 읽을때
	eco_fname = eco.getFname();						//첨부파일명
	eco_sname = eco.getSname();						//첨부파일 저장명
	eco_ftype = eco.getFtype();						//첨부파일Type
	eco_fsize = eco.getFsize();						//첨부파일Size
	eco_chg_position = eco.getChgPosition();
	eco_trouble = eco.getTrouble();
	eco_condition = eco.getCondition();
	eco_solution = eco.getSolution();
	String eco_bon_path = "/dcm/"+ecc.getEcoId();	//첨부파일 저장 확장path

	//ECO 첨부파일 개별로 읽기
	int eco_cnt = 0;
	for(int i=0; i<eco_fname.length(); i++) if(eco_fname.charAt(i) == '|') eco_cnt++;

	String[][] eco_addFile = new String[eco_cnt][5];
	for(int i=0; i<eco_cnt; i++) for(int j=0; j<5; j++) eco_addFile[i][j]="";

	if(eco_fname.length() != 0) {
		StringTokenizer eco_f = new StringTokenizer(eco_fname,"|");		//파일명 담기
		int eco_m = 0;
		while(eco_f.hasMoreTokens()) {
			eco_addFile[eco_m][0] = eco_f.nextToken();
			eco_addFile[eco_m][0] = eco_addFile[eco_m][0].trim(); 
			if(eco_addFile[eco_m][0] == null) eco_addFile[eco_m][0] = "";
			eco_m++;
		}
		StringTokenizer eco_t = new StringTokenizer(eco_ftype,"|");		//파일type 담기
		eco_m = 0;
		while(eco_t.hasMoreTokens()) {
			eco_addFile[eco_m][1] = eco_t.nextToken();
			eco_addFile[eco_m][1] = eco_addFile[eco_m][1].trim();
			if(eco_addFile[eco_m][1] == null) eco_addFile[eco_m][1] = "";
			eco_m++;
		}
		StringTokenizer eco_s = new StringTokenizer(eco_fsize,"|");		//파일크기 담기
		eco_m = 0;
		while(eco_s.hasMoreTokens()) {
			eco_addFile[eco_m][2] = eco_s.nextToken();
			eco_addFile[eco_m][2] = eco_addFile[eco_m][2].trim();
			if(eco_addFile[eco_m][2] == null) eco_addFile[eco_m][2] = "";
			eco_m++;
		}
		StringTokenizer eco_o = new StringTokenizer(eco_sname,"|");		//저장파일 담기
		eco_m = 0;
		int eco_no = 1;
		while(eco_o.hasMoreTokens()) {
			eco_addFile[eco_m][3] = eco_o.nextToken();	
			eco_addFile[eco_m][4] = eco_addFile[eco_m][3];
			eco_addFile[eco_m][3] = eco_addFile[eco_m][3].trim() + ".bin";			//저장파일명
			eco_addFile[eco_m][4] = eco_addFile[eco_m][4].trim();					//TEMP 저장파일명
			if(eco_addFile[eco_m][3] == null) eco_addFile[eco_m][3] = "";
			eco_m++;
			eco_no++;
		}
	}

%>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../dcm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<form name="sForm" method="post" style="margin:0" encType="multipart/form-data">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR  bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR  bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../dcm/images/blet.gif"> 설계변경정보</TD>
				</TR>
				</TBODY></TABLE></TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=30><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px;'>
					<a href="javascript:goLIST();"><img src="../dcm/images/bt_list.gif" border=0 align='absmiddle'></a>
					<% if(ecc_status.equals("3")) {		//기술검토 책임자 일때 %> 
						<a href="javascript:changeMgr();"><img src="../dcm/images/bt_chg_mgr.gif" border=0 alt='책임자변경' align='absmiddle'></a>
						<a href="javascript:setUser();"><img src="../dcm/images/bt_set_user.gif" border=0 alt='담당자지정' align='absmiddle'></a>
						<a href="javascript:rejectECC();"><img src="../dcm/images/bt_reject_ecc.gif" border=0 alt='검토반려' align='absmiddle'></a>
					<% } else if(ecc_status.equals("4")) { //기술검토 담당자 일때 %> 
						<a href="javascript:writeECO();"><img src="../dcm/images/bt_write_eco.gif" border=0 alt='ECO작성' align='absmiddle'></a>
						<a href="javascript:rejectECC();"><img src="../dcm/images/bt_reject_ecc.gif" border=0 alt='검토반려' align='absmiddle'></a>
					<% } else if(ecc_status.equals("0") || ecc_status.equals("5")) { //반려일때 %> 
						<a href="javascript:modifyECC();"><img src="../dcm/images/bt_save.gif" border=0 alt='설계변경수정' align='absmiddle'></a>
					<% } %>
					</TD>
				</TR></TBODY></TABLE></TD>
	</TR>
</TABLE>

<!--내용-->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR>
		<TD align="center">
		<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
			<TBODY>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
				<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">ECO NO</td>
				<TD width="37%" height="25" class="bg_04" ><%=ecc.getEcoNo()%></td>
				<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">제 목</td>
				<TD width="37%" height="25" class="bg_04" ><%=ecc.getEccSubject()%></td>			  
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">변경사유</td>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEccReason()%></td>
				<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">적용구분</td>
			    <TD width="37%" height="25" class="bg_04"><%=ecc.getEccFactor()%></td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">적용범위</td>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEccScope()%></td>
				<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">업무구분</td>
			    <TD width="37%" height="25" class="bg_04"><%=ecc.getEccKind()%></td>
			</TR>
			<!--
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">제품군</td>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getPdgCode()%></td>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">제품</td>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getPdCode()%></td>
			</TR>-->
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">문제발생모델(FG)</td>
			   <TD width="37%" height="25" class="bg_04">
					<INPUT NAME="fg_code" rows='4' value='<%=ecc.getFgCode()%>' size='12' readonly></td>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">적용일</td>
			   <TD width="87%" height="25" class="bg_04" colspan='3'><%=anbdt.getSepDate(ecc.getOrderDate(),"-")%></td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			    <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">문제발생부품</td>
			    <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="part_code" rows='4' cols='30' readonly><%=ecc.getPartCode()%></TEXTAREA>
					<a href="Javascript:searchItems();"><img src="../dcm/images/bt_search.gif" border="0" align="top"></a></td>
			</TR>
			<!-- ECO 작성 -->
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR><TD height="25" colspan="4"><img src='../dcm/images/title_pre_check.gif' align='absmiddle' alt='기술검토전 정보'></td></TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">문제분류</td>
			   <TD width="37%" height="25" class="bg_04"><%=trouble%></td>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">문제발생부분</td>
			   <TD width="37%" height="25" class="bg_04"><%=chg_position%></td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">현상및원인</td>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="condition" rows='4' cols='38' readonly><%=condition%></TEXTAREA></td>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">ECO적용내용</td>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="condition" rows='4' cols='38' readonly><%=solution%></TEXTAREA></td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">첨부파일</td>
			   <TD width="37%" height="25" class="bg_04" colspan='3'>
				<% 
				for(int i=0; i<cnt; i++) {
					out.print("<a href='../dcm/attach_download.jsp?fname="+addFile[i][0]);
					out.print("&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]);
					out.print("&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a>");
					out.println("<br>"); 
				}
				%>		   
			   </td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR><TD height="25" colspan="4"><img src='../dcm/images/title_after_check.gif' align='absmiddle' alt='기술검토후 정보'></td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">문제분류</td>
			   <TD width="37%" height="25" class="bg_04"><%=eco_trouble%></td>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">문제발생부분</td>
			   <TD width="37%" height="25" class="bg_04"><%=eco_chg_position%></td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">현상및원인</td>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="solution" rows='4' cols='38' readonly><%=eco_condition%></TEXTAREA></td>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">ECO적용내용</td>
			   <TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="solution" rows='4' cols='38' readonly><%=eco_solution%></TEXTAREA></td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">첨부파일</td>
			   <TD width="37%" height="25" class="bg_04" colspan='3'>
				<% 
				for(int i=0; i<eco_cnt; i++) {
					out.print("<a href='../dcm/attach_download.jsp?fname="+eco_addFile[i][0]);
					out.print("&ftype="+eco_addFile[i][1]+"&fsize="+eco_addFile[i][2]);
					out.print("&sname="+eco_addFile[i][3]+"&extend="+eco_bon_path+"'>"+eco_addFile[i][0]+"</a>");
					out.println("<br>"); 
				}
				%>		   
			   </td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR><TD height='5'></TD></TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">작성자</td>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEcrName()%></td>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">부서명</td>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEcrDivName()%></td>
			</TR>
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">전화번호</td>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEcrTel()%></td>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">작성일</td>
			   <TD width="37%" height="25" class="bg_04"><%=anbdt.getSepDate(ecc.getEcrDate(),"-")%></td>
			</TR> 
			<TR  bgcolor="c7c7c7"><TD height=1 colspan="4"></td></TR>
			<TR>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">검토책임자</td>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getMgrName()%></td>
			   <TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">검토담당자</td>
			   <TD width="37%" height="25" class="bg_04"><%=ecc.getEcoName()%>
			   <% if(ecc_status.equals("3")) {		//기술검토 책임자 일때 %> 
					<INPUT type="text" name="eco_name" value="" size="20" class="text_01" readonly><a href="Javascript:checkUser();"><img src="../dcm/images/bt_search.gif" border="0" align="absmiddle"></a>
			   <% } %>
			   </td>
			</TR></TBODY></TABLE>
		</TD>
	</TR>
</TABLE>

<INPUT type="hidden" name="mode" size="15" value="">
<INPUT type="hidden" name="pid" size="15" value="<%=ecc.getPid()%>">
<INPUT type="hidden" name="v_list" size="15" value="<%=v_list%>"> 
<INPUT type="hidden" name="eco_id" size="15" value="">
<INPUT type="hidden" name="mgr_id" size="15" value="<%=ecc.getMgrId()%>">
<INPUT type="hidden" name="mgr_name" size="15" value="<%=ecc.getMgrName()%>">
<INPUT type="hidden" name="note" size="15" value="">
</FORM>
</BODY>
</HTML>

<script language=javascript>
<!--
//목록보기
function goLIST()
{
	var v_list = document.sForm.v_list.value;
	document.sForm.action='../servlet/CbomProcessServlet';
	document.sForm.mode.value=v_list;
	document.sForm.submit();
}
//책임자:책임자변경
function changeMgr()
{
	var para = "pid=<%=ecc.getPid()%>&ecr_id=<%=ecc.getEcrId()%>&ecr_name=<%=ecc.getEcrName()%>";	wopen("../dcm/ecc/mgrChange.jsp?"+para,"mgrChange","500","220","scrollbar=yes,toolbar=no,status=no,resizable=no");
}
//책임자:담당자 지정
function setUser()
{
	var f = document.sForm;
	var eco_name = f.eco_name.value;
	if(eco_name == '') { alert('기술검토담당자가 입력되지 않았습니다.'); f.eco_name.focus(); return; }
	
	var note = "기술검토 담당자가 지정되었습니다.";

	document.sForm.action='../servlet/CbomProcessServlet';
	document.sForm.mode.value='eco_assign';
	document.sForm.note.value=note;
	document.sForm.submit();
}
//담당자:ECO작성
function writeECO()
{
	document.sForm.action='../servlet/CbomProcessServlet';
	document.sForm.mode.value='ecc_premodify';
	document.sForm.submit();
}
//반려된 설계변경 수정하기
function modifyECC()
{
	if(confirm("설계변경 수정처리를 하시겠습니까?")) {
		document.sForm.action='../servlet/CbomProcessServlet';
		document.sForm.mode.value='ecc_premodify';
		document.sForm.submit();
	} else {
		return;
	}
}
//검토반려
function rejectECC()
{
	if(confirm("검토반려 처리를 하시겠습니까?")) {
		para = "pid=<%=ecc.getPid()%>&ecr_id=<%=ecc.getEcrId()%>&ecr_name=<%=ecc.getEcrName()%>";
		para += "&mgr_id=<%=ecc.getMgrId()%>&mgr_name=<%=ecc.getMgrName()%>";	wopen("../dcm/ecc/mgrEccReject.jsp?"+para,"mgrChange","450","190","scrollbar=yes,toolbar=no,status=no,resizable=no");
	} else {
		return;
	}
}
//검토담당자
function checkUser()
{
	wopen("../dcm/searchName.jsp?target=sForm.eco_id/sForm.eco_name","proxy","250","380","scrollbar=yes,toolbar=no,status=no,resizable=no");

}
//창띄우기 공통
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}

//문제발생부품 찾기
function searchItems()
{
	//대표 FG코드만 선택한다:첫번째를 대표FG로 간주
	var fg_list = document.sForm.fg_code.value;
	var fgc = fg_list.split('\n');
	var fg_code = fgc[0];
	if(fg_code.length == 0) { alert('문제 발생모델(FG)을 먼저 입력하십시요.'); return; }
	wopen("../servlet/CbomBaseInfoServlet?mode=pl_list&fg_code="+fg_code,"proxy","700","400","scrollbars=auto,toolbar=no,status=no,resizable=yes");

}
-->
</script>