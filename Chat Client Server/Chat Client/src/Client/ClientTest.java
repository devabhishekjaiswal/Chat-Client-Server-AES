/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import javax.swing.JFrame;
public class ClientTest {
    public static void main(String[] args) {
        AbhishekClient clientAbhishek;
        clientAbhishek = new AbhishekClient("127.0.0.1");
        clientAbhishek.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        clientAbhishek.startRunning();
    }
}