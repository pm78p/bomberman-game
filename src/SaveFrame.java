import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class SaveFrame extends JFrame {

    private String path = new String() ;

    public SaveFrame() {
        SaveFrameinit();
    }

    private void SaveFrameinit() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        this.add(jfc);
        jfc.setDialogTitle("Choose a directories to writer");
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnvalue = jfc.showSaveDialog(this);
        File file = new File("");

        if (returnvalue == JFileChooser.APPROVE_OPTION)
            file = jfc.getSelectedFile();

        this.setVisible(false);
        this.dispose();

        path = file.getPath() + ".txt" ;

    }

    public String getPath() {
        return path;
    }
}
