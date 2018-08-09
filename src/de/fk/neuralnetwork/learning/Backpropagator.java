package de.fk.neuralnetwork.learning;

import de.fk.neuralnetwork.NeuralLayer;
import de.fk.neuralnetwork.training.TrainingSupplier;
import de.fk.neuralnetwork.NeuralNetwork;
import de.fk.neuralnetwork.NeuralNetworkState;
import de.fk.neuralnetwork.math.NeuralMath;
import de.fk.neuralnetwork.training.TrainingExample;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.util.Pair;

/**
 *
 * @author Felix
 */
public class Backpropagator {
    
    public static final int SAVE_EVERY_X_ITERATIONS = 10;
    public static final double ADAPTIVE_LEARNING_RATE_DOWN_MIN = 0.7,
            ADAPTIVE_LEARNING_RATE_UP_MIN = 1.025,
            ADAPTIVE_LEARNING_RATE_DOWN_MAX = 0.9,
            ADAPTIVE_LEARNING_RATE_UP_MAX = 1.15;

    private NeuralNetwork net;
    private double learningRate, regularizationRate, momentum;
    private boolean stopped, training, adaptiveLREnabled;
    private Runnable learningRateUpdated = null;
    private Consumer<Pair<Double, Double>> trainingProgressUpdated = null;
    
    public Backpropagator(NeuralNetwork net, double learningRate, double regularizationRate, double momentum) {
        this.net = net;
        this.learningRate = learningRate;
        this.regularizationRate = regularizationRate;
        this.momentum = momentum;
        this.stopped = false;
        this.training = false;
        this.adaptiveLREnabled = true;
    }

    public void setLearningRate(double learningRate) {
        this.learningRate = learningRate;
    }

    public double getLearningRate() {
        return learningRate;
    }

    public void setNet(NeuralNetwork net) {
        this.net = net;
    }

    public void setLearningRateUpdated(Runnable learningRateUpdated) {
        this.learningRateUpdated = learningRateUpdated;
    }

    public void setAdaptiveLearningRateEnabled(boolean adaptiveLREnabled) {
        this.adaptiveLREnabled = adaptiveLREnabled;
    }

    public void setTrainingProgressUpdated(Consumer<Pair<Double, Double>> trainingProgressUpdated) {
        this.trainingProgressUpdated = trainingProgressUpdated;
    }
    
    private double error = 0.0, lastError = -1.0, accuracy = 0.0;
    private int iteration = 0;
    
    /**
     * Startet den Trainingsvorgang.
     *
     * @param trainingSupplier TrainingSupplier mit Trainingsbeispielen
     * @param iterations Wiederholungen aller Trainingsbeispiele
     * @param errorLoggerStream OutputStream auf dem Iteration und Fehlerrate durch Leerzeichen separiert zeilenweise ausgegeben werden
     * @param logEveryXIterations Bestimmt, aller wie vieler Iterationen eine Ausgabe erfolgen soll
     * @return Trainingsthread
     * @throws IllegalStateException Wenn bereits ein Trainingsthread läuft
     * @see Backpropagator#stopTraining() 
     */
    public Thread train(TrainingSupplier trainingSupplier, int iterations, OutputStream errorLoggerStream, int logEveryXIterations) throws IllegalStateException {
        if(training) throw new IllegalStateException("Es wird bereits trainiert.");
        stopped = false;
        training = true;
        Thread trainThread;
        (trainThread = new Thread(() -> {
        
            int exampleCount = trainingSupplier.getExampleCount(), logIt = logEveryXIterations;
            int example;
            long startTime = System.currentTimeMillis();
            trainingSupplier.reset();
            TrainingExample[] originalTrainingExamples = trainingSupplier.originalTrainingExamples();
            System.out.println("Transformation: " + ((System.currentTimeMillis() / startTime) / (double) exampleCount) + " ms/example");
            net.prepareParallelBackprop(1);
            System.out.println("Training with " + exampleCount + " examples per iteration.");
            error = 0.0;
            NeuralLayer[] layers = net.getLayers();
            
            //Trainingsschleife
            for(iteration = 0; !stopped && iteration < iterations; iteration++, logIt++) {
                
                //Backpropagation; Alle Trainingsbeispiele ansehen
                System.out.print("Backpropagating...");
                for(example = 0; example < exampleCount; example++) {
                    TrainingExample ex = trainingSupplier.nextTrainingExample();
                    backpropStepParallel(layers, ex);
                    //Lernen/Gewichte updaten
                    for(NeuralLayer l : layers) l.accumulate(learningRate, regularizationRate, momentum);
                    if(stopped) break;
                }
                //error /= exampleCount;
                //accuracy /= exampleCount;
                //Forward Propagation wiederholen, um Error & Accuracy zu bestimmen
                lastError = error;
                error = 0.0;
                accuracy = 0.0;
                System.out.println("Determining error and accuracy...");
                for(TrainingExample originalTrainingExample : originalTrainingExamples) {
                    double[] out = net.triggerParallel(originalTrainingExample.getIn()).getOutput();
                    error += NeuralMath.getRegularizedError(out, originalTrainingExample.getOut(), regularizationRate, net);
                    if (NeuralMath.getPredictedLabel(out) == NeuralMath.getPredictedLabel(originalTrainingExample.getOut())) {
                        accuracy++;
                    }
                }
                error /= originalTrainingExamples.length;
                accuracy /= originalTrainingExamples.length;
                if(trainingProgressUpdated != null) trainingProgressUpdated.accept(new Pair<>(error, accuracy));
                
                //Logging
                if(logIt == logEveryXIterations) try {
                    logIt = 0;
                    double time = (System.currentTimeMillis() - startTime) / 1000.0;
                    byte[] out = ((iteration + 1) + " " + error + " " + accuracy + " " + time + "\r\n").getBytes("UTF-8");
                    if(errorLoggerStream != null) errorLoggerStream.write(out);
                    System.out.write(out);
                    //Kopie speichern
                    /*ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("300-100_" + iteration + ".net")));
                    oos.writeObject(net);
                    oos.close();*/
                } catch (IOException ex) {}
                
                //Adaptive Lernrate
                if(adaptiveLREnabled && lastError > 0.0) {
                    if(lastError > error) learningRate *= Math.max(ADAPTIVE_LEARNING_RATE_UP_MIN, Math.min(ADAPTIVE_LEARNING_RATE_UP_MAX, lastError / error));
                    else learningRate *= Math.max(ADAPTIVE_LEARNING_RATE_DOWN_MIN, Math.min(ADAPTIVE_LEARNING_RATE_DOWN_MAX, lastError / error));
                    if(learningRateUpdated != null) learningRateUpdated.run();
                    System.out.println("Lernrate angepasst: " + learningRate);
                }
                if(stopped) break;
            }
            //Ende der Schleife
            System.out.println("Trained for " + iterations + " iterations. Error: " + error);
            this.training = false;
            if(errorLoggerStream != null) try {
                errorLoggerStream.close();
            } catch (IOException ex) {
                Logger.getLogger(Backpropagator.class.getName()).log(Level.SEVERE, null, ex);
            }
        })).start();
        trainThread.setUncaughtExceptionHandler((Thread t, Throwable e) -> {
            e.printStackTrace();
            training = false;
        });
        return trainThread;
    }
    
    private TrainingExample[][] examples;
    private CyclicBarrier trainingBarrier;
    
    protected class TrainingRunnable implements Runnable {
        
        private CyclicBarrier trainingBarrier;
        private int threadId;

        public TrainingRunnable(CyclicBarrier trainingBarrier, int threadId) {
            this.trainingBarrier = trainingBarrier;
            this.threadId = threadId;
        }

        @Override
        public void run() {
            while(training) {
                for(TrainingExample threadExample : examples[threadId]) {
                    backpropStep(threadExample, threadId);
                }
                try {
                    trainingBarrier.await();
                } catch (InterruptedException | BrokenBarrierException ex) {
                    Logger.getLogger(Backpropagator.class.getName()).log(Level.SEVERE, null, ex);
                    break;
                }
            }
            System.out.println("Trainingsthread #" + threadId + " angehalten!");
        }
        
    }
    
    /**
     * Startet den Trainingsvorgang (Multi-Threading-fähig).
     *
     * @param trainingSupplier TrainingSupplier mit Trainingsbeispielen
     * @param iterations Wiederholungen (pro Wiederholung werden threadCount * examplesPerThread Trainigsbeispiele verwendet)
     * @param errorLoggerStream OutputStream auf dem Iteration und Fehlerrate durch Leerzeichen separiert zeilenweise ausgegeben werden
     * @param threadCount Anzahl der zu verwendenden Backpropagation-Threads
     * @param examplesPerThread Trainingsbeispiele, die jeder Thread für die Backpropagation verwenden soll (empfohlen: examplesCount / threadCount; sollte nicht größer sein, da sonst Beispiele von mehreren Threads gleichzeitig trainiert werden)
     * @param staticExamples true, wenn für jeden Thread dauerhaft dieselben Trainingsbeispiele verwendet werden sollen (nur bei einer konstanten, endlichen Anzahl an Trainingsbeispielen sinnvoll)
     * @throws IllegalStateException Wenn bereits trainiert wird
     * @see Backpropagator#stopTraining() 
     */
    public void trainParallel(TrainingSupplier trainingSupplier, int iterations, OutputStream errorLoggerStream, int threadCount, int examplesPerThread, boolean staticExamples) throws IllegalStateException {
        if(training) throw new IllegalStateException("Es wird bereits trainiert.");
        training = true;
        stopped = false;
        //Initialisieren
        final long startTime = System.currentTimeMillis();
        int exampleCount = trainingSupplier.getExampleCount(), fullTrainingCycle = exampleCount / (examplesPerThread * threadCount);
        net.prepareParallelBackprop(threadCount);
        examples = new TrainingExample[threadCount][examplesPerThread];
        error = 0.0;
        accuracy = 0.0;
        lastError = -1.0;
        iteration = 0;
        trainingSupplier.reset();
        TrainingExample[] originalTrainingExamples = trainingSupplier.originalTrainingExamples();
        //Trainingsbeispiele laden
        for(int t = 0; t < threadCount; t++) {
            examples[t] = trainingSupplier.nextTrainingExamples(examplesPerThread);
            //System.out.println("Example 1 for thread #" + t + " out: " + Arrays.toString(examples[t][0].getOut()) + " In: " + Arrays.toString(examples[t][0].getIn()));
        }
        //CyclicBarrier erstellen
        trainingBarrier = new CyclicBarrier(threadCount, () -> {
            iteration++;
            //Lernen/Gewichte updaten
            Arrays.stream(net.getLayers()).forEach(l -> l.accumulate(learningRate, regularizationRate, momentum));
            
            //Alle Beispiele angesehen
            if(iteration % fullTrainingCycle == 0) {
                //Forward Propagation wiederholen, um Error & Accuracy zu bestimmen
                lastError = error;
                error = 0.0;
                accuracy = 0.0;
                System.out.println("Determining error and accuracy...");
                for(TrainingExample originalTrainingExample : originalTrainingExamples) {
                    double[] out = net.triggerParallel(originalTrainingExample.getIn()).getOutput();
                    error += NeuralMath.getRegularizedError(out, originalTrainingExample.getOut(), regularizationRate, net);
                    if (NeuralMath.getPredictedLabel(out) == NeuralMath.getPredictedLabel(originalTrainingExample.getOut())) {
                        accuracy++;
                    }
                }
                error /= originalTrainingExamples.length;
                accuracy /= originalTrainingExamples.length;
                if(trainingProgressUpdated != null) trainingProgressUpdated.accept(new Pair<>(error, accuracy));
                
                //Logging
                try {
                    double time = (System.currentTimeMillis() - startTime) / 1000.0;
                    byte[] out = (iteration + " " + error + " " + accuracy + " " + time + "\r\n").getBytes("UTF-8");
                    if(errorLoggerStream != null) errorLoggerStream.write(out);
                    System.out.write(out);
                    /*if(iteration % SAVE_EVERY_X_ITERATIONS == 0) {
                        //Kopie speichern
                        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File("300-100_" + iteration + ".net")));
                        oos.writeObject(net);
                        oos.close();
                    }*/
                } catch (IOException ex) {}
                //Adaptive Lernrate
                if(adaptiveLREnabled && lastError > 0.0) {
                    if(lastError > error) learningRate *= Math.max(ADAPTIVE_LEARNING_RATE_UP_MIN, Math.min(ADAPTIVE_LEARNING_RATE_UP_MAX, lastError / error));
                    else learningRate *= Math.max(ADAPTIVE_LEARNING_RATE_DOWN_MIN, Math.min(ADAPTIVE_LEARNING_RATE_DOWN_MAX, lastError / error));
                    if(learningRateUpdated != null) learningRateUpdated.run();
                    System.out.println("Lernrate angepasst: " + learningRate);
                }
            }
            //Überprüfen ob Training gestoppt
            if(stopped || iteration >= iterations) {
                System.out.println("Trainiert für " + iterations + " Iterationen.");
                training = false;
                if(errorLoggerStream != null) try {
                    errorLoggerStream.close();
                } catch (IOException ex) {
                    Logger.getLogger(Backpropagator.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            //Neue Trainingsbeispiele laden
            if(!staticExamples) {
                for(int t = 0; t < threadCount; t++) {
                    examples[t] = trainingSupplier.nextTrainingExamples(examplesPerThread);
                    //System.out.println("Example 1 for thread #" + t + " out: " + Arrays.toString(examples[t][0].getOut()) + " In: " + Arrays.toString(examples[t][0].getIn()));
                }
            }
        });
        
        //Threads erstellen
        for(int t = 0; t < threadCount; t++) new Thread(new TrainingRunnable(trainingBarrier, t), "TrainingThread#" + t).start();
    }
    
    /**
     * Unterbricht das Training bei der nächsten Iteration.
     * 
     * Gibt true zurück, wenn ein Training-Thread lief und dieser zum Anhalten
     * aufgefordert wurde, sonst false.
     *
     * @return Erfolg
     */
    public boolean stopTraining() {
        return training && (stopped = true);
    }
    
    /**
     * Führt einen Backprop-Schritt aus und gibt die Netzausgabe zurück (die
     * Gewichte werden nicht geupdatet!).
     * 
     * Sollte nur für Batch Gradient Descent verwendet werden.
     *
     * @param trainingExample
     * @param threadId ID des ausführenden Threads (bei Single-Threading 0)
     * @return
     * @see Backpropagator#backpropStepParallel(de.fk.neuralnetwork.training.TrainingExample) Für Stochastic Gradient Descent
     */
    public double[] backpropStep(TrainingExample trainingExample, int threadId) {
        NeuralLayer[] layers = net.getLayers();
        //Sammeln der Trainingsdaten
        double[] input = trainingExample.getIn(), expectedOutput = trainingExample.getOut();
        //Aktivierungen berechnen
        NeuralNetworkState out = net.trigger(input);
        //System.out.println("Training for " + Arrays.toString(input) + " -> " + Arrays.toString(expectedOutput));
        //Output Layer
        NeuralLayer outputLayer = layers[layers.length - 1];
        //Berechne Errors & Error Deltas
        double[] activationsBefore = layers.length > 1 ? out.getLayerActivations(layers.length - 2) : NeuralMath.addBias(input),
                errors = NeuralMath.getErrors(out.getOutput(), expectedOutput),
                errorDeltas = outputLayer.getErrorDeltas(errors, activationsBefore);
        //Berechne Accumulators
        outputLayer.calcAccumulatorMatrices(errorDeltas, activationsBefore, threadId);
        //Hidden Layer
        for(int i = layers.length - 2; i >= 0; i--) {
            //Berechne Errors & Error Deltas
            activationsBefore = i > 0 ? out.getLayerActivations(i - 1) : NeuralMath.addBias(input);
            errors = layers[i].getErrors(layers[i + 1], errorDeltas);
            errorDeltas = layers[i].getErrorDeltas(errors, activationsBefore);
            //Berechne Accumulators
            layers[i].calcAccumulatorMatrices(errorDeltas, activationsBefore, threadId);
        }
        //Fehler berechnen
        return out.getOutput();
    }
    
    /**
     * Führt einen Backprop-Schritt aus und gibt die Netzausgabe zurück (die
     * Gewichte werden nicht geupdatet!) (nutzt parallele Streams).
     * 
     * Sollte nur für Stochastic Gradient Descent verwendet werden.
     *
     * @param layers
     * @param trainingExample
     * @return Fehler E
     * @see Backpropagator#backpropStep(de.fk.neuralnetwork.training.TrainingExample, int) Für paralleles Lernen (Batch Gradient Descent)
     */
    public double[] backpropStepParallel(NeuralLayer[] layers, TrainingExample trainingExample) {
        //Sammeln der Trainingsdaten
        double[] input = trainingExample.getIn(), expectedOutput = trainingExample.getOut();
        //Aktivierungen berechnen
        NeuralNetworkState out = net.triggerParallel(input);
        //System.out.println("Training for " + Arrays.toString(input) + " -> " + Arrays.toString(expectedOutput));
        //Output Layer
        NeuralLayer outputLayer = layers[layers.length - 1];
        //Berechne Errors & Error Deltas
        double[] activationsBefore = layers.length > 1 ? out.getLayerActivations(layers.length - 2) : NeuralMath.addBias(input),
                errors = NeuralMath.getErrors(out.getOutput(), expectedOutput),
                errorDeltas = outputLayer.getErrorDeltas(errors, activationsBefore);
        //Berechne Accumulators
        outputLayer.calcAccumulatorMatrices(errorDeltas, activationsBefore, 0);
        //Hidden Layer
        for(int i = layers.length - 2; i >= 0; i--) {
            //Berechne Errors & Error Deltas
            activationsBefore = i > 0 ? out.getLayerActivations(i - 1) : NeuralMath.addBias(input);
            errors = layers[i].getErrorsParallel(layers[i + 1], errorDeltas);
            errorDeltas = layers[i].getErrorDeltas(errors, activationsBefore);
            //Berechne Accumulators
            layers[i].calcAccumulatorMatrices(errorDeltas, activationsBefore, 0);
        }
        //Fehler berechnen
        return out.getOutput();
    }
    
}
