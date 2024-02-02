import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DisBoosterDecreaseBomb extends Stuff {

    public DisBoosterDecreaseBomb() {

        try {
            this.setImage(ImageIO.read(new File(thispath + "/src/pictures/DisBoosterDecreaseBomb.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
