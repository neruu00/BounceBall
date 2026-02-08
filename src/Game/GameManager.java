package Game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GameManager extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    
    private static GameManager instance = new GameManager();
    
    private GamePanel gamePanel;
    private Timer timer;
    private List<Block> blocks;
    private Ball ball;

    
    // 물리 상수
    private static final int FPS = 60; // 게임 프레임 속도
    private final double GRAVITY = 0.5; // 중력 가속도
    private final int JUMP_POWER = -10; // 튀어오르는 힘 (위쪽이 -y 방향)
    private final int MOVE_SPEED = 5; // 좌우 이동 속도
    private int TILE_SIZE;
    

    private GameManager() {
        setTitle("Bounce Ball Game!");
        setSize(1260, 860);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        timer = new Timer(1000 / FPS, this);
        gamePanel = new GamePanel();
        this.add(gamePanel);
        initMap();
        
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                    case KeyEvent.VK_A:
                        ball.setVx(-MOVE_SPEED); // 왼쪽 이동
                        break;
                    case KeyEvent.VK_RIGHT:
                    case KeyEvent.VK_D:
                        ball.setVx(MOVE_SPEED);  // 오른쪽 이동
                        break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                // 키를 뗐을 때 공이 즉시 멈추도록 설정
                if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A ||
                    key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
                    ball.setVx(0);
                }
            }
        });
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
                g.fillRect(b.getX(), b.getY(), TILE_SIZE, TILE_SIZE);
                g.setColor(Color.BLACK);
                g.drawRect(b.getX(), b.getY(), TILE_SIZE, TILE_SIZE);
            }
		}
    }
    
    private void initMap() {
    	Map mapData = new Map(1);
        this.ball = mapData.getInitialBall();
        this.blocks = mapData.getBlocks();
        TILE_SIZE = mapData.getTileSize();
    }
    
    private void checkCollision() {
        // 공의 현재 영역 계산
        Rectangle ballRect = new Rectangle(ball.getX(), ball.getY(), ball.getR() * 2, ball.getR() * 2);

        for (Block b : blocks) {
            if (ballRect.intersects(b.getBounds())) {
                // 공이 떨어지는 중(vy > 0)에 블록 윗면에 닿았을 때만 튕기게 처리
                if (ball.getVy() > 0 && ball.getY() + ball.getR() < b.getY() + TILE_SIZE) {
                    ball.setY(b.getY() - (ball.getR() * 2)); // 위치 보정
                    ball.setVy(JUMP_POWER); // 튕기기
                }
            }
        }
    }
    
    // X축 충돌 (옆면)
    private void checkHorizontalCollision() {
        Rectangle ballRect = new Rectangle(ball.getX(), ball.getY(), ball.getR() * 2, ball.getR() * 2);
        
        for (Block b : blocks) {
            if (ballRect.intersects(b.getBounds())) {
                // 공의 중심 X좌표와 블록의 중심 X좌표 비교
                double ballCenterX = ball.getX() + ball.getR();
                double blockCenterX = b.getX() + (TILE_SIZE / 2.0);

                if (ballCenterX < blockCenterX) { // 블록의 왼쪽에서 충돌
                    ball.setX(b.getX() - (ball.getR() * 2));
                } else { // 블록의 오른쪽에서 충돌
                    ball.setX(b.getX() + TILE_SIZE);
                }
                ball.setVx(0); // 옆면 충돌 시 멈춤
                // 충돌했으므로 ballRect 갱신 (다음 블록과의 중복 체크 방지)
                ballRect.setLocation(ball.getX(), ball.getY());
            }
        }
    }

    // Y축 충돌 (윗면/아랫면)
    private void checkVerticalCollision() {
        Rectangle ballRect = new Rectangle(ball.getX(), ball.getY(), ball.getR() * 2, ball.getR() * 2);
        
        for (Block b : blocks) {
            if (ballRect.intersects(b.getBounds())) {
                double ballCenterY = ball.getY() + ball.getR();
                double blockCenterY = b.getY() + (TILE_SIZE / 2.0);

                if (ballCenterY < blockCenterY) { // 블록의 위쪽에서 충돌 (밟기)
                    ball.setY(b.getY() - (ball.getR() * 2));
                    ball.setVy(JUMP_POWER); // 튕겨 올라감
                } else { // 블록의 아래쪽에서 충돌 (머리 박기)
                    ball.setY(b.getY() + TILE_SIZE);
                    ball.setVy(0.5); // 툭 떨어짐
                }
                ballRect.setLocation(ball.getX(), ball.getY());
            }
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
        // 1. 수평(X) 이동 및 충돌 체크
        ball.setX((int)(ball.getX() + ball.getVx()));
        checkHorizontalCollision();

        // 2. 수직(Y) 이동 (중력 먼저 적용 후 이동)
        ball.setVy(ball.getVy() + GRAVITY);
        ball.setY((int)(ball.getY() + ball.getVy()));
        checkVerticalCollision();
        
        // 3. 화면 바닥 낙사 방지 (임시)
        if (ball.getY() > 900) {
            initMap(); // 떨어지면 리스폰
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        update();
        gamePanel.repaint();
    }
}