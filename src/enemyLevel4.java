import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class enemyLevel4 extends enemy {

    public enemyLevel4() {

        this.setSpeed(750);
        this.setLvl(4);
        try {
            this.setImage(ImageIO.read(new File(thispath + "/src/pictures/enemylvl4.png"))); // should change
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public enemyLevel4(double id) {

        this.setId(id);
        this.setSpeed(750);
        this.setLvl(4);
        try {
            this.setImage(ImageIO.read(new File(thispath + "/src/pictures/enemylvl4.png"))); // should change
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean checkPiece2move(Piece piece, int tool, int arz) {
        if (piece.getxInBoard() >= 0 && piece.getyInBoard() >= 0 && piece.getxInBoard() < tool && piece.getyInBoard() < arz && !piece.isHasbomb())
            return true;
        return false;
    }

    @Override
    public void Move(Bomberman bomberman, int tool, int arz, Chornometr chornometr, Piece[][] pieces, int elapsedTime) {
        int k = (int) (chornometr.getNow() / 750);
        if (chornometr.getNow() - k * 750 <= elapsedTime)
            this.move2bomberman(bomberman, tool, arz, chornometr, pieces);
    }

}
