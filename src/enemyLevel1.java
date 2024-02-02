import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class enemyLevel1 extends enemy {

    public enemyLevel1() {

        this.setSpeed(1500);
        this.setLvl(1);
        try {
            this.setImage( ImageIO.read(new File(thispath + "/src/pictures/enemylvl1.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public enemyLevel1(double id) {

        this.setId(id);
        this.setSpeed(1500);
        this.setLvl(1);
        try {
            this.setImage( ImageIO.read(new File(thispath + "/src/pictures/enemylvl1.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean checkPiece2move(Piece piece, int tool, int arz) {
        if (piece.getxInBoard() >= 0 && piece.getyInBoard() >= 0 && piece.getxInBoard() < tool && piece.getyInBoard() < arz && piece.isEmptyforEnemy())
            return true;
        return false;
    }

    @Override
    public void Move(Bomberman bomberman, int tool, int arz, Chornometr chornometr, Piece[][] pieces, int elapsedTime) {
        int k = (int) (chornometr.getNow() / 1500);
        if (chornometr.getNow() - k * 1500 <= elapsedTime)
            this.idiotmoving(tool, arz, chornometr, pieces);
    }

}
