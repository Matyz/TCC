import java.util.ArrayList;


public class Memory {

    volatile private VisualInfo m_info;
    volatile private BodyInfo m_binfo;
    final static int SIMULATOR_STEP = 100;

    public void store(VisualInfo info) {
        m_info = info;
    }

    public ObjectInfo getObject(String name) {
        if (m_info == null) {
            waitForNewInfo();
        }

        for (int c = 0; c < m_info.objetos.size(); c++) {
            ObjectInfo object = (ObjectInfo) m_info.objetos.get(c);
            //System.out.println(object.m_type);
            if (object.m_type.compareTo(name) == 0) {
                return object;
            }
        }

        return null;
    }
    
    public void printarTodosObjetos(String name){
        if (m_info == null) {
            waitForNewInfo();
        }

        for (int c = 0; c < m_info.objetos.size(); c++) {
            ObjectInfo object = (ObjectInfo) m_info.objetos.get(c);
            if (object.m_type.contains(name)) {
                System.out.println(object.m_type);
            }
            
        }
    }

    public void waitForNewInfo() {
        m_info = null;
        while (m_info == null) {
            try {
                Thread.sleep(SIMULATOR_STEP);
            } catch (Exception e) {
            }
        }
    }

    public ObjectInfo rivalMaisProximo(String meuTime) {
        ObjectInfo retorno = null;
        double dist = Double.MAX_VALUE;

        for (ObjectInfo objeto : m_info.objetos) {
            if (objeto.getType().equals("player")) {
                PlayerInfo player = (PlayerInfo) objeto;
                if (!player.m_teamName.equals(meuTime) && player.m_distance < dist) {
                    retorno = player;
                    dist = player.m_distance;
                }
            }
        }
        return retorno;
    }
    
    public ObjectInfo companheiroMaisProximo(String meuTime) {
        ObjectInfo retorno = null;
        double dist = Double.MAX_VALUE;

        for (ObjectInfo objeto : m_info.objetos) {
            if (objeto.getType().equals("player")) {
                PlayerInfo player = (PlayerInfo) objeto;
                if (player.m_teamName.equals(meuTime) && player.m_distance < dist) {
                    retorno = player;
                    dist = player.m_distance;
                }
            }
        }
        return retorno;
    }
    
    public ArrayList<ObjectInfo> rivaisNaVisao(String meuTime){
        ArrayList<ObjectInfo> rivais = new ArrayList<>();               
                
        for (ObjectInfo objeto : m_info.objetos) {
            if (objeto.getType().equals("player")) {
                PlayerInfo player = (PlayerInfo) objeto;
                if (!player.m_teamName.equals(meuTime))
                    rivais.add(player);
            }
        }
        return rivais;
    }
    
    public ArrayList<ObjectInfo> companheirosNaVisao(String meuTime){
        ArrayList<ObjectInfo> companheiros = new ArrayList<>();               
                
        for (ObjectInfo objeto : m_info.objetos) {
            if (objeto.getType().equals("player")) {
                PlayerInfo player = (PlayerInfo) objeto;
                if (player.m_teamName.equals(meuTime))
                    companheiros.add(player);
            }
        }
        return companheiros;
    }
    
}
