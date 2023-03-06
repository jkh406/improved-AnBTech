package com.anbtech.file;
import java.io.*;
import java.io.File;
import java.sql.*;
import java.util.*;
import com.anbtech.util.normalFormat;
import com.anbtech.date.anbDate;

public class textFileReader extends Object {
	private normalFormat fmt = null;		//data output format
	private anbDate anbdt = null;
	
	//�����б� 
	private String file="";					//���ϸ� (path����)
	private BufferedReader d = null;
	private FileInputStream fin = null;
	private String data = "";				//line������ ���� ���ϳ��� (line ������:\n)
	private String[][] array;				//���� ���ϳ����� �迭�� ��� 
	private int elecnt=0;					//���� ������ �� ���δ� ������ ���� 
	private int linecnt=0;					//���� ������ ���ΰ��� 
		
	//------------------------------
	// ������ 
	//------------------------------
	public textFileReader()
	{
		fmt = new normalFormat();
		anbdt = new anbDate();

	}
		
	//------------------------------
	// ���� ���� ���� �Ѱ��ֱ� 
	//------------------------------
	public String getFileString(String file)
	{
		//���� �о� data�� ��� 
		setFile(file);
		if(openFile()) textReader();
		
		//System.out.println(this.data);
		return this.data;
			
	}
	//------------------------------
	// ���� ���� ���� �����ڷ� ������ ����
	//------------------------------
	public int getFileElementCount()
	{
		return elecnt;	
	}
	
	//------------------------------
	// �Ѷ��� ����
	//------------------------------
	public int getFileArrayCount()
	{
		return linecnt;	
	}

	//------------------------------
	// ���� ���� ���� �����ڷ� �����Ͽ� �迭�� ��� 
	//------------------------------
	public String[][] getFileArray()
	{
		return array;
	}	
	//------------------------------
	// ���� ���� ���� �����ڷ� �����Ͽ� �迭�� ��� 
	// ù��°�迭�� ������ȣ ���
	//------------------------------
	public void setFileArray(String file,String separator)
	{
		//���� �о� data�� ��� 
		setFile(file);
		if(openFile()) textReader();	//�����б� 
	
		
		//������ȣ �ʱⰪ �����ϱ� 
		fmt.setFormat("0000");		//�Ϸù�ȣ ��� ����(6�ڸ�)	
		String id = anbdt.getID();		//����Ͻú��� (13�ڸ�)
		
		//ù��° ���� �о� �ش��� �����ľ��ϱ� 
		//line�� ���(elements)���� ã�� 
		int nln = data.indexOf("\n");	
		this.elecnt = 0;
		if(nln != -1) {
			String eStr = data.substring(0,nln);
			StringTokenizer stEle = new StringTokenizer(eStr,separator);
			elecnt = stEle.countTokens()+1;			//element/line ���� (�Ϸù�ȣ ������� +1)
		}
		
		//���� ���� �迭�� ��� (��,ù��°�� �Ϸù�ȣ�� ��´�.)
		String tmp = "";
		StringTokenizer st = new StringTokenizer(this.data,"\n"); 			//data�� ������ �����Ͽ� ���� 
		this.linecnt = st.countTokens();				//line ���� 
		array = new String[linecnt][elecnt];			//�迭 �Ҵ��ϱ� 
		
		int n=0;					//���� �迭 ��ȣ
     	while (st.hasMoreTokens()) {
     		tmp = st.nextToken();					//line������ �б� 	
     		StringTokenizer substr = new StringTokenizer(tmp,separator); //�����ڷ� �����Ͽ� ���� 
 
 			array[n][0] = id+fmt.toDigits(n+1);
 			System.out.print(array[n][0] + " : ");
     		int e=1;	//���� �迭 ��ȣ
     		while(substr.hasMoreTokens()) {
     			array[n][e] = substr.nextToken();	//�� line�� ������(separator)�� �б� 
     			System.out.print(array[n][e] + " : ");
     			e++;
     		}
     		//System.out.println("");
     		n++;
      	
     	}	
	}
	//------------------------------
	// ���� ���� ���� �����ڷ� �����Ͽ� �迭�� ��� 
	// ���� ������ �״�� �迭�� ���. ��,�Ǿչ迭�� �������� ���ܵд�.
	//------------------------------
	public void readFileArray(String file,String separator)
	{
		//���� �о� private data�� ��� 
		setFile(file);
		if(openFile()) textReader();	//�����б� 
	
		//����� �������� ���ڷ� element���� ���Ѵ�.
		String one_value = "";
		this.elecnt = 0;
		StringTokenizer one_line = new StringTokenizer(this.data,"\n"); 
		while(one_line.hasMoreTokens()) {
			one_value = one_line.nextToken();	
			StringTokenizer oneEle = new StringTokenizer(one_value,separator);
			int one_cnt = oneEle.countTokens();
			if(elecnt < one_cnt) elecnt = one_cnt;		//����� ��
		}
		elecnt++;
		
		//���� ��ü�� line���� �˾Ƴ���.
		String tmp = "";
		StringTokenizer st = new StringTokenizer(this.data,"\n"); 			//data�� ������ �����Ͽ� ���� 
		this.linecnt = st.countTokens();									//line ���� 

		//�迭�� �Ҵ��Ѵ�.
		array = new String[linecnt][elecnt];	
		for(int i=0; i<linecnt; i++) for(int j=0; j<elecnt; j++) array[i][j]="";	//�迭clear : null������
		
		//���� ������ �����ڷ� �����Ͽ� �迭�� ��´�.
		int n=0;					
     	while (st.hasMoreTokens()) {
     		tmp = st.nextToken();											//line������ �б� 	
     		StringTokenizer substr = new StringTokenizer(tmp,separator);	//�����ڷ� �����Ͽ� ���� 
     		int e=1;	//���� �迭 ��ȣ
     		while(substr.hasMoreTokens()) {
     			array[n][e] = substr.nextToken();							//�� line�� ������(separator)�� �б� 
     			System.out.print(array[n][e] + " : ");
     			e++;
     		}
     		//System.out.println("");
     		n++;
     	}	
		
	}
		
	//------------------------------
	// import�� ����  �����ϱ� 
	//------------------------------	
	public boolean delFilename(String filepath)
	{
		File FN = new File(filepath);
		if(FN.exists()) return FN.delete();	
		else return false;
	}
	
	//------------------------------
	// import�� file name �ٲٱ�
	// file1 : import�� ���ϸ� 
	// file2 : �ٲ����ϸ� 
	//------------------------------	
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
		
	//------------------------------
	// ���� �����ϱ� 
	//------------------------------
	private void setFile(String file)
	{
		this.file = file;
	}
		
	//------------------------------
	// ���� open�ϱ� 
	//------------------------------
	private boolean openFile() 
	{
		try { 
			fin=new FileInputStream(new File(this.file)); 
			d = new BufferedReader(new InputStreamReader(fin));
			return true;
		} catch(FileNotFoundException e) {
			return false;
		} 
	}
	//------------------------------
	// Line ������ Text File�� �о� �����ֱ� 
	//------------------------------
	private void textReader()
	{
		String text="";
		data = "";
		try {
			while((text = d.readLine()) != null) data += text + "\n";
		} catch(IOException e) {
			data = "";
		}
		close();	//���� �ݱ� 
	}	

	//------------------------------
	// ID�� ���ϴ� �޼ҵ�
	//------------------------------
	public String getID()
	{
		String ID;
		
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		
		String y = first.format(now);
		String s = last.format(now);
		
		fmt.setFormat("000");		//�Ϸù�ȣ ��� ����(6�ڸ�)		
		ID = y + fmt.toDigits(Integer.parseInt(s));
		
		return ID;
	}

	//------------------------------
	//   ���� �ݱ� 
	//------------------------------		
	public void close()
	{
		try {
			d.close();
			fin.close();	
		} catch (Exception e) {				
		}
	}
	
	//------------------------------
	//   finalize()
	//------------------------------
	protected void finalize() throws Throwable
	{	  
		close();
	}		
	//------------------------------
	// main
	//------------------------------
	public static void main(String args[]) {
		textFileReader app = new textFileReader();
		app.fmt.setFormat("000000");
		for(int i=1; i < 3; i++) 
			//System.out.println(app.fmt.toDigits(i));
			
		//�����б�           
		System.out.println(app.getFileString("C:/temp/gw.txt"));
		
		//�����б� (������ ���� ���������� 
		//app.setFileArray("C:/temp/tabTEXT.txt","	");
		//System.out.println(app.getFileArrayCount());
		//System.out.println(app.delFilename("C:/Server/Tomcat/webapps/ROOT/pdm/import/tabTEXT.txt"));

	}
}
