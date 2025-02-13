package ajprogramming.TouchMouse.Mouse;

import ajprogramming.TouchMouse.Tray.Tray;

public class SavedMouse implements IMouse{

    private String mouseId;
    private String mouseName;
    private String mouseAddress;
    private boolean isDefault;

    public SavedMouse(Mouse mouse) {
        this.mouseName = mouse.getMouseName();
        this.mouseAddress = mouse.getMouseAddress();
        this.mouseId = mouse.getMouseId();
    }

    public SavedMouse(String mouseId, String mouseName, String mouseAddress, boolean isDefault) {
        this.mouseName = mouseName;
        this.mouseId = mouseId;
        this.mouseAddress = mouseAddress;
        this.isDefault = isDefault;
    }
    public String getMouseName() {
        return this.mouseName;
    }

    public void setMouseName(String mouseName) {
        this.mouseName = mouseName;
    }

    public void setMouseAddress(String mouseAddress) {
        this.mouseAddress = mouseAddress;
    }

    public String getMouseAddress() {
        return this.mouseAddress;
    }

    public String getMouseID() {
        return this.mouseId;
    }

    public void setMouseId(String mouseId) {
        this.mouseId = mouseId;
    }
    public void removeMouse() {
        Tray.getInstance().showMessage("Mouse removed", String.format("Mouse %s removed", this.mouseName));
    }
    @Override
    public String toString() {
        return String.format("%s, address: %s, status: Unknown", this.getMouseName(), this.getMouseAddress());

    }
}
