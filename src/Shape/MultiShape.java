package Shape;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

// 该类可以根据选择操作操作的不同绘制不同种类的图
public class MultiShape extends Shape {
    public int x2;
    public int y2;

    public MultiShape(int x1, int y1, int x2, int y2) {
        super(x1, y1);
        this.x2 = x2;
        this.y2 = y2;
    }

    public MultiShape(int x1, int y1,boolean isTextShape) {
        /*
        * designed only for construction of Class Shape.TextShape*/
        super(x1,y1);
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D p = (Graphics2D) g;
        p.setColor(this.selectedColor);
        p.setStroke(new BasicStroke(this.width));
        this.innerDraw(p);
    }

    private void swap() {
        // 保证x1<=x2,y1<=y2，在绘制矩形、圆时需要
        if (x1 > x2) {
            int tmp = x1;
            x1 = x2;
            x2 = tmp;
        }
        if (y1 > y2) {
            int tmp = y1;
            y1 = y2;
            y2 = tmp;
        }
    }

    private void innerDraw(Graphics p) {
        switch (this.operation) {
            case "铅笔":
            case "直线":
                p.drawLine(x1, y1, x2, y2);
                break;
            case "矩形":
                this.swap();
                p.drawRect(x1, y1, x2 - x1, y2 - y1);
                break;
            case "圆":
                this.swap();
                p.drawOval(x1, y1, x2 - x1, y2 - y1);
                break;
        }
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }
}
