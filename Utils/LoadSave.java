package Utils;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import java.util.ArrayList;

import Entity.Boss;
import Entity.Dog;
import static Utils.Constants.EnemyConstants.DOG;
import static Utils.Constants.EnemyConstants.BOSS;
import main.Game;

public class LoadSave {
	public static final String BACKGROUND="resources/rsz_1cladiri-fara-fundal.png";
	public static final String PLAYER_ATLAS = "resources/Assets_F.png";
	public static final String LEVEL_ATLAS = "resources/outside_sprites.png";
	public static final String LEVEL_ONE_DATA = "resources/FINAL.png";
	public static final String MENU_BUTTONS = "resources/button_atlas.png";
	public static final String MENU_BACKGROUND = "resources/menu_background.png";
	public static final String PAUSE_BACKGROUND = "resources/pause_menu.png";
	public static final String SOUND_BUTTONS = "resources/sound_button.png";
	public static final String URM_BUTTONS = "resources/urm_buttons.png";
	public static final String VOLUME_BUTTONS = "resources/volume_buttons.png";
	public static final String DOG_SPRITE = "resources/caine2.png";
	public static final String BOSS_SPRITE = "resources/BOSS_F.png";
	public static final String PLAYING_BIG_IMG = "resources/nor1.png";
	public static final String STATUS_BAR = "resources/health_power_bar.png";
	public static final String MENU_BACKGROUND_IMG = "resources/new_menu_background.png";
	public static final String WATER_TOP = "resources/water_atlas_animation.png";
	public static final String WATER_BOTTOM = "resources/water.png";
	public static final String DEAD = "resources/death_screen.png";
	

	
	public static BufferedImage GetSpriteAtlas(String fileName) {
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/" + fileName);
		try {
			img = ImageIO.read(is);

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}
	
	public static ArrayList<Dog> GetDogs() {
		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
		ArrayList<Dog> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == DOG)
					list.add(new Dog(i * Game.TILES_SIZE, j * Game.TILES_SIZE));
			}
		return list;

	}
	public static ArrayList<Boss> GetBoss() {
		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
		ArrayList<Boss> list = new ArrayList<>();
		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getGreen();
				if (value == BOSS)
					list.add(new Boss((i-3) * Game.TILES_SIZE, (j-3) * Game.TILES_SIZE));
			}
		return list;

	}

	public static int[][] GetLevelData() {
		BufferedImage img = GetSpriteAtlas(LEVEL_ONE_DATA);
		int[][] lvlData = new int[img.getHeight()][img.getWidth()];
		

		for (int j = 0; j < img.getHeight(); j++)
			for (int i = 0; i < img.getWidth(); i++) {
				Color color = new Color(img.getRGB(i, j));
				int value = color.getRed();
				if (value >= 49)
					value = 0;
				lvlData[j][i] = value;
			}
		return lvlData;

	}
}
