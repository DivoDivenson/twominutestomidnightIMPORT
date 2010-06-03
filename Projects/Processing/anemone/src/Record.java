import java.net.*;
import java.text.*;
import java.util.*;
import java.util.regex.*;

import processing.core.PApplet;


public class Record {
  
  // IP address of hostname of visitor
  String ip;
  
  // Identity and password for users that have logged in.
  String userIdent;
  String userAuth;
  
  // Timestamp for when visit happened (milliseconds since Jan 1, 1970)
  long timestamp;
  
  // Retrieval method, usually GET or POST
  String method;
  
  // The URL requested from the server
  String url;  
  
  // Usually HTTP/1.1, but might be HTTP/1.0 or some other
  String protocol;

  // Status code, the full list is available from the W3C.
  // http://www.w3.org/Protocols/rfc2616/rfc2616-sec10.html
  int status;
  
  // Number of bytes in this transaction
  int bytes;

  // Last page/object visited prior to this one
  String referer;
  
  // Could keep track of different user agents: browser types, platforms, etc
  String userAgent;

  // Date parser for Apache log file format, e.g. 08/Apr/2007:16:49:00 -0400
  static DateFormat timestampFormat = 
    new SimpleDateFormat("dd/MMMM/yyyy:HH:mm:ss ZZZZ");

  // Pattern to match Apache "combined" log files
  // Alter this for other log file formats
  static final String combinedLogFormat =
    "^(\\S+) (\\S+) (\\S+) \\[([^\\]]+)\\] " +
    "\"(\\S+) (.*?) (\\S+)\" (\\S+) (\\S+) \"(\\S+)\" \"(.*)\"$";
  // Use this if combined logs are not available
  static final String commonLogFormat =
    "^(\\S+) (\\S+) (\\S+) \\[([^\\]]+)\\] " +
    "\"(\\S+) (.*?) (\\S+)\" (\\S+) (\\S+)$";
  static Pattern pattern = Pattern.compile(combinedLogFormat);
  
  
  public Record(String line) {
    Matcher m = pattern.matcher(line);

    if (m.matches()) {
      ip = m.group(1);
      
      userIdent = m.group(2);
      userAuth = m.group(3);

      try {
        String timestampStr = m.group(4);
        Date timestampDate = timestampFormat.parse(timestampStr);
        timestamp = timestampDate.getTime();
      } catch (ParseException e) {
        e.printStackTrace();
      }

      method = m.group(5);
      url = m.group(6);
      protocol = m.group(7);
      
      status = PApplet.parseInt(m.group(8));
      bytes = PApplet.parseInt(m.group(9)); 
      
      if (m.groupCount() > 9) {
        referer = m.group(10);      
        userAgent = m.group(11);
      } else {
        // If no referer or useragent info available, leave it blank
        referer = "-";
        userAgent = "";
      }
    }
  }
  
  
  /////////////////////////////////////////////////////////////////////////////

  
  // If a CGI script, don't record the ? character or what follows
  public void removeQueryString() {
    int queryIndex = url.lastIndexOf('?');
    if (queryIndex != -1) {
      url = url.substring(0, queryIndex);
    }
  }

  
  /////////////////////////////////////////////////////////////////////////////


  // If your index page uses something besides the list here, 
  // (e.g. it's index.aspx, or index.php3) add a line for it.
  static ArrayList<String> indexPageList = new ArrayList<String>();
  static {
    indexPageList.add("/index.html");
    indexPageList.add("/index.htm");
    indexPageList.add("/index.cgi");
    indexPageList.add("/index.php");
  }
  
  // Remove the name of the index page to ensure that a URL like 
  // http://benfry.com/ is treated the same as http://benfry.com/index.html 
  public void removeIndexPage() {
    for (String ending : indexPageList) {
      if (url.endsWith(ending)) {
        // Remove the ending portion, except for the slash
        url = url.substring(0, url.length() - ending.length() + 1);
      }
    }
  }
  
  
  /////////////////////////////////////////////////////////////////////////////

  
  HashMap<String,String> hostTable = new HashMap<String, String>();
  
  /**
   * If your hosting provider only stores IP addresses, those could also
   * be converted into named addresses here, with the help of a HashMap
   * so that the same lookup need not happen repeatedly.
   * Host name lookups are time-consuming, so cache the results in a HashMap.
   */
  public void lookupHostName() {
    String name = (String) hostTable.get(ip);
    if (name == null) {
      try {
        InetAddress addr = InetAddress.getByName(ip);
        name = addr.getHostName();
      } catch (UnknownHostException e) {
        // If there's a problem, just use the original ip as the name, 
        // but still put into the table, to avoid hitting this exception again.
        name = ip;
      }
      hostTable.put(ip, name);
    }
    // Replace ip address with the new name
    ip = name;
  }
  
  
  /////////////////////////////////////////////////////////////////////////////

  
  static String[] skippedExtensions = {
    "gif", "jpg", "jpeg", "au", "class", "jar", "gz", "zip", "sit"
  };
  static ArrayList<String> skippedExtensionList = 
    new ArrayList<String>(Arrays.asList(skippedExtensions));
  
  
  public boolean skipExtension() {
    String ext = getExtension();
    return (ext != null && skippedExtensionList.contains(ext));
  }

  
  public String getExtension() {
    int extIndex = url.lastIndexOf(".");
    if (extIndex != -1) {
      return url.substring(extIndex + 1);
    }
    return null;
  }
  
  
  /////////////////////////////////////////////////////////////////////////////
  
  
  public String cleanReferer() {
    // Figure out if this is referer is from the same site, 
    // which might be using an alias for the name
    // e.g. processing.org instead of www.processing.org
    if (!referer.startsWith(siteAddress)) {
      for (String alias : siteAliases) {
        if (referer.startsWith(alias)) {
          // Replace the alias with the real address of the site
          referer = siteAddress + referer.substring(alias.length());
          break;
        }
      }
    }
    // Remove the site address from the beginning of the URL, 
    // so that it's the same format as the other links
    if (referer.startsWith(siteAddress)) {
      referer = referer.substring(siteAddress.length());
    }
    return referer;
  }

  
  /////////////////////////////////////////////////////////////////////////////

 
  static String siteAddress;

  static public void setSiteAddress(String address) {
    siteAddress = address;
  }

  
  /////////////////////////////////////////////////////////////////////////////

  
  static ArrayList<String> siteAliases;

  static public void addSiteAlias(String alias) {
    if (siteAliases == null) {
      siteAliases = new ArrayList<String>();
    }
    siteAliases.add(alias);
  }  
}


