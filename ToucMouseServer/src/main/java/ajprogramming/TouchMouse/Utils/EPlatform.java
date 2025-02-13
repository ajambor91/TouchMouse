package ajprogramming.TouchMouse.Utils;

public enum EPlatform {
    WINDOWS("windows"),
    LINUX("linux");

    private String os;

    EPlatform(String os) {this.os = os;}

    public String getOs() {
        return this.os;
    }
}
