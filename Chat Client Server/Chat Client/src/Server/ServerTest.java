/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;
import javax.swing.JFrame;
/**
 *
 * @author Abhishek
 */
public class ServerTest {
    public static void main(String[] args) {
        Server ServerAbhishek = new Server();
        ServerAbhishek.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ServerAbhishek.startRunning();
    }
    
}
