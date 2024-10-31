import java.awt.*;

public class Obstacle {
    private String shape;
    private int x;
    private int y;

    public Obstacle(String shape, int x, int y) {
        this.shape = shape;
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(0, 0, 139)); // 设置障碍物为深蓝色
        switch (shape) {
            case "Square":
                g.fillRect(x * 50, y * 50, 50, 50);
                break;
            case "Rectangle":
                g.fillRect(x * 50, y * 50, 50, 30);
                break;
            case "Hexagon":
                int[] xPoints = {x * 50 + 25, x * 50 + 50, x * 50 + 50, x * 50 + 25, x * 50, x * 50};
                int[] yPoints = {y * 50, y * 50 + 15, y * 50 + 35, y * 50 + 50, y * 50 + 35, y * 50 + 15};
                g.fillPolygon(xPoints, yPoints, 6);
                break;
            case "Triangle":
                int[] xTriangle = {x * 50 + 25, x * 50 + 50, x * 50};
                int[] yTriangle = {y * 50, y * 50 + 50, y * 50 + 50};
                g.fillPolygon(xTriangle, yTriangle, 3);
                break;
        }
    }
}
