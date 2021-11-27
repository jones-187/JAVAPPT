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
            MainWindow.getInstance().saveAs();
            JOptionPane.showMessageDialog(MainWindow.getInstance(),"请保存");
        }
    }

    private class load implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            MainWindow.getInstance().load();
        }
    }
}
