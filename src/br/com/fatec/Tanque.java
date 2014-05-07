package br.com.fatec;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;

public class Tanque {
	private double x,y;
	private double angulo;
	private double velocidade;
	private Color cor;
	private boolean estaAtivo;
	public Tanque(int x, int y, int a, Color cor){
		this.x = x; this.y = y; this.angulo = 90-a;
		this.cor = cor; velocidade = 10;
		this.estaAtivo = false;
	}
	public void aumentarVelocidade(){
		velocidade++;
	}
	public void girarHorario(int a){
		angulo += a;
	}
	public void girarAntiHorario(int a){
		angulo -= a;
	}
	public void mover(){
		x = x + Math.sin(Math.toRadians(angulo)) * velocidade;
		y = y - Math.sin(Math.toRadians(angulo)) * velocidade;
	}
	public void setEstaAtivo(boolean estaAtivo){
		this.estaAtivo = estaAtivo;
	}
	public boolean getEstaAtivo(){
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
	public void atirar() {
		// TODO Auto-generated method stub
		System.out.println("Tanque atirou!");
	}
}
