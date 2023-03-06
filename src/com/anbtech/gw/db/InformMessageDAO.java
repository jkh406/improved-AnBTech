package com.anbtech.gw.db;
import com.anbtech.gw.entity.*;
import java.sql.*;
import java.util.*;
import com.anbtech.date.anbDate;

public class InformMessageDAO
{
	private anbDate anbdt = new anbDate();
	private Connection con;

	public InformMessageDAO(Connection con) 
	{
		this.con = con;
	}
	//*******************************************************************
	//	���� �ľ��ϱ�
	//*******************************************************************/
	public ArrayList getTotal(String id) throws Exception
	{
		//���� �ʱ�ȭ
		if(id == null) id = "";	//���
		Statement stmt;
		ResultSet rs;
		TableItemCount table;
		String tablename = "";
		String query = "";	
		
		//return �� ArrayList �����
		ArrayList table_list = new ArrayList();

		//���� �׸�
		stmt = con.createStatement();

		//----------------------------------------------
		// �̰���
		//----------------------------------------------
		query = "SELECT COUNT(*) FROM APP_MASTER ";

		//������ �����϶� (����,���� ����)
		String ING_data = "where (app_state='APV' and reviewer='" + id + "') or (app_state='APL' and decision='" + id + "') or (agree_method='SERIAL' and app_state='APG' and agree='" + id + "') or (agree_method='SERIAL' and app_state='APG2' and agree2='";
		ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG3' and agree3='" + id + "') or (agree_method='SERIAL' and app_state='APG4' and agree4='" + id + "') or (agree_method='SERIAL' and app_state='APG5' and agree5='" + id + "') or (agree_method='SERIAL' and app_state='APG6' and agree6='";
		ING_data +=  id + "') or (agree_method='SERIAL' and app_state='APG7' and agree7='" + id + "') or (agree_method='SERIAL' and app_state='APG8' and agree8='" + id + "') or (agree_method='SERIAL' and app_state='APG9' and agree9='" + id + "') or (agree_method='SERIAL' and app_state='APG10' and agree10='";
		ING_data +=  id + "')";

		//�ϰ��� �����϶�(������ ����)
		String PAL_data = " or (agree_method='PARALLEL' and app_state='APG' and agree_comment is NULL and agree='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree2_comment is NULL and agree2='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree3_comment is NULL and agree3='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree4_comment is NULL and agree4='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree5_comment is NULL and agree5='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree6_comment is NULL and agree6='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree7_comment is NULL and agree7='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree8_comment is NULL and agree8='" + id + "')";
		PAL_data += " or (agree_method='PARALLEL' and app_state='APG' and agree9_comment is NULL and agree9='" + id + "') or (agree_method='PARALLEL' and app_state='APG' and agree10_comment is NULL and agree10='" + id + "')";

		query += ING_data + PAL_data;

		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppIngCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
			//System.out.println("�̰��� : " + table.getAppIngCnt());
		}
		
		//----------------------------------------------
		// �����
		//----------------------------------------------
		query = "SELECT COUNT(*) FROM APP_SAVE ";

		//���� ���� (��ü����)
		String app_box_data = "where (writer='" + id + "' or decision='" + id + "' or reviewer='" + id + "') and app_state='APS' and decision like '%"+anbdt.getDate()+"%'";
		String abd_query = query + app_box_data;
		rs = stmt.executeQuery(abd_query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setAppBoxCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
			//System.out.println("�������ü : " + table.getAppBoxCnt());
		}
		
		//----------------------------------------------
		// �ݷ���
		//----------------------------------------------
		query = "SELECT COUNT(*) FROM APP_MASTER ";

		String rej_box_data = "where writer='"+id+"' and app_state='APR'";
		query += rej_box_data;
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setRejBoxCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
			//System.out.println("�ݷ��� : " + table.getRejBoxCnt());
		}

		//----------------------------------------------
		// �뺸��
		//----------------------------------------------
		query = "SELECT COUNT(*) FROM APP_RECEIVE ";

		//���� ���� ����
		String see_box_data = "where receiver='"+id+"' and isOpen='0'";
		query += see_box_data;
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setSeeBoxCnt(rs.getInt(1));		//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
			//System.out.println("������������ : " + table.getSeeBoxCnt());
		}

		//----------------------------------------------
		// ������ ����
		//----------------------------------------------
		query = "SELECT COUNT(*) FROM POST_LETTER "; 
		String post_data = "where post_receiver='"+id+"' and isopen='0'";
		query += post_data;
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();			//TableItemCount �����ϱ�
			table.setPostCnt(rs.getInt(1));			//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
			//System.out.println("���������� : " + table.getPostCnt());
		}

		//----------------------------------------------
		// ��������
		//----------------------------------------------
		//��Ͻð�
		//java.util.Date now = new java.util.Date();
		//java.text.SimpleDateFormat vans = new java.text.SimpleDateFormat("yyyy-MM-dd a hh:mm");
		//String w_time = vans.format(now);

		query = "SELECT COUNT(*) FROM notice_board "; 
		String inform_data = "where w_time like '"+anbdt.getDate()+"%'";
		query += inform_data;
		rs = stmt.executeQuery(query);
		if(rs.next()) { 
			table = new TableItemCount();				//TableItemCount �����ϱ�
			table.setInformCnt(rs.getInt(1));			//TableItemCount�� setting�ϱ�
			table_list.add(table);					//�߰��ϱ�
			//System.out.println("�������� : " + table.getInformCnt());
		}
		

		//���� �׸� ������ (�ݱ�)
		stmt.close();
		rs.close();
		return table_list;
	}

}