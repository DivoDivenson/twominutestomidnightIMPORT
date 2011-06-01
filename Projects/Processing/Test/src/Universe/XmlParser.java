package Universe;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.*;

import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.apache.xerces.parsers.DOMParser;

public class XmlParser {
	private DocumentBuilder builder;



	public Period[] parseHis(File file){

		Period results[] = new Period[0]; //So I'm being lazy to get a return out of a try block
		try {
			//File file = new File(filename);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			NodeList periods = doc.getElementsByTagName("period");
			results = new Period[periods.getLength()];
			for(int i =0; i < periods.getLength(); i++){
				Node period = periods.item(i);
				if(period.getNodeType() == Node.ELEMENT_NODE){
					Element element = (Element) period;
					NodeList nameList = element.getElementsByTagName("name");
					Element nameEle = (Element) nameList.item(0); //Get first element of type name
					NodeList names = nameEle.getChildNodes();
					
					String Name = ((Node) names.item(0)).getNodeValue();
					
					NodeList text = element.getElementsByTagName("text");
					Element textEle = (Element) text.item(0);
					NodeList texts = textEle.getChildNodes();
					
					//System.out.println(Name);
					String Text = "";
					if(texts.item(0) != null){
						Text = ((Node) texts.item(0)).getNodeValue();
					}
					Period result = new Period(Name, Text);
					
					NodeList eventList = element.getElementsByTagName("event");
					for(int j = 0; j < eventList.getLength(); j ++){
						Node event = eventList.item(j);
						if(event.getNodeType() == Node.ELEMENT_NODE){
							Element eventEle = (Element) event;
							NodeList times = eventEle.getElementsByTagName("time");
							Element time = (Element) times.item(0);
							NodeList timeSingle = time.getChildNodes(); //Should really only contain a single value
							
							long year = new Float(((Node) timeSingle.item(0)).getNodeValue()).longValue(); //Some rounding errors for v/ large
							//System.out.println(year);
							
							NodeList eventTexts = eventEle.getElementsByTagName("text");
							NodeList textSingle = ((Element) eventTexts.item(0)).getChildNodes();
							String eventText = ((Node)textSingle.item(0)).getNodeValue();
							
							
							Event eventFinal =  new Event(eventText, year);
							result.addEvent(eventFinal);
							
						}//if
					}//for j
				//	System.out.println(result);
					results[i] = result;
											
				}//if
											
			}//for i
			
			
			
	
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//System.out.println(results.length);
		return results;

	}
}
