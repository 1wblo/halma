package org.halmaboard.api;

/**
 * Represente le deplacement d'un pion d'une case vers une autre.
 *
 * @param origin case de depart du pion.
 * @param target case d'arrivee du pion.
 * @param jump   true si le deplacement est un saut (ou une suite de sauts) par-dessus des pions.
 */
public record Move(Field origin, Field target, boolean jump) {
}
