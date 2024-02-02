import javax.swing.*;
import java.awt.*;

public class GamePlay extends JFrame {

    int tool = 0;
    int arz = 0;

    GameInfo gameInfo;
    BoardPSingle boardPSingle;

    public GamePlay() {
        GameplayInit();
    }

    public GamePlay(int t, int a) {
        tool = t;
        arz = a;
        GameplayInit();
    }

    public void GameplayInit() {

        gameInfo = new GameInfo();
        if (tool == 0)
            boardPSingle = new BoardPSingle(gameInfo);
        else
            boardPSingle = new BoardPSingle(gameInfo, tool, arz);

        this.setBounds(0, 0, 1725, 935);
        this.setFocusTraversalKeysEnabled(false);
        this.setTitle("Boomberman");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setBackground(Color.BLUE);

        JPanel center = new JPanel();
        center.setBackground(Color.blue);

        this.add(gameInfo, BorderLayout.EAST);
        this.add(center, BorderLayout.CENTER);
        this.add(boardPSingle, BorderLayout.CENTER);

        Thread thr = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {

                    if ( gameInfo.exitbool || boardPSingle.back2main ){
                        GamePlay.this.dispose();
                        Main main = new Main(false) ;
                        main.setVisible(true);
                        return;
                    }

                    if ( boardPSingle.exitthisgame )
                        GamePlay.this.dispose();

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        thr.start();

    }
}

