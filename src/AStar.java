import java.awt.Point;
import java.util.*;

public class AStar {

    private static class Node {
        Point point;
        double g; // 从起点到当前节点的成本
        double h; // 当前节点到目标的预估成本
        double f; // g + h
        Node parent; // 父节点

        Node(Point point, double g, double h, Node parent) {
            this.point = point;
            this.g = g;
            this.h = h;
            this.f = g + h;
            this.parent = parent;
        }
    }

    public List<Point> findPath(Point start, Point goal, List<Obstacle> obstacles, int gridSize) {
        // OpenSet使用PriorityQueue来优先处理f值最小的节点
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(node -> node.f));
        Set<Point> closedSet = new HashSet<>();
        Map<Point, Node> openMap = new HashMap<>(); // 存储OpenSet中的节点，用于快速查找

        Node startNode = new Node(start, 0, heuristic(start, goal), null);
        openSet.add(startNode);
        openMap.put(start, startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current.point.equals(goal)) {
                return reconstructPath(current); // 找到目标，重建路径
            }

            closedSet.add(current.point);

            for (Point neighbor : getNeighbors(current.point, gridSize)) {
                if (closedSet.contains(neighbor) || isObstacle(neighbor, obstacles)) {
                    continue;
                }

                double tentativeG = current.g + 1; // 假设每个节点的移动代价为1
                Node neighborNode = openMap.get(neighbor);

                // 如果邻居节点不在openSet中，或者通过当前节点到达邻居节点的g值更小
                if (neighborNode == null || tentativeG < neighborNode.g) {
                    // 如果邻居节点在openSet中，更新它
                    if (neighborNode == null) {
                        neighborNode = new Node(neighbor, tentativeG, heuristic(neighbor, goal), current);
                        openSet.add(neighborNode);
                        openMap.put(neighbor, neighborNode);
                    } else {
                        // 更新g值，并重新计算f值
                        neighborNode.g = tentativeG;
                        neighborNode.f = neighborNode.g + neighborNode.h;
                        neighborNode.parent = current;
                    }
                }
            }
        }
        return Collections.emptyList(); // 没有找到路径
    }

    // 曼哈顿距离作为启发式函数
    private double heuristic(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

    // 重建路径
    private List<Point> reconstructPath(Node current) {
        List<Point> totalPath = new ArrayList<>();
        while (current != null) {
            totalPath.add(current.point);
            current = current.parent;
        }
        Collections.reverse(totalPath); // 从起点到目标点逆序重建路径
        return totalPath;
    }

    // 获取邻居节点（上下左右）
    private List<Point> getNeighbors(Point point, int gridSize) {
        List<Point> neighbors = new ArrayList<>();
        int[] dX = {-1, 0, 1, 0}; // 上下左右
        int[] dY = {0, -1, 0, 1};

        for (int i = 0; i < 4; i++) {
            Point neighbor = new Point(point.x + dX[i], point.y + dY[i]);
            if (neighbor.x >= 0 && neighbor.x < gridSize && neighbor.y >= 0 && neighbor.y < gridSize) {
                neighbors.add(neighbor);
            }
        }
        return neighbors;
    }

    // 检查某个点是否是障碍物
    private boolean isObstacle(Point point, List<Obstacle> obstacles) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getPolygon().contains(point)) {
                return true;
            }
        }
        return false;
    }
}
