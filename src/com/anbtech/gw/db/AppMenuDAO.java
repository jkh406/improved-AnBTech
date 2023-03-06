package com.anbtech.gw.db;
import com.anbtech.gw.entity.*;
import java.sql.*;
import java.util.*;
import com.anbtech.dbconn.DBConnectionManager;
import com.anbtech.date.anbDate;

public class AppMenuDAO
{
	private com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
	private DBConnectionManager connMgr;
	private Connection con;
	
	//������
	public AppMenuDAO(Connection con) 
	{
		this.con = con;
	}

	public AppMenuDAO() 
	{
		connMgr = com.anbtech.dbconn.DBConnectionManager.getInstance();
		con = connMgr.getConnection("mssql");
	}
	
	//�Ҹ���
	public void close(Connection con) throws Exception {
		connMgr.freeConnection("mssql",con);
	}
	//*******************************************************************
	//	���� �ľ��ϱ�
	//*******************************************************************/
	public ArrayList getTotal(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���

		//���������� �����ϱ�
		deleteAppMasterAMV(id);		//������ ���̺�
		deleteAppSaveAMV(id);		//��Ṯ�� ���� ���̺�
		deleteAppReceive(id);		//�뺸���� ���̺� ����

		//������(AMV)������ storehouse ���̺����̽��� app_save�� �ű��� �����ϱ�
		StoreHouseApp();			//������ �����ϱ� (groupware app_save --> storehouse app_save)
		deleteAMV();				//����� ������ �����ϱ�(groupware app_master,app_save)
		
		Statement stmt = null;
		ResultSet rs = null;
		TableItemCount table = null;
		String tablename = "";
		String query = "";	
		
		//return �� ArrayList �����
		ArrayList table_list = new ArrayList();

		//���� �׸�
		stmt = con.createStatement();
		

		//----------------------------------------------
		// �̰���
		//----------------------------------------------
		tablename = "APP_MASTER";
		query = "SELECT COUNT(*) FROM "+tablename + " ";

		String ING_data = "where (app_state='APV' and reviewer='" + id + "') or (app_state='APL' and decision='" + id + "') or (agree_method='SERIAL' and app_state='APG' and agree='" + id + "') or (agree_method='SERIAL' and app_state='APG2' and agree2='";
		ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG3' and agree3='" + id + "') or (agree_method='SERIAL' and app_state='APG4' and agree4='" + id + "') or (agree_method='SERIAL' and app_state='APG5' and agree5='" + id + "') or (agree_method='SERIAL' and app_state='APG6' and agree6='";
		ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG7' and agree7='" + id + "') or (agree_method='SERIAL' and app_state='APG8' and agree8='" + id + "') or (agree_method='SERIAL' and app_state='APG9' and agree9='" + id + "') or (agree_method='SERIAL' and app_state='APG10' and agree10='";
		ING_data +=  id + "')";

		//�ϰ��� �����϶�(������ ����)
		/*String PAL_data = " or (agree_method='PARALLEL' and app_state='APG' and agree_comment is NULL and agree='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree2_comment is NULL and agree2='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree3_comment is NULL and agree3='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree4_comment is NULL and agree4='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree5_comment is NULL and agree5='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree6_comment is NULL and agree6='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree7_comment is NULL and agree7='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree8_comment is NULL and agree8='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree9_comment is NULL and agree9='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree10_comment is NULL and agree10='" + id + "')";
		*/
		String PAL_data = " or (agree_method='PARALLEL' and app_state='APG' and agree='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree2='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree3='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree4='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree5='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree6='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree7='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree8='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree9='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree10='" + id + "')";	
		
		query += ING_data + PAL_data;
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppIngCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
			//System.out.println("�̰��� : " + table.getAppIngCnt());
		}

		//----------------------------------------------
		// ������
		//----------------------------------------------
		tablename = "APP_MASTER";
		query = "SELECT COUNT(*) FROM "+tablename + " ";

		String ask_ing_data = "where (writer='"+id+"' or (reviewer='"+id+"' and review_date != 'NULL') or (agree='"+id+"' and agree_date != 'NULL') or (agree2='"+id+"' and agree2_date != 'NULL') or (agree3='"+id+"' and agree3_date != 'NULL') or (agree4='"+id+"' and agree4_date != 'NULL') or (agree5='"+id+"' and agree5_date != 'NULL') or (agree6='"+id+"' and agree6_date != 'NULL') or (agree7='"+id+"' and agree7_date != 'NULL') or (agree8='"+id+"' and agree8_date != 'NULL') or (agree9='"+id+"' and agree9_date != 'NULL') or (agree10='"+id + "' and agree10_date != 'NULL') )";
		ask_ing_data += " and (app_state='APV' or app_state='APG' or app_state='APG2' or app_state='APG3'";
		ask_ing_data += " or app_state='APG4' or app_state='APG5' or app_state='APG6' or app_state='APG7' or app_state='APG8' or app_state='APG9'";
		ask_ing_data += " or app_state='APG10' or app_state='APL')";					
		query += ask_ing_data;
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAskIngCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
			////System.out.println("������ : " + table.getAskIngCnt());
		}

		//----------------------------------------------
		// �����
		//----------------------------------------------
		tablename = "APP_SAVE";
		query = "SELECT COUNT(*) FROM "+tablename + " ";

		//���� ���� (��ü����)
		String app_box_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS'";
		
		//�����ϱ��� �־������ڸ�ŭ�� �����ϱ�
		String cut_date = anbdt.getID(getAppAps());		//�����ϱ��� ID���ϱ�
		String app_cut = " and pid >= '"+cut_date+"'";
		
		String abd_query = query + app_box_data+app_cut;
		rs = stmt.executeQuery(abd_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppBoxCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
			////System.out.println("�������ü : " + table.getAppBoxCnt());
		}

		//�Ϲݹ���
		String app_gen_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GEN'";
		String agd_query = query + app_gen_data;
		rs = stmt.executeQuery(agd_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppGenCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
			////System.out.println("������Ϲ�: " + table.getAppGenCnt());
		}

		//����������
		String app_ser_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='SERVICE'";
		String asd_query = query + app_ser_data;
		rs = stmt.executeQuery(asd_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppSerCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
			////System.out.println("����԰����� : " + table.getAppSerCnt());
		}

		String app_yan_data = "";
		String yan_query = "";
		//(����� : an outing)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='OE_CHUL'";
		yan_query = query + app_yan_data;
		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppOutCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
			////System.out.println("�����(�����)���� : " + table.getAppOutCnt());
		}

		//(�����û : a business trip)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='CHULJANG_SINCHEONG'";
		yan_query = query + app_yan_data;
		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppBtrCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
			////System.out.println("�����(�����û)���� : " + table.getAppBtrCnt());
		}

		//(�ް��� : holidays)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='HYU_GA'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppHdyCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
			////System.out.println("�����(�ް���)���� : " + table.getAppHdyCnt());
		}

		//(������û�� : Allocation of cars)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='BAE_CHA'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppCarCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(���� : report)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='BOGO'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppRepCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(���庸�� : a business trip report)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='CHULJANG_BOGO'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppBrpCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(��ȼ� : drafting)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GIAN'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppDrfCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(���Խ�û�� : a business card)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='MYEONGHAM'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppCrdCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(������ : a reason)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='SAYU'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppRsnCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(������ : help)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='HYEOPJO'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppHlpCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(����ٹ���û�� : overtime work)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='YEONJANG'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppOtwCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(�����Ƿڼ� : job offering)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GUIN'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppOffCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(�������� : education)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='GYOYUK_ILJI'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppEduCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(���ο� : acknowledgment)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='AKG'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppAkgCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(������� : technical document)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='TD'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppTdCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(�������� : an official document)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ODT'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppOdtCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(�系���� : an internal official document)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='IDS'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppIdsCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(��ܰ��� : an outside official document)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ODS'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppOdsCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(�ڻ���� : an asset)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ASSET'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppAstCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(�������� : an estimate)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='EST'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppEstCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(Ư�ٰ��� : an extra work)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='EWK'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppEwkCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(BOM���� : bill of material)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='BOM'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppBomCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(���躯����� : design change management)����
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='DCM'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppDcmCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(���ſ�û : purchase request)
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='PCR'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppPcrCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(���ֿ�û : order request)
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='ODR'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppOdrCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(�����԰� : purchase warehousing)
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='PHW'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppPwhCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}

		//(��ǰ��� : take goods out of a warehouse)
		app_yan_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and flag='TGW'";
		yan_query = query + app_yan_data;

		rs = stmt.executeQuery(yan_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppTgwCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
		}


		//----------------------------------------------
		// �ݷ���
		//----------------------------------------------
		tablename = "APP_MASTER";
		query = "SELECT COUNT(*) FROM "+tablename + " ";

		String rej_box_data = "where writer='"+id+"' and app_state='APR'";
		query += rej_box_data;
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setRejBoxCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
			////System.out.println("�ݷ��� : " + table.getRejBoxCnt());
		}

		//----------------------------------------------
		// �ӽ� ������
		//----------------------------------------------
		tablename = "APP_MASTER";
		query = "SELECT COUNT(*) FROM "+tablename + " ";

		String tmp_box_data = "where writer='"+id+"' and app_state='APT'";
		query += tmp_box_data;
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setTmpBoxCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
			////System.out.println("�ӽú����� : " + table.getTmpBoxCnt());
		}

		//----------------------------------------------
		// �뺸��
		//----------------------------------------------
		query = "SELECT COUNT(*) FROM APP_RECEIVE where receiver='"+id+"' and isopen='0'";
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setSeeBoxCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
			//System.out.println("������������ : " + table.getSeeBoxCnt());
		}

		//��ü ����
		query = "SELECT COUNT(*) FROM APP_RECEIVE where receiver='"+id+"'";
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setSeeBoxTot(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
			//System.out.println("�뺸��ü���� : " + table.getSeeBoxTot());
		}

		//----------------------------------------------
		// ������
		//----------------------------------------------
		query = "SELECT COUNT(*) FROM storehouse.dbo.APP_SAVE where app_state='AMV'";
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setDelBoxCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
			////System.out.println("������ : " + table.getDelBoxCnt());
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

	//*******************************************************************
	//	�Ⱓ���� �ӽú����� ���� ã�� (APP_MASTER)
	//*******************************************************************/
	public void deleteAppMasterAMV(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
		String cur_date = anbdt.getDateNoformat();
		int cudate = Integer.parseInt(cur_date);

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "";
		//APP_MASTER���̺��� AMV�� �ٲ�
		query = "SELECT pid,delete_date from APP_MASTER where (writer='"+id+"') and (app_state != 'AMV')";
		query += " and (delete_date < '"+cur_date+"')";
		rs = stmt.executeQuery(query);

		while(rs.next()) {
			String PID = rs.getString("pid"); if(PID == null) PID = "";
			String del = rs.getString("delete_date"); 
			if(del != null) {
				int dedate = Integer.parseInt(del);
				setAmv("APP_MASTER",PID,cudate,dedate);
			}
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
	}
	
	//*******************************************************************
	//	�Ⱓ���� ���幮�� ã�� (APP_SAVE)
	//*******************************************************************/
	public void deleteAppSaveAMV(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
		String cur_date = anbdt.getDateNoformat();
		int cudate = Integer.parseInt(cur_date);

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "";
		
		//APP_SAVE���̺���  AMV�� �ٲ�
		query = "SELECT pid,delete_date from APP_SAVE where (writer='"+id+"') and (app_state != 'AMV')";
		query += " and (delete_date < '"+cur_date+"')";
		rs = stmt.executeQuery(query);

		while(rs.next()) {
			String PID = rs.getString("pid");
			String del = rs.getString("delete_date"); 
			if(del != null) {
				int dedate = Integer.parseInt(del);
				setAmv("APP_SAVE",PID,cudate,dedate);
			}
		}

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
	}

	//*******************************************************************
	//	������ �Ϸ�� ���� ���¸� �ٲ��ֱ� ( --> AMV) update�ϱ�
	//*******************************************************************/
	private void setAmv(String tablename,String PID,int cur, int del) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		if(cur > del) {
				String update = "update "+tablename+" set app_state='AMV' where pid='"+PID+"'";
				stmt.executeUpdate(update);
		}
		stmt.close();
	}

	//*******************************************************************
	//	�Ⱓ���� �뺸���� ã�� (APP_RECEIVE)
	//*******************************************************************/
	public void deleteAppReceive(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		com.anbtech.date.anbDate anbdt = new com.anbtech.date.anbDate();
		String cur_date = anbdt.getDateNoformat();
		int cudate = Integer.parseInt(cur_date);

		Statement stmt = null;
		ResultSet rs = null;
		stmt = con.createStatement();

		String query = "";
		// APP_RECEIVE���̺��� ��������
		query = "SELECT pid,delete_date from APP_RECEIVE where writer='"+id+"'";
		rs = stmt.executeQuery(query);

		while(rs.next()) {
			String PID = rs.getString("pid");
			String del = rs.getString("delete_date");
			if(del != null) {
				int dedate = Integer.parseInt(del);
				StoreHouseReceive(PID);							//storehouse�� �ű��
				setDelete("APP_RECEIVE",PID,cudate,dedate);		//�����ϱ�
			}
		}
		
		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
	}

	//*******************************************************************
	//	������ �Ϸ�� ���� �����ϱ�
	//*******************************************************************/
	private void setDelete(String tablename,String PID,int cu,int de) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		if(cu > de) {
			String update = "delete from "+tablename+" where pid='"+PID+"'";
			stmt.executeUpdate(update);
		}
		
		stmt.close();
	}

	//*******************************************************************
	//	������ �Ϸ�� ���� storehouse���̺� ��Ű���� app_save�� �����ϱ�
	//*******************************************************************/
	private void StoreHouseApp() throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		
		String update = "insert into storehouse.dbo.APP_SAVE ";
			update += "select * from APP_SAVE where app_state='AMV'";
		//System.out.println(" StoreHouseApp : " + update);
		stmt.executeUpdate(update);		
		stmt.close();
	}

	//*******************************************************************
	//	������ �Ϸ�� ���� storehouse���̺� ��Ű���� app_receive�� �����ϱ�
	//*******************************************************************/
	private void StoreHouseReceive(String PID) throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		
		String update = "insert into storehouse.dbo.APP_RECEIVE ";
			update += "select * from APP_RECEIVE where pid='"+PID+"'";
		//System.out.println("recevie �ű�� : " + update);
		stmt.executeUpdate(update);		
		stmt.close();
	}

	//*******************************************************************
	//	������ �Ϸ�� ����(AMV) groupware���̺� ��Ű������ ���������ϱ�
	//*******************************************************************/
	private void deleteAMV() throws Exception
	{
		Statement stmt = null;
		stmt = con.createStatement();
		
		String[] tb = {"APP_MASTER","APP_SAVE"};
		for(int i=0; i<2; i++) {
			String update = "delete from "+tb[i]+" where app_state='AMV'";
			//System.out.println(" deleteAMV : " + update);
			stmt.executeUpdate(update);
		}
		stmt.close();
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
		return data;		
	}
}