import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Obstacle {
    private Polygon polygon;

    public Obstacle(Polygon polygon) {
        this.polygon = polygon;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillPolygon(polygon);
    }

    public boolean intersects(Obstacle other) {
        Area area1 = new Area(this.polygon);
        Area area2 = new Area(other.getPolygon());
        area1.intersect(area2);
        return !area1.isEmpty();
    }

    // 获取最高点
    public Point getHighestPoint() {
        int highestY = Integer.MIN_VALUE;
        int highestX = 0;

        for (int i = 0; i < polygon.npoints; i++) {
            if (polygon.ypoints[i] > highestY) {
                highestY = polygon.ypoints[i];
                highestX = polygon.xpoints[i];
            }
        }
        return new Point(highestX, highestY);
    }

    // 获取最低点
    public Point getLowestPoint() {
        int lowestY = Integer.MAX_VALUE;
        int lowestX = 0;

        for (int i = 0; i < polygon.npoints; i++) {
            if (polygon.ypoints[i] < lowestY) {
                lowestY = polygon.ypoints[i];
                lowestX = polygon.xpoints[i];
            }
        }
        return new Point(lowestX, lowestY);
    }
}

public class Environment extends JPanel {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 200;
    private static final int NUM_OBSTACLES = 25; // 增加障碍物数量
    private List<Obstacle> obstacles;
    private Random random;

    public Environment() {
        this.obstacles = new ArrayList<>();
        this.random = new Random();
        generateObstacles();
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
    }

    private void generateObstacles() {
        while (obstacles.size() < NUM_OBSTACLES) {
            Obstacle newObstacle = new Obstacle(createRandomPolygon());
            boolean overlaps = false;

            for (Obstacle obstacle : obstacles) {
                if (newObstacle.intersects(obstacle)) {
                    overlaps = true;
                    break;
                }
            }

            if (!overlaps) {
                obstacles.add(newObstacle);
            }
        }
    }

    private Polygon createRandomPolygon() {
        int shapeType = random.nextInt(4); // 增加多种形状类型

        switch (shapeType) {
            case 0: // Rectangle
                return createRectangle();
            case 1: // Triangle
                return createTriangle();
            case 2: // Hexagon
                return createHexagon();
            case 3: // Pentagon
                return createPentagon(); // 新增五边形
            default:
                return null;
        }
    }

    private Polygon createRectangle() {
        int x = random.nextInt(WIDTH - 60);
        int y = random.nextInt(HEIGHT - 60);
        int width = random.nextInt(40) + 20;
        int height = random.nextInt(40) + 20;
        return new Polygon(new int[]{x, x + width, x + width, x},
                new int[]{y, y, y + height, y + height}, 4);
    }

    private Polygon createTriangle() {
        int x = random.nextInt(WIDTH - 50);
        int y = random.nextInt(HEIGHT - 50);
        return new Polygon(new int[]{x, x + 40, x + 20},
                new int[]{y, y, y + 40}, 3);
    }

    private Polygon createHexagon() {
        int x = random.nextInt(WIDTH - 50);
        int y = random.nextInt(HEIGHT - 50);
        return new Polygon(new int[]{x, x + 20, x + 40, x + 40, x + 20, x},
                new int[]{y + 10, y, y + 10, y + 30, y + 40, y + 30}, 6);
    }

    private Polygon createPentagon() {
        int x = random.nextInt(WIDTH - 50);
        int y = random.nextInt(HEIGHT - 50);
        return new Polygon(new int[]{x + 20, x + 40, x + 30, x, x - 10},
                new int[]{y, y + 20, y + 40, y + 30, y + 20}, 5);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 绘制所有障碍物并输出其最高点和最低点
        for (Obstacle obstacle : obstacles) {
            obstacle.draw(g);
            Point highestPoint = obstacle.getHighestPoint();
            Point lowestPoint = obstacle.getLowestPoint();

            // 输出最高点和最低点
            System.out.println("Obstacle Highest Point: " + highestPoint);
            System.out.println("Obstacle Lowest Point: " + lowestPoint);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Maze-like Environment");
        Environment environment = new Environment();
        frame.add(environment);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
}
