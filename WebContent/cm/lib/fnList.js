//�Խ��� ����Ʈ �޴� �׼�

	function fnAttach() { //÷�κ���
		Top = (window.screen.height - 200)/2;	
		Left = (window.screen.width - 250)/2;

		window.open("./wndAttachFile.asp?SortCD=2&Seq=" + objID, "", "scrollbars=0,width=400,height=250,top=" + Top + ",left=" + Left);
	}

	function fnRefresh(){
		document.frmList.submit();
	}

	function go_sort(param) {		
	
		if(strSort == param)
			strOrder = strOrder * -1
		else
			strOrder = 1
		
		document.frmList.Sort.value = param;
		document.frmList.Order.value = strOrder;
		
		document.frmList.submit();
	}

	function fnChangePage(page) {
	  document.frmList.strCurrPage.value = page;
	  document.all.frmList.submit();
	}
	
	function fnMovePage(str,Total){
		if(window.event.keyCode == 13){
			if (isNaN(str) == true ){
				alert("���ڸ� �Է� �����մϴ�.");
			}
			else if(str>Total || str<1 ){
				alert("Page ������ ����ϴ�.");
			}	
			else{
				document.frmList.strCurrPage.value = str;
				document.all.frmList.submit();
			}
		}
		else{
			return;
		}
	}

	function openNew(param) {
	        var winHeight = window.screen.availHeight;
	        var winWidth = window.screen.availWidth;

		//Layer2.style.visibility = "hidden";
		if (param == 1) { //���ۼ�
			window.open("./BbsWrite.asp?strClass="+ objClass +"&BoardCD="+ objBoardCD +"&BoardName="+ objBoardName,"","scrollbars=no,resizable=1,left=0, top=0,width=" + newWindowWidth + ",height=" + newWindowHeigth);
		}
		else{ //���ϰԽ�
			var Top = (window.screen.height - 300)/2;
			var Left = (window.screen.width - 350)/2;
        
			window.open("./BbsWriteAttach.asp?strClass="+ objClass +"&BoardCD="+ objBoardCD +"&BoardName="+ objBoardName,"","scrollbars=0,resizable=1,top=" + Top + ",left=" + Left + ",width=350,height=300")
		}
	}

	function fnPreView(it){
		if (it.checked == true)
			parent.Left.document.frmData.hidPreview.value= "on" ;
		else
			parent.Left.document.frmData.hidPreview.value= "off" ;
		document.all.frmList.submit();
	}

	function fnDel(){
		var chkCnt;
		var strSeq = "";
		var obj;
		var checkedObj;		
		var selectCount = 0;
		//alert(document.frmList.AuthDel1.disabled);
		for(i = 1 ; i <= totalCount;i++) {
			
			checkedObj = eval("document.frmList.chkDel"+ i);			
			if (checkedObj.checked) {
				selectCount = selectCount + 1;
				obj = eval("document.frmList.AuthDel"+ i +".disabled");
				if (obj) {					
					alert("������ ���� �޼����� �־ ������ �� �����ϴ�.");
					return;
				}
				else {		
					strSeq = checkedObj.value +";"+ strSeq ;
				}
			}
			
		}
		if ( selectCount == 0 ) 
			alert("������ �޼����� �����ϼ���");
		else {
			if(confirm("�����Ͻðڽ��ϱ�?"))				
				ifrmTask.location.href = "./BbsMultiDel.asp?CompanyCD="+objCompanyCD+"&strChecked="+ strSeq;
			else
				return;
		}
	}

	function fnSearch(){
		Top = (window.screen.height - 300)/2;
		Left = (window.screen.width - 250)/2;
		window.open("./wndSearch.asp","wndSearch","width=300, height=260, top=" + Top + ", left=" + Left);
	}

	function fnSearchAct(){
		document.frmList.hidSrchFlag.value = "S";
		document.frmList.submit();
	}
	
