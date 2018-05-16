/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package MultiServer;

/**
 *
 * @author Abhishek Jaiswal
 */
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
 
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.JFrame;
/**
 *
 * @author Abhishek
 */
class ServerTest {
    public static void main(String[] args) {
        Server sally = new Server();
        sally.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        sally.startRunning();
    }
    
}



class Server extends JFrame {
    MultiClient.StrongAES crypt = new MultiClient.StrongAES();
    private JTextField userText; // this is the text where we type an is sent to other clients
    private JTextArea chatWindow;
    HashMap<String, String> hm = new HashMap<String, String>();
        
    
    
    private ServerSocket server;
    private Socket connectionSocket;
    int size = 10;
    
    static clientThread t[] = new clientThread[10];
    
    public Server() {
        super("Bucky Instant Manager");
        userText = new JTextField();
        userText.setEditable(true);
        
        hm.put("abhishek", "456");
        hm.put("a", "123");
        hm.put("abc", "212");
        hm.put("xyz", "123");
        
        userText.addActionListener(
            
            new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                   // sendMessage(event.getActionCommand()); // we create it to send message
                    String message = event.getActionCommand();
                    chatWindow.append("\nSERVER" + message);

                    for(int i=0; i<=9; i++) {
                        if (t[i]!=null)  {
                            
                            try {
                                t[i].output.writeObject("SERVER- "+ message); // displays our send text on client screen
                                t[i].output.flush();
                                
                                
                            }catch(IOException ioException) {
                                chatWindow.append("\n ERROR: cant send that message ");
                            }
                        }
                    }
                    userText.setText("");
                }
            }
        );
        
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(300, 150);
        setVisible(true);
    }
    
    public void startRunning() {
        try {
            server = new ServerSocket(6789, 100);
            
            while(true) {
                connectionSocket = server.accept();
                int i;
                for(i = 0; i < 10; i++) {
                    if(t[i] == null) {
                        (t[i] = new clientThread(connectionSocket, t )).start();
                        break;
                    }
                }
                
                if(i == 10) {
                    ObjectOutputStream output = new ObjectOutputStream(connectionSocket.getOutputStream());
                    output.writeObject("Server Busy");
                    output.close();
                    connectionSocket.close();
                }
            }
            
  
        }catch(IOException ioexception) {
            ioexception.printStackTrace();
        }
    }
    
    
//********************************************************************************
    class clientThread extends Thread {
 
        private ObjectOutputStream output;
        private ObjectInputStream input;
        Socket clientSocket = null;
        clientThread[] th;

        public clientThread(Socket clientSocket, clientThread[] t ){
            this.clientSocket=clientSocket;
            this.th=t;
            
        }
        public void setUpStream() throws IOException {
            output = new ObjectOutputStream(clientSocket.getOutputStream());
            output.flush();
            input = new ObjectInputStream(clientSocket.getInputStream());
            showMessage("Streams are setup");
        }   

        public void run() {
            String message;
            String prev = null;
            String loginuser;
            String loginpass;
            
            try {
                setUpStream();
                
                loginuser = (String) input.readObject();
                loginpass = (String) input.readObject();
                
                System.out.println(loginuser + " " + loginpass);
                /*Set set = hm.entrySet();
                Iterator it = set.iterator();*/
                
                if(hm.get(loginuser) == null) {
                    output.writeObject("\nYOU ARE NOT AUTHORISED ");
                    return;
                }
                if(!loginpass.equals(hm.get(loginuser))) {
                    output.writeObject("\nYOU ARE NOT AUTHORISED ");
                    return;
                }
                
                
                sendMessage("welcome new user\n");
                       

                
                while (true) {
                    
                   

                    //System.out.println("I enterd in to RUN BABY");
                    message = (String) input.readObject();
                    message = crypt.decrypt(message);
                    if(message == null)
                        break;
                    
                    if(message.equals(prev)) {
                        continue;
                    }
                    showMessage("\nClient " + message);
                    prev = message;
                    
                    for(int i=0; i<=9; i++) {
                        if (th[i] != null )  {
                            
                            
                            // System.out.println("I enterd in to RUN BABY");
                            th[i].output.writeObject(crypt.encrypt(message));
                            th[i].output.flush();
                           // System.out.println(message);
                            
                        }
                    }
                }
                
                
                for (int i=0; i<=9; i++) {
                     if (t[i]==this) 
                         t[i]=null;
                }
                closeCrap();

            }catch(EOFException eofexception) {
                showMessage("\n server closed the connection ");
            } catch (IOException ex) {
                Logger.getLogger(clientThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(clientThread.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }finally {
                closeCrap();
            }     
        }

        void closeCrap() {
            showMessage("\n Closing connections");
           // ableToType(false);

            try {
                output.close();
                input.close();
                clientSocket.close();
            }
            catch(IOException ioException) {
            }
        }
 
        void sendMessage(String message) {
            try {
                output.writeObject("SERVER- "+ message); // displays our send text on client screen
                output.flush();
                showMessage("\nSERVER " + message); // shows message sent by us on our screen

            }catch(IOException ioException) {
                chatWindow.append("\n ERROR: cant send that message ");
            }     
        }

        void showMessage(final String text) {
            SwingUtilities.invokeLater( // we are just updating a portion of gui so we use this method to
                    //just update the existing gui by appending the text in chat window
                new Runnable() {
                    public void run(){
                        chatWindow.append(text);
                    }
                }

            );
        } 

        void ableToType(final boolean tof) {
            SwingUtilities.invokeLater( // we are just updating a portion of gui so we use
                    //this method to just update the existing gui by appending the text in chat window
                new Runnable() {
                    public void run(){
                        userText.setEditable(tof);
                    }
                }

            );
        }



    }

    
}
