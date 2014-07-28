package gui;

import java.awt.Color;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;

public class Client {

	private JFrame frame;
	private JTextPane gameText;
	private DefaultStyledDocument document;

	/**
	 * Create the application.
	 * 
	 * @throws IOException
	 */
	public Client() throws IOException {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws IOException
	 */
	private void initialize() throws IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 600, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		document = new DefaultStyledDocument();
		gameText = new JTextPane(document);
		 gameText.setBackground(Color.WHITE);
		gameText.setBounds(0, 0, 600, 378);
		gameText.setEditable(false);
		gameText.setFocusable(true);
		frame.getContentPane().add(gameText);

		frame.setResizable(false);
	}
	
	public void showText(){
		gameText.setVisible(true);
	}
	
	public void hideText(){
		gameText.setVisible(false);
	}

	public void clear() {
		gameText.setText("");
	}
	
	public void addText(String msg, Color c){
		StyleContext context = new StyleContext();
		// build a style
		Style style = context.addStyle("test", null);
		// set some style properties
		StyleConstants.setForeground(style, c);
		// add some data to the document
		try {
			document.insertString(0, msg, style);
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}

	public void addText(char c) {
		gameText.setText(gameText.getText() + c);
	}

	public void addText(String c) {
		gameText.setText(gameText.getText() + c);
	}

	public void addText(int c) {
		gameText.setText(gameText.getText() + c);
	}

	public void newLine() {
		addText("\n");
	}

	public JTextPane getGT() {
		return gameText;
	}

	public JFrame getFrame() {
		return frame;
	}

	public String gsText() {
		return gameText.getText();
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

}
