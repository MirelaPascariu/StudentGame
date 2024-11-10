package Utils;

import main.Game;

public class Constants {
	
	public static class EnemyConstants {
		//enemy
		public static final int DOG= 0;
		public static final int BOSS= 1;

		public static final int IDLE = 1;
		public static final int RUNNING = 0;
		public static final int ATTACK = 2;
		public static final int HIT = 3;
		public static final int DEAD = 4;
		
		//dog
		public static final int DOG_WIDTH_DEFAULT = 70;
		public static final int DOG_HEIGHT_DEFAULT = 40;
		//Boss
		public static final int BOSS_WIDTH_DEFAULT = 130;
		public static final int BOSS_HEIGHT_DEFAULT = 130;
		
		//dog
		public static final int DOG_WIDTH = (int) (DOG_WIDTH_DEFAULT * Game.SCALE);
		public static final int DOG_HEIGHT = (int) (DOG_HEIGHT_DEFAULT * Game.SCALE);
		//Boss
		public static final int BOSS_WIDTH = (int) (BOSS_WIDTH_DEFAULT * Game.SCALE);
		public static final int BOSS_HEIGHT = (int) (BOSS_HEIGHT_DEFAULT * Game.SCALE);
		
		//DOG
		public static final int DOG_DRAWOFFSET_X = (int) (15 * Game.SCALE);
		public static final int DOG_DRAWOFFSET_Y = (int) (9 * Game.SCALE);
		//BOSS
		public static final int BOSS_DRAWOFFSET_X = (int) (-112 * Game.SCALE);
		public static final int BOSS_DRAWOFFSET_Y = (int) (96 * Game.SCALE);

		public static int GetSpriteAmount(int enemy_type, int enemy_state) {

			switch (enemy_type) {
			case DOG:
				switch (enemy_state) {
				case IDLE:
					return 4;
				case RUNNING:
					return 6;
				case ATTACK:
					return 4;
				case HIT:
					return 2;
				case DEAD:
					return 4;
				}
			case BOSS:
				switch (enemy_state) {
				case IDLE:
					return 6;
				case RUNNING:
					return 9;
				case ATTACK:
					return 8;
				case HIT:
					return 4;
				case DEAD:
					return 7;
				}
			}

			return 0;

		}

		public static int GetMaxHealth(int enemy_type) {
			switch (enemy_type) {
			case DOG:
				return 30;
			case BOSS:
				return 100;
			default:
				return 1;
			}
		}

		public static int GetEnemyDmg(int enemy_type) {
			switch (enemy_type) {
			case DOG:
				return 10;
			case BOSS:
				return 30;
			default:
				return 0;
			}

		}

	}
	public static class UI {
		public static class Buttons {
			public static final int B_WIDTH_DEFAULT = 140;
			public static final int B_HEIGHT_DEFAULT = 56;
			public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * Game.SCALE);
			public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * Game.SCALE);
		}

		public static class PauseButtons {
			public static final int SOUND_SIZE_DEFAULT = 42;
			public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * Game.SCALE);
		}

		public static class URMButtons {
			public static final int URM_DEFAULT_SIZE = 56;
			public static final int URM_SIZE = (int) (URM_DEFAULT_SIZE * Game.SCALE);

		}

		public static class VolumeButtons {
			public static final int VOLUME_DEFAULT_WIDTH = 28;
			public static final int VOLUME_DEFAULT_HEIGHT = 44;
			public static final int SLIDER_DEFAULT_WIDTH = 215;

			public static final int VOLUME_WIDTH = (int) (VOLUME_DEFAULT_WIDTH * Game.SCALE);
			public static final int VOLUME_HEIGHT = (int) (VOLUME_DEFAULT_HEIGHT * Game.SCALE);
			public static final int SLIDER_WIDTH = (int) (SLIDER_DEFAULT_WIDTH * Game.SCALE);
		}
	}

	public static class Directions {
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}

	public static class PlayerConstants {
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int JUMP = 2;
		public static final int ATTACK = 4;
		public static final int HIT = 5;
		public static final int DEAD = 6;
		public static final int FALLING = 3;

		public static int GetSpriteAmount(int player_action) {
			switch (player_action) {
			
			case DEAD:
				return 10;
			case RUNNING:
				return 8;
			case IDLE:
				return 10;
			case HIT:
				return 4;
			case JUMP:
				return 1;
			case ATTACK:
				return 5;
			case FALLING:
				return 1;
			default:
				return 1;
			}
		}
	}

}
