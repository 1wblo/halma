package org.halmaboard.api;

/**
 * Levee lorsqu'une case referencee n'existe pas ou n'est pas valide.
 */
public class FieldException extends RuntimeException {
  /**
   * Cree l'exception avec un message descriptif.
   *
   * @param message description du probleme.
   */
  public FieldException(String message) {
    super(message);
  }
}
