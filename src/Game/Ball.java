package Game;

public class Ball {
    private int x, y;    // 위치
    private int r;       // 반지름
    private double vx;   // 수평 속도 (좌우 이동)
    private double vy;   // 수직 속도 (점프 및 낙하)

    // 생성자 수정: r 값을 제대로 할당해야 합니다.
    public Ball(int x, int y, int r) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.vx = 0;
        this.vy = 0;
    }

    // 위치 업데이트를 위한 편의 메서드
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }


    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public int getR() { return r; }
    
    public double getVx() { return vx; }
    public void setVx(double vx) { this.vx = vx; }

    public double getVy() { return vy; }
    public void setVy(double vy) { this.vy = vy; }
}