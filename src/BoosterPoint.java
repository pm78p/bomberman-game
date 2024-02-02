import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class BoosterPoint extends Stuff {

    public BoosterPoint() {

        try {
            this.setImage(ImageIO.read(new File(thispath + "/src/pictures/boostPoint.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
