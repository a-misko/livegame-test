package org.gamelive;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GameLiveApplication {

  public static void main(String... args) {
    int stepEvolution = 10;
    byte[][] area = initLive(25, 25, new Point(12, 14), 5);
    System.out.println("Init area");
    print(area);
    for (int step = 1; step <= stepEvolution; step++) {
      area = nextEvolution(area);
      System.out.println("\nEvolution step " + step);
      print(area);

    }
  }

  public static byte[][] initLive(int xSize, int ySize, Point start, int liveSize) {
    List<Point> points = new ArrayList<>(liveSize);
    points.add(start);

    while (points.size() < liveSize) {
      Point newPoint = generateNewPoint(points.get(points.size() - 1));
      while (!isValidPoint(newPoint, points, xSize, ySize)) {
        newPoint = generateNewPoint(points.get(points.size() - 1));
      }
      points.add(newPoint);
    }
    byte[][] area = new byte[xSize][ySize];
    points.forEach(point -> area[point.x][point.y] = 1);
    return area;
  }

  private static boolean isValidPoint(Point newPoint, List<Point> existingPoints, int xMax, int yMax) {
    if (existingPoints.stream().noneMatch(point -> point.equals(newPoint))) {
      return newPoint.x < xMax && newPoint.y < yMax && newPoint.x >= 0 && newPoint.y >= 0;
    }
    return false;
  }

  public static byte[][] nextEvolution(byte[][] area) {
    byte[][] nextArea = new byte[area.length][area[0].length];
    for (int x = 0; x < area.length; x++) {
      for (int y = 0; y < area[0].length; y++) {
        nextArea[x][y] = newValue(new Point(x, y), area);
      }
    }
    return nextArea;
  }

  public static byte newValue(Point point, byte[][] area) {
    Set<Point> pointsAround = new HashSet<>();
    pointsAround.add(newPoint(point, Math.max(0, point.x - 1), point.y));
    pointsAround.add(newPoint(point, point.x, Math.max(0, point.y - 1)));
    pointsAround.add(newPoint(point, Math.max(0, point.x - 1), Math.max(0, point.y - 1)));
    pointsAround.add(newPoint(point, Math.min(area.length - 1, point.x + 1), Math.max(0, point.y - 1)));
    pointsAround.add(newPoint(point, Math.max(0, point.x - 1), Math.min(area[0].length - 1, point.y + 1)));
    pointsAround.add(newPoint(point, Math.min(area.length - 1, point.x + 1), point.y));
    pointsAround.add(newPoint(point, point.x, Math.min(area[0].length - 1, point.y + 1)));
    pointsAround.add(newPoint(point, Math.min(area.length - 1, point.x + 1), Math.min(area[0].length - 1, point.y + 1)));
    int cntLive = 0;
    for (Point p : pointsAround) {
      if (p != null && area[p.x][p.y] == 1) {
        cntLive++;
      }
    }

    if ((cntLive == 2 || cntLive == 3) && area[point.x][point.y] == 1) {
      return 1;
    } else if (cntLive == 3 && area[point.x][point.y] == 0) {
      return 1;
    } else {
      return 0;
    }

  }

  static Point newPoint(Point currentPoint, int newX, int newY) {
    Point newPoint = new Point(newX, newY);
    if (newPoint.equals(currentPoint)) {
      return null;
    }
    return newPoint;
  }

  public static Point generateNewPoint(Point oldPoint) {
    int x = generateNewInt(oldPoint.x);
    int y = generateNewInt(oldPoint.y);
    return new Point(x, y);
  }

  private static int generateNewInt(int old) {
    int v = new Random().nextInt(3);
    return switch (v) {
      case 0 -> old;
      case 1 -> old - 1;
      case 2 -> old + 1;
      default -> throw new IllegalStateException("Unexpected value: " + v);
    };
  }

  static class Point {

    private final int x;
    private final int y;

    Point(int x, int y) {
      this.x = x;
      this.y = y;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      Point point = (Point) o;

      if (x != point.x) {
        return false;
      }
      return y == point.y;
    }

    @Override
    public int hashCode() {
      int result = x;
      result = 31 * result + y;
      return result;
    }

    @Override
    public String toString() {
      return "Point{" +
          "x=" + x +
          ", y=" + y +
          '}';
    }
  }

  public static void print(byte area[][]) {
    for (byte[] row : area) {
      System.out.println();
      for (byte x : row) {
        char v = x == 1 ? 'Х' : '*';
        System.out.print(v);
      }
    }
  }
}
