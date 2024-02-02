import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Bomberman extends Stuff implements Serializable {

    private int speed = 300;

    private long last_move = 0;

    private boolean ghostMode = false ;

    private int bombradius = 1 ;
    private int bomblimit = 1 ;
    private boolean ControlBomb = false ;
    private int point = 0 ;
    private double id ;
    private boolean alive = true ;

    public Bomberman() {

        id = Math.random() ;
        try {
            this.setImage(ImageIO.read(new File(thispath + "/src/pictures/Bomberman.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "( " + getxInBoard() + " , " + getyInBoard() + " )";
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public void setLast_move(long last_move) {
        this.last_move = last_move;
    }

    public boolean isGhostMode() {
        return ghostMode;
    }

    public void setGhostMode(boolean ghostMode) {
        this.ghostMode = ghostMode;
    }

    public boolean permision2move(long thistime, int elapsTime) {

        if (last_move == 0)
            return true;
        else if (thistime - last_move - speed > elapsTime)
            return true;
        return false;

    }

    public int getBombradius() {
        return bombradius;
    }

    public void setBombradius(int bombradius) {
        this.bombradius = bombradius;
    }

    public int getBomblimit() {
        return bomblimit;
    }

    public void setBomblimit(int bomblimit) {
        this.bomblimit = bomblimit;
    }

    public boolean isControlBomb() {
        return ControlBomb;
    }

    public void setControlBomb(boolean controlBomb) {
        ControlBomb = controlBomb;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void makebombermanalive(){
        this.alive = true;
        speed = 300;
        ghostMode = false ;
        bombradius = 1 ;
        bomblimit = 1 ;
        ControlBomb = false ;
        int point = 0 ;
    }
}
