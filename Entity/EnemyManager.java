package Entity;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import Gamestates.Playing;
import Utils.LoadSave;
import static Utils.Constants.EnemyConstants.*;

public class EnemyManager {

	private Playing playing;
	private BufferedImage[][] dogArr;
	private BufferedImage[][] bossArr;
	private ArrayList<Dog> dogs = new ArrayList<>();
	private ArrayList<Boss> boss = new ArrayList<>();

	public EnemyManager(Playing playing) {
		this.playing = playing;
		loadEnemyImgs();
		addEnemies();
	}

	private void addEnemies() {
		dogs = LoadSave.GetDogs();
		boss=LoadSave.GetBoss();
		//System.out.println("size of dogs: " + dogs.size());
	}

	public void update(int[][] lvlData, Player player) {
		for (Dog c : dogs)
			if (c.isActive())
				c.update(lvlData, player);
		for (Boss c : boss)
			if (c.isActive())
				c.update(lvlData, player);
	}

	public void draw(Graphics g, int xLvlOffset) {
		drawDogs(g, xLvlOffset);
		drawBoss(g, xLvlOffset);

	}

	private void drawDogs(Graphics g, int xLvlOffset) {
		for (Dog c : dogs)
			if (c.isActive()) {
				g.drawImage(dogArr[c.getEnemyState()][c.getAniIndex()], (int) c.getHitbox().x - xLvlOffset - DOG_DRAWOFFSET_X + c.flipX(), (int) c.getHitbox().y - DOG_DRAWOFFSET_Y,
						DOG_WIDTH * c.flipW(), DOG_HEIGHT, null);
				//c.drawHitbox(g, xLvlOffset); 
				//c.drawAttackBox(g, xLvlOffset);
			}

	}
	private void drawBoss(Graphics g, int xLvlOffset) {
		for (Boss c : boss)
			if (c.isActive()) {
				g.drawImage(bossArr[c.getEnemyState()][c.getAniIndex()], (int) c.getHitbox().x - xLvlOffset - BOSS_DRAWOFFSET_X + c.flipX(), (int) c.getHitbox().y - BOSS_DRAWOFFSET_Y,
						(int)((BOSS_WIDTH * c.flipW())*1.5), (int)(BOSS_HEIGHT*1.5), null);
				//c.drawHitbox(g, xLvlOffset); 
				c.drawAttackBox(g, xLvlOffset);
			}
	}

	public void checkEnemyHit(Rectangle2D.Float attackBox) {
		boolean dead = false;
		for (Dog c : dogs)
			if (c.isActive()) {
				if (attackBox.intersects(c.getHitbox())) {
					if(c.currentHealth<=0) {
						dead = true;
					}
					c.hurt(10,dead);
					return;
				}
			}
		for (Boss c : boss)
			if (c.isActive())
				if (attackBox.intersects(c.getHitbox())) {
					if(c.currentHealth<=0) {
						dead = true;
					}
					c.hurt(10,dead);
					return;
				}
		}
	



private void loadEnemyImgs() {
	dogArr = new BufferedImage[5][6];
	BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.DOG_SPRITE);

	for (int j = 0; j < dogArr.length; j++)
		for (int i = 0; i < dogArr[j].length; i++) {
			dogArr[j][i] = temp.getSubimage(i * DOG_WIDTH_DEFAULT, j * DOG_HEIGHT_DEFAULT, DOG_WIDTH_DEFAULT, DOG_HEIGHT_DEFAULT);
			System.out.println(i * DOG_WIDTH_DEFAULT+" "+j * DOG_HEIGHT_DEFAULT);
		}
	BufferedImage temp2 = LoadSave.GetSpriteAtlas(LoadSave.BOSS_SPRITE);
	bossArr = new BufferedImage[5][8];
	for (int j = 0; j < bossArr.length; j++)
		for (int i = 0; i < bossArr[j].length; i++) {
			bossArr[j][i] = temp2.getSubimage(i * BOSS_WIDTH_DEFAULT, j * BOSS_HEIGHT_DEFAULT, BOSS_WIDTH_DEFAULT, BOSS_HEIGHT_DEFAULT);

		}
}

public void resetAllEnemies() {
	for (Dog c : dogs)
		c.resetEnemy();
	for (Boss c : boss)
		c.resetEnemy();
}
}