import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Vector;

public class ChatInfoBean
{

    public ChatInfoBean()
    {
    }

    private String getUrl(String s)
    {
        if(s == null)
            return null;
        int i;
        if((i = s.indexOf("?")) <= 0)
            return null;
        if(i == s.length() - 1)
            return null;
        String s1 = s.substring(0, i) + "?" + "conf" + "=" + s.substring(i + 1) + "&" + "actn" + "=" + "gtnf";
        if(!s.toUpperCase().startsWith("HTTP"))
            s1 = "http://" + s1;
        return s1;
    }

    public int getUsersCount(String s)
    {
        String s1 = getUrl(s);
        if(s1 == null)
        {
            return 0;
        } else
        {
            Vector vector = getVector(s1);
            return vector.size() / 2;
        }
    }

    public Vector getUsersList(String s)
    {
        String s1 = getUrl(s);
        if(s1 == null)
            return new Vector();
        else
            return getVector(s1);
    }

    private Vector getVector(String s)
    {
        Object obj = null;
        URLConnection urlconnection = null;
        String s1 = "";
        Vector vector = new Vector();
        try
        {
            URL url = new URL(s);
            urlconnection = url.openConnection();
            urlconnection.setDoInput(true);
            urlconnection.setUseCaches(false);
        }
        catch(Exception _ex)
        {
            return vector;
        }
        try
        {
            BufferedReader bufferedreader = new BufferedReader(new InputStreamReader(urlconnection.getInputStream()));
            String s2;
            try
            {
                while((s2 = bufferedreader.readLine()) != null) 
                    if(s2.startsWith("==="))
                        vector.addElement(s2.substring("===".length()));
            }
            catch(Exception _ex)
            {
                new Vector();
            }
            bufferedreader.close();
        }
        catch(Exception _ex)
        {
            return new Vector();
        }
        return vector;
    }

    private static final String ACTION = "actn";
    private static final String CONFIG = "conf";
    private static final String GETINFO = "gtnf";
    private static final String MARK = "===";
}
