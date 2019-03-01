package twoDWalkingThing;

import java.awt.EventQueue;
import javax.swing.JFrame;

public class Walking extends JFrame{
	
	private static final long serialVersionUID = 5089L;

	public Walking(){
		
		add(new WalkingBoard());
		
		setResizable(false);
		pack();
		
		setTitle("6 Shooter");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void main(String[] args){
		
		EventQueue.invokeLater(new Runnable(){
			@Override
			public void run(){
				JFrame ex = new Walking();
				ex.setVisible(true);
			}
		});
	}
}
