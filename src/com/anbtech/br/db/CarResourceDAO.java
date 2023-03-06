package com.anbtech.br.db;

import com.anbtech.br.entity.*;
import com.anbtech.br.business.*;
import java.sql.*;
import java.util.*;
import java.text.DecimalFormat;
import java.io.*;


public class CarResourceDAO{
	private Connection con;

	/*******************************************************************
	 * 생성자
	 *******************************************************************/
	public CarResourceDAO(Connection con){
		this.con = con;
	}


	/*****************************************************************************
	 * 신규 차량 등록하기
	 *****************************************************************************/
	 public void saveNewCarInfo(String car_type,String car_no,String model_name,String produce_year,String buy_date,String price,String fuel_type,String fuel_efficiency,String maker_company,String car_id) throws Exception
	{
		PreparedStatement pstmt = null;

		//등록시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd");
		String w_time = vans.format(now);

		String query = "INSERT INTO car_info (car_type,car_no,model_name,produce_year,buy_date,price,fuel_type,fuel_efficiency,maker_company,reg_date,stat,car_id) VALUES (?,?,?,?,?,?,?,?,?,?,'1','')";
		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,car_type);
		pstmt.setString(2,car_no);
		pstmt.setString(3,model_name);
		pstmt.setString(4,produce_year);
		pstmt.setString(5,buy_date);
		pstmt.setString(6,price);
		pstmt.setString(7,fuel_type);
		pstmt.setString(8,fuel_efficiency);
		pstmt.setString(9,maker_company);
		pstmt.setString(10,w_time);
	
		pstmt.executeUpdate();
		pstmt.close();
	}

	/**************************
	 * 차량정보를 수정한다.
	 **************************/
	public void updateCarInfo(String cid,String car_type,String car_no,String model_name,String produce_year,String buy_date,String price,String fuel_type,String fuel_efficiency,String maker_company,String car_id,String stat) throws Exception{

		Statement stmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs;
		String query= "";

		query = "UPDATE car_info SET car_type=?,car_no=?,model_name=?,produce_year=?,buy_date=?,price=?,fuel_type=?,fuel_efficiency=?,maker_company=?,car_id=?,stat=? WHERE cid='"+cid+"'";
		pstmt = con.prepareStatement(query);
		pstmt.setString(1,car_type);
		pstmt.setString(2,car_no);
		pstmt.setString(3,model_name);
		pstmt.setString(4,produce_year);
		pstmt.setString(5,buy_date);
		pstmt.setString(6,price);
		pstmt.setString(7,fuel_type);
		pstmt.setString(8,fuel_efficiency);
		pstmt.setString(9,maker_company);
		pstmt.setString(10,car_id);
		pstmt.setString(11,stat);
		pstmt.executeUpdate();
		pstmt.close();

	}//updateCarInfo()

	/**********************************************
	 * (입고처리시 수리차량)차량 상태정보를 수정한다.
	 ***********************************************/
	public void updateCarState(String cid,String st) throws Exception{
		Statement stmt = null;
		String query = "UPDATE car_info SET stat='"+st+"' WHERE cid ='"+cid+"'";
		stmt=con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*****************************************************************************
	 * 등록된 차량 리스트를 가져온다.
	 *****************************************************************************/
	public ArrayList getCarList() throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		CarInfoTable table = new CarInfoTable();
		CarResourceBO carBO = new CarResourceBO(con);
		ArrayList table_list = new ArrayList();

		//검색조건에 맞는 게시물을 가져온다.
		String query = "SELECT * FROM car_info WHERE stat != '6' order by cid desc";
		//System.out.println(query);
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next()){
			table = new CarInfoTable();
			table.setCid(rs.getString("cid"));
			table.setCarType(rs.getString("car_type"));
			table.setCarNo(rs.getString("car_no"));
			table.setModelName(rs.getString("model_name"));
			table.setProduceYear(rs.getString("produce_year"));
			table.setBuyDate(rs.getString("buy_date"));
			table.setPrice(rs.getString("price"));
			table.setFuelType(rs.getString("fuel_type"));
			table.setFuelEfficiency(rs.getString("fuel_efficiency"));
			table.setMakerCompany(rs.getString("maker_company"));
			table.setRegDate(rs.getString("reg_date"));
			table.setStat(carBO.getStatName(rs.getString("stat")));
			table.setCarId(rs.getString("car_id"));
			table_list.add(table);
			//System.out.println(rs.getString("car_no"));
		}

		stmt.close();
		rs.close();
		return table_list;
	}

	/*****************************************************************************
	 * 등록된 차량 리스트를 가져온다.
	 *****************************************************************************/
	public ArrayList getAllCarList() throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		CarInfoTable table = new CarInfoTable();
		CarResourceBO carBO = new CarResourceBO(con);
		ArrayList table_list = new ArrayList();

		//검색조건에 맞는 게시물을 가져온다.
		String query = "SELECT * FROM car_info order by cid desc";
		//System.out.println(query);
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next()){
			table = new CarInfoTable();
			table.setCid(rs.getString("cid"));
			table.setCarType(rs.getString("car_type"));
			table.setCarNo(rs.getString("car_no"));
			table.setModelName(rs.getString("model_name"));
			table.setProduceYear(rs.getString("produce_year"));
			table.setBuyDate(rs.getString("buy_date"));
			table.setPrice(rs.getString("price"));
			table.setFuelType(rs.getString("fuel_type"));
			table.setFuelEfficiency(rs.getString("fuel_efficiency"));
			table.setMakerCompany(rs.getString("maker_company"));
			table.setRegDate(rs.getString("reg_date"));
			table.setStat(carBO.getStatName(rs.getString("stat")));
			table.setCarId(rs.getString("car_id"));
			table_list.add(table);
			//System.out.println(rs.getString("car_no"));
		}

		stmt.close();
		rs.close();
		return table_list;
	}


	/*****************************************************************************
	 * 선택된(해당 차량 1대) 차량의 사용이력 리스트를 가져오기 
	 *****************************************************************************/
	public ArrayList getEachCarHistory(String cid, String page, String total, String year) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		CarUseInfoTable table = new CarUseInfoTable();
		CarResourceBO carBO = new CarResourceBO(con);
		ArrayList table_list = new ArrayList();
		//String str="";
		
		int l_maxlist = 10;			// 한페이지내에 출력할 레코드 수

		int current_page_num =Integer.parseInt(page);
		int recNum = Integer.parseInt(total);

		//검색조건에 맞는 게시물을 가져온다.
		String query = "SELECT * FROM charyang_master WHERE u_year='"+year+"' AND c_id='"+cid+"' AND flag2='EF' ORDER BY no desc";  
//		System.out.println(query);		WHERE c_id ='"+cid+"'  ";		                 
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

		//while(rs.next()){
			table = new CarUseInfoTable();
			
			table.setCrId(rs.getString("cr_id"));
			table.setCid(rs.getString("c_id"));
			table.setWriteName(rs.getString("write_name"));
			table.setWriteId(rs.getString("write_id"));
			table.setUserId(rs.getString("user_id"));
			table.setUserName(rs.getString("user_name"));		
			table.setUserCode(rs.getString("user_code"));		
			table.setUserRank(rs.getString("user_rank"));
			table.setAcId(rs.getString("ac_id"));
			table.setAcCode(rs.getString("ac_code"));
			table.setAcName(rs.getString("ac_name"));
			table.setFellowNames(rs.getString("fellow_names"));
			table.setInDate(rs.getString("in_date"));
		   	table.setUyear(rs.getString("u_year"));
			table.setUmonth(rs.getString("u_month"));
			table.setUdate(rs.getString("u_date"));
			table.setUtime(rs.getString("u_time"));
			table.setTuYear(rs.getString("c_year"));
			table.setTuMonth(rs.getString("c_month"));
			table.setTuDate(rs.getString("c_date"));
			table.setTuTime(rs.getString("c_time"));
			table.setCrPurpose(rs.getString("cr_purpose"));
			table.setCrDest(rs.getString("cr_dest"));
			table.setContent(rs.getString("content"));
			table.setEmTel(rs.getString("em_tel"));
			table.setEnteringState(rs.getString("entering_state"));
			table.setVstatus(carBO.getStatName(rs.getString("v_status")));	
			table.setMgrId(rs.getString("mgr_id"));
			table.setMgrName(rs.getString("mgr_name"));
			table.setChgCont(rs.getString("chg_cont"));
		
/*			
			 if(rs.getString("v_status").equals("1")) { 
				str = "<a href='javascript:entering("+rs.getString("c_id")+","+rs.getString("cr_id")+")'>입고</a>";
			} else if(rs.getString("v_status").equals("2")) { 
				str = "<a href='javascript:lending_cancel("+rs.getString("c_id")+","+rs.getString("cr_id")+")'>예약취소</a>|                    <a href='javascript:lending_process("+rs.getString("c_id")+","+rs.getString("cr_id")+")'>배차</a>";
			} else if(rs.getString("v_status").equals("3")) {
				str = "<a href='javascript:entering("+rs.getString("c_id")+","+rs.getString("cr_id")+")'>입고</a>";
			} else if(rs.getString("v_status").equals("7")) { str ="사용완료"+rs.getString("entering_state");
			} else if(rs.getString("v_status").equals("4")) { str ="예약취소";
			} else if(rs.getString("v_status").equals("5")) { str ="수리중";	
			} else if(rs.getString("v_status").equals("6")) { str ="폐차처리";
			} else if(rs.getString("v_status").equals("8")) { str ="임시저장";
			}

			table.setVstatusStr(str);
*/			
			table_list.add(table);
			recNum--;
		}

		stmt.close();
		rs.close();
		return table_list;
	}

	/*****************************************************************************
	 * 선택된 차량 정보를 가져온다.
	 *****************************************************************************/
	public CarInfoTable getCarInfo(String cid) throws Exception{
		CarInfoTable table = new CarInfoTable();
		Statement stmt = null;
		ResultSet rs = null;
		
		//ArrayList table_list = new ArrayList();

		//검색조건에 맞는 게시물을 가져온다.
		String query = "SELECT * FROM car_info WHERE cid='"+cid+"'";
		//System.out.println(query);
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		while(rs.next()){
			table = new CarInfoTable();
			table.setCid(rs.getString("cid"));
			table.setCarType(rs.getString("car_type"));
			table.setCarNo(rs.getString("car_no"));
			table.setModelName(rs.getString("model_name"));
			table.setProduceYear(rs.getString("produce_year"));
			table.setBuyDate(rs.getString("buy_date"));
			table.setPrice(rs.getString("price"));
			table.setFuelType(rs.getString("fuel_type"));
			table.setFuelEfficiency(rs.getString("fuel_efficiency"));
			table.setMakerCompany(rs.getString("maker_company"));
			table.setRegDate(rs.getString("reg_date"));
			table.setStat(rs.getString("stat"));
			table.setCarId(rs.getString("car_id"));
		//	table_list.add(table);
			//System.out.println(rs.getString("car_no"));
		}

		stmt.close();
		rs.close();
		return table;
	}
	

	/*****************************************************************************
	 * 월별 각 차량 예약 현황을 가져온다.
	 *****************************************************************************/
	public ArrayList getResourceStat(String y,String m) throws Exception{

		Statement stmt = null;
		ResultSet rs = null;
		
		CarInfoTable table = new CarInfoTable();

		//해당년월의 날수를 구한다.
		int total_day = getDaysInMonth(Integer.parseInt(m),Integer.parseInt(y));

		ArrayList list_view = new ArrayList();

		// 차량 리스트를 가져온다.
		ArrayList car_list = new ArrayList();
		car_list = (ArrayList)getCarList();
	  
		Iterator car_iter = car_list.iterator();
		int count1 = 1;
		int a=1;
		while(car_iter.hasNext()){
			table = (CarInfoTable)car_iter.next();
			
			String cid = table.getCid();			// 차량 마스터키
			String car_no = table.getCarNo();		// 차량 번호
			String car_stat = table.getStat();		// 차 상태
			String car_type = table.getCarType();	// 차종
			String model_name = table.getModelName(); // 차 모델명

			//각 차량의 사용 이력을 가져온다.
			String query = "SELECT v_status, u_year,u_month,u_date,u_time,c_year,c_month,c_date,c_time,flag,flag2 FROM charyang_master WHERE c_id = '" + cid + "' and (u_year='" + y + "' or c_year='" + y + "') and (u_month='" + m + "' or c_month='" + m +"') and (v_status = '7' or v_status = '2' or v_status = '3') and flag!='EN'";
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			int b=10;
			String day[][] = new String[total_day+1][2];
			
			//배열 초기화
			for(int i=0;i<total_day+1;i++){
				if(car_stat.equals("수리중")) {
					day[0][0] ="<TD align=middle height=22 onmouseover=\"ANB_layerAction(" + "z" +a+b+", 'visible')\" onmouseout=\"ANB_layerAction(" + "z" + a+b+", 'hidden')\" ><a href='BookResourceServlet?tablename=charyang_master&category=car&mode=eachcar&cid="+cid+"'>"+model_name+"</a><div id=z"+a+b+" style=\"position:absolute;background-color:#FEFEED;width:130;height:50;padding-top: 5px ;padding-left: 5px ;font:9pt '돋움체';border:#C0C0C0 1px solid;visibility:hidden; \">"+car_no+"<br>"+car_stat+"</div></TD>$수리중";

				} else	{
					day[0][0] = "<TD align=middle height='22' onmouseover=\"ANB_layerAction(" + "z"+b +a+", 'visible')\" onmouseout=\"ANB_layerAction(" + "z"+b+a+", 'hidden')\" ><a href='BookResourceServlet?tablename=charyang_master&category=car&mode=eachcar&cid="+cid+"'>"+model_name+"</a><div id=z"+b+a+" style=\"position:absolute;background-color:#FEFEED;width:130;height:50;padding-top: 5px ;padding-left: 5px ;font:9pt '돋움체';border:#C0C0C0 1px solid;visibility:hidden; \">"+car_no+"<br>"+car_stat+"</div></TD>$"+cid;
				}
				day[0][1] = "";
				day[i][0] = "";
				day[i][1] = "";
				b++;
			}
			a++;
			int count2 = 1;
			String temp="";
			while(rs.next()){

				String year		= rs.getString("u_year");
				String month	= rs.getString("u_month");
				String date		= rs.getString("u_date");
				String time		= rs.getString("u_time");

				String year2	= rs.getString("c_year");
				String month2	= rs.getString("c_month");
				String date2	= rs.getString("c_date");
				String time2	= rs.getString("c_time");

				String v_status = rs.getString("v_status");
				String flag = rs.getString("flag");
				String flag2 = rs.getString("flag2");
				
				String str="";

						if(v_status.equals("3")){ // 배차중
							str = "<img src='../br/images/ex_out.gif' border='0'>";
							temp = "[배차중]";
						} else if(v_status.equals("7")) { // 입고 완료
							str = "<img src='../br/images/ex_in.gif' border='0'>"; 
							temp = "[입고]";
						} else	if(flag.equals("EF") && flag2.equals("EE") && v_status.equals("2")) { // 2차결재 진행중
							str ="<img src='../br/images/ex_ing.gif' border='0'>"; 
							temp = "[결재진행중]";
						} else	if(flag.equals("EF") && flag2.equals("EN") && v_status.equals("2")) { // 1차결재 완료
							str ="<img src='../br/images/ex_ing.gif' border='0'>"; 
							temp = "[결재진행중]";
						}  else	if(flag.equals("EE") && flag2.equals("EN") && v_status.equals("2")) { // 1차결재 진행중
							str ="<img src='../br/images/ex_ing.gif' border='0'>"; 
							temp = "[1차결재진행중]";
						}	else 	if(flag.equals("EF") && flag2.equals("EF") && v_status.equals("2")) { // 2차결재 완료(예약중)
							str = "<img src='../br/images/ex_book.gif' border='0'>"; 
							temp = "[예약중]";
						}  

				String info		= month + "/" + date + " " + time + " ~ " + month2 + "/" + date2 + " " + time2+" "+temp;
			
				//시작일과 종료일이 같은 달 내에 있는 경우
				if(month.equals(month2)){
						//System.out.println(v_status);
						//System.out.println("date "+date+" date2="+date2);
					for(int d=Integer.parseInt(date);d<=Integer.parseInt(date2);d++){
						day[d][0] = "<a href='#' onmouseover=\"ANB_layerAction(" + "t" + d + count1 + count2 + ", 'visible')\" onmouseout=\"ANB_layerAction(" + "t" + d + count1 + count2 + ", 'hidden')\">"+str+"</a><center><div id=t" + d + count1 + count2 + " style=\"position:absolute; left:20px; top:25px; width:250; height:20; z-index:1; visibility: hidden; background-color:#FEFEED; font:9pt '돋움체'; border:#C0C0C0 1px solid;\">";
						day[d][1] += info + "<br>";
					}
				}

				// 종료일이 현재달에 포함되는 경우
				else if(!month.equals(month2) && month2.equals(m)){
					for(int d=1;d<=Integer.parseInt(date2);d++){
						day[d][0] = "<a href='#' onmouseover=\"ANB_layerAction(" + "t" + d + count1 + count2 + ", 'visible')\" onmouseout=\"ANB_layerAction(" + "t" + d + count1 + count2 + ", 'hidden')\">"+str+"</a><div id=t" + d + count1 + count2 + " style=\"position:absolute; left:20px; top:25px; width:250; height:20; z-index:1; visibility: hidden; background-color:#FEFEED; font:9pt '돋움체'; border:#C0C0C0 1px solid;\">";
						day[d][1] += info + "<br>";
					}
				}

				//시작일은 현재달이지만 종료일이 다음달로 넘어가는 경우
				else if(!month.equals(month2) && month.equals(m)){
					for(int d=Integer.parseInt(date);d<=total_day;d++){
					day[d][0] = "<a href='#' onmouseover=\"ANB_layerAction(" + "t" + d + count1 + count2 + ", 'visible')\" onmouseout=\"ANB_layerAction(" + "t" + d + count1 + count2 + ", 'hidden')\">"+str+"</a><div id=t" + d + count1 + count2 + " style=\"position:absolute; left:20px; top:25px; width:250; height:20; z-index:1; visibility: hidden; background-color:#FEFEED; font:9pt '돋움체'; border:#C0C0C0 1px solid;\">";
						day[d][1] += info + "<br>";
					}
				}

				count2++;
			}
			list_view.add(day);
			count1++;
		}

		stmt.close();
		rs.close();

		return list_view;
	}


  /*********************************************************
   * 지정한 년도, 지정한 월의 총 날짜 수를 구한다.
   *********************************************************/
  public static int getDaysInMonth(int m, int y) {
    if (m < 1 || m > 12)
        throw new RuntimeException("Invalid month: " + m);

    int[] b = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
    if (m != 2 && m >= 1 && m <= 12 && y != 1582)
        return b[m - 1];
    if (m != 2 && m >= 1 && m <= 12 && y == 1582)
        if (m != 10)
            return b[m - 1];
        else 
            return b[m - 1] - 10;

    if (m != 2)
        return 0;

    // m == 2 (즉 2월)
    if (y > 1582) {
        if (y % 400 == 0)
            return 29;
        else if (y % 100 == 0)
            return 28;
        else if (y % 4 == 0)
            return 29;
        else
            return 28;
    }
    else if (y == 1582)
        return 28;
    else if (y > 4) {
        if (y % 4 == 0)
            return 29;
        else
            return 28;
    }
    else if (y > 0)
        return 28;
    else
        throw new RuntimeException("Invalid year: " + y);
  } // End of getDaysInMonth()

	/*********************************************************
   * 차량 배차 정보를 가져온다.
   *********************************************************/
	public ArrayList getlendingCarList() throws Exception{
		
		com.anbtech.br.business.CarResourceBO carResourceBO = new com.anbtech.br.business.CarResourceBO(con);
		CarInfoTable table = new CarInfoTable();
		
		
		Statement stmt = null;
		ResultSet rs = null;
		ArrayList table_list = new ArrayList();
		ArrayList car_list = new ArrayList();

		car_list = (ArrayList)getCarList();

		Iterator car_iter = car_list.iterator();

		while(car_iter.hasNext()){
			table = (CarInfoTable)car_iter.next();
	
			String cid = table.getCid();			// 차량 마스터키
			String car_no = table.getCarNo();		// 차량 번호
			String car_type = table.getCarType();	// 차종
			String model_name = table.getModelName(); // 차 모델명
			String car_stat = table.getStat();

			//각 차량의 사용 이력을 가져온다.
			String query = "SELECT c_id,write_id,write_name,user_id,user_name,ac_name,fellow_names,v_status,u_year,u_month,u_date,u_time,c_year,c_month,c_date,c_time,cr_dest,content FROM charyang_master WHERE c_id = '" + cid + "' and ( v_status = '1' or v_status='3' or v_status='5')";
			stmt = con.createStatement();
			rs = stmt.executeQuery(query);
			
			CarUseInfoTable table2 = new CarUseInfoTable();
			if(rs.next()){
				table2.setCid(rs.getString("c_id"));
				table2.setWriteId(rs.getString("write_id"));
				table2.setWriteName(rs.getString("write_name"));
				table2.setUserId(rs.getString("user_id"));
				table2.setUserName(rs.getString("user_name"));
				table2.setAcName(rs.getString("ac_name"));
				table2.setFellowNames(rs.getString("fellow_names"));
				table2.setVstatus(carResourceBO.getStatName(rs.getString("v_status")));
				table2.setUyear(rs.getString("u_year"));
				table2.setUmonth(rs.getString("u_month"));
				table2.setUtime(rs.getString("u_time"));
				table2.setUdate(rs.getString("u_date"));
				table2.setTuYear(rs.getString("c_year"));
				table2.setTuMonth(rs.getString("c_month"));
				table2.setTuTime(rs.getString("c_time"));
				table2.setTuDate(rs.getString("c_date"));
				table2.setCrDest(rs.getString("cr_dest"));
				table2.setContent(rs.getString("content"));
				table2.setCarType(car_type);
				table2.setCarNo(car_no);
				table2.setModelName(model_name);
				table2.setCarStat(car_stat);

				//System.out.println(rs.getString("c_id"));
			} else { 
				table2.setCid(cid);
				table2.setUserId("");
				table2.setUserName("");
				table2.setAcName("");
					if(car_stat.equals("5")) { 
					table2.setVstatus(carResourceBO.getStatName("5"));					
					} else {
					table2.setVstatus(carResourceBO.getStatName("1"));
					}
				table2.setUyear("");
				table2.setUmonth("");
				table2.setUtime("");
				table2.setUdate("");
				table2.setTuYear("");
				table2.setTuMonth("");
				table2.setTuTime("");
				table2.setTuDate("");
				table2.setCarType(car_type);
				table2.setCarNo(car_no);
				table2.setModelName(model_name);		
				table2.setCarStat(car_stat);
			}
				
				table_list.add(table2);
		}
		rs.close();
		stmt.close();

		return table_list;
	}

   /*********************************************************
   * 선택된 각 차량 배차 정보를 가져온다. (입고처리/정보보기 화면정보 셀렉트)
   *********************************************************/
	public CarUseInfoTable getEachCarUseInfo(String cr_id,String login_id,String login_name) throws Exception{

		com.anbtech.br.entity.CarUseInfoTable eachUseinfo = new com.anbtech.br.entity.CarUseInfoTable();
		
		Statement stmt = null;
		ResultSet rs = null;
		
		String query = "SELECT cr_id,user_id,user_name,user_code,user_rank,ac_id,ac_code,ac_name,fellow_names,in_date,return_date,v_status,u_year,u_month,u_date,u_time,tu_year,tu_month,tu_date,tu_time,c_year,c_month,c_date,c_time,cr_purpose,cr_dest,mgr_id,mgr_name,em_tel,content,chg_cont FROM charyang_master WHERE cr_id ='"+cr_id+"'";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
	
		if(rs.next()){
			eachUseinfo.setCrId(rs.getString("cr_id"));
			eachUseinfo.setUserId(rs.getString("user_id"));
			eachUseinfo.setUserName(rs.getString("user_name"));
			eachUseinfo.setUserCode(rs.getString("user_code"));
			eachUseinfo.setUserRank(rs.getString("user_rank"));
			eachUseinfo.setAcId(rs.getString("ac_id"));
			eachUseinfo.setAcCode(rs.getString("ac_code"));
			eachUseinfo.setAcName(rs.getString("ac_name"));
			eachUseinfo.setFellowNames(rs.getString("fellow_names"));
			eachUseinfo.setInDate(rs.getString("in_date"));
			eachUseinfo.setReturnDate(rs.getString("return_date"));	//
			eachUseinfo.setVstatus(rs.getString("v_status"));
			eachUseinfo.setUyear(rs.getString("u_year"));
			eachUseinfo.setUmonth(rs.getString("u_month"));
			eachUseinfo.setUdate(rs.getString("u_date"));
			eachUseinfo.setUtime(rs.getString("u_time"));
			eachUseinfo.setTuYear(rs.getString("tu_year"));
			eachUseinfo.setTuMonth(rs.getString("tu_month"));
			eachUseinfo.setTuDate(rs.getString("tu_date"));
			eachUseinfo.setTuTime(rs.getString("tu_time"));
			eachUseinfo.setCyear(rs.getString("c_year"));		//
			eachUseinfo.setCmonth(rs.getString("c_month"));		//
			eachUseinfo.setCdate(rs.getString("c_date"));		//
			eachUseinfo.setCtime(rs.getString("c_time"));		//
			eachUseinfo.setCrPurpose(rs.getString("cr_purpose"));
			eachUseinfo.setCrDest(rs.getString("cr_dest"));
			//eachUseinfo.setMgrId(login_id);
			//eachUseinfo.setMgrName(login_name);
			eachUseinfo.setMgrId(rs.getString("mgr_id"));
			eachUseinfo.setMgrName(rs.getString("mgr_name"));
			eachUseinfo.setEmTel(rs.getString("em_tel"));
			eachUseinfo.setContent(rs.getString("content"));
			eachUseinfo.setChgCont(rs.getString("chg_cont"));	//

		}

		stmt.close();
		rs.close();

		return eachUseinfo;
	}

	/***********************
	 * 차량예약정보 등록
	 ***********************/
	 public void saveLendingCar(String cr_id,String c_id,String write_id,String write_name,String user_id,String user_name,String user_code,String user_rank,String ac_id,String ac_code,String ac_name,String fellow_names,String u_year,String u_month,String u_date,String u_time,String tu_year,String tu_month,String tu_date,String tu_time,String cr_purpose,String cr_dest,String content,String em_tel,String flag,String flag2) throws Exception
	{
		
		PreparedStatement pstmt = null;

		//등록시간
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd hh:mm");
		String w_time = vans.format(now);

		String query = "INSERT INTO charyang_master (cr_id,c_id,write_id,write_name,user_id,user_name,user_code,user_rank,ac_id,ac_code,ac_name,fellow_names,in_date,v_status,u_year,u_month,u_date,u_time,tu_year,tu_month,tu_date,tu_time,cr_purpose,cr_dest,content,c_year,c_month,c_date,c_time,em_tel,flag,flag2) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		
		pstmt = con.prepareStatement(query);
		
		pstmt.setString(1,cr_id);
		pstmt.setString(2,c_id);	
		pstmt.setString(3,write_id);
		pstmt.setString(4,write_name);
		pstmt.setString(5,user_id);
		pstmt.setString(6,user_name);
		pstmt.setString(7,user_code);
		pstmt.setString(8,user_rank);
		pstmt.setString(9,ac_id);
		pstmt.setString(10,ac_code);
		pstmt.setString(11,ac_name);
		pstmt.setString(12,fellow_names);
		pstmt.setString(13,w_time);
		pstmt.setString(14,"2");
		pstmt.setString(15,u_year);
		pstmt.setString(16,u_month);
		pstmt.setString(17,u_date);
		pstmt.setString(18,u_time);
		pstmt.setString(19,tu_year);
		pstmt.setString(20,tu_month);
		pstmt.setString(21,tu_date);
		pstmt.setString(22,tu_time);
		pstmt.setString(23,cr_purpose);
		pstmt.setString(24,cr_dest);
		pstmt.setString(25,content);
		pstmt.setString(26,tu_year);
		pstmt.setString(27,tu_month);
		pstmt.setString(28,tu_date);
		pstmt.setString(29,tu_time);
		pstmt.setString(30,em_tel);
		pstmt.setString(31,flag);
		pstmt.setString(32,flag2);
		
		pstmt.executeUpdate();
		pstmt.close();
		
	}

	/*****************************************************************************
	 *  해당 차량 예약일자 가져오기
	 *****************************************************************************/
	public ArrayList getSavedate(String cid) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;

		String query = "SELECT count(*) FROM charyang_master WHERE c_id = '"+cid+"'";
		stmt=con.createStatement();
		rs=stmt.executeQuery(query);
		int j=0;
		if(rs.next()){
			j=rs.getInt(1);
		}

		ArrayList arr_list = new ArrayList();
		String str[]=new String[j*2];

		query = "SELECT u_year,u_month,u_date,u_time,c_year,c_month,c_date,c_time FROM charyang_master WHERE c_id ='"+cid+"' and (v_status = '2' or v_status = '3') and flag != 'EN'";
		stmt=con.createStatement();
		rs=stmt.executeQuery(query);
		
		int i=0;
		while(rs.next()){

			String u_year=rs.getString("u_year");
			String u_month=rs.getString("u_month");
			String u_date=rs.getString("u_date");
			String u_time=rs.getString("u_time");
			
			u_time = u_time.substring(0,2)+u_time.substring(3,5);
			str[i] = u_year+u_month+u_date+u_time;
			arr_list.add(str[i]);

			String c_year=rs.getString("c_year");
			String c_month=rs.getString("c_month");
			String c_date=rs.getString("c_date");
			String c_time=rs.getString("c_time");

			c_time = c_time.substring(0,2)+c_time.substring(3,5);
			str[i+=1] = c_year+c_month+c_date+c_time;
			arr_list.add(str[i]);
			i+=1;
		}
		rs.close();
		stmt.close();

		return arr_list;
			
	}

	/*****************************************************************************
	 *  차량 입고 처리
	 *****************************************************************************/
	public void enteringCar(String cid,String cr_id, String  mgr_id, String mgr_name, String st,String chg_cont,String w_time,String y,String m,String d,String t) throws Exception {

		Statement stmt=null;
				
		String query = "UPDATE  charyang_master SET mgr_id='"+mgr_id+"', mgr_name='"+mgr_name+"',entering_state='"+st+"', return_date='"+w_time+"',v_status='7',chg_cont='"+chg_cont+"' , c_year='"+y+"',c_month='"+m+"',c_date='"+d+"',c_time='"+t+"' WHERE cr_id='"+cr_id+"'";

		stmt=con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	
	/*****************************************************************************
	 *  차량 배차 처리
	 *****************************************************************************/
	public void lendingProcess(String cr_id,String mgr_id,String mgr_name) throws Exception{

		Statement stmt=null;

		String query = "UPDATE charyang_master SET mgr_id='"+mgr_id+"', mgr_name='"+mgr_name+"', v_status='3' WHERE cr_id='"+cr_id+"'";
		stmt=con.createStatement();
		stmt.executeUpdate(query);

		stmt.close();
	}

	/*****************************************************************************
	 *  차량 배차예약 취소 
	 *****************************************************************************/
	public void cancelBooking(String cr_id) throws Exception{
		Statement stmt = null;
		String query = "UPDATE charyang_master SET v_status='4' WHERE cr_id='"+cr_id+"'";
		stmt=con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

	/*****************************************************************************
	 *  삭제 - TABLE charyang_master에서 flag(Field)가 EN인 모든 데이터 삭제 
	 ****************************************************************************/
	 public void delFlagEnCarInfo() throws Exception {
		Statement stmt=null;
		String query = "DELETE charyang_master WHERE flag='EN' AND flag='EN'";
		stmt = con.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}
		
	
	/*****************************************************************************
	 *  1차 결재 완료된 배차건 리스트 
	 *****************************************************************************/
	public ArrayList getFirstFlagList(String tablename,String mode,String searchword,String searchscope,String category,String page,String login_id,String cid) throws Exception {
		
		CarResourceBO carResourceBO = new CarResourceBO(con);
		CarInfoTable table = new CarInfoTable();
	
		Statement stmt = null;
		ResultSet rs = null;

		int l_maxlist = 15;
		int l_maxpage = 7;
		int current_page_num = Integer.parseInt(page);

		ArrayList table_list = new ArrayList();
		String where = carResourceBO.getWhere(mode,searchword,searchscope,category,login_id,cid);
		int total = getTotalCount(tablename, where);
		int recNum = total;

		String query = "SELECT * FROM " +tablename + where + " ORDER BY no DESC";
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);

		for(int i=0; i<(current_page_num - 1)*l_maxlist; i++){
			recNum--;
			rs.next();
		}

		CarUseInfoTable table2;
		
		for(int i=0; i < l_maxlist; i++){
			if(!rs.next()){break;}

			table2 = new CarUseInfoTable();
			table=(CarInfoTable)getCarInfo(rs.getString("c_id")); // 차정보

			table2.setCid(rs.getString("c_id"));
			table2.setCrId(rs.getString("cr_id"));
			table2.setWriteId(rs.getString("write_id"));
			table2.setWriteName(rs.getString("write_name"));
			table2.setUserId(rs.getString("user_id"));
			table2.setUserName(rs.getString("user_name"));
			table2.setAcName(rs.getString("ac_name"));
			table2.setFellowNames(rs.getString("fellow_names"));
			table2.setVstatus(carResourceBO.getStatName(rs.getString("v_status")));
			table2.setUyear(rs.getString("u_year"));
			table2.setUmonth(rs.getString("u_month"));
			table2.setUtime(rs.getString("u_time"));
			table2.setUdate(rs.getString("u_date"));
			table2.setTuYear(rs.getString("tu_year"));
			table2.setTuMonth(rs.getString("tu_month"));
			table2.setTuTime(rs.getString("tu_time"));
			table2.setTuDate(rs.getString("tu_date"));
			table2.setCrDest(rs.getString("cr_dest"));
			table2.setContent(rs.getString("content"));
			table2.setCarType(table.getCarType());
			table2.setCarNo(table.getCarNo());
			table2.setModelName(table.getModelName());
			table_list.add(table2);
			recNum--;
		}
		rs.close();
		stmt.close();

		return table_list;
	}

	/*******************************************************************
	 * 레코드의 전체 개수를 구한다.
	 *******************************************************************/
	public int getTotalCount(String tablename, String where) throws Exception{
		Statement stmt = null;
		ResultSet rs = null;
		int total_count = 0;

		String query = "SELECT COUNT(*) FROM " + tablename + where;
		stmt = con.createStatement();
		rs = stmt.executeQuery(query);
		rs.next();
		total_count = Integer.parseInt(rs.getString(1));

		stmt.close();
		rs.close();
		
		return total_count;
	}
}