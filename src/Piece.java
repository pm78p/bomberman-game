import java.awt.*;
import java.io.Serializable;

public class Piece implements Serializable {

    private int x ;
    private int y ;

    private int xInBoard;
    private int yInBoard;

    private boolean hasenemy = false ;
    private boolean hasbomberman = false ;
    private boolean hasbomb = false ;
    private boolean hasstone = false ;
    private boolean hasbreak = false ;

    private boolean hasbombboost = false ;
    private boolean hasdisbombboost = false ;
    private boolean increasebomb = false ;
    private boolean decreasebomb = false ;
    private boolean ControlBomb = false ;
    private boolean speedboost = false ;
    private boolean pointboost = false ;
    private boolean pointdisboost = false ;
    private boolean speeddisboost = false ;
    private boolean ghostboost = false ;

    private double breakbroker = -1 ;

    private Color color ;

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setxy(int x , int y){
        this.x = x ;
        this.y = y ;
    }

    public int getxInBoard() {
        return xInBoard;
    }

    public void setxInBoard(int xInBoard) {
        this.xInBoard = xInBoard;
    }

    public int getyInBoard() {
        return yInBoard;
    }

    public void setyInBoard(int yInBoard) {
        this.yInBoard = yInBoard;
    }

    public boolean isHasstone() {
        return hasstone;
    }

    public void setHasstone(boolean hasstone) {
        this.hasstone = hasstone;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public boolean isHasbomb() {
        return hasbomb;
    }

    public void setHasbomb(boolean hasbomb) {
        this.hasbomb = hasbomb;
    }

    public boolean isHasbreak() {
        return hasbreak;
    }

    public void setHasbreak(boolean hasbreak) {
        this.hasbreak = hasbreak;
    }

    public boolean isHasenemy() {
        return hasenemy;
    }

    public void setHasenemy(boolean hasenemy) {
        this.hasenemy = hasenemy;
    }

    public boolean isHasbomberman() {
        return hasbomberman;
    }

    public void setHasbomberman(boolean hasbomberman) {
        this.hasbomberman = hasbomberman;
    }

    public boolean isHasbombboost() {
        return hasbombboost;
    }

    public void setHasbombboost(boolean hasbombboost) {
        this.hasbombboost = hasbombboost;
    }

    public boolean isHasdisbombboost() {
        return hasdisbombboost;
    }

    public void setHasdisbombboost(boolean hasdisbombboost) {
        this.hasdisbombboost = hasdisbombboost;
    }

    public boolean isIncreasebomb() {
        return increasebomb;
    }

    public void setIncreasebomb(boolean increasebomb) {
        this.increasebomb = increasebomb;
    }

    public boolean isDecreasebomb() {
        return decreasebomb;
    }

    public void setDecreasebomb(boolean decreasebomb) {
        this.decreasebomb = decreasebomb;
    }

    public boolean isControlBomb() {
        return ControlBomb;
    }

    public void setControlBomb(boolean controlBomb) {
        ControlBomb = controlBomb;
    }

    public boolean isSpeedboost() {
        return speedboost;
    }

    public void setSpeedboost(boolean speedboost) {
        this.speedboost = speedboost;
    }

    public boolean isPointboost() {
        return pointboost;
    }

    public void setPointboost(boolean pointboost) {
        this.pointboost = pointboost;
    }

    public boolean isPointdisboost() {
        return pointdisboost;
    }

    public void setPointdisboost(boolean pointdisboost) {
        this.pointdisboost = pointdisboost;
    }

    public boolean isSpeeddisboost() {
        return speeddisboost;
    }

    public void setSpeeddisboost(boolean speeddisboost) {
        this.speeddisboost = speeddisboost;
    }

    public boolean isGhostboost() {
        return ghostboost;
    }

    public void setGhostboost(boolean ghostboost) {
        this.ghostboost = ghostboost;
    }

    public double getBreakbroker() {
        return breakbroker;
    }

    public void setBreakbroker(double breakbroker) {
        this.breakbroker = breakbroker;
    }

    public boolean isEmpty(){

        if ( !this.isControlBomb() && !this.isDecreasebomb() && !this.isIncreasebomb() && !this.isHasbombboost()
                && !this.isHasdisbombboost() && !this.isSpeedboost() && !this.isPointboost() && !this.isPointdisboost()
                && !this.isSpeeddisboost() && !this.ghostboost )
            if ( !this.isHasenemy() && !this.isHasbomberman() && !this.isHasbomb() && !this.isHasstone() && !this.isHasbreak() )
                return true ;

        return false ;
    }

    public boolean isEmptyforEnemy(){

        if ( !this.isControlBomb() && !this.isDecreasebomb() && !this.isIncreasebomb() && !this.isHasbombboost()
                && !this.isHasdisbombboost() && !this.isSpeedboost() && !this.isPointboost() && !this.isPointdisboost()
                && !this.isSpeeddisboost() && !this.ghostboost )
            if ( !this.isHasenemy() && !this.isHasbomb() && !this.isHasstone() && !this.isHasbreak() )
                return true ;

        return false ;
    }

    public void makeEmpty(){

        hasenemy = false ;
        hasbomberman = false ;
        hasbomb = false ;
        hasstone = false ;
        hasbreak = false ;
        hasbombboost = false ;
        hasdisbombboost = false ;
        increasebomb = false ;
        decreasebomb = false ;
        ControlBomb = false ;
        speedboost = false ;
        pointboost = false ;
        pointdisboost = false ;
        speeddisboost = false ;
        ghostboost = false ;

    }

}
