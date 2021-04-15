package com.company;

import com.company.visualization.GUI;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class GridWorld {
    public static int BOUNDARY = 5;

    private Agent agent = null;

    public enum Policy{
        PRANDOM,
        PEXPLOIT,
        PGREEDY
    }

    public enum LearningType{
        QLEARNING,
        SARSA
    }

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
        OperatorType(int i){
            index = i;
        }
    }

    public class Operator {
        public OperatorType opType;
        public float qTableValue;
    }

    public int[][] pdWorld = new int[5][5];
    public float[][] qTable = new float[50][6]; // 50 states; 6 actions: North, West, South, East, Pickup, Dropoff
    public CellType[][] cellType = new CellType[5][5];
    float alpha, gamma;
    int i, j, x, a, b, c, d, e, f;

    public GridWorld(){
        initWorld();
        //initGUI();
        agent = new Agent();

    }

    private void initWorld(){
        alpha = 1.0f;
        gamma = 1.0f;
        i = 4; //starting position, vertical
        j = 0; //starting position, horizontal
        x = 0; //1 if carrying block, 0 otherwise
        a = 0; //dropoff cell
        b = 0; //dropoff cell
        c = 0; //dropoff cell
        d = 8; //pickup cell
        e = 8; //pickup cell
        f = 0; //dropoff cell

        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
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

    public void setLearningParameters(float alpha, float gamma){
        this.alpha = alpha;
        this.gamma = gamma;
    }

    //reset world to initial state without resetting agent, Q table, alpha, or gamma
    public void resetWorld(){
        i = 4;
        j = 0;
        x = 0;
        a = 0;
        b = 0;
        c = 0;
        d = 8;
        e = 8;
        f = 0;
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 5; j++){
                pdWorld[i][j] = 0;
            }
        }
        pdWorld[2][4] = 8;
        pdWorld[3][1] = 8;
    }

    public void resetAgent(){
        agent = new Agent();
    }

    public void resetQTable(){
        for (int state = 0; state < 50; state++){
            for (int op = 0; op < 6; op++){
                qTable[state][op] = 0;
            }
        }
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
        if(isInsideBoundary(i - 1, j)){
            Operator operator = new Operator();
            OperatorType op = OperatorType.NORTH;
            operator.opType = op;
            operator.qTableValue = getQValue(i, j, x, op);
            availableOperators.add(operator);
        }
        if(isInsideBoundary(i + 1, j)){
            Operator operator = new Operator();
            OperatorType op = OperatorType.SOUTH;
            operator.opType = op;
            operator.qTableValue = getQValue(i, j, x, op);
            availableOperators.add(operator);
        }
        if(isInsideBoundary(i, j + 1)){
            Operator operator = new Operator();
            OperatorType op = OperatorType.EAST;
            operator.opType = op;
            operator.qTableValue = getQValue(i, j, x, op);
            availableOperators.add(operator);
        }
        if(isInsideBoundary(i, j - 1)){
            Operator operator = new Operator();
            OperatorType op = OperatorType.WEST;
            operator.opType = op;
            operator.qTableValue = getQValue(i, j, x, op);
            availableOperators.add(operator);
        }
        if (cellType[i][j] == CellType.PICKUP && x == 0 && pdWorld[i][j] > 0) {
            Operator operator = new Operator();
            OperatorType op = OperatorType.PICK;
            operator.opType = op;
            operator.qTableValue = getQValue(i, j, x, op);
            availableOperators.add(operator);
        } else if (cellType[i][j] == CellType.DROPOFF && x == 1 && pdWorld[i][j] < 4) {
            Operator operator = new Operator();
            OperatorType op = OperatorType.DROP;
            operator.opType = op;
            operator.qTableValue = getQValue(i, j, x, op);
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
            pdWorld[i][j] += 1;
            updateStatesToLetter();
            return true;
        } else {
            return false;
        }
    }

    public boolean pick(int i, int j, int x){
        if(x==0 && pdWorld[i][j] > 0 && cellType[i][j] == CellType.PICKUP){
            pdWorld[i][j] -= 1;
            updateStatesToLetter();
            return true;
        } else {
            return false;
        }
    }

    public void applyOperator(Operator operator){
       OperatorType operatorType = operator.opType;
        switch(operatorType){
            case NORTH:
                i--;
                break;
            case SOUTH:
                i++;
                break;
            case EAST:
                j++;
                break;
            case WEST:
                j--;
                break;
            case DROP:
                drop(i,j,x);
                x = 0;
                break;
            case PICK:
                pick(i,j,x);
                x = 1;
                break;
            default:
                break;
        }
    }

    public float calculateQValue(float q, float qPrime, int reward){
        return (1 - alpha) * q + alpha * (reward + gamma * qPrime);
    }

    public float calculateQValueSARSA(float q, float qPrime, int reward) {
        return q + alpha * (reward + gamma * qPrime - q);
    }

    private boolean isInsideBoundary(int i, int j){
        return (i >= 0 && i < BOUNDARY) && (j >= 0 && j < BOUNDARY);
    }

    private boolean isGoalState(){
        return a == 4 && b == 4 && c == 4 && f == 4 && d == 0 && e == 0;
    }

    public void printStep(int step, Operator op, int temp_i, int temp_j, int temp_x, float qValue){
        System.out.print("STEP = " + step + " ");
        System.out.print("ACTION -> " + op.opType.toString() + " (i=" + temp_i + ", j=" + temp_j + ", x = " + temp_x + ")");
        System.out.println(" qValue = " + qValue);
    }

    public void printQTable(OperatorType op){
        for (int x = 0; x < 2; x++){
            System.out.format("Q Table for operation %s with %d blocks:\n\n", op.toString(), x);
            for (int i = 0; i < 5; i++){
                for (int j = 0; j < 5; j++){
                    System.out.format("%6.4f  ", getQValue(i, j, x, op));
                }
                System.out.print("\n\n");
            }
        }
    }

    public void printAllQTables(){
        printQTable(OperatorType.NORTH);
        printQTable(OperatorType.EAST);
        printQTable(OperatorType.SOUTH);
        printQTable(OperatorType.WEST);
        printQTable(OperatorType.PICK);
        printQTable(OperatorType.DROP);
    }

    public void runExperimentOneA(){
        setLearningParameters(0.3f, 0.5f);
        resetAgent();
        resetWorld();
        resetQTable();
        for (int step = 1; step <= 6000; step++){
            run(Policy.PRANDOM, LearningType.QLEARNING, step);
            if (isGoalState()){
                System.out.println("REACHED GOAL STATE");
                resetWorld();
            }
        }
        printAllQTables();
    }

    public void runExperimentOneB(){
        setLearningParameters(0.3f, 0.5f);
        resetAgent();
        resetWorld();
        resetQTable();
        int rewardCollected=0;
        for (int step = 1; step <= 500; step++){
            rewardCollected += run(Policy.PRANDOM, LearningType.QLEARNING, step);
            if (isGoalState()){
                System.out.println("REACHED GOAL STATE");
                resetWorld();
            }
        }
        for (int step = 501; step <= 6000; step++){
            rewardCollected += run(Policy.PGREEDY, LearningType.QLEARNING, step);
            if (isGoalState()){
                System.out.println("REACHED GOAL STATE");
                resetWorld();
            }
        }
        System.out.println("REWARD COLLECTED " + rewardCollected);
        printAllQTables();
    }

    public void runExperimentOneC(){
        setLearningParameters(0.3f, 0.5f);
        resetAgent();
        resetWorld();
        resetQTable();
        for (int step = 1; step <= 500; step++){
            run(Policy.PRANDOM, LearningType.QLEARNING, step);
            if (isGoalState()){
                System.out.println("REACHED GOAL STATE");
                resetWorld();
            }
        }
        for (int step = 501; step <= 6000; step++){
            run(Policy.PEXPLOIT, LearningType.QLEARNING, step);
            if (isGoalState()){
                System.out.println("REACHED GOAL STATE");
                resetWorld();
            }
        }
        printAllQTables();
    }

    public void runExperimentTwo(){
        setLearningParameters(0.3f, 0.5f);
        resetAgent();
        resetWorld();
        resetQTable();
        int rewardCollected = 0;
        for (int step = 1; step <= 500; step++){
            rewardCollected += run(Policy.PRANDOM, LearningType.SARSA, step);
            if (isGoalState()){
                System.out.println("REACHED GOAL STATE");
                resetWorld();
            }
        }
        for (int step = 501; step <= 6000; step++){
            rewardCollected += run(Policy.PEXPLOIT, LearningType.SARSA, step);
            if (isGoalState()){
                System.out.println("REACHED GOAL STATE");
                resetWorld();
            }
        }
        System.out.println("REWARD COLLECTED " + rewardCollected);

        printAllQTables();
    }

    public void runExperimentThree(){
        setLearningParameters(0.15f, 0.45f);
        resetAgent();
        resetWorld();
        resetQTable();
        for (int step = 1; step <= 500; step++){
            run(Policy.PRANDOM, LearningType.QLEARNING, step);
            if (isGoalState()){
                System.out.println("REACHED GOAL STATE");
                resetWorld();
            }
        }
        for (int step = 501; step <= 6000; step++){
            run(Policy.PEXPLOIT, LearningType.QLEARNING, step);
            if (isGoalState()){
                System.out.println("REACHED GOAL STATE");
                resetWorld(); }
        }
        printAllQTables();
    }

    public void customExperiment(float alpha, float gamma){
        int rewardCollected = 0;
        setLearningParameters(alpha, gamma);
        resetAgent();
        resetWorld();
        resetQTable();
        for (int step = 1; step <= 500; step++){
            rewardCollected += run(Policy.PRANDOM, LearningType.SARSA, step);
            if (isGoalState()){
                System.out.println("REACHED GOAL STATE");
                resetWorld();
            }
        }
        for (int step = 501; step <= 6000; step++){
            rewardCollected += run(Policy.PGREEDY, LearningType.SARSA, step);
            if (isGoalState()){
                System.out.println("REACHED GOAL STATE");
                resetWorld();
            }
        }
        System.out.println("REWARD COLLECTED " + rewardCollected);
//        printAllQTables();
    }

    //run one step under provided policy and learning type
    public int run(Policy policy, LearningType learning_type, int step){
        //returns a collected reward
        int temp_i = i; //save current values of i, j, x to update Q(a,s)
        int temp_j = j;
        int temp_x = x;
        if(learning_type == LearningType.SARSA){
            ArrayList<Operator> ops = availableOperators(i, j, x); //find Q(a,s) with i, j, x
            Operator op = agent.chooseOperator(ops, policy); //choose operator based on provided policy
            float qValue = getQValue(i, j, x, op.opType);
            int reward = getReward(op.opType);
            applyOperator(op);
            ArrayList<Operator> opsPrime = availableOperators(i, j, x); //find Q(a',s') with new i, j, x
            Operator opPrime = agent.chooseOperator(opsPrime, Policy.PGREEDY); //choose max(Q(a',s')) with PGreedy
            float qValuePrime = getQValue(i, j, x, opPrime.opType);
            qValue = calculateQValueSARSA(qValue, qValuePrime, reward);
            updateQValue(temp_i, temp_j, temp_x, qValue, op.opType);
            printStep(step, op, temp_i, temp_j, temp_x, qValue);
            return reward;
        }
        else{
            ArrayList<Operator> ops = availableOperators(i, j, x); //find Q(a,s) with i, j, x
            Operator op = agent.chooseOperator(ops, policy); //choose operator based on provided policy
            float qValue = getQValue(i, j, x, op.opType);
            int reward = getReward(op.opType);
            applyOperator(op);
            ArrayList<Operator> opsPrime = availableOperators(i, j, x); //find Q(a',s') with new i, j, x
            Operator opPrime = agent.chooseOperator(opsPrime, Policy.PGREEDY); //choose max(Q(a',s')) with PGreedy
            float qValuePrime = getQValue(i, j, x, opPrime.opType);
            qValue = calculateQValue(qValue, qValuePrime, reward);
            updateQValue(temp_i, temp_j, temp_x, qValue, op.opType);
            printStep(step, op, temp_i, temp_j, temp_x, qValue);
            return reward;
        }
    }
//
    public void runDemo(Policy policy){
       //This function runs the agent until it reaches goal state
        int count = 1;
        resetAgent();
        resetWorld();
        GUI gui = new GUI(this);
        gui.updateWallEPosition(i, j);

        while(!isGoalState()){
            ArrayList<Operator> ops = availableOperators(i,j,x);
            Operator op = agent.chooseOperator(ops, policy);
            float qValue = getQValue(i,j,x, op.opType);

            applyOperator(op);

            printStep(count, op, i,j,x, qValue);
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
                TimeUnit.MILLISECONDS.sleep(100);
            } catch(InterruptedException e){
                System.out.println(e);
            }
        }
        System.out.println("REACHED GOAL STATE");
    }
}
