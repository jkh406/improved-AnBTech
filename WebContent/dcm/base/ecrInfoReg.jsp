<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= "ECR 작성하기"		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import		= "java.util.*"
	import		= "com.anbtech.dcm.entity.*"
	import		= "com.anbtech.bm.entity.*"
	import		= "com.anbtech.date.anbDate"
	import		= "com.anbtech.text.StringProcess"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//----------------------------------------------------
	//	parameter 선언
	//----------------------------------------------------
	String pid="",ecr_id="",ecr_name="",ecr_code="",ecr_div_code="",ecr_div_name="",ecr_tel="";
	int attache_cnt = 4;			//첨부파일 최대갯수 (미만)
	
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();

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
	//	ECC REQ 읽기
	//----------------------------------------------------
	com.anbtech.dcm.entity.eccReqTable ecr;
	ecr = (eccReqTable)request.getAttribute("REQ_List");

	//----------------------------------------------------
	//	설계변경 선택항목[F04:업무구분] 데이터 읽기
	//----------------------------------------------------
	com.anbtech.bm.entity.mbomEnvTable eck;
	ArrayList eck_list = new ArrayList();
	eck_list = (ArrayList)request.getAttribute("ECK_List");
	eck = new mbomEnvTable();
	Iterator eck_iter = eck_list.iterator();

%>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../dcm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<FORM name="eForm" method="post" style="margin:0" encType="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../dcm/images/blet.gif"> ECR작성</TD>
				</TR>
				</TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		<TABLE cellSpacing=0 cellPadding=0 border=0>
			<TBODY>
				<TR><TD align=left width='20%' style='padding-left:5px;'>
					<a href="javascript:sendSave();"><img src="../dcm/images/bt_reg.gif" border='0' align='absmiddle'></a>
					</TD></TR>
			</TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--내용-->
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center">
		<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
			<TBODY>
			    <!--<TR> 기초정보 작성 
				<TD height="25" colspan="4"><img src="../dcm/images/basic_info.gif" width="209" height="25" border="0"></TD></TR>
				</TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>-->
				<TR><TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">제 목</TD>
					<TD width="87%" height="25" class="bg_04" colspan='3'>
						<INPUT type="text" name="ecc_subject" value="<%=ecc.getEccSubject()%>" size="40"  maxlength='50' class="text_01"></TD>					
				</TR> 
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR>
					<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">문제발생모델(F/G)</TD>
					<TD width="37%" height="25" class="bg_04">
						<INPUT name="fg_code" size='12' maxlength='' class="text_01" readonly><%=ecc.getFgCode()%>
						<a href="Javascript:searchModel();"><img src="../dcm/images/bt_search.gif" border="0" align="absmiddle"></a>
					</TD>
					<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">업무구분</TD>
					<TD width="37%" height="25" class="bg_04" >
						<SELECT name="ecc_kind" class="text_01"> 
						<OPTGROUP label='    --------'>
						<%
							String sel="",ecc_kind=ecc.getEccKind();
							while(eck_iter.hasNext()) {
								eck = (mbomEnvTable)eck_iter.next(); 
								if(ecc_kind.equals(eck.getSpec())) sel = "selected";
								else sel = "";
								out.print("<option "+sel+" value='"+eck.getSpec()+"'>");
								out.println(eck.getSpec()+"</option>");
							} 
						%></select>
					</TD></TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR>
					<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">문제발생부품</TD>
					<TD width="87%" height="25" class="bg_04" colspan='3'>
						<TEXTAREA NAME="part_code" rows='4' cols='30' readonly><%=ecc.getPartCode()%></TEXTAREA>
						<a href="Javascript:searchItems();"><img src="../dcm/images/bt_search.gif" border="0" align="top"></a>
						</TD></TR>
				<!-- ECR 작성 
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR>
				<TD height="23" colspan="4"><img src='../dcm/images/title_ecr_info.gif' border='0' align='absmiddle' alt='ECR 내용'></TD></TR>-->
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD height='5'></TD></TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">문제분류</TD>
					<TD width="37%" height="25" class="bg_04">
						<SELECT name="trouble" class="text_01"> 
						<OPTGROUP label='-----'>
						<%
							String[] trb_data = {"부품","동작","공정","성능"};
							String tsel="",trouble=ecr.getTrouble();
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
						<INPUT type='text' name='chg_position' value='<%=ecr.getChgPosition()%>' size='20'  maxlength='25' class="text_01"></TD></TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">현상및원인</TD>
					<TD width="37%" height="25" class="bg_04">
						<TEXTAREA NAME="condition" rows='4' cols='38'  class="text_01"><%=ecr.getCondition()%></TEXTAREA></TD>
					<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">대책</TD>
					<TD width="37%" height="25" class="bg_04">
					<TEXTAREA NAME="solution" rows='4' cols='38'><%=ecr.getSolution()%></TEXTAREA></TD></TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">첨부파일</TD>
					<TD width="87%" height="25" class="bg_04" colspan="3">
				<%
					for(int i=1; i<attache_cnt; i++) {
						out.println("<INPUT type='file' name='attachfile"+i+"' size='40'><br>");
					}
				%>		   
					</TD></TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD height='5'></TD></TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR><TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">검토책임자</TD>
					<TD width="37%" height="25" class="bg_04">
						<INPUT type="text" name="mgr_name" value="<%=ecc.getMgrName()%>" size="12" class="text_01" readonly> <A href="Javascript:checkMgr();"><img src="../dcm/images/bt_search.gif" border="0" align="absmiddle"></a>
					</TD>
					<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">전화번호</TD>
					<TD width="37%" height="25" class="bg_04">
						<INPUT type="text" name="ecr_tel" value="<%=ecr_tel%>" size="20" maxlength='20'></TD>
					</TR>
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
				<TR>
					<TD width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">작성일</TD>
					<TD width="87%" height="25" class="bg_04" colspan='3'><%=anbdt.getDate()%>
						</TD>
				</TR> 
				<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			</TBODY>
		</TABLE></TD></TR>
</TABLE>
<INPUT type='hidden' name='mode' value=''>
<INPUT type='hidden' name='pid' value='<%=ecc.getPid()%>'>
<INPUT type='hidden' name='eco_no' value=''>
<INPUT type='hidden' name='ecr_id' value='<%=ecr_id%>'>
<INPUT type='hidden' name='ecr_code' value='<%=ecr_code%>'>
<INPUT type='hidden' name='ecr_div_code' value='<%=ecr_div_code%>'>
<INPUT type='hidden' name='mgr_id' value=''>
<INPUT type='hidden' name='pdg_code' value='<%=ecc.getPdgCode()%>'>
<INPUT type='hidden' name='pd_code' value='<%=ecc.getPdCode()%>'>
<INPUT type="hidden" name="ecr_date" value="<%=ecc.getEcrDate()%>" size="30">
</FORM>

	<DIV id="lding" style="position:absolute;left:300px;top:150px;width:320px;height:100px;visibility:hidden;">
		<IMG src='../bm/images/loading8.gif' border='0' width='214' height='200'>
	</DIV>

</BODY>
</HTML>


<script language=javascript>
<!--
//등록하기
function sendSave()
{
	var f = document.eForm;

	var ecc_subject = f.ecc_subject.value;
	if(ecc_subject == '') { alert('설계변경 제목을 입력하십시오.'); f.ecc_subject.focus(); return; }
		
	var fg_code = f.fg_code.value;
	if(fg_code == '') { alert('발생모델(FG코드)을 입력하십시오.'); f.fg_code.focus(); return; }
	
	var chg_position = f.chg_position.value;
	if(chg_position == '') { alert('문제 발생 부분을 입력하십시오.'); f.chg_position.focus(); return; }
	
	var condition = f.condition.value;
	if(condition == '') { alert('원인및현상을 입력하십시오.'); f.condition.focus(); return; }
	
	var mgr_name = f.mgr_name.value;
	if(mgr_name == '') { alert('기술검토책임자를 입력하십시오.'); f.mgr_name.focus(); return; }
	document.onmousedown=dbclick;

	//데이터 저장하기
	document.all['lding'].style.visibility="visible";	//처리중 메시지 출력

	document.eForm.action='../servlet/CbomBaseInfoServlet';
	document.eForm.mode.value='ecr_write';
	document.eForm.submit();
}

// 전송중에 다른 버튼 누르지 못하도록 처리
function dbclick(){
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}

//검토책임자
function checkMgr()
{
	wopen("../dcm/searchName.jsp?target=eForm.mgr_id/eForm.mgr_name","proxy","250","380","scrollbar=yes,toolbar=no,status=no,resizable=no");

}

//발생부품 찾기
function searchItems()
{
	//대표 FG코드만 선택한다:첫번째를 대표FG로 간주
	var fg_list = document.eForm.fg_code.value;
	var fgc = fg_list.split('\n');
	var fg_code = fgc[0];
	if(fg_code.length == 0) { alert('발생모델(FG)을 먼저 입력하십시요.'); return; }
	wopen("../servlet/CbomBaseInfoServlet?mode=pl_list&fg_code="+fg_code,"proxy","700","400","scrollbars=auto,toolbar=no,status=no,resizable=yes");

}

//모델 검색하기(FG코드)
function searchModel(){
		
	var strUrl = "../gm/openModelInfoWindow.jsp?one_class=pdg_code&one_name=pdg_name&two_class=pd_code&two_name=pd_name&three_class=modelg_code&three_name=modelg_name&four_class=model_code&four_name=model_name&fg_code=fg_code";
	
	wopen(strUrl,"search_bominfo",'820','405','scrollbars=no,toolbar=no,status=no,resizable=no');
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