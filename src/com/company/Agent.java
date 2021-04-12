package com.company;

import java.util.Random;

class RLAgent {
    GridWorld gridWorld;
    Random random_generator;

    /*
    //This is where we define to run the experiments
    enum Policy{
        PRANDOM,
        PEXPLOIT,
        PGREEDY
    }

    public Policy policy;
     */

    public RLAgent(){
        //this.policy = policy;
        gridWorld = new GridWorld();
        random_generator = null; //Want to initialize new random generator for each experiment
    }

    //
    public void runExperimentOneA(){
        gridWorld.initWorld();
        gridWorld.setAlpha(0.3);
        gridWorld.setGamma(0.5);
        random_generator = new Random(System.currentTimeMillis());
        for (int i = 0; i < 500; i++){
            executePRandom();
            if (gridWorld.isGoalState()){
                gridWorld.resetWorld();
            }
        }
        gridWorld.printQTable();
    }

    private void executePGreedy(){

    }

    //executes one step of PRandom policy
    public void executePRandom(){
        int rand_int = random_generator.nextInt();
        //pickup block if pickup is valid
        if (gridWorld.pickupValid()){
            gridWorld.pickup();
        }
        //dropoff block if dropoff is valid
        else if (gridWorld.dropoffValid()){
            gridWorld.dropoff();
        }
        //go east or south at random if agent is at (1, 1) but dropoff is not valid
        else if (gridWorld.getI() == 0 && gridWorld.getJ() == 0){
            if (rand_int % 2 == 0){
                gridWorld.east();
            }
            else{
                gridWorld.south();
            }
        }
        //go south or west at random if agent is at (1, 5) but dropoff is not valid
        else if (gridWorld.getI() == 0 && gridWorld.getJ() == 4){
            if (rand_int % 2 == 0){
                gridWorld.south();
            }
            else{
                gridWorld.west();
            }
        }
        //go north or east at random if agent is at (5, 1)
        else if (gridWorld.getI() == 4 && gridWorld.getJ() == 0){
            if (rand_int % 2 == 0){
                gridWorld.north();
            }
            else{
                gridWorld.east();
            }
        }
        //go north or west at random if agent is at (5, 5) but dropoff is not valid
        else if (gridWorld.getI() == 4 && gridWorld.getJ() == 4){
            if (rand_int % 2 == 0){
                gridWorld.north();
            }
            else{
                gridWorld.west();
            }
        }
        //go east, south, or west at random if agent is at north edge
        else if (gridWorld.getI() == 0){
            if (rand_int % 3 == 0){
                gridWorld.east();
            }
            else if (rand_int % 3 == 1){
                gridWorld.south();
            }
            else{
                gridWorld.west();
            }
        }
        //go north, east, or west at random if agent is at south edge
        else if (gridWorld.getI() == 4){
            if (rand_int % 3 == 0){
                gridWorld.north();
            }
            else if (rand_int % 3 == 1){
                gridWorld.east();
            }
            else{
                gridWorld.west();
            }
        }
        //go east, south, or north at random if agent is at west edge
        else if (gridWorld.getJ() == 0){
            if (rand_int % 3 == 0){
                gridWorld.east();
            }
            else if (rand_int % 3 == 1){
                gridWorld.south();
            }
            else{
                gridWorld.north();
            }
        }
        //go north, south, or east at random if agent is at east edge
        else if (gridWorld.getJ() == 4){
            if (rand_int % 3 == 0){
                gridWorld.north();
            }
            else if (rand_int % 3 == 1){
                gridWorld.south();
            }
            else{
                gridWorld.west();
            }
        }
        //go north, east, south, or west at random if agent is not at an edge
        else{
            if (rand_int % 4 == 0){
                gridWorld.north();
            }
            else if (rand_int % 4 == 1){
                gridWorld.east();
            }
            else if (rand_int % 4 == 2){
                gridWorld.south();
            }
            else{
                gridWorld.west();
            }
        }
    }
}
