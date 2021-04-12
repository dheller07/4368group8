package com.company;

public class GridWorld {

    //private int[][] pdWorld = new int[5][5];
    //private int[][] cellType = new int[5][5];
    private double[][][][] qTable = new double[5][5][2][6]; // 5x5x2 = 50 (i, j, x) states; 6 possible operations

    double alpha, gamma; //Q-learning parameters
    int i, j, x, a, b, c, d, e, f; //current world state

    public GridWorld(){
        initWorld();
    }

    //initializes world in initial state and Q table to zeros
    public void initWorld(){

        alpha = 1; //default initialization, no experiment actually uses this value
        gamma = 1; //default initialization, no experiment actually uses this value

        i = 4; // start position
        j = 0; // start position
        x = 0; // 1 agent carry block; 0 else
        a = 0; // number of blocks at dropoff cell
        b = 0; // number of blocks at dropoff cell
        c = 0; // number of blocks at dropoff cell
        d = 8; // number of blocks at pickup cell
        e = 8; // number of blocks at pickup cell
        f = 0; // number of blocks at dropoff cell

        //value of 0 ==> normal
        //value of 1 ==> drop-off
        //value of 2 ==> pickup
        for(int k = 0; k<5; k++){
            for(int l = 0; l < 5; l++){
                //pdWorld[i][j] = 0;
                //cellType[i][j] = 0;
                for(int m = 0; m < 2; m++){
                    for (int n = 0; n < 6; n++){
                        qTable[k][l][m][n] = 0;
                    }
                }
            }
        }
        /*

        //Not sure these arrays are necessary. Not currently using them.
        cellType[0][0] = 1;
        cellType[0][4] = 1;
        cellType[2][2] = 1;
        cellType[4][4] = 1;
        cellType[2][4] = 2;
        cellType[3][1] = 2;

        //Initial number of blocks in each pickup cell - Note d and e already represent these
        pdWorld[2][4] = 8;
        pdWorld[3][1] = 8;

         */
    }

    //resets world in initial state without resetting Q table
    public void resetWorld(){
        i = 4; // start position
        j = 0; // start position
        x = 0; // 1 agent carry block; 0 else
        a = 0; // number of blocks at dropoff cell
        b = 0; // number of blocks at dropoff cell
        c = 0; // number of blocks at dropoff cell
        d = 8; // number of blocks at pickup cell
        e = 8; // number of blocks at pickup cell
        f = 0; // number of blocks at dropoff cell
    }

    public void setAlpha(double alpha){
        this.alpha = alpha;
    }

    public void setGamma(double gamma){
        this.gamma = gamma;
    }

    public int getI(){
        return i;
    }

    public int getJ(){
        return j;
    }

    public void printState(){
        System.out.format("%d, %d, %d, %d, %d, %d, %d, %d, %d\n", i, j, x, a, b, c, d, e, f);
    }

    public void printQTable(){
        for (int k = 0; k < 6; k++){
            for (int l = 0; l < 2; l++){
                System.out.format("Q Table for operation %d with %d blocks:\n\n", k, l);
                for (int m = 0; m < 5; m++){
                    for (int n = 0; n < 5; n++){
                        System.out.format("%6.4f  ", qTable[m][n][l][k]);
                    }
                    System.out.println();
                }
            }
        }
    }

    //updates Q table value at (i, j, x, operation)
    //operation value:
    //0 ==> pickup
    //1 ==> dropoff
    //2 ==> north
    //3 ==> east
    //4 ==> south
    //5 ==> west
    public void qTableUpdate(int operation){
        double updateValue = 0;
        switch (operation){
            case 0:
                updateValue = ((1 - alpha) * qTable[i][j][x - 1][operation]) + alpha * (13 + (gamma * getQMax(i, j, x)));
                break;
            case 1:
                updateValue = ((1 - alpha) * qTable[i][j][x + 1][operation]) + alpha * (13 + (gamma * getQMax(i, j, x)));
                break;
            case 2:
                updateValue = ((1 - alpha) * qTable[i + 1][j][x][operation]) + alpha * (-1 + (gamma * getQMax(i, j, x)));
                break;
            case 3:
                updateValue = ((1 - alpha) * qTable[i][j - 1][x][operation]) + alpha * (-1 + (gamma * getQMax(i, j, x)));
                break;
            case 4:
                updateValue = ((1 - alpha) * qTable[i - 1][j][x][operation]) + alpha * (-1 + (gamma * getQMax(i, j, x)));
                break;
            case 5:
                updateValue = ((1 - alpha) * qTable[i][j + 1][x][operation]) + alpha * (-1 + (gamma * getQMax(i, j, x)));
                break;
            default:
                System.out.println("ERROR: Invalid operation");
        }
        qTable[i][j][x][operation] = updateValue;
    }

    //returns true if GridWorld is in goal state and false otherwise
    public boolean isGoalState(){
        if(a == 4 && b == 4 && c == 4 && f == 4 && d == 0 && e == 0){
            return true;
        } else{
            return false;
        }
    }

    //returns the Q table value of the best operation at state (i, j, x)
    public double getQMax(int i, int j, int x){
        double q_max = qTable[i][j][x][0];
        for (int k = 1; k < 6; k++){
            if (qTable[i][j][x][k] > q_max){
                q_max = qTable[i][j][x][k];
            }
        }
        return q_max;
    }

    //returns true if pickup() is a valid operator and false otherwise
    public boolean pickupValid() {
        if (x == 1){
            return false;
        }
        else{
            if (i == 2 && j == 4){
                if (d > 0){
                    return true;
                }
                else{
                    return false;
                }
            }
            else if (i == 3 && j == 1){
                if (e > 0){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
    }

    //returns true if dropoff() is a valid operator and false otherwise
    public boolean dropoffValid() {
        if (x == 0){
            return false;
        }
        else{
            if (i == 0 && j == 0){
                if (a < 4){
                    return true;
                }
                else{
                    return false;
                }
            }
            else if (i == 0 && j == 4){
                if (b < 4){
                    return true;
                }
                else{
                    return false;
                }
            }
            else if (i == 2 && j == 2){
                if (c < 4){
                    return true;
                }
                else{
                    return false;
                }
            }
            else if (i == 4 && j == 4){
                if (f < 4){
                    return true;
                }
                else{
                    return false;
                }
            }
            else{
                return false;
            }
        }
    }

    //picks up block at current pickup location and updates Q table
    public void pickup(){
        System.out.println("Pickup");
        if (i == 2 && j == 4){
            d--;
        }
        else if (i == 3 && j == 1){
            e--;
        }
        else{
            System.out.println("ERROR: Not at pickup location");
        }
        x = 1;
        qTableUpdate(0);
    }

    //drops off block at current dropoff location and updates Q table
    public void dropoff(){
        System.out.println("Dropoff");
        if (i == 0 && j == 0){
            a++;
        }
        else if (i == 0 && j == 4){
            b++;
        }
        else if (i == 2 && j == 2){
            c++;
        }
        else if (i == 4 && j == 4){
            f++;
        }
        else{
            System.out.println("ERROR: Not at dropoff location");
        }
        x = 0;
        qTableUpdate(1);
    }

    //moves to the tile north of the current tile and updates Q table
    public void north(){
        System.out.println("North");
        if (i <= 0){
            System.out.println("ERROR: Invalid operation");
        }
        else{
            i--;
            qTableUpdate(2);
        }
    }

    //moves to the tile east of the current tile and updates Q table
    public void east(){
        System.out.println("East");
        if (j >= 4){
            System.out.println("ERROR: Invalid operation");
        }
        else{
            j++;
            qTableUpdate(3);
        }
    }

    //moves to the tile south of the current tile and updates Q table
    public void south(){
        System.out.println("South");
        if (i >= 4){
            System.out.println("ERROR: Invalid operation");
        }
        else{
            i++;
            qTableUpdate(4);
        }
    }

    //moves to the tile west of the current tile and updates Q table
    public void west(){
        System.out.println("West");
        if (j <= 0){
            System.out.println("ERROR: Invalid operation");
        }
        else{
            j--;
            qTableUpdate(5);
        }
    }
}
