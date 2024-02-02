import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

class BoardPServer extends JPanel implements ActionListener {

    static final long serialVersionUID = 2L;
    GameInfo gameInfo = null;

    int arz = 15;
    int tool = 27;
    Piece[][] pieces = new Piece[arz][tool];
    int piec_size = 60;
    int start_x = 0;
    int start_y = 0;
    int GameLevel = 1;

    ArrayList<Bomberman> bombermen = new ArrayList<>();
    ArrayList<enemy> enemies = new ArrayList<>();
    ArrayList<Integer> enemyMovecounter = new ArrayList<>();
    ArrayList<Boolean> enemybool = new ArrayList<>();
    int enemyNumber = Math.min(tool, arz) - 2;
    double enemyWorthRate = enemyNumber;

    ArrayList<Piece> breakpiece = new ArrayList<>();
    ArrayList<Piece> breakpiece2server = new ArrayList<>();
    ArrayList<Piece> emptypiece = new ArrayList<>();
    ArrayList<Bomb> bomb = new ArrayList<>();
    ArrayList<BoosterIncreaseRadius> boosterIncreaseRadii = new ArrayList<>();
    ArrayList<DisBoosterDecreaseRadius> disBoosterDecreasRadii = new ArrayList<>();
    ArrayList<BoosterIncreaseBomb> boosterIncreaseBombs = new ArrayList<>();
    ArrayList<DisBoosterDecreaseBomb> disBoosterDecreaseBombs = new ArrayList<>();
    ArrayList<BoosterControlBomb> boosterControlBombs = new ArrayList<>();
    ArrayList<BoosterSpeed> boosterSpeeds = new ArrayList<>();
    ArrayList<BoosterPoint> boosterPoints = new ArrayList<>();
    ArrayList<DisBoosterPoint> disBoosterPoints = new ArrayList<>();
    ArrayList<DisBoosterSpeed> disBoosterSpeeds = new ArrayList<>();
    ArrayList<BoosterGhost> boosterGhosts = new ArrayList<>();

    public BufferedImage stoneimage;
    public BufferedImage breakimage;
    public BufferedImage openDoor;
    public BufferedImage closeDoor;
    public BufferedImage fireimage = null;
    boolean makeBoostCount = true;

    boolean bmb = false;

    Timer timer;
    private int delay = 10;

    int elapsedTime = 30;

    boolean play = false;

    String thispath = "";

    Chornometr chornometr;
    boolean counter = true; // for start_time in actionperfume

    Graphics g1;

    int enmycounter = 0;
    boolean stopButtun = false;
    boolean back2main = false;
    boolean exitthisgame = false;
    PrintStream printStream;

    boolean[] changelvl = {false, false};

    public BoardPServer(GameInfo gameInfo, int tool, int arz) {

        this.tool = tool;
        this.arz = arz;
        this.setBackground(Color.BLUE);

        Path currentRelativePath = Paths.get("");
        thispath = currentRelativePath.toAbsolutePath().toString();

        this.gameInfo = gameInfo;
        setFocusable(true);
        timer = new Timer(delay, this);
        timer.start();

        for (int i = 0; i < arz; i++) {
            for (int j = 0; j < tool; j++) {
                pieces[i][j] = new Piece();
            }
        }

        setstone();
        setbreak();

    }

    public void paint(Graphics g) {

        Thread thr = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    if (!gameInfo.stop && stopButtun) {
                        stopButtun = false;
                        chornometr.resume();
                        timer.start();
                        return;
                    }

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (gameInfo.stop && !stopButtun) {
            stopButtun = true;
            chornometr.stop();
            timer.stop();
            thr.start();
        }

        if (!counter)
            gameInfo.filler(0, chornometr.getNow(), GameLevel, 1 - bomb.size(), 1);

        for (int i = 0; i < bombermen.size(); i++) {
            Bomberman bomberman = bombermen.get(i);
            if (bomberman.getxInBoard() == tool - 1 && bomberman.getyInBoard() == arz - 2 && enemies.size() == 0)
                resetDetails2changLvl();
        }

        g1 = g;

        boardpaint(g); //paint pieces and stones and breaks

        boombermanpain(g);   //paint bomberman

        if (bmb == true)
            bombpaint(g);       //paint bombs

        if (makeBoostCount) {
            BoosterMake(g);
            makeBoostCount = false;
        }

        enemypaint(g);           // paint enemy

        BoostRadiuspaint(g, false, pieces[0][0]);

        DisBoostRadiuspaint(g, false, pieces[0][0]);

        BoostBombspaint(g, false, pieces[0][0]);

        DisBoostBombspaint(g, false, pieces[0][0]);

        BoostControlpaint(g, false, pieces[0][0]);

        BoostSpeedpaint(g, false, pieces[0][0]);

        BoostPointpaint(g, false, pieces[0][0]);

        DisBoostPointpaint(g, false, pieces[0][0]);

        DisBoostSpeedpaint(g, false, pieces[0][0]);

        BoostGhostpaint(g, false, pieces[0][0]);

        g.dispose();

    }

    public void boardpaint(Graphics g) {

        g.setColor(Color.blue);
        g.fillRect(0, 0, piec_size * 27, piec_size * 15);

        if (enemies.size() == 0 && play)
            pieces[arz - 2][tool - 1].setHasstone(false);

        try {
            breakimage = ImageIO.read(new File(thispath + "/src/pictures/break.png"));
            stoneimage = ImageIO.read(new File(thispath + "/src/pictures/stone.png"));
            openDoor = ImageIO.read(new File(thispath + "/src/pictures/dooropen.png"));
            closeDoor = ImageIO.read(new File(thispath + "/src/pictures/doorclose.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int x = start_x;
        int y = start_y;

        for (int i = 0; i < arz; i++) {
            for (int j = 0; j < tool; j++) {

                pieces[i][j].setxy(x, y);
                pieces[i][j].setxInBoard(j);
                pieces[i][j].setyInBoard(i);

                if (i == arz - 2 && j == tool - 1 && pieces[i][j].isHasstone())
                    g.drawImage(closeDoor, pieces[i][j].getX(), pieces[i][j].getY(), null);
                else if (i == arz - 2 && j == tool - 1)
                    g.drawImage(openDoor, pieces[i][j].getX(), pieces[i][j].getY(), null);
                else if (pieces[i][j].isHasstone())
                    g.drawImage(stoneimage, pieces[i][j].getX(), pieces[i][j].getY(), null);
                else if (pieces[i][j].isHasbreak()) {
                    g.drawImage(breakimage, pieces[i][j].getX(), pieces[i][j].getY(), null);
                } else {
                    g.setColor(Color.orange);
                    g.fillRect(pieces[i][j].getX(), pieces[i][j].getY(), piec_size, piec_size);
                }

                x += piec_size;

            }
            x = start_x;
            y += piec_size;
        }


    }

    public void boombermanpain(Graphics g) {

        for (int i = 0; i < bombermen.size(); i++) {
            Bomberman tmp = bombermen.get(i);
            if (tmp.isAlive()) {
                pieces[tmp.getyInBoard()][tmp.getxInBoard()].setHasbomberman(true);
                getbooster(tmp);
                if (pieces[tmp.getyInBoard()][tmp.getxInBoard()].isHasenemy())
                    tmp.setAlive(false);
                else
                    g.drawImage(tmp.getImage(), tmp.getX(), tmp.getY(), null);
            }
        }
    }

    public void enemypaint(Graphics g) {

        if (!play && enmycounter == 0) {
            enemyMaker();
            enmycounter++;
        } else {

            int k = (int) (chornometr.getNow() / 750);
            if (chornometr.getNow() - k * 750 <= elapsedTime) {
                enmycounter++;
                for (int i = 0; i < enemies.size(); i++) {
                    Bomberman nearestbomberman = nearestBomberman(enemies.get(i));
                    enemies.get(i).Move(nearestbomberman, tool, arz, chornometr, pieces, elapsedTime);
                }
            }
        }

        for (int i = 0; i < enemies.size(); i++) {

            if (enemies.get(i).getImage() == null) {
                enemy tmp = null;
                int k = enemies.get(i).getLvl();
                switch (k) {
                    case 1:
                        tmp = new enemyLevel1();
                        break;
                    case 2:
                        tmp = new enemyLevel2();
                        break;
                    case 3:
                        tmp = new enemyLevel3();
                        break;
                    case 4:
                        tmp = new enemyLevel4();
                        break;
                }
                enemies.get(i).setImage(tmp.getImage());
            }
            g.drawImage(enemies.get(i).getImage(), enemies.get(i).getX(), enemies.get(i).getY(), null);

        }
    }

    public void bombpaint(Graphics g) {

        for (int i = 0; i < bomb.size(); i++) {

            if (bomb.get(i).getImage() == null) {

                Bomb tmp = new Bomb(1);
                bomb.get(i).setImage(tmp.getImage());

            }

            if (chornometr.getNow() - bomb.get(i).getStart_time() <= 5000) {
                g.drawImage(bomb.get(i).getImage(), bomb.get(i).getX(), bomb.get(i).getY(), null);
                pieces[bomb.get(i).getyInBoard()][bomb.get(i).getxInBoard()].setHasbomb(true);
            } else {
                int k = findingbomberman(bomb.get(i).getOwnerId(), bombermen);
                if (k >= 0)
                    bombexpluding(bomb.get(i), bombermen.get(k));
            }
        }
    }

    public void BoostRadiuspaint(Graphics g, boolean tmp, Piece piece) {

        if (tmp) {
            BoosterIncreaseRadius boosterIncreaseRadius = new BoosterIncreaseRadius();
            boosterIncreaseRadius.setX(piece.getX());
            boosterIncreaseRadius.setY(piece.getY());

            boosterIncreaseRadius.setxInBoard(piece.getxInBoard());
            boosterIncreaseRadius.setyInBoard(piece.getyInBoard());
            boosterIncreaseRadii.add(boosterIncreaseRadius);
            pieces[boosterIncreaseRadius.getyInBoard()][boosterIncreaseRadius.getxInBoard()].setHasbombboost(true);
        }

        for (int i = 0; i < boosterIncreaseRadii.size(); i++) {

            if (boosterIncreaseRadii.get(i).getImage() == null) {

                BoosterIncreaseRadius tmpboost = new BoosterIncreaseRadius();
                boosterIncreaseRadii.get(i).setImage(tmpboost.getImage());

            }

            g.drawImage(boosterIncreaseRadii.get(i).getImage(), boosterIncreaseRadii.get(i).getX(), boosterIncreaseRadii.get(i).getY(), null);

        }

    }

    public void DisBoostRadiuspaint(Graphics g, boolean tmp, Piece piece) {

        if (tmp) {
            DisBoosterDecreaseRadius disBoosterDecreasRadius = new DisBoosterDecreaseRadius();
            disBoosterDecreasRadius.setX(piece.getX());
            disBoosterDecreasRadius.setY(piece.getY());

            disBoosterDecreasRadius.setxInBoard(piece.getxInBoard());
            disBoosterDecreasRadius.setyInBoard(piece.getyInBoard());
            disBoosterDecreasRadii.add(disBoosterDecreasRadius);
            pieces[disBoosterDecreasRadius.getyInBoard()][disBoosterDecreasRadius.getxInBoard()].setHasdisbombboost(true);
        }

        for (int i = 0; i < disBoosterDecreasRadii.size(); i++) {

            if (disBoosterDecreasRadii.get(i).getImage() == null) {

                DisBoosterDecreaseRadius tmpboost = new DisBoosterDecreaseRadius();
                disBoosterDecreasRadii.get(i).setImage(tmpboost.getImage());

            }

            g.drawImage(disBoosterDecreasRadii.get(i).getImage(), disBoosterDecreasRadii.get(i).getX(), disBoosterDecreasRadii.get(i).getY(), null);
        }

    }

    public void BoostBombspaint(Graphics g, boolean tmp, Piece piece) {

        if (tmp) {
            BoosterIncreaseBomb boosterIncreaseBomb = new BoosterIncreaseBomb();
            boosterIncreaseBomb.setX(piece.getX());
            boosterIncreaseBomb.setY(piece.getY());

            boosterIncreaseBomb.setxInBoard(piece.getxInBoard());
            boosterIncreaseBomb.setyInBoard(piece.getyInBoard());
            boosterIncreaseBombs.add(boosterIncreaseBomb);
            pieces[boosterIncreaseBomb.getyInBoard()][boosterIncreaseBomb.getxInBoard()].setIncreasebomb(true);
        }

        for (int i = 0; i < boosterIncreaseBombs.size(); i++) {

            if (boosterIncreaseBombs.get(i).getImage() == null) {

                BoosterIncreaseBomb tmpboost = new BoosterIncreaseBomb();
                boosterIncreaseBombs.get(i).setImage(tmpboost.getImage());

            }

            g.drawImage(boosterIncreaseBombs.get(i).getImage(), boosterIncreaseBombs.get(i).getX(), boosterIncreaseBombs.get(i).getY(), null);
        }

    }

    public void DisBoostBombspaint(Graphics g, boolean tmp, Piece piece) {

        if (tmp) {
            DisBoosterDecreaseBomb disBoosterDecreasBomb = new DisBoosterDecreaseBomb();
            disBoosterDecreasBomb.setX(piece.getX());
            disBoosterDecreasBomb.setY(piece.getY());

            disBoosterDecreasBomb.setxInBoard(piece.getxInBoard());
            disBoosterDecreasBomb.setyInBoard(piece.getyInBoard());
            disBoosterDecreaseBombs.add(disBoosterDecreasBomb);
            pieces[disBoosterDecreasBomb.getyInBoard()][disBoosterDecreasBomb.getxInBoard()].setDecreasebomb(true);
        }

        for (int i = 0; i < disBoosterDecreaseBombs.size(); i++) {

            if (disBoosterDecreaseBombs.get(i).getImage() == null) {

                DisBoosterDecreaseBomb tmpboost = new DisBoosterDecreaseBomb();
                disBoosterDecreaseBombs.get(i).setImage(tmpboost.getImage());

            }

            g.drawImage(disBoosterDecreaseBombs.get(i).getImage(), disBoosterDecreaseBombs.get(i).getX(), disBoosterDecreaseBombs.get(i).getY(), null);
        }

    }

    public void BoostControlpaint(Graphics g, boolean tmp, Piece piece) {

        if (tmp) {
            BoosterControlBomb boosterControlBomb = new BoosterControlBomb();
            boosterControlBomb.setX(piece.getX());
            boosterControlBomb.setY(piece.getY());

            boosterControlBomb.setxInBoard(piece.getxInBoard());
            boosterControlBomb.setyInBoard(piece.getyInBoard());
            boosterControlBombs.add(boosterControlBomb);
            pieces[boosterControlBomb.getyInBoard()][boosterControlBomb.getxInBoard()].setControlBomb(true);
        }

        for (int i = 0; i < boosterControlBombs.size(); i++) {

            if (boosterControlBombs.get(i).getImage() == null) {

                BoosterControlBomb tmpboost = new BoosterControlBomb();
                boosterControlBombs.get(i).setImage(tmpboost.getImage());

            }

            g.drawImage(boosterControlBombs.get(i).getImage(), boosterControlBombs.get(i).getX(), boosterControlBombs.get(i).getY(), null);
        }

    }

    public void BoostSpeedpaint(Graphics g, boolean tmp, Piece piece) {

        if (tmp) {
            BoosterSpeed boosterSpeed = new BoosterSpeed();
            boosterSpeed.setX(piece.getX());
            boosterSpeed.setY(piece.getY());

            boosterSpeed.setxInBoard(piece.getxInBoard());
            boosterSpeed.setyInBoard(piece.getyInBoard());
            boosterSpeeds.add(boosterSpeed);
            pieces[boosterSpeed.getyInBoard()][boosterSpeed.getxInBoard()].setSpeedboost(true);
        }

        for (int i = 0; i < boosterSpeeds.size(); i++) {

            if (boosterSpeeds.get(i).getImage() == null) {

                BoosterSpeed tmpboost = new BoosterSpeed();
                boosterSpeeds.get(i).setImage(tmpboost.getImage());

            }

            g.drawImage(boosterSpeeds.get(i).getImage(), boosterSpeeds.get(i).getX(), boosterSpeeds.get(i).getY(), null);
        }

    }

    public void BoostPointpaint(Graphics g, boolean tmp, Piece piece) {

        if (tmp) {
            BoosterPoint boosterPoint = new BoosterPoint();
            boosterPoint.setX(piece.getX());
            boosterPoint.setY(piece.getY());

            boosterPoint.setxInBoard(piece.getxInBoard());
            boosterPoint.setyInBoard(piece.getyInBoard());
            boosterPoints.add(boosterPoint);
            pieces[boosterPoint.getyInBoard()][boosterPoint.getxInBoard()].setPointboost(true);
        }

        for (int i = 0; i < boosterPoints.size(); i++) {

            if (boosterPoints.get(i).getImage() == null) {

                BoosterPoint tmpboost = new BoosterPoint();
                boosterPoints.get(i).setImage(tmpboost.getImage());

            }

            g.drawImage(boosterPoints.get(i).getImage(), boosterPoints.get(i).getX(), boosterPoints.get(i).getY(), null);
        }

    }

    public void DisBoostPointpaint(Graphics g, boolean tmp, Piece piece) {

        if (tmp) {
            DisBoosterPoint disBoosterPoint = new DisBoosterPoint();
            disBoosterPoint.setX(piece.getX());
            disBoosterPoint.setY(piece.getY());

            disBoosterPoint.setxInBoard(piece.getxInBoard());
            disBoosterPoint.setyInBoard(piece.getyInBoard());
            disBoosterPoints.add(disBoosterPoint);
            pieces[disBoosterPoint.getyInBoard()][disBoosterPoint.getxInBoard()].setPointdisboost(true);
        }

        for (int i = 0; i < disBoosterPoints.size(); i++) {

            if (disBoosterPoints.get(i).getImage() == null) {

                DisBoosterPoint tmpboost = new DisBoosterPoint();
                disBoosterPoints.get(i).setImage(tmpboost.getImage());

            }

            g.drawImage(disBoosterPoints.get(i).getImage(), disBoosterPoints.get(i).getX(), disBoosterPoints.get(i).getY(), null);
        }

    }

    public void DisBoostSpeedpaint(Graphics g, boolean tmp, Piece piece) {

        if (tmp) {
            DisBoosterSpeed disBoosterSpeed = new DisBoosterSpeed();
            disBoosterSpeed.setX(piece.getX());
            disBoosterSpeed.setY(piece.getY());

            disBoosterSpeed.setxInBoard(piece.getxInBoard());
            disBoosterSpeed.setyInBoard(piece.getyInBoard());
            disBoosterSpeeds.add(disBoosterSpeed);
            pieces[disBoosterSpeed.getyInBoard()][disBoosterSpeed.getxInBoard()].setSpeeddisboost(true);
        }

        for (int i = 0; i < disBoosterSpeeds.size(); i++) {

            if (disBoosterSpeeds.get(i).getImage() == null) {

                DisBoosterSpeed tmpboost = new DisBoosterSpeed();
                disBoosterSpeeds.get(i).setImage(tmpboost.getImage());

            }

            g.drawImage(disBoosterSpeeds.get(i).getImage(), disBoosterSpeeds.get(i).getX(), disBoosterSpeeds.get(i).getY(), null);
        }

    }

    public void BoostGhostpaint(Graphics g, boolean tmp, Piece piece) {

        if (tmp) {
            BoosterGhost boosterGhost = new BoosterGhost();
            boosterGhost.setX(piece.getX());
            boosterGhost.setY(piece.getY());

            boosterGhost.setxInBoard(piece.getxInBoard());
            boosterGhost.setyInBoard(piece.getyInBoard());
            boosterGhosts.add(boosterGhost);
            pieces[boosterGhost.getyInBoard()][boosterGhost.getxInBoard()].setGhostboost(true);
        }

        for (int i = 0; i < boosterGhosts.size(); i++) {

            if (boosterGhosts.get(i).getImage() == null) {

                BoosterGhost tmpboost = new BoosterGhost();
                boosterGhosts.get(i).setImage(tmpboost.getImage());

            }

            g.drawImage(boosterGhosts.get(i).getImage(), boosterGhosts.get(i).getX(), boosterGhosts.get(i).getY(), null);
        }

    }

    public void enemyMaker() {

        int[] a;
        if (GameLevel < 5)
            a = chancyNumber(GameLevel, enemyNumber);
        else
            a = chancyNumber(4, enemyNumber);

        for (int i = 0; i < a[0]; i++) {
            int k = ((int) (Math.random() * Math.pow(10, 5))) % emptypiece.size();
            enemyLevel1 enemy = new enemyLevel1();
            enemy.setX(emptypiece.get(k).getX());
            enemy.setY(emptypiece.get(k).getY());
            enemy.setxInBoard(emptypiece.get(k).getxInBoard());
            enemy.setyInBoard(emptypiece.get(k).getyInBoard());
            enemies.add(enemy);
            pieces[enemy.getyInBoard()][enemy.getxInBoard()].setHasenemy(true);
            enemyMovecounter.add(0);
            enemybool.add(true);
            emptypiece.remove(k);
        }

        if (GameLevel > 1) {
            for (int i = 0; i < a[1]; i++) {
                int k = ((int) (Math.random() * Math.pow(10, 5))) % emptypiece.size();
                enemyLevel2 enemy = new enemyLevel2();
                enemy.setX(emptypiece.get(k).getX());
                enemy.setY(emptypiece.get(k).getY());
                enemy.setxInBoard(emptypiece.get(k).getxInBoard());
                enemy.setyInBoard(emptypiece.get(k).getyInBoard());
                enemies.add(enemy);
                pieces[enemy.getyInBoard()][enemy.getxInBoard()].setHasenemy(true);
                enemyMovecounter.add(0);
                enemybool.add(true);
                emptypiece.remove(k);
            }
        }

        if (GameLevel > 2) {
            for (int i = 0; i < a[2]; i++) {
                int k = ((int) (Math.random() * Math.pow(10, 5))) % emptypiece.size();
                enemyLevel3 enemy = new enemyLevel3();
                enemy.setX(emptypiece.get(k).getX());
                enemy.setY(emptypiece.get(k).getY());
                enemy.setxInBoard(emptypiece.get(k).getxInBoard());
                enemy.setyInBoard(emptypiece.get(k).getyInBoard());
                enemies.add(enemy);
                pieces[enemy.getyInBoard()][enemy.getxInBoard()].setHasenemy(true);
                enemyMovecounter.add(0);
                enemybool.add(true);
                emptypiece.remove(k);
            }
        }

        if (GameLevel > 3) {
            for (int i = 0; i < a[3]; i++) {
                int k = ((int) (Math.random() * Math.pow(10, 5))) % emptypiece.size();
                enemyLevel4 enemy = new enemyLevel4();
                enemy.setX(emptypiece.get(k).getX());
                enemy.setY(emptypiece.get(k).getY());
                enemy.setxInBoard(emptypiece.get(k).getxInBoard());
                enemy.setyInBoard(emptypiece.get(k).getyInBoard());
                enemies.add(enemy);
                pieces[enemy.getyInBoard()][enemy.getxInBoard()].setHasenemy(true);
                enemyMovecounter.add(0);
                enemybool.add(true);
                emptypiece.remove(k);
            }
        }

        play = true;
        if (changelvl[0])
            changelvl[1] = true;

    }

    public void BoosterMake(Graphics g) {

        int kprim = (enemyNumber + ((arz - 2) * (tool - 2) - emptypiece.size() - (arz - 3) * (tool - 2) / 4) / 3) / 2;

        int[] a = chancyNumber(10, kprim);

        for (int i = 0; i < a[0]; i++) {

            int k = ((int) (Math.random() * Math.pow(10, 5))) % breakpiece.size();
            BoostRadiuspaint(g, true, breakpiece.get(k));
            pieces[breakpiece.get(k).getyInBoard()][breakpiece.get(k).getxInBoard()].setHasbombboost(true);
            breakpiece.remove(k);

        }

        for (int i = 0; i < a[1]; i++) {

            int k = ((int) (Math.random() * Math.pow(10, 5))) % breakpiece.size();
            DisBoostRadiuspaint(g, true, breakpiece.get(k));
            pieces[breakpiece.get(k).getyInBoard()][breakpiece.get(k).getxInBoard()].setHasdisbombboost(true);
            breakpiece.remove(k);

        }
        for (int i = 0; i < a[2]; i++) {

            int k = ((int) (Math.random() * Math.pow(10, 5))) % breakpiece.size();
            BoostBombspaint(g, true, breakpiece.get(k));
            pieces[breakpiece.get(k).getyInBoard()][breakpiece.get(k).getxInBoard()].setIncreasebomb(true);
            breakpiece.remove(k);

        }

        for (int i = 0; i < a[3]; i++) {

            int k = ((int) (Math.random() * Math.pow(10, 5))) % breakpiece.size();
            DisBoostBombspaint(g, true, breakpiece.get(k));
            pieces[breakpiece.get(k).getyInBoard()][breakpiece.get(k).getxInBoard()].setDecreasebomb(true);
            breakpiece.remove(k);

        }

        for (int i = 0; i < a[4]; i++) {

            int k = ((int) (Math.random() * Math.pow(10, 5))) % breakpiece.size();
            BoostControlpaint(g, true, breakpiece.get(k));
            pieces[breakpiece.get(k).getyInBoard()][breakpiece.get(k).getxInBoard()].setControlBomb(true);
            breakpiece.remove(k);

        }

        for (int i = 0; i < a[5]; i++) {

            int k = ((int) (Math.random() * Math.pow(10, 5))) % breakpiece.size();
            BoostSpeedpaint(g, true, breakpiece.get(k));
            pieces[breakpiece.get(k).getyInBoard()][breakpiece.get(k).getxInBoard()].setSpeedboost(true);
            breakpiece.remove(k);

        }

        for (int i = 0; i < a[6]; i++) {

            int k = ((int) (Math.random() * Math.pow(10, 5))) % breakpiece.size();
            BoostPointpaint(g, true, breakpiece.get(k));
            pieces[breakpiece.get(k).getyInBoard()][breakpiece.get(k).getxInBoard()].setPointboost(true);
            breakpiece.remove(k);

        }

        for (int i = 0; i < a[7]; i++) {

            int k = ((int) (Math.random() * Math.pow(10, 5))) % breakpiece.size();
            DisBoostPointpaint(g, true, breakpiece.get(k));
            pieces[breakpiece.get(k).getyInBoard()][breakpiece.get(k).getxInBoard()].setPointdisboost(true);
            breakpiece.remove(k);

        }

        for (int i = 0; i < a[8]; i++) {

            int k = ((int) (Math.random() * Math.pow(10, 5))) % breakpiece.size();
            DisBoostSpeedpaint(g, true, breakpiece.get(k));
            pieces[breakpiece.get(k).getyInBoard()][breakpiece.get(k).getxInBoard()].setSpeeddisboost(true);
            breakpiece.remove(k);

        }

        for (int i = 0; i < a[9]; i++) {

            int k = ((int) (Math.random() * Math.pow(10, 5))) % breakpiece.size();
            BoostGhostpaint(g, true, breakpiece.get(k));
            pieces[breakpiece.get(k).getyInBoard()][breakpiece.get(k).getxInBoard()].setGhostboost(true);
            breakpiece.remove(k);

        }

    }

    public void firepaint(Graphics g, int xtmp, int ytmp) {

        try {
            fireimage = ImageIO.read(new File(thispath + "/src/pictures/fire.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        g.drawImage(fireimage, xtmp, ytmp, null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (counter)
            chornometr = new Chornometr();


        counter = false;
        timer.start();
        repaint();

    }

    public void setstone() {

        for (int i = 0; i < arz; i++) {
            for (int j = 0; j < tool; j++) {

                if (i == 0 || j == 0 || i == arz - 1 || j == tool - 1) {
                    pieces[i][j].setHasstone(true);
                } else if (i % 2 == (arz + 1) % 2 && j % 2 == (tool + 1) % 2) {
                    pieces[i][j].setHasstone(true);
                }
            }
        }
    }

    public boolean ehtemal() {

        double rand = Math.random();
        double tmp = rand / ((int) rand + 1);
        if (tmp < 0.4)
            return true;
        else
            return false;

    }

    public void setbreak() {

        for (int i = 1; i < arz - 1; i++) {
            for (int j = 1; j < tool - 1; j++) {

                if (pieces[i][j].isHasstone() == false && (i > 2 || j > 2) && ehtemal() && (i != arz - 2 || j != tool - 2)) {
                    pieces[i][j].setHasbreak(true);
                    breakpiece.add(pieces[i][j]);
                    breakpiece2server.add(pieces[i][j]);
                } else if (pieces[i][j].isEmpty() && (i > 2 || j > 2)) {
                    emptypiece.add(pieces[i][j]);
                    pieces[i][j].setHasbreak(false);
                } else
                    pieces[i][j].setHasbreak(false);
            }
        }

    }

    public boolean helpexplude(Bomb bmb, int i, int j, Bomberman bomberman) {

        boolean tmp = true;

        if (bmb.getyInBoard() + i >= 0 && bmb.getyInBoard() + i < arz && bmb.getxInBoard() + j >= 0 && bmb.getxInBoard() + j < tool) {

            if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty())
                firepaint(g1, pieces[bmb.getyInBoard() + i][bmb.getxInBoard()].getX() + j, pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].getY());

            if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isHasstone())
                tmp = false;
            else {

                if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isHasbreak()) {
                    bomberman.setPoint(bomberman.getPoint() + 10);
                    pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setHasbreak(false);
                    tmp = false;
                } else {

                    if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isHasbomberman()) {
                        int k = finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, bombermen);
                        if (k >= 0) {
                            bombermen.get(k).setAlive(false);
                            int killer = findingbomberman(bmb.getOwnerId(), bombermen);
                            if (killer >= 0)
                                bombermen.get(killer).setPoint(bombermen.get(killer).getPoint() + 100);
                            tmp = false;
                        }
                    }

                    if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isHasbomb()) {
                        tmp = false;
                        bombexpluding(bomb.get(finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, bomb)), bomberman);
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isHasenemy()) {
                            int numberOfenemy = finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, enemies);
                            bomberman.setPoint(bomberman.getPoint() + enemies.get(numberOfenemy).getLvl() * 20);
                            tmp = false;
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setHasenemy(false);
                            enemies.remove(numberOfenemy);
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isHasbombboost()) {
                            tmp = false;
                            int k = finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, boosterIncreaseRadii);
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setHasbombboost(false);
                            boosterIncreaseRadii.remove(k);
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isHasdisbombboost()) {
                            tmp = false;
                            int k = finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, disBoosterDecreasRadii);
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setHasdisbombboost(false);
                            disBoosterDecreasRadii.remove(k);
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isIncreasebomb()) {
                            tmp = false;
                            int k = finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, boosterIncreaseBombs);
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setIncreasebomb(false);
                            boosterIncreaseBombs.remove(k);
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isDecreasebomb()) {
                            tmp = false;
                            int k = finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, disBoosterDecreaseBombs);
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setDecreasebomb(false);
                            disBoosterDecreaseBombs.remove(k);
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isControlBomb()) {
                            tmp = false;
                            int k = finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, boosterControlBombs);
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setControlBomb(false);
                            boosterControlBombs.remove(k);
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isSpeedboost()) {
                            tmp = false;
                            int k = finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, boosterSpeeds);
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setSpeedboost(false);
                            boosterSpeeds.remove(k);
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isPointboost()) {
                            tmp = false;
                            int k = finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, boosterPoints);
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setPointboost(false);
                            boosterPoints.remove(k);
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isPointdisboost()) {
                            tmp = false;
                            int k = finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, disBoosterPoints);
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setPointdisboost(false);
                            disBoosterPoints.remove(k);
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isSpeeddisboost()) {
                            tmp = false;
                            int k = finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, disBoosterSpeeds);
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setSpeeddisboost(false);
                            disBoosterSpeeds.remove(k);
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isGhostboost()) {
                            tmp = false;
                            int k = finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, boosterGhosts);
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setGhostboost(false);
                            boosterGhosts.remove(k);
                        }
                    }

                }
            }
        }
        return tmp;
    }

    public void bombexpluding(Bomb bmb, Bomberman bomberman) {

        boolean ymines = true;
        boolean yplus = true;
        boolean xmines = true;
        boolean xplus = true;

        pieces[bmb.getyInBoard()][bmb.getxInBoard()].setHasbomb(false);

        firepaint(g1, pieces[bmb.getyInBoard()][bmb.getxInBoard()].getX(), pieces[bmb.getyInBoard()][bmb.getxInBoard()].getY());
        if (pieces[bmb.getyInBoard()][bmb.getxInBoard()].isHasbomberman())
            ;

        if (pieces[bmb.getyInBoard()][bmb.getxInBoard()].isHasbreak()) {
            bomberman.setPoint(bomberman.getPoint() + 10);
            pieces[bmb.getyInBoard()][bmb.getxInBoard()].setHasbreak(false);
        }

        for (int i = 1; i <= bmb.getBombRadius(); i++) {

            if (ymines)
                ymines = helpexplude(bmb, -i, 0, bomberman);
            if (yplus)
                yplus = helpexplude(bmb, i, 0, bomberman);
            if (xmines)
                xmines = helpexplude(bmb, 0, -i, bomberman);
            if (xplus)
                xplus = helpexplude(bmb, 0, i, bomberman);

        }

        int k = finding(bmb.getxInBoard(), bmb.getyInBoard(), bomb);
        if (k >= 0) {
            pieces[bomb.get(k).getyInBoard()][bomb.get(k).getxInBoard()].setHasbomb(false);
            bomb.remove(k);
        }
    }

    public int finding(int x, int y, ArrayList<? extends Stuff> a) {

        for (int i = 0; i < a.size(); i++) {

            if (a.get(i).getxInBoard() == x && a.get(i).getyInBoard() == y) {
                return i;
            }
        }

        return -1;//its never gonna happen

    }

    public int findingbomberman(double id, ArrayList<Bomberman> a) {

        for (int i = 0; i < a.size(); i++) {

            if (id == a.get(i).getId()) {
                return i;
            }
        }

        return -1;//its never gonna happen

    }

    public int[] chancyNumber(int k, int total) {

        double[] a = new double[k];
        double sum = 0;

        for (int i = 0; i < k; i++) {

            a[i] = Math.random() * Math.pow(10, 5);
            sum += a[i];

        }

        for (int i = 0; i < k; i++) {
            a[i] = (int) (total * a[i] / sum);
        }

        sum = 0;
        int[] b = new int[k];

        for (int i = 0; i < k; i++) {
            b[i] = (int) a[i];
            sum += a[i];
        }

        return fillingChance(k, total - (int) sum, b);

    }

    public int[] fillingChance(int k, int remain, int[] a) {

        if (remain != 0) {
            remain -= 1;
            int tmp = (int) ((Math.random() * Math.pow(10, 5)) % a.length);
            a[tmp] += 1;
            a = fillingChance(k, remain, a);
        }

        return a;
    }

    public void idiotmoving(int i, boolean isLvl4) {

        ArrayList<Piece> tmppiece = new ArrayList<>();

        if (enemies.get(i).getyInBoard() - 1 >= 0 && !pieces[enemies.get(i).getyInBoard() - 1][enemies.get(i).getxInBoard()].isHasbomb())
            if (isLvl4 || pieces[enemies.get(i).getyInBoard() - 1][enemies.get(i).getxInBoard()].isEmptyforEnemy())
                tmppiece.add(pieces[enemies.get(i).getyInBoard() - 1][enemies.get(i).getxInBoard()]);

        if (enemies.get(i).getxInBoard() + 1 < tool && !pieces[enemies.get(i).getyInBoard()][enemies.get(i).getxInBoard() + 1].isHasbomb())
            if (isLvl4 || pieces[enemies.get(i).getyInBoard()][enemies.get(i).getxInBoard() + 1].isEmptyforEnemy())
                tmppiece.add(pieces[enemies.get(i).getyInBoard()][enemies.get(i).getxInBoard() + 1]);

        if (enemies.get(i).getyInBoard() + 1 < arz && !pieces[enemies.get(i).getyInBoard() + 1][enemies.get(i).getxInBoard()].isHasbomb())
            if (isLvl4 || pieces[enemies.get(i).getyInBoard() + 1][enemies.get(i).getxInBoard()].isEmptyforEnemy())
                tmppiece.add(pieces[enemies.get(i).getyInBoard() + 1][enemies.get(i).getxInBoard()]);

        if (enemies.get(i).getxInBoard() - 1 >= 0 && !pieces[enemies.get(i).getyInBoard()][enemies.get(i).getxInBoard() - 1].isHasbomb())
            if (isLvl4 || pieces[enemies.get(i).getyInBoard()][enemies.get(i).getxInBoard() - 1].isEmptyforEnemy())
                tmppiece.add(pieces[enemies.get(i).getyInBoard()][enemies.get(i).getxInBoard() - 1]);

        if (tmppiece.size() != 0 && chornometr.getNow() - enemies.get(i).getLastTimeMove() > enemies.get(i).getSpeed() - 200) {

            pieces[enemies.get(i).getyInBoard()][enemies.get(i).getxInBoard()].setHasenemy(false);
            int k = ((int) (Math.random() * Math.pow(10, 5))) % tmppiece.size();
            enemies.get(i).setLastTimeMove(chornometr.getNow());
            enemies.get(i).setX(tmppiece.get(k).getX());
            enemies.get(i).setY(tmppiece.get(k).getY());
            enemies.get(i).setxInBoard(tmppiece.get(k).getxInBoard());
            enemies.get(i).setyInBoard(tmppiece.get(k).getyInBoard());
            pieces[enemies.get(i).getyInBoard()][enemies.get(i).getxInBoard()].setHasenemy(true);

        }

    }

    public void bombplanet(Bomberman bomberman) {
        Bomb tmp = new Bomb(bomberman.getBombradius());
        tmp.setOwnerId(bomberman.getId());
        tmp.setStart_time(chornometr.getNow());
        tmp.setX(bomberman.getX());
        tmp.setY(bomberman.getY());
        tmp.setxInBoard(bomberman.getxInBoard());
        tmp.setyInBoard(bomberman.getyInBoard());
        pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].setHasbomb(true);
        bomb.add(tmp);
        bmb = true;
    }

    public void getbooster(Bomberman bomberman) {
        if (!pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].isHasbreak()) {
            if (pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].isHasbombboost()) {

                bomberman.setBombradius(bomberman.getBombradius() + 1);
                pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].setHasbombboost(false);
                boosterIncreaseRadii.remove(finding(bomberman.getxInBoard(), bomberman.getyInBoard(), boosterIncreaseRadii));

            }

            if (pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].isHasdisbombboost()) {

                if (bomberman.getBombradius() > 1)
                    bomberman.setBombradius(bomberman.getBombradius() - 1);
                pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].setHasdisbombboost(false);
                disBoosterDecreasRadii.remove(finding(bomberman.getxInBoard(), bomberman.getyInBoard(), disBoosterDecreasRadii));

            }

            if (pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].isIncreasebomb()) {

                bomberman.setBomblimit(bomberman.getBomblimit() + 1);
                pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].setIncreasebomb(false);
                boosterIncreaseBombs.remove(finding(bomberman.getxInBoard(), bomberman.getyInBoard(), boosterIncreaseBombs));

            }

            if (pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].isDecreasebomb()) {

                if (bomberman.getBomblimit() > 1)
                    bomberman.setBomblimit(bomberman.getBomblimit() - 1);
                pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].setDecreasebomb(false);
                disBoosterDecreaseBombs.remove(finding(bomberman.getxInBoard(), bomberman.getyInBoard(), disBoosterDecreaseBombs));

            }

            if (pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].isControlBomb()) {

                bomberman.setControlBomb(true);
                pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].setControlBomb(false);
                boosterControlBombs.remove(finding(bomberman.getxInBoard(), bomberman.getyInBoard(), boosterControlBombs));

            }

            if (pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].isSpeedboost()) {

                if (bomberman.getSpeed() - 50 > 0)
                    bomberman.setSpeed(bomberman.getSpeed() - 50);
                pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].setSpeedboost(false);
                boosterSpeeds.remove(finding(bomberman.getxInBoard(), bomberman.getyInBoard(), boosterSpeeds));

            }

            if (pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].isPointboost()) {

                bomberman.setPoint(bomberman.getPoint() + 100);
                pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].setPointboost(false);
                boosterPoints.remove(finding(bomberman.getxInBoard(), bomberman.getyInBoard(), boosterPoints));

            }

            if (pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].isPointdisboost()) {

                bomberman.setPoint(bomberman.getPoint() - 100);
                pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].setPointdisboost(false);
                disBoosterPoints.remove(finding(bomberman.getxInBoard(), bomberman.getyInBoard(), disBoosterPoints));

            }

            if (pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].isSpeeddisboost()) {

                if (bomberman.getSpeed() + 50 <= 300)
                    bomberman.setSpeed(bomberman.getSpeed() + 50);
                pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].setSpeeddisboost(false);
                disBoosterSpeeds.remove(finding(bomberman.getxInBoard(), bomberman.getyInBoard(), disBoosterSpeeds));

            }

            if (pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].isGhostboost()) {

                bomberman.setGhostMode(true);
                pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].setGhostboost(false);
                boosterGhosts.remove(finding(bomberman.getxInBoard(), bomberman.getyInBoard(), boosterGhosts));

            }
        }

    }

    public void resetDetails2changLvl() {

        changelvl[0] = true;
        chornometr.stop();
        timer.stop();
        play = false;

        this.removeAll();

        for (int i = 0; i < bombermen.size(); i++)
            bombermen.get(i).makebombermanalive();


        while (breakpiece.size() != 0)
            breakpiece.remove(0);

        while (breakpiece2server.size() != 0)
            breakpiece2server.remove(0);

        while (emptypiece.size() != 0)
            emptypiece.remove(0);

        while (bomb.size() != 0)
            bomb.remove(0);

        while (boosterIncreaseRadii.size() != 0)
            boosterIncreaseRadii.remove(0);

        while (disBoosterDecreasRadii.size() != 0)
            disBoosterDecreasRadii.remove(0);

        while (boosterIncreaseBombs.size() != 0)
            boosterIncreaseBombs.remove(0);

        while (disBoosterDecreaseBombs.size() != 0)
            disBoosterDecreaseBombs.remove(0);

        while (boosterControlBombs.size() != 0)
            boosterControlBombs.remove(0);

        while (boosterSpeeds.size() != 0)
            boosterSpeeds.remove(0);

        while (boosterPoints.size() != 0)
            boosterPoints.remove(0);

        while (disBoosterPoints.size() != 0)
            disBoosterPoints.remove(0);

        while (disBoosterSpeeds.size() != 0)
            disBoosterSpeeds.remove(0);

        while (boosterGhosts.size() != 0)
            boosterGhosts.remove(0);

        for (int i = 0; i < arz; i++) {
            for (int j = 0; j < tool; j++) {
                pieces[i][j].makeEmpty();
            }
        }

        GameLevel++;

        enemyWorthRate = 105 * enemyWorthRate / 100;
        if ((int) enemyWorthRate > enemyNumber)
            enemyNumber = (int) enemyWorthRate;

        setstone();
        setbreak();
        enmycounter = 0;
        makeBoostCount = true;

        for (int i = 0; i < bombermen.size(); i++) {
            Bomberman bomberman = bombermen.get(i);
            bomberman.setX(pieces[1][1].getX());
            bomberman.setY(pieces[1][1].getY());
            bomberman.setxInBoard(1);
            bomberman.setyInBoard(1);
        }

        chornometr.resume();
        timer.start();

    }

    public Bomberman nearestBomberman(enemy enemy) {
        if (bombermen.size() != 0) {
            Bomberman nearest = bombermen.get(0);
            int dist = Math.abs(nearest.getxInBoard() - enemy.getxInBoard()) + Math.abs(nearest.getyInBoard() - enemy.getyInBoard());
            for (int i = 1; i < bombermen.size(); i++) {
                Bomberman tmp = bombermen.get(i);
                int dist2 = Math.abs(tmp.getxInBoard() - enemy.getxInBoard()) + Math.abs(tmp.getyInBoard() - enemy.getyInBoard());
                if (dist > dist2) {
                    nearest = tmp;
                    dist = dist2;
                }
            }
            return nearest;
        } else {
            Bomberman tmp = new Bomberman();
            tmp.setxInBoard(((int) arz / 2));
            tmp.setyInBoard(((int) tool / 2));
            return tmp;
        }

    }

    public Bomberman makeBomberman(double id) {
        Bomberman bomberman = new Bomberman();
        bomberman.setId(id);
        bomberman.setX(pieces[1][1].getX());
        bomberman.setY(pieces[1][1].getY());
        bomberman.setxInBoard(1);
        bomberman.setyInBoard(1);
        bombermen.add(bomberman);
        return bomberman;
    }

}