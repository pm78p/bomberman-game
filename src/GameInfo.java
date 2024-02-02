import oracle.jrockit.jfr.JFR;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class GameInfo extends JPanel {

    JTextField score = new JTextField();
    JTextField time = new JTextField();
    JTextField level = new JTextField();
    JTextField bomb = new JTextField();
    JTextField bomb_radius = new JTextField();
    JTextField scr_tex = new JTextField();
    JTextField tim_tex = new JTextField();
    JTextField lvl_tex = new JTextField();
    JTextField bmb_tex = new JTextField();
    JTextField brd_tex = new JTextField();

    JButton play = new JButton();
    JButton pause = new JButton();
    JButton exit = new JButton();
    JButton information = new JButton();
    JButton chat = new JButton();

    boolean stop = false;
    boolean exitbool = false;
    boolean server = false;
    boolean client = false;
    ArrayList<Bomberman> bombermen = new ArrayList<>();
    ArrayList<Double> dltbomberman = new ArrayList<>();
    ArrayList<Massage> massages = new ArrayList<>();
    ChatFrame chatFrame;

    public GameInfo(boolean server, ArrayList<Bomberman> bombermen,String name) {
        chatFrame = new ChatFrame(name);
        this.bombermen = bombermen;
        this.server = server;
        init();
    }

    public GameInfo() {
        init();
    }

    public GameInfo(boolean client,String name) {
        chatFrame = new ChatFrame(name);
        this.client = client;
        init();
    }

    public void init() {

        Path currentRelativePath = Paths.get("");
        String thispath = currentRelativePath.toAbsolutePath().toString();

        this.setBackground(Color.BLUE);
        this.setPreferredSize(new Dimension(100, 100));
        this.setVisible(true);

        NewApplet tmp = new NewApplet();
        tmp.setPreferredSize(new Dimension(50, 10));
        NewApplet tmp1 = new NewApplet();
        tmp1.setPreferredSize(new Dimension(50, 20));
        NewApplet tmp2 = new NewApplet();
        tmp2.setPreferredSize(new Dimension(50, 20));
        NewApplet tmp3 = new NewApplet();
        tmp3.setPreferredSize(new Dimension(50, 20));
        NewApplet tmp4 = new NewApplet();
        tmp4.setPreferredSize(new Dimension(50, 20));

        tim_tex.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, Color.blue));
        tim_tex.setPreferredSize(new Dimension(45, 20));
        tim_tex.setOpaque(false);
        tim_tex.setForeground(Color.WHITE);
        tim_tex.setText(" time");
        tim_tex.setEditable(false);
        this.add(tim_tex);
        this.add(time);
        this.add(tmp);

        if (!server) {
            scr_tex.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, Color.blue));
            scr_tex.setPreferredSize(new Dimension(45, 20));
            scr_tex.setOpaque(false);
            scr_tex.setForeground(Color.WHITE);
            scr_tex.setText(" point");
            scr_tex.setEditable(false);
            lvl_tex.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, Color.blue));
            lvl_tex.setPreferredSize(new Dimension(45, 20));
            lvl_tex.setOpaque(false);
            lvl_tex.setForeground(Color.WHITE);
            lvl_tex.setText(" Level");
            lvl_tex.setEditable(false);
            bmb_tex.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, Color.blue));
            bmb_tex.setPreferredSize(new Dimension(85, 20));
            bmb_tex.setOpaque(false);
            bmb_tex.setForeground(Color.WHITE);
            bmb_tex.setText("bomb remains");
            bmb_tex.setEditable(false);
            brd_tex.setBorder(BorderFactory.createMatteBorder(0, 0, 5, 0, Color.blue));
            brd_tex.setPreferredSize(new Dimension(85, 20));
            brd_tex.setOpaque(false);
            brd_tex.setForeground(Color.WHITE);
            brd_tex.setText("bombs radius");
            brd_tex.setEditable(false);
            this.add(scr_tex);
            this.add(score);
            this.add(tmp1);
            this.add(lvl_tex);
            this.add(level);
            this.add(tmp2);
            this.add(bmb_tex);
            this.add(bomb);
            this.add(tmp3);
            this.add(brd_tex);
            this.add(bomb_radius);
            this.add(tmp4);
        }

        if (server || client) {

            BufferedImage chatimg = null;
            try {
                chatimg = ImageIO.read(new File(thispath + "/src/pictures/chat.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Graphics2D g2 = chatimg.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.BLACK);
            g2.drawImage(chatimg, 0, 0, null);
            g2.dispose();
            chat.setIcon(new ImageIcon(chatimg));
            this.add(chat);
        }

        if (server) {

            BufferedImage info = null;
            try {
                info = ImageIO.read(new File(thispath + "/src/pictures/information.jpg"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Graphics2D g2 = info.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.BLACK);
            g2.drawImage(info, 0, 0, null);
            g2.dispose();
            information.setIcon(new ImageIcon(info));
            this.add(information);
        }

        if (!client) {
            //
            BufferedImage animPlayer = null;
            try {
                animPlayer = ImageIO.read(new File(thispath + "/src/pictures/play.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            Graphics2D g2 = animPlayer.createGraphics();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.BLACK);
            g2.drawImage(animPlayer, 0, 0, null);
            g2.dispose();
            play.setIcon(new ImageIcon(animPlayer));
            this.add(play);

            //
            BufferedImage animPauser = null;
            try {
                animPauser = ImageIO.read(new File(thispath + "/src/pictures/pause.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            g2.drawImage(animPauser, 0, 0, null);
            g2.dispose();
            pause.setIcon(new ImageIcon(animPauser));
        }

        //
        BufferedImage exitimg = null;
        try {
            exitimg = ImageIO.read(new File(thispath + "/src/pictures/exit.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Graphics g2 = exitimg.createGraphics();
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.BLACK);
        g2.drawImage(exitimg, 0, 0, null);
        g2.dispose();
        exit.setIcon(new ImageIcon(exitimg));
        this.add(exit);

        information.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame infofram = new JFrame();
                infofram.setLayout(null);
                infofram.setBounds(0, 0, 300, 450);
                infofram.setFocusTraversalKeysEnabled(false);
                infofram.setTitle("players");
                infofram.setResizable(false);
                infofram.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                JPanel infopanel = new JPanel();
                infopanel.setLayout(null);

                for (int i = 0; i < bombermen.size(); i++) {
                    JTextField number = new JTextField();
                    JTextField lastpos = new JTextField();
                    JTextField point = new JTextField();

                    number.setText("" + (i + 1));
                    number.setBounds(20, 50 + i * 20, 20, 20);
                    number.setOpaque(false);
                    infopanel.add(number);
                    number.setVisible(true);

                    lastpos.setText("(" + bombermen.get(i).getyInBoard() + "," + bombermen.get(i).getxInBoard() + ")");
                    lastpos.setBounds(55, 50 + i * 20, 40, 20);
                    lastpos.setOpaque(false);
                    infopanel.add(lastpos);
                    lastpos.setVisible(true);

                    point.setText(String.valueOf(bombermen.get(i).getPoint()));
                    point.setBounds(110, 50 + i * 20, 30, 20);
                    point.setOpaque(false);
                    infopanel.add(point);
                    point.setVisible(true);

                    JButton ban = new JButton();
                    ban.setBounds(160, 50 + i * 20, 80, 20);
                    ban.setText("ban");
                    infopanel.add(ban);
                    ban.setVisible(true);
                    int finalI = i;
                    ban.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            dltbomberman.add(bombermen.get(finalI).getId());
                            bombermen.remove(finalI);
                            infopanel.validate();

                        }
                    });

                }

                infofram.setContentPane(infopanel);
                infopanel.setVisible(true);
                infofram.setVisible(true);

            }
        });

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameInfo.this.remove(play);
                GameInfo.this.add(pause);
                GameInfo.this.validate();

                stop = true;

                repaint();
            }
        });

        pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GameInfo.this.remove(pause);
                GameInfo.this.add(play);
                GameInfo.this.validate();

                stop = false;

                repaint();
            }
        });

        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop = true;
                askexit();
            }
        });

        chat.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chatFrame.setVisible(true);
            }
        });

    }

    public void filler(int scre, long tim, int nlevel, int bombremain, int radius) {

        score.setText("" + scre);
        score.setEditable(false);
        score.setPreferredSize(new Dimension(50, 20));

        tim = (int) tim / 1000;
        time.setText(sec2time((int) tim));
        time.setEditable(false);
        time.setPreferredSize(new Dimension(53, 20));

        level.setText("" + nlevel);
        level.setEditable(false);
        level.setPreferredSize(new Dimension(50, 20));

        bomb.setText("" + bombremain);
        bomb.setEditable(false);
        bomb.setPreferredSize(new Dimension(50, 20));

        bomb_radius.setText("" + radius);
        bomb_radius.setEditable(false);
        bomb_radius.setPreferredSize(new Dimension(50, 20));

        this.validate();
        this.repaint();

    }

    public String sec2time(int second) {

        int long_min = 60;
        int long_hour = 60 * long_min;

        int many_hour = second / long_hour;
        int many_min = second / long_min - many_hour * 60;
        int many_sec = second - many_min * 60 - many_hour * 3600;

        String many_secondS;
        String many_minuteS;
        String many_hourS;

        if (many_sec < 10)
            many_secondS = "0" + many_sec;
        else
            many_secondS = "" + many_sec;

        if (many_min < 10)
            many_minuteS = "0" + many_min;
        else
            many_minuteS = "" + many_min;

        if (many_hour < 10)
            many_hourS = "0" + many_hour;
        else
            many_hourS = "" + many_hour;


        return "" + many_hourS + ":" + many_minuteS + ":" + many_secondS;


    }

    public void askexit() {

        JFrame ender = new JFrame();
        ender.setSize(400, 200);
        ender.setLocation(new Point(650, 350));
        JPanel enderPanel = new JPanel();
        enderPanel.setLayout(null);
        enderPanel.setSize(new Dimension(400, 200));

        JTextField tmp = new JTextField();
        tmp.setText("are you sure you want to exit?");
        tmp.setBounds(100, 20, 200, 30);
        tmp.setEditable(false);
        enderPanel.add(tmp);

        JButton yes = new JButton();
        JButton no = new JButton();

        yes.setText("Yes");
        no.setText("No");

        yes.setBounds(120, 80, 80, 20);
        no.setBounds(210, 80, 80, 20);

        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitbool = true;
                ender.dispose();
            }
        });

        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                stop = false;
                ender.dispose();
            }
        });

        ender.add(yes);
        ender.add(no);
        ender.add(enderPanel);
        ender.setVisible(true);
        ender.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}

class NewApplet extends Applet {
    public void init() {
        setBackground(Color.BLUE);
    }

    public void paint(Graphics g) {
        g.setColor(Color.BLACK);
        g.drawString("", 0, 0);
    }
}
