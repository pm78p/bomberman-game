import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Bomb extends Stuff implements Serializable {

    private int BombRadius = 1 ;

    private long start_time ;

    private double ownerId ;

    public Bomb(int bombRadius){

        this.BombRadius = bombRadius ;

        try {
            this.setImage(ImageIO.read(new File(thispath + "/src/pictures/bomb.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public long getStart_time() {
        return start_time;
    }

    public void setStart_time(long start_time) {
        this.start_time = start_time;
    }

    public int getBombRadius() {
        return BombRadius;
    }

    public void setBombRadius(int bombRadius) {
        BombRadius = bombRadius;
    }

    public double getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(double ownerId) {
        this.ownerId = ownerId;
    }
}
