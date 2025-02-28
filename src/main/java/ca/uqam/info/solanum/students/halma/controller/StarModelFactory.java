package ca.uqam.info.solanum.students.halma.controller;

import ca.uqam.info.solanum.inf2050.f24halma.controller.ModelFactory;
import ca.uqam.info.solanum.inf2050.f24halma.model.Model;
import ca.uqam.info.solanum.students.halma.model.ModelImpl;

/**
 * blala
 */
public class StarModelFactory implements ModelFactory {
    /**
     * Le constructeur par defaut de StarModelFactory
     */
public StarModelFactory() {}
    @Override
    public Model createModel(int baseSize, String[] players){
        int nbPlayers = players.length;
        return new ModelImpl(baseSize, players, nbPlayers );
    }
}
