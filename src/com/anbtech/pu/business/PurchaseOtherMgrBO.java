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
	 * 발주정보집계화면에서 품목선택 후 견적의뢰 요청했을 때
	 * 선택된 품목 리스트를 가져와서 리턴한다.
     * items == "요청번호|발주번호|품목번호,요청번호|발주번호|품목번호,...." 식임.
	 ***********************************************/
	public ArrayList getEstimateItemList(String mode,String items) throws Exception{
		PurchaseMgrDAO purchaseDAO	= new PurchaseMgrDAO(con);
		RequestInfoTable table		= new RequestInfoTable();
		ArrayList item_list			= new ArrayList();

		//품목을 분리하여 배열에 담는다.
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

		//분리된 품목을 db에 담는다.
		for(int i = 0;i< item_count; i++){
			String request_no	= item[i][0]; //요청번호
			String item_code	= item[i][1]; //품목코드

			table = new RequestInfoTable();			
			table = purchaseDAO.getRequestItemInfo(request_no,item_code);

			item_list.add(table);
		}

		return item_list;
	}

	/******************************
	 * 품목견적의뢰시 메일본문 만들기
	*******************************/
	public String makeMailContent(String items) throws Exception  
	{
		String content	= "";

		PurchaseMgrDAO purchaseDAO	= new PurchaseMgrDAO(con);
		RequestInfoTable table		= new RequestInfoTable();
		ArrayList item_list			= new ArrayList();

		//품목을 분리하여 배열에 담는다.
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

		//본문 내용 만들기
		content+= "<html>";
		content+= "<head>";
		content+= "<title>견적의뢰</title>";
		content+= "</head>";
		content+= "<body>";
		content+= "<table border='0' width='800'>";
		content+= "  <tr>";
		content+= "		<td width='100%'><!--발신자 정보 -->";
		content+= "		<TABLE cellSpacing=0 cellPadding=0 width='800' border=1 bordercolordark='white' bordercolorlight='#9CA9BA'>";
		content+= "			<tr>"; 
		content+= "				<td width='100%' height='25' align='middle' bgcolor='#EAF3FD'>아래와 같은 품목에 대해서 견적을 요청합니다. 빠른 시일내에 응답을 해 주시면 고맙겠습니다.</td></tr>";
		content+= "		</TABLE></td></tr>";
		content+= "  <tr>";
		content+= "		<td width='100%'><!--품목리스트 -->";
		content+= "		<TABLE cellSpacing=0 cellPadding=0 width='100%' border=1 bordercolordark='white' bordercolorlight='#9CA9BA'>";
		content+= "			<tr>";
		content+= "				<td width='100' height='25' align='middle' bgcolor='#EAF3FD'>품목코드</td>";
		content+= "				<td width='100' bgcolor='#EAF3FD'>품목명</td>";
		content+= "				<td width='280' bgcolor='#EAF3FD'>품목설명</td>";
		content+= "				<td width='100' bgcolor='#EAF3FD'>수량</td>";
		content+= "				<td width='100' bgcolor='#EAF3FD'>단위</td>";
		content+= "				<td width='100' bgcolor='#EAF3FD'>희망입고일</td></tr>";

		
		for(int i = 0;i< item_count; i++){
			String request_no	= item[i][0]; //요청번호
			String item_code	= item[i][1]; //품목코드

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
	 * 발주서 출력시 메일본문 만들기
	*******************************/
	public String makeMailContentforOrderForm(String order_no,String req_message) throws Exception  
	{
		String content	= "";

		PurchaseMgrBO purchaseBO = new PurchaseMgrBO(con);
		PurchaseMgrDAO purchaseDAO = new PurchaseMgrDAO(con);
		OrderInfoTable table = new OrderInfoTable();
		com.anbtech.text.StringProcess sp = new com.anbtech.text.StringProcess();

		//선택된 발주번호에 대한 정보를 가져온다.
		table = purchaseDAO.getOrderInfo(order_no);
		String is_vat_contained = table.getIsVatContained().equals("1")?"포함":"별도";

		//선택된 발주번호에 존재하는 품목리스트를 가져온다.
		ArrayList item_list = new ArrayList();
		item_list = purchaseDAO.getOrderItemList(order_no);
		Iterator table_iter = item_list.iterator();

		//본문 내용 만들기
		content+= "<html>";
		content+= "<head>";
		content+= "<title>발주서</title>";
		content+= "</head>";
		content+= "<body>";
		content+= "	<table border='0' width='700' height='132' cellpadding='2'>";
		content+= "		<tr><td width='100%' height='16'>";
		content+= "			(주)스페이스링크<br>411-816 경기도 고양시 일산구 백석동 1294-3 재능교육빌딩 10층<br>TEL:(82)031-810-2262,";
		content+= "			FAX:(82)031-810-2203, HomePage:www.spacelink.co.kr";
		content+= "			</td></tr>";
		content+= "		<tr><td width='100%' height='7'><hr></td></tr>";
		content+= "		<tr><td width='100%' height='16'>";
		content+= "			<div align='center'><font size='4' face='굴림' color='#000000'><b><u>";
		content+= "			발 주 서</u></b></font></div></td></tr>";
		content+= "		<tr><td width='100%' height='37'>";
		content+= "			<table border='0' width='100%' cellspacing='0' cellpadding='2'>";
		content+= "				<tr><td width=15%><p align=left>공급업체명</td>";
		content+= "					<td width='35%'>" + table.getSupplyerName() + "</td>";
		content+= "					<td width='25%'><p align='right'>발주일자:</td>";
		content+= "					<td width='25%'>" + table.getOrderDate() + "</td></tr>";

		content+= "				<tr><td width=15%><p align=left>공급업체코드</td>";
		content+= "					<td width='35%'>" + table.getSupplyerCode() + "</td>";
		content+= "					<td width='25%'><p align='right'>발주번호:</td>";
		content+= "					<td width='25%'>" + table.getOrderNo() + "</td></tr>";

		content+= "				<tr><td width=15%><p align=left></td>";
		content+= "					<td width='35%'></td>";
		content+= "					<td width='25%'><p align='right'>발주담당:</td>";
		content+= "					<td width='25%'>" + table.getRequestorInfo() + "</td></tr>";
		content+= "			</table></td></tr>";
		content+= "		<tr><td width='100%' height='48'>하기와 같이 주문하오니 조건대로 납품될 수 있도록 귀사의";
		content+= "			협조를 부탁드립니다.<br></td></tr>";
		content+= "		<tr><td width='100%' height='16'>";
		content+= "			<table border='0' width='100%' cellspacing='0' cellpadding='2'>";
		content+= "				<tr><td width='11%'>결재방법</td><td width='89%'>: " + table.getApprovalType() + "</td></tr>";
		content+= "				<tr><td width='11%'>결재기간</td><td width='89%'>: " + table.getApprovalPeriod() + "일</td></tr>";
		content+= "				<tr><td width='11%'>화폐단위</td><td width='89%'>: " + table.getMonetaryUnit() + "</td></tr>";
		content+= "				<tr><td width='11%'>적용환율</td><td width='89%'>: " + table.getExchangeRate() + "원</td></tr>";
		content+= "				<tr><td width='11%'>VAT율</td><td width='89%'>: " + table.getVatRate() + "%</td></tr>";
		content+= "				<tr><td width='11%'>VAT금액</td><td width='89%'>: " + table.getVatMount() + "원</td></tr>";
		content+= "				<tr><td width='11%'>VAT포함여부</td><td width='89%'>: " + is_vat_contained + "</td></tr>";
		content+= "				<tr><td width='11%'>총발주금액</td><td width='89%'>: " + table.getOrderTotalMount() + "원</td></tr>";
		content+= "			</table></td></tr>";
		content+= "		<tr><td width='100%' height='16'></td></tr>";
		content+= "		<tr><td width='100%' height='16'>";
		content+= "			<TABLE cellSpacing=0 cellPadding=0 width='100%' border=0>";
		content+= "				<TBODY><TR bgColor=#9DA9B9 height=1><TD colspan=15></TD></TR>";
		content+= "					<TR vAlign=middle height=23>";
		content+= "					  <TD noWrap width=30 align=middle class='list_title'>번호</TD>";
		content+= "					  <TD noWrap width=6 class='list_title'></TD>";
		content+= "					  <TD noWrap width=80 align=middle class='list_title'>품목코드</TD>";
		content+= "					  <TD noWrap width=6 class='list_title'></TD>";
		content+= "					  <TD noWrap width=100% align=middle class='list_title'>품목명</TD>";
		content+= "					  <TD noWrap width=6 class='list_title'></TD>";
		content+= "					  <TD noWrap width=80 align=middle class='list_title'>발주단가</TD>";
		content+= "					  <TD noWrap width=6 class='list_title'></TD>";
		content+= "					  <TD noWrap width=80 align=middle class='list_title'>발주수량</TD>";
		content+= "					  <TD noWrap width=6 class='list_title'></TD>";
		content+= "					  <TD noWrap width=80 align=middle class='list_title'>발주단위</TD>";
		content+= "					  <TD noWrap width=6 class='list_title'></TD>";
		content+= "					  <TD noWrap width=80 align=middle class='list_title'>발주금액</TD>";
		content+= "					  <TD noWrap width=6 class='list_title'></TD>";
		content+= "					  <TD noWrap width=100 align=middle class='list_title'>희망납기일</TD></TR>";
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
				content+= "				<TR><TD colspan=15>특이사항:<br>" + req_message + "</TD></TR>";
		content+= "				</TBODY></TABLE></td></tr></table>";
		content+= "</body>";
		content+= "</html>";

		return content;
	}

	/******************
	 * 전자메일 보내기
	 ******************/
	public String sendEmail(String sender_email,String receiver_email,String content) throws Exception
	{
		//발신정보
		String fromName = "스페이스링크";					//보내는사람 이름
		String fromAdd	= sender_email;						//보내는사람 주소
		String host		= "mail.anbtech.co.kr";				//보내는 멜 서버명
		String subject	= "견적의뢰서";						//제목
		
		//정보세팅
		com.anbtech.email.emailSend email = new com.anbtech.email.emailSend();
		email.setSmtpUrl(host);
		email.setFrom(fromAdd);
		email.setFromName(fromName);
		email.setTo(receiver_email);
		email.setSubject(subject);
		email.setContent(content);

		//메일발송
		String result = email.sendMessageHtml();

		return result;
	}
}