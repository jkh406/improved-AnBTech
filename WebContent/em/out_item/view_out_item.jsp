<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info		= ""		
	contentType = "text/html; charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.em.entity.*"
%>
<%!
	ItemInfoTable item;
	EmLinkUrl link;
%>

<%
	String mode = request.getParameter("mode");
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	
	//ǰ��װ�������
	item = (ItemInfoTable)request.getAttribute("ItemInfo");

	String item_name			= item.getItemName();       
	String model_code			= item.getModelCode();      
	String model_name			= item.getModelName();      
	String maker_name			= item.getMakerName();      
	String standards			= item.getStandards();      
	String unit					= item.getUnit();           
	String writer				= item.getWriter();         
	String written_day			= item.getWrittenDay();

	//����ó����Ʈ
	ArrayList list = new ArrayList();
	list = (ArrayList)request.getAttribute("SupplyerList");
	Iterator iter = list.iterator();
%>

<html>
<link rel="stylesheet" type="text/css" href="../em/css/style.css">
<head>
<title></title>
</head>

<body topmargin="0" leftmargin="0">

<form method=post name="writeForm" action='../servlet/EstimateMgrServlet' enctype='multipart/form-data' style="margin:0">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../em/images/blet.gif"> ǰ�������</TD></TR></TBODY>
		</TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		  <TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
			<TR>
			  <TD align=left width=5>&nbsp;</TD>
			  <TD align=left width=100%>
					<a href="javascript:modify_info('<%=model_code%>');"><img src='../em/images/bt_modify_info.gif' border='0' align='absmiddle' alt="ǰ����������"></a> <a href="javascript:delete_info('<%=model_code%>');"><img src='../em/images/bt_delete_info.gif' border='0' align='absmiddle' alt="ǰ�����"></a> <a href="javascript:add_supplyer('<%=model_code%>');"><img src='../em/images/bt_add_supplyer.gif' border='0' align='absmiddle' alt="����ó�߰�"></a> <a href="javascript:go_list();"><img src='../em/images/bt_list.gif' border='0' align='absmiddle' alt="��Ϻ���"></a>
			  </TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!-- ǰ��������� -->
<TABLE  cellspacing=0 cellpadding=0 width="100%" border=0>
	<TBODY>
		<tr bgcolor="c7c7c7"><td height=1 colspan="4"></td></tr>
		 <tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">ǰ���</td>
           <td width="37%" height="25" class="bg_04"><%=item_name%></td>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">ǰ�����</td>
           <td width="37%" height="25" class="bg_04"><%=unit%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">���ڵ�</td>
           <td width="37%" height="25" class="bg_04"><%=model_code%></td>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">�𵨸�</td>
           <td width="37%" height="25" class="bg_04"><%=model_name%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">�𵨱԰�</td>
           <td width="37%" height="25" class="bg_04"><%=standards%></td>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_04"><%=maker_name%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
		<tr>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">�����</td>
           <td width="37%" height="25" class="bg_04"><%=writer%></td>
           <td width="13%" height="25" class="bg_03" background="../em/images/bg-01.gif">�������</td>
           <td width="37%" height="25" class="bg_04"><%=written_day%></td></tr>
	    <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	   </tbody></table>

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
  <TR><TD align="left">
	<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
	   <TBODY>
		<TR><TD height=10></TD></TR>
		<TR><TD><IMG src='../em/images/supply_history.gif' border='0' alt='ǰ�����ó��Ȳ'></TD></TR>
		</TBODY></TABLE></TD></TR>

  <TR><TD align="left">
	 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=40 align=middle class='list_title'>��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>����ó�ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=200 align=middle class='list_title'>����ó��</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>���޴ܰ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>÷������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>����������</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'></TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=13></TD></TR>
<%
	int i = 1;
	while(iter.hasNext()){
		item = (ItemInfoTable)iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=i%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getSupplyerCode()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getSupplyerName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(item.getSupplyCost(),"")%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getFileName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getWrittenDay()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'></td>
			</TR>
			<TR><TD colSpan=13 background="../em/images/dot_line.gif"></TD></TR>
<%
		i++;
	}
%>
		</TBODY></TABLE></TD></TR></TABLE>

<input type="hidden" name="mode" value="<%=mode%>">
</form>
</body>
</html>

<script language="javascript">

//ǰ����������
function modify_info(model_code) {
	location.href = "../servlet/EstimateMgrServlet?mode=modify_out_item&model_code="+model_code;
}

//������������
function delete_info(model_code) {
	var delete_confirm = confirm("ǰ�������� �����ϸ� ǰ����������� ��� �����˴ϴ�.\n�׷��� ǰ�������� �����Ͻðڽ��ϱ�?");

	if(delete_confirm) location.href = "../servlet/EstimateMgrServlet?mode=delete_out_item&model_code="+model_code;
	else return;
}

//ǰ�����ó�߰�
function add_supplyer(model_code) {
	location.href = "../servlet/EstimateMgrServlet?mode=add_out_item_supply_info&model_code="+model_code;
}

//��Ϻ���
function go_list() 
{ 
	location.href = '../servlet/EstimateMgrServlet?mode=list_out_item';
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

function myRound(num, pos) { 
	var posV = Math.pow(10, (pos ? pos : 2))
	return Math.round(num*posV)/posV
}

/**********************
 * ���ڸ� �Էµǰ�
 **********************/
function currency(obj)
{
	if (event.keyCode >= 48 && event.keyCode <= 57) {
		
	} else {
		event.returnValue = false
	}
}

function com(obj)
{
	obj.value = unComma(obj.value);
	obj.value = Comma(obj.value);
}

/**********************
 * õ���� �޸� ����
 **********************/
function Comma(input) {

  var inputString = new String;
  var outputString = new String;
  var counter = 0;
  var decimalPoint = 0;
  var end = 0;
  var modval = 0;

  inputString = input.toString();
  outputString = '';
  decimalPoint = inputString.indexOf('.', 1);

  if(decimalPoint == -1) {
     end = inputString.length - (inputString.charAt(0)=='0' ? 1:0);
     for (counter=1;counter <=inputString.length; counter++)
     {
        var modval =counter - Math.floor(counter/3)*3;
        outputString = (modval==0 && counter <end ? ',' : '') + inputString.charAt(inputString.length - counter) + outputString;
     }
  }
  else {
     end = decimalPoint - ( inputString.charAt(0)=='-' ? 1 :0);
     for (counter=1; counter <= decimalPoint ; counter++)
     {
        outputString = (counter==0  && counter <end ? ',' : '') +  inputString.charAt(decimalPoint - counter) + outputString;
     }
     for (counter=decimalPoint; counter < decimalPoint+3; counter++)
     {
        outputString += inputString.charAt(counter);
     }
 }
    return (outputString);
}

/**********************
 * ���ڿ��� Comma ����
 **********************/
function unComma(input) {
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

//�ػ󵵸� ���ؼ� div�� ���̸� ����
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 590;
	item_list.style.height = div_h;

}
</script>