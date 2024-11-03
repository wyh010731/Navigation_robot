import java.awt.*;

public class Traveler {
    private int x, y; // 圆心坐标
    private int radius;

    public Traveler(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillOval(x - radius, y - radius, radius * 2, radius * 2);
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
    }
}
