
interface SendCommand {
    
    void move(double x, double y);
    
    void turn(double moment);
    
    void turn_neck(double moment);
    
    void dash(double power);
    
    void kick(double power, double direction);
    
    void say(String message);
    
    void goalie_catch(double direction);
    
    void changeView(String angle, String quality);
    
    void estrategiaDeJogo();
    
    void pontapeInicial();
    
    void posicaoEmCampo();

    void bye();
}

interface SensorInput {
   
    public void see(VisualInfo info);
   
    public void hear(int time, int direction, String message);
   
    public void hear(int time, String message);
    
    public Memory getM_memory();
        
    public boolean isM_timeOver();
    
    public String getM_playMode();
    
    public Matematica getMatematica();
    
}
