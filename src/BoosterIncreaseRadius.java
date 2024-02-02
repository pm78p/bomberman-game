import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class BoosterIncreaseRadius extends Stuff {

    public BoosterIncreaseRadius() {

        try {
            this.setImage( ImageIO.read(new File(thispath + "/src/pictures/BoosterIncreaseRadius.png")) );
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
