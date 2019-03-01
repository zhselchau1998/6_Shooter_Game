package twoDWalkingThing;

import java.awt.Color;
import java.awt.Rectangle;

public class Invincible extends PowerUp{
	
	public Invincible(){
		super();
		this.shade=Color.yellow;
	}
	
	public Invincible(int posX, int posY){
		super();
		this.posX=posX;
		this.posY=posY;
		this.hitBox=new Rectangle(posX, posY);
		this.shade=Color.yellow;
	}
	
	public String powerUpType(){
		return "INVINCIBLE";
	}

}
