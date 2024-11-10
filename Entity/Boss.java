package Entity;

import static Utils.Constants.EnemyConstants.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.Game;
import static Utils.Constants.Directions.*;
import Utils.HelpMethods.*;
import Entity.Enemy;
public class Boss extends Enemy {
	
	private static final int ATTACK_COOLDOWN = 240;
    private int attackCooldown;

	
	public int attackBoxOffsetX;
	public Rectangle2D.Float attackBox;

	public Boss(float x, float y) {
		super(x, y, BOSS_WIDTH, BOSS_HEIGHT, BOSS);
		initHitbox(x, y, (int) (48 * Game.SCALE), (int) (90 * Game.SCALE));
		attackCooldown = 0;

	}

	private void initAttackBox() {
		attackBox = new Rectangle2D.Float(x, y, (int) (95 * Game.SCALE), (int) (70 * Game.SCALE));
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
			attackBox.x = hitbox.x - attackBoxOffsetX +20*Game.SCALE;
		else
			attackBox.x = hitbox.x - attackBoxOffsetX -10*Game.SCALE;
		attackBox.y = hitbox.y+10*Game.SCALE;

	}

	private void updateBehavior(int[][] lvlData, Player player) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			updateInAir(lvlData);
		else {
			switch (enemyState) {
			case IDLE:
				if(canSeePlayer(lvlData, player,BOSS) && !isPlayerCloseForAttack(player,BOSS))
					newState(RUNNING);
				else if (isPlayerCloseForAttack(player,BOSS) && attackCooldown <= 0)
					newState(ATTACK);
				else
					attackCooldown--;
				break;
			case RUNNING:
				attackCooldown--;
				if (canSeePlayer(lvlData, player,BOSS)) {
					turnTowardsPlayer(player);
					if (isPlayerCloseForAttack(player,BOSS) && attackCooldown <= 0)
						newState(ATTACK);
				}
				else
					newState(IDLE);
				move(lvlData);
				break;
			case ATTACK:
				if (aniIndex == 0)
					attackChecked = false;

				if (aniIndex == 3 && !attackChecked) {
	                checkPlayerHit(attackBox, player);
	                attackCooldown = ATTACK_COOLDOWN;
	            }

	            break;
			case HIT:
				attackCooldown--;
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
			return (int) -(width+48*Game.SCALE);
		else
			return 0;
	}

	public int flipW() {
		if (walkDir == RIGHT)
			return 1;
		else
			return -1;

	}

}


