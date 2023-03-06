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
	
	//파일읽기 
	private String file="";					//파일명 (path포함)
	private BufferedReader d = null;
	private FileInputStream fin = null;
	private String data = "";				//line단위로 읽은 파일내용 (line 구분자:\n)
	private String[][] array;				//읽은 파일내용을 배열에 담기 
	private int elecnt=0;					//읽은 파일의 한 라인당 데이터 갯수 
	private int linecnt=0;					//읽은 파일의 라인갯수 
		
	//------------------------------
	// 생성자 
	//------------------------------
	public textFileReader()
	{
		fmt = new normalFormat();
		anbdt = new anbDate();

	}
		
	//------------------------------
	// 읽은 파일 내용 넘겨주기 
	//------------------------------
	public String getFileString(String file)
	{
		//파일 읽어 data에 담기 
		setFile(file);
		if(openFile()) textReader();
		
		//System.out.println(this.data);
		return this.data;
			
	}
	//------------------------------
	// 읽은 파일 내용 구분자로 구별한 갯수
	//------------------------------
	public int getFileElementCount()
	{
		return elecnt;	
	}
	
	//------------------------------
	// 총라인 갯수
	//------------------------------
	public int getFileArrayCount()
	{
		return linecnt;	
	}

	//------------------------------
	// 읽은 파일 내용 구분자로 구별하여 배열에 담기 
	//------------------------------
	public String[][] getFileArray()
	{
		return array;
	}	
	//------------------------------
	// 읽은 파일 내용 구분자로 구별하여 배열에 담기 
	// 첫번째배열은 관리번호 담기
	//------------------------------
	public void setFileArray(String file,String separator)
	{
		//파일 읽어 data에 담기 
		setFile(file);
		if(openFile()) textReader();	//내용읽기 
	
		
		//관리번호 초기값 설정하기 
		fmt.setFormat("0000");		//일련번호 출력 형식(6자리)	
		String id = anbdt.getID();		//년월일시분초 (13자리)
		
		//첫번째 라인 읽어 해당요소 수량파악하기 
		//line당 요소(elements)숫자 찾기 
		int nln = data.indexOf("\n");	
		this.elecnt = 0;
		if(nln != -1) {
			String eStr = data.substring(0,nln);
			StringTokenizer stEle = new StringTokenizer(eStr,separator);
			elecnt = stEle.countTokens()+1;			//element/line 갯수 (일련번호 담기위해 +1)
		}
		
		//일은 내용 배열로 담기 (단,첫번째는 일련번호를 담는다.)
		String tmp = "";
		StringTokenizer st = new StringTokenizer(this.data,"\n"); 			//data의 내용을 구분하여 읽음 
		this.linecnt = st.countTokens();				//line 갯수 
		array = new String[linecnt][elecnt];			//배열 할당하기 
		
		int n=0;					//담을 배열 번호
     	while (st.hasMoreTokens()) {
     		tmp = st.nextToken();					//line단위로 읽기 	
     		StringTokenizer substr = new StringTokenizer(tmp,separator); //구분자로 구분하여 읽음 
 
 			array[n][0] = id+fmt.toDigits(n+1);
 			System.out.print(array[n][0] + " : ");
     		int e=1;	//담을 배열 번호
     		while(substr.hasMoreTokens()) {
     			array[n][e] = substr.nextToken();	//한 line당 구분자(separator)로 읽기 
     			System.out.print(array[n][e] + " : ");
     			e++;
     		}
     		//System.out.println("");
     		n++;
      	
     	}	
	}
	//------------------------------
	// 읽은 파일 내용 구분자로 구별하여 배열에 담기 
	// 읽은 파일을 그대로 배열에 담기. 단,맨앞배열은 공란으로 남겨둔다.
	//------------------------------
	public void readFileArray(String file,String separator)
	{
		//파일 읽어 private data에 담기 
		setFile(file);
		if(openFile()) textReader();	//내용읽기 
	
		//가장긴 구분자의 숫자로 element값을 구한다.
		String one_value = "";
		this.elecnt = 0;
		StringTokenizer one_line = new StringTokenizer(this.data,"\n"); 
		while(one_line.hasMoreTokens()) {
			one_value = one_line.nextToken();	
			StringTokenizer oneEle = new StringTokenizer(one_value,separator);
			int one_cnt = oneEle.countTokens();
			if(elecnt < one_cnt) elecnt = one_cnt;		//가장긴 값
		}
		elecnt++;
		
		//읽은 전체의 line수를 알아낸다.
		String tmp = "";
		StringTokenizer st = new StringTokenizer(this.data,"\n"); 			//data의 내용을 구분하여 읽음 
		this.linecnt = st.countTokens();									//line 갯수 

		//배열을 할당한다.
		array = new String[linecnt][elecnt];	
		for(int i=0; i<linecnt; i++) for(int j=0; j<elecnt; j++) array[i][j]="";	//배열clear : null값없애
		
		//읽은 내용을 구분자로 구분하여 배열에 담는다.
		int n=0;					
     	while (st.hasMoreTokens()) {
     		tmp = st.nextToken();											//line단위로 읽기 	
     		StringTokenizer substr = new StringTokenizer(tmp,separator);	//구분자로 구분하여 읽음 
     		int e=1;	//담을 배열 번호
     		while(substr.hasMoreTokens()) {
     			array[n][e] = substr.nextToken();							//한 line당 구분자(separator)로 읽기 
     			System.out.print(array[n][e] + " : ");
     			e++;
     		}
     		//System.out.println("");
     		n++;
     	}	
		
	}
		
	//------------------------------
	// import한 파일  삭제하기 
	//------------------------------	
	public boolean delFilename(String filepath)
	{
		File FN = new File(filepath);
		if(FN.exists()) return FN.delete();	
		else return false;
	}
	
	//------------------------------
	// import한 file name 바꾸기
	// file1 : import한 파일명 
	// file2 : 바꿀파일명 
	//------------------------------	
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
		
	//------------------------------
	// 파일 셋팅하기 
	//------------------------------
	private void setFile(String file)
	{
		this.file = file;
	}
		
	//------------------------------
	// 파일 open하기 
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
	// Line 단위로 Text File을 읽어 돌려주기 
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
		close();	//파일 닫기 
	}	

	//------------------------------
	// ID을 구하는 메소드
	//------------------------------
	public String getID()
	{
		String ID;
		
		java.util.Date now = new java.util.Date();
		java.text.SimpleDateFormat first = new java.text.SimpleDateFormat("yyyyMMddHHmm");
		java.text.SimpleDateFormat last  = new java.text.SimpleDateFormat("SS");
		
		String y = first.format(now);
		String s = last.format(now);
		
		fmt.setFormat("000");		//일련번호 출력 형식(6자리)		
		ID = y + fmt.toDigits(Integer.parseInt(s));
		
		return ID;
	}

	//------------------------------
	//   파일 닫기 
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
			
		//파일읽기           
		System.out.println(app.getFileString("C:/temp/gw.txt"));
		
		//파일읽기 (구분자 구분 개별단위로 
		//app.setFileArray("C:/temp/tabTEXT.txt","	");
		//System.out.println(app.getFileArrayCount());
		//System.out.println(app.delFilename("C:/Server/Tomcat/webapps/ROOT/pdm/import/tabTEXT.txt"));

	}
}
