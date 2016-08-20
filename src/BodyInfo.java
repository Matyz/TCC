/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Valderlei
 */
public class BodyInfo {
    
    private String message;
    private Body body;
    
    public BodyInfo(String info){
        info.trim();
        message = info;
        body = new Body();
    }
    
    
    class Body{
        private String viewMode;
        private double stamina;
        private double amountOfSpeed;
        private double directionOfSpeed;
        private double headDirection;
        private int kickCount;
        private int dashCount;
        private int turnCount;
        private int sayCount;
        private int turn_neckCount;
        private int catchCount;
        private int moveCount;
        private int change_viewCount;

        public String getViewMode() {
            return viewMode;
        }

        public void setViewMode(String viewMode) {
            this.viewMode = viewMode;
        }

        public double getStamina() {
            return stamina;
        }

        public void setStamina(double stamina) {
            this.stamina = stamina;
        }

        public double getAmountOfSpeed() {
            return amountOfSpeed;
        }

        public void setAmountOfSpeed(double amountOfSpeed) {
            this.amountOfSpeed = amountOfSpeed;
        }

        public double getDirectionOfSpeed() {
            return directionOfSpeed;
        }

        public void setDirectionOfSpeed(double directionOfSpeed) {
            this.directionOfSpeed = directionOfSpeed;
        }

        public double getHeadDirection() {
            return headDirection;
        }

        public void setHeadDirection(double headDirection) {
            this.headDirection = headDirection;
        }

        public int getKickCount() {
            return kickCount;
        }

        public void setKickCount(int kickCount) {
            this.kickCount = kickCount;
        }

        public int getDashCount() {
            return dashCount;
        }

        public void setDashCount(int dashCount) {
            this.dashCount = dashCount;
        }

        public int getTurnCount() {
            return turnCount;
        }

        public void setTurnCount(int turnCount) {
            this.turnCount = turnCount;
        }

        public int getSayCount() {
            return sayCount;
        }

        public void setSayCount(int sayCount) {
            this.sayCount = sayCount;
        }

        public int getTurn_neckCount() {
            return turn_neckCount;
        }

        public void setTurn_neckCount(int turn_neckCount) {
            this.turn_neckCount = turn_neckCount;
        }

        public int getCatchCount() {
            return catchCount;
        }

        public void setCatchCount(int catchCount) {
            this.catchCount = catchCount;
        }

        public int getMoveCount() {
            return moveCount;
        }

        public void setMoveCount(int moveCount) {
            this.moveCount = moveCount;
        }

        public int getChange_viewCount() {
            return change_viewCount;
        }

        public void setChange_viewCount(int change_viewCount) {
            this.change_viewCount = change_viewCount;
        }
        
        
    }
    
}
