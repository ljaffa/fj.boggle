package fj.boggle;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class BoggleGui extends JFrame{
	
	private JLabel timerLabel;
	
	public BoggleGui(){
		
	
	setTitle("BOGGLE");
	setSize(600, 700);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setLocationRelativeTo(null);
	setResizable(false);
	
	Container container = getContentPane();
	container.setLayout(new BorderLayout());
	
	JPanel boardPanel = new JPanel();
	
	timerLabel = new JLabel();
	
	
	
	}

}
