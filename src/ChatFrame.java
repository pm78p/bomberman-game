import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class ChatFrame extends JFrame {

    private String name;
    ArrayList<Massage> massages = new ArrayList<>();
    JPanel contentpain = new JPanel();
    JPanel showmassage = new JPanel();
    JPanel writemassage = new JPanel();
    JButton send = new JButton();
    JTextPane shower = new JTextPane();
    //    JTextArea shower = new JTextArea();
    JTextArea write = new JTextArea();
    StyledDocument doc = shower.getStyledDocument();
    SimpleAttributeSet left = new SimpleAttributeSet();
    SimpleAttributeSet right = new SimpleAttributeSet();
    double id = Math.random();

    public ChatFrame(String name) {
        this.name = name;
        init();
    }

    public void init() {

        this.setLayout(null);
        this.setBounds(0, 0, 500, 600);
        this.setFocusTraversalKeysEnabled(false);
        this.setTitle("Chat room");
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        contentpain.setLayout(null);
        contentpain.setBackground(Color.BLUE);

        showmassage.setLayout(null);
        showmassage.setBounds(10, 10, 480, 480);

//        JScrollPane scroll = new JScrollPane(showmassage);
//        scroll.setViewportView(shower);
//        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
//        scroll.setSize(new Dimension(20, 460));
//        scroll.setVisible(true);
//        showmassage.add(scroll);

//        showmassage.add(new JScrollPane(shower));
        JScrollPane scroll = new JScrollPane(shower);
        scroll.setViewportView(shower);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setSize(new Dimension(20,460));
        scroll.setVisible(true);
        showmassage.add(scroll);

        StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(left, Color.RED);

        StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setForeground(right, Color.BLUE);

        shower.setEditable(false);
        shower.setBounds(20, 0, 460, 100000);
        shower.setBackground(Color.green);

        showmassage.add(shower);
//        shower.append(tmp);
//        shower.append(tmp2);

        writemassage.setLayout(null);
        writemassage.setBounds(10, 500, 400, 60);
        write.setText("");
        write.setBounds(0, 0, 400, 60);
        writemassage.add(write);
        write.setBackground(Color.CYAN);

        send.setBounds(420, 500, 70, 30);
        send.setText("send");

//        contentpain.add(new JScrollPane(showmassage));
        contentpain.add(showmassage);
        contentpain.add(writemassage);
        contentpain.add(send);

        this.setContentPane(contentpain);

        send.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (write.getText().length() != 0) {
                    try {
                        doc.insertString(doc.getLength(), "" + "\n", right);
                        doc.setParagraphAttributes(doc.getLength(), 1, right, false);
                        doc.insertString(doc.getLength(), write.getText() + "\n", right);
                        doc.setParagraphAttributes(doc.getLength(), 1, right, false);
                        Massage tmpmassage = new Massage(name, write.getText(), ChatFrame.this.id);
                        massages.add(tmpmassage);
//                        if (write.getText().equals("salam"))
//                            recievemassage("asqar" , "kose nanat");
                    } catch (BadLocationException e1) {
                        e1.printStackTrace();
                    }
                    write.setText("");
                }
            }
        });

    }

    public void recievemassage(Massage massage) {
        String textshape = massage.getName() + ": " + "\n" + massage.getText() + "\n";
        try {
            doc.insertString(doc.getLength(), "" + "\n", left);
            doc.setParagraphAttributes(doc.getLength(), 1, left, false);
            doc.insertString(doc.getLength(), textshape, left);
            doc.setParagraphAttributes(doc.getLength(), 1, left, false);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        ChatFrame chatFrame = new ChatFrame("jafar");
        chatFrame.setVisible(true);

    }

}
