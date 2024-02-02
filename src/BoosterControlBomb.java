import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class BoosterControlBomb extends Stuff {

    public BoosterControlBomb() {

        try {
            this.setImage(ImageIO.read(new File(thispath + "/src/pictures/BoosterControlBomb.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
