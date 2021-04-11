package com.company;

public class GridWorld {

    private int[][] pdWorld = new int[5][5];
    private int[][] qTable = new int[50][6]; // 50 states; 6 actions: North, West, South, East, Pickup, Dropoff
    private int[][] cellType = new int[5][5];
    private int[][] rewards = new int[5][5];

    public GridWorld(){
        initWorld();
    }

    double alpha, gamma;
    int i = 5; int j = 1; // start position
    int x = 0; // 1 agent carry block; 0 else
    int a,b,c,f = 0; // # blocks dropcell
    int d,e = 8; // # blocks pickup cell

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

        //set -1 rewards on normal type cell
        for(int g = 0; g < 5; g++){
            for(int h = 0; h < 5; h++){
                rewards[g][h] = -1;
            }
        }
        //set +13 rewards on pickup/dropoff type cell
        rewards[0][0] = 13;
        rewards[0][4] = 13;
        rewards[2][2] = 13;
        rewards[4][4] = 13;
        rewards[2][4] = 13;
        rewards[3][1] = 13;
    }

    public void qTableUpdate(int action, int state, int updateValue){
        qTable[action][state] = updateValue;
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
    public void resetPDWorld(){
        this.pdWorld = new int[5][5];
        //reset start location
        this.i = 4;
        this.j = 0;
    }
}
