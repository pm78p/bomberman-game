import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class BoosterGhost extends Stuff{

    public BoosterGhost() {

        try {
            this.setImage(ImageIO.read(new File(thispath + "/src/pictures/GhostBoost.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
