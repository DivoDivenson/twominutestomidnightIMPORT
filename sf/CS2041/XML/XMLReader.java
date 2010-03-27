import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import  java.util.List;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
public class XMLReader {
     public static void main(String[] args) {
          try {
                //Open the File
               File file = new File("book.xml");
                //Parse it into a DOM “Document”
               Document doc = null;
               org.jdom.input.SAXBuilder builder = new org.jdom.input.SAXBuilder();
               doc = builder.build(file);
                //get the root “element”
               Element rt = doc.getRootElement();
               System.out.println("Root element " + rt.getName());
               List attrs = rt.getAttributes();
                for (Iterator iterator = attrs.iterator(); iterator.hasNext();){
                     Attribute attr = (Attribute) iterator.next();
                     System.out.println("\t Attribute '"+attr.getName()+"' has value '"+attr.getValue()+"'");
               }
               System.out.println();
                //get a list of elements called “employee”
               List nodeLst = rt.getChildren("employee");
               System.out.println("Information of all employees");
                //iterate through the list of nodes and print out some Elements
                for (Iterator iter = nodeLst.iterator(); iter.hasNext();){
                     Element empElement = (Element)(iter.next());
                     //find an int attribute called "id" in the "employee" element
                     Attribute idAttr = empElement.getAttribute("id");
                     System.out.println("\t ID : " + idAttr.getIntValue());
                     //find a child node called "firstname" in the "employee" element
                     Element fstNmElmnt = empElement.getChild("firstname");
                     //find a child node called "lastname" in the "employee" element
                     Element lstNmElmnt = empElement.getChild("lastname");
                     System.out.println("\t First Name : " + fstNmElmnt.getText());
                     System.out.println("\t Second Name : " + lstNmElmnt.getText());
                     System.out.println();
               }
          } catch (IOException e) {
               e.printStackTrace();
          } catch (JDOMException e) {
               e.printStackTrace();
          }
     }
}

