<%@ include file= "../../admin/configHead.jsp"%>
<%@ page 
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.util.*,com.anbtech.ew.entity.*,com.anbtech.date.anbDate,com.anbtech.text.Hanguel"
%>

<%	// ���� �ð�
	anbDate anbdt = new com.anbtech.date.anbDate();		
	String hrs		= anbdt.getHours();	
	String c_date	= anbdt.getDateNoformat();
	String cur		= c_date.substring(0,4)+"-"+c_date.substring(4,6)+"-"+c_date.substring(6,8);

	// ���رٹ��ð�
	StandardWorkTimeTable swtimeTable = new StandardWorkTimeTable();
	swtimeTable = (StandardWorkTimeTable)request.getAttribute("swtimeTable");

	String offstime = swtimeTable.getOffStime();				// ���� �ٹ� ���� �ð�
	String offstime_hh = offstime.substring(0,2);	
	String holidaystime = swtimeTable.getFixStimeHoliday();		// ���� �ٹ� ���� �ð�
	String holidaystime_hh = holidaystime.substring(0,2);
	String holidaystimesat = swtimeTable.getOffStimeSat();		// ����� ���� �ٹ� ���� �ð�
	String holidaystimesat_hh = holidaystimesat.substring(0,2);
%>
<HTML>
<HEAD><TITLE></TITLE>
<META http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<LINK rel="stylesheet" href="../ew/css/style.css" type="text/css">
</HEAD>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<TABLE border=0 cellspacing=0 cellpadding=0 width="100%">
	<TR><TD height=27><!--Ÿ��Ʋ-->
		<TABLE height=27 cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR bgcolor="#4F91DD"><TD height="2" colspan="2"></TD></TR>
				<TR bgcolor="#BAC2CD">
					<TD valign='middle' class="title"><img src="../ew/images/blet.gif"> Ư�ٽ�û</TD></TR></TBODY></TABLE></TD></TR>
	<TR><TD height='1' bgcolor='#9DA8BA'></TD></TR>
	<TR><TD height=32><!--��ư-->
		<TABLE cellSpacing=0 cellPadding=0>
			<TBODY>
				<TR><TD align=left width=5 ></TD>
					<TD align=left width=500>
					<A href="javascript:go_recording()"><img src="../ew/images/bt_save.gif" border="0" align="absmiddle"></a>
					<A href="javascript:history.go(-1)"><img src="../ew/images/bt_cancel.gif" border="0" align="absmiddle"></a>
					</TD></TR></TBODY></TABLE></TD></TR></TABLE>

<!--����-->
<FORM method='post' name='eForm' action="../servlet/ExtraWorkServlet" style="margin:0">
<TABLE width="100%" border="0" cellspacing="0" cellpadding="0">
	<TR><TD align="center">
    <!--�⺻����-->
	<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
		<TBODY>
			<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../ew/images/bg-01.gif">�ۼ���</TD>
				<TD width="37%" height="25" class="bg_04"><%=login_div%> <%=login_name%>/<%=login_id%></TD>
				<TD width="13%" height="25" class="bg_03" background="../ew/images/bg-01.gif">�ۼ�����</TD>
				<TD width="37%" height="25" class="bg_04"><%=c_date.substring(0,4)%>-<%=c_date.substring(4,6)%>-<%=c_date.substring(6,8)%></TD></TR>
			<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
			<TR>
				<TD width="13%" height="25" class="bg_03" background="../ew/images/bg-01.gif">Ư�ٻ���</TD>
				<TD width="37%" height="25" class="bg_04"><input type="text" name="duty" size="30" maxlength="30" class="text_01"></TD>
				<TD width="13%" height="25" class="bg_03" background="../ew/images/bg-01.gif">�ٹ�����</TD>
				<TD width="37%" height="25" class="bg_04">
					<SELECT name="w_type">
						<OPTION value="n">���� Ư��</OPTION>
						<OPTION value="s">����� Ư��</OPTION>
						<OPTION value="h">���� Ư��</OPTION>
					</SELECT></TD></TR>
			<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../ew/images/bg-01.gif">Ư������</TD>
				<TD width="37%" height="25" class="bg_04"><input type="text" name="w_sdate" size='10' value="<%=cur%>" maxlength="10" readOnly> <a href="javascript:wopen('../ew/common/Calendar.jsp?FieldName=w_sdate', '', 180, 250);"><img src='../ew/images/bt_calendar.gif' border='0' align='absmiddle'></a></TD>
				<TD width="13%" height="25" class="bg_03" background="../ew/images/bg-01.gif">Ư�ٽð�</TD>
				<TD width="37%" height="25" class="bg_04">
				    <SELECT name="w_stime" CLASS="etc">
	<%
			String[] asHour = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
			String msSEL = "";
					
			for(int asH=0; asH<24; asH++){
				if(asH == Integer.parseInt(hrs)) msSEL = " SELECTED "; else msSEL="";
				out.println(" <OPTION " + msSEL + " value='"+asHour[asH]+":00'>" + asHour[asH] + ":" + "00");
				out.println(" <OPTION value='"+asHour[asH]+":30'>" + asHour[asH] + ":" + "30");
			}
				out.println("</SELECT> ~ ");
	%>
				<SELECT name='to_tom'>
					<option value='1'>����</option>
					<option value='2'>����</option>
				</SELECT>
	
				<SELECT name="w_etime" CLASS="etc">
	<%			String[] asHour2 = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23"};
				String msSEL2 = "";
				for(int asH2=0; asH2<24; asH2++){
					if(asH2 == Integer.parseInt(hrs)) msSEL2 = " SELECTED "; else msSEL2="";
						out.println("<OPTION " + msSEL2 + " value='"+asHour2[asH2]+":00'>" + asHour2[asH2] + ":" + "00");
						out.println("<OPTION value='"+asHour2[asH2]+":30'>" + asHour2[asH2] + ":" + "30");
				}
				out.println("</SELECT>");
	%>				
				</TD></TR>
			<TR bgcolor="C7C7C7"><TD height=1 colspan="4"></TD></TR>
			<TR><TD width="13%" height="25" class="bg_03" background="../ew/images/bg-01.gif">��������</TD>
				<TD width="87%" height="25" class="bg_04" colspan="3"><textarea name="duty_cont" rows="4" cols="80"></textarea></TD></TR>
			<TR bgcolor="C7C7C7"><TD height="1" colspan="4"></TD></TR></TBODY></TABLE>

</TD></TR></TABLE>
<input type="hidden" name="mode" value="input_data">
</FORM>

</BODY>
</HTML>

<SCRIPT language="javascript">
	
	function go_recording(){
		var f = document.eForm;

		if(f.duty.value == ""){
			alert("Ư�ٻ����� �Է��Ͻʽÿ�.");
			f.duty.focus();
			return;
		}
/*
		if(f.duty_cont.value == ""){
			alert("���������� �Է��Ͻʽÿ�.");
			f.duty_cont.focus();
			return;
		}
*/
		var w_sdate = f.w_sdate.value;			//������
		var w_stime = f.w_stime.value;			//���۽ð�
		var w_shour = w_stime.substring(0,2);	//���۽�
		var w_smin	= w_stime.substring(3,5);	//���ۺ�

		
		var w_etime = f.w_etime.value;			//��ħ�ð�
		var w_ehour = w_etime.substring(0,2);	//��ħ��
		var w_emin	= w_etime.substring(3,5);	//��ħ��
		var to_tom  = f.to_tom.value;			//���� or ����


		var datefield = w_sdate.split("-")
	    w_sdate =  datefield[0]+datefield[1]+datefield[2]
		
		var stime = f.w_stime.value.charAt(0) +""+ f.w_stime.value.charAt(1);;

		today = new Date();
		var h = today.getHours();

		var year  = today.getYear();
		var month = today.getMonth()+1;
		var day	  = today.getDate();

		// ���ó�¥
		if(month < 10) month = "0" + month;
		if(day < 10)	 day = "0" + day;
		
		var cday = today.getYear()+""+month+""+day;
		
		if(w_sdate < cday) { // �������� ���� �������� Ȯ��
			alert("Ư�ٽ�û���� �߸��Ǿ����ϴ�. ���� ���ķ� �����Ͻʽÿ�.");
			return;
		} else if(w_sdate == cday){	// �������� ������ ���
			
			if(Number(h) < Number(w_shour)){
			} else {
				alert("Ư�ٽ�û�ð��� �߸��Ǿ����ϴ�. ����ð� ���ķ� �����Ͻʽÿ�.");
				return;
			}

			var hour = Number(w_ehour) - Number(w_shour);
			var minute = Number(w_emin) - Number(w_smin);
			
			if(hour<1){	
				alert("Ư�ٽ�û�ð��� �߸��Ǿ����ϴ�.");
				return;
			} else if(hour <2 && hour >0 && minute <0 ){
				alert("Ʈ�ٽð��� �ּ� 1�ð� �̻��̾�� �մϴ�.");
				return;
			} 
		} 

		if(f.w_type.value == "h") {
			if(stime < <%=holidaystime_hh%>) {
				alert("���� Ư�� ��û �ð��� "+ <%=holidaystime_hh%>+"�� ���Ŀ� �����մϴ�.");
				return;
			}
		} else if(f.w_type.value == "n") {
			if(stime < <%=offstime_hh%>) {
				alert("���� Ư�� ��û �ð��� "+ <%=offstime_hh%>+"�� ���Ŀ� �����մϴ�.");
				return;
			}
		
		} else if(f.w_type.value == "s") {
			if(stime < <%=holidaystimesat_hh%>) {
				alert("����� Ư�� ��û �ð��� "+ <%=holidaystimesat_hh%>+"�� ���Ŀ� �����մϴ�.");
				return;
			}
		}
		
		f.w_sdate.value = w_sdate;
		f.submit();
	}
	
	function wopen(url, t, w, h) {
		var sw;
		var sh;

		sw = (screen.Width - w) / 2;
		sh = (screen.Height - h) / 2 - 50;

		window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+',scrollbars=no,toolbar=no,status=no,resizable=no');
	}
</script>

