<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info		= ""		
	contentType = "text/html; charset=euc-kr" 		
	errorPage	= "../../admin/errorpage.jsp"
	import		= "com.anbtech.text.Hanguel,com.anbtech.text.StringProcess"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();

	/*****************************************************************************************
	 * item_code : ǰ���ڵ�
	 * sf	: ���̸�
	 * sid	: ȸ�� ���̵�(������ȣ)
	 * sname : ȸ���
	 * scost : ���޴ܰ�
	 * �� �������� ȣ���� ������ search_item_supply_info.jsp?item_code=ǰ���ȣ&sf=���̸�&sid=���̵�&sname=ȸ���&scost=�ܰ��ʵ�� ������ ȣ���Ѵ�.
	 *****************************************************************************************/
	String item_code	= request.getParameter("item_code")==null?"":request.getParameter("item_code");
	String sf			= request.getParameter("sf");
	String sid			= request.getParameter("sid")==null?"na":request.getParameter("sid");
	String sname		= request.getParameter("sname")==null?"na":request.getParameter("sname");
	String scost		= request.getParameter("scost")==null?"na":request.getParameter("scost");
	
	String sql			= "";
	sql = "SELECT a.*,b.name_kor,b.name_eng FROM pu_item_supply_info a,company_customer b WHERE a.item_code = '" + item_code + "' AND a.supplyer_code = b.company_no AND a.is_trade_now = 'y'";

	bean.openConnection();	
	bean.executeQuery(sql);
%>

<HTML><HEAD><TITLE>ǰ����޾�ü�˻�</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onLoad="display()">
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD>
	<!--Ÿ��Ʋ-->
		<TABLE cellSpacing=0 cellPadding=0 width="100%" height='27' border=0>
			<TBODY>
				<TR><TD height="3" bgcolor="0C2C55"></TD></TR>
				<TR><TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_source_company.gif"  alt="ǰ����޾�ü�˻�"></TD></TR>
				<TR><TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='30' style='padding-left:10px'>*�ش� ���޾�ü���� Ŭ���Ͻʽÿ�.</TD></TR>
	<TR><TD height="190" valign="top">
		<DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:115; overflow-x:auto; overflow-y:scroll;">
		<TABLE cellSpacing=0 cellPadding=0 width="90%" border=0 align='middle'>
			<TBODY>
				<TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR>
				<TR vAlign=middle height=23>
				  <TD noWrap width=100 align=middle class='list_title'>ǰ���ȣ</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
				  <TD noWrap width=100 align=middle class='list_title'>���޾�ü��</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
				  <TD noWrap width=100 align=middle class='list_title'>���޾�ü�ڵ�</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
				  <TD noWrap width=100 align=middle class='list_title'>���޴ܰ�</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
				  <TD noWrap width=80 align=middle class='list_title'>���ֹ���<br>����ġ</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
				  <TD noWrap width=50 align=middle class='list_title'>Lead Time</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
				  <TD noWrap width=50 align=middle class='list_title'>�ְ���ó����</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
				  <TD noWrap width=80 align=middle class='list_title'>�ּҹ��ַ�</TD>
				  <TD noWrap width=6 class='list_title'><IMG src="../images/list_tep2.gif"></TD>
				  <TD noWrap width=80 align=middle class='list_title'>�ִ���ַ�</TD>
				<TR bgColor=#9DA9B9 height=1><TD colspan=17></TD></TR>
<%
	int count = 0;
	while(bean.next()){
		String supplyer_name = "<a href=\"javascript:returnValue('"+bean.getData("supplyer_code")+"','"+bean.getData("name_kor")+"/"+bean.getData("name_eng")+"','"+bean.getData("request_unit_cost")+"');\">"+bean.getData("name_kor")+"</a>";
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=bean.getData("item_code")%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=supplyer_name%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=bean.getData("supplyer_code")%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=sp.getMoneyFormat(bean.getData("request_unit_cost"),"")%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=bean.getData("order_weight")%> %</TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=bean.getData("lead_time")%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=bean.getData("is_main_supplyer")%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=bean.getData("min_order_quantity")%></TD>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=bean.getData("max_order_quantity")%></TD>
			</TR>
			<TR><TD colSpan=17 background="../images/dot_line.gif"></TD></TR>
<%
		count++;
	}

	if(count == 0){
%>
			<TR><TD height=30 colSpan=17 align=middle class='list_bg'>�����Ͻ� ǰ�� ���� ���������� �����ϴ�.</TD></TR>		
<%
	}
%>
		</TBODY></TABLE></DIV></TD></TR>
	<TR><TD><!--������-->
		<TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
			<TBODY>
				<TR><TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px">
					<a href='javascript:self.close()'><img src='../images/bt_close.gif' border='0' align='absmiddle'></a></TD></TR>
				<TR><TD width="100%" height=1 bgcolor="0C2C55"></TD></TR></TBODY></TABLE></TD></TR></TABLE>
</BODY></HTML>

<script language="javascript">

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

function returnValue(rid,rname,rcost)
{
	if(opener.document.<%=sf%>.<%=sid%>) opener.document.<%=sf%>.<%=sid%>.value = rid;
	if(opener.document.<%=sf%>.<%=sname%>) opener.document.<%=sf%>.<%=sname%>.value = rname;
	if(opener.document.<%=sf%>.<%=scost%>) opener.document.<%=sf%>.<%=scost%>.value = rcost;
	if(opener.document.<%=sf%>.<%=scost%>) opener.document.<%=sf%>.<%=scost%>.focus();

	//opener.reflect_supplyer();
	self.close();
}

function changeAll()
{
	var f = document.mForm;
	if(f.sItem.options[0].selected){
		f.sItem.value = "name";
		f.sWord.value = "";
		f.submit();
	}

}
function checkForm()
{
	var f = document.sForm;

	if(f.sWord.value.length < 2){
			alert("�˻�� 2�ܾ� �̻� �Է��ϼ���.");
			f.sWord.focus();
			return false;
	}
}

//�ػ󵵸� ���ؼ� div�� ���̸� ����
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	
	var c_h = this.document.body.scrollHeight; // ���� �������� ũ��
	//var div_h = h - 580;
	var div_h = c_h - 103;
	item_list.style.height = div_h;

}
</script>