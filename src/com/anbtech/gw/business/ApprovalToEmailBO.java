package com.anbtech.gw.business;
import java.io.*;
import java.util.*;
import java.sql.*;
import com.anbtech.text.*;
import com.anbtech.file.FileWriteString;
import com.anbtech.file.textFileReader;
import com.anbtech.date.anbDate;

public class ApprovalToEmailBO
{
	// Database Wrapper Class ����
	private com.anbtech.date.anbDate anbdt = null;					//���� ó��
	private com.anbtech.text.StringProcess str = null;				//����,���ڿ��� ���õ� ������
	private com.anbtech.file.FileWriteString text;					//������ ���Ϸ� ���
	
	/***************************************************************************
	 * ������ �޼ҵ�
	 **************************************************************************/
	public ApprovalToEmailBO() 
	{	
		anbdt = new com.anbtech.date.anbDate();			//����ó��
		str = new com.anbtech.text.StringProcess();		//����,���ڿ��� ���õ� ������
		text = new com.anbtech.file.FileWriteString();	//������ ���Ϸ� ���
	}


}