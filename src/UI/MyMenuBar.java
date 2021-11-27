package UI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MyMenuBar extends JMenuBar {
    public MyMenuBar() {
//        “文件”菜单栏
        JMenu menu1=new JMenu("文件");
        JMenuItem saveAsButton = new JMenuItem("另存为");
        saveAsButton.addActionListener(new saveAs());
        JMenuItem loadButton = new JMenuItem("载入");
        loadButton.addActionListener(new load());
        menu1.add(saveAsButton);
        menu1.add(loadButton);

        this.add(menu1);
    }

    private class saveAs implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String message;
            if (MainWindow.getInstance().saveAs() )
                message="已保存";
            else
                message="保存失败";
            JOptionPane.showMessageDialog(MainWindow.getInstance(),message);
        }
    }

    private class load implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            String message;
            if ( MainWindow.getInstance().load() )
                message="已载入";
            else
                message="载入失败";
            JOptionPane.showMessageDialog(MainWindow.getInstance(),message);
        }
    }
}
