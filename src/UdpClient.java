import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.time.Duration;
import java.time.Instant;
import java.util.Scanner;

public class UdpClient {
    public static void main(String[] args) throws SocketException, IOException {
        int            port          = 12345;
        InetAddress    serverAddress = InetAddress.getLocalHost();
        // We don't specify the @ip and port here because they'll be defined in the packet;
        // specifying them would make the socket a listening socket.
        try ( DatagramSocket socketClient = new DatagramSocket();
              Scanner scanner = new Scanner(System.in);){
            while (true){
                byte[] buffer;
                System.out.print("Enter message: ");
                String msgToSend = scanner.nextLine();
                buffer = msgToSend.getBytes();
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length, serverAddress, port); // to received packet
                socketClient.send(packet); // send to newMessage via socket to the client
                Instant begin = Instant.now();
                System.out.println("Message sent to server: [" + msgToSend + "]");
                byte[] receivedDMsg = new byte[256];
                packet = new DatagramPacket(receivedDMsg, receivedDMsg.length);
                socketClient.receive(packet);
                Instant end = Instant.now();
                String received = new String(receivedDMsg);
                System.out.println("Message received from server: [" + received.trim() + "] in [" + Duration.between(begin, end).toSeconds() + "s]");
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
