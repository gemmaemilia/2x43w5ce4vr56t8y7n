package com.grid.de.main;

import java.io.IOException;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;

public class TermWindow implements Runnable {

	Terminal term = null;
	Screen screen = null;
	TextGraphics tg = null;
	
	String output;
	
	public TermWindow() throws IOException {
		term = new DefaultTerminalFactory().createTerminal();
		screen = new TerminalScreen(term);

		tg = screen.newTextGraphics();
		
		screen.doResizeIfNecessary();
		
	}
	
	public void SetOutput(String output) {
		this.output = output;
	}
	
	public void DisplayOutput() {
		
		DisplayOutputBox();
		
		
		try {
			screen.refresh();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private void DisplayOutputBox() {
		tg.putString(2, 1, "Output Window");
		tg.drawRectangle(new TerminalPosition(2, 2), new TerminalSize(14, 10),new TextCharacter(TextCharacter.DEFAULT_CHARACTER));
		screen.doResizeIfNecessary();
	}
	
	public void run() {
		while(true) {
			DisplayOutput();
		}
	}
	
}
