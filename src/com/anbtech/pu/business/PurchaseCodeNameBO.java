package com.anbtech.pu.business;

import java.util.*;
import java.io.*;

public class PurchaseCodeNameBO{
	public PurchaseCodeNameBO(){

	}

	/*****************************************************************
	 * �����ڵ带 �޾Ƽ� ���� ���ڿ��� ����� �ش�.
	 *****************************************************************/
	public String getStatus(String stat) throws Exception{

		String status = "";

		if(stat.equals("S01"))		status = "���ſ�û";	//���� ����
		else if(stat.equals("S02")) status = "������";	//���ſ�û ���� ��� ����
		else if(stat.equals("S03")) status = "��������";	//���ſ�û ���簡 �Ϸ�Ǿ� ���źμ��� �Ѿ�� ����
		else if(stat.equals("S05")) status = "�����غ�";	//���źμ����� �����ϱ� ���� ���ּ��� �ۼ��� ����
		else if(stat.equals("S06")) status = "�Ϻι���";	//
		else if(stat.equals("S09")) status = "������";	//���źμ����� �ۼ��� ���ּ��� �������� ����
		else if(stat.equals("S13")) status = "���ֿϷ�";	//����Ϸ�� ����
		else if(stat.equals("S18")) status = "�԰���";	//�԰���ó������ ����
		else if(stat.equals("S19")) status = "�԰���";	//�԰���������� ����
		else if(stat.equals("S21"))	status = "�˻����԰�";	//�԰��������� �Ϸ�� ����
		else if(stat.equals("S22"))	status = "�Ϻ��԰�";	//
		else if(stat.equals("S25"))	status = "�԰�Ϸ�";	//�԰�ǰ���� ǰ���˻簡 �Ϸ�Ȼ���

		return status;
	}
}