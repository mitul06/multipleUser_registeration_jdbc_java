package JDBC.UserRegistrastion;


import java.sql.*;
import java.util.Scanner;
import java.util.stream.Stream;


public class Main {

   private static DatabaseHandler databaseHandler = new DatabaseHandler();
    private static final String First_Promt = "Please select an option : " + "\n1.Login" + "\n2.Register"+"\n3.Exit";
    private static final String Logged_Promt = "Please select an option : " + "\n1.Show Details" + "\n2.Reset Password"+"\n3.Log Out";

    private static Users login(Scanner sc) {
        System.out.print("Enter Your Email: ");
        String email = sc.next().toLowerCase();
        System.out.print("Enter Your Password: ");
        String pass = sc.next().toLowerCase();

        Users users = new Users();
        users.setUserEmail(email);
        users.setUserPass(pass);

        ResultSet userRow = databaseHandler.getUser(users);

        try {
            while (userRow.next()) {
                String uEmail = userRow.getString("userEmail");
                String uName = userRow.getString("userName");
                System.out.println("<----------------------------------->");
                System.out.println();
                System.out.println("Your Username : " +uName);
                System.out.println("Your Email id : "+uEmail);
                System.out.println();
                System.out.println("<----------------------------------->");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return users;


    }
    private static Users Register(Scanner sc){
        System.out.print("Enter Your userName : ");
        String name = sc.next().toLowerCase();

        System.out.print("Enter Your Email : ");
        String email = sc.next().toLowerCase();

        System.out.print("Enter Your Password : ");
        String pass = sc.next().toLowerCase();

        Users users = new Users(name,email,pass);

        if (databaseHandler.CheckUserAlreadyRegistered(name)){
            System.out.println("--------------------------");

        }else {

            databaseHandler.signUpUser(users);
            System.out.println();
            System.out.println("Registration Successfull");
            System.out.println();
            System.out.println("<----------------------------------->");
        }

        return users;
    }

    private static void resetPassword(Scanner sc, Users user){
        System.out.print("Enter your current password: ");
        String currentPassword = sc.next().toLowerCase();
        if(user.getUserPass().equals(currentPassword)) {
            System.out.print("Enter your new password: ");
            String newPassword = sc.next().toLowerCase();

            try{

                Connection connection = databaseHandler.getDbConnection();
                databaseHandler.resetPassword(newPassword,user.getUserEmail());
                user.setUserPass(newPassword);
            }catch (SQLException | ClassNotFoundException e){
                e.printStackTrace();
            }
            System.out.println();
            System.out.println("Password successfully updated");
            System.out.println();
            System.out.println("<----------------------------------->");
        } else {
            System.out.println();
            System.out.println("Incorrect Password, Please try again");
            System.out.println();
            System.out.println("<----------------------------------->");
        }
    }

    public static void main(String[] args) {
        Users loggedInUser = null;
        Scanner sc = new Scanner(System.in);

        while(true) {
            if(loggedInUser != null) {
                System.out.println(Logged_Promt);
                int request = sc.nextInt();
                switch(request) {
                    case 1:
                        login(sc);
                        break;
                    case 2:
                        resetPassword(sc, loggedInUser);
                        break;
                    case 3:
                        loggedInUser = null;
                        System.out.println();
                        System.out.println("Successfully Logged Out");
                        System.out.println();
                        System.out.println("<----------------------------------->");
                        break;
                    default:
                        System.out.println();
                        System.out.println("Invalid selection please try again");
                        System.out.println();
                        System.out.println("<----------------------------------->");
                }
            } else {
                System.out.println(First_Promt);
                int request = sc.nextInt();
                switch(request) {
                    case 1:
                        loggedInUser = login(sc);
                        break;
                    case 2:
                        loggedInUser = Register(sc);
                        break;
                    case 3:
                        System.out.println();
                        System.out.println("Exiting...");
                        System.out.println();
                        System.out.println("<----------------------------------->");
                        sc.close();
                        System.exit(0);
                    default:
                        System.out.println();
                        System.out.println("Invalid selection please try again");
                        System.out.println();
                        System.out.println("<----------------------------------->");
                }
            }
        }
    }
}
