package Sistem;

import Podaci.Proizvod;
import Podaci.Racun;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
import java.util.ArrayList;
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

    private Scanner cIn;
//    podaci o radnicima i administratoru
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
            String password = cIn.nextLine();
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
            SistemProdaje.radnikMeni(_cashier, _listaKupaca, _listaZahtjeva, _inventar);
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

        BufferedReader in;
        PrintWriter out;
        ObjectInputStream oInS;
        ObjectOutputStream oOutS;
        Socket sock;
        HashMap<Proizvod, Integer> zahtjev;

        public KasaSistemThread(Socket sock) {
            try {
                this.sock = sock;
                this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
                this.out = new PrintWriter((new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))), true);
            } catch (IOException ex) {
                Logger.getLogger(KasaServer.class.getName()).log(Level.SEVERE, null, ex);
            }
            start();
        }

        @Override
        public void run() {
            boolean end = false;
            String msg;
            String name=null;
            try {
                while (!end) {
                    msg = in.readLine();
                    if (_listaKupaca.contains(msg)) {
                        out.println("1");
                        end = true;
                        name=msg;
                    } else {
                        out.println("0");
                    }
                }
                System.out.printf("AAaasdasd");
                //in.close();
                //out.close();
                this.oInS = new ObjectInputStream(sock.getInputStream());
                this.oOutS=new ObjectOutputStream(sock.getOutputStream());
                oOutS.writeObject(_inventar);
                System.out.printf("AAa");
                if ((zahtjev = (HashMap<Proizvod, Integer>) oInS.readObject()) != null) {
                    Racun racun=new Racun(getCashier(),name);
                    for(Proizvod p:zahtjev.keySet()){
                        racun.add(p, zahtjev.get(p));
                        KasaServer._listaZahtjeva.add(racun);
                    }
                    oInS.close();
                    oOutS.writeObject(racun);
                    oOutS.close();
                }
            } catch (IOException | ClassNotFoundException ex) {
                Logger.getLogger(KasaServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
}
