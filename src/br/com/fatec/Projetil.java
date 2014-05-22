package br.com.fatec;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

public class Projetil implements Runnable{
		private double x,y;
		private double angulo;
		private double velocidade;

		private Color cor;
		private boolean estaAtivo;
		
		public Projetil(double x, double y, double a, Color cor){
			this.x = x; this.y = y; this.angulo = a;
			this.cor = cor; velocidade = 40;
			this.estaAtivo = true;
		}
		
		public void aumentarVelocidade(){
			// limita a velocidade
			if(velocidade <= 20)
				velocidade += 0.5;
			System.out.println("Velocidade: " + velocidade);
		}
		
		public void girarHorario(int a){
			angulo += a;
		}
		
		public void girarAntiHorario(int a){
			angulo -= a;
		}
		
		public void mover(){
			x = x + Math.sin(Math.toRadians(angulo)) * velocidade;
			y = y - Math.cos(Math.toRadians(angulo)) * velocidade;
			
			System.out.println("X: " + x + " - Y: " + y + " Angulo: " +  this.getAngulo());
		}
		
		public void setEstaAtivo(boolean estaAtivo){
			this.estaAtivo = estaAtivo;
		}
		
		public boolean getEstaAtivo(){
			return this.estaAtivo;
		}
		
		public double getVelocidade() {
			return velocidade;
		}

		public void setVelocidade(double velocidade) {
			this.velocidade = velocidade;
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
			
			//O canh�o
			g2d.fillOval(-3, -25, 10, 10);
			g2d.setColor(cor);
			//g2d.drawOval(-3, -25, 10, 10);
			 //g2d.fillRect(-3, -25, 6, 25);
		     //g2d.setColor(cor);
     		 //g2d.drawRect(-3, -25, 6, 25);
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

		public void reverso() {
			x = x + Math.sin(Math.toRadians(angulo)) * (velocidade * -1);
			y = y - Math.cos(Math.toRadians(angulo)) * (velocidade * -1);	
			
			System.out.println("X: " + x + " - Y: " + y + " Angulo: " +  this.getAngulo());
		}

		public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getY() {
			return y;
		}

		public void setY(double y) {
			this.y = y;
		}

		public double getAngulo() {
			return angulo;
		}

		public void setAngulo(double angulo) {
			this.angulo = angulo;
		}
		
		public double nextX(boolean reverso) {
			return x + Math.sin(Math.toRadians(angulo)) * (velocidade * (reverso? -1 : 1));
		}

		public double nextY(boolean reverso) {
			return y - Math.cos(Math.toRadians(angulo)) * (velocidade * (reverso? -1 : 1));
		}

		public void mudarSentido(){
			if(this.getAngulo() % 360 <= 0)
				this.setAngulo(this.getAngulo() - 5);
			else
				this.setAngulo(this.getAngulo() + 5);
		}

		@Override
		public void run() {
			mover();
		}
}
