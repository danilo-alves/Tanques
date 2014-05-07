package br.com.fatec;

import java.awt.Color;

import javax.swing.JFrame;

public class Aplicacao {

	public static void main(String[] args) {
		Arena arena = new Arena(600, 400);
		arena.adicionaTanque(new Tanque(100, 200, 0, Color.BLUE));
		arena.adicionaTanque(new Tanque(200, 200, 45, Color.RED));
		arena.adicionaTanque(new Tanque(470, 360, 90, Color.GREEN));
		arena.adicionaTanque(new Tanque(450, 50, 157, Color.YELLOW));

		JFrame janela = new JFrame("Tanques");
		janela.getContentPane().add(arena);
		janela.pack();
		janela.setVisible(true);
		janela.setDefaultCloseOperation(3);
	}
}
