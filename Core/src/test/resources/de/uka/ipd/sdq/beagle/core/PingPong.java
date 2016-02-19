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

	@SuppressWarnings("unused")
	private final static int SCHRITTWEITE = 1;

	private final JFrame frame;

	private final Rectangle ball;

	PingPong() {
		this.frame = new JFrame();
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.setMaximumSize(this.getMaximumSize());
		this.frame.setBounds(100, 100, 500, 500);
		this.frame.add(this);
		this.frame.setVisible(true);

		// Da ein Kreis in Java durch seine "BoundingBox" definiert ist,
		// bietet es sich an, den Ball durch ein Rectangle-Objekt zu beschreiben.
		this.ball = new Rectangle(300, 200, 10, 10);

		final Thread spiel = new Thread(this);
		spiel.start();
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);

		g.setColor(Color.BLACK);
		g.fillOval(this.ball.x, this.ball.y, this.ball.width, this.ball.height);
	}

	@Override
	public void run() {
		while (this.frame.isVisible()) {
			// Der Ball bewegt sich, macht also einen "Schritt".
			this.bewegeBall();

			try {
				// Durch die Wartezeit kann die Bewegungsgeschwindigkeit angepasst werden.
				Thread.sleep(10);
			} catch (final InterruptedException e) {
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
	public static void main(final String[] args) {
		final PingPong meinPingPongPanel = new PingPong();
	}

}
