<%@ include file="../../admin/configPopUp.jsp"%>

<%@ page		
	info= "���������� ����"		
	contentType = "text/html; charset=KSC5601" 		
	import="java.io.*"
	import="java.util.*"
	import="java.sql.*"
	import="com.anbtech.text.Hanguel"
%>
<%@ page import="java.util.StringTokenizer"				%>
<%@	page import="com.anbtech.date.anbDate"				%>
<%@	page import="com.anbtech.text.StringProcess"		%>
<%@	page import="com.anbtech.BoardListBean"		%>
	
<%!
	//�޽��� ���޺���
	String Message="";			//�޽��� ���� ����  

	//������ ������ ������ ���
	String dec_idno="";			//���
	String dec_name="";			//�̸�
	String dec_posi="";			//����
	String dec_divi="";			//�μ�	
	String dec_line="";			//������ ���� 

	//DB�κ��� ���� �����Ͱ� �ִ��� �˾ƺ���
	String isShareid = "";		//�������� �����Ͱ� �ִ���
	String idList = "";			//������ �������� ������ ���
	String[] DBsabun;			//DB�� ������ ���
	String[] DBname;			//DB�� ������ �̸�
	String[] MDsabun;			//������ ������ ���
	String[] MDname;			//������ ������ �̸�
	String PID = "";			//������ȣ
	String DATA = "";			//������ DB����(�߰�)
	String dDATA ="";			//������ DB����(����)

	anbDate anbdt = new com.anbtech.date.anbDate();							//Date�� ���õ� ������
	StringProcess str = new com.anbtech.text.StringProcess();				//����,���ڿ��� ���õ� ������
	BoardListBean bean = new com.anbtech.BoardListBean();			//Query�� ���õ� ������

	/***********************************************************************
	// ���������� �����ϱ�
	***********************************************************************/	
	public boolean saveUser(String share_list,String id,String name){

		System.out.println("�ű� share_list : " + share_list + "<br>");
		System.out.println("�ű� id : " + id + "<br>");
		System.out.println("�ű� name : " + name + "<br>");

		try {
			//����1. ������ ������ �Ҵ��ϱ� ���� ����� �̸����� �����ϱ� (�� ���)
			StringTokenizer shdata = new StringTokenizer(share_list,";");
			int cnt = shdata.countTokens();
			MDsabun = new String[cnt];			//���
			MDname = new String[cnt];			//�̸�
			int i = 0;
			while(shdata.hasMoreTokens()){
				String gdata = shdata.nextToken();
				MDsabun[i] = gdata.substring(0,gdata.indexOf("/"));
				MDname[i] = gdata.substring(gdata.indexOf("/")+1,gdata.length());
				i++;
			} //while

			//���1. ���������ڿ��� �ڽ��� ����������(nlist)�� ����� �ش�.
			for(int bn = 0; bn < cnt; bn++) {
			//�������θ� �Ǵ��Ѵ�.
				String[] shColumns = {"id","item","shareid","nlist"};
				bean.setTable("CALENDAR_COMMON");
				bean.setColumns(shColumns);
				bean.setOrder("id ASC");
				bean.setSearch("id",MDsabun[bn],"item","SID");	
				bean.init_unique();	

				if(bean.isEmpty()) {		//�ű� ����
					String NAlist = id + "/" + name + ";";	//�ڽ��� �����ڿ��� �߰��Ѵ�.
					DATA = "INSERT INTO CALENDAR_COMMON (pid,id,item,nlist) values (";
					DATA += "'" + bean.getID() + bn + "',";
					DATA += "'" + MDsabun[bn] + "',";
					DATA += "'" + "SID" + "',";                  
					DATA += "'" + NAlist + "')";
		System.out.println("�ű� DATA : " + DATA + "<br>");
					try { bean.execute(DATA); Message="INSERT";} catch (Exception e) { System.out.println(e + "<br>");}

				} else {					//�̹� �����ڸ� ��������
					String Alist = ""; 
					while(bean.isAll()) Alist = bean.getData("nlist");	//DB�� �ִ� ������
					String Slist = id + "/" + name + ";";				//�ڽ��� �������ϵ��� ����Ʈ ����

					//�ڽ��� ��ϵǾ� ���������� ����Ѵ�.
					if(Alist.indexOf(Slist) == -1) {		
						Alist += Slist;									//�ڽ��� �߰��Ѵ�.
						DATA = "UPDATE CALENDAR_COMMON set nlist='" + Alist + "' where (id = '";
						DATA += MDsabun[bn] + "') and (item='SID')";
						try { bean.execute(DATA); Message="INSERT";} catch (Exception e) { System.out.println(e + "<br>");}
					} //if
				} //if
			} //for
	
			//���2. �ڽ��� �������� ����� DB�� ��´�. (shareid)
			if(isShareid.equals("YES")) {								//������ ���� ����
				DATA = "UPDATE CALENDAR_COMMON set shareid='" + share_list + "' where pid = '" + PID + "'";
	System.out.println("����������YES UPdate DATA : " + DATA + "<br>");			
				try { bean.execute(DATA); Message="INSERT"; } catch (Exception e) { System.out.println(e);}
			} else if(isShareid.equals("NO")) {							//�űԷ� ���� �����.
				DATA = "INSERT INTO CALENDAR_COMMON (pid,id,item,shareid) values (";
				DATA += "'" + bean.getID() + "',";
				DATA += "'" + id + "',";
				DATA += "'" + "SID" + "',";                  
				DATA += "'" + share_list + "')";
	System.out.println("����������NO UPdate DATA : " + DATA + "<br>");		
				try { bean.execute(DATA); Message="INSERT"; } catch (Exception e) { System.out.println(e);}
			}

			return true;
		}catch (Exception e){
			System.out.println(e);
			return false;
		}
	}


	/***********************************************************************
	// ���������� �����ϱ�
	// select �ڽ��� �����ϰ��� �ϴ� ����� ������ �������� del�� ������ 
	// �������ظ��(shareid)�� �����ϰ�, �������(nlist)������ ����DB�� ������
	// �� ���Ͽ� �����ڸ� ������ܿ��� ���ܽ�Ų��.
	***********************************************************************/	
	public boolean dropUser(String share_list,String id,String name){
		try {
		
			//����1. ������ ������ �Ҵ��ϱ� ���� ����� �̸����� �����ϱ� (�� ���)
			StringTokenizer shdata = new StringTokenizer(share_list,";");
			int cnt = shdata.countTokens();
			MDsabun = new String[cnt];			//���
			MDname = new String[cnt];			//�̸�
			int i = 0;
			while(shdata.hasMoreTokens()){
				String gdata = shdata.nextToken();
				MDsabun[i] = gdata.substring(0,gdata.indexOf("/"));
				MDname[i] = gdata.substring(gdata.indexOf("/")+1,gdata.length());
				i++;
			} //while
	
			//����2. ����DB�� ��Ͽ� ��ϵ� ������ �����ϱ� (�� ����)
			StringTokenizer dbdata = new StringTokenizer(idList,";");
			int dbcnt = dbdata.countTokens();
			DBsabun = new String[dbcnt];		//���
			DBname = new String[dbcnt];			//�̸�
			int dbi = 0;
			while(dbdata.hasMoreTokens()){
				String wasdata = dbdata.nextToken();
				DBsabun[dbi] = wasdata.substring(0,wasdata.indexOf("/"));
				DBname[dbi] = wasdata.substring(wasdata.indexOf("/")+1,wasdata.length());
				dbi++;
			} //while

			//��1/���1. ���������ڿ� ������ �����ڸ� ���Ͽ� ���������� ã�� �ݿ��ϱ�(nlist)
			//���� : DB�� ��ϵ� ����������(idList) ���:������ ������(save)
			String[] shareColumns = {"id","item","shareid","nlist"};
			bean.setTable("CALENDAR_COMMON");
			bean.setColumns(shareColumns);
			bean.setOrder("id ASC");

			String flag="0";
			for(int bn = 0; bn < dbcnt; bn++) {			//����
				String tag = "0";								//clear��Ų��.
				for(int mn = 0; mn < cnt; mn++) {		//��� ���ϱ�
					if(DBsabun[bn].equals(MDsabun[mn])) tag = "1";
				}
				if(tag.equals("0")) {					//������ ������
					//������ ID���� �ڽ��� ���������� �����Ѵ�.
					//out.println("del������ : " + DBsabun[bn] + "/" + DBname[bn] + "<br>");
					bean.setSearch("id",DBsabun[bn],"item","SID");	
					bean.init_unique();	
					if(bean.isEmpty()) {		//skip (������ ������ ����)
					} else {
						String Dlist = ""; 
						while(bean.isAll()) Dlist = bean.getData("nlist");
						String DDlist = id + "/" + name + ";";			//�ڽ��� �����ڿ��� �����Ѵ�.
						String Rlist = str.repWord(Dlist,DDlist,"");
						dDATA = "UPDATE CALENDAR_COMMON set nlist='" + Rlist + "' where (id = '";
						dDATA += DBsabun[bn] + "') and (item='SID')";
						try { bean.execute(dDATA);} catch (Exception e) { System.out.println(e + "<br>");} 
						//out.println("D:" + dDATA + "<br><br><br>");
					}
				} //if
			} //for

			//���2. �ڽ��� �������� ����� DB�� ��´�. 
			//(������������ ����� shareid�� ���)
			if(isShareid.equals("YES")) {								//������ ���� ����
				dDATA = "UPDATE CALENDAR_COMMON set shareid='" + share_list + "' where pid = '" + PID + "'";
				try { bean.execute(dDATA); Message="DELETE";} catch (Exception e) { System.out.println(e);}
			} //if
			return true;
		}catch (Exception e){
			System.out.println(e);
			return false;
		}
	}			
%>

<%
	/*********************************************************************
	 	login �˾ƺ���
	*********************************************************************/
	String id = login_id; 			//������ login id
	String name = login_name;		//������ login �̸� 

	//����,���� ó���ϱ�
	String mode = request.getParameter("mode");
	String shared_list = Hanguel.toHanguel(request.getParameter("share_list"));	//���õ� ������ ����

	if ("save".equals(mode)){
		saveUser(shared_list,id,name);
	}else if ("drop".equals(mode)){
		dropUser(shared_list,id,name);
	}
	
	/***********************************************************************
	// ������ �������� (���/�̸�;���/�̸�;)
	// (ó���� �ѹ� �б�)
	***********************************************************************/
	String get_shareid = "";
	String[] com_dbColumns = {"id","item","shareid","pid"};
	bean.setTable("CALENDAR_COMMON");
	bean.setColumns(com_dbColumns);
	bean.setOrder("shareid ASC");
	bean.setSearch("id",id,"item","SID");	
	bean.init_unique();	
		
	if(bean.isEmpty()) isShareid = "NO";		//����
	else {
		while(bean.isAll()) {
			get_shareid = bean.getData("shareid");
			PID = bean.getData("pid");
		}
		isShareid = "YES";		//�̹� ��ϵ� �����Ͱ� ����.
		idList = get_shareid;	//��ϵ� ������ ��� 
	}
	//ȭ�鿡 ����ϱ�
	dec_line = get_shareid;	
%>


<HTML>
<HEAD>
<META content="text/html;charset=euc-kr" http-equiv=content-type>
<LINK href="../css/style.css" rel=stylesheet>

</HEAD>
<BODY topmargin="0" leftmargin=0 marginwidth=0>

<table border="0" cellpadding="0" cellspacing="0" style="border-collapse: collapse" bordercolor="" width="100%" align='left'>
  <tr>
    <td width="100%" height="25"><font color="#4D91DC"><b>������ ��� ���</b></font></td></tr>
  <tr>
    <td width="100%" height="130" valign="top"><!-- ���� ����Ʈ ����-->
		<form name="aForm" method="post" style="margin:0">
		<select name="dec_app_line" multiple size="8">
		<OPTGROUP label='----------------------'>
			<% 
				//���������� �и��ϱ�
				StringTokenizer STR = new StringTokenizer(dec_line,";");
				while(STR.hasMoreTokens()) {
					String SPS = STR.nextToken()+";";
					out.println("<option value='"+SPS+"'>"+SPS+"</option>");
				}

			%>
				</select></form></td></tr>
  <tr>
	<td width="100%" height="30" align='center'><a href='javascript:delSelected();'><img src='../images/bt_del_sel.gif' border='0'></a></td></tr>
  <tr>
	<td width="100%" height="30" align='center'></td></tr>
  <tr>
	<td width="100%" height="20"><font color="#4D91DC"><b>���� ������ ���� ���</b></font></td></tr>
  <tr><td width="100%" height="100" valign="top" bgcolor="F5F5F5"><font color="565656">
			1.�����ϰ��� �ϴ� ����� �˻�ȭ�鿡��<br>&nbsp;&nbsp;&nbsp;�˻��Ѵ�.<br>
			2.�˻��� ����� �̸��� Ŭ���Ͽ� ���� ��<br>&nbsp;&nbsp;&nbsp;��Ʈ�� �߰��Ѵ�.</font>
</td></tr></table>

</BODY>
</HTML>

<script Language = "Javascript">
<!--

function centerWindow() 
{ 
        var sampleWidth = 640;                        // �������� ���� ������ ���� 
        var sampleHeight = 490;                       // �������� ���� ������ ���� 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
} 

function addUsers(item)
{
    var fromField=item.split("|")
    var type =  fromField[0];
	var user = fromField[1];

	var fromField2=user.split("/");
	var id = fromField2[0];
	var rank = fromField2[1];
	var name = fromField2[2];

	user = id + "/" + name;



	if(type == "div"){
		alert("�μ����� ������ �� �� �����ϴ�.");
		return;
	}

	var to = document.aForm.dec_app_line;
	var ToLen = to.length;
	var Ptr = user.indexOf("/");

	// �ڽ��� ������ �� ���� �Ѵ�.
	if(user.substring(0,Ptr) == '<%=login_id%>') {
		alert('�ڽ��� ������ �� �����ϴ�.');
		return;				
	}
	
	//������ ������� �Ǵ��ϱ�
	for(j=0;j<ToLen;j++) {	
		if(to.options[j].value == user + ";") {
			alert('�̹� ������ ����Դϴ�.');
			return;
		}
	}

	var option0 = new Option(user,user);
	to.options[ToLen] = option0;

	//�Ѱ��� ������ �����
	var data = "";
	for (i = to.length - 1; i >= 0 ;i--) {
		data += to.options[i].text + ";";  
    }
	
	if(user == null) {
		alert("������ ����� ������ �ֽʽÿ�.");
		return;
	}
	
	location.href="ViewShareList.jsp?mode=save&share_list="+data;
}

//������ ������ �����ϱ�
function delSelected()
{
	var to = document.aForm.dec_app_line;
	var len = to.length-1;
	var data = "";
	var nsel = 0;


	//���õ� ������ �����ϱ�(���õ��� ���� ����� �ѱ��.)
	for (i=len; i>=0; i--) {
        if(to.options[i].selected == true)	nsel++;
    }
	if(nsel == 0){
		alert("�������� ������ ����� ������ �ֽʽÿ�.");
		return;
	}

	if(!confirm("�����Ͻðڽ��ϱ�?")) return;

	//���õ� ������ �����ϱ�(���õ��� ���� ����� �ѱ��.)
	for (i=len; i>=0; i--) {
        if(to.options[i].selected == true) {
			nsel++;
			to.options[i] = null;
		} else {
			data += to.options[i].text + ";"; 
		} 
    }

	//������ ���������� �ٽ� �����ֱ�
	dd = data.split(';'); 
	len = dd.length;
	pp = "";
	for(i=1; i<=len; i++) {
		pp += dd[len-i]+";";
	}
	location.href="ViewShareList.jsp?mode=drop&share_list=" + pp;
}
//-->
</script>