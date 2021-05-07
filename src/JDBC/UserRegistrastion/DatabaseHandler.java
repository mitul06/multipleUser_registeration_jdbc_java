package JDBC.UserRegistrastion;

import java.sql.*;

public class DatabaseHandler extends Config {
    Connection dbConnection;
    public Connection getDbConnection() throws SQLException,ClassNotFoundException{
        String ConnectingString = DbURL;
        Class.forName("com.mysql.jdbc.Driver");

        dbConnection = DriverManager.getConnection(ConnectingString,DbUser,DbPass);
        return dbConnection;
    }

    public void signUpUser(Users users){
        String insert = "INSERT INTO usersdetails ( userName, userEmail, userPass) values (?,?,?)";

        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement(insert);

            preparedStatement.setString(1,users.getUserName());
            preparedStatement.setString(2,users.getUserEmail());
            preparedStatement.setString(3,users.getUserPass());

            preparedStatement.executeUpdate();
        }catch (SQLException | ClassNotFoundException e){
                e.printStackTrace();
        }
    }

    public void resetPassword(String newPass,String userEmail) throws SQLException, ClassNotFoundException{
        String query = "UPDATE usersdetails SET userPass=? where userEmail=?";
        PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);

        preparedStatement.setString(1, newPass);
        preparedStatement.setString(2,userEmail);
        preparedStatement.executeUpdate();
    }

    public ResultSet getUser(Users users){
        ResultSet resultSet = null;

        if (!users.getUserEmail().equals("") || !users.getUserPass().equals("")){
            String query = "SELECT * FROM usersdetails WHERE userEmail=? AND userPass=?";

            try{
                PreparedStatement preparedStatement = getDbConnection().prepareStatement(query);
                preparedStatement.setString(1,users.getUserEmail());
                preparedStatement.setString(2,users.getUserPass());

                resultSet = preparedStatement.executeQuery();
            }catch (SQLException | ClassNotFoundException e){
                e.printStackTrace();
            }
        }else {
            System.out.println("Please Enter UserEmail and UserPassword!");
        }
        return resultSet;
    }

    public boolean CheckUserAlreadyRegistered(String userName){
        boolean userExists = false;

        try{
            PreparedStatement preparedStatement = getDbConnection().prepareStatement("SELECT * FROM usersdetails ORDER BY userName DESC");
            ResultSet resultSet = preparedStatement.executeQuery();
            String usernameCounter;
            if (resultSet.next()){
                usernameCounter = resultSet.getString("userName");
                if (usernameCounter.toLowerCase().equals(userName)){
                    System.out.println("User Already Registered! Please Login");
                    userExists = true;
                }
            }

        }catch (SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return userExists;
    }

}
