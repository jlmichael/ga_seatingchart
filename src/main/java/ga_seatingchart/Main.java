package ga_seatingchart;

import yagalib.EvolutionManager;
import yagalib.util.SampledFileStatWriter;
import yagalib.util.StatWriter;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        EvolutionManager em = new EvolutionManager();
        em.setEnvironment(new Office());
        em.setPopulation(100);
        em.setBirthRatePerThousand(100);
        em.setDeathRatePerThousand(100);
        em.setMutationChancePerThousand(100);
        em.setComplexifyChancePerThousand(0);
        em.setSimplifyChancePerThousand(0);
        StatWriter writer = new SampledFileStatWriter("/tmp/seatingchart", 100);
        writer.init();
        em.setWriter(writer);
        em.evolve(10000);

        List<SeatingChart> charts = em.getEnvironment().getOrganisms();
        System.out.println("Best organism genome was: " + charts.get(0).getGenome());
    }
}
