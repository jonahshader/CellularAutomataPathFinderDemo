package jonahshader;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.event.MouseEvent;

import java.awt.*;

public class Main extends PApplet{
    public final int WIDTH = 180;
    public final int HEIGHT = 120;
    boolean[][] world, exploredArea, newArea;
    Point[][] parents;
    int targetX, targetY, startX, startY;
    double fillPercent = 0.25;
    PGraphics batch;
    boolean reachedTarget;
    boolean paused = false;

    public static void main(String[] args) {
        PApplet.main("jonahshader.Main");
    }

    @Override
    public void settings() {
        size(WIDTH * 6, HEIGHT * 6);
        noSmooth();
    }

    @Override
    public void setup() {
        frameRate(999);
        reachedTarget = false;
        batch = createGraphics(WIDTH, HEIGHT);
        world = new boolean[WIDTH][HEIGHT];
        exploredArea = new boolean[WIDTH][HEIGHT];
        parents = new Point[WIDTH][HEIGHT];
        generateTerrain();
        calculateStartEndPos();
        exploredArea[startX][startY] = true;
    }

    @Override
    public void draw() {
        int mouseWorldX = (int) (((float) mouseX / width) * WIDTH);
        int mouseWorldY = (int) (((float) mouseY / height) * HEIGHT);
        if (mouseWorldX >= 0 && mouseWorldX < WIDTH && mouseWorldY >= 0 && mouseWorldY < HEIGHT && mousePressed)
            world[mouseWorldX][mouseWorldY] = true;
        if (!paused) {
            spread();
        }
        batch.beginDraw();
        batch.background(0, 0, 0);
        drawArray(world, 255, 255, 255);
        drawArray(exploredArea, 127, 127, 127);
        if (reachedTarget) {
            drawPath();
        }
        batch.stroke(0, 0, 255);
        batch.point(startX, startY);
        batch.stroke(0, 255, 0);
        batch.point(targetX, targetY);
        batch.endDraw();

        image(batch, 0, 0, width, height);
        fill(0, 255, 255);
        textSize(22);
        text("Fill percent: " + fillPercent, 0, 20);
    }

    private void generateTerrain() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                world[x][y] = Math.random() < fillPercent;
            }
        }
    }

    private void calculateStartEndPos() {
        boolean foundStart = false;
        while (!foundStart) {
            startX = (int) (Math.random() * WIDTH);
            startY = (int) (Math.random() * HEIGHT);
            foundStart = !world[startX][startY];
        }

        boolean foundTarget = false;
        while (!foundTarget) {
            targetX = (int) (Math.random() * WIDTH);
            targetY = (int) (Math.random() * HEIGHT);
            foundTarget = !world[targetX][targetY];
        }
    }

    private void spread() {
        if (!reachedTarget) {
            newArea = new boolean[WIDTH][HEIGHT];
            for (int x = 1; x < WIDTH - 1; x++) {
                for (int y = 1; y < HEIGHT - 1; y++) {
                    if (x == targetX && y == targetY && exploredArea[x][y]) {
                        //DONE!
                        System.out.println("Reached target");
                        reachedTarget = true;
                    } else {
                        if (exploredArea[x][y] && !newArea[x][y]) {
                            if (!exploredArea[x + 1][y]) {
                                if (!world[x + 1][y]) {
                                    exploredArea[x + 1][y] = true;
                                    newArea[x + 1][y] = true;
                                    parents[x + 1][y] = new Point(x, y);
                                }
                            }
                            if (!exploredArea[x - 1][y]) {
                                if (!world[x - 1][y]) {
                                    exploredArea[x - 1][y] = true;
                                    newArea[x - 1][y] = true;
                                    parents[x - 1][y] = new Point(x, y);
                                }
                            }
                            if (!exploredArea[x][y + 1]) {
                                if (!world[x][y + 1]) {
                                    exploredArea[x][y + 1] = true;
                                    newArea[x][y + 1] = true;
                                    parents[x][y + 1] = new Point(x, y);
                                }
                            }
                            if (!exploredArea[x][y - 1]) {
                                if (!world[x][y - 1]) {
                                    exploredArea[x][y - 1] = true;
                                    newArea[x][y - 1] = true;
                                    parents[x][y - 1] = new Point(x, y);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void drawPath() {
        batch.stroke(255, 0, 0);
        boolean foundStart = false;
        Point parent = parents[targetX][targetY];
        while (!foundStart) {
            if (parent == null) {
                foundStart = true;
            } else {
                batch.point(parent.x, parent.y);
                parent = parents[(int) parent.getX()][(int) parent.getY()];
            }
        }
    }

    private void drawArray(boolean[][] array, int r, int g, int b) {
        batch.stroke(r, g, b);
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (array[x][y]) {
                    batch.point(x, y);
                }
            }
        }
    }
    @Override
    public void keyPressed() {
        if (keyCode == 'p' || keyCode ==  'P') {
            paused = !paused;
        } else {
            world = new boolean[WIDTH][HEIGHT];
            exploredArea = new boolean[WIDTH][HEIGHT];
            parents = new Point[WIDTH][HEIGHT];
            generateTerrain();
            calculateStartEndPos();
            exploredArea[startX][startY] = true;
            reachedTarget = false;
        }

    }

    @Override
    public void mouseWheel(MouseEvent event) {
        fillPercent -= event.getCount() * 0.025;
    }

}

