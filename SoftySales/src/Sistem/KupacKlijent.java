package Sistem;

import Podaci.Proizvod;
import Podaci.Racun;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KupacKlijent extends Thread {

    public static final int PORT = 9001;
    public static final int IPORT = 9002;
    private InetAddress iAddress;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Scanner cIn;
    private Socket sock;
    private Socket objectSock;
    private boolean logInCheck;
    private boolean logOnCheck;
    private String _ime;
    private HashMap<Proizvod, Integer> inventar;
    private HashMap<Proizvod, Integer> korpa;

    public String getIme() {
        return _ime;
    }

    public void setIme(String ime) {
        _ime = ime;
    }

    public KupacKlijent() {
        try {
            this.logInCheck = false;
            this.korpa = new HashMap();
            this.iAddress = InetAddress.getByName("127.0.0.1");
            this.sock = new Socket(iAddress, PORT);
            this.out = new ObjectOutputStream(sock.getOutputStream());
            this.in = new ObjectInputStream(sock.getInputStream());
            this.cIn = new Scanner(System.in);
        } catch (UnknownHostException ex) {
            Logger.getLogger(KupacKlijent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(KupacKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void logInCheck() {
        try {
            while (!logInCheck) {
                System.out.printf("Da li ste privilegovani kupac? [Da/Ne]: ");
                String option = cIn.nextLine();
                if (option.toLowerCase().equals("da")) {
                    while (!logOnCheck) {
                        System.out.printf("Unesite vase ime:");
                        String ime = cIn.nextLine();
                        out.writeObject(ime);
                        String rec = (String) in.readObject();
                        if (rec.equals("ACCEPTED")) {
                            _ime = ime;
                            logOnCheck = true;
                            logInCheck = true;
                        } else if (rec.equals("DENIED")) {
                            System.out.println("Niste na spisku.");
                        } else if (rec.equals("EXIT")) {
                            _ime = "no_name";
                            logOnCheck = true;
                            logInCheck = true;
                        }
                    }
                } else if (option.toLowerCase().equals("ne")) {
                    logInCheck = true;
                    _ime = "no_name";
                    out.writeObject("EXIT");
                    String t = (String) in.readObject();
                } else {
                    System.out.println("Nepostojeci odgovor.");
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(KupacKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        try {
            logInCheck();
            if (!_ime.equals("no_name")) {
                System.out.println("Autorizacija uspjesna.");
                System.out.println("Dobro dosli " + _ime + " .");
            }
//            prima inventar
            System.out.println("Inicijalizacija inventara...");
            inventar = (HashMap<Proizvod, Integer>) in.readObject();
//          MENI+salje zahtjev
            kupacMeni(inventar);

//            KRAJ zatvori sve konekcije
            cIn.close();
            in.close();
            out.close();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(KupacKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String args[]) {
        KupacKlijent kk = new KupacKlijent();
        kk.start();
    }

    public void kupacMeni(HashMap<Proizvod, Integer> inventar) {
        boolean end = false;
        while (!end) {
            SistemProdaje.cls();
            for (Proizvod p : korpa.keySet()) {
                if (korpa.get(p) <= 0) {
                    korpa.remove(p);
                }
            }
            System.out.println("-------------------");
            System.out.println("Opcije:");
            System.out.println("1. Pregled inventara.");
            System.out.println("2. Pregled korpe.");
            System.out.println("3. Dodavanje proizvoda u korpi.");
            System.out.println("4. Uklanjanje proizvoda iz korpe.");
            System.out.println("5. Finisiranje narudzbe.");
            System.out.println("0. Izlaz.");
            System.out.println("-------------------");
            System.out.printf("[1/2/3/4/0]: ");
            switch (SistemProdaje.optionChooser(cIn.nextLine())) {
                case 1: {
                    if (inventar.isEmpty()) {
                        System.out.println("Inventar je prazan.");
                    } else {
                        System.out.println("-------------------");
                        System.out.println("SIFRA  NAZIV CIJENA KOLICINA");
                        for (Proizvod p : inventar.keySet()) {
                            p.print();
                            System.out.printf(" x" + inventar.get(p) + "\n");
                        }
                    }
                    System.out.println("Pritisnite ENTER da nastavite.");
                    cIn.nextLine();
                    break;
                }
                case 2: {
                    if (korpa.isEmpty()) {
                        System.out.println("Korpa je prazna.");
                    } else {
                        double sum = 0;
                        System.out.println("-------------------");
                        System.out.println("SIFRA  NAZIV CIJENA KOLICINA");
                        for (Proizvod p : korpa.keySet()) {
                            p.print();
                            sum += p.getCijena() * korpa.get(p);
                            System.out.printf(" x" + korpa.get(p) + "\n");
                        }
                        System.out.println("-------------------");
                    }
                    System.out.println("Pritisnite ENTER da nastavite.");
                    cIn.nextLine();
                    break;
                }
                case 3: {
                    if (inventar.isEmpty()) {
                        System.out.println("Inventar je prazan.");
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    } else {
                        System.out.println("-------------------");
                        System.out.println("SIFRA  NAZIV CIJENA KOLICINA");
                        for (Proizvod p : inventar.keySet()) {
                            p.print();
                            System.out.printf(" x" + inventar.get(p) + "\n");
                        }
                        System.out.println("-------------------");
                        System.out.println("Zeljeni proizvod:[NAZIV]: ");
                        String naziv = cIn.nextLine();
                        Proizvod proizvod = null;
                        for (Proizvod p : inventar.keySet()) {
                            if (p.getNaziv().equals(naziv)) {
                                proizvod = p;
                            }
                        }
                        if (inventar.containsKey(proizvod)) {
                            System.out.printf("Unesite kolicinu: ");
                            Integer kolicina = cIn.nextInt();
                            if (inventar.get(proizvod) < kolicina) {
                                System.out.println("Na stanju nema toliko artikala.");
                            } else {
                                korpa.put(proizvod, kolicina);
                                int temp = inventar.get(proizvod) - kolicina;
                                inventar.remove(proizvod);
                                inventar.put(proizvod, temp);
                                System.out.println("Proizvod dodan u korpu.");
                            }
                        } else {
                            System.out.println("Proizvod ne postoji.");
                        }
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    }
                    break;
                }
                case 4: {
                    if (korpa.isEmpty()) {
                        System.out.println("Korpa je prazan.");
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    } else {
                        System.out.println("-------------------");
                        System.out.println("SIFRA  NAZIV CIJENA KOLICINA");
                        for (Proizvod p : korpa.keySet()) {
                            p.print();
                            System.out.printf(" x" + korpa.get(p) + "\n");
                        }
                        System.out.println("-------------------");
                        System.out.println("Proizvod za uklanjanje:[NAZIV]: ");
                        String naziv = cIn.nextLine();
                        Proizvod proizvod = null;
                        for (Proizvod p : korpa.keySet()) {
                            if (p.getNaziv().equals(naziv)) {
                                proizvod = p;
                            }
                        }
                        if (korpa.containsKey(proizvod)) {
                            System.out.printf("Unesite kolicinu: ");
                            Integer kolicina = cIn.nextInt();
                            if (korpa.get(proizvod) < kolicina) {
                                System.out.println("U korpi nema toliko artikala.");
                            } else {
                                int iTemp = inventar.get(proizvod) + kolicina;
                                inventar.remove(proizvod);
                                inventar.put(proizvod, iTemp);
                                int kTemp = korpa.get(proizvod) - kolicina;
                                korpa.remove(proizvod);
                                korpa.put(proizvod, kTemp);
                                System.out.println("Artikli uspjesno uklonjeni iz korpe.");
                            }
                        } else {
                            System.out.println("Proizvod ne postoji.");
                        }
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    }
                    break;
                }
                case 5: {
                    try {
                        out.writeObject("kupovina");
                        if (((String) in.readObject()).equals("accepted")) {
                            out.writeObject(korpa);
                            System.out.println("Narudzba poslana.");
                            System.out.println("Pritisnite ENTER da nastavite.");
                            cIn.nextLine();
                            Racun racun = (Racun) in.readObject();
                            SistemProdaje.cls();
                            System.out.println("Vas racun:");
                            racun.print();
                            System.out.println("Pritisnite ENTER za kraj.");
                            cIn.nextLine();
                            end = true;
                        } else {
                            System.out.println("Zahtjev nije poslan.");
                            System.out.println("Pritisnite ENTER za kraj.");
                            cIn.nextLine();
                            end = true;
                        }
                    } catch (IOException | ClassNotFoundException ex) {
                        Logger.getLogger(KupacKlijent.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    end = true;
                    break;

                }
                case 0: {
                    try {
                        out.writeObject("0");
                        end = true;
                        break;
                    } catch (IOException ex) {
                        Logger.getLogger(KupacKlijent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                default:
            }
        }
    }

}
