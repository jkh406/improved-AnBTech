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
	   생성자 
	*********************************************************************/  	
	public FileWriteString()
	{
		
	}

	/*********************************************************************
	   내용을 파일로 담기
	*********************************************************************/

	//--------------------------------------------------------------------
	//   저장할 파일명지정
	//--------------------------------------------------------------------
	public void setFilename(String file)
	{
		this.file_name = file;	
	}
	
	//--------------------------------------------------------------------
	//   저장할 파일Path 지정 
	//--------------------------------------------------------------------
	public void setFilepath(String path)
	{
    	dir = new File(path);
		
    	// 디렉토리가 이미 있는지 ??  없으면 새로 생성    
    	if (!dir.exists()) dir.mkdirs();
    
		this.file_path = path;	
	}	
	
	//--------------------------------------------------------------------
	//   내용을 파일로 담을 준비를 한다.
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
	//   내용을 파일로 담든다.(Byte)
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
	//   파일 닫기 
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
	   한글을 파일로 담기
	*********************************************************************/	
	public void WriteHanguel(String FullPathName,String fileName,String content)
	{
		//디렉토리 있는지 검사하기
		dir = new File(FullPathName);
    	if (!dir.exists()) dir.mkdirs();

		//파일로 담기
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
	*   file name 바꾸기
	*******************************************************************************************/	
	public boolean chgFilename(String file1,String file2)
	{
		File BFN = new File(file1);		//바꿀File명
		File AFN = new File(file2);		//새로운 파일명
		if(BFN.exists()) return BFN.renameTo(AFN);	
		else return false;
	}

	/*********************************************************************
	 	파일 복사하기
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
