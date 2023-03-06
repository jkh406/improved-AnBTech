// Decompiled by Jad v1.5.7d. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http://www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ChatServlet extends HttpServlet
{

    public ChatServlet()
    {
    }

    /*******************************************
	 * 현재 로그인되어 있는 사용자 목록 출력
	 *******************************************/
	private void ShowUsersTable(String s, Hashtable hashtable, String s1, PrintWriter printwriter, String s2, String s3)
    {
        Vector vector = getUsers(s1, 7);
        StringBuffer stringbuffer = new StringBuffer();
        int i = 0;
        boolean flag = s2.equals("1");
        boolean flag1 = false;
        String s4 = "#FFFFFF";
        String s5 = "#CCCCCC";
        String s6 = s5;
        String s7 = (String)hashtable.get("admin");
        String s10 = "";
        String s11 = "<td nowrap>";
        String s12 = "</td>";
        byte byte0 = 2;
        s10 = "<font color=\"#000000\"";
        String s8;
        if((s8 = (String)hashtable.get("size")) != null)
            s10 = s10 + " size=\"" + s8 + "\"";
        if((s8 = (String)hashtable.get("face")) != null)
            s10 = s10 + " face=\"" + s8 + "\"";
        s10 = s10 + ">";
        if(s7 != null)
        {
            Hashtable hashtable1 = getUsersHash(s1);
            if(hashtable1 != null)
            {
                Vector vector1 = (Vector)hashtable1.get(s3);
                if(vector1 != null && s7.equals((String)vector1.elementAt(0)))
                    flag1 = true;
            }
        }
        if(flag1)
            byte0 = 3;
        s11 = s11 + s10;
        s12 = "</font></td>";
        stringbuffer.append("<head>\n");
        stringbuffer.append("<title>" + (String)hashtable.get("title") + ": users</title>\n");
        stringbuffer.append("</head>\n");
        stringbuffer.append("<body bgcolor=\"#FFFFFF\">\n");
        if(s10.length() > 0)
            stringbuffer.append(s10 + NEWLINE);
        stringbuffer.append("<h1>Current users:</h1>\n");
        stringbuffer.append("<table cols=" + (flag ? byte0 + 2 : byte0) + "border=1 width=98%>\n");
        stringbuffer.append("<tr bgcolor=\"#CCCCFF\">\n");
        stringbuffer.append(s11 + getLabel(hashtable, "User") + s12);
        stringbuffer.append(s11 + getLabel(hashtable, "Mail") + s12);
        if(flag)
        {
            stringbuffer.append(s11 + getLabel(hashtable, "Host") + s12);
            stringbuffer.append(s11 + getLabel(hashtable, "Address") + s12);
        }
        if(flag1)
            stringbuffer.append(s11 + "&nbsp;" + s12);
        stringbuffer.append("</tr>\n");
        for(; i <= vector.size() - 8; i += 8)
        {
            if(s6.equals(s4))
                s6 = s5;
            else
                s6 = s4;
            stringbuffer.append("<tr bgcolor=\"" + s6 + "\">");
            stringbuffer.append(s11 + (String)vector.elementAt(i + 1) + s12);
            String s9 = (String)vector.elementAt(i + 2);
            if(s9.length() == 0)
                s9 = "&nbsp;&nbsp;&nbsp;";
            else
                s9 = "<a href=\"mailto:" + s9 + "\">" + s9 + "</a>";
            stringbuffer.append(s11 + s9 + s12);
            if(flag)
            {
                stringbuffer.append(s11 + (String)vector.elementAt(i + 6) + s12);
                stringbuffer.append(s11 + (String)vector.elementAt(i + 5) + s12);
            }
            if(flag1)
                stringbuffer.append(s11 + "<a href=\"" + s + "?" + "actn" + "=" + "dlsr" + "&" + "conf" + "=" + s1 + "&" + "id" + "=" + s3 + "&" + "id1" + "=" + (String)vector.elementAt(0) + "&" + "privacy" + "=" + s2 + "\">Delete</a>" + s12);
            stringbuffer.append("</tr>\n");
        }

        stringbuffer.append("</table>\n");
        stringbuffer.append("<br><br>&copy;&nbsp;<a href=mailto:info@servletsuite.com>Coldbeans</a>&nbsp;&nbsp;ver. 1.50" + NEWLINE);
        if(s10.length() > 0)
            stringbuffer.append("</font>\n");
        stringbuffer.append("</body>\n");
        printwriter.println(stringbuffer.toString());
        stringbuffer = null;
        vector = null;
    }
    
    /*******************************************
	 * 
	 *******************************************/	
	private String acceptWml(String s, String s1, String s2, HttpServletRequest httpservletrequest)
    {
        Hashtable hashtable = getChatHash(s1);
        String s3 = getUserName(s1, s2);
        if(s3.length() == 0)
            return initWml(s, s1);
        String s4;
        if((s4 = httpservletrequest.getParameter("name")) == null)
            s4 = "";
        else
            s4 = prepareMsg(decodeString(s4, httpservletrequest, (String)hashtable.get("encoding")));
        if(s4.length() > 0)
            addMessage(hashtable, s1, s2, "0", s4);
        return mainWml(s, s1, s2, hashtable);
    }

    /*******************************************
	 * 
	 *******************************************/	
	private void addMessage(Hashtable hashtable, String s, String s1, String s2, String s3)
    {
        Vector vector = getMsgsVector(s);
        Date date = getCurrentDate(hashtable);
        String s4 = date.toString();
        String s5 = "";
        int i = s4.indexOf(":");
        String as[] = {
            "", "", "#000000"
        };
        if(s2 == null || s1 == null || s3 == null)
            return;
        if(!s1.equals("0"))
            getUserInfo(s, s1, as);
        if(as[0].length() == 0)
            return;
        Vector vector1 = new Vector();
        vector1.addElement(date);
        vector1.addElement(s1);
        vector1.addElement(s2);
        if(!as[1].equals(""))
            as[0] = "<a href=\"mailto:" + as[1] + "\">" + as[0] + "</a>";
        String s6 = "<font color=\"" + as[2] + "\"";
        s6 = s6 + ">";
        vector1.addElement(s6 + s4.substring(i - 2, i) + ":" + s4.substring(i + 1, i + 3) + (s2.equals("0") ? "&nbsp;" : "*") + "&nbsp;" + as[0] + "&nbsp;:&nbsp;" + s3 + "</font>");
        vector1.addElement(getId());
        synchronized(hashtable.get("messages"))
        {
            vector.insertElementAt(vector1, 0);
        }
    }

    /*******************************************
	 * 
	 *******************************************/	
	boolean badIP(Hashtable hashtable, String s)
    {
        String s1 = (String)hashtable.get("blacklist");
        if(s1 == null)
            return false;
        s1 = getBanner(s1);
        if(s1.length() == 0)
            return false;
        for(StringTokenizer stringtokenizer = new StringTokenizer(s1, " ,"); stringtokenizer.hasMoreTokens();)
        {
            String s2 = stringtokenizer.nextToken();
            int i;
            if((i = s2.indexOf("*")) >= 0)
            {
                if(compareAddr(s2, s))
                    return true;
            } else
            if(s2.equals(s))
                return true;
        }

        return false;
    }

    /*******************************************
	 * 
	 *******************************************/	
	private String checkFrames(String s)
    {
        int i = s.indexOf(",");
        boolean flag = false;
        boolean flag1 = false;
        if(i <= 0 || i == s.length() - 1)
            return "75%,25%";
        String s1 = s.substring(0, i).trim();
        String s2 = s.substring(i).trim();
        if(s1.length() == 0 || s2.length() == 0)
            return "75%,25%";
        if(s1.endsWith("%"))
        {
            if(s1.length() == 1)
                return "75%,25%";
            s1 = s.substring(0, s1.length() - 1);
            flag = true;
        }
        if(s2.endsWith("%"))
        {
            if(s2.length() == 1)
                return "75%,25%";
            s2 = s.substring(0, s1.length() - 1);
            flag1 = true;
        }
        int j = getInteger(s1);
        int k = getInteger(s2);
        if(j <= 0 || k <= 0)
            return "75%,25%";
        if(flag && j >= 100)
            return "75%,25%";
        if(flag1 && k >= 100)
            return "75%,25%";
        if(flag && !flag1)
            return "75%,25%";
        if(!flag && flag1)
            return "75%,25%";
        if(!flag && !flag1)
            return "75%,25%";
        if(flag && flag1 && j + k != 100)
            return s1 + "%," + (100 - j) + "%";
        else
            return s;
    }

    /*******************************************
	 * 
	 *******************************************/	
	private void clearMessages(Vector vector, long l, Hashtable hashtable)
    {
        Object obj = null;
        PrintWriter printwriter = null;
        String s = (String)hashtable.get("log");
        String s6 = null;
        if(s != null)
        {
            Calendar calendar = Calendar.getInstance();
            String s1 = String.valueOf(calendar.get(5));
            if(s1.length() == 1)
                s1 = "0" + s1;
            String s4 = String.valueOf(calendar.get(2) + 1);
            if(s4.length() == 1)
                s4 = "0" + s4;
            s6 = s + s1 + s4 + calendar.get(1) + ".htm";
        }
        String s2 = "";
        String s5 = "";
        synchronized(hashtable.get("title"))
        {
            if(s6 != null)
                try
                {
                    FileWriter filewriter = new FileWriter(s6, true);
                    printwriter = new PrintWriter(filewriter);
                }
                catch(Exception _ex)
                {
                    Object obj1 = null;
                    printwriter = null;
                }
            for(; (long)vector.size() > l; vector.removeElementAt(vector.size() - 1))
                if(printwriter != null)
                {
                    Vector vector1 = (Vector)vector.elementAt(vector.size() - 1);
                    String s3 = (String)vector1.elementAt(3);
                    s5 = s5 + "<br>" + s3 + NEWLINE;
                }

            if(printwriter != null)
                try
                {
                    printwriter.println(s5);
                    printwriter.flush();
                    printwriter.close();
                }
                catch(Exception _ex) { }
        }
    }

    /*******************************************
	 * 
	 *******************************************/	
	private boolean compareAddr(String s, String s1)
    {
        StringTokenizer stringtokenizer = new StringTokenizer(s, ".");
        StringTokenizer stringtokenizer1 = new StringTokenizer(s1, ".");
        if(stringtokenizer.countTokens() != stringtokenizer1.countTokens())
            return false;
        while(stringtokenizer.hasMoreTokens()) 
        {
            String s2 = stringtokenizer.nextToken();
            String s3 = stringtokenizer1.nextToken();
            if(!compareTokens(s2, s3))
                return false;
        }
        return true;
    }

    /*******************************************
	 * 
	 *******************************************/	
	private boolean compareTokens(String s, String s1)
    {
        if(s.equals("*"))
            return true;
        if(s1.equals("*"))
            return true;
        else
            return s.equals(s1);
    }

    /*******************************************
	 * 
	 *******************************************/	
	private String decodeString(String s, HttpServletRequest httpservletrequest, String s1)
    {
        if(s1 == null)
            return s;
        String s2;
        try
        {
            String s3 = httpservletrequest.getCharacterEncoding();
            s2 = new String(s.getBytes(s3 != null ? s3 : "ISO-8859-1"), s1);
        }
        catch(Exception _ex)
        {
            s2 = s;
        }
        return s2;
    }

    /*******************************************
	 * 
	 *******************************************/	
	private void deleteUser(String s, String s1)
    {
        Hashtable hashtable = getUsersHash(s);
        synchronized(hashtable)
        {
            if(hashtable != null)
                hashtable.remove(s1);
        }
    }

    /********************************************************************
	 * doGet() 메소드 구현
	 ********************************************************************/	
	public void doGet(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws ServletException, IOException
    {
        String s = "";	// 서블릿 URL. http://192.168.1.103/anb/servelt/ChatServlet
        String s1 = ""; // 요청의 원시 질의 문자열. action=login&id=1234567&name=A030003&mail=subaksi@abc.com&color=red
        String s2 = "";	// action(mode). null일 경우에는 init로 세팅된다.
        String s3 = ""; // 환경파일명. chatconf.txt
		String s22 = httpservletrequest.getRemoteAddr();// 클라이언트의 IP주소
		String s23 = ""; // 로긴ID
        s = HttpUtils.getRequestURL(httpservletrequest).toString();
        int i;
        if((i = s.indexOf("?")) > 0) s = s.substring(0, i); 
        if((s1 = httpservletrequest.getQueryString()) == null) s1 = "";

		//클라인언트의 무선단말기일 경우에 wmlChat()로 분기
		if(httpservletrequest.getHeader("Accept").indexOf("wap.wml") >= 0)
        {
            wmlChat(s, s1, httpservletrequest, httpservletresponse);
            return;
        }
        httpservletresponse.setContentType("text/html");

		PrintWriter printwriter = httpservletresponse.getWriter();
		printwriter.println("<html>");
		if(s1.length() == 0) //파라미터가 넘어오지 않았을 경우
        {
            printwriter.println("<br>Could not read configuration file");
            printwriter.println("</html>");
            printwriter.flush();
            printwriter.close();
            return;
        }

        s3 = getFromQuery(s1, "conf=");	// 환경파일 가져오기
        if(s3.length() == 0) s3 = s1;

        s23 = getFromQuery(s1, "id=");	// 로긴아이디 가져오기
        if(s23.length() == 0) s23 = httpservletrequest.getParameter("id");

        s2 = getFromQuery(s1, "actn="); // 모드 가져오기
        if(s2.length() == 0)
            if((s2 = httpservletrequest.getParameter("actn")) == null)
                s2 = "init";
            else
            if(s2.length() == 0)
                s2 = "init";
        
		//무슨 모드인지 모르겠음.
		if(s2.equals("gtnf"))
        {
            printwriter.println(getInfoBean(s3));
            printwriter.println("</html>");
            printwriter.flush();
            printwriter.close();
            return;
        }

		//로그인
        if(httpservletrequest.getParameter("name") != null && httpservletrequest.getParameter("mail") != null && s2.equals("init"))
        {
            s2 = "login";
            s23 = getId();
            if(getChatHash(s3) == null)
            {
                Hashtable hashtable = new Hashtable();
                readConfig(s3, hashtable);
                registerChat(s3, hashtable);
            }
        }

		//로그아웃
        if(s2.equals("logout"))
        {
            Hashtable hashtable1 = getChatHash(s3);
            addMessage(hashtable1, s3, s23, "0", "<i>logout from chat ...</i>");
            deleteUser(s3, s23);
            if(((String)hashtable1.get("logout")).equals("1"))
                s2 = "init";
            else
                s2 = "init2";
        }
        
		//초기 모드
		//두개의 프레임을 만든다.
		if(s2.equals("init"))
        {
            Hashtable hashtable2 = new Hashtable();
            readConfig(s3, hashtable2);
            registerChat(s3, hashtable2);
            String s4;
            if((s4 = (String)hashtable2.get("init")) == null)
            {
                boolean flag = ((String)hashtable2.get("dhtml")).equals("1");
                boolean flag3 = ((String)hashtable2.get("view")).equals("1");
                String s24 = getId();
                printwriter.println("<head>");
                printwriter.println("<title>" + (String)hashtable2.get("title") + "</title>");
                printwriter.println("</head>");
                printwriter.print("<frameset border=" + (String)hashtable2.get("border") + " rows=\"" + (String)hashtable2.get("frames"));
                if(flag && flag3)
                    printwriter.print(",*");
                printwriter.println("\">");
                if(flag3)
                    printwriter.println("<frame name=\"up\" src=\"" + s + "?" + "conf" + "=" + s3 + "&" + "actn" + "=" + "get" + "&" + "id" + "=" + s24 + "\">");
                else
                    printwriter.println("<frame name=\"up\" src=\"" + s + "?" + "conf" + "=" + s3 + "&" + "actn" + "=" + "init1" + "\">");
                printwriter.println("<frame name=\"down\" src=\"" + s + "?" + "conf" + "=" + s3 + "&" + "actn" + "=" + "init2" + "\">");
                if(flag3 && flag)
                    printwriter.println("<frame name=\"last\" src=\"" + s + "?" + "conf" + "=" + s3 + "&" + "actn" + "=" + "init4" + "&" + "id" + "=" + s24 + "\">");
                printwriter.println("</frameset>");
            } else
            {
                printwriter.println(getBanner(s4));
            }
        } 

		//
        else if(s2.equals("init1"))
        {
            Hashtable hashtable3 = getChatHash(s3);
            String s19 = getFont(hashtable3);
            String s5 = (String)hashtable3.get("bgcolor1");
            printwriter.println("<body bgcolor=\"" + s5 + "\">");
            printwriter.println(s19);
            if((s5 = (String)hashtable3.get("logo")) == null)
            {
                printwriter.println("<br><br><br><br><br><center>ChatServlet&nbsp;&copy;&nbsp;<a href=mailto:info@servletsuite.com>Coldbeans</a>&nbsp;&nbsp;ver. 1.50");
                printwriter.println("<br><br><br><font size=+2><center>please login !</center></font>");
            } else
            {
                printwriter.println(getBanner(s5));
            }
            printwriter.println("</font>");
            printwriter.println("</body>");
        } 
		
		else if(s2.equals("init2"))
        {
            Hashtable hashtable4 = getChatHash(s3);
            String s20 = getFont(hashtable4);
            String s6 = (String)hashtable4.get("bgcolor2");
            printwriter.print("<body bgcolor=\"" + s6 + "\"");
            String s16;
            if((s16 = (String)hashtable4.get("link2")) != null)
                printwriter.print(" link=\"" + s16 + "\"");
            if((s16 = (String)hashtable4.get("vlink2")) != null)
                printwriter.print(" vlink=\"" + s16 + "\"");
            if((s16 = (String)hashtable4.get("alink2")) != null)
                printwriter.print(" alink=\"" + s16 + "\"");
            printwriter.println(">");
            printwriter.println(s20);
            printwriter.println("<br>");
            drawLoginScreen(hashtable4, printwriter, s, s3);
            printwriter.println("<br>&copy;&nbsp;<a href=mailto:ruking@daum.net>codelove</a>&nbsp;&nbsp;ver. 1.50");
            printwriter.println("<script>");
            printwriter.println("parent.frames[1].document.forms[0].name.focus();");
            printwriter.println("function checkForm() {");
            printwriter.println("if (document.forms[0].name.value == '') {");
            printwriter.println("alert(\"You must enter your name\");");
            printwriter.println("return false; }");
            printwriter.println("document.forms[0].submit();");
            printwriter.println("return true; } ");
            printwriter.println("</script>");
            printwriter.println("</font>");
            printwriter.println("</body>");
        } 
		
		else if(s2.equals("login"))
        {
            Hashtable hashtable5 = getChatHash(s3);
            if(s22 != null && badIP(hashtable5, s22))
            {
                printwriter.println("<br><br>Connection is disabled");
                printwriter.println("</html>");
                printwriter.flush();
                printwriter.close();
                return;
            }
            int i1;
            if((i1 = getConnectionsCount(hashtable5)) > 0)
            {
                Hashtable hashtable11 = getUsersHash(s3);
                if(hashtable11.size() >= i1)
                {
                    printwriter.println("<br><br>Too many users. Please, check out this room later");
                    printwriter.println("</html>");
                    printwriter.flush();
                    printwriter.close();
                    return;
                }
            }
            boolean flag1 = ((String)hashtable5.get("dhtml")).equals("1");
            String s7 = (String)hashtable5.get("border");
            printwriter.println("<head>");
            printwriter.println("<title>" + (String)hashtable5.get("title") + "</title>");
            printwriter.println("</head>");
            printwriter.print("<frameset border=" + s7 + " rows=\"" + (String)hashtable5.get("frames"));
            if(flag1)
                printwriter.print(",*,*");
            printwriter.println("\">");
            registerUser(s3, hashtable5, s23, httpservletrequest);
            s7 = (String)hashtable5.get("privacy");
            String s15;
            if(s7.equals("1"))
            {
                if((s15 = httpservletrequest.getRemoteHost()) == null)
                    addMessage(hashtable5, s3, s23, "0", "<i>login from " + httpservletrequest.getRemoteAddr() + "</i>");
                else
                    addMessage(hashtable5, s3, s23, "0", "<i>login from " + s15 + "(" + httpservletrequest.getRemoteAddr() + ")</i>");
            } else
            {
                addMessage(hashtable5, s3, s23, "0", "<i>just login ...</i>");
            }
            printwriter.println("<frame name=\"up\" src=\"" + s + "?" + "conf" + "=" + s3 + "&" + "actn" + "=" + "get" + "&" + "id" + "=" + s23 + "\">");
            printwriter.println("<frame name=\"down\" src=\"" + s + "?" + "conf" + "=" + s3 + "&" + "actn" + "=" + "init3" + "&" + "id" + "=" + s23 + "\">");
            if(flag1)
            {
                printwriter.println("<frame name=\"last\"  src=\"" + s + "?" + "conf" + "=" + s3 + "&" + "actn" + "=" + "init4" + "&" + "id" + "=" + s23 + "\">");
                printwriter.println("<frame name=\"last1\" src=\"" + s + "?" + "conf" + "=" + s3 + "&" + "actn" + "=" + "init6" + "\">");
            }
            printwriter.println("</frameset>");
        } 
		
		else if(s2.equals("init3") || s2.equals("update"))
        {
            Hashtable hashtable6 = getChatHash(s3);
            String s8 = (String)hashtable6.get("bgcolor2");
            String s13 = (String)hashtable6.get("privacy");
            printwriter.print("<body bgcolor=\"" + s8 + "\"");
            String s17;
            if((s17 = (String)hashtable6.get("link2")) != null)
                printwriter.print(" link=\"" + s17 + "\"");
            if((s17 = (String)hashtable6.get("vlink2")) != null)
                printwriter.print(" vlink=\"" + s17 + "\"");
            if((s17 = (String)hashtable6.get("alink2")) != null)
                printwriter.print(" alink=\"" + s17 + "\"");
            printwriter.println(">");
            printwriter.println("<style type=\"text/css\"> a { text-decoration:none } </style>");
            drawMsgScreen(hashtable6, printwriter, s, s3, s23, s13, (String)hashtable6.get("log"));
            printwriter.println("<script>");
            printwriter.println("parent.frames[1].document.forms[0].name.value='';");
            printwriter.println("parent.frames[1].document.forms[0].name.focus();");
            printwriter.println("</script>");
            printwriter.println("</body>");
        } 
		
		else if(s2.substring(0, "get".length()).compareTo("get") == 0)
        {
            Hashtable hashtable7 = getChatHash(s3);
            String s21 = getFont(hashtable7);
            boolean flag2 = ((String)hashtable7.get("dhtml")).equals("1");
            String s9 = (String)hashtable7.get("refresh");
            String s14;
            if(s2.equals("get1"))
                s14 = getUserName(s3, s23);
            else
                s14 = "this is a live user";
            printwriter.println("<head>");
            if(flag2 && s2.equals("get"))
            {
                printwriter.println("<script language=\"JavaScript\">");
                printwriter.println("function ff(s)");
                printwriter.println("{ s0=document.all.tags(\"p\").item(\"idchat2000\").innerHTML;");
                if("1".equals((String)hashtable7.get("direction")))
                    printwriter.println("  document.all.tags(\"p\").item(\"idchat2000\").innerHTML=s+s0; }");
                else
                    printwriter.println("  document.all.tags(\"p\").item(\"idchat2000\").innerHTML=s0+s; }");
                printwriter.println("</script>");
            } else
            if(s14.length() > 0 && !flag2)
                printwriter.println("<META HTTP-EQUIV=\"REFRESH\" CONTENT=\"" + s9 + ";URL=" + s + "?" + "conf" + "=" + s3 + "&" + "actn" + "=" + "get" + "&" + "id" + "=" + s23 + "\">");
            printwriter.println("</head>");
            printwriter.print("<body bgcolor=\"" + (String)hashtable7.get("bgcolor1") + "\"");
            String s18;
            if((s18 = (String)hashtable7.get("link1")) != null)
                printwriter.print(" link=\"" + s18 + "\"");
            if((s18 = (String)hashtable7.get("vlink1")) != null)
                printwriter.print(" vlink=\"" + s18 + "\"");
            if((s18 = (String)hashtable7.get("alink1")) != null)
                printwriter.print(" alink=\"" + s18 + "\"");
            if((s18 = (String)hashtable7.get("bgimage")) != null)
                printwriter.print(" background=\"" + s18 + "\"");
            printwriter.println(">");
            printwriter.println(s21);
            if(s2.equals("get1"))
            {
                if(s14.length() == 0)
                    if(flag2)
                    {
                        printwriter.println("<script language=\"JavaScript\">");
                        printwriter.println("parent.frames[1].document.writeln('<br><br><br><center><b>Your session has been expired</b></center>');");
                        printwriter.println("parent.frames[1].document.writeln('<br><center>Please <a href=\"" + s + "?" + dosPrepare(s3) + "\" target=\"" + (String)hashtable7.get("login") + "\">login again !</a></center>');");
                        printwriter.println("</script>");
                        printwriter.flush();
                        printwriter.close();
                        return;
                    } else
                    {
                        printwriter.println("<br><br><br><center><b>Your session has been expired</b></center>");
                        printwriter.println("<br><center>Please <a href=\"" + s + "?" + s3 + "\" target=\"" + (String)hashtable7.get("login") + "\">login again !</a></center>");
                        printwriter.println("</font></body></html>");
                        printwriter.println("</html>");
                        printwriter.flush();
                        printwriter.close();
                        return;
                    }
                printwriter.println("<script>");
                printwriter.println("parent.frames[1].document.forms[0].name.value='';");
                printwriter.println("parent.frames[1].document.forms[0].name.focus();");
                printwriter.println("</script>");
                String s10;
                if((s10 = httpservletrequest.getParameter("name")) == null)
                    s10 = "";
                else
                    s10 = prepareMsg(decodeString(s10, httpservletrequest, (String)hashtable7.get("encoding")));
                if(s10.length() > 0)
                    addMessage(hashtable7, s3, s23, httpservletrequest.getParameter("user"), s10);
            }
            if(flag2 && s2.equals("get"))
            {
                printwriter.println("<p id=\"idchat2000\">");
                printwriter.println("</p>");
            }
            if(!flag2)
            {
                String s11 = (String)hashtable7.get("messages");
                long l1 = Long.parseLong(s11);
                Vector vector = getMsgsVector(s3);
                synchronized(hashtable7.get("messages"))
                {
                    Vector vector2 = new Vector();
                    for(int j = 0; j < vector.size() && (long)j < l1; j++)
                    {
                        Vector vector1 = (Vector)vector.elementAt(j);
                        String s12 = (String)vector1.elementAt(2);
                        if(s12.equals("0") || s12.equals(s23) || s23.compareTo((String)vector1.elementAt(1)) == 0)
                            vector2.addElement("<br>" + (String)vector1.elementAt(3));
                        if(j > 0 && j % 7 == 0)
                            vector2.addElement("<br>Java server-side programming &copy;&nbsp;<a href=mailto:info@servletsuite.com>Coldbeans</a>&nbsp;ver. 1.50");
                    }

                    if(hashtable7.get("direction").equals("1"))
                    {
                        for(int k = 0; k < vector2.size(); k++)
                            printwriter.println((String)vector2.elementAt(k));

                    } else
                    {
                        for(int l = vector2.size() - 1; l >= 0; l--)
                            printwriter.println((String)vector2.elementAt(l));

                        printwriter.println("<br>&nbsp;<script language=\"JavaScript\"> scroll(0,31000);</script>");
                    }
                    vector2 = null;
                    if((long)vector.size() > l1 + 10L)
                        clearMessages(vector, l1, hashtable7);
                }
                printwriter.println("</font></body>");
            }
        } 
		
		else if(s2.equals("log"))
        {
            Hashtable hashtable8 = getChatHash(s3);
            printwriter.println(showLogFile(hashtable8));
        } 
		
		else if(s2.equals("list"))
        {
            Hashtable hashtable9 = getChatHash(s3);
            ShowUsersTable(s, hashtable9, s3, printwriter, getFromQuery(s1, "privacy="), s23);
        } 
		
		else if(s2.equals("dlsr"))
        {
            deleteUser(s3, getFromQuery(s1, "id1="));
            Hashtable hashtable10 = getChatHash(s3);
            ShowUsersTable(s, hashtable10, s3, printwriter, getFromQuery(s1, "privacy="), s23);
        } 
		
		else if(s2.equals("init4"))
            showDhtmlFrame(s, s3, s23, printwriter);
        
		else if(s2.equals("init5"))
            showDhtmlPush(s, getChatHash(s3), s3, s23, getFromQuery(s1, "id2="), printwriter);
        
		else if(s2.equals("init6"))
            showDhtmlEmpty(printwriter);
        
		printwriter.println("</html>");
        printwriter.flush();
        printwriter.close();
    }

    /********************************************************************
	 * doPost() 메소드 구현
	 * doGet() 메소드의 코드를 호출하도록 하여 doGet()에서 처리하도록 함.
	 ********************************************************************/	
	public void doPost(HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws ServletException, IOException
    {
        if(httpservletrequest.getContentLength() > 25600)
        {
            httpservletresponse.setContentType("text/html");
            ServletOutputStream servletoutputstream = httpservletresponse.getOutputStream();
            servletoutputstream.println("<html><head><title>Too big</title></head>");
            servletoutputstream.println("<body><h1>Error - content length &gt;25k not ");
            servletoutputstream.println("</h1></body></html>");
        } else
        {
            doGet(httpservletrequest, httpservletresponse);
        }
    }

    /*******************************************
	 * 
	 *******************************************/	
	private String dosPrepare(String s)
    {
        StringBuffer stringbuffer = new StringBuffer("");
        for(int i = 0; i < s.length(); i++)
        {
            char c;
            if((c = s.charAt(i)) == '\\')
                stringbuffer.append("\\\\");
            else
                stringbuffer.append(c);
        }

        return stringbuffer.toString();
    }

    /*******************************************
	 * 초기 하단의 로그인 페이지 생성
	 *******************************************/	
	private void drawLoginScreen(Hashtable hashtable, PrintWriter printwriter, String s, String s1)
    {
        String s2 = getFont(hashtable);
        String s3 = (String)hashtable.get("bgcolor1");
        printwriter.println("<form method=\"post\" action=\"" + s + "?" + "conf" + "=" + s1 + "\" target=\"" + (String)hashtable.get("login") + "\">");
        printwriter.println("<table border=0>");
        printwriter.println("<tr>");
        printwriter.print("<td nowrap>" + s2);
        printwriter.print("Name:&nbsp;<input type=\"TEXT\" name=\"name\" maxlength=\"" + (String)hashtable.get("maxlogin") + "\">");
        printwriter.println("</font></td>");
        printwriter.print("<td nowrap>" + s2);
        printwriter.print("E-mail:&nbsp;<input type=\"TEXT\" name=\"mail\">");
        printwriter.println("</font></td>");
        printwriter.print("<td nowrap>" + s2);
        printwriter.println("<select name=\"color\">");
        printwriter.println("<option style=\"background:" + s3 + ";color:#000000\" value=\"#000000\">black</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#F0F8FF\" value=\"#F0F8FF\">aliceblue</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FAEBD7\" value=\"#FAEBD7\">antiquewhite</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#00FFFF\" value=\"#00FFFF\">aqua</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#7FFFD4\" value=\"#7FFFD4\">aquamarine</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#F0FFFF\" value=\"#F0FFFF\">azure</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#F5F5DC\" value=\"#F5F5DC\">beige</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FFE4C4\" value=\"#FFE4C4\">bisque</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#0000FF\" value=\"#0000FF\">blue</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#8A2BE2\" value=\"#8A2BE2\">blueviolet</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#A52A2A\" value=\"#A52A2A\">brown</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#DEB887\" value=\"#DEB887\">burlywood</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#5F9EA0\" value=\"#5F9EA0\">cadetblue</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#7FFF00\" value=\"#7FFF00\">chartreuse</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#D2691E\" value=\"#D2691E\">chocolate</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FF7F50\" value=\"#FF7F50\">coral</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FFF8DC\" value=\"#FFF8DC\">cornsilk</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#DC143C\" value=\"#DC143C\">crimson</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#00FFFF\" value=\"#00FFFF\">cyan</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#00008B\" value=\"#00008B\">darkblue</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#008B8B\" value=\"#008B8B\">darkcyan</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#A9A9A9\" value=\"#A9A9A9\">darkgray</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#006400\" value=\"#006400\">darkgreen</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#BDB76B\" value=\"#BDB76B\">darkkhaki</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FF8C00\" value=\"#FF8C00\">darkorange</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#9932CC\" value=\"#9932CC\">darkorhid</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#8B0000\" value=\"#8B0000\">darkred</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#9400D3\" value=\"#9400D3\">darkviolet</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FF1493\" value=\"#FF1493\">deeppink</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#696969\" value=\"#696969\">dimgray</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#1E90FF\" value=\"#1E90FF\">dodgerblue</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#B22222\" value=\"#B22222\">firebrick</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FFFAF0\" value=\"#FFFAF0\">floralwhite</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#228B22\" value=\"#228B22\">forestgreen</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FF00FF\" value=\"#FF00FF\">fuchsia</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#DCDCDC\" value=\"#DCDCDC\">gainsboro</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#F8F8FF\" value=\"#F8F8FF\">ghostwhite</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FFD700\" value=\"#FFD700\">gold</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#DAA520\" value=\"#DAA520\">goldenrod</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#808080\" value=\"#808080\">gray</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#008000\" value=\"#008000\">green</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#ADFF2F\" value=\"#ADFF2F\">greenyellow</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#F0FFF0\" value=\"#F0FFF0\">honeydew</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FF69B4\" value=\"#FF69B4\">hotpink</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#CD5C5C\" value=\"#CD5C5C\">indianred</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#4B0082\" value=\"#4B0082\">indigo</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FFFFF0\" value=\"#FFFFF0\">ivory</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#F0E68C\" value=\"#F0E68C\">khaki</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#7CFC00\" value=\"#7CFC00\">lavngreen</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#ADD8E6\" value=\"#ADD8E6\">lightblue</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#F08080\" value=\"#F08080\">lightcoral</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#E0FFFF\" value=\"#E0FFFF\">lightcyan</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#90EE90\" value=\"#90EE90\">lightgreen</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#D3D3D3\" value=\"#D3D3D3\">lightgrey</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FFB6C1\" value=\"#FFB6C1\">lightpink</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#00FF00\" value=\"#00FF00\">lime</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#32CD32\" value=\"#32CD32\">limegreen</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FAF0E6\" value=\"#FAF0E6\">linen</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FF00FF\" value=\"#FF00FF\">magenta</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#800000\" value=\"#800000\">maroon</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#0000CD\" value=\"#0000CD\">mediumblue</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#F5FFFA\" value=\"#F5FFFA\">mintcream</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FFE4E1\" value=\"#FFE4E1\">mistyrose</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FFE4B5\" value=\"#FFE4B5\">moccasin</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#000080\" value=\"#000080\">navy</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FDF5E6\" value=\"#FDF5E6\">oldlace</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#808000\" value=\"#808000\">olive</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#6B8E23\" value=\"#6B8E23\">olivedrab</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FFA500\" value=\"#FFA500\">orange</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FF4500\" value=\"#FF4500\">orangered</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#DA70D6\" value=\"#DA70D6\">orchid</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#98FB98\" value=\"#98FB98\">palegreen</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FFDAB9\" value=\"#FFDAB9\">peachpuff</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#CD853F\" value=\"#CD853F\">peru</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FFC0CB\" value=\"#FFC0CB\">pink</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#DDA0DD\" value=\"#DDA0DD\">plum</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#B0E0E6\" value=\"#B0E0E6\">powderblue</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#800080\" value=\"#800080\">purple</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FF0000\" value=\"#FF0000\">red</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#BC8F8F\" value=\"#BC8F8F\">rosybrown</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#4169E1\" value=\"#4169E1\">royalblue</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FA8072\" value=\"#FA8072\">salmon</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#2E8B57\" value=\"#2E8B57\">seagreen</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FFF5EE\" value=\"#FFF5EE\">seashell</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#A0522D\" value=\"#A0522D\">sienna</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#C0C0C0\" value=\"#C0C0C0\">silver</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#87CEEB\" value=\"#87CEEB\">skyblue</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#6A5ACD\" value=\"#6A5ACD\">slateblue</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#708090\" value=\"#708090\">slategray</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FFFAFA\" value=\"#FFFAFA\">snow</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#4682B4\" value=\"#4682B4\">steelblue</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#D2B48C\" value=\"#D2B48C\">tan</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#008080\" value=\"#008080\">teal</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#D8BFD8\" value=\"#D8BFD8\">thistle</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FF6347\" value=\"#FF6347\">tomato</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#40E0D0\" value=\"#40E0D0\">turquoise</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#EE82EE\" value=\"#EE82EE\">violet</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#F5DEB3\" value=\"#F5DEB3\">wheat</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FFFFFF\" value=\"#FFFFFF\">white</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#F5F5F5\" value=\"#F5F5F5\">whitesmoke</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#FFFF00\" value=\"#FFFF00\">yellow</option>");
        printwriter.println("<option style=\"background:" + s3 + ";color:#9ACD32\" value=\"#9ACD32\">yellowgreen</option>");
        printwriter.println("</select>");
        printwriter.println("</font></td>");
        printwriter.print("<td nowrap>" + s2);
        printwriter.print("<input type=\"Button\" value=\"Login\" onClick=\"checkForm();\" title=\"Join to chat\">");
        printwriter.println("</font></td></tr>");
        printwriter.println("<input type=\"hidden\" name=\"actn\" value=\"login\">");
        printwriter.println("<input type=\"hidden\" name=\"id\" value=\"" + getId() + "\">");
        printwriter.println("</table>");
        printwriter.println("</form>");
    }

    /*******************************************
	 * 
	 *******************************************/	
	private void drawMsgScreen(Hashtable hashtable, PrintWriter printwriter, String s, String s1, String s2, String s3, String s4)
    {
        String s5 = "";
        String s6 = "";
        String s7 = getUserName(s1, s2);
        Object obj = null;
        int i = 0;
        boolean flag = false;
        boolean flag1 = ((String)hashtable.get("dhtml")).equals("1");
        s6 = (String)hashtable.get("logout");
        if(s6.equals("1"))
            s6 = (String)hashtable.get("login");
        else
            s6 = "_self";
        s5 = getFont(hashtable);
        printwriter.println(s5);
        printwriter.println("<table witdh=100% border=0>");
        printwriter.print("<form method=\"post\" action=\"" + s + "?" + "conf" + "=" + s1 + "\"");
        if(flag1)
            printwriter.println(" target=last1>");
        else
            printwriter.println(" target=up>");
        printwriter.println("<tr><td nowrap align=left>" + s5);
        printwriter.println("&nbsp;" + s7 + ":&nbsp;");
        printwriter.println("</font></td><td nowrap align=right>" + s5);
        printwriter.println("<input type=\"TEXT\" name=\"name\" size=\"" + (String)hashtable.get("input") + "\" maxlength=\"" + (String)hashtable.get("maxlength") + "\">");
        printwriter.println("</font></td><td nowrap align=right>" + s5);
        printwriter.println(getLabel(hashtable, "To") + ":&nbsp;<select name=\"user\">");
        String s8;
        if((s8 = (String)hashtable.get("master")) != null)
        {
            if(s7.equals(s8))
            {
                printwriter.println("<option value=\"0\">ALL</option>");
                flag = true;
            }
            for(Vector vector = getUsers(s1, 1); i <= vector.size() - 2; i += 2)
                if(s7.equals(s8) || s7.equals((String)vector.elementAt(i + 1)) || s8.equals((String)vector.elementAt(i + 1)))
                {
                    printwriter.println("<option value=\"" + (String)vector.elementAt(i) + "\">" + (String)vector.elementAt(i + 1) + "</option>");
                    flag = true;
                }

        } else
        {
            printwriter.println("<option value=\"0\">" + getLabel(hashtable, "All") + "</option>");
            flag = true;
            for(Vector vector1 = getUsers(s1, 1); i <= vector1.size() - 2; i += 2)
                printwriter.println("<option value=\"" + (String)vector1.elementAt(i) + "\">" + (String)vector1.elementAt(i + 1) + "</option>");

            Object obj1 = null;
        }
        printwriter.println("</select>");
        printwriter.println("</font></td></tr>");
        printwriter.println("<tr><td nowrap align=left>" + s5);
        printwriter.print("<input type=\"");
        if(!flag)
            printwriter.print("button");
        else
            printwriter.print("submit");
        printwriter.print("\" value=\"" + getLabel(hashtable, "Send") + "\" title=\"Send message\"");
        if(!flag)
            printwriter.print(" DISABLED");
        printwriter.println(">");
        printwriter.println("<input type=\"hidden\" name=\"actn\" value=\"get1\">");
        printwriter.println("<input type=\"hidden\" name=\"id\" value=\"" + s2 + "\">");
        printwriter.println("</font></td></form>");
        printwriter.print("<td align=right nowrap nosave>");
        if(s5.length() > 0)
            printwriter.println(s5);
        else
            printwriter.println("<font size=-1>");
        printwriter.println("[&nbsp;<a href=\"" + s + "?" + "conf" + "=" + s1 + "&" + "actn" + "=" + "logout" + "&" + "id" + "=" + s2 + "\" target=" + s6 + " title=\"Logout from chat\">" + getLabel(hashtable, "Logout") + "</a>&nbsp;");
        printwriter.println("<a href=\"" + s + "?" + "conf" + "=" + s1 + "&" + "actn" + "=" + "update" + "&" + "id" + "=" + s2 + "\" target=down title=\"Update list of users\">" + getLabel(hashtable, "Refresh") + "</a>&nbsp;");
        if(s4 != null)
            printwriter.println("<a href=\"" + s + "?" + "conf" + "=" + s1 + "&" + "actn" + "=" + "log" + "&" + "id" + "=" + s2 + "\" target=_blank title=\"Show log file\">" + getLabel(hashtable, "Log") + "</a>&nbsp;");
        printwriter.println("<a href=\"" + s + "?" + "conf" + "=" + s1 + "&" + "actn" + "=" + "list" + "&" + "id" + "=" + s2 + "&" + "privacy" + "=" + s3 + "\" target=_blank title=\"Show connected users\">" + getLabel(hashtable, "Users") + "</a>&nbsp;");
        printwriter.print("]</font></td>");
        printwriter.print("<td align=right nowrap>");
        if(s5.length() > 0)
            printwriter.print(s5);
        else
            printwriter.print("<font size=-1>");
        printwriter.println("&nbsp;&copy;&nbsp;<a href=mailto:info@servletsuite.com>codelove.co.kr</a>&nbsp;&nbsp;ver. 1.50</font></td>");
        printwriter.println("</tr></table></font>");
        if(!flag)
        {
            printwriter.println("<script language=\"JavaScript\">");
            printwriter.println("document.forms[0].action.value='';");
            printwriter.println("</script>");
        }
    }

    /*******************************************
	 * 
	 *******************************************/	
	private String getBanner(String s)
    {
        StringBuffer stringbuffer = new StringBuffer("");
        try
        {
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(s)));
            String s1;
            while((s1 = bufferedreader.readLine()) != null) 
            {
                stringbuffer.append(s1);
                stringbuffer.append(NEWLINE);
            }
            bufferedreader.close();
        }
        catch(Exception _ex) { }
        return stringbuffer.toString();
    }

    /*******************************************
	 * 
	 *******************************************/	
	private Hashtable getChatHash(String s)
    {
        Hashtable hashtable = null;
        Vector vector = null;
        synchronized(forLock1)
        {
            vector = (Vector)cfgs.get(s);
        }
        if(vector != null)
            hashtable = (Hashtable)vector.elementAt(0);
        if(hashtable == null)
        {
            hashtable = new Hashtable();
            readConfig(s, hashtable);
            registerChat(s, hashtable);
        }
        return hashtable;
    }

    /*******************************************
	 * 
	 *******************************************/	
	private int getConnectionsCount(Hashtable hashtable)
    {
        String s = (String)hashtable.get("connections");
        if(s == null)
            return -1;
        try
        {
            return Integer.parseInt(s);
        }
        catch(Exception _ex)
        {
            return -1;
        }
    }

    /*******************************************
	 * 
	 *******************************************/	
	private Date getCurrentDate(Hashtable hashtable)
    {
        int i = Integer.parseInt((String)hashtable.get("offset"));
        if(i == 0)
        {
            return new Date();
        } else
        {
            Calendar calendar = Calendar.getInstance();
            calendar.add(10, i);
            return calendar.getTime();
        }
    }

    /*******************************************
	 * 
	 *******************************************/	
	private String getFont(Hashtable hashtable)
    {
        String s = (String)hashtable.get("size");
        String s1 = (String)hashtable.get("face");
        String s2 = "<font color=\"" + (String)hashtable.get("fgcolor") + "\"";
        if(s != null)
            s2 = s2 + " size=\"" + s + "\"";
        if(s1 != null)
            s2 = s2 + " face=\"" + s1 + "\"";
        s2 = s2 + ">";
        return s2;
    }

    /*******************************************
	 * 원시 질의문자열에서 원하는 파라미터값을 가져온다.
	 * s = 질의 문자열 전체
	 * 뽑고자 하는 파라미터 이름
	 *******************************************/	
	private String getFromQuery(String s, String s1)
    {
        if(s == null)
            return "";
        int i;
        if((i = s.indexOf(s1)) < 0)
            return "";
        String s2 = s.substring(i + s1.length());
        if((i = s2.indexOf("&")) < 0)
            return s2;
        else
            return s2.substring(0, i);
    }

    /*******************************************
	 * 로그인 사용자에게 부여할 랜덤한 ID 생성
	 *******************************************/	
	private String getId()
    {
        String s = "";
        synchronized(forLock)
        {
            long l = System.currentTimeMillis();
            Random random = new Random();
            s = String.valueOf(l);
            for(int i = 1; i <= 6; i++)
                s = s + (int)(1.0D + 6D * random.nextDouble());

        }
        return s;
    }

    /*******************************************
	 * 
	 *******************************************/	
	private String getInfoBean(String s)
    {
        StringBuffer stringbuffer = new StringBuffer("");
        int i = 0;
        if(getChatHash(s) == null)
            return "";
        for(Vector vector = getUsers(s, 2); i < vector.size(); i += 3)
        {
            stringbuffer.append("===");
            stringbuffer.append((String)vector.elementAt(i + 1));
            stringbuffer.append(NEWLINE);
            stringbuffer.append("===");
            stringbuffer.append((String)vector.elementAt(i + 2));
            stringbuffer.append(NEWLINE);
        }

        return stringbuffer.toString();
    }

    /*******************************************
	 * 
	 *******************************************/		
	private int getInteger(String s)
    {
        try
        {
            return Integer.parseInt(s);
        }
        catch(Exception _ex)
        {
            return -1;
        }
    }

    /*******************************************
	 * 
	 *******************************************/	
	private String getLabel(Hashtable hashtable, String s)
    {
        Hashtable hashtable1 = (Hashtable)hashtable.get("translation");
        return (String)hashtable1.get(s);
    }

    /*******************************************
	 * 
	 *******************************************/	
	private Vector getMsgsVector(String s)
    {
        Vector vector = null;
        Vector vector1 = null;
        synchronized(forLock1)
        {
            vector1 = (Vector)cfgs.get(s);
        }
        if(vector1 != null)
            vector = (Vector)vector1.elementAt(2);
        if(vector == null)
            vector = new Vector();
        return vector;
    }

    /*******************************************
	 * 
	 *******************************************/	
	public String getServletInfo()
    {
        return "A servlet that supports chatver. 1.50";
    }

    /*******************************************
	 * 
	 *******************************************/	
	private void getUserInfo(String s, String s1, String as[])
    {
        Hashtable hashtable = getUsersHash(s);
        Vector vector;
        synchronized(hashtable)
        {
            vector = (Vector)hashtable.get(s1);
            if(vector != null)
            {
                vector.removeElementAt(6);
                vector.addElement(new Date());
            }
        }
        if(vector != null)
        {
            as[0] = (String)vector.elementAt(0);
            as[1] = (String)vector.elementAt(1);
            as[2] = (String)vector.elementAt(2);
        } else
        {
            as[0] = "";
            as[1] = "";
            as[2] = "#000000";
        }
    }

    /*******************************************
	 * 
	 *******************************************/	
	private String getUserName(String s, String s1)
    {
        Hashtable hashtable = getUsersHash(s);
        Vector vector;
        synchronized(hashtable)
        {
            vector = (Vector)hashtable.get(s1);
        }
        if(vector != null)
            return (String)vector.elementAt(0);
        else
            return "";
    }

    /*******************************************
	 * 
	 *******************************************/	
	private Vector getUsers(String s, int i)
    {
        Hashtable hashtable = getUsersHash(s);
        Hashtable hashtable1 = getChatHash(s);
        Vector vector = new Vector();
        Date date = new Date();
        long l = date.getTime();
        long l1 = 600L;
        String s1 = (String)hashtable1.get("inactivity");
        l1 = Long.parseLong(s1);
        l1 *= 1000L;
        synchronized(hashtable)
        {
            for(Enumeration enumeration = hashtable.keys(); enumeration.hasMoreElements();)
            {
                String s2 = (String)enumeration.nextElement();
                Vector vector1 = (Vector)hashtable.get(s2);
                if(vector1 != null)
                {
                    Date date1 = (Date)vector1.elementAt(6);
                    if(l - date1.getTime() > l1)
                    {
                        hashtable.remove(s2);
                    } else
                    {
                        vector.addElement(s2);
                        for(int j = 0; j < i; j++)
                            vector.addElement(vector1.elementAt(j));

                    }
                }
            }

        }
        return vector;
    }

    /*******************************************
	 * 
	 *******************************************/	
	private Hashtable getUsersHash(String s)
    {
        Hashtable hashtable = null;
        Vector vector = null;
        synchronized(forLock1)
        {
            vector = (Vector)cfgs.get(s);
        }
        if(vector != null)
            hashtable = (Hashtable)vector.elementAt(1);
        if(hashtable == null)
            hashtable = new Hashtable();
        return hashtable;
    }

    /*******************************************
	 * 
	 *******************************************/	
	public void init(ServletConfig servletconfig)
        throws ServletException
    {
        super.init(servletconfig);
        NEWLINE = System.getProperty("line.separator");
        separator = System.getProperty("file.separator");
        cfgs = new Hashtable();
    }

    /*******************************************
	 * 
	 *******************************************/	
	private String initWml(String s, String s1)
    {
        Hashtable hashtable = new Hashtable();
        StringBuffer stringbuffer = new StringBuffer("");
        readConfig(s1, hashtable);
        registerChat(s1, hashtable);
        stringbuffer.append("<card id=\"user\" title=\"" + (String)hashtable.get("title") + "\">\n");
        stringbuffer.append("<do type=\"accept\" label=\"Login\">\n");
        stringbuffer.append("<go href=\"" + s + "?" + "conf" + "=" + s1 + "&amp;" + "fct" + "=" + getId() + "\" method=\"post\">\n");
        stringbuffer.append("<postfield name=\"name\" value=\"$(sUser)\"/>\n");
        stringbuffer.append("<postfield name=\"mail\" value=\"$(sEmail)\"/>\n");
        stringbuffer.append("<postfield name=\"actn\" value=\"login\"/>\n");
        stringbuffer.append("<postfield name=\"id\" value=\"" + getId() + "\"/>\n");
        stringbuffer.append("</go>\n");
        stringbuffer.append("</do>\n");
        stringbuffer.append("<p align=\"center\"><b>" + (String)hashtable.get("title") + "</b></p>\n");
        stringbuffer.append("<p>User: <input name=\"sUser\" emptyok=\"false\" /></p>\n");
        stringbuffer.append("<p>Email: <input name=\"sEmail\" emptyok=\"true\"/></p>\n");
        stringbuffer.append("</card>\n");
        return stringbuffer.toString();
    }

    /*******************************************
	 * 
	 *******************************************/	
    private String jsPrepare(String s)
    {
        StringBuffer stringbuffer = new StringBuffer("");
        for(int i = 0; i < s.length(); i++)
        {
            char c;
            if((c = s.charAt(i)) == '\'')
                stringbuffer.append("\\'");
            else
                stringbuffer.append(c);
        }

        return stringbuffer.toString();
    }

    /*******************************************
	 * 
	 *******************************************/	
    private String loginWml(String s, String s1, String s2, HttpServletRequest httpservletrequest)
    {
        Hashtable hashtable = getChatHash(s1);
        StringBuffer stringbuffer = new StringBuffer("");
        String s5 = httpservletrequest.getRemoteAddr();
        if(s5 != null && badIP(hashtable, s5))
        {
            stringbuffer.append("<card id=\"limit\" title=\"Disabled\">\n");
            stringbuffer.append("<do type=\"prev\">\n");
            stringbuffer.append("<prev/>\n");
            stringbuffer.append("</do>\n");
            stringbuffer.append("<p>Connection is disabled</p>\n");
            stringbuffer.append("</card>\n");
            return stringbuffer.toString();
        }
        int i;
        if((i = getConnectionsCount(hashtable)) > 0)
        {
            Hashtable hashtable1 = getUsersHash(s1);
            if(hashtable1.size() >= i)
            {
                stringbuffer.append("<card id=\"limit\" title=\"Connections limit\">\n");
                stringbuffer.append("<do type=\"prev\">\n");
                stringbuffer.append("<prev/>\n");
                stringbuffer.append("</do>\n");
                stringbuffer.append("<p>Too many users. Please, check out this room later</p>\n");
                stringbuffer.append("</card>\n");
                return stringbuffer.toString();
            }
        }
        registerUser(s1, hashtable, s2, httpservletrequest);
        String s3 = (String)hashtable.get("privacy");
        String s4;
        if(s3.equals("1"))
        {
            if((s4 = httpservletrequest.getRemoteHost()) == null)
                addMessage(hashtable, s1, s2, "0", "<i>login from " + httpservletrequest.getRemoteAddr() + "</i>");
            else
                addMessage(hashtable, s1, s2, "0", "<i>login from " + s4 + "(" + httpservletrequest.getRemoteAddr() + ")</i>");
        } else
        {
            addMessage(hashtable, s1, s2, "0", "<i>just login ...</i>");
        }
        stringbuffer.append(mainWml(s, s1, s2, hashtable));
        return stringbuffer.toString();
    }

    /*******************************************
	 * 
	 *******************************************/	
    private String logoutWml(String s, String s1, String s2)
    {
        Hashtable hashtable = getChatHash(s1);
        addMessage(hashtable, s1, s2, "0", "<i>logout from chat ...</i>");
        deleteUser(s1, s2);
        return initWml(s, s1);
    }

    /*******************************************
	 * 
	 *******************************************/	
    private String mainWml(String s, String s1, String s2, Hashtable hashtable)
    {
        StringBuffer stringbuffer = new StringBuffer("");
        stringbuffer.append("<card id=\"messages\" title=\"" + (String)hashtable.get("title") + "\" ontimer=\"" + s + "?" + "conf" + "=" + s1 + "&amp;" + "actn" + "=" + "get" + "&amp;" + "id" + "=" + s2 + "&amp;" + "fct" + "=" + getId() + "\">\n");
        stringbuffer.append("<timer value=\"" + 10 * Integer.parseInt((String)hashtable.get("refresh")) + "\"/>\n");
        stringbuffer.append("<do type=\"accept\" label=\"Refresh\">\n");
        stringbuffer.append("<go href=\"" + s + "?" + "conf" + "=" + s1 + "&amp;" + "actn" + "=" + "get" + "&amp;" + "id" + "=" + s2 + "\">\n");
        stringbuffer.append("</go>\n");
        stringbuffer.append("</do>\n");
        stringbuffer.append("<p><a href=\"" + s + "?" + "conf" + "=" + s1 + "&amp;" + "actn" + "=" + "init1" + "&amp;" + "id" + "=" + s2 + "\">Send</a></p>\n");
        Vector vector = getMsgsVector(s1);
        synchronized(hashtable.get("messages"))
        {
            for(int i = 0; i < vector.size() && stringbuffer.length() < 950; i++)
            {
                Vector vector1 = (Vector)vector.elementAt(i);
                String s3 = (String)vector1.elementAt(2);
                if(!s3.equals("0") && !s3.equals(s2) && s2.compareTo((String)vector1.elementAt(1)) != 0)
                    continue;
                String s4 = "<p>" + prepareWml((String)vector1.elementAt(3)) + "</p>";
                if(s4.length() + stringbuffer.length() > 950)
                    break;
                stringbuffer.append(replaceDollar(s4) + NEWLINE);
            }

        }
        stringbuffer.append("<p><a href=\"" + s + "?" + "conf" + "=" + s1 + "&amp;" + "actn" + "=" + "logout" + "&amp;" + "id" + "=" + s2 + "\">Logout</a></p>\n");
        stringbuffer.append("</card>\n");
        return stringbuffer.toString();
    }

    /*******************************************
	 * 
	 *******************************************/	
    private String prepareMsg(String s)
    {
        StringBuffer stringbuffer = new StringBuffer();
        if(s.length() == 0)
            return "";
        for(int i = 0; i < s.length(); i++)
        {
            char c = s.charAt(i);
            if(c == '>')
                stringbuffer.append("&gt;");
            else
            if(c == '<')
                stringbuffer.append("&lt;");
            else
            if(c == '&')
                stringbuffer.append("&amp;");
            else
            if(c == '"')
                stringbuffer.append("&quot;");
            else
                stringbuffer.append(c);
        }

        return stringbuffer.toString();
    }

    /*******************************************
	 * 
	 *******************************************/	
    private String prepareWml(String s)
    {
        String s1 = "";
        int i = s.indexOf("</font>");
        if(i < 0)
            s1 = s;
        else
            s1 = s.substring(0, i);
        if((i = s1.indexOf("<font")) >= 0)
        {
            s1 = s1.substring(i + 1);
            i = s1.indexOf(">");
            if(i >= 0)
                s1 = s1.substring(i + 1);
        }
        if((i = s1.indexOf("<a href=")) > 0)
        {
            s = s1.substring(0, i);
            i = s1.indexOf("\">");
            s1 = s + s1.substring(i + 2);
            i = s1.indexOf("</a>");
            s = s1.substring(0, i);
            s1 = s + s1.substring(i + 4);
        }
        return s1;
    }

    /*******************************************
	 * 
	 *******************************************/	
    private void readConfig(String s, Hashtable hashtable)
    {
        try
        {
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(s)));
            String s1;
            while((s1 = bufferedreader.readLine()) != null) 
            {
                s1 = s1.trim();
                if(s1.length() > 0)
                {
                    int i = s1.indexOf("=");
                    if(i > 0 && i < s1.length() - 1 && s1.charAt(0) != '#' && !s1.startsWith("//"))
                        hashtable.put(s1.substring(0, i).trim(), s1.substring(i + 1).trim());
                }
            }
            bufferedreader.close();
            File file = new File(s);
            hashtable.put("edited", String.valueOf(file.lastModified()));
        }
        catch(Exception _ex) { }
        if(hashtable.get("border") == null)
            hashtable.put("border", "1");
        if(hashtable.get("bgcolor1") == null)
            hashtable.put("bgcolor1", "#FFFFFF");
        if(hashtable.get("bgcolor2") == null)
            hashtable.put("bgcolor2", "#FFFFFF");
        if(hashtable.get("fgcolor") == null)
            hashtable.put("fgcolor", "#000000");
        if(hashtable.get("refresh") == null)
            hashtable.put("refresh", "20");
        if(hashtable.get("logout") == null)
            hashtable.put("logout", "1");
        if(hashtable.get("title") == null)
            hashtable.put("title", "codelove.co.kr chat");
        if(hashtable.get("login") == null)
            hashtable.put("login", "_top");
        if(hashtable.get("input") == null)
            hashtable.put("input", "60");
        if(hashtable.get("dhtml") == null)
            hashtable.put("dhtml", "0");
        if(hashtable.get("direction") == null)
            hashtable.put("direction", "1");
        if(hashtable.get("maxlength") == null)
            hashtable.put("maxlength", "255");
        String s2;
        if((s2 = (String)hashtable.get("translation")) == null)
        {
            hashtable.put("translation", new Hashtable());
        } else
        {
            Hashtable hashtable1 = new Hashtable();
            try
            {
                BufferedReader bufferedreader1 = new BufferedReader(new InputStreamReader(new FileInputStream(s2)));
                while((s2 = bufferedreader1.readLine()) != null) 
                {
                    s2 = s2.trim();
                    if(s2.length() > 0)
                    {
                        int j = s2.indexOf("=");
                        if(j > 0 && j < s2.length() - 1 && s2.charAt(0) != '#' && !s2.startsWith("//"))
                            hashtable1.put(s2.substring(0, j).trim(), s2.substring(j + 1).trim());
                    }
                }
                bufferedreader1.close();
            }
            catch(Exception _ex) { }
            hashtable.put("translation", hashtable1);
        }
        Hashtable hashtable2 = (Hashtable)hashtable.get("translation");
        if(hashtable2.get("User") == null)
            hashtable2.put("User", "User");
        if(hashtable2.get("Mail") == null)
            hashtable2.put("Mail", "Mail");
        if(hashtable2.get("Host") == null)
            hashtable2.put("Host", "Host");
        if(hashtable2.get("Address") == null)
            hashtable2.put("Address", "Address");
        if(hashtable2.get("To") == null)
            hashtable2.put("To", "To");
        if(hashtable2.get("Logout") == null)
            hashtable2.put("Logout", "LOGOUT");
        if(hashtable2.get("Refresh") == null)
            hashtable2.put("Refresh", "REFRESH");
        if(hashtable2.get("Log") == null)
            hashtable2.put("Log", "LOG");
        if(hashtable2.get("Users") == null)
            hashtable2.put("Users", "USERS");
        if(hashtable2.get("Send") == null)
            hashtable2.put("Send", "Send");
        if(hashtable2.get("All") == null)
            hashtable2.put("All", "ALL");
        if((s2 = (String)hashtable.get("maxlogin")) == null)
            hashtable.put("maxlogin", "20");
        else
        if(getInteger(s2) <= 0)
        {
            hashtable.remove("maxlogin");
            hashtable.put("maxlogin", "20");
        }
        if((s2 = (String)hashtable.get("frames")) == null)
        {
            hashtable.put("frames", "75%,25%");
        } else
        {
            hashtable.remove("frames");
            hashtable.put("frames", checkFrames(s2));
        }
        if((s2 = (String)hashtable.get("inactivity")) == null)
            hashtable.put("inactivity", "600");
        else
        if(getInteger(s2) <= 0)
        {
            hashtable.remove("inactivity");
            hashtable.put("inactivity", "600");
        }
        if(hashtable.get("privacy") == null)
            hashtable.put("privacy", "0");
        if(hashtable.get("view") == null)
            hashtable.put("view", "1");
        if((s2 = (String)hashtable.get("log")) != null)
        {
            File file1 = new File(s2);
            if(!file1.isDirectory() || !file1.canRead() || !file1.canWrite())
                hashtable.remove("log");
            else
            if(!s2.endsWith(separator))
            {
                hashtable.remove("log");
                hashtable.put("log", s2 + separator);
            }
        }
        if((s2 = (String)hashtable.get("offset")) == null)
        {
            hashtable.put("offset", "0");
        } else
        {
            if(s2.startsWith("+"))
                s2 = s2.substring(1);
            try
            {
                Integer.parseInt(s2);
                hashtable.remove("offset");
                hashtable.put("offset", s2);
            }
            catch(Exception _ex)
            {
                hashtable.remove("offset");
                hashtable.put("offset", "0");
            }
        }
        if((s2 = (String)hashtable.get("messages")) == null)
            hashtable.put("messages", "60");
        else
        if(getInteger(s2) <= 0)
        {
            hashtable.remove("messages");
            hashtable.put("messages", "60");
        }
    }

    /*******************************************
	 * 
	 *******************************************/	
    private String readWml(String s, String s1, String s2)
    {
        Hashtable hashtable = getChatHash(s1);
        String s3 = getUserName(s1, s2);
        if(s3.length() == 0)
            return initWml(s, s1);
        else
            return mainWml(s, s1, s2, hashtable);
    }

    /*******************************************
	 * 
	 *******************************************/	
    private void registerChat(String s, Hashtable hashtable)
    {
        synchronized(forLock1)
        {
            Vector vector = (Vector)cfgs.get(s);
            if(vector != null)
                return;
            vector = new Vector();
            vector.addElement(hashtable);
            vector.addElement(new Hashtable());
            vector.addElement(new Vector());
            cfgs.put(s, vector);
        }
    }

    /*******************************************
	 * 
	 *******************************************/	
    private void registerUser(String s, Hashtable hashtable, String s1, HttpServletRequest httpservletrequest)
    {
        Hashtable hashtable1 = getUsersHash(s);
        int i = Integer.parseInt((String)hashtable.get("maxlogin"));
        String s2;
        if((s2 = httpservletrequest.getParameter("name")) == null)
        {
            s2 = "noname";
        } else
        {
            s2 = s2.trim();
            if(s2.length() == 0)
                s2 = "noname";
            else
            if(s2.length() > i)
                s2 = s2.substring(0, i);
        }
        s2 = prepareMsg(decodeString(s2, httpservletrequest, (String)hashtable.get("encoding")));
        String s3;
        if((s3 = httpservletrequest.getParameter("mail")) == null)
            s3 = "";
        else
            s3 = prepareMsg(s3);
        String s4;
        if((s4 = httpservletrequest.getParameter("color")) == null)
            s4 = "#000000";
        String s5 = httpservletrequest.getRemoteUser();
        String s6 = httpservletrequest.getRemoteAddr();
        String s7 = httpservletrequest.getRemoteHost();
        synchronized(hashtable1)
        {
            Vector vector = (Vector)hashtable1.get(s1);
            if(vector != null)
                return;
            vector = new Vector();
            vector.addElement(s2);
            vector.addElement(s3);
            vector.addElement(s4);
            vector.addElement(s5);
            vector.addElement(s6);
            vector.addElement(s7);
            vector.addElement(new Date());
            hashtable1.put(s1, vector);
        }
    }

    /*******************************************
	 * 
	 *******************************************/	
    private String replaceDollar(String s)
    {
        StringBuffer stringbuffer = new StringBuffer("");
        for(int i = 0; i < s.length(); i++)
        {
            char c;
            if((c = s.charAt(i)) == '$')
                stringbuffer.append("$$");
            else
                stringbuffer.append(c);
        }

        return stringbuffer.toString();
    }

    /*******************************************
	 * 
	 *******************************************/	   
	private String sendWml(String s, String s1, String s2)
    {
        StringBuffer stringbuffer = new StringBuffer("");
        stringbuffer.append("<card id=\"message\" title=\"Send message\">\n");
        stringbuffer.append("<do type=\"accept\" label=\"Send\">\n");
        stringbuffer.append("<go href=\"" + s + "?" + "conf" + "=" + s1 + "&amp;" + "fct" + "=" + getId() + "\" method=\"post\">\n");
        stringbuffer.append("<postfield name=\"name\" value=\"$(sName)\"/>\n");
        stringbuffer.append("<postfield name=\"actn\" value=\"init2\"/>\n");
        stringbuffer.append("<postfield name=\"id\" value=\"" + s2 + "\"/>\n");
        stringbuffer.append("</go>\n");
        stringbuffer.append("</do>\n");
        stringbuffer.append("<do type=\"prev\">\n");
        stringbuffer.append("<prev/>\n");
        stringbuffer.append("</do>\n");
        stringbuffer.append("<p>Msg: <input name=\"sName\" value=\"\" emptyok=\"false\"/></p>\n");
        stringbuffer.append("</card>\n");
        return stringbuffer.toString();
    }

    /*******************************************
	 * 
	 *******************************************/	
    private void showDhtmlEmpty(PrintWriter printwriter)
    {
        printwriter.println("<body>");
        printwriter.println("&nbsp;");
        printwriter.println("</body>");
    }

    /*******************************************
	 * 
	 *******************************************/	
    private void showDhtmlFrame(String s, String s1, String s2, PrintWriter printwriter)
    {
        printwriter.println("<body>");
        printwriter.println("<script language=\"JavaScript\">");
        printwriter.println("function initChatPush() {");
        printwriter.println("location.href='" + s + "?" + "conf" + "=" + dosPrepare(s1) + "&" + "actn" + "=" + "init5" + "&" + "id" + "=" + s2 + "'; }");
        printwriter.println("setTimeout(\"initChatPush()\",2000);");
        printwriter.println("</script>");
        printwriter.println("&nbsp;");
        printwriter.println("</body>");
    }

    /*******************************************
	 * 
	 *******************************************/	
    private void showDhtmlPush(String s, Hashtable hashtable, String s1, String s2, String s3, PrintWriter printwriter)
    {
        String s4 = s3;
        StringBuffer stringbuffer = new StringBuffer("");
        boolean flag = "1".equals((String)hashtable.get("direction"));
        if(s4.length() == 0)
            s4 = "0";
        String s5 = (String)hashtable.get("messages");
        long l = Long.parseLong(s5);
        Vector vector = getMsgsVector(s1);
        synchronized(hashtable.get("messages"))
        {
            for(int i = 0; i < vector.size() && (long)i < l; i++)
            {
                Vector vector1 = (Vector)vector.elementAt(i);
                String s6 = (String)vector1.elementAt(4);
                if(i == 0)
                    s4 = s6;
                if(s6.equals(s3))
                    break;
                s6 = (String)vector1.elementAt(2);
                if(s6.equals("0") || s6.equals(s2) || s2.compareTo((String)vector1.elementAt(1)) == 0)
                    if(flag)
                    {
                        stringbuffer.append("<br>");
                        stringbuffer.append((String)vector1.elementAt(3));
                    } else
                    {
                        stringbuffer.insert(0, (String)vector1.elementAt(3));
                        stringbuffer.insert(0, "<br>");
                    }
                if(i > 0 && i % 5 == 0)
                    if(flag)
                        stringbuffer.append("<br>Java server-side programming &copy;&nbsp;<a href=mailto:info@servletsuite.com>codelove.co.kr</a>&nbsp;ver. 1.50");
                    else
                        stringbuffer.insert(0, "<br>Java server-side programming &copy;&nbsp;<a href=mailto:info@servletsuite.com>codelove.co.kr</a>&nbsp;ver. 1.50");
            }

        }
        printwriter.println("<body>");
        printwriter.println("<script language=\"JavaScript\">");
        printwriter.println("var pp=parent.frames[0].window;");
        printwriter.println("function nextPortion()");
        printwriter.println("{ location.href='" + s + "?" + "conf" + "=" + dosPrepare(s1) + "&" + "actn" + "=" + "init5" + "&" + "id" + "=" + s2 + "&" + "id2" + "=" + s4 + "'; }");
        if(stringbuffer.length() > 0)
            printwriter.println("pp.ff('" + jsPrepare(stringbuffer.toString()) + "');" + NEWLINE);
        printwriter.println("setTimeout(\"nextPortion()\"," + (String)hashtable.get("refresh") + "000);");
        printwriter.println("</script>");
        printwriter.println("&nbsp;");
        printwriter.println("</body>");
    }

    /*******************************************
	 * 
	 *******************************************/	
    private String showLogFile(Hashtable hashtable)
    {
        StringBuffer stringbuffer = new StringBuffer("");
        String s = (String)hashtable.get("log");
        Calendar calendar = Calendar.getInstance();
        if(s == null)
            return stringbuffer.toString();
        String s1 = String.valueOf(calendar.get(5));
        if(s1.length() == 1)
            s1 = "0" + s1;
        String s2 = String.valueOf(calendar.get(2) + 1);
        if(s2.length() == 1)
            s2 = "0" + s2;
        stringbuffer.append("<head>\n");
        stringbuffer.append("<title>Chat log for " + s1 + "/" + s2 + "/" + calendar.get(1) + "</title>\n");
        stringbuffer.append("</head>\n");
        synchronized(hashtable.get("title"))
        {
            try
            {
                BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(new FileInputStream(s + s1 + s2 + calendar.get(1) + ".htm")));
                while((s1 = bufferedreader.readLine()) != null) 
                {
                    stringbuffer.append(s1);
                    stringbuffer.append(NEWLINE);
                }
                bufferedreader.close();
            }
            catch(Exception _ex) { }
        }
        return stringbuffer.toString();
    }

    /*******************************************
	 * 클라이언트가 무선단말기일 경우의 처리
	 *******************************************/	
    private void wmlChat(String s, String s1, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse)
        throws IOException
    {
        httpservletresponse.setContentType("text/vnd.wap.wml");
        PrintWriter printwriter = httpservletresponse.getWriter();
        printwriter.println("<?xml version=\"1.0\"?>");
        printwriter.println("<!DOCTYPE wml PUBLIC \"-//WAPFORUM//DTD WML 1.1//EN\" \"http://www.wapforum.org/DTD/wml_1.1.xml\">");
        printwriter.println("<wml>");
        printwriter.println("<head>");
        printwriter.println("<meta http-equiv=\"Cache-Control\" content=\"max-age=0\" forua=\"true\"/>");
        printwriter.println("</head>");
        if(s1.length() == 0)
        {
            printwriter.println("<card>");
            printwriter.println("<p>");
            printwriter.println("Could not open configuration file");
            printwriter.println("</p>");
            printwriter.println("</card>");
        } else
        {
            String s2 = getFromQuery(s1, "conf=");
            if(s2.length() == 0)
                s2 = s1;
            String s3 = getFromQuery(s1, "id=");
            if(s3.length() == 0)
                s3 = httpservletrequest.getParameter("id");
            String s4 = getFromQuery(s1, "actn=");
            if(s4.length() == 0)
                if((s4 = httpservletrequest.getParameter("actn")) == null)
                    s4 = "init";
                else
                if(s4.length() == 0)
                    s4 = "init";
            if(s4.equals("gtnf"))
            {
                printwriter.println(getInfoBean(s2));
                printwriter.println("</wml>");
                printwriter.flush();
                printwriter.close();
                return;
            }
            if(s4.equals("init"))
                printwriter.println(initWml(s, s2));
            else
            if(s4.equals("login"))
                printwriter.println(loginWml(s, s2, s3, httpservletrequest));
            else
            if(s4.equals("init1"))
                printwriter.println(sendWml(s, s2, s3));
            else
            if(s4.equals("init2"))
                printwriter.println(acceptWml(s, s2, s3, httpservletrequest));
            else
            if(s4.equals("logout"))
                printwriter.println(logoutWml(s, s2, s3));
            else
            if(s4.equals("get"))
                printwriter.println(readWml(s, s2, s3));
        }
        printwriter.println("</wml>");
        printwriter.flush();
        printwriter.close();
    }

    private static Object forLock = new Object();
    private static Object forLock1 = new Object();
    private static Hashtable cfgs;
    private static final String VERSION = "ver. 1.50";
    private static final String CPR = "&copy;&nbsp;<a href=mailto:info@servletsuite.com>codelove.co.kr</a>&nbsp;";
    private static final String DEMOSTRING = "Java server-side programming &copy;&nbsp;<a href=mailto:info@servletsuite.com>codelove.co.kr</a>&nbsp;ver. 1.50";
    private static final boolean DEMO = true;
    private static final int HOW_LONG = 6;
    private static final int MAX_WML = 950;
    private static final int DELTA = 10;
    private static final String ACTION = "actn";
    private static final String INIT = "init";
    private static final String INIT1 = "init1";
    private static final String INIT2 = "init2";
    private static final String INIT3 = "init3";
    private static final String INIT4 = "init4";
    private static final String INIT5 = "init5";
    private static final String INIT6 = "init6";
    private static final String LOGIN = "login";
    private static final String PUT = "put";
    private static final String GET = "get";
    private static final String GET1 = "get1";
    private static final String LOGOUT = "logout";
    private static final String UPDATE = "update";
    private static final String LOG = "log";
    private static final String USERS = "list";
    private static final String DELUSER = "dlsr";
    private static final String GETINFO = "gtnf";
    private static final String MARK = "===";
    private static final String ID = "id";
    private static final String ID1 = "id1";
    private static final String ID2 = "id2";
    private static final String ALLID = "0";
    private static final String FICT = "fct";
    private static final String CHAT = "idchat2000";
    private static final String CONFIG = "conf";
    private static final String BORDER = "border";
    private static final String VIEW = "view";
    private static final String BGCOLOR1 = "bgcolor1";
    private static final String BGCOLOR2 = "bgcolor2";
    private static final String FGCOLOR = "fgcolor";
    private static final String REFRESH = "refresh";
    private static final String INACTIVITY = "inactivity";
    private static final String PRIVACY = "privacy";
    private static final String LOGFILE = "log";
    private static final String LOGO = "logo";
    private static final String MESSAGES = "messages";
    private static final String SIZE = "size";
    private static final String FACE = "face";
    private static final String TITLE = "title";
    private static final String EDITED = "edited";
    private static final String MASTER = "master";
    private static final String ADMIN = "admin";
    private static final String ENCODING = "encoding";
    private static final String INPUT = "input";
    private static final String MAXLENGTH = "maxlength";
    private static final String MAXLOGIN = "maxlogin";
    private static final String LINK1 = "link1";
    private static final String VLINK1 = "vlink1";
    private static final String ALINK1 = "alink1";
    private static final String LINK2 = "link2";
    private static final String VLINK2 = "vlink2";
    private static final String ALINK2 = "alink2";
    private static final String BGIMAGE = "bgimage";
    private static final String OFFSET = "offset";
    private static final String DHTML = "dhtml";
    private static final String FRAMES = "frames";
    private static final String DIRECTION = "direction";
    private static final String BLACKLIST = "blacklist";
    private static final String BADREDIRECT = "redirect";
    private static final String CONNECTIONS = "connections";
    private static final String TRANSLATION = "translation";
    private static final String USER_LABEL = "User";
    private static final String MAIL_LABEL = "Mail";
    private static final String HOST_LABEL = "Host";
    private static final String ADDRESS_LABEL = "Address";
    private static final String TO_LABEL = "To";
    private static final String LOGOUT_LABEL = "Logout";
    private static final String REFRESH_LABEL = "Refresh";
    private static final String LOG_LABEL = "Log";
    private static final String USERS_LABEL = "Users";
    private static final String SEND_LABEL = "Send";
    private static final String ALL_LABEL = "All";
    private static final String DEFAULT_USER_LABEL = "User";
    private static final String DEFAULT_MAIL_LABEL = "Mail";
    private static final String DEFAULT_HOST_LABEL = "Host";
    private static final String DEFAULT_ADDRESS_LABEL = "Address";
    private static final String DEFAULT_TO_LABEL = "To";
    private static final String DEFAULT_LOGOUT_LABEL = "LOGOUT";
    private static final String DEFAULT_REFRESH_LABEL = "REFRESH";
    private static final String DEFAULT_LOG_LABEL = "LOG";
    private static final String DEFAULT_USERS_LABEL = "USERS";
    private static final String DEFAULT_SEND_LABEL = "Send";
    private static final String DEFAULT_ALL_LABEL = "ALL";
    private static final String NAME = "name";
    private static final String MAIL = "mail";
    private static final String COLOR = "color";
    private static final String USER = "user";
    private static final String DEFBGCOLOR = "#FFFFFF";
    private static final String DEFFGCOLOR = "#000000";
    private static final String DEFBORDER = "1";
    private static final String DEFCOLOR = "#000000";
    private static final String DEFREFRESH = "20";
    private static final String DEFUSER = "noname";
    private static final long DEFINACTIVITY = 600L;
    private static final String DEFPRIVACY = "0";
    private static final long DEFMESSAGES = 60L;
    private static final String DEFVIEW = "1";
    private static final String DEFLOGOUT = "1";
    private static final String DEFTITLE = "codelove.co.kr chat";
    private static final String DEFENCODING = "ISO-8859-1";
    private static final String DEFLOGIN = "_top";
    private static final String DEFINPUT = "60";
    private static final String DEFMAXLENGTH = "255";
    private static final String DEFMAXLOGIN = "20";
    private static final String DEFOFFSET = "0";
    private static final String DEFDHTML = "0";
    private static final String DEFFRAMES = "75%,25%";
    private static final String DEFDIRECTION = "1";
    private static String NEWLINE = "\n";
    private static String separator = "/";

}
