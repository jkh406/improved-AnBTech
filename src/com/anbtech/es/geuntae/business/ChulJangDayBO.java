package com.anbtech.es.geuntae.business;
import com.anbtech.es.geuntae.db.*;
import java.util.Date;
import java.util.*;
import java.text.*;
import java.text.DecimalFormat;
import java.sql.*;

public class ChulJangDayBO { 
	private String year = "";
	private	String month = "";
	private	String date = "";
	private	String year2 = "";
	private	String month2 = "";
	private	String date2 = "";
	private String[][] lhd_list;	//매년같은날자의 법정휴일 
	private int lhd_cnt = 0;		//매년같은날자의 법정휴일 갯수
	private String[][] bhd_list;	//동년의 법정휴일 및 기타 휴일
	private int bhd_cnt = 0;		//동년의 법정휴일 및 기타 휴일 갯수

	/*******************************************************************
	 * 생성자
	 *******************************************************************/
	private Connection con;

	public ChulJangDayBO(Connection con){
		this.con = con;
	}

	public ChulJangDayBO(){
		
	}


  /*********************************************************
   * 시작일과 종료일이 같은 년도와 같은 월에 포함된 경우
   *********************************************************/
  public String getDayAtSameYearSameMonth(String inputDate,String inputDate2) throws Exception{
		DecimalFormat fmt = new DecimalFormat("00");
		String list = "";
		double rcount = 0.0;
		int diff = Integer.parseInt(date2) - Integer.parseInt(date);
		for(int i=0; i<=diff; i++){
			String ndate = year + month + fmt.format(Integer.parseInt(date) + i);;
			rcount = rcount + getDayCount(ndate);
		}
		list = year + month + "," + rcount + "|";

		return list;
  } // End of getDayAtSameYearSameMonth()


  /*********************************************************
   * 시작일이 포함된 월의 개수 구하기
   *********************************************************/
  public String getDayAtStartMonth() throws Exception{
	    DecimalFormat fmt = new DecimalFormat("00");
		int in_last_date = getDaysInMonth(Integer.parseInt(month),Integer.parseInt(year));
		int diff = in_last_date - Integer.parseInt(date);

		String list = "";
		double rcount = 0.0;
		for(int i=0; i<=diff; i++){
			String ndate = year + month + fmt.format(Integer.parseInt(date) + i);
			rcount = rcount + getDayCount(ndate);
		}
		list = year + month + "," + rcount + "|";
		return list;
  } // End of getDayAtStartMonth()

  /*********************************************************
   * 종료일이 포함된 월의 개수 구하기
   *********************************************************/
  public String getDayAtEndMonth() throws Exception{
		DecimalFormat fmt = new DecimalFormat("00");
		String list = "";
		double rcount = 0.0;
		for(int i=1; i<=Integer.parseInt(date2); i++){
			String ndate = year2 + month2 + fmt.format(i);
			rcount = rcount + getDayCount(ndate);
		}
		list = year2 + month2 + "," + rcount + "|";
		return list;
  } // End of getDayAtEndMonth()

  /*********************************************************
   * 같은 년도에 중간에 낀 월의 개수 구하기
   *********************************************************/
  public String getDayInMonth() throws Exception{
		DecimalFormat fmt = new DecimalFormat("00");
		String list = "";

		for(int nmonth = Integer.parseInt(month) + 1; nmonth < Integer.parseInt(month2); nmonth++){
			int nmonth_last_day = getDaysInMonth(nmonth,Integer.parseInt(year));

			double rcount = 0.0;
			for(int nday = 1; nday <= nmonth_last_day; nday++){
				String ndate = year + fmt.format(nmonth) + fmt.format(nday);
				rcount = rcount + getDayCount(ndate);
			}
			list += year + fmt.format(nmonth) + "," + rcount + "|";
		}
		return list;
  } // End of getDayInMonth()

  /*********************************************************
   * 시작년도에 포함된 월의 개수 구하기(시작월일이 포함된 월은 제외
   *********************************************************/
   public String getDayAtStartYear() throws Exception{
		DecimalFormat fmt = new DecimalFormat("00");
		String list = "";

		for(int nmonth = Integer.parseInt(month) + 1; nmonth <= 12; nmonth++){
			int nmonth_last_day = getDaysInMonth(nmonth,Integer.parseInt(year));

			double rcount = 0.0;
			for(int nday = 1; nday <= nmonth_last_day; nday++){
				String ndate = year + fmt.format(nmonth) + fmt.format(nday);
				rcount = rcount + getDayCount(ndate);
			}
			list += year + fmt.format(nmonth) + "," + rcount +"|";
		}
		return list;
   } // End of getDayAtStartYear()

  /*********************************************************
   * 종료년도에 포함된 월의 개수 구하기(종료월일이 포함된 월은 제외
   *********************************************************/
   public String getDayAtEndYear() throws Exception{
		DecimalFormat fmt = new DecimalFormat("00");
		String list = "";

		for(int nmonth = 1; nmonth < Integer.parseInt(month2) ; nmonth++){
			int nmonth_last_day = getDaysInMonth(nmonth,Integer.parseInt(year2));

			double rcount = 0.0;
			for(int nday = 1; nday <= nmonth_last_day; nday++){
				String ndate = year + fmt.format(nmonth) + fmt.format(nday);
				rcount = rcount + getDayCount(ndate);
			}
			list += year2 + fmt.format(nmonth) + "," + rcount + "|";
		}
		return list;
   } // End of getDayAtEndYear()


  /*********************************************************
   * 중간에 낀 년도에 포함된 월 구하기
   *********************************************************/
   public String getDayInYear() throws Exception{
		DecimalFormat fmt = new DecimalFormat("00");
		String list = "";

		for(int nyear = Integer.parseInt(year) + 1; nyear < Integer.parseInt(year2); nyear++){
			for(int nmonth = 1; nmonth <=12; nmonth++){
				int nmonth_last_day = getDaysInMonth(nmonth,nyear);

				double rcount = 0.0;
				for(int nday = 1; nday <= nmonth_last_day; nday++){
					String ndate = nyear + fmt.format(nmonth) + fmt.format(nday);
					rcount = rcount + getDayCount(ndate);
				}
				list += nyear + fmt.format(nmonth) + "," + rcount + "|";
			}
		}
		return list;
   } // End of getDayInYear()


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
   * 일요일,토요일,공휴일에 따른 일수를 계산하여 리턴한다.
   * 법정 공휴일 및 임시 휴일도 여기서 고려해야 한다.
   *********************************************************/
  public double getDayCount(String ndate){
		double rcount = 1.0;
		return rcount;
  } // End of getDayCount()

  /*********************************************************
   * 년월별 수량 구하기 (yyyyMM,xx|yyyyMM,xx ...)
   *********************************************************/
   public String getCount(String inputDate,String inputDate2) throws Exception {
		this.year = inputDate.substring(0,4);
		this.month = inputDate.substring(4,6);
		this.date = inputDate.substring(6,8);

		this.year2 = inputDate2.substring(0,4);
		this.month2 = inputDate2.substring(4,6);
		this.date2 = inputDate2.substring(6,8);

		String list = "";

		//년도와 달이 같은 경우
		if(year.equals(year2) && month.equals(month2)){
			list = getDayAtSameYearSameMonth(inputDate,inputDate2);
		}
		//년도는 같지만 시작일과 종료일의 달이 다른 경우(2달에 걸쳐지는 경우)
		else if(year.equals(year2) && (Integer.parseInt(month2)-Integer.parseInt(month)) <= 1){
			list = getDayAtStartMonth();
			list += getDayAtEndMonth();
		}
		//년도는 같지만 시작일과 종료일의 달이 다른 경우(3달이상 걸쳐지는 경우)
		else if(year.equals(year2) && (Integer.parseInt(month2)-Integer.parseInt(month)) > 1){
			list = getDayAtStartMonth();
			list += getDayAtEndMonth();
			list += getDayInMonth();
		}
		//년도가 바뀌는 경우(2년에 걸쳐지는 경우)
		else if((Integer.parseInt(year2)-Integer.parseInt(year)) <= 1){
			list = getDayAtStartMonth();
			list += getDayAtEndMonth();
			list += getDayAtStartYear();
			list += getDayAtEndYear();
		}
		//년도가 바뀌는 경우(3년 이상에 걸쳐지는 경우)
		else if((Integer.parseInt(year2)-Integer.parseInt(year)) > 1){
			list = getDayAtStartMonth();
			list += getDayAtEndMonth();
			list += getDayAtStartYear();
			list += getDayAtEndYear();
			list += getDayInYear();
		}

		//System.out.println(list);
		return list;

   }

} 