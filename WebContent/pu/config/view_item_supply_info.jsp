<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.pu.entity.*"
%>
<%
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	ItemSupplyInfoTable table = new ItemSupplyInfoTable();
	table = (ItemSupplyInfoTable)request.getAttribute("ITEM_SUPPLY_INFO");

	String mode = request.getParameter("mode");

	String mid					= table.getMid();
	String item_code			= table.getItemCode();
	String supplyer_code		= table.getSupplyerCode();
	String order_weight			= table.getOrderWeight();
	String lead_time			= table.getLeadTime();
	String is_trade_now			= table.getIsTradeNow();
	String is_main_supplyer		= table.getIsMainSupplyer();
	String min_order_quantity	= table.getMinOrderQuantity();
	String max_order_quantity	= table.getMaxOrderQuantity();
	if(min_order_quantity!=null && !min_order_quantity.equals(""))
		min_order_quantity  = sp.getMoneyFormat(min_order_quantity,"");
	if(max_order_quantity!=null && !max_order_quantity.equals(""))
		max_order_quantity  = sp.getMoneyFormat(max_order_quantity,"");
	String order_unit			= table.getOrderUnit();
	String supplyer_item_code	= table.getSupplyerItemCode();
	String supplyer_item_name	= table.getSupplyerItemName();
	String supplyer_item_desc	= table.getSupplyerItemDesc();
	String maker_name			= table.getMakerName();
	String supply_unit_cost		= table.getSupplyUnitCost();
	if(supply_unit_cost!=null && !supply_unit_cost.equals(""))
			supply_unit_cost    = sp.getMoneyFormat(supply_unit_cost,"");
	String request_unit_cost	= table.getRequestUnitCost();
	if(request_unit_cost!=null && !request_unit_cost.equals(""))
			request_unit_cost    = sp.getMoneyFormat(request_unit_cost,"");
	String supplyer_name		= table.getSupplyerName();
	String item_desc			= table.getItemDesc();
	String item_name			= table.getItemName();
%>
<HTML>
<LINK rel="stylesheet" type="text/css" href="../pu/css/style.css">
<HEAD>
<TITLE></TITLE>
</HEAD>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">

<FORM name="eForm" method="post" action="PurchaseConfigMgrServlet" enctype="multipart/form-data">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--타이틀-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../pu/images/blet.gif"> 품목공급정보</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--버튼-->
		<TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5 ></TD>
			  <TD align=left width=500>
					<IMG src='../pu/images/bt_modify.gif' onClick="javascript:modify('<%=mid%>');" style='cursor:hand' align='absmiddle'>
					<IMG src='../pu/images/bt_del.gif' onClick="javascript:del('<%=mid%>');" style='cursor:hand' align='absmiddle'>
					<IMG src='../pu/images/bt_list.gif' onClick='javascript:history.back();' style='cursor:hand' align='absmiddle'>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></td></tr>
		 <TR>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목번호</td>
           <TD width="35%" height="25" class="bg_04"><%=item_code%></td>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목명</td>
           <TD width="35%" height="25" class="bg_04"><%=item_name%></td></tr>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></td></tr>
		 <TR>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목설명</td>
           <TD width="35%" height="25" class="bg_04"><%=item_desc%></td>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목단위</td>
           <TD width="35%" height="25" class="bg_04"><%=order_unit%></td></tr>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></td></tr>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">공급업체코드</td>
           <TD width="35%" height="25" class="bg_04"><%=supplyer_code%></td>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">공급업체명</td>
           <TD width="35%" height="25" class="bg_04"><%=supplyer_name%></td></tr>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></td></tr>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">발주배정가중치</td>
           <TD width="35%" height="25" class="bg_04"><%=order_weight%>%</td>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">구매L/T</td>
           <TD width="35%" height="25" class="bg_04"><%=lead_time%>일</td></tr>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></td></tr>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">사용여부</td>
           <TD width="35%" height="25" class="bg_04">
					<%if(is_trade_now.equals("y")) out.print("예");%> 
					<%if(is_trade_now.equals("n")) out.print("아니오");%></td>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">주공급처여부</td>
           <TD width="35%" height="25" class="bg_04">
					<%if(is_main_supplyer.equals("y")) out.print("예");%>  
					<%if(is_main_supplyer.equals("n")) out.print("아니오");%></td></tr>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></td></tr>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">최소발주량</td>
           <TD width="35%" height="25" class="bg_04"><%=min_order_quantity%></td>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">최대발주량</td>
           <TD width="35%" height="25" class="bg_04"><%=max_order_quantity%></td></tr>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></td></tr>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">발주단가</td>
           <TD width="35%" height="25" class="bg_04"><%=supply_unit_cost%>원</td>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">구매요청단가</td>
           <TD width="35%" height="25" class="bg_04"><%=request_unit_cost%>원</td></tr>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></td></tr></tbody></table><br>

<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></td></tr>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">공급업체품목코드</td>
           <TD width="35%" height="25" class="bg_04"><%=supplyer_item_code%></td>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">공급업체품목명</td>
           <TD width="35%" height="25" class="bg_04"><%=supplyer_item_name%></td></tr>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></td></tr>
		<TR>
           <TD width="15%" height="25" class="bg_03" background="../pu/images/bg-01.gif">품목규격</td>
           <TD width="35%" height="25" class="bg_04" colspan="3"><%=supplyer_item_desc%></td></tr>
	    <TR bgcolor="C7C7C7"><TD height="1" colspan="4"></td></tr>
	   </tbody></table><br>
<INPUT type='hidden' name='mode' value='<%=mode%>'>
<INPUT type='hidden' name='mid' value='<%=mid%>'>
</form>
</body>
</html>


<script language=javascript>
function modify(mid){
	location.href="../servlet/PurchaseConfigMgrServlet?mode=modify_item_supply_info&mid="+mid;
}

function del(mid){
	if(confirm("정말 삭제 하시겠습니까?")) {
	location.href="../servlet/PurchaseConfigMgrServlet?mode=delete_item_supply_info&mid="+mid;
	}
}
</script>