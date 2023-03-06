package com.anbtech.text;

import java.lang.SecurityException;
import java.io.UnsupportedEncodingException;

//-----------------------------------------------------------------------
// �ѱ� ó��
//-----------------------------------------------------------------------
public class Hanguel {

    public Hanguel() {}	
    public static String toHanguel(String s) {
		try {
		   if (s != null){
				s = s.replace('\'','`');
				return (new String(s.getBytes("8859_1"),"EUC_KR"));
		   }
		   return s;
		} catch (UnsupportedEncodingException e) {
		   return "Encoding Error";
		}
    }

}