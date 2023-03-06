<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0 Transitional//EN">
<HTML>
<HEAD>
<TITLE> New Document </TITLE>
<META NAME="Generator" CONTENT="EditPlus">
<META NAME="Author" CONTENT="">
<META NAME="Keywords" CONTENT="">
<META NAME="Description" CONTENT="">
</HEAD>

<BODY>
<form name='uForm1' >
	<input type="file" name="file1" size='50' onChange="func_addfile();">
	<input type="reset" name="reset" style="visibility:hidden;">
</form>


<img id="chkimg" style="visibility:hidden" onload="imgChk();">
<input type="hidden"  name="tmpname"  >
<input type="hidden"  name="totalfilesize" value="0" >

</BODY>
</HTML>

<script language='javascript'>
<!--

function func_clickaddfile()
{
	document.uForm1.file1.click();
	func_addfile();
}

function func_addfile()
{
	var file = document.uForm1.file1.value;
/*
	if (file == "")
		return;
	if (!func_validfile(file))
	{
		alert("jpg, gif 파일만 업로드할 수 있습니다");
		document.uForm1.file1.value = "";
		return;
	}

	file = file.replace(/\\/g, "\\\\");
*/
	imgInput(file);
}

function func_validfile(file)
{
	var ext = file.substring(file.lastIndexOf(".")+1);
	ext = ext.toUpperCase();
	if (ext == "JPG" || ext=="JPEG" || ext=="GIF")
		return true;
	return false;
}

function imgInput(src){
    src = src.replace(/\\\\/g, "\\");

	document.getElementById("tmpname").value = src;
	document.getElementById("chkimg").src = src;
}

function imgChk(){
	var MAX_SIZE = 1024 * 10;

	var tImg = document.getElementById("chkimg");
	var iWidth = tImg.width;
	var iHeight = tImg.height;
	var iSize = tImg.fileSize;

	if(iSize==0){
		document.getElementById("chkimg").src = "";
		alert("파일오류 입니다.");
		return;
	}

    if (iSize > MAX_SIZE){
		alert("400k 미만의 파일만 등록할 수 있습니다.");
		document.uForm1.file1.src = "";

		return;
	}
}

</script>