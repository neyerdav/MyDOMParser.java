import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String argv[]) {
        try {
            DocumentBuilderFactory dbuilderfactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbulder = dbuilderfactory.newDocumentBuilder();
            Document doc = dbulder.parse("ea31.xml");
            doc.normalizeDocument();
            doc.getDocumentElement().normalize();
            printNodes(doc.getDocumentElement());
            Node leistungsnachweis = doc.getElementsByTagName("Leistungsnachweis").item(0);
            Node kontodaten = doc.getElementsByTagName("Kontodaten").item(0);
            NodeList klist = kontodaten.getChildNodes();
            for(int i = 0; i < klist.getLength(); i++){
                Node node = klist.item(i);
                if (node.getNodeName().equals("KontoNr")) {
                    node.setTextContent("000000");
                }
                if (node.getNodeName().equals("BLZ")) {
                    node.setTextContent("0000000");
                }
            }
            Element kurs = doc.createElement("Kurs");
            kurs.appendChild(doc.createTextNode("31251"));
            Element note = doc.createElement("Note");
            note.appendChild(doc.createTextNode("2,0"));
            Element gesamtpunkte = doc.createElement("Gesamtpunkte");
            gesamtpunkte.appendChild(doc.createTextNode("80"));
            Element datum = doc.createElement("Datum");
            datum.appendChild(doc.createTextNode("23.03.2013"));
            Element versuche = doc.createElement("AnzVersuche");
            versuche.appendChild(doc.createTextNode("1"));
            Element leistungsnachweis_neu = doc.createElement("Leistungsnachweis");
            leistungsnachweis_neu.appendChild(kurs);
            leistungsnachweis_neu.appendChild(datum);
            leistungsnachweis_neu.appendChild(note);
            leistungsnachweis_neu.appendChild(gesamtpunkte);
            leistungsnachweis_neu.appendChild(versuche);
            leistungsnachweis.getParentNode().insertBefore(leistungsnachweis_neu, leistungsnachweis);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("test_neu.xml"));
            transformer.transform(source, result);
        } catch (ParserConfigurationException ex){
            ex.printStackTrace();
        } catch (SAXException ex){
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (TransformerException ex) {
            ex.printStackTrace();
        }
    }
    public static void printNodes(Node node) {
        System.out.print("\n" + node.getNodeName());
        if(node.hasAttributes()) {
            for (int i=0; i < node.getAttributes().getLength(); i++) {
                System.out.print(": " + node.getAttributes().item(i));
            }
            if (node.getChildNodes().getLength() == 1) {
                System.out.print(": " + node.getTextContent());
            }
            NodeList nodeList = node.getChildNodes();
            for(int i = 0; i < nodeList.getLength(); i++){
                Node currentNode = nodeList.item(i);
                if(currentNode.getNodeType() == Node.ELEMENT_NODE) {
                    printNodes(currentNode);
                }
            }
        }
    }
}