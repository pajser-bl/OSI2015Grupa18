package Sistem;

import Podaci.Dan;
import Podaci.Godina;
import Podaci.Izvjestaj;
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
                listaRadnika.put("admin", sha256("1111"));
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
            System.out.println("0. Nazad");
            System.out.println("-------------------");
            System.out.printf("[1/2/0]: ");

            switch (Integer.parseInt(cIn.nextLine())) {
                case 1: {
                    boolean endPodmeni = false;
                    while (!endPodmeni) {
                        cls();
                        System.out.println("-------------------");
                        System.out.println("Pregled racuna:");
                        System.out.println("1. Dan.");
                        System.out.println("2. Sedmica.");
                        System.out.println("3. Mjesec.");
                        System.out.println("4. Godina.");
                        System.out.println("0. Nazad");
                        System.out.println("-------------------");
                        System.out.printf("[1/2/3/4/0]: ");

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
                                Dan.read("dani" + File.separator + danZaIspis).print();
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
                                Sedmica.read("sedmice" + File.separator + sedmicaZaIspis).print();
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
                                Mjesec.read("mjeseci" + File.separator + mjesecZaIspis).print();
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
                                Godina.read("godine" + File.separator + godinaZaIspis).print();
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
                        System.out.println("Pregled statistike:");
                        System.out.println("1. Pojedinacnog proizvoda.");
                        System.out.println("2. Svih proizvoda.");
                        System.out.println("0. Nazad");
                        System.out.println("-------------------");
                        System.out.printf("[1/2/0]: ");
                        switch (Integer.parseInt(cIn.nextLine())) {
                            case 1: {
                                System.out.println("Unesite sifru proizvoda:");
                                String proizvod = cIn.nextLine();
                                File godina = new File("godine");
                                File godine[] = godina.listFiles();
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
                                cIn.nextLine();
                        }
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
    public static void radnikMeni(String kasir, ArrayList<String> listaKupaca, ArrayList<HashMap<Proizvod, Integer>> listaZahtjeva) {
        Scanner cIn = new Scanner(System.in);
        boolean end = false;
//        FORMAT VREMENA
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        Date date = new Date();

//        podaci o vremenu
        String pocetakRada = dateFormat.format(date);
        String krajRada;

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

                    }
                    break;
                }
                case 2: {
                    cls();
                    upravljanjeKupcima(listaKupaca);
                    break;
                }
                case 3: {
                    break;
                }
                case 4: {
                    pravljenjeIzvjestaja(kasir);
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
                        System.out.println("Kupac " + ime + " uspjesno dodan.");
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

    public static void upravljanjeProizvodima() {
    }

    public static void pravljenjeIzvjestaja(String kasir) {
        Scanner cIn = new Scanner(System.in);
        String opcija;
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
                    opcija = "DAN";
                    break;
                }
                case 2: {
                    opcija = "SEDMICA";
                    break;
                }
                case 3: {
                    opcija = "MJESEC";
                    break;
                }
                case 4: {
                    opcija = "GODINA";
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
        if (!end) {
            System.out.println("Izvjestaj:");
            String text = cIn.nextLine();
            Izvjestaj izvjestaj = new Izvjestaj(kasir, text);
            izvjestaj.save();
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
            saveKupci(new ArrayList());
        }

        if (!new File("lista_radnika.ser").exists()) {
            saveRadnici(new HashMap());
        }
    }

}
