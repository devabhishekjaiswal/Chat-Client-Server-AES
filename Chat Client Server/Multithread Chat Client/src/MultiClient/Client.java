/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package MultiClient;

/**
 *
 * @author Abhishek Jaiswal
 */
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

public class Client extends JFrame implements Runnable{
    
    private javax.swing.JTextArea chatWindow;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton login;
    private javax.swing.JLabel passlabel;
    private javax.swing.JTextField password;
    private javax.swing.JButton send;
    private javax.swing.JTextField userText;
    private javax.swing.JLabel userlabel;
    private javax.swing.JTextField username;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;
    Thread t = null;
//constructor
    StrongAES crypt = new StrongAES();
    
    
    
    
    public Client(String host){
        super("Client");
        
        
       
        serverIP = host; // 127.0.0.1 
        password = new javax.swing.JTextField();
        userlabel = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        chatWindow = new javax.swing.JTextArea();
        passlabel = new javax.swing.JLabel();
        username = new javax.swing.JTextField();
        send = new javax.swing.JButton();
        login = new javax.swing.JButton();
        userText = new javax.swing.JTextField();
      
        password.setText("Enter your Password");
        userlabel.setText("    USERNAME");

        chatWindow.setColumns(20);
        chatWindow.setRows(5);
        jScrollPane1.setViewportView(chatWindow);
        passlabel.setText("   PASSWORD");

        username.setText("Enter your username.");
        
        send.setText("SEND");

        login.setText("LOG IN");

        userText.setText("What's in your mind?");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(passlabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(userlabel, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(27, 27, 27))
            .addGroup(layout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(send, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(login)
                .addContainerGap(75, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userText)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(userlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(username, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(passlabel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(password, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(send)
                    .addComponent(login))
                .addGap(7, 7, 7)
                .addComponent(userText, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        
        
        
        
        pack();

    }
        //connect to server
    public void startRunning(){
        try{
            connectToServer();
            setupStreams();
        
        }catch(EOFException eofException){
            showMessage("\n Client terminated the connection");
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
        
        
    }
        //connect to server
    private void connectToServer() throws IOException{
        showMessage("Attempting connection... \n");
        connection = new Socket(InetAddress.getByName(serverIP), 6789);
        showMessage("Connection Established! Connected to: " + connection.getInetAddress().getHostName());
    }
        //set up streams
    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n The streams are now set up! \n");
    }
        //while chatting with server
    
    public void run() {
        
        login.addActionListener( new ActionListener(){
            public void actionPerformed(ActionEvent event){
                    try{
                        output.writeObject(username.getText());
                        output.flush();
                        
                        output.writeObject(password.getText());
                        output.flush();
                    }catch(IOException ioException){
                           chatWindow.append("\n Oops! Something went wrong!");
                    }   
                          
                }
        });
        
        
       
        
        do{
            try{
                
                userText.addActionListener(
                    new ActionListener(){
                        public void actionPerformed(ActionEvent event){
                            sendMessage(event.getActionCommand());
                          
                        }
                    }
                );
                
               // System.out.println("I entered into run");
                message =(String)  input.readObject();
                
                try {
                    //  System.out.println(message);
                    showMessage("\n" + crypt.decrypt(message));
                } catch (Exception ex) {
                    Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
                }
            }catch(ClassNotFoundException classNotFoundException){
                showMessage("Unknown data received!");
            }catch(IOException e) {
                
            }
        }while(!message.equals("SERVER - END"));
        closeConnection();
    }
    
        //Close connection
    private void closeConnection(){
        showMessage("\n Closing the connection!");
        
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }
        //send message to server
    private void sendMessage(String message){
        try{
            try {
                output.writeObject(crypt.encrypt("CLIENT send - " + message));
            } catch (Exception ex) {
                Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
            }
            output.flush();
           // showMessage("\nCLIENT - " + message);
        }catch(IOException ioException){
             chatWindow.append("\n Oops! Something went wrong!");
        }
        
        
        
    }
        //update chat window
    private void showMessage(final String message){
        
        chatWindow.append( message);
    }

}
