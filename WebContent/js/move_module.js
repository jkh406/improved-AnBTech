function GO(url,txt)
{
//	var confirm_move = confirm(txt + '�� �̵��Ͻðڽ��ϱ�?');
//	if(confirm_move) top.location.href = url;

	top.location.href = url;
}

document.write("<SELECT onchange=GO(this.options[this.selectedIndex].value,this.options[this.selectedIndex].text) name=urlGO>");
document.write("<OPTION style=\"COLOR: #000000\" value='' selected>�� ��� �ٷΰ��� ��</OPTION>");
document.write("<OPTION style=\"COLOR: #000000\" value=/webffice/gw.htm>���ǽ�-�׷����</OPTION>");
document.write("<OPTION style=\"COLOR: #000000\" value=/webffice/pdm.htm>���ǽ�-����/���� PKG</OPTION>");
document.write("<OPTION style=\"COLOR: #000000\" value=/webffice/erp.htm>���ǽ�-����/���� PKG</OPTION>");
document.write("</SELECT>");