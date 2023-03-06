<%@ include file="../admin/configPopUp.jsp"%>
<%@ page
	language	= "java" 
	contentType	= "text/html;charset=euc-kr"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.text.Hanguel,com.anbtech.pu.entity.*,com.anbtech.st.entity.*"
	errorPage	= "../admin/errorpage.jsp"
%>
<%	/**** ������ �˻� ***************************************************************/
	/*  1. ȣ�� page���� �ʿ��� ������ field name�� Parameter�� �޴´�.
	/*     
	/*  2. �������� �������� iframe�� ���� ������ ������� Servlet����
	/*     �ٷ� �����Ѵ�.
	/*     1) servlet�� ���� ������ jsp page�� "returnValue()"ȣ���ؼ� 
	/*        �޾ƿ� ������ ����page(openModelInfoWindow.jsp)�� setting �ϵ����Ѵ�.
	/*        setting�� openModelInfoWindow.jsp���� ȣ�� page�� ���� �Ѱ��ִ� 
	/*        �޼ҵ� (parent.return_value()) �ڵ� ����Ǿ�  ȣ���� page�� ����
	/*        ������ �Ѱ��ش�.
	/*        �䱸���� setting�� �迭�� ������ GoodsInfoDAO���� searchModelList()�� �����Ѵ�.
	/*        ���� �߰��� GoodsInfoDAO���� �����ϵ����Ѵ�. 
	/*    
	/*   #. ȣ������������ parameter �ѱ�� String 
	/*   var strUrl = "../gm/openModelInfoWindow.jsp?one_class=��ǰ�� field��
	/*                &one_name=��ǰ���� field��&two_class=��ǰ�ڵ� field��&two_name=��ǰ�� field��
	/*                &three_class=�𵨱��ڵ� field��&three_name=�𵨱��� field ��
	/*                &four_class=���ڵ� field��&four_name=�𵨸� field��&fg_code=fg�ڵ� field��";
	/*
	/*	 #.	���� �߰��� �����ؾ��� ���� �� method
    /*      1. ȣ�� �������� �䱸���� ������ �Ѱ��ִ� parameter�� �߰�
	/*      2. gm/openModelInfoWindow.jsp (ȣ��page���� �ޱ�, iframe���� �ޱ�)
	/*      3. GoodsInfoDAO �� ���� ���� �߰�
	/*      4. gm/searchModelInfo.jsp �� �߰�field �߰�
	/*
	/********************************************************************************************/


	// ȣ��(�䱸)���� field�� 
	String one_class   = request.getParameter("one_class");		// ��ǰ���ڵ�
	String one_name    = request.getParameter("one_name");		// ��ǰ����
	String two_class   = request.getParameter("two_class");		// ��ǰ�ڵ�
	String two_name    = request.getParameter("two_name");		// ��ǰ��
	String three_class = request.getParameter("three_class");	// �𵨱��ڵ�
	String three_name  = request.getParameter("three_name");	// �𵨱���
	String four_class  = request.getParameter("four_class");	// ���ڵ�
	String four_name   = request.getParameter("four_name");		// �𵨱���
	String fg_code	   = request.getParameter("fg_code");		// f/g�ڵ�	
%>

<HTML>
<LINK rel="stylesheet" type="text/css" href="../../gm/css/style.css">
<HEAD>
<TITLE>�������˻�</TITLE>
</HEAD>
<BODY topmargin="0" leftmargin="0">
<FORM name="eForm" style="margin:0">

<TABLE  cellSpacing=0 cellPadding=0 width="100%">
	<TR><TD vAlign=top>
		<TABLE  cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR><TD height="33" valign="middle" bgcolor="#73AEEF" ><img src="images/gm_modelsearch.gif" alt='�������˻�'></TD>
					<TD style="padding-right:10px" align='right' valign='middle' bgcolor="#73AEEF"></TD></TR>
				<TR><TD height='1' bgcolor='#9DA8BA' colspan='2'></TD></TR>
				<TR><TD height="2" bgcolor="2167B6"  colspan='2'></TD></TR>
				</TBODY></TABLE></TD></TR>
	<TR height='32'><TD vAlign=middle>
		<TABLE cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR align='center'><TD>
					<IFRAME name="search" src="../servlet/GoodsInfoServlet?mode=search_model_info" width="98%" height="330" border="0" frameborder="0" scrolling="no">�������� �ζ��� �������� �������� �ʰų� ���� �ζ��� �������� ǥ������ �ʵ��� �����Ǿ� �ֽ��ϴ�.</IFRAME></TD></TR>
				
				</TBODY></TABLE></TD></TR>
	<TR><TD height=5 colspan="2"></TD></TR>
	<!--������-->
	<TR><TD>
		<TABLE cellSpacing=0 cellPadding=0 width="100%">
			<TBODY>
				<TR><TD width='820' height="33" align="right" bgcolor="#73AEEF" style='padding-right:4px'>
					<A href='javascript:self.close()'><img src='images/bt_close.gif' border='0' align='absright'></a></TD></TR>
				<TR><TD height="" bgcolor="0C2C55" colspan='2'></TD></TR></TBODY></TABLE></TD></TR>
		<!-- IFRAME���� �޾ƿ��� ���� �� -->
		<INPUT type='hidden' name='fname'>
		<INPUT type='hidden' name='one_class'>
		<INPUT type='hidden' name='one_name'>
		<INPUT type='hidden' name='two_class'>
		<INPUT type='hidden' name='two_name'>
		<INPUT type='hidden' name='three_class'>
		<INPUT type='hidden' name='three_name'>
		<INPUT type='hidden' name='four_class'>
		<INPUT type='hidden' name='four_name'>
		<INPUT type='hidden' name='fg_code'>

		<!-- ȣ�� �������� Field�� -->
		<INPUT type='hidden' name='field_one_class' value='<%=one_class%>'>
		<INPUT type='hidden' name='field_one_name' value='<%=one_name%>'>
		<INPUT type='hidden' name='field_two_class' value='<%=two_class%>'>
		<INPUT type='hidden' name='field_two_name' value='<%=two_name%>'>
		<INPUT type='hidden' name='field_three_class' value='<%=three_class%>'>
		<INPUT type='hidden' name='field_three_name' value='<%=three_name%>'>
		<INPUT type='hidden' name='field_four_class' value='<%=four_class%>'>
		<INPUT type='hidden' name='field_four_name' value='<%=four_name%>'>
		<INPUT type='hidden' name='field_fg_code' value='<%=fg_code%>'>

</FORM>

</TABLE>
</BODY>
</HTML>

<script language='javascript'>
	
function return_value(){
	var f = document.forms[0];
	
		var one_class	= f.one_class.value;
		var one_name	= f.one_name.value;
		var two_class	= f.two_class.value;
		var two_name	= f.two_name.value;
		var three_class	= f.three_class.value;
		var three_name	= f.three_name.value;
		var four_class	= f.four_class.value;
		var four_name	= f.four_name.value;
		var fg_code		= f.fg_code.value;

		var f_one_class		= f.field_one_class.value;
		var f_one_name		= f.field_one_name.value;
		var f_two_class		= f.field_two_class.value;
		var f_two_name		= f.field_two_name.value;
		var f_three_class	= f.field_three_class.value;
		var f_three_name	= f.field_three_name.value;
		var f_four_class	= f.field_four_class.value;
		var f_four_name		= f.field_four_name.value;
		var f_fg_code		= f.field_fg_code.value;

		if(opener.document.forms[0].<%=one_class%>){
			opener.document.forms[0].<%=one_class%>.value = one_class;
		}

		if(opener.document.forms[0].<%=one_name%>){
			opener.document.forms[0].<%=one_name%>.value = one_name;
		}

		if(opener.document.forms[0].<%=two_class%>){
			opener.document.forms[0].<%=two_class%>.value = two_class;
		}

		if(opener.document.forms[0].<%=two_name%>){
			opener.document.forms[0].<%=two_name%>.value = two_name;
		}

		if(opener.document.forms[0].<%=three_class%>){
			opener.document.forms[0].<%=three_class%>.value = three_class;
		}

		if(opener.document.forms[0].<%=three_name%>){
			opener.document.forms[0].<%=three_name%>.value = three_name;
		}

		if(opener.document.forms[0].<%=four_class%>){
			opener.document.forms[0].<%=four_class%>.value = four_class;
		}

		if(opener.document.forms[0].<%=four_name%>){
			opener.document.forms[0].<%=four_name%>.value = four_name;
		}

		if(opener.document.forms[0].<%=fg_code%>){
			opener.document.forms[0].<%=fg_code%>.value = fg_code;
		}

		self.close();
}
</script>