package ga_seatingchart;

import yagalib.Organism;

public class SeatingChart implements Organism<AssignmentStrategy> {

    private int sumOfWeights = 0;
    private AssignmentStrategy strategy = new AssignmentStrategy();

    public void setSumOfWeights(int sumOfWeights) {
        this.sumOfWeights = sumOfWeights;
    }

    public void setStrategy(AssignmentStrategy strategy) {
        this.strategy = strategy;
    }

    public Integer getFitness() {
        return sumOfWeights;
    }

    public void resetFitness() {
        sumOfWeights = 0;
    }

    public AssignmentStrategy getGenome() {
        return strategy;
    }

    public void pointMutate() {
        strategy.pointMutateOneGene();
    }

    public SeatingChart reproduceWith(Organism otherOrganism) {
        SeatingChart offspring = new SeatingChart();
        AssignmentStrategy newStrategy = (AssignmentStrategy)strategy.crossoverWith(otherOrganism.getGenome());
        offspring.setStrategy(newStrategy);
        return offspring;
    }

    public void complexifyGenome() {
        strategy.complexify();
    }

    public void simplifyGenome() {
        strategy.simplify();
    }
}
