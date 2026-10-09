# Halma

Implémentation en Java du modèle de jeu **Halma** (variante en étoile, aussi connue sous le nom de « dames chinoises ») : génération du plateau, placement initial des pions de chaque joueur et affichage textuel de l'état de la partie dans la console.

Projet réalisé dans le cadre d'un cours universitaire de génie logiciel (hiver 2025).

## Ce que fait le projet

- Construit un plateau en étoile à six branches dont la taille dépend d'un paramètre `baseSize`.
- Calcule, pour chaque joueur, sa zone de départ et la zone cible située à l'opposé du plateau.
- Détermine les cases voisines d'une case ainsi que la case « au-delà » d'une voisine, nécessaire pour les sauts.
- Maintient l'état de la partie : noms des joueurs, joueur courant, cases occupées, avec validation des accès (case inexistante, case déjà occupée, index de joueur invalide).
- Affiche le plateau initial en mode texte dans la console.

## Architecture

Le code suit une organisation **MVC**, avec une **fabrique** (*Factory*) pour créer le modèle :

| Couche | Classe | Rôle |
|--------|--------|------|
| Modèle | `BoardImpl` | Géométrie du plateau : cases, zones de départ et zones cibles, voisinage. |
| Modèle | `ModelImpl` | État de la partie : joueurs, joueur courant, occupation des cases. |
| Contrôleur | `StarModelFactory` | Fabrique qui construit un modèle prêt à l'emploi à partir de la taille du plateau et des noms des joueurs. |
| Vue | `DefaultConsoleLauncher` | Point d'entrée : crée un modèle par la fabrique et l'affiche dans la console. |

Les classes implémentent des interfaces fournies par le cours (`Board`, `Model`, `ModelFactory`). La vue ne dépend que de la version en lecture seule du modèle (`ModelReadOnly`).

## Structure des dossiers

```
.
├── pom.xml                  Configuration Maven (compilation, Checkstyle, Javadoc, exécution)
├── google_checks.xml        Règles Checkstyle (style Google)
└── src
    ├── main/java/ca/uqam/info/solanum/students/halma
    │   ├── controller/      StarModelFactory
    │   ├── model/           BoardImpl, ModelImpl
    │   └── view/            DefaultConsoleLauncher
    └── test/java/ca/uqam/info/solanum/students/halma
        ├── controller/      StarModelFactoryTest
        └── model/           BoardImplTest, ModelImplTest, StarModelTest
```

## Lancer le projet

Prérequis : Java 21 et Maven.

```bash
mvn clean package   # compile, vérifie le style, exécute les tests, génère le jar et la Javadoc
mvn test            # exécute uniquement les tests unitaires
mvn exec:java       # lance DefaultConsoleLauncher et affiche le plateau
```

## Limite connue : dépendances du cours

Le projet dépend de deux bibliothèques fournies par le cours, `halmainterfaces` (interfaces et visualiseur textuel) et `halmatests` (tests abstraits), hébergées sur un dépôt Maven de l'université. **Sans accès à ce dépôt, le projet ne compile pas de façon autonome.** Ces bibliothèques ne m'appartiennent pas et ne sont donc pas incluses ici.

## Contexte

Projet d'équipe réalisé à trois. Le squelette initial (configuration Maven, lanceur console et tests de base) a été fourni par l'enseignant du cours ; l'historique Git conserve cette attribution.

## Auteur

Abdoul Wadoud Bello
