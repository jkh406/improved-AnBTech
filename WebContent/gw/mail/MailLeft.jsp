<%@ include file="../../admin/configHead.jsp"%>
<%@ page		
	info= "���ΰ��� ����"		
	contentType = "text/html; charset=KSC5601" 		
	errorPage	= "../../admin/errorpage.jsp" 
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
%>
<%@	page import="com.anbtech.text.Hanguel"	%>
<%@	page import="com.anbtech.date.anbDate"	%>
<%@	page import="com.anbtech.text.StringProcess"	%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.BoardListBean" />

	
<%

	//�޽��� ���޺���
	String Message="";			//�޽��� ���� ����  

	String id = "";				//������ id
	String passwd="";			//������ ��й�ȣ
	String name = "";			//������ �̸�
	String division = "";			//������ �μ���	
	String PROCESS = "REC_ING";		//���������� ����ޱ�
	String PROCESS_NAME = "��������";	//���������� �̸�����

	//������DB Columns
	String[] masterColumns = {"pid","post_subj","writer_id","writer_name","write_date","post_receiver","isopen","open_date","post_state","post_select","bon_path","bon_file","add_1_file","add_2_file","add_3_file","delete_date"};

	//������ DB Columns
	String[] wasteColumns = {"pid","post_subj","writer_id","writer_name","write_date","post_receiver","isopen","open_date","post_state","post_select","bon_path","bon_file","add_1_file","add_2_file","add_3_file","delete_date"};
	
	//LETTER DB Columns
	String[] letterColumns= {"pid","post_subj","writer_id","writer_name","write_date","post_receiver","isopen","open_date","post_select","delete_date"};

	//���� ���޺���
	int REC_CNT = 0;		//�������� ����
	int NEW_REC_CNT = 0;	//�������� �� ������ ����

	int SND_CNT = 0;		//�������� ����
	int TMP_CNT = 0;		//�������� ����
	int TOT_CNT = 0;		//������� ����
	int WST_CNT = 0;		//������ ����

	//������ LINEã��
	int count = 0;			//line ��ȣ
	String ALL_SEL = "";		//��ü�����ϱ� 
	String ALL_CHG = "";		//��ü�����ϱ� ������ �ٲ��ֱ�

	//��޼��������� ���� ó���ϱ�
	String cfm = "";		//����Ȯ�� ��û
	String sec = "";		//������� ��û
	String rsp = "";		//ȸ�ſ�� ��û

	String sendConfirm="";		//���Ȯ�� �����Ͽ� ������������ �����ֱ�

	/********************************************************************
		new ������ �����ϱ�
	*********************************************************************/
	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	StringProcess str = new com.anbtech.text.StringProcess();				//����,���ڿ��� ���õ� ������

	id = login_id; 		//������ login id
	String[] idColumn = {"a.id","a.passwd","a.name","b.ac_name"};
	bean.setTable("user_table a,class_table b");			//EBOM Master Table List
	bean.setColumns(idColumn);
	bean.setOrder("a.id ASC");	

	String item_data = "where (a.id ='"+id+"' and a.ac_id = b.ac_id)";
	bean.setSearchWrite(item_data);
	bean.init_write();

	bean.isAll();
	name = bean.getData("name");			//������ ��
	division = bean.getData("ac_name");	//������ �μ���
	passwd = bean.getData("passwd");		//������ ��й�ȣ

	/*********************************************************************
	 	�������� ������ ���Ѵ�.
	*********************************************************************/

	/***********************************************************************
	���������� ���� LIST UP 		
	***********************************************************************/	
	REC_CNT = 0 ;		//������ �ʱ�ȭ
	bean.setTable("POST_LETTER");
	bean.setColumns(letterColumns);
	bean.setOrder("pid DESC");
	bean.setClear();
	bean.setSearch("post_receiver",id);
	bean.init_unique();
	REC_CNT = bean.getTotalcount();		

	NEW_REC_CNT = 0;	//������ ����
	bean.setSearch("post_receiver",id,"isopen","0");
	bean.init_unique();
	NEW_REC_CNT = bean.getTotalcount();
	/***********************************************************************
	���������� ���� LIST UP 		
	***********************************************************************/	
	SND_CNT = 0 ;		//������ �ʱ�ȭ
	bean.setTable("POST_MASTER");
	bean.setColumns(masterColumns);
	String query = "where writer_id ='"+id+"' and ((post_state = 'SND') or (post_state = 'email'))";
	bean.setSearchWrite(query);
	bean.init_write();
	SND_CNT = bean.getTotalcount();
		
	/***********************************************************************
	���������� ���� LIST UP 		
	***********************************************************************/	
	TMP_CNT = 0 ;		//������ �ʱ�ȭ
	bean.setTable("POST_MASTER");
	bean.setColumns(masterColumns);
	bean.setOrder("pid DESC");
	bean.setClear();
	bean.setSearch("post_state","TMP","writer_id",id);
	bean.init_unique();
	TMP_CNT = bean.getTotalcount();
		
	/***********************************************************************
	������ ���� LIST UP 		
	***********************************************************************/	
	WST_CNT = 0 ;		//������ �ʱ�ȭ
	bean.setTable("POST_WASTE");
	bean.setColumns(wasteColumns);
	bean.setClear();
	bean.setSearch("post_receiver",id);
	bean.init_unique();
	WST_CNT = bean.getTotalcount();		


%>

<HTML><HEAD><TITLE>�޴�</TITLE>
<META http-equiv=Content-Type content="text/html; charset=ks_c_5601-1987">
<LINK href="../css/lmenu.css" type=text/css rel=stylesheet>

<SCRIPT language=JavaScript>
<!--
//���� �޴� ���� ��ũ��Ʈ
    var main_cnt = 5 //�� �޴� ����
     
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
function wopen(url, t, w, h,st) {
	var sw;
	var sh;

	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

//email�������� 
function getEmail()
{
	RES = confirm("���ڸ����� �������ðڽ��ϱ�? ��Ʈ�� ������ ���� �ð��� ���� �ɸ��ų� ������ �� �ֽ��ϴ�.");
	if(RES)
	wopen('getEmail.jsp?INI=','getEmail','480','275','scrollbars=no,toolbar=no,status=no,resizable=no');
}

//email������
function sendEmail()
{
	wopen('sendEmail.jsp?INI=','sendEmail','700','650','scrollbars=yes,toolbar=no,status=no,resizable=yes');
}



//email ȯ�� setting
function EmailEnv()
{
	wopen('EmailMain.jsp?INI=','EmailMain','450','275','scrollbars=no,toolbar=no,status=no,resizable=no');
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
  <TR><!--���������� -->
    <TD onmouseover="menu1.src='../images/lm_mail1_over.gif'" 
    style="CURSOR: hand" onclick=showhide(1);subhideall()
    onmouseout="menu1.src='../images/lm_mail1.gif'"><A 
      href="post_main.jsp?ORDER=REC_ING" target="up"><IMG src="../images/lm_mail1.gif" border=0 
      name=menu1></A><DIV id=Layer1 style="Z-INDEX: 1; LEFT: 90px; POSITION: absolute; TOP: 13px;"><FONT SIZE='2' COLOR= '#565656'>(<%=NEW_REC_CNT%>/<%=REC_CNT%>)</FONT></DIV></TD></TR>
  <TR>
    <TD><SPAN id=block1 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>

  <TR><!--���������� -->
    <TD onmouseover="menu2.src='../images/lm_mail2_over.gif'" 
    style="CURSOR: hand" onclick=showhide(2);subhideall() onmouseout="menu2.src='../images/lm_mail2.gif'"><A 
      href="post_main.jsp?ORDER=SND_ING" target="up"><IMG src="../images/lm_mail2.gif" border=0 
    name=menu2></A><DIV id=Layer1 style="Z-INDEX: 1; LEFT: 90px; POSITION: absolute; TOP: 41px;"><FONT SIZE='2' COLOR= '#565656'>(<%=SND_CNT%>)</FONT></DIV></TD></TR>
  <TR>
    <TD><SPAN id=block2 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>


  <TR><!--���������� -->
    <TD onmouseover="menu3.src='../images/lm_mail3_over.gif'" 
    style="CURSOR: hand" onclick=showhide(3);subhideall();
    onmouseout="menu3.src='../images/lm_mail3.gif'"><A 
      href="post_main.jsp?ORDER=TMP_ING" target="up"><IMG src="../images/lm_mail3.gif" border=0 
      name=menu3></A><DIV id=Layer1 style="Z-INDEX: 1; LEFT: 90px; POSITION: absolute; TOP: 69px;"><FONT SIZE='2' COLOR= '#565656'>(<%=TMP_CNT%>)</FONT></DIV></TD></TR>
  <TR>
    <TD><SPAN id=block3 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>

  <TR><!--���������� -->
    <TD onmouseover="menu4.src='../images/lm_mail4_over.gif'" 
    style="CURSOR: hand" onclick=showhide(4);subhideall();
    onmouseout="menu4.src='../images/lm_mail4.gif'"><A 
      href="post_main.jsp?ORDER=WST_ING" target="up"><IMG src="../images/lm_mail4.gif" border=0 
      name=menu4></A><DIV id=Layer1 style="Z-INDEX: 1; LEFT: 90px; POSITION: absolute; TOP: 97px;"><FONT SIZE='2' COLOR= '#565656'>(<%=WST_CNT%>)</FONT></DIV></TD></TR>
  <TR>
    <TD><SPAN id=block4 style="DISPLAY: none; xCURSOR: hand"></SPAN></TD></TR>

  <!--��ܿ����� -->
  <TR>
    <TD onmouseover="menu5.src='../images/lm_mail5_over.gif'" 
    style="CURSOR: hand" onclick=showhide(5);subhideall();selectedtext(5,1) 
    onmouseout="menu5.src='../images/lm_mail5.gif'"><IMG src="../images/lm_mail5.gif" border=0 
      name=menu5></TD></TR>
  <TR>
    <TD><SPAN id=block5 style="DISPLAY: none; xCURSOR: hand">
      <TABLE borderColor=#ffffff cellSpacing=0 cellPadding=0 width="100%" 
      border=0>
        <TBODY>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../images/blank.gif" width=9><IMG src="../images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,1);subhideall() 
            href="javascript:sendEmail()"><SPAN id=m5_1>�������ۼ�</SPAN></A></TD></TR>
        <TR>
          <TD background=../images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../images/blank.gif" width=9><IMG src="../images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,2);subhideall() 
            href="javascript:getEmail()"><SPAN id=m5_2>�ܺ���������</SPAN></A></TD></TR>
        <TR>
          <TD background=../images/left_hline.gif height=1></TD></TR>
        <TR bgColor=#f3f3f3 height=22>
          <TD class=type1 vAlign=center align=left><IMG 
            src="../images/blank.gif" width=9><IMG src="../images/bullet.gif" 
            align=center> <A onclick=selectedtext(5,3);subhideall() 
            href="javascript:EmailEnv()"><SPAN id=m5_3>POP��������</SPAN></A>
		</TD></TR></TBODY></TABLE></SPAN></TD></TR>

  <TR><!--�ٷΰ��� -->
    <TD align='center' height='30' valign='middle' bgcolor='#cccccc'>
	<SCRIPT LANGUAGE="JavaScript" SRC="../../js/move_module.js"></SCRIPT></TD></TR>

  <TR>
    <TD height='100%' align='center' valign='top'>&nbsp;</TD></TR></TBODY></TABLE></BODY></HTML>

