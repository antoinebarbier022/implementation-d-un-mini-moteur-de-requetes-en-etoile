from genericpath import isfile
import os, sys, re
from posixpath import join
from posix import listdir

path = ""
if len(sys.argv) > 1:
    path = sys.argv[1]
    # On vérifie que le dossier passé en paramètre est bien un dossier
    if not os.path.exists(path):
        print(path + " isn't a directory.")
        sys.exit()
else:
    print("Il faut passer un dossier en paramètre.")
    sys.exit()
        
print("Execution du programme pour les fichiers de requêtes suivants : ")
# On récupère seulement les fichier .queryset
for file in os.listdir(path):
    if file.endswith(".queryset"):
        print(file)
        os.system('/usr/bin/env /Library/Java/JavaVirtualMachines/temurin-8.jdk/Contents/Home/bin/java -Xmx4024m -Xms4024m -cp /var/folders/h7/lyvclp_d0c70hx6v7mklwfkh0000gn/T/cp_4lc1qsjfs2nzd8hugppfmullr.jar qengine.program.Main -data data/100K.nt -queries ' + os.path.join(path, file))
        
