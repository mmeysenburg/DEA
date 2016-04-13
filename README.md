## Synopsis 

Doane Evolutionary Algorithm (DEA): a simple, Java-coded framework for solving problems with Evolutionary Computation. Currently the framework supports genetic algorithms with three types of chromosomes: 

	* fixed-length binary, 
	* fixed-length integer, and 
	* fixed-length double. 
	
More options, including program-tree individuals for genetic programming, are planned. The framework automatically divides evaluation over multiple threads, to take advantage of modern processors with multiple cores.
	
The DEA was created at Doane University in the Doane Undergraduate Genetic Algorithms Lab (DUGAL), and has been used for several successful undergraduate research projects in EC. The framework has a simple architecture that makes it easily approachable for undergraduate research students, even those early in their university careers. 

The following text assumes you have basic knowledge of how genetic algoritms work. To read more, see [The Hitch-Hiker's Guide to Evolutionary Computation](http://www.faqs.org/faqs/ai-faq/genetic/part1/).

## Code Example

First, create an implementation of `edu.doane.dugal.dea.Problem` that defines the problem you're trying to solve with the framework. This class has to know how to do two things: 

	1. Create random potential solutions to the problem, and
	2. Evaluate a potential solution and determine its fitness

If the code should run from the command line, you may also include a main method in your `Problem` class. In the main method, create an instance of your problem, create instances of the EC operators you want to use, add all of that to an instance of the `DEA` class, and then start things off!
	
Here is a sample, for solving DeJong's first function. Note that the DEA is configured to maximize fitness values. 

```
package edu.doane.dugal.samples.functions;

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
 * (This is the negation of the standard DeJong's first function, since the 
 * DEA framework maximizes instead of minimizes.)
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
     */
    @Override
    public Individual createRandomIndividual() {
        // create a new Individual with three double chromosomes in the
        // range [-5.12, 5.12]; use 2 decimal points when printing 
        // fitness of the individual
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
        DEA alg = new DEA(dj01, 1000, 100); // 1000 population, 100 generations

        // create and add operators. First, crossover...
        alg.addOperator(new PointCrossover(0.65));
        alg.addOperator(new PointMutation(0.02));   // ... then mutation ...
        
        // ... then evaluation, using threshold of 10 individuals to
        // evaluate in-place instead of with threads ...
        alg.addOperator(new Evaluate(dj01, 10));    
        
        alg.addOperator(new ElitistTournamentSelection()); // ... then selection ...
        StandardStats stats = new StandardStats(2); // ... then statistics
        alg.addOperator(stats);

        // dump run parameters to standard output, so a successful run could be duplicated
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

The DEA framework was created to allow undergraduate research students to solve problems using evolutionary computation (EC) techniques with relative ease. In the simplest case, all one has to do is to create an instance of the `edu.doane.dugal.dea.Problem` class that can create random individuals and evaluate their fitness. The existing framework takes care of the rest, including built-in multithreading to speed up the evaluation process. The framework is also easy to extend: new types of individuals and new types of EC operators can be added to the existing class structure, allowing us to solve different types of problems. 

## Installation

To successfully build and run DEA, you first need to download and install

* The [Java Software Development Kit (SDK)](http://www.oracle.com/technetwork/java/javase/downloads/index.html), currently at least version 1.8; to compile the source code and run your programs
* The [Apache Ant](https://ant.apache.org/bindownload.cgi) build tool to control the compilation and packaging of the source code
* A git client to download the source in the first place

Create a source directory, perhaps called `DEA`, on your machine. Then, from a console session in that directory, execute

```
git clone https://github.com/mmeysenburg/DEA.git
```

That should download the DEA source. To build the software, `cd` to the directory created by the `git clone` command, and execute `ant` from the console. Ant will compile all of the source and package everything in to the `dist/DEA.jar` file. 

Execute the default sample program, currently the `DeJong01` class shown above, by executing the command `java -jar dist/DEA.jar`. To execute another sample, or one of your own problems that's been compiled into the jarfile, execute something like `java -cp dist/DEA.jar edu.doane.dugal.samples.functions.DeJong02`.

## API Reference

Run the `ant docs` command to locally produce the javadoc documentation for the DEA framework. 

To access the latest version of the documentation follow this link: [http://ist.doane.edu/dugal/docs/index.html].

## License

This code is distributed under the MIT license. See LICENSE.md for the full text of the license.