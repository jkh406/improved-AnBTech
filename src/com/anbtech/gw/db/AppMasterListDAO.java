package com.anbtech.gw.db;
import com.anbtech.gw.entity.*;
import java.sql.*;
import java.util.*;
import java.util.StringTokenizer;
import com.anbtech.dbconn.DBConnectionManager;

public class AppMasterListDAO
{
	private DBConnectionManager connMgr;
	private Connection con;
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
	private int total_page = 0;
	private int current_page = 0;

	//*******************************************************************
	//	������ �����
	//*******************************************************************/
	public AppMasterListDAO(Connection con) 
	{
		this.con = con;
	}

	public AppMasterListDAO() 
	{
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
	}

	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : �̰��� APP_MASTER
	//--------------------------------------------------------------------
	public int getTotalApp(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_MASTER ";
		//������ �����϶� (����,���� ����)
		String ING_data = "where (app_state='APV' and reviewer='" + id + "') or (app_state='APL' and decision='" + id + "') or (agree_method='SERIAL' and app_state='APG' and agree='" + id + "') or (agree_method='SERIAL' and app_state='APG2' and agree2='";
		ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG3' and agree3='" + id + "') or (agree_method='SERIAL' and app_state='APG4' and agree4='" + id + "') or (agree_method='SERIAL' and app_state='APG5' and agree5='" + id + "') or (agree_method='SERIAL' and app_state='APG6' and agree6='";
		ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG7' and agree7='" + id + "') or (agree_method='SERIAL' and app_state='APG8' and agree8='" + id + "') or (agree_method='SERIAL' and app_state='APG9' and agree9='" + id + "') or (agree_method='SERIAL' and app_state='APG10' and agree10='";
		ING_data +=  id + "')";

		//�ϰ��� �����϶�(������ ����)
		//String PAL_data = " or (agree_method='PARALLEL' and app_state='APG' and agree_comment is NULL and agree='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree2_comment is NULL and agree2='" + id + "')";
		//PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree3_comment is NULL and agree3='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree4_comment is NULL and agree4='" + id + "')";
		//PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree5_comment is NULL and agree5='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree6_comment is NULL and agree6='" + id + "')";
		//PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree7_comment is NULL and agree7='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree8_comment is NULL and agree8='" + id + "')";
		//PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree9_comment is NULL and agree9='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree10_comment is NULL and agree10='" + id + "')";	
		String PAL_data = " or (agree_method='PARALLEL' and app_state='APG' and agree='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree2='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree3='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree4='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree5='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree6='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree7='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree8='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree9='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree10='" + id + "')";	
		
		query += ING_data + PAL_data;
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;	
	}	
	
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ������ APP_MASTER
	//--------------------------------------------------------------------
	public int getTotalAsk(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_MASTER ";
		String ask_ing_data = "where (writer='"+id+"' or (reviewer='"+id+"' and review_date != 'NULL') or (agree='"+id+"' and agree_date != 'NULL') or (agree2='"+id+"' and agree2_date != 'NULL') or (agree3='"+id+"' and agree3_date != 'NULL') or (agree4='"+id+"' and agree4_date != 'NULL') or (agree5='"+id+"' and agree5_date != 'NULL') or (agree6='"+id+"' and agree6_date != 'NULL') or (agree7='"+id+"' and agree7_date != 'NULL') or (agree8='"+id+"' and agree8_date != 'NULL') or (agree9='"+id+"' and agree9_date != 'NULL') or (agree10='"+id + "' and agree10_date != 'NULL') )";
		ask_ing_data += " and (app_state='APV' or app_state='APG' or app_state='APG2' or app_state='APG3'";
		ask_ing_data += " or app_state='APG4' or app_state='APG5' or app_state='APG6' or app_state='APG7' or app_state='APG8' or app_state='APG9'";
		ask_ing_data += " or app_state='APG10' or app_state='APL')";					
		query += ask_ing_data;
		////System.out.println("������ ���� : "  + query);
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;
	}	

	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : �ݷ��� APP_MASTER
	//--------------------------------------------------------------------
	public int getTotalRej(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_MASTER ";
		String rej_box_data = "where writer='"+id+"' and app_state='APR'";
		query += rej_box_data;
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}			
	
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : �ӽú����� APP_MASTER
	//--------------------------------------------------------------------
	public int getTotalTmp(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_MASTER ";
		String tmp_box_data = "where writer='"+id+"' and app_state='APT'";
		query += tmp_box_data;
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}	
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ������ storehouse.dbo.APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalDel(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM storehouse.dbo.APP_SAVE ";
		String del_box_data = "where app_state='AMV'";
		query += del_box_data;
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}

	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� ��ü APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalSav(String id,String sItem,String sWord) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_box_data = "";
		if(sWord.length() == 0)
			app_box_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS'";
		else 
			app_box_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and "+sItem+" like '%"+sWord+"%'";
		
		//�����ϱ��� �־������ڸ�ŭ�� �����ϱ�
		String cut_date = anbdt.getID(getAppAps());		//�����ϱ��� ID���ϱ�
		String app_cut = " and pid >= '"+cut_date+"'";

		String abd_query = query + app_box_data+app_cut;

		rs = stmt.executeQuery(abd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;			
	}

	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� �Ϲݹ��� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalGen(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_gen_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GEN'";
		String agd_query = query + app_gen_data;
		rs = stmt.executeQuery(agd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}

	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� ������ APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalSer(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_ser_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='SERVICE'";
		String asd_query = query + app_ser_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;			
	}

	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� ����� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalOut(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_out_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='OE_CHUL'";
		String asd_query = query + app_out_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;			
	}

	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� �����û�� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalBtr(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_btr_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='CHULJANG_SINCHEONG'";
		String asd_query = query + app_btr_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}

	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� �ް��� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalHdy(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='HYU_GA'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;			
	}

	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� ������û APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalCar(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='BAE_CHA'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;			
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� ���� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalRep(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='BOGO'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� ���庸�� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalBrp(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='CHULJANG_BOGO'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;			
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� ��ȼ� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalDrf(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GIAN'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� ���Խ�û�� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalCrd(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='MYEONGHAM'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� ������ APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalRsn(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='SAYU'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� ������ APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalHlp(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='HYEOPJO'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� ����ٹ���û�� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalOtw(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='YEONJANG'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� �����Ƿڼ� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalOff(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GUIN'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� �������� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalEdu(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GYOYUK_ILJI'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� ���ο� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalAkg(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='AKG'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� ������� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalTd(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='TD'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� �������� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalOdt(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ODT'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� �系���� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalIds(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='IDS'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� ��ܰ��� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalOds(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ODS'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� �ڻ���� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalAst(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ASSET'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� �������� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalEst(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='EST'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� Ư�ٰ��� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalEwk(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='EWK'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� BOM���� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalBom(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='BOM'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� ���躯����� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalDcm(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='DCM'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� ���ſ�û�������� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalPcr(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='PCR'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� ���ֿ�û�������� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalOdr(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ODR'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� �����԰���� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalPwh(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='PWH'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : ����� ��ǰ������ APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalTgw(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_SAVE ";
		String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='TGW'";
		String asd_query = query + app_hdy_data;
		rs = stmt.executeQuery(asd_query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}
	//--------------------------------------------------------------------
	//	���� �ľ��ϱ� : �뺸�� APP_SAVE
	//--------------------------------------------------------------------
	public int getTotalSee(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT COUNT(*) FROM APP_RECEIVE ";
		String see_box_totD = "where receiver='"+id+"'";
		query += see_box_totD;
		rs = stmt.executeQuery(query);
		rs.next();
		int cnt = rs.getInt(1);
		stmt.close();
		rs.close();
		return cnt;		
	}

	//*******************************************************************
	//	�� ������ �� ���ϱ�
	//*******************************************************************/
	public int getTotalPage() 
	{
		return this.total_page;
	}

	//*******************************************************************
	//	�� ������ �� ���ϱ�
	//*******************************************************************/
	public int getCurrentPage() 
	{
		return this.current_page;
	}
	//*******************************************************************
	//	Query ���� �������� : ��ü LIST
	// id:���, appKind:����������, page:����page, out:���������� ����� ����
	// sItem : �˻��÷���, sWord : �˻��� ����, app_path:�������� rootPath
	//*******************************************************************/	
	public ArrayList getTable_list (String id,String appKind,String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";			//���
		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������
		
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		String SER_data = "";			//�˻�����
		String SORT_data = "";			//��������
		
		stmt = con.createStatement();
		TableAppMaster table = null;	//������ ���������� "��"�� ������� �����Ͽ� ��´�.
		ArrayList table_list = new ArrayList();
			
		//query���� �����
		if(appKind.equals("APP_ING")) {				//�̰��� APP_MASTER
				total_cnt = getTotalApp(id);	
				
				String ING_data = "where ((app_state='APV' and reviewer='" + id + "') or (app_state='APL' and decision='" + id + "') or (agree_method='SERIAL' and app_state='APG' and agree='" + id + "') or (agree_method='SERIAL' and app_state='APG2' and agree2='";
				ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG3' and agree3='" + id + "') or (agree_method='SERIAL' and app_state='APG4' and agree4='" + id + "') or (agree_method='SERIAL' and app_state='APG5' and agree5='" + id + "') or (agree_method='SERIAL' and app_state='APG6' and agree6='";
				ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG7' and agree7='" + id + "') or (agree_method='SERIAL' and app_state='APG8' and agree8='" + id + "') or (agree_method='SERIAL' and app_state='APG9' and agree9='" + id + "') or (agree_method='SERIAL' and app_state='APG10' and agree10='";
				ING_data +=  id + "')";

				//�ϰ��� �����϶�(������ ����)
				//String PAL_data = " or (agree_method='PARALLEL' and app_state='APG' and agree_comment is NULL and agree='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree2_comment is NULL and agree2='" + id + "')";
				//PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree3_comment is NULL and agree3='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree4_comment is NULL and agree4='" + id + "')";
				//PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree5_comment is NULL and agree5='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree6_comment is NULL and agree6='" + id + "')";
				//PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree7_comment is NULL and agree7='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree8_comment is NULL and agree8='" + id + "')";
				//PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree9_comment is NULL and agree9='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree10_comment is NULL and agree10='" + id + "'))";
				String PAL_data = " or (agree_method='PARALLEL' and app_state='APG' and agree='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree2='" + id + "')";
				PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree3='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree4='" + id + "')";
				PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree5='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree6='" + id + "')";
				PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree7='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree8='" + id + "')";
				PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree9='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree10='" + id + "'))";	
		

				//�˻�
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				//����
				SORT_data = " ORDER BY write_date DESC";

				query = "SELECT * FROM APP_MASTER ";
				query += ING_data + PAL_data + SER_data + SORT_data;
				//System.out.println("�̰��� : " + query);
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("ASK_ING")) {		//������ APP_MASTER
				total_cnt = getTotalAsk(id);
				
				String ask_ing_data = "where ((writer='"+id+"' or (reviewer='"+id+"' and review_date != 'NULL') or (agree='"+id+"' and agree_date != 'NULL') or (agree2='"+id+"' and agree2_date != 'NULL') or (agree3='"+id+"' and agree3_date != 'NULL') or (agree4='"+id+"' and agree4_date != 'NULL') or (agree5='"+id+"' and agree5_date != 'NULL') or (agree6='"+id+"' and agree6_date != 'NULL') or (agree7='"+id+"' and agree7_date != 'NULL') or (agree8='"+id+"' and agree8_date != 'NULL') or (agree9='"+id+"' and agree9_date != 'NULL') or (agree10='"+id + "' and agree10_date != 'NULL') )";
				ask_ing_data += " and (app_state='APV' or app_state='APG' or app_state='APG2' or app_state='APG3'";
				ask_ing_data += " or app_state='APG4' or app_state='APG5' or app_state='APG6' or app_state='APG7' or app_state='APG8' or app_state='APG9'";
				ask_ing_data += " or app_state='APG10' or app_state='APL'))";						
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_MASTER ";
				query += ask_ing_data+SER_data+SORT_data;
				////System.out.println("������ : " + query);
				rs = stmt.executeQuery(query);
		} 
		else if(appKind.equals("REJ_BOX")) {		//�ݷ��� APP_MASTER
				total_cnt = getTotalRej(id);
				
				String rej_box_data = "where writer='"+id+"' and app_state='APR'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_MASTER ";
				query += rej_box_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("TMP_BOX")) {		//�ӽú����� APP_MASTER
				total_cnt = getTotalTmp(id);
				
				String tmp_box_data = "where writer='"+id+"' and app_state='APT'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_MASTER ";
				query += tmp_box_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("DEL_BOX")) {		//������ APP_MASTER
				total_cnt = getTotalDel(id);
				
				String del_box_data = "where app_state='AMV'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM storehouse.dbo.APP_SAVE ";
				query += del_box_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_BOX")) {		//����� ��ü APP_SAVE
				total_cnt = getTotalSav(id,sItem,sWord);
				
				String app_box_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 

				//�����ϱ��� �־������ڸ�ŭ�� �����ϱ�
				String cut_date = anbdt.getID(getAppAps());		//�����ϱ��� ID���ϱ�
				String app_cut = " and pid >= '"+cut_date+"'";

				query = "SELECT * FROM APP_SAVE ";
				query += app_box_data+SER_data+app_cut+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_GEN")) {		//����� �Ϲݹ��� APP_SAVE
				total_cnt = getTotalGen(id);
				
				String app_gen_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GEN'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_gen_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_SER")) {		//����� ������ APP_SAVE
				total_cnt = getTotalSer(id);
				
				String app_ser_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='SERVICE'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_ser_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_OUT")) {		//����� ����� APP_SAVE
				total_cnt = getTotalOut(id);
				
				String app_out_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='OE_CHUL'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_out_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_BTR")) {		//����� �����û�� APP_SAVE
				total_cnt = getTotalBtr(id);
				
				String app_btr_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='CHULJANG_SINCHEONG'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_btr_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_HDY")) {		//����� �ް��� APP_SAVE
				total_cnt = getTotalHdy(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='HYU_GA'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_CAR")) {		//����� ������û�� APP_SAVE
				total_cnt = getTotalCar(id);
			
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='BAE_CHA'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_REP")) {		//����� ���� APP_SAVE
				total_cnt = getTotalRep(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='BOGO'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_BRP")) {		//����� ���庸�� APP_SAVE
				total_cnt = getTotalBrp(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='CHULJANG_BOGO'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_DRF")) {		//����� ��ȼ� APP_SAVE
				total_cnt = getTotalDrf(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GIAN'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_CRD")) {		//����� ���Խ�û�� APP_SAVE
				total_cnt = getTotalCrd(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='MYEONGHAM'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_RSN")) {		//����� ������ APP_SAVE
				total_cnt = getTotalRsn(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='SAYU'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_HLP")) {		//����� ������ APP_SAVE
				total_cnt = getTotalHlp(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='HYEOPJO'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_OTW")) {		//����� ����ٹ���û�� APP_SAVE
				total_cnt = getTotalOtw(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='YEONJANG'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_OFF")) {		//����� �����Ƿڼ� APP_SAVE
				total_cnt = getTotalOff(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GUIN'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_EDU")) {		//����� �������� APP_SAVE
				total_cnt = getTotalEdu(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GYOYUK_ILJI'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_AKG")) {		//����� ���ο� APP_SAVE
				total_cnt = getTotalAkg(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='AKG'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_TD")) {			//����� ������� APP_SAVE
				total_cnt = getTotalTd(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='TD'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_ODT")) {			//����� �������� APP_SAVE
				total_cnt = getTotalOdt(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ODT'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_IDS")) {			//����� �系���� APP_SAVE
				total_cnt = getTotalIds(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='IDS'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_ODS")) {			//����� ��ܰ��� APP_SAVE
				total_cnt = getTotalOds(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ODS'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_AST")) {			//����� �ڻ���� APP_SAVE
				total_cnt = getTotalAst(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ASSET'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_EST")) {			//����� �������� APP_SAVE
				total_cnt = getTotalEst(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='EST'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_EWK")) {			//����� Ư�ٰ��� APP_SAVE
				total_cnt = getTotalEwk(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='EWK'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_BOM")) {			//����� BOM���� APP_SAVE
				total_cnt = getTotalBom(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='BOM'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_DCM")) {			//����� ���躯����� APP_SAVE
				total_cnt = getTotalDcm(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='DCM'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_PCR")) {			//����� ���ſ�û���� APP_SAVE
				total_cnt = getTotalPcr(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='PCR'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_ODR")) {			//����� ���ֿ�û���� APP_SAVE
				total_cnt = getTotalOdr(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ODR'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_PWH")) {			//����� �����԰���� APP_SAVE
				total_cnt = getTotalPwh(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='PWH'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("APP_TGW")) {			//����� ��ǰ������ APP_SAVE
				total_cnt = getTotalTgw(id);
				
				String app_hdy_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='TGW'";
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_SAVE ";
				query += app_hdy_data+SER_data+SORT_data;
				rs = stmt.executeQuery(query);
		}
		else if(appKind.equals("SEE_BOX")) {		//�뺸�� ������ APP_RECEIVE
				total_cnt = getTotalSee(id);
				//System.out.println("total_cnt : " + total_cnt);
				SER_data = " and ("+sItem+" like '%"+sWord+"%')"; 
				SORT_data = "order by write_date desc"; 
				query = "SELECT * FROM APP_RECEIVE where receiver='"+id+"'";
				query += SER_data+SORT_data;
				//System.out.println("query : " + query);
				rs = stmt.executeQuery(query);
		}

		//������ ������ �ٲ��ֱ�
		if(page == null) page = "1";
		if(page.length() == 0) page = "1";
		current_page = Integer.parseInt(page);	//����� ������

		//��ü page ���ϱ�
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(this.total_page*max_display_cnt != total_cnt) this.total_page += 1;

		//�������� ���� query ����ϱ�
		if(current_page == 1) { startRow = 1; endRow = max_display_cnt; }
		else { startRow = (current_page - 1) * max_display_cnt + 1; endRow = startRow + max_display_cnt - 1; }
		if(total_cnt == 0) endRow = -1;

		//������ skip �ϱ� (�ش���� �ʴ� �������� ����)
		for(int i=1; i<current_page; i++) for(int j=0; j<max_display_cnt; j++) rs.next();
		
		//������ ���
		int show_cnt = 0;
		while(rs.next() && (show_cnt < max_display_cnt)) { 
				//����Ʈ ���
				table = new TableAppMaster();
				String pid = rs.getString("pid");			if(pid == null) pid = "";
				table.setAmPid(pid);									//������ȣ

				String content = rs.getString("app_subj");  if(content == null) content = "";
				String subLink = "<a href=\"javascript:eleApprovalView('"+pid+"','"+appKind+"');\">"+content+"</a>";

				table.setAmAppSubj(subLink);							//����

				table.setAmWriterName(rs.getString("writer_name"));		//�����
				table.setAmWriteDate(rs.getString("write_date"));		//�������

				String STATE = "";										//��������
				String  status = "";
				if(appKind.equals("SEE_BOX")) {
					status = rs.getString("isopen");
					if(status.equals("0")) STATE = "��Ȯ��";
					else STATE = "Ȯ����";
				} else {
					status = rs.getString("app_state");
					if(status.equals("APV")) STATE = "������";
					else if(status.equals("APG")) STATE = "�������";
					else if(status.equals("APL")) STATE="���δ��";
					else if(status.equals("APR")) STATE="�ݷ�";
					else if(status.equals("APT")) STATE="�ӽú���";
					else if(status.equals("APS")) STATE="����Ϸ�";
					else if(status.equals("API")) STATE="�뺸";
					else if(status.equals("AMV")) STATE="��������";
					else STATE="�������";	//APG2 ~ APG10
				} //if
				table.setAmAppStatus(STATE);							//����
				
				int add_cnt = 0;										//÷������ ����
				if(appKind.equals("SEE_BOX")) {
					add_cnt = Integer.parseInt(rs.getString("add_counter"));
				} else {
					String adn = rs.getString("add_counter"); if(adn == null) adn = "0"; if(adn.length() == 0) adn = "0";
					////System.out.println("adn : " + adn);
					add_cnt = Integer.parseInt(adn);
					String add_f1 = rs.getString("add_1_file"); if(add_f1 == null) add_f1 = ""; //else { if(add_f1.length() > 1) add_cnt++; }
					String add_f2 = rs.getString("add_2_file"); if(add_f2 == null) add_f2 = ""; //else { if(add_f2.length() > 1) add_cnt++; }
					String add_f3 = rs.getString("add_3_file"); if(add_f3 == null)	add_f3 = ""; //else { if(add_f3.length() > 1) add_cnt++; }
					table.setAmAdd1File(add_f1);							//÷������ 1
					table.setAmAdd2File(add_f2);							//÷������ 2
					table.setAmAdd3File(add_f3);							//÷������ 3
				}
				table.setAmAddCounter(Integer.toString(add_cnt));		

				table.setAmDeleteDate(rs.getString("delete_date"));			//������
				table.setAmPlid(rs.getString("Plid"));						//���ù��� ������ȣ
				table_list.add(table);
				show_cnt++;
		} //while

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	//	���� �����Կ� �ִ� ���� �����Ͽ� ��������
	//*******************************************************************/	
	public ArrayList getTableDelBoxlist (String id,String flag,String syear,String sItem,String sWord,String page,int max_display_cnt) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";			//���
		int total_cnt = 0;				//�� ����
		int startRow = 0;				//������
		int endRow = 0;					//��������
		
		Statement stmt = null;
		ResultSet rs = null;
		String query = "";	
		
		stmt = con.createStatement();
		TableAppMaster table = null;	//������ ���������� "��"�� ������� �����Ͽ� ��´�.
		ArrayList table_list = new ArrayList();
			
		//query���� �����
		total_cnt = getTotalDel(id);
		query = "SELECT * FROM storehouse.dbo.APP_SAVE ";
		query += "where flag like '%"+flag+"%' and write_date like '%"+syear+"%' ";
		query += " and "+sItem+" like '%"+sWord+"%' order by write_date desc"; 
		rs = stmt.executeQuery(query);
		
		//������ ������ �ٲ��ֱ�
		if(page == null) page = "1";
		if(page.length() == 0) page = "1";
		current_page = Integer.parseInt(page);	//����� ������

		//��ü page ���ϱ�
		this.total_page = (int)(total_cnt / max_display_cnt);
		if(this.total_page*max_display_cnt != total_cnt) this.total_page += 1;

		//�������� ���� query ����ϱ�
		if(current_page == 1) { startRow = 1; endRow = max_display_cnt; }
		else { startRow = (current_page - 1) * max_display_cnt + 1; endRow = startRow + max_display_cnt - 1; }
		if(total_cnt == 0) endRow = -1;

		//������ skip �ϱ� (�ش���� �ʴ� �������� ����)
		for(int i=1; i<current_page; i++) for(int j=0; j<max_display_cnt; j++) rs.next();
		
		//������ ���
		int show_cnt = 0;
		while(rs.next() && (show_cnt < max_display_cnt)) { 
				//����Ʈ ���
				table = new TableAppMaster();
				String pid = rs.getString("pid");			if(pid == null) pid = "";
				table.setAmPid(pid);									//������ȣ

				String content = rs.getString("app_subj");  if(content == null) content = "";
				String subLink = "<a href=\"javascript:eleApprovalView('"+pid+"','DEL_BOX');\">"+content+"</a>";

				table.setAmAppSubj(subLink);							//����

				table.setAmWriterName(rs.getString("writer_name"));		//�����
				table.setAmWriteDate(rs.getString("write_date"));		//�������

				String STATE = "";										//��������
				String  status = "��������";
				table.setAmAppStatus(status);							//����
				
				int add_cnt = 0;										//÷������ ����
				String adn = rs.getString("add_counter"); if(adn == null) adn = "0"; if(adn.length() == 0) adn = "0";
				////System.out.println("adn : " + adn);
				add_cnt = Integer.parseInt(adn);
				String add_f1 = rs.getString("add_1_file"); if(add_f1 == null) add_f1 = ""; //else { if(add_f1.length() > 1) add_cnt++; }
				String add_f2 = rs.getString("add_2_file"); if(add_f2 == null) add_f2 = ""; //else { if(add_f2.length() > 1) add_cnt++; }
				String add_f3 = rs.getString("add_3_file"); if(add_f3 == null)	add_f3 = ""; //else { if(add_f3.length() > 1) add_cnt++; }
				table.setAmAdd1File(add_f1);							//÷������ 1
				table.setAmAdd2File(add_f2);							//÷������ 2
				table.setAmAdd3File(add_f3);							//÷������ 3
				
				table.setAmAddCounter(Integer.toString(add_cnt));		

				table.setAmDeleteDate(rs.getString("delete_date"));			//������
				table.setAmPlid(rs.getString("Plid"));						//���ù��� ������ȣ
				table_list.add(table);
				show_cnt++;
		} //while

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}
	//*******************************************************************
	//	��Ṯ�� ������ �������� ȭ�鿡 ����� ������ ���� ���ϱ�
	//*******************************************************************/
	public int getAppAps() throws Exception
	{
		//���� �ʱ�ȭ
		int data = 0;
		Statement stmt = null;
		ResultSet rs = null;
		
		//���� �׸�
		stmt = con.createStatement();

		String query = "SELECT env_value FROM app_env where env_name='APPAPS'";
		rs = stmt.executeQuery(query);
		if(rs.next()) data = Integer.parseInt(rs.getString("env_value"));

		stmt.close();
		rs.close();

		return data;
	}
}