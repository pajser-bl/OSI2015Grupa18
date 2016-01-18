/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistem;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pajser
 */
public class KupacKlijent extends Thread{
    public static final int PORT=9000;
    private InetAddress iAddress;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner cIn;
    private Socket sock;
    private boolean logInCheck;
    private boolean logOnCheck;
    private String _ime;
    
    public String getIme(){return _ime;}
    public KupacKlijent(){
        try {
            this.logInCheck=false;
            this.logOnCheck=false;
            this.iAddress=InetAddress.getByName("127.0.0.1");
            this.sock=new Socket(iAddress,PORT);
            this.in=new BufferedReader(new InputStreamReader(sock.getInputStream()));
            this.out=new PrintWriter((new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))),true);
            this.cIn=new Scanner(System.in);
        } catch (UnknownHostException ex) {
            Logger.getLogger(KupacKlijent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(KupacKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public boolean logInCheck(){
        try {
            String ime;
            ime=cIn.nextLine();
            out.write(ime);
            if(in.read()==1){
                _ime=ime;
                return true;
            }else
                return false;
        } catch (IOException ex) {
            Logger.getLogger(KupacKlijent.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
    @Override
    public void run () {
        while(!logOnCheck){
            System.out.printf("Da li ste privilegovani kupac?[Da/Ne]");
            if(cIn.nextLine().toLowerCase().equals("da")){
                if(logInCheck()){
                    System.out.println("Autorizacija uspjesna.");
                    System.out.println("Dobrodosli "+_ime+" .");
                    logInCheck=true;
                    logOnCheck=true;
                }
            }else if(cIn.nextLine().toLowerCase().equals("ne")){
                out.write("0");
                logOnCheck=true;
            }
        }
    }
    public static void main(String args[]){
        KupacKlijent kk=new KupacKlijent();
        kk.start();
    }
}
