/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package testquestion6;

import java.io.*;
import java.net.*;


/**
 *
 * @author jvinson
 */

class summationThread extends Thread{
    Socket clientSocket;
    summationThread(Socket cs){
        clientSocket = cs;
    }
    public void run(){
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            DataOutputStream dos = new DataOutputStream(clientSocket.getOutputStream());
            String s;
            String errMsg = "Unable to resolve host ";
            boolean quit = false;
        do {
            String msg = "";
            s = br.readLine();
            int len = s.length();
            if (s.equals("exit")) {
                quit = true;
            }
            
            for (int i = 0; i < len; i++) {
                msg = msg + s.charAt(i);
                //dos.write((byte) s.charAt(i));
            }
            
            InetAddress address = null;
            try {
                address = InetAddress.getByName(msg);
            } catch (UnknownHostException e) {
                errMsg += "<" + msg + ">";
                System.out.println(errMsg);
                for (int i = 0; i < errMsg.length(); i++){
                    dos.write((byte) errMsg.charAt(i));
                }
                e.printStackTrace();
            }
            String result = (address.getHostName() + " = " + address.getHostAddress());
            
            int resLen = result.length();
            
            for (int i = 0; i < resLen; i++){
                dos.write((byte) result.charAt(i));
            }
 
            System.out.println("From client :" + msg);
            dos.write(13);
            dos.write(10);
            dos.flush();
            Thread.sleep(200);
        } while (!quit);
        clientSocket.close();
            

        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}


public class TestQuestion6 {

    public static void main(String[] args) {
        boolean state = true;
        try{
            int serverPort = 6052;
            ServerSocket calcServer = new ServerSocket(serverPort);
            while(true){
                Socket clientSocket = calcServer.accept();
                summationThread thread = new summationThread(clientSocket);
                thread.start();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
    
}


