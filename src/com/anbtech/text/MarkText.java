package com.anbtech.text;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkText {

	public static void main(String[] args) 
	{
		String word = "property";
		String str = " use property attribute in the jsp:setProperty tag ";
		System.out.print(markKeyword(str, word));
	}

	public static String markKeyword(String str, String keyword) {
		keyword =
			replace(
				replace(replace(keyword, "[", "\\["), ")", "\\)"),
				"(", "\\(");

		Pattern p = Pattern.compile( keyword , Pattern.CASE_INSENSITIVE );
		Matcher m = p.matcher( str );
		int start = 0;
		int lastEnd = 0;
		StringBuffer sbuf = new StringBuffer();
		while( m.find() ) {
			start = m.start();
			sbuf.append( str.substring(lastEnd, start) )
				.append("<font color='red'>"+m.group()+"</font>" );
			lastEnd = m.end();
		} 
		return sbuf.append(str.substring(lastEnd)).toString() ;
	}

	public static String replace(String str, String oldStr, String newStr) {

		if (str == null) {
			return null;
		}
		if (oldStr == null || newStr == null || oldStr.length() == 0) {
			return str;
		}

		int i = str.lastIndexOf(oldStr);
		if (i < 0)
			return str;

		StringBuffer sbuf = new StringBuffer(str);

		while (i >= 0) {
			sbuf.replace(i, (i + oldStr.length()), newStr);
			i = str.lastIndexOf(oldStr, i - 1);
		}
		return sbuf.toString();
	}

}
