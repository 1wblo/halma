package ca.uqam.info.solanum.students.halma.view;

import ca.uqam.info.solanum.students.halma.controller.HalmaController;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import org.halmaboard.api.Field;
import org.halmaboard.api.ModelAccessConsistencyException;
import org.halmaboard.api.ModelReadOnly;
import org.halmaboard.api.Move;
import org.halmaboard.view.SimpleConsoleVisualizer;

/**
 * Partie de Halma jouee dans la console : affiche le plateau, liste les coups permis et lit le
 * choix du joueur courant.
 */
public class ConsoleGame {
  private static final String QUIT = "q";

  private final HalmaController controller;
  private final SimpleConsoleVisualizer visualizer;
  private final Scanner input;
  private final PrintStream output;

  /**
   * Cree une partie console.
   *
   * @param controller controleur de la partie.
   * @param visualizer visualiseur utilise pour dessiner le plateau.
   * @param input      source des choix des joueurs.
   * @param output     flux sur lequel la partie est affichee.
   */
  public ConsoleGame(HalmaController controller, SimpleConsoleVisualizer visualizer,
                     Scanner input, PrintStream output) {
    this.controller = controller;
    this.visualizer = visualizer;
    this.input = input;
    this.output = output;
  }

  /**
   * Joue la partie jusqu'a la victoire d'un joueur, l'abandon ou la fin de l'entree.
   *
   * @throws ModelAccessConsistencyException si le modele refuse un coup pourtant permis.
   */
  public void play() throws ModelAccessConsistencyException {
    ModelReadOnly model = controller.getModel();
    String[] names = model.getPlayerNames();
    int consecutiveBlocked = 0;
    while (!controller.isGameOver()) {
      String player = names[model.getCurrentPlayer()];
      List<Move> moves = controller.getPlayerMoves();
      if (moves.isEmpty()) {
        consecutiveBlocked++;
        if (consecutiveBlocked >= names.length) {
          output.println("Aucun joueur ne peut jouer : partie nulle.");
          return;
        }
        output.println(player + " ne peut pas jouer et passe son tour.");
        controller.passTurn();
        continue;
      }
      consecutiveBlocked = 0;
      output.println(visualizer.stringifyModel(model, true));
      printMoves(moves);
      Move move = readMove(player, moves);
      if (move == null) {
        output.println("Partie interrompue.");
        return;
      }
      controller.performMove(move);
    }
    output.println(visualizer.stringifyModel(model, true));
    output.println("Victoire de " + names[controller.getWinner()] + " !");
  }

  private void printMoves(List<Move> moves) {
    output.println("Coups permis (x,y) :");
    for (int i = 0; i < moves.size(); i++) {
      Move move = moves.get(i);
      output.printf("  %2d : %s -> %s%s%n", i + 1, describe(move.origin()),
          describe(move.target()), move.jump() ? "  (saut)" : "");
    }
  }

  private static String describe(Field field) {
    return "(" + field.x() + "," + field.y() + ")";
  }

  /**
   * Lit le choix du joueur jusqu'a obtenir un numero de coup valide.
   *
   * @param player nom du joueur courant.
   * @param moves  coups permis.
   * @return le coup choisi, ou null si le joueur quitte ou si l'entree est terminee.
   */
  private Move readMove(String player, List<Move> moves) {
    while (true) {
      output.print(player + ", ton coup [1-" + moves.size() + ", " + QUIT + " pour quitter] : ");
      output.flush();
      if (!input.hasNextLine()) {
        output.println();
        return null;
      }
      String line = input.nextLine().trim();
      if (line.equalsIgnoreCase(QUIT)) {
        return null;
      }
      try {
        int choice = Integer.parseInt(line);
        if (choice >= 1 && choice <= moves.size()) {
          return moves.get(choice - 1);
        }
      } catch (NumberFormatException e) {
        // Entree non numerique : on redemande.
      }
      output.println("Choix invalide.");
    }
  }
}
