package Shape;

import java.awt.Color;
import java.awt.Graphics;

import Listener.EventListener;

import UI.*;
// 基本图形的抽象类（橡皮擦类和多图形类都从该类继承）
public abstract class Shape {
    public int x1;
    public int y1;
//    protected static final Listener.EventListener el = Listener.EventListener.getInstance(UI.MainWindow.getInstance().getCurId());
    EventListener el = EventListener.getInstance(MainWindow.getInstance().getCurId());
    protected Color selectedColor;
    protected String operation;
    protected int width;

    public Shape(int x1, int y1) {
        selectedColor = el.getSelectedColor();
        operation = el.getOperation();
        width = el.getWidth();
        this.x1 = x1;
        this.y1 = y1;
    }

    public Shape(int x1, int y1, Color customColor) {
        this(x1, y1);
        selectedColor = customColor;
    }

    public abstract void draw(Graphics p);

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public void refresh() {
    }

}
