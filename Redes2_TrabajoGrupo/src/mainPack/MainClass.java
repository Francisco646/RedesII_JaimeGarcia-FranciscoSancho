package mainPack;

import javax.swing.SwingUtilities;

import clientPack.ClientGUIProgram;

public class MainClass {

	public static void main(String[] args) {
		SwingUtilities.invokeLater(ClientGUIProgram::new);
	}

}
