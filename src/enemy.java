import org.omg.CORBA.MARSHAL;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public abstract class enemy extends Stuff implements Serializable {

    private double id;
    private boolean enemybool = true;
    private int enemyMovecounter = 0;
    private int lvl;
    private long lastTimeMove;
    private int speed;

    public enemy() {
        this.id = Math.random();
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public long getLastTimeMove() {
        return lastTimeMove;
    }

    public void setLastTimeMove(long lastTimeMove) {
        this.lastTimeMove = lastTimeMove;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public double getId() {
        return id;
    }

    public void setId(double id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.id + " " + this.lvl + " " + this.getX() + " " + this.getY() + " " + this.getxInBoard() + " " + this.getyInBoard();
    }

    public abstract boolean checkPiece2move(Piece piece, int tool, int arz);

    public abstract void Move(Bomberman bomberman, int tool, int arz, Chornometr chornometr, Piece[][] pieces, int elapsedTime);

    public void idiotmoving(int tool, int arz, Chornometr chornometr, Piece[][] pieces) {

        ArrayList<Piece> tmppiece = new ArrayList<>();

        if (this.getyInBoard() - 1 >= 0 && this.checkPiece2move(pieces[this.getyInBoard() - 1][this.getxInBoard()], tool, arz))
            tmppiece.add(pieces[this.getyInBoard() - 1][this.getxInBoard()]);

        if (this.getxInBoard() + 1 < tool && this.checkPiece2move(pieces[this.getyInBoard()][this.getxInBoard() + 1], tool, arz))
            tmppiece.add(pieces[this.getyInBoard()][this.getxInBoard() + 1]);

        if (this.getyInBoard() + 1 < arz && this.checkPiece2move(pieces[this.getyInBoard() + 1][this.getxInBoard()], tool, arz))
            tmppiece.add(pieces[this.getyInBoard() + 1][this.getxInBoard()]);

        if (this.getxInBoard() - 1 >= 0 && this.checkPiece2move(pieces[this.getyInBoard()][this.getxInBoard() - 1], tool, arz))
            tmppiece.add(pieces[this.getyInBoard()][this.getxInBoard() - 1]);

        if (tmppiece.size() != 0 && chornometr.getNow() - this.getLastTimeMove() > this.getSpeed() - 200) {

            pieces[this.getyInBoard()][this.getxInBoard()].setHasenemy(false);
            int k = ((int) (Math.random() * Math.pow(10, 5))) % tmppiece.size();
            this.setLastTimeMove(chornometr.getNow());
            this.setX(tmppiece.get(k).getX());
            this.setY(tmppiece.get(k).getY());
            this.setxInBoard(tmppiece.get(k).getxInBoard());
            this.setyInBoard(tmppiece.get(k).getyInBoard());
            pieces[this.getyInBoard()][this.getxInBoard()].setHasenemy(true);

        }

    }

    public void move2bomberman(Bomberman bomberman, int tool, int arz, Chornometr chornometr, Piece[][] pieces) {

        ArrayList<Piece> tmppiece = new ArrayList<>();

        if (enemybool) {

            if (this.getyInBoard() - 1 >= 0 && this.checkPiece2move(pieces[this.getyInBoard() - 1][this.getxInBoard()], tool, arz))
                if (Math.abs(this.getyInBoard() - 1 - bomberman.getyInBoard()) < Math.abs(this.getyInBoard() - bomberman.getyInBoard()))
                    tmppiece.add(pieces[this.getyInBoard() - 1][this.getxInBoard()]);

            if (this.getxInBoard() + 1 < tool && this.checkPiece2move(pieces[this.getyInBoard()][this.getxInBoard() + 1], tool, arz))
                if (Math.abs(this.getxInBoard() + 1 - bomberman.getxInBoard()) < Math.abs(this.getxInBoard() - bomberman.getxInBoard()))
                    tmppiece.add(pieces[this.getyInBoard()][this.getxInBoard() + 1]);

            if (this.getyInBoard() + 1 < arz && this.checkPiece2move(pieces[this.getyInBoard() + 1][this.getxInBoard()], tool, arz))
                if (Math.abs(this.getyInBoard() + 1 - bomberman.getyInBoard()) < Math.abs(this.getyInBoard() - bomberman.getyInBoard()))
                    tmppiece.add(pieces[this.getyInBoard() + 1][this.getxInBoard()]);

            if (this.getxInBoard() - 1 >= 0 && this.checkPiece2move(pieces[this.getyInBoard()][this.getxInBoard() - 1], tool, arz))
                if (Math.abs(this.getxInBoard() - 1 - bomberman.getxInBoard()) < Math.abs(this.getxInBoard() - bomberman.getxInBoard()))
                    tmppiece.add(pieces[this.getyInBoard()][this.getxInBoard() - 1]);
        }

        if (chornometr.getNow() - this.getLastTimeMove() > this.getSpeed() - 100) {
            if (tmppiece.size() != 0) {

                pieces[this.getyInBoard()][this.getxInBoard()].setHasenemy(false);
                int k = ((int) (Math.random() * Math.pow(10, 5))) % tmppiece.size();
                this.setLastTimeMove(chornometr.getNow());
                this.setX(tmppiece.get(k).getX());
                this.setY(tmppiece.get(k).getY());
                this.setxInBoard(tmppiece.get(k).getxInBoard());
                this.setyInBoard(tmppiece.get(k).getyInBoard());
                pieces[this.getyInBoard()][this.getxInBoard()].setHasenemy(true);

            } else if (enemyMovecounter != 10) {
                enemyMovecounter += 1;
                enemybool = false;
                idiotmoving(tool, arz, chornometr, pieces);
            } else {
                enemyMovecounter = 0;
                enemybool = true;
                move2bomberman(bomberman, tool, arz, chornometr, pieces);
            }
        }

    }

}
