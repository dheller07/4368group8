package com.company.visualization;

import com.company.GridWorldSarsa;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GUI {

    JFrame frame;
    ImageIcon wallEImage;
    ImageIcon wallEWithPackageImage;
    ImageIcon packageImage;
    boolean isCarrying = false;
    JButton[][] buttons;
    int x;
    int y;

    public GUI(GridWorldSarsa gridWorld){
        frame = new JFrame();
        wallEImage = createImageIcon("res/walle.png");
        packageImage = createImageIcon("res/package.png");
        wallEWithPackageImage = createImageIcon("res/wallEWithPackage.png");
        buttons = new JButton[5][5];

        frame.setLayout(new GridLayout(5,5));

        for(int i = 0;i<5;i++){
            for(int j = 0;j<5;j++){
                JButton button = new JButton();
                if(gridWorld.cellType[i][j] == GridWorldSarsa.CellType.PICKUP){
                    button.setText("PICKUP (8)");
                    frame.add(button);
                } else if(gridWorld.cellType[i][j] == GridWorldSarsa.CellType.DROPOFF){
                    button.setText("DROPOFF (0)");
                    frame.add(button);
                } else {
                    if(i == 4 && j == 0){
                        button.setIcon(wallEImage);
                        frame.add(button);
                        x = i;
                        y = j;
                    } else {
                        frame.add(button);
                    }
                }
                buttons[i][j] = button;
            }
        }

        frame.setSize(500,500);
        frame.setVisible(true);
    }

    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = GUI.class.getResource(path);
        ImageIcon icon = new ImageIcon(imgURL);
        Image img = icon.getImage();
        Image newImg = img.getScaledInstance(80,80, Image.SCALE_SMOOTH);
        return new ImageIcon(newImg);
    }

    public void updateWallEPosition(int newX, int newY){
        buttons[x][y].setIcon(null);
        if(isCarrying){
            buttons[newX][newY].setIcon(wallEWithPackageImage);
        } else{
            buttons[newX][newY].setIcon(wallEImage);
        }
        x = newX;
        y = newY;
    }

    public void wallEPickup(){
        buttons[x][y].setIcon(wallEWithPackageImage);
        isCarrying = true;
    }
    public void wallEDropoff(){
        buttons[x][y].setIcon(wallEImage);
        isCarrying = false;
    }

    public void updatePickupPackage(int value){
        buttons[x][y].setText("PICKUP (" + Integer.toString(value) + ")");
    }
    public void updateDropoffPackage(int value){
        buttons[x][y].setText("DROPOFF (" + Integer.toString(value) + ")");
    }
}
