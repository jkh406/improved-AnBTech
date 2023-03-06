<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info= "과제내용 상세보기 EXCEL출력"		
	contentType = "application/vnd.ms-excel; charset=euc-kr" 	
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
<HEAD><title>과제내용 상세보기 EXCEL출력</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
</HEAD>

<BODY topmargin="0" leftmargin="0">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR>
					<TD valign='middle'></TD>
					<TD valign='middle'><%=psm_korea%>과제 등록내용</TD>
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
		<table cellspacing=0 cellpadding=2 width="100%" border=1>
			<tbody>
			<tr> <!-- 과제정보 -->
				<td height="25" colspan="4">&nbsp;<b>과제 정보</b></td></tr>
			<tr>
			   <td width="13%" height="25">작성자</td>
			   <td width="37%" height="25"><%=psm_user%></td>
			   <td width="13%" height="25">작성일</td>
			   <td width="37%" height="25"><%=reg_date%></td>
			</tr>
			<tr>
			   <td width="13%" height="25">과제코드</td>
			   <td width="37%" height="25"><%=psm_code%></td>
			   <td width="13%" height="25">과제종류</td>
			   <td width="37%" height="25"><%=psm_type%></td>
			</tr>
			<tr>
			   <td width="13%" height="25">과제구분</td>
			   <td width="37%" height="25">
					<%
						if(psm_view.equals("V")) out.println("단독과제");
						else if(psm_view.equals("VM")) out.println("복합메인");
						else if(psm_view.equals("N")) out.println("복합서브");
					%>
				</td>
			   <td width="13%" height="25">연관과제코드</td>
			   <td width="37%" height="25">
					<% 
					String n_link="";
					for(int i=0; i<link_cnt; i++) { 
						n_link =link_data[i][1]+"["+link_data[i][3]+"]";
						out.println(n_link);
					} 
					%></td>
			</tr>
			<tr>
			   <td width="13%" height="25">제품코드</td>
			   <td width="37%" height="25"><%=pd_code%></td>
			   <td width="13%" height="25">제품명</td>
			   <td width="37%" height="25"><%=pd_name%></td>
			</tr>
			<tr>
			   <td width="13%" height="25">과제고객</td>
			   <td width="37%" height="25"><%=comp_name%></td>
			   <td width="13%" height="25">과제카테고리</td>
			   <td width="37%" height="25"><%=comp_category%></td>
			</tr>
			<tr>
			   <td width="13%" height="25">과제명(한글)</td>
			   <td width="37%" height="25"><%=psm_korea%></td>
			   <td width="13%" height="25">과제명(영문)</td>
			   <td width="37%" height="25"><%=psm_english%></td>
			</tr>
			<tr>
			   <td width="13%" height="25">과제시작일</td>
			   <td width="37%" height="25"><%=psm_start_date%></td>
			   <td width="13%" height="25">과제완료일</td>
			   <td width="37%" height="25"><%=psm_end_date%></td>
			</tr>
			<tr>
			   <td width="13%" height="25">과제담당자</td>
			   <td width="37%" height="25"><%=psm_mgr%></td>
			   <td width="13%" height="25">예산담당자</td>
			   <td width="37%" height="25"><%=psm_budget%></td>
			</tr>
			<tr>
			   <td width="13%" height="25">과제PM</td>
			   <td width="37%" height="25"><%=psm_pm%></td>
			   <td width="13%" height="25">진행상태</td>
			   <td width="37%" height="25">
					<%
						String[] list = {"1","2","3","4","5","6"};
						String[] list_name = {"미진행","진행","재진행","완료","보류","취소"};
						for(int i=0; i<list.length; i++) {
							if(psm_status.equals(list[i])) out.println(list_name[i]);
						}
					%></select></td>
			</tr>
			<tr>
			   <td width="13%" height="25">과제설명</td>
			   <td width="87%" height="25" colspan=3><%=psm_desc%></td>
			</tr>
			<tr> <!-- 예산정보 -->
				<td height="25" colspan="4">&nbsp;<b>과제 예산정보
						&nbsp;&nbsp;&nbsp;[잔액 : <%=diff_sum%> ]</b></td></tr>
			<tr>
				<td width="10%" height="25">예산 총액</td>
				<td width="40%" height="25"><%=plan_sum%></td>
				<td width="10%" height="25">지출 총액</td>
				<td width="40%" height="25"><%=result_sum%></td>
			</tr>
			<tr>
				<td width="10%" height="25">예산 인건비</td>
				<td width="40%" height="25"><%=plan_labor%></td>
				<td width="10%" height="25">지츨 인건비</td>
				<td width="40%" height="25"><%=result_labor%></td>
			</tr>
			<tr>
				<td width="10%" height="25">예산 재료비</td>
				<td width="40%" height="25"><%=plan_material%></td>
				<td width="10%" height="25">지츨 재료비</td>
				<td width="40%" height="25"><%=result_material%></td>
			</tr>
			<tr>
				<td width="10%" height="25">예산 경비</td>
				<td width="40%" height="25"><%=plan_cost%></td>
				<td width="10%" height="25">지츨 경비</td>
				<td width="40%" height="25"><%=result_cost%></td>
			</tr>
			<tr>
				<td width="10%" height="25">예산 용역비</td>
				<td width="40%" height="25"><%=plan_plant%></td>
				<td width="10%" height="25">지츨 용역비</td>
				<td width="40%" height="25"><%=result_plant%></td>
			</tr>
			<tr> <!-- 수주정보 -->
				<td height="25" colspan="4">&nbsp;<b>과제 수주정보</b></td></tr>
			<tr>
			   <td width="13%" height="25">계약명</td>
			   <td width="37%" height="25"><%=contract_name%></td>
			   <td width="13%" height="25">계약일</td>
			   <td width="37%" height="25"><%=contract_date%></td>
			</tr>
			<tr>
			   <td width="13%" height="25">계약금액</td>
			   <td width="37%" height="25"><%=contract_price%></td>
			   <td width="13%" height="25">납기일자</td>
			   <td width="37%" height="25"><%=complete_date%></td>
			</tr>
			<tr>
			   <td width="13%" height="25">첨부파일</td>
			   <td width="87%" height="25" colspan="3">
				<% 
				for(int i=0,no=1; no<attache_cnt; i++,no++) {
					if((i<cnt) && (addFile[i][0].length() != 0)) {		//등록된 첨부가 있을때
						out.print("첨부파일 있음");
					}
				}
				%>			   
			   </td>
			</tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>

</body>
</html>
