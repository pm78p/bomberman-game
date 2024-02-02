import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class Client extends JFrame {

    BoardPClient bpc = null;
    String ip = "";
    int port;
    Socket s;
    ArrayList<Massage> massages = new ArrayList<>();
    String name = "Client";

    public Client() throws IOException {
        this.ip = "127.0.0.1";
        this.port = 8090;
        this.s = new Socket(ip, port);
        init();
    }

    public Client(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
        this.s = new Socket(ip, port);
        init();
    }

    public Client(Socket s, String name) throws IOException {
        this.name = name;
        this.s = s;
        init();
    }

    public void init() throws IOException {

        Scanner sc = new Scanner(System.in);
        InputStream fis = null;
        fis = s.getInputStream();
        Scanner sc1 = new Scanner(fis);

        PrintStream p = null;
        try {
            p = new PrintStream(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

//        JFrame this = new JFrame();
        this.setBounds(0, 0, 1725, 935);
        this.setFocusTraversalKeysEnabled(false);
        this.setTitle("Boomberman client");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel center = new JPanel();
        center.setBackground(Color.blue);

        GameInfo gameInfo = new GameInfo(true, name);
        bpc = new BoardPClient(gameInfo, p);
        this.add(gameInfo, BorderLayout.EAST);
        this.add(center, BorderLayout.CENTER);
        this.add(bpc, BorderLayout.CENTER);
        this.setVisible(true);
        bpc.setVisible(true);

        final boolean[] first_time = {true};

        PrintStream finalP = p;
        Thread chat = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (gameInfo.chatFrame.massages.size() != 0) {
                        int i = gameInfo.chatFrame.massages.size() - 1;
                        while (massages.size() != gameInfo.chatFrame.massages.size() && i != -1) {
                            Massage massage = gameInfo.chatFrame.massages.get(i--);
                            finalP.println("massage " + massage.toString());
                            massages.add(massage);
                        }
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        chat.start();

        Thread serverlistener = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (s.isClosed()) {
                        bpc.timer.stop();
                        bpc.chornometr.stop();
                        bpc.bomberman = null;
                    }
                    if (sc1.hasNext()) {
                        String temp = sc1.next();
                        int k;
                        System.out.println("++" + temp + "++");

                        switch (temp) {
                            case "chonometr":
                                int glvl = Integer.parseInt(sc1.next());
                                long sartt = Long.parseLong(sc1.next());
                                long zero = Long.parseLong(sc1.next());
                                long gaps = Long.parseLong(sc1.next());
                                long savet = Long.parseLong(sc1.next());
                                bpc.GameLevel = glvl;
                                boolean loop = true;
                                while (loop) {
                                    if (bpc.chornometr != null) {
                                        bpc.chornometr.loadString(sartt, zero, gaps, savet);
                                        loop = false;
                                    }
                                }
                                break;
                            case "m":
                                double idbmbman = Double.parseDouble(sc1.next());
                                String alive = sc1.next();
                                int x = Integer.parseInt(sc1.next());
                                int y = Integer.parseInt(sc1.next());
                                int x11 = Integer.parseInt(sc1.next());
                                int y11 = Integer.parseInt(sc1.next());
                                System.out.println(idbmbman + " " + x11 + " " + y11);
                                if (bpc.bomberman != null && idbmbman == bpc.bomberman.getId()) {
                                    System.out.println("bomberman move to " + x11 + " " + y11);
                                    bpc.pieces[bpc.bomberman.getyInBoard()][bpc.bomberman.getxInBoard()].setHasbomberman(false);
                                    bpc.bomberman.setX(x);
                                    bpc.bomberman.setY(y);
                                    bpc.bomberman.setxInBoard(x11);
                                    bpc.bomberman.setyInBoard(y11);
                                    if (alive.equals("y"))
                                        bpc.bomberman.setAlive(true);
                                    else
                                        bpc.bomberman.setAlive(false);
                                } else {
                                    int othrbmbmn = findingbbman(idbmbman, bpc.otherbomBombermen);
                                    if (othrbmbmn >= 0) {
                                        System.out.println("another bomberman is moving");
                                        bpc.pieces[bpc.otherbomBombermen.get(othrbmbmn).getyInBoard()][bpc.otherbomBombermen.get(othrbmbmn).getxInBoard()].setHasbomberman(false);
                                        bpc.otherbomBombermen.get(othrbmbmn).setX(x);
                                        bpc.otherbomBombermen.get(othrbmbmn).setY(y);
                                        bpc.otherbomBombermen.get(othrbmbmn).setxInBoard(x11);
                                        bpc.otherbomBombermen.get(othrbmbmn).setyInBoard(y11);
                                        if (alive.equals("y"))
                                            bpc.otherbomBombermen.get(othrbmbmn).setAlive(true);
                                        else
                                            bpc.otherbomBombermen.get(othrbmbmn).setAlive(false);
                                    } else {
                                        System.out.println("another bomberman is creating");
                                        Bomberman othrbomberman = new Bomberman();
                                        othrbomberman.setId(idbmbman);
                                        othrbomberman.setX(x);
                                        othrbomberman.setY(y);
                                        othrbomberman.setxInBoard(x11);
                                        othrbomberman.setyInBoard(y11);
                                        if (alive.equals("y"))
                                            othrbomberman.setAlive(true);
                                        else
                                            othrbomberman.setAlive(false);
                                        bpc.otherbomBombermen.add(othrbomberman);
                                    }
                                }
                                break;
                            case "e":
                                double id = Double.parseDouble(sc1.next());
                                int lvl = Integer.parseInt(sc1.next());
                                x = Integer.parseInt(sc1.next());
                                y = Integer.parseInt(sc1.next());
                                x11 = Integer.parseInt(sc1.next());
                                y11 = Integer.parseInt(sc1.next());
                                if (first_time[0]) {
                                    enemy tmpen = null;
                                    switch (lvl) {
                                        case 1:
                                            tmpen = new enemyLevel1(id);
                                            break;
                                        case 2:
                                            tmpen = new enemyLevel2(id);
                                            break;
                                        case 3:
                                            tmpen = new enemyLevel3(id);
                                            break;
                                        case 4:
                                            tmpen = new enemyLevel4(id);
                                            break;

                                    }
                                    tmpen.setX(x);
                                    tmpen.setY(y);
                                    tmpen.setxInBoard(x11);
                                    tmpen.setyInBoard(y11);
                                    bpc.enemies.add(tmpen);
                                    bpc.pieces[y11][x11].setHasenemy(true);
                                } else {
                                    k = finding(id, bpc.enemies);
                                    if (k >= 0) {
                                        bpc.pieces[bpc.enemies.get(k).getyInBoard()][bpc.enemies.get(k).getxInBoard()].setHasenemy(false);
                                        bpc.enemies.get(k).setX(x);
                                        bpc.enemies.get(k).setY(y);
                                        bpc.enemies.get(k).setxInBoard(x11);
                                        bpc.enemies.get(k).setyInBoard(y11);
                                        bpc.pieces[bpc.enemies.get(k).getyInBoard()][bpc.enemies.get(k).getxInBoard()].setHasenemy(true);
                                    } else {
                                        System.out.println("problm");
                                    }
                                }

                                break;
                            case "g":
                                k = Integer.parseInt(sc1.next());
                                if (first_time[0]) {
                                    BoosterGhost tmpghost = new BoosterGhost();
                                    bpc.boosterGhosts.add(tmpghost);
                                } else
                                    bpc.pieces[bpc.boosterGhosts.get(k).getyInBoard()][bpc.boosterGhosts.get(k).getxInBoard()].setGhostboost(false);

                                bpc.boosterGhosts.get(k).setX(Integer.parseInt(sc1.next()));
                                bpc.boosterGhosts.get(k).setY(Integer.parseInt(sc1.next()));
                                bpc.boosterGhosts.get(k).setxInBoard(Integer.parseInt(sc1.next()));
                                bpc.boosterGhosts.get(k).setyInBoard(Integer.parseInt(sc1.next()));
                                bpc.pieces[bpc.boosterGhosts.get(k).getyInBoard()][bpc.boosterGhosts.get(k).getxInBoard()].setGhostboost(true);
                                break;
                            case "p":
                                k = Integer.parseInt(sc1.next());
                                if (first_time[0]) {
                                    BoosterPoint tmpp = new BoosterPoint();
                                    bpc.boosterPoints.add(tmpp);
                                } else
                                    bpc.pieces[bpc.boosterPoints.get(k).getyInBoard()][bpc.boosterPoints.get(k).getxInBoard()].setPointboost(false);

                                bpc.boosterPoints.get(k).setX(Integer.parseInt(sc1.next()));
                                bpc.boosterPoints.get(k).setY(Integer.parseInt(sc1.next()));
                                bpc.boosterPoints.get(k).setxInBoard(Integer.parseInt(sc1.next()));
                                bpc.boosterPoints.get(k).setyInBoard(Integer.parseInt(sc1.next()));
                                bpc.pieces[bpc.boosterPoints.get(k).getyInBoard()][bpc.boosterPoints.get(k).getxInBoard()].setPointboost(true);
                                break;
                            case "dp":
                                k = Integer.parseInt(sc1.next());
                                if (first_time[0]) {
                                    DisBoosterPoint tmpdp = new DisBoosterPoint();
                                    bpc.disBoosterPoints.add(tmpdp);
                                } else
                                    bpc.pieces[bpc.disBoosterPoints.get(k).getyInBoard()][bpc.disBoosterPoints.get(k).getxInBoard()].setPointdisboost(false);

                                bpc.disBoosterPoints.get(k).setX(Integer.parseInt(sc1.next()));
                                bpc.disBoosterPoints.get(k).setY(Integer.parseInt(sc1.next()));
                                bpc.disBoosterPoints.get(k).setxInBoard(Integer.parseInt(sc1.next()));
                                bpc.disBoosterPoints.get(k).setyInBoard(Integer.parseInt(sc1.next()));
                                bpc.pieces[bpc.disBoosterPoints.get(k).getyInBoard()][bpc.disBoosterPoints.get(k).getxInBoard()].setPointdisboost(true);
                                break;
                            case "s":
                                k = Integer.parseInt(sc1.next());
                                if (first_time[0]) {
                                    BoosterSpeed tmps = new BoosterSpeed();
                                    bpc.boosterSpeeds.add(tmps);
                                } else
                                    bpc.pieces[bpc.boosterSpeeds.get(k).getyInBoard()][bpc.boosterSpeeds.get(k).getxInBoard()].setSpeedboost(false);

                                bpc.boosterSpeeds.get(k).setX(Integer.parseInt(sc1.next()));
                                bpc.boosterSpeeds.get(k).setY(Integer.parseInt(sc1.next()));
                                bpc.boosterSpeeds.get(k).setxInBoard(Integer.parseInt(sc1.next()));
                                bpc.boosterSpeeds.get(k).setyInBoard(Integer.parseInt(sc1.next()));
                                bpc.pieces[bpc.boosterSpeeds.get(k).getyInBoard()][bpc.boosterSpeeds.get(k).getxInBoard()].setSpeedboost(true);
                                break;
                            case "ds":
                                k = Integer.parseInt(sc1.next());
                                if (first_time[0]) {
                                    DisBoosterSpeed tmpds = new DisBoosterSpeed();
                                    bpc.disBoosterSpeeds.add(tmpds);
                                } else
                                    bpc.pieces[bpc.disBoosterSpeeds.get(k).getyInBoard()][bpc.disBoosterSpeeds.get(k).getxInBoard()].setSpeeddisboost(false);

                                bpc.disBoosterSpeeds.get(k).setX(Integer.parseInt(sc1.next()));
                                bpc.disBoosterSpeeds.get(k).setY(Integer.parseInt(sc1.next()));
                                bpc.disBoosterSpeeds.get(k).setxInBoard(Integer.parseInt(sc1.next()));
                                bpc.disBoosterSpeeds.get(k).setyInBoard(Integer.parseInt(sc1.next()));
                                bpc.pieces[bpc.disBoosterSpeeds.get(k).getyInBoard()][bpc.disBoosterSpeeds.get(k).getxInBoard()].setSpeeddisboost(true);
                                break;
                            case "c":
                                k = Integer.parseInt(sc1.next());
                                if (first_time[0]) {
                                    BoosterControlBomb tmpc = new BoosterControlBomb();
                                    bpc.boosterControlBombs.add(tmpc);
                                } else
                                    bpc.pieces[bpc.boosterControlBombs.get(k).getyInBoard()][bpc.boosterControlBombs.get(k).getxInBoard()].setControlBomb(false);

                                bpc.boosterControlBombs.get(k).setX(Integer.parseInt(sc1.next()));
                                bpc.boosterControlBombs.get(k).setY(Integer.parseInt(sc1.next()));
                                bpc.boosterControlBombs.get(k).setxInBoard(Integer.parseInt(sc1.next()));
                                bpc.boosterControlBombs.get(k).setyInBoard(Integer.parseInt(sc1.next()));
                                bpc.pieces[bpc.boosterControlBombs.get(k).getyInBoard()][bpc.boosterControlBombs.get(k).getxInBoard()].setControlBomb(true);
                                break;
                            case "i":
                                k = Integer.parseInt(sc1.next());
                                if (first_time[0]) {
                                    BoosterIncreaseBomb tmpi = new BoosterIncreaseBomb();
                                    bpc.boosterIncreaseBombs.add(tmpi);
                                } else
                                    bpc.pieces[bpc.boosterIncreaseBombs.get(k).getyInBoard()][bpc.boosterIncreaseBombs.get(k).getxInBoard()].setIncreasebomb(false);

                                bpc.boosterIncreaseBombs.get(k).setX(Integer.parseInt(sc1.next()));
                                bpc.boosterIncreaseBombs.get(k).setY(Integer.parseInt(sc1.next()));
                                bpc.boosterIncreaseBombs.get(k).setxInBoard(Integer.parseInt(sc1.next()));
                                bpc.boosterIncreaseBombs.get(k).setyInBoard(Integer.parseInt(sc1.next()));
                                bpc.pieces[bpc.boosterIncreaseBombs.get(k).getyInBoard()][bpc.boosterIncreaseBombs.get(k).getxInBoard()].setIncreasebomb(true);
                                break;
                            case "di":
                                k = Integer.parseInt(sc1.next());
                                if (first_time[0]) {
                                    DisBoosterDecreaseBomb tmpdi = new DisBoosterDecreaseBomb();
                                    bpc.disBoosterDecreaseBombs.add(tmpdi);
                                } else
                                    bpc.pieces[bpc.disBoosterDecreaseBombs.get(k).getyInBoard()][bpc.disBoosterDecreaseBombs.get(k).getxInBoard()].setDecreasebomb(false);

                                bpc.disBoosterDecreaseBombs.get(k).setX(Integer.parseInt(sc1.next()));
                                bpc.disBoosterDecreaseBombs.get(k).setY(Integer.parseInt(sc1.next()));
                                bpc.disBoosterDecreaseBombs.get(k).setxInBoard(Integer.parseInt(sc1.next()));
                                bpc.disBoosterDecreaseBombs.get(k).setyInBoard(Integer.parseInt(sc1.next()));
                                bpc.pieces[bpc.disBoosterDecreaseBombs.get(k).getyInBoard()][bpc.disBoosterDecreaseBombs.get(k).getxInBoard()].setDecreasebomb(true);
                                break;
                            case "r":
                                k = Integer.parseInt(sc1.next());
                                if (first_time[0]) {
                                    BoosterIncreaseRadius tmpr = new BoosterIncreaseRadius();
                                    bpc.boosterIncreaseRadii.add(tmpr);
                                } else
                                    bpc.pieces[bpc.boosterIncreaseRadii.get(k).getyInBoard()][bpc.boosterIncreaseRadii.get(k).getxInBoard()].setHasbombboost(false);

                                bpc.boosterIncreaseRadii.get(k).setX(Integer.parseInt(sc1.next()));
                                bpc.boosterIncreaseRadii.get(k).setY(Integer.parseInt(sc1.next()));
                                bpc.boosterIncreaseRadii.get(k).setxInBoard(Integer.parseInt(sc1.next()));
                                bpc.boosterIncreaseRadii.get(k).setyInBoard(Integer.parseInt(sc1.next()));
                                bpc.pieces[bpc.boosterIncreaseRadii.get(k).getyInBoard()][bpc.boosterIncreaseRadii.get(k).getxInBoard()].setHasbombboost(true);
                                break;
                            case "dr":
                                k = Integer.parseInt(sc1.next());
                                if (first_time[0]) {
                                    DisBoosterDecreaseRadius tmpdr = new DisBoosterDecreaseRadius();
                                    bpc.disBoosterDecreasRadii.add(tmpdr);
                                } else
                                    bpc.pieces[bpc.disBoosterDecreasRadii.get(k).getyInBoard()][bpc.disBoosterDecreasRadii.get(k).getxInBoard()].setHasdisbombboost(false);

                                bpc.disBoosterDecreasRadii.get(k).setX(Integer.parseInt(sc1.next()));
                                bpc.disBoosterDecreasRadii.get(k).setY(Integer.parseInt(sc1.next()));
                                bpc.disBoosterDecreasRadii.get(k).setxInBoard(Integer.parseInt(sc1.next()));
                                bpc.disBoosterDecreasRadii.get(k).setyInBoard(Integer.parseInt(sc1.next()));
                                bpc.pieces[bpc.disBoosterDecreasRadii.get(k).getyInBoard()][bpc.disBoosterDecreasRadii.get(k).getxInBoard()].setHasdisbombboost(true);
                                break;
                            case "break":
                                int x1 = Integer.parseInt(sc1.next());
                                int y1 = Integer.parseInt(sc1.next());
                                bpc.pieces[y1][x1].setHasbreak(true);
                                break;
                            case "step1":
                                first_time[0] = false;
                                break;
                            case "chngstep":
                                first_time[0] = true;
                                bpc.resetDetails2changLvl();
                                break;
                            case "bomb":
                                double owner = Double.parseDouble(sc1.next());
                                int radius = Integer.parseInt(sc1.next());
                                long start_time = Long.parseLong(sc1.next());
                                x = Integer.parseInt(sc1.next());
                                y = Integer.parseInt(sc1.next());
                                x11 = Integer.parseInt(sc1.next());
                                y11 = Integer.parseInt(sc1.next());
                                int bmbtmp2 = findingwithxy(x11, y11, bpc.bomb);
                                int bmbtmp = findingwithxy(x11, y11, bpc.otherbomb);
                                if (bmbtmp == -1 && bmbtmp2 == -1) {
                                    Bomb bombtmp = new Bomb(radius);
                                    bombtmp.setOwnerId(owner);
                                    bombtmp.setStart_time(start_time);
                                    bombtmp.setX(x);
                                    bombtmp.setY(y);
                                    bombtmp.setxInBoard(x11);
                                    bombtmp.setyInBoard(y11);
                                    bpc.bmb = true;
                                    bpc.otherbomb.add(bombtmp);
                                }
                                break;
                            case "bomblastexpl":
                                if (bpc.otherbomb.size() != 0)
                                    bpc.bombexpluding(bpc.otherbomb, bpc.otherbomb.get(0));
                                break;
                            case "dltbomberman":
                                double bmbmanid = Double.parseDouble(sc1.next());
                                k = findingbbman(bmbmanid, bpc.otherbomBombermen);
                                if (k >= 0)
                                    bpc.otherbomBombermen.remove(k);
                                break;
                            case "massage":
                                String firstword = sc1.next();
                                while (!firstword.equals("end")) {
                                    Double mssgeid = Double.parseDouble(firstword);
                                    String name = sc1.next();
                                    String text = sc1.nextLine();
                                    if (mssgeid != bpc.gameInfo.chatFrame.id) {
                                        Massage massage = new Massage(name, text, mssgeid);
                                        bpc.gameInfo.massages.add(massage);
                                        bpc.gameInfo.chatFrame.recievemassage(massage);
                                    }
                                    firstword = sc1.next();
                                }
                                break;
                            default:
                                System.out.println("kir b " + temp);
                                break;
                        }
                    }

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        serverlistener.start();

    }

    public static void main(String[] args) {

        try {
            Client client = new Client();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public int finding(double id, ArrayList<? extends enemy> a) {

        for (int i = 0; i < a.size(); i++) {

            if (id == a.get(i).getId()) {
                return i;
            }
        }

        return -1;//its never gonna happen

    }

    public int findingwithxy(int x, int y, ArrayList<? extends Stuff> a) {

        for (int i = 0; i < a.size(); i++) {

            if (a.get(i).getxInBoard() == x && a.get(i).getyInBoard() == y) {
                return i;
            }
        }

        return -1;//its never gonna happen

    }

    public int findingbbman(double id, ArrayList<Bomberman> a) {

        for (int i = 0; i < a.size(); i++) {

            if (id == a.get(i).getId()) {
                return i;
            }
        }

        return -1;//its never gonna happen

    }

}
