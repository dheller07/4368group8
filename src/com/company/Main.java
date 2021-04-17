package com.company;

public class Main {

    public static void main(String[] args) {
        GridWorld world = new GridWorld();
//        world.wallEAnimation(0.15f, 0.8f, GridWorld.policy.PEXPLOIT, Agent.learning_type.SARSA);

        //Run each experiment twice
        System.out.println("########################");
        System.out.println("RUNNING EXPERIMENT 1A");
        System.out.println("########################");
        System.out.println("Experiment 1a) 1st run");
        world.experiment1A(false);
        System.out.println();
        System.out.println("Experiment 1a) 2nd run");
        world.experiment1A(false);
        System.out.println("########################");
        System.out.println("RUNNING EXPERIMENT 1B");
        System.out.println("########################");
        System.out.println("Experiment 1b) 1st run");
        world.experiment1B(false);
        System.out.println();
        System.out.println("Experiment 1b) 2nd run");
        world.experiment1B(false);
        System.out.println("########################");
        System.out.println("RUNNING EXPERIMENT 1C");
        System.out.println("########################");
        System.out.println("Experiment 1c) 1st run");
        world.experiment1C(false);
        System.out.println();
        System.out.println("Experiment 1c) 2nd run");
        world.experiment1C(false);
        System.out.println("########################");
        System.out.println("RUNNING EXPERIMENT 2");
        System.out.println("########################");
        System.out.println("Experiment 2) 1st run");
        world.experiment2(false);
        System.out.println("Experiment 2) 2nd run");
        world.experiment2(false);
        System.out.println("########################");
        System.out.println("RUNNING EXPERIMENT 3");
        System.out.println("########################");
        System.out.println("Experiment 3A) 1st run");
        world.experiment3A(false);
        System.out.println("Experiment 3A) 2nd run");
        world.experiment3A(false);
        System.out.println("Experiment 3B) 1st run");
        world.experiment3B(false);
        System.out.println("Experiment 3B) 2nd run");
        world.experiment3B(false);
        System.out.println("########################");
        System.out.println("RUNNING EXPERIMENT 4");
        System.out.println("########################");
        System.out.println("Experiment 4) 1st run");
        world.experiment4(false);
        System.out.println("Experiment 4) 2nd run");
        world.experiment4(false);

    }
}