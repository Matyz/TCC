
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Valderlei
 */
public class JogadorDeLinha extends Jogador {

        
    public JogadorDeLinha(InetAddress host, int port, String team) throws SocketException {
        super(host, port, team);
    }

    @Override
    protected void init() {
        send("(init " + team + " (version 9))");
    }

    public static void main(String a[]) throws SocketException, IOException {
        String hostName = "";
        int port = 6000;
        String team = "Krislet3";

        try {
            // First look for parameters
            for (int c = 0; c < a.length; c += 2) {
                if (a[c].compareTo("-host") == 0) {
                    hostName = a[c + 1];
                } else if (a[c].compareTo("-port") == 0) {
                    port = Integer.parseInt(a[c + 1]);
                } else if (a[c].compareTo("-team") == 0) {
                    team = a[c + 1];
                } else {
                    throw new Exception();
                }

            }
        } catch (Exception e) {
            System.err.println("");
            System.err.println("USAGE: krislet [-parameter value]");
            System.err.println("");
            System.err.println("    Parameters  value          default");
            System.err.println("   --------------------------------------");
            System.err.println("    host        host_name      localhost");
            System.err.println("    port        port_number    6000");
            System.err.println("    team        team_name      Kris");
            System.err.println("    goalie      is_goalie?     false");
            System.err.println("");
            System.err.println("    Example:");
            System.err.println("      krislet -host www.host.com -port 6000 -team Poland");
            System.err.println("    or");
            System.err.println("      krislet -host 193.117.005.223");
            return;
        }

        Jogador jogador = new JogadorDeLinha((InetAddress.getByName(hostName)), port, team);
        jogador.mainLoop();
    }
    
    @Override
    public void posicaoEmCampo(){
        move(Positions.getX(numero), Positions.getY(numero));
    }
    
    @Override
    public void estrategiaDeJogo() {
        Memory m_memory = brain.getM_memory();
        Matematica matematica = brain.getMatematica();
        
        ObjectInfo object = m_memory.getObject("ball");
                
        if (object == null) {
            // If you don't know where is ball then find it
            turn(40);
            m_memory.waitForNewInfo();
        } else if (object.m_distance > 1.0) {
                    // If ball is too far then
            // turn to ball or 
            // if we have correct direction then go to ball
            ObjectInfo companheiro = m_memory.companheiroMaisProximo(team);
            
            if (object.m_direction != 0) {
                turn(object.m_direction);
            } else {
                if(companheiro != null){
                    PlayerInfo player = (PlayerInfo)companheiro;
                    if(matematica.distanciaEntre2Objetos(object, companheiro) > object.m_distance || player.isGoalie())
                        dash(100);
                }else{
                    dash(100);
                }
            }

        } else {
            // We know where is ball and we can kick it
            // so look for goal
            if (lado == 'l') {
                object = m_memory.getObject("goal r");
            } else {
                object = m_memory.getObject("goal l");
            }

            if (object == null) {
                turn(40);
                m_memory.waitForNewInfo();
            } else {
                kick(100, object.m_direction);
            }
        }

        // sleep one step to ensure that we will not send
        // two commands in one cycle.
    }
    
    @Override
    public void pontapeInicial(){
        if(numero == 3){
            Memory memoria = brain.getM_memory();
            char ladoRival;
            ObjectInfo bola = memoria.getObject("ball");
            
            if(lado == 'l')
                ladoRival = 'r';
            else
                ladoRival = 'l';
            
            ObjectInfo golRival = memoria.getObject("goal " + ladoRival);
            
            if(bola != null){
                if(bola.m_distance < 1)
                    kick(100,golRival.m_direction);
                else if(bola.m_direction != 0)
                    turn(bola.m_direction);
                else
                    dash(60);
            }else{
                turn(45);
            }
        }
    }

}
