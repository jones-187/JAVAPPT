import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

public class DrawingArea extends JPanel {
    private int id=0;
    private static ArrayList<DrawingArea> pageList = new ArrayList<>();
    private BufferedImage image = null;
    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public DrawingArea(int id) {
        this.id=id;
        // 为画图区域添加背景色
        this.setBackground(Color.WHITE);
        // 绘制窗口，设置布局，将工具栏、绘图区域（this）加入到window中并显示
//        this.drawUI();
        // 为绘图区域添加鼠标/键盘监听
//        this.bindEvent();
        // 不打开文件
        image = null;
        // 得到窗口焦点
        this.requestFocus();
    }

    public static DrawingArea getInstance(int index){
        return (pageList.isEmpty() || index>=pageList.size() )? null : pageList.get(index);
//        if (pageList.isEmpty() || index>=pageList.size())
//            return null;
//        else 
//            return pageList.get(index);
    }
    
    public static void addInstance(){
        EventListener.addInstance();
        pageList.add(new DrawingArea(pageList.size()));
    }

    public void bindEvent() {
        // 得到监听器实例
        EventListener el = EventListener.getInstance(id);
        // 添加鼠标/鼠标移动/键盘的监听器
        // （因为el实现了鼠标MouseListener、鼠标移动MouseMotionListener、键盘KeyListener的接口，所以可以这样写）
        this.addMouseListener(el);
        this.addMouseMotionListener(el);
        this.addKeyListener(el);
        // 将绘图区域的“笔”传给监听器，在监听器内进行绘制
        assert this.getGraphics()!=null;
        el.setPen(this.getGraphics());
    }

    public void paint(Graphics p) {
        // 该函数是窗口大小变化时自动调用的函数，其中的p默认是this.getGraphics()（也就是绘图区域的画笔）
        // 为父类重新绘制（即添加背景色）
        super.paint(p);
        // 如果读取了图片，则先把图片画上
        if (image != null) {
            p.drawImage(image, 0, 0, null);
        }
        EventListener el = EventListener.getInstance(this.id);
        // 遍历绘图历史，绘制该图形
        for (Shape item : el.getHistory()) {
            item.draw(p);
        }
    }

    // 保存为图片
    public void savePanelAsImage() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("saved.png"));
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showSaveDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            // String.split的参数是正则表达式，为了匹配纯文本的.需要把参数用方括号括起来
            String[] tmp = file.getName().split("[.]");
            if (tmp.length <= 1) {
                JOptionPane.showMessageDialog(null, "保存文件没有拓展名，保存失败...");
                return;
            }
            String extension = tmp[tmp.length - 1];
            // HELP:文档内说imageio支持jpg，但本地测试保存无反应
            if (!extension.equals("png") && !extension.equals("jpg")) {
                JOptionPane.showMessageDialog(null, "拓展名非法（允许使用：png/jpg），保存失败...");
                return;
            }
            Dimension imageSize = this.getSize();
            BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            this.paint(graphics);
            graphics.dispose();
            try {
                ImageIO.write(image, extension, file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // 从图片打开
    public void loadImageToPanel() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("images", "*.png", "*.jpg"));
        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }
        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
        try {
            // 读取该张图片
            image = ImageIO.read(new File(filePath));
            // 清除所有历史
            EventListener.getInstance(this.id).clear(false);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }


    public void clearFile() {
        // 移除内部的图片缓存
        this.image = null;
    }
}
