
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.*;

public abstract class Jogador implements SendCommand {

    protected DatagramSocket socket;		
    protected InetAddress host;		
    protected int port;			
    protected String team;			
    protected char lado;
    protected char ladoInimigo;
    protected int numero;
    protected SensorInput brain;		
    protected boolean isPlaying;              
    protected Pattern message_pattern = Pattern.compile("^\\((\\w+?)\\s.*");
    protected Pattern hear_pattern = Pattern.compile("^\\(hear\\s(\\w+?)\\s(\\w+?)\\s(.*)\\).*");
    //protected Pattern coach_pattern = Pattern.compile("coach");
    // constants
    protected static final int MSG_SIZE = 4096;
    protected double MARCACAO = 3;

    public Jogador(InetAddress host, int port, String team)
            throws SocketException {
        socket = new DatagramSocket();
        this.host = host;
        this.port = port;
        this.team = team;
        isPlaying = true;
    }

    public void finalize() {
        socket.close();
    }

    protected void mainLoop() throws IOException {
        byte[] buffer = new byte[MSG_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, MSG_SIZE);

        // first we need to initialize connection with server
        init();

        socket.receive(packet);
        parseInitCommand(new String(buffer));
        port = packet.getPort();

        // Now we should be connected to the server
        // and we know side, player number and play mode
        while (isPlaying) {
            parseSensorInformation(receive());
        }
        finalize();
    }

    @Override
    public void move(double x, double y) {
        send("(move " + Double.toString(x) + " " + Double.toString(y) + ")");
    }

    public void turn(double moment) {
        send("(turn " + Double.toString(moment) + ")");
    }

    public void turn_neck(double moment) {
        send("(turn_neck " + Double.toString(moment) + ")");
    }

    public void dash(double power) {
        send("(dash " + Double.toString(power) + ")");
    }

    public void goalie_catch(double direction) {
        send("(catch " + Double.toString(direction) + ")");
    }

    public void kick(double power, double direction) {
        send("(kick " + Double.toString(power) + " " + Double.toString(direction) + ")");
    }

    public void say(String message) {
        send("(say " + message + ")");
    }

    public void changeView(String angle, String quality) {
        send("(change_view " + angle + " " + quality + ")");
    }

    public void bye() {
        isPlaying = false;
        send("(bye)");
    }

    protected void parseInitCommand(String message)
            throws IOException {
        Matcher m = Pattern.compile("^\\(init\\s(\\w)\\s(\\d{1,2})\\s(\\w+?)\\).*$").matcher(message);
        if (!m.matches()) {
            throw new IOException(message);
        }

        // initialize player's brain
        lado = m.group(1).charAt(0);
        
        if(lado == 'r')
            ladoInimigo = 'l';
        else
            ladoInimigo = 'r';
        
        numero = Integer.parseInt(m.group(2));

        brain = new Brain(this,
                team,
                m.group(1).charAt(0),
                Integer.parseInt(m.group(2)),
                m.group(3));
    }

    protected abstract void init();

    protected void parseSensorInformation(String message)
            throws IOException {
        // First check kind of information		
        Matcher m = message_pattern.matcher(message);
        if (!m.matches()) {
            throw new IOException(message);
        }
        if (m.group(1).compareTo("see") == 0) {
            VisualInfo info = new VisualInfo(message);
            info.parse();
            brain.see(info);
        } else if (m.group(1).compareTo("hear") == 0) {
            parseHear(message);
        }else if(m.group(1).compareTo("sense_body") == 0){
            //System.out.println(message);
        }
    }

    protected void parseHear(String message)
            throws IOException {
        // get hear information
        Matcher m = hear_pattern.matcher(message);
        int time;
        String sender;
        String uttered;
        if (!m.matches()) {
            throw new IOException(message);
        }
        time = Integer.parseInt(m.group(1));
        sender = m.group(2);
        uttered = m.group(3);
        if (sender.compareTo("referee") == 0) {
            brain.hear(time, uttered);
        } //else if( coach_pattern.matcher(sender).find())
        //    brain.hear(time,sender,uttered);
        else if (sender.compareTo("self") != 0) {
            brain.hear(time, Integer.parseInt(sender), uttered);
        }
    }

    protected void send(String message) {
        byte[] buffer = Arrays.copyOf(message.getBytes(), MSG_SIZE);
        try {
            DatagramPacket packet = new DatagramPacket(buffer, MSG_SIZE, host, port);
            socket.send(packet);
        } catch (IOException e) {
            System.err.println("socket sending error " + e);
        }

    }

    protected String receive() {
        byte[] buffer = new byte[MSG_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, MSG_SIZE);
        try {
            socket.receive(packet);
        } catch (SocketException e) {
            System.out.println("shutting down...");
        } catch (IOException e) {
            System.err.println("socket receiving error " + e);
        }
        return new String(buffer);
    }
}
