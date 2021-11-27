package Shape;

import Listener.EventListener;
import UI.MainWindow;

import java.awt.Graphics;
public class Eraser extends Shape {
    public Eraser(int x1, int y1) {
        super(x1, y1);
        EventListener el = EventListener.getInstance(MainWindow.getInstance().getCurId());
        selectedColor=el.getBackgroundColor();
    }

    @Override
    public void draw(Graphics p) {
        p.setColor(this.selectedColor);
        // 以(x1,y1)为圆心，this.width*3为半径的白色实心圆
        p.fillOval(x1 - this.width * 3, y1 - this.width * 3, this.width * 6, this.width * 6);
    }

    @Override
    public void refresh() {
        // 刷新颜色
        EventListener el = EventListener.getInstance(MainWindow.getInstance().getCurId());
        this.selectedColor = el.getBackgroundColor();
    }
}
