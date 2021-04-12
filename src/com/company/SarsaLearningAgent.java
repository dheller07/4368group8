package com.company;

import java.util.ArrayList;

public class SarsaLearningAgent {
    enum Policy{
        PRANDOM,
        PEXPLOIT,
        PGREEDY
    }

    private Policy policy;

    public SarsaLearningAgent(Policy policy){
        this.policy = policy;
    }

    public GridWorldSarsa.Operator PGreedyPolicy(ArrayList<GridWorldSarsa.Operator> ops){
        //Chose the operator

       float maxQvalue = ops.get(0).qTableValue;
       int maxIndex = 0;

       for(int i = 1;i<ops.size();i++){
           if(ops.get(i).opType == GridWorldSarsa.OperatorType.PICK || ops.get(i).opType== GridWorldSarsa.OperatorType.DROP){
               return ops.get(i);
           }
           if(ops.get(i).qTableValue > maxQvalue){
               maxQvalue = ops.get(i).qTableValue;
               maxIndex = i;
           }
       }

        return ops.get(maxIndex);
    }



}
