package ajprogramming.TouchMouse.Mouse;

public interface IMouse {
    public String getMouseName();

    public void setMouseName(String mouseName);

    public void setMouseAddress(String mouseAddress);


    public String getMouseAddress();
    public String getMouseID();
    public void setMouseId(String mouseId);
    public void removeMouse();
}
