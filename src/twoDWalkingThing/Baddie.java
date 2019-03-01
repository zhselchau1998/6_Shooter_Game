package twoDWalkingThing;

import java.awt.Color;
import java.awt.Rectangle;

public class Baddie {
	
	int posX;
	int posY;
	int walkSpeed;
	int size;
	int healthPoints;
	int scorePoints;
	boolean upD;
	boolean downD;
	boolean rightD;
	boolean leftD;
	Color shade;
	String deathSound;
	Rectangle hitBox;
	
	//Constructors
	public Baddie(){
		this.posX=0;
		this.posY=0;
		this.walkSpeed=1;
		this.size=20;
		this.healthPoints=10;
		this.scorePoints=1;
		this.upD=false;
		this.downD=false;
		this.rightD=false;
		this.leftD=false;
		this.shade=Color.BLUE;
		this.deathSound="MunchingSound.wav";
		this.hitBox=new Rectangle(posX, posY, size, size);
	}
	
	public Baddie(int posX, int posY){
		this.posX=posX;
		this.posY=posY;
		this.walkSpeed=1;
		this.size=20;
		this.healthPoints=10;
		this.scorePoints=1;
		this.upD=false;
		this.downD=false;
		this.rightD=false;
		this.leftD=false;
		this.shade=Color.BLUE;
		this.deathSound="MunchingSound.wav";
		this.hitBox=new Rectangle(posX, posY, size, size);
	}
	
	public Baddie(int walkSpeed, int size, int healthPoints){
		this.posX=0;
		this.posY=0;
		this.walkSpeed=walkSpeed;
		this.size=size;
		this.healthPoints=healthPoints;
		this.scorePoints=1;
		this.upD=false;
		this.downD=false;
		this.rightD=false;
		this.leftD=false;
		this.shade=Color.BLUE;
		this.deathSound="MunchingSound.wav";
		this.hitBox=new Rectangle(posX, posY, size, size);
	}
	
	public Baddie(int posX, int posY, int walkSpeed, int size, int healthPoints){
		this.posX=posX;
		this.posY=posY;
		this.walkSpeed=walkSpeed;
		this.size=size;
		this.healthPoints=healthPoints;
		this.scorePoints=1;
		this.upD=false;
		this.downD=false;
		this.rightD=false;
		this.leftD=false;
		this.shade=Color.BLUE;
		this.deathSound="MunchingSound.wav";
		this.hitBox=new Rectangle(posX, posY, size, size);
	}
	
	public Baddie(int walkSpeed, int size, int healthPoints, Color shade){
		this.posX=0;
		this.posY=0;
		this.walkSpeed=walkSpeed;
		this.size=size;
		this.healthPoints=healthPoints;
		this.scorePoints=1;
		this.upD=false;
		this.downD=false;
		this.rightD=false;
		this.leftD=false;
		this.shade=shade;
		this.deathSound="MunchingSound.wav";
		this.hitBox=new Rectangle(posX, posY, size, size);
	}
	
	public Baddie(int posX, int posY, int walkSpeed, int size, int healthPoints, Color shade){
		this.posX=0;
		this.posY=0;
		this.walkSpeed=walkSpeed;
		this.size=size;
		this.healthPoints=healthPoints;
		this.scorePoints=1;
		this.upD=false;
		this.downD=false;
		this.rightD=false;
		this.leftD=false;
		this.shade=shade;
		this.deathSound="MunchingSound.wav";
		this.hitBox=new Rectangle(posX, posY, size, size);
	}
	
	//Set Commands
	public void setWalkSpeed(int walkSpeed){
		this.walkSpeed=walkSpeed;
	}
	
	public void setPos(int posX, int posY){
		this.posX=posX;
		this.posY=posY;
	}

	public void setSize(int size){
		this.size=size;
	}
	
	public void setHealthPoints(int healthPoints){
		this.healthPoints=healthPoints;
	}
	
	public void setScorePoints(int scorePoints){
		this.scorePoints=scorePoints;
	}
	
	public void setDir(String direction){
		upD=downD=rightD=leftD=false;
		if(direction.compareTo("UP")==0)upD=true;
		if(direction.compareTo("DOWN")==0)downD=true;
		if(direction.compareTo("RIGHT")==0)rightD=true;
		if(direction.compareTo("LEFT")==0)leftD=true;
	}
	
	public void setShade(Color shade){
		this.shade=shade;
	}
	
	public void setDeathSound(String deathSound){
		this.deathSound=deathSound;
	}
	
	public void setHitBox(){
		this.hitBox.setBounds(posX-(size/2), posY-(size/2), size, size);
	}
	
	public void setHitBox(int posX, int posY, int size){
		this.hitBox.setBounds(posX, posY, size, size);
	}
	
	//Get Commands
	public int getWalkSpeed(){
		return this.walkSpeed;
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
	
	public int getHealthPoints(){
		return this.healthPoints;
	}
	
	public int getBaseHealth(){
		return 10;
	}
	
	public int getScorePoints(){
		return this.scorePoints;
	}
	
	public String getDirection(){
		if(upD)return "UP";
		if(downD)return "DOWN";
		if(rightD)return "RIGHT";
		if(leftD)return "LEFT";
		return null;
	}

	public Color getShade(){
		return this.shade;
	}
	
	public String getDeathSound(){
		return this.deathSound;
	}
	
	public Rectangle getHitBox(){
		return this.hitBox;
	}

	//Functions
	public boolean move(int HEIGHT, int WIDTH){
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
