package ajprogramming.TouchMouse.Mouse;

public interface IMouse {
    String getMouseName();

    void setMouseName(String mouseName);

    String getMouseAddress();

    void setMouseAddress(String mouseAddress);

    String getMouseID();

    void setMouseId(String mouseId);

    void removeMouse();
}
