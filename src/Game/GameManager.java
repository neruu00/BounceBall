package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameManager extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    
    private static GameManager instance = new GameManager();
    
    private GamePanel gamePanel;
    private Timer timer;
    private Ball ball;
    private List<Block> blocks;
    
    // 물리 상수
    private static final int FPS = 60; // 게임 프레임 속도
    private final double GRAVITY = 0.5; // 중력 가속도
    private final int JUMP_POWER = -10; // 튀어오르는 힘 (위쪽이 -y 방향)
    private final int BLOCK_SIZE; // 블럭의 사이즈

    private GameManager() {
        setTitle("Bounce Ball Game!");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        
        gamePanel = new GamePanel();
        this.add(gamePanel);
        
        timer = new Timer(1000 / FPS, this);
        ball = new Ball(600, 100, 8);
        
        BLOCK_SIZE = Block.getSIZE();
        blocks = new ArrayList<Block>();
        for(int i = 0; i < 29; i++) {
            blocks.add(new Block(10+i*BLOCK_SIZE, 710));
        }
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
			for (Block b : blocks) {
                g.setColor(Color.DARK_GRAY);
                g.fillRect(b.getX(), b.getY(), BLOCK_SIZE, BLOCK_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(b.getX(), b.getY(), BLOCK_SIZE, BLOCK_SIZE);
            }
		}
    }
    
    private void checkCollision() {
        // 공의 현재 영역 계산
        Rectangle ballRect = new Rectangle(ball.getX(), ball.getY(), ball.getR() * 2, ball.getR() * 2);

        for (Block b : blocks) {
            if (ballRect.intersects(b.getBounds())) {
                // 공이 떨어지는 중(vy > 0)에 블록 윗면에 닿았을 때만 튕기게 처리
                if (ball.getVy() > 0 && ball.getY() + ball.getR() < b.getY() + BLOCK_SIZE) {
                    ball.setY(b.getY() - (ball.getR() * 2)); // 위치 보정
                    ball.setVy(JUMP_POWER); // 튕기기
                }
            }
        }
        
        // 화면 밖으로 나가는 것 방지 (바닥)
        if (ball.getY() + (ball.getR() * 2) > 750) {
            ball.setY(750 - (ball.getR() * 2));
            ball.setVy(JUMP_POWER);
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
        // 3. 블럭과의 충돌 체크
        checkCollision();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        gamePanel.repaint();
    }
}