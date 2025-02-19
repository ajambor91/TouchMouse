package aj.phone.client.NetworkModule.Enums;

public enum EFunctionalKey {

    BCKSPC(67, "BCKSPC"),
    SPC(32, "SPC"),
    RTN(66, "RTN");

    private final int key;
    private final String keyValue;

    EFunctionalKey(int key, String keyValue) {
        this.key = key;
        this.keyValue = keyValue;
    }

    public static EFunctionalKey fromKey(int key) {
        for (EFunctionalKey e : values()) {
            if (e.key == key) {
                return e;
            }
        }
        return null;
    }

    public static EFunctionalKey fromKeyValue(String keyValue) {
        for (EFunctionalKey e : values()) {
            if (e.keyValue.equals(keyValue)) {
                return e;
            }
        }
        return null;
    }

    public int getKey() {
        return key;
    }

    public String getKeyValue() {
        return keyValue;
    }


}