<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "개별 개발비용 작성하기"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%
	/*********************************************************************
	 	초기화 선언
	*********************************************************************/
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//날자
	com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();			//문자열 처리
	com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat("0,000");		//출력형태
	String pre_cost = "",rst_cost="";	//예산비용,실적비용
	String cost_name = "";				//비용이름
	
	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String pjtWord="1",pjt_code="",pjt_name="",sItem="",sWord="";
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");
	para = new projectTable();
	Iterator para_iter = para_list.iterator();
	
	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();	
		pjt_code = para.getPjtCode();	
		pjt_name = para.getPjtName();	
		pjtWord = para.getPjtword();
		sItem = para.getSitem();		
		sWord = para.getSword();		
	} 

	//해당멤버의 전체 과제 LIST
	com.anbtech.pjt.entity.projectTable pjt;
	ArrayList pjt_list = new ArrayList();
	pjt_list = (ArrayList)request.getAttribute("PJT_List");
	pjt = new projectTable();
	Iterator pjt_iter = pjt_list.iterator();
	int pjt_cnt = pjt_list.size();

	String[][] project = new String[pjt_cnt][2];
	int p = 0;
	while(pjt_iter.hasNext()) {
		pjt = (projectTable)pjt_iter.next();
		project[p][0] = pjt.getPjtCode();
		project[p][1] = pjt.getPjtName();
		p++;
	}
	
	/*********************************************************************
	 	해당과제 비용 예산및 집행정보 읽기
	*********************************************************************/
	com.anbtech.pjt.entity.projectTable scost;									
	ArrayList scost_list = new ArrayList();
	scost_list = (ArrayList)request.getAttribute("SCOST_List");
	scost = new projectTable();
	Iterator scost_iter = scost_list.iterator();

	//parameter 받기
	String pjt_mbr_id="",mgt_plan="",pjt_status="",cost_exp="",cost_rst="",dif_cost="",mbr_exp="";
	String plan_start_date="",plan_end_date="";
	String chg_start_date="",chg_end_date="",rst_start_date="",rst_end_date="";
	String plan_labor="",plan_sample="",plan_metal="",plan_mup="",plan_oversea="",plan_plant="";
	String rst_labor="",rst_sample="",rst_metal="",rst_mup="",rst_oversea="",rst_plant="";
	if(scost_iter.hasNext()) {
		scost = (projectTable)scost_iter.next();

		pjt_code=scost.getPjtCode();
		pjt_name=scost.getPjtName();
		pjt_mbr_id=scost.getPjtMbrId();
		mgt_plan=scost.getMgtPlan();
		mbr_exp=Integer.toString(scost.getMbrExp());
		cost_exp=scost.getCostExp();		//예산 총비용
		cost_rst=scost.getCostRst();		//실적 총비용
		dif_cost = scost.getDifCost();		//예산 - 실적

		String psd = scost.getPlanStartDate();
		String ped = scost.getPlanEndDate();
		plan_start_date=psd.substring(0,4)+"/"+psd.substring(4,6)+"/"+psd.substring(6,8);
		plan_end_date=ped.substring(0,4)+"/"+ped.substring(4,6)+"/"+ped.substring(6,8);

		String csd = scost.getChgStartDate();	if(csd == null) csd = "";
		String ced = scost.getChgEndDate();		if(ced == null) ced = "";
		if(csd.length() > 4) chg_start_date=csd.substring(0,4)+"/"+csd.substring(4,6)+"/"+csd.substring(6,8);
		if(ced.length() > 4) chg_end_date=ced.substring(0,4)+"/"+ced.substring(4,6)+"/"+ced.substring(6,8);
		
		String rsd = scost.getRstStartDate();	if(rsd == null) rsd = "";
		String red = scost.getRstEndDate();		if(red == null) red = "";
		if(rsd.length() > 4) rst_start_date=rsd.substring(0,4)+"/"+rsd.substring(4,6)+"/"+rsd.substring(6,8);
		if(red.length() > 4) rst_end_date=red.substring(0,4)+"/"+red.substring(4,6)+"/"+red.substring(6,8);

		pjt_status = scost.getPjtStatus();	if(pjt_status == null) pjt_status = "S";
		plan_labor=scost.getPlanLabor();
		plan_sample=scost.getPlanSample();
		plan_metal=scost.getPlanMetal();
		plan_mup=scost.getPlanMup();
		plan_oversea=scost.getPlanOversea();
		plan_plant=scost.getPlanPlant();

		rst_labor=scost.getResultLabor();
		rst_sample=scost.getResultSample();
		rst_metal=scost.getResultMetal();
		rst_mup=scost.getResultMup();
		rst_oversea=scost.getResultOversea();
		rst_plant=scost.getResultPlant();
	}

	/*********************************************************************
	 	화면출력용 비용 금액
	*********************************************************************/
	if(sWord.equals("")) {				//전체
		pre_cost = cost_exp;			//예산
		rst_cost = cost_rst;			//실적
	} else if(sWord.equals("1")) {		//인건비
		pre_cost = plan_labor;
		rst_cost = rst_labor;
	} else if(sWord.equals("2")) {		//SAMPLE
		pre_cost = plan_sample;
		rst_cost = rst_sample;
	} else if(sWord.equals("3")) {		//금형비
		pre_cost = plan_metal;
		rst_cost = rst_metal;
	} else if(sWord.equals("4")) {		//투자비
		pre_cost = plan_mup;
		rst_cost = rst_mup;
	} else if(sWord.equals("5")) {		//규격승인비
		pre_cost = plan_oversea;
		rst_cost = rst_oversea;
	} else if(sWord.equals("6")) {		//투자비
		pre_cost = plan_plant;
		rst_cost = rst_plant;
	}

	//-----------------------------------
	//	과제 전체노드 찾기
	//-----------------------------------
	com.anbtech.pjt.entity.projectTable node;
	ArrayList node_list = new ArrayList();
	node_list = (ArrayList)request.getAttribute("NODE_List");
	node = new projectTable();
	Iterator node_iter = node_list.iterator();
	int node_cnt = node_list.size();
	String[][] process = new String[node_cnt][4];
	
	int a=0;
	while(node_iter.hasNext()) {
		node = (projectTable)node_iter.next();
		
		//step레벨만 비용항목으로 
		if(node.getLevelNo().equals("2")) {
			process[a][0] = node.getChildNode();		//노드코드
			process[a][1] = node.getNodeName();			//노드명
			process[a][2] = node.getUserId();			//담당자사번
			process[a][3] = node.getUserName();			//담당자이름
			a++;
		} else node_cnt--;
	}

	//-----------------------------------
	//	개발 멤버 찾기
	//-----------------------------------
	com.anbtech.pjt.entity.projectTable man;
	ArrayList man_list = new ArrayList();
	man_list = (ArrayList)request.getAttribute("MAN_List");
	man = new projectTable();
	Iterator man_iter = man_list.iterator();
	int man_cnt = man_list.size();
	String[][] member = new String[man_cnt][4];
	
	int n=0;
	while(man_iter.hasNext()) {
		man = (projectTable)man_iter.next();
		member[n][0] = man.getPjtMbrId();		
		member[n][1] = man.getPjtMbrName();		
		member[n][2] = man.getPjtMbrGrade();			
		member[n][3] = man.getPjtMbrDiv();		
		n++;
	}

	//금일 날자 구하기 : format : yyyy-MM-dd
	String toDate = anbdt.getDate(0);
	toDate = toDate.replace('/','-');
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//과제 비용 SUMMARY보기
function costWrite()
{
	//[노드코드|노드이름] 찾기
	var node = document.eForm.node.value;
	var nd = node.split('|');

	//[담당자사번|담당자이름] 찾기
	var users = document.eForm.users.value;
	var us = users.split('|');

	//금액에서 ','없애기
	var cexp = unCommaObj(document.eForm.node_cost.value);		//개발비용

	//sWord바꿔주기
	var sw = document.eForm.cost_type.value;

	document.eForm.action='../servlet/projectStfCostServlet';
	document.eForm.mode.value='PCO_W';
	document.eForm.node_code.value=nd[0];
	document.eForm.node_name.value=nd[1];
	document.eForm.user_id.value=us[0];
	document.eForm.user_name.value=us[1];
	document.eForm.node_cost.value=cexp;
	document.eForm.sWord.value=sw;
	document.eForm.submit();

}
//환율 적용하기
function exchangeM() 
{
	//금액에서 ','없애기
	var cexp = unCommaObj(document.eForm.node_cost.value);		//개발비용

	//환율금액
	var exc = document.eForm.exchange.value;

	//환율적용 소숫점 자릿수 검사
	var point_cnt = 0;		//소숫점 찾기
	for(i=0; i<exc.length; i++) {
		if(exc.charAt(i) == '.') point_cnt++;
	}
	if(point_cnt > 1) { alert('환율입력에 오류가 있습니다. 확인후 다시 하십시요.'); return; }


	//환율적용 원화
	var mey = 0;
	if(exc != 0) mey = cexp * exc;

	//반영하기
	document.eForm.node_cost.value = Comma(mey);
	
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

	var cost = document.eForm.node_cost.value;
	document.eForm.node_cost.value = Comma(cost);
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
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif"> 과제진행 개발비용 계정별 상세보기</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--버튼-->
			<TABLE width='100%' cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='5'></TD>
					<TD align=left width=''>
						<a href='javascript:costWrite()'>
						<img src='../pjt/images/bt_add.gif' border='0'></a>
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!-- 예산및 실적비용 비교 -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr> <!-- 계정 상세정보 -->
			   <td width="10%" height="25" colspan=4><b>계정과목 상세정보</b></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">과 제 명</td>
			   <td width="40%" height="25" class="bg_04"><%=pjt_code%> <%=pjt_name%></td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">P M 명</td>
				<td width="40%" height="25" class="bg_04"><%=pjt_mbr_id%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">과제상태</td>
			   <td width="40%" height="25" class="bg_04">
				<%
					if(pjt_status.equals("S")) out.println("미착수");
					else if(pjt_status.equals("0")) out.println("진행전");
					else if(pjt_status.equals("1")) out.println("진행중");
					else if(pjt_status.equals("2")) out.println("완료");
					else if(pjt_status.equals("3")) out.println("DROP");
					else if(pjt_status.equals("4")) out.println("HOLDING");
				%></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">계획기간</td>
			   <td width="40%" height="25" class="bg_04">
					<%=plan_start_date%>&nbsp;&nbsp;~&nbsp;&nbsp;<%=plan_end_date%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">실적/예산</td>
			   <td width="40%" height="25" class="bg_04">
					<input type=text value='<%=rst_cost%>' style='border:0px;background-color:#ffffff;text-align:right;color:blue' readonly size=15> /
					<input type=text value='<%=pre_cost%>' style='border:0px;background-color:#ffffff;text-align:right;color:blue' readonly size=15> [원]</td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">수정기간</td>
			   <td width="40%" height="25" class="bg_04">
					<%=chg_start_date%>&nbsp;&nbsp;~&nbsp;&nbsp;<%=chg_end_date%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">차 액</td>
			   <td width="40%" height="25" class="bg_04">
					<input type=text value='<%=dif_cost%>' style='border:0px;background-color:#ffffff;text-align:right;color:blue' readonly size=33> [원]</td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">실적기간</td>
			   <td width="40%" height="25" class="bg_04">
					 <%=rst_start_date%>&nbsp;&nbsp;~&nbsp;&nbsp;<%=rst_end_date%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>

<!-- 리스트 -->
<TABLE  cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- 타이틀 및 페이지 정보 -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
				<TD valign='middle' class="title"><img src="../pjt/images/blet.gif" align="absmiddle"> 해당계정과목 입력하기</TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>	
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
</TABLE>
<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">담당자</td>
			   <td width="40%" height="25" class="bg_04">
					<select name="users" style=font-size:9pt;color="black";> 
					<%
						for(int i=0; i<man_cnt; i++) {
							out.print("<option value='"+member[i][0]+"|"+member[i][1]+"'>"+member[i][1]+" "+member[i][2]);
							out.println("</option>");
						} 
					%>
					</select></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">노드명</td>
			   <td width="40%" height="25" class="bg_04">
					<select name="node" style=font-size:9pt;color="black";>  
					<%
						for(int ti=1; ti<node_cnt; ti++) {
							out.print("<option value='"+process[ti][0]+"|"+process[ti][1]+"'>");
							out.println(process[ti][0]+" "+process[ti][1]+"</option>");
						}
					%>
					</select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">경비항목</td>
			   <td width="40%" height="25" class="bg_04">
					<select name="cost_type" style=font-size:9pt;color="black";> 
					<%
						String[] s_code = {"1","2","3","4","5","6"};
						String[] s_name = {"인건비","SAMPLE","금형비","투자경비","규격승인비","시설투자비"};
						String tsel = "";
						for(int ti=0; ti<s_code.length; ti++) {
							if(sWord.equals(s_code[ti])) {tsel = "selected"; cost_name=s_name[ti]; } 
							else tsel = "";
							out.println("<option "+tsel+" value='"+s_code[ti]+"'>"+s_name[ti]+"</option>");
						}
					%>
					</select></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">작성일</td>
			   <td width="40%" height="25" class="bg_04"><%=toDate%>
						<input type='hidden' name='in_date' value='<%=toDate%>'></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">지출금액</td>
			   <td width="40%" height="25" class="bg_04">
					<input type='text' name='node_cost' value='' size=15 maxlength=16 style="text-align:right;" OnBlur="isNumObj(this);"> [원]</td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">환 율</td>
			   <td width="40%" height="25" class="bg_04">
					<input type='text' name='exchange' value='' size=15 maxlength=16 style="text-align:right;" OnBlur="javascript:exchangeM();"> [환율입력시 지출금액에 적용됨]</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">비 고</td>
			   <td width="40%" height="25" class="bg_04" colspan=3>
					<textarea name='remark' rows='5' cols='80'></textarea></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type="hidden" name="mode" size="15" value="">
<input type="hidden" name="pid" size="15" value="">
<input type="hidden" name="page" size="15" value="">
<input type="hidden" name="sItem" size="15" value="<%=sItem%>">
<input type="hidden" name="sWord" size="15" value="">
<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
<input type="hidden" name="pjt_code" size="15" value="<%=pjt_code%>">
<input type="hidden" name="pjt_name" size="15" value="<%=pjt_name%>">
<input type="hidden" name="node_code" size="15" value="">
<input type="hidden" name="node_name" size="15" value="">
<input type="hidden" name="user_id" size="15" value="">
<input type="hidden" name="user_name" size="15" value="">
</form>

</body>
</html>
