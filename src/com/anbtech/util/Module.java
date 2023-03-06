package com.anbtech.util;

import java.net.*;
import javax.servlet.http.*;

public class Module{

	public static String getMIME(String filename, String type){
		if (type.endsWith("jpeg") || filename.endsWith("jpeg"))
			return "jpeg";
		else if (type.endsWith("jpg") || filename.endsWith("jpg"))
			return "jpg";
		else if (type.endsWith("gif") || filename.endsWith("gif"))
			return "gif";
		else if (type.endsWith("avi") || filename.endsWith("avi"))
			return "avi";
		else if (type.endsWith("mp3") || filename.endsWith("mp3"))
			return "mp3";
		else if (type.endsWith("mp2") || filename.endsWith("mp2"))
			return "mp2";
		else if (type.endsWith("exe") || filename.endsWith("exe"))
			return "exe";
		else if (type.endsWith("aif") || filename.endsWith("aif"))
			return "aif";
		else if (type.endsWith("aifc") || filename.endsWith("aifc"))
			return "aifc";
		else if (type.endsWith("aiff") || filename.endsWith("aiff"))
			return "aiff";
		else if (type.endsWith("arj") || filename.endsWith("arj"))
			return "arj";
		else if (type.endsWith("au") || filename.endsWith("au"))
			return "au";
		else if (type.endsWith("bat") || filename.endsWith("bat"))
			return "bat";
		else if (type.endsWith("bmp") || filename.endsWith("bmp"))
			return "bmp";
		else if (type.endsWith("cdf") || filename.endsWith("cdf"))
			return "cdf";
		else if (type.endsWith("cgi") || filename.endsWith("cgi"))
			return "cgi";
		else if (type.endsWith("com") || filename.endsWith("com"))
			return "exe";
		else if (type.endsWith("compressed") || filename.endsWith("compressed"))
			return "compressed";
		else if (type.endsWith("css") || filename.endsWith("css"))
			return "css";
		else if (type.endsWith("default") || filename.endsWith("default"))
			return "default";
		else if (type.endsWith("device") || filename.endsWith("device"))
			return "device";
		else if (type.endsWith("dif") || filename.endsWith("dif"))
			return "dif";
		else if (type.endsWith("dll") || filename.endsWith("dll"))
			return "dll";
		else if (type.endsWith("doc") || filename.endsWith("doc"))
			return "doc";
		else if (type.endsWith("dv") || filename.endsWith("dv"))
			return "dv";
		else if (type.endsWith("eml") || filename.endsWith("eml"))
			return "eml";
		else if (type.endsWith("gz") || filename.endsWith("gz"))
			return "gz";
		else if (type.endsWith("htm") || filename.endsWith("htm"))
			return "htm";
		else if (type.endsWith("html") || filename.endsWith("html"))
			return "html";
		else if (type.endsWith("hwp") || filename.endsWith("hwp"))
			return "hwp";
		else if (type.endsWith("iff") || filename.endsWith("iff"))
			return "iff";
		else if (type.endsWith("image") || filename.endsWith("image"))
			return "image";
		else if (type.endsWith("jfif") || filename.endsWith("jfif"))
			return "jfif";
		else if (type.endsWith("jpeg") || filename.endsWith("jpeg"))
			return "jpeg";
		else if (type.endsWith("js") || filename.endsWith("js"))
			return "js";
		else if (type.endsWith("lhz") || filename.endsWith("lhz"))
			return "lhz";
		else if (type.endsWith("lzh") || filename.endsWith("lzh"))
			return "lzh";
		else if (type.endsWith("mac") || filename.endsWith("mac"))
			return "mac";
		else if (type.endsWith("midi") || filename.endsWith("midi"))
			return "midi";
		else if (type.endsWith("mov") || filename.endsWith("mov"))
			return "mov";
		else if (type.endsWith("movie") || filename.endsWith("movie"))
			return "movie";
		else if (type.endsWith("nws") || filename.endsWith("nws"))
			return "nws";
		else if (type.endsWith("pcx") || filename.endsWith("pcx"))
			return "pcx";
		else if (type.endsWith("png") || filename.endsWith("png"))
			return "png";
		else if (type.endsWith("ppt") || filename.endsWith("ppt"))
			return "ppt";
		else if (type.endsWith("ps") || filename.endsWith("ps"))
			return "ps";
		else if (type.endsWith("psd") || filename.endsWith("psd"))
			return "psd";
		else if (type.endsWith("qif") || filename.endsWith("qif"))
			return "qif";
		else if (type.endsWith("qt") || filename.endsWith("qt"))
			return "qt";
		else if (type.endsWith("qti") || filename.endsWith("qti"))
			return "qti";
		else if (type.endsWith("qtif") || filename.endsWith("qtif"))
			return "qtif";
		else if (type.endsWith("ra") || filename.endsWith("ra"))
			return "ra";
		else if (type.endsWith("ram") || filename.endsWith("ram"))
			return "ram";
		else if (type.endsWith("rar") || filename.endsWith("rar"))
			return "rar";
		else if (type.endsWith("rle") || filename.endsWith("rle"))
			return "rle";
		else if (type.endsWith("rv") || filename.endsWith("rv"))
			return "rv";
		else if (type.endsWith("sound") || filename.endsWith("sound"))
			return "sound";
		else if (type.endsWith("spl") || filename.endsWith("spl"))
			return "spl";
		else if (type.endsWith("swf") || filename.endsWith("swf"))
			return "swf";
		else if (type.endsWith("sys") || filename.endsWith("sys"))
			return "sys";
		else if (type.endsWith("tar") || filename.endsWith("tar"))
			return "tar";
		else if (type.endsWith("text") || filename.endsWith("text"))
			return "text";
		else if (type.endsWith("tga") || filename.endsWith("tga"))
			return "tga";
		else if (type.endsWith("tgz") || filename.endsWith("tgz"))
			return "tgz";
		else if (type.endsWith("tif") || filename.endsWith("tif"))
			return "tif";
		else if (type.endsWith("tiff") || filename.endsWith("tiff"))
			return "tiff";
		else if (type.endsWith("txt") || filename.endsWith("txt"))
			return "txt";
		else if (type.endsWith("wav") || filename.endsWith("wav"))
			return "wav";
		else if (type.endsWith("wmf") || filename.endsWith("wmf"))
			return "wmf";
		else if (type.endsWith("xls") || filename.endsWith("xls"))
			return "xls";
		else if (type.endsWith("z") || filename.endsWith("z"))
			return "z";
		else if (type.endsWith("zip") || filename.endsWith("zip"))
			return "zip";
		else 
			return "unknown";
		
	}

	//쿠키를 저장하거나 가져오는 클래스이다.. 쿠키가 깨지면 변형시킬것	
	public static void setCookie(HttpServletResponse response, String writer, String email, String homepage){
		Cookie c_writer = new Cookie("anbtech.com_writer",URLEncoder.encode(writer));
		Cookie c_email = new Cookie("anbtech.com_email",URLEncoder.encode(email));
		Cookie c_homepage = new Cookie("anbtech.com_homepage",URLEncoder.encode(homepage));
		
		int cookie_age = 999999999;
		c_writer.setMaxAge(cookie_age);
		c_email.setMaxAge(cookie_age);
		c_homepage.setMaxAge(cookie_age);
		c_writer.setPath("/");
		c_email.setPath("/");
		c_homepage.setPath("/");
		response.addCookie(c_writer);
		response.addCookie(c_email);
		response.addCookie(c_homepage);
	}
	public static void setCookie(HttpServletResponse response, String writer){//코맨트추가시에 writer만 쿠키저장
		Cookie c_writer = new Cookie("anbtech.com_writer",URLEncoder.encode(writer));
		
		int cookie_age = 999999999;
		c_writer.setMaxAge(cookie_age);
		c_writer.setPath("/");
		response.addCookie(c_writer);
	}
	
	public static String[] getCookie(HttpServletRequest request){
		String str[] = new String[3];

		try {
			Cookie c[] = request.getCookies();
			for (int i=0; i<c.length; i++){
				if ("anbtech.com_writer".equals(c[i].getName()))
					str[0] = URLDecoder.decode(c[i].getValue());
				if ("anbtech.com_email".equals(c[i].getName()))
					str[1] = URLDecoder.decode(c[i].getValue());
				if ("anbtech.com_homepage".equals(c[i].getName()))
					str[2] = URLDecoder.decode(c[i].getValue());
			}
		}catch (Exception e){}
		return str;
	}
}