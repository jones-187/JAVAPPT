import javax.swing.*;

import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.image.BufferedImage;
import java.io.File;

// 画图区域（窗口也是在该类中创建的）
public class MainWindow extends  JFrame{
    private int curId=0;

    //    static final long serialVersionUID = 1357997531;
    // 同样使用单例模式
    private static MainWindow db;
//    private BufferedImage image = null;

    // 获得实例的静态函数
    public static MainWindow getInstance() {
        if (db == null) {
            db = new MainWindow();
        }
        return db;
    }

    private MainWindow() {
        // 绘制窗口，设置布局，将工具栏、绘图区域（this）加入到window中并显示
        this.drawUI();
//        // 为绘图区域添加鼠标/键盘监听
        this.bindEvent();
        // 不打开文件
//        image = null;
        // 得到窗口焦点
//        this.requestFocus();
    }

    public boolean addNewPage() {
        DrawingArea.addInstance(this.curId+1);
        return this.turnToPage(curId+1);
    }

    private boolean turnToPage(int index){
        DrawingArea curInstance,nextInstance;
        if ( (curInstance = DrawingArea.getInstance(curId)) == null
                || (nextInstance = DrawingArea.getInstance(index)) == null ) {
            return false;
        }

        this.remove(curInstance);
        this.add(nextInstance);
        curId=index;
        /*
         * 在使用remove后，总是重新验证并重新绘制
         * */
        this.revalidate();
        this.repaint();
        this.bindEvent();
        DrawingArea.getInstance(curId).requestFocus();
        return true;
    }

    private void drawUI() {
//        JFrame window = new JFrame("PPT Editor");
        this.setTitle("PPT Editor");
        this.setSize(900, 675);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        DrawingArea.addInstance();
        this.add(DrawingArea.getInstance(0), BorderLayout.CENTER);
        this.add(Toolbar.getInstance(), BorderLayout.NORTH);
        this.add(PageSwitcher.getInstance(),BorderLayout.SOUTH);
        this.setVisible(true);
    }

    private void bindEvent() {
        DrawingArea.getInstance(curId).bindEvent();
    }

//    public void paint(Graphics p) {
//        // 该函数是窗口大小变化时自动调用的函数，其中的p默认是this.getGraphics()（也就是绘图区域的画笔）
//        // 为父类重新绘制（即添加背景色）
//        super.paint(p);
//        // 如果读取了图片，则先把图片画上
//        if (image != null) {
//            p.drawImage(image, 0, 0, null);
//        }
//        EventListener el = EventListener.getInstance();
//        // 遍历绘图历史，绘制该图形
//        for (Shape item : el.getHistory()) {
//            item.draw(p);
//        }
//    }

//    // 保存为图片
//    public void savePanelAsImage() {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setSelectedFile(new File("saved.png"));
//        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
//        int result = fileChooser.showSaveDialog(null);
//        if (result == JFileChooser.APPROVE_OPTION) {
//            File file = fileChooser.getSelectedFile();
//            // String.split的参数是正则表达式，为了匹配纯文本的.需要把参数用方括号括起来
//            String[] tmp = file.getName().split("[.]");
//            if (tmp.length <= 1) {
//                JOptionPane.showMessageDialog(null, "保存文件没有拓展名，保存失败...");
//                return;
//            }
//            String extension = tmp[tmp.length - 1];
//            // HELP:文档内说imageio支持jpg，但本地测试保存无反应
//            if (!extension.equals("png") && !extension.equals("jpg")) {
//                JOptionPane.showMessageDialog(null, "拓展名非法（允许使用：png/jpg），保存失败...");
//                return;
//            }
//            Dimension imageSize = this.getSize();
//            BufferedImage image = new BufferedImage(imageSize.width, imageSize.height, BufferedImage.TYPE_INT_ARGB);
//            Graphics2D graphics = image.createGraphics();
//            this.paint(graphics);
//            graphics.dispose();
//            try {
//                ImageIO.write(image, extension, file);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    // 从图片打开
//    public void loadImageToPanel() {
//        JFileChooser fileChooser = new JFileChooser();
//        fileChooser.setFileFilter(new FileNameExtensionFilter("images", "*.png", "*.jpg"));
//        int result = fileChooser.showOpenDialog(null);
//        if (result != JFileChooser.APPROVE_OPTION) {
//            return;
//        }
//        String filePath = fileChooser.getSelectedFile().getAbsolutePath();
//        try {
//            // 读取该张图片
//            image = ImageIO.read(new File(filePath));
//            // 清除所有历史
//            EventListener.getInstance().clear(false);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return;
//        }
//    }
//
//    public void clearFile() {
//        // 移除内部的图片缓存
//        this.image = null;
//    }

    public  int getCurId() {
        return curId;
    }

    public boolean nextPage() {
        return this.turnToPage(curId+1);
    }

    public boolean lastPage() {
        return this.turnToPage(curId-1);
    }
}
