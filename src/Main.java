import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.*;

public class Main extends JFrame {

    int count = 0;
    JPanel panel;
    Path currentRelativePath = Paths.get("");
    String thispath = currentRelativePath.toAbsolutePath().toString();
    ArrayList<String> connectionsIP = new ArrayList<>();
    ArrayList<Socket> connectionSockets = new ArrayList<>();

    public Main(boolean musicb) {

        this.setTitle("Boomberman");
        this.setBounds(0, 0, 1725, 935);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        panel = new JPanel() {
            public void paintComponent(Graphics g) {
                Image img = Toolkit.getDefaultToolkit().getImage(thispath + "/src/media5gif.gif");
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };

        Thread msc = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {

                    playmusic();

                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        if (count == 0 && musicb)
            msc.start();
        count++;

        panel.setLayout(null);

        JButton playgame = new JButton();
        JButton info = new JButton();
        JButton exit = new JButton();

        playgame.setText("play");
        info.setText("information");
        exit.setText("EXIT");

        panel.add(playgame);
        panel.add(info);
        panel.add(exit);

        playgame.setBounds(650, 300, 100, 100);
        info.setBounds(650 + 130, 300, 100, 100);
        exit.setBounds(650 + 2 * 130, 300, 100, 100);

        JPanel playgamepanel = new JPanel() {
            public void paintComponent(Graphics g) {
                Image img = Toolkit.getDefaultToolkit().getImage(thispath + "/src/media5gif.gif");
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };

        playgamepanel.setLayout(null);

        JButton newGame = new JButton();
        JButton loadGame = new JButton();
        JButton backInplay = new JButton();
        JButton multyplayer = new JButton();

        newGame.setText("new game");
        loadGame.setText("load game");
        backInplay.setText("back");
        multyplayer.setText("multyplayer");

        playgamepanel.add(newGame);
        playgamepanel.add(loadGame);
        playgamepanel.add(backInplay);
        playgamepanel.add(multyplayer);

        newGame.setBounds(650, 300, 100, 100);
        loadGame.setBounds(650 + 130, 300, 100, 100);
        multyplayer.setBounds(650 + 2 * 130, 300, 100, 100);
        backInplay.setBounds(650 + 3 * 130, 300, 100, 100);

        JPanel multypanel = new JPanel() {
            public void paintComponent(Graphics g) {
                Image img = Toolkit.getDefaultToolkit().getImage(thispath + "/src/media5gif.gif");
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };

        multypanel.setLayout(null);

        JTextField startIp = new JTextField();
        JTextField endIp = new JTextField();
        JTextField port = new JTextField();
        JTextField name = new JTextField();

        startIp.setBounds(680, 400, 100, 20);
        endIp.setBounds(800, 400, 100, 20);
        port.setBounds(960, 400, 60, 20);
        name.setBounds(800, 250, 100, 20);

        JTextField startIptip = new JTextField();
        JTextField endIptip = new JTextField();
        JTextField porttip = new JTextField();
        JTextField nametip = new JTextField();

        startIp.setText("");
        endIp.setText("");
        port.setText("");
        name.setText("");
        startIptip.setText("start ip");
        endIptip.setText("end ip");
        porttip.setText("port");
        nametip.setText("name");

        startIptip.setEditable(false);
        endIptip.setEditable(false);
        porttip.setEditable(false);
        nametip.setEditable(false);

        startIptip.setBounds(680, 370, 100, 20);
        endIptip.setBounds(800, 370, 100, 20);
        porttip.setBounds(960, 370, 60, 20);
        nametip.setBounds(800, 220, 100, 20);

        final String[] strtip = {""};
        final String[] ndip = {""};
        final String[] prtip = {""};

        startIp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                strtip[0] = startIp.getText();
            }
        });

        endIp.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ndip[0] = endIp.getText();
            }
        });

        port.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                prtip[0] = port.getText();
            }
        });

        JButton nextToServers = new JButton();
        nextToServers.setText("start");
        nextToServers.setBounds(790, 480, 100, 30);

        JButton backInmulty = new JButton();
        backInmulty.setText("back");
        backInmulty.setBounds(790, 540, 100, 30);

        multypanel.add(startIp);
        multypanel.add(endIp);
        multypanel.add(port);
        multypanel.add(startIptip);
        multypanel.add(endIptip);
        multypanel.add(porttip);
        multypanel.add(nextToServers);
        multypanel.add(backInmulty);
        multypanel.add(name);
        multypanel.add(nametip);

        final int[] xserver = {0};
        final int[] yserver = {0};

        JPanel serverspanel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                Image img = Toolkit.getDefaultToolkit().getImage(thispath + "/src/media5gif.gif");
                g2d.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);

                BufferedImage srvrs = null;
                try {
                    srvrs = ImageIO.read(new File(thispath + "/src/pictures/srvrbackground.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g2d.setComposite(AlphaComposite.SrcOver.derive((float) 0.95));
                xserver[0] = this.getWidth() / 2 - srvrs.getWidth() / 2;
                yserver[0] = this.getHeight() / 2 - srvrs.getHeight() / 2;
                g2d.drawImage(srvrs, xserver[0], yserver[0], this);

            }
        };

        serverspanel.setLayout(null);

        JPanel newgamepanel = new JPanel() {
            public void paintComponent(Graphics g) {
                Image img = Toolkit.getDefaultToolkit().getImage(thispath + "/src/media5gif.gif");
                g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);
            }
        };

        newgamepanel.setLayout(null);

        String[] list = new String[11];
        int i = 27;
        int m = 0;
        while (i > 5) {
            list[m++] = "" + i;
            i -= 2;
        }

        String[] list1 = new String[5];
        i = 15;
        m = 0;
        while (i > 5) {
            list1[m++] = "" + i;
            i -= 2;
        }

        JComboBox comboBox = new JComboBox(list);
        JComboBox comboBox1 = new JComboBox(list1);

        final String[] k = new String[1];
        k[0] = (String) comboBox.getSelectedItem();
        final String[] k1 = new String[1];
        k1[0] = (String) comboBox.getSelectedItem();

        final boolean[] tmp = {false};

        Thread thr = new Thread(new Runnable() {

            @Override
            public void run() {
                while (true) {
                    if (!k[0].equals((String) comboBox.getSelectedItem()))
                        k[0] = (String) comboBox.getSelectedItem();

                    if (!k1[0].equals((String) comboBox1.getSelectedItem()))
                        k1[0] = (String) comboBox1.getSelectedItem();

                    if (tmp[0])
                        return;

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        comboBox.setBounds(760, 400, 60, 30);
        comboBox1.setBounds(860, 400, 60, 30);
        newgamepanel.add(comboBox);
        newgamepanel.add(comboBox1);

        JTextField height = new JTextField();
        height.setText("height");
        height.setBounds(760, 370, 60, 20);
        height.setEditable(false);

        JTextField width = new JTextField();
        width.setText("width");
        width.setBounds(860, 370, 60, 20);
        width.setEditable(false);

        JTextField bull = new JTextField();
        bull.setText("chose the height and width of board");
        bull.setBounds(710, 270, 250, 40);
        bull.setEditable(false);

        newgamepanel.add(height);
        newgamepanel.add(width);
        newgamepanel.add(bull);

        JButton next = new JButton();
        next.setText("start");
        newgamepanel.add(next);
        next.setBounds(790, 480, 100, 30);

        JButton backInNewgame = new JButton();
        backInNewgame.setText("back");
        backInNewgame.setBounds(790, 540, 100, 30);
        newgamepanel.add(backInNewgame);

        JPanel infopanel = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);
                Image img1 = Toolkit.getDefaultToolkit().getImage(thispath + "/src/media5gif.gif");
                g.drawImage(img1, 0, 0, this.getWidth(), this.getHeight(), this);

            }

        };

        infopanel.setLayout(null);

        JButton keyboard = new JButton();
        JButton intru = new JButton();
        JButton backfrominfo = new JButton();

        keyboard.setText("keyboard");
        intru.setText("introduction with things");
        backfrominfo.setText("back");

        infopanel.add(intru);
        infopanel.add(keyboard);
        infopanel.add(backfrominfo);

        this.add(infopanel);

        keyboard.setBounds(680, 200, 300, 100);
        intru.setBounds(680, 350, 300, 100);
        backfrominfo.setBounds(680, 500, 300, 100);

        JPanel panelkey = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);
                BufferedImage img;
                try {
                    Image img1 = Toolkit.getDefaultToolkit().getImage(thispath + "/src/media5gif.gif");
                    g.drawImage(img1, 0, 0, this.getWidth(), this.getHeight(), this);
                    img = ImageIO.read(new File(thispath + "/src/pictures/keyboard.png"));
                    g.drawImage(img, 310, 140, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        };

        panelkey.setLayout(null);
        JButton backfromkey = new JButton();
        backfromkey.setText("back");
        panelkey.add(backfromkey);
        backfromkey.setBounds(800, 800, 100, 30);

        JPanel panelintru = new JPanel() {

            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);
                BufferedImage img;
                try {
                    Image img1 = Toolkit.getDefaultToolkit().getImage(thispath + "/src/media5gif.gif");
                    g.drawImage(img1, 0, 0, this.getWidth(), this.getHeight(), this);
                    img = ImageIO.read(new File(thispath + "/src/pictures/infopic.png"));
                    g.drawImage(img, 140, 50, null);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

        };

        panelintru.setLayout(null);
        JButton backfromintro = new JButton();
        backfromintro.setText("back");
        panelintru.add(backfromintro);
        backfromintro.setBounds(810, 800, 100, 30);

        thr.start();

        keyboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Main.this.remove(infopanel);
                Main.this.setContentPane(panelkey);
                Main.this.validate();

                backfromkey.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Main.this.remove(panelkey);
                        Main.this.setContentPane(infopanel);
                        Main.this.validate();
                    }
                });
            }
        });

        backfrominfo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Main.this.remove(infopanel);
                Main.this.setContentPane(panel);
                Main.this.validate();

            }
        });

        intru.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Main.this.remove(infopanel);
                Main.this.setContentPane(panelintru);
                Main.this.validate();

                backfromintro.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {

                        Main.this.remove(panelintru);
                        Main.this.setContentPane(infopanel);
                        Main.this.validate();

                    }
                });
            }
        });

        playgame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.this.remove(panel);
                Main.this.setContentPane(playgamepanel);
                Main.this.validate();
            }
        });

        info.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Main.this.remove(panel);
                Main.this.setContentPane(infopanel);
                Main.this.validate();

            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitGame();
            }
        });

        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                tmp[0] = true;
                Main.this.dispose();
                int he = Integer.parseInt(k[0]);
                int wi = Integer.parseInt(k1[0]);
                GamePlay gp = new GamePlay(he, wi);
                gp.setVisible(true);

            }
        });

        loadGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                LoadFrame loadFrame = new LoadFrame();
                String path = loadFrame.getPath();

                if (!path.equals("")) {
                    FileInputStream fis = null;
                    ObjectInputStream in = null;
                    try {
                        Main.this.dispose();
                        GamePlay gp = new GamePlay();
                        fis = new FileInputStream(path);
                        in = new ObjectInputStream(fis);
                        gp.boardPSingle.replace2this((BoardPSingle) in.readObject(), true);
                        in.close();
                        gp.setVisible(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

            }
        });

        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.this.remove(playgame);
                Main.this.setContentPane(newgamepanel);
                Main.this.validate();
            }
        });

        backInplay.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.this.remove(playgamepanel);
                Main.this.setContentPane(panel);
                Main.this.validate();
            }
        });

        backInNewgame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Main.this.remove(newgamepanel);
                Main.this.setContentPane(playgamepanel);
                Main.this.validate();
            }
        });

        multyplayer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Main.this.remove(playgame);
                Main.this.setContentPane(multypanel);
                Main.this.validate();
            }
        });

        backInmulty.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Main.this.remove(multypanel);
                Main.this.setContentPane(playgamepanel);
                Main.this.validate();
            }
        });

        nextToServers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                if (!strtip[0].equals("") && !ndip[0].equals("") && !prtip[0].equals("") && !name.getText().equals("")) {
                    int port = Integer.parseInt(prtip[0]);
                    connectionfinder(strtip[0], ndip[0], port);

                    Main.this.remove(multypanel);
                    Main.this.setContentPane(serverspanel);
                    Main.this.validate();
                    serverspanel.setLayout(null);

                    JTextField Ip = new JTextField();
                    JTextField port2 = new JTextField();

                    Ip.setText("0.0.0.0");
                    port2.setText("0000");
                    Ip.setForeground(Color.yellow);
                    port2.setForeground(Color.yellow);
                    Ip.setOpaque(false);
                    port2.setOpaque(false);

                    final String[] ip2 = {"0.0.0.0"};
                    final int[] prt2ip = {0};

                    Ip.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            ip2[0] = Ip.getText();
                        }
                    });

                    port2.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            prt2ip[0] = Integer.parseInt(port2.getText());
                        }
                    });

                    JTextField emtpy = null;
                    if (connectionsIP.size() == 0) {
                        emtpy = new JTextField();
                        emtpy.setText("no servers found");
                        emtpy.setBounds(800, 250, 110, 20);
                        emtpy.setOpaque(false);
                        emtpy.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.DARK_GRAY));
                        emtpy.setEditable(false);
                        emtpy.setForeground(Color.yellow);
                        serverspanel.add(emtpy);
                        emtpy.setVisible(true);
                        Ip.setBounds(925 / 2 + 15, 25 + 75 + 385 / 2, 100, 25);
                        port2.setBounds(925 / 2 + 15 + 120, 25 + 75 + 385 / 2, 50, 25);
                    } else {
                        int socketnumber = connectionsIP.size();
                        Ip.setBounds(925 / 2 + 15, 25 + 75 + 385 / 2 + socketnumber * 30, 100, 25);
                        port2.setBounds(925 / 2 + 15 + 120, 25 + 75 + 385 / 2 + socketnumber * 30, 50, 25);
                    }

                    JButton search = new JButton();
                    search.setText("Search");
                    search.setBounds(925 / 2 + 515, Ip.getY(), 80, 25);
                    search.setVisible(true);
                    serverspanel.add(search);

                    JTextField finalEmtpy = emtpy;
                    search.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                            if (!ip2[0].equals("0.0.0.0") && prt2ip[0] != 0) {
                                try {
                                    Socket s = new Socket(ip2[0], prt2ip[0]);
                                    search.setVisible(false);
                                    serverspanel.remove(search);
                                    JButton join = new JButton();
                                    join.setText("Join");
                                    join.setBounds(925 / 2 + 515, Ip.getY(), 80, 25);
                                    serverspanel.add(join);
                                    finalEmtpy.setVisible(false);
                                    serverspanel.remove(finalEmtpy);
                                    join.addActionListener(new ActionListener() {
                                        @Override
                                        public void actionPerformed(ActionEvent e) {
                                            Main.this.dispose();
                                            try {
                                                Client client = new Client(s,name.getText());
                                            } catch (IOException e1) {
                                                e1.printStackTrace();
                                            }
                                        }
                                    });
                                    join.setVisible(true);

                                } catch (IOException e1) {
                                }
                            }
                        }
                    });


                    port2.setVisible(true);
                    Ip.setVisible(true);
                    serverspanel.add(Ip);
                    serverspanel.add(port2);

                    for (int j = 0; j < connectionsIP.size(); j++) {
                        JTextField tmp = new JTextField();
                        tmp.setEditable(false);
                        tmp.setForeground(Color.yellow);
                        tmp.setText((j + 1) + "- " + connectionsIP.get(j) + ":" + port);
                        tmp.setOpaque(false);
                        tmp.setBounds(925 / 2 + 15, 75 + 385 / 2 + j * 30, 130, 25);
                        serverspanel.add(tmp);
                        tmp.setVisible(true);
                    }

                    for (int j = 0; j < connectionsIP.size(); j++) {
                        JButton join = new JButton();
                        JButton visit = new JButton();

                        join.setText("Join");
                        visit.setText("Visit");

                        join.setBounds(925 / 2 + 515, 75 + 385 / 2 + j * 30, 60, 25);
                        visit.setBounds(925 / 2 + 515 + 75, 75 + 385 / 2 + j * 30, 60, 25);

                        join.setVisible(true);
                        visit.setVisible(true);
                        serverspanel.add(join);
                        serverspanel.add(visit);

                        int finalJ = j;
                        join.addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                Main.this.dispose();
                                try {
                                    Client client = new Client(connectionSockets.get(finalJ),name.getText());
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                            }
                        });

                    }


                }
            }
        });

        this.setContentPane(panel);

    }

    public void connectionfinder(String start, String end, int port) {

        String tmp = start;
        while (!tmp.equals(end)) {
            try {
                Socket s = new Socket(tmp, port);
                if (connectionsIP.size() != 0 && !connectionsIP.get(connectionsIP.size() - 1).equals(tmp)) {
                    connectionsIP.add(tmp);
                    connectionSockets.add(s);
                } else if (connectionsIP.size() == 0) {
                    connectionsIP.add(tmp);
                    connectionSockets.add(s);
                }
            } catch (IOException e) {
                if (tmp.charAt(tmp.length() - 2) == '.') {
                    int number1 = Integer.parseInt(String.valueOf(tmp.charAt(tmp.length() - 1))) + 1;
                    tmp = tmp.substring(0, tmp.length() - 1) + number1;
                } else if (tmp.charAt(tmp.length() - 3) == '.') {
                    int number1 = Integer.parseInt(tmp.substring(tmp.length() - 2, tmp.length())) + 1;
                    tmp = tmp.substring(0, tmp.length() - 2) + number1;
                } else if (tmp.charAt(tmp.length() - 4) == '.') {
                    int number1 = Integer.parseInt(tmp.substring(tmp.length() - 3, tmp.length())) + 1;
                    if (number1 == 226) {
                        if (tmp.charAt(tmp.length() - 6) == '.') {
                            int number2 = Integer.parseInt(String.valueOf(tmp.charAt(tmp.length() - 5))) + 1;
                            tmp = tmp.substring(0, tmp.length() - 5) + number2 + "." + 0;
                        } else if (tmp.charAt(tmp.length() - 7) == '.') {
                            int number2 = Integer.parseInt(tmp.substring(tmp.length() - 6, tmp.length() - 4)) + 1;
                            tmp = tmp.substring(0, tmp.length() - 6) + number2 + "." + 0;
                        } else if (tmp.charAt(tmp.length() - 8) == '.') {
                            int number2 = Integer.parseInt(tmp.substring(tmp.length() - 7, tmp.length() - 4)) + 1;
                            tmp = tmp.substring(0, tmp.length() - 7) + number2 + "." + 0;
                        }
                    } else {
                        tmp = tmp.substring(0, tmp.length() - 4) + "." + number1;
                    }
                }
            }
        }

    }

    public void playmusic() {

        ArrayList<FileInputStream> fis = new ArrayList<>();
        try {
            fis.add(new FileInputStream(thispath + "/src/music/01 Nocturne (1).mp3"));
            fis.add(new FileInputStream(thispath + "/src/music/01 - 2+2=5 (The Lukewarm.).mp3"));
            fis.add(new FileInputStream(thispath + "/src/music/2-10 The Beginning And The End.mp3"));
            fis.add(new FileInputStream(thispath + "/src/music/11. The Conflagration.mp3"));
            fis.add(new FileInputStream(thispath + "/src/music/Thurisaz – Endless....mp3"));
            fis.add(new FileInputStream(thispath + "/src/music/01. Main Title Theme – Westworld.mp3"));
            int i = 2;
            while (i < fis.size()) {
                Player player = new Player(fis.get(i++));
                player.play();
                if (i == fis.size())
                    i = 0;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (JavaLayerException e) {
            e.printStackTrace();

        }

    }

    public void exitGame() {
        System.exit(0);
    }

    public static void main(String[] args) {
        Main newMain = new Main(true);
        newMain.setVisible(true);
    }
}
