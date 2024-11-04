import java.awt.Point;
import java.util.*;

public class AStar {
    private static class Node {
        Point point;
        double g; // 从起点到当前节点的成本
        double h; // 当前节点到目标的预估成本
        double f; // g + h

        Node(Point point, double g, double h) {
            this.point = point;
            this.g = g;
            this.h = h;
            this.f = g + h;
        }
    }

    public List<Point> findPath(Point start, Point goal, List<Obstacle> obstacles, int gridSize) {
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingDouble(node -> node.f));
        Set<Point> closedSet = new HashSet<>();
        Map<Point, Point> cameFrom = new HashMap<>();

        openSet.add(new Node(start, 0, heuristic(start, goal)));

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current.point.equals(goal)) {
                return reconstructPath(cameFrom, current.point);
            }

            closedSet.add(current.point);
            for (Point neighbor : getNeighbors(current.point, gridSize)) {
                if (closedSet.contains(neighbor) || isObstacle(neighbor, obstacles)) {
                    continue;
                }

                double tentativeG = current.g + 1; // Assume cost to move to neighbor is 1
                Node neighborNode = new Node(neighbor, tentativeG, heuristic(neighbor, goal));

                if (!openSet.contains(neighborNode) || tentativeG < neighborNode.g) {
                    cameFrom.put(neighbor, current.point);
                    if (!openSet.contains(neighborNode)) {
                        openSet.add(neighborNode);
                    }
                }
            }
        }
        return Collections.emptyList(); // No path found
    }

    private double heuristic(Point a, Point b) {
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y); // 曼哈顿距离
    }

    private List<Point> reconstructPath(Map<Point, Point> cameFrom, Point current) {
        List<Point> totalPath = new ArrayList<>();
        while (cameFrom.containsKey(current)) {
            totalPath.add(current);
            current = cameFrom.get(current);
        }
        Collections.reverse(totalPath);
        return totalPath;
    }

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

    private boolean isObstacle(Point point, List<Obstacle> obstacles) {
        for (Obstacle obstacle : obstacles) {
            if (obstacle.getPolygon().contains(point)) {
                return true;
            }
        }
        return false;
    }
}
