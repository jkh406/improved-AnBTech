<%@ include file="../../admin/configPopUp.jsp"%>
<%@ page		
	info		= ""		
	contentType = "text/html; charset=KSC5601"
	errorPage	= "../../admin/errorpage.jsp"
	import		= "java.sql.*, java.io.*, java.util.*,com.anbtech.em.entity.*,com.anbtech.admin.entity.*"
%>
<%!
	ItemInfoTable item;
	EstimateInfoTable estimate;
	EmLinkUrl link;
	ApprovalInfoTable app_table;
	com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();
%>

<%
	//�������� ��������
	app_table = new ApprovalInfoTable();
	app_table = (ApprovalInfoTable)request.getAttribute("Approval_Info");

	String writer_sign		= app_table.getWriterSig()==null?"&nbsp;":app_table.getWriterSig();
	String writer_name		= app_table.getWriterName()==null?"&nbsp;":app_table.getWriterName();
	String reviewer_sign	= app_table.getReviewerSig()==null?"&nbsp;":app_table.getReviewerSig();
	String reviewer_name	= app_table.getReviewerName()==null?"&nbsp;":app_table.getReviewerName();
	String decision_sign	= app_table.getDecisionSig()==null?"&nbsp;":app_table.getDecisionSig();
	String decision_name	= app_table.getDecisionName()==null?"&nbsp;":app_table.getDecisionName();
	String memo				= app_table.getMemo()==null?"&nbsp;":app_table.getMemo();

	//�������� ��������
	String mode = request.getParameter("mode");
	estimate = new EstimateInfoTable();
	estimate = (EstimateInfoTable)request.getAttribute("Estimate_Info");

	link = new EmLinkUrl();
	link = (EmLinkUrl)request.getAttribute("Redirect");
	String link_list = link.getLinkList();
	String input_hidden = link.getInputHidden();
%>

<html>
<head>
<title>������</title>
<meta http-equiv='Content-Type' content='text/html; charset=EUC-KR'>
</head>

<body>

<table border="0" width="550" height="550" cellspacing="0" cellpadding="0">
  <tr>
    <td width="100%" align="right">
		<div id="print" style="position:relative;visibility:visible;">
		<a href='Javascript:winprint();'><img src="../em/images/bt_print.gif" border="0"></a>
		<a href='Javascript:self.close();'><img src="../em/images/bt_close.gif" border="0"></a></div>	
	</td>  
  </tr>  
  <tr>
    <td width="100%" height="47">
      <p align="center"><u><b><font size="6">�� �� ��</font></b></u></td>  
  </tr>  
  <tr>  
    <td width="100%" height="178">  
      <table border="0" width="100%">  
        <tr>  
          <td width="40%" valign="top">  
            <table border="0" width="100%" height="148" cellspacing="0" cellpadding="0">  
              <tr>  
                <td width="100%" height="33"><u>�������� : <%=estimate.getWrittenDay()%></u></td>    
              </tr>    
              <tr>    
                <td width="100%" height="51"><u><%=estimate.getCompanyName()%> ����</u></td>   
              </tr>   
              <tr>   
                <td width="100%" height="60"></td>   
              </tr>   
              <tr>   
                <td width="100%" height="30">�Ʒ��� ���� �����մϴ�.</td>    
              </tr>    
            </table>    
          </td>    
          <td width="60%">    
            <table border="1" width="100%">    
              <tr>    
                <td width="8%" rowspan="6" valign="middle" align="center">��
                  <p><br>
                  ��</p>
                  <p><br>
                  ��</p>
                </td>
                <td width="30%">����ڵ�Ϲ�ȣ</td>
                <td width="62%">126-81-69066</td>
              </tr>
              <tr>
                <td width="30%">��ȣ��</td>
                <td width="62%">���̾غ���ũ(��)</td>
              </tr>
              <tr>
                <td width="30%">��ǥ�̻�</td>
                <td width="62%">�� �� �� (��)</td>    
              </tr>    
              <tr>    
                <td width="30%">����������</td> 
                <td width="62%">��⵵ ��õ�� �ι��� ���ϸ� 557-4����     
                  ��Ϻ��� 2��</td>    
              </tr>    
              <tr>    
                <td width="30%">��ȭ��ȣ</td>    
                <td width="62%">031-632-5330~1</td>    
              </tr>    
              <tr>    
                <td width="30%">�ѽ���ȣ</td> 
                <td width="62%">031-632-5344</td> 
              </tr> 
            </table> 
          </td> 
        </tr> 
      </table> 
    </td> 
  </tr> 
  <tr> 
    <td width="100%" height="26"> 
      <table border="1" width="100%" height="32"> 
        <tr> 
          <td width="22%" height="26" align="middle">���ް���</td> 
          <td width="78%" height="26"><b><%=sp.getMoneyFormat(estimate.getTotalAmount(),"")%>��</b></td> 
        </tr> 
      </table> 
    </td> 
  </tr> 
  <tr> 
    <td width="100%" height="500" valign="top"> 
      <table border="1" width="100%"> 
        <tr> 
          <td width="7%" align="middle">��ȣ</td> 
          <td width="20%" align="middle">ǰ��</td> 
          <td width="25%" align="middle">�𵨸�</td> 
          <td width="7%" align="middle">����</td> 
          <td width="7%" align="middle">����</td> 
          <td width="13%" align="middle">�ܰ�</td> 
          <td width="13%" align="middle">���ް���</td> 
        </tr> 
	<%
		ArrayList item_list = new ArrayList();
		item_list = (ArrayList)request.getAttribute("Item_List");
		Iterator item_iter = item_list.iterator();
		int count = 1;
		while(item_iter.hasNext()){
			item = (ItemInfoTable)item_iter.next();

	%>
        <tr> 
          <td width="7%" align="middle"><%=count%></td> 
          <td width="27%"><%=item.getItemName()%></td> 
          <td width="12%"><%=item.getModelName()%></td> 
          <td width="7%" align="middle"><%=item.getQuantity()%></td> 
          <td width="7%" align="middle"><%=item.getUnit()%></td> 
          <td width="13%" align="right"><%=sp.getMoneyFormat(item.getSupplyCost(),"")%></td> 
          <td width="13%" align="right"><%=sp.getMoneyFormat(item.getEstimateValue(),"")%></td> 
        </tr> 
	<%		count++;
		}
	%>
       <tr> 
          <td width="100%" height="20" colspan="7"><%=estimate.getSpecialInfo()%>&nbsp;</td> 
        </tr>
      </table> 
    </td> 
  </tr> 
  <tr> 
    <td width="100%" height="16"> 
      <table border="1" width="100%"> 
        <tr> 
          <td width="21%" align="middle" height="50">Ư�̻���</td> 
          <td width="79%">��</td> 
        </tr> 
      </table> 
    </td> 
  </tr> 
  <tr> 
    <td width="100%" height="16">[����] �� �������� <%=estimate.getValidPeriod()%>��     
      ��ȿ�ϸ� �ܺη� ���� ����Ǿ�� �ȵ�.</td>    
  </tr>    
  <tr>    
    <td width="100%" height="16"></td>    
  </tr>    
  <tr>    
    <td width="100%" height="16">    
      <p align="center">������ ��ȭ�� �ֵ��ϰ� �����ϴ� ���     
      ���̾غ���ũ(��)</td>    
  </tr>
  <tr>    
    <td width="100%" height="30"> </td>    
  </tr>
  <tr>    
    <td width="100%" height="16"><div id="app" style="position:relative;visibility:visible;">
		<TABLE cellSpacing=0 cellPadding=0 width="550" border=1 bordercolordark="white" bordercolorlight="#9CA9BA">
		<TBODY>
			<TR vAlign=middle height=23>
				<TD noWrap width=20 align=middle rowspan="3" class="bg_05">��<p>��</TD>
				<TD noWrap width=100% align=left rowspan="3"><TEXTAREA NAME="doc_app_line" rows=6 cols=66 readOnly style="border:0"><%=memo%></TEXTAREA></TD>
				<TD noWrap width=20 align=middle rowspan="3" class="bg_05">��<p>��</TD>
				<TD noWrap width=50 align=middle class="bg_07">�����</TD>
				<TD noWrap width=50 align=middle class="bg_07">������</TD>
				<TD noWrap width=50 align=middle class="bg_07">������</TD></TR>
			<TR vAlign=middle height=50>
				<TD noWrap width=50 align=middle class="bg_06"><%=writer_sign%></TD>
				<TD noWrap width=50 align=middle class="bg_06"><%=reviewer_sign%></TD>
				<TD noWrap width=50 align=middle class="bg_06"><%=decision_sign%></TD></TR>
			<TR vAlign=middle height=23>
				<TD noWrap width=50 align=middle class="bg_07"><%=writer_name%><img src='' width='0' height='0'></TD>
				<TD noWrap width=50 align=middle class="bg_07"><%=reviewer_name%><img src='' width='0' height='0'></TD>
				<TD noWrap width=50 align=middle class="bg_07"><%=decision_name%><img src='' width='0' height='0'></TD></TR></TR></TBODY></TABLE></div>
	</td>    
  </tr>    
</table>    
    
</body>    
    
</html>    


<script language='javascript'>
function winprint()
{
	document.all['print'].style.visibility="hidden";
	document.all['app'].style.visibility="hidden";
	window.print();
	document.all['print'].style.visibility="visible";
	document.all['app'].style.visibility="visible";
}

function view_value_info(item_no,supplyer) {

	var url = "../servlet/EstimateMgrServlet?mode=view_supply_info&item_no="+item_no+"&supplyer="+supplyer;

	wopen(url,'view_history','600','285','scrollbars=yes,toolbar=no,status=no,resizable=no');
}

function wopen(url, t, w, h,st) {
	var sw;
	var sh;
	sw = (screen.Width - w) / 2;
	sh = (screen.Height - h) / 2 - 50;

	window.open(url, t, 'Width='+w+'px, Height='+h+'px, Left='+sw+', Top='+sh+', '+st);
}

</script>