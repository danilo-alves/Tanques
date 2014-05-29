package br.com.fatec;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

public class Tanque {
	private double x,y; 		// Position
	private double angulo;		// Attitude
	private double velocidade;	// Linear speed
	private Color cor;			// Color
	private boolean estaAtivo;	// Current selection status
	public static final double deltaV = 1; // Linear speed increment

	/* Main constructor: */
	/* a = angle, in degrees, measured counter clockwise. */
	public Tanque(int posX, int posY, int atAngle, int speedMod,  Color cor){
		this.x = posX;
		this.y = posY;
		this.angulo = atAngle;
		this.cor = cor;
		this.velocidade = speedMod;
		this.estaAtivo = false;
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

	/* Returns the current tank's attitude (orientation
	 * angle) */
	public double getAttitude()
	{
		return this.angulo;
	}

	/* Sets the current tank's attitude (orientation angle) */
	public void setAttitude(double attitude)
	{
		this.angulo = attitude;
 	}

	/* Linear speed modulus increment */
	public void aumentarVelocidade(){
		this.velocidade = velocidade + deltaV;
	}

	/* Linear speed modulus decrement */
	public void diminuirVelocidade()
	{
		this.velocidade = velocidade - deltaV;
		if(this.velocidade < 0) //Avoids negative speed modulus
		{
			this.velocidade = 0;
		}
	}
	public void girarHorario(int a){
		angulo += a;
	}
	public void girarAntiHorario(int a){
		angulo -= a;
	}

	/* Tank's movement takes into account the fact that
	 * linear speed coordinate system is equivalent to
	 * position coordinate system rotated by 90degrees */
	public void mover(){
		x = x + Math.sin(Math.toRadians(angulo)) * this.velocidade;
		y = y - Math.cos(Math.toRadians(angulo)) * this.velocidade;
	}
	public void setEstaAtivo(boolean estaAtivo){
		this.estaAtivo = estaAtivo;
	}

	public boolean getActivationStatus()
	{
		return this.estaAtivo;
	}

	public void draw(Graphics2D g2d){
		//Armazenamos o sistema de coordenadas original.
		AffineTransform antes = g2d.getTransform();
		//Criamos um sistema de coordenadas para o tanque.
		AffineTransform depois = new AffineTransform();
		depois.translate(x, y);
		depois.rotate(Math.toRadians(angulo));
		//Aplicamos o sistema de coordenadas.
		g2d.transform(depois);
		//Desenhamos o tanque. Primeiro o corpo
		g2d.setColor(cor);
		g2d.fillRect(-10, -12, 20, 24);
		//Agora as esteiras
		for(int i = -12; i <= 8; i += 4){
			g2d.setColor(Color.LIGHT_GRAY);
			g2d.fillRect(-15, i, 5, 4);
			g2d.fillRect(10, i, 5, 4);
			g2d.setColor(Color.BLACK);
			g2d.fillRect(-15, i, 5, 4);
			g2d.fillRect(10, i, 5, 4);
		}
		//O canh�o
		g2d.setColor(Color.LIGHT_GRAY);
		g2d.fillRect(-3, -25, 6, 25);
		g2d.setColor(cor);
		g2d.drawRect(-3, -25, 6, 25);
		//Se o tanque estiver ativo
		//Desenhamos uma margem
		if(estaAtivo){
			g2d.setColor(new Color(120,120,120));
			Stroke linha = g2d.getStroke();
			g2d.setStroke(new BasicStroke(1f,BasicStroke.CAP_ROUND,
					BasicStroke.JOIN_ROUND,0,
					new float[]{8},0));
			g2d.drawRect(-24, -32, 48, 55);
			g2d.setStroke(linha);
		}
		//Aplicamos o sistema de coordenadas
		g2d.setTransform(antes);
	}

	public Shape getRectEnvolvente(){
		AffineTransform at = new AffineTransform();
		at.translate(x,y);
		at.rotate(Math.toRadians(angulo));
		Rectangle rect = new Rectangle(-24,-32,48,55);
		return at.createTransformedShape(rect);
	}
}