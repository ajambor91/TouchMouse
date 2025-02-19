package ajprogramming.TouchMouse.Utils;


import ajprogramming.TouchMouse.AppConfig;
import ajprogramming.TouchMouse.Mouse.IMouse;
import ajprogramming.TouchMouse.Mouse.SavedMouse;
import org.w3c.dom.*;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.UUID;


public class XMLUtils {
    public final static String defaultMouseName = "MouseTouch";
    public final static String defaultOptionAttr = "value";
    public final static String mouseNameTag = "name";
    public final static String uniqueTag = "app-id";
    public final static String appNameTag = "app-name";
    private final static String appName = "TouchMouse";
    private final static String configDirName = "config";
    private final static String configFileName = "/app_data.xml";
    private final static String mouseAddressTagName = "mouseAddress";
    private final static String mouseDefaultTagName = "default";
    private final static String mouseNameTagName = "mouseName";
    private final static String mouseIdTagName = "mouseId";
    private final static String mouseTagName = "mouse";
    private final static String rootTag = "root";
    private final static String hostTagName = "host";
    private final static String miceTagName = "mice";
    private final static DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();


    public static boolean isDocumentExists() {
        return Files.exists(Path.of(AppConfig.getInstance().getAppDataPath() + configFileName));
    }

    public static void createFile() throws IOException, ParserConfigurationException, TransformerException {


        Files.createFile(Path.of(AppConfig.getInstance().getAppDataPath() + configFileName));

    }


    public static boolean createDocument() throws ParserConfigurationException, TransformerException {
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = XMLUtils.createElement(document, XMLUtils.rootTag, XMLUtils.rootTag);

        Element host = XMLUtils.createElement(document, XMLUtils.hostTagName, XMLUtils.hostTagName);
        root.appendChild(host);
        document.appendChild(root);
        XMLUtils.saveFile(document);
        return true;

    }

    public static boolean checkIsDocumentIsEmpty() throws IOException, SAXException, ParserConfigurationException {
        Document document = XMLUtils.initializeDocument();


        Node config = document.getElementsByTagName(XMLUtils.rootTag).item(0);
        if (config == null) {
            return true;
        }
        NodeList nodeList = config.getChildNodes();
        return nodeList.getLength() <= 0;

    }

    public static void getDefaultHost() throws ParserConfigurationException, IOException, SAXException {
        Document document = XMLUtils.initializeDocument();
        NodeList nodeList = document.getElementsByTagName("hostsList").item(0).getChildNodes();

    }

    private static Element createElement(Document root, String tagName) {
        return root.createElement(tagName);
    }

    private static Element createElement(Document root, String tagName, String id) {

        Element newElement = root.createElement(tagName);
        Attr idAttr = root.createAttribute("id");
        idAttr.setValue(id);
        newElement.setAttributeNode(idAttr);
        newElement.setIdAttribute("id", true);
        return newElement;
    }

    public static HashMap<String, IMouse> getMice() throws ParserConfigurationException, IOException, SAXException {
        HashMap<String, IMouse> miceHashMap = new HashMap<>();

        Document document = XMLUtils.initializeDocument();
        Element rootElement = (Element) document.getElementsByTagName(XMLUtils.rootTag).item(0);
        NodeList nodes = document.getElementsByTagName(XMLUtils.miceTagName);
        Node node = null;
        if (nodes != null) {
            node = nodes.item(0);
        }

        NodeList miceList = null;
        if (node == null) {
            Element hosts = XMLUtils.createElement(document, XMLUtils.miceTagName, XMLUtils.miceTagName);
            rootElement.appendChild(hosts);

            miceList = hosts.getChildNodes();
        } else {
            miceList = node.getChildNodes();
        }

        for (int i = 0; i < miceList.getLength(); i++) {
            node = miceList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                String mouseId = element.getAttribute(XMLUtils.mouseIdTagName);
                String mouseAddress = element.getAttribute(XMLUtils.mouseAddressTagName);
                String mouseName = element.getAttributeNode(XMLUtils.mouseNameTagName).getValue();
                String defaultMouse = element.getAttributeNode(XMLUtils.mouseDefaultTagName).getValue();


                boolean isDefault = defaultMouse.equals("true");
                IMouse mouse = new SavedMouse(mouseId, mouseName, mouseAddress, isDefault);
                miceHashMap.put(mouse.getMouseID(), mouse);
            }
        }
        return miceHashMap;
    }

    public static void removeMouse(IMouse mouseToRemove) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = XMLUtils.initializeDocument();
        NodeList mice = document.getElementsByTagName(XMLUtils.miceTagName).item(0).getChildNodes();
        for (int i = 0; i < mice.getLength(); i++) {
            Element mouseEl = (Element) mice.item(i);
            String mouseId = mouseEl.getAttribute(XMLUtils.mouseIdTagName);

            if (mouseToRemove.getMouseID().equals(mouseId)) {
                mouseEl.getParentNode().removeChild(mouseEl);
                break;
            }
        }
        XMLUtils.saveFile(document);
    }

    public static void changeMouseName(IMouse mouseToChange) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = XMLUtils.initializeDocument();
        NodeList mice = document.getElementsByTagName(XMLUtils.miceTagName).item(0).getChildNodes();
        for (int i = 0; i < mice.getLength(); i++) {
            Element mouseEl = (Element) mice.item(i);
            String mouseId = mouseEl.getAttribute(XMLUtils.mouseIdTagName);

            if (mouseToChange.getMouseID().equals(mouseId)) {
                Attr nameAttr = mouseEl.getAttributeNode(XMLUtils.mouseNameTagName);
                nameAttr.setValue(mouseToChange.getMouseName());
                mouseEl.setAttributeNode(nameAttr);
                break;
            }
        }
        XMLUtils.saveFile(document);
    }

    public static void addMouse(IMouse mouse) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = XMLUtils.initializeDocument();
        Element rootElement = (Element) document.getElementsByTagName(XMLUtils.miceTagName).item(0);
        if (rootElement == null) {
            Element config = (Element) document.getElementsByTagName(XMLUtils.rootTag).item(0);
            rootElement = XMLUtils.createElement(document, XMLUtils.miceTagName, XMLUtils.miceTagName);
            config.appendChild(rootElement);
        }
        Element mouseElement = document.createElement(XMLUtils.mouseTagName);
        Attr defaultAttr = document.createAttribute(XMLUtils.mouseDefaultTagName);
        if (rootElement.getChildNodes().getLength() == 0) {
            defaultAttr.setValue("true");
        } else {
            defaultAttr.setValue("false");
        }
        mouseElement.setAttributeNode(defaultAttr);
        Attr mouseAddrAttr = document.createAttribute(XMLUtils.mouseAddressTagName);
        mouseAddrAttr.setValue(mouse.getMouseAddress());
        mouseElement.setAttributeNode(mouseAddrAttr);
        Attr mouseNameAttr = document.createAttribute(XMLUtils.mouseNameTagName);
        mouseNameAttr.setValue(mouse.getMouseName());
        mouseElement.setAttributeNode(mouseNameAttr);

        Attr mouseIdAttr = document.createAttribute(XMLUtils.mouseIdTagName);
        mouseIdAttr.setValue(mouse.getMouseID());
        mouseElement.setAttributeNode(mouseIdAttr);

        Attr idAttr = document.createAttribute("id");
        idAttr.setValue(mouse.getMouseID());
        mouseElement.setAttributeNode(idAttr);
        rootElement.appendChild(mouseElement);


        XMLUtils.saveFile(document);
    }

    private static void saveFile(Document document) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(AppConfig.getInstance().getAppDataPath() + configFileName));
        transformer.transform(source, result);
    }

    private static Document initializeDocument() throws ParserConfigurationException, IOException, SAXException {
        File xmlFile = new File(AppConfig.getInstance().getAppDataPath() + configFileName);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        document.getDocumentElement().normalize();
        return document;
    }

    public static void initializeConfigOnFirstRun() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = XMLUtils.initializeDocument();
        Element nameElement = (Element) document.getElementsByTagName(XMLUtils.mouseNameTag).item(0);
        Element config = (Element) document.getElementsByTagName(XMLUtils.hostTagName).item(0);

        if (nameElement == null || nameElement.getAttributeNode(XMLUtils.mouseNameTag) == null) {
            nameElement = XMLUtils.createElement(document, XMLUtils.mouseNameTag);
            Attr attr = document.createAttribute(XMLUtils.mouseNameTag);
            attr.setValue(XMLUtils.defaultMouseName);
            nameElement.setAttributeNode(attr);
            attr = document.createAttribute("id");
            attr.setValue(XMLUtils.mouseNameTag);
            nameElement.setAttributeNode(attr);
            config.appendChild(nameElement);
        }
        Element uniqueIdEl = (Element) document.getElementsByTagName(XMLUtils.uniqueTag).item(0);
        if (uniqueIdEl == null || uniqueIdEl.getAttribute(XMLUtils.defaultOptionAttr) == null) {
            uniqueIdEl = document.createElement(XMLUtils.uniqueTag);
            Attr attr = document.createAttribute("id");
            attr.setValue(XMLUtils.uniqueTag);
            uniqueIdEl.setAttributeNode(attr);
            attr = document.createAttribute(XMLUtils.defaultOptionAttr);
            String id = String.valueOf(UUID.randomUUID());
            attr.setValue(id);
            uniqueIdEl.setAttributeNode(attr);
            config.appendChild(uniqueIdEl);
        }

        Element appNameEl = (Element) document.getElementsByTagName(XMLUtils.appNameTag).item(0);
        if (appNameEl == null || appNameEl.getAttribute(XMLUtils.defaultOptionAttr) == null) {
            appNameEl = document.createElement(XMLUtils.appNameTag);
            Attr attr = document.createAttribute("id");
            attr.setValue(XMLUtils.appNameTag);
            appNameEl.setAttributeNode(attr);
            attr = document.createAttribute(XMLUtils.defaultOptionAttr);
            attr.setValue(XMLUtils.appName);
            appNameEl.setAttributeNode(attr);
            config.appendChild(appNameEl);
        }
        XMLUtils.saveFile(document);

    }

    public static void setOption(String optionName, String optionValue) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = XMLUtils.initializeDocument();
        Element nameElement = (Element) document.getElementsByTagName(optionName).item(0);
        Attr nameAttr = nameElement.getAttributeNode("name");
        nameAttr.setValue(optionValue);
        XMLUtils.saveFile(document);
    }

    public static String getOption(String optionName, String attribute) throws ParserConfigurationException, IOException, SAXException {
        Document document = XMLUtils.initializeDocument();
        Element optionEl = (Element) document.getElementsByTagName(optionName).item(0);
        if (optionEl == null) {
            throw new NoSuchElementException("No option found");
        }
        return optionEl.getAttributeNode(attribute).getValue();

    }
}
