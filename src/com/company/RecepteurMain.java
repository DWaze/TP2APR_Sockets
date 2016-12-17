package com.company;

/**
 * Created by lhadj on 11/12/2016.
 */
public class RecepteurMain {

    public static void main(String[] args) {

        ///////// Starting the server
        while(true){
            RecepteurProcess recrRecepteurProcess =  new RecepteurProcess();
            recrRecepteurProcess.recepteur();
        }

    }
}
