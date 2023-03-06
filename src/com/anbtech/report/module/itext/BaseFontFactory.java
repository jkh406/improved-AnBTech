/* ===========================================================
 * BASEFONT : BASE FONT찾기
 * ===========================================================
 *
 * (C) Copyright 2005, A&B Tech
 *
 * ---------------------
 * .java
 * ---------------------
 * Changes
 * -------
 * 2005년 4월 15일 : Version 1;
 *
 */

package com.anbtech.report.module.itext;

import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.anbtech.report.util.HashNMap;
import com.anbtech.report.util.StringUtilities;

/**
 *
 * PDF파일에 필요한 시스템에서 제공되는 TrueType Fonts찾기
 *
 */
public final class BaseFontFactory extends DefaultFontMapper
{
	private final String font_file = tempPath()+"/eng_font_file.txt";	//properties파일 저장파일명(영문형태리스트)
	private final String hangul_font = tempPath()+"/han_font_file.txt";	//properties파일 저장파일명(한글폰트만)
	private static Properties notEmbeddedFonts;
    private static Properties fontsByName;								//font명 찾기
	private Properties confirmedFiles;									//확정된 파일map
	private final HashNMap fontByNameMap = new HashNMap();				//hash map파일명 담기(한글폰트담기용)

	/**
	 * 생성자
	 */
	public BaseFontFactory()
	{
		fontsByName = new Properties();
		notEmbeddedFonts = new Properties();
	}

	/**
	 * 초기화 진행하기
	 */
	public void setInitial() throws Exception
	{
		String directory = registerWindowsFontPath();			//window directory
		readFileName(directory);								//directory안의 파일을 필터하여 읽는다.
		storeProperties();										//검색된 모든 Fonts을 저장한다. [font_file]
		storeFileName();										//검색된 True Type Fonts(.ttc)만 저장한다.[hangul_font:현 한글폰트임]
	}

	/**
	 * 해당 디렉토리안의 파일LIST읽기
	 */
    public void readFileName(String directory) throws Exception
	{
		//String encoding = "UTF-16";											//ttc fonts
		//String encoding = "MS949";											//나머지 fonts
		String fontname="",font="";
		BaseFont bfont;
		FontPathFilter FONTPATHFILTER = new FontPathFilter();	//파일필터 만들기 [해당되는 파일만 읽는다]
		File file = new File(directory);						//해당디렉토리안의 파일들 읽기[디렉토리포함]
		File[] files = file.listFiles(FONTPATHFILTER);
		for(int i=0; i<files.length; i++) {
			String filename = files[i].toString();				//풀파일명 
			if(filename.toLowerCase().endsWith(".ttc")) {
				final String[] fontNames = BaseFont.enumerateTTCNames(filename);
				for(int j=0; j<fontNames.length; j++) {
					fontname = fontNames[j];								//fonts name
					font = filename+","+j;									//fonts (full path name)
					fontsByName.put(fontname,font);							//properties로 담기

					//System.out.println("fontNames : "+fontname);
					//System.out.println("fontFile : "+font);
					
					//한글폰트만 별도로 출력하기
					//bfont = BaseFont.createFont(font,BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
					bfont=BaseFont.createFont(font,"UTF-16",true,false,null,null);
					final String[][] fi = bfont.getFullFontName();
					int fi_len = fi.length;		//0:영어, 1:한글
					for (int k = fi_len-1; k < fi_len; k++)
					{
						  final String[] ffi = fi[k];
						  final String knownFontEmbeddedState = notEmbeddedFonts.getProperty(font, "false");

						  // if unknown or the known font is a restricted version and this one is none,
						  // then register the new font
						  String embedded = "true";
						  final String logicalFontname = ffi[3];
						  notEmbeddedFonts.setProperty (font, embedded);
						  if ((fontsByName.containsKey(logicalFontname) == false) ||
							  ((knownFontEmbeddedState.equals("true") == false) &&
							   embedded.equals(knownFontEmbeddedState) == false))
						  {
								fontByNameMap.add(logicalFontname,font);		//한글폰트만
								//System.out.println("Registered truetype font "+logicalFontname+"; File="+font);
						  }
					}
				}
			} else {
				String fs = System.getProperty("file.separator");
				int len = filename.length();
				fontname = filename.substring(filename.lastIndexOf(fs)+1,len);	//fonts name
				font = filename;												//fonts (full path name)
				fontsByName.put(fontname,font);									//properties로 담기

				//System.out.println("fontNames : "+fontname);
				//System.out.println("fontFile : "+font);
				//bfont=BaseFont.createFont(font,"MS949",false,false,null,null);

			}
		}
    }
	/**
	 * 검색한 모든 폰트리스트를 저장한다.
	 * Properties의 store메소드 활용
	 */
    public void storeProperties() throws Exception
	{
		Properties table = new Properties();
		FileOutputStream output = new FileOutputStream(font_file,false);		//내용 저장하기(append false)
		Enumeration keys = fontsByName.propertyNames();
        while (keys.hasMoreElements())
        {
          String fontName = (String) keys.nextElement();
          String fileName = fontsByName.getProperty(fontName);
		  table.put(fontName,fileName);
        }
		table.store(output,"fonts file list");
		output.close();
	}
	/**
	 * 검색한 한글폰트리스트를 한글로 저장하기 [True Type Font만을 저장한다.]
	 * BufferedWriter활용
	 */
    public void storeFileName() throws Exception
	{
		BufferedWriter bw = null;
		String content = "";

		Iterator it = fontByNameMap.keys();
        while (it.hasNext())
        {
              String fileName = (String) it.next();
			  bw = new BufferedWriter(new FileWriter(hangul_font));
			  content += fileName+"="+fontByNameMap.getFirst(fileName).toString()+"\n"; 
        }
		bw.write(content);
		bw.close();
	}

	/**
	 * 한글폰트저장 파일만 읽어서 리턴하기
	 * return : hashmap
	 */
    public HashMap getHanguelFont() throws Exception
	{
		File f = new File(hangul_font);
		if(!f.exists()) setInitial();	//초기화 진행
		return readStoreFile(hangul_font);
	}
	/**
	 * 저장된 폰트파일 내용 읽기
	 * 구분자가 "="임
	 *
	 * return : hashmap
	 */
    public HashMap readStoreFile(String filename) throws Exception
	{
		HashMap confMap = new HashMap();	//초기화

		//properties 파일명
		File fn = new File(filename);
		if(!fn.exists()) return confMap;			//file이 없으면 종료

		FileInputStream fin=new FileInputStream(fn); 
		BufferedReader d = new BufferedReader(new InputStreamReader(fin));
		String text="",name="",value="";
		int ing = 0;
		while((text = d.readLine()) != null) { 
			if((text.indexOf("#") == -1) && (text.length() > 5)) {		//주석제외
				int n = text.indexOf("=");		//key와 value구분자
				if(n != -1) {
					//이어질 문장이 없이 새로운 문장이 시작됨으로 put
					if(ing > 0) { 
						//System.out.println(name+":"+value);
						confMap.put(new String(name),new String(value)); 
						name="";
						value="";
						ing = 0; 
					}
					//key와 value로 나누워 읽자.(공백은 없애...)
					name = text.substring(0,n).trim();
					value = text.substring(n+1,text.length()).trim();
					ing++;					//일단 이어질 문장이 있다고 판단하자. 그리고 위 또는 마지막에 put
				} else {
					value += " "+text;		//붙어있는 문자 한칸 공백 만들어 
					ing++;					//이어질 문장이 있음
				}
			}
		}

		//마지막줄을 읽어 반드시 put한다.
		if(ing > 0) { 
			//System.out.println(name+":"+value);		
			confMap.put(new String(name),new String(value)); 
		}
		return confMap;
    }

	/**
	 * 임시 디렉토리 찾기
	 */
	private String tempPath()
	{
		String path = System.getProperty("java.io.tmpdir");
		return path;
	}
	/**
	 * 윈도우즈 OS의 Fonts 디렉토리 찾기
	 * @param encoding : default font endoding
	 * @param knownFonts : 폰트 map
	 * @param seenFiles  : 폰트 파일
	 */
	private String registerWindowsFontPath()
	{
		String fontPath = null;
		final String windirs = System.getProperty("java.library.path");
		final String fs = System.getProperty("file.separator");

		if (windirs != null)
		{
		  final StringTokenizer strtok
			  = new StringTokenizer(windirs, System.getProperty("path.separator"));
		  while (strtok.hasMoreTokens())
		  {
			final String token = strtok.nextToken();
			if (StringUtilities.endsWithIgnoreCase(token, "System32"))
			{
			  // found windows folder ;-)
			  final int lastBackslash = token.lastIndexOf(fs);
			  fontPath = token.substring(0, lastBackslash) + fs + "Fonts";
			  break;
			}
		  }
		}
		//System.out.println("Fonts located in \"" + fontPath + "\"");
		return fontPath;
	}

	//******************************************************************
	// 파일필터 클래스 만들기
	//*******************************************************************
	private static class FontPathFilter implements FileFilter
   {
		/**
		 * Default Constructor.
		 */
		public FontPathFilter()
		{
		}

		/**
		* 절대path에 해당파일을 포함하고 있는지 판단
		*
		* @param  pathname  The abstract pathname to be tested
		* @return  <code>true</code> if and only if <code>pathname</code>
		*          should be included
		**/
		public boolean accept(final File pathname)
		{
		  if (pathname.canRead() == false) return false;		//읽을수 없다면 false
		  if (pathname.isDirectory()) return true;				//디렉토리이면 true

		  //아래의 확장을 갖는 파일만을 읽는다
		  final String name = pathname.getName();				//파일명 읽기
		  if (StringUtilities.endsWithIgnoreCase(name, ".afm")) return true;
		  if (StringUtilities.endsWithIgnoreCase(name, ".ttf")) return true;
		  if (StringUtilities.endsWithIgnoreCase(name, ".ttc")) return true;
		  if (StringUtilities.endsWithIgnoreCase(name, ".otf")) return true;
		  return false;
		}
	 }

	 /**
	 * Default Encoding구하기
	 **/
	 public String getPlatformDefaultEncoding()
	 {
		try
		{
		  return System.getProperty("file.encoding", "Cp1252");
		}
		catch (SecurityException se)
		{
		  return "Cp1252";
		}
	 }

	 //test
	 public static void main(java.lang.String[] args) throws Exception {
		BaseFontFactory tst = new BaseFontFactory();
		try {

			//font 읽기 초기화진행
			tst.setInitial();
			//String directory = tst.registerWindowsFontPath();		//window directory
			//String directory = "D:/tomcat4/webapps/ROOT/WEB-INF/Fonts";
			//tst.readFileName(directory);						//directory안의 파일을 필터하여 읽는다.
			//tst.storeProperties();
			//tst.storeFileName();


			//System.out.println(tst.getPlatformDefaultEncoding());

			//test
			//System.out.println(System.getProperty("file.separator"));
			//System.out.println(System.getProperty("file.separato",File.separator));
	

		} catch (Exception e) {
			System.out.println("Exception - " + e.toString());
		}
		return;
	}

}