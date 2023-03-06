<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "부서공통 과제기본정보 신규등록"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.Connection"
	import="com.anbtech.pjt.entity.*"
	import="com.anbtech.util.normalFormat"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	/*********************************************************************
	 	초기화 선언
	*********************************************************************/
	com.anbtech.pjt.entity.pjtCodeTable table;									//과제기본정보helper
	com.anbtech.pjt.entity.prsCodeTable prstable;								//프로세스helper
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//날자
	normalFormat money = new com.anbtech.util.normalFormat("#,000");			//출력형식 (금액)
	com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
	Connection con = connMgr.getConnection("mssql");
	com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);	//과제기본정보
	com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);		//표준프로세스
	
	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String pjtWord="S",sItem="pjt_name",sWord="";

	//-----------------------------------
	// 과제관리자 정보 변수선언
	//-----------------------------------
	String user_id = "";			//작성자 사번
	String user_name = "";			//작성자 이름
	String user_rank = "";			//작성자 직급
	String div_id = "";				//작성자 부서명 관리코드
	String div_name = "";			//작성자 부서명
	String div_code = "";			//작성자 부서코드
	String code = "";				//작성자 부서Tree 관리코드
	String cexp = "";				//비용금액 한글로 읽기

	/*********************************************************************
	 	과제관리자 정보 알아보기 [사번/이름]
	*********************************************************************/
	String[] Column = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code","b.code"};
	bean.setTable("user_table a,class_table b,rank_table c");		
	bean.setColumns(Column);
	bean.setOrder("a.id ASC");	
	String query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		user_id = login_id;								//기안자 사번
		user_name = bean.getData("name");				//기안자 명
		user_rank = bean.getData("ar_name");			//기안자 직급
		div_id = bean.getData("ac_id");					//기안자 부서명 관리코드
		div_name = bean.getData("ac_name");				//기안자 부서명 
		div_code = bean.getData("ac_code");				//기안자 부서코드
		code = bean.getData("code");					//작성자 부서Tree 관리코드
	} //while

	/*********************************************************************
	 	과제중 미등록과제 찾기 [상태 : S(미착수)]
	*********************************************************************/
	ArrayList table_list = new ArrayList();
	table_list = genDAO.getDivStandbyProjectList(user_id);		//부서 기본정보 미등록 과제
	table = new pjtCodeTable();
	Iterator table_iter = table_list.iterator();

	int pjt_cnt = table_list.size();
	String[][] project = new String[pjt_cnt][2];
	int n = 0;
	while(table_iter.hasNext()) {
		table = (pjtCodeTable)table_iter.next();
		project[n][0] = table.getPjtCode();
		project[n][1] = table.getPjtName();
		n++;
	}

	/*********************************************************************
	 	과제중 메인과제 찾기 [상태 : 1(등록과제), 2(노드시작)]
	*********************************************************************/
	ArrayList main_list = new ArrayList();
	main_list = genDAO.getMainProjectList();		//전사 기본정보 미등록 과제
	table = new pjtCodeTable();
	Iterator main_iter = main_list.iterator();

	int main_cnt = main_list.size();
	String[][] main = new String[main_cnt][2];
	int m = 0;
	while(main_iter.hasNext()) {
		table = (pjtCodeTable)main_iter.next();
		main[m][0] = table.getPjtCode();
		main[m][1] = table.getPjtName();
		m++;
	}

	/*********************************************************************
	 	부서 공통 process
	*********************************************************************/
	ArrayList prs_list = new ArrayList();
	prs_list = pnameDAO.getPrsnameDivList(login_id,"prs_code","","1",50);
	prstable = new prsCodeTable();		
	Iterator prs_iter = prs_list.iterator();

	int prs_cnt = prs_list.size();
	String[][] process = new String[prs_cnt][3];
	int p = 0;
	while(prs_iter.hasNext()) {
		prstable = (prsCodeTable)prs_iter.next();
		process[p][0] = prstable.getPrsCode();
		process[p][1] = prstable.getPrsName();
		process[p][2] = prstable.getType();
		p++;
	}
	/*********************************************************************
	 	Connection 닫기
	*********************************************************************/
	connMgr.freeConnection("mssql",con);
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//등록하기
function sendSave()
{
	//[과제코드|과제이름] 찾기
	var pjt_info = document.eForm.pjt_info.value;
	var info = pjt_info.split('|');
	if(info[0] == '') {alert('과제명을 지정하십시요.');  return;}
	if(document.eForm.pjt_mbr_id.value == '')		{alert('과제PM을 지정하십시요.');			return;}
	if(document.eForm.mbr_exp.value == '')			{alert('개발인원수을 입력하십시요.');		return;}
	if(document.eForm.plan_start_date.value == '')	{alert('계획기간 시작일을 입력하십시요.');  return;}
	if(document.eForm.plan_end_date.value == '')	{alert('계획기간 종료일을 입력하십시요.');  return;}
	if(document.eForm.pjt_desc.value == '')			{alert('과제개요를 입력하십시요.');			return;}
	if(document.eForm.pjt_spec.value == '')			{alert('주요SPEC을 입력하십시요.');		return;}
	if(document.eForm.pjt_mbr_id.value == '')		{alert('과제PM을 입력하십시요.');			return;}

	//숫자인지 판단하기
	if(isNaN(document.eForm.mbr_exp.value)) { alert('개발인원은 숫자로 입력하십시요.');  return; }
	
	//[프로세스코드|프로세스이름] 찾기
	var prs_info = document.eForm.prs_info.value;
	var prs = prs_info.split('|');

	//날자에서 '/'제거
	var psd = document.eForm.plan_start_date.value;		//계획기간 시작일
	for(i=0;i<2;i++) psd = psd.replace('/','');	
	var ped = document.eForm.plan_end_date.value;		//계획기간 종료일
	for(i=0;i<2;i++) ped = ped.replace('/','');	
	var csd = document.eForm.chg_start_date.value;		//수정기간 시작일
	for(i=0;i<2;i++) csd = csd.replace('/','');	
	var ced = document.eForm.chg_end_date.value;		//수정기간 종료일
	for(i=0;i<2;i++) ced = ced.replace('/','');	
	var rsd = document.eForm.rst_start_date.value;		//실적기간 시작일
	for(i=0;i<2;i++) rsd = rsd.replace('/','');	
	var red = document.eForm.rst_end_date.value;		//실적기간 종료일
	for(i=0;i<2;i++) red = red.replace('/','');	

	//금액에서 ','없애기
	var cexp = unCommaObj(document.eForm.cost_exp.value);		//개발비용
	var clab = unCommaObj(document.eForm.plan_labor.value);		//계획인건비
	var csam = unCommaObj(document.eForm.plan_sample.value);	//계획sample
	var cmet = unCommaObj(document.eForm.plan_metal.value);		//계획금형
	var cmup = unCommaObj(document.eForm.plan_mup.value);		//계획muckup
	var cove = unCommaObj(document.eForm.plan_oversea.value);	//계획규격승인
	var cpla = unCommaObj(document.eForm.plan_plant.value);		//계획시설투자


	//처리중 메시지 출력
	document.all['lding'].style.visibility="visible";

	//등록하기
	document.eForm.action='../../servlet/projectGenServlet';
	document.eForm.mode.value='PBS_WD';	
	document.eForm.pjt_code.value=info[0];
	document.eForm.pjt_name.value=info[1];
	document.eForm.prs_code.value=prs[0];
	document.eForm.prs_type.value=prs[1];
	document.eForm.plan_start_date.value=psd;
	document.eForm.plan_end_date.value=ped;
	document.eForm.chg_start_date.value=csd;
	document.eForm.chg_end_date.value=ced;
	document.eForm.rst_start_date.value=rsd;
	document.eForm.rst_end_date.value=red;
	document.eForm.cost_exp.value=cexp;
	document.eForm.plan_labor.value=clab;
	document.eForm.plan_sample.value=csam;
	document.eForm.plan_metal.value=cmet;
	document.eForm.plan_mup.value=cmup;
	document.eForm.plan_oversea.value=cove;
	document.eForm.plan_plant.value=cpla;
	document.eForm.submit();

}
//PM 찾기
function searchPM()
{
	window.open("../searchPM.jsp?target=eForm.pjt_mbr_id","proxy","width=460,height=480,scrollbar=yes,toolbar=no,status=no,resizable=no");

}
//일자 입력하기
function OpenCalendar(FieldName) {
	var strUrl = "../Calendar.jsp?FieldName=" + FieldName;
	newWIndow = window.open(strUrl, "Calendar", "width=0, height=0");
}

//금액 천단위에 콤마넣기
function InputMoney(input,obj){
	str = input.value;
	str = unComma(str);
	MoneyToHan(str,obj)
	str = Comma(str);
	input.value = str;
}
function MoneyToHan(str,obj){
	arrayNum=new Array("0","1","2","3","4","5","6","7","8","9");
	arrayUnit=new Array("","","","","만 ","","","","억 ","","","","조 ","","","","경 ");
	arrayStr= new Array()
	len = str.length; 
	hanStr = "";
	for(i=0;i<len;i++) { arrayStr[i] = str.substr(i,1) }
	code = len;
	for(i=0;i<len;i++) {
		code--;
		tmpUnit = "";
		if(arrayNum[arrayStr[i]] != ""){
			tmpUnit = arrayUnit[code];
			if(code>4) {
				if(( Math.floor(code/4) == Math.floor((code-1)/4)
				     && arrayNum[arrayStr[i+1]] != "") || 
				   ( Math.floor(code/4) == Math.floor((code-2)/4) 
				     && arrayNum[arrayStr[i+2]] != "")) {
					tmpUnit=arrayUnit[code].substr(0,1);
				} 
			}
		}
		hanStr +=  arrayNum[arrayStr[i]]+tmpUnit;
    }
	obj.value = hanStr;
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

-->
</script>
<BODY topmargin="0" leftmargin="0">
<form name="eForm" method="post" style="margin:0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../images/blet.gif"> 과제 기본정보 등록</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0>
				<TBODY>
				<TR>
					<TD align=left width=5></TD>
					<TD align=left width=200><a href="javascript:sendSave();"><img src="../images/bt_add.gif" border="0"></a><a href="javascript:history.go(-1)"><img src="../images/bt_cancel.gif" border="0"></a>
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
</TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr> <!-- 기본정보 -->
				<td height="25" colspan="4"><img src="../images/basic_info.gif" width="209" height="25" border="0"></td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">과 제 명</td>
			   <td width="40%" height="25" class="bg_04">
					<select name='pjt_info'>
					<option value=''>과제명을 선택하십시요.</option>
					<%
					for(int i=0; i<pjt_cnt; i++) {
						out.print("<OPTION value='"+project[i][0]+"|"+project[i][1]+"'>");
						out.println(project[i][0]+" "+project[i][1]+"</OPTION>");
					}
					%>
					</select></td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">MAIN과제</td>
			    <td width="40%" height="25" class="bg_04">
					<select name='parent_code'>
					<option value='M'>MAIN 과제명이 있으면 선택하십시요.</option>
					<%
					for(int i=0; i<main_cnt; i++) {
						out.print("<OPTION value='"+main[i][0]+"'>");
						out.println(main[i][0]+" "+main[i][1]+"</OPTION>");
					}
					%>
					</select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">P M 명</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='pjt_mbr_id' value='' readonly>
					&nbsp;<a href="Javascript:searchPM();"><img src="../images/bt_search2.gif" border="0" align="absmiddle"></a></td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">개발인원수</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='mbr_exp' value='' size=5> [인력등록시 자동반영됨]</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">계획기간</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_start_date' value='<%=anbdt.getDate(0)%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('plan_start_date');"><img src="../images/bt_calendar.gif" border="0" valign='absbottom'></A>&nbsp;&nbsp;~&nbsp;&nbsp;
					<input type='text' name='plan_end_date' value='<%=anbdt.getDate(180)%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('plan_end_date');"><img src="../images/bt_calendar.gif" border="0" valign='absbottom'></A></td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">과제등급</td>
				<td width="40%" height="25" class="bg_04">
					<select name='pjt_class'>
					<%
					String[] grade_no = {"1","2","3","4"};
					String[] grade_name = {"1등급","2등급","3등급","4등급"};
					for(int i=0; i<grade_no.length; i++) {
						out.print("<OPTION value='"+grade_no[i]+"'>");
						out.println(grade_name[i]+"</OPTION>");
					}
					%>
					</select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">수정기간</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='chg_start_date' value='' size=10 readonly>&nbsp;&nbsp;~&nbsp;&nbsp;
					<input type='text' name='chg_end_date' value='' size=10 readonly></td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">개발목표</td>
				<td width="40%" height="25" class="bg_04">
					<select name='pjt_target'>
					<%
					String[] target = {"상품화과제","연구과제","선행과제","기능개선과제"};
					for(int i=0; i<target.length; i++) { 
						out.print("<OPTION value='"+target[i]+"'>");
						out.println(target[i]+"</OPTION>");
					}
					%>
					</select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">실적기간</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='rst_start_date' value='' size=10 readonly>&nbsp;&nbsp;~&nbsp;&nbsp;
					<input type='text' name='rst_end_date' value='' size=10 readonly></td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">프로세스</td>
				<td width="40%" height="25" class="bg_04">
					<select name='prs_info'>
					<%
					for(int i=0; i<process.length; i++) {
						out.print("<OPTION value='"+process[i][0]+"|"+process[i][2]+"'>");
						out.println(process[i][0]+" "+process[i][1]+"</OPTION>");
					}
					%>
					</select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">과제개요</td>
				<td width="40%" height="25" class="bg_04">
					<TEXTAREA NAME="pjt_desc" rows=6 cols=42 style="border:1px solid #787878;"></TEXTAREA></td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">주요SPEC</td>
				<td width="40%" height="25" class="bg_04">
					<TEXTAREA NAME="pjt_spec" rows=6 cols=42 style="border:1px solid #787878;"></TEXTAREA></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr> <!-- 예산정보 -->
				<td height="25" colspan="4">&nbsp;<b>과제 예산정보</b></td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">개발비용</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='cost_exp' value='' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.cost_exp_money);">
					<input type=text name=cost_exp_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> 원</td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">경영계획</td>
				<td width="40%" height="25" class="bg_04">
					<select name='mgt_plan'>
					<%
					String[] mgt_tag = {"Y","N"};
					String[] mgt_name = {"반영","미반영"};
					for(int i=0; i<mgt_tag.length; i++) {
						out.print("<OPTION value='"+mgt_tag[i]+"'>");
						out.println(mgt_name[i]+"</OPTION>");
					}
					%>
					</select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">인건비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_labor' value='' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_labor_money);">
					<input type=text name=plan_labor_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> 원</td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">SAMPLE</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_sample' value='' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_sample_money);">
					<input type=text name=plan_sample_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> 원</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">금형비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_metal' value='' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_metal_money);">
					<input type=text name=plan_metal_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> 원</td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">투자경비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_mup' value='' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_mup_money);">
					<input type=text name=plan_mup_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> 원</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">규격승인비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_oversea' value='' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_oversea_money);">
					<input type=text name=plan_oversea_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> 원</td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">시설비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_plant' value='' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_plant_money);">
					<input type=text name=plan_plant_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> 원</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value='<%=bean.getID()%>'>
<input type='hidden' name='pjt_code' value=''>
<input type='hidden' name='pjt_name' value=''>
<input type='hidden' name='owner' value='<%=user_id%>/<%=user_name%>'>
<input type='hidden' name='in_date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='prs_code' value=''>
<input type='hidden' name='prs_type' value=''>

<input type='hidden' name='pjt_status' value='S'>
<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
<input type="hidden" name="sItem" size="15" value="<%=sItem%>">
<input type="hidden" name="sWord" size="15" value="<%=sWord%>">
</form>

<div id="lding" style="position:absolute;left:180px;top:300px;width:300px;height:100px;visibility:hidden;">
<table width="400" border="1" cellspacing=1 cellpadding=1 bgcolor="#ABDDE9">
	<tr><td height="50" align="center" valign="middle" class='subB'>
		<marquee behavior="alternate">데이터 처리중입니다. 잠시만 기다려 주십시요.</marquee>
	</td> 
	</tr>
</table>
</div>

</body>
</html>
