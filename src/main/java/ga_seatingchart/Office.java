package ga_seatingchart;

import org.yaml.snakeyaml.Yaml;
import yagalib.Environment;
import yagalib.EvolutionManager;

import java.util.*;

public class Office implements Environment<SeatingChart> {

    private List<SeatingChart> seatingCharts = new ArrayList<SeatingChart>();
    private static Map<Cubicle, Map<Cubicle, Integer>> cubicleDistances = new HashMap<Cubicle, Map<Cubicle, Integer>>();
    private static Map<Worker, Map<Worker, Integer>> workerRelationships = new HashMap<Worker, Map<Worker, Integer>>();

    static {
        Yaml yaml = new Yaml();
        cubicleDistances = (Map<Cubicle, Map<Cubicle, Integer>>)yaml.load(Office.class.getClassLoader()
                .getResourceAsStream("cubedistances.yaml"));
        workerRelationships = (Map<Worker, Map<Worker, Integer>>)yaml.load(Office.class.getClassLoader()
                .getResourceAsStream("workerrelationships.yaml"));
    }

    public static Worker getRandomWorker() {
        List<Worker> workers = new ArrayList<Worker>();
        workers.addAll(workerRelationships.keySet());
        return workers.get(EvolutionManager.random.nextInt(workers.size()));
    }

    public static Cubicle getRandomCubicle() {
        List<Cubicle> cubicles = new ArrayList<Cubicle>();
        cubicles.addAll(cubicleDistances.keySet());
        return cubicles.get(EvolutionManager.random.nextInt(cubicles.size()));
    }
    
    public void populateWithOrganisms(Integer population) {
        for(int i = 0; i < population; i++) {
            seatingCharts.add(new SeatingChart());
        }
    }

    public void reset() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public List<SeatingChart> getOrganisms() {
        return seatingCharts;
    }

    public void clearOrganisms() {
        seatingCharts.clear();
    }

    public void doWorkOnOrganisms() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
