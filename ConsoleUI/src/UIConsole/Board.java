package UIConsole;
import GameObjects.Player;
import GameObjects.Territory;
import java.util.Map;

import java.util.Scanner;

public class Board {
    int rows,columns;
    Map <Integer, Territory> territoriesMap;
    Player player1,player2,player3,player4;


    Board(Map <Integer, Territory> territoriesMap, int rows, int columns, Player player1 , Player player2 ,Player player3 , Player player4) {
        this.player1 = player1;
        this.player2 = player2;
        this.player3 = player3;
        this.player4 = player4;
        this.territoriesMap = territoriesMap;
        this.rows = rows;
        this.columns = columns;

    }



    public void PrintDataTable()
    {
        String format = "|%1$-10s|%2$-10s|%3$-20s|%4$-20s|\n";
        System.out.format(format,"ID","Owner", "Profit", "Threshold");
        String name;
        for(int i=1;i<=rows*columns;i++) {

            Territory currentPrintedObject =territoriesMap.get(i);
            int id=currentPrintedObject.getID();
            String owner;
            if(currentPrintedObject.getConquer() == null) owner="no owner";
            else {
                    owner=currentPrintedObject.getConquer().getPlayer_name();
            }
            int profit=currentPrintedObject.getProfit();
            int threshold=currentPrintedObject.getArmyThreshold();
            System.out.format(format, id, owner, profit, threshold);
        }

    }
        public void PrintBoardTable()
    {

        String repeatedLine = new String(new char[columns*9]).replace('\0', '-');
        String repeatedSpaces = new String(new char[7]).replace('\0', ' ');
        int counter=1;
        for (int i=0;i<rows;i++)
        {
            System.out.println(repeatedLine);
            for(int k=0;k<columns;k++) {
                System.out.print("|");
                System.out.print(repeatedSpaces);
                System.out.print("|");
            }
            System.out.print('\n');
            for(int j=0;j<columns;j++)
            {
                if(counter<10) {
                    System.out.print("|   ");
                    System.out.print(counter);
                    if (j + 1 == columns)
                        System.out.println("   |");
                    else
                        System.out.print("   |");
                }
                else{
                    if(counter<100) {
                        System.out.print("|   ");
                        System.out.print(counter);
                        if (j + 1 == columns)
                            System.out.println("  |");
                        else
                            System.out.print("  |");
                    }
                    else{
                        System.out.print("|  ");
                        System.out.print(counter);
                        if (j + 1 == columns)
                            System.out.println("  |");
                        else
                            System.out.print("  |");
                    }
                }
                counter++;
            }
            for(int z=0;z<columns;z++) {
                System.out.print("|");

                System.out.print(repeatedSpaces);
                System.out.print("|");
            }
            System.out.print('\n');

        }
        System.out.println(repeatedLine);
    }
}
