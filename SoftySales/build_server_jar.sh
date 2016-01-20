#!/bin/bash

cd build/classes/

jar_name="SoftySales Server.jar"

jar cfe "$jar_name" Sistem.KasaServer Podaci/*.class Sistem/KasaServer*.class Sistem/SistemProdaje.class

mv "$jar_name" ../../jars/

exit 0
