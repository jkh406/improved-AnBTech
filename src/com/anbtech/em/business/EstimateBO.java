package com.anbtech.em.business;

import com.anbtech.em.entity.*;
import com.anbtech.em.db.*;
import java.text.DecimalFormat;
import java.sql.*;
import java.util.*;
import java.io.*;
import com.oreilly.servlet.MultipartRequest;

public class EstimateBO{

	private Connection con;

	public EstimateBO(Connection con){
		this.con = con;
	}

	/*****************************************************************
	 * 쿼리문의 검색 조건을 만든다.
	 *****************************************************************/
	public String getWhere(String mode,String searchword,String searchscope,String category,String login_id) throws Exception{

		String where = "", where_and = "", where_sea = "";

		if (searchword.length() > 0){
			if (searchscope.equals("company_name")){			// 견적회사명
				where_sea += "( company_name like '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("charge_name")){		// 견적고객명
				where_sea += "( charge_name like '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("estimate_subj")){	// 견적서 제목
				where_sea += "( estimate_subj like '%" +  searchword + "%' )";
			}
			else if (searchscope.equals("estimate_no")){		// 견적번호
				where_sea += "( estimate_no = '" +  searchword + "' )";
			}
			else if (searchscope.equals("writer")){	// 작성자 사번
				where_sea += "( writer = '" +  searchword + "' )";
			}

			if(mode.equals("list")) where = " WHERE " + where_sea + " and stat = '3'";
			else if(mode.equals("mylist")) where = " WHERE " + where_sea + " and stat in ('1','4') and writer LIKE '" + login_id + "%'";
		}
		else{
			if(mode.equals("list")) where = " WHERE stat = '3'";
			else if(mode.equals("mylist")) where = " WHERE stat in ('1','4') and writer LIKE '" + login_id + "%'";
		}

		return where;
	}

	/*****************************************************************
	 * 검색조건 문자열이 직접 넘어오는 경우의 where 구문을 만든다.
	 *****************************************************************/
	public String getWhereForDetail(String mode,String searchword,String category,String login_id) throws Exception{

		String where = "";
		String where_cat = "";
		String where_sea = "";
		String where_mode = "";
		
//		if(category.length() > 0) where_cat = "category = '"+category+"' ";

		if (searchword.length() > 0){
			//searchword = "and|estimate_no|E03-009,and|model_code|320200010, ..... 형태로 넘어옴.

			StringTokenizer str = new StringTokenizer(searchword, ",");
			int scope_count = str.countTokens();
			String scope[] = new String[scope_count];
			String[][] search = new String[scope_count][3];

			for(int i=0; i<scope_count; i++){ 
				scope[i] = str.nextToken();

				StringTokenizer str2 = new StringTokenizer(scope[i],"|");

				for(int j=0; j<3; j++){ 
					search[i][j] = str2.nextToken();

				}
			}
			
			for(int i = 0; i< scope_count-1; i++){
				if(search[i][1].equals("written_day")){
					//s_day = 2003070120030830 으로 넘어온다. 2003-07-01 ~ 2003-08-30 까지의 의미임.
					String s_day = search[i][2].substring(0,4) + "-" + search[i][2].substring(4,6) + "-" + search[i][2].substring(6,8);
					String e_day = search[i][2].substring(8,12) + "-" + search[i][2].substring(12,14) + "-" + search[i][2].substring(14,16);
					where_sea += " (written_day >= '" + s_day + "' and written_day <= '" + e_day +"') " + search[i][0];
				}else{
					where_sea += " (" + search[i][1] + " LIKE '%" + search[i][2] + "%') " + search[i][0];
				}
			}
			// 마지막 검색항목에는 and를 붙이지 않기 위해 따로 처리함.
			if(search[scope_count-1][1].equals("written_day")){
				String s_day = search[scope_count-1][2].substring(0,4) + "-" + search[scope_count-1][2].substring(4,6) + "-" + search[scope_count-1][2].substring(6,8);
				String e_day = search[scope_count-1][2].substring(8,12) + "-" + search[scope_count-1][2].substring(12,14) + "-" + search[scope_count-1][2].substring(14,16);
				where_sea += " (written_day >= '" + s_day + "' and written_day <= '" + e_day +"')";
			}else{
				where_sea += " (" + search[scope_count-1][1] + " LIKE '%" + search[scope_count-1][2] + "%')";
			}
			where = " WHERE " + where_sea;

			//list 모드일 경우에는 결재가 완료된 문건만
			//mylist 모드일 경우에는 stat == 1 or 4 이고 writer = login_id 인것만 
			if(mode.equals("list")) where += " and stat = '3'";
			else if(mode.equals("mylist")) where += " and stat in ('1','4') and writer LIKE '" + login_id + "%'";
		}else{
			if(mode.equals("list")) where = " WHERE stat = '3'";
			else if(mode.equals("mylist")) where = " WHERE stat in ('1','4') and writer LIKE '" + login_id + "%'";		
		}

		return where;
	}

	/************************************
	 * 견적정보 작성 폼을 가져오기
	 ************************************/
	public EstimateInfoTable getWriteForm(String mode,String estimate_no,String version,String login_id) throws Exception{
		EstimateDAO estimateDAO = new EstimateDAO(con);
		EstimateItemViewDAO item_viewDAO = new EstimateItemViewDAO(con);
		EstimateInfoTable estimate = new EstimateInfoTable();
		
		String company_name		= "";	//회사명
		String estimate_subj	= "";	//견적제목
		String charge_name		= "";	//담당자 이름
		String charge_rank		= "";	//담당자 직급
		String charge_div		= "";	//담당자 부서명
		String charge_mobile	= "";	//담당자 휴대전화번호
		String charge_tel		= "";	//담당자 전화번호
		String charge_fax		= "";	//담당자 팩스
		String charge_email		= "";	//담당자 이메일
		String delivery_place	= "";	//인도장소
		String payment_terms	= "";	//지불조건
		String guarantee_term	= "";	//보증기간
		String delivery_period	= "";	//납품기간
		String valid_period		= "";	//유효기간
		String delivery_day		= "";	//납품일자

		//작성 시간 구하기
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		//작성자 정보
		com.anbtech.admin.db.UserInfoDAO userDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable userinfo = new com.anbtech.admin.entity.UserInfoTable();
		userinfo = userDAO.getUserListById(login_id);

		String division = userinfo.getDivision();
		String user_name = userinfo.getUserName();
		String user_rank = userinfo.getUserRank();
		String writer_info = user_rank + " " + user_name;

		if(mode.equals("write")){			// 신규작성 모드
			estimate_no			= "E" + System.currentTimeMillis();
			version				= "1.0";
			delivery_place		= "귀사(하)가 원하는 곳";
			payment_terms		= "현금 100%";
			guarantee_term		= "6개월";
			delivery_period		= "수주후 일주일 내";
			valid_period		= "발급 후 1개월";

			estimate.setCompanyName(company_name);
			estimate.setEstimateSubj(estimate_subj);
			estimate.setEstimateNo(estimate_no);
			estimate.setVersion(version);
			estimate.setChargeName(charge_name);
			estimate.setChargeRank(charge_rank);
			estimate.setChargeDiv(charge_div);
			estimate.setChargeTel(charge_tel);
			estimate.setChargeMobile(charge_mobile);
			estimate.setChargeFax(charge_fax);
			estimate.setChargeEmail(charge_email);
			estimate.setDeliveryPlace(delivery_place);
			estimate.setPaymentTerms(payment_terms);
			estimate.setGuaranteeTerm(guarantee_term);
			estimate.setDeliveryPeriod(delivery_period);
			estimate.setValidPeriod(valid_period);
			estimate.setDeliveryDay(delivery_day);
			estimate.setWrittenDay(w_time);
			estimate.setWriter(writer_info);
			estimate.setLastUpdatedDay(w_time);
			estimate.setLastModifier(writer_info);
			estimate.setTotalAmount("");
			estimate.setSpecialInfo("");

		}else if(mode.equals("modify") || mode.equals("write_item")){	// 수정 및 견적품목 추가 모드
			estimate = estimateDAO.getEstimateInfo(estimate_no,version);

			estimate.setLastUpdatedDay(w_time);
			estimate.setLastModifier(writer_info);

			String total_amount = estimateDAO.getTotalAmount(estimate_no,version,estimate.getCutUnit(),estimate.getSpecialChange());
			String special_info = "";
			if(!estimate.getSpecialChange().equals("0")) special_info = "<font color=red> (특별할인 " + estimate.getSpecialChange() + "% 적용)</font>";
				
			estimate.setTotalAmount(total_amount);
			estimate.setSpecialInfo(special_info);

		}else if(mode.equals("copy")){		// 견적서 복사 모드
			String new_estimate_no	= "E" + System.currentTimeMillis();
			String new_version		= "1.0";

			//견적정보 복사하기
			estimateDAO.copyEstimateInfo(estimate_no,version,new_estimate_no,new_version,login_id);
			//견적품목 복사하기
			item_viewDAO.copyEstimateItem(estimate_no,version,new_estimate_no,new_version);

			//복사된 견적정보 가져오기
			estimate = estimateDAO.getEstimateInfo(new_estimate_no,new_version);
			estimate.setEstimateNo(new_estimate_no);
			estimate.setVersion(version);
			estimate.setWrittenDay(w_time);
			estimate.setWriter(writer_info);
			estimate.setLastUpdatedDay(w_time);
			estimate.setLastModifier(writer_info);

			String total_amount = estimateDAO.getTotalAmount(new_estimate_no,new_version,estimate.getCutUnit(),estimate.getSpecialChange());
			String special_info = "";
			if(!estimate.getSpecialChange().equals("0")) special_info = "<font color=red> (특별할인 " + estimate.getSpecialChange() + "% 적용)</font>";
				
			estimate.setTotalAmount(total_amount);
			estimate.setSpecialInfo(special_info);

		}else if(mode.equals("revision")){		// 견적서 리비젼 모드
			String max_version = estimateDAO.getMaxVersion(estimate_no);
			String new_version = Integer.toString(Integer.parseInt(max_version.substring(0,1)) + 1) + ".0";

			//견적정보 복사하기
			estimateDAO.copyEstimateInfo(estimate_no,version,estimate_no,new_version,login_id);
			//견적품목 복사하기
			item_viewDAO.copyEstimateItem(estimate_no,version,estimate_no,new_version);

			//복사된 견적정보 가져오기
			estimate = estimateDAO.getEstimateInfo(estimate_no,new_version);
			estimate.setEstimateNo(estimate_no);
			estimate.setVersion(version);
			estimate.setWrittenDay(w_time);
			estimate.setWriter(writer_info);
			estimate.setLastUpdatedDay(w_time);
			estimate.setLastModifier(writer_info);

			String total_amount = estimateDAO.getTotalAmount(estimate_no,new_version,estimate.getCutUnit(),estimate.getSpecialChange());
			String special_info = "";
			if(!estimate.getSpecialChange().equals("0")) special_info = "<font color=red> (특별할인 " + estimate.getSpecialChange() + "% 적용)</font>";
				
			estimate.setTotalAmount(total_amount);
			estimate.setSpecialInfo(special_info);
		}

		return estimate;
	}

	/*************************
	 * 견적품목 추가 및 수정 폼 가져오기
	 *************************/
	public ItemInfoTable getAddEstimateItemForm(String mode,String estimate_no,String version,String item_class,String mid) throws Exception{
		
		EstimateDAO estimateDAO = new EstimateDAO(con);
		ItemInfoTable item = new ItemInfoTable();

		String item_name		= "";
		String model_code		= "";
		String model_name		= "";
		String quantity			= "1";
		String unit				= "EA";
		String standards		= "";
		String maker_name		= "";
		String supplyer_code	= "";
		String supplyer_name	= "";
		String buying_cost		= "0";
		String gains_percent	= "0";
		String gains_value		= "0";
		String supply_cost		= "0";
		String discount_percent	= "0";
		String discount_value	= "0";
		String tax_percent		= "10";
		String tax_value		= "0";
		String estimate_value	= "0";

		if(mode.equals("modify_estimate_item")){
			item = estimateDAO.getEstimateItemInfo(mid);
			
			item_class		= item.getItemClass();            
			item_name		= item.getItemName();              
			model_code		= item.getModelCode();            
			model_name		= item.getModelName();            
			quantity		= item.getQuantity();               
			unit			= item.getUnit();                       
			standards		= item.getStandards();             
			maker_name		= item.getMakerName();            
			supplyer_code	= item.getSupplyerCode();      
			supplyer_name	= item.getSupplyerName();      
			buying_cost		= item.getBuyingCost();          
			gains_percent	= item.getGainsPercent();      
			gains_value		= item.getGainsValue();          
			supply_cost		= item.getSupplyCost();          
			discount_percent= item.getDiscountPercent();
			discount_value	= item.getDiscountValue();    
			tax_percent		= item.getTaxPercent();          
			tax_value		= item.getTaxValue();              
			estimate_value	= item.getEstimateValue();    
		}

		item.setMid(mid);
		item.setEstimateNo(estimate_no);
		item.setVersion(version);
		item.setItemClass(item_class);
		item.setItemName(item_name);
		item.setModelCode(model_code);
		item.setModelName(model_name);
		item.setQuantity(quantity);
		item.setUnit(unit);
		item.setStandards(standards);
		item.setMakerName(maker_name);
		item.setSupplyerCode(supplyer_code);
		item.setSupplyerName(supplyer_name);
		item.setBuyingCost(buying_cost);
		item.setGainsPercent(gains_percent);
		item.setGainsValue(gains_value);
		item.setSupplyCost(supply_cost);
		item.setDiscountPercent(discount_percent);
		item.setDiscountValue(discount_value);
		item.setTaxPercent(tax_percent);
		item.setTaxValue(tax_value);
		item.setEstimateValue(estimate_value);

		return item;
	}
	
	/*****************************************************************
	 * 상태코드를 받아서 상태 문자열을 만들어 준다.
	 *****************************************************************/
	public String getStat(String stat) throws Exception{

		String status = "";

		if(stat.equals("1"))	  status = "견적작성";	// 상신 전
		else if(stat.equals("2")) status = "결재진행";	// 결재 진행중
		else if(stat.equals("3")) status = "견적완료";	// 최종 승인 완료,견적 진행
		else if(stat.equals("4")) status = "결재반려";	// 전자결재에서 반려된 문서
		else if(stat.equals("5")) status = "발송완료";	// 견적서가 발부가 된 상태
		else if(stat.equals("6")) status = "Reserved";
		else if(stat.equals("7")) status = "Reserved";
		else if(stat.equals("8")) status = "Reserved";
		else if(stat.equals("9")) status = "삭제됨";	// 최종 승인이 완료된 후에 삭제 처리된 문서

		return status;
	}

	/*****************************************************************
	 * 상신처리 한다. stat == 2
	 *****************************************************************/
	public void makeReviewStat(String estimate_no,String version) throws Exception{
		EstimateDAO estimateDAO = new EstimateDAO(con);
		estimateDAO.updStat(estimate_no,version,"2");
	}

	/*****************************************************************
	 * 반려처리 한다. stat == 4
	 *****************************************************************/
	public void makeReturnStat(String estimate_no,String version) throws Exception{
		EstimateDAO estimateDAO = new EstimateDAO(con);
		estimateDAO.updStat(estimate_no,version,"4");
	}

	/*****************************************************************
	 * 승인처리 한다. 
	 * (1) stat == 3
	 * (2) 정식승인 번호 생성 및 업데이트
	 * (3) 결재 정보 가져와서 저장하기
	 *****************************************************************/
	public void makeCommitStat(String estimate_no,String version,String aid,String real_estimate_no) throws Exception{
		EstimateDAO estimateDAO = new EstimateDAO(con);
		
//		String real_estimate_no = estimateDAO.calculateEstimateNo();

		estimateDAO.updStat(estimate_no,version,"3");
		estimateDAO.getAppInfoAndSave(aid);
		estimateDAO.updAid(estimate_no,version,aid);

		//신규버젼인 경우에만 임시견적번호를 정식견적번호로 바꾸어준다.
		if(version.equals("1.0")){
			estimateDAO.updEstimateNo(estimate_no,version,"estimate_info",real_estimate_no);
			estimateDAO.updEstimateNo(estimate_no,version,"estimate_item_info",real_estimate_no);
		}
	}

}