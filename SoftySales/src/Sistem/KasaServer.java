/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sistem;

import Podaci.Proizvod;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pajser
 */
public class KasaServer{
    private String _cashier;
    
    
    

    private Scanner cIn;
//    podaci o radnicima i administratoru
    private static HashMap<String,String> _listaRadnika;
    private static ArrayList<String> _listaKupaca;
    private static ArrayList<HashMap<Proizvod,Integer>> _listaZahtjeva;
    public String getCashier(){return _cashier;}
    
    public KasaServer(){
        cIn=new Scanner(System.in);
        
//        _listaKupaca=new ArrayList();
//        SistemProdaje.saveKupci(_listaKupaca);
//        _listaKupaca=SistemProdaje.readKupci();
        
        _listaZahtjeva=new ArrayList();
        _listaRadnika=SistemProdaje.read();
        _listaKupaca=SistemProdaje.readKupci();
        //SistemProdaje.save(_listaRadnika);
//        inicijalizacija fajlova
        SistemProdaje.fajlSistem();
//        inicijalizacija threda
        KasaThread kasaThread=new KasaThread();
        kasaThread.start();
//        login i rad servera
        System.out.println("Server online, enter your credentials.");
        boolean loginCheck=false;
        boolean adminCheck=false;
        while(!loginCheck){
            System.out.printf("Username: ");
            String username=cIn.nextLine();
            System.out.printf("Password:");
            String password=cIn.nextLine();
            String loginInfo=username+"#"+password;
            switch(SistemProdaje.loginCheck(loginInfo,_listaRadnika)){
                case "WUSER":{
                    System.out.println("Nepostojeci korisnik.");
                    break;
                }
                case "WPASS":{
                    System.out.println("Pogresna sifra.");
                    break;
                }
                case "WORKER":{
                    _cashier=username;
                    System.out.println("Dobrodosli "+_cashier+".");
                    loginCheck=true;
                    break;
                }
                case "ADMIN":{
                    adminCheck=true;
                    loginCheck=true;
                    break;
                }
                case "EXIT":{
                    System.exit(0);
                }
            }
        }
        if(adminCheck){
            SistemProdaje.adminMeni(_listaRadnika);
        }
        else{
            SistemProdaje.radnikMeni(_cashier,_listaKupaca,_listaZahtjeva);
        }
    }
    public static void main(String args[]){
        System.out.println("KasaServer is starting...");
        KasaServer kasaServer = new KasaServer();
        System.out.println("Server is shuting down...");
        System.exit(0);
    }
    public static class KasaThread extends Thread{
        public boolean work=true;
        //    serverski podaci
        public static final int SERVER_PORT=9001;
        private ServerSocket sSocket;
        public KasaThread(){
            try {
                sSocket=new ServerSocket(SERVER_PORT);
            } catch (IOException ex) {
                Logger.getLogger(KasaServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        @Override
        public void run(){
            try {
                while(work){
                    Socket sock=sSocket.accept();
                    KasaSistemThread kasaSistemThread = new KasaSistemThread(sock);
                }
                sSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(KasaServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public static class KasaSistemThread extends Thread{
        ObjectInputStream oInS;
        HashMap<Proizvod,Integer>zahtjev;
        public KasaSistemThread(Socket sock){
            try {
                oInS=new ObjectInputStream(sock.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(KasaServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            start();
        }
        @Override
        public void run(){
            try {
                if((zahtjev=(HashMap<Proizvod,Integer>)oInS.readObject())!=null){
                    KasaServer._listaZahtjeva.add(zahtjev);
                    oInS.close();
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(KasaServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
}
