package twoDWalkingThing;

import java.awt.Color;
import java.awt.Rectangle;

public class TriShot extends PowerUp{
	
	public TriShot(){
		super();
		this.shade=Color.red;
	}
	
	public TriShot(int posX, int posY){
		super();
		this.posX=posX;
		this.posY=posY;
		this.hitBox=new Rectangle(posX, posY);
		this.shade=Color.pink;
	}
	
	public String powerUpType(){
		return "TRISHOT";
	}

}
