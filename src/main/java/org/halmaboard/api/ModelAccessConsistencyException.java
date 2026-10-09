package org.halmaboard.api;

/**
 * Levee lorsqu'une operation sur le modele viole une regle de coherence.
 */
public class ModelAccessConsistencyException extends Exception {
  /**
   * Cree l'exception avec un message descriptif.
   *
   * @param message description du probleme.
   */
  public ModelAccessConsistencyException(String message) {
    super(message);
  }
}
