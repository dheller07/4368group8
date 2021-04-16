package com.company;

public class Operator {

    public enum operator_type{
        NORTH,     //index 0
        EAST,      //index 1
        SOUTH,     //index 2
        WEST,      //index 3
        PICKUP,    //index 4
        DROPOFF    //index 5
    }

    private operator_type op_type;
    private int reward;
    private double q_value;

    public Operator(operator_type op_type, double q_value){
        this.op_type = op_type;
        if (op_type == operator_type.PICKUP || op_type == operator_type.DROPOFF){
            reward = 13;
        }
        else{
            reward = -1;
        }
        this.q_value = q_value;
    }

    public operator_type getOpType(){
        return op_type;
    }

    public int getReward(){
        return reward;
    }

    public double getQValue(){
        return q_value;
    }
}
