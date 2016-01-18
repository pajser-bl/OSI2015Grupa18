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
    private InetAddress iAddress;
    private BufferedReader in;
    private PrintWriter out;
    private Scanner cIn;
    private Socket sock;
    private boolean logInCheck;
    private boolean logOnCheck;
    private String _ime;
    private HashMap<Proizvod,Integer>inventar;
    private HashMap<Proizvod,Integer>korpa;
    
    public String getIme() {
        return _ime;
    }

    public KupacKlijent() {
        try {
            this.logInCheck = false;
            this.logOnCheck = false;
            this.korpa=new HashMap();
            this.iAddress = InetAddress.getByName("127.0.0.1");
            this.sock = new Socket(iAddress, PORT);
            this.in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            this.out = new PrintWriter((new BufferedWriter(new OutputStreamWriter(sock.getOutputStream()))), true);
            this.cIn = new Scanner(System.in);
        } catch (UnknownHostException ex) {
            Logger.getLogger(KupacKlijent.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(KupacKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean logInCheck() {
        try {
            String ime;
            System.out.printf("Unesite vase ime: ");
            ime = cIn.nextLine();
            out.println(ime);
            String rec = in.readLine();
            if (rec.equals("1")) {
                _ime = ime;
                return true;
            } else {
                System.out.println("Neodgovarajuci podatci.");
            }
            return false;
        } catch (IOException ex) {
            Logger.getLogger(KupacKlijent.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    @Override
    public void run() {
        try {
            while (!logOnCheck) {
                System.out.printf("Da li ste privilegovani kupac?[Da/Ne]:");
                if (cIn.nextLine().toLowerCase().equals("da")) {
                    if (logInCheck()) {
                        logInCheck = true;
                        logOnCheck = true;
                    }
                } else if (cIn.nextLine().toLowerCase().equals("ne")) {
                    out.println("0");
                    logOnCheck = true;
                }
            }
            System.out.println("Autorizacija uspjesna.");
            System.out.println("Dobrodosli " + _ime + " .");
//            in.close();
//            out.close();
            ObjectInputStream oIn=new ObjectInputStream(sock.getInputStream());
            ObjectOutputStream oOut=new ObjectOutputStream(sock.getOutputStream());
            inventar=(HashMap<Proizvod,Integer>)oIn.readObject();
            
//        Meni
            kupacMeni(inventar,oOut,oIn);
            Racun racun=(Racun)oIn.readObject();
            System.out.println("Vas racun:");
            racun.print();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(KupacKlijent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String args[]) {
        KupacKlijent kk = new KupacKlijent();
        kk.start();
    }
    public void kupacMeni(HashMap<Proizvod,Integer>inventar,ObjectOutputStream oOutS,ObjectInputStream oInS){
        Scanner cIn=new Scanner(System.in);
        boolean end=false;
        while(!end){
            SistemProdaje.cls();
            for(Proizvod p:korpa.keySet()){
                if(korpa.get(p)<=0)
                    korpa.remove(p);
            }
            System.out.println("-------------------");
            System.out.println("Opcije:");
            System.out.println("1. Pregled inventara.");
            System.out.println("2. Pregled korpe.");
            System.out.println("3. Promjena proizvoda u korpi.");
            System.out.println("4. Finisiranje narudzbe.");
            System.out.println("0. Izlaz.");
            System.out.println("-------------------");
            System.out.printf("[1/2/3/4/0]: ");
            switch(Integer.parseInt(cIn.nextLine())){
                case 1:{
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
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    break;
                    }
                }
                case 2:{
                    if (korpa.isEmpty()) {
                        System.out.println("Korpa je prazna.");
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    } else {
                        double sum=0;
                        System.out.println("-------------------");
                        System.out.println("SIFRA  NAZIV CIJENA KOLICINA");
                        for (Proizvod p : inventar.keySet()) {
                            p.print();
                            sum+=p.getCijena()*inventar.get(p);
                            System.out.printf(" x" + inventar.get(p) + "\n");
                        }
                        System.out.println("-------------------");
                        System.out.println("Ukupna cijena: "+sum);
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    }
                break;
                }
                case 3:{
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
                        String naziv=cIn.nextLine();
                        Proizvod proizvod=null;
                        for(Proizvod p:inventar.keySet()){
                            if(p.getNaziv().equals(naziv)){
                                proizvod=p;
                                }
                        }
                        if(inventar.containsKey(proizvod)){
                            System.out.printf("Unesite kolicinu: ");
                            Integer kolicina=cIn.nextInt();
                            korpa.put(proizvod,kolicina);
                            System.out.println("Proizvod dodan u korpu.");
                        }else
                            System.out.println("Proizvod ne postoji.");
                            
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    }
                        
                    break;
                }
                
                case 4:{
                try {
                    oOutS.writeObject(korpa);
                    System.out.println("Narudzba poslana.");
                    System.out.println("Pritisnite ENTER da nastavite.");
                    cIn.nextLine();
                    Racun racun=(Racun)oInS.readObject();
                    racun.print();
                } catch (IOException | ClassNotFoundException ex) {
                    Logger.getLogger(KupacKlijent.class.getName()).log(Level.SEVERE, null, ex);
                }
                    
                    end=true;
                    break;
                    
                }
                case 0:{
                    end=true;
                    break;
                }
                default:
            }
        }
    }



}
