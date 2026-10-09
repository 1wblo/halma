# Halma

Implémentation en Java du modèle de jeu **Halma** (variante en étoile, aussi connue sous le nom de « dames chinoises ») : génération du plateau, placement initial des pions, règles de déplacement et de saut, et partie jouable à plusieurs dans la console.

Projet réalisé dans le cadre d'un cours universitaire de génie logiciel (hiver 2025).

## Ce que fait le projet

- Construit un plateau en étoile à six branches dont la taille dépend d'un paramètre `baseSize`.
- Calcule, pour chaque joueur, sa zone de départ dans une des branches de l'étoile et sa zone cible dans la branche opposée.
- Détermine les cases voisines d'une case ainsi que la case « au-delà » d'une voisine, nécessaire pour les sauts.
- Maintient l'état de la partie : noms des joueurs, joueur courant, cases occupées, avec validation des accès (case inexistante, case déjà occupée, index de joueur invalide).
- Calcule les coups permis du joueur courant : déplacement vers une case voisine libre, ou saut par-dessus un pion, avec enchaînement de sauts.
- Fait jouer une partie dans la console : dessin du plateau en étoile, liste numérotée des coups permis, tours successifs et annonce du gagnant quand un joueur remplit sa zone cible.

## Architecture

Le code suit une organisation **MVC**, avec une **fabrique** (*Factory*) pour créer le modèle :

| Couche | Classe | Rôle |
|--------|--------|------|
| Modèle | `BoardImpl` | Géométrie du plateau : cases, zones de départ et zones cibles, voisinage. |
| Modèle | `ModelImpl` | État de la partie : joueurs, joueur courant, occupation des cases. |
| Contrôleur | `StarModelFactory` | Fabrique qui construit un modèle prêt à l'emploi à partir de la taille du plateau et des noms des joueurs. |
| Contrôleur | `HalmaController` | Règles du jeu : coups permis (déplacements et sauts), exécution d'un coup, tour suivant, victoire. |
| Vue | `DefaultConsoleLauncher` | Point d'entrée : crée le modèle par la fabrique et lance une partie. |
| Vue | `ConsoleGame` | Boucle de jeu : affiche le plateau et les coups, lit le choix du joueur. |
| Vue | `SimpleConsoleVisualizer` | Produit le résumé textuel d'un modèle et le dessin du plateau. |

Les classes implémentent les interfaces du package `org.halmaboard.api` (`Board`, `Model`, `ModelFactory`). La vue ne dépend que de la version en lecture seule du modèle (`ModelReadOnly`).

## Structure des dossiers

```
.
├── pom.xml                  Configuration Maven (compilation, Checkstyle, Javadoc, exécution)
├── google_checks.xml        Règles Checkstyle (style Google)
└── src
    ├── main/java/org/halmaboard
    │   ├── api/             Interfaces, types et exceptions : Board, Model, ModelReadOnly, ModelFactory, Field, Move
    │   └── view/            SimpleConsoleVisualizer
    ├── main/java/ca/uqam/info/solanum/students/halma
    │   ├── controller/      StarModelFactory, HalmaController
    │   ├── model/           BoardImpl, ModelImpl
    │   └── view/            DefaultConsoleLauncher, ConsoleGame
    ├── test/java/ca/uqam/info/solanum/students/halma
    │   ├── controller/      StarModelFactoryTest, HalmaControllerTest
    │   ├── model/           BoardImplTest, ModelImplTest, StarModelTest
    │   └── view/            ConsoleGameTest
    └── test/java/org/halmaboard/view
                             SimpleConsoleVisualizerTest
```

## Lancer le projet

Prérequis : Java 21 et Maven.

```bash
mvn clean package   # compile, vérifie le style, exécute les tests, génère le jar et la Javadoc
mvn test            # exécute uniquement les tests unitaires
mvn compile exec:java   # lance une partie à trois joueurs dans la console
```

Pour choisir la taille du plateau et les joueurs (3 ou 6) :

```bash
mvn compile exec:java -Dexec.args="3 Alice Bob Charlie"
```

À chaque tour, le programme dessine le plateau gradué en coordonnées, liste les coups permis du joueur courant et attend le numéro du coup à jouer (`q` pour quitter).

Plateau de départ (trois joueurs, `baseSize = 3`) : chaque chiffre est un pion du joueur de même indice, chaque point une case libre.

```
         .                 1
            .           1
         .     .     1     1
            .     .     1
         .     .     .     1
            .     .     .
         .     .     .     .
      0     .     .     .     .
   0     .     .     .     .     .
0     0     .     .     .     .     .
   0     .     .     .     .     .
      0     .     .     .     .
         .     .     .     .
            .     .     .
         .     .     .     2
            .     .     2
         .     .     2     2
            .           2
         .                 2
```

## Dépendances

Le projet est autonome : il ne dépend que de JUnit 4 pour les tests et compile avec un Maven standard.

À l'origine, il s'appuyait sur des bibliothèques fournies par le cours (interfaces, visualiseur et tests abstraits), hébergées sur un dépôt privé de l'université. Elles ont été remplacées par des interfaces, un visualiseur et des tests écrits pour ce dépôt.

## Contexte

Projet d'équipe réalisé à trois. Le squelette initial (configuration Maven, lanceur console et tests de base) a été fourni par l'enseignant du cours ; ces commits apparaissent dans l'historique Git sous l'auteur « Materiel de cours ».

## Auteur

Abdoul Wadoud Bello
