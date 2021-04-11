package com.company;

class RLAgent {
    GridWorld gridWorld;

    //This is where we define to run the experiments
    enum Policy{
        PRANDOM,
        PEXPLOIT,
        PGREEDY
    }

    public Policy policy;

    public RLAgent(Policy policy){
        this.policy = policy;
        gridWorld = new GridWorld();
    }

    public void run(){
        if(this.policy == Policy.PEXPLOIT){
            //set gamma and alpha based on experiment #
            gridWorld.setAlphaGamma(.3,.5); //Experiment 1
            ExecutePExploit();
        }
    }

    private void ExecutePGreedy(){

    }

    private void ExecutePExploit(){
        int steps = 0;
        while(steps < 6000){
            while(!gridWorld.isGoalState()){
                if(gridWorld.x == 0){
                    //check if can pickup block
                }
                //check if can drop block
                else if(gridWorld.x == 1){
                }
                steps++;
            }
            gridWorld.resetPDWorld();
        }
    }
}
