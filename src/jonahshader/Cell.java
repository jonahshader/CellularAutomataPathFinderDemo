package jonahshader;

public class Cell {
    public int x, y, parentX, parentY;
    public boolean spreaded = false;

    public Cell(int x, int y, int parentX, int parentY) {
        this.x = x;
        this.y = y;
        this.parentX = parentX;
        this.parentY = parentY;
    }
}
