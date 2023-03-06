<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "해당과제 전체 비용Summary보기"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.pjt.entity.*"
%>
<%
	/*********************************************************************
	 	초기화 선언
	*********************************************************************/
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//날자
	com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();			//문자열 처리
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("0.00");		//출력형대
	
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
	com.anbtech.pjt.entity.projectTable gcost;									
	ArrayList gcost_list = new ArrayList();
	gcost_list = (ArrayList)request.getAttribute("GCOST_List");
	gcost = new projectTable();
	Iterator gcost_iter = gcost_list.iterator();

	//parameter 받기
	String pjt_mbr_id="",mgt_plan="",pjt_status="",cost_exp="",cost_rst="",mbr_exp="";
	String plan_start_date="",plan_end_date="";
	String chg_start_date="",chg_end_date="",rst_start_date="",rst_end_date="";
	String plan_labor="",plan_sample="",plan_metal="",plan_mup="",plan_oversea="",plan_plant="";
	String rst_labor="",rst_sample="",rst_metal="",rst_mup="",rst_oversea="",rst_plant="";
	String plan_labor_ac="0",plan_sample_ac="0",plan_metal_ac="0",plan_mup_ac="0";
	String plan_oversea_ac="0",plan_plant_ac="0";
	String rst_labor_ac="0",rst_sample_ac="0",rst_metal_ac="0",rst_mup_ac="0";
	String rst_oversea_ac="0",rst_plant_ac="0";
	String labor_pro="0",sample_pro="0",metal_pro="0",mup_pro="0",oversea_pro="0",plant_pro="0";
	if(gcost_iter.hasNext()) {
		gcost = (projectTable)gcost_iter.next();

		pjt_code=gcost.getPjtCode();
		pjt_name=gcost.getPjtName();
		pjt_mbr_id=gcost.getPjtMbrId();
		mgt_plan=gcost.getMgtPlan();
		mbr_exp=Integer.toString(gcost.getMbrExp());
		cost_exp=gcost.getCostExp();		//예산 총비용
		cost_rst=gcost.getCostRst();		//실적 총비용

		String psd = gcost.getPlanStartDate();
		String ped = gcost.getPlanEndDate();
		plan_start_date=psd.substring(0,4)+"/"+psd.substring(4,6)+"/"+psd.substring(6,8);
		plan_end_date=ped.substring(0,4)+"/"+ped.substring(4,6)+"/"+ped.substring(6,8);

		String csd = gcost.getChgStartDate();	if(csd == null) csd = "";
		String ced = gcost.getChgEndDate();		if(ced == null) ced = "";
		if(csd.length() > 4) chg_start_date=csd.substring(0,4)+"/"+csd.substring(4,6)+"/"+csd.substring(6,8);
		if(ced.length() > 4) chg_end_date=ced.substring(0,4)+"/"+ced.substring(4,6)+"/"+ced.substring(6,8);
		
		String rsd = gcost.getRstStartDate();	if(rsd == null) rsd = "";
		String red = gcost.getRstEndDate();		if(red == null) red = "";
		if(rsd.length() > 4) rst_start_date=rsd.substring(0,4)+"/"+rsd.substring(4,6)+"/"+rsd.substring(6,8);
		if(red.length() > 4) rst_end_date=red.substring(0,4)+"/"+red.substring(4,6)+"/"+red.substring(6,8);

		pjt_status = gcost.getPjtStatus();		if(pjt_status == null) pjt_status = "S";
		plan_labor=gcost.getPlanLabor();			//if(plan_labor.equals("0")) plan_labor="1";
		plan_sample=gcost.getPlanSample();			//if(plan_sample.equals("0")) plan_sample="1";
		plan_metal=gcost.getPlanMetal();			//if(plan_metal.equals("0")) plan_metal="1";
		plan_mup=gcost.getPlanMup();				//if(plan_mup.equals("0")) plan_mup="1";
		plan_oversea=gcost.getPlanOversea();		//if(plan_oversea.equals("0")) plan_oversea="1";
		plan_plant=gcost.getPlanPlant();			//if(plan_plant.equals("0")) plan_plant="1";

		plan_labor_ac=gcost.getPlanLaborAc();
		plan_sample_ac=gcost.getPlanSampleAc();
		plan_metal_ac=gcost.getPlanMetalAc();
		plan_mup_ac=gcost.getPlanMupAc();
		plan_oversea_ac=gcost.getPlanOverseaAc();
		plan_plant_ac=gcost.getPlanPlantAc();

		rst_labor=gcost.getResultLabor();			//if(rst_labor.equals("0")) rst_labor="1";
		rst_sample=gcost.getResultSample();			//if(rst_sample_ac.equals("0")) rst_sample_ac="1";
		rst_metal=gcost.getResultMetal();			//if(rst_metal.equals("0")) rst_metal="1";
		rst_mup=gcost.getResultMup();				//if(rst_mup.equals("0")) rst_mup="1";
		rst_oversea=gcost.getResultOversea();		//if(rst_oversea.equals("0")) rst_oversea="1";
		rst_plant=gcost.getResultPlant();			//if(rst_plant.equals("0")) rst_plant="1";

		rst_labor_ac=gcost.getResultLaborAc();			
		rst_sample_ac=gcost.getResultSampleAc();		
		rst_metal_ac=gcost.getResultMetalAc();			
		rst_mup_ac=gcost.getResultMupAc();				
		rst_oversea_ac=gcost.getResultOverseaAc();		
		rst_plant_ac=gcost.getResultPlantAc();			

		//실적/계획 비율구하기
		if(!plan_labor.equals("0"))
			labor_pro = nfm.DoubleToString(Double.parseDouble(str.repWord(rst_labor,",",""))/Double.parseDouble(str.repWord(plan_labor,",",""))*100);
		if(!plan_sample.equals("0"))
			sample_pro = nfm.DoubleToString(Double.parseDouble(str.repWord(rst_sample,",",""))/Double.parseDouble(str.repWord(plan_sample,",",""))*100);
		if(!plan_metal.equals("0"))
			metal_pro = nfm.DoubleToString(Double.parseDouble(str.repWord(rst_metal,",",""))/Double.parseDouble(str.repWord(plan_metal,",",""))*100);
		if(!plan_mup.equals("0"))
			mup_pro = nfm.DoubleToString(Double.parseDouble(str.repWord(rst_mup,",",""))/Double.parseDouble(str.repWord(plan_mup,",",""))*100);
		if(!plan_oversea.equals("0"))
			oversea_pro = nfm.DoubleToString(Double.parseDouble(str.repWord(rst_oversea,",",""))/Double.parseDouble(str.repWord(plan_oversea,",",""))*100);
		if(!plan_plant.equals("0"))
			plant_pro = nfm.DoubleToString(Double.parseDouble(str.repWord(rst_plant,",",""))/Double.parseDouble(str.repWord(plan_plant,",",""))*100);	
	}

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//검색하기
function goSearch()
{
	document.sForm.action='../servlet/projectPmCostServlet';
	document.sForm.mode.value='PCO_SMLG';
	document.sForm.submit();
}
//과제선택하기
function goProject()
{
	var sItem = '<%=sItem%>';
	var sWord = '<%=sWord%>';
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	document.eForm.action='../servlet/projectPmCostServlet';
	document.eForm.mode.value='PCO_SMLG';
	document.eForm.sItem.value=sItem;
	document.eForm.sWord.value=sWord;
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.submit();
}
//과제 비용 개별내용보기
function goDetail()
{
	var sItem = 'cost_type';
	var sWord = '1';
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	document.eForm.action='../servlet/projectPmCostServlet';
	document.eForm.mode.value='PCO_ACLV';
	document.eForm.sItem.value=sItem;
	document.eForm.sWord.value=sWord;
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.submit();
}
//비용 보기선택하기
function goView()
{
	var sItem = '<%=sItem%>';
	var sWord = '<%=sWord%>';
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	var v = document.gForm.v.value;

	document.eForm.action='../servlet/projectPmCostServlet';
	if(v == 'costText') document.eForm.mode.value='PCO_SMLT';		//비용 Text보기
	else if(v == 'costGraph') document.eForm.mode.value='PCO_SMLG';	//비용 Graph보기
	
	document.eForm.sItem.value=sItem;
	document.eForm.sWord.value=sWord;
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.submit();
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
<BODY topmargin="0" leftmargin="0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--타이틀-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="3"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD width=70% valign='middle' class="title"><img src="../pjt/images/blet.gif"> 과제진행 개발비용SUMMARY</TD>
					<TD valign='middle'>
						<b><a href='Javascript:goDetail();'>계정별 상세보기</b></a></TD>
					<TD valign='middle'>
						<form name="gForm" method="post" style="margin:0">
						<select name='v' onChange='javascript:goView()' style=font-size:9pt;color='black';>
							<option value='costText'>TEXT</option>
							<option selected value='costGraph'>그래프</option>
						</select>
						</form></TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--버튼-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width=5></TD>
					<TD align=left width=380>
						<form name="sForm" method="post" style="margin:0">
						<select name="pjtWord" style=font-size:9pt;color="black"; onChange='javascript:goSearch();'>  
						<%
							String[] s_code = {"","S","0","1","2","3","4"};
							String[] s_name = {"전체과제","멤버미등록","진행전과제","진행중과제","완료과제","DROP과제","HOLD과제"};
							String tsel = "";
							for(int ti=0; ti<s_code.length; ti++) {
								if(pjtWord.equals(s_code[ti])) tsel = "selected";
								else tsel = "";
								out.println("<option "+tsel+" value='"+s_code[ti]+"'>"+s_name[ti]+"</option>");
							}
						%>
						</select>
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
						<input type="hidden" name="mode" size="15" value="">
						<input type="hidden" name="pid" size="15" value="">
						<a href='Javascript:goSearch();'><img src="../pjt/images/bt_search3.gif" border="0" align='absmiddle'></a>
						</form>
					</TD>
					<TD align=middle width=''>
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
			<tr bgcolor="c7c7c7"><td height=2 colspan="4"></td></tr>
			<tr> <!-- 기본정보 -->
				<td height="25" colspan="4"><img src="../pjt/images/basic_info.gif" width="209" height="25" border="0"></td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">과 제 명</td>
			   <td width="40%" height="25" class="bg_04"><%=pjt_code%> <%=pjt_name%></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">P M 명</td>
			   <td width="40%" height="25" class="bg_04"><%=pjt_mbr_id%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">계획기간</td>
				<td width="40%" height="25" class="bg_04">
					<%=plan_start_date%>&nbsp;&nbsp;~&nbsp;&nbsp;<%=plan_end_date%></td>
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
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">수정기간</td>
				<td width="40%" height="25" class="bg_04">
					<%=chg_start_date%>&nbsp;&nbsp;~&nbsp;&nbsp;<%=chg_end_date%></td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">개발인원</td>
				<td width="40%" height="25" class="bg_04"><%=mbr_exp%> 명</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">실적기간</td>
				<td width="40%" height="25" class="bg_04">
					<%=rst_start_date%>&nbsp;&nbsp;~&nbsp;&nbsp;<%=rst_end_date%></td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">경영계획</td>
				<td width="40%" height="25" class="bg_04">
					<%
						String[] mgt_tag = {"Y","N"};
						String[] mgt_name = {"반영","미반영"};
						if(mgt_plan.equals("Y")) out.println("반영");
						else out.println("미반영");
					%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr> <!-- 예산정보 -->
				<td height="25" colspan="4">&nbsp;<b>과제 예산/실적 정보 그래프</b></td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="100%" height="100%" colspan=4>
					<table cellspacing=0 cellpadding=2 width="100%" border=0>
					<tbody>
						<tr>
							<td width='120' height='25' class="bg_03" background="../pjt/images/bg-01.gif">항&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;목</td>
							<td width='600' height='25' class="bg_04" align=middle>계획/실적 비교</td>
						</tr>
						<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
						<tr>
							<td width='120' height='25' noWrap align=middle>
							<!-- 계정항목 (계획/실적) 구분 -->
							<%
							out.println("<TABLE cellSpacing=0 cellPadding=0 width='100%' border=0 valign='top'>");
							String[] item_p = {"인건비[계획]","SAMPLE[계획]","금형비[계획]","투자경비[계획]","규격승인비[계획]","시설비[계획]"};
							for(int i=0; i<item_p.length; i++) {
								out.println("<TR vAlign=center height=5>");
								out.print("<TD noWrap width=100% height=8 align=right>"+item_p[i]+"</TD>");
								out.println("</TR>");
								out.println("<TR vAlign=center height=5>");
								out.print("<TD noWrap width=100% height=8 align=right>&nbsp;[실적]</TD>");
								out.println("</TR>");
								out.println("<TR><TD height=5></TD></TR>");
							}
							out.println("</TABLE>");
							%>

							</td>
							<td width='600' height='25' noWrap align=right>
							<!-- 막대 그래프 그리기 -->
							<%
							out.println("<TABLE cellSpacing=0 cellPadding=0 width='100%' border=0 valign='top'>");

							//1.인건비 계획
							out.println("<TR vAlign=center height=5>");
							for(int i=0; i<Integer.parseInt(plan_labor_ac); i++) {
								out.print("<TD noWrap width=5 height=8 align=left bgcolor='479933' ");
								out.print("onmouseover=\"ANB_layerAction(z10"+i+", 'visible')\" onmouseout=\"ANB_layerAction(z10"+i+", 'hidden')\">");
								out.print("<div id=z10"+i+" style=\"position:absolute;background-color:#FEFEED;width:150;	height:50;padding-top:5px ;padding-left:5px ;font:9pt '돋움체';border:#C0C0C0 1px solid;visibility:hidden;\"><font color=#009966>[인건비]</font><br>계획비용:"+plan_labor+"<br>실적비용:"+rst_labor+"</div>");
								out.println("</TD>");
							}
							out.print("<TD noWrap width=5 height=8 align=right> &nbsp;</TD>");
							out.println("</TR>");
							//인건비 실적
							out.println("<TR vAlign=center height=5>");
							for(int i=0; i<Integer.parseInt(rst_labor_ac); i++) {
								out.print("<TD noWrap width=5 height=8 align=left bgcolor='blue' ");
								out.print("onmouseover=\"ANB_layerAction(z11"+i+", 'visible')\" onmouseout=\"ANB_layerAction(z11"+i+", 'hidden')\">");
								out.print("<div id=z11"+i+" style=\"position:absolute;background-color:#FEFEED;width:150;	height:50;padding-top:5px ;padding-left:5px ;font:9pt '돋움체';border:#C0C0C0 1px solid;visibility:hidden;\"><font color=#009966>[인건비]</font><br>계획비용:"+plan_labor+"<br>실적비용:"+rst_labor+"</div>");
								out.println("</TD>");
							}
							out.print("<TD noWrap width=5 height=8 align=right>  &nbsp;["+labor_pro+"%]</TD>");
							out.println("</TR>");
							out.println("<TR><TD height=5></TD></TR>");

							//2.SAMPLE 계획
							out.println("<TR vAlign=center height=5>");
							for(int i=0; i<Integer.parseInt(plan_sample_ac); i++) {
								out.print("<TD noWrap width=5 height=8 align=left bgcolor='479933' ");
								out.print("onmouseover=\"ANB_layerAction(z20"+i+", 'visible')\" onmouseout=\"ANB_layerAction(z20"+i+", 'hidden')\">");
								out.print("<div id=z20"+i+" style=\"position:absolute;background-color:#FEFEED;width:150;	height:50;padding-top:5px ;padding-left:5px ;font:9pt '돋움체';border:#C0C0C0 1px solid;visibility:hidden;\"><font color=#009966>[SAMPLE비]</font><br>계획비용:"+plan_sample+"<br>실적비용:"+rst_sample+"</div>");
								out.println("</TD>");
							}
							out.print("<TD noWrap width=5 height=8 align=right> &nbsp;</TD>");
							out.println("</TR>");
							//SAMPLE 실적
							out.println("<TR vAlign=center height=5>");
							for(int i=0; i<Integer.parseInt(rst_sample_ac); i++) {
								out.print("<TD noWrap width=5 height=8 align=left bgcolor='blue' ");
								out.print("onmouseover=\"ANB_layerAction(z21"+i+", 'visible')\" onmouseout=\"ANB_layerAction(z21"+i+", 'hidden')\">");
								out.print("<div id=z21"+i+" style=\"position:absolute;background-color:#FEFEED;width:150;	height:50;padding-top:5px ;padding-left:5px ;font:9pt '돋움체';border:#C0C0C0 1px solid;visibility:hidden;\"><font color=#009966>[SAMPLE비]</font><br>계획비용:"+plan_sample+"<br>실적비용:"+rst_sample+"</div>");
								out.println("</TD>");
							}
							out.print("<TD noWrap width=5 height=8 align=right>  &nbsp;["+sample_pro+"%]</TD>");
							out.println("</TR>");
							out.println("<TR><TD height=5></TD></TR>");

							//3.금형비 계획
							out.println("<TR vAlign=center height=5>");
							for(int i=0; i<Integer.parseInt(plan_metal_ac); i++) {
								out.print("<TD noWrap width=5 height=8 align=left bgcolor='479933' ");
								out.print("onmouseover=\"ANB_layerAction(z30"+i+", 'visible')\" onmouseout=\"ANB_layerAction(z30"+i+", 'hidden')\">");
								out.print("<div id=z30"+i+" style=\"position:absolute;background-color:#FEFEED;width:150;	height:50;padding-top:5px ;padding-left:5px ;font:9pt '돋움체';border:#C0C0C0 1px solid;visibility:hidden;\"><font color=#009966>[금형비]</font><br>계획비용:"+plan_metal+"<br>실적비용:"+rst_metal+"</div>");
								out.println("</TD>");
							}
							out.print("<TD noWrap width=5 height=8 align=right> &nbsp;</TD>");
							out.println("</TR>");
							//금형비 실적
							out.println("<TR vAlign=center height=5>");
							for(int i=0; i<Integer.parseInt(rst_metal_ac); i++) {
								out.print("<TD noWrap width=5 height=8 align=left bgcolor='blue' ");
								out.print("onmouseover=\"ANB_layerAction(z31"+i+", 'visible')\" onmouseout=\"ANB_layerAction(z31"+i+", 'hidden')\">");
								out.print("<div id=z31"+i+" style=\"position:absolute;background-color:#FEFEED;width:150;	height:50;padding-top:5px ;padding-left:5px ;font:9pt '돋움체';border:#C0C0C0 1px solid;visibility:hidden;\"><font color=#009966>[금형비]</font><br>계획비용:"+plan_metal+"<br>실적비용:"+rst_metal+"</div>");
								out.println("</TD>");
							}
							out.print("<TD noWrap width=5 height=8 align=right>  &nbsp;["+metal_pro+"%]</TD>");
							out.println("</TR>");
							out.println("<TR><TD height=5></TD></TR>");

							//4.투자경비 계획
							out.println("<TR vAlign=center height=5>");
							for(int i=0; i<Integer.parseInt(plan_mup_ac); i++) {
								out.print("<TD noWrap width=5 height=8 align=left bgcolor='479933' ");
								out.print("onmouseover=\"ANB_layerAction(z40"+i+", 'visible')\" onmouseout=\"ANB_layerAction(z40"+i+", 'hidden')\">");
								out.print("<div id=z40"+i+" style=\"position:absolute;background-color:#FEFEED;width:150;	height:50;padding-top:5px ;padding-left:5px ;font:9pt '돋움체';border:#C0C0C0 1px solid;visibility:hidden;\"><font color=#009966>[투자경비]</font><br>계획비용:"+plan_mup+"<br>실적비용:"+rst_mup+"</div>");
								out.println("</TD>");
							}
							out.print("<TD noWrap width=5 height=8 align=right> &nbsp;</TD>");
							out.println("</TR>");
							//금형비 실적
							out.println("<TR vAlign=center height=5>");
							for(int i=0; i<Integer.parseInt(rst_mup_ac); i++) {
								out.print("<TD noWrap width=5 height=8 align=left bgcolor='blue' ");
								out.print("onmouseover=\"ANB_layerAction(z41"+i+", 'visible')\" onmouseout=\"ANB_layerAction(z41"+i+", 'hidden')\">");
								out.print("<div id=z41"+i+" style=\"position:absolute;background-color:#FEFEED;width:150;	height:50;padding-top:5px ;padding-left:5px ;font:9pt '돋움체';border:#C0C0C0 1px solid;visibility:hidden;\"><font color=#009966>[투자경비]</font><br>계획비용:"+plan_mup+"<br>실적비용:"+rst_mup+"</div>");
								out.println("</TD>");
							}
							out.print("<TD noWrap width=5 height=8 align=right>  &nbsp;["+mup_pro+"%]</TD>");
							out.println("</TR>");
							out.println("<TR><TD height=5></TD></TR>");

							//5.규격승인비 계획
							out.println("<TR vAlign=center height=5>");
							for(int i=0; i<Integer.parseInt(plan_oversea_ac); i++) {
								out.print("<TD noWrap width=5 height=8 align=left bgcolor='479933' ");
								out.print("onmouseover=\"ANB_layerAction(z50"+i+", 'visible')\" onmouseout=\"ANB_layerAction(z50"+i+", 'hidden')\">");
								out.print("<div id=z50"+i+" style=\"position:absolute;background-color:#FEFEED;width:150;	height:50;padding-top:5px ;padding-left:5px ;font:9pt '돋움체';border:#C0C0C0 1px solid;visibility:hidden;\"><font color=#009966>[규격승인비]</font><br>계획비용:"+plan_oversea+"<br>실적비용:"+rst_oversea+"</div>");
								out.println("</TD>");
							}
							out.print("<TD noWrap width=5 height=8 align=right> &nbsp;</TD>");
							out.println("</TR>");
							//금형비 실적
							out.println("<TR vAlign=center height=5>");
							for(int i=0; i<Integer.parseInt(rst_oversea_ac); i++) {
								out.print("<TD noWrap width=5 height=8 align=left bgcolor='blue' ");
								out.print("onmouseover=\"ANB_layerAction(z51"+i+", 'visible')\" onmouseout=\"ANB_layerAction(z51"+i+", 'hidden')\">");
								out.print("<div id=z51"+i+" style=\"position:absolute;background-color:#FEFEED;width:150;	height:50;padding-top:5px ;padding-left:5px ;font:9pt '돋움체';border:#C0C0C0 1px solid;visibility:hidden;\"><font color=#009966>[규격승인비]</font><br>계획비용:"+plan_oversea+"<br>실적비용:"+rst_oversea+"</div>");
								out.println("</TD>");
							}
							out.print("<TD noWrap width=5 height=8 align=right>  &nbsp;["+oversea_pro+"%]</TD>");
							out.println("</TR>");
							out.println("<TR><TD height=5></TD></TR>");

							//6.시설비 계획
							out.println("<TR vAlign=center height=5>");
							for(int i=0; i<Integer.parseInt(plan_plant_ac); i++) {
								out.print("<TD noWrap width=5 height=8 align=left bgcolor='479933' ");
								out.print("onmouseover=\"ANB_layerAction(z60"+i+", 'visible')\" onmouseout=\"ANB_layerAction(z60"+i+", 'hidden')\">");
								out.print("<div id=z60"+i+" style=\"position:absolute;background-color:#FEFEED;width:150;	height:50;padding-top:5px ;padding-left:5px ;font:9pt '돋움체';border:#C0C0C0 1px solid;visibility:hidden;\"><font color=#009966>[시설비]</font><br>계획비용:"+plan_plant+"<br>실적비용:"+rst_plant+"</div>");
								out.println("</TD>");
							}
							out.print("<TD noWrap width=5 height=8 align=right> &nbsp;</TD>");
							out.println("</TR>");
							//시설비 실적
							out.println("<TR vAlign=center height=5>");
							for(int i=0; i<Integer.parseInt(rst_plant_ac); i++) {
								out.print("<TD noWrap width=5 height=8 align=left bgcolor='blue' ");
								out.print("onmouseover=\"ANB_layerAction(z61"+i+", 'visible')\" onmouseout=\"ANB_layerAction(z61"+i+", 'hidden')\">");
								out.print("<div id=z61"+i+" style=\"position:absolute;background-color:#FEFEED;width:150;	height:50;padding-top:5px ;padding-left:5px ;font:9pt '돋움체';border:#C0C0C0 1px solid;visibility:hidden;\"><font color=#009966>[시설비]</font><br>계획비용:"+plan_plant+"<br>실적비용:"+rst_plant+"</div>");
								out.println("</TD>");
							}
							out.print("<TD noWrap width=5 height=8 align=right>  &nbsp;["+plant_pro+"%]</TD>");
							out.println("</TR>");
							out.println("<TR><TD height=5></TD></TR>");

							out.println("</TABLE>");
							%>
							</td>
						</tr>
					</tbody>
					</table>
				</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>

</body>
</html>
