import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MainServer extends JFrame {

    Path currentRelativePath = Paths.get("");
    String thispath = currentRelativePath.toAbsolutePath().toString();
    ArrayList<ServerFrame> serverFrames = new ArrayList<>();

    public MainServer() {
        init();
    }

    public void init() {

        this.setTitle("BoombermanServer");
        this.setBounds(0, 0, 1725, 935);
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final int[] xserver = {0};
        final int[] yserver = {0};
        JPanel firstpanel;
        firstpanel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                Image img = Toolkit.getDefaultToolkit().getImage(thispath + "/src/media5gif.gif");
                g2d.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);

                BufferedImage srvrs = null;
                try {
                    srvrs = ImageIO.read(new File(thispath + "/src/pictures/srvrbackground2.png"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g2d.setComposite(AlphaComposite.SrcOver.derive((float) 0.95));
                xserver[0] = this.getWidth() / 2 - srvrs.getWidth() / 2;
                yserver[0] = this.getHeight() / 2 - srvrs.getHeight() / 2;
                g2d.drawImage(srvrs, xserver[0], yserver[0], this);
            }
        };

        firstpanel.setLayout(null);
        JButton newGame = new JButton();
        newGame.setText("new Game");
        newGame.setBounds(xserver[0] + 3 * 800 / 2 - 40, yserver[0] + 3 * 450 / 2 + 20, 100, 20);
        newGame.setVisible(true);
        firstpanel.add(newGame);

        firstpanel.setVisible(true);
        this.setContentPane(firstpanel);

        JPanel constructingpanel;
        constructingpanel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                Image img = Toolkit.getDefaultToolkit().getImage(thispath + "/src/media5gif.gif");
                g2d.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);

                BufferedImage srvrs = null;
                try {
                    srvrs = ImageIO.read(new File(thispath + "/src/pictures/srvrbackground.png")); // 800 450
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g2d.setComposite(AlphaComposite.SrcOver.derive((float) 0.95));
                xserver[0] = this.getWidth() / 2 - srvrs.getWidth() / 2;
                yserver[0] = this.getHeight() / 2 - srvrs.getHeight() / 2;
                g2d.drawImage(srvrs, xserver[0], yserver[0], this);
            }
        };

        constructingpanel.setLayout(null);

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
        constructingpanel.add(comboBox);
        constructingpanel.add(comboBox1);

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

        constructingpanel.add(height);
        constructingpanel.add(width);
        constructingpanel.add(bull);

        JButton next = new JButton();
        next.setText("Next");
        constructingpanel.add(next);
        next.setBounds(790, 480, 100, 30);

        JButton backInNewgame = new JButton();
        backInNewgame.setText("back");
        backInNewgame.setBounds(790, 540, 100, 30);
        constructingpanel.add(backInNewgame);

        thr.start();

        JPanel srvrdetailpanel;
        srvrdetailpanel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                Image img = Toolkit.getDefaultToolkit().getImage(thispath + "/src/media5gif.gif");
                g2d.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), this);

                BufferedImage srvrs = null;
                try {
                    srvrs = ImageIO.read(new File(thispath + "/src/pictures/srvrbackground.png")); // 800 450
                } catch (IOException e) {
                    e.printStackTrace();
                }
                g2d.setComposite(AlphaComposite.SrcOver.derive((float) 0.95));
                xserver[0] = this.getWidth() / 2 - srvrs.getWidth() / 2;
                yserver[0] = this.getHeight() / 2 - srvrs.getHeight() / 2;
                g2d.drawImage(srvrs, xserver[0], yserver[0], this);
            }
        };
        srvrdetailpanel.setLayout(null);

        JTextField port = new JTextField();
        JTextField maxplayers = new JTextField();

        port.setBounds(xserver[0] + 800, yserver[0] + 450 - 180, 70, 20);
        maxplayers.setBounds(xserver[0] + 800, yserver[0] + 450 - 180 + 30, 70, 20);

        port.setText("8090");
        maxplayers.setText("4");

        JTextField porttip = new JTextField();
        JTextField maxplayerstip = new JTextField();

        porttip.setBounds(xserver[0] + 800 - 100 + 50, yserver[0] + 450 - 180, 70, 20);
        maxplayerstip.setBounds(xserver[0] + 800 - 130, yserver[0] + 450 + 30 - 180, 150, 20);

        porttip.setText("port:");
        maxplayerstip.setText("maximum players:");

        porttip.setForeground(Color.yellow);
        maxplayerstip.setForeground(Color.yellow);

        porttip.setEditable(false);
        maxplayerstip.setEditable(false);

        porttip.setOpaque(false);
        maxplayerstip.setOpaque(false);

        porttip.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.DARK_GRAY));
        maxplayerstip.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.darkGray));

        JButton makegame = new JButton();
        JButton back2 = new JButton();

        makegame.setText("start");
        makegame.setBounds(xserver[0] + 800, yserver[0] + 450 + 100, 70, 20);

        back2.setText("back");
        back2.setBounds(xserver[0] + 800, yserver[0] + 450 + 130, 70, 20);

        srvrdetailpanel.add(port);
        srvrdetailpanel.add(maxplayers);
        srvrdetailpanel.add(porttip);
        srvrdetailpanel.add(maxplayerstip);
        srvrdetailpanel.add(makegame);
        srvrdetailpanel.add(back2);

        JPanel finalConstructingpanel = constructingpanel;
        newGame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainServer.this.remove(firstpanel);
                MainServer.this.setContentPane(finalConstructingpanel);
                MainServer.this.validate();
            }
        });

        backInNewgame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                MainServer.this.remove(finalConstructingpanel);
                MainServer.this.setContentPane(firstpanel);
                MainServer.this.validate();
            }
        });

        next.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                tmp[0] = false;
                MainServer.this.remove(finalConstructingpanel);
                MainServer.this.setContentPane(srvrdetailpanel);
                MainServer.this.validate();
            }
        });

        back2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                MainServer.this.remove(srvrdetailpanel);
                MainServer.this.setContentPane(finalConstructingpanel);
                MainServer.this.validate();
            }
        });

        makegame.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makegameThread thread = new makegameThread(Integer.parseInt(k[0]), Integer.parseInt(k1[0]), Integer.parseInt(port.getText()), Integer.parseInt(maxplayers.getText()));
                thread.start();
                serverFrames.add(thread.frame);
                MainServer.this.remove(srvrdetailpanel);
                paintserversdetail(firstpanel, xserver[0], yserver[0], Integer.parseInt(k[0]), Integer.parseInt(k1[0]), Integer.parseInt(port.getText()), Integer.parseInt(maxplayers.getText()));
                MainServer.this.setContentPane(firstpanel);
                MainServer.this.validate();

            }
        });

    }

    public void paintserversdetail(JPanel panel, int x, int y, int tool, int arz, int porte, int maxplayer) {
        for (int i = 0; i < serverFrames.size(); i++) {
            JTextField port = new JTextField();
            JTextField size = new JTextField();
            JTextField players = new JTextField();

            port.setText(String.valueOf(porte));
            size.setText(String.valueOf(tool) + "-" + String.valueOf(arz));
            players.setText(String.valueOf(0));

            port.setForeground(Color.yellow);
            size.setForeground(Color.yellow);
            players.setForeground(Color.yellow);

            port.setOpaque(false);
            size.setOpaque(false);
            players.setOpaque(false);

            port.setEditable(false);
            size.setEditable(false);
            players.setEditable(false);

            port.setBounds(x + 15, y + 30 + 10 + 20 + i * 30, 60 , 20);
            size.setBounds(x + 15 + 800 - 150 - 5, y + 30 + 10 + 20 + i * 30, 60, 20);
            players.setBounds(x + 15 + 800 - 60, y + 30 + 10 + 20 + i *30, 60, 20);

            port.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.DARK_GRAY));
            size.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.DARK_GRAY));
            players.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 0, Color.DARK_GRAY));

            port.setVisible(true);
            size.setVisible(true);
            players.setVisible(true);

            panel.add(port);
            panel.add(size);
            panel.add(players);
        }
        panel.validate();
    }

    public static void main(String[] args) throws IOException {
        MainServer mainServer = new MainServer();
        mainServer.setVisible(true);
    }

}

class makegameThread extends Thread {

    ServerFrame frame;
    int x, y, port, maxplayer;

    public makegameThread(int x, int y, int port, int maxplayer) {
        this.x = x;
        this.y = y;
        this.port = port;
        this.maxplayer = maxplayer;
    }

    @Override
    public void run() {

        while (true) {

            try {
                frame = new ServerFrame(x, y, port, maxplayer);
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
