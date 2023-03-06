<%@ include file="../../admin/configHead.jsp"%>
<%@ include file="../../admin/chk/chkCM01.jsp"%>
<%@ page
	language	= "java"
	import		= "java.sql.*,com.anbtech.text.Hanguel,java.util.*"
	contentType	= "text/html;charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
%>
<jsp:useBean id="bean" scope="request" class="com.anbtech.ViewQueryBean" />

<%
	String code_b		= request.getParameter("code_b");	// ��з� �ڵ�
	String code_s		= request.getParameter("code_s")==null?"":request.getParameter("code_s");	// �Һз� �ڵ�

	String item_code	= request.getParameter("item_code")==null?"":request.getParameter("item_code");// ǰ���ڵ�
	String codestring	= request.getParameter("codestring");
	
	/***************************************************
	 * �ش� �Һз��ڵ�� ��ϵ� ���ڵ尡 �ִ��� �˻��Ͽ�
	 * ������ ������ �����׸��� ������Ʈ�ϰ�, ������
	 * ���� �߰��Ѵ�.
	 ***************************************************/
	bean.openConnection();
	String query = "";
	if(codestring != null){
		query = "SELECT COUNT(code_s) FROM item_spec WHERE code_s = '"+code_s+"'";
		bean.executeQuery(query);
		bean.next();
		if(Integer.parseInt(bean.getData(1)) > 0 ){	// �ִ� ��� ����
			query = "UPDATE item_spec SET code_str = '"+codestring+"' WHERE code_s = '"+code_s+"'";
			bean.executeUpdate(query);			
		}else{	// ���� ��� ���� �߰�
			query = "INSERT INTO item_spec(code_s,code_str) VALUES('"+code_s+"','"+codestring+"')";
			bean.executeUpdate(query);
		}

	}
%>

<html>
<head><title></title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
<link rel="stylesheet" href="../css/style.css" type="text/css">
</head>

<BODY topmargin="0" leftmargin="0" oncontextmenu="return false">
<FORM ACTION="modifyItem.jsp" METHOD=POST style="margin:0">
<table cellspacing=0 cellpadding=2 width="100%" border=0>
   <tbody>
	 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	 <tr>
		 <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">ǥ�ر԰��׸�</td>
		 <td width="87%" height="25" class="bg_04" valign="top"><SELECT name="std_template_code" SIZE=10 style="WIDTH:400;" MULTIPLE>
				<%
					query = "SELECT spec_code,spec_name FROM spec_code WHERE item_code = '" + item_code +"'";
					bean.executeQuery(query);
					
					while(bean.next()) {
				%>
					<OPTION VALUE="[<%=bean.getData("spec_code")%>]<%=bean.getData("spec_name")%>">[<%=bean.getData("spec_code")%>]<%=bean.getData("spec_name")%></OPTION>
				<%	}	%>
				</SELECT>
				<a href="javascript:add_code()"><img src="../images/bt_add_sel.gif" border="0" alt="�����׸��߰�"></a></td></tr>
	 <tr bgcolor="C7C7C7"><td height="1" colspan="4"></td></tr>
	 <tr>
		 <td width="13%" height="25" class="bg_03" background="../images/bg-01.gif">����԰��׸�</td>
		 <td width="87%" height="25" class="bg_04" valign="top">
			<table border='0'>
				<tr><td><SELECT name="selected_code" SIZE="10" style="WIDTH:400;" onChange="javascript:UserSelected(document.forms[0].selected_code);">
				<%
					query = "SELECT code_str FROM item_spec WHERE code_s = '"+code_s+"'";
					bean.executeQuery(query);

					String code_str = "";
					if(bean.next()) code_str = bean.getData("code_str");

					StringTokenizer str = new StringTokenizer(code_str, ",");
						
					String item[] = new String[str.countTokens()]; 
					int i=0;
					while(str.hasMoreTokens()){ 
						item[i] = str.nextToken(); // item[i] == "31002|y|n"

						int m = item[i].indexOf("|");
						String spec_code = item[i].substring(0,m);
						String is_essence = item[i].substring(m+1,m+2);
						String is_desc = item[i].substring(m+3,m+4);
/*
						out.print("�׸��ڵ�:" + spec_code + "<br>");
						out.print("�ʼ�:" + is_essence + "<br>");
						out.print("����:" + is_desc + "<br>");
*/						
						query = "SELECT spec_name FROM spec_code WHERE spec_code = '" + spec_code + "'";
						bean.executeQuery(query);
						bean.next();
						String spec_name = bean.getData("spec_name");

						String option = is_essence + " " + is_desc + " " + "[" + spec_code + "]" + spec_name;
				%>
						<OPTION VALUE="<%=option%>"><%=option%></OPTION>
				<%		i++; 
					}
				%>	
					</SELECT></td>
				<td valign='bottom'>
					<input type="radio" value="y" checked name="is_essence" onClick="javascript:change_essence(document.forms[0].selected_code,'y');">�ʼ��Է� <input type="radio" value="n" name="is_essence" onClick="javascript:change_essence(document.forms[0].selected_code,'n');">�ɼ��Է�<br>
					<input type="radio" value="y" checked name="is_desc" onClick="javascript:change_desc(document.forms[0].selected_code,'y');">Description�� ���� <input type="radio" value="n" name="is_desc" onClick="javascript:change_desc(document.forms[0].selected_code,'n');">Description�� ���Ծ���.<br>
					<a href="javascript:move_code(document.forms[0].selected_code,-1);"><img src="../images/bt_move_up.gif" border="0" alt="�����̵�"></a>
					<a href="javascript:move_code(document.forms[0].selected_code,1);"><img src="../images/bt_move_down.gif" border="0" alt="�Ʒ����̵�"></a>
					<a href="javascript:sub_code(document.forms[0].selected_code,document.forms[0].std_template_code)"><img src="../images/bt_del_sel.gif" border="0" alt="�����׸����"></a></td></tr></table></td></tr>
     <tr bgcolor=c7c7c7><td height=1 colspan="4"></td></tr>
     <tr><td height=10 colspan="4"></td></tr></tbody></table>
<input type=hidden name=codestring>
<input type=hidden name=code_b value=<%=code_b%>>
<input type=hidden name=code_s value=<%=code_s%>>
<input type=hidden name=item_code value=<%=item_code%>>

</FORM>
</body>
</html>

<script language="Javascript">
//���� ����
function save(usermenu)
{
    var MenuLen, MenuString="";

    MenuLen = usermenu.length;

	if (MenuLen < 1 ){
		alert("������ �׸��� 1�� �̻��̾�� �մϴ�.");
		return;
	}

    for(i=0;i<MenuLen;i++)
    {
       	var fromField = usermenu.options[i].value.split(" ");
		var is_essence = fromField[0];
		var is_desc = fromField[1];
		var spec_code = fromField[2].substring(fromField[2].indexOf("[")+1,fromField[2].indexOf("]"));
		MenuString += spec_code + "|" + is_essence + "|" + is_desc + ",";
    }

    document.forms[0].codestring.value = MenuString;
	
	if(MenuString.indexOf("y|y") < 0){
		alert("���������� ������� �ʾҽ��ϴ�. ��� �� �׸��� �ʼ��׸��̸鼭 Description�� ���Եǵ��� �����Ͽ��� �մϴ�.");
		return
	}

//    alert("�׸� " + MenuString + " �� �����Ͽ����ϴ�.");
    document.forms[0].submit();

	alert("���������� ����Ǿ����ϴ�.");
}


//�׸��߰�
function add_code ()
{
	var f = document.forms[0];

	if(f.std_template_code.value == ""){
		alert("�߰��� �׸��� �����Ͻʽÿ�.");
		return;
	}

	for(var i = 0; i < f.selected_code.length; i++){
		if(f.selected_code.options[i].value.substring(f.selected_code.options[i].value.indexOf("["),f.selected_code.options[i].value.length) == f.std_template_code.value){
			alert("�̹� ��ϵ� �׸��Դϴ�.");
			return;
		}
	}

	if(f.std_template_code.value != ""){
		f.selected_code.options[f.selected_code.length]
			= new Option("y n "+f.std_template_code.value, "y n "+f.std_template_code.value);
	}
}

//�׸����
function sub_code (from, to)
{
    for (i=from.length-1;i>=0 ;i--)
    {

        if(from.options[i].selected == true)
                from.options[i] = null;
    }
}

//�׸��̵�
function move_code(usermenu, flag)
{
    var tmpOpt, tmpText,tmpIndex, selIndex;
    var Count =0;

    for (i=0;i<usermenu.length ;i++)
    {
                if(usermenu.options[i].selected == true)
                        Count++;

                if( Count > 1)
                {
                        alert("�ϳ��� �������ּ���.");
                        return;
                }
    }
	if (Count !=0)
   	{
        selIndex = usermenu.selectedIndex;
        tmpIndex = usermenu.selectedIndex+flag;

        if((tmpIndex >= usermenu.length) || ( tmpIndex < 0 ))
        {
            alert('�̵��� �� �����ϴ�.');
            return;
        }

        tmpOpt = usermenu.options[tmpIndex].value;
        tmpText = usermenu.options[tmpIndex].text;

        usermenu.options[tmpIndex].value = usermenu.options[selIndex].value;
        usermenu.options[tmpIndex].text  = usermenu.options[selIndex].text;
        usermenu.options[selIndex].value = tmpOpt;
        usermenu.options[selIndex].text  = tmpText;
	
		usermenu.options[tmpIndex].selected = true;
		usermenu.options[selIndex].selected = false;
   	}
   	else
    {
        alert('�̵��� �׸��� �������ּ���.');
        return;
    }
    return;
}

//�ʼ� or �ɼ� ����
function change_essence(Sel, Value)
{

	var f = document.forms[0];

   	var Count =0;

   	for (i=0;i<Sel.length ;i++)
   	{
       	if(Sel.options[i].selected == true)
	   		Count++;
    
		if( Count > 1)
       	{
       		alert("�� �׸� �������ּ���.");
       		return;
       	}
   	}

   	if (Count !=0)
   	{
       	var OldTxt = Sel.options[Sel.selectedIndex].text;
		var NewTxt = Value + OldTxt.substring(1,OldTxt.length);

		//�����׸��� description�� ���Խ�ų �� ����.
		if(f.is_desc[0].checked && Value == 'n'){
			alert('Description�� ���Ե� �׸��� �����׸����� ������ �� �����ϴ�. ���� Description �������� ������ �����ϼ���.');
			f.is_essence[0].checked = true;
			return;
		}

		Sel.options[Sel.selectedIndex].text = NewTxt;
		Sel.options[Sel.selectedIndex].value = NewTxt;

   	}
   	else
   	{
        alert("�׸��� �������ּ���.");
        return;
   	}
}

//description ���Կ��� ����
function change_desc(Sel, Value)
{

	var f = document.forms[0];

   	var Count =0;

   	for (i=0;i<Sel.length ;i++)
   	{
       	if(Sel.options[i].selected == true)
	   		Count++;
    
		if( Count > 1)
       	{
       		alert("�� �׸� �������ּ���.");
       		return;
       	}
   	}

   	if (Count !=0)
   	{
       	var OldTxt = Sel.options[Sel.selectedIndex].text;
		var NewTxt = OldTxt.substring(0,2) + Value + OldTxt.substring(3,OldTxt.length);

		//�����׸��� description�� ���Խ�ų �� ����.
		if(f.is_essence[1].checked && Value == 'y'){
			alert('�����׸��� Description�� ���Խ�ų �� �����ϴ�.');
			f.is_desc[1].checked = true;
			NewTxt = OldTxt;
		}

		Sel.options[Sel.selectedIndex].text = NewTxt;
		Sel.options[Sel.selectedIndex].value = NewTxt;

   	}
   	else
   	{
        alert("�׸��� �������ּ���.");
        return;
   	}
}

//�׸� ����ó��
function UserSelected(menu)
{
	var selIndex = menu.selectedIndex;
	var SelTxt = menu.options[selIndex].text;
	var fromField = SelTxt.split(" ");
	var is_essence = fromField[0];
	var is_desc = fromField[1];

	if(is_essence == "y") document.forms[0].is_essence[0].checked = true;
	else document.forms[0].is_essence[1].checked = true;

	if(is_desc == "y") document.forms[0].is_desc[0].checked = true;
	else document.forms[0].is_desc[1].checked = true;

//	document.forms[0].re_weight.value = SelTxt.substring(SelTxt.indexOf("[")+1,SelTxt.indexOf("]"));
}

</script>