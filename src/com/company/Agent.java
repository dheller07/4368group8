package com.company;

class Agent {
    GridWorld gridWorld;

    //This is where we define to run the experiments
    enum Policy{
        PRANDOM,
        PEXPLOIT,
        PGREEDY
    }

    public Policy policy;

    public Agent(Policy policy){
        this.policy = policy;
        gridWorld = new GridWorld();
    }

    public void run(){

    }

    private void ExecutePGreedy(){

    }


}
