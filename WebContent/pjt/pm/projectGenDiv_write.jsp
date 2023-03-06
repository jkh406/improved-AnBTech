<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "�μ����� �����⺻���� �űԵ��"		
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
	String pjtWord="S",sItem="pjt_name",sWord="";

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
	 	������ �̵�ϰ��� ã�� [���� : S(������)]
	*********************************************************************/
	ArrayList table_list = new ArrayList();
	table_list = genDAO.getDivStandbyProjectList(user_id);		//�μ� �⺻���� �̵�� ����
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
	 	�μ� ���� process
	*********************************************************************/
	ArrayList prs_list = new ArrayList();
	prs_list = pnameDAO.getPrsnameDivList(login_id,"prs_code","","1",50);
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
%>

<HTML>
<HEAD>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<LINK href="../css/style.css" rel=stylesheet type="text/css">
</HEAD>
<script language=javascript>
<!--
//����ϱ�
function sendSave()
{
	//[�����ڵ�|�����̸�] ã��
	var pjt_info = document.eForm.pjt_info.value;
	var info = pjt_info.split('|');
	if(info[0] == '') {alert('�������� �����Ͻʽÿ�.');  return;}
	if(document.eForm.pjt_mbr_id.value == '')		{alert('����PM�� �����Ͻʽÿ�.');			return;}
	if(document.eForm.mbr_exp.value == '')			{alert('�����ο����� �Է��Ͻʽÿ�.');		return;}
	if(document.eForm.plan_start_date.value == '')	{alert('��ȹ�Ⱓ �������� �Է��Ͻʽÿ�.');  return;}
	if(document.eForm.plan_end_date.value == '')	{alert('��ȹ�Ⱓ �������� �Է��Ͻʽÿ�.');  return;}
	if(document.eForm.pjt_desc.value == '')			{alert('�������並 �Է��Ͻʽÿ�.');			return;}
	if(document.eForm.pjt_spec.value == '')			{alert('�ֿ�SPEC�� �Է��Ͻʽÿ�.');		return;}
	if(document.eForm.pjt_mbr_id.value == '')		{alert('����PM�� �Է��Ͻʽÿ�.');			return;}

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

	//����ϱ�
	document.eForm.action='../../servlet/projectGenServlet';
	document.eForm.mode.value='PBS_WD';	
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
	window.open("../searchPM.jsp?target=eForm.pjt_mbr_id","proxy","width=460,height=480,scrollbar=yes,toolbar=no,status=no,resizable=no");

}
//���� �Է��ϱ�
function OpenCalendar(FieldName) {
	var strUrl = "../Calendar.jsp?FieldName=" + FieldName;
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
					<TD valign='middle' class="title"><img src="../images/blet.gif"> ���� �⺻���� ���</TD>
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
					<TD align=left width=200><a href="javascript:sendSave();"><img src="../images/bt_add.gif" border="0"></a><a href="javascript:history.go(-1)"><img src="../images/bt_cancel.gif" border="0"></a>
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
				<td height="25" colspan="4"><img src="../images/basic_info.gif" width="209" height="25" border="0"></td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
			   <td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">�� �� ��</td>
			   <td width="40%" height="25" class="bg_04">
					<select name='pjt_info'>
					<option value=''>�������� �����Ͻʽÿ�.</option>
					<%
					for(int i=0; i<pjt_cnt; i++) {
						out.print("<OPTION value='"+project[i][0]+"|"+project[i][1]+"'>");
						out.println(project[i][0]+" "+project[i][1]+"</OPTION>");
					}
					%>
					</select></td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">MAIN����</td>
			    <td width="40%" height="25" class="bg_04">
					<select name='parent_code'>
					<option value='M'>MAIN �������� ������ �����Ͻʽÿ�.</option>
					<%
					for(int i=0; i<main_cnt; i++) {
						out.print("<OPTION value='"+main[i][0]+"'>");
						out.println(main[i][0]+" "+main[i][1]+"</OPTION>");
					}
					%>
					</select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">P M ��</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='pjt_mbr_id' value='' readonly>
					&nbsp;<a href="Javascript:searchPM();"><img src="../images/bt_search2.gif" border="0" align="absmiddle"></a></td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">�����ο���</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='mbr_exp' value='' size=5> [�ηµ�Ͻ� �ڵ��ݿ���]</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">��ȹ�Ⱓ</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_start_date' value='<%=anbdt.getDate(0)%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('plan_start_date');"><img src="../images/bt_calendar.gif" border="0" valign='absbottom'></A>&nbsp;&nbsp;~&nbsp;&nbsp;
					<input type='text' name='plan_end_date' value='<%=anbdt.getDate(180)%>' size=10 readonly>
					<A Href="Javascript:OpenCalendar('plan_end_date');"><img src="../images/bt_calendar.gif" border="0" valign='absbottom'></A></td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">�������</td>
				<td width="40%" height="25" class="bg_04">
					<select name='pjt_class'>
					<%
					String[] grade_no = {"1","2","3","4"};
					String[] grade_name = {"1���","2���","3���","4���"};
					for(int i=0; i<grade_no.length; i++) {
						out.print("<OPTION value='"+grade_no[i]+"'>");
						out.println(grade_name[i]+"</OPTION>");
					}
					%>
					</select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">�����Ⱓ</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='chg_start_date' value='' size=10 readonly>&nbsp;&nbsp;~&nbsp;&nbsp;
					<input type='text' name='chg_end_date' value='' size=10 readonly></td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">���߸�ǥ</td>
				<td width="40%" height="25" class="bg_04">
					<select name='pjt_target'>
					<%
					String[] target = {"��ǰȭ����","��������","�������","��ɰ�������"};
					for(int i=0; i<target.length; i++) { 
						out.print("<OPTION value='"+target[i]+"'>");
						out.println(target[i]+"</OPTION>");
					}
					%>
					</select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">�����Ⱓ</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='rst_start_date' value='' size=10 readonly>&nbsp;&nbsp;~&nbsp;&nbsp;
					<input type='text' name='rst_end_date' value='' size=10 readonly></td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">���μ���</td>
				<td width="40%" height="25" class="bg_04">
					<select name='prs_info'>
					<%
					for(int i=0; i<process.length; i++) {
						out.print("<OPTION value='"+process[i][0]+"|"+process[i][2]+"'>");
						out.println(process[i][0]+" "+process[i][1]+"</OPTION>");
					}
					%>
					</select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">��������</td>
				<td width="40%" height="25" class="bg_04">
					<TEXTAREA NAME="pjt_desc" rows=6 cols=42 style="border:1px solid #787878;"></TEXTAREA></td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">�ֿ�SPEC</td>
				<td width="40%" height="25" class="bg_04">
					<TEXTAREA NAME="pjt_spec" rows=6 cols=42 style="border:1px solid #787878;"></TEXTAREA></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr> <!-- �������� -->
				<td height="25" colspan="4">&nbsp;<b>���� ��������</b></td></tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">���ߺ��</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='cost_exp' value='' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.cost_exp_money);">
					<input type=text name=cost_exp_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> ��</td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">�濵��ȹ</td>
				<td width="40%" height="25" class="bg_04">
					<select name='mgt_plan'>
					<%
					String[] mgt_tag = {"Y","N"};
					String[] mgt_name = {"�ݿ�","�̹ݿ�"};
					for(int i=0; i<mgt_tag.length; i++) {
						out.print("<OPTION value='"+mgt_tag[i]+"'>");
						out.println(mgt_name[i]+"</OPTION>");
					}
					%>
					</select></td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">�ΰǺ�</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_labor' value='' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_labor_money);">
					<input type=text name=plan_labor_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> ��</td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">SAMPLE</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_sample' value='' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_sample_money);">
					<input type=text name=plan_sample_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> ��</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">������</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_metal' value='' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_metal_money);">
					<input type=text name=plan_metal_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> ��</td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">���ڰ��</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_mup' value='' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_mup_money);">
					<input type=text name=plan_mup_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> ��</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">�԰ݽ��κ�</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_oversea' value='' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_oversea_money);">
					<input type=text name=plan_oversea_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> ��</td>
				<td width="10%" height="25" class="bg_03" background="../images/bg-01.gif">�ü���</td>
				<td width="40%" height="25" class="bg_04">
					<input type='text' name='plan_plant' value='' size=15 maxlength=16 style="text-align:right;" onKeyPress="isNumObj(this);" onKeyUp="InputMoney(this,document.eForm.plan_plant_money);">
					<input type=text name=plan_plant_money style="border:0px;background-color:#ffffff;text-align:right;color:#FF0033" readonly size=20> ��</td>
			</tr>
			<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
			<tr><td height=10 colspan="4"></td></tr>
			</tbody>
		</table>
		</td>
	</tr>
</table>
<input type='hidden' name='mode' value=''>
<input type='hidden' name='pid' value='<%=bean.getID()%>'>
<input type='hidden' name='pjt_code' value=''>
<input type='hidden' name='pjt_name' value=''>
<input type='hidden' name='owner' value='<%=user_id%>/<%=user_name%>'>
<input type='hidden' name='in_date' value='<%=anbdt.getTime()%>'>
<input type='hidden' name='prs_code' value=''>
<input type='hidden' name='prs_type' value=''>

<input type='hidden' name='pjt_status' value='S'>
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
