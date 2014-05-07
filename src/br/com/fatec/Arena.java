package br.com.fatec;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
		implements MouseListener, ActionListener, KeyListener{
	private int largura,altura;
	private HashSet<Tanque> tanques;
	private Timer contador;
	public Arena(int largura,int altura){
		this.largura = largura;
		this.altura = altura;
		tanques = new HashSet<Tanque>();
		addMouseListener(this);
		addKeyListener(this);
		contador = new Timer(500,this);
		contador.start();
	}
	public void adicionaTanque(Tanque t){
		tanques.add(t);
	}
	public Dimension getMaximumSize(){
		return getPreferredSize();
	}
	public Dimension getMinimumSize(){
		return getPreferredSize();
	}
	public Dimension getPreferredSize(){
		return new Dimension(largura,altura);
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
	}
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
	public void actionPerformed(ActionEvent e){
		for(Tanque t:tanques)
			t.mover();
		repaint();
	}

	public static void main(String args[]){
		Arena arena = new Arena(600,400);
		arena.adicionaTanque(new Tanque(100,200,0,Color.BLUE));
		arena.adicionaTanque(new Tanque(200,200,45,Color.RED));
		arena.adicionaTanque(new Tanque(470,360,90,Color.GREEN));
		arena.adicionaTanque(new Tanque(450,50,157,Color.YELLOW));
		JFrame janela = new JFrame("Tanques");
		janela.getContentPane().add(arena);
		janela.pack();
		janela.setVisible(true);
		janela.setDefaultCloseOperation(3);
	}
	
	
	@Override
	public void keyPressed(KeyEvent e) {
		System.out.println(e.getKeyChar());
	}
	
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		for(Tanque t:tanques){
			boolean clicado = t.getEstaAtivo();
			if (clicado){
				switch(e.getKeyCode()){
					case KeyEvent.VK_0: t.atirar(); break;
				}
				break;
			}
		}
		repaint();
	}
}