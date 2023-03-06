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
	//�̰��� get���� ������ searchword�� ó���ϴ� ���̴�.
   	public static String kwordProcess(String str){
  		try {
			if(str==null) return null;
			return new String(str.getBytes("8859_1"),"euc-kr");
//			return new String(str.getBytes("KSC5601"),"8859_1");
	    	}catch ( UnsupportedEncodingException e){
	    		return str;
	    	}
  	}
  	 	
  	// Ư�� ���ڿ��� ġȯ
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
	
	//DB������ ���� Ư�����ڸ� ġȯ (' to `)
	public static String quoteReplace(String str) {
		return str.replace('\'','`');	
	}

  	// url�ּҿ��ٰ� ��ũ�ɱ�
	public static String repUrl(String buffer) {

		int sp=0;
		int ep=0;
		int cp=0;

		while (ep <= buffer.length()) {
			ep = buffer.indexOf("http://", sp);
			if (ep == -1) break;
			
			int b = buffer.indexOf("&nbsp;", ep);
			int n = buffer.indexOf("<br>", ep);

			// cp�� �ٲ������ url������ ���ϴ� ���̴�. 4������ ����Ǽ�
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
	
	// ȭ������� ���
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

	/*���� ������ �����ͼ� ���� �� ��ĭ ó���� ���� ���� */
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
	 * �Էµ� �ݾ��� õ���� �޸��� �� �� ������.
	 *********************************************************/
	public String getMoneyFormat(String value,String decimal_digit) throws Exception{
		
		DecimalFormat fmt;
		
		if(decimal_digit.equals("")) fmt = new java.text.DecimalFormat("#,###");
		else fmt = new java.text.DecimalFormat("#,###." + decimal_digit);

		String result = fmt.format(Double.parseDouble(value));

		return result;
	}

	/*********************************************************
	 * �Էµ� �ݾ��� ���������Ų �� õ���� �޸� �� �� ����.
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