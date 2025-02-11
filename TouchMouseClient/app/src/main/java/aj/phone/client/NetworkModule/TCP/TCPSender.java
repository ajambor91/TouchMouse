package aj.phone.client.NetworkModule.TCP;

import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import aj.phone.client.NetworkModule.Message.MessageCreator;

public class TCPSender extends Thread {
    private final Socket socket;
    private final boolean running;
    private final TCPMessageBuffer tcpMessageBuffer;

    public TCPSender(Socket socket, TCPMessageBuffer tcpMessageBuffer) {
        this.running = true;
        this.tcpMessageBuffer = tcpMessageBuffer;
        this.socket = socket;
        this.start();
    }

    @Override
    public void run() {
        while (this.running) {

            try {
                Log.d("TCP SENDER", "Main loop");

                if (!this.tcpMessageBuffer.isEmpty()) {
                    MessageCreator messageCreator = new MessageCreator(this.tcpMessageBuffer.getMessage());
                    String jsonMessage = messageCreator.jsonfyMessage();
                    Log.d("TCP SENDER", String.format("Sending message: %s", jsonMessage));
                    OutputStream outputStream = this.socket.getOutputStream();
                    PrintWriter writer = new PrintWriter(outputStream, true);
                    writer.println(jsonMessage);
                }
                synchronized (this) {
                    this.wait();
                }

            } catch (InterruptedException | IOException e) {
                Log.d("TCP Sender", "Host diconnected");
            }
        }
    }

}
