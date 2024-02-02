import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.File;

public class LoadFrame extends JFrame {

    private String path = "" ;

    public LoadFrame() {
        LoadFrameinit();
    }

    private void LoadFrameinit() {

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        this.add(jfc);
        jfc.setDialogTitle("Choose a file to load");
        jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int returnvalue = jfc.showSaveDialog(this);
        File file = new File("");

        if (returnvalue == JFileChooser.APPROVE_OPTION) {
            file = jfc.getSelectedFile();
            path = file.getPath() ;
        }

        this.setVisible(false);
        this.dispose();



    }

    public String getPath() {
        return path;
    }


}
