import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class DisBoosterPoint extends Stuff {

    public DisBoosterPoint() {

        try {
            this.setImage(ImageIO.read(new File(thispath + "/src/pictures/disboostPoint.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
