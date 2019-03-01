package twoDWalkingThing;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.Random;

import javax.sound.sampled.*;
import javax.swing.*;

public class WalkingBoard extends JPanel implements ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6953L;
	//////////////////////////////////////////////
	//Constants
	//////////////////////////////////////////////
	private final int B_WIDTH = 300;
	private final int B_HEIGHT = 300;
	private final int DELAY = 40;//40 fps
	private final int MAX_SHOTS = 6;
	private final int MAX_BADDIES = 6;
	//////////////////////////////////////////////
	//Variables
	//////////////////////////////////////////////
	//Strings
	
	//Ints/Numbers
	private int playerX;
	private int playerY;
	private int playerSize;
	private int playerSpeed;
	private int baddiesSize;
	private int shotsSize;
	private int score;
	private int level;
	private int extraBaddies;
	private int extraBigBaddies;
	private int extraFastBaddies;
	private long lastTimeHit;
	private long startTime;
	private long endLevelTime;
	private long powerUpTime;
	
	//Booleans
	private boolean upDir;
	private boolean downDir;
	private boolean rightDir;
	private boolean leftDir;
	private boolean playerAlive;
	private boolean inGame;
	private boolean betweenLevels;
	private boolean beatGame;
	private boolean pauseGame;
	private boolean triShot;
	private boolean invincible;
	
	//Structure and texture
	private Timer timer;
	private Font smlFont;
	private Font bigFont;
	private FontMetrics smlMtr;
	private FontMetrics bigMtr;
	private Rectangle playerHitBox = new Rectangle();
	private File soundFile;
	private Clip loopClip;
	private Clip onePlayClip;
	private Color playerColor;
	
	//NPCs
	private Baddie[] baddies;
	private Shot[] shots;
	private PowerUp powerUp;
	
	//////////////////////////////////////////////
	//Constructor
	//////////////////////////////////////////////
	public WalkingBoard(){
		
		addKeyListener(new TAdapter());
		setBackground(Color.LIGHT_GRAY);
		setFocusable(true);
		
		setPreferredSize(new Dimension(B_WIDTH,B_HEIGHT));
		initGame();
	}
	
	//////////////////////////////////////////////
	//Functions
	//////////////////////////////////////////////
	
	//TODO:Game/System Stuff
	private void initGame(){
		//Player
		playerX = B_WIDTH/2;
		playerY = B_HEIGHT/2;
		playerSize=20;
		playerSpeed=2;
		playerHitBox.setBounds(playerX-(playerSize/2), playerY-(playerSize/2), playerSize, playerSize);
		upDir=false;
		downDir=false;
		rightDir=false;
		leftDir=false;
		playerAlive=true;
		playerColor=Color.black;
		
		//Baddies
		baddies  = new Baddie[MAX_BADDIES];
		baddiesSize=0;
		extraBaddies=0;
		extraBigBaddies=0;
		extraFastBaddies=0;
		
		//Shot
		shots = new Shot[MAX_SHOTS];
		shotsSize=0;
		
		//PowerUps
		powerUp=null;
		powerUpTime=0;
		triShot=false;
		invincible=false;
		
		//System
		score=0;
		level=0;
		inGame=true;
		betweenLevels=true;
		beatGame=false;
		pauseGame=false;
		timer= new Timer(DELAY, this);
		smlFont = new Font("Helvetica", Font.BOLD, 14);
		bigFont = new Font("Helvetica", Font.BOLD, 30);
		smlMtr = getFontMetrics(smlFont);
		bigMtr = getFontMetrics(bigFont);
		lastTimeHit = System.currentTimeMillis();
		startTime = lastTimeHit;
		endLevelTime = startTime;
		
		timer.start();
		
		loopSound("WalkingGameMusic.wav");
		
	}
	
	public void actionPerformed(ActionEvent e) {
		moveShot();
		move();
		checkCollision();
		moveBaddies();
		powerUpChecker();
		if(baddiesEmpty() && extraBaddies==0 )betweenLevels=true;
		
		repaint();
		
	}
	
	private void playSound(String fileName){
    	
    	soundFile = new File(fileName);
    	
    	try{
    		
    		onePlayClip = AudioSystem.getClip();
    		onePlayClip.open(AudioSystem.getAudioInputStream(soundFile));
    		onePlayClip.start();
    		
    	}catch(Exception e){}
    }
	
    private void loopSound(String fileName){
    	
    	soundFile = new File(fileName);
    	
    	try{
    		
    		loopClip = AudioSystem.getClip();
    		loopClip.open(AudioSystem.getAudioInputStream(soundFile));
    		loopClip.start();
    		loopClip.loop(Clip.LOOP_CONTINUOUSLY);
    		
    	}catch(Exception e){}
    	
    }
    
    private void pauseMusic(){
    	if(loopClip.isActive())
    		loopClip.stop();
    	else{
    		loopClip.start();
    		loopClip.loop(Clip.LOOP_CONTINUOUSLY);
    	}
    }
	
	private void checkCollision(){
		
		//PowerUp
		if(powerUp!=null&&powerUp.getHitBox().intersects(playerHitBox)){
			switch(powerUp.powerUpType()){
			case "TRISHOT":
				triShot=true;
				playerColor=Color.pink;
				break;
			case "INVINCIBLE":
				invincible=true;
				playerSpeed=4;
				playerColor=Color.yellow;
				break;
			}
			powerUpTime=System.currentTimeMillis();
			System.out.println(powerUp.powerUpType()+" grabbed, "+(powerUpTime+5000-System.currentTimeMillis())/1000+"s left");
			powerUp=null;
		}
		
		//Baddies
		for(int i=0; i<MAX_BADDIES; i++){
			if(baddies[i]==null) continue;
			
			//Rects
			//Player and Baddies
			if(baddies[i].getHitBox().intersects(playerHitBox))
				if(invincible){
					lastTimeHit=System.currentTimeMillis();
					playSound(baddies[i].getDeathSound());
					score+=baddies[i].scorePoints;
					long currTime = (System.currentTimeMillis()-startTime)/1000;
					System.out.println("Score: "+score+" Time: "+currTime);
					baddies[i]=null;
					continue;
				}
				else playerAlive=false;
			
			//Shot and Baddies
			for(int n=0; n<MAX_SHOTS;n++){
				if(shots[n]==null)continue;
				if(baddies[i].getHitBox().intersects(shots[n].getHitBox())){
					baddies[i].setHealthPoints(baddies[i].getHealthPoints()-shots[n].getShotDamage());
					if(baddies[i].getHealthPoints()<=0){
						lastTimeHit=System.currentTimeMillis();
						playSound(baddies[i].getDeathSound());
						score+=baddies[i].scorePoints;
						long currTime = (System.currentTimeMillis()-startTime)/1000;
						System.out.println("Score: "+score+" Time: "+currTime);
						baddies[i]=null;
						break;
					}
						baddies[i].setSize(baddies[i].getSize()-8);
					shots[n]=null;
					break;
				}
			}
		}
	}
	
	private void level(int level){
		clearShots();
		playerX=B_WIDTH/2;
		playerY=B_HEIGHT/2;
		
		switch(level){
		case 1:
			spawnBaddie();
			break;
		case 2:
			for(int i=0; i<2; i++)
				spawnBaddie();
			break;
		case 3:
			for(int i=0; i<5; i++)
				spawnBaddie();
			spawnPowerUp(1);
			break;
		case 4:
			spawnBigBaddie();
			break;
		case 5:
			for(int i=0; i<3; i++)
				spawnBaddie();
			spawnBigBaddie();
			spawnPowerUp(1);
			break;
		case 6:
			for(int i=0; i<4; i++)
				spawnBaddie();
			for(int i=0; i<2; i++)
				spawnBigBaddie();
			spawnPowerUp(1);
			extraBaddies=1;
			break;
		case 7:
			for(int i=0; i<5; i++)
				spawnBaddie();
			spawnBigBaddie();
			spawnPowerUp(2);
			extraBigBaddies=2;
			break;
		case 8:
			for(int i=0; i<4; i++)
				spawnBaddie();
			for(int i=0; i<2; i++)
				spawnBigBaddie();
			//spawnPowerUp(1);
			extraBigBaddies=2;
			extraBaddies=2;
			break;
		case 9:
			for(int i=0; i<4; i++)
				spawnBigBaddie();
			for(int i=0; i<2; i++)
				spawnBaddie();
			spawnPowerUp(1);
			extraBaddies=4;
			break;
		case 10:
			for(int i=0; i<6; i++)
				spawnBigBaddie();
			spawnPowerUp(1);
			extraBaddies=4;
			extraBigBaddies=3;
			break;
		case 11:
				spawnFastBaddie();
			break;
		case 12:
			for(int i=0; i<2; i++)
				spawnFastBaddie();
			for(int i=0; i<3; i++)
				spawnBaddie();
			//spawnPowerUp(1);
			break;
		case 13:
			for(int i=0; i<2; i++)
				spawnFastBaddie();
			for(int i=0; i<2; i++)
				spawnBigBaddie();
			for(int i=0; i<2; i++)
				spawnBaddie();
			spawnPowerUp(1);
			extraBaddies=2;
			break;
		case 14:
			for(int i=0; i<4; i++)
				spawnBaddie();
			for(int i=0; i<2; i++)
				spawnFastBaddie();
			extraBaddies=2;
			extraBigBaddies=2;
			//spawnPowerUp(2);
			break;
		case 15:
			for(int i=0; i<3; i++)
				spawnFastBaddie();
			for(int i=0; i<3; i++)
				spawnBigBaddie();
			spawnPowerUp(1);
			extraBaddies=3;
			extraBigBaddies=3;
			extraFastBaddies=1;
			break;
		case 16:
			for(int i=0; i<4; i++)
				spawnFastBaddie();
			for(int i=0; i<2; i++)
				spawnBigBaddie();
			spawnPowerUp(2);
			extraFastBaddies=3;
			extraBigBaddies=2;
			extraBaddies=4;
			break;
		case 17:
			for(int i=0; i<6; i++)
				spawnFastBaddie();
			extraFastBaddies=5;
			spawnPowerUp(1);
			break;
		case 18:
			for(int i=0; i<6; i++)
				spawnFastBaddie();
			extraBaddies=6;
			extraBigBaddies=4;
			extraFastBaddies=3;
			spawnPowerUp(2);
			break;
		case 19:
			for(int i=0; i<6; i++)
				spawnFastBaddie();
			extraBaddies=5;
			extraBigBaddies=6;
			extraFastBaddies=5;
			spawnPowerUp(2);
			break;
		case 20:
			for(int i=0; i<6; i++)
				spawnFastBaddie();
			extraBigBaddies=8;
			extraFastBaddies=8;
			spawnPowerUp(1);
			break;
		}
		
	}
	
	private void startLevel(Graphics g){
		setBackground(Color.gray);
		g.setColor(Color.white);
		g.setFont(bigFont);
		int fake = level+1;
		g.drawString("Level "+fake, (B_WIDTH-bigMtr.stringWidth("Level "+fake))/2, B_HEIGHT/2);
		
		if(endLevelTime+1000<System.currentTimeMillis()){
			betweenLevels=false;
			
			level++;
			level(level);
		}
	}
	
	private void pauseGame(){
		if(pauseGame){
			pauseGame=false;
			timer.restart();
		}
		else {
			pauseGame=true;
			timer.stop();
		}
	}
	
	private void overlay(Graphics g){
		//Save color
		Color currColor = g.getColor();
		g.setColor(Color.white);
		
		//Score
		g.drawString("Score: "+score, B_WIDTH-smlMtr.stringWidth("Score: "+score), B_HEIGHT-2);
		
		//Level
		g.drawString("Level: "+level, (B_WIDTH-smlMtr.stringWidth("Level: "+level))/2, 16);
		
		//Time
		int gameTime= (int) (System.currentTimeMillis()-startTime)/1000;
		g.drawString("Time: "+gameTime, 2, B_HEIGHT-2);
		
		//Powerup
		g.setColor(Color.RED);
		if(invincible) g.drawString("INVINCIBLE: "+(powerUpTime-System.currentTimeMillis()+5000)/1000, 2, 16);
		if(triShot) g.drawString("TRISHOT: "+(powerUpTime-System.currentTimeMillis()+5000)/1000, 2, 16);
		//Reset color
		g.setColor(currColor);
	}
	
	private void gameOver(Graphics g){
		int gameTime= (int) (System.currentTimeMillis()-startTime)/1000;
		inGame=false;
		
		g.setColor(getBackground());
		g.fillRect(0, 0, B_WIDTH, B_HEIGHT);
		
		g.setColor(Color.white);
		g.setFont(bigFont);
		g.drawString("GAME OVER", (B_WIDTH-bigMtr.stringWidth("GAME OVER"))/2, B_HEIGHT/3);
		
		g.setFont(smlFont);
		g.drawString("SCORE: "+score+" TIME: "+gameTime, (B_WIDTH-smlMtr.stringWidth("SCORE: "+score+" TIME: "+gameTime))/2, B_HEIGHT/3*2);
		g.drawString("Press Enter to Restart", (B_WIDTH-smlMtr.stringWidth("Press Enter to Restart"))/2, B_HEIGHT/2);
		
		timer.stop();
		
		//End Game Sound
		try {
			Thread.sleep(900);
		} catch (InterruptedException e) {}
		
		loopClip.stop();
		playSound("endGame.wav");
		
		try {
			Thread.sleep(900);
		} catch (InterruptedException e) {}
		
		onePlayClip.stop();
		playSound("endGame.wav");
		
		try {
			Thread.sleep(900);
		} catch (InterruptedException e) {}
		
		onePlayClip.stop();
		playSound("endGame.wav");
		
		

		
	}
	
	private void beatGame(Graphics g){
		int gameTime= (int) (System.currentTimeMillis()-startTime)/1000;
		inGame=false;
		score+=20;
		
		loopClip.stop();
		
		if(level==20){
			playSound("Win Game.wav");
			level++;
		}
		
		g.setColor(Color.WHITE);
		g.setFont(bigFont);
		
		g.drawString("CONGRATS!", (B_WIDTH-bigMtr.stringWidth("CONGRATS!"))/2, B_HEIGHT/2);
		
		g.setFont(smlFont);
		g.drawString("SCORE: "+score+" TIME: "+gameTime, (B_WIDTH-smlMtr.stringWidth("SCORE: "+score+" TIME: "+gameTime))/2, B_HEIGHT/3*2);
		g.drawString("Press Enter to Restart", (B_WIDTH-smlMtr.stringWidth("Press Enter to Restart"))/2, B_HEIGHT/2+20);
		
		timer.stop();
	}
	
	//TODO:Player Stuff
	private void outOfBoundsCheck(){
		
		if(playerY<15) playerY=15;
		if(playerY>B_HEIGHT-15) playerY=B_HEIGHT-15;
		if(playerX<15) playerX=15;
		if(playerX>B_WIDTH-15) playerX=B_WIDTH-15;
		
	}

	private void move(){	
		
		if(upDir)
			playerY-= playerSpeed;
		if(downDir)
			playerY+= playerSpeed;
		if(rightDir)
			playerX+= playerSpeed;
		if(leftDir)
			playerX-= playerSpeed;
		
		outOfBoundsCheck();
		
		playerHitBox.setBounds(playerX-(playerSize/2), playerY-(playerSize/2), playerSize, playerSize);
	}
	
	private void jump(){
		if(leftDir) playerX-=50;
		if(rightDir) playerX+=50;
		if(upDir) playerY-=50;
		if(downDir) playerY+=50;
		
		outOfBoundsCheck();
		
		playerHitBox.setBounds(playerX-(playerSize/2), playerY-(playerSize/2), playerSize, playerSize);
	}
	
	//TODO:Shot Stuff
	private void moveShot(){
		for(int i=0; i< MAX_SHOTS; i++){
			
			if(shots[i]!=null){
				if(!shots[i].move(B_HEIGHT, B_WIDTH)){
					shots[i]=null;
					continue;
				}
			
				shots[i].setHitBox();
			}	
		}
	}
	
	private void shot(){
		
		System.out.print("Shot called");
		
		if(!shotAvailable()) {
			System.out.println(": failed");
			return;
		}
		System.out.println(": succeded");
		
		int shotX=playerX;
		int shotY=playerY;
		
		while(shots[shotsSize]!=null){
			shotsSize++;
			if(shotsSize==MAX_SHOTS)shotsSize=0;
		}
		
		shots[shotsSize]= new Shot(playerX, playerY);
		
		playSound(shots[shotsSize].shotSound);
		shots[shotsSize].setDir(upDir, downDir, rightDir, leftDir);
		if(upDir)shots[shotsSize].setY(shotY-22);
		if(downDir)shots[shotsSize].setY(shotY+16);
		if(rightDir)shots[shotsSize].setX(shotX+16);
		if(leftDir)shots[shotsSize].setX(shotX-22);
		
		if(triShot){
			//2nd Shot
			if(!shotAvailable()) return;
			
			while(shots[shotsSize]!=null){
				shotsSize++;
				if(shotsSize==MAX_SHOTS)shotsSize=0;
			}
			
			shots[shotsSize]=new Shot(playerX, playerY);
			if((upDir&&downDir)||(!upDir&&!downDir)){
				shots[shotsSize].setDir(true,false, rightDir, leftDir);
				if(rightDir)shots[shotsSize].setPos(shotX+16, shotY-22);
				if(leftDir)shots[shotsSize].setPos(shotX-22, shotY-22);
			}
			else if((rightDir&&leftDir)||(!rightDir&&!leftDir)){
				shots[shotsSize].setDir(upDir,downDir, true, false);
				if(upDir)shots[shotsSize].setPos(shotX+16, shotY-22);
				if(downDir)shots[shotsSize].setPos(shotX+16, shotY+16);
			}
			else if(upDir){
				shots[shotsSize].setDir("UP");
				shots[shotsSize].setPos(shotX, shotY-22);
			}
			else if(downDir){
				shots[shotsSize].setDir("DOWN");
				shots[shotsSize].setPos(shotX, shotY+16);
			}
			
			//3rd Shot
			if(!shotAvailable()) return;
			
			while(shots[shotsSize]!=null){
				shotsSize++;
				if(shotsSize==MAX_SHOTS)shotsSize=0;
			}
			
			shots[shotsSize]=new Shot(playerX, playerY);
			if((upDir&&downDir)||(!upDir&&!downDir)){
				shots[shotsSize].setDir(false,true, rightDir, leftDir);
				if(rightDir)shots[shotsSize].setPos(shotX+16, shotY+16);
				if(leftDir)shots[shotsSize].setPos(shotX-22, shotY+16);
			}
			else if((rightDir&&leftDir)||(!rightDir&&!leftDir)){
				shots[shotsSize].setDir(upDir,downDir, false, true);
				if(upDir)shots[shotsSize].setPos(shotX-22, shotY-22);
				if(downDir)shots[shotsSize].setPos(shotX-22, shotY+16);
			}
			else if(rightDir){
				shots[shotsSize].setDir("RIGHT");
				shots[shotsSize].setPos(shotX+16, shotY);
			}
			else if(leftDir){
				shots[shotsSize].setDir("LEFT");
				shots[shotsSize].setPos(shotX-22, shotY);
			}
		}

	}
	
	private void clearShots(){
		for (int i=0;i<MAX_SHOTS;i++)
			shots[i]=null;
	}
	
	private boolean shotAvailable(){
		for(int i=0; i<MAX_SHOTS; i++)
			if(shots[i]==null)return true;
		return false;
	}	
	
	//TODO:PowerUp Stuff
	private void spawnPowerUp(int choosePowerUp){
		
		switch(choosePowerUp){
		case 1:
			powerUp=new TriShot(B_WIDTH/2, B_HEIGHT/3);
			break;
		case 2:
			powerUp=new Invincible(B_WIDTH/2, B_HEIGHT/3);
			break;
		}
		
		System.out.println("PowerUp spawned at: "+powerUp.getPosX()+", "+powerUp.getPosY());
	}
	
	private void drawPowerUp(Graphics g){
		if(powerUp !=null){
			Color save=g.getColor();
			g.setColor(powerUp.shade);
			g.fillRect(powerUp.getPosX()-powerUp.getSize(), powerUp.getPosY()-powerUp.getSize(), powerUp.getSize(), powerUp.getSize());
			g.setColor(save);
		}
	}
	
	private void powerUpChecker(){
		if(powerUp!=null)return;
		if(!triShot&&!invincible)return;
		if(baddiesEmpty()){
			triShot=invincible=false;
			playerSpeed=2;
			playerColor=Color.black;
			System.out.println("powerUp ended");
		}
		if(powerUpTime<System.currentTimeMillis()-5000){
			triShot=invincible=false;
			playerSpeed=2;
			playerColor=Color.black;
			System.out.println("powerUp ended");
		}
	}
	
	//TODO:Baddie Stuff
	private void spawnBaddie(){
		if(baddiesFull()||baddiesSize>=MAX_BADDIES)return;
		while(baddies[baddiesSize]!=null) {
			baddiesSize++;
			if(baddiesSize==MAX_BADDIES)baddiesSize=0;
		}
		System.out.println("Baddie spawned");
		baddies[baddiesSize++] = new Baddie();
		findSpawn(baddiesSize-1);
		baddies[baddiesSize-1].setHitBox();
		System.out.println(baddies[baddiesSize-1].getX() + ", " + baddies[baddiesSize-1].getY());
		if(baddiesSize==MAX_BADDIES)baddiesSize=0;
	}
	
	private void spawnBigBaddie(){
		if(baddiesFull()||baddiesSize>=MAX_BADDIES)return;
		while(baddies[baddiesSize]!=null) {
			baddiesSize++;
			if(baddiesSize==MAX_BADDIES)baddiesSize=0;
		}
		System.out.println("Baddie spawned");
		baddies[baddiesSize++] = new BigBaddie();
		findSpawn(baddiesSize-1);
		baddies[baddiesSize-1].setHitBox();
		System.out.println(baddies[baddiesSize-1].getX() + ", " + baddies[baddiesSize-1].getY());
		if(baddiesSize==MAX_BADDIES)baddiesSize=0;
	}
	
	private void spawnFastBaddie(){
		if(baddiesFull()||baddiesSize>=MAX_BADDIES)return;
		while(baddies[baddiesSize]!=null) {
			baddiesSize++;
			if(baddiesSize==MAX_BADDIES)baddiesSize=0;
		}
		System.out.println("Baddie spawned");
		baddies[baddiesSize++] = new fastBaddie();
		findSpawn(baddiesSize-1);
		baddies[baddiesSize-1].setHitBox();
		System.out.println(baddies[baddiesSize-1].getX() + ", " + baddies[baddiesSize-1].getY());
		if(baddiesSize==MAX_BADDIES)baddiesSize=0;
	}
	
	private void findSpawn(int idx){
		if(idx < 0||idx>=MAX_BADDIES||baddies[idx]==null)return;
		
		int rand = (int) Math.abs(new Random().nextInt(4));
		
		switch(rand){
		
			case 0: 
				baddies[idx].setPos((int) Math.abs(new Random().nextInt(B_WIDTH-baddies[idx].getSize())+baddies[idx].getSize()/2), 0);
				baddies[idx].setDir("DOWN");
				break;
			
			case 1:
				baddies[idx].setPos((int)Math.abs(new Random().nextInt(B_WIDTH-baddies[idx].getSize())+baddies[idx].getSize()/2), B_HEIGHT);
				baddies[idx].setDir("UP");
				break;
			
			case 2:
				baddies[idx].setPos( 0, (int)Math.abs(new Random().nextInt(B_HEIGHT-baddies[idx].getSize())+baddies[idx].getSize()/2));
				baddies[idx].setDir("RIGHT");
				break;
			
			case 3:
				baddies[idx].setPos(B_WIDTH, (int)Math.abs(new Random().nextInt(B_HEIGHT-baddies[idx].getSize())+baddies[idx].getSize()/2));
				baddies[idx].setDir("LEFT");
				break;
			
		}
	}
	
	private void drawBaddies(Graphics g){
		for(int i = 0; i < MAX_BADDIES; i++){
			if(baddies[i]!=null){
				g.setColor(baddies[i].shade);
				g.fillRect((int)baddies[i].getHitBox().getX(), (int)baddies[i].getHitBox().getY(), baddies[i].getSize(), baddies[i].getSize());
				g.setColor(Color.GREEN);
				g.fillRect(baddies[i].getX(), baddies[i].getY(), 2, 2);
			}
		}
	}
	
	private void moveBaddies(){
		for(int i = 0; i < MAX_BADDIES; i++){
			if(baddies[i]!=null){
				if(!baddies[i].move(B_HEIGHT, B_WIDTH)){
					playerAlive=false;
					continue;
				}
				
				baddies[i].setHitBox();
			}
		}
	}
	
	private boolean baddiesFull(){
		for(int i = 0; i<MAX_BADDIES;i++)
			if(baddies[i]==null)return false;
		return true;
	}
	
	private boolean baddiesEmpty(){
		for(int i = 0; i<MAX_BADDIES;i++)
			if(baddies[i]!=null)return false;
		return true;
	}
	
	//TODO:Paint Stuff
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		
		if(beatGame) beatGame(g);
		else if(betweenLevels&&lastTimeHit+2000<System.currentTimeMillis()){
			if(level<20)startLevel(g);
			else beatGame=true;
		}
		else doDrawing(g);
		
		if(betweenLevels&&lastTimeHit+2000>System.currentTimeMillis())endLevelTime=lastTimeHit+2000;
	}
	

	private void doDrawing(Graphics g){
		
		if(level<2){
			g.setColor(Color.WHITE);
			g.setFont(smlFont);
			
			g.drawString("Use arrows keys to move", (B_WIDTH-smlMtr.stringWidth("Use arrow keys to move"))/2, B_HEIGHT/3);
			
			g.drawString("Press 1 to shoot", (B_WIDTH-smlMtr.stringWidth("Press 1 to shoot"))/2, B_HEIGHT/2);
			g.drawString("You can ONLY shoot while moving", (B_WIDTH-smlMtr.stringWidth("You can ONLY shoot while moving"))/2, B_HEIGHT/2+20);
			
			g.drawString("Don't get hit or you lose!", (B_WIDTH-smlMtr.stringWidth("Don't get hit or you lose!"))/2, B_HEIGHT*2/3);
		}
		
		if(level == 2){
			g.setColor(Color.WHITE);
			g.setFont(smlFont);
			
			g.drawString("Press spacebar to jump", (B_WIDTH-smlMtr.stringWidth("Press spacebar to jump"))/2, B_HEIGHT/3);
			g.drawString("Don't let them get to", (B_WIDTH-smlMtr.stringWidth("Don't let them get to"))/2, B_HEIGHT*2/3);
			g.drawString("the other side or you lose!", (B_WIDTH-smlMtr.stringWidth("the other side or you lose!"))/2, B_HEIGHT*2/3+20);
		}
		
		if(level == 3){
			g.setColor(Color.white);
			g.setFont(smlFont);
			
			g.drawString("Power ups spawn in the", (B_WIDTH-smlMtr.stringWidth("Power ups spawn in the"))/2, 2*B_HEIGHT/3);
			g.drawString("same spot ever few levels", (B_WIDTH-smlMtr.stringWidth("same spot ever few levels"))/2, 2*B_HEIGHT/3+20);
		}
		
		if(inGame){
			g.setFont(smlFont);
			setBackground(Color.GRAY);
		
			//player
			g.setColor(playerColor);
			g.fillRect(playerX-playerSize/2, playerY-playerSize/2, playerSize, playerSize);
			g.setColor(Color.GREEN);
			g.fillRect(playerX, playerY, 2, 2);
		
			//shot
			for(int i=0; i<MAX_SHOTS; i++)
				if(shots[i]!=null){
					g.setColor(shots[i].getShade());
					g.fillRect(shots[i].getX(), shots[i].getY(), shots[i].getSize(), shots[i].getSize());
				}
		
			//powerUp
			drawPowerUp(g);
			
			//Baddies
			drawBaddies(g);
			if(extraBaddies!=0 && extraBigBaddies!=0){
				int i=(int) Math.abs(new Random().nextInt(2));
				switch (i){
				case 0:
					if(extraBaddies!=0 && !baddiesFull()){
						spawnBaddie();
						extraBaddies--;
					}
					break;
				case 1:
					if(extraBigBaddies!=0 && !baddiesFull()){
						spawnBigBaddie();
						extraBigBaddies--;
					}
					break;
				}
			}else
				if(extraBaddies!=0 && !baddiesFull()){
					spawnBaddie();
					extraBaddies--;
				}
				else if(extraBigBaddies!=0 && !baddiesFull()){
					spawnBigBaddie();
					extraBigBaddies--;
				}
				else if(extraFastBaddies!=0 && !baddiesFull()){
					spawnFastBaddie();
					extraFastBaddies--;
				}
		
			//System
			overlay(g);
			if(pauseGame){
				g.setColor(Color.white);
				g.setFont(bigFont);
				g.drawString("PAUSE", (B_WIDTH-bigMtr.stringWidth("PAUSE"))/2, B_HEIGHT/2-20);
			}
				
		
		}
		
		//GameOvers
		if(!playerAlive)
			gameOver(g);
		
	}
	
	
	//Classes
	private class TAdapter extends KeyAdapter{
		
		public void keyPressed(KeyEvent e){
			int key = e.getKeyCode();
			
			//Player
			//Direction
			if(key == KeyEvent.VK_LEFT)
				leftDir=true;
			if(key == KeyEvent.VK_UP)
				upDir=true;
			if(key == KeyEvent.VK_DOWN)
				downDir=true;
			if(key == KeyEvent.VK_RIGHT)
				rightDir=true;
			//Jump
			if(key == KeyEvent.VK_SPACE)
				jump();
			//Shot
			if(key == KeyEvent.VK_1 || key == KeyEvent.VK_NUMPAD0)
				shot();
			
			//Baddies
			//Spawn Baddie
			if(key == KeyEvent.VK_S)
				spawnBaddie();
			
			//System
			if(key == KeyEvent.VK_ESCAPE)
				System.exit(0);
			
			if(key == KeyEvent.VK_M)
				pauseMusic();
			
			if(!inGame && key == KeyEvent.VK_ENTER){
				if(onePlayClip!=null)onePlayClip.stop();
				initGame();
			}
			
			if(key == KeyEvent.VK_P)
				pauseGame();
				
			
		}
		
		public void keyReleased(KeyEvent e){
			int key = e.getKeyCode();
			
			//Player
			if(key == KeyEvent.VK_LEFT)
				leftDir=false;
			if(key == KeyEvent.VK_RIGHT)
				rightDir=false;
			if(key == KeyEvent.VK_UP)
				upDir=false;
			if(key == KeyEvent.VK_DOWN)
				downDir=false;

		}
		
	}
}
