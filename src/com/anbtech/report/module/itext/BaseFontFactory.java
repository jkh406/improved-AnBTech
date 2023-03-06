/* ===========================================================
 * BASEFONT : BASE FONTã��
 * ===========================================================
 *
 * (C) Copyright 2005, A&B Tech
 *
 * ---------------------
 * .java
 * ---------------------
 * Changes
 * -------
 * 2005�� 4�� 15�� : Version 1;
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
 * PDF���Ͽ� �ʿ��� �ý��ۿ��� �����Ǵ� TrueType Fontsã��
 *
 */
public final class BaseFontFactory extends DefaultFontMapper
{
	private final String font_file = tempPath()+"/eng_font_file.txt";	//properties���� �������ϸ�(�������¸���Ʈ)
	private final String hangul_font = tempPath()+"/han_font_file.txt";	//properties���� �������ϸ�(�ѱ���Ʈ��)
	private static Properties notEmbeddedFonts;
    private static Properties fontsByName;								//font�� ã��
	private Properties confirmedFiles;									//Ȯ���� ����map
	private final HashNMap fontByNameMap = new HashNMap();				//hash map���ϸ� ���(�ѱ���Ʈ����)

	/**
	 * ������
	 */
	public BaseFontFactory()
	{
		fontsByName = new Properties();
		notEmbeddedFonts = new Properties();
	}

	/**
	 * �ʱ�ȭ �����ϱ�
	 */
	public void setInitial() throws Exception
	{
		String directory = registerWindowsFontPath();			//window directory
		readFileName(directory);								//directory���� ������ �����Ͽ� �д´�.
		storeProperties();										//�˻��� ��� Fonts�� �����Ѵ�. [font_file]
		storeFileName();										//�˻��� True Type Fonts(.ttc)�� �����Ѵ�.[hangul_font:�� �ѱ���Ʈ��]
	}

	/**
	 * �ش� ���丮���� ����LIST�б�
	 */
    public void readFileName(String directory) throws Exception
	{
		//String encoding = "UTF-16";											//ttc fonts
		//String encoding = "MS949";											//������ fonts
		String fontname="",font="";
		BaseFont bfont;
		FontPathFilter FONTPATHFILTER = new FontPathFilter();	//�������� ����� [�ش�Ǵ� ���ϸ� �д´�]
		File file = new File(directory);						//�ش���丮���� ���ϵ� �б�[���丮����]
		File[] files = file.listFiles(FONTPATHFILTER);
		for(int i=0; i<files.length; i++) {
			String filename = files[i].toString();				//Ǯ���ϸ� 
			if(filename.toLowerCase().endsWith(".ttc")) {
				final String[] fontNames = BaseFont.enumerateTTCNames(filename);
				for(int j=0; j<fontNames.length; j++) {
					fontname = fontNames[j];								//fonts name
					font = filename+","+j;									//fonts (full path name)
					fontsByName.put(fontname,font);							//properties�� ���

					//System.out.println("fontNames : "+fontname);
					//System.out.println("fontFile : "+font);
					
					//�ѱ���Ʈ�� ������ ����ϱ�
					//bfont = BaseFont.createFont(font,BaseFont.IDENTITY_H,BaseFont.EMBEDDED);
					bfont=BaseFont.createFont(font,"UTF-16",true,false,null,null);
					final String[][] fi = bfont.getFullFontName();
					int fi_len = fi.length;		//0:����, 1:�ѱ�
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
								fontByNameMap.add(logicalFontname,font);		//�ѱ���Ʈ��
								//System.out.println("Registered truetype font "+logicalFontname+"; File="+font);
						  }
					}
				}
			} else {
				String fs = System.getProperty("file.separator");
				int len = filename.length();
				fontname = filename.substring(filename.lastIndexOf(fs)+1,len);	//fonts name
				font = filename;												//fonts (full path name)
				fontsByName.put(fontname,font);									//properties�� ���

				//System.out.println("fontNames : "+fontname);
				//System.out.println("fontFile : "+font);
				//bfont=BaseFont.createFont(font,"MS949",false,false,null,null);

			}
		}
    }
	/**
	 * �˻��� ��� ��Ʈ����Ʈ�� �����Ѵ�.
	 * Properties�� store�޼ҵ� Ȱ��
	 */
    public void storeProperties() throws Exception
	{
		Properties table = new Properties();
		FileOutputStream output = new FileOutputStream(font_file,false);		//���� �����ϱ�(append false)
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
	 * �˻��� �ѱ���Ʈ����Ʈ�� �ѱ۷� �����ϱ� [True Type Font���� �����Ѵ�.]
	 * BufferedWriterȰ��
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
	 * �ѱ���Ʈ���� ���ϸ� �о �����ϱ�
	 * return : hashmap
	 */
    public HashMap getHanguelFont() throws Exception
	{
		File f = new File(hangul_font);
		if(!f.exists()) setInitial();	//�ʱ�ȭ ����
		return readStoreFile(hangul_font);
	}
	/**
	 * ����� ��Ʈ���� ���� �б�
	 * �����ڰ� "="��
	 *
	 * return : hashmap
	 */
    public HashMap readStoreFile(String filename) throws Exception
	{
		HashMap confMap = new HashMap();	//�ʱ�ȭ

		//properties ���ϸ�
		File fn = new File(filename);
		if(!fn.exists()) return confMap;			//file�� ������ ����

		FileInputStream fin=new FileInputStream(fn); 
		BufferedReader d = new BufferedReader(new InputStreamReader(fin));
		String text="",name="",value="";
		int ing = 0;
		while((text = d.readLine()) != null) { 
			if((text.indexOf("#") == -1) && (text.length() > 5)) {		//�ּ�����
				int n = text.indexOf("=");		//key�� value������
				if(n != -1) {
					//�̾��� ������ ���� ���ο� ������ ���۵����� put
					if(ing > 0) { 
						//System.out.println(name+":"+value);
						confMap.put(new String(name),new String(value)); 
						name="";
						value="";
						ing = 0; 
					}
					//key�� value�� ������ ����.(������ ����...)
					name = text.substring(0,n).trim();
					value = text.substring(n+1,text.length()).trim();
					ing++;					//�ϴ� �̾��� ������ �ִٰ� �Ǵ�����. �׸��� �� �Ǵ� �������� put
				} else {
					value += " "+text;		//�پ��ִ� ���� ��ĭ ���� ����� 
					ing++;					//�̾��� ������ ����
				}
			}
		}

		//���������� �о� �ݵ�� put�Ѵ�.
		if(ing > 0) { 
			//System.out.println(name+":"+value);		
			confMap.put(new String(name),new String(value)); 
		}
		return confMap;
    }

	/**
	 * �ӽ� ���丮 ã��
	 */
	private String tempPath()
	{
		String path = System.getProperty("java.io.tmpdir");
		return path;
	}
	/**
	 * �������� OS�� Fonts ���丮 ã��
	 * @param encoding : default font endoding
	 * @param knownFonts : ��Ʈ map
	 * @param seenFiles  : ��Ʈ ����
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
	// �������� Ŭ���� �����
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
		* ����path�� �ش������� �����ϰ� �ִ��� �Ǵ�
		*
		* @param  pathname  The abstract pathname to be tested
		* @return  <code>true</code> if and only if <code>pathname</code>
		*          should be included
		**/
		public boolean accept(final File pathname)
		{
		  if (pathname.canRead() == false) return false;		//������ ���ٸ� false
		  if (pathname.isDirectory()) return true;				//���丮�̸� true

		  //�Ʒ��� Ȯ���� ���� ���ϸ��� �д´�
		  final String name = pathname.getName();				//���ϸ� �б�
		  if (StringUtilities.endsWithIgnoreCase(name, ".afm")) return true;
		  if (StringUtilities.endsWithIgnoreCase(name, ".ttf")) return true;
		  if (StringUtilities.endsWithIgnoreCase(name, ".ttc")) return true;
		  if (StringUtilities.endsWithIgnoreCase(name, ".otf")) return true;
		  return false;
		}
	 }

	 /**
	 * Default Encoding���ϱ�
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

			//font �б� �ʱ�ȭ����
			tst.setInitial();
			//String directory = tst.registerWindowsFontPath();		//window directory
			//String directory = "D:/tomcat4/webapps/ROOT/WEB-INF/Fonts";
			//tst.readFileName(directory);						//directory���� ������ �����Ͽ� �д´�.
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