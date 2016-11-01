package geneticalgorithmassignmentt;

import java.util.Arrays;

/**
 *
 * @author Rhys
 */
public class GeneticAlgorithmAssignmentt {
    
    private double mutationRate;
    private double crossoverRate;

    public GeneticAlgorithmAssignmentt(double mutationRate, double crossoverRate) {
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
    }
    
    //Tested
    public boolean conditionMet(Population population) {
        for (Individual individual : population.getIndividuals()) {
            if (individual.getFitness() == 1) {
                return true;
            }
        }
        return false;
    }

    //Tested
    public double calcFitness(Individual individual) {

        int noOnes = 0;

        for (int i = 0; i < individual.size(); i++) {
            if (individual.getGene(i) == 1) {
                noOnes++;

            }

        }
        double fitness = (double) noOnes / individual.size();

        individual.setFitness(fitness);

        return fitness;
    }
    
    public void calcPopulationFitness(Population population) {

        double populationFitness = 0;
        for (Individual individual : population.getIndividuals()) {
            populationFitness += calcFitness(individual);
        }
        population.setPopulationFitness(populationFitness);

    }

    //Tested
    public Individual crossover(Individual parent1, Individual parent2) {

        int crossoverPoint = (int) (Math.random() * parent1.size());
        // System.out.println(parent1 + " " + parent2 + " " + crossoverPoint);
        Individual offspring = new Individual();

        for (int i = 0; i < parent1.size(); i++) {
            if (i < crossoverPoint) {
                offspring.setGene(i, (byte) parent1.getGene(i));
            } else {
                offspring.setGene(i, (byte) parent2.getGene(i));
            }
        }

        return offspring;
    }
    
    //Tested
    public Individual tournementSelection (Population population, int size){
       
        Population tour = new Population(size);
        for (int i = 0; i < size; i++) {
            tour.saveIndividual(i, population.getIndividual((int) (Math.random() * population.size())));
            calcFitness(tour.getIndividual(i));
        }
        
        return tour.getFittest();
    }

    //Tested now replaced by tournementSelection
    public Individual parentSelect(Population population) {

        Individual best = new Individual();
        Individual individual1 = population.getIndividual((int) (Math.random() * population.size()));
        Individual individual2 = population.getIndividual((int) (Math.random() * population.size()));
        //System.out.println("Parent1: " + parent1);
        //System.out.println("Parent2: " + parent2);

        if (individual1.getFitness() >= individual2.getFitness()) {
            best = individual1;
        } else {
            best = individual2;
        }
        return best;
    }

    //Tested
    public void mutate(Individual individual) {

        for (int i = 0; i < individual.size(); i++) {

            if (mutationRate > Math.random()) {
                byte newGene = 1;
                if (individual.getGene(i) == 1) {
                    newGene = 0;
                }
                individual.setGene(i, newGene);

            }
            //System.out.println(individual);
        }

    }

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        
        GeneticAlgorithmAssignmentt ga = new GeneticAlgorithmAssignmentt(0.002, 0.8);
        Population newPop = new Population(50);
        

        int generationNo = 1;
        
        while (ga.conditionMet(newPop) == false) {
            for (Individual individual : newPop.getIndividuals()) {
                ga.calcFitness(individual);
                
            }
            
            
            Population selected = new Population(newPop.size());
            for (int i = 0; i < newPop.size(); i++) {
                Individual best = ga.tournementSelection(newPop, 5);
                selected.saveIndividual(i, best);
               
            }
            
            Population crossoverPop = new Population(newPop.size());
            crossoverPop.saveIndividual(1, selected.getFittest());
            

            for (int i = 1; i < newPop.size(); i++) {
                if (Math.random()< ga.crossoverRate){
                Individual crossover = ga.crossover(selected.getFittest(), ga.tournementSelection(selected, 5));
                crossoverPop.saveIndividual(i, crossover);
               }else{
                crossoverPop.saveIndividual(i, selected.getFittest());
                }

            }
            
            for (int i = 0; i < newPop.size(); i++) {
                ga.mutate(crossoverPop.getIndividual(i));
                ga.calcFitness(crossoverPop.getIndividual(i));

            }
            newPop = crossoverPop;
            ga.calcPopulationFitness(newPop);
            System.out.println(newPop.getPopulationFitness());
            generationNo ++;
        }
        for (Individual individual : newPop.getIndividuals()) {
            
            System.out.println(individual.getFitness() + " " + individual.toString());
        }
        System.out.println("Generation: " + generationNo );

    }

}
    