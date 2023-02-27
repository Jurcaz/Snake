package juego;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener {

	private static final int SCREEN_WIDTH = 600;
	private static final int SCREEN_HEIGHT = 600;
	
	private static final int UNIT_SIZE = 25;
	
	private static final int GAME_UNITS = (SCREEN_HEIGHT*SCREEN_WIDTH)/UNIT_SIZE;
	
	private static final int DELAY = 75;
	
	private final int x[] = new int[GAME_UNITS];
	private final int y[] = new int[GAME_UNITS];
	
	private int bodyParts = 6;
	private int applesEaten;
	
	private int appleX;
	private int appleY;
	
	private Direction direction = Direction.RIGHT;
	
	private boolean running = false;
	private Timer timer;
	private Random random;
	
	public GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}
	
	public void startGame() {
		setApple();
		this.running = true;
		timer = new Timer(DELAY, this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		if(running) {
			for(int i = 0; i<SCREEN_HEIGHT/UNIT_SIZE; i++) {
				g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
			}
			
			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
			
			for(int i = 0; i<bodyParts; i++) {
				if(i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);
				} else {
					g.setColor(new Color(45,180,0));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}
		} else {
			gameOver(g);
		}
		
		
	}
	
	public void move() {
		for(int i = bodyParts; i>0; i--) {
			x[i] = x[i-1];
			y[i] = y[i-1];
		}
		
		switch (direction) {
			case UP: {
				y[0] = y[0] - UNIT_SIZE;
				break;
			}
			case DOWN: {
				y[0] = y[0] + UNIT_SIZE;
				break;
			}
			case LEFT: {
				x[0] = x[0] - UNIT_SIZE;
				break;
			}
			case RIGHT: {
				x[0] = x[0] + UNIT_SIZE;
				break;
			}
		}
		
	}
	
	public void setApple() {
		appleX = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
		appleY = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
	}
	
	public void checkApple() {
		if(	(x[0] == appleX)	&&	(y[0] == appleY) ) {
			setApple();
			this.bodyParts ++;
			this.applesEaten ++;
		}
	}
	
	public void checkCollisions() {
		//comprueba si choca con su cuerpo
		
		for(int i = bodyParts; i>0; i--) {
			if((x[0] == x[i]) && (y[0] == y[i]) ) {
				running = false;
			}
		}
		
		//comprueba si choca con algun borde
		if(x[0] < 0 || x[0] >= SCREEN_WIDTH) {
			this.running = false;
		}
		if(y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
			this.running = false;
		}
		
		if(!running) timer.stop();
	}
	
	public void gameOver(Graphics g) {
		g.setColor(Color.red);
		g.setFont(new Font("Colibri",Font.BOLD,40));
		FontMetrics metrics = getFontMetrics(g.getFont());
		g.drawString("Game over", (SCREEN_WIDTH - metrics.stringWidth("Game over"))/2, 200);
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score:  "))/2, 300);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if (running) {
			goToApple();
			move();
			checkApple();
			checkCollisions();
			
			System.out.println(x[0]+"-"+y[0]);
			System.out.println(appleX+"-"+appleY);
		}
		
		repaint();
	}
	
	public void goToApple() {
		int headX = x[0];
		int x = Math.abs(headX - appleX);
		
		int headY = y[0];
		int y = Math.abs(headY - appleY);
		
		if(x==0 || y==0) {
			if(x == 0) {
				if(headY > appleY) {
					if(direction != Direction.DOWN) {
						direction = Direction.UP;
					} else {
						if(headX > appleX) {
							if(direction != Direction.RIGHT) direction = Direction.LEFT;
						} else {
							if(direction != Direction.LEFT) direction = Direction.RIGHT;
						}
					}
				} else {
					if(direction != Direction.UP) {
						direction = Direction.DOWN;
					} else {
						if(headX > appleX) {
							if(direction != Direction.RIGHT) direction = Direction.LEFT;
						} else {
							if(direction != Direction.LEFT) direction = Direction.RIGHT;
						}
					}
				}
			}
			
			if(y == 0) {
				if(headX > appleX) {
					if(direction != Direction.RIGHT) {
						direction = Direction.LEFT;
					} else {
						if(headY > appleY) {
							if(direction != Direction.DOWN) direction = Direction.UP;
						} else {
							if(direction != Direction.UP) direction = Direction.DOWN;
						}
					}
				} else {
					if(direction != Direction.LEFT) {
						direction = Direction.RIGHT;
					} else {
						if(headY > appleY) {
							if(direction != Direction.DOWN) direction = Direction.UP;
						} else {
							if(direction != Direction.UP) direction = Direction.DOWN;
						}
					}
				}
			}
		
		} else {
			//movemos en el eje Y
			if(x>y) {
				if(headY > appleY) {
					//up
					if(this.direction != Direction.DOWN) {
						direction = Direction.UP;
					} else {
						if(headX > appleX) {
							if(direction != Direction.RIGHT) direction = Direction.LEFT;
						} else {
							if(direction != Direction.LEFT) direction = Direction.RIGHT;
						}
					}
				} else {
					//down
					if(this.direction != Direction.UP) {
						direction = Direction.DOWN;
					} else {
						if(headX > appleX) {
							if(direction != Direction.RIGHT) direction = Direction.LEFT;
						} else {
							if(direction != Direction.LEFT) direction = Direction.RIGHT;
						}
					}
				}
			//movemos en el eje X
			} else {
				if(headX > appleX) {
					//right
					if(this.direction != Direction.RIGHT) {
						direction = Direction.LEFT;
					} else {
						if(headY > appleY) {
							if(direction != Direction.DOWN) direction = Direction.UP;
						} else {
							if(direction != Direction.UP) direction = Direction.DOWN;
						}
					}
				} else {
					//left
					if(this.direction != Direction.LEFT) {
						direction = Direction.RIGHT;
					} else {
						if(headY > appleY) {
							if(direction != Direction.DOWN) direction = Direction.UP;
						} else {
							if(direction != Direction.UP) direction = Direction.DOWN;
						}
					}
				}
			}	
		}
		
	}
	
	public class MyKeyAdapter extends KeyAdapter {
		
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != Direction.RIGHT)	direction = Direction.LEFT;
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != Direction.LEFT)		direction = Direction.RIGHT;
				break;
			case KeyEvent.VK_UP:
				if(direction != Direction.DOWN)		direction = Direction.UP;
				break;
			case KeyEvent.VK_DOWN:
				if(direction != Direction.UP)		direction = Direction.DOWN;
				break;
			}
		}
	}

	public enum Direction {
		UP,DOWN,RIGHT,LEFT
	}
}
