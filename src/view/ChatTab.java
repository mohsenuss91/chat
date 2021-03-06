package view;

import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.rmi.RemoteException;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import controller.proxy.RemoteServerController;
import model.Chatroom;
import model.Message;
import model.User;

import javax.swing.JList;
import javax.swing.JLabel;

public class ChatTab extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -631964749795742487L;
	private JTextField textField;
	private JTextPane textPane;
	private DefaultListModel<String> users;
	
	public ChatTab(final Chatroom chatroom, final RemoteServerController stub) {
		this.setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(10, 333, 343, 22);
		this.add(textField);
		textField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textField.setColumns(10);
		textField.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	int key=e.getKeyCode();
                if(key==KeyEvent.VK_ENTER)
                { 
                    try {
						stub.sendMessage(chatroom, textField.getText());
						textField.setText("");
					} catch (RemoteException e1) {
						e1.printStackTrace();
					}                  
                }
            }
        });
		
		textPane = new JTextPane();
		textPane.setBounds(10, 6, 343, 316);
		this.add(textPane);
		
		users = new DefaultListModel<String>();
		JList<String> list = new JList<String>(users);
		list.setBounds(363, 30, 77, 292);
		add(list);
		
		JLabel lblUtilisateurs = new JLabel("Utilisateurs :");
		lblUtilisateurs.setBounds(363, 11, 77, 14);
		add(lblUtilisateurs);
	}

	public void addMessage(String author, String content) throws BadLocationException {
		StyledDocument doc = (StyledDocument)textPane.getDocument();
		Style style = doc.addStyle("Style", null);
		
        StyleConstants.setBold(style, true);
        doc.insertString(doc.getLength(), "[" + author + "] ", style);
		
        StyleConstants.setBold(style, false);   
		doc.insertString(doc.getLength(), content + "\n", style);
		
		textPane.setCaretPosition(doc.getLength());
	}

	public void setMessages(Chatroom chatroom) {
		try {
			textPane.getDocument().remove(0, textPane.getDocument().getLength());
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		
		for (Message m : chatroom.getMessages()) {
			try {
				addMessage(m.getAuthor().getLogin(), m.getContent());
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void setUsers(List<User> list) {
		this.users.removeAllElements();
		for(User u : list) {
			this.users.addElement(u.getLogin());
		}
	}

}
