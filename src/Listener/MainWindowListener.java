package Listener;

import UI.MainWindow;
import UI.DrawingArea;
import UI.Toolbar;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindowListener implements ActionListener, ChangeListener {
    private Color selectedColor;
    private Color backgroundColor;
    private String operation;
    private int width;

    /**
     * Invoked when an action occurs.
     *
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JButton instance = (JButton) e.getSource();
        // 点击的是颜色（因为颜色按钮没有文字）
        if ("".equals(e.getActionCommand())) {
            if (Toolbar.getInstance().isForebackgroundSelected()) {
                // 设置前景色
                selectedColor = instance.getBackground();
                EventListener.getInstance( MainWindow.getInstance().getCurId() ).setSelectedColor(selectedColor);
            } else {
                // 设置背景色
                backgroundColor = instance.getBackground();
                EventListener.getInstance( MainWindow.getInstance().getCurId() ).setBackgroundColor(backgroundColor);
                // 刷新画板
                EventListener.getInstance( MainWindow.getInstance().getCurId() ).setBackgroundColor();
            }
        } else {
            // 选择帮助操作时输出帮助信息并return
            if (instance.getText().equals("帮助")) {
                EventListener.getInstance( MainWindow.getInstance().getCurId() ).showHelpMessage();
                DrawingArea.getInstance( MainWindow.getInstance().getCurId() ).requestFocus();
                return;
            }
            // 否则将操作赋值给参数
            this.operation = instance.getText();
            EventListener.getInstance( MainWindow.getInstance().getCurId() ).setOperation(operation);
        }
        // 将焦点还给绘图区域（没有焦点没有办法响应键盘事件）
        DrawingArea.getInstance( MainWindow.getInstance().getCurId() ).requestFocus();
    }

    /**
     * Invoked when the target of the listener has changed its state.
     *
     * @param e a ChangeEvent object
     */
    @Override
    public void stateChanged(ChangeEvent e) {
        JSlider jslider = (JSlider) e.getSource();
        this.width = jslider.getValue();
        EventListener.getInstance( MainWindow.getInstance().getCurId() ).setWidth(width);
        // 将焦点还给绘图区域（没有焦点没有办法响应键盘事件）
        DrawingArea.getInstance( MainWindow.getInstance().getCurId() ).requestFocus();
    }
}
