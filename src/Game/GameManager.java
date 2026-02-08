package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameManager extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    
    private static GameManager instance = new GameManager();
    
    private Timer timer;
    private Ball ball;
    private GamePanel gamePanel;
    
    // 물리 상수
    private static final int FPS = 60; // 게임 프레임 속도
    private final double GRAVITY = 0.5; // 중력 가속도
    private final int JUMP_POWER = -10; // 튀어오르는 힘 (위쪽이 -y 방향)

    private GameManager() {
        setTitle("Bounce Ball Game!");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        gamePanel = new GamePanel();
        this.add(gamePanel);
        
        timer = new Timer(1000 / FPS, this);
        ball = new Ball(600, 100, 5);
    }
    
    // 그리기 전용 GameManager의 내부 클래스
    private class GamePanel extends JPanel {
		private static final long serialVersionUID = 1L;

		@Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g); // 이전 화면 클리어 - 깜빡임 방지
            
            drawBall(g); // 공 그리기
            drawBlocks(g); // 블럭 그리기
        }
		
		private void drawBall(Graphics g) {
			g.setColor(Color.BLUE);
            g.fillOval(ball.getX(), ball.getY(), ball.getR() * 2, ball.getR() * 2);
		}
		
		private void drawBlocks(Graphics g) {
            /**
             * TODO - 사물 그리기 구현
             * 현재는 바닥을 그리는 선만 하나 있어서
             * 나중이 선 그리기는 지워야 함
             */
            g.setColor(Color.BLACK);
            g.drawLine(0, 700, 1200, 700);
		}
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
    public void actionPerformed(ActionEvent e) {
        update();
        gamePanel.repaint();
    }
}