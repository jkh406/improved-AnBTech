package com.anbtech.text;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;


public class StringProcess {
  	public static String ascToksc(String str){
  		try {
	    		if(str==null) return null;
	    		return new String(str.getBytes("8859_1"),"euc-kr");
	    	}catch ( UnsupportedEncodingException e){
	    		return str;
	    	}
  	}
  	
  	public static String kscToasc(String str){
  		try {
  			if (str==null) return null;
  			return new String(str.getBytes("euc-kr"),"8859_1");
  		}catch (UnsupportedEncodingException e){
  			return str;
  		}
  	}
	//이곳이 get으로 가져온 searchword를 처리하는 곳이다.
   	public static String kwordProcess(String str){
  		try {
			if(str==null) return null;
			return new String(str.getBytes("8859_1"),"euc-kr");
//			return new String(str.getBytes("KSC5601"),"8859_1");
	    	}catch ( UnsupportedEncodingException e){
	    		return str;
	    	}
  	}
  	 	
  	// 특정 문자열을 치환
	public static String repWord(String buffer, String OldWord, String NewWord) {
   		int sp=0;
   		int ep=0;

   		while (ep <= buffer.length()) {
       			ep = buffer.indexOf(OldWord, sp);
       			sp=ep+NewWord.length();
       			if (ep == -1) break;
       			buffer = buffer.substring(0, ep) + NewWord + buffer.substring(ep+OldWord.length());
   		}
   		return buffer;
	}
	
	//DB저장을 위한 특정문자를 치환 (' to `)
	public static String quoteReplace(String str) {
		return str.replace('\'','`');	
	}

  	// url주소에다가 링크걸기
	public static String repUrl(String buffer) {

		int sp=0;
		int ep=0;
		int cp=0;

		while (ep <= buffer.length()) {
			ep = buffer.indexOf("http://", sp);
			if (ep == -1) break;
			
			int b = buffer.indexOf("&nbsp;", ep);
			int n = buffer.indexOf("<br>", ep);

			// cp는 바뀌기전의 url까지를 구하는 넘이다. 4가지의 경우의수
			if(b != -1){
				if(n != -1) cp = b>n? n : b;
				else cp = b;
			}else{
				if(n != -1) cp = n;
				else cp = buffer.length();
			}
			String oldUrl = buffer.substring(ep, cp);
			String newUrl = "<a href='"+oldUrl+"' target='_blank'>"+oldUrl+"</a>";
			sp = ep+newUrl.length();
			buffer = buffer.substring(0, ep) + newUrl + buffer.substring(cp);
		}
		return buffer;
	}
	
	// 화폐단위로 출력
	public static String getMoneyFormat(String s){
		String temp = "";
		boolean chk = true;

		while (chk){
			if (s.length() >= 4){
				if (temp.length() >0)
					temp = "," + temp;
				temp = s.substring(s.length()-3) + temp;
				s = s.substring(0, s.length()-3);
			}else {
				if (temp.length()>0)
					temp = s + "," + temp;
				else
					temp = "0";
				chk = false;
			}
		}
		
		return temp;
	}
	
	public static String getMoneyFormat(int s){
		String str = s + "";
		return getMoneyFormat(str);
	}

	/*본문 내용을 가져와서 엔터 및 빈칸 처리를 한후 리턴 */
	public static String getContentTxt(String content){
		String result = "";
		if(content.equals("null")) result = "";
		for(int i=0; i<content.length(); i++) {
			if(content.charAt(i) == '\n') result += "<br>";
//			else if(content.charAt(i) == ' ') result += "&nbsp;";
			else result += content.charAt(i);
		}
		return result;
	}

	/*********************************************************
	 * 입력된 금액을 천단위 콤마를 찍어서 내 보낸다.
	 *********************************************************/
	public String getMoneyFormat(String value,String decimal_digit) throws Exception{
		
		DecimalFormat fmt;
		
		if(decimal_digit.equals("")) fmt = new java.text.DecimalFormat("#,###");
		else fmt = new java.text.DecimalFormat("#,###." + decimal_digit);

		String result = fmt.format(Double.parseDouble(value));

		return result;
	}

	/*********************************************************
	 * 입력된 금액을 단위절사시킨 후 천단위 콤마 찍어서 내 보냄.
	 *********************************************************/
	public String getMoneyFormat(String value,String decimal_digit,String cut_unit) throws Exception{
		
		DecimalFormat fmt;
		fmt = new java.text.DecimalFormat("#");
		value = fmt.format(Double.parseDouble(value));

		if(cut_unit.equals("1")){
			value = value.substring(0,value.length()-1) + "0";
		}
		else if(cut_unit.equals("10")){
			value = value.substring(0,value.length()-2) + "00";		
		}
		else if(cut_unit.equals("100")){
			value = value.substring(0,value.length()-3) + "000";		
		}
		else if(cut_unit.equals("1000")){
			value = value.substring(0,value.length()-4) + "0000";		
		}
		else if(cut_unit.equals("10000")){
			value = value.substring(0,value.length()-5) + "00000";		
		}

	
		if(decimal_digit.equals("")) fmt = new java.text.DecimalFormat("#,###");
		else fmt = new java.text.DecimalFormat("#,###." + decimal_digit);

		String result = fmt.format(Double.parseDouble(value));
//System.out.println(result);
		return result;
	}

}