package com.company;

import java.util.ArrayList;
import java.util.Random;

public class QLearningAgent {

    private Random random_generator;

    public QLearningAgent(){
        random_generator = new Random(System.currentTimeMillis());
    }

    public QGridWorld.Operator chooseOperator(ArrayList<QGridWorld.Operator> ops, QGridWorld.Policy policy){
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

    private QGridWorld.Operator PRandomPolicy(ArrayList<QGridWorld.Operator> ops){
        for(int i = 0; i < ops.size(); i++){
            if(ops.get(i).opType == QGridWorld.OperatorType.PICK || ops.get(i).opType == QGridWorld.OperatorType.DROP){
                return ops.get(i);
            }
        }
        return ops.get(random_generator.nextInt(ops.size()));
    }

    private QGridWorld.Operator PExploitPolicy(ArrayList<QGridWorld.Operator> ops){
        int rand_int = random_generator.nextInt();
        float maxQvalue = ops.get(0).qTableValue;
        ArrayList<QGridWorld.Operator> choice_ops = new ArrayList<>(); //Select subset of ops to return from

        for(int i = 0; i < ops.size(); i++){
            if(ops.get(i).opType == QGridWorld.OperatorType.PICK || ops.get(i).opType == QGridWorld.OperatorType.DROP){
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
        return choice_ops.get(random_generator.nextInt(choice_ops.size()));
    }

    private QGridWorld.Operator PGreedyPolicy(ArrayList<QGridWorld.Operator> ops){
        float maxQvalue = ops.get(0).qTableValue;
        ArrayList<QGridWorld.Operator> choice_ops = new ArrayList<>(); //Select subset of ops to return from

        for(int i = 0; i < ops.size(); i++){
            if(ops.get(i).opType == QGridWorld.OperatorType.PICK || ops.get(i).opType == QGridWorld.OperatorType.DROP){
                return ops.get(i);
            }
            if(ops.get(i).qTableValue > maxQvalue){
               maxQvalue = ops.get(i).qTableValue;
            }
        }
        for (int i = 0; i < ops.size(); i++){
            if (ops.get(i).qTableValue == maxQvalue){
                choice_ops.add(ops.get(i));
            }
        }
        return choice_ops.get(random_generator.nextInt(choice_ops.size()));
    }

}
