<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "������� �����⺻���� �����ϱ�"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.Connection"
	import="com.anbtech.pjt.entity.*"
	import="com.anbtech.util.normalFormat"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	/*********************************************************************
	 	�ʱ�ȭ ����
	*********************************************************************/
	com.anbtech.pjt.entity.pjtCodeTable table;									//�����⺻����helper
	com.anbtech.pjt.entity.projectTable view;									//�⺻�������� helper
	com.anbtech.pjt.entity.prsCodeTable prstable;								//���μ���helper
	com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();			//����
	normalFormat money = new com.anbtech.util.normalFormat("#,000");			//������� (�ݾ�)
	com.anbtech.dbconn.DBConnectionManager connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
	Connection con = connMgr.getConnection("mssql");
	com.anbtech.pjt.db.pjtGeneralDAO genDAO = new com.anbtech.pjt.db.pjtGeneralDAO(con);	//�����⺻����
	com.anbtech.pjt.db.prsNameDAO pnameDAO = new com.anbtech.pjt.db.prsNameDAO(con);		//ǥ�����μ���
	
	//-----------------------------------
	//	�Ķ���� �ޱ�
	//-----------------------------------
	String pjtWord="S",sItem="",sWord="";
	com.anbtech.pjt.entity.projectTable para;
	ArrayList para_list = new ArrayList();
	para_list = (ArrayList)request.getAttribute("PARA_List");
	para = new projectTable();
	Iterator para_iter = para_list.iterator();
	
	if(para_iter.hasNext()) {
		para = (projectTable)para_iter.next();
		pjtWord = para.getPjtword();	
		sItem = para.getSitem();		
		sWord = para.getSword();		
	} 
	
	//-----------------------------------
	// ���������� ���� ��������
	//-----------------------------------
	String user_id = "";			//�ۼ��� ���
	String user_name = "";			//�ۼ��� �̸�
	String user_rank = "";			//�ۼ��� ����
	String div_id = "";				//�ۼ��� �μ��� �����ڵ�
	String div_name = "";			//�ۼ��� �μ���
	String div_code = "";			//�ۼ��� �μ��ڵ�
	String code = "";				//�ۼ��� �μ�Tree �����ڵ�
	String cexp = "";				//���ݾ� �ѱ۷� �б�

	/*********************************************************************
	 	���������� ���� �˾ƺ��� [���/�̸�]
	*********************************************************************/
	String[] Column = {"a.id","a.name","c.ar_code","c.ar_name","b.ac_id","b.ac_name","b.ac_code","b.code"};
	bean.setTable("user_table a,class_table b,rank_table c");		
	bean.setColumns(Column);
	bean.setOrder("a.id ASC");	
	String query = "where (a.id ='"+login_id+"' and a.ac_id = b.ac_id and a.rank = c.ar_code)";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		user_id = login_id;								//����� ���
		user_name = bean.getData("name");				//����� ��
		user_rank = bean.getData("ar_name");			//����� ����
		div_id = bean.getData("ac_id");					//����� �μ��� �����ڵ�
		div_name = bean.getData("ac_name");				//����� �μ��� 
		div_code = bean.getData("ac_code");				//����� �μ��ڵ�
		code = bean.getData("code");					//�ۼ��� �μ�Tree �����ڵ�
	} //while

	/*********************************************************************
	 	������ ������,������� ���� ã�� [���� : S(������),0(�����)]
	*********************************************************************/
	ArrayList table_list = new ArrayList();
	table_list = genDAO.getAllReadyProjectList();		//���� �⺻���� �̵��,����� ����
	table = new pjtCodeTable();
	Iterator table_iter = table_list.iterator();

	int pjt_cnt = table_list.size();
	String[][] project = new String[pjt_cnt][2];
	int n = 0;
	while(table_iter.hasNext()) {
		table = (pjtCodeTable)table_iter.next();
		project[n][0] = table.getPjtCode();
		project[n][1] = table.getPjtName();
		n++;
	}

	/*********************************************************************
	 	������ ���ΰ��� ã�� [���� : 1(��ϰ���), 2(������)]
	*********************************************************************/
	ArrayList main_list = new ArrayList();
	main_list = genDAO.getMainProjectList();		//���� �⺻���� �̵�� ����
	table = new pjtCodeTable();
	Iterator main_iter = main_list.iterator();

	int main_cnt = main_list.size();
	String[][] main = new String[main_cnt][2];
	int m = 0;
	while(main_iter.hasNext()) {
		table = (pjtCodeTable)main_iter.next();
		main[m][0] = table.getPjtCode();
		main[m][1] = table.getPjtName();
		m++;
	}

	/*********************************************************************
	 	���� ���� process
	*********************************************************************/
	ArrayList prs_list = new ArrayList();
	prs_list = pnameDAO.getPrsnameAllList(login_id,"prs_code","","1",50);
	prstable = new prsCodeTable();		
	Iterator prs_iter = prs_list.iterator();

	int prs_cnt = prs_list.size();
	String[][] process = new String[prs_cnt][3];
	int p = 0;
	while(prs_iter.hasNext()) {
		prstable = (prsCodeTable)prs_iter.next();
		process[p][0] = prstable.getPrsCode();
		process[p][1] = prstable.getPrsName();
		process[p][2] = prstable.getType();
		p++;
	}
	/*********************************************************************
	 	Connection �ݱ�
	*********************************************************************/
	connMgr.freeConnection("mssql",con);

	/*********************************************************************
	 	������������ �б�
	*********************************************************************/
	ArrayList view_list = new ArrayList();
	view_list = (ArrayList)request.getAttribute("Data_List");
	view = new projectTable();
	Iterator view_iter = view_list.iterator();

	//parameter �ޱ�
	String pid= "",pjt_code="",pjt_name="",owner="",pjt_mbr_id="",pjt_class="",pjt_target="",mgt_plan="";
	String parent_code="",mbr_exp="",cost_exp="",plan_start_date="",plan_end_date="";
	String chg_start_date="",chg_end_date="",rst_start_date="",rst_end_date="";
	String prs_code="",prs_type="",pjt_desc="",pjt_spec="",pjt_status="";
	String plan_labor="",plan_sample="",plan_metal="",plan_mup="",plan_oversea="",plan_plant="";
	while(view_iter.hasNext()) {
		view = (projectTable)view_iter.next();

		pid= view.getPid();
		pjt_code=view.getPjtCode();
		pjt_name=view.getPjtName();
		owner=view.getOwner();
		pjt_mbr_id=view.getPjtMbrId();
		pjt_class=view.getPjtClass();
		pjt_target=view.getPjtTarget();
		mgt_plan=view.getMgtPlan();
		parent_code=view.getParentCode();
		mbr_exp=Integer.toString(view.getMbrExp());
		cost_exp=view.getCostExp();

		String psd = view.getPlanStartDate();
		String ped = view.getPlanEndDate();
		plan_start_date=psd.substring(0,4)+"/"+psd.substring(4,6)+"/"+psd.substring(6,8);
		plan_end_date=ped.substring(0,4)+"/"+ped.substring(4,6)+"/"+ped.substring(6,8);

		String csd = view.getChgStartDate();	if(csd == null) csd = "";
		String ced = view.getChgEndDate();		if(ced == null) ced = "";
		if(csd.length() > 4) chg_start_date=csd.substring(0,4)+"/"+csd.substring(4,6)+"/"+csd.substring(6,8);
		if(ced.length() > 4) chg_end_date=ced.substring(0,4)+"/"+ced.substring(4,6)+"/"+ced.substring(6,8);
		
		String rsd = view.getRstStartDate();	if(rsd == null) rsd = "";
		String red = view.getRstEndDate();		if(red == null) red = "";
		if(rsd.length() > 4) rst_start_date=rsd.substring(0,4)+"/"+rsd.substring(4,6)+"/"+rsd.substring(6,8);
		if(red.length() > 4) rst_end_date=red.substring(0,4)+"/"+red.substring(4,6)+"/"+red.substring(6,8);

		prs_code=view.getPrsCode();
		prs_type=view.getPrsType();
		pjt_desc=view.getPjtDesc();
		pjt_spec=view.getPjtSpec();
		pjt_status = view.getPjtStatus();	if(pjt_status == null) pjt_status = "S";
		plan_labor=view.getPlanLabor();
		plan_sample=view.getPlanSample();
		plan_metal=view.getPlanMetal();
		plan_mup=view.getPlanMup();
		plan_oversea=view.getPlanOversea();
		plan_plant=view.getPlanPlant();
	}

	//���������϶� �������������ϵ��� �ʱⰪ�� ��ȹ������ ������ Setting���ش�.
	if(pjt_status.equals("1") && (chg_start_date.length() == 0)) {
		chg_start_date = plan_start_date;
		chg_end_date = plan_end_date;
	}

%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../pjt/css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//�����ϱ�
function sendModify()
{
	//[�����ڵ�|�����̸�] ã��
	var pjt_info = document.eForm.pjt_info.value;
	var info = pjt_info.split('|');

	if(document.eForm.mbr_exp.value == '')			{alert('�����ο����� �Է��Ͻʽÿ�.');		return;}
	if(document.eForm.pjt_desc.value == '')			{alert('�������並 �Է��Ͻʽÿ�.');			return;}
	if(document.eForm.pjt_spec.value == '')			{alert('�ֿ�SPEC�� �Է��Ͻʽÿ�.');		return;}

	//�������� �Ǵ��ϱ�
	if(isNaN(document.eForm.mbr_exp.value)) { alert('�����ο��� ���ڷ� �Է��Ͻʽÿ�.');  return; }
	
	//[���μ����ڵ�|���μ����̸�] ã��
	var prs_info = document.eForm.prs_info.value;
	var prs = prs_info.split('|');

	//���ڿ��� '/'����
	var psd = document.eForm.plan_start_date.value;		//��ȹ�Ⱓ ������
	for(i=0;i<2;i++) psd = psd.replace('/','');	
	var ped = document.eForm.plan_end_date.value;		//��ȹ�Ⱓ ������
	for(i=0;i<2;i++) ped = ped.replace('/','');	
	var csd = document.eForm.chg_start_date.value;		//�����Ⱓ ������
	for(i=0;i<2;i++) csd = csd.replace('/','');	
	var ced = document.eForm.chg_end_date.value;		//�����Ⱓ ������
	for(i=0;i<2;i++) ced = ced.replace('/','');	
	var rsd = document.eForm.rst_start_date.value;		//�����Ⱓ ������
	for(i=0;i<2;i++) rsd = rsd.replace('/','');	
	var red = document.eForm.rst_end_date.value;		//�����Ⱓ ������
	for(i=0;i<2;i++) red = red.replace('/','');	

	//�ݾ׿��� ','���ֱ�
	var cexp = unCommaObj(document.eForm.cost_exp.value);		//���ߺ��
	var clab = unCommaObj(document.eForm.plan_labor.value);		//��ȹ�ΰǺ�
	var csam = unCommaObj(document.eForm.plan_sample.value);	//��ȹsample
	var cmet = unCommaObj(document.eForm.plan_metal.value);		//��ȹ����
	var cmup = unCommaObj(document.eForm.plan_mup.value);		//��ȹmuckup
	var cove = unCommaObj(document.eForm.plan_oversea.value);	//��ȹ�԰ݽ���
	var cpla = unCommaObj(document.eForm.plan_plant.value);		//��ȹ�ü�����


	//ó���� �޽��� ���
	document.all['lding'].style.visibility="visible";

	//�����ϱ�
	document.eForm.action='../servlet/projectGenServlet';
	document.eForm.mode.value='PBS_MA';	
	document.eForm.pjt_code.value=info[0];
	document.eForm.pjt_name.value=info[1];
	document.eForm.prs_code.value=prs[0];
	document.eForm.prs_type.value=prs[1];
	document.eForm.plan_start_date.value=psd;
	document.eForm.plan_end_date.value=ped;
	document.eForm.chg_start_date.value=csd;
	document.eForm.chg_end_date.value=ced;
	document.eForm.rst_start_date.value=rsd;
	document.eForm.rst_end_date.value=red;
	document.eForm.cost_exp.value=cexp;
	document.eForm.plan_labor.value=clab;
	document.eForm.plan_sample.value=csam;
	document.eForm.plan_metal.value=cmet;
	document.eForm.plan_mup.value=cmup;
	document.eForm.plan_oversea.value=cove;
	document.eForm.plan_plant.value=cpla;
	document.eForm.submit();

}
//PM ã��
function searchPM()
{
	window.open("../pjt/searchPM.jsp?target=eForm.pjt_mbr_id","proxy","width=460,height=480,scrollbar=yes,toolbar=no,status=no,resizable=no");

}
//���� �Է��ϱ�
function OpenCalendar(FieldName) {
	var strUrl = "../pjt/Calendar.jsp?FieldName=" + FieldName;
	newWIndow = window.open(strUrl, "Calendar", "width=0, height=0");
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
					<TD valign='middle' class="title"><img src="../pjt/images/blet.gif"> ���� �⺻���� ����</TD>
				</TR>
				</TBODY>
			</TABLE>
		</TD>
	</TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR>
		<TD height=32><!--��ư-->
			<TABLE cellSpacing=0 cellPadding=0>
				<TBODY>
				<TR>
					<TD align=left width=5></TD>
					<TD align=left width=200><a href="javascript:sendModify();"><img src="../pjt/images/bt_modify.gif" border="0"></a><a href="javascript:history.go(-1)"><img src="../pjt/images/bt_cancel.gif" border="0"></a>
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
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr> <!-- �⺻���� -->
				<td height="25" colspan="4"><img src="../pjt/images/basic_info.gif" width="209" height="25" border="0"></td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�� �� ��</td>
			   <td width="40%" height="25" class="bg_04">
				<%	//������ �������� ���� �ٲܼ� ����
					out.println("<input type='hidden' name='pjt_info' value='"+pjt_code + "|" + pjt_name+"'>");
					out.println(pjt_code + " " + pjt_name);
				%></td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">MAIN����</td>
			    <td width="40%" height="25" class="bg_04">
				<%
					//�������°� ������[S] �϶�
					if(pjt_status.equals("S")) {
						String msel = ""; 
						out.println("<select name='parent_code'>");
						out.println("<option value='M'>"+pjt_code + " " + pjt_name);
						for(int i=0; i<main_cnt; i++) {
							if(parent_code.equals(main[i][0])) msel="selected"; else msel="";
							out.print("<OPTION "+msel+" value='"+main[i][0]+"'>");
							out.println(main[i][0]+" "+main[i][1]+"</OPTION>");
						}
						out.println("</select>");
					}
					//���������°� �ƴҶ�
					else {
						out.println("<input type='hidden' name='parent_code' value='"+parent_code+"'>");
						out.println(pjt_code + " " + pjt_name);
					}
				%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">P M ��</td>
				<td width="40%" height="25" class="bg_04">
				<%
					if(pjt_status.equals("S") || pjt_status.equals("0")) {
						out.println("<input type='text' name='pjt_mbr_id' value='"+pjt_mbr_id+"' readonly>");
						out.println("&nbsp;<a href=\"Javascript:searchPM();\"><img src='../pjt/images/bt_search2.gif' border='0' align='absmiddle'></a>");
					} else {
						out.println("<input type='hidden' name='pjt_mbr_id' value='"+pjt_mbr_id+"' readonly>");
						out.println(pjt_mbr_id);
					}
				%></td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����ο���</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='mbr_exp' value='<%=mbr_exp%>' size=5> [�ηµ�Ͻ� �ڵ��ݿ���]</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">��ȹ�Ⱓ</td>
				<td width="40%" height="25" class="bg_04">
				<% if(pjt_status.equals("S") || pjt_status.equals("0")) {	//������,��������� %>
					<input type='text' name='plan_start_date' value='<%=plan_start_date%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('plan_start_date');"><img src="../pjt/images/bt_calendar.gif" border="0" valign='absbottom'></A>&nbsp;&nbsp;~&nbsp;&nbsp;
					<input type='text' name='plan_end_date' value='<%=plan_end_date%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('plan_end_date');"><img src="../pjt/images/bt_calendar.gif" border="0" valign='absbottom'></A></td>
				<% } else  { %>
					<input type='hidden' name='plan_start_date' value='<%=plan_start_date%>' size=10 >
					<input type='hidden' name='plan_end_date' value='<%=plan_end_date%>' size=10 >
						<%=plan_start_date%>&nbsp;&nbsp;~&nbsp;&nbsp;<%=plan_end_date%>
					</td>
				<% } %>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�������</td>
				<td width="40%" height="25" class="bg_04">
				<%
					String[] grade_no = {"1","2","3","4"};
					String[] grade_name = {"1���","2���","3���","4���"};
					String gsel = "";
					if(pjt_status.equals("S") || pjt_status.equals("0")) {	//������,���������
						out.println("<select name='pjt_class'>");
						for(int i=0; i<grade_no.length; i++) {
							if(pjt_class.equals(grade_no[i])) gsel="selected"; else gsel="";
							out.print("<OPTION "+gsel+" value='"+grade_no[i]+"'>");
							out.println(grade_name[i]+"</OPTION>");
						}
						out.println("</select>");
					} else {
						out.println("<input type='hidden' name='pjt_class' value='"+pjt_class+"'>");
						out.println(pjt_class+"���");
					}
				%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����Ⱓ</td>
				<td width="40%" height="25" class="bg_04">
				<% if(pjt_status.equals("S") || pjt_status.equals("0")) {	//������,��������� %>
					<input type='text' name='chg_start_date' value='' size=10 readonly>&nbsp;&nbsp;~&nbsp;&nbsp;
					<input type='text' name='chg_end_date' value='' size=10 readonly></td>
				<% } else if(pjt_status.equals("1")) {  //������ %>
					<input type='text' name='chg_start_date' value='<%=chg_start_date%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('chg_start_date');"><img src="../pjt/images/bt_calendar.gif" border="0" valign='absbottom'></A>&nbsp;&nbsp;~&nbsp;&nbsp;
					<input type='text' name='chg_end_date' value='<%=chg_end_date%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('chg_end_date');"><img src="../pjt/images/bt_calendar.gif" border="0" valign='absbottom'></A></td>
				<% } %>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">���߸�ǥ</td>
				<td width="40%" height="25" class="bg_04">
				<%
					String[] target = {"��ǰȭ����","��������","�������","��ɰ�������"};
					String tsel = "";
					if(pjt_status.equals("S") || pjt_status.equals("0")) {	//������,���������
						out.println("<select name='pjt_target'>");
						for(int i=0; i<target.length; i++) { 
							if(pjt_target.equals(target[i])) tsel = "selected";
							else tsel = "";
							out.print("<OPTION "+tsel+" value='"+target[i]+"'>");
							out.println(target[i]+"</OPTION>");
						}
						out.println("</select>");
					} else {
						out.println("<input type='hidden' name='pjt_target' value='"+pjt_target+"'>");
						out.println(pjt_target);
					}
				%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�����Ⱓ</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='rst_start_date' value='<%=rst_start_date%>' size=10 readonly>&nbsp;&nbsp;~&nbsp;&nbsp;
					<input type='text' name='rst_end_date' value='<%=rst_end_date%>' size=10 readonly></td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">���μ���</td>
				<td width="40%" height="25" class="bg_04">
				<%
					String psel = "";
					if(pjt_status.equals("S")) {	//������,���������
						out.println("<select name='prs_info'>");
						for(int i=0; i<process.length; i++) { 
							if(prs_code.equals(process[i][0])) psel = "selected";
							else psel = "";
							out.print("<OPTION "+psel+" value='"+process[i][0]+"|"+process[i][2]+"'>");
							out.println(process[i][0]+" "+process[i][1]+"</OPTION>");
						}
						out.println("</select>");
					} else {
						String prs_name = "";
						for(int i=0; i<process.length; i++) { 
							if(prs_code.equals(process[i][0])) prs_name = process[i][1];
						}
						out.println("<input type='hidden' name='prs_info' value='"+prs_code+"|"+prs_type+"'>");
						out.println(prs_code+" "+prs_name);
					}
				%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">��������</td>
				<td width="40%" height="25" class="bg_04">
				<% 
					if(pjt_status.equals("S") || pjt_status.equals("0")) {	//������,���������
						out.println("<TEXTAREA NAME='pjt_desc' rows=6 cols=42 style='border:1px solid #787878;'>"+pjt_desc+"</TEXTAREA>");
					} else {
						out.println("<TEXTAREA NAME='pjt_desc' rows=6 cols=42 style='border:1px solid #787878;' readonly>"+pjt_desc+"</TEXTAREA>");
					}
				%></td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�ֿ�SPEC</td>
				<td width="40%" height="25" class="bg_04">
				<% 
					if(pjt_status.equals("S") || pjt_status.equals("0")) {	//������,���������
						out.println("<TEXTAREA NAME='pjt_spec' rows=6 cols=42 style='border:1px solid #787878;'>"+pjt_spec+"</TEXTAREA>");
					} else {
						out.println("<TEXTAREA NAME='pjt_spec' rows=6 cols=42 style='border:1px solid #787878;' readonly>"+pjt_spec+"</TEXTAREA>");
					}
				%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr> <!-- �������� -->
				<td height="25" colspan="4">&nbsp;<b>���� ��������</b></td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">���ߺ��</td>
				<td width="40%" height="25" class="bg_04">
				<%
					if(pjt_status.equals("S") || pjt_status.equals("0")) {	//������,���������
						out.println("<input type='text' name='cost_exp' value='"+cost_exp+"' size=15 maxlength=16 style='text-align:right;' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this,document.eForm.cost_exp_money);'>");
						out.println("<input type=text name=cost_exp_money style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' readonly size=20> ��");	
					} else {
						out.println("<input type='text' name='cost_exp' value='"+cost_exp+"' size=15 maxlength=16 style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this,document.eForm.cost_exp_money);' readonly>");
						out.println("<input type=text name=cost_exp_money style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' readonly size=20> ��");
					}
				%></td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�濵��ȹ</td>
				<td width="40%" height="25" class="bg_04">
				<%
					String[] mgt_tag = {"Y","N"};
					String[] mgt_name = {"�ݿ�","�̹ݿ�"};
					if(pjt_status.equals("S") || pjt_status.equals("0")) {	//������,���������
						out.println("<select name='mgt_plan'>");
						for(int i=0; i<mgt_tag.length; i++) {
							out.print("<OPTION value='"+mgt_tag[i]+"'>");
							out.println(mgt_name[i]+"</OPTION>");
						}
						out.println("</select>");
					} else {
						out.println("<input type='hidden' name='mgt_plan' value='"+mgt_plan+"'>");
						if(mgt_plan.equals("Y")) out.println("�ݿ�");
						else out.println("�̹ݿ�");
					}
					
				%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�ΰǺ�</td>
				<td width="40%" height="25" class="bg_04">
				<%
					if(pjt_status.equals("S") || pjt_status.equals("0")) {	//������,���������
						out.println("<input type='text' name='plan_labor' value='"+plan_labor+"' size=15 maxlength=16 style='text-align:right;' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this,document.eForm.plan_labor_money);'>");
						out.println("<input type=text name=plan_labor_money style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' readonly size=20> ��");	
					} else {
						out.println("<input type='text' name='plan_labor' value='"+plan_labor+"' size=15 maxlength=16 style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' readonly onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this,document.eForm.plan_labor_money);'>");
						out.println("<input type=text name=plan_labor_money style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' readonly size=20> ��");
					}
				%></td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">SAMPLE</td>
				<td width="40%" height="25" class="bg_04">
				<%
					if(pjt_status.equals("S") || pjt_status.equals("0")) {	//������,���������
						out.println("<input type='text' name='plan_sample' value='"+plan_sample+"' size=15 maxlength=16 style='text-align:right;' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this,document.eForm.plan_sample_money);'>");
						out.println("<input type=text name=plan_sample_money style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' readonly size=20> ��");	
					} else {
						out.println("<input type='text' name='plan_sample' value='"+plan_sample+"' size=15 maxlength=16 style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this,document.eForm.plan_sample_money);' readonly>");
						out.println("<input type=text name=plan_sample_money style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' readonly size=20> ��");
					}
				%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">������</td>
				<td width="40%" height="25" class="bg_04">
				<%
					if(pjt_status.equals("S") || pjt_status.equals("0")) {	//������,���������
						out.println("<input type='text' name='plan_metal' value='"+plan_metal+"' size=15 maxlength=16 style='text-align:right;' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this,document.eForm.plan_metal_money);'>");
						out.println("<input type=text name=plan_metal_money style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' readonly size=20> ��");	
					} else {
						out.println("<input type='text' name='plan_metal' value='"+plan_metal+"' size=15 maxlength=16 style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this,document.eForm.plan_metal_money);' readonly>");
						out.println("<input type=text name=plan_metal_money style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' readonly size=20> ��");
					}
				%></td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">���ڰ��</td>
				<td width="40%" height="25" class="bg_04">
				<%
					if(pjt_status.equals("S") || pjt_status.equals("0")) {	//������,���������
						out.println("<input type='text' name='plan_mup' value='"+plan_mup+"' size=15 maxlength=16 style='text-align:right;' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this,document.eForm.plan_mup_money);'>");
						out.println("<input type=text name=plan_mup_money style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' readonly size=20> ��");	
					} else {
						out.println("<input type='text' name='plan_mup' value='"+plan_mup+"' size=15 maxlength=16 style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this,document.eForm.plan_mup_money);' readonly>");
						out.println("<input type=text name=plan_mup_money style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' readonly size=20> ��");
					}
				%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�԰ݽ��κ�</td>
				<td width="40%" height="25" class="bg_04">
				<%
					if(pjt_status.equals("S") || pjt_status.equals("0")) {	//������,���������
						out.println("<input type='text' name='plan_oversea' value='"+plan_oversea+"' size=15 maxlength=16 style='text-align:right;' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this,document.eForm.plan_oversea_money);'>");
						out.println("<input type=text name=plan_oversea_money style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' readonly size=20> ��");	
					} else {
						out.println("<input type='text' name='plan_oversea' value='"+plan_oversea+"' size=15 maxlength=16 style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this,document.eForm.plan_oversea_money);' readonly>");
						out.println("<input type=text name=plan_oversea_money style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' readonly size=20> ��");
					}
				%></td>
				<td width="10%" height="25" class="bg_03" background="../pjt/images/bg-01.gif">�ü���</td>
				<td width="40%" height="25" class="bg_04">
				<%
					if(pjt_status.equals("S") || pjt_status.equals("0")) {	//������,���������
						out.println("<input type='text' name='plan_plant' value='"+plan_plant+"' size=15 maxlength=16 style='text-align:right;' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this,document.eForm.plan_plant_money);'>");
						out.println("<input type=text name=plan_plant_money style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' readonly size=20> ��");	
					} else {
						out.println("<input type='text' name='plan_plant' value='"+plan_plant+"' size=15 maxlength=16 style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' onKeyPress='isNumObj(this);' onKeyUp='InputMoney(this,document.eForm.plan_plant_money);' readonly>");
						out.println("<input type=text name=plan_plant_money style='border:0px;background-color:#ffffff;text-align:right;color:#FF0033' readonly size=20> ��");
					}
				%></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value='<%=pid%>'>
<input type='hidden' name='pjt_code' value=''>
<input type='hidden' name='pjt_name' value=''>
<input type='hidden' name='owner' value='<%=user_id%>/<%=user_name%>'>
<input type='hidden' name='in_date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='prs_code' value=''>
<input type='hidden' name='prs_type' value=''>

<input type='hidden' name='pjt_status' value='<%=pjt_status%>'>
<input type="hidden" name="pjtWord" size="15" value="<%=pjtWord%>">
<input type="hidden" name="sItem" size="15" value="<%=sItem%>">
<input type="hidden" name="sWord" size="15" value="<%=sWord%>">
</form>

<div id="lding" style="position:absolute;left:180px;top:300px;width:300px;height:100px;visibility:hidden;">
<table width="400" border="1" cellspacing=1 cellpadding=1 bgcolor="#ABDDE9">
	<tr><td height="50" align="center" valign="middle" class='subB'>
		<marquee behavior="alternate">������ ó�����Դϴ�. ��ø� ��ٷ� �ֽʽÿ�.</marquee>
	</td> 
	</tr>
</table>
</div>

</body>
</html>
