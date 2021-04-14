package com.company;

public class GridWorld {

    private int[][] pdWorld = new int[5][5];
    private double[][] qTable = new double[50][6]; // 50 states; 6 actions: North, West, South, East, Pickup, Dropoff
    private int[][] cellType = new int[5][5];

    public GridWorld(){
        initWorld();
    }

    double alpha, gamma;
    int i = 4; int j = 0; // start position
    int x = 0; // 1 agent carry block; 0 else
    int a,b,c,f = 0; // # blocks dropcell [0,0][0,4][2,2][4,4]
    int d,e = 8; // # blocks pickup cell [2,4][3,1]

    private void initWorld(){

        //value of 0 ==> normal
        //value of 1 ==> drop-off
        //value of 2 ==> pickup
        cellType[0][0] = 1;
        cellType[0][4] = 1;
        cellType[2][2] = 1;
        cellType[4][4] = 1;
        cellType[2][4] = 2;
        cellType[3][1] = 2;

        //Initial number of blocks in each pickup cell
        pdWorld[2][4] = 8;
        pdWorld[3][1] = 8;
    }

    public void qTableUpdate(int state, int action, int updateValue){
        //qTable[state][action] = updateValue;
        //update if pickup or drop off by reward = 13
        if(cellType[i][j] == 2 || cellType[i][j] == 1){
            qTable[state][action] = ((1-alpha)*qTable[state][action]) + (alpha * ((13)+gamma*(qTable[state][action]))); //still figuring out
        }
        else{
            System.out.println("State: " + state);
            System.out.println("Action: " + action);
            qTable[state][action] = ((1-alpha)*qTable[state][action]) + (alpha * ((-1)+gamma*(qTable[state][updateValue])));
        }
    }

    public boolean isGoalState(){
        if(a == 4 && b == 4 && c == 4 && f == 4 && d == 0 && e == 0){
            return true;
        } else{
            return false;
        }
    }
    public void setAlphaGamma(double alpha, double gamma){
        this.alpha = alpha;
        this.gamma = gamma;
    }
    public int[][] getPDWorld(){
        return this.pdWorld;
    }
    public int[][] getCellType(){
        return this.cellType;
    }
    public void resetPDWorld(){
        this.pdWorld = new int[5][5];
        //reset start location
        this.i = 4;
        this.j = 0;
        this.i = 5; this.j = 1; // start position
        this.x = 0; // 1 agent carry block; 0 else
        this.a = 0; this.b=0; this.c=0; this.f=0; // # blocks dropcell
        this.d=8; this.e=8; // # blocks pickup cell
    }
    public void pickUpBlock(){
        //remove block from specific location if possible
        if(i == 2 && j == 4 && d > 0){
            d--;
            x = 1;
        }
        else if(i == 3 && j == 1 && e > 0){
            e--;
            x = 1;
        }
    }
    public void dropBlock(){
        //drop block if possible
        if(i == 0 && j == 0 && a < 4){
            a++;
            x = 0;
        }
        else if(i == 0 && j == 4 && b < 4){
            b++;
            x = 0;
        }
        else if(i == 2 && j == 2 && c < 4){
            c++;
            x = 0;
        }
        else if(i == 4 && j == 4 && f < 4){
            f++;
            x = 0;
        }
    }
    public int selectAction(){
        int action = -100;
        double q = 0;
            //north
            if(i-1 > 0 && qTable[i-1][j] > q){
                q = qTable[i-1][j];
                action = 0;
            }
            //south
            if(i+1 < 50 && qTable[i-1][j] > q){
                q = qTable[i+1][j];
                action = 2;
            }
            //west
            if(j-1 > 0 && qTable[i][j-1] > q){
                q = qTable[i][j-1];
                action = 1;
            }
            //east
            if(j+1 < 6 && qTable[i][j+1] > q){
                q = qTable[i][j+1];
                action = 3;
            }
            
        return action;
    }
}
