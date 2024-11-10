/*package tiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

import java.io.InputStream;

import main.GamePanel;

public class TileManager {
	GamePanel gp;
	public tiles[] tile;
	public int mapTileNum[][];
	public BufferedImage background;
	public TileManager(GamePanel gp) {
		this.gp=gp;
		tile =new tiles[10];
		mapTileNum=new int[gp.maxWorldCol][gp.maxWorldRow];
		getTileImage();
		loadMap();
	}
	public void getTileImage() {
		try {
			background=ImageIO.read(getClass().getResource("/main/back.png"));
			tile[0]=new tiles();
			tile[0].image=ImageIO.read(getClass().getResourceAsStream("/tiles/earth.png"));
			tile[1]=new tiles();
			tile[1].image=ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
			tile[1].collision=true;
			tile[2]=new tiles();
			tile[2].image=ImageIO.read(getClass().getResourceAsStream("/tiles/water.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public void loadMap() {
		try {
			InputStream is=getClass().getResourceAsStream("/map/map.txt");
			BufferedReader br= new BufferedReader(new InputStreamReader(is));
			int col=0;
			int row=0;
			while(col<gp.maxWorldCol && row<gp.maxWorldRow) {
				String line1 =br.readLine();
				String line = line1.replace("\t"," ");
				while(col<gp.maxWorldCol) {
					String numbers[]=line.split(" ");
					int num = Integer.parseInt(numbers[col]);
					mapTileNum[col][row]=num;
					col++;
				}
				if(col== gp.maxWorldCol) {
					col=0;
					row++;
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void draw(Graphics2D g2) {
		int Worldcol=0;
		int Worldrow=0;
		g2.drawImage(background,0-gp.player.x/2+gp.screenX-4*gp.tileSize,0,gp.WorldWidth/2,gp.WorldHeight,null);
		while(Worldcol <gp.maxWorldCol && Worldrow <gp.maxWorldRow) {
			int tileNum = mapTileNum[Worldcol][Worldrow];
			int worldX=Worldcol*gp.tileSize;
			int worldY=Worldrow*gp.tileSize;
			int screenX=worldX-gp.player.x+gp.screenX;
			int screenY=worldY;
			g2.drawImage(tile[tileNum].image,screenX,screenY,gp.tileSize,gp.tileSize,null);
			Worldcol++;
			if(Worldcol==gp.maxWorldCol) {
				Worldcol=0;
				Worldrow++;
			}
		}
		
	}
}
*/