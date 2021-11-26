package UI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PageSwitcher extends JPanel {
    private int totalPagesNum = 1,index=1;
    private static PageSwitcher instance;
    private JLabel pagesIndicator = new JLabel();

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public PageSwitcher() {
//        this.setLayout(new FlowLayout());

//        StringBuilder sb= new StringBuilder();
//        sb.append(index);
//        sb.append(" / ");
//        sb.append(totalPagesNum);
//        this.pagesIndicator=new JLabel(sb.toString());
        this.updatePagesIndicator();
        JButton nextPage = new JButton("下一张");
        nextPage.addActionListener(new nextPage());
        this.add(nextPage,BorderLayout.NORTH);
        JButton lastPage = new JButton("上一张");
        lastPage.addActionListener(new lastPage());
        this.add(lastPage,BorderLayout.SOUTH);
        JButton newPage = new JButton("新建");
        newPage.addActionListener(new getNewPage());
//                new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                DragingFrame.this.addPa();
//            }
//        });
        this.add(pagesIndicator);
        this.add(newPage);
        this.add(lastPage);
        this.add(nextPage);

    }

    private void updatePagesIndicator(){
        StringBuilder sb= new StringBuilder();
        sb.append(index);
        sb.append(" / ");
        sb.append(totalPagesNum);
        this.pagesIndicator.setText(sb.toString());
        pagesIndicator.repaint();
    }

    private class getNewPage implements ActionListener{

        /**
         * Invoked when an action occurs.
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if(MainWindow.getInstance().addNewPage()) {
                PageSwitcher.this.totalPagesNum++;
                PageSwitcher.this.index++;
                PageSwitcher.this.updatePagesIndicator();
            }
//            JOptionPane.showMessageDialog(UI.PageSwitcher.this, "提示消息", "标题",JOptionPane.WARNING_MESSAGE);
        }
    }

    private class nextPage implements ActionListener{

        /**
         * Invoked when an action occurs.
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if(MainWindow.getInstance().nextPage()){
                PageSwitcher.this.index++;
                PageSwitcher.this.updatePagesIndicator();
            }
//            JOptionPane.showMessageDialog(UI.PageSwitcher.this, "提示消息", "标题",JOptionPane.WARNING_MESSAGE);
        }
    }

    private class lastPage implements ActionListener{

        /**
         * Invoked when an action occurs.
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if(MainWindow.getInstance().lastPage()){
                PageSwitcher.this.index--;
                PageSwitcher.this.updatePagesIndicator();
            }
//            JOptionPane.showMessageDialog(UI.PageSwitcher.this, "提示消息", "标题",JOptionPane.WARNING_MESSAGE);
        }
    }

    public static PageSwitcher getInstance(){
        if (instance == null) {
            instance = new PageSwitcher();
        }
        return instance;
    }
}
