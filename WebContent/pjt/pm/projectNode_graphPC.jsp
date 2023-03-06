<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "진행중과제 Gantt Chart[진행율/수정일]보기"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.pjt.entity.*"
	import="com.anbtech.date.anbDate"
%>
<%
	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();
	String psd="",ped="",csd="",ced="",rsd="",red="";

	//-----------------------------------
	//	파라미터 받기
	//-----------------------------------
	String pjt_code="",pjtWord="",sItem="",sWord="",parent_node="",child_node="";
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");
	para = new projectTable();
	Iterator para_iter = para_list.iterator();
	
	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();
		pjt_code = para.getPjtCode();
		pjtWord = para.getPjtword();
		sItem = para.getSitem();
		sWord = para.getSword();
		parent_node = para.getParentNode();
		child_node = para.getChildNode();
	}

	//-----------------------------------
	//	과제일정정보 편집하기
	//-----------------------------------
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

	//해당과제의 일정 전체 LIST
	com.anbtech.pjt.entity.projectTable sch;
	ArrayList sch_list = new ArrayList();
	sch_list = (ArrayList)request.getAttribute("SCH_List");
	sch = new projectTable();
	Iterator sch_iter = sch_list.iterator();
	int sch_cnt = sch_list.size();

	String[][] schedule = new String[sch_cnt][10];
	int m = 0;
	while(sch_iter.hasNext()) {
		sch = (projectTable)sch_iter.next();
		
		schedule[m][0] = sch.getNodeName();							//노드명
		schedule[m][1] = sch.getPlanStartDate();					//계획시작일
		schedule[m][2] = sch.getPlanEndDate();						//계획종료일

		csd = sch.getChgStartDate();	
		if(csd.length() != 0) schedule[m][1] = csd;					//수정시작일 로
		ced = sch.getChgEndDate();
		if(ced.length() != 0) schedule[m][2] = ced;					//수정종료일 로
 
		schedule[m][3] = Double.toString(sch.getProgress()*100);	//진행율(%)

		schedule[m][4] = Integer.toString(sch.getPlanCnt());		//계획일수
		if(csd.length() != 0) schedule[m][4] = Integer.toString(sch.getChgCnt());//수정일수 로

		schedule[m][5] = schedule[m][1];							//실적시작일(계획시작일과 동일하게)

		//예상진행일수(진행율로 계산)
		if(sch.getProgress() != 1.0) {		//100%
			String ps = Double.toString(sch.getProgress() * Integer.parseInt(schedule[m][4]));	
			ps = ps.substring(0,ps.indexOf("."));
			int dps = Integer.parseInt(ps);
			schedule[m][6] = anbdt.getDate(schedule[m][1],dps,"");

			//일수가 너무 작으면 같은날자가 생길수 있어 보상
			if(schedule[m][2].equals(schedule[m][6])) {
				schedule[m][6] = anbdt.getDate(schedule[m][2],-1,"");	//-1일 "":날자출력포멧
			}
		} else {
			schedule[m][6] = schedule[m][2];
		}

		schedule[m][7] = sch.getLevelNo();							//노드 Level No
		schedule[m][8] = sch.getChgStartDate();						//수정시작 일
		schedule[m][9] = sch.getChgEndDate();						//수정종료 일
		m++;
	}

	//전체 과제일정 갯수 구하기
	int pn_cnt = Integer.parseInt(schedule[0][4]);	

	//과제전체 진행율 구하기
	String pjt_progress = schedule[0][3];
	pjt_progress = Double.toString(Double.parseDouble(pjt_progress)+0.001);
	pjt_progress = pjt_progress.substring(0,pjt_progress.indexOf('.')+2);

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet>
<script language=javascript>
<!--
//검색하기
function goSearch()
{
	var v = document.gForm.v.value;

	document.sForm.action='../servlet/projectNodeAppServlet';
	if(v == 'ganttPP') document.sForm.mode.value='PSN_GPP';			//gantt chart 보기 : 진행율/계획일
	else if(v == 'ganttPC') document.sForm.mode.value='PSN_GPC';	//gantt chart 보기 : 진행율/수정일
	else if(v == 'ganttSP') document.sForm.mode.value='PSN_GSP';	//gantt chart 보기 : 실적일/계획일
	else if(v == 'ganttSC') document.sForm.mode.value='PSN_GSC';	//gantt chart 보기 : 실적일/수정일
	else document.sForm.mode.value='PSN_L';							//text로 보기

	document.sForm.submit();
}
//과제선택하기
function goProject()
{
	var sItem = '<%=sItem%>';
	var sWord = '<%=sWord%>';
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	var v = document.gForm.v.value;
	
	document.eForm.action='../servlet/projectNodeAppServlet';
	if(v == 'ganttPP') document.eForm.mode.value='PSN_GPP';			//gantt chart 보기 : 진행율/계획일
	else if(v == 'ganttPC') document.eForm.mode.value='PSN_GPC';	//gantt chart 보기 : 진행율/수정일
	else if(v == 'ganttSP') document.eForm.mode.value='PSN_GSP';	//gantt chart 보기 : 실적일/계획일
	else if(v == 'ganttSC') document.eForm.mode.value='PSN_GSC';	//gantt chart 보기 : 실적일/수정일
	else document.eForm.mode.value='PSN_L';							//text로 보기

	document.eForm.sItem.value=sItem;
	document.eForm.sWord.value=sWord;
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.submit();
}
//과제 보기선택하기
function goView()
{
	var sItem = '<%=sItem%>';
	var sWord = '<%=sWord%>';
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	var v = document.gForm.v.value;

	document.eForm.action='../servlet/projectNodeAppServlet';
	if(v == 'ganttPP') document.eForm.mode.value='PSN_GPP';			//gantt chart 보기 : 진행율/계획일
	else if(v == 'ganttPC') document.eForm.mode.value='PSN_GPC';	//gantt chart 보기 : 진행율/수정일
	else if(v == 'ganttSP') document.eForm.mode.value='PSN_GSP';	//gantt chart 보기 : 실적일/계획일
	else if(v == 'ganttSC') document.eForm.mode.value='PSN_GSC';	//gantt chart 보기 : 실적일/수정일
	else document.eForm.mode.value='PSN_L';							//text로 보기

	document.eForm.sItem.value=sItem;
	document.eForm.sWord.value=sWord;
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.submit();
}
//해당노드 이력보기
function detailNode(pjt_code,parent_node,child_node)
{
	document.vForm.action='../servlet/projectNodeAppServlet';
	document.vForm.mode.value='PSN_AV';
	document.vForm.pjt_code.value=pjt_code;
	document.vForm.parent_node.value=parent_node;
	document.vForm.child_node.value=child_node;
	document.vForm.submit();
}
//풍선 도움말 보기
var select_obj;
function ANB_layerAction(obj,status) 
{ 
	var _tmpx,_tmpy, marginx, marginy;
		_tmpx = event.clientX + parseInt(obj.offsetWidth);
		_tmpy = event.clientY + parseInt(obj.offsetHeight);
		_marginx = document.body.clientWidth - _tmpx;
		_marginy = document.body.clientHeight - _tmpy ;
	if(_marginx < 0)
		_tmpx = event.clientX + document.body.scrollLeft + _marginx ;
	else
		_tmpx = event.clientX + document.body.scrollLeft ;
	if(_marginy < 0)
		_tmpy = event.clientY + document.body.scrollTop + _marginy +20;
	else
		_tmpy = event.clientY + document.body.scrollTop ;

	obj.style.posLeft=_tmpx-13;
	obj.style.posTop=_tmpy+20;

	if(status=='visible') {
		if(select_obj) {
			select_obj.style.visibility='hidden';
			select_obj=null;
		}
		select_obj=obj;
	}else{
		select_obj=null;
	}
	obj.style.visibility=status; 
}
-->
</script>
</HEAD>

<BODY topmargin="0" link="blue" alink="blue" vlink="blue" leftmargin="0">

<!-- 상단 시작 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- 타이틀 및 페이지 정보 -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
				<TD valign='middle' class="title"><img src="../pjt/images/blet.gif" align="absmiddle"> 과제진행정보</TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR height=32><!--버튼 및 페이징-->
		<TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
				<TD width=4>&nbsp;</TD>
				<TD align=left width='280'>
					<form name="sForm" method="post" style="margin:0">
					<select name="sItem" style=font-size:9pt;color="black";>  
					<%
						String[] sitems = {"pjt_name","pjt_code"};
						String[] snames = {"과제이름","과제코드"};
						String sel = "";
						for(int si=0; si<sitems.length; si++) {
							if(sItem.equals(sitems[si])) sel = "selected";
							else sel = "";
							out.println("<option "+sel+" value='"+sitems[si]+"'>"+snames[si]+"</option>");
						}
					%>
					</select>
					<input type="text" name="sWord" size="15" value="<%=sWord%>">
					<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
					<input type="hidden" name="mode" size="15" value="">
					<input type="hidden" name="pid" size="15" value="">
					<a href='Javascript:goSearch();'><img src='../pjt/images/bt_search3.gif' border='0' align='absmiddle'></a>
					</form>
				</TD>
				<TD align=left width=''>
					<form name="eForm" method="post" style="margin:0">과 제 명
					<%
						out.println("<select name='project' onChange='javascript:goProject()' style=font-size:9pt;color='black';>");
						String psel = "";
						for(int i=0; i<pjt_cnt; i++) {
							if(pjt_code.equals(project[i][0])) psel = "selected";
							else psel = "";
							out.print("<option "+psel+" value='"+project[i][0]+"|"+project[i][1]+"'>");
							out.println(project[i][0]+" "+project[i][1]+"</option>");
						}
					%>
					<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
					<input type="hidden" name="sItem" size="15" value="<%=sItem%>">
					<input type="hidden" name="sWord" size="15" value="<%=sWord%>">
					<input type="hidden" name="mode" size="15" value="">
					<input type="hidden" name="pjt_code" size="15" value="">
					<input type="hidden" name="pjt_name" size="15" value="">
					<input type="hidden" name="level_no" size="15" value="0">
					<input type="hidden" name="parent_node" size="15" value="0">
					<input type="hidden" name="pid" size="15" value="">
					</form>
				</TD>
				<TD align=left width=''>
					<form name="gForm" method="post" style="margin:0">
					<select name='v' onChange='javascript:goView()' style=font-size:9pt;color='black';>
						<option value='text'>TEXT</option>
						<option value='ganttPP'>그래프[진행율/계획일]</option>
						<option selected value='ganttPC'>그래프[진행율/수정일]</option>
						<option value='ganttSP'>그래프[실적일/계획일]</option>
						<option value='ganttSC'>그래프[실적일/수정일]</option>
					</select>
					</form>
				</TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
	</TR>
	</TBODY>
</TABLE>

<!-- LIST 시작 -->
<TABLE cellSpacing=0 cellPadding=0 width="100%" border=1 valign='top'>
	<TR vAlign=center bgColor=#E6F5FF height=22>
		<TD noWrap width=300 align=middle>
			일정단계 / 구분
		</TD>
		<TD noWrap width=100% background="../pjt/img/list_bg.gif">
			<% //----- 일자 LIST -----// 
			out.println("<TABLE cellSpacing=0 cellPadding=0 width='100%' border=1 valign='top'>");
			//공통 변수
			String ymd = "",tag="1",pjt_sd="",pjt_ed="";
			int cy=0,cm=0,cd=0,colspan=0;	//년,월,일,colspan

			//--------------------------
			//phase표시하기
			//--------------------------
			out.println("<TR vAlign=center bgColor=#E6F5FF height=22>");
			pjt_sd = schedule[0][1];									//각Phase의 계획시작 년월일
			for(int i=0; i<sch_cnt; i++) {
				if(schedule[i][7].equals("1")) {
					pjt_ed = schedule[i][2];							//각Phase의 계획종료 년월일
					colspan = anbdt.getPeriodDate(pjt_sd,pjt_ed);		//남은일수 구하기
					out.print("<TD noWrap align=center colspan='"+colspan+"'  background='../pjt/img/list_bg.gif'>"+schedule[i][0]+"</TD>");
					pjt_sd = pjt_ed;									//전phase을 다음phase의 시작일로
				}
			}
			out.println("</TR>");

			//--------------------------
			//년월 표시하기
			//--------------------------
			int yy = Integer.parseInt(schedule[0][1].substring(0,4));	//계획(수정)시작 년도
			int mm = Integer.parseInt(schedule[0][1].substring(4,6));	//계획(수정)시작 월
			int dd = Integer.parseInt(schedule[0][1].substring(6,8));	//계획(수정)시작 일
			int lyy = Integer.parseInt(schedule[0][2].substring(0,4));	//계획(수정)종료 년도
			int lmm = Integer.parseInt(schedule[0][2].substring(4,6));	//계획(수정)종료 월
			int ldd = Integer.parseInt(schedule[0][2].substring(6,8));	//계획(수정)종료 일 (마지막달 일수임)
			out.println("<TR vAlign=center bgColor=#E6F5FF height=22>");
			for(int i=0; i<pn_cnt; i++) {
				ymd = anbdt.getDate(yy,mm,dd,i);					//년/월/일
				cy = Integer.parseInt(ymd.substring(0,4));			//년
				cm = Integer.parseInt(ymd.substring(5,7));			//월
				cd = Integer.parseInt(ymd.substring(8,10));			//일
				colspan = anbdt.getDatePeriod(cy,cm,cd);			//남은일수 구하기(첫달,중간달 적용)
				if((cy == lyy) && (cm == lmm)) colspan = ldd;		//마지막달 colspan

				if(tag.equals("1")) {	//처음 무조건 출력
					if(colspan > 15) {	//년월 표시
						out.print("<TD noWrap align=center colspan='"+colspan+"'  background='../pjt/img/list_bg.gif'>"+cy+"년"+cm+"월</TD>");
					} else {			//년월 표시 제외
						out.print("<TD noWrap align=center colspan='"+colspan+"'  background='../pjt/img/list_bg.gif'>&nbsp;</TD>");
					}
					tag = "0";
				} else if(cd == 1) {	//매 첫달시작후 15일이면 출력
					if(colspan > 15) {	//년월 표시
						out.print("<TD noWrap align=center colspan='"+colspan+"'  background='../pjt/img/list_bg.gif'>"+cy+"년"+cm+"월</TD>");
					} else {			//년월 표시 제외
						out.print("<TD noWrap align=center colspan='"+colspan+"'  background='../pjt/img/list_bg.gif'>&nbsp;</TD>");
					}
				}
			}
			out.println("</TR>");

			//--------------------------
			//아랫단 눈금표시 (일자눈금)
			//--------------------------
			out.println("<TR vAlign=center bgColor=#E6F5FF height=5>");
			for(int i=0; i<pn_cnt; i++) {
				//ymd = anbdt.getDate(yy,mm,dd,i);					//년/월/일
				//cm = Integer.parseInt(ymd.substring(5,7));			//월
				out.println("<TD noWrap width=5 height=8 align=right bgcolor='darkgray'></TD>");
			}
			out.println("</TR>");
		
			out.println("</TABLE>");
			%>
		</TD>
	</TR>
	<TR vAlign=center bgColor=#E6F5FF height=22>
		<TD noWrap width=300 align=middle background="../pjt/img/list_bg.gif">
			<% //----- PROCESS LIST -----// 
			String progress = "",space="";
			for(int i=1; i<sch_cnt; i++) {
				//phase,step,activity구분하기
				if(schedule[i][7].equals("1")) {
					space="<IMG src='../pjt/icons/base.gif'>";
				} else if(schedule[i][7].equals("2")) {
					space="&nbsp;&nbsp;<IMG src='../pjt/icons/folder.gif'>";
				} else if(schedule[i][7].equals("3")) {
					space = "&nbsp;&nbsp;&nbsp;&nbsp;<IMG src='../pjt/icons/page.gif'>";
				}

				//진행율 소숫점 정리
				progress = schedule[i][3];
				progress = Double.toString(Double.parseDouble(progress)+0.001);
				progress = progress.substring(0,progress.indexOf('.')+2);

				out.println("<TABLE cellSpacing=0 cellPadding=0 width='100%' border=0 valign='top'>");
				out.println("	<TR vAlign=center bgColor=#E6F5FF height=22>");
				out.println("		<TD noWrap width=100% align=left rowspan=2  background='../pjt/img/list_bg.gif'>"+space+schedule[i][0]+"["+progress+"%]&nbsp;&nbsp;</TD>");
				out.println("	</TR>");
				out.println("	<TR vAlign=center height=2><TD noWrap width=100% ></TD></TR>");
				out.println("</TABLE>");
			}
			%>
		</TD>
		<TD noWrap width=100% background="../pjt/img/list_bg.gif">
			<% //----- 진행율(계획/실적) LIST -----// 
			String pdate = "";
			for(int r=1; r<sch_cnt; r++) {
				progress = schedule[r][3];
				progress = Double.toString(Double.parseDouble(progress)+0.001);
				progress = progress.substring(0,progress.indexOf('.')+2);
				out.println("<TABLE cellSpacing=0 cellPadding=0 width='100%' border=0 valign='top'>");

				//--------------------------
				//계획 표시하기
				//--------------------------
				out.println("	<TR vAlign=center bgColor=#E6F5FF height=22>");
				int py = Integer.parseInt(schedule[0][1].substring(0,4)); //년도
				int pm = Integer.parseInt(schedule[0][1].substring(4,6)); //월
				int pd = Integer.parseInt(schedule[0][1].substring(6,8)); //일
				String con = "0",pjsd="",pjed="",pcsd="",pced="";	//출력표기,계획시작일,종료일,수정시작,종료일
				for(int i=0; i<pn_cnt; i++) {
					pdate = str.repWord(anbdt.getDate(py,pm,pd,i),"/",""); 
					//시작일
					if(pdate.equals(schedule[r][1])) {if(con.equals("0")) con = "1"; else con = "0"; }
					
					//풍선도움말용
					pjsd = anbdt.getSepDate(schedule[r][1],"/");	//계획시작일
					pjed = anbdt.getSepDate(schedule[r][2],"/");	//계획종료일 	
					if(schedule[r][8].length() == 8) pcsd=anbdt.getSepDate(schedule[r][8],"/");//수정시작일
					if(schedule[r][9].length() == 8) pced=anbdt.getSepDate(schedule[r][9],"/");//수정종료일

					//표시하기
					if(con.equals("1")) {
						out.print("<TD noWrap width=5 height=10 align=left bgcolor='479933' ");
						out.print("onmouseover=\"ANB_layerAction(z"+r+i+", 'visible')\" onmouseout=\"ANB_layerAction(z"+r+i+", 'hidden')\">");
						out.print("<div id=z"+r+i+" style=\"position:absolute;background-color:#FEFEED;width:250;	height:50;padding-top:5px ;padding-left:5px ;font:9pt '돋움체';border:#C0C0C0 1px solid;visibility:hidden;\"><font color=#009966>["+schedule[r][0]+"]</font><br>계획기간:"+pjsd+" ~ "+pjed+"<br>수정기간:"+pcsd+" ~ "+pced+"<br>진행율:"+progress+"%</div>");
						out.println("</TD>");
					} 
					else if(pdate.equals(anbdt.getDateNoformat())) {		//금일표시
						out.println("<TD noWrap width=5 height=10 align=right bgcolor='ff9999'></TD>");
					}
					else {
						out.println("<TD noWrap width=5 height=10 align=right bgcolor='white'></TD>");
					}

					//종료일
					if(pdate.equals(schedule[r][2])) { if(con.equals("0")) con = "1"; else con = "0";}
				}
				out.println("	</TR>");

				//--------------------------
				//결과표시하기 (진행율)
				//--------------------------
				out.println("	<TR vAlign=center bgColor=#E6F5FF height=22>");
				for(int i=0; i<pn_cnt; i++) {
					pdate = str.repWord(anbdt.getDate(py,pm,pd,i),"/",""); 
					//시작일
					if(pdate.equals(schedule[r][5])) {if(con.equals("0")) con = "1"; else con = "0"; }

					//풍선도움말용
					pjsd = anbdt.getSepDate(schedule[r][1],"/");	//계획시작일
					pjed = anbdt.getSepDate(schedule[r][2],"/");	//계획종료일 	
					if(schedule[r][8].length() == 8) pcsd=anbdt.getSepDate(schedule[r][8],"/");//수정시작일
					if(schedule[r][9].length() == 8) pced=anbdt.getSepDate(schedule[r][9],"/");//수정종료일


					//진행율로 조정
					if(schedule[r][3].equals("0.0")) con = "0";

					//표시하기
					if(con.equals("1")) {
						out.print("<TD noWrap width=5 height=10 align=left bgcolor='blue' ");
						out.print("onmouseover=\"ANB_layerAction(z0"+r+i+", 'visible')\" onmouseout=\"ANB_layerAction(z0"+r+i+", 'hidden')\">");
						out.print("<div id=z0"+r+i+" style=\"position:absolute;background-color:#FEFEED;width:250;	height:50;padding-top:5px ;padding-left:5px ;font:9pt '돋움체';border:#C0C0C0 1px solid;visibility:hidden;\"><font color=#009966>["+schedule[r][0]+"]</font><br>계획기간:"+pjsd+" ~ "+pjed+"<br>수정기간:"+pcsd+" ~ "+pced+"<br>진행율:"+progress+"%</div>");
						out.println("</TD>");
					} 
					else if(pdate.equals(anbdt.getDateNoformat())) {		//금일표시
						out.println("<TD noWrap width=5 height=10 align=right bgcolor='ff9999'></TD>");
					}
					else {
						out.println("<TD noWrap width=5 height=10 align=right bgcolor='white'></TD>");
					}

					//종료일
					if(pdate.equals(schedule[r][6])) { if(con.equals("0")) con = "1"; else con = "0"; }
				}
				out.println("	</TR>");
				out.println("	<TR vAlign=center height=2><TD noWrap width=100% ></TD></TR>");
				out.println("</TABLE>");
			}
			%>
		</TD>
	</TR>
</TABLE>

</body>
</html>