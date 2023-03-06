package com.anbtech.pu.business;

import com.anbtech.pu.entity.*;
import com.anbtech.pu.db.*;
import java.text.DecimalFormat;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.oreilly.servlet.MultipartRequest;

public class PurchaseOtherMgrBO{

	private Connection con;

	public PurchaseOtherMgrBO(Connection con){
		this.con = con;
	}

	/***********************************************
	 * ������������ȭ�鿡�� ǰ���� �� �����Ƿ� ��û���� ��
	 * ���õ� ǰ�� ����Ʈ�� �����ͼ� �����Ѵ�.
     * items == "��û��ȣ|���ֹ�ȣ|ǰ���ȣ,��û��ȣ|���ֹ�ȣ|ǰ���ȣ,...." ����.
	 ***********************************************/
	public ArrayList getEstimateItemList(String mode,String items) throws Exception{
		PurchaseMgrDAO purchaseDAO	= new PurchaseMgrDAO(con);
		RequestInfoTable table		= new RequestInfoTable();
		ArrayList item_list			= new ArrayList();

		//ǰ���� �и��Ͽ� �迭�� ��´�.
		StringTokenizer str = new StringTokenizer(items, ",");
		int item_count = str.countTokens();
		String item_a[] = new String[item_count];
		String[][] item = new String[item_count][2];

		for(int i=0; i<item_count; i++){ 
			item_a[i] = str.nextToken();

			StringTokenizer str2 = new StringTokenizer(item_a[i],"|");
			for(int j=0; j<2; j++){ 
				item[i][j] = str2.nextToken();
			}
		}

		//�и��� ǰ���� db�� ��´�.
		for(int i = 0;i< item_count; i++){
			String request_no	= item[i][0]; //��û��ȣ
			String item_code	= item[i][1]; //ǰ���ڵ�

			table = new RequestInfoTable();			
			table = purchaseDAO.getRequestItemInfo(request_no,item_code);

			item_list.add(table);
		}

		return item_list;
	}

	/******************************
	 * ǰ������Ƿڽ� ���Ϻ��� �����
	*******************************/
	public String makeMailContent(String items) throws Exception  
	{
		String content	= "";

		PurchaseMgrDAO purchaseDAO	= new PurchaseMgrDAO(con);
		RequestInfoTable table		= new RequestInfoTable();
		ArrayList item_list			= new ArrayList();

		//ǰ���� �и��Ͽ� �迭�� ��´�.
		StringTokenizer str = new StringTokenizer(items, ",");
		int item_count = str.countTokens();
		String item_a[] = new String[item_count];
		String[][] item = new String[item_count][2];

		for(int i=0; i<item_count; i++){ 
			item_a[i] = str.nextToken();

			StringTokenizer str2 = new StringTokenizer(item_a[i],"|");
			for(int j=0; j<2; j++){ 
				item[i][j] = str2.nextToken();
			}
		}

		//���� ���� �����
		content+= "<html>";
		content+= "<head>";
		content+= "<title>�����Ƿ�</title>";
		content+= "</head>";
		content+= "<body>";
		content+= "<table border='0' width='800'>";
		content+= "  <tr>";
		content+= "		<td width='100%'><!--�߽��� ���� -->";
		content+= "		<TABLE cellSpacing=0 cellPadding=0 width='800' border=1 bordercolordark='white' bordercolorlight='#9CA9BA'>";
		content+= "			<tr>"; 
		content+= "				<td width='100%' height='25' align='middle' bgcolor='#EAF3FD'>�Ʒ��� ���� ǰ�� ���ؼ� ������ ��û�մϴ�. ���� ���ϳ��� ������ �� �ֽø� ���ڽ��ϴ�.</td></tr>";
		content+= "		</TABLE></td></tr>";
		content+= "  <tr>";
		content+= "		<td width='100%'><!--ǰ�񸮽�Ʈ -->";
		content+= "		<TABLE cellSpacing=0 cellPadding=0 width='100%' border=1 bordercolordark='white' bordercolorlight='#9CA9BA'>";
		content+= "			<tr>";
		content+= "				<td width='100' height='25' align='middle' bgcolor='#EAF3FD'>ǰ���ڵ�</td>";
		content+= "				<td width='100' bgcolor='#EAF3FD'>ǰ���</td>";
		content+= "				<td width='280' bgcolor='#EAF3FD'>ǰ�񼳸�</td>";
		content+= "				<td width='100' bgcolor='#EAF3FD'>����</td>";
		content+= "				<td width='100' bgcolor='#EAF3FD'>����</td>";
		content+= "				<td width='100' bgcolor='#EAF3FD'>����԰���</td></tr>";

		
		for(int i = 0;i< item_count; i++){
			String request_no	= item[i][0]; //��û��ȣ
			String item_code	= item[i][1]; //ǰ���ڵ�

			table = purchaseDAO.getRequestItemInfo(request_no,item_code);		
		
			content+= "		<tr>";
			content+= "			<td width='100'>" + table.getItemCode() + "</td>";
			content+= "			<td width='100'>" + table.getItemName() + "</td>";
			content+= "			<td width='280'>" + table.getItemDesc() + "</td>";
			content+= "			<td width='100'>" + table.getRequestQuantity() + "</td>";
			content+= "			<td width='100'>" + table.getRequestUnit() + "</td>";
			content+= "			<td width='100'>" + table.getDeliveryDate() + "</td></tr>";		
		}

		content+= "		</table></td></tr>"; 
		content+= "</table>";
		content+= "</body>";
		content+= "</html>";

		return content;
	}

	/******************************
	 * ���ּ� ��½� ���Ϻ��� �����
	*******************************/
	public String makeMailContentforOrderForm(String order_no,String req_message) throws Exception  
	{
		String content	= "";

		PurchaseMgrBO purchaseBO = new PurchaseMgrBO(con);
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		OrderInfoTable table = new OrderInfoTable();
		com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();

		//���õ� ���ֹ�ȣ�� ���� ������ �����´�.
		table = purchaseDAO.getOrderInfo(order_no);
		String is_vat_contained = table.getIsVatContained().equals("1")?"����":"����";

		//���õ� ���ֹ�ȣ�� �����ϴ� ǰ�񸮽�Ʈ�� �����´�.
		ArrayList item_list = new ArrayList();
		item_list = purchaseDAO.getOrderItemList(order_no);
		Iterator table_iter = item_list.iterator();

		//���� ���� �����
		content+= "<html>";
		content+= "<head>";
		content+= "<title>���ּ�</title>";
		content+= "</head>";
		content+= "<body>";
		content+= "	<table border='0' width='700' height='132' cellpadding='2'>";
		content+= "		<tr><td width='100%' height='16'>";
		content+= "			(��)�����̽���ũ<br>411-816 ��⵵ ���� �ϻ걸 �鼮�� 1294-3 ��ɱ������� 10��<br>TEL:(82)031-810-2262,";
		content+= "			FAX:(82)031-810-2203, HomePage:www.spacelink.co.kr";
		content+= "			</td></tr>";
		content+= "		<tr><td width='100%' height='7'><hr></td></tr>";
		content+= "		<tr><td width='100%' height='16'>";
		content+= "			<div align='center'><font size='4' face='����' color='#000000'><b><u>";
		content+= "			�� �� ��</u></b></font></div></td></tr>";
		content+= "		<tr><td width='100%' height='37'>";
		content+= "			<table border='0' width='100%' cellspacing='0' cellpadding='2'>";
		content+= "				<tr><td width=15%><p align=left>���޾�ü��</td>";
		content+= "					<td width='35%'>" + table.getSupplyerName() + "</td>";
		content+= "					<td width='25%'><p align='right'>��������:</td>";
		content+= "					<td width='25%'>" + table.getOrderDate() + "</td></tr>";

		content+= "				<tr><td width=15%><p align=left>���޾�ü�ڵ�</td>";
		content+= "					<td width='35%'>" + table.getSupplyerCode() + "</td>";
		content+= "					<td width='25%'><p align='right'>���ֹ�ȣ:</td>";
		content+= "					<td width='25%'>" + table.getOrderNo() + "</td></tr>";

		content+= "				<tr><td width=15%><p align=left></td>";
		content+= "					<td width='35%'></td>";
		content+= "					<td width='25%'><p align='right'>���ִ��:</td>";
		content+= "					<td width='25%'>" + table.getRequestorInfo() + "</td></tr>";
		content+= "			</table></td></tr>";
		content+= "		<tr><td width='100%' height='48'>�ϱ�� ���� �ֹ��Ͽ��� ���Ǵ�� ��ǰ�� �� �ֵ��� �ͻ���";
		content+= "			������ ��Ź�帳�ϴ�.<br></td></tr>";
		content+= "		<tr><td width='100%' height='16'>";
		content+= "			<table border='0' width='100%' cellspacing='0' cellpadding='2'>";
		content+= "				<tr><td width='11%'>������</td><td width='89%'>: " + table.getApprovalType() + "</td></tr>";
		content+= "				<tr><td width='11%'>����Ⱓ</td><td width='89%'>: " + table.getApprovalPeriod() + "��</td></tr>";
		content+= "				<tr><td width='11%'>ȭ�����</td><td width='89%'>: " + table.getMonetaryUnit() + "</td></tr>";
		content+= "				<tr><td width='11%'>����ȯ��</td><td width='89%'>: " + table.getExchangeRate() + "��</td></tr>";
		content+= "				<tr><td width='11%'>VAT��</td><td width='89%'>: " + table.getVatRate() + "%</td></tr>";
		content+= "				<tr><td width='11%'>VAT�ݾ�</td><td width='89%'>: " + table.getVatMount() + "��</td></tr>";
		content+= "				<tr><td width='11%'>VAT���Կ���</td><td width='89%'>: " + is_vat_contained + "</td></tr>";
		content+= "				<tr><td width='11%'>�ѹ��ֱݾ�</td><td width='89%'>: " + table.getOrderTotalMount() + "��</td></tr>";
		content+= "			</table></td></tr>";
		content+= "		<tr><td width='100%' height='16'></td></tr>";
		content+= "		<tr><td width='100%' height='16'>";
		content+= "			<TABLE cellSpacing=0 cellPadding=0 width='100%' border=0>";
		content+= "				<TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=15></TD></TR>";
		content+= "					<TR vAlign=middle height=23>";
		content+= "					  <TD noWrap width=30 align=middle class='list_title'>��ȣ</TD>";
		content+= "					  <TD noWrap width=6 class='list_title'></TD>";
		content+= "					  <TD noWrap width=80 align=middle class='list_title'>ǰ���ڵ�</TD>";
		content+= "					  <TD noWrap width=6 class='list_title'></TD>";
		content+= "					  <TD noWrap width=100% align=middle class='list_title'>ǰ���</TD>";
		content+= "					  <TD noWrap width=6 class='list_title'></TD>";
		content+= "					  <TD noWrap width=80 align=middle class='list_title'>���ִܰ�</TD>";
		content+= "					  <TD noWrap width=6 class='list_title'></TD>";
		content+= "					  <TD noWrap width=80 align=middle class='list_title'>���ּ���</TD>";
		content+= "					  <TD noWrap width=6 class='list_title'></TD>";
		content+= "					  <TD noWrap width=80 align=middle class='list_title'>���ִ���</TD>";
		content+= "					  <TD noWrap width=6 class='list_title'></TD>";
		content+= "					  <TD noWrap width=80 align=middle class='list_title'>���ֱݾ�</TD>";
		content+= "					  <TD noWrap width=6 class='list_title'></TD>";
		content+= "					  <TD noWrap width=100 align=middle class='list_title'>���������</TD></TR>";
		content+= "					<TR bgColor=#9DA9B9 height=1><TD colspan=15></TD></TR>";

		int no = 1;
		while(table_iter.hasNext()){
			table = (OrderInfoTable)table_iter.next();
				content+= "				<TR onmouseover='this.style.backgroundColor=#F5F5F5' onmouseout='this.style.backgroundColor=' bgColor=#ffffff>";
				content+= "					  <TD align=middle height='24' class='list_bg'>" + no + "</td>";
				content+= "					  <TD><IMG height=1 width=1></TD>";
				content+= "					  <TD align=middle class='list_bg'>" + table.getItemCodeLink() + "</td>";
				content+= "					  <TD><IMG height=1 width=1></TD>";
				content+= "					  <TD align=left class='list_bg' style='padding-left:5px'>" + table.getItemName() + "</td>";
				content+= "					  <TD><IMG height=1 width=1></TD>";
				content+= "					  <TD align=right class='list_bg' style='padding-right:5px'>" + sp.getMoneyFormat(table.getUnitCost(),"") + "</td>";
				content+= "					  <TD><IMG height=1 width=1></TD>";
				content+= "					  <TD align=middle class='list_bg'>" + table.getOrderQuantity() + "</td>";
				content+= "					  <TD><IMG height=1 width=1></TD>";
				content+= "					  <TD align=middle class='list_bg'>" + table.getOrderUnit() + "</td>";
				content+= "					  <TD><IMG height=1 width=1></TD>";
				content+= "					  <TD align=right class='list_bg' style='padding-right:5px'>" + sp.getMoneyFormat(table.getOrderCost(),"") + "</td>";
				content+= "					  <TD><IMG height=1 width=1></TD>";
				content+= "					  <TD align=middle class='list_bg'>" + table.getDeliveryDate() + "</td></TR>";
				content+= "				<TR><TD colSpan=15></TD></TR>";

			no++;	
		}
				content+= "				<TR><TD colspan=15>Ư�̻���:<br>" + req_message + "</TD></TR>";
		content+= "				</TBODY></TABLE></td></tr></table>";
		content+= "</body>";
		content+= "</html>";

		return content;
	}

	/******************
	 * ���ڸ��� ������
	 ******************/
	public String sendEmail(String sender_email,String receiver_email,String content) throws Exception
	{
		//�߽�����
		String fromName = "�����̽���ũ";					//�����»�� �̸�
		String fromAdd	= sender_email;						//�����»�� �ּ�
		String host		= "mail.anbtech.co.kr";				//������ �� ������
		String subject	= "�����Ƿڼ�";						//����
		
		//��������
		com.anbtech.email.emailSend email = new com.anbtech.email.emailSend();
		email.setSmtpUrl(host);
		email.setFrom(fromAdd);
		email.setFromName(fromName);
		email.setTo(receiver_email);
		email.setSubject(subject);
		email.setContent(content);

		//���Ϲ߼�
		String result = email.sendMessageHtml();

		return result;
	}
}