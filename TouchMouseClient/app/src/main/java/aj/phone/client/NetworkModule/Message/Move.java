package aj.phone.client.NetworkModule.Message;

public class Move implements UDPAction {
    private float x;
    private float y;

    public Move() {
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
