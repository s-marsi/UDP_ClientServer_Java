import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UdpServer {
    public static void main(String[] args) throws SocketException , IOException {
        int port = 12345;
        DatagramSocket server;
        //create DatagramSocket to receive the DatagramPackets from client on the port.
        try (DatagramSocket socketServer = new DatagramSocket(port);
             ExecutorService executor = Executors.newFixedThreadPool(2);){

            while (true) {
                System.out.println("Server listening on port " + port);
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length); // to received packet
                socketServer.receive(packet);
                executor.submit(() -> {
                    try {
                        String received = receiveMessage(socketServer, packet);
                        sendMessage(socketServer, packet, received);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    static public void sendMessage(DatagramSocket socketServer, DatagramPacket packet, String received) throws SocketException, IOException, InterruptedException {
        Thread.sleep(10000);
        String      responseMsg   = received.toUpperCase().trim();
        InetAddress clientIp      = packet.getAddress();
        int         clientPort    = packet.getPort();
        byte[]      responseBytes = responseMsg.getBytes();

        packet = new DatagramPacket(responseBytes, responseBytes.length, clientIp, clientPort);
        socketServer.send(packet); // send to newMessage via socket to the client
        System.out.println("Message sent: [" + responseMsg.trim() + "]");
    }

    static public String receiveMessage(DatagramSocket socketServer, DatagramPacket packet){
        InetAddress clientIp = packet.getAddress();
        int         clientPort = packet.getPort();
        String received = new String(packet.getData());
        System.out.println("Message received from client Ip:[" + clientIp + "], port: ["+ clientPort + "], message: [" + received.trim() + "]");
        return (received);
    }
}
