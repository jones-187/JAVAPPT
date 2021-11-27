package Listener;

import java.awt.event.*;
import javax.swing.event.*;

import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import Shape.*;
import Shape.Shape;
import UI.MainWindow;

import UI.*;
// 该类实现了鼠标事件、键盘事件和按钮点击事件的监听
public class EventListener extends MouseInputAdapter implements  KeyListener { //ActionListener,, ChangeListener
    private int id;
    // 使用单例模式
    private static ArrayList<EventListener> elList = new ArrayList<>();
    // 点击点和落点的坐标
    private int x1, x2, y1, y2,dragX,dragY;
    // 当前选中的颜色
    private Color selectedColor;
    // 当前背景色
    private Color backgroundColor;
    // 当前使用的操作
    private String operation;
    // 当前线条粗细
    private int width;
    // 画笔
    private Graphics pen;
    // 所有画过的图
    private Deque<Shape> history = new LinkedList<>();
    // 保存实时按键的栈
    private final Deque<Integer> stack = new LinkedList<>();
    // 保存按鼠标时的历史状态（用于笔和橡皮擦的撤销）
    private Deque<Shape> previous = new LinkedList<>();
    // 选中的图形
    private Shape selectedShape=null;
    private Shape afterSelectedShape=null;

    private EventListener(int id) {
        this.id=id;
        // 默认画笔为黑色，背景色为白色，选中操作为铅笔
        this.selectedColor = Color.BLACK;
        this.backgroundColor = Color.WHITE;
        this.operation = "铅笔";
        this.width = 1;
    }

    // 获取实例的静态方法
    public static EventListener getInstance(int index) {
        return (elList.isEmpty() || index>=elList.size() || index<0 )? null : elList.get(index);
    }

    public static void addInstance(){
        elList.add(new EventListener(elList.size()));
    }

    public static void addInstance(int index) {
        for (int i = index; i <elList.size() ; i++) {
            elList.get(i).id++;
        }
        elList.add(index,new EventListener(index));
    }

    // 清除所有状态并重新绘制
    public void clear(boolean clearFile) {
        history.clear();
        previous.clear();
        if (clearFile) {
            // 清除之前打开的文件
            DrawingArea.getInstance(this.id).clearFile();
        }
        DrawingArea.getInstance(this.id).repaint();
    }
//
//    @Override
//    public void actionPerformed(ActionEvent e) {
//        JButton instance = (JButton) e.getSource();
//        // 点击的是颜色（因为颜色按钮没有文字）
//        if ("".equals(e.getActionCommand())) {
//            if (UI.Toolbar.getInstance().isForebackgroundSelected()) {
//                // 设置前景色
//                selectedColor = instance.getBackground();
//            } else {
//                // 设置背景色
//                backgroundColor = instance.getBackground();
//                // 刷新画板
//                this.setBackgroundColor();
//            }
//        } else {
//            // 选择帮助操作时输出帮助信息并return
//            if (instance.getText().equals("帮助")) {
//                showHelpMessage();
//                UI.MainWindow.getInstance().requestFocus();
//                return;
//            }
//            // 否则将操作赋值给参数
//            this.operation = instance.getText();
//        }
//        // 将焦点还给绘图区域（没有焦点没有办法响应键盘事件）
//        UI.MainWindow.getInstance().requestFocus();
//    }

    @Override
    public void mousePressed(MouseEvent e) {
        // 保存该个时刻的最新状态
        if (history.peekLast() == null)
            previous.push(new InitShape());
        else
            previous.push(history.peekLast());
        // 按下鼠标时调用的函数
        x1 = e.getX();
        y1 = e.getY();
        x2 = e.getX();
        y2 = e.getY();
        // 原地画点，为了和mouseDragged协作实现动态拖拽的效果
        if (this.operation.equals("橡皮擦")) {
            addEraser();
        } else if (this.operation.equals("文本")) {
            addText();
        } else if (this.operation.equals("拖拽")) {
            dragging(x1,y1);
        } else {
            addShape();
        }
        DrawingArea.getInstance( MainWindow.getInstance().getCurId() ).requestFocus();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        x2 = e.getX();
        y2 = e.getY();
        switch (this.operation) {
            case "拖拽":
                dragging2(x2, y2);
                addDragging();
                break;
            case "铅笔":
                addShape();
                break;
            case "橡皮擦":
                addEraser();
                break;
            case "文本":
                revert(true);
                addText();
                break;
            default:
                revert(true);
                addShape();
                break;
        }
    }

    private void dragging2(int x2, int y2) {
//        未选中，则不拖动任何
        if (selectedShape == null) {
            return;
        }

        int dX=x2-dragX;
        int dY=y2-dragY;
        boolean changeFlag=false;
        if(selectedShape instanceof InitShape) changeFlag=true;
        for (Shape shape:history
            ){
            if(changeFlag){
                shape.x1+=dX;
                shape.y1+=dY;
                if (shape instanceof MultiShape){
                    ((MultiShape)shape).x2+=dX;
                    ((MultiShape)shape).y2+=dY;
                }
            }
            if (shape.equals(selectedShape)) {
                changeFlag=true;
            }
            if(shape.equals(afterSelectedShape)){
                changeFlag=false;
                break;
            }
        }

//        for (Shape.Shape shape:history
//             ) {
//            if (shape == null) {
//                continue;
//            }
//            shape.x1+=dX;
//            shape.y1+=dY;
//            if (shape instanceof Shape.MultiShape){
//                ((Shape.MultiShape)shape).x2+=dX;
//                ((Shape.MultiShape)shape).y2+=dY;
//            }
//        }
//        for (Shape.Shape shape:previous
//        ) {
//            if (shape == null) {
//                continue;
//            }
//            shape.x1+=dX;
//            shape.y1+=dY;
//            if (shape instanceof Shape.MultiShape){
//                ((Shape.MultiShape)shape).x2+=dX;
//                ((Shape.MultiShape)shape).y2+=dY;
//            }
//        }
//        释放选中的图形
        selectedShape=null;
        afterSelectedShape=null;
        MainWindow.getInstance().repaint();
    }

    /*
     * 遍历操作历史。
     * 计算每一个操作历史的最大x 最小x  最大Y  最小Y
     * 判断是否包括了x1 y1
     * 如果不包括，则跳过该操作
     * 反之，
     * 获取操作面积，与已有面积对比，小则替换
     *
     * 循环直至遍历完。获取当前xy的最小操作的element，然后选中该操作
     * */
    private void dragging(int x1, int y1) {
        dragX=x1;
        dragY=y1;

        boolean resetFlag=true;
        int maxX = 0,maxY = 0,minX = 0,minY = 0,minS=0;
        int curIndex=0,nextIndex=0,selectedIndex=-1;
        Iterator<Shape> curShape=previous.iterator();
//        Iterator<Shape.Shape> nextShape=previous.iterator();
//        Iterator<Shape.Shape> selectedShape=null;
        LinkedList<Shape> Rprevious = new LinkedList<>();
//        倒置操作栈,用于遍历
        while(curShape.hasNext()){
            Rprevious.addFirst(curShape.next());
        }
//        nextShape=Rprevious.iterator();
//        跳过开头的null

//        Rprevious.get(0);
//        curShape=nextShape.;
        if(Rprevious.size()>1){
            nextIndex=1;
        }
        else
            return;
        for (Shape shape:history
        ) {
            if (shape == null) {
                continue;
            }
//            设置当前遍历操作的初始最大最小
            if(resetFlag){
                if (shape instanceof MultiShape){
                    maxX= Math.max(((MultiShape) shape).x2, shape.x1);
                    maxY= Math.max(((MultiShape) shape).y2, shape.y1);
                    minX= Math.min(((MultiShape) shape).x2, shape.x1);
                    minY= Math.min(((MultiShape) shape).y2, shape.y1);
                }
                else {
                    maxX=shape.x1;
                    maxY=shape.y1;
                    minX=shape.x1;
                    minY=shape.y1;
                }
                resetFlag=false;
            }
//            寻找当前遍历操作的最大最小值  _xy的
            if (shape instanceof MultiShape){

                maxX= Math.max(maxX, Math.max(((MultiShape) shape).x2, shape.x1));
                maxY= Math.max(maxY, Math.max(((MultiShape) shape).y2, shape.y1));
                minX= Math.min(minX, Math.min(((MultiShape) shape).x2, shape.x1));
                minY= Math.min(minY, Math.min(((MultiShape) shape).y2, shape.y1));
            }
            else {
                maxX=Math.max(maxX, shape.x1);
                maxY=Math.max(maxY, shape.y1);
                minX=Math.min(minX, shape.x1);
                minY=Math.min(minY, shape.y1);
            }
//            如果该操作不包括当前点击点，则跳过该操作
            if(!(x1<=maxX&&x1>=minX&&y1<=maxY&&y1>=minY)){
//                确保下一个操作能够正常推进
                if (nextIndex<Rprevious.size()) {
                    if(shape.equals(Rprevious.get(nextIndex))){
                        //                    设置移动到下一操作
                        resetFlag=true;
                        curIndex=nextIndex;
                        nextIndex++;
                    }
                }
//                对于最后一个操作，如果不包含当前点击点，则不需要任何调整
                continue;
            }

            if(shape.equals(history.peekLast())){
                if(selectedIndex==-1){
                    minS=(maxX-minX)*(maxY-minY);
                    selectedIndex=curIndex;
                }
                else {
                    if(minS!=Math.min(minS, (maxX-minX)*(maxY-minY))){
                        minS=Math.min(minS, (maxX-minX)*(maxY-minY));
                        selectedIndex=curIndex;
                    }
                }
            }

//            如果到达操作末点，则计算是否是当前操作最小面积
            if (nextIndex<Rprevious.size()) {
                if(shape.equals(Rprevious.get(nextIndex))){
                    if(selectedIndex==-1){
                        minS=(maxX-minX)*(maxY-minY);
                        selectedIndex=curIndex;
                    }
                    else {
                        if(minS!=Math.min(minS, (maxX-minX)*(maxY-minY))){
                            minS=Math.min(minS, (maxX-minX)*(maxY-minY));
                            selectedIndex=curIndex;
                        }
                    }
//                    设置移动到下一操作
                    resetFlag=true;
                    curIndex=nextIndex;
                    nextIndex++;
                }
            }

        }
        if(selectedIndex!=-1){
            selectedShape=Rprevious.get(selectedIndex);
            assert selectedIndex+1<Rprevious.size();
            afterSelectedShape=Rprevious.get(selectedIndex+1);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x2 = e.getX();
        y2 = e.getY();
        switch (this.operation) {
            case "拖拽":
//                dragging(x1, y1);
                break;
            case "铅笔":
                addShape();
                x1 = x2;
                y1 = y2;
                break;
            case "橡皮擦":
                addEraser();
                x1 = x2;
                y1 = y2;
                break;
            case "文本":
                revert(true);
                addText();
                break;
            default:
                revert(true);
                addShape();
                break;
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        // 有大于一个按键并且上一个按键为Ctrl
        if (stack.size() >= 1 && stack.peek() == 17) {
            switch (e.getKeyCode()) {
                case 90 -> revert(false); // Ctrl+Z -> 撤销
                case 83 -> DrawingArea.getInstance(this.id).savePanelAsImage(); // Ctrl+S -> 保存图片
                case 79 -> DrawingArea.getInstance(this.id).loadImageToPanel(); // Ctrl+O -> 打开图片
                case 81 -> this.clear(true); // Ctrl+Q -> 清空历史
                case 72 -> showHelpMessage();// Ctrl+H -> 弹出帮助信息
            }
        }
        // 将按键码压栈
        stack.push(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        // 松开按键则弹栈
        stack.pop();
    }



    // 撤销有两种类型，锁定撤销和非锁定撤销
    // 锁定撤销时，起点不会被删除，在动态拖拽操作中使用
    // 非锁定撤销时，起点和边同时被删除，在手动调用的撤销操作中使用
    public void revert(boolean fixed) {
        Shape toCompare = fixed ? previous.element() : previous.poll();
        Shape tmp;
        while ((tmp = history.peekLast()) != null) {
            if (!tmp.equals(toCompare)) {
                history.pollLast();
            } else {
                break;
            }
        }
        MainWindow.getInstance().repaint();
    }

    public Color getSelectedColor() {
        return this.selectedColor;
    }

    public String getOperation() {
        return this.operation;
    }

    public Color getBackgroundColor() {
        return this.backgroundColor;
    }

    public int getWidth() {
        return this.width;
    }

    public void setPen(Graphics pen) {
        this.pen = pen;
    }

    private void addDragging()
    {
        // 添加新动作
//        Shape.Shape tmp = new Shape.DragShape(x1, y1, x2, y2);
        Shape tmp = new DragShape(x1, y1);
        // 加入历史
        history.add(tmp);
    }

    private void addShape() {
        // 添加新图
        Shape tmp = new MultiShape(x1, y1, x2, y2);
        // 加入历史
        history.add(tmp);
        // 用pen将tmp画在图上
        tmp.draw(pen);
    }

    // 设置背景色
    public void setBackgroundColor() {
        DrawingArea instance = DrawingArea.getInstance( MainWindow.getInstance().getCurId() );
        instance.setBackground(backgroundColor);
        for (var item : history) {
            if (item instanceof Eraser) {
                // 刷新历史橡皮到当前背景颜色
                item.refresh();
            }
        }
        // 重新绘制
        instance.repaint();
    }

    private void addEraser() {
        // 添加新图
        Shape tmp = new Eraser(x1, y1);
        // 加入历史
        history.add(tmp);
        // 用pen将tmp画在图上
        tmp.draw(pen);
    }

    private void addText() {
        // 添加新文本
        Shape tmp = new TextShape(x2, y2);
        // 加入历史
        history.add(tmp);
        // 用pen将tmp画在图上
        tmp.draw(pen);
    }

    public void showHelpMessage() {
        JOptionPane.showInternalMessageDialog(null, Utils.getHelpMessage(), "帮助", JOptionPane.INFORMATION_MESSAGE);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setSelectedColor(Color selectedColor) {
        this.selectedColor = selectedColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Deque<Shape> getPrevious() {
        return this.previous;
    }

    public Deque<Shape> getHistory() {
        return this.history;
    }

    public static int getCount(){
        return EventListener.elList.size();
    }


    public static void reset() {
        elList.clear();
    }

    public void load(Deque<Shape> history, Deque<Shape> previous) {
        this.history=history;
        this.previous=previous;
    }
}
