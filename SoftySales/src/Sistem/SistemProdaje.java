package Sistem;

import Podaci.Dan;
import Podaci.Godina;
import Podaci.Mjesec;
import Podaci.Proizvod;
import Podaci.Racun;
import Podaci.Sedmica;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;

public class SistemProdaje {

    public static final File RACUNI = new File("racuni");
    public static final File DANI = new File("dani");
    public static final File SEDMICE = new File("sedmice");
    public static final File MJESECI = new File("mjeseci");
    public static final File GODINE = new File("godine");
    public static final File IZVJESTAJI = new File("izvjestaji");

    public static String sha256(String password) {

        String _pwd = "";

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            _pwd = hexString.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(SistemProdaje.class.getName()).log(Level.SEVERE, null, ex);
        }

        return _pwd;
    }

    public static Integer loginCheck(String username, String password, HashMap<String, String> listaKorisnika) {
        if (username.equals("EXIT".toLowerCase()) || password.equals("EXIT".toLowerCase())) {
            return 0;
        }
        if (listaKorisnika.containsKey(username)) {

            String _pwd = sha256(password);

            if (listaKorisnika.get(username).equals(_pwd) && username.equals("admin")) {
                return 2;
            }
            if (listaKorisnika.get(username).equals(_pwd)) {
                return 1;
            }
            return -1;
        }
        return -1;
    }
//    serijalizacija naloga

    public static void saveRadnici(HashMap<String, String> lista) {
        try {
            FileOutputStream fOut = new FileOutputStream("lista_radnika.ser");
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(lista);
            fOut.close();
            oOut.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SistemProdaje.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SistemProdaje.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static HashMap<String, String> readRadnici() {
        try {
            HashMap<String, String> listaRadnika;
            FileInputStream fIn = new FileInputStream("lista_radnika.ser");
            ObjectInputStream oIn = new ObjectInputStream(fIn);
            listaRadnika = (HashMap) oIn.readObject();
            oIn.close();
            fIn.close();
            if (!listaRadnika.containsKey("admin")) {
                listaRadnika.put("admin", "d17f25ecfbcc7857f7bebea469308be0b2580943e96d13a3ad98a13675c4bfc2");
            }
            return listaRadnika;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SistemProdaje.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(SistemProdaje.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
//  serijalizacija kupaca

    public static void saveKupci(ArrayList<String> listaKupaca) {
        try {
            FileOutputStream fOut = new FileOutputStream("lista_kupaca.ser");
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(listaKupaca);
            fOut.close();
            oOut.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SistemProdaje.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SistemProdaje.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ArrayList<String> readKupci() {
        try {
            ArrayList<String> listaKupaca;
            FileInputStream fIn = new FileInputStream("lista_kupaca.ser");
            ObjectInputStream oIn = new ObjectInputStream(fIn);
            listaKupaca = (ArrayList) oIn.readObject();
            oIn.close();
            fIn.close();
            return listaKupaca;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SistemProdaje.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(SistemProdaje.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
//serijalizacija inventara

    public static void saveInventar(HashMap<Proizvod, Integer> inventar) {
        try {
            FileOutputStream fOut = new FileOutputStream("inventar.ser");
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(inventar);
            oOut.close();
            fOut.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SistemProdaje.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SistemProdaje.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static HashMap<Proizvod, Integer> readInventar() {
        try {
            HashMap<Proizvod, Integer> inventar;
            FileInputStream fIn = new FileInputStream("inventar.ser");
            ObjectInputStream oIn = new ObjectInputStream(fIn);
            inventar = (HashMap<Proizvod, Integer>) oIn.readObject();
            oIn.close();
            fIn.close();
            return inventar;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SistemProdaje.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(SistemProdaje.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

//  serijalizacija statistike koristenja
    public static void saveStatistikaKoristenja(String statistikaKoristenja) {
        try {
            FileOutputStream fOut = new FileOutputStream("statistika_koristenja.ser");
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(statistikaKoristenja);
            oOut.close();
            fOut.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SistemProdaje.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SistemProdaje.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String readStatistikaKoristenja() {
        try {
            String statistikaKoristenja;
            FileInputStream fIn = new FileInputStream("statistika_koristenja.ser");
            ObjectInputStream oIn = new ObjectInputStream(fIn);
            statistikaKoristenja = (String) oIn.readObject();
            oIn.close();
            fIn.close();
            return statistikaKoristenja;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SistemProdaje.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(SistemProdaje.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
//  admin meni

    public static void adminMeni(HashMap<String, String> lista) {
        boolean end = false;
        Scanner cIn = new Scanner(System.in);
        while (!end) {
            cls();
            System.out.println("-------------------");
            System.out.println("Administrativne opcije:");
            System.out.println("1. Upravljanje nalozima.");
            System.out.println("2. Pregled statistike.");
            System.out.println("3. Izmjena parametara sistema-PRAZNO.");
            System.out.println("0. Izlaz.");
            System.out.println("-------------------");
            System.out.printf("[1/2/3/0]: ");

            switch (Integer.parseInt(cIn.nextLine())) {
                case 1: {
                    cls();
                    upravljanjeNalozima(lista);
                    break;
                }
                case 2: {
                    cls();
                    pregledStatistike();
                    break;
                }
                case 3: {
//                    PRAZNO
                    break;
                }
                case 0: {
                    end = true;
                    break;
                }
                default:
                    System.out.println("Nepostojeca opcija.");
                    cIn.nextLine();
            }
        }
        cIn.close();
    }

    public static void upravljanjeNalozima(HashMap<String, String> lista) {
        Scanner cIn = new Scanner(System.in);
        boolean end = false;
        while (!end) {
            cls();
            saveRadnici(lista);
            lista = readRadnici();
            System.out.println("-------------------");
            System.out.println("Uravljanje nalozima:");
            System.out.println("1. Pregled naloga.");
            System.out.println("2. Dodavanje naloga.");
            System.out.println("3. Modifikacija naloga.");
            System.out.println("4. Brisanje naloga.");
            System.out.println("0. Nazad");
            System.out.println("-------------------");
            System.out.printf("[1/2/3/4/0]: ");

            switch (Integer.parseInt(cIn.nextLine())) {
                case 1: {
                    cls();
                    System.out.println("-------------------");
                    for (String i : lista.keySet()) {
                        System.out.println(i);
                    }
                    System.out.println("-------------------");
                    System.out.println("Pritisnite ENTER da nastavite.");
                    cIn.nextLine();
                    break;
                }
                case 2: {
                    cls();
                    System.out.printf("Username: ");
                    String user = cIn.nextLine();
                    if (lista.containsKey(user)) {
                        cls();
                        System.out.println("Korisnik vec postoji.");
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                        break;
                    } else {
                        System.out.printf("Password: ");
                        String pass = cIn.nextLine();
                        lista.put(user, sha256(pass));
                        System.out.println("Korisnik uspjesno dodan.");
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                        break;
                    }

                }
                case 3: {
                    cls();
                    System.out.println("-------------------");
                    for (String i : lista.keySet()) {
                        System.out.println(i);
                    }
                    System.out.println("-------------------");
                    System.out.printf("Nalog za modifikaciju: ");
                    String user = cIn.nextLine();
                    String pass;
                    if (lista.containsKey(user)) {
                        lista.remove(user);
                        System.out.printf("Novi Username: ");
                        user = cIn.nextLine();
                        System.out.printf("Novi Password: ");
                        pass = cIn.nextLine();
                        lista.put(user, sha256(pass));
                        System.out.printf("Modifikacija zavrsena.");
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    } else {
                        System.out.println("User ne postoji.");
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    }
                    break;
                }
                case 4: {
                    cls();
                    System.out.println("-------------------");
                    for (String i : lista.keySet()) {
                        System.out.println(i);
                    }
                    System.out.println("-------------------");
                    System.out.printf("Nalog za brisanje: ");
                    String user = cIn.nextLine();
                    if (lista.containsKey(user)) {
                        lista.remove(user);
                        cls();
                        System.out.println("Nalog obrisan.");
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    } else {
                        System.out.printf("Nalog ne postoji.");
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    }
                    break;
                }
                case 0: {
                    end = true;
                    break;
                }
                default:
                    System.out.println("Nepostojeca opcija.");
                    cIn.nextLine();
            }
        }
    }

    public static void pregledStatistike() {
        Scanner cIn = new Scanner(System.in);
        boolean end = false;
        while (!end) {
            cls();
            System.out.println("-------------------");
            System.out.println("Pregled statistike prodaje:");
            System.out.println("1. Pregled racuna.");
            System.out.println("2. Pregled prodaje.");
            System.out.println("3. Pregled rada.");
            System.out.println("0. Nazad");
            System.out.println("-------------------");
            System.out.printf("[1/2/3/0]: ");

            switch (Integer.parseInt(cIn.nextLine())) {
                case 1: {
                    boolean endPodmeni = false;
                    while (!endPodmeni) {
                        cls();
                        System.out.println("-------------------");
                        System.out.println("Pregled racuna za:");
                        System.out.println("1. Dan.");
                        System.out.println("2. Sedmica.");
                        System.out.println("3. Mjesec.");
                        System.out.println("4. Godina.");
                        System.out.println("5. Pojedinacnih racuna.");
                        System.out.println("0. Nazad");
                        System.out.println("-------------------");
                        System.out.printf("[1/2/3/4/5/0]: ");

                        switch (Integer.parseInt(cIn.nextLine())) {
                            case 1: {
                                File dani = new File("dani");
                                File[] files = dani.listFiles();
                                String danZaIspis;
                                System.out.println("Racuni za:");
                                for (File f : files) {
                                    System.out.println(f.getName());
                                }
                                System.out.println("Za koji dan zelite da vidite racune?");
                                danZaIspis = cIn.nextLine();
                                cls();
                                if (new File("dani" + File.separator + danZaIspis).exists()) {
                                    Dan.read("dani" + File.separator + danZaIspis).print();
                                } else {
                                    System.out.println("Nepostojeci dan.");
                                }
                                System.out.println("Pritisnite ENTER da nastavite.");
                                cIn.nextLine();
                                break;
                            }
                            case 2: {
                                File sedmice = new File("sedmice");
                                File[] files = sedmice.listFiles();
                                String sedmicaZaIspis;
                                System.out.println("Racuni za:");
                                for (File f : files) {
                                    System.out.println(f.getName());
                                }
                                System.out.println("Za koju sedmicu zelite da vidite racune?");
                                sedmicaZaIspis = cIn.nextLine();
                                cls();
                                if (new File("sedmice" + File.separator + sedmicaZaIspis).exists()) {
                                    Sedmica.read("sedmice" + File.separator + sedmicaZaIspis).print();
                                } else {
                                    System.out.println("Nepostojeca sedmica.");
                                }
                                System.out.println("Pritisnite ENTER da nastavite.");
                                cIn.nextLine();
                                break;
                            }
                            case 3: {
                                File mjeseci = new File("mjeseci");
                                File[] files = mjeseci.listFiles();
                                String mjesecZaIspis;
                                System.out.println("Racuni za:");
                                for (File f : files) {
                                    System.out.println(f.getName());
                                }
                                System.out.println("Za koji mjesec zelite da vidite racune?");
                                mjesecZaIspis = cIn.nextLine();
                                cls();
                                if (new File("mjeseci" + File.separator + mjesecZaIspis).exists()) {
                                    Mjesec.read("mjeseci" + File.separator + mjesecZaIspis).print();
                                } else {
                                    System.out.println("Nepostojeci mjesec.");
                                }
                                System.out.println("Pritisnite ENTER da nastavite.");
                                cIn.nextLine();
                                break;
                            }
                            case 4: {
                                File godine = new File("godine");
                                File[] files = godine.listFiles();
                                String godinaZaIspis;
                                System.out.println("Racuni za:");
                                for (File f : files) {
                                    System.out.println(f.getName());
                                }
                                System.out.println("Za koju godinu zelite da vidite racune?");
                                godinaZaIspis = cIn.nextLine();
                                cls();
                                if (new File("godine" + File.separator + godinaZaIspis).exists()) {
                                    Godina.read("godine" + File.separator + godinaZaIspis).print();
                                } else {
                                    System.out.println("Nepostojeca godina.");
                                }
                                System.out.println("Pritisnite ENTER da nastavite.");
                                cIn.nextLine();
                                break;
                            }
                            case 5: {
                                File racuni = new File("racuni");
                                File[] files = racuni.listFiles();
                                String racunZaIspis;
                                System.out.println("Racuni:");
                                for (File f : files) {
                                    System.out.println(f.getName());
                                }
                                System.out.println("Koji racun zelite da vidite?");
                                racunZaIspis = cIn.nextLine();
                                cls();
                                if (new File("racuni" + File.separator + racunZaIspis).exists()) {
                                    Racun.read("racuni" + File.separator + racunZaIspis).print();
                                } else {
                                    System.out.println("Nepostojeci racun.");
                                }
                                System.out.println("Pritisnite ENTER da nastavite.");
                                cIn.nextLine();
                                break;
                            }
                            case 0: {
                                endPodmeni = true;
                                break;
                            }
                            default:
                        }
                    }
                    break;
                }
                case 2: {
                    boolean endPodmeni = false;
                    while (!endPodmeni) {
                        cls();
                        System.out.println("-------------------");
                        System.out.println("Pregled statistike za:");
                        System.out.println("1. Dan.");
                        System.out.println("2. Mjesec.");
                        System.out.println("3. Mjesec.");
                        System.out.println("4. Godina.");
                        System.out.println("0. Nazad");
                        System.out.println("-------------------");
                        System.out.printf("[1/2/3/4/0]: ");
                        switch (Integer.parseInt(cIn.nextLine())) {
                            case 1: {
                                boolean endPodmeniPodmeni=false;
                                while(!endPodmeniPodmeni){
                                    cls();
                                    System.out.println("-------------------");
                                    System.out.println("1. Pregled pojedinacne statistike:");
                                    System.out.println("2. Pregled uopstene statistike:");
                                    System.out.println("0. Nazad");
                                    System.out.println("-------------------");
                                    System.out.printf("[1/2/0]: ");
                                    switch(Integer.parseInt(cIn.nextLine())){
                                        case 1:{
                                            File dani = new File("dani");
                                            File[] files = dani.listFiles();
                                            String danZaIspis;
                                            System.out.println("Racuni:");
                                            for (File f : files) {
                                                System.out.println(f.getName());
                                            }
                                            System.out.println("Za koji dan zelite da vidite statistiku?");
                                            danZaIspis = cIn.nextLine();
                                            cls();
                                            if (new File("dani" + File.separator + danZaIspis).exists()) {
                                                System.out.println("Za koji proizvod zelite da vidite statistiku[NAZIV]?");
                                                String nazivProizvoda=cIn.nextLine();
                                                System.out.println("");
                                                if(!Dan.read("dani" + File.separator + danZaIspis).statistika.printSingle(nazivProizvoda))
                                                    System.out.println("Nema tog proizvoda.");
                                            } else {
                                                System.out.println("Nepostojeci dan.");
                                            }
                                            System.out.println("Pritisnite ENTER da nastavite.");
                                            cIn.nextLine();
                                        break;
                                        }
                                        case 2:{
                                            File dani = new File("dani");
                                            File[] files = dani.listFiles();
                                            String danZaIspis;
                                            System.out.println("Racuni:");
                                            for (File f : files) {
                                                System.out.println(f.getName());
                                            }
                                            System.out.println("Za koji dan zelite da vidite statistiku?");
                                            danZaIspis = cIn.nextLine();
                                            cls();
                                            if (new File("dani" + File.separator + danZaIspis).exists()) {
                                                Dan.read("dani" + File.separator + danZaIspis).statistika.print();
                                            } else {
                                                System.out.println("Nepostojeci dan.");
                                            }
                                            System.out.println("Pritisnite ENTER da nastavite.");
                                            cIn.nextLine();
                                        break;
                                        }
                                        case 0:{
                                            endPodmeniPodmeni=true;
                                            break;
                                        }
                                        default:
                                    }
                                }
                                break;
                            }
                            case 2: {
                                break;
                            }
                            case 0: {
                                endPodmeni = true;
                                break;
                            }
                            default:
                                System.out.println("Nepostojeca opcija.");
                                System.out.println("Pritisnite ENTER da nastavite.");
                                cIn.nextLine();
                        }
                    }
                    break;
                }
                case 3: {
                    String statistikaKoristenja = readStatistikaKoristenja();
                    if (statistikaKoristenja.equals("")) {
                        System.out.println("Statistika je prazna.");
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    } else {
                        System.out.println("-------------------");
                        System.out.println("Statistika koristenja: ");
                        System.out.println("DATUM RADNIK POCETAK KRAJ");
                        System.out.printf(statistikaKoristenja);
                        System.out.println("-------------------");
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    }
                    break;
                }
                case 0: {
                    end = true;
                    break;
                }
                default:
                    System.out.println("Nepostojeca opcija.");
                    cIn.nextLine();
            }

        }
    }

//    radnik meni
    public static void radnikMeni(String kasir, ArrayList<String> listaKupaca, ArrayList<Racun> listaZahtjeva, HashMap<Proizvod, Integer> inventar, String startTime) {
        Scanner cIn = new Scanner(System.in);
        boolean end = false;

        while (!end) {
            cls();
            System.out.println("Radnik: " + kasir + " .");
            System.out.println("-------------------");
            System.out.println("1. Prihvati narudzbu.");
            System.out.println("2. Upravljanje kupcima.");
            System.out.println("3. Upravljanje proizvodima.");
            System.out.println("4. Pravljenje izvjestaja.");
            System.out.println("0. Izlaz.");
            System.out.println("-------------------");
            System.out.printf("[1/2/3/4/0]: ");
            switch (Integer.parseInt(cIn.nextLine())) {
                case 1: {
                    cls();
                    if (listaZahtjeva.isEmpty()) {
                        System.out.println("Trenutno nema zahtjeva za kupovinu.");
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    } else {
//                        sistem kupovine
                        for (int i = 0; i < listaZahtjeva.size(); i++) {
                            listaZahtjeva.get(i).finish();
                            listaZahtjeva.get(i).print();
                            listaZahtjeva.remove(i);
                            System.out.println("Pritisnite ENTER da nastavite.");
                            cIn.nextLine();
                        }
                    }
                    break;
                }
                case 2: {
                    upravljanjeKupcima(listaKupaca);
                    break;
                }
                case 3: {
                    upravljanjeProizvodima(inventar);
                    break;
                }
                case 4: {
                    pravljenjeIzvjestaja(kasir);
                    break;
                }
                case 0: {
                    addStatistikaKoristenja(kasir, startTime);
                    end = true;
                    break;
                }
                default:
                    System.out.println("Nepostojeca opcija.");
                    cIn.nextLine();
            }
        }
    }

    public static void upravljanjeKupcima(ArrayList<String> listaKupaca) {
        boolean end = false;
        Scanner cIn = new Scanner(System.in);
        while (!end) {
            cls();
            listaKupaca = readKupci();
            System.out.println("-------------------");
            System.out.println("1. Prikaz kupaca.");
            System.out.println("2. Dodavanje kupaca.");
            System.out.println("3. Modifikacija kupaca.");
            System.out.println("4. Brisanje kupaca.");
            System.out.println("0. Izlaz.");
            System.out.println("-------------------");
            System.out.printf("[1/2/3/0]: ");
            switch (Integer.parseInt(cIn.nextLine())) {
                case 1: {
                    if (listaKupaca.isEmpty()) {
                        System.out.println("Lista kupaca je prazna.");
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    } else {
                        System.out.println("-------------------");
                        for (String s : listaKupaca) {
                            System.out.println(s);
                        }
                        System.out.println("-------------------");
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    }
                    break;
                }
                case 2: {
                    System.out.printf("Ime novog kupca:");
                    String ime = cIn.nextLine();
                    if (listaKupaca.contains(ime)) {
                        System.out.println("Kupac vec postoji.");
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    } else {
                        listaKupaca.add(ime);
                        saveKupci(listaKupaca);
                        System.out.println("Kupac " + ime + " uspjesno dodan.\nNalog ce biti aktiviran u sledecoj sesiji.");
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    }
                    break;
                }
                case 3: {
                    System.out.println("-------------------");
                    for (String s : listaKupaca) {
                        System.out.println(s);
                    }
                    System.out.println("-------------------");
                    System.out.printf("Ime koje se modifikuje: ");
                    String ime = cIn.nextLine();
                    if (!listaKupaca.contains(ime)) {
                        System.out.println("Kupac ne postoji.");
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    } else {
                        System.out.printf("Novo ime: ");
                        String novoIme = cIn.nextLine();
                        if (listaKupaca.contains(novoIme)) {
                            System.out.println("Ime vec zauzeto.");
                            System.out.println("Pritisnite ENTER da nastavite.");
                            cIn.nextLine();
                        } else {
                            listaKupaca.remove(ime);
                            listaKupaca.add(novoIme);
                            saveKupci(listaKupaca);
                            System.out.println("Izmjena uspjesna.");
                            System.out.println("Pritisnite ENTER da nastavite.");
                            cIn.nextLine();
                        }
                    }
                    break;
                }
                case 4: {
                    System.out.println("-------------------");
                    for (String s : listaKupaca) {
                        System.out.println(s);
                    }
                    System.out.println("-------------------");
                    System.out.printf("Kupac koji se uklanja: ");
                    String ime = cIn.nextLine();
                    if (!listaKupaca.contains(ime)) {
                        System.out.println("Kupac ne postoji.");
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    } else {
                        listaKupaca.remove(ime);
                        saveKupci(listaKupaca);
                        System.out.println("Kupac uspjesno uklonjen.");
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                    }
                    break;

                }
                case 0: {
                    end = true;
                    break;
                }
                default:
                    System.out.println("Nepostojeca opcija.");
                    cIn.nextLine();
            }
        }
    }

    public static void upravljanjeProizvodima(HashMap<Proizvod, Integer> inventar) {
        Scanner cIn = new Scanner(System.in);
        boolean end = false;
        while (!end) {
            cls();
            inventar = readInventar();
            System.out.println("-------------------");
            System.out.println("1. Prikaz proizvoda.");
            System.out.println("2. Dodavanje novih proizvoda.");
            System.out.println("3. Promjena stanja proizvoda.");
            System.out.println("4. Uklanjanje proizvoda.");
            System.out.println("0. Nazad.");
            System.out.println("-------------------");
            switch (Integer.parseInt(cIn.nextLine())) {
                case 1: {
                    if (inventar.isEmpty()) {
                        System.out.println("Lista proizvoda je prazna.");
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
                    }
                    break;
                }
                case 2: {
                    cls();
                    System.out.printf("Naziv novog proizvoda: ");
                    String naziv = cIn.nextLine();
                    System.out.printf("Cijena novog proizvoda:");
                    double cijena = cIn.nextDouble();
                    Proizvod proizvod = new Proizvod(naziv, cijena);
                    System.out.printf("Unesite stanje proizvoda: ");
                    int kolicina = cIn.nextInt();
                    inventar.put(proizvod, kolicina);
                    saveInventar(inventar);
                    System.out.println("Proizvod uspjesno dodan.");
                    System.out.println("Pritisnite ENTER da nastavite.");
                    cIn.nextLine();
                    break;
                }
                case 3: {
                    cls();
                    if (inventar.isEmpty()) {
                        System.out.println("Lista proizvoda je prazna.");
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
                        System.out.printf("Proizvod cije stanje se mjenja[NAZIV]: ");
                        String ime = cIn.nextLine();
                        Proizvod proizvod = null;
                        for (Proizvod p : inventar.keySet()) {
                            if (p.getNaziv().equals(ime)) {
                                proizvod = p;
                            }
                        }
                        if (inventar.containsKey(proizvod)) {
                            System.out.printf("Novo stanje proizvoda: ");
                            Integer stanje = cIn.nextInt();
                            inventar.remove(proizvod);
                            inventar.put(proizvod, stanje);
                            saveInventar(inventar);
                            System.out.println("Stanje uspjesno promjenjeno.");
                        } else {
                            System.out.println("Proizvod ne postoji.");
                        }
                    }
                    System.out.println("Pritisnite ENTER da nastavite.");
                    cIn.nextLine();
                    break;
                }
                case 4: {
                    cls();
                    if (inventar.isEmpty()) {
                        System.out.println("Lista proizvoda je prazna.");
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
                        System.out.printf("Proizvod koji zelite da uklonite[NAZIV]: ");
                        String ime = cIn.nextLine();
                        Proizvod proizvod = null;
                        for (Proizvod p : inventar.keySet()) {
                            if (p.getNaziv().equals(ime)) {
                                proizvod = p;
                            }
                        }
                        if (inventar.containsKey(proizvod)) {
                            inventar.remove(proizvod);
                            saveInventar(inventar);
                            System.out.println("Proizvod uspjesno uklonjen.");
                        } else {
                            System.out.println("Proizvod ne postoji.");
                        }
                    }
                    System.out.println("Pritisnite ENTER da nastavite.");
                    cIn.nextLine();
                    break;
                }
                case 0: {
                    end = true;
                    break;
                }
                default:
            }
        }
    }

    public static void pravljenjeIzvjestaja(String kasir) {
        Scanner cIn = new Scanner(System.in);
        boolean end = false;
        while (!end) {
            cls();
            System.out.println("Izvjestaj za:");
            System.out.println("-------------------");
            System.out.println("1. Dan.");
            System.out.println("2. Sedmicu.");
            System.out.println("3. Mjesec.");
            System.out.println("4. Godinu.");
            System.out.println("0. Izlaz.");
            System.out.println("-------------------");
            switch (Integer.parseInt(cIn.nextLine())) {
                case 1: {
                    System.out.println("Za koji dan zelite da napravite izvjestaj?[dd.MM.yyyy]:");
                    String regEx=cIn.nextLine();
                    File racuni=new File("racuni");
                    Dan dan=new Dan();
                    for(File f:racuni.listFiles()){
                        if(f.getName().contains(regEx)){
                        Racun r=Racun.read(f.getPath());
                        dan.add(r);
                        }
                    }
                    if(dan.isEmpty()){
                        System.out.println("Nema racuna za taj dan.");
                    }else{
                        dan.save();
                        System.out.println("Izvjestaj za "+dan.getDatum()+" napravljen.");
                    }
                    System.out.println("Pritisnite ENTER da nastavite.");
                    cIn.nextLine();
                    break;
                }
                case 2: {
                    try {
                        System.out.println("Unesite prvi dan sedmice.[dd.MM.yyyy-dd.MM.yyyy]:");
                        String regEx=cIn.nextLine();
                        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        Date date0=dateFormat.parse(regEx);
                        Date date1=new Date(date0.getTime()+86400000);
                        Date date2=new Date(date1.getTime()+86400000);
                        Date date3=new Date(date2.getTime()+86400000);
                        Date date4=new Date(date3.getTime()+86400000);
                        Date date5=new Date(date4.getTime()+86400000);
                        Date date6=new Date(date5.getTime()+86400000);
                        String datum0=dateFormat.format(date0);
                        String datum1=dateFormat.format(date1);
                        String datum2=dateFormat.format(date2);
                        String datum3=dateFormat.format(date3);
                        String datum4=dateFormat.format(date4);
                        String datum5=dateFormat.format(date5);
                        String datum6=dateFormat.format(date6);
                        
                        File dani=new File("dani");
                        Sedmica sedmica=new Sedmica();
                        for(File f:dani.listFiles()){
                            if(f.getName().contains(datum0)){
                                Dan d=Dan.read(f.getPath());
                                if(!d.isEmpty())
                                    sedmica.add(d);
                            }
                            if(f.getName().contains(datum1)){
                                Dan d=Dan.read(f.getPath());
                                    if(!d.isEmpty())
                                        sedmica.add(d);
                            }
                            if(f.getName().contains(datum2)){
                                Dan d=Dan.read(f.getPath());
                                if(!d.isEmpty())
                                    sedmica.add(d);
                            }
                            if(f.getName().contains(datum3)){
                                Dan d=Dan.read(f.getPath());
                                if(!d.isEmpty())
                                    sedmica.add(d);
                            }
                            if(f.getName().contains(datum4)){
                                Dan d=Dan.read(f.getPath());
                                if(!d.isEmpty())
                                    sedmica.add(d);
                            }
                            if(f.getName().contains(datum5)){
                                Dan d=Dan.read(f.getPath());
                                if(!d.isEmpty())
                                    sedmica.add(d);
                            }
                            if(f.getName().contains(datum6)){
                                Dan d=Dan.read(f.getPath());
                                if(!d.isEmpty())
                                    sedmica.add(d);
                            }
                        }
                        if(sedmica.isEmpty()){
                            System.out.println("Nema dana za tu sedmicu.");
                        }else{
                            sedmica.setStartDate(datum0);
                            sedmica.setEndDate(datum6);
                            sedmica.save();
                            System.out.println("Izvjestaj za "+sedmica.getStartDate()+"-"+sedmica.getEndDate()+" napravljen.");
                        }   
                        System.out.println("Pritisnite ENTER da nastavite.");
                        cIn.nextLine();
                        break;
                    } catch (ParseException ex) {
                        Logger.getLogger(SistemProdaje.class.getName()).log(Level.SEVERE, null, ex);
                    }
                break;
                }
                case 3: {
                    System.out.println("Unesite broj mjeseca.[MM]:");
                    String MM=cIn.nextLine();
                    System.out.println("Unesite godinu.[yyyy]:");
                    String yyyy=cIn.nextLine();
                    File dani=new File("dani");
                    int brMjesec=Integer.parseInt(MM);
                    Mjesec mjesec=new Mjesec(brMjesec,yyyy);
                    for(File f:dani.listFiles()){
                        if(f.getName().contains(MM+"."+yyyy)){
                            Dan d=Dan.read(f.getPath());
                            if(!d.isEmpty())
                                mjesec.add(d);
                        }
                    }
                    if(mjesec.isEmpty()){
                        System.out.println("Nema dana za taj mjesec.");
                    }else{
                        mjesec.save();
                        System.out.println("Izvjestaj za "+mjesec.getMjesec()+" napravljen.");
                    }
                    System.out.println("Pritisnite ENTER da nastavite.");
                    cIn.nextLine();
                    break;
                }
                case 4: {
                    System.out.println("Unesite godinu.[yyyy]:");
                    String regEx=cIn.nextLine();
                    File mjeseci=new File("mjeseci");
                    Godina godina=new Godina(regEx);
                    for(File f:mjeseci.listFiles()){
                        if(f.getName().contains(regEx)){
                            Mjesec m=Mjesec.read(f.getPath());
                            if(!m.isEmpty())
                                godina.add(m);
                        }
                    }
                    if(godina.isEmpty()){
                        System.out.println("Nema mjeseci za tu godinu.");
                    }else{
                        godina.save();
                        System.out.println("Izvjestaj za "+godina.getGodina()+" napravljen.");
                    }
                    System.out.println("Pritisnite ENTER da nastavite.");
                    cIn.nextLine();
                    break;
                }
                case 0: {
                    end = true;
                    break;
                }
                default:
                    System.out.println("Nepostojeca opcija.");
                    cIn.nextLine();
            }
        }
    }

    public final static void cls() {
        try {
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")) {
                Runtime.getRuntime().exec("cls");
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException ex) {
            Logger.getLogger(SistemProdaje.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static final void fajlSistem() {
        File f;
        if (!RACUNI.exists() || !RACUNI.isDirectory()) {
            f = new File("racuni");
            f.mkdir();
        }
        if (!DANI.exists() || !DANI.isDirectory()) {
            f = new File("dani");
            f.mkdir();
        }
        if (!SEDMICE.exists() || !SEDMICE.isDirectory()) {
            f = new File("sedmice");
            f.mkdir();
        }
        if (!MJESECI.exists() || !MJESECI.isDirectory()) {
            f = new File("mjeseci");
            f.mkdir();
        }

        if (!GODINE.exists() || !GODINE.isDirectory()) {
            f = new File("godine");
            f.mkdir();
        }
        if (!IZVJESTAJI.exists() || !IZVJESTAJI.isDirectory()) {
            f = new File("izvjestaji");
            f.mkdir();
        }

        if (!new File("lista_kupaca.ser").exists()) {
            ArrayList<String> list = new ArrayList();
            saveKupci(list);
        }

        if (!new File("lista_radnika.ser").exists()) {
            HashMap<String, String> hm = new HashMap();
            hm.put("admin",sha256("1111"));
            saveRadnici(hm);
        }
        if (!new File("inventar.ser").exists()) {
            HashMap<Proizvod, Integer> hm = new HashMap();
            saveInventar(hm);
        }
        if (!new File("statistika_koristenja.ser").exists()) {
            String s = "";
            saveStatistikaKoristenja(s);
        }
    }

    public static String time() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String time = dateFormat.format(date);
        return time;
    }

    public static String date() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        String datum = dateFormat.format(date);
        return datum;
    }

    public static void addStatistikaKoristenja(String cashier, String sTime) {
        String datum = date();
        String eTime = time();
        String statistikaKoristenja = readStatistikaKoristenja();
        String toAdd = "[ " + datum + " ] " + cashier + ": < " + sTime + " | " + eTime + " >\n";
        String ss = statistikaKoristenja + toAdd;
        saveStatistikaKoristenja(ss);
    }
}
