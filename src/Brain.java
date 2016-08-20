
public class Brain extends Thread implements SensorInput {

    private SendCommand m_krislet;			// robot which is controled by this brain
    private Memory m_memory;				// place where all information is stored
    private char m_side;
    volatile private boolean m_timeOver;
    private String m_playMode;
    private int m_number;
    private String m_team;
    private boolean posicionado = false;
    private Matematica matematica = new Matematica();

    public Brain(SendCommand krislet,
            String team,
            char side,
            int number,
            String playMode) {
        m_timeOver = false;
        m_krislet = krislet;
        m_memory = new Memory();
        m_team = team;
        m_side = side;
        m_number = number;
        m_playMode = playMode;
        start();
    }

    @Override
    public void run() {
        while (!m_timeOver) {
            if ("before_kick_off".equals(m_playMode)) {
                if (!posicionado) {
                    m_krislet.posicaoEmCampo();
                    posicionado = true;
                }
            } else if ("play_on".equals(m_playMode) || getM_playMode().contains("free_kick_")) {
                posicionado = false;
                m_krislet.estrategiaDeJogo();
            } else if (("kick_off_l".equals(m_playMode) && m_side == 'l') || ("kick_off_r".equals(m_playMode) && m_side == 'r')) {
                m_krislet.pontapeInicial();
            } else if ("goal_l".equals(m_playMode) || "goal_r".equals(m_playMode)) {
                m_krislet.posicaoEmCampo();
            }else{
                m_krislet.posicaoEmCampo();
            }
            try {
                Thread.sleep(2 * SoccerParams.simulator_step);
            } catch (InterruptedException e) {
            }
        }
        m_krislet.bye();
    }

    @Override
    public void see(VisualInfo info) {
        m_memory.store(info);
    }

    @Override
    public void hear(int time, int direction, String message) {
    }

    @Override
    public void hear(int time, String message) {
        m_playMode = message;
        if (message.compareTo("time_over") == 0) {
            m_timeOver = true;
        }
    }
    
    /*GETTERS*/
    
    @Override
    public Memory getM_memory() {
        return m_memory;
    }

    public char getM_side() {
        return m_side;
    }

    @Override
    public boolean isM_timeOver() {
        return m_timeOver;
    }

    @Override
    public String getM_playMode() {
        return m_playMode;
    }

    public int getM_number() {
        return m_number;
    }

    public String getM_team() {
        return m_team;
    }
    
    public Matematica getMatematica() {
        return matematica;
    }
}
