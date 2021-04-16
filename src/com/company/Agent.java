package com.company;

import java.util.ArrayList;
import java.util.Random;

public class Agent {

    enum learning_type{
        QLEARNING,
        SARSA
    }

    private double[][][][] q_table;
    private double alpha, gamma;
    private learning_type learn_type;
    private Random random_generator;

    public Agent(double alpha, double gamma, learning_type learn_type){
        q_table = new double[5][5][2][6];
        for (int i = 0; i <= 4; i++){
            for (int j = 0; j <= 4; j++){
                for (int x = 0; x <= 1; x++){
                    for (int op = 0; op <= 5; op++){
                        q_table[i][j][x][op] = 0;
                    }
                }
            }
        }
        this.alpha = alpha;
        this.gamma = gamma;
        this.learn_type = learn_type;
        random_generator = new Random(System.currentTimeMillis());
    }

    public void updateQValue(int i, int j, int x, int a, int b, int c, int d, int e, int f, boolean reset, Operator op){
        switch (op.getOpType()){
            case NORTH:
                q_table[i][j][x][0] = (1 - alpha) * q_table[i][j][x][0] + alpha * (op.getReward()
                        + gamma * pMaxQValue(applicableOperators(i - 1, j, x, a, b, c, d, e, f, reset)).getQValue());
                break;
            case EAST:
                q_table[i][j][x][1] = (1 - alpha) * q_table[i][j][x][1] + alpha * (op.getReward()
                        + gamma * pMaxQValue(applicableOperators(i, j + 1, x, a, b, c, d, e, f, reset)).getQValue());
                break;
            case SOUTH:
                q_table[i][j][x][2] = (1 - alpha) * q_table[i][j][x][2] + alpha * (op.getReward()
                        + gamma * pMaxQValue(applicableOperators(i + 1, j, x, a, b, c, d, e, f, reset)).getQValue());
                break;
            case WEST:
                q_table[i][j][x][3] = (1 - alpha) * q_table[i][j][x][3] + alpha * (op.getReward()
                        + gamma * pMaxQValue(applicableOperators(i, j - 1, x, a, b, c, d, e, f, reset)).getQValue());
                break;
            case PICKUP:
                q_table[i][j][x][4] = (1 - alpha) * q_table[i][j][x][4] + alpha * (op.getReward()
                        + gamma * pMaxQValue(applicableOperators(i, j, x + 1, a, b, c, d, e, f, reset)).getQValue());
                break;
            case DROPOFF:
                q_table[i][j][x][5] = (1 - alpha) * q_table[i][j][x][5] + alpha * (op.getReward()
                        + gamma * pMaxQValue(applicableOperators(i, j, x - 1, a, b, c, d, e, f, reset)).getQValue());
                break;
            default:
                System.out.println("ERROR: No operator provided");
        }
    }

    public double getQValue(int i, int j, int x, Operator.operator_type op){
        switch (op){
            case NORTH:
                return q_table[i][j][x][0];
            case EAST:
                return q_table[i][j][x][1];
            case SOUTH:
                return q_table[i][j][x][2];
            case WEST:
                return q_table[i][j][x][3];
            case PICKUP:
                return q_table[i][j][x][4];
            case DROPOFF:
                return q_table[i][j][x][5];
            default:
                System.out.println("ERROR: No operator provided");
                return 0;
        }
    }

    public void printQTable(Operator.operator_type op){
        for (int x = 0; x <= 1; x++){
            System.out.format("Q Table for operation %s with %d blocks:\n\n", op.toString(), x);
            for (int i = 0; i <= 4; i++){
                for (int j = 0; j <= 4; j++){
                    System.out.format("%7.4f  ", getQValue(i, j, x, op));
                }
                System.out.print("\n\n");
            }
        }
    }

    public void printAllQTables(){
        printQTable(Operator.operator_type.NORTH);
        printQTable(Operator.operator_type.EAST);
        printQTable(Operator.operator_type.SOUTH);
        printQTable(Operator.operator_type.WEST);
        printQTable(Operator.operator_type.PICKUP);
        printQTable(Operator.operator_type.DROPOFF);
    }

    public ArrayList<Operator> applicableOperators(int i, int j, int x, int a, int b, int c, int d, int e, int f, boolean reset){
        ArrayList<Operator> apl_op = new ArrayList<>();
        if (i > 0){
            apl_op.add(new Operator(Operator.operator_type.NORTH, getQValue(i, j, x, Operator.operator_type.NORTH)));
        }
        if (j < 4){
            apl_op.add(new Operator(Operator.operator_type.EAST, getQValue(i, j, x, Operator.operator_type.EAST)));
        }
        if (i < 4){
            apl_op.add(new Operator(Operator.operator_type.SOUTH, getQValue(i, j, x, Operator.operator_type.SOUTH)));
        }
        if (j > 0){
            apl_op.add(new Operator(Operator.operator_type.WEST, getQValue(i, j, x, Operator.operator_type.WEST)));
        }
        if (reset){
            if (((e > 0 && (i == 0 && j == 2)) || (d > 0 && (i == 2 && j == 0))) && x == 0){
                apl_op.add(new Operator(Operator.operator_type.PICKUP, getQValue(i, j, x, Operator.operator_type.PICKUP)));
            }
        }
        else{
            if (((e > 0 && (i == 3 && j == 1)) || (d > 0 && (i == 2 && j == 4))) && x == 0){
                apl_op.add(new Operator(Operator.operator_type.PICKUP, getQValue(i, j, x, Operator.operator_type.PICKUP)));
            }
        }
        if (((a < 4 && (i == 0 && j == 0)) || (b < 4 && (i == 0 && j == 4)) || (c < 4 && (i == 2 && j == 2))
                || (f < 4 && (i == 4 && j == 4))) && x == 1){
            apl_op.add(new Operator(Operator.operator_type.DROPOFF, getQValue(i, j, x, Operator.operator_type.DROPOFF)));
        }
        return apl_op;
    }

    public Operator pMaxQValue(ArrayList<Operator> op_list){
        double max_q_value = op_list.get(0).getQValue();
        int max_index = 0;
        for (int i = 0; i < op_list.size(); i++){
            if (op_list.get(i).getQValue() > max_q_value){
                max_q_value = op_list.get(i).getQValue();
                max_index = i;
            }
        }
        return op_list.get(max_index);
    }

    public Operator pRandom(ArrayList<Operator> op_list){
        for (int i = 0; i < op_list.size(); i++){
            if (op_list.get(i).getOpType() == Operator.operator_type.PICKUP || op_list.get(i).getOpType() == Operator.operator_type.DROPOFF){
                return op_list.get(i);
            }
        }
        return op_list.get(random_generator.nextInt(op_list.size()));
    }

    public Operator pGreedy(ArrayList<Operator> op_list){
        for (int i = 0; i < op_list.size(); i++){
            if (op_list.get(i).getOpType() == Operator.operator_type.PICKUP || op_list.get(i).getOpType() == Operator.operator_type.DROPOFF){
                return op_list.get(i);
            }
        }
        return pMaxQValue(op_list);
    }

    public Operator pExploit(ArrayList<Operator> op_list){
        int rand_int = random_generator.nextInt();
        for (int i = 0; i < op_list.size(); i++){
            if (op_list.get(i).getOpType() == Operator.operator_type.PICKUP || op_list.get(i).getOpType() == Operator.operator_type.DROPOFF){
                return op_list.get(i);
            }
        }
        double max_q_value = op_list.get(0).getQValue();
        int max_index = 0;
        for (int i = 0; i < op_list.size(); i++){
            if (op_list.get(i).getQValue() > max_q_value){
                max_q_value = op_list.get(i).getQValue();
                max_index = i;
            }
        }
        if (rand_int % 5 == 0){
            op_list.remove(max_index);
            return op_list.get(random_generator.nextInt(op_list.size()));
        }
        else{
            return op_list.get(max_index);
        }
    }
}
