import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

public class Cell {
	public static final Color LIGHTGRAY = new Color(210,210,210);
	public static final Color GRAY = new Color(200,200,200);
	public static final Color DARKGRAY = new Color(130,130,130);
	public static final Color LIGHTBLUE = new Color(150,150,255);
	public static final Color LIGHTGREEN = new Color(150,255,150, 100);
	public static final Color RED = new Color(255,100,100);
	
	public int x, y, degree;
	public boolean mine;
	public boolean highlighted = false;
	public boolean processed = false;
	public boolean marked = false;
	
	public GradientPaint gradient, lightGradient, lightestGradient, hoverGradient;
	
	public double height, width;

	private int posX, posY, displayWidth, displayHeight;
	
	//List of neighbors
	Cell[] neighbors = new Cell[8];
	
	public Cell(int _x, int _y){
		x = _x;
		y = _y;
		degree = 0;
		mine = false;

		height = Main.cellHeight;
		width = Main.cellWidth;
		
		posX = (int)(x*width);
		posY = (int)(y*height);
		
		displayWidth = (int)width;
		displayHeight = (int)height;
		
		//Fix for rounding errors. Some cells get an extra pixel of width/height
		if ((int)((x+1)*width) > posX + displayWidth){
			displayWidth++;
		}
		if ((int)((y+1)*height) > posY + displayHeight){
			displayHeight++;
		}
	}
	
	public void draw(Graphics2D g2d) {
		//Fix for rounding errors. Some cells get an extra pixel of width/height
		if ((int)((x+1)*width) > posX + displayWidth){
			displayWidth++;
		}
		if ((int)((y+1)*height) > posY + displayHeight){
			displayHeight++;
		}
		
		if (processed){
			if (degree == 0){
				g2d.setPaint(lightestGradient);
			} else {
				g2d.setPaint(lightGradient);
			}
		} else {
			g2d.setPaint(gradient);
		}
		
		
		if (Board.dead && mine){
			g2d.setColor(RED);
		}
		
		if (highlighted && (!processed || degree > 0) && !Board.dead && !Board.won){
			if (processed && !mine){
				g2d.setPaint(lightestGradient);
			} else {
				g2d.setPaint(hoverGradient);
			}
		}
		
		g2d.fillRect(posX, posY, displayWidth, displayHeight);
		
		if (marked){
			g2d.setColor(LIGHTGREEN);
			g2d.fillRect(posX, posY, displayWidth, displayHeight);
		}
		
		if (processed && (degree > 0 || mine)){
			g2d.setFont(new Font("default", Font.BOLD, 19));
			if (mine){
				g2d.setColor(Color.RED);
				g2d.drawString("X", posX+((int)width/2)-4, posY+((int)height/2)+7);
			} else {
				switch (degree){
					case 1: g2d.setColor(Color.BLUE);break;
					case 2: g2d.setColor(new Color(50,150,50));break;
					case 3: g2d.setColor(new Color(220,125,0));break;
					case 4: g2d.setColor(new Color(200,0,215));break;
					case 5: g2d.setColor(new Color(217,0,0));break;
					case 6: g2d.setColor(new Color(0,200,200));break;
					case 7: g2d.setColor(new Color(160,110,50));break;
					case 8: g2d.setColor(Color.BLACK);break;
				}
				g2d.drawString(""+degree, posX+((int)width/2)-5, posY+((int)height/2)+7);
			}
		}
		
		//Draw highlight effect
		g2d.setColor(new Color(255,255,255,70));
		g2d.fillRect(posX, posY+1, 1, displayHeight);
		g2d.fillRect(posX, posY, displayWidth, 1);
		g2d.setColor(new Color(0,0,0,70));
		g2d.fillRect(posX+displayWidth-1, posY, 1, displayHeight);
		g2d.fillRect(posX, posY+displayHeight-1, displayWidth, 1);
	}
	
	//Called when window is resized
	public void resize(){
		height = Main.cellHeight;
		width = Main.cellWidth;
		
		posX = (int)(x*width);
		posY = (int)(y*height);
		
		displayWidth = (int)width;
		displayHeight = (int)height;
		
		//Fix for rounding errors. Some cells get an extra pixel of width/height
		if ((int)((x+1)*width) > posX + displayWidth){
			displayWidth++;
		}
		if ((int)((y+1)*height) > posY + displayHeight){
			displayHeight++;
		}

		gradient = new GradientPaint(0, (int)(y*height), new Color(120,120,120), 0, (int)(y*height+height), DARKGRAY);
		lightGradient = new GradientPaint(0, (int)(y*height), new Color(190,190,190), 0, (int)(y*height+height), GRAY);
		lightestGradient = new GradientPaint(0, (int)(y*height), new Color(200,200,200), 0, (int)(y*height+height), LIGHTGRAY);
		hoverGradient = new GradientPaint(0, (int)(y*height), new Color(160,160,160), 0, (int)(y*height+height), new Color(150,150,150));
	}
}
