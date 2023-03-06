package com.anbtech.es.geuntae.business;
import com.anbtech.es.geuntae.db.*;
import java.util.Date;
import java.util.*;
import java.text.*;
import java.text.DecimalFormat;
import java.sql.*;

public class HyuGaDayBO { 
	private String year = "";
	private	String month = "";
	private	String date = "";
	private	String year2 = "";
	private	String month2 = "";
	private	String date2 = "";
	private String[][] lhd_list;	//�ųⰰ�������� �������� 
	private int lhd_cnt = 0;		//�ųⰰ�������� �������� ����
	private String[][] bhd_list;	//������ �������� �� ��Ÿ ����
	private int bhd_cnt = 0;		//������ �������� �� ��Ÿ ���� ����
	private com.anbtech.es.geuntae.db.scheduleHolidayDAO hdDAO;

	/*******************************************************************
	 * ������
	 *******************************************************************/
	private Connection con;

	public HyuGaDayBO(Connection con){
		this.con = con;
	}

	public HyuGaDayBO(){
		hdDAO = new com.anbtech.es.geuntae.db.scheduleHolidayDAO();
	}


  /*********************************************************
   * �����ϰ� �������� ���� �⵵�� ���� ���� ���Ե� ���
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
   * �������� ���Ե� ���� ���� ���ϱ�
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
   * �������� ���Ե� ���� ���� ���ϱ�
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
   * ���� �⵵�� �߰��� �� ���� ���� ���ϱ�
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
   * ���۳⵵�� ���Ե� ���� ���� ���ϱ�(���ۿ����� ���Ե� ���� ����
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
   * ����⵵�� ���Ե� ���� ���� ���ϱ�(��������� ���Ե� ���� ����
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
   * �߰��� �� �⵵�� ���Ե� �� ���ϱ�
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
   * ������ �⵵, ������ ���� �� ��¥ ���� ���Ѵ�.
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

    // m == 2 (�� 2��)
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
   * ������������ ���� ���ϱ�
   *********************************************************/
  public void getHolidayList() throws Exception{

		//�ų� ���������� ��������
		this.lhd_cnt = hdDAO.getHdCount("LHD");			
		lhd_list = new String[lhd_cnt][2];
		lhd_list = hdDAO.getHdAtEveryYear();
			
		//���� ��������
		this.bhd_cnt = hdDAO.getHdCount("BHD");			
		bhd_list = new String[bhd_cnt][3];
		bhd_list = hdDAO.getHdAtSameYear();

		//con �ݱ�
		hdDAO.close();
			
  }
  /*********************************************************
   * �Ͽ���,�����,�����Ͽ� ���� �ϼ��� ����Ͽ� �����Ѵ�.
   * ���� ������ �� �ӽ� ���ϵ� ���⼭ ����ؾ� �Ѵ�.
   *********************************************************/
  public double getDayCount(String ndate) throws Exception{
		Calendar cld = Calendar.getInstance();
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		Date myDate = df.parse(ndate);
		cld.setTime(myDate);

		String mmdd = ndate.substring(4,8);		//MMdd
		String flag = "0";						//��������(1) �ƴ���(0)
		getHolidayList();						//������������ ������ ��������
		//System.out.println("mmdd : " + ndate);
		double rcount = 0.0;
		if(cld.get(Calendar.DAY_OF_WEEK) == 1){		  // �Ͽ��� �Ǵ� �������� ���
			rcount = rcount + 0;
		}else if(cld.get(Calendar.DAY_OF_WEEK) == 7){ // ������� ���
			//�ųⰰ�� ������ ���� �������̸�
			for(int i=0; i<lhd_cnt; i++) {
				String md = lhd_list[i][0]+lhd_list[i][1];
				if(md.equals(mmdd)) flag = "1";
			}
			//������ ����������, ��Ÿ �������̸�
			for(int i=0; i<bhd_cnt; i++) {
				String ymd = bhd_list[i][0]+bhd_list[i][1]+bhd_list[i][2];
				if(ymd.equals(ndate)) flag = "1";
			}

			if(flag.equals("0"))	
				rcount = rcount + 0.5;

		}else{											//������ ���
			//�ųⰰ�� ������ ���� �������̸�
			for(int i=0; i<lhd_cnt; i++) {
				String md = lhd_list[i][0]+lhd_list[i][1];
				if(md.equals(mmdd)) flag = "1";
			}
			//������ ����������, ��Ÿ �������̸�
			for(int i=0; i<bhd_cnt; i++) {
				String ymd = bhd_list[i][0]+bhd_list[i][1]+bhd_list[i][2];
				if(ymd.equals(ndate)) flag = "1";
			}

			if(flag.equals("0")) 
				rcount = rcount + 1;
		}
		return rcount;
  } // End of getDayCount()

  /*********************************************************
   * ����� ���� ���ϱ� (yyyyMM,xx|yyyyMM,xx ...)
   *********************************************************/
   public String getCount(String inputDate,String inputDate2) throws Exception {
		this.year = inputDate.substring(0,4);
		this.month = inputDate.substring(4,6);
		this.date = inputDate.substring(6,8);

		this.year2 = inputDate2.substring(0,4);
		this.month2 = inputDate2.substring(4,6);
		this.date2 = inputDate2.substring(6,8);

		String list = "";

		//�⵵�� ���� ���� ���
		if(year.equals(year2) && month.equals(month2)){
			list = getDayAtSameYearSameMonth(inputDate,inputDate2);
		}
		//�⵵�� ������ �����ϰ� �������� ���� �ٸ� ���(2�޿� �������� ���)
		else if(year.equals(year2) && (Integer.parseInt(month2)-Integer.parseInt(month)) <= 1){
			list = getDayAtStartMonth();
			list += getDayAtEndMonth();
		}
		//�⵵�� ������ �����ϰ� �������� ���� �ٸ� ���(3���̻� �������� ���)
		else if(year.equals(year2) && (Integer.parseInt(month2)-Integer.parseInt(month)) > 1){
			list = getDayAtStartMonth();
			list += getDayAtEndMonth();
			list += getDayInMonth();
		}
		//�⵵�� �ٲ�� ���(2�⿡ �������� ���)
		else if((Integer.parseInt(year2)-Integer.parseInt(year)) <= 1){
			list = getDayAtStartMonth();
			list += getDayAtEndMonth();
			list += getDayAtStartYear();
			list += getDayAtEndYear();
		}
		//�⵵�� �ٲ�� ���(3�� �̻� �������� ���)
		else if((Integer.parseInt(year2)-Integer.parseInt(year)) > 1){
			list = getDayAtStartMonth();
			list += getDayAtEndMonth();
			list += getDayAtStartYear();
			list += getDayAtEndYear();
			list += getDayInYear();
		}

//		System.out.println(list);
		return list;

   }

  /***********************************************************************************
   * �Ի���(20030101)�� ���س�(2003)�� �Է��ϸ� ���س��� 12�� 31���� ��������
   * �ټӳ���� ����Ͽ� �����Ѵ�.
   ***********************************************************************************/
   public int getContinousYear(String enter_day,String year) throws Exception {
		SimpleTimeZone kst = new SimpleTimeZone(9*60*60*1000, "KST");
		Calendar cal1 = Calendar.getInstance(kst);
		Calendar cal2 = Calendar.getInstance(kst);

		int year1=0;
		int year2=0;
		int dayCount1 = 0;
		int dayCount2 = 0;

		cal1.set(Integer.parseInt(enter_day.substring(0,4)),Integer.parseInt(enter_day.substring(4,6)),Integer.parseInt(enter_day.substring(6,8)));
		cal2.set(Integer.parseInt(year),12,31);
		
		year1 = cal1.get(Calendar.YEAR);
		year2 = cal2.get(Calendar.YEAR);
		
		dayCount1 = (year1-1) * 365 + cal1.get(Calendar.DAY_OF_YEAR);
		dayCount2 = (year2-1) * 365 + cal2.get(Calendar.DAY_OF_YEAR);
		
//		System.out.println( (dayCount2-dayCount1)/365 );
		return (dayCount2-dayCount1)/365;
   }

	/***************************************************************************
	 * �������� ����ɶ� �ڿ��� ȸ���ϱ� ���� �޼ҵ� 
	 **************************************************************************/
	protected void finalize() throws Throwable 
	{
		if(hdDAO != null) {
			hdDAO.close();
		}
	}
} 
