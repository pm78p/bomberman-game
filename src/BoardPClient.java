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

class BoardPClient extends JPanel implements KeyListener, ActionListener {

    static final long serialVersionUID = 3L;
    int playerPoint = 0;
    GameInfo gameInfo = null;

    int arz = 15;
    int tool = 27;
    Piece[][] pieces = new Piece[arz][tool];
    int piec_size = 60;
    int start_x = 0;
    int start_y = 0;
    int GameLevel = 1;

    Bomberman bomberman = null;
    ArrayList<enemy> enemies = new ArrayList<>();
    int enemyNumber = Math.min(tool, arz) - 2;

    ArrayList<Piece> breakpiece = new ArrayList<>();
    ArrayList<Piece> emptypiece = new ArrayList<>();
    ArrayList<Bomberman> otherbomBombermen = new ArrayList<>();
    ArrayList<Bomb> bomb = new ArrayList<>();
    ArrayList<Bomb> otherbomb = new ArrayList<>();
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
    int bombradius = 1;
    int bomblimit = 1;
    boolean ControlBomb = false;

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

    public BoardPClient(GameInfo gameInfo, PrintStream printStream) {

        this.setBackground(Color.BLUE);
        this.printStream = printStream;

        Path currentRelativePath = Paths.get("");
        thispath = currentRelativePath.toAbsolutePath().toString();

        this.gameInfo = gameInfo;
        addKeyListener(this);
        setFocusable(true);
        timer = new Timer(delay, this);
        timer.start();

        for (int i = 0; i < arz; i++) {
            for (int j = 0; j < tool; j++) {
                pieces[i][j] = new Piece();
            }
        }

        setstone();

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

        if (bomberman == null)
            playerPoint = 0;
        else
            playerPoint = bomberman.getPoint();

        if (!counter)
            gameInfo.filler(playerPoint, chornometr.getNow(), GameLevel, bomblimit - bomb.size(), bombradius);

        g1 = g;

        boardpaint(g); //paint pieces and stones and breaks

        boombermanpain(g);   //paint bomberman

        enemypaint(g);           // paint enemy

        if (bmb == true)
            bombpaint(g);       //paint bombs

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

        if (play == false && bomberman == null) {
            bomberman = new Bomberman();
            bomberman.setX(pieces[1][1].getX());
            bomberman.setY(pieces[1][1].getY());
            bomberman.setxInBoard(1);
            bomberman.setyInBoard(1);
            printStream.println("bomberman " + bomberman.getId());
        }

        pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].setHasbomberman(true);

        if (bomberman.getImage() == null) {
            BufferedImage bombermanimage = null;
            try {
                bombermanimage = ImageIO.read(new File(thispath + "/src/pictures/Bomberman.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            bomberman.setImage(bombermanimage);
        }

        for (int i = 0; i < otherbomBombermen.size(); i++) {
            Bomberman otherbomberman = otherbomBombermen.get(i);
            if (otherbomberman.isAlive()) {
                if (pieces[otherbomberman.getyInBoard()][otherbomberman.getxInBoard()].isHasenemy())
                    otherbomberman.setAlive(false);
                else {
                    g.drawImage(otherbomberman.getImage(), otherbomberman.getX(), otherbomberman.getY(), null);
                    getbooster(otherbomberman);
                    pieces[otherbomberman.getyInBoard()][otherbomberman.getxInBoard()].setHasbomberman(true);
                }
            }
        }

        if (pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].isHasenemy())
            bomberman.setAlive(false);
        else if (bomberman.isAlive())
            g.drawImage(bomberman.getImage(), bomberman.getX(), bomberman.getY(), null);

    }

    public void enemypaint(Graphics g) {

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
                bombexpluding(bomb, bomb.get(i));
            }
        }

        for (int i = 0; i < otherbomb.size(); i++) {

            if (otherbomb.get(i).getImage() == null) {

                Bomb tmp = new Bomb(1);
                otherbomb.get(i).setImage(tmp.getImage());

            }

            if (chornometr.getNow() - otherbomb.get(i).getStart_time() <= 5000) {
                g.drawImage(otherbomb.get(i).getImage(), otherbomb.get(i).getX(), otherbomb.get(i).getY(), null);
                pieces[otherbomb.get(i).getyInBoard()][otherbomb.get(i).getxInBoard()].setHasbomb(true);
            } else {
                bombexpluding(otherbomb, otherbomb.get(i));
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

            if (!pieces[boosterIncreaseRadii.get(i).getyInBoard()][boosterIncreaseRadii.get(i).getxInBoard()].isHasbreak())
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

            if (!pieces[disBoosterDecreasRadii.get(i).getyInBoard()][disBoosterDecreasRadii.get(i).getxInBoard()].isHasbreak())
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

            if (!pieces[boosterIncreaseBombs.get(i).getyInBoard()][boosterIncreaseBombs.get(i).getxInBoard()].isHasbreak())
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

            if (!pieces[disBoosterDecreaseBombs.get(i).getyInBoard()][disBoosterDecreaseBombs.get(i).getxInBoard()].isHasbreak())
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

            if (!pieces[boosterControlBombs.get(i).getyInBoard()][boosterControlBombs.get(i).getxInBoard()].isHasbreak())
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

            if (!pieces[boosterSpeeds.get(i).getyInBoard()][boosterSpeeds.get(i).getxInBoard()].isHasbreak())
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

            if (!pieces[boosterPoints.get(i).getyInBoard()][boosterPoints.get(i).getxInBoard()].isHasbreak())
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

            if (!pieces[disBoosterPoints.get(i).getyInBoard()][disBoosterPoints.get(i).getxInBoard()].isHasbreak())
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

            if (!pieces[disBoosterSpeeds.get(i).getyInBoard()][disBoosterSpeeds.get(i).getxInBoard()].isHasbreak())
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

            if (!pieces[boosterGhosts.get(i).getyInBoard()][boosterGhosts.get(i).getxInBoard()].isHasbreak())
                g.drawImage(boosterGhosts.get(i).getImage(), boosterGhosts.get(i).getX(), boosterGhosts.get(i).getY(), null);
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

    @Override
    public void keyPressed(KeyEvent e) {

        if (bomberman.isAlive()) {
            if (e.getKeyCode() == KeyEvent.VK_UP && bomberman.permision2move(chornometr.getNow(), elapsedTime))
                movee(-1, 0);

            else if (e.getKeyCode() == KeyEvent.VK_RIGHT && bomberman.permision2move(chornometr.getNow(), elapsedTime))
                movee(0, 1);

            else if (e.getKeyCode() == KeyEvent.VK_DOWN && bomberman.permision2move(chornometr.getNow(), elapsedTime)) {
                movee(1, 0);

            } else if (e.getKeyCode() == KeyEvent.VK_LEFT && bomberman.permision2move(chornometr.getNow(), elapsedTime))
                movee(0, -1);

            else if (e.getKeyCode() == KeyEvent.VK_B && bomb.size() < bomberman.getBomblimit() && !pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].isHasbomb()) {
                Bomb tmp = new Bomb(bombradius);
                tmp.setStart_time(chornometr.getNow());
                tmp.setX(bomberman.getX());
                tmp.setY(bomberman.getY());
                tmp.setxInBoard(bomberman.getxInBoard());
                tmp.setyInBoard(bomberman.getyInBoard());
                pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].setHasbomb(true);
                tmp.setOwnerId(bomberman.getId());
                bomb.add(tmp);
                bmb = true;
                String bombplant = "pbomb " + bomberman.getId() + " ";
                printStream.println(bombplant);
            } else if (e.getKeyCode() == KeyEvent.VK_E)
                if (ControlBomb && bomb.size() != 0) {
                    printStream.println("lastbombexpl ");
                    bombexpluding(bomb, bomb.get(0));
                }
        }
        play = true;

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    public void movee(int i, int j) {

        boolean ghostmode = false;

        if (bomberman.isGhostMode() && !pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].isHasbomb())
            ghostmode = true;

        if (bomberman.getyInBoard() + i <= arz - 1 && bomberman.getxInBoard() + j <= tool - 1 && bomberman.getyInBoard() + i > 0 && bomberman.getxInBoard() + j > 0)

            if ((!bomberman.isGhostMode()
                    && !pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].isHasstone()
                    && !pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].isHasbreak()
                    && !pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].isHasbomb())
                    || ghostmode) {

                if (!pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].isHasbreak()) {

                    if (pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].isHasbombboost()) {

                        bombradius++;
                        pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].setHasbombboost(false);
                        boosterIncreaseRadii.remove(finding(bomberman.getxInBoard() + j, bomberman.getyInBoard() + i, boosterIncreaseRadii));

                    }

                    if (pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].isHasdisbombboost()) {

                        if (bombradius > 1)
                            bombradius--;
                        pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].setHasdisbombboost(false);
                        disBoosterDecreasRadii.remove(finding(bomberman.getxInBoard() + j, bomberman.getyInBoard() + i, disBoosterDecreasRadii));

                    }

                    if (pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].isIncreasebomb()) {

                        bomblimit++;
                        bomberman.setBomblimit(bomblimit);
                        pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].setIncreasebomb(false);
                        boosterIncreaseBombs.remove(finding(bomberman.getxInBoard() + j, bomberman.getyInBoard() + i, boosterIncreaseBombs));

                    }

                    if (pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].isDecreasebomb()) {

                        if (bomblimit > 1)
                            bomblimit--;
                        bomberman.setBomblimit(bomblimit);
                        pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].setDecreasebomb(false);
                        disBoosterDecreaseBombs.remove(finding(bomberman.getxInBoard() + j, bomberman.getyInBoard() + i, disBoosterDecreaseBombs));

                    }

                    if (pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].isControlBomb()) {

                        ControlBomb = true;
                        pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].setControlBomb(false);
                        boosterControlBombs.remove(finding(bomberman.getxInBoard() + j, bomberman.getyInBoard() + i, boosterControlBombs));

                    }

                    if (pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].isSpeedboost()) {

                        if (bomberman.getSpeed() - 50 > 0)
                            bomberman.setSpeed(bomberman.getSpeed() - 50);
                        pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].setSpeedboost(false);
                        boosterSpeeds.remove(finding(bomberman.getxInBoard() + j, bomberman.getyInBoard() + i, boosterSpeeds));

                    }

                    if (pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].isPointboost()) {

                        bomberman.setPoint(bomberman.getPoint() + 100);
                        pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].setPointboost(false);
                        boosterPoints.remove(finding(bomberman.getxInBoard() + j, bomberman.getyInBoard() + i, boosterPoints));

                    }

                    if (pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].isPointdisboost()) {

                        bomberman.setPoint(bomberman.getPoint() - 100);
                        pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].setPointdisboost(false);
                        disBoosterPoints.remove(finding(bomberman.getxInBoard() + j, bomberman.getyInBoard() + i, disBoosterPoints));

                    }

                    if (pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].isSpeeddisboost()) {

                        if (bomberman.getSpeed() + 50 <= 300)
                            bomberman.setSpeed(bomberman.getSpeed() + 50);
                        pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].setSpeeddisboost(false);
                        disBoosterSpeeds.remove(finding(bomberman.getxInBoard() + j, bomberman.getyInBoard() + i, disBoosterSpeeds));

                    }

                    if (pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].isGhostboost()) {

                        bomberman.setGhostMode(true);
                        pieces[bomberman.getyInBoard() + i][bomberman.getxInBoard() + j].setGhostboost(false);
                        boosterGhosts.remove(finding(bomberman.getxInBoard() + j, bomberman.getyInBoard() + i, boosterGhosts));

                    }
                }

                if ((bomberman.getyInBoard() + i == arz - 2 && bomberman.getxInBoard() + j == tool - 1 && enemies.size() == 0)
                        || (bomberman.getyInBoard() + i != arz - 2 || bomberman.getxInBoard() + j != tool - 1)) {
                    pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].setHasbomberman(false);
                    String move2 = "move " + bomberman.getId() + " " + (bomberman.getX() + j * piec_size) + " " + (bomberman.getY() + i * piec_size) + " "
                            + (bomberman.getxInBoard() + j) + " " + (bomberman.getyInBoard() + i) + " ";
                    printStream.println(move2);
                    bomberman.setLast_move(chornometr.getNow());

                }
            }
    }

    public boolean helpexplude(Bomb bmb, int i, int j) {

        boolean tmp = true;

        if (bmb.getyInBoard() + i >= 0 && bmb.getyInBoard() + i < arz && bmb.getxInBoard() + j >= 0 && bmb.getxInBoard() + j < tool) {

            if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty())
                firepaint(g1, pieces[bmb.getyInBoard() + i][bmb.getxInBoard()].getX() + j, pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].getY());

            if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isHasstone())
                tmp = false;
            else {

                if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isHasbreak()) {
                    if (bmb.getOwnerId() == bomberman.getId())
                        bomberman.setPoint(bomberman.getPoint() + 10);
                    pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setHasbreak(false);
                    tmp = false;
                } else {

                    if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isHasbomberman()) {
                        int otherbmbman = finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, otherbomBombermen);
                        if (otherbmbman >= 0) {
                            otherbomBombermen.get(otherbmbman).setAlive(false);
                            if (bmb.getOwnerId() == bomberman.getId()) {
                                bomberman.setPoint(bomberman.getPoint() + 100);
                            }
                            tmp = false;
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setHasbomberman(false);
                        }
                        if (bomberman.getxInBoard() == bmb.getxInBoard() + j && bomberman.getyInBoard() == bmb.getyInBoard() + i) {
                            bomberman.setAlive(false);
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setHasbomberman(false);
                            tmp = false;
                        }
                    }

                    if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isHasbomb()) {
                        tmp = false;
                        int mybombs = finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, this.bomb);
                        int otherbombs = finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, this.otherbomb);
                        if (mybombs != -1)
                            bombexpluding(this.bomb, this.bomb.get(mybombs));
                        else if (otherbombs != -1)
                            bombexpluding(otherbomb, otherbomb.get(otherbombs));
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isHasenemy()) {
                            int numberOfenemy = finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, enemies);
                            if (bmb.getOwnerId() == bomberman.getId())
                                bomberman.setPoint(bomberman.getPoint() + enemies.get(numberOfenemy).getLvl() * 20);
                            tmp = false;
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setHasenemy(false);
                            enemies.remove(numberOfenemy);
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isHasbombboost()) {
                            tmp = false;
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setHasbombboost(false);
                            boosterIncreaseRadii.remove(finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, boosterIncreaseRadii));
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isHasdisbombboost()) {
                            tmp = false;
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setHasdisbombboost(false);
                            disBoosterDecreasRadii.remove(finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, disBoosterDecreasRadii));
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isIncreasebomb()) {
                            tmp = false;
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setIncreasebomb(false);
                            boosterIncreaseBombs.remove(finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, boosterIncreaseBombs));
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isDecreasebomb()) {
                            tmp = false;
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setDecreasebomb(false);
                            disBoosterDecreaseBombs.remove(finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, disBoosterDecreaseBombs));
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isControlBomb()) {
                            tmp = false;
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setControlBomb(false);
                            boosterControlBombs.remove(finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, boosterControlBombs));
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isSpeedboost()) {
                            tmp = false;
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setSpeedboost(false);
                            boosterSpeeds.remove(finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, boosterSpeeds));
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isPointboost()) {
                            tmp = false;
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setPointboost(false);
                            boosterPoints.remove(finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, boosterPoints));
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isPointdisboost()) {
                            tmp = false;
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setPointdisboost(false);
                            disBoosterPoints.remove(finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, disBoosterPoints));
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isSpeeddisboost()) {
                            tmp = false;
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setSpeeddisboost(false);
                            disBoosterSpeeds.remove(finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, disBoosterSpeeds));
                        }
                    }

                    if (!pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isEmpty()) {
                        if (pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].isGhostboost()) {
                            tmp = false;
                            pieces[bmb.getyInBoard() + i][bmb.getxInBoard() + j].setGhostboost(false);
                            boosterGhosts.remove(finding(bmb.getxInBoard() + j, bmb.getyInBoard() + i, boosterGhosts));
                        }
                    }

                }
            }
        }
        return tmp;
    }

    public void bombexpluding(ArrayList<Bomb> bomb, Bomb bmb) {

        boolean ymines = true;
        boolean yplus = true;
        boolean xmines = true;
        boolean xplus = true;

        pieces[bmb.getyInBoard()][bmb.getxInBoard()].setHasbomb(false);

        firepaint(g1, pieces[bmb.getyInBoard()][bmb.getxInBoard()].getX(), pieces[bmb.getyInBoard()][bmb.getxInBoard()].getY());
        if (pieces[bmb.getyInBoard()][bmb.getxInBoard()].isHasbomberman())
            ;

        if (pieces[bmb.getyInBoard()][bmb.getxInBoard()].isHasbreak()) {
            if (bmb.getOwnerId() == bomberman.getId())
                bomberman.setPoint(bomberman.getPoint() + 10);
            pieces[bmb.getyInBoard()][bmb.getxInBoard()].setHasbreak(false);
        }

        for (int i = 1; i <= bmb.getBombRadius(); i++) {

            if (ymines)
                ymines = helpexplude(bmb, -i, 0);
            if (yplus)
                yplus = helpexplude(bmb, i, 0);
            if (xmines)
                xmines = helpexplude(bmb, 0, -i);
            if (xplus)
                xplus = helpexplude(bmb, 0, i);

        }

        int k = finding(bmb.getxInBoard(), bmb.getyInBoard(), bomb);
        if (k >= 0) {
            pieces[bomb.get(k).getyInBoard()][bomb.get(k).getxInBoard()].setHasbomb(false);
            bomb.remove(k);
        }
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

    public void resetDetails2changLvl() {

        chornometr.stop();
        timer.stop();
        play = false;

        this.removeAll();

        bomberman.makebombermanalive();

        for (int i = 0; i < otherbomBombermen.size(); i++)
            otherbomBombermen.get(i).makebombermanalive();

        while (breakpiece.size() != 0)
            breakpiece.remove(0);

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

        setstone();
        enmycounter = 0;
        makeBoostCount = true;

        bomberman.setX(pieces[1][1].getX());
        bomberman.setY(pieces[1][1].getY());
        bomberman.setxInBoard(1);
        bomberman.setyInBoard(1);

        chornometr.resume();
        timer.start();

    }

    public int finding(int x, int y, ArrayList<? extends Stuff> a) {

        for (int i = 0; i < a.size(); i++) {

            if (a.get(i).getxInBoard() == x && a.get(i).getyInBoard() == y) {
                return i;
            }
        }

        return -1;//its never gonna happen

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

}