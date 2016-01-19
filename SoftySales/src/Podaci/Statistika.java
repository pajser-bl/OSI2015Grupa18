package Podaci;

import java.util.HashMap;

public class Statistika implements java.io.Serializable {

    public HashMap<Proizvod, Integer> _listaProizvoda;

    public Statistika(){
        _listaProizvoda=new HashMap<>();
    }
    public void add(Proizvod proizvod, Integer kolicina) {
        if (_listaProizvoda.containsKey(proizvod)) {
            Integer t=_listaProizvoda.get(proizvod)+kolicina;
            _listaProizvoda.remove(proizvod);
            _listaProizvoda.put(proizvod, t);
        } else {
            _listaProizvoda.put(proizvod, kolicina);
        }
    }

    public void print() {
        _listaProizvoda.keySet().stream().forEach((i) -> {
            String proizvod = i.getNaziv();
            int kolicina = _listaProizvoda.get(i);
            double sum=i.getCijena()*kolicina;
            i.print();
            System.out.printf(" x" + kolicina+"Sum: "+sum+"\n");
        });
    }
    public boolean printSingle(String naziv){
        boolean rVal=false;
        for(Proizvod p:_listaProizvoda.keySet()){
            if(p.getNaziv().equals(naziv)){
                double sum=_listaProizvoda.get(p)*p.getCijena();
                System.out.println(p.getSifra()+" "+p.getNaziv()+" x"+_listaProizvoda.get(p)+" SUM: "+sum+"\n");
                rVal=true;
            }
        }
        return rVal;
    }
    public int getCoeficient(Proizvod proizvod) {
        return _listaProizvoda.get(proizvod);
    }

    public void addStatistika(Statistika statistika) {
        for (Proizvod i : statistika._listaProizvoda.keySet()) {
            Integer kol=statistika._listaProizvoda.get(i);
            this.add(i, kol);
        }
    }
}
