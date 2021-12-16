# Implementation d’un mini moteur de requêtes en étoile

Groupe : Antoine Barbier et Djamel Benameur

Git : https://gitlab.com/An_toine/implementation-d-un-mini-moteur-de-requetes-en-etoile/

---

## Passer les options en paramètre
Pour lancer le programme, suivre les étapes suivantes:

Options du programme : 
Soit on utilise l'option seul : ```clean```
L'option clean permet de supprimer le fichier output-historique.csv

Soit on utilise la combinaison des options suivante :
```-queries <queryFile>``` : obligatoire

```-data <dataFile> ``` : obligatoire

```-output <restultFolder>``` : facultative

```-type-output <type>``` : falcultatif, le type est ```txt``` ou ```csv``` (par défault c'est csv)


Par exemple, on peut lancer le programme de la façon suivante :

```qengine -queries <queryFile> -data <dataFile> -output <restultFolder>```

---

## Exécuter le programme

+ VsCode : Modifier le fichier ```.vscode/launch.json```, placer les options dans "args" comme dans l'exemple suivant : 
    ```
    "args": "queries/sample_query.queryset data/sample_data.nt output/"
    ```
+ Eclipse : Configurer le RUN, java application > Main, et placer les options dans program arguments. 



