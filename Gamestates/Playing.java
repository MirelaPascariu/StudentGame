package Gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import Entity.EnemyManager;

import Entity.Player;
import Levels.LevelManager;
import main.Game;
import UI.GameOverOverlay;
import UI.PauseButton;
import UI.PauseOverlay;
import Utils.LoadSave;
import Entity.Dog;
import UI.GameOverOverlay;

//import static Utils.Constants.Environment.*;

public class Playing extends State implements StateMethods {
	
	public static Player player;
	private LevelManager levelManager;
	private boolean paused = false;
	private PauseOverlay pauseOverlay;
	private GameOverOverlay gameOverOverlay;
	
	private EnemyManager enemyManager;
	
	private int xLvlOffset;
	private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
	private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
	private int lvlTilesWide = LoadSave.GetLevelData()[0].length;
	private int maxTilesOffset = lvlTilesWide - Game.TILES_IN_WIDTH;
	private int maxLvlOffsetX = maxTilesOffset * Game.TILES_SIZE;
	
	private BufferedImage backgroundImg,backgroundImg2;

	private Random rnd = new Random();
	private boolean gameOver;
	
	public Playing(Game game) {
		super(game);
		initClasses();

		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BIG_IMG);
		
		backgroundImg2 = LoadSave.GetSpriteAtlas(LoadSave.BACKGROUND);
	}

	private void initClasses() {
		levelManager = new LevelManager(game);
		enemyManager = new EnemyManager(this);
		player = new Player(200, 300, (int) (96* Game.SCALE), (int) (96 * Game.SCALE), this);
		player.loadLvlData(levelManager.getCurrentLevel().getLevelData());
		pauseOverlay = new PauseOverlay(this);
		gameOverOverlay = new GameOverOverlay(this);

	}

	@Override
	public void update() {
		
		if (paused) {
			pauseOverlay.update();
		
		} else if (gameOver) {
			gameOverOverlay.update();
		} else {
			levelManager.update();

			player.update();
			enemyManager.update(levelManager.getCurrentLevel().getLevelData(), player);
			checkCloseToBorder();
		}

	}

	private void checkCloseToBorder() {
		int playerX = (int) player.getHitbox().x;
		int diff = playerX - xLvlOffset;

		if (diff > rightBorder)
			xLvlOffset += diff - rightBorder;
		else if (diff < leftBorder)
			xLvlOffset += diff - leftBorder;

		if (xLvlOffset > maxLvlOffsetX)
			xLvlOffset = maxLvlOffsetX;
		else if (xLvlOffset < 0)
			xLvlOffset = 0;

	}
	@Override
	public void draw(Graphics g) {
		
		for(int i=0; i<30; i++) {
			g.drawImage(backgroundImg, 0+i*(int) (Game.GAME_WIDTH/1.5) - (int)(xLvlOffset * 0.3), 0, (int) (Game.GAME_WIDTH/1.5), (int) ( Game.GAME_HEIGHT/1.1), null);
		}
		
		g.drawImage(backgroundImg2, 0 - (int)(xLvlOffset * 0.5), 0, (int) (Game.GAME_WIDTH*10), (int) ( Game.GAME_HEIGHT/1.05), null);
		
		levelManager.draw(g, xLvlOffset);
		player.render(g,xLvlOffset);
		enemyManager.draw(g,xLvlOffset);
		if (paused) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
			pauseOverlay.draw(g);
		}
		else if (gameOver)
			gameOverOverlay.draw(g);

	}

	public void resetAll() {
		gameOver = false;
		paused = false;
		player.resetAll();
		enemyManager.resetAllEnemies();
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		enemyManager.checkEnemyHit(attackBox);
	}



	@Override
	public void mouseClicked(MouseEvent e) {
		if (!gameOver)
			if (e.getButton() == MouseEvent.BUTTON1)
				player.setAttacking(true);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (gameOver)
			gameOverOverlay.keyPressed(e);
		else
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(true);
				break;
			case KeyEvent.VK_D:
				player.setRight(true);
				break;
			case KeyEvent.VK_W:
				player.setJump(true);
				break;
			case KeyEvent.VK_ESCAPE:
				paused = !paused;
				break;
			}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (!gameOver)
			switch (e.getKeyCode()) {
			case KeyEvent.VK_A:
				player.setLeft(false);
				break;
			case KeyEvent.VK_D:
				player.setRight(false);
				break;
			case KeyEvent.VK_W:
				player.setJump(false);
				break;
			}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!gameOver)
			{if (paused)
				pauseOverlay.mousePressed(e);
			}
		else
			gameOverOverlay.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!gameOver)
			{if (paused)
			pauseOverlay.mouseReleased(e);
			}
		else
		gameOverOverlay.mouseReleased(e);

	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!gameOver)
			{if (paused)
			pauseOverlay.mousePressed(e);
			}
		else
			gameOverOverlay.mousePressed(e);

	}

	public void mouseDragged(MouseEvent e) {
		if (!gameOver)
			if (paused)
				pauseOverlay.mouseDragged(e);
	}

	public void windowFocusLost() {
		player.resetDirBooleans();
	}

	public Player getPlayer() {
		return player;
	}
	public void unpauseGame() {
		paused = false;
	}

}
