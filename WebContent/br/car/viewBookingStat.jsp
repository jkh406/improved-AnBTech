<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,java.text.*"
%>
<%
	//�Ķ���� �ޱ�
	int total_day = Integer.parseInt(request.getParameter("td"));

	String year = request.getParameter("y");
	String month = request.getParameter("m");

	//��¥�� ���� ǥ��
	DateFormat df = new SimpleDateFormat("yyyyMMdd"); 
	DateFormat ddf = new SimpleDateFormat("EEE"); 
	String array_day[] = new String[total_day];
	String inputDate = "";
	String current_day = "";
	for(int i=1; i<=total_day; i++){
		inputDate = year + month + i;
		if(ddf.format(df.parse(inputDate)).equals("��")) current_day = "<font color='blue'>" + i + "</font>";
		else if(ddf.format(df.parse(inputDate)).equals("��")) current_day = "<font color='red'>" + i + "</font>";
		else current_day = current_day = "<font color='#000000'>" + i + "</font>";
		array_day[i-1] = current_day;
	}//������� ��¥�� ���� ǥ��

	//����Ʈ ��������
	ArrayList table_list = new ArrayList();
	table_list = (ArrayList)request.getAttribute("ResourceList");
	String day[][] = new String[total_day+1][2];
	Iterator table_iter = table_list.iterator();
%>

<HTML><HEAD><TITLE></TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<LINK href="../br/css/style.css" rel=stylesheet>
</HEAD>

<BODY bgColor='#ffffff' leftMargin='0' topMargin='0' marginheight='0' marginwidth='0' oncontextmenu="return false">

<TABLE height="100%" cellSpacing=0 cellPadding=0 width="100%" border=0 valign="top">
  <TBODY>
  <TR height=27><!-- Ÿ��Ʋ �� ������ ���� -->
    <TD vAlign=top>
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
		  <TBODY>
			<TR bgcolor="#4A91E1"><TD height="2" colspan="2"></TD></TR>
			<TR bgcolor="#BAC2CD">
			  <TD valign='middle' class="title"><img src="../br/images/blet.gif" align="absmiddle"> ���� ���� ���� ��Ȳ</TD>
			  <TD style="padding-right:10px" align='right' valign='middle'></TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
  <TR height=32><!--��ư �� ����¡-->
    <TD vAlign=top>
		<TABLE height=32 cellSpacing=0 cellPadding=0 width="100%" border='0'>
			<TBODY>
			<TR>
			  <TD width=4>&nbsp;</TD>
			  <TD align=left width='400'>
					<form method="get" name="sForm" action="../servlet/BookResourceServlet" style="margin:0">
					<SELECT name='year' onChange='javascript:go()'>
						<OPTION value="2000">2000��</OPTION>
						<OPTION value="2001">2001��</OPTION>
						<OPTION value="2002">2002��</OPTION>
						<OPTION value="2003">2003��</OPTION>
						<OPTION value="2004">2004��</OPTION>
						<OPTION value="2005">2005��</OPTION>
						<OPTION value="2006">2006��</OPTION>
						<OPTION value="2007">2007��</OPTION>
						<OPTION value="2008">2008��</OPTION>
						<OPTION value="2009">2009��</OPTION>
						<OPTION value="2010">2010��</OPTION>
						<OPTION value="2011">2011��</OPTION>
					</SELECT>
				<%	if(!year.equals("")){	%>
						<script language='javascript'>
							document.sForm.year.value = '<%=year%>';
						</script>
				<%	}	%>
					<SELECT name='month' onChange='javascript:go()'>
						<OPTION value="01">1��</OPTION>
						<OPTION value="02">2��</OPTION>
						<OPTION value="03">3��</OPTION>
						<OPTION value="04">4��</OPTION>
						<OPTION value="05">5��</OPTION>
						<OPTION value="06">6��</OPTION>
						<OPTION value="07">7��</OPTION>
						<OPTION value="08">8��</OPTION>
						<OPTION value="09">9��</OPTION>
						<OPTION value="10">10��</OPTION>
						<OPTION value="11">11��</OPTION>
						<OPTION value="12">12��</OPTION>
					</SELECT>		  
				<%	if(!month.equals("")){	%>
						<script language='javascript'>
							document.sForm.month.value = '<%=month%>';
						</script>
				<%	}	%>
				<input type='hidden' name='day' value='<%=day%>'>
			  </form>			  
			  </TD>
			  <TD width='' align='right' style="padding-right:10px"><img src='../br/images/ex_ing.gif' border='0' align='absmiddle'>������ &nbsp;<img src='../br/images/ex_book.gif' border='0' align='absmiddle'>����Ϸ� &nbsp;<img src='../br/images/ex_out.gif' border='0' align='absmiddle'>������ &nbsp;<img src='../br/images/ex_in.gif' border='0' align='absmiddle'>�԰�Ϸ�</TD></TR></TBODY>
		</TABLE></TD></TR>
  <TR><TD height='2' bgcolor='#9CA9BA'></TD></TR>

<!--����Ʈ-->
  <TR height=100%>
    <TD vAlign=top><form name='listForm' style='magrgin:0'>
	  <TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
        <TBODY>
			<TR vAlign=middle height=25>
			  <TD noWrap width=100% align=middle class='list_title'>�𵨸�</TD>
<%			for(int i=1; i<=total_day; i++){	%>
			  <TD noWrap width=1 class='list_title'><IMG src="../br/images/list_tep2.gif"></TD>
			  <TD noWrap width=21 align=middle class='list_title'><%=array_day[i-1]%></TD>
<%	        }	%></TR>
			<TR bgColor=#9DA9B9 height=1><TD colspan=<%=33+total_day%>></TD></TR>

<%			while(table_iter.hasNext()){
				day = (String[][])table_iter.next();
				int l=day[0][0].indexOf('$');         
				String cid = day[0][0].substring(l+1);  // cid OR "������" �ޱ�
%>

				<TR bgColor=#ffffff>
				<%=day[0][0].substring(0,l)%>
<%				for(int i=1; i<=total_day; i++){	%>
				  <TD width=1></TD>
				  <TD align=middle height="22" width="15" <% if (cid.equals("������")){}else{%> onclick="javascript:lending_App('<%=cid%>','<%=i%>')" style="CURSOR: hand" <%}%> <% if(i%2 != 0) out.print(" bgcolor='#F5F5F5'"); %>><%=day[i][0]%><%=day[i][1]%></td>
<%		}	
%>				 </TR>
				 <TR bgColor=#dadada height=1>
					  <TD colSpan='<%=33+total_day%>'><IMG height=1 src="../br/images/n.gif" width=1 border=0></TD>
				 </TR>
<% } //---- end of while
%>
		</TBODY></TABLE></TD></TR></TBODY></TABLE></form>
</BODY>
</HTML>


<script language='javascript'>
	function go(){
		
		var f = document.sForm;
		var year = f.year.value;
		var month = f.month.value;
		location.href = "../servlet/BookResourceServlet?category=car&mode=view_stat&y="+year+"&m="+month;
	}

	function visible(curr,ie,ne){//t11,visible,show ; curr:���簴ü
		if(document.all){
			curr.style.visibility=ie
			curr.style.pixelLeft=event.clientX+10//�հ����� ������ �κ� ó��
			curr.stylepixelTop=event.clientY+10

		}else{
			curr.visibility=ne
			curr.left=e.pageX
			curr.top=e.pageY
		}
	}



	var select_obj;
	
	function ANB_layerAction(obj,status) { 

		var _tmpx,_tmpy, marginx, marginy;
			_tmpx = event.clientX + parseInt(obj.offsetWidth);
			_tmpy = event.clientY + parseInt(obj.offsetHeight);
			_marginx = document.body.clientWidth - _tmpx;
			_marginy = document.body.clientHeight - _tmpy ;
		if(_marginx < 0)
			_tmpx = event.clientX + document.body.scrollLeft + _marginx ;
		else
			_tmpx = event.clientX + document.body.scrollLeft ;
		if(_marginy < 0)
			_tmpy = event.clientY + document.body.scrollTop + _marginy +20;
		else
			_tmpy = event.clientY + document.body.scrollTop ;

		obj.style.posLeft=_tmpx-13;
		obj.style.posTop=_tmpy+20;

		if(status=='visible') {
			if(select_obj) {
				select_obj.style.visibility='hidden';
				select_obj=null;
			}
			select_obj=obj;
		}else{
			select_obj=null;
		}
		obj.style.visibility=status; 

	}


function lending_App(cid,i) {
	
		var f = document.sForm;
		var year = f.year.value;
		var month = f.month.value;
		
			if(i<10) {
				i="0"+i;
			} 	

		var fday=year+""+month+""+i;

		// ���ó�¥
		today = new Date();
		var c_month = today.getMonth()+1;
		var c_date = today.getDate();

		if(c_month < 10) c_month = "0" + c_month;
		if(c_date < 10) c_date = "0" + c_date;
		
		var cday = today.getYear()+""+c_month+""+c_date;
		///

		if( fday < cday ) { // ��¥ �� (�˻���¥/���糯¥)
			alert("���� ���� ��¥���� ���� ��û�� �� �� �����ϴ�.");		
		} else {
			location.href ="../servlet/BookResourceServlet?category=car&mode=add_lending&cid="+cid+"&y="+year+"&m="+month+"&d="+i;	
		}

}
</script>
