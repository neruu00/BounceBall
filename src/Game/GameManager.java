package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;

public class GameManager extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private static GameManager instance = new GameManager();
    private Timer timer;
    private Ball ball;
    
    // 물리 상수
    private static final int FPS = 60;
    private final double GRAVITY = 0.5;    // 중력 가속도
    private final int JUMP_POWER = -10;    // 튀어오르는 힘 (위쪽이 -y 방향)

    private GameManager() {
        setTitle("Bounce Ball Game!");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        timer = new Timer(1000 / FPS, this);
        ball = new Ball(600, 100, 5);
    }

    public static GameManager getGameManager() {
        return instance;
    }

    public void render() {
        this.setVisible(true);
        timer.start();
    }

    private void update() {
        // 1. 중력 적용: 속도에 중력을 더함
        ball.setVy(ball.getVy() + GRAVITY);
        
        // 2. 위치 업데이트: 위치에 속도를 더함
        ball.setY((int)(ball.getY() + ball.getVy()));

        // 3. 바닥 충돌 체크 (임시 바닥: 700px)
        int groundY = 700;
        if (ball.getY() + (ball.getR() * 2) > groundY) {
            ball.setY(groundY - (ball.getR() * 2)); // 바닥에 박히지 않게 보정
            ball.setVy(JUMP_POWER); // 위로 튕겨내기
        }
    }

    @Override
    public void paint(Graphics g) {
        // 더블 버퍼링 없이 그릴 경우 화면을 먼저 지워줘야 합니다.
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        // 바닥 선 그리기
        g.setColor(Color.BLACK);
        g.drawLine(0, 700, 1200, 700);

        // 공 그리기
        int x = ball.getX();
        int y = ball.getY();
        int diameter = ball.getR() * 2;

        g.setColor(Color.BLUE);
        g.fillOval(x, y, diameter, diameter); // drawOval 대신 색이 채워진 fillOval 사용
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        repaint();
    }
}