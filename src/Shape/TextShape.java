package Shape;

import java.awt.*;

import UI.*;
// 该类可以根据选择操作操作的不同绘制不同种类的图
public class TextShape extends MultiShape {
    private final int descent;
    private final int Asent;
    private String content;
    private Font font;
    private int size;

    public TextShape(int x1, int y1) {
        super(x1, y1,true);
        Toolbar toolbar = Toolbar.getInstance();
        this.content = toolbar.getTextString();
        this.font = toolbar.getSelectedFont();
        this.size = toolbar.getSelectedSize();
        FontMetrics metrics = Toolkit.getDefaultToolkit().getFontMetrics(new Font(this.font.getName(),Font.PLAIN,this.size));
        this.descent =metrics.getDescent();
        this.Asent= metrics.getAscent();
        int strWidth = metrics.stringWidth(this.content);
        int strHeight = metrics.getHeight();
        x2=x1+strWidth;
        y2=y1+strHeight;
    }

    @Override
    public void draw(Graphics g) {
        Graphics2D p = (Graphics2D) g;
        p.setColor(this.selectedColor);
        p.setFont(this.font.deriveFont((float) size));
        p.drawString(this.content, x1, y1+Asent);
//        p.drawRect(x1, y1, x2 - x1, y2 - y1);
    }
}