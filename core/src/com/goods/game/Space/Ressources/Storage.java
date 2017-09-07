package com.goods.game.Space.Ressources;

/**
 * Created by Higgy on 07.09.2017.
 */

public class Storage {

    private Form form = Form.Loose;
    private float storageSpace = 1000;
    private float currentAmount = 0;
    private RessourceType ressourceType;

    public Storage(Form form, float storageSpace) {
        this.form = form;
        this.storageSpace = storageSpace;
    }

    // gibt die differenz zurück:
    // Mehr laden als möglich : load(500) platz 200 => return 300
    // Weniger laden als möglich : load(500) platz 1000 => return 0
    // voll return amount
    // fehler = -1f
    public float load(RessourceType type, float amount){
        if (ressourceType == type || ressourceType == null){
            ressourceType = type;
            if (this.form == type.getForm()){
                if (isFull()){
                    // full
                    return amount;
                }else{
                    if (amount > storageSpace - currentAmount){
                        // lade was rein passt gibt rest zurück
                        amount -= (storageSpace - currentAmount);
                        currentAmount = storageSpace;
                    }else{
                        // Passt alles rein
                        currentAmount += amount;
                        amount = 0f;
                    }

                    return amount;
                }
            }else{
                // wrong form
                return amount;
            }
        }else{
            // wrong Ressourcetype
            return amount;
        }
    }

    public float unload(RessourceType type, float amount){
        if (this.ressourceType == type){
            if (amount > currentAmount) {
                // mehr verfügbar als benötigt
                return currentAmount;
            }else{
                // weniger verfügbar als gewünscht
                currentAmount -= amount;
                return amount;
                }
        }else{
            // wrong form
            return amount;
        }
    }

    public void removeLoad(){
        currentAmount = 0f;
    }

    public float getWeight(){
        return currentAmount * ressourceType.getWeight();
    }

    public float getCurrentAmount(){
        return currentAmount;
    }

    public boolean isFull(){
        return currentAmount == storageSpace ? true: false;
    }
}
