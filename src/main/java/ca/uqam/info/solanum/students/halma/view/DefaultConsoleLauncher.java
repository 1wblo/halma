package ca.uqam.info.solanum.students.halma.view;

import ca.uqam.info.solanum.students.halma.controller.StarModelFactory;
import org.halmaboard.api.ModelFactory;
import org.halmaboard.api.ModelReadOnly;
import org.halmaboard.view.SimpleConsoleVisualizer;
// TODO: create something implementing ModelFactory in controller package, and import it here

/**
 * Sample console launcher, to start TP code.
 */
public class DefaultConsoleLauncher {
  /**
   * Default Constructor.
   */
  public DefaultConsoleLauncher() {
  }

  /**
   * Main class for the console launcher.
   *
   * @param args no arguments required.
   */
  public static void main(String[] args) {
    runTp01();
    //    runTP01();
    //    runTP01();
  }

  private static void runTp01() {
    // Set default parameters
    int baseSize = 1;
    String[] playerNames = new String[] {"Alice", "Bob", "Charlie"};
    // Create a model (read only access) for the provided game parameters
    ModelFactory modelFactory =
        new StarModelFactory(); // TODO: Create this class in YOUR package and import/use it here.
    ModelReadOnly model = modelFactory.createModel(baseSize, playerNames);
    // Visualize initial model state
    boolean useColours = false;
    SimpleConsoleVisualizer visualizer = new SimpleConsoleVisualizer(useColours);
    System.out.println(visualizer.stringifyModel(model));
  }

  private static void runTp02() {
    // Will be released with TP02 instructions.
  }

  private static void runTp03() {
    // Will be released with TP02 instructions.
  }
}