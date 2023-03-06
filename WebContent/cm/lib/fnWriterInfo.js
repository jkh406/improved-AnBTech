//작성자 정보창
	function	ShowWriterInfo(param){
		var oparam = "../mail/gw_mail_getUserInfo.asp?type=pop&ID=" + param;
		Top = (window.screen.height - 430)/2;
		Left = (window.screen.width - 650)/2;
		window.open(oparam, "", "width=650, height=430, top=" + Top + ", left=" + Left);
	}