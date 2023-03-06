<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "���� ���ߺ�� �ۼ��ϱ�"		
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
	 	�ʱ�ȭ ����
	*********************************************************************/
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();					//����
	com.anbtech.text.StringProcess str = new com.anbtech.text.StringProcess();			//���ڿ� ó��
	com.anbtech.util.normalFormat fmt = new com.anbtech.util.normalFormat("0,000");		//�������
	String pre_cost = "",rst_cost="";	//������,�������
	String cost_name = "";				//����̸�
	
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
		cost_exp=scost.getCostExp();		//���� �Ѻ��
		cost_rst=scost.getCostRst();		//���� �Ѻ��
		dif_cost = scost.getDifCost();		//���� - ����

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
	 	ȭ����¿� ��� �ݾ�
	*********************************************************************/
	if(sWord.equals("")) {				//��ü
		pre_cost = cost_exp;			//����
		rst_cost = cost_rst;			//����
	} else if(sWord.equals("1")) {		//�ΰǺ�
		pre_cost = plan_labor;
		rst_cost = rst_labor;
	} else if(sWord.equals("2")) {		//SAMPLE
		pre_cost = plan_sample;
		rst_cost = rst_sample;
	} else if(sWord.equals("3")) {		//������
		pre_cost = plan_metal;
		rst_cost = rst_metal;
	} else if(sWord.equals("4")) {		//���ں�
		pre_cost = plan_mup;
		rst_cost = rst_mup;
	} else if(sWord.equals("5")) {		//�԰ݽ��κ�
		pre_cost = plan_oversea;
		rst_cost = rst_oversea;
	} else if(sWord.equals("6")) {		//���ں�
		pre_cost = plan_plant;
		rst_cost = rst_plant;
	}

	//-----------------------------------
	//	���� ��ü��� ã��
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
		
		//step������ ����׸����� 
		if(node.getLevelNo().equals("2")) {
			process[a][0] = node.getChildNode();		//����ڵ�
			process[a][1] = node.getNodeName();			//����
			process[a][2] = node.getUserId();			//����ڻ��
			process[a][3] = node.getUserName();			//������̸�
			a++;
		} else node_cnt--;
	}

	//-----------------------------------
	//	���� ��� ã��
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

	//���� ���� ���ϱ� : format : yyyy-MM-dd
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
//���� ��� SUMMARY����
function costWrite()
{
	//[����ڵ�|����̸�] ã��
	var node = document.eForm.node.value;
	var nd = node.split('|');

	//[����ڻ��|������̸�] ã��
	var users = document.eForm.users.value;
	var us = users.split('|');

	//�ݾ׿��� ','���ֱ�
	var cexp = unCommaObj(document.eForm.node_cost.value);		//���ߺ��

	//sWord�ٲ��ֱ�
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
//ȯ�� �����ϱ�
function exchangeM() 
{
	//�ݾ׿��� ','���ֱ�
	var cexp = unCommaObj(document.eForm.node_cost.value);		//���ߺ��

	//ȯ���ݾ�
	var exc = document.eForm.exchange.value;

	//ȯ������ �Ҽ��� �ڸ��� �˻�
	var point_cnt = 0;		//�Ҽ��� ã��
	for(i=0; i<exc.length; i++) {
		if(exc.charAt(i) == '.') point_cnt++;
	}
	if(point_cnt > 1) { alert('ȯ���Է¿� ������ �ֽ��ϴ�. Ȯ���� �ٽ� �Ͻʽÿ�.'); return; }


	//ȯ������ ��ȭ
	var mey = 0;
	if(exc != 0) mey = cexp * exc;

	//�ݿ��ϱ�
	document.eForm.node_cost.value = Comma(mey);
	
}

//�ݾ� õ������ �޸��ֱ�
function InputMoney(input,obj){
	str = input.value;
	str = unComma(str);
	MoneyToHan(str,obj)
	str = Comma(str);
	input.value = str;
}
function MoneyToHan(str,obj){
	arrayNum=new Array("0","1","2","3","4","5","6","7","8","9");
	arrayUnit=new Array("","","","","�� ","","","","�� ","","","","�� ","","","","�� ");
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

//obj�ι޾� �޸� ���ֱ�
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
		<TD height=27><!--Ÿ��Ʋ-->
			<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
				<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif"> �������� ���ߺ�� ������ �󼼺���</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--��ư-->
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

<!-- ����� ������� �� -->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr> <!-- ���� ������ -->
			   <td width="10%" height="25" colspan=4><b>�������� ������</b></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�� �� ��</td>
			   <td width="40%" height="25" class="bg_04"><%=pjt_code%> <%=pjt_name%></td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">P M ��</td>
				<td width="40%" height="25" class="bg_04"><%=pjt_mbr_id%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
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
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">��ȹ�Ⱓ</td>
			   <td width="40%" height="25" class="bg_04">
					<%=plan_start_date%>&nbsp;&nbsp;~&nbsp;&nbsp;<%=plan_end_date%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">����/����</td>
			   <td width="40%" height="25" class="bg_04">
					<input type=text value='<%=rst_cost%>' style='border:0px;background-color:#ffffff;text-align:right;color:blue' readonly size=15> /
					<input type=text value='<%=pre_cost%>' style='border:0px;background-color:#ffffff;text-align:right;color:blue' readonly size=15> [��]</td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����Ⱓ</td>
			   <td width="40%" height="25" class="bg_04">
					<%=chg_start_date%>&nbsp;&nbsp;~&nbsp;&nbsp;<%=chg_end_date%></td>
			</tr>
			<tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�� ��</td>
			   <td width="40%" height="25" class="bg_04">
					<input type=text value='<%=dif_cost%>' style='border:0px;background-color:#ffffff;text-align:right;color:blue' readonly size=33> [��]</td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����Ⱓ</td>
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

<!-- ����Ʈ -->
<TABLE  cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
	<TBODY>
	<TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
		<TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
				<TD valign='middle' class="title"><img src="../pjt/images/blet.gif" align="absmiddle"> �ش�������� �Է��ϱ�</TD>
			</TR>
			</TBODY>
		</TABLE>
		</TD>
	</TR>	
	<TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
</TABLE>
<!--����-->
<table width="100%" border="0" cellspacing="0" cellpadding="0">
	<tr>
		<td align="center">
		<table cellspacing=0 cellpadding=2 width="100%" border=0>
			<tbody>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����</td>
			   <td width="40%" height="25" class="bg_04">
					<select name="users" style=font-size:9pt;color="black";> 
					<%
						for(int i=0; i<man_cnt; i++) {
							out.print("<option value='"+member[i][0]+"|"+member[i][1]+"'>"+member[i][1]+" "+member[i][2]);
							out.println("</option>");
						} 
					%>
					</select></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">����</td>
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
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">����׸�</td>
			   <td width="40%" height="25" class="bg_04">
					<select name="cost_type" style=font-size:9pt;color="black";> 
					<%
						String[] s_code = {"1","2","3","4","5","6"};
						String[] s_name = {"�ΰǺ�","SAMPLE","������","���ڰ��","�԰ݽ��κ�","�ü����ں�"};
						String tsel = "";
						for(int ti=0; ti<s_code.length; ti++) {
							if(sWord.equals(s_code[ti])) {tsel = "selected"; cost_name=s_name[ti]; } 
							else tsel = "";
							out.println("<option "+tsel+" value='"+s_code[ti]+"'>"+s_name[ti]+"</option>");
						}
					%>
					</select></td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�ۼ���</td>
			   <td width="40%" height="25" class="bg_04"><%=toDate%>
						<input type='hidden' name='in_date' value='<%=toDate%>'></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">����ݾ�</td>
			   <td width="40%" height="25" class="bg_04">
					<input type='text' name='node_cost' value='' size=15 maxlength=16 style="text-align:right;" OnBlur="isNumObj(this);"> [��]</td>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">ȯ ��</td>
			   <td width="40%" height="25" class="bg_04">
					<input type='text' name='exchange' value='' size=15 maxlength=16 style="text-align:right;" OnBlur="javascript:exchangeM();"> [ȯ���Է½� ����ݾ׿� �����]</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�� ��</td>
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
