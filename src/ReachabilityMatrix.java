// Name: <Amir Davis>
// Class: CS 3305/Section#04
// Term: Spring 2022
// Instructor: Dr. Haddad
// Assignment: 7

import java.util.Scanner;
public class ReachabilityMatrix {
    public static void main(String[] args) {
        int userChoice = 0;
        int numOfNodes;
        boolean flag = false;
        int[][] myArray = new int[0][0];
        Scanner myScanner = new Scanner(System.in);

        while(userChoice != 3){
            /*if-else statement is used so the user is forced to create a matrix. Then afterwards the user can
            select what they want to do from the menu.
             */
            if(!flag){
                userChoice = 1;
                flag = true;
            }
            else{
                printMenu();
                userChoice = myScanner.nextInt();
            }

            //switch case that executes a certain action depending on the user's choice
            switch (userChoice){
                //creates a matrix depending on the user's inputs
                case 1:
                    System.out.println("How many nodes?");
                    numOfNodes = myScanner.nextInt();
                    myArray = new int[numOfNodes][numOfNodes];

                    for(int j = 0; j < myArray.length; j++) {
                        for (int k = 0; k < myArray.length; k++) {
                            System.out.print("Enter A1[" + j + "," + k + "]: ");
                            myArray[j][k] = myScanner.nextInt();
                        }
                    }
                    break;
                //prints out the outputs: paths, cycles, loops, etc of the user's entered matrix
                case 2:
                    printOutputs(myArray);
                    break;
                //exits the program
                case 3:
                    break;
            }
        }
    }

    //prints out the options the user can choose from
    public static void printMenu(){
        System.out.print("\n------MAIN MENU------\n" +
                "1. Enter graph data\n" +
                "2. Print outputs\n" +
                "3. Exit program\n\n" +
                "Enter option number: ");
    }

    //prints out all the outputs of the entered matrix
    public static void printOutputs(int[][] userArray){
        int numOfSelfLoops = 0;
        int degree = 0;
        int pathsOfLengthAnEdges = 0;
        int cyclesOfLengthAnEdges = 0;
        int numOfPathsOfLength1 = 0;
        int numOfPathsToLengthX = 0;
        int numOfCyclesOfLengthX = 0;
        int[][] matrixA1TimesAx = new int[userArray.length][userArray.length];
        int[][] reachabilityMatrix = new int[userArray.length][userArray.length];
        int[][] tempArray = new int[userArray.length][userArray.length];
        int[][] Ax = new int[userArray.length][userArray.length];

        //returns the user's entered matrix in a square/matrix format
        System.out.println("Input Array: ");
        for(int j = 0; j < userArray.length; j++){
            for(int k = 0; k < userArray.length; k++){
                System.out.printf("%-4d", userArray[j][k]);
            }
            System.out.println();
        }
        System.out.println();

        /*sets reachabilityMatrix to userArray and tempArray to userArray. This is important for the next
        nested loop.
         */
        for(int j = 0; j < userArray.length; j++){
            for(int k = 0; k < userArray.length; k++){
                reachabilityMatrix[j][k] = userArray[j][k];
                tempArray[j][k] = userArray[j][k];
            }
        }

        int x1 = 0;
        int x2 = 0;
        int y1 = 0;
        int y2 = 0;
        int whynot = 0;
        /*
        Calculates the reachability matrix.
         */
        while(whynot < userArray.length-1){
            /*
            Nested loop that multiples A1(The user's original matrix) x the next matrix to be multiplied until whynot
            reaches the length of A1.
             */
            for(int j = 0; j < userArray.length; j++){
                for(int k = 0; k < userArray.length; k++){
                    for(int i = 0; i < userArray.length; i++){
                        matrixA1TimesAx[j][k] += tempArray[x1][y1++] * userArray[x2++][y2];
                    }
                    y1 = 0;
                    x2 = 0;
                    y2++;
                }//k loop
                y2 = 0;
                x1++;
            }//j loop
            x1 = 0;

            //Copies the data from the last matrix to be multiplied against A1 (which would give us the
            // reachability matrix) and inputs in into Ax
            if(whynot == Ax.length - 2){
                for(int j = 0; j < Ax.length; j++){
                    for(int k = 0; k < Ax.length; k++){
                        Ax[j][k] = matrixA1TimesAx[j][k];
                    }
                }
            }

            /*adds each matrix to reachabilityMatrix. This is done because the formula for the reachability matrix is
            A1 + A2 + ... + An. Sets tempArray equal to maxtrixA1TimesAx so when the loop reiterates, it will be An * A1.
            Also resets all of maxtrixA1TimesAx positions to 0 so that it can take in the next (An * A1).
             */
            for(int j = 0; j < userArray.length; j++){
                for(int k = 0; k < userArray.length; k++){
                    reachabilityMatrix[j][k] += matrixA1TimesAx[j][k];
                    tempArray[j][k] = matrixA1TimesAx[j][k];
                    matrixA1TimesAx[j][k] = 0;
                }
            }
            whynot++;
        }

        //prints out the reachability matrix
        System.out.println("Reachability Matrix: ");
        for(int j = 0; j < reachabilityMatrix.length; j++){
            for(int k = 0; k < reachabilityMatrix.length; k++){
                System.out.printf("%-4d", reachabilityMatrix[j][k]);
            }
            System.out.println();
        }
        System.out.println();

        //Calculates the number of cycles and paths from A1 to An
        for(int j = 0; j < reachabilityMatrix.length; j++){
            for(int k = 0; k < reachabilityMatrix.length; k++){
                if(j == k){
                    numOfCyclesOfLengthX += reachabilityMatrix[j][k];
                }
                numOfPathsToLengthX += reachabilityMatrix[j][k];
            }
        }

        //Prints the number of pathsOfLengthAnEdges-degrees
        System.out.println("In-degrees: ");
        for(int j = 0; j < userArray.length; j++){
            for(int k = 0; k < userArray.length; k++){
                if(userArray[k][j] == 1){
                    degree++;
                }
            }
            System.out.println("Node " + (j+1) + " in-degree is " + degree);
            degree = 0;
        }
        System.out.println();

        //Prints the number of out-degrees
        System.out.println("Out-degrees: ");
        for(int j = 0; j < userArray.length; j++){
            for(int k = 0; k < userArray.length; k++){
                if(userArray[j][k] == 1){
                    degree++;
                }
            }
            System.out.println("Node " + (j+1) + " out-degree is " + degree);
            degree = 0;
        }
        System.out.println();

        //Calculates the number of loops from A1
        int index = 0;
        while (index < userArray.length){
            if(userArray[index][index] == 1){
                numOfSelfLoops++;
            }
            index++;
        }

        //Calculates the number of paths from A1
        for(int j = 0; j < userArray.length; j++){
            for(int k = 0; k < userArray.length; k++){
                if(userArray[j][k] == 1){
                    numOfPathsOfLength1++;
                }
            }
        }


        for(int j = 0; j < Ax.length; j++){
            for(int k = 0; k < Ax.length; k++){
                pathsOfLengthAnEdges += Ax[j][k];
                if(j == k){
                    cyclesOfLengthAnEdges += Ax[j][k];
                }
            }
        }

        System.out.println("Total number of self-loops: " + numOfSelfLoops);
        System.out.println("Total number of cycles of length " + userArray.length + " edges: " + cyclesOfLengthAnEdges);
        System.out.println("Total number of paths of length 1 edge: " + numOfPathsOfLength1);
        System.out.println("Total number of paths of length " + userArray.length + " edges: " + pathsOfLengthAnEdges);
        System.out.println("Total number of paths of length 1 to " + userArray.length +" edges: " + numOfPathsToLengthX);
        System.out.println("Total number of cycles of length 1 to " + userArray.length + " edges: " + numOfCyclesOfLengthX);
    }
}
