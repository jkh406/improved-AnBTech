package com.anbtech.util;

import java.util.*;

public class Token{
	// ��¥�� ���Ѵ�
	public static ArrayList getTokenList(String str){
		StringTokenizer str_st = new StringTokenizer(str, "|");
		ArrayList str_list = new ArrayList(); 
		while(str_st.hasMoreElements()){
			str_list.add(str_st.nextToken());
		}
		return str_list;
	}
}