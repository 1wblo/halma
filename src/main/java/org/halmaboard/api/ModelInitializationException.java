package org.halmaboard.api;

/**
 * Levee lorsque les parametres d'initialisation du modele sont invalides.
 */
public class ModelInitializationException extends RuntimeException {
  /**
   * Cree l'exception avec un message descriptif.
   *
   * @param message description du probleme.
   */
  public ModelInitializationException(String message) {
    super(message);
  }
}
