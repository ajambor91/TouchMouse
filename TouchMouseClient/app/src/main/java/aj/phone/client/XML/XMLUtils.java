package aj.phone.client.XML;

import android.util.Log;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import aj.phone.client.IHost;
import aj.phone.client.NetworkModule.Host;
import aj.phone.client.Utils.Config;
import aj.phone.client.Utils.DirUtils;

public class XMLUtils {
    public final static String defaultMouseName = "JaTouch";
    public final static String defaultOptionAttr = "value";
    public final static String mouseNameTag = "name";
    public final static String uniqueTag = "app-id";
    public final static String appNameTag = "app-name";
    private final static String appName = "JaTouch";
    private final static String path = Config.getInstance().getDataPath() + "/config";
    private final static String configDirName = "config";
    private final static String configFileName = "/app_data.xml";
    private final static String rootTag = "config";
    private final static String hostsTag = "hosts";
    private final static DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();


    public static boolean checkIsDocumentExists() {

        Log.d("XMLUtils", "Checking is file exists");
        final File directory = new File(path);
        boolean isDir = directory.isDirectory();
        if (isDir) {
            Log.d("XMLUtils", "XML dir exists");
            File file = new File(path + configFileName);
            Log.d("XML", String.format("Is file exists: %s", file));
            return file.exists();
        }
        return false;
    }

    public static boolean createDocumentDir() {
        Log.d("XMLUTILS", "Creating xml dir");
        final boolean isDir = DirUtils.createDir(configDirName);
        return isDir;
    }

    public static boolean createDocumentList() throws ParserConfigurationException, TransformerException {
        Log.d("XMLUtils", "Building xml file");
        DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element root = XMLUtils.createElement(document, XMLUtils.rootTag, XMLUtils.rootTag);
        document.appendChild(root);
        XMLUtils.saveFile(document);
        return true;

    }

    public static boolean checkIsDocumentIsEmpty() throws IOException, SAXException, ParserConfigurationException {
        Document document = XMLUtils.initializeDocument();

        NodeList nodeList = document.getElementById("hostsList").getChildNodes();
        return nodeList.getLength() <= 0;

    }

    public static void getDefaultHost() throws ParserConfigurationException, IOException, SAXException {
        Document document = XMLUtils.initializeDocument();
        NodeList nodeList = document.getElementById("hostsList").getChildNodes();

    }

    private static Element createElement(Document root, String tagName) {
        return root.createElement(tagName);
    }

    private static Element createElement(Document root, String tagName, String id) {
        Log.d("XML", "Creating new markdown element");

        Element newElement = root.createElement(tagName);
        Attr idAttr = root.createAttribute("id");
        idAttr.setValue(id);
        newElement.setAttributeNode(idAttr);
        Log.d("XML", "Created new markdown element");
        return newElement;
    }

    public static HashMap<String, IHost> getHosts() throws ParserConfigurationException, IOException, SAXException {
        HashMap<String, IHost> hostHashMap = new HashMap<>();
        Document document = XMLUtils.initializeDocument();
        Element rootElement = document.getElementById(XMLUtils.rootTag);
        NodeList nodeList = document.getElementById(XMLUtils.hostsTag).getChildNodes();
        if (nodeList == null) {
            Element hosts = XMLUtils.createElement(document, XMLUtils.hostsTag, XMLUtils.hostsTag);
            rootElement.appendChild(hosts);
            nodeList = hosts.getChildNodes();
        }

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                IHost host = new Host();
                Element element = (Element) node;
                String ip = element.getAttributeNode("address").getValue();
                String name = element.getAttributeNode("name").getValue();
                String defaultHost = element.getAttributeNode("default").getValue();
                host.setIsDefault(defaultHost.equals("true"));
                host.setHostAddress(ip);
                host.setName(name);
                hostHashMap.put(host.getHostAddress(), host);
            }
        }
        return hostHashMap;
    }

    public static void addHost(IHost host) throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Document document = XMLUtils.initializeDocument();
        Element rootElement = document.getElementById(XMLUtils.hostsTag);
        if (rootElement == null) {
            Element config = document.getElementById(XMLUtils.rootTag);
            rootElement = XMLUtils.createElement(document, XMLUtils.hostsTag, XMLUtils.hostsTag);
            config.appendChild(rootElement);
        }
        Element hostElement = document.createElement("host");
        Attr defaultAttr = document.createAttribute("default");
        if (hostElement.getChildNodes().getLength() == 0) {
            defaultAttr.setValue("true");
        } else {
            defaultAttr.setValue("false");
        }
        hostElement.setAttributeNode(defaultAttr);
        Attr ipAttr = document.createAttribute("address");
        ipAttr.setValue(host.getHostAddress());
        hostElement.setAttributeNode(ipAttr);
        Attr hostNameAttr = document.createAttribute("name");
        hostNameAttr.setValue("192.168.2.165");
        hostElement.setAttributeNode(hostNameAttr);
        rootElement.appendChild(hostElement);


        XMLUtils.saveFile(document);
    }

    private static void saveFile(Document document) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(path + configFileName));
        transformer.transform(source, result);
        Log.d("XML", "Saved document");
    }

    private static Document initializeDocument() throws ParserConfigurationException, IOException, SAXException {
        File xmlFile = new File(path + configFileName);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(xmlFile);
        document.getDocumentElement().normalize();
        return document;
    }

    public static void removeHost(IHost host)
            throws ParserConfigurationException,
            IOException,
            SAXException,
            NullPointerException
    {
        Document document = XMLUtils.initializeDocument();
        Node hostNode = document.getElementById(XMLUtils.hostsTag);
        if (hostNode == null) {
            throw new NullPointerException("Node host does not exist");
        }
        NodeList hostsListNode = hostNode.getChildNodes();

        if (hostsListNode == null || hostsListNode.getLength() == 0) {
            throw new NullPointerException("Node host is null or empty");
        }

        for (int i = 0; i < hostsListNode.getLength(); i++) {
            Element hostEl = (Element) hostsListNode.item(i);
            String ipAddr = hostEl.getAttribute("address");
            if (host.getHostAddress().equals(ipAddr)) {
                hostEl.getParentNode().removeChild(hostEl);
            }
        }

    }

    public static void initializeConfigOnFirstRun() throws ParserConfigurationException, IOException, SAXException, TransformerException {
        Log.d("XML", "Initializiing config");
        Document document = XMLUtils.initializeDocument();
        Element nameElement = document.getElementById(XMLUtils.mouseNameTag);
        Element config = document.getElementById(XMLUtils.rootTag);

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
        Element uniqueIdEl = document.getElementById(XMLUtils.uniqueTag);
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

        Element appNameEl = document.getElementById(XMLUtils.appNameTag);
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
        Log.d("XML", String.format("Set option %s with %s value", optionName, optionValue));
        Document document = XMLUtils.initializeDocument();
        Element nameElement = document.getElementById(optionName);
        Attr nameAttr = nameElement.getAttributeNode("name");
        nameAttr.setValue(optionValue);
        XMLUtils.saveFile(document);
    }

    public static String getOption(String optionName, String attribute) throws ParserConfigurationException, IOException, SAXException {
        Log.d("XML", String.format("Get option %s", optionName));
        Document document = XMLUtils.initializeDocument();
        Element optionEl = document.getElementById(optionName);
        if (optionEl == null) {
            throw new NoSuchElementException("No option found");
        }
        return optionEl.getAttributeNode(attribute).getValue();

    }
}
