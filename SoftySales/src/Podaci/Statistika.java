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
            String kolicina = _listaProizvoda.get(i).toString();
            System.out.println(proizvod + " x" + kolicina);
        });
    }

    public int getCoeficient(Proizvod proizvod) {
        return (Integer) _listaProizvoda.get(proizvod);
    }

    public void addStatistika(Statistika statistika) {
        for (Proizvod i : statistika._listaProizvoda.keySet()) {
            this.add(i, statistika._listaProizvoda.get(i));
        }
    }
}
