package ca.uqam.info.solanum.students.halma.view;

import ca.uqam.info.solanum.students.halma.controller.HalmaController;
import ca.uqam.info.solanum.students.halma.controller.StarModelFactory;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Scanner;
import org.halmaboard.api.Model;
import org.halmaboard.api.ModelAccessConsistencyException;
import org.halmaboard.api.ModelFactory;
import org.halmaboard.api.ModelInitializationException;
import org.halmaboard.view.SimpleConsoleVisualizer;

/**
 * Lanceur console : cree une partie de Halma et la fait jouer dans le terminal.
 */
public class DefaultConsoleLauncher {
  private static final int DEFAULT_BASE_SIZE = 3;
  private static final String[] DEFAULT_PLAYERS = {"Alice", "Bob", "Charlie"};

  /**
   * Default Constructor.
   */
  public DefaultConsoleLauncher() {
  }

  /**
   * Lance une partie. Sans argument, la partie se joue a trois sur un plateau de taille 3.
   *
   * @param args optionnel : la taille de base du plateau, suivie des noms de 3 ou 6 joueurs.
   * @throws ModelAccessConsistencyException si le modele refuse un coup pourtant permis.
   */
  public static void main(String[] args) throws ModelAccessConsistencyException {
    int baseSize = DEFAULT_BASE_SIZE;
    String[] playerNames = DEFAULT_PLAYERS;
    Model model;
    try {
      if (args.length > 0) {
        baseSize = Integer.parseInt(args[0]);
        playerNames = Arrays.copyOfRange(args, 1, args.length);
      }
      ModelFactory modelFactory = new StarModelFactory();
      model = modelFactory.createModel(baseSize, playerNames);
    } catch (IllegalArgumentException | ModelInitializationException e) {
      System.err.println("Parametres invalides : " + e.getMessage());
      System.err.println(
          "Usage : [tailleDeBase joueur1 joueur2 joueur3 [joueur4 joueur5 joueur6]]"
          + " (3 ou 6 joueurs)");
      return;
    }
    boolean useColours = System.console() != null;
    ConsoleGame game = new ConsoleGame(new HalmaController(model),
        new SimpleConsoleVisualizer(useColours),
        new Scanner(System.in, StandardCharsets.UTF_8), System.out);
    game.play();
  }
}
