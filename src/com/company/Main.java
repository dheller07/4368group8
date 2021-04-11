package com.company;

import com.company.RLAgent.Policy;

public class Main {

    public static void main(String[] args) {
        // write your code here
        // System.out.println("Hello world");
        RLAgent one = new RLAgent(Policy.PEXPLOIT);
        one.run();
    }
}