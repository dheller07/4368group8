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
            for(int g = 0; g < 50; g++){
                for(int h = 0; h < 6; h++){
                    System.out.print((gridWorld.getPDWorld())[g][h] + " ");
                }
                System.out.println();
            }
        }
    }

    private void ExecutePGreedy(){

    }

    private void ExecutePExploit(){
        while(!gridWorld.isGoalState()){
            //check if on pickup block
            if(gridWorld.x == 0 && (gridWorld.getCellType())[gridWorld.i][gridWorld.j] == 2){
                //check if can pickup block
                gridWorld.pickUpBlock();
                gridWorld.qTableUpdate(gridWorld.x, 4, 13);
            }
            //check if can drop block
            else if(gridWorld.x == 0 && (gridWorld.getCellType())[gridWorld.i][gridWorld.j] == 1){
                //check if can drop block
                gridWorld.dropBlock();
                gridWorld.qTableUpdate(gridWorld.x, 5, 13);   
            }
            else{
                gridWorld.qTableUpdate(gridWorld.i,gridWorld.selectAction(), -1);
            }
        }
        //reset world after terminating
        gridWorld.resetPDWorld();
    }
}
