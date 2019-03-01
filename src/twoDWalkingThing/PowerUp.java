package twoDWalkingThing;

import java.awt.Color;
import java.awt.Rectangle;

public class PowerUp {

	int posX;
	int posY;
	int size;
	Rectangle hitBox;
	Color shade;
	
	//Contructors
	public PowerUp(){
		this.posX=0;
		this.posY=0;
		this.size=10;
		this.hitBox=new Rectangle(posX,posY,size,size);
		this.shade=Color.PINK;
	}
	
	public PowerUp(int posX, int posY){
		this.posX=posX;
		this.posY=posY;
		this.size=10;
		this.hitBox=new Rectangle(posX,posY,size,size);
		this.shade=Color.PINK;
	}
	
	//Gets
	public int getPosX(){
		return this.posX;
	}
	
	public int getPosY(){
		return this.posY;
	}
	
	public int getSize(){
		return this.size;
	}
	
	public Rectangle getHitBox(){
		return this.hitBox;
	}
	
	public Color getShade(){
		return this.shade;
	}
	
	
	//Set
	public void setPosX(int posX){
		this.posX=posX;
		resetHitBox();
	}
	
	public void setPosY(int posY){
		this.posY=posY;
		resetHitBox();
	}
	
	public void setPos(int posX, int posY){
		this.posX=posX;
		this.posY=posY;
		resetHitBox();
	}
	
	public void setSize(int size){
		this.size=size;
	}
	
	public void setshade(Color shade){
		this.shade=shade;
	}
	
	//Functions
	private void resetHitBox(){
		hitBox.setBounds(posX, posY, size, size);
	}
	
	public String powerUpType(){
		return "NULL";
	}
}
