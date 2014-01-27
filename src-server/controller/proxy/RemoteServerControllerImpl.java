package controller.proxy;

import java.rmi.RemoteException;

import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import controller.ChatroomController;
import controller.DatabaseController;
import controller.MainController;

import model.Chatroom;
import model.Message;
import model.User;

public class RemoteServerControllerImpl extends UnicastRemoteObject implements RemoteServerController {

    /**
	 * 
	 */
	private static final long serialVersionUID = -3441443474490088855L;
    User user;
    ROLE role;
    private ChatroomController chatroom;
    private RemoteClientController stub;
    
    public RemoteServerControllerImpl() throws RemoteException {
        DatabaseController.getConnection();
        chatroom = ChatroomController.getInstance();
        user = null;
        role = ROLE.ANONYMOUS;
    }
    
    public void setUser(User u) {
        user = u;
    }
    
    @Override
    public User getUser() {
        return user;
    }
    
    public void setGranted(ROLE r) {
        role = r;
    }
    
    @Override
    public ROLE getGranted() {
        return role;
    }
    
    @Override
    public void setClientStub(RemoteClientController stub) throws RemoteException {
    	this.stub = stub;
    }

	@Override
	public RemoteServerController login(String user, String pass)
			throws RemoteException {
		return MainController.getInstance().getProxy(user, pass);
	}
	

	@Override
	public RemoteServerController logout() throws RemoteException {
		return MainController.getInstance().getProxy("","");
	}

	@Override
	public List<User> getUserList() throws RemoteException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Chatroom joinChatroom(String name) throws RemoteException {
		stub.sendPublicMessage(user.getLogin() + "viens de rejoindre la chatroom" + name);
		return chatroom.joinChatroom(user, name);
	}

	@Override
	public List<Chatroom> getChatroomList() throws RemoteException {
		return chatroom.getRooms();
	}

	@Override
	public void sendPublicMessage(String message) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendMessage(Chatroom chat, String message)
			throws RemoteException {
		stub.updateChatroom(chatroom.sendMessage(chat, new Message(user, message)));
		
	}

}
