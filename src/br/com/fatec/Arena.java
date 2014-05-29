package br.com.fatec;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/* New imported packages: */
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashSet;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class Arena extends JComponent
		implements MouseListener,  KeyListener, ActionListener{
	private int largura,altura;
	private HashSet<Tanque> tanques;
	private HashSet<Bullet> bullets; //Temporary code
	private Timer contador;
	public Arena(int largura,int altura){
		this.largura = largura;
		this.altura = altura;
		this.tanques = new HashSet<Tanque>();
		this.bullets = new HashSet<Bullet>();
		addMouseListener(this);
		addKeyListener(this);
		setFocusable(true); //Activates the keyboark control
		contador = new Timer(500,this);
		contador.start();
	}
	public void adicionaTanque(Tanque t){
		this.tanques.add(t);
	}

	/* Test code */
	public void addBullet(Bullet b)
	{
		this.bullets.add(b);
	}

	public void removeBullet(Bullet b)
	{
		this.bullets.remove(b);
	}

	public void removeTank(Tanque t)
	{
		this.tanques.remove(t);
	}

	/* end - test code */

	public Dimension getMaximumSize(){
		return getPreferredSize();
	}
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
	public Dimension getPreferredSize(){
		return new Dimension(largura,altura);
	}

	/* Method that shots a bullet from one tank*/
	public void shotBullet()
	{
		double [] bulletPosition = new double[2];
		double [] bulletPositionC = new double[2]; //Corrected bullet position
		double correctionRadius = 35;
		double bulletAttitude;
		int bulletSpeed = (int) Bullet.bulletSpeed;
		for(Tanque t: this.tanques)
		{
			if(t.getActivationStatus())
			{
				bulletPosition = t.getPosition();
				bulletAttitude = t.getAttitude();
				/* bullet position shall be corrected with respect
				 * to the current tank attitude. */
				bulletPositionC[1] = bulletPosition[1] - Math.cos(Math.toRadians(bulletAttitude))*correctionRadius;
				bulletPositionC[0] = bulletPosition[0] + Math.sin(Math.toRadians(bulletAttitude))* correctionRadius;
				Bullet b = new Bullet((int)bulletPositionC[0], (int)bulletPositionC[1], (int)bulletAttitude,  bulletSpeed, Color.BLACK);
				this.addBullet(b);
				break;
			}
		}
	}


	/* Function that performs collision check for
	 * the case where one tank approaches too much to
	 * another. */
	public void detectTankDestructionByCollision()
	{
		double [] tank1Position = new double[2];
		double [] tank2Position = new double[2];
		double distance;
		HashSet<Tanque> tanksThatCollide = new HashSet<Tanque>();
		for(Tanque t1: this.tanques)
		{
			for(Tanque t2: this.tanques)
			{
				/* Collision must be checked between different tanks. */
				if( !t1.equals(t2) )
				{
					tank1Position = t1.getPosition();
					tank2Position = t2.getPosition();
					distance = Math.sqrt(  Math.pow(tank1Position[0]-tank2Position[0], 2)
							             + Math.pow(tank1Position[1]-tank2Position[1], 2));
					/* Distance that defines the tank collision limit*/
					if(distance <=15)
					{
						tanksThatCollide.add(t2);
						tanksThatCollide.add(t1);
					}
				}//Different tanks
			} //for #2
			//if(collision) break;
		}//for #2
		for(Tanque t:tanksThatCollide)
		{
			this.removeTank(t);
		}
	}

	/* Function that destroys a target whenever
	 * the distance between a bullet and a tank
	 * is smaller than 4pixels.*/
	public void detectTargetDestructionByBullet()
	{
		double [] bulletPosition = new double[2];
		double [] tankPosition =  new double[2];
		HashSet<Tanque> tanksThatWereShot = new HashSet<Tanque>();
		HashSet<Bullet> bulletsThatShotTanks = new HashSet<Bullet>();
		double distance;
		for(Bullet b: this.bullets)
		{
			for(Tanque t: this.tanques)
			{
				bulletPosition = b.getPosition();
				tankPosition = t.getPosition();
				distance = Math.sqrt(  Math.pow(bulletPosition[0]-tankPosition[0], 2)
						             + Math.pow(bulletPosition[1]-tankPosition[1], 2));
				if(distance <=10)
				{
					tanksThatWereShot.add(t);
					bulletsThatShotTanks.add(b);
					break; //One bullet can only destroy one tank
				}
			} //All active tanks checked
		}//All active bullets checked

		/* Tank and bullet sets update:*/
		for(Bullet b:bulletsThatShotTanks)
		{
			this.removeBullet(b);
		}
		for(Tanque t: tanksThatWereShot)
		{
			this.removeTank(t);
		}
	}

	/* Detects collision of the tanks with respect
	 * to arena borders. It performs tank rotation and
	 * brings the tank back to the inside of the arena.  */
	public void detectArenaBorderCollision()
	{
		double [] position = new double[2];
		double [] correctedPosition = new double[2];
		double attitude;
		HashSet<Tanque> tankAfterCollision = new HashSet<Tanque>();
		for(Tanque currentTank: this.tanques)
		{
			position = currentTank.getPosition();
			correctedPosition = currentTank.getPosition();
			attitude = currentTank.getAttitude();
			double vx, vy;
			vx = Math.cos(Math.toRadians(attitude));
			vy = Math.sin(Math.toRadians(attitude));
			if( ((int)position[0] )>= this.largura ) //right border collision
			{
				correctedPosition[0] = this.largura - 1; //Brings the tank inside the arena again.
				vy = -vy;
			}
			if( ((int)position[0] )<= 0) //left border collision
			{
				correctedPosition[0] = 1; //Brings the tank inside the arena again.
				vy = -vy;
			}
			if( ((int)position[1]) <= 0)  //upper border collision
			{
				// Corrects the tank orientation and position
				vx = -vx;
				correctedPosition[1] = 1;//Brings the tank inside the arena again.
			}
			if( ((int)position[1]) >= this.altura) //lower border collision
			{
				// Corrects the tank orientation and position
				vx = -vx;
				correctedPosition[1] = this.altura-1; //Brings the tank inside the arena again.
			}
			/* Corrects the current attitude based on the fact that
			 * a collision has occurred */
			attitude = Math.atan2(vy, vx);
			currentTank.setAttitude(Math.toDegrees(attitude));
			currentTank.setPosition(correctedPosition[0], correctedPosition[1]);
			tankAfterCollision.add(currentTank);
		}
		this.tanques = tankAfterCollision;
	}


	@Override
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(new Color(245,245,255));
		g2d.fillRect(0,0,largura,altura);
		g2d.setColor(new Color(220,220,220));
		for(int _largura=0;_largura<=largura;_largura+=20)
			g2d.drawLine(_largura,0,_largura,altura);
		for(int _altura=0;_altura<=altura;_altura+=20)
			g2d.drawLine(0,_altura,largura,_altura);
		// Desenhamos todos os tanques
		for(Tanque t:tanques) t.draw(g2d);
		// Bullet drawing
		for(Bullet b:this.bullets) b.draw(g2d);
	}

	/* Control based on mouse events: */
	public void mouseClicked(MouseEvent e){
		for(Tanque t:tanques)
			t.setEstaAtivo(false);
		for(Tanque t:tanques){
			boolean clicado = t.getRectEnvolvente().contains(e.getX(),e.getY());
			if (clicado){
				t.setEstaAtivo(true);
				switch(e.getButton()){
					case MouseEvent.BUTTON1: t.girarAntiHorario(3); break;
					case MouseEvent.BUTTON2: t.aumentarVelocidade(); break;
					case MouseEvent.BUTTON3: t.girarHorario(3); break;
				}
				break;
			}
		}
		repaint();
	}
	public void mouseEntered(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
	public void mousePressed(MouseEvent e) { }
	public void mouseReleased(MouseEvent e) { }


	@Override
	public void keyTyped(KeyEvent ke)	{}
	@Override
	public void keyReleased(KeyEvent ke) {}

	/* Directional and speed control using direction keys. */
	@Override
	public void keyPressed(KeyEvent ke)
	{
		/* Directional control. */
		for(Tanque t:tanques)
		{
			boolean selected = t.getActivationStatus();
			if (selected){
				switch(ke.getKeyCode())
				{
					case KeyEvent.VK_LEFT: t.girarAntiHorario(3); break;
					case KeyEvent.VK_RIGHT: t.girarHorario(3); break;
					case KeyEvent.VK_UP: t.aumentarVelocidade();break;
					case KeyEvent.VK_DOWN: t.diminuirVelocidade(); break;
					case KeyEvent.VK_SPACE: this.shotBullet(); break;
				}
				break;
			}
		}
		repaint();
	}


	public void actionPerformed(ActionEvent e)
	{
		detectArenaBorderCollision(); /* Detects arena border collision. */
		detectTankDestructionByCollision(); /* Detects collision between tanks.*/
		detectTargetDestructionByBullet();	 /* Detects target destruction */
		for(Tanque t:tanques)
			t.mover();
		for(Bullet b:this.bullets)
			b.move();

		repaint();
	}

	public static void main(String args[]){
		Arena arena = new Arena(600,400);
		arena.adicionaTanque(new Tanque(100,100,0, 0, Color.BLUE));
		arena.adicionaTanque(new Tanque(200,200,45, 10, Color.RED));
		arena.adicionaTanque(new Tanque(300,300,90, 10, Color.GREEN));
		arena.adicionaTanque(new Tanque(350,350,135, 10, Color.YELLOW));

		JFrame janela = new JFrame("Tanques");
		janela.getContentPane().add(arena);
		janela.pack();
		janela.setVisible(true);
		janela.setDefaultCloseOperation(3);
	}

}