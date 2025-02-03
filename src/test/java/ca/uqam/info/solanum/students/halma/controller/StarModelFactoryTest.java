package ca.uqam.info.solanum.students.halma.controller;

import ca.uqam.info.solanum.inf2050.f24halma.controller.AbstractStarModelFactoryTest;
import ca.uqam.info.solanum.inf2050.f24halma.controller.ModelFactory;

/**
 * This class must be implemented by students.
 */
public class StarModelFactoryTest extends AbstractStarModelFactoryTest {

  @Override
  public ModelFactory getModelFactory() {

    // TODO: Provide new Model factory object (your implementation).
    // Example (your factory constructor may be different)
    return new ModelFactory();
  }


@Test
  public void testStarPositions() {
    // Exemple d'appel à la méthode de création du modèle
    Model model = getModel(5);  // Remplace 5 par la taille de base que tu veux tester

    // Exemple d'assertion sur la position des étoiles
    assertNotNull(model);
    assertEquals(3, model.getStarCount());  // Assure-toi qu'il y a bien 3 étoiles
    assertEquals(0, model.getStarPosition("Alice").getX());  // Vérifie la position de "Alice"
    assertEquals(1, model.getStarPosition("Alice").getY());  // Vérifie la position de "Alice"

    // Ajoute d'autres assertions similaires pour tester les autres aspects du modèle
  }
}
