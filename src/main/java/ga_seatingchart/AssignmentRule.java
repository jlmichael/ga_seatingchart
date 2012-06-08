package ga_seatingchart;

import yagalib.EvolutionManager;
import yagalib.Gene;

import java.util.Map;

public class AssignmentRule implements Gene {

    private Cubicle cubicle;
    private Worker worker;

    public AssignmentRule(Worker worker, Cubicle cubicle) {
        this.cubicle = cubicle;
        this.worker = worker;
    }

    public Cubicle getCubicle() {
        return cubicle;
    }

    public void setCubicle(Cubicle cubicle) {
        this.cubicle = cubicle;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public void mutate() {
        if(EvolutionManager.random.nextInt(2) == 0) {
            worker = Office.getRandomWorker();
        } else {
            cubicle = Office.getRandomCubicle();
        }
    }

    public AssignmentRule generateRandomGene() {
        return new AssignmentRule(Office.getRandomWorker(), Office.getRandomCubicle());
    }
}
