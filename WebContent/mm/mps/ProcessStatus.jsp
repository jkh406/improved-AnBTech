<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "��������º���"		
	contentType = "text/html; charset=euc-kr" 	
	errorPage	= "../../admin/errorpage.jsp"
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.*"
	import="com.anbtech.mm.entity.*"
	import="java.sql.Connection"
	import="com.anbtech.date.anbDate"
	import="com.anbtech.text.StringProcess"
%>
<%
	String factory_no = (String)request.getAttribute("factory_no");
	String fg_code = (String)request.getAttribute("fg_code");
	String model_code = (String)request.getAttribute("model_code");
	String model_name = (String)request.getAttribute("model_name");

	String mps_status = (String)request.getAttribute("mps_status");
	String mrp_status = (String)request.getAttribute("mrp_status");
	if(mrp_status.length() == 0) mrp_status = "-1";

	String pur_status = (String)request.getAttribute("pur_status");
	if((Integer.parseInt(mrp_status) >= 3) && pur_status.length() ==0) pur_status="S00";

	String mfg_status = (String)request.getAttribute("mfg_status");
	if(mfg_status.length() == 0) mfg_status = "-1";

	String pro_status = (String)request.getAttribute("pro_status");
	String qc_status = (String)request.getAttribute("qc_status");

%>

<HTML><HEAD><TITLE>����������Ȳ</TITLE>
<LINK href="../mm/css/style.css" rel=stylesheet></head>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0">

<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../mm/images/pop_making_status.gif"  alt='����������Ȳ'></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
	<TR><TD height='20'></TD></TR>
	<table cellspacing=0 cellpadding=0 width="94%" border=0 align='center'>
	   <tbody>
         <TR><TD align="center">
			<TABLE cellspacing=0 cellpadding=2 width="100%" border=0>
				<TBODY>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">�����ȹ</TD>
						<TD width="87%" height="25" class="bg_04" colspan=3>
						<%
							String[] mps_no = {"1","2","3"};
							String[] mps_name = {"MPS���","���ο�û","MPSȮ��"};
							String mps_sel = "";
							for(int i=0; i<mps_no.length; i++) {
								if(mps_no[i].equals(mps_status)) mps_sel = "checked";
								else mps_sel = "";
								out.println("<INPUT type='radio' "+mps_sel+" value=''>"+mps_name[i]);
							} 
							if(mps_status.length() != 0 && Integer.parseInt(mps_status)>3) 
								out.println("<INPUT type='radio' checked value=''>��ȹ�Ϸ�");
						%>
						</TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">MRS����</TD>
						<TD width="87%" height="25" class="bg_04" colspan=3>
						<%
							String[] mrp_no = {"1","2","31","0","3"};
							String[] mrp_name = {"�������","MRS�ۼ�","������","����ݷ�","MRSȮ��"};
							String mrp_sel = "";
							for(int i=0; i<mrp_no.length; i++) {
								if(mrp_no[i].equals(mrp_status)) mrp_sel = "checked";
								else mrp_sel = "";
								out.println("<INPUT type='radio' "+mrp_sel+" value=''>"+mrp_name[i]);
							} 
							if(mrp_status.length() != 0 && Integer.parseInt(mrp_status)>3) 
								out.println("<INPUT type='radio' checked value=''>MRS�Ϸ�");
						%>
						</TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">��ǰ����</TD>
						<TD width="87%" height="25" class="bg_04" colspan=3>
						<%
							if((pur_status.length() != 0) && !pur_status.equals("S21") && !pur_status.equals("S25"))
								out.println("<INPUT type='radio' checked value=''>����ó��");

							String[] pur_no = {"S21","S25"};
							String[] pur_name = {"�����԰�","ǰ���԰�"};
							String pur_sel = "";
							for(int i=0; i<pur_no.length; i++) {
								if(pur_no[i].equals(pur_status)) pur_sel = "checked";
								else pur_sel = "";
								out.println("<INPUT type='radio' "+pur_sel+" value=''>"+pur_name[i]);
							} 
						
						%>
						</TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">��������</TD>
						<TD width="87%" height="25" class="bg_04" colspan=3>
						<%
							String[] mfg_no = {"1","2","3","4"};
							String[] mfg_name = {"�������","�������","�۾�����","��ǰ���"};
							String mfg_sel = "";
							for(int i=0; i<mfg_no.length; i++) {
								if(mfg_no[i].equals(mfg_status)) mfg_sel = "checked";
								else mfg_sel = "";
								out.println("<INPUT type='radio' "+mfg_sel+" value=''>"+mfg_name[i]);
							} 
							if(mfg_status.length() != 0 && Integer.parseInt(mfg_status)>4) 
								out.println("<INPUT type='radio' checked value=''>���ÿϷ�");
						%>
						</TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">��ǰ����</TD>
						<TD width="87%" height="25" class="bg_04" colspan=3>
						<%
							String[] pro_no = {"1","2"};
							String[] pro_name = {"�������","����Ϸ�"};
							String pro_sel = "";
							for(int i=0; i<pro_no.length; i++) {
								if(pro_no[i].equals(pro_status)) pro_sel = "checked";
								else pro_sel = "";
								out.println("<INPUT type='radio' "+pro_sel+" value=''>"+pro_name[i]);
							} 
							
						%>
						</TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					<TR>
						<TD width="13%" height="25" class="bg_03" background="../mm/images/bg-01.gif">ǰ���˻�</TD>
						<TD width="87%" height="25" class="bg_04" colspan=3>
						<%
							String[] qc_no = {"S01","S05"};
							String[] qc_name = {"�˻��Ƿ�","�˻�Ϸ�"};
							String qc_sel = "";
							for(int i=0; i<qc_no.length; i++) {
								if(qc_no[i].equals(qc_status)) qc_sel = "checked";
								else qc_sel = "";
								out.println("<INPUT type='radio' "+qc_sel+" value=''>"+qc_name[i]);
							} 
							
						%>
						</TD>
					</TR>
					<TR bgcolor="c7c7c7"><TD height=1 colspan="4"></TD></TR>
					
			</TBODY></TABLE></TD></TR></tbody></table>
	
	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
		<TR><TD height='17'></TD></TR>
          <TR>
            <TD height=32 colSpan=4 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="javascript:self.close();"><img src="../mm/images/close.gif" width="46" height="19" border="0"></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=1 colSpan=4 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr>
</table>
</BODY>
</HTML>