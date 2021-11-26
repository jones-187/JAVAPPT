package UI;

import Listener.Utils;

import java.awt.*;
import java.util.List;
import javax.swing.*;

import Listener.*;
// 该类用于构建工具栏
public class Toolbar extends JPanel {
    // 单例模式
    private static Toolbar tb;
    static final long serialVersionUID = 12345;

    // 文本输入文本框
    private JTextField jtf1 = new JTextField("Input Content Here", 20);
    // “前景色”单选框
    JRadioButton fore;
    // 字体选择器
    JComboBox<String> fontChooser = new JComboBox<>();
    // 字号选择器
    JComboBox<Integer> sizeChooser = new JComboBox<>();

    private Toolbar() {
        JPanel northPanel = new JPanel();
        JPanel southPanel = new JPanel();
        this.setLayout(new BorderLayout());

        MainWindowListener el = new MainWindowListener();
        String[] shapeArray = { "拖拽","铅笔", "直线", "矩形", "圆", "文本", "橡皮擦", "帮助" };
        // 添加所有的按钮并添加按钮点击事件监听
        for (String item : shapeArray) {
            JButton tmp = new JButton(item);
            tmp.addActionListener(el);
            northPanel.add(tmp);
        }
        // 添加颜色列表
        northPanel.add(new Colorlist());
        // 用于判断设置前景色还是设置背景色
        ButtonGroup bg = new ButtonGroup();
        fore = new JRadioButton("前景色", true);
        JRadioButton back = new JRadioButton("背景色");
        // 加入到同一个按钮组
        bg.add(fore);
        bg.add(back);
        // 添加单选框
        northPanel.add(fore);
        northPanel.add(back);
        // 添加线条粗细调整到toolbar的第二行
        southPanel.add(new LineWidth());
        // 添加字体选择器到toolbar的第二行
        setComboBox(Utils.getSystemFonts());
        southPanel.add(fontChooser);
        // 添加字号选择器到toolbar的第二行
        for (int i = 9; i <= 72; i++) {
            sizeChooser.addItem(Integer.valueOf(i));
        }
        sizeChooser.setSelectedIndex(7);
        southPanel.add(sizeChooser);
        // 添加文本框到toolbar的第二行
        southPanel.add(jtf1);
        // Toolbar布局
        this.add(northPanel, BorderLayout.NORTH);
        this.add(southPanel, BorderLayout.SOUTH);
    }

    // 获取实例的静态方法
    public static Toolbar getInstance() {
        if (tb == null) {
            tb = new Toolbar();
        }
        return tb;
    }

    // 获取文本
    public String getTextString() {
        return this.jtf1.getText();
    }

    // 当前选中的是否是前景色
    public boolean isForebackgroundSelected() {
        return this.fore.isSelected();
    }

    public void setComboBox(List<String> list) {
        for (var item : list) {
            this.fontChooser.addItem(item);
        }
    }

    public Font getSelectedFont() {
        return Utils.map.get(this.fontChooser.getSelectedItem());
    }

    public int getSelectedSize() {
        return (Integer) this.sizeChooser.getSelectedItem();
    }

    private static class Colorlist extends JPanel {
//        static final long serialVersionUID = 1471001741;

        public Colorlist() {
            // 得到监听器实例
            MainWindowListener el = new MainWindowListener();
            // 为列表使用二行四列的栅格布局
            this.setLayout(new GridLayout(2, 4, 2, 2));
            // 通过颜色数组快速构建前七个按钮
            Color[] colorArray = { Color.BLACK, Color.BLUE, Color.YELLOW, Color.GREEN, Color.PINK, Color.RED, Color.CYAN };
            for (Color item : colorArray) {
                JButton tmp = new JButton();
                tmp.setBackground(item);
                // 为该按钮添加点击监听，详见EventListener->actionPerformed
                tmp.addActionListener(el);
                this.add(tmp);
            }
            // 最后一个按钮是自定义颜色
            JButton customColor = new JButton();
            customColor.setBackground(Color.WHITE);
            // 为该按钮加入与其它按钮相同的监听
            customColor.addActionListener(el);
            // 点击弹出颜色对话框，将选中颜色设置为背景色
            // 注意：经过测试，多个ActionListener的情况下会优先执行后添加的
            customColor.addActionListener(e -> {
                Color selectedColor = JColorChooser.showDialog(null, "自定义颜色", Color.BLACK);
                if (selectedColor == null) {
                    selectedColor = Color.WHITE;
                }
                customColor.setBackground(selectedColor);
            });
            this.add(customColor);
        }
    }

    // 调整线宽
    public static class LineWidth extends JPanel {
//    private static final long serialVersionUID = 15100151;

        public LineWidth() {
            this.add(new JLabel("线条粗细"));
            JSlider slider = new JSlider(1, 9, 1);
            slider.setMajorTickSpacing(4);
            slider.setMinorTickSpacing(1);
            slider.setPaintTicks(true);
            MainWindowListener el = new MainWindowListener();
            slider.addChangeListener(el);
            this.add(slider);
        }
    }
}
