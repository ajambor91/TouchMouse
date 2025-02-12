package ajprogramming.TouchMouse.Mouse;

import ajprogramming.TouchMouse.AppConfig;
import ajprogramming.TouchMouse.Menus.MainFrame;
import ajprogramming.TouchMouse.Network.Enums.EConnectionStatus;
import ajprogramming.TouchMouse.Network.IHost;
import ajprogramming.TouchMouse.Network.Messages.TCPMessage;
import ajprogramming.TouchMouse.Network.Messages.UDPMessage;
import ajprogramming.TouchMouse.Utils.LoggerEx;
import ajprogramming.TouchMouse.Utils.XML;

import java.net.Socket;
import java.util.HashMap;
import java.util.List;

public class MouseHandler {
    private final IHost host;
    private HashMap<String, IMouse> mouseHashMap;
    private MainFrame mainFrame;
    private final LoggerEx loggerEx;
    private final XML xml;
    public MouseHandler(IHost host) {
        this.xml = new XML();
        this.host = host;
        this.loggerEx = LoggerEx.getLogger(this.getClass().getName());
        this.initialize();
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public void addMouse(TCPMessage tcpMessage, Socket clientSocket) {
        this.loggerEx.info("Adding mouse", tcpMessage.getMouseId());
        IMouse mouse = this.mouseHashMap.get(tcpMessage.getMouseId());


        if (mouse == null ||
                (mouse instanceof Mouse && ((Mouse) mouse).getConnectionStatus() == EConnectionStatus.FAIL)
                || mouse instanceof SavedMouse) {

            Mouse initMouse = new Mouse(clientSocket, this, tcpMessage, this.host);

            if (mouse == null) {
                this.xml.addMouse(initMouse);
                this.loggerEx.info("Mouse added", tcpMessage.getMouseId());

            }
            mouse = null;
            this.mouseHashMap.put(tcpMessage.getMouseId(), initMouse);
            initMouse.start();
            this.loggerEx.info("Mouse started", tcpMessage.getMouseId());

            this.refreshMouseList();

        }
    }

    public void overrideMouse(Mouse mouse) {
        IMouse mouseFromMap = this.mouseHashMap.get(mouse.getMouseId());
        if (mouseFromMap == null) {
            this.loggerEx.warning("Mouse override failed", mouse.getMouseId());
            return;
        }
        this.mouseHashMap.put(mouse.getMouseID(), mouse);
    }

    public void reconnect(TCPMessage tcpMessage, Socket clientSocket) {
        this.loggerEx.info("Reconnecting mouse", tcpMessage.getMouseId());

        IMouse mouse = this.mouseHashMap.get(tcpMessage.getMouseId());
        if (mouse instanceof Mouse) {
            this.loggerEx.info("Mouse reconnected", tcpMessage.getMouseId());
            ((Mouse) mouse).reconnect(clientSocket);
            this.refreshMouseList();

        } else {
            this.addMouse(tcpMessage, clientSocket);
        }
    }

    public void onAction(List<UDPMessage> message) {
        message.forEach(msg -> {
            Mouse mouse = (Mouse) this.mouseHashMap.get(msg.getMouseId());
            if (mouse != null) {
                mouse.addMsg(msg);
            } else {
                this.loggerEx.info("Cannot find mouse for action", msg.getMouseId());

            }
        });


    }

    public void removeMouse(IMouse mouse) {
        this.loggerEx.info("Removing mouse", mouse.getMouseID());

        IMouse mouseFromMap =  this.mouseHashMap.get(mouse.getMouseID());
        if (mouseFromMap != null) {
            this.mouseHashMap.remove(mouse.getMouseID());
            mouseFromMap.removeMouse();
            this.xml.removeMouse(mouseFromMap);
            this.loggerEx.info("Mouse removed", mouse.getMouseID());

            this.refreshMouseList();
        } else  {
            this.loggerEx.info("Cannot find mouse to remove", mouse.getMouseID());

        }
    }

    public void disconnectMouse(IMouse mouse) {
        this.loggerEx.info("Disconnecting mouse", mouse.getMouseID());

        IMouse mouseFromMap =  this.mouseHashMap.get(mouse.getMouseID());
        if (mouseFromMap != null) {
            if (mouseFromMap instanceof Mouse) {
                ((Mouse) mouseFromMap).disconnectMouse();
                this.loggerEx.info("Mouse disconnected", mouse.getMouseID());

            }
            this.refreshMouseList();
        } else {
            this.loggerEx.info("Cannot find mouse to disconnect", mouse.getMouseID());

        }
    }

    public void disconnectAllMouse() {
        this.loggerEx.info("Disconnecting all mice");

        this.mouseHashMap.forEach((id, mouse) -> {
            if (mouse instanceof Mouse) {
                this.loggerEx.info("Disconnecting all mice, current mouse", id);

                ((Mouse) mouse).disconnectMouse();
            }
        });
        this.refreshMouseList();

    }

    private void refreshMouseList() {
        if (this.mainFrame != null) {
            this.mainFrame.refreshMousesList();

        }
    }

    public HashMap<String, IMouse> getMice() {
        return this.mouseHashMap;
    }

    private void initialize() {
        HashMap<String, IMouse> mice = AppConfig.getInstance().getMice();
        this.loggerEx.info("Initializing mouse hadnler");

        this.mouseHashMap = mice;
    }
}
