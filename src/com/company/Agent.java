package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Agent {

    private Random random_generator;

    public Agent(){
        random_generator = new Random(System.currentTimeMillis());
    }

    public GridWorld.Operator chooseOperator(ArrayList<GridWorld.Operator> ops, GridWorld.Policy policy){
        switch (policy){
            case PRANDOM:
                return PRandomPolicy(ops);
            case PEXPLOIT:
                return PExploitPolicy(ops);
            case PGREEDY:
                return PGreedyPolicy(ops);
            default:
                System.out.println("ERROR: Policy not selected");
                return PRandomPolicy(ops);
        }
    }

    private GridWorld.Operator PRandomPolicy(ArrayList<GridWorld.Operator> ops){
        for(int i = 0; i < ops.size(); i++){
            if(ops.get(i).opType == GridWorld.OperatorType.PICK || ops.get(i).opType == GridWorld.OperatorType.DROP){
                return ops.get(i);
            }
        }
        return ops.get(random_generator.nextInt(ops.size()));
    }

    private GridWorld.Operator PExploitPolicy(ArrayList<GridWorld.Operator> ops){
        int rand_int = random_generator.nextInt();
        float maxQvalue = ops.get(0).qTableValue;
        ArrayList<GridWorld.Operator> choice_ops = new ArrayList<>(); //Select subset of ops to return from

        for(int i = 0; i < ops.size(); i++){
            if(ops.get(i).opType == GridWorld.OperatorType.PICK || ops.get(i).opType == GridWorld.OperatorType.DROP){
                return ops.get(i);
            }
            if(ops.get(i).qTableValue > maxQvalue){
                maxQvalue = ops.get(i).qTableValue;
            }
        }
        if (rand_int % 5 == 0){
            for (int i = 0; i < ops.size(); i++){
                if (ops.get(i).qTableValue < maxQvalue){
                    choice_ops.add(ops.get(i));
                }
            }
        }
        else{
            for (int i = 0; i < ops.size(); i++){
                if (ops.get(i).qTableValue == maxQvalue){
                    choice_ops.add(ops.get(i));
                }
            }
        }
        try{
            return choice_ops.get(random_generator.nextInt(choice_ops.size()));
        } catch (Exception e){
            System.out.println("SIZE " + choice_ops.size());
            throw e;
        }


    }

    private GridWorld.Operator PGreedyPolicy(ArrayList<GridWorld.Operator> ops){
        float maxQvalue = ops.get(0).qTableValue;
        int maxIndex = 0;
        ArrayList<GridWorld.Operator> choice_ops = new ArrayList<>(); //Select subset of ops to return from

        for(int i = 0; i < ops.size(); i++){
            if(ops.get(i).opType == GridWorld.OperatorType.PICK || ops.get(i).opType == GridWorld.OperatorType.DROP){
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
