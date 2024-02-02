import java.awt.image.BufferedImage;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class Stuff implements Serializable {

    private int x ;
    private int y ;

    private int xInBoard ;
    private int yInBoard ;

    private double killerId ;

    private transient BufferedImage image;
    transient Path currentRelativePath = Paths.get("");
    String thispath = currentRelativePath.toAbsolutePath().toString();

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

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public double getKillerId() {
        return killerId;
    }

    public void setKillerId(double killerId) {
        this.killerId = killerId;
    }
}
