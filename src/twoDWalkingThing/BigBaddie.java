package twoDWalkingThing;

import java.awt.Color;
//import java.awt.Rectangle;

public class BigBaddie extends Baddie {

	int moveChecker;
	public BigBaddie(){
		super();
		this.size=30;
		this.healthPoints=30;
		this.scorePoints=2;
		this.moveChecker=0;
		this.shade=Color.magenta;
	}
	
	public int getBaseHealth(){
		return 30;
	}
	
	public boolean move(int HEIGHT, int WIDTH){
		if(moveChecker==2){
			moveChecker=0;
			return true;
		}
		moveChecker++;
			
		if(upD){
			if(posY-walkSpeed<0)return false;
			posY-=walkSpeed;
			return true;
		}
		if(downD){
			if(posY+walkSpeed>HEIGHT)return false;
			posY+=walkSpeed;
			return true;
		}
		if(rightD){
			if(posX+walkSpeed>WIDTH)return false;
			posX+=walkSpeed;
			return true;
		}
		if(leftD){
			if(posX-walkSpeed<0)return false;
			posX-=walkSpeed;
			return true;
		}
		return false;
	}
}
