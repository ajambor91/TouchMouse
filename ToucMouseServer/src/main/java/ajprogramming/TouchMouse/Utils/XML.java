package ajprogramming.TouchMouse.Utils;

import ajprogramming.TouchMouse.Mouse.IMouse;
import ajprogramming.TouchMouse.Tray.Tray;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashMap;

public class XML {

    public XML() {
        this.initialize();
    }


    private void initialize() {
        try {
            if (!XMLUtils.isDocumentExists()) {
                XMLUtils.createFile();
                XMLUtils.createDocument();
                XMLUtils.initializeConfigOnFirstRun();
            } else if (XMLUtils.checkIsDocumentIsEmpty()) {
                XMLUtils.createDocument();
                XMLUtils.initializeConfigOnFirstRun();
            }
        } catch (IOException | ParserConfigurationException | TransformerException | SAXException e) {
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

    public HashMap<String, IMouse> getMice() {
        try {
            return XMLUtils.getMice();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            Tray.getInstance().showMessage("Warning", "Cannot get saved mice lists");
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
