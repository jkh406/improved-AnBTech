	var ItemEntry = new Array();
	MenuWidth =  120 // Width of the pop-up menu	
	MenuHeight = 165 // Height of the pop-up menu
	ItemInMenu = 8   // How many items are in the menu
	
	ItemEntry[0] = new MenuEntry(" > 이동/복사","#");
	ItemEntry[1] = new MenuEntry(" > 로컬저장","#")		
	ItemEntry[2] = new MenuEntry(" > 회신","#");
	ItemEntry[3] = new MenuEntry(" > 전달","#");	
	ItemEntry[4] = new MenuEntry(" > 회신게시","#")	
	ItemEntry[5] = new MenuEntry(" > 게시자정보","#");		
	ItemEntry[6] = new MenuEntry(" > 수정", "#");
	ItemEntry[7] = new MenuEntry(" > 닫기", "#");
	
	// Setup your menu words
	var menuEle = new Array(ItemInMenu);
	// DO NOT MODIFY THE CODE BELOW
	
	function MenuEntry(cap,adrs) {
	this.cap = cap;
	this.adrs = adrs;
	}
	
	function init() {
	        // initialize objects
	        menu = new dynLayer("menuDiv",null);
	        for (var k = 0; k < menuEle.length; k++) {
	                menuEle[k] = new dynLayer("input" + k + "Div","menuDiv");
	                }
	        menu.hide();
	
	        // initialize events
	        //document.onmousedown = mouseDown
	        //document.onmousemove = mouseMove
	        //document.onmouseup = mouseUp
	}
	
	// Temperary Varibles
	showing = false;
	
	// drag variables
	dragActive = 0;
	dragObject = null;
	dragArray = new Array();
	dragLayerX = 0;
	dragLayerY = 0;
	dragLayerZ = 0;

	function dynLayer(id,nestref,des) {
	        this.css = document.all[id].style;
	        this.ref = document;
	        this.x = this.css.pixelLeft;
	        this.y = this.css.pixelTop;
	        this.w = this.css.pixelWidth;
	        this.h = this.css.pixelHeight;
	
	        this.obj = id + "Object";
	        eval(this.obj + "=this");
	        this.moveBy = dynLayerMoveBy;
	        this.moveTo = dynLayerMoveTo;
	        this.show = dynLayerShow;
	        this.hide = dynLayerHide;
	}
	function dynLayerMoveBy(x,y) {
	        this.x += x;
	        this.css.left = this.x;
	        this.y += y;
	        this.css.top = this.y;
	}
	function dynLayerMoveTo(x,y) {
	        this.x = x;
	        this.css.left = this.x;
	        this.y = y;
	        this.css.top = this.y;
	}
	function dynLayerShow() {
	        this.css.visibility = "visible";
	}
	function dynLayerHide() {
	        this.css.visibility = "hidden";
	}
	
	document.writeln('<STYLE TYPE=\"text\/css\">')
	document.writeln('#menuDiv {position:absolute; left:0; top:0; width:' + MenuWidth + '; height:' + MenuHeight + '; clip:rect(0,' + MenuWidth + ',' + MenuHeight + ',0); background-color:404040; layer-background-color:404040; visibility:hidden;}')
	document.writeln('#menuLayer1Div {position:absolute; left:0; top:0; width:' + (MenuWidth  -1) + '; height:' + (MenuHeight -1) + '; clip:rect(0,' + (MenuWidth -1) + ',' + (MenuHeight -1) + ',0); background-color:DEDEDE; layer-background-color:DEDEDE;}')
	document.writeln('#menuLayer2Div {position:absolute; left:1; top:1; width:' + (MenuWidth -2) + '; height:' + (MenuHeight-2) + '; clip:rect(0,' + (MenuWidth-2) + ',' + (MenuHeight -2) + ',0); background-color:808080; layer-background-color:808080;}')
	document.writeln('#menuLayer3Div {position:absolute; left:1; top:1; width:' + (MenuWidth -3) + '; height:' + (MenuHeight-3) + '; clip:rect(0,' + (MenuWidth-3) + ',' + (MenuHeight -3) + ',0); background-color:ffffff; layer-background-color:ffffff;}')
	document.writeln('#menuLayer4Div {position:absolute; left:2; top:2; width:' + (MenuWidth -4) + '; height:' + (MenuHeight-4) + '; clip:rect(0,' + (MenuWidth-4) + ',' + (MenuHeight -4) + ',0); background-color:DEDEDE; layer-background-color:DEDEDE;}')
	//document.writeln('#menuLayer4Div {position:absolute; left:2; top:2; width:' + (MenuWidth -4) + '; height:' + (MenuHeight-4) + '; clip:rect(0,' + (MenuWidth-4) + ',' + (MenuHeight -4) + ',0); background-color:DEDEDE; layer-background-color:DEDEDE;}') 0504바꿈
	
	document.writeln('A.JSmenu {color:black;}')
	
	for (var k = 0; k < menuEle.length; k++) {
	        //document.writeln('#input' + k + 'Div {position:absolute; left:3; top:' + (18 * k + 3) + '; width:' + (MenuWidth-6) + '; height:18; clip:rect(0,' + (MenuWidth -6) + ',18,0); background-color:D0D0D0; layer-background-color:D0D0D0;}')
	        document.writeln('#input' + k + 'Div {position:absolute; left:3; top:' + (20 * k + 3) + '; width:' + (MenuWidth-6) + '; height:20; clip:rect(0,' + (MenuWidth -6) + ',20,0); background-color:D0D0D0; layer-background-color:D0D0D0;}') //0504바꿈
	        }
	
	document.writeln('<\/STYLE>')
	document.writeln('<DIV ID="menuDiv">')
	document.writeln('<DIV ID="menuLayer1Div"></DIV>')
	document.writeln('<DIV ID="menuLayer2Div"></DIV>')
	document.writeln('<DIV ID="menuLayer3Div"></DIV>')
	document.writeln('<DIV ID="menuLayer4Div"></DIV>')
	
	for (var k = 0; k < menuEle.length; k++) {
	        document.writeln('<DIV ID="input' + k + 'Div" >&nbsp;&nbsp;<FONT FACE="돋움체" SIZE="1"><A CLASS="JSmenu" HREF="JavaScript:fnMenuAct(\'' + ItemEntry[k].adrs + '\', \'' + k + '\');">' + ItemEntry[k].cap + '</FONT></A></DIV>')
	        }
	document.writeln('</DIV>')
	init();
	
	
	
	function viewHit(param) {
		
		var g_IsCanonicForm = "<%=IsCanonicForm%>"; 
		ifrmmailDetail.location = "gw_mail_preview.asp?" + param + "<%=strFullURLString%>";
	
	}