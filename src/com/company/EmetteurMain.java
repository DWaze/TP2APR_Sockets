package com.company;

import java.util.Scanner;

public class EmetteurMain {

    public static void main(String[] args) {
        boolean var=true;
        Scanner sc = new Scanner(System.in);
        ///////// starting the process ou Sender
        while(var=true){
            System.out.print("Entre Message Here : ");
            String msg = sc.nextLine();
            EmetteurProcess.ProcedureEmeteur(msg,0);
        }
    }
}
