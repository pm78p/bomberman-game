import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class BoosterIncreaseBomb extends Stuff {

    public BoosterIncreaseBomb() {

        try {
            this.setImage(ImageIO.read(new File(thispath + "/src/pictures/BoosterIncreaseBomb.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
