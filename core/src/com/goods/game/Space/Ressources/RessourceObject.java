package com.goods.game.Space.Ressources;

/**
 * Created by Higgy on 31.08.2017.
 */

public class RessourceObject {
    private double deposite;
    private double ressourceTypeFactor;
    private RessourceType type;

    public RessourceObject(RessourceType type, double ressourceTypeFactor) {
        this.type = type;
        this.ressourceTypeFactor = ressourceTypeFactor;
    }

    public double getDeposite() {
        return deposite;
    }

    public void setDeposite(double deposite) {
        this.deposite = deposite * ressourceTypeFactor;
    }

    @Override
    public String toString() {
        return type.name()+":"+String.format("%1$,.0f", deposite);
    }
}
