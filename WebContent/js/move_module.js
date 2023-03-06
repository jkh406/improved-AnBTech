function GO(url,txt)
{
//	var confirm_move = confirm(txt + '로 이동하시겠습니까?');
//	if(confirm_move) top.location.href = url;

	top.location.href = url;
}

document.write("<SELECT onchange=GO(this.options[this.selectedIndex].value,this.options[this.selectedIndex].text) name=urlGO>");
document.write("<OPTION style=\"COLOR: #000000\" value='' selected>▒ 모듈 바로가기 ▒</OPTION>");
document.write("<OPTION style=\"COLOR: #000000\" value=/webffice/gw.htm>웹피스-그룹웨어</OPTION>");
document.write("<OPTION style=\"COLOR: #000000\" value=/webffice/pdm.htm>웹피스-연구/개발 PKG</OPTION>");
document.write("<OPTION style=\"COLOR: #000000\" value=/webffice/erp.htm>웹피스-생산/자재 PKG</OPTION>");
document.write("</SELECT>");