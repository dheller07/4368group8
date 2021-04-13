package com.company;

import com.company.visualization.GUI;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class GridWorldSarsa {
    public GUI gui;

    //NOTE: This is a duplicate of GridWorld that was setup for SARSA Learning.
    //Given the time we have, I don't want conflicts we merge our code
    public static int BOUNDARY = 5;

    private SarsaLearningAgent agent = null;

    public enum CellType{
        NORMAL,
        PICKUP,
        DROPOFF
    }

    public enum OperatorType {
        NORTH(0),
        WEST(1),
        SOUTH(2),
        EAST(3),
        PICK(4),
        DROP(5);

        public final int index;
        private OperatorType(int i){
            index = i;
        }
    }

    class Operator {
        public OperatorType opType;
        public float qTableValue;
    }

    public int[][] pdWorld = new int[5][5];
    public float[][] qTable = new float[50][6]; // 50 states; 6 actions: North, West, South, East, Pickup, Dropoff
    public CellType[][] cellType = new CellType[5][5];

    public GridWorldSarsa(){
        initWorld();
        initGUI();
        agent = new SarsaLearningAgent(SarsaLearningAgent.Policy.PRANDOM);

    }

    float alpha, gamma;
    int j = 0; int i = 4; // start position
    int x = 0; // 1 agent carry block; 0 else
    int a,b,c,f = 0; // # blocks dropcell
    int d,e = 8; // # blocks pickup cell

    private void initWorld(){
        alpha = 0.5f;
        gamma = 0.5f;

        //value of 0 ==> normal
        //value of 1 ==> drop-off
        //value of 2 ==> pickup
        for(int i = 0;i<5;i++){
            for(int j = 0;j<5;j++){
                pdWorld[i][j] = 0;
                cellType[i][j] = CellType.NORMAL;
            }
        }
        cellType[0][0] = CellType.DROPOFF;
        cellType[0][4] = CellType.DROPOFF;
        cellType[2][2] = CellType.DROPOFF;
        cellType[4][4] = CellType.DROPOFF;
        cellType[2][4] = CellType.PICKUP;
        cellType[3][1] = CellType.PICKUP;

        //Initial number of blocks in each pickup cell
        pdWorld[2][4] = 8;
        pdWorld[3][1] = 8;

        updateStatesToLetter();
    }

    public float getQValue(int i, int j, int x, OperatorType op){
        int stateIndex = i * 5 + j + x * 25;
        float qValue = qTable[stateIndex][op.index];
        return qValue;
    }

    public void updateQValue(int i, int j, int x, float qValue, OperatorType op){
        int stateIndex = i * 5 + j + x * 25;
        qTable[stateIndex][op.index] = qValue;
    }

    public int getReward(OperatorType op){
        switch(op){
            case DROP:
            case PICK:
                return 13;
            default:
                return -1;
        }
    }

    public ArrayList<Operator> availableOperators(int i, int j, int x){
        ArrayList<Operator> availableOperators = new ArrayList<>();
        if(isInsideBoundary(i-1, j)){
            Operator operator = new Operator();
            OperatorType op = OperatorType.NORTH;
            operator.opType = op;
            operator.qTableValue = getQValue(i,j,x,op);
            availableOperators.add(operator);
        }
        if(isInsideBoundary(i+1, j)){
            Operator operator = new Operator();
            OperatorType op = OperatorType.SOUTH;
            operator.opType = op;
            operator.qTableValue = getQValue(i,j,x,op);
            availableOperators.add(operator);
        }
        if(isInsideBoundary(i, j+1)){
            Operator operator = new Operator();
            OperatorType op = OperatorType.EAST;
            operator.opType = op;
            operator.qTableValue = getQValue(i,j,x,op);
            availableOperators.add(operator);
        }
        if(isInsideBoundary(i, j-1)){
            Operator operator = new Operator();
            OperatorType op = OperatorType.WEST;
            operator.opType = op;
            operator.qTableValue = getQValue(i,j,x,op);
            availableOperators.add(operator);
        }
        if (cellType[i][j] == CellType.PICKUP && x == 0 && pdWorld[i][j] > 0) {
            Operator operator = new Operator();
            OperatorType op = OperatorType.PICK;
            operator.opType = op;
            operator.qTableValue = getQValue(i,j,x,op);
            availableOperators.add(operator);
        } else if (cellType[i][j] == CellType.DROPOFF && x == 1 && pdWorld[i][j] < 4) {
            Operator operator = new Operator();
            OperatorType op = OperatorType.DROP;
            operator.opType = op;
            operator.qTableValue = getQValue(i,j,x,op);
            availableOperators.add(operator);
        }
        return availableOperators;
    }

    public void updateStatesToLetter(){
        //Update the states in pdWorld array to a,b,c,d,e,f
        a = pdWorld[0][0];
        b = pdWorld[0][4];
        c = pdWorld[2][2];
        d = pdWorld[2][4];
        e = pdWorld[3][1];
        f = pdWorld[4][4];
    }

    public boolean drop(int i, int j, int x){
        if(x==1 && pdWorld[i][j] < 4 && cellType[i][j] == CellType.DROPOFF){
            pdWorld[i][j]+=1;
            updateStatesToLetter();
            return true;
        } else {
            return false;
        }
    }

    public boolean pick(int i, int j, int x){
        if(x==0 && pdWorld[i][j] > 0 && cellType[i][j] == CellType.PICKUP){
            pdWorld[i][j] -=1;
            updateStatesToLetter();
            return true;
        } else {
            return false;
        }

    }

    public int[] applyOperator(int i, int j, int x, Operator operator, boolean updateWorldState){
       OperatorType operatorType = operator.opType;
        switch(operatorType){
            case NORTH:
                i = i-1;
                break;
            case SOUTH:
                i = i+1;
                break;
            case EAST:
                j = j+1;
                break;
            case WEST:
                j = j-1;
                break;
            case DROP:
                if(updateWorldState){
                    drop(i,j,x);
                }
                x = 0;
                break;
            case PICK:
                if(updateWorldState){
                    pick(i,j,x);
                }
                x = 1;
                break;
            default:
                break;
        }

        if(updateWorldState){
            this.i = i;
            this.j = j;
            this.x = x;
        }

        int[] updatedStates ={i,j,x};
        return updatedStates;
    }

   public float calculateQValueSARSA(float q, float qPrime, int reward){
       return q + alpha * (reward + gamma * qPrime - q);
   }

    private boolean isInsideBoundary(int i, int j){
       if((i<0 || i >=BOUNDARY) || (j<0 || j>=BOUNDARY)){
           return false;
       }
       return true;
    }

    private boolean isGoalState(){
        if(a == 4 && b == 4 && c == 4 && f == 4 && d == 0 && e == 0){
            return true;
        } else{
            return false;
        }
    }

    public void run(){
        int count = 1;
        while(!isGoalState()){
            int temp_i = i;
            int temp_j = j;
            int temp_x = x;
            ArrayList<Operator> ops = availableOperators(temp_i, temp_j, temp_x);
            Operator op = agent.PGreedyPolicy(ops);
            float qValue = getQValue(temp_i, temp_j, temp_x, op.opType);

            int[] updatedStates = applyOperator(temp_i,temp_j,temp_x, op, false);
            int reward = getReward(op.opType);

            int temp_i_prime = updatedStates[0];
            int temp_j_prime = updatedStates[1];
            int temp_x_prime = updatedStates[2];
            ArrayList<Operator> opsPrime = availableOperators(temp_i_prime, temp_j_prime, temp_x_prime);
            Operator opPrime = agent.PGreedyPolicy(opsPrime); // Prime means next state of the this current state
            float qValuePrime = getQValue(temp_i_prime, temp_j_prime, temp_x_prime, opPrime.opType);


            qValue = calculateQValueSARSA(qValue, qValuePrime, reward);
            updateQValue(temp_i, temp_j, temp_x, qValue, op.opType);

            applyOperator(temp_i, temp_j, temp_x, op, true);

            System.out.print("STEP = " + count + " ");
            System.out.print("ACTION -> " + op.opType.toString() + " (i=" + temp_i + ", j=" + temp_j + ", x = " + temp_x + ")");
            System.out.println(" qValue = " + Float.toString(qValue));

            count+=1;

            //GUI
            if(op.opType == OperatorType.PICK){
                gui.wallEPickup();
                gui.updatePickupPackage(pdWorld[i][j]);
            } else if(op.opType == OperatorType.DROP) {
                gui.wallEDropoff();
                gui.updateDropoffPackage(pdWorld[i][j]);
            } else {
                gui.updateWallEPosition(i, j);
            }
            try{
                TimeUnit.MILLISECONDS.sleep(200);
            } catch(InterruptedException e){
                System.out.println(e.toString());
            }
        }
        System.out.println("REACHED GOAL STATE");
    }

    private void initGUI(){
        gui = new GUI(this);
    }
}
