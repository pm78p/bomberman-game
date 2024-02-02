import com.sun.jmx.snmp.tasks.ThreadService;
import com.sun.security.ntlm.Server;
import com.sun.xml.internal.ws.api.config.management.policy.ManagementAssertion;
import javafx.embed.swing.JFXPanel;

import javax.swing.*;
import javax.xml.crypto.dom.DOMCryptoContext;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class ServerFrame extends JFrame {

    int tool;
    int arz;
    int port;
    int maxplayer;
    BoardPServer bps = null;
    boolean explud = false;
    int threadnumber = -1;
    ArrayList<ServerThread> threads = new ArrayList<>();
    ArrayList<Bomberman> bombermen = new ArrayList<>();
    ArrayList<Massage> massages = new ArrayList<>();
    ArrayList<Massage> adminmassages = new ArrayList<>();
    int massage_added = 0;

    public ServerFrame() throws IOException {
        this.tool = 27;
        this.arz = 15;
        this.port = 8090;
        this.maxplayer = 4;
        init();
    }

    public ServerFrame(int tool, int arz, int port, int maxplayer) throws IOException {
        this.tool = tool;
        this.arz = arz;
        this.port = port;
        this.maxplayer = maxplayer;
        init();
    }

    public void init() throws IOException {

        this.setBounds(0, 0, 1725, 935);
        this.setFocusTraversalKeysEnabled(false);
        this.setTitle("server: " + port);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JPanel center = new JPanel();
        center.setBackground(Color.blue);
        GameInfo gameInfo = new GameInfo(true, bombermen, "Server");

        bps = new BoardPServer(gameInfo, tool, arz);
        ServerFrame.this.add(gameInfo, BorderLayout.EAST);
        ServerFrame.this.add(center, BorderLayout.CENTER);
        ServerFrame.this.add(bps, BorderLayout.CENTER);
        bps.setVisible(true);
        ServerFrame.this.setVisible(true);

        ServerSocket s1 = new ServerSocket(port);

        final String[] allmassage = {""};

        Thread chat = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    for (int i = 0; i < threads.size(); i++) {
                        if(threads.get(i).ss.isClosed()){
                            threads.get(i).close();
                            threads.remove(i);
                        }
                    }

                    if (gameInfo.chatFrame.massages.size() != 0) {
                        int i = gameInfo.chatFrame.massages.size() - 1;
                        while (adminmassages.size() != gameInfo.chatFrame.massages.size() && i != -1) {
                            Massage massage = gameInfo.chatFrame.massages.get(i--);
                            adminmassages.add(massage);
                            massages.add(massage);
                            massage_added++;
                        }
                    }

                    allmassage[0] = "massage ";
                    for (int i = 0; i < massage_added; i++) {

                        if (massages.get(massages.size() - 1 - i).getId() != gameInfo.chatFrame.id) {
                            Massage massage = massages.get(massages.size() - 1 - i);
                            gameInfo.massages.add(massage);
                            gameInfo.chatFrame.recievemassage(massage);
                        }

                        allmassage[0] += massages.get(massages.size() - 1 - i).toString();
                        massage_added--;
                    }

                    if (!allmassage[0].equals("massage ")) {
                        allmassage[0] += "end ";
                        for (int i = 0; i < threads.size(); i++) {
                            threads.get(i).p.println(allmassage[0]);
                        }
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        chat.start();

        Thread banchecker = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    for (int i = 0; i < gameInfo.dltbomberman.size(); i++) {
                        double id = gameInfo.dltbomberman.get(i);
                        int k = finding(id, bps.bombermen);
                        int numberinthread = finding(id, bombermen);
                        try {
                            for (int j = 0; j < threads.size(); j++) {
                                if (k != j)
                                    threads.get(j).p.println("dltbomberman " + id);
                            }
                            threads.get(k).ss.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (k >= 0)
                            bps.bombermen.remove(k);
                        gameInfo.dltbomberman.remove(i);
                    }

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        banchecker.start();

        while (true) {
            if (threads.size() != maxplayer) {
                Socket ss = s1.accept();
                ServerThread sv = new ServerThread(ss, this, threads.size());
                threads.add(sv);
                sv.start();
            }
        }

    }

    public int finding(double id, ArrayList<Bomberman> a) {

        for (int i = 0; i < a.size(); i++) {

            if (id == a.get(i).getId()) {
                return i;
            }
        }

        return -1;//its never gonna happen

    }

}

class ServerThread extends Thread {

    Socket ss = null;
    HashMap<Double, String> bomberman = new HashMap<>();
    HashMap<Double, String> enemies = new HashMap<>();
    HashMap<Long, String> bomb = new HashMap<>();
    ArrayList<String> boosterIncreaseRadii = new ArrayList<>();
    ArrayList<String> disBoosterDecreasRadii = new ArrayList<>();
    ArrayList<String> boosterIncreaseBombs = new ArrayList<>();
    ArrayList<String> disBoosterDecreaseBombs = new ArrayList<>();
    ArrayList<String> boosterControlBombs = new ArrayList<>();
    ArrayList<String> boosterSpeeds = new ArrayList<>();
    ArrayList<String> boosterPoints = new ArrayList<>();
    ArrayList<String> disBoosterPoints = new ArrayList<>();
    ArrayList<String> disBoosterSpeeds = new ArrayList<>();
    ArrayList<String> boosterGhosts = new ArrayList<>();
    int boostercounter = 0;
    BoardPServer bps = null;
    boolean addbmbman = false;
    int number;
    ServerFrame sf = null;
    PrintStream p = null;
    boolean sendchrno = true;

    public ServerThread(Socket ss, ServerFrame sf, int number) {
        this.number = number;
        this.sf = sf;
        this.ss = ss;
        this.bps = sf.bps;
    }

    @Override
    public void run() {

        Scanner sc = null;
        try {
            sc = new Scanner(ss.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            p = new PrintStream(ss.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        final int[] count = {0};

        PrintStream finalP = p;
        Thread boardUpdater = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    ArrayList<String> list2printstream = new ArrayList<>();
                    if (bps.changelvl[0] && bps.changelvl[1]) {
                        resetDetails2changLvl();
                        finalP.println("chngstep ");
                        writer(true, list2printstream);
                    }
                    if (count[0] == 0) {
                        count[0]++;
                        writer(true, list2printstream);
                    } else if (!bps.changelvl[0])
                        writer(false, list2printstream);

                    for (int i = 0; i < list2printstream.size(); i++) {
                        finalP.println(list2printstream.get(i) + " ");
                    }

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Scanner finalSc = sc;
        Thread clientslistener = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (finalSc.hasNext()) {
                        String actionfromclient = finalSc.next();
                        switch (actionfromclient) {
                            case "move":
                                double id = finalSc.nextDouble();
                                int k = finding(id, bps.bombermen);
                                if (k >= 0) {
                                    Bomberman bomberman = bps.bombermen.get(k);
                                    bps.pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].setHasbomberman(false);
                                    int x = finalSc.nextInt();
                                    int y = finalSc.nextInt();
                                    int x1 = finalSc.nextInt();
                                    int y1 = finalSc.nextInt();
                                    bomberman.setX(x);
                                    bomberman.setY(y);
                                    bomberman.setxInBoard(x1);
                                    bomberman.setyInBoard(y1);
                                    bps.pieces[bomberman.getyInBoard()][bomberman.getxInBoard()].setHasbomberman(false);
                                }
                                break;
                            case "pbomb":
                                id = finalSc.nextDouble();
                                k = finding(id, bps.bombermen);
                                if (k >= 0) {
                                    Bomberman bomberman = bps.bombermen.get(k);
                                    bps.bombplanet(bomberman);
                                }
                                break;
                            case "lastbombexpl":
                                if (bps.bomb.size() != 0) {
                                    id = bps.bomb.get(0).getOwnerId();
                                    k = finding(id, bps.bombermen);
                                    if (k >= 0) {
                                        Bomberman bomberman = bps.bombermen.get(k);
                                        bps.bombexpluding(bps.bomb.get(0), bomberman);
                                        sf.explud = true;
                                        sf.threadnumber = ServerThread.this.number;
                                    }
                                }
                                break;
                            case "bomberman":
                                id = finalSc.nextDouble();
                                Bomberman bomberman = bps.makeBomberman(id);
                                String alive = "";
                                if (bomberman.isAlive())
                                    alive = "y";
                                else
                                    alive = "n";
                                String tmpb = "m " + bomberman.getId() + " " + alive + " " + bomberman.getX() + " " + bomberman.getY() + " " + bomberman.getxInBoard() + " " + bomberman.getyInBoard() + " ";
                                ServerThread.this.bomberman.put(bomberman.getId(), tmpb);
                                finalP.println(tmpb);
                                sf.bombermen.add(bomberman);
                                addbmbman = true;
                                break;
                            case "massage":
                                sf.massage_added++;
                                Double mssgid = Double.parseDouble(finalSc.next());
                                String name = finalSc.next();
                                String text = finalSc.nextLine();
                                Massage massage = new Massage(name, text, mssgid);
                                sf.massages.add(massage);
                                break;

                        }

                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        Thread starterThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (bps.play) {
                        boardUpdater.start();
                        clientslistener.start();
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

        starterThread.start();

    }

    public void close(){ return;}

    public void writer(boolean flag, ArrayList<String> list2print) {

        if (ServerThread.this.sendchrno) {
            p.println("chonometr " + bps.GameLevel + " " + bps.chornometr.saveString());
            ServerThread.this.sendchrno = false;
        }

        for (int i = 0; i < bps.bombermen.size(); i++) {
            Bomberman bomberman = bps.bombermen.get(i);
            if (bomberman.getX() != 0) {
                String alive = "";
                if (bomberman.isAlive())
                    alive = "y";
                else
                    alive = "n";
                String tmpb = "m " + bomberman.getId() + " " + alive + " " + bomberman.getX() + " " + bomberman.getY() + " " + bomberman.getxInBoard() + " " + bomberman.getyInBoard() + " ";
                if (!this.bomberman.containsKey(bomberman.getId())) {
                    this.bomberman.put(bomberman.getId(), tmpb);
                    list2print.add(tmpb);
                } else if (this.bomberman.containsKey(bomberman.getId()) && !this.bomberman.get(bomberman.getId()).equals(tmpb)) {
                    this.bomberman.replace(bomberman.getId(), this.bomberman.get(bomberman.getId()), tmpb);
                    list2print.add(tmpb);
                }
            }
        }

        for (int i = 0; i < bps.enemies.size(); i++) {
            enemy enemy = bps.enemies.get(i);
            String tmp = "e" + " " + enemy.getId() + " " + enemy.getLvl() + " " + enemy.getX() + " " + enemy.getY() + " " + enemy.getxInBoard() + " " + enemy.getyInBoard() + " ";
            if (flag) {
                enemies.put(enemy.getId(), tmp);
                list2print.add(tmp);
            } else if (!enemies.get(enemy.getId()).equals(tmp)) {
                enemies.replace(enemy.getId(), enemies.get(enemy.getId()), tmp);
                list2print.add(tmp);
            }
        }

        int counter = 0;

        for (int i = 0; i < bps.breakpiece2server.size(); i++) {
            Piece tmppiec = bps.breakpiece2server.get(i);
            String tmp = "break " + tmppiec.getxInBoard() + " " + tmppiec.getyInBoard() + " ";
            if (flag) {
                if (tmppiec.isHasbreak()) {
                    counter++;
                    list2print.add(tmp);
                }
            }
        }

        for (int i = 0; i < bps.bomb.size(); i++) {
            Bomb bomb = bps.bomb.get(i);
            String tmp = "bomb " + bomb.getOwnerId() + " " + bomb.getBombRadius() + " " + bomb.getStart_time() + " "
                    + bomb.getX() + " " + bomb.getY() + " " + bomb.getxInBoard() + " " + bomb.getyInBoard() + " ";
            if (!this.bomb.containsKey(bomb.getStart_time())) {
                this.bomb.put(bomb.getStart_time(), tmp);
                list2print.add(tmp);
            }
        }

        if (sf.explud) {
            for (int i = 0; i < sf.threads.size(); i++) {
                if (i != sf.threadnumber) {
                    sf.threads.get(i).p.println("bomblastexpl ");
                }
            }
            sf.explud = false;
        }

        boostersave("g", list2print, bps.boosterGhosts, boosterGhosts, flag);
        boostersave("p", list2print, bps.boosterPoints, boosterPoints, flag);
        boostersave("dp", list2print, bps.disBoosterPoints, disBoosterPoints, flag);
        boostersave("s", list2print, bps.boosterSpeeds, boosterSpeeds, flag);
        boostersave("ds", list2print, bps.disBoosterSpeeds, disBoosterSpeeds, flag);
        boostersave("c", list2print, bps.boosterControlBombs, boosterControlBombs, flag);
        boostersave("i", list2print, bps.boosterIncreaseBombs, boosterIncreaseBombs, flag);
        boostersave("di", list2print, bps.disBoosterDecreaseBombs, disBoosterDecreaseBombs, flag);
        boostersave("r", list2print, bps.boosterIncreaseRadii, boosterIncreaseRadii, flag);
        boostersave("dr", list2print, bps.disBoosterDecreasRadii, disBoosterDecreasRadii, flag);

        if (flag) {
            bps.changelvl = new boolean[]{false, false};
            list2print.add("step1 ");
        }

    }

    public void boostersave(String name, ArrayList<String> list2printstream, ArrayList<? extends
            Stuff> boosterlist, ArrayList<String> StringOFboosters, boolean flag) {
        for (int i = 0; i < boosterlist.size(); i++) {
            Stuff tmpboost = boosterlist.get(i);
            String tmp = name + " " + i + " " + tmpboost.getX() + " " + tmpboost.getY() + " " + tmpboost.getxInBoard() + " " + tmpboost.getyInBoard() + " ";
            if (flag) {
                boostercounter++;
                StringOFboosters.add(tmp);
                list2printstream.add(tmp);
            }
        }
    }

    public void resetDetails2changLvl() {

        while (enemies.size() != 0)
            enemies.clear();

        while (bomb.size() != 0)
            bomb.clear();

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

    }

    public int finding(double id, ArrayList<Bomberman> a) {

        for (int i = 0; i < a.size(); i++) {

            if (id == a.get(i).getId()) {
                return i;
            }
        }

        return -1;//its never gonna happen

    }

}
