import java.util.Scanner;
import MenuClass.*;

public class UserInterface {
    public static void main(String[] args) {
        
        
        try {
            // Initializing player and menu
            Player newPlayer = new Player();
            Menu MainM = new Menu();
            Scanner input = new Scanner(System.in);


            System.out.println(MainM.getD());
            System.out.println(MainM.getA());

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
                    msg = MainM.doAction(Integer.parseInt(action));
                }

                System.out.println("\n" + msg);
            }
        }
        
        catch(Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
