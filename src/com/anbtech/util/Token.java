package com.anbtech.util;

import java.util.*;

public class Token{
	// 날짜를 구한다
	public static ArrayList getTokenList(String str){
		StringTokenizer str_st = new StringTokenizer(str, "|");
		ArrayList str_list = new ArrayList(); 
		while(str_st.hasMoreElements()){
			str_list.add(str_st.nextToken());
		}
		return str_list;
	}
}