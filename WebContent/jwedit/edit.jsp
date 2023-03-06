<%@ page contentType="text/html;charset=MS949"%>

<html>
<head>
<title></title>
<META HTTP-EQUIV="Content-type" CONTENT="text/html;charset=ksc5601">
<script language=javascript>

	// JWSoft의 웹편집기 크기를 조절합니다.
	function resizeEditor(x, y) {
		document.all.jw.width = Number(document.all.jw.width) + x;
		document.all.jw.height = Number(document.all.jw.height) + y;
	}

	function checkData() {
		//제목
		if (document.mailForm.title.value=='') {
			alert("제목을 입력하여 주십시요.");
			return false;
		}

		//이름
		if (document.mailForm.name.value=='') {
			alert("이름을 입력하여 주십시요.");
			return false;
		}
		
		//암호
		if (document.mailForm.pw.value=='') {
			alert("암호를 입력하여 주십시요.");
			return false;
		}
	
		///////////////////////////////////////////////////////////////////////////////////
		// 이미지 + 배경 이미지 처리	
		var jww = document.mailForm;
		
		//IMAGE SEND
		jww.jw.ImgFileReadUrl = "http://test.diane.pe.kr/jwedit/upload_img/";
		
		if (jww.jw.HttpSendImg("/jwedit/upload.jsp") < 0) {
			alert(jww.jw.GetHTTPErrText());
			return false;
		}
			
		document.mailForm.htmlBODY.value = document.mailForm.jw.AllHtml;	

		return true;
	}

	function sendData() {
		if (checkData()) {
			document.mailForm.submit();
			return true;
		}
		return false;
	}
</script>
</head>
<body>
  <form method="post" action="input.jsp" name="mailForm">
	<input type="hidden" name="testvalue" value="and32">
    <textarea rows="2" name="htmlBODY" cols="20" style="display:none"></textarea>          

    <table border="0" width="600" height="103" cellspacing="0" cellpadding="0" bordercolorlight="#000000" bordercolordark="#FFFFFF" style="border: 1 solid #000000">
      <tr>
		<td height="22">
		  <table width=600 bgcolor="#EEEEEE">
		    <tr height=24>
              <td width="80" align="center">제&nbsp; 목</td>
              <td width="400" colspan="4" align='left'>
			    <input type="text" name="title" size="55" style="background-color: #EFEFEF; border: 1 solid #808080">
			  </td>
			  <td width=120></td>
			</tr>
		  </table>
		</td>
      </tr>
      <tr>
		<td height="22">
		  <table width=600 bgcolor="#EEEEEE">
		    <tr>
		      <td width="80" height="22" align="center">작성자</td>
              <td width="200" height="22" align='left'>
			    <input type="text" name="name" size="14" style="background-color: #EFEFEF; border: 1 solid #808080">
			  </td>
              <td width="80" height="22" align="center">암호</td>
              <td width="100" height="22" align='left'>
			    <input type="password" name="pw" size="4" maxlength=4 style="background-color: #EFEFEF; border: 1 solid #808080">
			  </td>
			  <td width=140></td>
		    </tr>
		  </table>
		</td>
      </tr>
      <tr>
        <td width="572" height="31" colspan="5" align="center" bgcolor="#F7F7F7" valign="top">
          <OBJECT CLASSID="clsid:5220cb21-c88d-11cf-b347-00aa00a28331">
            <PARAM NAME="LPKPath" VALUE="./jwWebEdit_banner.lpk">
          </OBJECT>　 
          <OBJECT id=jw codeBase="./jwWebEdit_banner.cab#version=1,0,0,55"classid="clsid:3D91F786-09A0-4347-AE67-01FA9480CD50" width=600 height=480 VIEWTEXT>
		    <!-- BaseUrl 파라미터는 게시물을 수정할때 사용합니다. 자세한 사항은 JWSoft의 매뉴얼을 참고하시기 바랍니다. -->
            <!-- <param name="BaseUrl" value="http://www.jwsoft.co.kr"> -->
            <param name="FontName" value="굴림">
            <param name="FontSize" value="10">
            <param name="HttpServerName" value="test.diane.pe.kr">
            <param name="HttpPORT" value="80">
            <param name="HttpSendingCancelBut" value=true>
          </OBJECT>
        </td>
      </tr>
  </form>
      <tr>
		<td height="22" bgcolor="#F7F7F7">
		  <table width=100% bgcolor="#F7F7F7">
		    <tr>
			  <td>
			    <font size="2" color="#336699">편집창 크기조절</font>&nbsp;
                <input type=button value='가로-' onClick="javascript:resizeEditor(-100,0);">
                <input type=button value='세로-' onClick="javascript:resizeEditor(0,-100);">
                <input type=button value='가로+' onClick="javascript:resizeEditor(100,0);">
                <input type=button value='세로+' onClick="javascript:resizeEditor(0,100);">
		      </td>
              <td><input type="button" value="저 장" name="B1" onClick="javascript:sendData();"></td>
			</tr>
		  </table>
		</td>
      </tr>
    </table>   　      
</body>