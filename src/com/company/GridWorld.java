package com.company;

import java.util.ArrayList;

public class GridWorld {

    private int i, j, x, a, b, c, d, e, f;
    private boolean reset_pickup;
    private Operator prev_op;
    private Agent agent;

    enum policy {
        PRANDOM,
        PGREEDY,
        PEXPLOIT
    }

    public GridWorld() {
        initWorld();
        reset_pickup = false;
        prev_op = null;
        agent = null;
    }

    public void initWorld() {
        i = 4;
        j = 0;
        x = 0;
        a = 0; //tile (0, 0)
        b = 0; //tile (0, 4)
        c = 0; //tile (2, 2)
        d = 8; //tile (2, 4) //reset (2, 0)
        e = 8; //tile (3, 1) //reset (0, 2)
        f = 0; //tile (4, 4)
    }

    public void resetPickup() {
        reset_pickup = true;
    }

    public void applyOperator(Operator op) {
        switch (op.getOpType()) {
            case NORTH:
                i--;
                break;
            case EAST:
                j++;
                break;
            case SOUTH:
                i++;
                break;
            case WEST:
                j--;
                break;
            case PICKUP:
                if (reset_pickup) {
                    if (i == 2 && j == 0) {
                        d--;
                    } else if (i == 0 && j == 2) {
                        e--;
                    } else {
                        System.out.println("ERROR: Illegal pickup operation performed");
                    }
                } else {
                    if (i == 2 && j == 4) {
                        d--;
                    } else if (i == 3 && j == 1) {
                        e--;
                    } else {
                        System.out.println("ERROR: Illegal pickup operation performed");
                    }
                }
                x++;
                break;
            case DROPOFF:
                if (i == 0 && j == 0) {
                    a++;
                } else if (i == 0 && j == 4) {
                    b++;
                } else if (i == 2 && j == 2) {
                    c++;
                } else if (i == 4 && j == 4) {
                    f++;
                } else {
                    System.out.println("ERROR: Illegal dropoff operation performed");
                }
                x--;
                break;
            default:
                System.out.println("ERROR: No operator provided");
        }
    }

    private boolean isGoalState() {
        return a == 4 && b == 4 && c == 4 && d == 0 && e == 0 && f == 4;
    }

    public void run(policy p) {
        ArrayList<Operator> op_list = agent.applicableOperators(i, j, x, a, b, c, d, e, f, reset_pickup);
        Operator op = null;
        switch (p) {
            case PRANDOM:
                op = agent.pRandom(op_list);
                break;
            case PGREEDY:
                op = agent.pGreedy(op_list);
                break;
            case PEXPLOIT:
                op = agent.pExploit(op_list);
                break;
            default:
                System.out.println("ERROR: Policy not provided");
        }
        if (agent.getLearningType() == Agent.learning_type.SARSA){
            if (prev_op != null){
                agent.updateQValueSarsa(i, j, x, prev_op, op);
            }
        }
        else{
            agent.updateQValue(i, j, x, a, b, c, d, e, f, reset_pickup, op);
        }
        applyOperator(op);
        prev_op = op;
    }

    public void experiment1A() {
        agent = new Agent(0.3, 0.5, Agent.learning_type.QLEARNING);
        int reward = 0;
        for (int z = 1; z < 6000; z++) {
            run(policy.PRANDOM);
            reward += prev_op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, prev_op.getOpType().toString(), prev_op.getQValue());
        }
        agent.printAllQTables();
        System.out.format("Reward: %d\n", reward);
    }

    public void experiment1B() {
        agent = new Agent(0.3, 0.5, Agent.learning_type.QLEARNING);
        int reward = 0;
        for (int z = 1; z < 500; z++) {
            run(policy.PRANDOM);
            reward += prev_op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, prev_op.getOpType().toString(), prev_op.getQValue());
        }
        for (int z = 501; z < 6000; z++) {
            run(policy.PGREEDY);
            reward += prev_op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, prev_op.getOpType().toString(), prev_op.getQValue());
        }
        agent.printAllQTables();
        System.out.format("Reward: %d\n", reward);
    }

    public void experiment1C() {
        agent = new Agent(0.3, 0.5, Agent.learning_type.QLEARNING);
        int reward = 0;
        for (int z = 1; z < 500; z++) {
            run(policy.PRANDOM);
            reward += prev_op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, prev_op.getOpType().toString(), prev_op.getQValue());
        }
        for (int z = 501; z < 6000; z++) {
            run(policy.PEXPLOIT);
            reward += prev_op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, prev_op.getOpType().toString(), prev_op.getQValue());
        }
        agent.printAllQTables();
        System.out.format("Reward: %d\n", reward);
    }

    public void experiment2() {
        agent = new Agent(0.3, 0.5, Agent.learning_type.SARSA);
        int reward = 0;
        for (int z = 1; z < 500; z++) {
            run(policy.PRANDOM);
            reward += prev_op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, prev_op.getOpType().toString(), prev_op.getQValue());
        }
        for (int z = 501; z < 6000; z++) {
            run(policy.PEXPLOIT);
            reward += prev_op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, prev_op.getOpType().toString(), prev_op.getQValue());
        }
        agent.printAllQTables();
        System.out.format("Reward: %d\n", reward);
    }

    public void experiment3A() {
        agent = new Agent(0.15, 0.5, Agent.learning_type.QLEARNING);
        int reward = 0;
        for (int z = 1; z < 500; z++) {
            run(policy.PRANDOM);
            reward += prev_op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, prev_op.getOpType().toString(), prev_op.getQValue());
        }
        for (int z = 501; z < 6000; z++) {
            run(policy.PEXPLOIT);
            reward += prev_op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, prev_op.getOpType().toString(), prev_op.getQValue());
        }
        agent.printAllQTables();
        System.out.format("Reward: %d\n", reward);
    }

    public void experiment3B() {
        agent = new Agent(0.45, 0.5, Agent.learning_type.QLEARNING);
        int reward = 0;
        for (int z = 1; z < 500; z++) {
            run(policy.PRANDOM);
            reward += prev_op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, prev_op.getOpType().toString(), prev_op.getQValue());
        }
        for (int z = 501; z < 6000; z++) {
            run(policy.PEXPLOIT);
            reward += prev_op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, prev_op.getOpType().toString(), prev_op.getQValue());
        }
        agent.printAllQTables();
        System.out.format("Reward: %d\n", reward);
    }

    public void experiment4() {
        agent = new Agent(0.3, 0.5, Agent.learning_type.QLEARNING);
        int reward = 0;
        int goal_state_count = 0;
        for (int z = 1; z < 500; z++) {
            run(policy.PRANDOM);
            reward += prev_op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                goal_state_count++;
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, prev_op.getOpType().toString(), prev_op.getQValue());
        }
        for (int z = 501; z < 6000; z++) {
            run(policy.PEXPLOIT);
            reward += prev_op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                goal_state_count++;
                if (goal_state_count >= 2) {
                    reset_pickup = true;
                }
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, prev_op.getOpType().toString(), prev_op.getQValue());
        }
        agent.printAllQTables();
        System.out.format("Reward: %d\n", reward);
    }
}