import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class BoosterSpeed extends Stuff {

    public BoosterSpeed() {

        try {
            this.setImage(ImageIO.read(new File(thispath + "/src/pictures/speedbooster.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
