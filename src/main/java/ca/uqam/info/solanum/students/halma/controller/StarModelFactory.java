package ca.uqam.info.solanum.students.halma.model;

import ca.uqam.info.solanum.inf2050.f24halma.controller.ModelFactory;
import ca.uqam.info.solanum.inf2050.f24halma.model.Board;
import ca.uqam.info.solanum.inf2050.f24halma.model.Field;
import ca.uqam.info.solanum.inf2050.f24halma.model.Model;
import ca.uqam.info.solanum.inf2050.f24halma.model.ModelInitializationException;

import java.util.*;




public class StarModelFactory implements ModelFactory {
    @Override
    public Model createModel(int baseSize, String[] players) {
        // Vérifie si le nombre de joueur est correct.
        if (players.length != 3 && players.length != 6) {
            throw new ModelInitializationException("Star board requires 3 or 6 players");
        }

        // Calculdes dimensions du plateau.
        int largeur = baseSize * 3;  // largeur du plateau
        int height = baseSize * 4 + 1;  // Hauteur du plateau (ordonnée)
        int centre = largeur / 2;

        // Créé des ensembles pour stocker les cases du plateau
        Set<Field> allFields = new HashSet<>(); // stockage de toutes les cases fields du plateau
        Map<Integer, Set<Field>> homeFields = new HashMap<>(); //
        Map<Integer, Set<Field>> targetFields = new HashMap<>();

        // Initialise chaque zone
        for (int i = 0; i < players.length; i++) {
            homeFields.put(i, new HashSet<>());
            targetFields.put(i, new HashSet<>());
        }

        //  Crée un plateau de jeu en forme d'étoile et assigne les cases aux zones des joueurs.
        plateauEtoile(baseSize, largeur, height, centre, allFields, homeFields, targetFields);

        Board board = new BoardImpl(allFields, homeFields, targetFields);

        return new ModelImpl(board, players);
    }

    /**
     * Crée toutes les cases valides pour le plateau en forme d'étoile et les ajoute
     *  aux collections de champs appropriées.
     * @param baseSize      taille du plateau
     * @param large         largeur du plateau
     * @param hauteur       hauteur du plateau
     * @param centre        position du centre
     * @param allFields     ensemble contenant toutes les cases
     * @param homeFields    zone de départ
     * @param targetFields  zone cible
     */
    private void plateauEtoile (int baseSize, int large, int hauteur, int centre,
                               Set<Field> allFields,
                               Map<Integer, Set<Field>> homeFields,
                               Map<Integer, Set<Field>> targetFields) {
        // Crée toutes les cases valides

        for (int y = 0; y < hauteur; y++) {
            for (int x = 0; x < large; x++) {
                if (positionValide(x, y, large, hauteur, baseSize)) {
                    Field field = new Field(x, y);
                    allFields.add(field);


                    FieldToZone(field, x, y, large, hauteur, baseSize, homeFields, targetFields);
                }
            }
        }
    }
    /**
     * Vérifie si une position est valide en fonction des dimensions et des règles du plateau.
     * @param x        La position en x sur le plateau.
     * @param y        La position en y sur le plateau.
     * @param larg     La largeur du plateau.
     * @param haut     La hauteur du plateau.
     * @param baseSize La taille de la base.
     * @return true si la position est valide, sinon false.
     */
    private boolean positionValide(int x, int y, int larg, int haut, int baseSize) {
        int centre = larg / 2;

        // triangle du haut
        if (y < haut / 3) {
            int distanceFromCenter = Math.abs(x - centre);
            return distanceFromCenter <= (y + baseSize);
        }
        // triangle du bas
        else if (y >= haut * 2 / 3) {
            // triangle situé en bas à gauche
            if (x <= centre) {
return x <= baseSize + (haut - y);
            }
            // triangle situé en bas à droite
            else {
                return (larg - x - 1) <= baseSize + (haut - y);
            }
        }

        else {
            int distanceCentre = Math.abs(x - centre);
            return distanceCentre <= baseSize + 1;
        }
    }
    /**
     * Attribue les cases à des zones de départ et de cible pour chaque joueur sur le plateau.
     * @param field       La case à attribuer.
     * @param x           La position en x sur le plateau.
     * @param y           La position en y sur le plateau.
     * @param largeur     La largeur du plateau.
     * @param hauteur     La hauteur du plateau.
     * @param baseSize    La taille de la base.
     * @param homeFields  Un map associant une zone de départ à ses cases valides.
     * @param targetFields Un map associant une zone cible à ses cases valides.
     */
    private void FieldToZone(Field field, int x, int y, int largeur, int hauteur, int baseSize,
                             Map<Integer, Set<Field>> homeFields,
                             Map<Integer, Set<Field>> targetFields) {
        int centerX = largeur / 2;

        // Donner des cases de départ pour chaque joueur"

        if (y < baseSize && Math.abs(x - centerX) <= baseSize) {
            homeFields.computeIfAbsent(1, k -> new HashSet<>()).add(field);

            if (homeFields.size() > 3) homeFields.get(4).add(field);
        }
   if (y >= hauteur - baseSize && x <= baseSize) {
                homeFields.computeIfAbsent(0, k -> new HashSet<>()).add(field);

            if (homeFields.size() > 3) homeFields.get(3).add(field);
        }


        if (y >= hauteur - baseSize && x >= largeur - baseSize - 1) {
            homeFields.computeIfAbsent(2, k -> new HashSet<>()).add(field);

            if (homeFields.size() > 3) homeFields.get(5).add(field);
        }

        // Donner des cases cibles pour chaque joueur

        for (int i = 0; i < homeFields.size(); i++) {
            if (homeFields.get((i + 2) % homeFields.size()).contains(field)) {
                targetFields.get(i).add(field);
            }
        }
    }
}
