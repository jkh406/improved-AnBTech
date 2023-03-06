<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "�������� �޴�"		
	contentType = "text/html; charset=euc-kr" 	
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.text.Hanguel"	
	import="java.util.StringTokenizer"
%>

<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />
<jsp:useBean id="anbdt" scope="request" class="com.anbtech.date.anbDate" />

<%!
	//login ���� ����
	String id="";					//login id
	String lgname="";				//login name

	//�޽��� ���޺���
	String Message="";				//�޽��� ���� ���� 

	//view���޺��� �����ϱ�
	String cal_id="";				//�������ϴ� ������ ���
	String view_td="";				//�ְ� ���޳��� (yyyy/MM/dd)
	String Tyear="";				//���� �⵵
	String Tmonth="";				//���� ��
	String Tday="";					//���� ��

	//���������� ã��
	String[] sid;					//������ ���
	String[] sname;					//������ �̸�
	int shareid_cnt = 0;			//������ �� (����)

	//ȸ��/�μ� ���������� 
	String CSMC_items = "";			//ȸ�� �������������� ���
	String CSMC_NY = "";			//ȸ�� �űԵ�� ���� ��������
	String CSMD_items = "";			//�μ� �������������� ���
	String CSMD_NY = "";			//�μ� �űԵ�� ���� ��������
%>

<%	

	/*********************************************************************
	 	����� login �˾ƺ���
	*********************************************************************/
	Message = "NO_SESSION";
	id = login_id; 				//������ ���
	lgname = login_name;		//������ �̸�

	//�μ� �ʱ�ȭ
	cal_id = view_td = "";
	
	/*********************************************************************
	 	�Ѱܿ� ���� �б� (from Calendar_View.jsp)   Sabun=&Date=
	*********************************************************************/
	cal_id = request.getParameter("Sabun");			//�Ѱܹ��� ���
	if(cal_id == null) cal_id = id;

	/*********************************************************************
	//���ָ� �������� view(�ְ�,2�ְ�)���� �Ѱ��� ���������
	// (�ش����� �Ͽ��� ���ڸ� �Ѱ��ش�)
	*********************************************************************/	
	int iyear = Integer.parseInt(anbdt.getYear());			//�ݳ�
	int imonth = Integer.parseInt(anbdt.getMonth());		//�ݿ�
	int iday = Integer.parseInt(anbdt.getDates());			//����
	int tmp_td = anbdt.getDay(iyear,imonth,iday);			//���ÿ��� (1:�� 2:�� ~ 7:��)

	//�־��� ���ڿ��� ������ �̿��Ͽ� ������ �Ͽ��� ���ڸ� ���Ѵ�.
	//ã���� �ϴ� ������ �Ͽ��� ���ڷ� setting�� �ְ������ �Ѱ��ش�.
	int sunday = iday-tmp_td+1;
	view_td = anbdt.setDate(iyear,imonth,sunday);		
	
	/*********************************************************************
	//���� ������ ã��
	*********************************************************************/	
	String[] indColumns = {"pid","id","item","nlist"};
	bean.setTable("CALENDAR_COMMON");
	bean.setColumns(indColumns);
	bean.setOrder("id ASC");
	bean.setClear();
	bean.setSearch("id",id,"item","SID");
	bean.init_unique();

	String sdata = "";
	while(bean.isAll()) {
		sdata += bean.getData("nlist");					//������ ���
	}

	//�� ������ �׸񰹼� ���ϱ�
	shareid_cnt = 0;									//������ �׸񰹼�
	for(int ii = 0; ii < sdata.length(); ii++)
		if(sdata.charAt(ii) == ';') shareid_cnt++;

	//�迭�� ������ �׸���
	sid = new String[shareid_cnt+1];					//��� �迭�Ҵ�
	sname = new String[shareid_cnt+1];					//�̸� �迭�Ҵ�	

	sid[0] = id;										//������ �ڽ� ��� (�ʱⰪ)
	sname[0] = lgname;									//������ �ڽ� �̸� (�ʱⰪ)

	int aj = 0;											//���� ������
	int ak = 1;											//�迭 ���ۼ�
	for(int ai = 0; ai < sdata.length(); ai++) {
		if(sdata.charAt(ai) == ';') {
			String slist = sdata.substring(aj,ai);		//"���/�̸�" ������ �����б�
			int slash = slist.indexOf('/');
			
			sid[ak] = slist.substring(0,slash);			//���
			sname[ak] = slist.substring(slash+1,slist.length());		//�̸�
			
			aj = ai+1;									//���������
			ak++;
		}	//if
	} //for

	/*****************************************************
	//ȸ��/�μ� ���� ���������� ����� ��������
	*****************************************************/
	String[] itemColumns = {"item","nlist"};
	bean.setTable("CALENDAR_COMMON");
	bean.setColumns(itemColumns);

	//ȸ�� ���� ���� ������ ���
	String csmc_data = "where item='SMC' order by nlist DESC"; 
	bean.setSearchWrite(csmc_data);
	bean.init_write();
	
	CSMC_items = ";";							//ȸ�� ���� ���������� ��� LIST (������ : ';')
	CSMC_NY = "";								//ȸ�� �ű� clear
	if(bean.isEmpty()) { 
		CSMC_NY = "new";						//�űԵ����
		CSMC_items = "";						//���� ���������� ��� ����
	} else {
		while(bean.isAll()) CSMC_items += bean.getData("nlist");
	}	

	//�μ� ���� ���� ������ ���
	String csmd_data = "where item='SMD' order by nlist DESC"; 
	bean.setSearchWrite(csmd_data);
	bean.init_write();
	
	CSMD_items = ";";							//�μ� ���� ���������� ��� LIST (������ : ';')
	CSMD_NY = "";								//�μ� �ű� clear
	if(bean.isEmpty()) { 
		CSMD_NY = "new";						//�űԵ����
		CSMD_items = "";						//���� ���������� ��� ����
	} else {
		while(bean.isAll()) CSMD_items += bean.getData("nlist");
	}	

%>

<HTML><HEAD><TITLE>�޴�</TITLE>
<META http-equiv=Content-Type content="text/html; charset=ks_c_5601-1987">
<LINK href="../css/lmenu.css" type=text/css rel=stylesheet>

<SCRIPT language=JavaScript>
<!--
//���� �޴� ���� ��ũ��Ʈ
    var main_cnt = 6 //�� �޴� ����
     
    function showhide(num)    { 
	  for (i=1; i<=main_cnt; i++)   { 
		  menu=eval("document.all.block"+i+".style"); 

		  if (num==i ) {
			if (menu.display=="block")
			{
				menu.display="none"; 
			}
			else
			{
			  menu.display="block"; 
			}

		  }else { 
			 menu.display="none"; 
		  } 
		} 
	} 
	 
	 var sub_cnt = 0 // ���� �޴��� ������ �ִ� 2�ܰ� ����޴� ����

	 function subshowhide(num)    { 
	  for (i=1; i<=sub_cnt; i++)   { 
		  menu=eval("document.all.subblock"+i+".style"); 
		  if (num==i ) {
			if (menu.display=="block")
			{
				menu.display="none"; 
			}
			else
			{
				menu.display="block"; 
			}

		  }else { 
			 menu.display="none"; 
		  } 
		} 
	 } 

     function subhide(num)    { 
          for (i=1; i<=sub_cnt; i++)   { 
              menu=eval("document.all.subblock"+i+".style"); 
     
              if (num==i) { 
                
                   menu.display="none"; 
         
              } 
          } 
      }	
	 
	 function subhideall()    { 
          for (i=1; i<=sub_cnt; i++)   { 
              menu=eval("document.all.subblock"+i+".style"); 
              menu.display="none";               
          } 
      }	

	 function show(num)    { 
	  for (i=1; i<=main_cnt; i++)   { 
		  menu=eval("document.all.block"+i+".style"); 

		  if (num==i ) {
			
			  menu.display="block"; 			

		  }else { 
			 menu.display="none"; 
		  } 
		} 
	} 

	function subshow(num)    { 
	  for (i=1; i<=sub_cnt; i++)   { 
		  menu=eval("document.all.subblock"+i+".style"); 
		  if (num==i ) {
			
				menu.display="block"; 			

		  }else { 
			 menu.display="none"; 
		  } 
		} 
	} 

	 function selectedtext(one,two)    { 

	   for ( i=1 ; i <= 14 ; i++ )
	   {
    	  menu = eval("document.all.m"+one+"_"+i);
	      if ( menu != null )
		  {
		    menu.style.color="#666666";			
		  }
	   }

	   menu = eval("document.all.m"+one+"_"+two);

	   menu.style.color="#000000";
	   
	} 

	function subselectedtext(one,two,three)    { 

	   for ( i=1 ; i <= 4 ; i++ )
	   {
    	  menu = eval("document.all.sm"+one+"_"+two+"_"+i);
	      if ( menu != null )
		  {
		    menu.style.color="#B1A28D";
		  }
	   }

	   menu = eval("document.all.sm"+one+"_"+two+"_"+three);

	   menu.style.color="#654C33";
	   
	} 
  -->
</SCRIPT>

<SCRIPT language=javascript>
<!--
//������ ������ϱ�
function RefreshMenu() {
	var index = "";
	var form=document.forms[0];
	index = form.Sabun.options[form.Sabun.selectedIndex].value;
	txt	  = form.Sabun.options[form.Sabun.selectedIndex].text;

	var conf = confirm(txt + '���� ������ ���ðڽ��ϱ�?');
	if(conf){
		location.href="CalendarLeft.jsp?Sabun=" + index;
		parent.up.location.href="Calendar_View.jsp?Sabun=" + index;
	}
}

function viewShare()
{
	window.open("CalendarShare.jsp","Info","scrollbars=no,toolbar=no,width=600,height=600");
}

function wopen(url, t, w, h) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh);
}
-->
</SCRIPT>
</HEAD>

<BODY leftMargin=0 background="" topMargin=0 marginheight="0" marginwidth="0">

<!-- ���� �޴� ����-->
<TABLE cellSpacing=0 cellPadding=0 width=175 border=0 background='../images/lm_bg.gif' height='100%' valign='top'>
  <TBODY>
  <TR>
    <TD align='center' height='5' valign='middle' bgcolor='#cccccc'></TD></TR>
  <TR><!--������������ -->
    <TD onmouseover="menu1.src='../images/lm_sch1_over.gif'" 
    style="CURSOR: hand" onclick=showhide(1);subhideall()
    onmouseout="menu1.src='../images/lm_sch1.gif'"><A 
      href="Calendar_View.jsp?FLAG=IND&Sabun=<%=cal_id%>" 
      target="up"><IMG src="../images/lm_sch1.gif" border=0 
      name=menu1></A></TD></TR>
  <TR>
    <TD><SPAN id=block1 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>

  <TR><!--����������� -->
    <TD onmouseover="menu2.src='../images/lm_sch2_over.gif'" 
    style="CURSOR: hand" onclick=showhide(2);subhideall() onmouseout="menu2.src='../images/lm_sch2.gif'"><A 
      href="Calendar_WriteP.jsp?FLAG=INI" 
      target="up"><IMG src="../images/lm_sch2.gif" border=0 
    name=menu2></A></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand"></SPAN></A> </TD></TR>


  <TR><!--������������ -->
    <TD onmouseover="menu3.src='../images/lm_sch3_over.gif'" 
    style="CURSOR: hand" onclick=showhide(3);subhideall();
    onmouseout="menu3.src='../images/lm_sch3.gif'"><A 
      href="javascript:wopen('CalendarShare.jsp', '', 510,467);"><IMG src="../images/lm_sch3.gif" border=0 
      name=menu3></A></TD></TR>
  <TR>
    <TD><SPAN id=block3 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>

<% //�μ� �������� ���� üũ
	String toDay = anbdt.getDate(0);
	String perCom = "";
	StringTokenizer CSMD = new StringTokenizer(CSMD_items,";");

	while(CSMD.hasMoreTokens()) {
		String smd_id=CSMD.nextToken();
		if(smd_id.equals(id)) perCom = "PERMIT_DIV";
	}

	if(perCom.equals("PERMIT_DIV")) {
		//������ ����� �����ڵ� ã��
		String[] divColumn={"id","ac_id"};	
		String item_data = "where id ='"+id+"'";
		bean.setTable("user_table");			
		bean.setColumns(divColumn);
		bean.setSearchWrite(item_data);
		bean.init_write();
		String div_id = "";
		while(bean.isAll()) div_id = bean.getData("ac_id");
		if(div_id == null) div_id = "";
%>
  <TR><!--�μ��������� -->
    <TD onmouseover="menu4.src='../images/lm_sch4_over.gif'" 
    style="CURSOR: hand" onclick=showhide(4);subhideall();selectedtext(4,1) 
    onmouseout="menu4.src='../images/lm_sch4.gif'"><A 
      href="Calendar_divList.jsp?Sabun=<%=div_id%>&Date=<%=toDay%>" 
      target="up"><IMG src="../images/lm_sch4.gif" border=0 
      name=menu4></A></TD></TR>
  <TR>
    <TD><SPAN id=block4 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../images/blank.gif" width=9><IMG src="../images/bullet.gif" 
            align=center> <A onclick=selectedtext(4,1);subhideall() 
            href="Calendar_divList.jsp?Sabun=<%=div_id%>&Date=<%=toDay%>" 
            target="up"><SPAN id=m4_1>�μ���������</SPAN></A> </TD></TR>
        <TR>
          <TD background=../images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../images/blank.gif" width=9><IMG src="../images/bullet.gif" 
            align=center> <A onclick=selectedtext(4,2);subhideall() 
            href="Calendar_WriteD.jsp?FLAG=DIV&Sabun=<%=div_id%>" 
            target="up"><SPAN id=m4_2>�μ��������</SPAN></A> 
      </TD></TR></TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block4 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

<% //ȸ�� �������� ���� üũ
	perCom = "";
	StringTokenizer CSMC = new StringTokenizer(CSMC_items,";");

	while(CSMC.hasMoreTokens()) {
		String smc_id=CSMC.nextToken();
		if(smc_id.equals(id)) perCom = "PERMIT_COM";
	}

	if(perCom.equals("PERMIT_COM")) {
%>	
  <TR><!--ȸ���������� -->
    <TD onmouseover="menu5.src='../images/lm_sch5_over.gif'" 
    style="CURSOR: hand" onclick=showhide(5);subhideall();selectedtext(5,1) 
    onmouseout="menu5.src='../images/lm_sch5.gif'"><A 
      href="Calendar_comList.jsp?Sabun=0&Date=<%=toDay%>" 
      target="up"><IMG src="../images/lm_sch5.gif" border=0 
      name=menu5></A></TD></TR>
  <TR>
    <TD><SPAN id=block5 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../images/blank.gif" width=9><IMG src="../images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,1);subhideall() 
            href="Calendar_comList.jsp?Sabun=0&Date=<%=toDay%>" 
            target="up"><SPAN id=m5_1>ȸ����������</SPAN></A> </TD></TR>
        <TR>
          <TD background=../images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../images/blank.gif" width=9><IMG src="../images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,2);subhideall() 
            href="Calendar_WriteC.jsp?FLAG=COM&Sabun=0" 
            target="up"><SPAN id=m5_2>ȸ���������</SPAN></A>
	</TD></TR></TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block5 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

<%	// ���������� ���� üũ
	String prg_priv = sl.privilege;
	int idx = prg_priv.indexOf("SC01");
	if (idx >= 0){
%>
  <TR><!--������ ���� -->
    <TD onmouseover="menu6.src='../images/lm_mgr_over.gif'" 
    style="CURSOR: hand" onclick=showhide(6);subhideall();selectedtext(6,1) 
    onmouseout="menu6.src='../images/lm_mgr.gif'"><A 
      href="../admin/settingSchedule.jsp" 
      target="up"><IMG src="../images/lm_mgr.gif" border=0 
      name=menu6></A></TD></TR>
  <TR>
    <TD><SPAN id=block6 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../images/blank.gif" width=9><IMG src="../images/bullet.gif" 
            align=center> <A onclick=selectedtext(6,1);subhideall() 
            href="../admin/settingSchedule.jsp" 
            target="up"><SPAN id=m6_1>������/���������ڵ��</SPAN></A> </TD></TR></TBODY></TABLE></SPAN></TD></TR>
<%	}else{	%>
	<SPAN id=block6 style="DISPLAY: none; xCURSOR: hand"></SPAN>
<%	}	%>

    <TR><!--��������� ���� -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0><TR>
	  <TD align='right'><img src='../images/sel_user.gif' border='0'></TD>
	  <TD align='left'><FORM style='margin:0'><SELECT NAME="Sabun" onChange="RefreshMenu()">
<%	
			for(int si = 0; si < shareid_cnt+1; si++) {
				String SEL = "";
				if(cal_id.equals(sid[si])) SEL = "SELECTED";
				else SEL = "";
				out.println("<OPTION value=" + sid[si] + " " + SEL + ">" + sname[si]); 
			}
%>
		</SELECT></FORM></TD></TR></TABLE></TD></TR>

  <TR><!--�ٷΰ��� -->
    <TD align='center' height='1' valign='middle' bgcolor='#ffffff'></TD></TR>

  <TR><!--�ٷΰ��� -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../../js/move_module.js"></SCRIPT></TD></TR>

  <TR>
    <TD height='100%' align='center' valign='top'>
	</TD></TR></TBODY></TABLE></BODY></HTML>

