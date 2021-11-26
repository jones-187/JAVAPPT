import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PageSwitcher extends JPanel {
    private int totalPagesNum = 1,index=1;
    private static PageSwitcher instance;

    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public PageSwitcher() {
//        this.setLayout(new FlowLayout());

        StringBuilder sb= new StringBuilder();
        sb.append(index);
        sb.append(" / ");
        sb.append(totalPagesNum);
        JLabel pagesIndicator=new JLabel(sb.toString());

        JButton nextPage = new JButton("下一张");
        nextPage.addActionListener(new getNewPage());
        this.add(nextPage,BorderLayout.NORTH);
        JButton lastPage = new JButton("上一张");
        lastPage.addActionListener(new getNewPage());
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


    private class getNewPage implements ActionListener{

        /**
         * Invoked when an action occurs.
         *
         * @param e the event to be processed
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(PageSwitcher.this, "提示消息", "标题",JOptionPane.WARNING_MESSAGE);
        }
    }

    public static PageSwitcher getInstance(){
        if (instance == null) {
            instance = new PageSwitcher();
        }
        return instance;
    }
}
