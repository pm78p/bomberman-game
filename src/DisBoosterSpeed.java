import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DisBoosterSpeed extends Stuff {

    public DisBoosterSpeed() {

        try {
            this.setImage(ImageIO.read(new File(thispath + "/src/pictures/DisBoosterSpeed.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
