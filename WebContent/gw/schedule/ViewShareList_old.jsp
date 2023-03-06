<%@ include file="../../admin/configPopUp.jsp"%>

<%@ page		
	info= "일정공유자 지정"		
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
	//메시지 전달변수
	String Message="";			//메시지 전달 변수  

	//선택한 공유자 변수로 담기
	String dec_idno="";			//사번
	String dec_name="";			//이름
	String dec_posi="";			//직위
	String dec_divi="";			//부서	
	String dec_line="";			//선택한 영역 

	//DB로부터 읽은 데이터가 있는지 알아보기
	String isShareid = "";		//기존공유 데이터가 있는지
	String idList = "";			//기존에 공유해준 공유자 명단
	String[] DBsabun;			//DB내 공유된 사번
	String[] DBname;			//DB내 공유된 이름
	String[] MDsabun;			//수정된 공유된 사번
	String[] MDname;			//수정된 공유된 이름
	String PID = "";			//관리번호
	String DATA = "";			//공유자 DB저장(추가)
	String dDATA ="";			//공유자 DB삭제(삭제)

	anbDate anbdt = new com.anbtech.date.anbDate();							//Date에 관련된 연산자
	StringProcess str = new com.anbtech.text.StringProcess();				//문자,문자열에 관련된 연산자
	BoardListBean bean = new com.anbtech.BoardListBean();			//Query에 관련된 연산자

	/***********************************************************************
	// 일정공유자 저장하기
	***********************************************************************/	
	public boolean saveUser(String share_list,String id,String name){

		System.out.println("신규 share_list : " + share_list + "<br>");
		System.out.println("신규 id : " + id + "<br>");
		System.out.println("신규 name : " + name + "<br>");

		try {
			//분해1. 수정한 공유자 할당하기 위해 사번과 이름으로 분해하기 (비교 대상)
			StringTokenizer shdata = new StringTokenizer(share_list,";");
			int cnt = shdata.countTokens();
			MDsabun = new String[cnt];			//사번
			MDname = new String[cnt];			//이름
			int i = 0;
			while(shdata.hasMoreTokens()){
				String gdata = shdata.nextToken();
				MDsabun[i] = gdata.substring(0,gdata.indexOf("/"));
				MDname[i] = gdata.substring(gdata.indexOf("/")+1,gdata.length());
				i++;
			} //while

			//등록1. 읽은공유자에게 자신을 일정공유자(nlist)로 등록해 준다.
			for(int bn = 0; bn < cnt; bn++) {
			//공유여부를 판단한다.
				String[] shColumns = {"id","item","shareid","nlist"};
				bean.setTable("CALENDAR_COMMON");
				bean.setColumns(shColumns);
				bean.setOrder("id ASC");
				bean.setSearch("id",MDsabun[bn],"item","SID");	
				bean.init_unique();	

				if(bean.isEmpty()) {		//신규 공유
					String NAlist = id + "/" + name + ";";	//자신을 공유자에게 추가한다.
					DATA = "INSERT INTO CALENDAR_COMMON (pid,id,item,nlist) values (";
					DATA += "'" + bean.getID() + bn + "',";
					DATA += "'" + MDsabun[bn] + "',";
					DATA += "'" + "SID" + "',";                  
					DATA += "'" + NAlist + "')";
		System.out.println("신규 DATA : " + DATA + "<br>");
					try { bean.execute(DATA); Message="INSERT";} catch (Exception e) { System.out.println(e + "<br>");}

				} else {					//이미 공유자를 받은상태
					String Alist = ""; 
					while(bean.isAll()) Alist = bean.getData("nlist");	//DB에 있는 공유자
					String Slist = id + "/" + name + ";";				//자신을 공유자하도록 리스트 만듬

					//자신이 등록되어 있지않으며 등록한다.
					if(Alist.indexOf(Slist) == -1) {		
						Alist += Slist;									//자신을 추가한다.
						DATA = "UPDATE CALENDAR_COMMON set nlist='" + Alist + "' where (id = '";
						DATA += MDsabun[bn] + "') and (item='SID')";
						try { bean.execute(DATA); Message="INSERT";} catch (Exception e) { System.out.println(e + "<br>");}
					} //if
				} //if
			} //for
	
			//등록2. 자신이 공유해준 명단을 DB로 담는다. (shareid)
			if(isShareid.equals("YES")) {								//기존에 공유 있음
				DATA = "UPDATE CALENDAR_COMMON set shareid='" + share_list + "' where pid = '" + PID + "'";
	System.out.println("공유해준자YES UPdate DATA : " + DATA + "<br>");			
				try { bean.execute(DATA); Message="INSERT"; } catch (Exception e) { System.out.println(e);}
			} else if(isShareid.equals("NO")) {							//신규로 공유 등록함.
				DATA = "INSERT INTO CALENDAR_COMMON (pid,id,item,shareid) values (";
				DATA += "'" + bean.getID() + "',";
				DATA += "'" + id + "',";
				DATA += "'" + "SID" + "',";                  
				DATA += "'" + share_list + "')";
	System.out.println("공유해준자NO UPdate DATA : " + DATA + "<br>");		
				try { bean.execute(DATA); Message="INSERT"; } catch (Exception e) { System.out.println(e);}
			}

			return true;
		}catch (Exception e){
			System.out.println(e);
			return false;
		}
	}


	/***********************************************************************
	// 일정공유자 삭제하기
	// select 박스중 삭제하고자 하는 명단을 제외한 나머지를 del로 가져와 
	// 공유해준명단(shareid)에 저장하고, 공유명단(nlist)에서는 기존DB의 데이터
	// 와 비교하여 없는자를 공유명단에서 제외시킨다.
	***********************************************************************/	
	public boolean dropUser(String share_list,String id,String name){
		try {
		
			//분해1. 삭제한 공유자 할당하기 위해 사번과 이름으로 분해하기 (비교 대상)
			StringTokenizer shdata = new StringTokenizer(share_list,";");
			int cnt = shdata.countTokens();
			MDsabun = new String[cnt];			//사번
			MDname = new String[cnt];			//이름
			int i = 0;
			while(shdata.hasMoreTokens()){
				String gdata = shdata.nextToken();
				MDsabun[i] = gdata.substring(0,gdata.indexOf("/"));
				MDname[i] = gdata.substring(gdata.indexOf("/")+1,gdata.length());
				i++;
			} //while
	
			//분해2. 기존DB에 등록에 등록된 공유자 분해하기 (비교 기준)
			StringTokenizer dbdata = new StringTokenizer(idList,";");
			int dbcnt = dbdata.countTokens();
			DBsabun = new String[dbcnt];		//사번
			DBname = new String[dbcnt];			//이름
			int dbi = 0;
			while(dbdata.hasMoreTokens()){
				String wasdata = dbdata.nextToken();
				DBsabun[dbi] = wasdata.substring(0,wasdata.indexOf("/"));
				DBname[dbi] = wasdata.substring(wasdata.indexOf("/")+1,wasdata.length());
				dbi++;
			} //while

			//비교1/등록1. 기존공유자와 삭제할 공유자를 비교하여 삭제공유자 찾아 반영하기(nlist)
			//기준 : DB에 등록된 기존공유자(idList) 대상:수정된 공유자(save)
			String[] shareColumns = {"id","item","shareid","nlist"};
			bean.setTable("CALENDAR_COMMON");
			bean.setColumns(shareColumns);
			bean.setOrder("id ASC");

			String flag="0";
			for(int bn = 0; bn < dbcnt; bn++) {			//기준
				String tag = "0";								//clear시킨다.
				for(int mn = 0; mn < cnt; mn++) {		//대상 비교하기
					if(DBsabun[bn].equals(MDsabun[mn])) tag = "1";
				}
				if(tag.equals("0")) {					//삭제된 공유자
					//삭제된 ID에서 자신의 일정공유를 삭제한다.
					//out.println("del삭제된 : " + DBsabun[bn] + "/" + DBname[bn] + "<br>");
					bean.setSearch("id",DBsabun[bn],"item","SID");	
					bean.init_unique();	
					if(bean.isEmpty()) {		//skip (삭제할 내용이 없음)
					} else {
						String Dlist = ""; 
						while(bean.isAll()) Dlist = bean.getData("nlist");
						String DDlist = id + "/" + name + ";";			//자신을 공유자에서 삭제한다.
						String Rlist = str.repWord(Dlist,DDlist,"");
						dDATA = "UPDATE CALENDAR_COMMON set nlist='" + Rlist + "' where (id = '";
						dDATA += DBsabun[bn] + "') and (item='SID')";
						try { bean.execute(dDATA);} catch (Exception e) { System.out.println(e + "<br>");} 
						//out.println("D:" + dDATA + "<br><br><br>");
					}
				} //if
			} //for

			//등록2. 자신이 공유해준 명단을 DB로 담는다. 
			//(내가공유해준 명단을 shareid에 담기)
			if(isShareid.equals("YES")) {								//기존에 공유 있음
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
	 	login 알아보기
	*********************************************************************/
	String id = login_id; 			//접속자 login id
	String name = login_name;		//접속자 login 이름 

	//저장,삭제 처리하기
	String mode = request.getParameter("mode");
	String shared_list = Hanguel.toHanguel(request.getParameter("share_list"));	//선택된 공유자 내용

	if ("save".equals(mode)){
		saveUser(shared_list,id,name);
	}else if ("drop".equals(mode)){
		dropUser(shared_list,id,name);
	}
	
	/***********************************************************************
	// 공유자 가져오기 (사번/이름;사번/이름;)
	// (처음만 한번 읽기)
	***********************************************************************/
	String get_shareid = "";
	String[] com_dbColumns = {"id","item","shareid","pid"};
	bean.setTable("CALENDAR_COMMON");
	bean.setColumns(com_dbColumns);
	bean.setOrder("shareid ASC");
	bean.setSearch("id",id,"item","SID");	
	bean.init_unique();	
		
	if(bean.isEmpty()) isShareid = "NO";		//없음
	else {
		while(bean.isAll()) {
			get_shareid = bean.getData("shareid");
			PID = bean.getData("pid");
		}
		isShareid = "YES";		//이미 등록된 데이터가 있음.
		idList = get_shareid;	//등록된 공유자 명단 
	}
	//화면에 출력하기
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
    <td width="100%" height="25"><font color="#4D91DC"><b>공유된 사원 목록</b></font></td></tr>
  <tr>
    <td width="100%" height="130" valign="top"><!-- 공유 리스트 시작-->
		<form name="aForm" method="post" style="margin:0">
		<select name="dec_app_line" multiple size="8">
		<OPTGROUP label='----------------------'>
			<% 
				//일정공유자 분리하기
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
	<td width="100%" height="20"><font color="#4D91DC"><b>일정 공유자 지정 방법</b></font></td></tr>
  <tr><td width="100%" height="100" valign="top" bgcolor="F5F5F5"><font color="565656">
			1.공유하고자 하는 사원을 검색화면에서<br>&nbsp;&nbsp;&nbsp;검색한다.<br>
			2.검색된 사원의 이름을 클릭하여 공유 리<br>&nbsp;&nbsp;&nbsp;스트에 추가한다.</font>
</td></tr></table>

</BODY>
</HTML>

<script Language = "Javascript">
<!--

function centerWindow() 
{ 
        var sampleWidth = 640;                        // 윈도우의 가로 사이즈 지정 
        var sampleHeight = 490;                       // 윈도우의 세로 사이즈 지정 
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
		alert("부서단위 선택은 할 수 없습니다.");
		return;
	}

	var to = document.aForm.dec_app_line;
	var ToLen = to.length;
	var Ptr = user.indexOf("/");

	// 자신은 공유할 수 없게 한다.
	if(user.substring(0,Ptr) == '<%=login_id%>') {
		alert('자신은 공유할 수 없습니다.');
		return;				
	}
	
	//공유된 사원인지 판단하기
	for(j=0;j<ToLen;j++) {	
		if(to.options[j].value == user + ";") {
			alert('이미 공유된 사원입니다.');
			return;
		}
	}

	var option0 = new Option(user,user);
	to.options[ToLen] = option0;

	//넘겨줄 데이터 만들기
	var data = "";
	for (i = to.length - 1; i >= 0 ;i--) {
		data += to.options[i].text + ";";  
    }
	
	if(user == null) {
		alert("공유할 사원을 선택해 주십시오.");
		return;
	}
	
	location.href="ViewShareList.jsp?mode=save&share_list="+data;
}

//지정된 공유자 삭제하기
function delSelected()
{
	var to = document.aForm.dec_app_line;
	var len = to.length-1;
	var data = "";
	var nsel = 0;


	//선택된 공유자 삭제하기(선택되지 않은 명단을 넘긴다.)
	for (i=len; i>=0; i--) {
        if(to.options[i].selected == true)	nsel++;
    }
	if(nsel == 0){
		alert("공유에서 제외할 사원을 선택해 주십시오.");
		return;
	}

	if(!confirm("삭제하시겠습니까?")) return;

	//선택된 공유자 삭제하기(선택되지 않은 명단을 넘긴다.)
	for (i=len; i>=0; i--) {
        if(to.options[i].selected == true) {
			nsel++;
			to.options[i] = null;
		} else {
			data += to.options[i].text + ";"; 
		} 
    }

	//공유자 지정순으로 다시 돌려주기
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