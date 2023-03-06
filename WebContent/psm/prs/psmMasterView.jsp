<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "과제내용 상세보기"		
	contentType = "text/html; charset=KSC5601" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.psm.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%

	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();
	com.anbtech.psm.entity.psmEnvTable env;
	String msg = (String)request.getAttribute("msg");	if(msg == null) msg = "";
	String env_status = (String)request.getAttribute("env_status");	if(env_status == null) env_status = "1";

	//----------------------------------------------------
	//	PSM MASTER 입력/수정 정보 읽기
	//----------------------------------------------------
	com.anbtech.psm.entity.psmMasterTable psm;
	psm = (psmMasterTable)request.getAttribute("MASTER_table");

	String pid = psm.getPid();	
	String psm_code = psm.getPsmCode();	
	String psm_type = psm.getPsmType();	
	String comp_name = psm.getCompName();	
	String comp_category = psm.getCompCategory();	
	String psm_korea = psm.getPsmKorea();	
	String psm_english = psm.getPsmEnglish();
	String psm_start_date = psm.getPsmStartDate();
	String psm_end_date = psm.getPsmEndDate();
	String psm_pm = psm.getPsmPm();
	String psm_mgr = psm.getPsmMgr();
	String psm_budget = psm.getPsmBudget();
	String psm_user = psm.getPsmUser();
	String psm_desc = psm.getPsmDesc();

	String plan_sum = psm.getPlanSum();
	String plan_labor = psm.getPlanLabor();
	String plan_material = psm.getPlanMaterial();
	String plan_cost = psm.getPlanCost();
	String plan_plant = psm.getPlanPlant();

	String result_sum = psm.getResultSum();
	String result_labor = psm.getResultLabor();
	String result_material = psm.getResultMaterial();
	String result_cost = psm.getResultCost();
	String result_plant = psm.getResultPlant();

	String diff_sum = psm.getDiffSum();
	String diff_labor = psm.getDiffLabor();
	String diff_material = psm.getDiffMaterial();
	String diff_cost = psm.getDiffCost();
	String diff_plant = psm.getDiffPlant();

	String contract_date = psm.getContractDate();		
	String contract_name = psm.getContractName();
	String contract_price = psm.getContractPrice();
	String complete_date = psm.getCompleteDate();
	String psm_status = psm.getPsmStatus();
	String reg_date = psm.getRegDate();			
	String app_date = psm.getAppDate();	

	String pd_code = psm.getPdCode();
	String pd_name = psm.getPdName();
	String psm_kind = psm.getPsmKind();
	String psm_view = psm.getPsmView();
	String link_code = psm.getLinkCode();

	//과제담당자인지 판단하기
	String mgr = (String)request.getAttribute("statusMgr");
	if(mgr.indexOf(sl.id) != -1) mgr = "과제담당자";

	String budget = (String)request.getAttribute("budgetMgr");
	if(budget.indexOf(sl.id) != -1) budget = "예산담당자";

	//----------------------------------------------------
	//	첨부파일 읽기
	//----------------------------------------------------
	String id_path = "";
	if(psm_user.length() !=0) id_path = psm_user.substring(0,psm_user.indexOf("/"));
	String bon_path = "/psm/"+id_path;			//첨부파일 저장 확장path
	String fname = psm.getFname();				//첨부파일명
	String sname = psm.getSname();				//첨부파일 저장명
	String ftype = psm.getFtype();				//첨부파일Type
	String fsize = psm.getFsize();				//첨부파일Size
	int attache_cnt = 4;						//첨부파일 최대갯수 (미만)

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

	//--------------------------------------
	// 과제종류 리스트 가져오기
	//--------------------------------------
	ArrayList psm_list = new ArrayList();
	psm_list = (ArrayList)request.getAttribute("PSM_List");
	env = new psmEnvTable();
	Iterator env_iter = psm_list.iterator();

	int env_cnt = psm_list.size();
	String[][] pjt_list = new String[env_cnt][3];
	int n=0;
	while(env_iter.hasNext()) {
		env = (psmEnvTable)env_iter.next();
		pjt_list[n][0] = env.getEnvType();			//구분 : 과제표시
		pjt_list[n][1] = env.getEnvStatus();		//상태 : 1:예비등록과제, 2:정식등록과제
		pjt_list[n][2] = env.getEnvName();			//이름 : 과제종류명
		n++;
	}

	//제목달기
	String title = "과제";
	if(env_cnt > 0) {
		if(pjt_list[0][1].equals("1")) title = "예비과제";
		else if(pjt_list[0][1].equals("2")) title = "정식과제";
	}

	//--------------------------------------
	// 연관과제 리스트 가져오기
	//--------------------------------------
	ArrayList link_list = new ArrayList();
	link_list = (ArrayList)request.getAttribute("LINK_List");
	com.anbtech.psm.entity.psmMasterTable link = new psmMasterTable();
	Iterator link_iter = link_list.iterator();

	int link_cnt = 0;
	link_cnt = link_list.size();
	String[][] link_data = new String[link_cnt][4];
	for(int i=0; i<link_cnt; i++) for(int j=0; j<4; j++) link_data[i][j]="";
	int cn=0;
	while(link_iter.hasNext()) {
		link = (psmMasterTable)link_iter.next();
		link_data[cn][0] = link.getPid();
		link_data[cn][1] = link.getPsmCode();
		link_data[cn][2] = link.getPsmType();
		link_data[cn][3] = link.getPsmKorea();
		cn++;		
	}

%>

<HTML>
<HEAD><TITLE>과제내용 상세보기</TITLE>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../psm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<form name="eForm" method="post" style="margin:0" encType="multipart/form-data">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../psm/images/blet.gif"> <%=title%> 등록내용</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=27><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width='20%' style='padding-left:5px;'>
					<a href="javascript:sendStatus();"><img src='../psm/images/status_mgr.gif' border='0' align='absmiddle' alt='상태관리'></a>
					<a href="javascript:sendBudget();"><img src='../psm/images/money_mgr.gif' border='0' align='absmiddle' alt='예산관리'></a>
					<a href="javascript:sendList();"><img src="../psm/images/bt_list.gif" border=0 align='absmiddle'></a>
					</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
</TABLE>

<!--내용-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">작성자</td>
			   <td width="37%" height="25" class="bg_04"><%=psm_user%></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">작성일</td>
			   <td width="37%" height="25" class="bg_04"><%=reg_date%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제코드</td>
			   <td width="37%" height="25" class="bg_04"><%=psm_code%></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제종류</td>
			   <td width="37%" height="25" class="bg_04"><%=psm_type%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="15%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제구분</td>
			   <td width="35%" height="25" class="bg_04">
					<select name='psm_v_name' disabled>
					<%
						String[] plist = {"V","VM","N"};
						String[] plist_name = {"단독과제","복합메인","복합서브"};
						String psel="";
								
						for(int i=0; i<plist.length; i++) {
							if(psm_view.equals(plist[i])) psel="selected";
							else psel = "";
							out.println("<option value='"+plist[i]+"' "+psel+" >"+plist_name[i]+"</option>");
						}
					%></select>
				</td>
			   <td width="15%" height="25" class="bg_03" background="../psm/images/bg-01.gif">연관과제코드</td>
			   <td width="35%" height="25" class="bg_04">
					<select NAME='link_code' onChange="javascript:psmView();">
					<% 
					String v_link = "",n_link="";
					for(int i=0; i<link_cnt; i++) { 
						v_link =link_data[i][0]+"|"+link_data[i][2];
						n_link =link_data[i][1]+"["+link_data[i][3]+"]";
						out.println("<option value='"+v_link+"'>"+n_link+"</option>");
					} 
					%></select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="15%" height="25" class="bg_03" background="../psm/images/bg-01.gif">제품코드</td>
			   <td width="35%" height="25" class="bg_04"><%=pd_code%></td>
			   <td width="15%" height="25" class="bg_03" background="../psm/images/bg-01.gif">제품명</td>
			   <td width="35%" height="25" class="bg_04"><%=pd_name%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제고객</td>
			   <td width="37%" height="25" class="bg_04"><%=comp_name%></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제카테고리</td>
			   <td width="37%" height="25" class="bg_04"><%=comp_category%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제명(한글)</td>
			   <td width="37%" height="25" class="bg_04"><%=psm_korea%></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제명(영문)</td>
			   <td width="37%" height="25" class="bg_04"><%=psm_english%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제시작일</td>
			   <td width="37%" height="25" class="bg_04"><%=psm_start_date%></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제완료일</td>
			   <td width="37%" height="25" class="bg_04"><%=psm_end_date%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제담당자</td>
			   <td width="37%" height="25" class="bg_04"><%=psm_mgr%></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">예산담당자</td>
			   <td width="37%" height="25" class="bg_04"><%=psm_budget%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">과제PM</td>
			   <td width="37%" height="25" class="bg_04"><%=psm_pm%></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif" rowspan=3>과제설명</td>
			   <td width="37%" height="25" class="bg_04" rowspan=3>
					<TEXTAREA class='text_01' NAME='psm_desc' rows=3 cols=42 style='border:1px solid #787878;' readonly><%=psm_desc%></TEXTAREA></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">진행상태</td>
			   <td width="37%" height="25" class="bg_04">
					<select name='psm_status' disabled>
					<%
						String[] list = {"1","2","3","4","5","6"};
						String[] list_name = {"미진행","진행","재진행","완료","보류","취소"};
						String csel="";
						
						for(int i=0; i<list.length; i++) {
							if(psm_status.equals(list[i])) csel="selected";
							else csel = "";
							out.println("<option value='"+list[i]+"' "+csel+" >"+list_name[i]+"</option>");
						}
					%></select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr> <!-- 예산정보 -->
				<td height="25" colspan="4"><img src='../psm/images/title_money_info.gif' border='0' align='absmiddle' alt='과제 예산정보'>
						&nbsp;&nbsp;&nbsp;<img src='../psm/images/balance.gif' border='0' align='absmiddle'><font color='#FF3333'> <%=diff_sum%></font>원</td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">예산 총액</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_sum' value='<%=plan_sum%>' size=15 maxlength=16 style="border:0px;text-align:right;" readonly> 원</td>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">지출 총액</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_sum' value='<%=result_sum%>' size=15 maxlength=16 style="border:0px;text-align:right;" readonly> 원</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">예산 인건비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_labor' value='<%=plan_labor%>' size=15 maxlength=16 style="border:0px;text-align:right;" readonly> 원</td>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">지출 인건비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_labor' value='<%=result_labor%>' size=15 maxlength=16 style="border:0px;text-align:right;" readonly> 원</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">예산 재료비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_labor' value='<%=plan_material%>' size=15 maxlength=16 style="border:0px;text-align:right;" readonly> 원</td>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">지출 재료비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_labor' value='<%=result_material%>' size=15 maxlength=16 style="border:0px;text-align:right;" readonly> 원</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">예산 경비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_labor' value='<%=plan_cost%>' size=15 maxlength=16 style="border:0px;text-align:right;" readonly> 원</td>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">지출 경비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_labor' value='<%=result_cost%>' size=15 maxlength=16 style="border:0px;text-align:right;" readonly> 원</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">예산 용역비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_labor' value='<%=plan_plant%>' size=15 maxlength=16 style="border:0px;text-align:right;" readonly> 원</td>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">지출 용역비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_labor' value='<%=result_plant%>' size=15 maxlength=16 style="border:0px;text-align:right;" readonly> 원</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr> <!-- 수주정보 -->
				<td height="25" colspan="4"><img src='../psm/images/title_order_info.gif' border='0' align='absmiddle' alt='과제 수주정보'></td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">계약명</td>
			   <td width="37%" height="25" class="bg_04"><%=contract_name%></td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">계약일</td>
			   <td width="37%" height="25" class="bg_04"><%=contract_date%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">계약금액</td>
			   <td width="37%" height="25" class="bg_04">
					<input type='text' name='plan_labor' value='<%=contract_price%>' size=15 maxlength=16 style="border:0px;text-align:right;" readonly> 원</td>
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">납기일자</td>
			   <td width="37%" height="25" class="bg_04"><%=complete_date%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="13%" height="25" class="bg_03" background="../dcm/images/bg-01.gif">첨부파일</td>
			   <td width="87%" height="25" class="bg_04" colspan="3">
				<% 
				for(int i=0,no=1; no<attache_cnt; i++,no++) {
					if((i<cnt) && (addFile[i][0].length() != 0)) {		//등록된 첨부가 있을때
						out.print("<a href='../psm/attach_download.jsp?fname="+addFile[i][0]);
						out.print("&ftype="+addFile[i][1]+"&fsize="+addFile[i][2]);
						out.print("&sname="+addFile[i][3]+"&extend="+bon_path+"'>"+addFile[i][0]+"</a>");
						out.println("<br>"); 
					}
				}
				%>			   
			   </td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value=''>
<input type='hidden' name='psm_code' value='<%=psm_code%>'>
<input type='hidden' name='psm_type' value=''>
<input type='hidden' name='psm_status' value='<%=psm_status%>'>
<input type='hidden' name='psm_mgr' value='<%=psm_mgr%>'>
<input type='hidden' name='psm_budget' value='<%=psm_budget%>'>
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
<script language=javascript>
<!--
//msg 처리
var msg = '<%=msg%>';
if(msg.length != 0) {
	alert(msg);
	history.back(-1);
}

//상태관리
function sendStatus()
{
	var mgr = '<%=mgr%>';
	if(mgr != '과제담당자') { alert('과제 상태관리권한이 없습니다.'); return; }

	var psm_code = document.eForm.psm_code.value;
//	var Url = "../psm/prs/psmStatusFrame.jsp?psm_code=" + psm_code;
//	document.eForm.action=Url;	
	document.eForm.action='../servlet/PsmStatusServlet';	
	document.eForm.mode.value='sts_prewrite';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//예산관리
function sendBudget()
{
	var budget = '<%=budget%>';
	if(budget != '예산담당자') { alert('과제 예산관리권한이 없습니다.'); return; }

	var psm_code = document.eForm.psm_code.value;
//	var Url = "../psm/prs/psmBudgetFrame.jsp?psm_code=" + psm_code;
//	document.eForm.action=Url;	
	document.eForm.action='../servlet/PsmBudgetServlet';	
	document.eForm.mode.value='bud_prewrite';
	document.onmousedown=dbclick;
	document.eForm.submit();	
}
//목록으로
function sendList()
{
	document.eForm.action='../servlet/PsmBaseInfoServlet';
	document.eForm.mode.value='psm_list';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//상세내용보기
function psmView()
{
	var link_code = document.eForm.link_code.value;
	var link_data = link_code.split("|");
	var pid = link_data[0];
	var psm_type = link_data[1];


	document.eForm.action='../servlet/PsmStatusServlet';
	document.eForm.mode.value='sts_preview';
	document.eForm.pid.value=pid;
	document.eForm.psm_type.value=psm_type;
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//창
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}
//데이터 처리중 버튼막기
function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
//금액 천단위에 콤마넣기
function InputMoney(input,obj){
	str = input.value;
	str = unComma(str);
	MoneyToHan(str,obj)
	str = Comma(str);
	input.value = str;

	sumMoney();
}
//총액표시하기
function sumMoney(){
	labor = unCommaObj(document.eForm.plan_labor.value);
	material = unCommaObj(document.eForm.plan_material.value);
	cost = unCommaObj(document.eForm.plan_cost.value);
	plant = unCommaObj(document.eForm.plan_plant.value);
	
	sum = eval(labor+material+cost+plant); 
	MoneyToHanguel(sum);
	document.eForm.plan_sum.value=Comma(sum);
}
//한글금액 포시
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
//총금액 한글금액 표시
function MoneyToHanguel(str){
	arrayNum=new Array("0","1","2","3","4","5","6","7","8","9");
	arrayUnit=new Array("","","","","만 ","","","","억 ","","","","조 ","","","","경 ");
	arrayStr= new Array()
	str = str.toString();
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
	document.eForm.plan_sum_money.value = hanStr;
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