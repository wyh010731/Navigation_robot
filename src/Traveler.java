import java.awt.*;
import java.util.ArrayList;

public class Traveler {
    private int x, y; // 圆心坐标
    private int radius;
    private ArrayList<Point> path; // 存储运动路径

    public Traveler(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.path = new ArrayList<>();
        path.add(new Point(x, y)); // 初始位置加入路径
    }

    public void draw(Graphics g) {
        // 绘制 Traveler 的圆
        g.setColor(Color.RED);
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2);

        // 绘制运动路径
        g.setColor(Color.red);
        for (int i = 0; i < path.size() - 1; i++) {
            Point p1 = path.get(i);
            Point p2 = path.get(i + 1);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x - radius, y - radius, radius * 2, radius * 2);
    }

    public boolean overlaps(Obstacle obstacle) {
        return getBounds().intersects(obstacle.getPolygon().getBounds());
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        path.add(new Point(x, y)); // 将新的位置加入路径
    }

    // 新增的方法
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    // 新增的方法
    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
        path.add(new Point(x, y)); // 将移动后的坐标加入路径
    }

    // 获取路径
    public ArrayList<Point> getPath() {
        return path;
    }
}
