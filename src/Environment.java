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
}

public class Environment extends JPanel {
    private static final int WIDTH = 400;
    private static final int HEIGHT = 200;
    private static final int NUM_OBSTACLES = 25;
    private List<Obstacle> obstacles;
    private Random random;
    private Traveler traveler;
    private int goalX; // 终点的 x 坐标
    private int goalY; // 终点的 y 坐标
    private int goalRadius; // 终点的半径

    public Environment(Traveler traveler) {
        this.obstacles = new ArrayList<>();
        this.random = new Random();
        this.traveler = traveler;
        this.goalRadius = 10; // 设置终点的半径
        generateObstacles();
        generateGoal();
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

    private void generateGoal() {
        boolean overlaps;
        do {
            goalX = WIDTH - 30; // 设置终点的 x 坐标
            goalY = random.nextInt(HEIGHT - 30) + 15; // 随机设置终点的 y 坐标
            overlaps = false;

            // 检查终点是否与障碍物重叠
            Rectangle goalBounds = new Rectangle(goalX - goalRadius, goalY - goalRadius, goalRadius * 2, goalRadius * 2);
            for (Obstacle obstacle : obstacles) {
                if (goalBounds.intersects(obstacle.getPolygon().getBounds())) {
                    overlaps = true;
                    break;
                }
            }
        } while (overlaps); // 如果重叠，则重新生成
    }

    private Polygon createRandomPolygon() {
        int shapeType = random.nextInt(4);

        switch (shapeType) {
            case 0:
                return createRectangle();
            case 1:
                return createTriangle();
            case 2:
                return createHexagon();
            case 3:
                return createPentagon();
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

        for (Obstacle obstacle : obstacles) {
            obstacle.draw(g);
        }

        traveler.draw(g); // 绘制 Traveler

        // 绘制绿色圆形终点
        g.setColor(Color.GREEN);
        g.fillOval(goalX - goalRadius, goalY - goalRadius, goalRadius * 2, goalRadius * 2);
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }
}
