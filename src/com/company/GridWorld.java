package com.company;

import java.util.ArrayList;

public class GridWorld {

    private int i, j, x, a, b, c, d, e, f;
    private boolean reset_pickup;
    private Agent Agent;

    enum policy {
        PRANDOM,
        PGREEDY,
        PEXPLOIT
    }

    public GridWorld() {
        initWorld();
        reset_pickup = false;
        Agent = null;
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

    public Operator run(policy p) {
        ArrayList<Operator> op_list = Agent.applicableOperators(i, j, x, a, b, c, d, e, f, reset_pickup);
        Operator op = null;
        switch (p) {
            case PRANDOM:
                op = Agent.pRandom(op_list);
                break;
            case PGREEDY:
                op = Agent.pGreedy(op_list);
                break;
            case PEXPLOIT:
                op = Agent.pExploit(op_list);
                break;
            default:
                System.out.println("ERROR: Policy not provided");
        }
        Agent.updateQValue(i, j, x, a, b, c, d, e, f, reset_pickup, op);
        applyOperator(op);
        return op;
    }

    public void experiment1A() {
        Agent = new Agent(0.3, 0.5, com.company.Agent.learning_type.QLEARNING);
        int reward = 0;
        for (int z = 1; z < 6000; z++) {
            Operator op = run(policy.PRANDOM);
            reward += op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, op.getOpType().toString(), op.getQValue());
        }
        Agent.printAllQTables();
        System.out.format("Reward: %d\n", reward);
    }

    public void experiment1B() {
        Agent = new Agent(0.3, 0.5, com.company.Agent.learning_type.QLEARNING);
        int reward = 0;
        for (int z = 1; z < 500; z++) {
            Operator op = run(policy.PRANDOM);
            reward += op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, op.getOpType().toString(), op.getQValue());
        }
        for (int z = 501; z < 6000; z++) {
            Operator op = run(policy.PGREEDY);
            reward += op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, op.getOpType().toString(), op.getQValue());
        }
        Agent.printAllQTables();
        System.out.format("Reward: %d\n", reward);
    }

    public void experiment1C() {
        Agent = new Agent(0.3, 0.5, com.company.Agent.learning_type.QLEARNING);
        int reward = 0;
        for (int z = 1; z < 500; z++) {
            Operator op = run(policy.PRANDOM);
            reward += op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, op.getOpType().toString(), op.getQValue());
        }
        for (int z = 501; z < 6000; z++) {
            Operator op = run(policy.PEXPLOIT);
            reward += op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, op.getOpType().toString(), op.getQValue());
        }
        Agent.printAllQTables();
        System.out.format("Reward: %d\n", reward);
    }

    public void experiment3A() {
        Agent = new Agent(0.15, 0.5, com.company.Agent.learning_type.QLEARNING);
        int reward = 0;
        for (int z = 1; z < 500; z++) {
            Operator op = run(policy.PRANDOM);
            reward += op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, op.getOpType().toString(), op.getQValue());
        }
        for (int z = 501; z < 6000; z++) {
            Operator op = run(policy.PEXPLOIT);
            reward += op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, op.getOpType().toString(), op.getQValue());
        }
        Agent.printAllQTables();
        System.out.format("Reward: %d\n", reward);
    }

    public void experiment3B() {
        Agent = new Agent(0.45, 0.5, com.company.Agent.learning_type.QLEARNING);
        int reward = 0;
        for (int z = 1; z < 500; z++) {
            Operator op = run(policy.PRANDOM);
            reward += op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, op.getOpType().toString(), op.getQValue());
        }
        for (int z = 501; z < 6000; z++) {
            Operator op = run(policy.PEXPLOIT);
            reward += op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, op.getOpType().toString(), op.getQValue());
        }
        Agent.printAllQTables();
        System.out.format("Reward: %d\n", reward);
    }

    public void experiment4() {
        Agent = new Agent(0.3, 0.5, com.company.Agent.learning_type.QLEARNING);
        int reward = 0;
        int goal_state_count = 0;
        for (int z = 1; z < 500; z++) {
            Operator op = run(policy.PRANDOM);
            reward += op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                goal_state_count++;
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, op.getOpType().toString(), op.getQValue());
        }
        for (int z = 501; z < 6000; z++) {
            Operator op = run(policy.PEXPLOIT);
            reward += op.getReward();
            if (isGoalState()) {
                System.out.println("Goal state reached.");
                goal_state_count++;
                if (goal_state_count >= 2) {
                    reset_pickup = true;
                }
                initWorld();
            }
            System.out.format("State: %d, %d, %d, %d, %d, %d, %d, %d, %d; Operation: %s; Qvalue: %.3f\n",
                    i, j, x, a, b, c, d, e, f, op.getOpType().toString(), op.getQValue());
        }
        Agent.printAllQTables();
        System.out.format("Reward: %d\n", reward);
    }
}