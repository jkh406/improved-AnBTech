<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "�ش���� ��ü ���Summary����"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.pjt.entity.*"
%>
<%
	/*********************************************************************
	 	�ʱ�ȭ ����
	*********************************************************************/
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//����
	com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();			//���ڿ� ó��
	
	//-----------------------------------
	//	�Ķ���� �ޱ�
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

	//�ش����� ��ü ���� LIST
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
	 	�ش���� ��� ����� �������� �б�
	*********************************************************************/
	com.anbtech.pjt.entity.projectTable scost;									
	ArrayList scost_list = new ArrayList();
	scost_list = (ArrayList)request.getAttribute("SCOST_List");
	scost = new projectTable();
	Iterator scost_iter = scost_list.iterator();

	//parameter �ޱ�
	String pjt_mbr_id="",mgt_plan="",pjt_status="",cost_exp="",cost_rst="",mbr_exp="";
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
		cost_exp=scost.getCostExp();		//���� �Ѻ��
		cost_rst=scost.getCostRst();		//���� �Ѻ��

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

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//�˻��ϱ�
function goSearch()
{
	document.sForm.action='../servlet/projectPmCostServlet';
	document.sForm.mode.value='PCO_SMLT';
	document.sForm.submit();
}
//���������ϱ�
function goProject()
{
	var sItem = '<%=sItem%>';
	var sWord = '<%=sWord%>';
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	document.eForm.action='../servlet/projectPmCostServlet';
	document.eForm.mode.value='PCO_SMLT';
	document.eForm.sItem.value=sItem;
	document.eForm.sWord.value=sWord;
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.pjt_name.value=pjt[1];
	document.eForm.submit();
}
//���� ��� �������뺸��
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
//��� ���⼱���ϱ�
function goView()
{
	var sItem = '<%=sItem%>';
	var sWord = '<%=sWord%>';
	var project = document.eForm.project.value;
	var pjt = project.split('|');

	var v = document.gForm.v.value;

	document.eForm.action='../servlet/projectPmCostServlet';
	if(v == 'costText') document.eForm.mode.value='PCO_SMLT';		//��� Text����
	else if(v == 'costGraph') document.eForm.mode.value='PCO_SMLG';	//��� Graph����
	
	document.eForm.sItem.value=sItem;
	document.eForm.sWord.value=sWord;
	document.eForm.pjt_code.value=pjt[0];
	document.eForm.submit();
}
-->
</script>
<BODY topmargin="0" leftmargin="0">

<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR>
		<TD height=27><!--Ÿ��Ʋ-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="3"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD width=70% valign='middle' class="title"><img src="../pjt/images/blet.gif"> �������� ���ߺ��SUMMARY</TD>
					<TD valign='middle'>
						<b><a href='Javascript:goDetail();'>������ �󼼺���</b></a></TD>
					<TD valign='middle'>
						<form name="gForm" method="post" style="margin:0">
						<select name='v' onChange='javascript:goView()' style=font-size:9pt;color='black';>
							<option selected value='costText'>TEXT</option>
							<option value='costGraph'>�׷���</option>
						</select>
						</form></TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--��ư-->
			<TABLE cellSpacing=0 cellPadding=0 border=0>
				<TBODY>
				<TR>
					<TD align=left width=5></TD>
					<TD align=left width=380>
						<form name="sForm" method="post" style="margin:0">
						<select name="pjtWord" style=font-size:9pt;color="black"; onChange='javascript:goSearch();'>  
						<%
							String[] s_code = {"","S","0","1","2","3","4"};
							String[] s_name = {"��ü����","����̵��","����������","�����߰���","�Ϸ����","DROP����","HOLD����"};
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
							String[] snames = {"�����̸�","�����ڵ�"};
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
						<form name="eForm" method="post" style="margin:0">�� �� ��
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

<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=2 colspan="4"></td></tr>
			<tr> <!-- �⺻���� -->
				<td height="25" colspan="4"><img src="../pjt/images/basic_info.gif" width="209" height="25" border="0"></td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�� �� ��</td>
			   <td width="40%" height="25" class="bg_04"><%=pjt_code%> <%=pjt_name%></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">P M ��</td>
			   <td width="40%" height="25" class="bg_04"><%=pjt_mbr_id%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">��ȹ�Ⱓ</td>
				<td width="40%" height="25" class="bg_04">
					<%=plan_start_date%>&nbsp;&nbsp;~&nbsp;&nbsp;<%=plan_end_date%></td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">��������</td>
				<td width="40%" height="25" class="bg_04">
					<%
					if(pjt_status.equals("S")) out.println("������");
					else if(pjt_status.equals("0")) out.println("������");
					else if(pjt_status.equals("1")) out.println("������");
					else if(pjt_status.equals("2")) out.println("�Ϸ�");
					else if(pjt_status.equals("3")) out.println("DROP");
					else if(pjt_status.equals("4")) out.println("HOLDING");
					%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����Ⱓ</td>
				<td width="40%" height="25" class="bg_04">
					<%=chg_start_date%>&nbsp;&nbsp;~&nbsp;&nbsp;<%=chg_end_date%></td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����ο�</td>
				<td width="40%" height="25" class="bg_04"><%=mbr_exp%> ��</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����Ⱓ</td>
				<td width="40%" height="25" class="bg_04">
					<%=rst_start_date%>&nbsp;&nbsp;~&nbsp;&nbsp;<%=rst_end_date%></td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�濵��ȹ</td>
				<td width="40%" height="25" class="bg_04">
					<%
						String[] mgt_tag = {"Y","N"};
						String[] mgt_name = {"�ݿ�","�̹ݿ�"};
						if(mgt_plan.equals("Y")) out.println("�ݿ�");
						else out.println("�̹ݿ�");
					%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr> <!-- �������� -->
				<td height="25" colspan="4">&nbsp;<b>���� ����/���� ����</b></td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">���� �Ѻ��</td>
				<td width="40%" height="25" class="bg_04">
					<input type=text value='<%=cost_exp%>' style='border:0px;background-color:#ffffff;text-align:right;color:red' readonly size=20> ��</td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">���� �Ѻ��</td>
				<td width="40%" height="25" class="bg_04">
					<input type=text value='<%=cost_rst%>' style='border:0px;background-color:#ffffff;text-align:right;color:darkred' readonly size=20> ��</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">&nbsp;&nbsp;�ΰǺ�</td>
				<td width="40%" height="25" class="bg_04">
					<input type=text value='<%=plan_labor%>' style='border:0px;background-color:#ffffff;text-align:right;color:black' readonly size=20> ��</td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">&nbsp;&nbsp;�ΰǺ�</td>
				<td width="40%" height="25" class="bg_04">
					<input type=text value='<%=rst_labor%>' style='border:0px;background-color:#ffffff;text-align:right;color:blue' readonly size=20> ��</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">&nbsp;&nbsp;SAMPLE</td>
				<td width="40%" height="25" class="bg_04">
					<input type=text value='<%=plan_sample%>' style='border:0px;background-color:#ffffff;text-align:right;color:black' readonly size=20> ��</td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">&nbsp;&nbsp;SAMPLE</td>
				<td width="40%" height="25" class="bg_04">
					<input type=text value='<%=rst_sample%>' style='border:0px;background-color:#ffffff;text-align:right;color:blue' readonly size=20> ��</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">&nbsp;&nbsp;������</td>
				<td width="40%" height="25" class="bg_04">
					<input type=text value='<%=plan_metal%>' style='border:0px;background-color:#ffffff;text-align:right;color:black' readonly size=20> ��</td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">&nbsp;&nbsp;������</td>
				<td width="40%" height="25" class="bg_04">
					<input type=text value='<%=rst_metal%>' style='border:0px;background-color:#ffffff;text-align:right;color:blue' readonly size=20> ��</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">&nbsp;&nbsp;���ڰ��</td>
				<td width="40%" height="25" class="bg_04">
					<input type=text value='<%=plan_mup%>' style='border:0px;background-color:#ffffff;text-align:right;color:black' readonly size=20> ��</td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">&nbsp;&nbsp;���ڰ��</td>
				<td width="40%" height="25" class="bg_04">
					<input type=text value='<%=rst_mup%>' style='border:0px;background-color:#ffffff;text-align:right;color:blue' readonly size=20> ��</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">&nbsp;�԰ݽ��κ�</td>
				<td width="40%" height="25" class="bg_04">
					<input type=text value='<%=plan_oversea%>' style='border:0px;background-color:#ffffff;text-align:right;color:black' readonly size=20> ��</td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">&nbsp;�԰ݽ��κ�</td>
				<td width="40%" height="25" class="bg_04">
					<input type=text value='<%=rst_oversea%>' style='border:0px;background-color:#ffffff;text-align:right;color:blue' readonly size=20> ��</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">&nbsp;&nbsp;�ü���</td>
				<td width="40%" height="25" class="bg_04">
					<input type=text value='<%=plan_plant%>' style='border:0px;background-color:#ffffff;text-align:right;color:black' readonly size=20> ��</td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">&nbsp;&nbsp;�ü���</td>
				<td width="40%" height="25" class="bg_04">
					<input type=text value='<%=rst_plant%>' style='border:0px;background-color:#ffffff;text-align:right;color:blue' readonly size=20> ��</td>
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
