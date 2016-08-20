
public class ObjectInfo {

    public String m_type;
    public float m_distance;
    public float m_direction;
    public float m_distChange;
    public float m_dirChange;

    public ObjectInfo(String type) {
        m_type = type;
    }

    public float getDistance() {
        return m_distance;
    }

    public float getDirection() {
        return m_direction;
    }

    public float getDistChange() {
        return m_distChange;
    }

    public float getDirChange() {
        return m_dirChange;
    }

    public String getType() {
        return m_type;
    }
}

class PlayerInfo extends ObjectInfo {

    String m_teamName = "";
    int m_uniformName = 0;        // recognise 0 as not being able to see number
    float m_bodyDir;
    float m_headDir;
    boolean m_goalie = false;
    
    public PlayerInfo() {
        super("player");
    }

    public PlayerInfo(String team, int number, boolean is_goalie) {
        super("player");
        m_teamName = team;
        m_uniformName = number;
        m_goalie = is_goalie;
        m_bodyDir = 0;
        m_headDir = 0;
    }

    public PlayerInfo(String team, int number, float bodyDir, float headDir) {
        super("player");
        m_teamName = team;
        m_uniformName = number;
        m_bodyDir = bodyDir;
        m_headDir = headDir;
    }

    public String getTeamName() {
        return m_teamName;
    }

    public void setGoalie(boolean goalie) {
        m_goalie = goalie;
    }

    public boolean isGoalie() {
        return m_goalie;
    }

    public int getTeamNumber() {
        return m_uniformName;
    }
}

class GoalInfo extends ObjectInfo {

    private char m_side;
  
    public GoalInfo() {
        super("goal");
        m_side = ' ';
    }

    public GoalInfo(char side) {
        super("goal " + side);
        m_side = side;
    }

    public char getSide() {
        return m_side;
    }
}

//***************************************************************************
//
//	This class holds visual information about ball
//
//***************************************************************************
class BallInfo extends ObjectInfo {
  //===========================================================================
    // Initialization member functions
    public BallInfo() {
        super("ball");
    }
}

class FlagInfo extends ObjectInfo {

    char m_type;  // p|g
    char m_pos1;  // t|b|l|c|r
    char m_pos2;  // l|r|t|c|b
    int m_num;    // 0|10|20|30|40|50
    boolean m_out;

 
    public FlagInfo() {
        super("flag");
        m_type = ' ';
        m_pos1 = ' ';
        m_pos2 = ' ';
        m_num = 0;
        m_out = false;
    }

    public FlagInfo(String flagType, char type, char pos1, char pos2,
            int num, boolean out) {
        super(flagType);
        m_type = type;
        m_pos1 = pos1;
        m_pos2 = pos2;
        m_num = num;
        m_out = out;
    }

    public FlagInfo(char type, char pos1, char pos2, int num, boolean out) {
        super("flag");
        m_type = type;
        m_pos1 = pos1;
        m_pos2 = pos2;
        m_num = num;
        m_out = out;
    }
}

class LineInfo extends ObjectInfo {

    char m_kind;  
    
    public LineInfo() {
        super("line");
    }

    public LineInfo(char kind) {
        super("line");
        m_kind = kind;
    }
}
