package Sistem;

import Podaci.Proizvod;
import Podaci.Racun;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pajser
 */
public class KasaServer {

    private static String _cashier;
    private String _startTime;
    private Scanner cIn;
//    podaci o radnicima i administratoru
    private static String _statistikaKoristenja;
    private static HashMap<String, String> _listaRadnika;
    private static HashMap<Proizvod, Integer> _inventar;
    private static ArrayList<String> _listaKupaca;
    private static ArrayList<Racun> _listaZahtjeva;

    public static String getCashier() {
        return _cashier;
    }

    public KasaServer() {
        cIn = new Scanner(System.in);

//      inicijalizacija fajlova
        SistemProdaje.fajlSistem();

        _listaKupaca = SistemProdaje.readKupci();
        _listaRadnika = SistemProdaje.readRadnici();
        _statistikaKoristenja=SistemProdaje.readStatistikaKoristenja();
        _inventar = SistemProdaje.readInventar();
        _listaZahtjeva = new ArrayList();
        
//      inicijalizacija threda
        KasaThread kasaThread = new KasaThread();
        kasaThread.start();

//        login i rad servera
        System.out.println("Aplikacija uspjesno pokrenuta.\nUnesite korisnicke podatke...");
        boolean loginCheck = false;
        boolean adminCheck = false;
        while (!loginCheck) {
            System.out.printf("Korisnicko ime: ");
            String username = cIn.nextLine();
            System.out.printf("Sifra:");
            Console console = System.console();
            String password;
            if (console != null)
                password = String.valueOf(console.readPassword());
            else
                password = cIn.nextLine();
            
            switch (SistemProdaje.loginCheck(username, password, _listaRadnika)) {
                case -1: {
                    System.out.println("Pogresni podaci.");
                    break;
                }
                case 1: {
                    _cashier = username;
                    System.out.println("Dobrodosli " + username + ".");
                    loginCheck = true;
                    break;
                }
                case 2: {
                    System.out.println("Dobrodosli " + username + ".");
                    adminCheck = true;
                    loginCheck = true;
                    break;
                }
                case 0: {
                    System.exit(0);
                }
            }
        }
        if (adminCheck) {
            SistemProdaje.adminMeni(_listaRadnika);
        } else {
            _startTime=SistemProdaje.time();
            SistemProdaje.radnikMeni(_cashier, _listaKupaca, _listaZahtjeva, _inventar,_startTime);
            
        }
    }

    public static void main(String args[]) {
        System.out.println("KasaServer is starting...");
        KasaServer kasaServer = new KasaServer();
        System.out.println("Server is shuting down...");
        System.exit(0);
    }

    public static class KasaThread extends Thread {

        public boolean work = true;
        //    serverski podaci
        public static final int SERVER_PORT = 9001;
        private ServerSocket sSocket;

        public KasaThread() {
            try {
                sSocket = new ServerSocket(SERVER_PORT);
            } catch (IOException ex) {
                Logger.getLogger(KasaServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        @Override
        public void run() {
            try {
                while (work) {
                    Socket sock = sSocket.accept();
                    KasaSistemThread kasaSistemThread = new KasaSistemThread(sock);
                }
                sSocket.close();
            } catch (IOException ex) {
                Logger.getLogger(KasaServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static class KasaSistemThread extends Thread {

        ObjectInputStream in;
        ObjectOutputStream out;
        Socket sock;
        HashMap<Proizvod, Integer> zahtjev;

        public KasaSistemThread(Socket sock) {
            try {
                this.sock = sock;
                this.in = new ObjectInputStream(sock.getInputStream());
                this.out = new ObjectOutputStream(sock.getOutputStream());
            } catch (IOException ex) {
                Logger.getLogger(KasaServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            start();
        }

        @Override
        public void run() {
            boolean end = false;
            String msg;
            String name = null;
            try {
                while (!end) {
                    msg = (String) in.readObject();
                    if (_listaKupaca.contains(msg)) {
                        out.writeObject("ACCEPTED");
                        end = true;
                        name = msg;
                    } else if (msg.toLowerCase().equals("exit")) {
                        out.writeObject("EXIT");
                        end = true;
                        name = "no_name";
                    }else 
                        out.writeObject("DENIED");
                }

//                salje inventar
                out.writeObject(_inventar);
                HashMap<Proizvod, Integer> zahtjev;
                
//              prima zahtjev
                String t;
                t=(String)in.readObject();
                if(t.equals("kupovina")){
                    out.writeObject("accepted");
                    zahtjev = (HashMap<Proizvod, Integer>) in.readObject();
                    Racun racun = new Racun(getCashier(), name);
                    int temp=0;
                    for (Proizvod p : zahtjev.keySet()) {
                        racun.add(p, zahtjev.get(p));
                        temp=(_inventar.get(p)-zahtjev.get(p));
                        _inventar.remove(p);
                        _inventar.put(p, temp);
                    }
                    SistemProdaje.saveInventar(_inventar);
                    _inventar=SistemProdaje.readInventar();
//                  salje racun
                    KasaServer._listaZahtjeva.add(racun);
                    out.writeObject(racun);
                }
                out.close();
                in.close();
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(KasaServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

}
