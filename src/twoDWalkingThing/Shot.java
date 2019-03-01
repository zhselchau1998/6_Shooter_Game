package twoDWalkingThing;

import java.awt.Color;
import java.awt.Rectangle;

public class Shot {

	int posX;
	int posY;
	int shotSpeed;
	int size;
	int shotDamage;
	boolean upD;
	boolean downD;
	boolean rightD;
	boolean leftD;
	Color shade;
	String shotSound;
	Rectangle hitBox;
	
	public Shot(){
		this.posX=-20;
		this.posY=-20;
		this.shotSpeed=3;
		this.size=6;
		this.shotDamage=10;
		this.upD=false;
		this.downD=false;
		this.rightD=false;
		this.leftD=false;
		this.shade=Color.red;
		this.shotSound="Shot.wav";
		this.hitBox=new Rectangle(posX, posY, size, size);
	}
	
	public Shot(int posX, int posY){
		this.posX=posX;
		this.posY=posY;
		this.shotSpeed=3;
		this.size=6;
		this.shotDamage=10;
		this.upD=false;
		this.downD=false;
		this.rightD=false;
		this.leftD=false;
		this.shade=Color.red;
		this.shotSound="Shot.wav";
		this.hitBox=new Rectangle(posX, posY, size, size);
	}
	
	//Shot Commands
	public void setPos(int posX, int posY){
		this.posX=posX;
		this.posY=posY;
	}
	
	public void setX(int posX){
		this.posX=posX;
	}
	
	public void setY(int posY){
		this.posY=posY;
	}
	
	public void setSize(int size){
		this.size=size;
	}
	
	public void setShotDamage(int shotDamage){
		this.shotDamage=shotDamage;
	}
	
	public void setDir(String direction){
		direction=direction.toUpperCase();
		upD=downD=rightD=leftD=false;
		if(direction.compareTo("UP")==0)upD=true;
		if(direction.compareTo("DOWN")==0)downD=true;
		if(direction.compareTo("RIGHT")==0)rightD=true;
		if(direction.compareTo("LEFT")==0)leftD=true;
	}
	
	public void setDir(boolean upD, boolean downD, boolean rightD, boolean leftD){
		this.upD=this.downD=this.rightD=this.leftD=false;
		if(upD)this.upD=true;
		if(downD)this.downD=true;
		if(rightD)this.rightD=true;
		if(leftD)this.leftD=true;
		
		if(upD&&downD)this.upD=this.downD=false;
		if(rightD&&leftD)this.rightD=this.leftD=false;
	}
	
	public void setShade(Color shade){
		this.shade=shade;
	}
	
	public void setshotSound(String shotSound){
		this.shotSound=shotSound;
	}
	
	public void setHitBox(){
		this.hitBox.setBounds(posX-(size/2), posY-(size/2), size, size);
	}
	
	public void setHitBox(int posX, int posY, int size){
		this.hitBox.setBounds(posX, posY, size, size);
	}
	
	
	//Get Commands
	public int getShotSpeed(){
		return this.shotSpeed;
	}
	
	public int getX(){
		return this.posX;
	}
	
	public int getY(){
		return this.posY;
	}
	
	public int getSize(){
		return this.size;
	}
	
	public int getShotDamage(){
		return this.shotDamage;
	}
	
/*	public String getDirection(){
		if(upD)return "UP";
		if(downD)return "DOWN";
		if(rightD)return "RIGHT";
		if(leftD)return "LEFT";
		return null;
	}*/

	public Color getShade(){
		return this.shade;
	}
	
	public String getShotSound(){
		return this.shotSound;
	}
	
	public Rectangle getHitBox(){
		return this.hitBox;
	}
	
	public String[] getDir(){
		String[] list = new String[4];
		if(upD)list[0]="UP";
		if(downD)list[1]="DOWN";
		if(rightD)list[2]="RIGHT";
		if(leftD)list[3]="LEFT";
		return list;
	}
	
	
	//Functions
	public boolean move(int HEIGHT, int WIDTH){
		boolean moved=false;
		if(upD){
			if(posY-shotSpeed<0)return false;
			posY-=shotSpeed;
			moved=true;
		}
		if(downD){
			if(posY+shotSpeed>HEIGHT)return false;
			posY+=shotSpeed;
			moved=true;
		}
		if(rightD){
			if(posX+shotSpeed>WIDTH)return false;
			posX+=shotSpeed;
			moved=true;
		}
		if(leftD){
			if(posX-shotSpeed<0)return false;
			posX-=shotSpeed;
			moved=true;
		}
		return moved;
	}
}
