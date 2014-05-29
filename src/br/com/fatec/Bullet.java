package br.com.fatec;

//import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
//import java.awt.Rectangle;
//import java.awt.Shape;
//import java.awt.Stroke;
import java.awt.geom.AffineTransform;

public class Bullet
{

	private double x,y; 			// Position
	private double attitude;		// Attitude
	private double speedModulus;	// Linear speed
	private Color color;			// Color
	public static final double bulletSpeed = 10; // Linear speed increment

	/* Main constructor: */
	/* a = angle, in degrees, measured counter clockwise. */
	public Bullet(int posX, int posY, int atAngle, int speedMod,  Color cor)
	{
		this.x = posX;
		this.y = posY;
		this.attitude= atAngle;
		this.color = cor;
		this.speedModulus = speedMod;
	}

	/* Get linear position. It must be remarked that
	 * linear position setting is performed using  mover()
	 * method when tank opperates in normal conditions.*/
	public double [] getPosition()
	{
		double [] position = new double[2];
		position[0] = this.x;
		position[1] = this.y;
		return position;
	}

	public void setPosition(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	/* Returns the current bullet's attitude (orientation
	 * angle) */
	public double getAttitude()
	{
		return this.attitude;
	}

	/* Sets the current bullet's attitude (orientation angle) */
	public void setAttitude(double attitude)
	{
		this.attitude = attitude;
 	}

	/* Bullet's movement takes into account the fact that
	 * linear speed coordinate system is equivalent to
	 * position coordinate system rotated by 90degrees */
	public void move(){
		x = x + Math.sin(Math.toRadians(this.attitude)) * this.speedModulus;
		y = y - Math.cos(Math.toRadians(this.attitude)) * this.speedModulus;
	}

	public void draw(Graphics2D g2d){
		//Armazenamos o sistema de coordenadas original.
		AffineTransform antes = g2d.getTransform();
		//Criamos um sistema de coordenadas para o tanque.
		AffineTransform depois = new AffineTransform();
		depois.translate(x, y);
		depois.rotate(Math.toRadians(this.attitude));
		//Aplicamos o sistema de coordenadas.
		g2d.transform(depois);
		//Desenhamos o tanque. Primeiro o corpo
		g2d.setColor(this.color);
		g2d.fillRect(0, 0, 6, 10);
		//Aplicamos o sistema de coordenadas
		g2d.setTransform(antes);
	}

}