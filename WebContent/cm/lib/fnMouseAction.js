// 게시판 리스트에서 마우스 액션에 따른 함수들

	function fnDblClick(seq,AttachFlag,i,AttachCnt) {
		
		if (AttachFlag == "1" || AttachCnt > 1){ //글보기
		        var winHeight = window.screen.availHeight;
	        	var winWidth = window.screen.availWidth;
			var url;
			url = "./ParamTransmit.asp?strClass="+ objClass +"&Empower="+ objEmpower +"&BoardCD="+ objBoardCD;
			url = url + "&Seq="+ seq +"&RootCD="+ objRootCD +"&StatusNo="+ strStatusNo +"&BoardName="+ objBoardName + "&FolderAdmin="+ objFolderAdmin;
			
			window.open( url,"","scrollbars=0,resizable=1,top=0,left=0,width=" + newWindowWidth + ",height=" + newWindowHeigth);
		}
		else{
			Top = (window.screen.height - 180)/2;	
			Left = (window.screen.width - 250)/2;
			
			//alert("../Include/AttachOneOpen.asp?SortCD=2&seq=" + seq);
			ifrmTask.location.href="../Include/AttachOneOpen.asp?SortCD=2&seq=" + seq ;
		}
		document.frmList.hidI.value = i;
	}
	
	function ModifyCont(seq)	{
		var url;
		url = "./BbsModForm.asp?Seq="+ seq +"&BoardCD="+ objBoardCD +"&BoardName="+ objBoardName + "&hidBoardName=" + objBoardName + "&FromList=true";
		window.open( url,"","scrollbars=0,resizable=1,top=0,left=0,width=" + newWindowWidth + ",height=" + newWindowHeigth);
	}
	
	var preColor = "#ffffff";
	var preparam;
	
	function fnMousedown(seq,it,intAttCnt,WriterID,i,Nameless,BoardCD,CompanyCD,AttachFlag,Title,CurUID){
		document.frmList.hidI.value = i;
		
		objID = seq;
		objWriter = WriterID;
		objNameless = Nameless;
		objBoardCD  = BoardCD;
		objCompanyCD = CompanyCD;
		objAttachFlag = AttachFlag;
		objIndex = i;
		objAttachCnt = intAttCnt;
		objTitle = Title;
	
		
		if (intAttCnt > 0){ //첨부
			spnAttach_off.style.display = "none";
			spnAttach_on.style.display = "";
		}
		else{
			spnAttach_on.style.display = "none";
			spnAttach_off.style.display = "";
		}

		if (preparam == null){ //색변환
			preparam = it;
		}
		else  {
			preparam.style.backgroundColor = preColor;
			preparam = it;
		}
		it.style.backgroundColor = "#98FFFF";

		if ((event.button==2) || (event.button==3)) { //메뉴보이기
			
			//if (showing == true) {
			//	showing = false; menu.hide();
			//} else if (showing == false ) {
		  	if (showing == true) {showing = false; menu.hide()}
		  	if (showing == false) {
 	
				var xNow=event.x + 10; 
				var yNow=event.y - 57;
				menu.moveTo(xNow, yNow);
				menu.show();
				showing = true;
				
			}
			
			checkeditem = 0;	
			
			for(i = 2 ; i< 2 + (totalCount*2);i++) {
				if (document.frmList.elements[i].checked) {
		 				checkeditem = checkeditem + 1;	 				
				}
			}
			
			if(checkeditem > 1) 
			{
				for ( i = 1; i< ItemInMenu; i++){
					obj = eval("input" + i + "Div");
					obj.disabled = true;
					obj.style.cursor = "default";
				}			

				obj = eval("input1Div");					
				obj.disabled = false;
				obj.style.cursor = "auto";


			} else {
				for ( i = 1; i< ItemInMenu; i++){
					obj = eval("input" + i + "Div");
					obj.disabled = false;
					obj.style.cursor = "auto";					
				}				
			}	

			if( ( WriterID != CurUID ) && (objFolderAdmin != "Y") )	{
				obj = eval("input" + 6 + "Div");
				obj.disabled = true;
				obj.style.cursor = "default";	
			} else	{
				obj = eval("input" + 6 + "Div");
				obj.disabled = false;
				obj.style.cursor = "auto";
			}
			
	
			/*	
			if (objNameless == '1'){
				var strLinkReply = "&nbsp;&nbsp;&nbsp;&nbsp;<font color='#808080'>>회신</font><br>";
				var strLinkUserinfo = "&nbsp;&nbsp;&nbsp;&nbsp;<font color='#808080'>>등록자정보</font><br>";
			}
			else{
				var strLinkReply = "&nbsp;&nbsp;&nbsp;&nbsp;<a href='JavaScript:fnMenuAct(1);'>>회신</a><br>";
				var strLinkUserinfo = "&nbsp;&nbsp;&nbsp;&nbsp;<a href='JavaScript:fnMenuAct(4);'>>등록자정보</a><br>";
			}
			spnMenu1.innerHTML = strLinkReply ;
			spnMenu4.innerHTML = strLinkUserinfo ;
			
			lyrMenu.m_status = "on";
			lyrMenu.style.left = event.clientX + 10;
			lyrMenu.style.top  = event.clientY - 50;
			lyrMenu.style.visibility = "visible";*/
			
		}
		else { //미리보기
			
		  if (showing == true) {showing = false; menu.hide()}
		/*	if (lyrMenu.style.visibility == "visible"){
				lyrMenu.style.visibility = "hidden";
				lyrMenu.m_status = "off";
			}*/
			if(document.frmList.chkPreview.checked){
				ifrmPreview.location.href = "./preView.asp?Seq="+ seq ;
			}
		}
	}

	function fnMenuAct(param1,param) {
		showing = false; 
        menu.hide();
	    var winHeight = window.screen.availHeight;
	    var winWidth = window.screen.availWidth;
	    
		if (param == 0) { //이동/복사
			var strSeq = "";
			var obj;
			var checkedObj;		
			var selectCount = 0;	
			var CanNotMove = "N";	
			
				
			for(i = 1 ; i <= totalCount;i++) {
				
				checkedObj = eval("document.frmList.chkDel"+ i);			
				if (checkedObj.checked) {
					selectCount = selectCount + 1;									
					obj = eval("document.frmList.AuthDel"+ i +".disabled");
					if (obj) {					
						CanNotMove = "Y";					
					}							
					strSeq = checkedObj.value +";"+ strSeq ;
					
				}				
			}
			
			if ( selectCount == 0 ) {
				
				obj = eval("document.frmList.AuthDel"+ objIndex +".disabled");
				if (obj) {					
						CanNotMove = "Y";					
				}
				checkedObj = eval("document.frmList.chkDel"+ objIndex);
				strSeq = checkedObj.value+";";
			}	
								
			reparam = "./BbsCopyMove.asp?CompanyCD="+objCompanyCD+"&CanNotMove="+CanNotMove+"&strChecked="+strSeq;						
			
			Top = (window.screen.height - 250)/2;
	                Left = (window.screen.width - 260)/2;	                        
	                window.open(reparam, "", "scrollbars=no, width=280, height=340, top=" + Top + ", left=" + Left);          
	        
		}
		else if (param == 1) { //로컬저장
			var listID = "";
			var test = 0;		

			if ( checkeditem > 1 ) {        	

				for(i = 1 ; i <= totalCount;i++) {
				
					checkedObj = eval("document.frmList.chkDel"+ i);			
					if (checkedObj.checked) {
						test  = test + 1;									
						listID = listID  + checkedObj.value +";";
					}				
				}

				if ( test > 0 ) {
					frmList.ocxPlAtt.DoSaveLocal(listID, "pcm");

		        	//showing = true; 
				//menu.show();
				//return;
				}
			}
			else {		
				frmList.ocxPlAtt.objDataURL = objID;
				frmList.ocxPlAtt.objPostURL = objBoardCD;
				frmList.ocxPlAtt.objComURL = objCompanyCD;
				frmList.ocxPlAtt.objClsURL = objClass;
	
				reval = frmList.ocxPlAtt.LocalSave(objTitle);
			}
		}
		if (param == 2){ //회신
			if ( checkeditem > 1 ) {        	
	        	showing = true; 
				menu.show();
				return;
			}
			else {
				window.open("../forms3/ipm/note/gw_board_mail.asp?strClass="+ objClass +"&flag=R&Seq="+ objID +"&CompanyCD="+ objCompanyCD +"&BoardCD="+ objBoardCD + "&BoardName="+ objBoardName +"&szType=BOARD" ,"winWrite","scrollbars=no,resizable=1,left=0, top=0,width=" + newWindowWidth + ",height=" + newWindowHeigth);
			}
		}
		else if (param == 3){ //전달
			if ( checkeditem > 1 ) {        	
	        	showing = true; 
				menu.show();
				return;
			}
			else {
			window.open("../forms3/ipm/note/gw_board_mail.asp?strClass="+ objClass +"&flag=F&Seq="+ objID +"&CompanyCD="+ objCompanyCD +"&BoardCD="+ objBoardCD + "&BoardName="+ objBoardName +"&szType=BOARD","winWrite","scrollbars=no,resizable=1,left=0, top=0,width=" + newWindowWidth + ",height=" + newWindowHeigth);
			}
		}		
		else if (param == 4) { //회신게시
			if ( checkeditem > 1 ) {        	
	        	showing = true; 
				menu.show();
				return;
			}
			else {
				window.open("./BbsWrite.asp?strClass="+ objClass +"&Seq="+ objID +"&CompanyCD="+ objCompanyCD +"&BoardCD="+ objBoardCD +"&BoardName="+ objBoardName,"winWrite","scrollbars=no,resizable=1,left=0, top=0,width=" + newWindowWidth + ",height=" + newWindowHeigth);
			}
		}
		else if (param == 5) { //게시자정보
			if ( checkeditem > 1 ) {        	
	        	showing = true; 
			menu.show();
			return;
			}
			else {
				if (objNameless == '1'){
					alert("등록자정보를 볼 수 없습니다.");
				}
				else{
					ShowWriterInfo(objWriter);
				}
			}
		}
/*		else if (param == 5) { //읽기	
			if ( checkeditem > 1 ) {        	
	        	showing = true; 
			menu.show();
			return;
			}
			else {		
					fnDblClick(objID,objAttachFlag,objIndex,objAttachCnt,objBoardCD,objCompanyCD);
			}
		}
*/		
		else if (param == 6) { //수정
			if ( checkeditem > 1 ) {        	
	        			showing = true; 
				menu.show();
				return;
			}
			else {
				obj = eval("input" + 6 + "Div");
				if(!(obj.disabled == true))
					ModifyCont(objID);
			}
		}
		else {
			showing = false; 
			menu.hide();
			return;
		}
		//lyrMenu.style.visibility = "hidden";
	}
	
	var preover;
	function fnOver(param) {       
	   if (preover == null )  {
	        preover = param;
	   }
	   else {
	        preover.style.backgroundColor = preColor;
	        preover = param;
	   }    
	   param.style.backgroundColor = "#DCF9FE";
	   if (preparam != null) preparam.style.backgroundColor = "#98FFFF";
	}