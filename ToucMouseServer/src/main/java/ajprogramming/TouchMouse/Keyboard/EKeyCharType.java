package ajprogramming.TouchMouse.Keyboard;

public enum EKeyCharType {
    NORMAL_OR_SPECIAL("normal"),
    COMBINED("combined"),
    DIACTRIC("diactric");

    private final String charType;

    EKeyCharType(String type) {this.charType = type;}

}
