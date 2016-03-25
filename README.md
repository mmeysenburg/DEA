## Synopsis 

Doane Evolutionary Algorithm (DEA): a simple, Java-coded framework for solving problems with Evolutionary Computation. Currently the framework supports genetic algorithms with three types of chromosomes: 

	* fixed-length binary, 
	* fixed-length integer, and 
	* fixed-length double. 
	
DEA was created at Doane University in the Doane Undergraduate Genetic Algorithms Lab (DUGAL), and has been used for several successful undergraduate research projects in EC. The framework has a simple architecture that makes it easily approachable for undergraduate research students, even those early in their university careers. 

The following text assumes you have basic knowledge of how genetic algoritms work. To read more, see [The Hitch-Hiker's Guide to Evolutionary Computation](http://www.faqs.org/faqs/ai-faq/genetic/part1/).

## Code Example

First, create an implementation of `edu.doane.dea.Problem` that defines the problem you're trying to solve with the framework. This class has to know how to do two things: 

	1. Create random potential solutions to the problem, and
	2. Evaluate a potential solution and determine its fitness

If the code should run from the command line, you may also include a main method in your `Problem` class. 
	
Here is a sample, for solving DeJong's first function. Note that the DEA is configured to maximize fitness values. 

```
package edu.doane.dugal.samples.dejong01;

import edu.doane.dugal.dea.Individual;
import edu.doane.dugal.dea.Problem;
import edu.doane.dugal.dea.kits.dchrom.DoubleChromosome;
import edu.doane.dugal.dea.kits.dchrom.PointCrossover;
import edu.doane.dugal.dea.kits.dchrom.PointMutation;
import edu.doane.dugal.dea.kits.general.Evaluate;
import edu.doane.dugal.dea.kits.general.StandardStats;
import edu.doane.dugal.dea.DEA;
import edu.doane.dugal.dea.kits.general.ElitistTournamentSelection;

/**
 * Sample DEA application: DeJong's first function, using double chromosomes.
 * Maximize the function
 *
 * f_1 = -\sum_{i = 1}^{3} x_{i}^{2}, -5.12 \leq x_{i} 5.12
 *
 * @author Mark M. Meysenburg
 * @version 12/26/2014
 */
public class DeJong01 implements Problem {

    /**
     * Create a random potential solution to the problem.
     *
     * @return A DoubleChromosome Individual, with three genes, each in [-5.12,
     * 5.12].
     *
     */
    @Override
    public Individual createRandomIndividual() {
        return new DoubleChromosome(3, -5.12, 5.12, 2);
    }

    /**
     * Evaluate a potential solution to the problem. Determine the individual's
     * fitness, and set the individual's fitness value.
     *
     * @param ind A DoubleChromosome Individual, with three genes, each in
     * [-5.12, 5.12].
     */
    @Override
    public void evaluateIndividual(Individual ind) {
        DoubleChromosome d = (DoubleChromosome) ind;
        double f = 0.0;
        for (int i = 0; i < 3; i++) {
            f += d.getGene(i) * d.getGene(i);
        }

        d.setFitness(-f);
    }

    @Override
    public String toString() {
        return "DeJong's first fucntion (DeJong01)";
    }

    /**
     * Application entry point for console-based execution.
     *
     * @param args Command-line arguments; ignored by this application.
     */
    public static void main(String[] args) {

        // to run with a specific seed, set it before doing anything else;
        // otherwise, the first access to the PRNG object will cause it to
        // be seeded based on the system time.
        // PRNG p = PRNG.getInstance();
        // p.setSeed(1209432115);
        // create problem and algorithm
        Problem dj01 = new DeJong01();
        DEA alg = new DEA(dj01, 100, 100); // 1000 population, 100 generations

        // create and add operators. First, crossover...
        alg.addOperator(new PointCrossover());

        // ... then mutation ...
        alg.addOperator(new PointMutation());

        // ... then evaluation ...
        alg.addOperator(new Evaluate(dj01));

        // ... then selection ...
        alg.addOperator(new ElitistTournamentSelection());

        // ... then statistics
        StandardStats stats = new StandardStats(2);
        alg.addOperator(stats);

        // dump run parameters to standard output, so a successful run
        // could be duplicated
        System.out.println(alg.getTableau());

        // start the DEA thread
        alg.start();

        // wait for the DEA thread to complete before reporting final
        // statistics
        try {
            alg.join();
        } catch (InterruptedException ex) {
            System.err.println("DEA thread interrupted!");
        } finally {
            System.out.println("Best ever individual: " + stats.getBestEverIndividual());
            System.out.printf("Best ever fitness: %.2f\n", stats.getBestEverIndividual().getFitness());
        }
    } // main

}
```	

## Motivation

TODO: A short description of the motivation behind the creation and maintenance of the project. This should explain why the project exists.

## Installation

TODO: Provide code examples and explanations of how to get the project.

## API Reference

TODO: Depending on the size of the project, if it is small and simple enough the reference docs can be added to the README. For medium size to larger projects it is important to at least provide a link to where the API reference docs live.

## Tests

TODO: Describe and show how to run the tests with code examples.

## Contributors

TODO: Let people know how they can dive into the project, include important links to things like issue trackers, irc, twitter accounts if applicable.

## TODO: License

This code is distributed under the MIT license. See LICENSE.md for the full text of the license.