<%@ include file="../../admin/configHead.jsp"%>
<%@ page 
	info		= ""
	language	= "java"
	contentType = "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import = "java.sql.*, java.io.*, java.util.*,com.anbtech.st.entity.*"
%>
<%!
	ReservedItemInfoTable table;
%>
<%
	String mode		= request.getParameter("mode");

	//����Ʈ ��������
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ITEM_LIST");
	Iterator table_iter = table_list.iterator();
%>

<html>
<link rel="stylesheet" type="text/css" href="../st/css/style.css">
<head>
<title></title>
</head>

<body topmargin="0" leftmargin="0" onLoad="display()">

<form name="srForm" method="post" action="StockMgrServlet" enctype="multipart/form-data">
<!-- �Ƿ�ǰ�񸮽�Ʈ -->
 <DIV id="item_list" style="position:absolute; visibility:visible; width:100%; height:340; overflow-x:auto; overflow-y:auto;">
 <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
			<TR vAlign=middle height=23>
			  <TD noWrap width=30 align=middle class='list_title'>��ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>ǰ���ڵ�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>ǰ�񼳸�</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>��û<br>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>�����<br>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>���<br>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=50 align=middle class='list_title'>����</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>�������ù�ȣ</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../st/images/list_tep2.gif"></TD>
			  <TD noWrap width=80 align=middle class='list_title'>�����</TD>
			</TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=19></TD></TR>
<%
	int no = 1;
	while(table_iter.hasNext()){
		table = (ReservedItemInfoTable)table_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
				  <TD align=middle height="24" class='list_bg'><%=no%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getItemCode()%></TD>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=left class='list_bg' style='padding-left:5px'><%=table.getItemDesc()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getRequestQuantity()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getDeliveriedQuantity()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'>
<%	if(mode.equals("list_app_reserved_item")){	%>				  
				  <input type='text' size='5' name='delivery_quantity_<%=no%>' value='<%=table.getDeliveryQuantity()%>' onKeyPress="currency(this);" onChange="change_quantity('<%=no%>','<%=table.getRequestQuantity()%>','<%=table.getDeliveriedQuantity()%>','<%=table.getDeliveryQuantity()%>','<%=table.getDeliveryNo()%>','<%=table.getItemCode()%>','<%=table.getRefNo()%>','<%=table.getAid()%>');" style="text-align:center">
<%	}else{	%>
				<%=table.getDeliveryQuantity()%>
<%	}	%>
				  </td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getDeliveryUnit()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getRefNo()%></td>
				  <TD><IMG height=1 width=1></TD>
				  <TD align=middle class='list_bg'><%=table.getFactoryName()%></td>
			</TR>
			<TR><TD colSpan=19 background="../st/images/dot_line.gif"></TD></TR>
<%	
		no++;
	}
%>
		</TBODY></TABLE></DIV>
</form>
</body>
</html>

<script language=javascript>
//�˾�â ����
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

// ���ڸ� �Էµǰ�
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

// õ���� �޸� ����
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

// ���ڿ��� Comma ����
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

var checkflag = false; 

function check(field) { 
	if (checkflag == false) { 
		for (i = 0; i < field.length; i++) { 
		field[i].checked = true; 
		} 
	checkflag = true; 
	}else { 
		for (i = 0; i < field.length; i++) { 
		field[i].checked = false; 
		} 
	checkflag = false; 
	} 
}

//�ػ󵵸� ���ؼ� div�� ���̸� ����
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 380;
	item_list.style.height = div_h;

}

//������ ����
function change_quantity(no,request_quantity,deliveried_quantity,now_delivery_quantity,delivery_no,item_code,ref_no,aid){
	var f = document.srForm;

	var to_delivery_quantity = eval("document.srForm.delivery_quantity_" + no + ".value");
	var rest_quantity = request_quantity - deliveried_quantity;
	if(rest_quantity < to_delivery_quantity){
		alert("�������� ����û������ �ʰ��� �� �����ϴ�.");
		eval("document.srForm.delivery_quantity_" + no + ".value='" + now_delivery_quantity + "'");
		return;
	}


	var c = confirm("������(=" + to_delivery_quantity + ")�� �����Ͻðڽ��ϱ�?");
	if(c){
		var url = "../servlet/StockMgrServlet?mode=modify_delivery_quantity&delivery_no=" + delivery_no + "&ref_no=" + ref_no + "&item_code=" + item_code + "&aid=" + aid + "&delivery_quantity=" + to_delivery_quantity;
		location.href = url;
	}else{
		eval("document.srForm.delivery_quantity_" + no + ".value='" + now_delivery_quantity + "'");
	}
}
</script>