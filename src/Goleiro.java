
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Goleiro extends Jogador {

    private boolean reposicionado = false;
    private boolean pertoDaFlag = false;    //flag que indica se o goleiro está posicionado no centro do gol
    /*private boolean moveTop = true;
     private boolean moveBottom = false;
    
     private boolean _2movimentos = false;
     private boolean virando = false;*/
    private boolean viradoPraFrente = false;
    private ObjectInfo bolaMomentoInicial;
    private ObjectInfo bolaMomentoFinal;

    private double distanciaFlagCima = 7.01;
    private double distanciaFlagBaixo = 7.01;
    // private int tempoEspera = 0;
    private Memory m_memory;
    private Matematica matematica;

    public Goleiro(InetAddress host, int port, String team) throws SocketException {
        super(host, port, team);
    }

    @Override
    protected void init() {
        send("(init " + team + " (version 9)(goalie))");
    }

    @Override
    public void estrategiaDeJogo() {

        m_memory = brain.getM_memory();
        matematica = brain.getMatematica();

        ObjectInfo bola = m_memory.getObject("ball");
        ObjectInfo rival = m_memory.rivalMaisProximo(team);

        if (brain.getM_playMode().equals("free_kick_" + lado)) {
            if (!reposicionado) {
                posicionarNoGol();
            } else {
                cobrarTiroDeMeta();
            }
        } else {
            if (bola == null || bola.m_distance > 30) {
                posicionarNoGol();
            } else {
                if (bola.m_distance < 1) {
                    goalie_catch(bola.m_direction);
                } else if (bola.m_distance < 13 && rival == null) {
                    capturarBolaNaArea(bola);
                } else if (bola.m_distance < 13 && rival != null) {
                    if (matematica.distanciaEntre2Objetos(bola, rival) > bola.m_distance && (bola.m_distChange > -0.5) && (bola.m_distChange <= 0)) {
                        capturarBolaNaArea(bola);
                    } else {
                       turn(bola.m_direction);
                    }
                } else if (bola.m_distance >= 20 && bola.m_distance <= 30 && bola.m_distChange < 0) {
                    if (viradoPraFrente) {
                        cercarAnguloDeChute(bola);
                    } else {
                        virarFrente();
                    }
                }
            }
        }
    }

    private void capturarBolaNaArea(ObjectInfo bola) {
        while (!brain.getM_playMode().equals("free_kick_" + lado)) {
            if (bola == null) {
                pertoDaFlag = false;
                return;
            }
            if (bola.m_distance < 1.0) {
                goalie_catch(bola.m_direction);
            }
            if (Math.abs(bola.m_direction) < 5) {
                dash(100);
            } else {
                turn(bola.m_direction);
            }
            bola = m_memory.getObject("ball");
            try {
                Thread.sleep(2 * SoccerParams.simulator_step);
            } catch (InterruptedException ex) {
                Logger.getLogger(Goleiro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        pertoDaFlag = false;
        reposicionado = false;
    }

    private void cercarAnguloDeChute(ObjectInfo bolaInicial) {
        int cont = 0;
        double pX, pY;
        while (cont < 3) {
            if (cont == 0) {
                bolaMomentoInicial = bolaInicial;
                turn(bolaInicial.m_direction);
            } else if (cont == 2) {
                ObjectInfo bola = m_memory.getObject("ball");

                if (bola == null || bola.m_direction == 0) {
                    return;
                }

                bolaMomentoFinal = bola;
                turn(bola.m_direction);
                double distancia = matematica.distanciaEntre2Objetos(bolaMomentoInicial, bolaMomentoFinal);

                if ((bolaMomentoInicial.m_direction < 0 && bolaMomentoFinal.m_direction < 0) || (bolaMomentoInicial.m_direction > 0 && bolaMomentoFinal.m_direction > 0)) {
                    pX = matematica.getX(bolaMomentoInicial.m_distance, -1 * bolaMomentoInicial.m_direction);
                    pY = matematica.getY(bolaMomentoInicial.m_distance, -1 * bolaMomentoInicial.m_direction);
                } else {
                    pX = matematica.getX(bolaMomentoInicial.m_distance, bolaMomentoInicial.m_direction);
                    pY = matematica.getY(bolaMomentoInicial.m_distance, bolaMomentoInicial.m_direction);
                }
                double aresta, anguloA, anguloB;
                anguloA = matematica.calcularAngulos(bolaMomentoFinal.m_distance, bolaMomentoInicial.m_distance, distancia);
                if (bolaMomentoFinal.m_direction < 0) {
                    aresta = matematica.distanciaEntre2Pontos(pX, pY, distanciaFlagCima, 0);
                    BigDecimal bd = new BigDecimal(aresta).setScale(3, RoundingMode.HALF_EVEN);
                    aresta = bd.doubleValue();
                    anguloB = matematica.calcularAngulos(distanciaFlagCima, aresta, bolaMomentoInicial.m_distance);

                } else {
                    aresta = matematica.distanciaEntre2Pontos(pX, pY, distanciaFlagBaixo, 0);
                    BigDecimal bd = new BigDecimal(aresta).setScale(3, RoundingMode.HALF_EVEN);
                    aresta = bd.doubleValue();
                    anguloB = matematica.calcularAngulos(distanciaFlagBaixo, aresta, bolaMomentoInicial.m_distance);
                }
                anguloB = matematica.radianoToGraus(anguloB);
                anguloA = matematica.radianoToGraus(anguloA);
                System.out.println("graus A = " + anguloA);
                System.out.println("graus B = " + anguloB);
                if (anguloA <= anguloB) {
                    posicionarDirecaoBola(bolaMomentoFinal.m_direction, anguloA, anguloB);
                } else {
                    System.out.println("NÃO VAI PRO GOL");
                }
            }
            cont++;
            try {
                Thread.sleep(2 * SoccerParams.simulator_step);
            } catch (InterruptedException ex) {
                Logger.getLogger(Goleiro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       //  viradoPraFrente = false;
       // pertoDaFlag = false;
    }

    private void posicionarDirecaoBola(double anguloFinal, double anguloA, double anguloB) {
        boolean pos = false;
        double distFlag = (anguloFinal < 0) ? distanciaFlagBaixo : distanciaFlagCima;
        double distancia = matematica.regraDeTres(anguloB, distFlag, anguloA);
        distancia = (distFlag - distancia) + 2;
        System.out.println("distancia flag inicial = " + distFlag);
        ObjectInfo flagAlvo;

        while (!pos) {
            if (anguloFinal > 0 && lado == 'r' || anguloFinal < 0 && lado == 'l') {
                flagAlvo = m_memory.getObject("flag g " + lado + " t");
            } else {
                flagAlvo = m_memory.getObject("flag g " + lado + " b");
            }

            if (flagAlvo == null) {
                if (lado == 'r' && anguloFinal >= 0 || lado == 'l' && anguloFinal > 0) {
                    turn(45);
                } else {
                    turn(-45);
                }
            } else if (flagAlvo.m_direction != 0) {
                turn(flagAlvo.m_direction);
            } else {
                while (flagAlvo.m_distance > distancia) {

                    System.out.println(flagAlvo.m_type);

                    if (anguloFinal > 0 && lado == 'r' || anguloFinal < 0 && lado == 'l') {
                        flagAlvo = m_memory.getObject("flag g " + lado + " t");
                    } else {
                        flagAlvo = m_memory.getObject("flag g " + lado + " b");
                    }
                    
                    if (flagAlvo != null) {
                        dash(10 * flagAlvo.m_distance);
                        System.out.println("flag dist = " + flagAlvo.m_distance + " distancia = " + distancia);
                    }
                    try {
                        Thread.sleep(2 * SoccerParams.simulator_step);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Goleiro.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                pos = true;
            }
            try {
                Thread.sleep(2 * SoccerParams.simulator_step);
            } catch (InterruptedException ex) {
                Logger.getLogger(Goleiro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        //reposicionado = false;
        //pertoDaFlag = false;
        viradoPraFrente = false;
        virarFrente();
    }

    private void virarFrente() {
        System.out.println("Virando pra frente");
        while (!viradoPraFrente) {
            ObjectInfo flagGolBaixo = m_memory.getObject("flag g " + lado + " t");
            try {
                if (flagGolBaixo != null && flagGolBaixo.m_direction != 0) {
                    turn(flagGolBaixo.m_direction);
                } else if (flagGolBaixo != null && flagGolBaixo.m_direction == 0) {
                    if (lado == 'r') {
                        turn(-95);
                    } else {
                        turn(95);
                    }
                    distanciaFlagCima = flagGolBaixo.m_distance;
                    distanciaFlagBaixo = 14.02 - distanciaFlagCima;
                    viradoPraFrente = true;
                } else {
                    if (lado == 'r') {
                        turn(45);
                    } else {
                        turn(-45);
                    }
                }
                Thread.sleep(2 * SoccerParams.simulator_step);
            } catch (InterruptedException ex) {
                Logger.getLogger(Goleiro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void posicionarNoGol() {
        while (!reposicionado) {
            ObjectInfo linhaFundo = m_memory.getObject("goal " + lado);
            ObjectInfo flagAreaC = m_memory.getObject("flag p " + lado + " c");

            if (linhaFundo == null && !pertoDaFlag) {
                turn(45);
            } else if (linhaFundo != null && linhaFundo.m_direction != 0 && !pertoDaFlag) {
                turn(linhaFundo.m_direction);
            } else if (linhaFundo != null && linhaFundo.m_distance > 2) {
                dash(100);
            } else if (flagAreaC == null) {
                pertoDaFlag = true;
                turn(45);
            } else {
                turn(flagAreaC.m_direction);
                //repos = true;
                reposicionado = true;
            }
            try {
                Thread.sleep(2 * SoccerParams.simulator_step);
            } catch (InterruptedException ex) {
                Logger.getLogger(Goleiro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        distanciaFlagBaixo = 7.01;
        distanciaFlagCima = 7.01;
    }

    private void cobrarTiroDeMeta() {
        boolean isMarcado;
        pertoDaFlag = false;
        reposicionado = false;
        for (int i = 0; i < 2; i++) {
            try {
                if (i == 0) {
                    move(-40, 15);
                }
                if (i == 1) {
                    move(-40, -15);
                }
                Thread.sleep(SoccerParams.simulator_step);
                ArrayList<ObjectInfo> companheiros = m_memory.companheirosNaVisao(team);
                ArrayList<ObjectInfo> rivais = m_memory.rivaisNaVisao(team);
                ArrayList<Double> distancias = new ArrayList<>();
                for (ObjectInfo companheiro : companheiros) {
                    isMarcado = false;
                    double distancia;
                    double menorDistancia = 1000;
                    for (ObjectInfo rival : rivais) {
                        distancia = matematica.distanciaEntre2Objetos(companheiro, rival);
                        if (distancia <= MARCACAO) {
                            if (distancia < menorDistancia) {
                                menorDistancia = distancia;
                            }
                            isMarcado = true;
                            break;
                        }
                    }
                    if (!isMarcado) {
                        kick(80, companheiro.m_direction);
                        return;
                    } else {
                        distancias.add(menorDistancia);
                    }

                }
                if (i == 1) {
                    double maiorDistancia = 0;
                    int index = -1;
                    for (int j = 0; j < distancias.size(); j++) {
                        if (maiorDistancia < distancias.get(j)) {
                            maiorDistancia = distancias.get(j);
                            index = j;
                        }
                    }
                    if (index != -1) {
                        ObjectInfo companheiroMenosMarcado = companheiros.get(index);
                        kick(100, companheiroMenosMarcado.m_direction);
                    } else {
                        kick(100, 0);
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Goleiro.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String a[]) throws SocketException, IOException {
        String hostName = "";
        int port = 6000;
        String team = "Krislet3";

        try {
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
            System.err.println("");
            System.err.println("    Example:");
            System.err.println("      krislet -host www.host.com -port 6000 -team Poland");
            System.err.println("    or");
            System.err.println("      krislet -host 193.117.005.223");
            return;
        }

        Jogador jogador = new Goleiro((InetAddress.getByName(hostName)), port, team);
        jogador.mainLoop();
    }

    @Override
    public void posicaoEmCampo() {
        reposicionado = false;
        pertoDaFlag = false;
        move(-50.0, 0.0);
    }

    @Override
    public void pontapeInicial() {

    }

}
