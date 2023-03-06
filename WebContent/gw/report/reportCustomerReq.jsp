<%@ include file="../../admin/configPopUp.jsp"%>
<%@ 	page		
	info= "고객정보 출력리포트 입력받기"		
	contentType = "text/html; charset=euc-kr" 		
	import="java.io.*"
	import="java.util.*"
	import="com.anbtech.text.Hanguel"	
%>

<html>
<head>
<meta http-equiv="Content-Language" content="ko">
<meta name="GENERATOR" content="Microsoft FrontPage 5.0">
<meta name="ProgId" content="FrontPage.Editor.Document">
<meta http-equiv="Content-Type" content="text/html; charset=ks_c_5601-1987">
<LINK href="../css/style.css" rel=stylesheet>
<title>고객정보 Excel출력</title>
</head>
<BODY leftmargin='0' bgcolor='#F7F7F7' topmargin='0' marginwidth='0' marginheight='0'  onLoad="javascript:centerWindow();">
<table width='100%' border='0' cellspacing='0' cellpadding='0' height='41'>
	<tr>
		<td background='../img/pop_title_bg.gif'><img src='../img/pop_title_excel_print.gif'></td>
	</tr>
</table>
<br>
<table border="0" cellspacing="1" width="98%" height="">
	<form method="post" name="frm1" action="reportCustomerReq.jsp">
   <tr>
    <td width="100%" colspan="2" height="16" align=right>
	<a href="javascript:check()"><img src='../img/004_008_allsel.gif' border=0></a>
	<a href="javascript:reportExcel()"><img src='../img/button_excel_print.gif' border=0></a>
	<a href="javascript:this.close()"><img src='../img/002_013_del.gif' border=0></a>
    </td>

  </tr>
</table>
<table border="0"  width="95%" align="center"><tr><td>출력할 항목을 체크하신 후, 출력 버튼을 누르세요.</td></tr></table>
<table border="1" cellspacing="0" width="95%" align="center">
  <tr>
    <td class='subB' width="18%" height="" align=center>출력항목</td>
    <td width="82%" height="" align=center valign=top>
    <table border="0" cellspacing="5" width="99%" id="AutoNumber2">
      <tr>
        <td width="50%"><input type='checkbox' name='pname' value='pname'>기업명</td>
        <td width="50%"><input type='checkbox' name='dname' value='division'>부서명</td>
      </tr>

      <tr>
        <td width="50%"><input type='checkbox' name='cname' value='name'>고객명</td>
        <td width="50%"><input type='checkbox' name='crank' value='rank'>직 위</td> 
      </tr>

      <tr>
        <td width="50%"><input type='checkbox' name='coffitel' value='office_tel'>전화번호</td>
		<td width="50%"><input type='checkbox' name='cfax' value='fax'>팩 스</td>
      </tr>

      <tr>
        <td width="50%"><input type='checkbox' name='chandtel' value='hand_tel'>핸드폰</td>
		<td width="50%"><input type='checkbox' name='cemail' value='email'>전자우편</td>
      </tr>

      <tr>
        <td width="50%"><input type='checkbox' name='cjob' value='main_job'>담당업무</td>
		<td width="50%"><input type='checkbox' name='cpostno' value='post_no'>우편번호</td>
       </tr>

      <tr>
        <td width="50%"><input type='checkbox' name='caddress' value='address'>회사주소</td>
		<td width="50%"><input type='checkbox' name='ctype' value='customer_type'>당사제품</td>
      </tr>

      <tr>
        <td width="50%"><input type='checkbox' name='chobby' value='hobby'>취 미</td>
		<td width="50%"><input type='checkbox' name='cspec' value='speciality'>특 기</td>
      </tr>

      <tr>
        <td width="50%"><input type='checkbox' name='cweday' value='wedding_day'>결혼기념일</td>
		<td width="50%"><input type='checkbox' name='cbirth' value='birthday'>생 일</td>
      </tr>

      <tr>
        <td width="50%"><input type='checkbox' name='chometel' value='home_tel'>자택전화</td>
		<td width="50%"><input type='checkbox' name='cmemo' value='memo'>기타사항</td>
      </tr>

	  <tr>
        <td width="50%"><input type='checkbox' name='cclass' value='customer_class'>고객유형</td>
		<td width="50%">&nbsp;</td>
      </tr>
	  </form>
    </table>
    </td>
  </tr>
</table>
</body>
</html>

<script language=javascript>
<!--
function centerWindow() 
{ 
        var sampleWidth = 500;                        // 윈도우의 가로 사이즈 지정 
        var sampleHeight = 440;                       // 윈도으이 세로 사이즈 지정 
        window.resizeTo(sampleWidth,sampleHeight); 
        var screenPosX = screen.availWidth/2 - sampleWidth/2; 
        var screenPosY = screen.availHeight/2 - sampleHeight/2; 
        window.moveTo(screenPosX, screenPosY); 
} 
//Excel로 출력하기
function reportExcel() {
	if(document.frm1.pname.checked == true)		pname = document.frm1.pname.value; else pname="";
	if(document.frm1.dname.checked == true)		dname = document.frm1.dname.value; else dname="";
	if(document.frm1.cname.checked == true)		cname = document.frm1.cname.value; else cname="";
	if(document.frm1.crank.checked == true)		crank = document.frm1.crank.value; else crank="";
	if(document.frm1.coffitel.checked == true)	coffitel = document.frm1.coffitel.value; else coffitel="";
	if(document.frm1.cfax.checked == true)		cfax = document.frm1.cfax.value; else cfax="";
	if(document.frm1.chandtel.checked == true)	chandtel = document.frm1.chandtel.value; else chandtel="";
	if(document.frm1.cemail.checked == true)	cemail = document.frm1.cemail.value; else cemail="";
	if(document.frm1.cjob.checked == true)		cjob = document.frm1.cjob.value; else cjob="";
	if(document.frm1.cpostno.checked == true)	cpostno = document.frm1.cpostno.value; else cpostno="";
	if(document.frm1.caddress.checked == true)	caddress = document.frm1.caddress.value; else caddress="";
	if(document.frm1.ctype.checked == true)		ctype = document.frm1.ctype.value; else ctype="";
	if(document.frm1.chobby.checked == true)	chobby = document.frm1.chobby.value; else chobby="";
	if(document.frm1.cspec.checked == true)		cspec = document.frm1.cspec.value; else cspec="";
	if(document.frm1.cweday.checked == true)	cweday = document.frm1.cweday.value; else cweday="";
	if(document.frm1.cbirth.checked == true)	cbirth = document.frm1.cbirth.value; else cbirth="";
	if(document.frm1.chometel.checked == true)	chometel = document.frm1.chometel.value; else chometel="";
	if(document.frm1.cmemo.checked == true)		cmemo = document.frm1.cmemo.value; else cmemo="";
	if(document.frm1.cclass.checked == true)	cclass = document.frm1.cclass.value; else cclass="";
	

	var prg = 'reportCustomerExcel.jsp?pname='+pname+"&dname="+dname+"&cname="+cname;
	prg += "&crank=" + crank + "&coffitel=" + coffitel + "&cfax=" + cfax;
	prg += "&chandtel=" + chandtel + "&cemail=" + cemail + "&cjob=" + cjob;
	prg += "&cpostno=" + cpostno + "&caddress=" + caddress;
	prg += "&ctype=" + ctype;
	prg += "&chobby=" + chobby;
	prg += "&cspec=" + cspec;
	prg += "&cweday=" + cweday;
	prg += "&cbirth=" + cbirth;
	prg += "&chometel=" + chometel;
	prg += "&cmemo=" + cmemo;
	prg += "&cclass=" + cclass;
	
	window.open(prg,'report_view','width=800,height=500,scrollbars=yes,toolbar=no,menubar=yes,status=yes,resizable=yes');

}

var checkflag = false; 
function check() { 
	if (checkflag == false) { 
		document.frm1.pname.checked = true
		document.frm1.dname.checked = true
		document.frm1.cname.checked = true
		document.frm1.crank.checked = true
		document.frm1.coffitel.checked = true
		document.frm1.cfax.checked = true
		document.frm1.chandtel.checked = true
		document.frm1.cemail.checked = true
		document.frm1.cjob.checked = true
		document.frm1.cpostno.checked = true
		document.frm1.caddress.checked = true
		document.frm1.ctype.checked = true
		document.frm1.chobby.checked = true
		document.frm1.cspec.checked = true
		document.frm1.cweday.checked = true
		document.frm1.cbirth.checked = true
		document.frm1.chometel.checked = true
		document.frm1.cmemo.checked = true
		document.frm1.cclass.checked = true
		checkflag = true; 
	}else { 
		document.frm1.pname.checked = false
		document.frm1.dname.checked = false
		document.frm1.cname.checked = false
		document.frm1.crank.checked = false
		document.frm1.coffitel.checked = false
		document.frm1.cfax.checked = false
		document.frm1.chandtel.checked = false
		document.frm1.cemail.checked = false
		document.frm1.cjob.checked = false
		document.frm1.cpostno.checked = false
		document.frm1.caddress.checked = false
		document.frm1.ctype.checked = false
		document.frm1.chobby.checked = false
		document.frm1.cspec.checked = false
		document.frm1.cweday.checked = false
		document.frm1.cbirth.checked = false
		document.frm1.chometel.checked = false
		document.frm1.cmemo.checked = false
		document.frm1.cclass.checked = true
		checkflag = false; 
	} 
} 
-->
</script>
