package ajprogramming.TouchMouse.Utils;

import ajprogramming.TouchMouse.Mouse.IMouse;
import ajprogramming.TouchMouse.Tray.Tray;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashMap;

public class XML {

    private final LoggerEx loggerEx;
    public XML() {
        this.loggerEx = LoggerEx.getLogger(this.getClass().getName());
        this.initialize();
    }


    private void initialize() {
        try {
            this.loggerEx.info("Initializing XML Service");
            if (!XMLUtils.isDocumentExists()) {
                this.loggerEx.info("Document does not exist, creating new file");
                XMLUtils.createFile();
                this.loggerEx.info("Creating document");
                XMLUtils.createDocument();
                this.loggerEx.info("Initializing new config");

                XMLUtils.initializeConfigOnFirstRun();
            } else if (XMLUtils.checkIsDocumentIsEmpty()) {
                this.loggerEx.info("File exists, byut empty, creating new document");
                XMLUtils.createDocument();
                this.loggerEx.info("Initializing new config");
                XMLUtils.initializeConfigOnFirstRun();
            }
        } catch (IOException | ParserConfigurationException | TransformerException | SAXException e) {
            this.loggerEx.warning("Creating XML document error", e.getMessage());

            throw new RuntimeException(e);
        }

    }

    public void addMouse(IMouse mouse) {
        try {
            XMLUtils.addMouse(mouse);

        } catch (ParserConfigurationException | IOException | TransformerException | SAXException e) {
            Tray.getInstance().showMessage("Warning", String.format("Cannot add mouse name: %s, address: %s", mouse.getMouseName(), mouse.getMouseAddress()));
        }
    }

    public void changeMouseName(IMouse mouse) {
        try {
            XMLUtils.changeMouseName(mouse);
        } catch (ParserConfigurationException | IOException | TransformerException | SAXException e) {
            Tray.getInstance().showMessage("Warning", String.format("Cannot change mouse name: %s, address: %s", mouse.getMouseName(), mouse.getMouseAddress()));

        }
    }

    public HashMap<String, IMouse> getMice() {
        try {
            this.loggerEx.info("Getting mice list");

            return XMLUtils.getMice();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            Tray.getInstance().showMessage("Warning", "Cannot get saved mice lists");
            this.loggerEx.warning("Cannot get saved mice lists", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    public void removeMouse(IMouse mouse) {
        try {
            XMLUtils.removeMouse(mouse);
        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }



}
