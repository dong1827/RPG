import java.util.Scanner;
import MenuClass.*;

public class UserInterface {
    public static void main(String[] args) {
        
        
        try {
            // Initializing player and menu
            Scanner input = new Scanner(System.in);
            System.out.println("Please enter your name");
            Player newPlayer = new Player(input.nextLine());
            Menu mainM = new Menu(newPlayer);


            System.out.println(mainM.getD());
            System.out.println(mainM.getA());

            while (input.hasNextLine()) {
                String action = input.nextLine();
                String msg = "";

                if (action.equals("")) 
				{
					System.out.print("\n>");
					continue;
				}
                
                char digit = action.charAt(0);
				if (action.equalsIgnoreCase("Q") || action.equalsIgnoreCase("QUIT")) {
					break;
                }
                else if (Character.isDigit(digit)) {
                    msg = mainM.doAction(Integer.parseInt(action));
                }

                System.out.println("\n" + msg);
            }
            input.close();
        }
        
        catch(Exception e) {
            System.out.println(e.getMessage());
        }

        

    }
}
