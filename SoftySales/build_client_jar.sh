#!/bin/bash

cd build/classes/

jar_name="SoftySalesKlijent.jar"

jar cfe "$jar_name" Sistem.KupacKlijent Podaci/Proizvod.class Podaci/Racun.class Sistem/KupacKlijent.class Sistem/SistemProdaje.class

mv "$jar_name" ../../jars/

exit 0
