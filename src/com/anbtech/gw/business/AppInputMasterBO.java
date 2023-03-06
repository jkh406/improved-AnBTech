package com.anbtech.gw.business;

import java.io.PrintStream;
import java.util.StringTokenizer;

import java.util.*;
import java.io.*;

public class AppInputMasterBO
{
	private String doc_wri = "";
	private String doc_rev = "";
	private String doc_agr = "";
	private String doc_agr2 = "";
	private String doc_agr3 = "";
	private String doc_agr4 = "";
	private String doc_agr5 = "";
	private String doc_agr6 = "";
	private String doc_agr7 = "";
	private String doc_agr8 = "";
	private String doc_agr9 = "";
	private String doc_agr10 = "";
	private String doc_dec = "";
	private String doc_rec = "";
	private String doc_lin = "";
	private String doc_lin_pal = "";
	private String doc_ste = "";
	private String SEC_DATA[];
	private int in_cnt = 0;
	private int agree_cnt = 1;


    public AppInputMasterBO()
    {
        
    }

    private void lineAnalysis(String s)
    {
        String s1 = s;
        SEC_DATA = new String[s1.length()];
        in_cnt = 1;
        String s2 = "";
        for(StringTokenizer stringtokenizer = new StringTokenizer(s, "\n"); stringtokenizer.hasMoreTokens();)
        {
            String s4 = stringtokenizer.nextToken();
            for(StringTokenizer stringtokenizer1 = new StringTokenizer(s4, " "); stringtokenizer1.hasMoreTokens();)
            {
                String s3 = stringtokenizer1.nextToken();
                if(s3.length() < 1 || s3.equals("기안"))
                    break;
                SEC_DATA[in_cnt] = s3;
                in_cnt++;
            }

        }

    }

    private void lineOrder()
    {
        agree_cnt = 1;
        for(int i = 1; i < in_cnt;)
            if(SEC_DATA[i].equals("검토"))
            {
                doc_rev = SEC_DATA[++i];
                i++;
                i++;
                if(doc_lin.length() == 0)
                {
                    doc_lin = "APV";
                    doc_lin_pal = "APV";
                } else
                {
                    doc_lin = doc_lin + "," + "APV";
                    doc_lin_pal = doc_lin_pal + "," + "APV";
                }
            } else
            if(SEC_DATA[i].equals("승인"))
            {
                doc_dec = SEC_DATA[++i];
                i++;
                i++;
                if(doc_lin.length() == 0)
                {
                    doc_lin = "APL";
                    doc_lin_pal = "APL";
                } else
                {
                    doc_lin = doc_lin + "," + "APL";
                    doc_lin_pal = doc_lin_pal + "," + "APL";
                }
            } else
            if(SEC_DATA[i].equals("협조"))
            {
                if(agree_cnt == 1)
                {
                    doc_agr = SEC_DATA[++i];
                    i++;
                    i++;
                    if(doc_lin.length() == 0)
                    {
                        doc_lin = "APG";
                        doc_lin_pal = "APG";
                    } else
                    {
                        doc_lin = doc_lin + "," + "APG";
                        doc_lin_pal = doc_lin_pal + "," + "APG";
                    }
                    agree_cnt++;
                } else
                {
                    if(agree_cnt == 2)
                        doc_agr2 = SEC_DATA[++i];
                    else
                    if(agree_cnt == 3)
                        doc_agr3 = SEC_DATA[++i];
                    else
                    if(agree_cnt == 4)
                        doc_agr4 = SEC_DATA[++i];
                    else
                    if(agree_cnt == 5)
                        doc_agr5 = SEC_DATA[++i];
                    else
                    if(agree_cnt == 6)
                        doc_agr6 = SEC_DATA[++i];
                    else
                    if(agree_cnt == 7)
                        doc_agr7 = SEC_DATA[++i];
                    else
                    if(agree_cnt == 8)
                        doc_agr8 = SEC_DATA[++i];
                    else
                    if(agree_cnt == 9)
                        doc_agr9 = SEC_DATA[++i];
                    else
                    if(agree_cnt == 10)
                        doc_agr10 = SEC_DATA[++i];
                    i++;
                    i++;
                    doc_lin = doc_lin + "," + "APG" + Integer.toString(agree_cnt);
                    agree_cnt++;
                }
            } else
            if(SEC_DATA[i].equals("통보"))
            {
                if(doc_rec.length() == 0)
                {
                    doc_rec = SEC_DATA[++i];
                    if(doc_lin.length() == 0)
                    {
                        doc_lin = "API";
                        doc_lin_pal = "API";
                    } else
                    {
                        doc_lin = doc_lin + "," + "API";
                        doc_lin_pal = doc_lin_pal + "," + "API";
                    }
                } else
                {
                    doc_rec = doc_rec + "," + SEC_DATA[++i];
                }
                i++;
                i++;
            } else
            {
                i++;
            }


    }

    public void exeLineData(String s)
    {
        String s1 = s;
        lineAnalysis(s1);
        lineOrder();
    }

    public String getAppLine()
    {
        return doc_lin;
    }

    public String getAppLineParallel()
    {
        return doc_lin_pal;
    }

    public String getAppAgreeCnt()
    {
        String s = "0";
        if(agree_cnt == 0)
        {
            return s;
        } else
        {
            agree_cnt = agree_cnt - 1;
            String s1 = Integer.toString(agree_cnt);
            return s1;
        }
    }

    public String getAppLineState()
    {
        if(SEC_DATA[1].equals("검토"))
            doc_ste = "APV";
        else
        if(SEC_DATA[1].equals("승인"))
            doc_ste = "APL";
        else
        if(SEC_DATA[1].equals("협조"))
            doc_ste = "APG";
        return doc_ste;
    }

    public String getAppRev()
    {
        return doc_rev;
    }

    public String getAppAgr()
    {
        return doc_agr;
    }

    public String getAppAgr2()
    {
        return doc_agr2;
    }

    public String getAppAgr3()
    {
        return doc_agr3;
    }

    public String getAppAgr4()
    {
        return doc_agr4;
    }

    public String getAppAgr5()
    {
        return doc_agr5;
    }

    public String getAppAgr6()
    {
        return doc_agr6;
    }

    public String getAppAgr7()
    {
        return doc_agr7;
    }

    public String getAppAgr8()
    {
        return doc_agr8;
    }

    public String getAppAgr9()
    {
        return doc_agr9;
    }

    public String getAppAgr10()
    {
        return doc_agr10;
    }

    public String getAppDec()
    {
        return doc_dec;
    }

    public String getAppRec()
    {
        return doc_rec;
    }

    public String StrToNumFile(String s, String s1, String s2)
    {
        String s3 = s;
        String s4 = s1;
        String s5 = s2;
        int i = s3.indexOf(".");
        if(i == 0)
        {
            return s4 + "_" + s5;
        } else
        {
            String s6 = s3.substring(0, i);
            String s7 = s3.substring(i + 1, s3.length());
            return s4 + "_" + s5 + "." + s7;
        }
    }

	/******************************************
	 * 수정 시 첨부파일을 처리한다.
	 ******************************************/
	public String saveAttachFile(String old_link,String new_link,String del_link,String upload_path) throws Exception{


		String result = old_link;

		//기존 첨부링크에서 삭제링크를 뺀다.
		StringTokenizer str2 = new StringTokenizer(del_link,"|");
		for(int j=0; j<str2.countTokens(); j++){
			String sel_link = str2.nextToken();
			result = replace(result,sel_link+"|","");
			delAttachFile(sel_link,upload_path);
		}

		//새로 첨부된 파일링크를 추가
		result += new_link;

		return result;
	}
	/******************************************
	 * 삭제시 첨부된 파일을 서버에서 삭제처리
	 ******************************************/
	public void deleteAttachFile(String del_link,String upload_path) throws Exception{
		StringTokenizer str2 = new StringTokenizer(del_link,"|");
		for(int j=0; j<str2.countTokens(); j++){
			String sel_link = str2.nextToken();
			delAttachFile(sel_link,upload_path);
		}
	}

	//특정문자열을 지정된 문자열로 대치
	public String replace(String str, String pattern, String replace) { 
		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();

		while ((e = str.indexOf(pattern, s)) >= 0) { 
		 result.append(str.substring(s, e)); 
		 result.append(replace); 
		 s = e + pattern.length(); 
		} 
		result.append(str.substring(s)); 
		return result.toString(); 
	}

	//선택된 링크의 파일을 서버에서 삭제처리
	public void delAttachFile(String filelink, String uploadpath) { 
		uploadpath = "d:/tomcat4/webapps/ROOT";

		File myDir = new File(uploadpath);
		File delFile = new File(myDir,filelink);
		if(delFile.exists()) delFile.delete();
	}

	//TEST
    public static void main(String args[])
        throws Exception
    {
        AppInputMasterBO appinputmasterbo = new AppInputMasterBO();
        String s = "";
 /*       s = "기안 A030003 박동렬 연구실\n";
        s = s + "검토 A030003 박동렬 연구실\n협조 A030001 조진성 연구실\n";
        s = s + "협조 A030003 박동렬 연구실\n헙조 A030001 조진성 연구실\n";
        s = s + "협조 A030003 박동렬 연구실\n승인 A030001 조진성 연구실\n";
        s = s + "통보 A030003 박동렬 연구실\n통보 A030001 조진성 연구실\n";
        s = s + "통보 A030003 박동렬 연구실\n통보 A030001 조진성 연구실\n";
        s = s + "통보 A030003 박동렬 연구실\n통보 A030001 조진성 연구실\n";
*/
		s = "승인 A030001 조진성 대표이사\n";
		s = s + "통보 a 박동렬 선임연구원\n";
		s = s + "통보 A030004 개발일 선임연구원\n";
		s = s + "통보 A030005 개발이 선임연구원\n";
		s = s + "통보 A030006 개발삼 선임연구원\n";
		s = s + "통보 A030007 개발사 선임연구원\n";
		s = s + "통보 A030008 개발오 선임연구원\n";
		s = s + "통보 A030009 개발육 선임연구원\n";
		s = s + "통보 A030010 개발칠 선임연구원\n";
        try
        {
            appinputmasterbo.exeLineData(s);
            System.out.println("순차적:" + appinputmasterbo.getAppLine());
            System.out.println("일괄적:" + appinputmasterbo.getAppLineParallel());
            System.out.println("검토 : " + appinputmasterbo.getAppRev());
            System.out.println("승인 : " + appinputmasterbo.getAppDec());
            System.out.println("협조 : " + appinputmasterbo.getAppAgr());
            System.out.println("상태 :" + appinputmasterbo.getAppLineState());
            System.out.println("협조자수 :" + appinputmasterbo.getAppAgreeCnt());
            System.out.println(appinputmasterbo.StrToNumFile("rk.txt", "123", "1"));
        }
        catch(Exception exception)
        {
            System.out.println(exception);
        }
    }
}
