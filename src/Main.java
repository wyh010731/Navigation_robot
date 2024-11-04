import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Point;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze-like Environment");
        Traveler traveler = new Traveler(20, 100, 8); // 初始化 Traveler 的位置和半径
        Environment environment = new Environment(traveler); // 传递 Traveler 对象

        // 添加键盘监听器以允许旅行者移动
        environment.setFocusable(true);
        environment.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int dx = 0;
                int dy = 0;

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        dy = -5;
                        break;
                    case KeyEvent.VK_DOWN:
                        dy = 5;
                        break;
                    case KeyEvent.VK_LEFT:
                        dx = -5;
                        break;
                    case KeyEvent.VK_RIGHT:
                        dx = 5;
                        break;
                    case KeyEvent.VK_SPACE:
                        // 计算路径到目标
                        environment.calculatePath(new Point(environment.getGoalX(), environment.getGoalY()));
                        break;
                }

                // 移动 Traveler
                traveler.move(dx, dy);

                // 重新绘制环境
                environment.repaint();
            }
        });

        frame.add(environment);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
