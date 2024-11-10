package Entity;

import static Utils.Constants.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;
import static Utils.Constants.Directions.*;
import Utils.HelpMethods.*;
import Entity.Enemy;
public class Dog extends Enemy {
	
	private static final int ATTACK_COOLDOWN = 40;
    private int attackCooldown;

	
	public int attackBoxOffsetX;
	public Rectangle2D.Float attackBox;

	public Dog(float x, float y) {
		super(x, y, DOG_WIDTH, DOG_HEIGHT, DOG);
		initHitbox(x, y, (int) (40 * Game.SCALE), (int) (30 * Game.SCALE));
		attackCooldown = 20;

	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (35 * Game.SCALE), (int) (20 * Game.SCALE));
		attackBoxOffsetX = (int) (Game.SCALE * 30);
	}

	public void update(int[][] lvlData, Player player) {
		updateBehavior(lvlData, player);
		updateAnimationTick();
		updateAttackBox();

	}

	private void updateAttackBox() {
		initAttackBox();
		if(walkDir == RIGHT)
			attackBox.x = hitbox.x - attackBoxOffsetX +45*Game.SCALE;
		else
			attackBox.x = hitbox.x - attackBoxOffsetX +20*Game.SCALE;
		attackBox.y = hitbox.y-5*Game.SCALE;

	}

	private void updateBehavior(int[][] lvlData, Player player) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			updateInAir(lvlData);
		else {
			switch (enemyState) {
			case IDLE:
				newState(RUNNING);
				break;
			case RUNNING:
				if (canSeePlayer(lvlData, player,DOG)) {
					turnTowardsPlayer(player);
					if (isPlayerCloseForAttack(player,DOG))
						newState(ATTACK);
				}
				move(lvlData);
				break;
			case ATTACK:
				if (aniIndex == 0)
					attackChecked = false;

				if (aniIndex == 3 && !attackChecked && attackCooldown <= 0) {
	                checkPlayerHit(attackBox, player);
	                attackCooldown = ATTACK_COOLDOWN;
	            } else {
	                attackCooldown--;
	                
	            }

	            break;
			case HIT:
				break;
			}
		}

	}

	public void drawAttackBox(Graphics g, int xLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int) (attackBox.x - xLvlOffset), (int) attackBox.y, (int) attackBox.width, (int) attackBox.height);
	}

	public int flipX() {
		if (walkDir == RIGHT)
			return (int) (width);
		else
			return 0;
	}

	public int flipW() {
		if (walkDir == RIGHT)
			return -1;
		else
			return 1;

	}

}


