<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "MFG ���뺸��"		
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

	//�ʱ�ȭ ����
	com.anbtech.date.anbDate anbdt = new anbDate();
	com.anbtech.text.StringProcess str = new StringProcess();
	com.anbtech.util.normalFormat nfm = new com.anbtech.util.normalFormat("#,###");	//����
	
	//----------------------------------------------------
	//	���� �б�
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
	if(order_type.equals("MRP")) order_type_name = "MRP����";
	else if(order_type.equals("MANUAL")) order_type_name = "��޿���";
%>

<HTML>
<HEAD><title>�����ȹ����</title>
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
	<TD width='30%' align="middle" class="title2">�����ȹ����</TD>
	<TD width='30%' align="right" valign="bottom">
	<a href="javascript:self.close();"><img src="../mm/images/bt_close.gif" border=0></a></TD></TR>
<TR><TD height='2' bgcolor='#9CA9BA' colspan="3"></TD></TR>
<TR><TD height='10' colspan="3"></TD></TR></TABLE>

<!-- ���� ���� ���� -->
<TABLE cellSpacing=0 cellPadding=0 width="98%" border=0 align='center'>
	<tr><td>
		<TABLE cellSpacing=0 cellPadding=0 width="98%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA" align='center'>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>�������ù�ȣ</td>
			   <td width="35%" height="25" class="bg_06"><%=mfg_no%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>�����</td>
			   <td width="35%" height="25" class="bg_06">[<%=factory_no%>]<%=factory_name%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>�ۼ���</td>
			   <td width="35%" height="25" class="bg_06"><%=reg_id%>&nbsp;/<%=reg_name%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>�ۼ���</td>
			   <td width="35%" height="25" class="bg_06"><%=reg_date%>&nbsp;</td></tr></table><br>

		<TABLE cellSpacing=0 cellPadding=0 width="98%" border=1 bordercolordark="white" bordercolorlight="#9CA9BA" align='center'>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>F/G�ڵ�</td>
			   <td width="35%" height="25" class="bg_06"><%=fg_code%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>MRP������ȣ</td>
			   <td width="35%" height="25" class="bg_06"><%=mrp_no%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>������ڵ�</td>
			   <td width="35%" height="25" class="bg_06"><%=model_code%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>����𵨸�</td>
			   <td width="35%" height="25" class="bg_06"><%=model_name%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>����ǰ���ڵ�</td>
			   <td width="35%" height="25" class="bg_06"><%=item_code%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>����ǰ�񼳸�</td>
			   <td width="35%" height="25" class="bg_06"><%=item_spec%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>����ǰ���</td>
			   <td width="35%" height="25" class="bg_06"><%=item_name%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>�������</td>
			   <td width="35%" height="25" class="bg_06"><%=item_unit%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>�������</td>
			   <td width="35%" height="25" class="bg_06"><%=mfg_count%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>���ñ���</td>
			   <td width="35%" height="25" class="bg_06"><%=order_type_name%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>MPS��ȹ����</td>
			   <td width="35%" height="25" class="bg_06"><%=anbdt.getSepDate(plan_date,"-")%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>���ޱ���</td>
			   <td width="35%" height="25" class="bg_06">
				<%	String sel = "";
					String[] sl_data = {"�系����ǰ","���ְ���ǰ","����ǰ"};
					String[] sl_value = {"M","O","P"};
					for(int i=0; i<sl_data.length; i++) {
						if(buy_type.equals(sl_value[i])) sel="checked";
						else sel = "";
						out.print("<input type='radio' "+sel+" value=''>"+sl_data[i]);
					}
				%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>����������</td>
			   <td width="35%" height="25" class="bg_06"><%=order_start_date%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>�ϷΌ����</td>
			   <td width="35%" height="25" class="bg_06"><%=order_end_date%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>�����ȹȮ����</td>
			   <td width="35%" height="25" class="bg_06"><%=anbdt.getSepDate(order_date,"-")%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>�۾�����</td>
			   <td width="35%" height="25" class="bg_06">
				<%	String re_sel = "";
					String[] re_data = {"�۾�","���۾�"};
					for(int i=0; i<re_data.length; i++) {
						if(re_work.equals(re_data[i])) re_sel="checked";
						else re_sel = "";
						out.print("<input type='radio' "+re_sel+" value=''>"+re_data[i]);
					}
				%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>����ó�ڵ�</td>
			   <td width="35%" height="25" class="bg_06"><%=comp_code%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>����ó��</td>
			   <td width="35%" height="25" class="bg_06"><%=comp_name%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>����ó �����</td>
			   <td width="35%" height="25" class="bg_06"><%=comp_user%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>����ó ����ó</td>
			   <td width="35%" height="25" class="bg_06"><%=comp_tel%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>�������</td>
			   <td width="87%" height="25" class="bg_06" colspan=3>
				<%
					String[] status_no = {"1","2","3","4","5","6"};
					String[] status_name = {"�����ۼ�","�����ۼ�","����Ȯ��","��ǰ���","�������","��������"};
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
			   <td width="15%" height="25" class="bg_05" align='middle'>��������</td>
			   <td width="35%" height="25" class="bg_06"><%=rst_total_count%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>��ǰ����</td>
			   <td width="35%" height="25" class="bg_06"><%=rst_good_count%>&nbsp;</td>
			</tr>
			<tr>
			   <td width="15%" height="25" class="bg_05" align='middle'>�ܷ�����</td>
			   <td width="35%" height="25" class="bg_06"><%=working_count%>&nbsp;</td>
			   <td width="15%" height="25" class="bg_05" align='middle'>�ҷ�����</td>
			   <td width="35%" height="25" class="bg_06"><%=rst_bad_count%>&nbsp;</td>
			</tr>
		
			</table>
	</td></tr></table>

</body></html>