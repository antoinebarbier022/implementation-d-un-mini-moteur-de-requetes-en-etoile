from genericpath import isfile
import os, sys, re
from posixpath import join
from posix import listdir

print("Pour changer le fichier de données, modifiez le dans le scripte ")
# fichier donnée à modifier si besoin
dataFile = 'data/100K.nt'

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
        os.system('java -jar qengine-AntoineBARBIER-DjamelBENAMEUR.jar -data '+ dataFile + ' -type-output csv -queries ' + os.path.join(path, file))
        
