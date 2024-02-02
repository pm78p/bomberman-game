import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DisBoosterDecreaseRadius extends Stuff {

    public DisBoosterDecreaseRadius() {

        try {
            this.setImage(ImageIO.read(new File(thispath + "/src/pictures/DisBoosterDecreaseRadius.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
