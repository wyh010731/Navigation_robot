import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze-like Environment");
        Traveler traveler = new Traveler(20, 100, 8); // 初始化 Traveler 的位置和半径
        Environment environment = new Environment(traveler); // 传递 Traveler 对象

        frame.add(environment);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);

        // 确保 Traveler 不与障碍物重叠
        boolean overlap;
        do {
            overlap = false;
            traveler.setPosition(20, (int) (Math.random() * 200)); // 重新生成 Traveler 位置
            for (Obstacle obstacle : environment.getObstacles()) {
                if (traveler.overlaps(obstacle)) {
                    overlap = true;
                    break;
                }
            }
        } while (overlap);

        environment.repaint(); // 重绘环境以显示 Traveler
    }
}
