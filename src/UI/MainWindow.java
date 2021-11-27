package UI;

import Listener.EventListener;

import javax.swing.*;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Deque;

import Shape.Shape;

// 画图区域（窗口也是在该类中创建的）
public class MainWindow extends  JFrame{
    private int curId=0;

    //    static final long serialVersionUID = 1357997531;
    // 同样使用单例模式
    private static MainWindow db;
//    private BufferedImage image = null;
//  保存的文件位置
    File file=new File(".\\mySerialize");
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
        this.setJMenuBar(new MyMenuBar());
        this.setVisible(true);
    }

    private void bindEvent() {
        DrawingArea.getInstance(curId).bindEvent();
    }

    public  int getCurId() {
        return curId;
    }

    public boolean nextPage() {
        return this.turnToPage(curId+1);
    }

    public boolean lastPage() {
        return this.turnToPage(curId-1);
    }

    //        打包数据
    class SavePackage implements Serializable {
        //PPT张数
        private int count;
        // 所有画过的图
        private  ArrayList<Deque<Shape>> historyList ;
        // 保存按鼠标时的历史状态（用于笔和橡皮擦的撤销）
        private ArrayList<Deque<Shape>> previousList ;

        public SavePackage(int count, ArrayList<Deque<Shape>> historyList, ArrayList<Deque<Shape>> previousList) {
            this.count = count;
            this.historyList = historyList;
            this.previousList = previousList;
        }

        @Override
        public String toString() {
            return "SavePackage{" +
                    "count=" + count +
                    ", historyList=" + historyList +
                    ", previousList=" + previousList +
                    '}';
        }
    }

    public boolean saveAs() {
//      获取当前数据
        ArrayList<Deque<Shape>> historyList = new ArrayList<>();
        ArrayList<Deque<Shape>> previousList = new ArrayList<>() ;
        for (int i = 0; i < EventListener.getCount(); i++) {
            historyList.add(EventListener.getInstance(i).getHistory());
            previousList.add(EventListener.getInstance(i).getPrevious());
        }

//        打包
        SavePackage curData = new SavePackage(EventListener.getCount(),historyList,previousList);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(curData);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean load(){
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
            try {
                SavePackage inData = (SavePackage) ois.readObject();

                this.remove(DrawingArea.getInstance(curId));
                DrawingArea.load(inData.count,inData.historyList,inData.previousList);
                this.add(DrawingArea.getInstance(0));
                curId=0;
                this.remove(PageSwitcher.getInstance());
                PageSwitcher.reset();
                this.add(PageSwitcher.getInstance(),BorderLayout.SOUTH);
                this.revalidate();
                this.repaint();
//                必须手动绑定
                this.bindEvent();
                DrawingArea.getInstance(curId).requestFocus();
//                System.out.println(inData);
                return true;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
