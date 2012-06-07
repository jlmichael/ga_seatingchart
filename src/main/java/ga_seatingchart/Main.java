package ga_seatingchart;

import yagalib.EvolutionManager;

public class Main {

    public static void main(String[] args) {
        EvolutionManager em = new EvolutionManager();
        em.setEnvironment(new Office());
    }
}
