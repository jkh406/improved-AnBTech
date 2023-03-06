package com.anbtech.file;
import java.io.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;

public class FileWriteString { 
	private String file_path="";
	private String file_name="";
	private FileOutputStream fout;
	private DataOutputStream output;
  	private File dir;
  
	/*********************************************************************
	   ������ 
	*********************************************************************/  	
	public FileWriteString()
	{
		
	}

	/*********************************************************************
	   ������ ���Ϸ� ���
	*********************************************************************/

	//--------------------------------------------------------------------
	//   ������ ���ϸ�����
	//--------------------------------------------------------------------
	public void setFilename(String file)
	{
		this.file_name = file;	
	}
	
	//--------------------------------------------------------------------
	//   ������ ����Path ���� 
	//--------------------------------------------------------------------
	public void setFilepath(String path)
	{
    	dir = new File(path);
		
    	// ���丮�� �̹� �ִ��� ??  ������ ���� ����    
    	if (!dir.exists()) dir.mkdirs();
    
		this.file_path = path;	
	}	
	
	//--------------------------------------------------------------------
	//   ������ ���Ϸ� ���� �غ� �Ѵ�.
	//--------------------------------------------------------------------
	public void setFilewrite()
	{
		String file = file_path + "/" + file_name;
		try {
			fout = new FileOutputStream(file);
			output = new DataOutputStream(fout);
		} catch (Exception e) { 
			System.out.println(e); 
			System.exit(0);
		}
	}


	//--------------------------------------------------------------------
	//   ������ ���Ϸ� ����.(Byte)
	//--------------------------------------------------------------------
	public void Writecontent(String content)
	{
		try {
			output.writeBytes(content);	
			fileClose();
		} catch (Exception e) {
			System.out.println(e); 
			System.exit(0);			
		}
	}

	//--------------------------------------------------------------------
	//   ���� �ݱ� 
	//--------------------------------------------------------------------	
	public void fileClose()
	{
		try {
			output.flush();
			output.close();	
		} catch (Exception e) {
			System.out.println(e); 
			System.exit(0);					
		}
	}

	/*********************************************************************
	   �ѱ��� ���Ϸ� ���
	*********************************************************************/	
	public void WriteHanguel(String FullPathName,String fileName,String content)
	{
		//���丮 �ִ��� �˻��ϱ�
		dir = new File(FullPathName);
    	if (!dir.exists()) dir.mkdirs();

		//���Ϸ� ���
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(FullPathName+"/"+fileName));
			bw.write(content);
			bw.close();
		} catch (Exception e) {
			System.out.println(e); 
			System.exit(0);			
		}
	}

	/*******************************************************************************************
	*   file name �ٲٱ�
	*******************************************************************************************/	
	public boolean chgFilename(String file1,String file2)
	{
		File BFN = new File(file1);		//�ٲ�File��
		File AFN = new File(file2);		//���ο� ���ϸ�
		if(BFN.exists()) return BFN.renameTo(AFN);	
		else return false;
	}

	/*********************************************************************
	 	���� �����ϱ�
	*********************************************************************/
	public void fileCopy(String source, String target) throws IOException
	{
		File f1 = new File(source);
		int i = 0;

		File s = new File(source);
		File t = new File(target);
		if(f1.exists()) {
			FileInputStream fin = new FileInputStream(s);
			FileOutputStream fout = new FileOutputStream(t);
			byte buffer[] = new byte[1024];
			int j=0;
			while((j=fin.read(buffer)) >=0) fout.write(buffer,0,j);
			fout.close();
			fin.close();
		}
	}


} // Class End 
