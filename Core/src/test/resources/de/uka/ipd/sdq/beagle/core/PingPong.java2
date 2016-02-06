package de.uka.ipd.sdq.beagle.core;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * CHECKSTYLE:OFF
 */
public class PingPong extends JPanel implements Runnable {
	private static final long serialVersionUID = 1L;

	private final static int SCHRITTWEITE = 1;
	
	private JFrame frame;
	private Rectangle ball;
	
	@SuppressWarnings("unused")
	private int deltaX = SCHRITTWEITE;
	@SuppressWarnings("unused")
	private int deltaY = SCHRITTWEITE;
	
	PingPong() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setMaximumSize(this.getMaximumSize());
		frame.setBounds(100, 100, 500, 500); 
		frame.add(this);
		frame.setVisible(true);
		
		// Da ein Kreis in Java durch seine "BoundingBox" definiert ist,
		// bietet es sich an, den Ball durch ein Rectangle-Objekt zu beschreiben.
		ball = new Rectangle(300, 200, 10, 10);
		
		Thread spiel = new Thread(this);
		spiel.start();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.BLACK);
		g.fillOval(ball.x, ball.y, ball.width, ball.height);	
	}

	@Override
	public void run() {
		while(frame.isVisible()) {
			// Der Ball bewegt sich, macht also einen "Schritt".
			this.bewegeBall();
			
			try {
				// Durch die Wartezeit kann die Bewegungsgeschwindigkeit angepasst werden.
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			// Hier wird neugezeichnet.
			this.repaint();
		}
		
	}
	
	private void bewegeBall() {
		// TODO: Berechnen Sie hier die neuen Koordinaten!
		// ball.x = ... 
		// ball.y = ... 
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		PingPong meinPingPongPanel = new PingPong();
	}

}
