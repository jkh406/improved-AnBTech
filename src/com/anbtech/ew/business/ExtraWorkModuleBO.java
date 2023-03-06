package com.anbtech.ew.business;

import com.anbtech.ew.entity.*;
import com.anbtech.ew.db.*;
import java.text.DecimalFormat;
import com.anbtech.util.CalendarBean;
import com.oreilly.servlet.MultipartRequest;
import com.anbtech.text.Hanguel;
import java.text.NumberFormat;

import java.sql.*;
import java.util.*;
import java.io.*;


public class ExtraWorkModuleBO
{
	private Connection con;

	public ExtraWorkModuleBO(Connection con){
		this.con = con;
	}

	/**************************
	 * �ڵ庰 �ڵ�� ��������
	 **************************/
	public String getStatname(String code) throws Exception {
		String stat_name = "";
		if(code.equals("1"))		stat_name = "�����";
		else if(code.equals("2"))	stat_name = "����������";
		else if(code.equals("3"))	stat_name = "����";
		else if(code.equals("4"))	stat_name = "�ݷ�";
		else if(code.equals("5"))   stat_name = "����Ȯ��";
		else if(code.equals("6"))   stat_name = "��ħȮ��";
		else if(code.equals("7"))   stat_name = "";
		else if(code.equals("9"))	stat_name = "���";
		else if(code.equals("0"))	stat_name = "����";
		
		else if(code.equals("n"))   stat_name = "����";
		else if(code.equals("s"))   stat_name = "�����";
		else if(code.equals("h"))   stat_name = "����";
		
		else if(code.equals("d"))   stat_name = "����";
		else if(code.equals("t"))   stat_name = "����";  // ����

		else if(code.equals("yp"))  stat_name = "���";
		else if(code.equals("mp"))  stat_name = "����";
		else if(code.equals("tp"))  stat_name = "�ð���";

		else if(code.equals("ds"))  stat_name = "�⺻��";
		else if(code.equals("ts"))  stat_name = "����";

		else if(code.equals("r"))   stat_name = "������";
		else if(code.equals("b"))   stat_name = "��������";
		else if(code.equals("c"))   stat_name = "�����";
		else if(code.equals("a"))   stat_name = "�Ƹ�����Ʈ";
		else if(code.equals("f"))   stat_name = "�İ���";
		else stat_name="";

		return stat_name;
	}

	/***********************
	 * ������ �������� ����
	 ***********************/
	public String getWhere(String mode,String login_id) throws Exception {
		
//		String where= " WHERE member_id = '" + login_id + "' AND status IN('1','4')";
		String where= " WHERE member_id = '" + login_id + "'";

		return where;
	}

	/***************************************
	 * Ư�ٽ�û ����Ʈ�� �� �� �ʿ��� ��ũ ����
	 ***************************************/
	public EWLinkTable getRedirect(String mode,String login_id,String page) throws Exception{
		int l_maxlist = 15;
		int l_maxpage = 7;
		
		ExtraWorkModuleBO ewBO = new ExtraWorkModuleBO(con);
		ExtraWorkModuleDAO ewDAO = new ExtraWorkModuleDAO(con);

		String where = ewBO.getWhere(mode,login_id);
		int total = ewDAO.getTotalCount("ew_history",where);

		// ��ü �������� ���� ���Ѵ�.
		int totalpage = (int)(total / l_maxlist);
		if(totalpage*l_maxlist  != total)
			totalpage = totalpage + 1;

		// ������������ �������������� ����
		int startpage = (int)((Integer.parseInt(page) - 1) / l_maxpage) * l_maxpage + 1;
		int endpage= (int)((((startpage - 1) + l_maxpage) / l_maxpage) * l_maxpage);
	
		// ������ �̵����� ���ڿ��� ���� ����. ��, [prev] [1][2][3] [next]
		String pagecut = "";
		
		int curpage = 1;
		if (totalpage <= endpage)
			endpage = totalpage;
		
		if (Integer.parseInt(page) > l_maxpage){
			curpage = startpage -1;
			pagecut = "<a href=ExtraWorkServlet?mode="+mode+"&page=" + curpage +  ">[Prev]</a>";
		}

		curpage = startpage;

		while(curpage<=endpage){
			if (curpage == Integer.parseInt(page)){
				if (totalpage != 1)
					pagecut = pagecut + curpage;
			}else {
				pagecut = pagecut + "<a href=ExtraWorkServlet?mode="+mode+"&page=" + curpage  +">[" + curpage + "]</a>";
			}
		
			curpage++;
		}
		
		if (totalpage > endpage){
			curpage = endpage + 1;
			pagecut = pagecut + "<a href=ExtraWorkServlet?mode="+mode+"&page=" + curpage + ">[Next]</a>";
		}
		
		EWLinkTable link =  new EWLinkTable();
	
		link.setViewPagecut(pagecut);
		link.setViewTotal(total);
		link.setViewBoardpage(page);
		link.setViewtotalpage(totalpage);
		
		return link;
	}

	/************
	 * ���� ó��
	 ************/
	public void ewAppInfoProcess(String ewid, String mno, String mode) throws Exception {	
		ExtraWorkModuleDAO extraWorkModuleDAO = new ExtraWorkModuleDAO(con);
		com.anbtech.admin.db.ApprovalInfoMgrDAO appDAO = new com.anbtech.admin.db.ApprovalInfoMgrDAO(con);
		
		String status="";
		
		//���� ��Ž�
		if("submit".equals(mode)){
			// 1�� �̸� status = "2", 2���� status = "4" ���
			status = "2";
			
			//Ư�ٽ�û���� ������ȣ�� �����ͼ� �� �ǿ� ���� �����ڵ带 ������Ʈ�Ѵ�.
			String ono_plus = extraWorkModuleDAO.getOnoPlus(mno);
			extraWorkModuleDAO.updateStatus(ono_plus,status,"");
		}

		//���� �ݷ���
		else if("reject".equals(mode)){
			status = "4"; // �ݷ� ó���ڵ�
		
			// ������ aid�� ���� ���� �ҷ����� �����ϱ�
			appDAO.getAppInfoAndSave("ew_app_save",ewid);

			//ew_master ���̺� ���ڰ��� ������ȣ�� �Է��ϰ� �����ڵ带 �����Ѵ�.
			extraWorkModuleDAO.setEwid(ewid,mno,status);

			//Ư�ٽ�û���� ������ȣ�� �����ͼ� �� �ǿ� ���� �����ڵ带 ������Ʈ�Ѵ�.
			String ono_plus = extraWorkModuleDAO.getOnoPlus(mno);
			extraWorkModuleDAO.updateStatus(ono_plus,status,ewid);
			
			//���Ϲ߼�ó��
//			String decision2 = extraWorkModuleDAO.getDecision(mno); // ���� ó���� ID��������
//			sendMail("",mno,decision2,"n");
		}

		//���� ���ν�
		else if("approval".equals(mode)) {
			status = "3"; // 1�� �̸� status = "3", 2���� status = "5"
		
			// ������ aid�� ���� ���� �ҷ����� �����ϱ�
			appDAO.getAppInfoAndSave("ew_app_save",ewid);

			//ew_master ���̺� ���ڰ��� ������ȣ�� �Է��ϰ� �����ڵ带 �����Ѵ�.
			extraWorkModuleDAO.setEwid(ewid,mno,status);

			//Ư�ٽ�û���� ������ȣ�� �����ͼ� �� �ǿ� ���� �����ڵ带 ������Ʈ�Ѵ�.
			String ono_plus = extraWorkModuleDAO.getOnoPlus(mno);
			extraWorkModuleDAO.updateStatus(ono_plus,status,ewid);

			//geuntae_count ���̺��� ������ ���κ�,���� Ư�ټ����� ������Ʈ�Ѵ�.
			updateGeunTaeCount(ono_plus);

			//���Ϲ߼�ó��
			//String decision = extraWorkModuleDAO.getDecision(mno);			// �ۼ��� ID��������
			//sendMail("",mno,decision,"y");
		}
	}

	/******************************************
	* ���õ� Ư�ٰǿ� ���� ����ó���� �Ѵ�.
	*******************************************/
	public void processJungsan(String login_id,String nos) throws Exception {
		ExtraWorkHistoryTable history_table = new ExtraWorkHistoryTable();
		MemberPayInfoTable member_table = new MemberPayInfoTable();;
		ExtraWorkModuleDAO ewDAO = new ExtraWorkModuleDAO(con);

		//Ư�ٽ�û�Ǻ��� �и�
		java.util.StringTokenizer st = new java.util.StringTokenizer(nos,";");
		int tokens_count = st.countTokens();

		//����ð�
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyyMMdd");
		String w_time = vans.format(now);
		String w_year = w_time.substring(0,4);

		//Ư�������� ���� �������� 
		com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable user_info = new com.anbtech.admin.entity.UserInfoTable();
		user_info = (com.anbtech.admin.entity.UserInfoTable)userinfoDAO.getUserListById(login_id);
		String login_name = user_info.getUserName();

		for (int j = 0 ; j<tokens_count ; j++)
		{
			String no = st.nextToken();
			//Ư�ٽ�û���� ��������
			history_table = ewDAO.getHistoryInfo(no);
			//Ư�ٽð� ��������
			String total_time = history_table.getTotalTime();

			//����ð� ��� - 60�� �̸��� Ư���� �����󿡼� ������.
			int result_time = Integer.parseInt(total_time) / 60;
			//Ư������ �ñ����� ��������
			member_table = ewDAO.getMemberPayInfo(history_table.getMemberId(),w_year);
			String hourly_pay = member_table.getHourlyPay();
			//Ư�ټ�����(�ñ�*�ٹ��ð�)
			String pay_by_work = Double.toString(Double.parseDouble(hourly_pay) * result_time);

			//������ ������Ʈ
			ewDAO.updateJungsanResult(no,hourly_pay,pay_by_work,login_id,login_name,w_time,Integer.toString(result_time));
		}
	}

	/******************************************
	* ���κ� ���� Ư����Ȳ�� �޷����·� ���
	*******************************************/
	public ArrayList getPersonalStatusByMonth(String year,String month,String login_id) throws Exception {
		ExtraWorkModuleDAO ewDAO = new ExtraWorkModuleDAO(con);
		ExtraWorkHistoryTable ewtable = new ExtraWorkHistoryTable();
		
		//���� ���� Ư�� ��Ȳ
		ArrayList ew_table = new ArrayList();
		ew_table = (ArrayList)ewDAO.getTableListByPerson(year,month,login_id);

	
		//########## �޷� �������� ��º� ���� ##################
		DecimalFormat fmt = new DecimalFormat("00");
		Calendar curren_date,temp_date;

		int cur_year = Integer.parseInt(year);
		int cur_month = Integer.parseInt(month);

		//������ �ϴ� ����� �����Ѵ�.
		temp_date = Calendar.getInstance();
		temp_date.set(Calendar.YEAR,cur_year);
		temp_date.set(Calendar.MONTH,cur_month-1);
		temp_date.set(Calendar.DATE,1);

		com.anbtech.util.CalendarBean calendar = new com.anbtech.util.CalendarBean();
		calendar.setNewDate(temp_date);

		int dayno = calendar.getMyWeek();
		int lastday = calendar.getMyLastDay();

		//������ �ϴ� ��� ����
		String cur_yyyymm = cur_year + fmt.format(cur_month);

		int inx;  
		String wdate		= "";	// �����(20030927)
		String wcontents	= "";	// ���� ����
		String url			= "";	// �ش� ��¥�� ������ ��ũ ���ڿ�
		String day			= "";	// �ش� ��¥
		String ys_kind		= "";	// ���� ����
		
		String w_sdate		= "";	// ��������
		String w_stime		= "";	// ���۽ð�
		String w_edate		= "";	// ��ħ����
		String w_etime		= "";	// ��ħ�ð�
		String total_time	= "";	// �����ٹ��ð�
		String result_time	= "";	// ����ð�
		String pay_by_work	= "";	// Ư�ټ���
		String confirm_date	= "";	// ��������

		inx = ((-1) * dayno) + 2 ;


		// �޷����� ����Ʈ�� ���� ���� ����
		com.anbtech.ew.entity.ExtraWorkHistoryTable table_1 = new com.anbtech.ew.entity.ExtraWorkHistoryTable();
		ArrayList table_list_1 = new ArrayList();
		
		while (inx < lastday){
					
			//######�Ͽ���########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);

				w_sdate	= "";
				w_stime = "";
				w_edate = "";
				w_etime = "";
				wcontents = "";				
				total_time = "";
				result_time = "";
				pay_by_work = "";
				confirm_date = "";

				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate(); // day = yyymmdd
					
					if(day.equals(wdate)){
						w_sdate = ewtable.getWsdate();
						w_stime = ewtable.getWstime();
						w_edate = ewtable.getWedate();
						w_etime = ewtable.getWetime();
						if(!w_sdate.equals(w_edate)) w_etime = "���� " + w_etime;
						wcontents = ewtable.getDuty();						
						total_time = ewtable.getTotalTime();
						result_time = ewtable.getResultTime();
						pay_by_work = ewtable.getPayByWork();
						confirm_date = ewtable.getConfirmDate();
					}
				}
				url = "<font color='red'>"+inx+"(��)</font>";

				table_1 = new ExtraWorkHistoryTable();
				table_1.setDay(url);
				table_1.setWstime(w_stime);
				table_1.setWetime(w_etime);
				table_1.setDuty(wcontents);
				table_1.setTotalTime(total_time);
				table_1.setResultTime(result_time);
				table_1.setPayByWork(pay_by_work);
				table_1.setConfirmDate(confirm_date);
				
				table_list_1.add(table_1);
				
			}
			inx++;


			//######������########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);

				w_sdate	= "";
				w_stime = "";
				w_edate = "";
				w_etime = "";
				wcontents = "";				
				total_time = "";
				result_time = "";
				pay_by_work = "";
				confirm_date = "";

				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate(); // day = yyymmdd
					
					if(day.equals(wdate)){
						w_sdate = ewtable.getWsdate();
						w_stime = ewtable.getWstime();
						w_edate = ewtable.getWedate();
						w_etime = ewtable.getWetime();
						if(!w_sdate.equals(w_edate)) w_etime = "���� " + w_etime;
						wcontents = ewtable.getDuty();						
						total_time = ewtable.getTotalTime();
						result_time = ewtable.getResultTime();
						pay_by_work = ewtable.getPayByWork();
						confirm_date = ewtable.getConfirmDate();
					}
				}
				url = inx + "(��)";

				table_1 = new ExtraWorkHistoryTable();
				table_1.setDay(url);
				table_1.setWstime(w_stime);
				table_1.setWetime(w_etime);
				table_1.setDuty(wcontents);
				table_1.setTotalTime(total_time);
				table_1.setResultTime(result_time);
				table_1.setPayByWork(pay_by_work);
				table_1.setConfirmDate(confirm_date);
				
				table_list_1.add(table_1);
				
			}
			inx++;


			//######ȭ����########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);

				w_sdate	= "";
				w_stime = "";
				w_edate = "";
				w_etime = "";
				wcontents = "";				
				total_time = "";
				result_time = "";
				pay_by_work = "";
				confirm_date = "";

				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate(); // day = yyymmdd
					
					if(day.equals(wdate)){
						w_sdate = ewtable.getWsdate();
						w_stime = ewtable.getWstime();
						w_edate = ewtable.getWedate();
						w_etime = ewtable.getWetime();
						if(!w_sdate.equals(w_edate)) w_etime = "���� " + w_etime;
						wcontents = ewtable.getDuty();						
						total_time = ewtable.getTotalTime();
						result_time = ewtable.getResultTime();
						pay_by_work = ewtable.getPayByWork();
						confirm_date = ewtable.getConfirmDate();
					}
				}
				url = inx + "(ȭ)";

				table_1 = new ExtraWorkHistoryTable();
				table_1.setDay(url);
				table_1.setWstime(w_stime);
				table_1.setWetime(w_etime);
				table_1.setDuty(wcontents);
				table_1.setTotalTime(total_time);
				table_1.setResultTime(result_time);
				table_1.setPayByWork(pay_by_work);
				table_1.setConfirmDate(confirm_date);
				
				table_list_1.add(table_1);
				
			}
			inx++;

			//######������########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);

				w_sdate	= "";
				w_stime = "";
				w_edate = "";
				w_etime = "";
				wcontents = "";				
				total_time = "";
				result_time = "";
				pay_by_work = "";
				confirm_date = "";

				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate(); // day = yyymmdd
					
					if(day.equals(wdate)){
						w_sdate = ewtable.getWsdate();
						w_stime = ewtable.getWstime();
						w_edate = ewtable.getWedate();
						w_etime = ewtable.getWetime();
						if(!w_sdate.equals(w_edate)) w_etime = "���� " + w_etime;
						wcontents = ewtable.getDuty();						
						total_time = ewtable.getTotalTime();
						result_time = ewtable.getResultTime();
						pay_by_work = ewtable.getPayByWork();
						confirm_date = ewtable.getConfirmDate();
					}
				}
				url = inx + "(��)";

				table_1 = new ExtraWorkHistoryTable();
				table_1.setDay(url);
				table_1.setWstime(w_stime);
				table_1.setWetime(w_etime);
				table_1.setDuty(wcontents);
				table_1.setTotalTime(total_time);
				table_1.setResultTime(result_time);
				table_1.setPayByWork(pay_by_work);
				table_1.setConfirmDate(confirm_date);
				
				table_list_1.add(table_1);
			}
			inx++;

			//######�����########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);

				w_sdate	= "";
				w_stime = "";
				w_edate = "";
				w_etime = "";
				wcontents = "";				
				total_time = "";
				result_time = "";
				pay_by_work = "";
				confirm_date = "";

				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate(); // day = yyymmdd
					
					if(day.equals(wdate)){
						w_sdate = ewtable.getWsdate();
						w_stime = ewtable.getWstime();
						w_edate = ewtable.getWedate();
						w_etime = ewtable.getWetime();
						if(!w_sdate.equals(w_edate)) w_etime = "���� " + w_etime;
						wcontents = ewtable.getDuty();						
						total_time = ewtable.getTotalTime();
						result_time = ewtable.getResultTime();
						pay_by_work = ewtable.getPayByWork();
						confirm_date = ewtable.getConfirmDate();
					}
				}
				url = inx + "(��)";

				table_1 = new ExtraWorkHistoryTable();
				table_1.setDay(url);
				table_1.setWstime(w_stime);
				table_1.setWetime(w_etime);
				table_1.setDuty(wcontents);
				table_1.setTotalTime(total_time);
				table_1.setResultTime(result_time);
				table_1.setPayByWork(pay_by_work);
				table_1.setConfirmDate(confirm_date);
				
				table_list_1.add(table_1);
				
			}
			inx++;

			//######�ݿ���########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);

				w_sdate	= "";
				w_stime = "";
				w_edate = "";
				w_etime = "";
				wcontents = "";				
				total_time = "";
				result_time = "";
				pay_by_work = "";
				confirm_date = "";

				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate(); // day = yyymmdd
					
					if(day.equals(wdate)){
						w_sdate = ewtable.getWsdate();
						w_stime = ewtable.getWstime();
						w_edate = ewtable.getWedate();
						w_etime = ewtable.getWetime();
						if(!w_sdate.equals(w_edate)) w_etime = "���� " + w_etime;
						wcontents = ewtable.getDuty();						
						total_time = ewtable.getTotalTime();
						result_time = ewtable.getResultTime();
						pay_by_work = ewtable.getPayByWork();
						confirm_date = ewtable.getConfirmDate();
					}
				}
				url = inx + "(��)";

				table_1 = new ExtraWorkHistoryTable();
				table_1.setDay(url);
				table_1.setWstime(w_stime);
				table_1.setWetime(w_etime);
				table_1.setDuty(wcontents);
				table_1.setTotalTime(total_time);
				table_1.setResultTime(result_time);
				table_1.setPayByWork(pay_by_work);
				table_1.setConfirmDate(confirm_date);
				
				table_list_1.add(table_1);
				
			}
			inx++;

			//######�����########
			if (inx >= 1 && inx <= lastday){
				wdate =cur_yyyymm+fmt.format(inx);

				w_sdate	= "";
				w_stime = "";
				w_edate = "";
				w_etime = "";
				wcontents = "";				
				total_time = "";
				result_time = "";
				pay_by_work = "";
				confirm_date = "";

				Iterator table_iter2 = ew_table.iterator();
				while(table_iter2.hasNext()){
					ewtable = (ExtraWorkHistoryTable)table_iter2.next();
					day = ewtable.getRsdate(); // day = yyymmdd
					
					if(day.equals(wdate)){
						w_sdate = ewtable.getWsdate();
						w_stime = ewtable.getWstime();
						w_edate = ewtable.getWedate();
						w_etime = ewtable.getWetime();
						if(!w_sdate.equals(w_edate)) w_etime = "���� " + w_etime;
						wcontents = ewtable.getDuty();						
						total_time = ewtable.getTotalTime();
						result_time = ewtable.getResultTime();
						pay_by_work = ewtable.getPayByWork();
						confirm_date = ewtable.getConfirmDate();
					}
				}
				url = "<font color='blue'>"+inx+"(��)</font>";

				table_1 = new ExtraWorkHistoryTable();
				table_1.setDay(url);
				table_1.setWstime(w_stime);
				table_1.setWetime(w_etime);
				table_1.setDuty(wcontents);
				table_1.setTotalTime(total_time);
				table_1.setResultTime(result_time);
				table_1.setPayByWork(pay_by_work);
				table_1.setConfirmDate(confirm_date);
				
				table_list_1.add(table_1);
				
			}
			inx++;
		}//end while

		return table_list_1;
	}

	/******************************************
	 * ���ó���� Ư�ٰ��� ������ ������Ʈ�Ѵ�.
	 *
	 * (1) ew_history ���̺� ������Ʈ
	 *     - �� �ٹ���ħ���� r_edate ������Ʈ
	 *	   - �� �ٹ���ħ�ð� r_etime ������Ʈ
	 *     - ������ Ȯ���ڵ� ew_confirm = '6' ���� ����
	 *      (������ Ȯ�������� ���������� ���� ��ٽ� �ڵ����� Ȯ������ �����ϵ�
	 *	     ��ħ�ð��� Ư�ٽ�û ���۽ð����� 1�ð� ���Ķ�߸� Ư������ �����Ѵ�.)
	 *	   - ���� �ٹ��ð� total_time �� ����Ͽ� ������Ʈ
     *
	 * (2) Ư������ ������ �ǿ� ���Ͽ� Ư�ٽð��� geuntae_count ���̺� ���(������Ʈ)�Ѵ�.
	 ******************************************/
	public void processWorkOut(String user_id,String w_sdate,String r_edate,String r_etime) throws Exception{
		ExtraWorkModuleDAO ewDAO = new ExtraWorkModuleDAO(con);
		ExtraWorkModuleBO ewBO = new ExtraWorkModuleBO(con);
		ExtraWorkHistoryTable table = new ExtraWorkHistoryTable();

		//���½�û������ �����´�.
		table = ewDAO.getHistoryInfo(user_id,w_sdate);

		//���� �ٹ��ð�
		long total_time = getTimeDistance(w_sdate,table.getRstime(),r_edate,r_etime);

		if(total_time >= 60){ //���� �ٹ��ð��� �ѽð� �̻��� ���
			ewDAO.updateHistoryInfo(Integer.toString(table.getOno()),r_edate,r_etime,"6",Long.toString(total_time));
		}else{
			ewDAO.updateHistoryInfo(Integer.toString(table.getOno()),r_edate,r_etime,"5","0");		
		}
	}


	/******************************
	 * �� �ð� ������ �ð����� ���
	 ******************************/
	public long getTimeDistance(String w_sdate,String w_stime,String w_edate,String w_etime) throws Exception{
	   String s_time = w_sdate + " " + w_stime;
	   String e_time = w_edate + " " + w_etime;

	   java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyyMMdd hh:mm"); 
	   java.util.Date s_date = dateFormat.parse(s_time);
	   java.util.Date e_date = dateFormat.parse(e_time);

	   long millisDifference = e_date.getTime() - s_date.getTime();
	   long distance = millisDifference/(1000*60);

	   return distance;
	}

	/******************************
	 * ����(��ĥ��)���� �˾ƿ���
	 * input  : ��������( 20031214 ),����
	 * output : �˰� ���� ����
	 ******************************/
	public String getWantDate( String current_date, String day, String type ) throws Exception {
	
		int input_year = Integer.parseInt(current_date.substring(0,4));
		int input_month = Integer.parseInt(current_date.substring(4,6));
		int input_day = Integer.parseInt(current_date.substring(6,8));
		
		Calendar want_date;
		String s="";
		Calendar calendar = Calendar.getInstance(); 
		//** ������� �����Ѵ�.
		want_date = Calendar.getInstance();
		want_date.set(Calendar.YEAR,input_year);
		want_date.set(Calendar.MONTH,input_month-1);
		want_date.set(Calendar.DATE,input_day);
		
        if(type.equals("-"))  want_date.add(want_date.DATE, -(Integer.parseInt(day))); 
		else if(type.equals("+")) want_date.add(want_date.DATE, Integer.parseInt(day)); 
         
        String year = String.valueOf(want_date.get(Calendar.YEAR)); 
        String month = String.valueOf(want_date.get(Calendar.MONTH)+1); 
        String day_ = String.valueOf(want_date.get(Calendar.DATE)); 
		int day_int = Integer.parseInt(day_);
		int month_int = Integer.parseInt(month);

		DecimalFormat fmt = new DecimalFormat("00");
		
		String t = (String)fmt.format(day_int);
		month = (String)fmt.format(month_int);		
		String temp = year+""+month+""+t;

		return temp;

	}

	/**************************
	 * ������� �������� geuntae_count ���̺��� Ư���ϼ� ������Ʈ
	 **************************/
	public void updateGeunTaeCount(String ono_plus) throws Exception {
		ExtraWorkHistoryTable table = new ExtraWorkHistoryTable();
		ExtraWorkModuleDAO ewDAO = new ExtraWorkModuleDAO(con);
		com.anbtech.es.geuntae.db.HyuGaDayDAO hyugaDAO = new com.anbtech.es.geuntae.db.HyuGaDayDAO(con);
		com.anbtech.admin.db.UserInfoDAO userinfoDAO = new com.anbtech.admin.db.UserInfoDAO(con);
		com.anbtech.admin.entity.UserInfoTable user_info = new com.anbtech.admin.entity.UserInfoTable();
		String mid = "";

		java.util.StringTokenizer stokens = new java.util.StringTokenizer(ono_plus,";");
		int tokens_count = stokens.countTokens();
	
		for (int j = 0 ; j<tokens_count ; j++){			
			mid = stokens.nextToken();

			//Ư�ٽ�û���� ��������
			table = ewDAO.getHistoryInfo(mid);

			String member_id = table.getMemberId();
			String member_name = table.getMemberName();
			String rank_name = table.getMemberRankName();
			String division	= table.getDivision();		//���κμ��ڵ�
			String division_name = table.getDivisionName();
			String this_year = table.getWsdate().substring(0,4);
			String this_month = table.getWsdate().substring(4,6);
			String column_name = hyugaDAO.searchColumn(this_month);

			//Ư�ٴ������ �������� ��������
			user_info = (com.anbtech.admin.entity.UserInfoTable)userinfoDAO.getUserListById(member_id);
			String rank_code = user_info.getArCode();	//�����ڵ�
			String ac_code = user_info.getAcCode();		//�μ��ڵ�

			//�ش������� �ִ��� üũ�Ѵ�.
			boolean is_empty = hyugaDAO.isEmpty("EXTRAWORK","EW_001",member_id,ac_code,this_year);
			
			if(is_empty){ //������� ���� �߰�
				String gt_id = System.currentTimeMillis() + "";
				hyugaDAO.insertCountTable(gt_id,"EXTRAWORK","EW_001",member_id,member_name,rank_code,rank_name,ac_code,division,division_name,this_year,column_name,"1");

			}else{ //������� ���� ������Ʈ
				//���� ���� ��������
				String[] data = new String[2];	//�����ڵ�, �������
				data = hyugaDAO.getGTCount("EXTRAWORK","EW_001",member_id,ac_code,this_year,this_month);

				String new_count = Double.toString(Double.parseDouble(data[1]) + 1);
				//������Ʈ
				hyugaDAO.updateCountTable(data[0],column_name,new_count);
			}

		}
	}

	/******************************************
	* ���õ� Ư�ٰ��� ����ó���Ѵ�.
	*******************************************/
	public void deleteSelectedEwInfo(String nos) throws Exception {
		ExtraWorkHistoryTable history_table = new ExtraWorkHistoryTable();
		ExtraWorkModuleDAO ewDAO = new ExtraWorkModuleDAO(con);

		//Ư�ٽ�û�Ǻ��� �и�
		java.util.StringTokenizer st = new java.util.StringTokenizer(nos,";");
		int tokens_count = st.countTokens();

		for (int j = 0 ; j<tokens_count ; j++)
		{
			String no = st.nextToken();
			//Ư�ٽ�û���� ��������
			history_table = ewDAO.getHistoryInfo(no);

			//�����ڵ� ��������
			String status = history_table.getStatus();
			
			//���°� ����� �Ǵ� �ݷ��� �͸� ����ó���Ѵ�.
			if(status.equals("1") || status.equals("4")){
				ewDAO.processEWDel(no);
			}
		}
	}
}
