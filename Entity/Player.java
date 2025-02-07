package Entity;

import static Utils.Constants.PlayerConstants.*;
import static Utils.Constants.Directions.*;
import static Utils.HelpMethods.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import Gamestates.Playing;
import main.Game;
import Utils.LoadSave;

public class Player extends Entity {

	private BufferedImage[][] animations;
	private int aniTick, aniIndex, aniSpeed = 20;
	private int playerAction = IDLE;
	private int direction=RIGHT;
	private boolean moving = false, attacking = false ,hit=false,die=false,dead=false;
	private boolean left, up, right, down, jump;
	private float playerSpeed = 0.9f * Game.SCALE;
	private int[][] lvlData;
	private float xDrawOffset = 32 * Game.SCALE;
	private float yDrawOffset = 32 * Game.SCALE;

	// Jumping / Gravity
	private int jumpCooldown = 150;
	private int jumpCooldownCounter = 0;
	private float airSpeed = 0f;
	private float gravity = 0.04f * Game.SCALE;
	private float jumpSpeed = -2.25f * Game.SCALE;
	private float fallSpeedAfterCollision = 0.5f * Game.SCALE;
	private boolean inAir = false;

	//StatusBar

	private BufferedImage statusBarImg;

	private int statusBarWidth = (int) (192 * Game.SCALE);
	private int statusBarHeight = (int) (58 * Game.SCALE);
	private int statusBarX = (int) (10 * Game.SCALE);
	private int statusBarY = (int) (10 * Game.SCALE);

	private int healthBarWidth = (int) (150 * Game.SCALE);
	private int healthBarHeight = (int) (4 * Game.SCALE);
	private int healthBarXStart = (int) (34 * Game.SCALE);
	private int healthBarYStart = (int) (14 * Game.SCALE);

	private int maxHealth = 50;
	private int currentHealth = maxHealth;
	private int healthWidth = healthBarWidth;
	
	//Water dmg
	private int waterCooldown = 60;
	private int waterCooldownCounter = 0;


	//AttackBox

	private Rectangle2D.Float attackBox;

	private int flipX = 0;
	private int flipW = 1;

	private boolean attackChecked;
	private Playing playing;

	public Player(float x, float y, int width, int height, Playing playing) {
		super(x, y, width, height);
		this.playing = playing;
		loadAnimations();
		initHitbox(x, y, (int) (32 * Game.SCALE), (int) (60* Game.SCALE));
		initAttackBox();
	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (40 * Game.SCALE), (int) (30 * Game.SCALE));
	}

	public void update() {
		updateHealthBar();
		if(IsEntityInWater(hitbox,lvlData)) {
			waterCooldownCounter++;
			if(waterCooldownCounter>=waterCooldown) {
				waterCooldownCounter=0;
				changeHealth(1);
			}
		}
		if (currentHealth <= 0) {
			die=true;
		}
		if(dead) {
			playing.setGameOver(true);
			return;
		}

		updateAttackBox();

		updatePos();
		if (attacking)
			checkAttack();
		updateAnimationTick();
		setAnimation();
	}


	private void checkAttack() {
		if (attackChecked || aniIndex != 1)
			return;
		attackChecked = true;
		playing.checkEnemyHit(attackBox);

	}

	private void updateAttackBox() {
		if (right)
			attackBox.x = hitbox.x + 23*Game.SCALE;
		else if (left)
			attackBox.x = hitbox.x - hitbox.width/2 - 15*Game.SCALE;

		attackBox.y = hitbox.y + (Game.SCALE * 15);
	}

	private void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBarWidth);
	}



	public void render(Graphics g, int lvlOffset) {
		g.drawImage(animations[playerAction][aniIndex], (int) (hitbox.x - xDrawOffset) - lvlOffset + flipX, (int) (hitbox.y - yDrawOffset), width * flipW, height, null);
		//drawHitbox(g,0);
		//drawAttackBox(g);
		drawUI(g);
	}


	private void drawAttackBox(Graphics g) {
		g.setColor(Color.red);
		g.drawRect((int) attackBox.x, (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);

	}


	private void drawUI(Graphics g) {
		g.drawImage(statusBarImg, statusBarX, statusBarY, statusBarWidth, statusBarHeight, null);
		g.setColor(Color.red);
		g.fillRect(healthBarXStart + statusBarX, healthBarYStart + statusBarY, healthWidth, healthBarHeight);
	}


	private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(playerAction)) {
				aniIndex = 0;
				attacking = false;
				attackChecked = false;
			}

		}

	}




	private void setAnimation() {
	    int startAni = playerAction;

	    if (moving) {
	        playerAction = RUNNING;
	    } else {
	        playerAction = IDLE;
	    }

	    if (inAir) {
	        if (airSpeed < 0) {
	            playerAction = JUMP;
	        } else {
	            playerAction = FALLING;
	        }
	    }

	    if (attacking) {
	        playerAction = ATTACK;
	    }

	    if (hit && !die) {
	        playerAction = HIT;
	        if (aniIndex == 3) {
	            hit = false;
	            return;
	        }
	    }

	    if (die) {
	        hit = false;
	        playerAction = DEAD;
	        if (aniIndex == 9) {
	            die = false;
	            dead = true;
	        }
	    }

	    if (startAni != playerAction) {
	        resetAniTick();
	    }
	}




	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		moving = false;
		float xSpeed = 0;
		if(!die && !dead) {
			if (jump) {
		        if (!inAir && jumpCooldownCounter <= 0) {
		            jump();
		            jumpCooldownCounter = jumpCooldown;
		        }
		    }

		    if (jumpCooldownCounter > 0) {
		        jumpCooldownCounter--;
		    }
			if (left)
			{	
				xSpeed -= playerSpeed;
				flipX = width;
				flipW = -1;}

			if (right)
			{
				xSpeed += playerSpeed;
				flipX = 0;
				flipW = 1;
			}
		}


		if (!inAir)
			if ((!left && !right) || (right && left))
				return;

		if (!inAir)
			if (!IsEntityOnFloor(hitbox, lvlData))
				inAir = true;

		if (inAir) {
			if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += gravity;
				updateXPos(xSpeed);
			} else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed,2);
				if (airSpeed > 0)
					resetInAir();
				else
					airSpeed = fallSpeedAfterCollision;
				updateXPos(xSpeed);
			}

		} else
			updateXPos(xSpeed);
		moving = true;

	}

	private void jump() {
		if (inAir)
			return;
		inAir = true;
		airSpeed = jumpSpeed;

	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;

	}

	private void updateXPos(float xSpeed) {
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
			hitbox.x += xSpeed;
		} else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
		}

	}


	public void changeHealth(int value) {
		currentHealth -= value;
		if(currentHealth>0)
			hit=true;
		if (currentHealth <= 0) {
			currentHealth = 0;
		}
		else if (currentHealth >= maxHealth)
			currentHealth = maxHealth;
	}


	private void loadAnimations() {

		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);

		animations = new BufferedImage[7][10];
		for (int j = 0; j < animations.length; j++)
			for (int i = 0; i < animations[j].length; i++) 
				animations[j][i] = img.getSubimage(i * 96, j*96, 96, 96);


		statusBarImg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);

	}

	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;

	}

	public void resetDirBooleans() {
		left = false;
		right = false;
		up = false;
		down = false;
	}

	public void setAttacking(boolean attacking) {
		this.attacking = attacking;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isUp() {
		return up;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public boolean isRight() {
		return right;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public boolean isDown() {
		return down;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setJump(boolean jump) {
		this.jump = jump;
	}

	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		attacking = false;
		moving = false;
		hit=false;
		die=false;
		dead=false;
		playerAction = IDLE;
		currentHealth = maxHealth;

		hitbox.x = x;
		hitbox.y = y;

		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
	}
	
	


}