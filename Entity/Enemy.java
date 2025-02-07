package Entity;



import java.awt.geom.Rectangle2D;

import main.Game;
import static Utils.HelpMethods.*;
import static Utils.Constants.Directions.*;

import static Utils.Constants.EnemyConstants.*;


public abstract class Enemy extends Entity {
	protected int aniIndex, enemyState, enemyType;
	protected int aniTick, aniSpeed = 25;
	protected boolean firstUpdate = true;
	protected boolean inAir;
	protected float fallSpeed;
	protected float gravity = 0.04f * Game.SCALE;
	protected float walkSpeed = 0.55f * Game.SCALE;
	protected int walkDir = Utils.Constants.Directions.LEFT;
	protected int tileY;
	protected float attackDistance = Game.TILES_SIZE;
	protected int maxHealth;
	protected int currentHealth;
	protected boolean active = true;
	protected boolean attackChecked;

	public Enemy(float x, float y, int width, int height, int enemyType) {
		super(x, y, width, height);
		this.enemyType = enemyType;
		initHitbox(x, y, width, height);
		maxHealth = GetMaxHealth(enemyType);
		currentHealth = maxHealth;

	}

	protected void firstUpdateCheck(int[][] lvlData) {
		if (!IsEntityOnFloor(hitbox, lvlData))
			inAir = true;
		firstUpdate = false;
	}

	protected void updateInAir(int[][] lvlData) {
		if (CanMoveHere(hitbox.x, hitbox.y + fallSpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += fallSpeed;
			fallSpeed += gravity;
		} else {
			inAir = false;
			if(enemyType==DOG) {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed,1);
				tileY = (int) (hitbox.y / Game.TILES_SIZE);}
			if(enemyType==BOSS) {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, fallSpeed,3);
				tileY = (int) (hitbox.y / Game.TILES_SIZE)+2;}
		}
	}


	protected void move(int[][] lvlData) {
		float xSpeed = 0;
		if(enemyType==BOSS)
			walkSpeed=0.40f * Game.SCALE;
		else
			walkSpeed=0.55f * Game.SCALE;
		if (walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData) && enemyState!=HIT)
			if (IsFloor(hitbox, xSpeed, lvlData)) {
				hitbox.x += xSpeed;
				return;
			}

		changeWalkDir();
	}
	


	protected void turnTowardsPlayer(Player player) {
		if (player.hitbox.x > hitbox.x)
			walkDir = RIGHT;
		else
			walkDir = LEFT;
	}


	protected boolean canSeePlayer(int[][] lvlData, Player player,int enemyType) {
		int playerTileY = (int) (player.getHitbox().y / Game.TILES_SIZE)+1;
		if (playerTileY == tileY)
			if (isPlayerInRange(player,enemyType)) {
				if (IsSightClear(lvlData, hitbox, player.hitbox, tileY))
					return true;
			}

		return false;
	}


	protected boolean isPlayerInRange(Player player,int enemyType) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		if(enemyType==BOSS)
			return absValue <= attackDistance * 15;
		if(enemyType==DOG)
			return absValue <= attackDistance * 10;
		return false;
		
	}


	protected boolean isPlayerCloseForAttack(Player player,int enemyType) {
		int absValue = (int) Math.abs(player.hitbox.x - hitbox.x);
		if(enemyType==BOSS)
			return absValue <= attackDistance*1.5;
		if(enemyType==DOG)
			return absValue <= attackDistance;
		return false;
	}


	protected void newState(int enemyState) {
		this.enemyState = enemyState;
		aniTick = 0;
		aniIndex = 0;
	}

	public void hurt(int amount,boolean dead) {
		currentHealth -= amount;
		if (currentHealth <= 0)
			newState(DEAD);
		else
			if(enemyState!=ATTACK)
				newState(HIT);
	}

	// Changed the name from "checkEnemyHit" to checkPlayerHit
	protected void checkPlayerHit(Rectangle2D.Float attackBox, Player player) {
		if (attackBox.intersects(player.hitbox))
			player.changeHealth(GetEnemyDmg(enemyType));
		attackChecked = true;

	}




	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= aniSpeed) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(enemyType, enemyState)) {
				aniIndex = 0;

				switch (enemyState) {
				//case ATTACK, HIT -> enemyState = Utils.Constants.EnemyConstants.IDLE;
				//case Utils.Constants.EnemyConstants.DEAD -> active = false;

				case ATTACK:
				case HIT:
					enemyState = IDLE;
					break;
				case DEAD:
					active = false;
					break;
				}
			}
		}
	}



	protected void changeWalkDir() {
		if (walkDir == LEFT)
			walkDir = RIGHT;
		else
			walkDir = LEFT;

	}

	public void resetEnemy() {
		hitbox.x = x;
		hitbox.y = y;
		firstUpdate = true;
		currentHealth = maxHealth;
		newState(IDLE);
		active = true;
		fallSpeed = 0;
	}


	public int getAniIndex() {
		return aniIndex;
	}

	public int getEnemyState() {
		return enemyState;
	}

	public boolean isActive() {
		return active;
	}

}