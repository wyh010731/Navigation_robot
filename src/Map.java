import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Map extends JPanel {
    private static final int GRID_SIZE = 10; // 网格大小
    private static final int CELL_SIZE = 50; // 每个单元格的大小
    private static final int MAX_OBSTACLES = 30; // 障碍物的最大数量
    private boolean[][] grid; // 用于表示是否有障碍物
    private Point start; // 起始点
    private Point goal; // 终点
    private Random random;

    public Map() {
        setPreferredSize(new Dimension(GRID_SIZE * CELL_SIZE, GRID_SIZE * CELL_SIZE));
        setBackground(Color.LIGHT_GRAY);
        grid = new boolean[GRID_SIZE][GRID_SIZE];
        random = new Random();
        setStartAndGoal(); // 先设置起点和终点
        generateObstacles(); // 然后生成障碍物
    }

    private void generateObstacles() {
        int count = 0;

        while (count < MAX_OBSTACLES) {
            int x = random.nextInt(GRID_SIZE);
            int y = random.nextInt(GRID_SIZE);

            // 确保该位置没有障碍物并且不是起点
            if (!grid[x][y] && !(x == start.x && y == start.y)) {
                grid[x][y] = true; // 设置为有障碍物
                count++;

                // 检查路径连通性
                if (!isPathAvailable()) {
                    grid[x][y] = false; // 如果导致封闭，移除障碍物
                    count--; // 障碍物计数减一
                }
            }
        }
    }

    private void setStartAndGoal() {
        start = new Point(0, 0); // 起点固定为(0, 0)
        goal = new Point(GRID_SIZE - 1, GRID_SIZE - 1); // 终点固定为(9, 9)

        // 确保终点是可通行的
        grid[goal.x][goal.y] = false;
    }

    private boolean isPathAvailable() {
        boolean[][] visited = new boolean[GRID_SIZE][GRID_SIZE];
        return dfs(start.x, start.y, visited);
    }

    private boolean dfs(int x, int y, boolean[][] visited) {
        if (x < 0 || x >= GRID_SIZE || y < 0 || y >= GRID_SIZE || visited[x][y] || grid[x][y]) {
            return false; // 超出边界或已访问或是障碍物
        }

        visited[x][y] = true; // 标记为已访问

        // 如果到达终点
        if (x == goal.x && y == goal.y) {
            return true;
        }

        // 递归遍历四个方向
        return dfs(x + 1, y, visited) || dfs(x - 1, y, visited) ||
                dfs(x, y + 1, visited) || dfs(x, y - 1, visited);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.BLACK);

        // 绘制网格
        for (int i = 0; i <= GRID_SIZE; i++) {
            g.drawLine(0, i * CELL_SIZE, GRID_SIZE * CELL_SIZE, i * CELL_SIZE); // 绘制横线
            g.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, GRID_SIZE * CELL_SIZE); // 绘制竖线
        }

        // 绘制障碍物
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                if (grid[x][y]) {
                    g.fillRect(x * CELL_SIZE + 1, y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2); // 填充障碍物
                }
            }
        }

        // 绘制起始点（红色）
        g.setColor(Color.RED);
        g.fillRect(start.x * CELL_SIZE + 1, start.y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);

        // 绘制终点（绿色）
        g.setColor(Color.GREEN);
        g.fillRect(goal.x * CELL_SIZE + 1, goal.y * CELL_SIZE + 1, CELL_SIZE - 2, CELL_SIZE - 2);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Map with Obstacles");
        Map map = new Map();
        frame.add(map);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
