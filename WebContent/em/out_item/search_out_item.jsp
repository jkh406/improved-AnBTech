<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info		= ""		
	contentType = "text/html; charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.em.entity.*"
%>
<%!
	ItemInfoTable item;
	EmLinkUrl redirect;

%>
<%
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
	
	String category = request.getParameter("category");

	//품목리스트 가져오기
	ArrayList item_list = new ArrayList();
	item_list = (ArrayList)request.getAttribute("Item_List");
	item = new ItemInfoTable();
	Iterator item_iter = item_list.iterator();

	//링크 문자열 가져오기
	redirect = new EmLinkUrl();
	redirect = (EmLinkUrl)request.getAttribute("OutItemRedirect");
	
	String view_pagecut = redirect.getViewPagecut();
	String link_write = "";
	String input_hidden_search = redirect.getInputHidden();
	String view_total = redirect.getViewTotal();
	String view_boardpage = redirect.getViewBoardpage();
	String view_totalpage = redirect.getViewTotalpage();

%>
<HTML><HEAD><TITLE>품목찾기</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../em/css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY bgColor=#ffffff leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" onLoad="display();">

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../em/images/pop_search_item.gif" width="181" height="17" hspace="10" alt="품목찾기"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>

<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><TD vAlign=top><!-- 타이틀 및 페이지 정보 -->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"></TD>
			  <TD style="padding-right:10px" align='right' valign='middle'><img src="../em/images/setup_total.gif" border="0" align="absmiddle"> <%=view_total%> <img src="../em/images/setup_articles.gif" border="0" align="absmiddle"> <%=view_boardpage%>/<%=view_totalpage%> <img src="../em/images/setup_pages_nowpage.gif" border="0" align="absmiddle"></TD></TR></TBODY></TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><TD vAlign=top><!--버튼 및 페이징-->
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='500'>
				  <form method=get action='../servlet/EstimateMgrServlet' name='srForm' onSubmit="if(this.searchword.value.length< 2){alert('검색어는 2자이상 입력하셔야 합니다.');this.searchword.value='';this.searchword.focus();return false;}else{return true;}" style="margin:0">
					  <select name='searchscope'>
						  <option value='item_name'>품목명</option>
						  <option value='model_name'>모델명</option>
						  <option value='maker_name'>제조회사명</option>
						  <option value='supplyer_name'>공급회사명</option>
					  </select>
					  &nbsp;<input type=text name='searchword' size='10'> <input type="image" onfocus=blur() src="../em/images/bt_search3.gif" border="0" align="absmiddle">
					  <%=input_hidden_search%></form></TD>
			  <TD width='' align='right' style="padding-right:10px"><%=view_pagecut%></TD></TR></TBODY></TABLE></TD></TR>

  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>
  <TR><TD height="330" vAlign=top><!--리스트-->
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=23>
			  <TD noWrap width=100 align=middle class='list_title'>품목명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=100 align=middle class='list_title'>모델코드</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=150 align=middle class='list_title'>모델명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=100% align=middle class='list_title'>공급회사명</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=90 align=middle class='list_title'>공급단가(원)</TD>
			  <TD noWrap width=6 class='list_title'><IMG src="../em/images/list_tep2.gif"></TD>
			  <TD noWrap width=90 align=middle class='list_title'>최종갱신일</TD>
		   </TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=11></TD></TR>
<%
	while(item_iter.hasNext()){
		item = (ItemInfoTable)item_iter.next();
%>
			<TR onmouseover="this.style.backgroundColor='#F5F5F5'" onmouseout="this.style.backgroundColor=''" bgColor=#ffffff>
			  <TD align=middle height="24" class='list_bg'><%=item.getItemName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getModelCode()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getModelName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=middle class='list_bg'><%=item.getSupplyerName()%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=right class='list_bg' style='padding-right:5px'><%=sp.getMoneyFormat(item.getSupplyCost(),"")%></td>
			  <TD><IMG height=1 width=1></TD>
			  <TD align=right class='list_bg' style='padding-right:5px'><%=item.getWrittenDay()%></td>
			</TR>
			<TR><TD colSpan=11 background="../em/images/dot_line.gif"></TD></TR>
<%
	}
%>
		</TBODY></TABLE><BR></DIV></TD></TR>
  <TR height=100%><TD vAlign=top><!--꼬리말-->
	<TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href='javascript:self.close()'><img src='../em/images/bt_close.gif' border='0' align='absmiddle'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></TD></TR></TBODY></TABLE>
		
</BODY></HTML>

<script language="javascript">
function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

function add_out_item() {
	var url = "../servlet/EstimateMgrServlet?mode=add_out_item";
	location.href = url;
}

//해상도를 구해서 div의 높이를 설정
function display() { 
    var w = window.screen.width; 
    var h = window.screen.height; 
	var div_h = h - 465;
}

//부모창에 값 넘겨주기
function returnValue(item_name,model_code,model_name,standards,maker_name,supplyer_code,supplyer_name,supply_cost,unit){
	var f = opener.document.writeForm;
	f.item_name.value		= item_name;
	f.model_code.value		= model_code;
	f.model_name.value		= model_name;
	f.standards.value		= standards;
	f.maker_name.value		= maker_name;
	f.supplyer_code.value	= supplyer_code;
	f.supplyer_name.value	= supplyer_name;
	f.buying_cost.value		= Comma(supply_cost);
	f.unit.value			= unit;
	
	self.close()
}

function com(obj)
{
	obj.value = unComma(obj.value);
	obj.value = Comma(obj.value);
}

/**********************
 * 천단위 콤마 삽입
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
 * 숫자에서 Comma 제거
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
</script>