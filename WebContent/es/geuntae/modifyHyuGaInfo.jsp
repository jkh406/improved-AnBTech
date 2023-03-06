<%@ include file="../../admin/configPopUp.jsp"%>
<%@ include file= "../../admin/chk/chkES02.jsp"%>
<%@ page		
	info		= "�����ϼ� ����"		
	contentType = "text/html; charset=euc-kr"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.io.*"
	import		= "java.util.*"
	import		= "java.sql.*"
	import		= "com.anbtech.date.anbDate"
	import		= "com.anbtech.util.normalFormat"
	import		= "com.anbtech.text.StringProcess"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<%
	//*********************************************************************
	// ���� ����
	//*********************************************************************
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	normalFormat fmt = new com.anbtech.util.normalFormat("00");				//�������
	StringProcess str = new com.anbtech.text.StringProcess();				//���ڿ� �ٷ��

	//�����û�� �������
	String msg = "";
	String query = "";
	String user_id = "";			//����� id
	String user_name = "";			//����� ��
	String fellow_names = "";		//������	 ���/�̸�;
	String syear = "";				//���� ��
	String smonth = "";				//    ��
	String sdate = "";				//    ��
	String edyear = "";				//���� ��
	String edmonth = "";			//    ��
	String eddate = "";				//    ��
	String hd_var = "";				//���� �ڵ�
	String hd_name = "";			//���� �̸�
	String search_cnt = "";			//�ش籸���� ����
	String gt_id = "";				//�����ڵ� //���޹��� from geuntae_master
	String[][] fellows;				//���, �̸�
	String search_yy = "";			//���⵵
	String search_mm = "";			//����
	String search_mm_col = "";		//���� �÷���
	String search_id = "";			//�ش��� ���
	String mod_id = "";				//������ �����ڵ� to geuntae_count
	/*********************************************************************
	 	���� �����ڵ� ����ϱ�(yangsic_env)
	*********************************************************************/	
	String[] ysColumn = {"ys_name","ys_value"};
	bean.setTable("yangsic_env");			
	bean.setColumns(ysColumn);
	bean.setOrder("ys_name ASC");	
	query = "where (ys_name like 'BT_%') or (ys_name like 'OT_%') or(ys_name like 'HD_%')";
	bean.setSearchWrite(query);
	bean.init_write();

	int cnt = bean.getTotalCount();
	String[][] items = new String[cnt][2];
	int n = 0;
	while(bean.isAll()) {
		items[n][0] = bean.getData("ys_name");		//�����ڵ�
		items[n][1] = bean.getData("ys_value");		//�����׸�
		//out.println(items[n][0] + " : " + items[n][1] + "<br>");
		n++;
	} //while

	/*********************************************************************
	// 	���� ������ ���� �˾ƺ���
	*********************************************************************/
	gt_id = request.getParameter("gt_id");	if(gt_id == null) gt_id = "";
	String[] Column = {"user_id","user_name","fellow_names","u_year","u_month","u_date",
						"tu_year","tu_month","tu_date","hd_var"};
	bean.setTable("geuntae_master");			
	bean.setColumns(Column);
	bean.setOrder("ac_name ASC");	
	query = "where (gt_id ='"+gt_id+"')";
	bean.setSearchWrite(query);
	bean.init_write();

	while(bean.isAll()) {
		user_id = bean.getData("user_id");			//�ۼ��� ���
		user_name = bean.getData("user_name");		//�ۼ��� ��
		fellow_names = bean.getData("fellow_names");//������ ���/�̸�
		syear = bean.getData("u_year");				//���� ��
		smonth = fmt.toDigits(Integer.parseInt(bean.getData("u_month")));			//    ��
		sdate = fmt.toDigits(Integer.parseInt(bean.getData("u_date")));				//    ��
		edyear = bean.getData("tu_year"); if(edyear.length() == 0) edyear = syear;	//���� ��
		String edm = bean.getData("tu_month"); if(edm.length() == 0) edm = smonth;
		edmonth = fmt.toDigits(Integer.parseInt(edm));								//    ��
		String edd = bean.getData("tu_date"); if(edd.length() == 0) edd = sdate;
		eddate = fmt.toDigits(Integer.parseInt(edd));								//    ��
		hd_var = bean.getData("hd_var");			// ���� ���� �ڵ�
	} //while
	//���� ���� ã��
	for(int x=0; x<items.length; x++) if(items[x][0].equals(hd_var)) hd_name = items[x][1];
	
	//����� ã��
	if(fellow_names == null) fellow_names = "";
	if(fellow_names.length() != 0) {
		fellow_names = fellow_names.trim();
		StringTokenizer f = new StringTokenizer(fellow_names,";");
		int fcnt = f.countTokens() + 1;
		fellows = new String[fcnt][2];
		fellows[0][0] = user_id;
		fellows[0][1] = user_name;
		int i = 1;
		while(f.hasMoreTokens()) {
			String fn = f.nextToken();
			fn = fn.trim();
			StringTokenizer in = new StringTokenizer(fn,"/");
			int j = 0;
			while(in.hasMoreTokens()) {
				if(j == 0) fellows[i][0] = in.nextToken();
				else fellows[i][1] = in.nextToken();
				j++;
			}
			i++;
		}
	}
	else {
		fellows = new String[1][2];
		fellows[0][0] = user_id;
		fellows[0][1] = user_name;
	}

	//���⵵ ã��
	search_yy = request.getParameter("search_yy"); if(search_yy == null) search_yy = syear;
	int tyi = Integer.parseInt(edyear) - Integer.parseInt(syear) + 1;
	int[] tyear = new int[tyi];
	for(int x=0; x<tyi; x++) tyear[x] = Integer.parseInt(syear) + x;
	//for(int x=0; x<tyear.length; x++) out.println("tyear : "+ tyear[x] + "<br>");
	//���� ã��
	int[] tmon ;
	if(tyi == 1) {		//���۰� ���� �����⵵
		int tmi = Integer.parseInt(edmonth) - Integer.parseInt(smonth) + 1;
		tmon = new int[tmi];
		for(int x=0; x<tmi; x++) tmon[x] = Integer.parseInt(smonth) + x;
		//for(int x=0; x<tmon.length; x++) out.println("tmon : "+ tmon[x] + "<br>");
	}
	else { //���۰� �� �⵵�� �ٸ����
		if(search_yy.equals(syear)) {				//���۳⵵
			int tmi = 13 - Integer.parseInt(smonth);
			tmon = new int[tmi];
			//out.println("tmi : " + tmi + "<br>");
			for(int x=Integer.parseInt(smonth),y=0; x<13; x++,y++) tmon[y] = x; 
		} else if (search_yy.equals(edyear)) {		//���⵵
			int tmi = Integer.parseInt(edmonth);
			tmon = new int[tmi];
			for(int x=1,y=0; x<=tmi; x++,y++) tmon[y] = x;  
		} else {									//�߰��⵵
			int tmi = 12;
			tmon = new int[tmi];
			for(int x=1,y=0; x<=tmi; x++,y++) tmon[y] = x; 
		}
		//for(int x=0; x<tmon.length; x++) out.println("tmon : "+ tmon[x] + "<br>");
	}

	/*********************************************************************
	// 	���� ���� ���� �˾ƺ���
	*********************************************************************/
	String[] cColumn = {"gt_id","jan1","feb2","mar3",
						"apr4","may5","jun6","jul7","aug8","sep9","oct10","nov11","dec12"};
	search_mm = request.getParameter("search_mm"); if(search_mm == null) search_mm = smonth;
	String pass = "0";
	for(int x=0; x<tmon.length; x++) {
		int sm = Integer.parseInt(search_mm);
		if(tmon[x] == sm) pass = "1";
	}
	if(pass.equals("0")) search_mm = fmt.toDigits(tmon[0]);

	if(search_mm.equals("01")) search_mm_col="jan1";
	else if(search_mm.equals("02")) search_mm_col = "feb2";
	else if(search_mm.equals("03")) search_mm_col = "mar3";
	else if(search_mm.equals("04")) search_mm_col = "apr4";
	else if(search_mm.equals("05")) search_mm_col = "may5";
	else if(search_mm.equals("06")) search_mm_col = "jun6";
	else if(search_mm.equals("07")) search_mm_col = "jul7";
	else if(search_mm.equals("08")) search_mm_col = "aug8";
	else if(search_mm.equals("09")) search_mm_col = "sep9";
	else if(search_mm.equals("10")) search_mm_col = "oct10";
	else if(search_mm.equals("11")) search_mm_col = "nov11";
	else if(search_mm.equals("12")) search_mm_col = "dec12";
	search_id = request.getParameter("search_id"); if(search_id == null) search_id = user_id;

	bean.setTable("geuntae_count");			
	bean.setColumns(cColumn);
	query = "where (hd_var ='"+hd_var+"') and (user_id ='"+search_id+"') and (thisyear='"+search_yy+"')";
	bean.setSearchWrite(query);
	bean.init_write();
	while(bean.isAll()) {
		mod_id = bean.getData("gt_id");
		search_cnt = bean.getData(search_mm_col);
	}

	/*********************************************************************
	// 	�����ϱ�
	*********************************************************************/
	String req = request.getParameter("req"); if(req == null) req = "";
	if(req.equals("mod")) {
		search_cnt = request.getParameter("search_cnt");
		query = "update geuntae_count set "+search_mm_col+"='"+search_cnt;
		query += "' where gt_id='"+mod_id+"'";
		bean.execute(query);
		msg = "����";
	}
%>

<script language=javascript>
<!--
var msg = '<%=msg%>'
if(msg.length != 0) self.close();


//����� ����
function selectUser()
{
	document.eForm.action="modifyHyuGaInfo.jsp";
	document.eForm.submit();
}
//���⵵
function selectYear()
{
	document.eForm.action="modifyHyuGaInfo.jsp";
	document.eForm.submit();

	document.eForm.action="modifyHyuGaInfo.jsp";
	document.eForm.submit();
	
}
//����
function selectMonth()
{
	document.eForm.action="modifyHyuGaInfo.jsp";
	document.eForm.submit();
}
//�����ϱ�
function cntModify()
{
	document.eForm.action="modifyHyuGaInfo.jsp";
	document.eForm.req.value="mod";
	document.eForm.submit();
}
-->
</script>

<HTML><HEAD><TITLE>���ο������¼��� ����</TITLE>
<META http-equiv=Content-Type content="text/html; charset=euc-kr">
<link href="../css/style.css" rel="stylesheet" type="text/css">
</HEAD>
<BODY leftMargin=0 topMargin=0 marginheight="0" marginwidth="0" oncontextmenu="return false">
<table width="100%" border="0" cellspacing="0" cellpadding="0">
  <tr><td align="center">
	<!--Ÿ��Ʋ-->
	<TABLE cellSpacing=0 cellPadding=0 width="100%" border=0>
      <TBODY>
        <TR><TD height="3" bgcolor="0C2C55"></TD></TR>
        <TR>
          <TD height="33" valign="middle" bgcolor="#73AEEF"><img src="../images/pop_geuntae_m.gif" width="181" height="17" hspace="10"></TD></TR>
        <TR>
          <TD height="2" bgcolor="2167B6"></TD></TR></TBODY></TABLE>
	
	<table cellspacing=0 cellpadding=2 width="94%" border=0>
	   <tbody><form name="eForm" action="modifyHyuGaInfo.jsp" method="post" style="margin:0">
         <tr><td height=20 colspan="2"></td></tr>
         <tr bgcolor="c7c7c7"><td height=1 colspan="2"></td></tr>
         <tr>
           <td width="30%" height="25" class="bg_01" background="../images/bg-01.gif">�����</td>
           <td width="70%" height="25" class="bg_04">
			<select name="search_id" width=50 onChange="javascript:selectUser();">
		<%
			for(int i=0; i<fellows.length; i++) {
				if(fellows[i][0].equals(search_id))
					out.println("<option selected value='"+fellows[i][0]+"'>"+fellows[i][1]+"</option>");
				else 
					out.println("<option value='"+fellows[i][0]+"'>"+fellows[i][1]+"</option>");
			}
		%></select>		   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
           <td width="30%" height="25" class="bg_01" background="../images/bg-01.gif">����</td>
           <td width="70%" height="25" class="bg_04"><%=hd_name%></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
           <td width="30%" height="25" class="bg_01" background="../images/bg-01.gif">������</td>
           <td width="70%" height="25" class="bg_04">
			<select name="search_yy" width=50 onChange="javascript:selectYear();">
		<%
			for(int i=0; i<tyear.length; i++) {
				if(tyear[i] == Integer.parseInt(search_yy))
					out.println("<option selected value='"+tyear[i]+"'>"+tyear[i]+"</option>");
				else
					out.println("<option value='"+tyear[i]+"'>"+tyear[i]+"</option>");
			}
		%>	</select>	   
			<select name="search_mm" width=50 onChange="javascript:selectMonth();">
		<%
			for(int i=0; i<tmon.length; i++) {
				if(tmon[i] == Integer.parseInt(search_mm))
					out.println("<option selected value='"+fmt.toDigits(tmon[i])+"'>"+fmt.toDigits(tmon[i])+"</option>");
				else 
					out.println("<option value='"+fmt.toDigits(tmon[i])+"'>"+fmt.toDigits(tmon[i])+"</option>");
			}
		%>	</select>	   
		   </td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
           <td width="30%" height="25" class="bg_01" background="../images/bg-01.gif">����</td>
           <td width="70%" height="25" class="bg_04"><input type='text' name='search_cnt' value='<%=search_cnt%>' size="5"></td></tr>
         <tr bgcolor="C7C7C7"><td height="1" colspan="2"></td></tr>
         <tr><td height=20 colspan="2"></td></tr></tbody></table>
<input type='hidden' name='req' value=''>
<input type='hidden' name='gt_id' value='<%=gt_id%>'>		 
</form>

	<!--������-->
    <TABLE cellSpacing=0 cellPadding=1 width="100%" border=0>
        <TBODY>
          <TR>
            <TD height=32 align=right bgcolor="C6DEF8" style="padding-right:10px"><a href="Javascript:cntModify();"><img src='../images/bt_modify.gif' align='absmiddle' border='0'></a> <a href="Javascript:self.close();"><img src='../images/bt_close.gif' align='absmiddle' border='0'></a></TD>
          </TR>
          <TR>
            <TD width="100%" height=3 bgcolor="0C2C55"></TD>
          </TR>
        </TBODY></TABLE></td></tr></table></BODY></HTML>