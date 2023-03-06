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
<HEAD><title>과제내용 상세보기</title>
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
					<TD valign='middle' class="title"><img src="../psm/images/blet.gif"> <%=psm_korea%>과제 등록내용</TD>
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
					<a href="javascript:sendList();"><img src="../psm/images/bt_list.gif" border=0></a>
					<a href="javascript:sendExcel();"><img src="../psm/images/bt_excel.gif" border=0></a>
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
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">지츨 인건비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_labor' value='<%=result_labor%>' size=15 maxlength=16 style="border:0px;text-align:right;" readonly> 원</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">예산 재료비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_labor' value='<%=plan_material%>' size=15 maxlength=16 style="border:0px;text-align:right;" readonly> 원</td>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">지츨 재료비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_labor' value='<%=result_material%>' size=15 maxlength=16 style="border:0px;text-align:right;" readonly> 원</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">예산 경비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_labor' value='<%=plan_cost%>' size=15 maxlength=16 style="border:0px;text-align:right;" readonly> 원</td>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">지츨 경비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_labor' value='<%=result_cost%>' size=15 maxlength=16 style="border:0px;text-align:right;" readonly> 원</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">예산 용역비</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_labor' value='<%=plan_plant%>' size=15 maxlength=16 style="border:0px;text-align:right;" readonly> 원</td>
				<td width="10%" height="25" class="bg_03" background="../psm/images/bg-01.gif">지츨 용역비</td>
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
			   <td width="13%" height="25" class="bg_03" background="../psm/images/bg-01.gif">첨부파일</td>
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
<input type='hidden' name='pid' value='<%=pid%>'>
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
//목록으로
function sendList()
{
	document.eForm.action='../servlet/PsmProcessServlet';
	document.eForm.mode.value='view_search';
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//EXCEL출력
function sendExcel()
{
	pid = document.eForm.pid.value;
	var strUrl = "../servlet/PsmProcessServlet?mode=excel_project&pid="+pid;
	wopen(strUrl,"PMexcel","870","600","scrollbars=yes,toolbar=no,menubar=yes,status=yes,resizable=yes");
}
//상세내용보기
function psmView()
{
	var link_code = document.eForm.link_code.value;
	var link_data = link_code.split("|");
	var pid = link_data[0];
	
	document.eForm.action='../servlet/PsmProcessServlet';
	document.eForm.mode.value='view_project';
	document.eForm.pid.value=pid;
	document.onmousedown=dbclick;
	document.eForm.submit();
}
//창띄우기 공통
function wopen(url, t, w, h, s) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+','+s);
}
//데이터 처리중 버튼막기
function dbclick()
{
	if(event.button==1) alert("이전 작업을 처리중입니다. 잠시만 기다려 주십시오.");
}
-->
</script>