<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "MFG 내용보기"		
	contentType = "text/html; charset=KSC5601" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.mm.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%

	//초기화 선언
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("#,###");	//포멧
	
	//----------------------------------------------------
	//	정보 읽기
	//----------------------------------------------------
	String pid="",mrp_no="",mfg_no="",model_code="",model_name="",fg_code="",item_code="",item_name="";
	String item_spec="",item_unit="",mfg_count="",buy_type="",factory_no="",factory_name="";
	String comp_code="",comp_name="",comp_user="",comp_tel="",order_status="",order_type="";
	String reg_date="",reg_id="",reg_name="",plan_date="",order_start_date="",order_end_date="";
	String re_work="",order_type_name="",order_date="";
	String rst_total_count="",rst_good_count="",rst_bad_count="",working_count="";
	int yy=0,mm=0,dd=0;

	com.anbtech.mm.entity.mfgMasterTable item;
	item = (mfgMasterTable)request.getAttribute("MFG_master");

	pid = item.getPid();
	mrp_no = item.getMrpNo();
	mfg_no = item.getMfgNo();
	model_code = item.getModelCode();
	model_name = item.getModelName();
	fg_code = item.getFgCode();
	item_code = item.getItemCode();
	item_name = item.getItemName();
	item_spec = item.getItemSpec();
	item_unit = item.getItemUnit();
	mfg_count = nfm.toDigits(item.getMfgCount());
	buy_type = item.getBuyType();			
	factory_no = item.getFactoryNo();
	factory_name = item.getFactoryName();
	comp_code = item.getCompCode();
	comp_name = item.getCompName();
	comp_user = item.getCompUser();
	comp_tel = item.getCompTel();
	order_status = item.getOrderStatus();
	order_type = item.getOrderType();
	reg_date = item.getRegDate();			
	reg_id = item.getRegId();				
	reg_name = item.getRegName();			
	plan_date = item.getPlanDate();	
	order_start_date = item.getOrderStartDate();
	order_end_date = item.getOrderEndDate();
	order_date = item.getOrderDate();
	re_work = item.getReWork();
	rst_total_count = Integer.toString(item.getRstTotalCount());
	rst_good_count = Integer.toString(item.getRstGoodCount());
	rst_bad_count = Integer.toString(item.getRstBadCount());
	working_count = Integer.toString(item.getWorkingCount());
	if(order_type.equals("MRP")) order_type_name = "MRP오더";
	else if(order_type.equals("MANUAL")) order_type_name = "긴급오더";
%>

<HTML>
<HEAD><title>생산계획정보</title>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../mm/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--

-->
</script>
<BODY topmargin="0" leftmargin="0">

<TABLE cellSpacing=0 cellPadding=0 width="95%" border=0 align='center'>
<TR>
	<TD width='30%' height="50" align="left" valign="bottom"><img src="../mm/images/logo.jpg" border="0"></TD>
	<TD width='30%' align="middle" class="title2">생산계획정보</TD>
	<TD width='30%' align="right" valign="bottom">
	<a href="javascript:self.close();"><img src="../mm/images/bt_close.gif" border=0></a></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
<TR><TD height='10' colspan="3"></TD></TR></TABLE>

<!-- 문서 내용 시작 -->
<TABLE cellSpacing=0 cellPadding=0 width="98%" border=0 align='center'>
	<tr><td>
		<TABLE cellSpacing=0 cellPadding=0 width="98%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA" align='center'>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>생산지시번호</td>
			   <td width="35%" height="25" class="bg_06"><%=mfg_no%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>공장명</td>
			   <td width="35%" height="25" class="bg_06">[<%=factory_no%>]<%=factory_name%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>작성자</td>
			   <td width="35%" height="25" class="bg_06"><%=reg_id%>&nbsp;/<%=reg_name%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>작성일</td>
			   <td width="35%" height="25" class="bg_06"><%=reg_date%>&nbsp;</td></tr></table><br>

		<TABLE cellSpacing=0 cellPadding=0 width="98%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA" align='center'>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>F/G코드</td>
			   <td width="35%" height="25" class="bg_06"><%=fg_code%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>MRP관리번호</td>
			   <td width="35%" height="25" class="bg_06"><%=mrp_no%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>생산모델코드</td>
			   <td width="35%" height="25" class="bg_06"><%=model_code%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>생산모델명</td>
			   <td width="35%" height="25" class="bg_06"><%=model_name%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>생산품목코드</td>
			   <td width="35%" height="25" class="bg_06"><%=item_code%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>생산품목설명</td>
			   <td width="35%" height="25" class="bg_06"><%=item_spec%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>생산품목명</td>
			   <td width="35%" height="25" class="bg_06"><%=item_name%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>생산단위</td>
			   <td width="35%" height="25" class="bg_06"><%=item_unit%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>생산수량</td>
			   <td width="35%" height="25" class="bg_06"><%=mfg_count%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>지시구분</td>
			   <td width="35%" height="25" class="bg_06"><%=order_type_name%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>MPS계획일자</td>
			   <td width="35%" height="25" class="bg_06"><%=anbdt.getSepDate(plan_date,"-")%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>조달구분</td>
			   <td width="35%" height="25" class="bg_06">
				<%	String sel = "";
					String[] sl_data = {"사내가공품","외주가공품","구매품"};
					String[] sl_value = {"M","O","P"};
					for(int i=0; i<sl_data.length; i++) {
						if(buy_type.equals(sl_value[i])) sel="checked";
						else sel = "";
						out.print("<input type='radio' "+sel+" value=''>"+sl_data[i]);
					}
				%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>착수예정일</td>
			   <td width="35%" height="25" class="bg_06"><%=order_start_date%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>완료예정일</td>
			   <td width="35%" height="25" class="bg_06"><%=order_end_date%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>생산계획확정일</td>
			   <td width="35%" height="25" class="bg_06"><%=anbdt.getSepDate(order_date,"-")%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>작업구분</td>
			   <td width="35%" height="25" class="bg_06">
				<%	String re_sel = "";
					String[] re_data = {"작업","재작업"};
					for(int i=0; i<re_data.length; i++) {
						if(re_work.equals(re_data[i])) re_sel="checked";
						else re_sel = "";
						out.print("<input type='radio' "+re_sel+" value=''>"+re_data[i]);
					}
				%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>외주처코드</td>
			   <td width="35%" height="25" class="bg_06"><%=comp_code%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>외주처명</td>
			   <td width="35%" height="25" class="bg_06"><%=comp_name%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>외주처 담당자</td>
			   <td width="35%" height="25" class="bg_06"><%=comp_user%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>외주처 연락처</td>
			   <td width="35%" height="25" class="bg_06"><%=comp_tel%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>진행상태</td>
			   <td width="87%" height="25" class="bg_06" colspan=3>
				<%
					String[] status_no = {"1","2","3","4","5","6"};
					String[] status_name = {"오더작성","작지작성","오더확정","부품출고","실적등록","실적마감"};
					String status_sel = "";
					for(int i=0; i<status_no.length; i++) {
						if(status_no[i].equals(order_status)) status_sel = "checked";
						else status_sel = "";
						out.println("<input type='radio' "+status_sel+" value=''>"+status_name[i]);
					} 
				%>&nbsp;		</td>
			</tr></table><br>

		<TABLE cellSpacing=0 cellPadding=0 width="98%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA" align='center'>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>실적수량</td>
			   <td width="35%" height="25" class="bg_06"><%=rst_total_count%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>양품수량</td>
			   <td width="35%" height="25" class="bg_06"><%=rst_good_count%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>잔량수량</td>
			   <td width="35%" height="25" class="bg_06"><%=working_count%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>불량수량</td>
			   <td width="35%" height="25" class="bg_06"><%=rst_bad_count%>&nbsp;</td>
			</tr>
		
			</table>
	</td></tr></table>

</body></html>