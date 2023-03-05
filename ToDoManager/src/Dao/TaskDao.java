package Dao;

import Model.Task;
import Model.User;
import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskDao {
    List<Task> taskList = new ArrayList<>();
    Connection conn = DBConnection.getConnection();
    
    public void addTask(Task task) throws Exception{
        String sql = "INSERT INTO tasks (taskId,taskTitle, taskText, assignedTo, taskCompleted) VALUES (?, ?, ?, ?, ?)";
        try{
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1,task.getTaskId());
            statement.setString(1,task.getTaskTitle());
            statement.setString(2,task.getTaskText());
            statement.setString(3,task.getAssignedTo());
            statement.setBoolean(4,task.isTaskCompleted());

            statement.executeUpdate();
        }
        catch(SQLException e){
            throw new Exception(e.getMessage());
        }
        taskList.add(task);
    }

    public void updateTask(Task task) throws Exception{
        String sql = "UPDATE tasks SET taskTitle = ?, taskText = ?, assignedTo = ?, taskCompleted = ? WHERE taskId = ?";

        try{
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1,task.getTaskTitle());
            statement.setString(2,task.getTaskText());
            statement.setString(3,task.getAssignedTo());
            statement.setBoolean(4,task.isTaskCompleted());
            statement.setInt(5,task.getTaskId());

            statement.executeUpdate();
        }
        catch(SQLException e){
            throw new Exception(e.getMessage());
        }
    }
    public void deleteTask(Task taskToDelete) throws Exception{
        String sql = "DELETE FROM tasks WHERE taskId = ?";

        try{
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1,taskToDelete.getTaskId());
            statement.executeUpdate();
        }
        catch(SQLException e){
            throw new Exception(e.getMessage());
        }
        taskList.removeIf(task-> task.getTaskId()==taskToDelete.getTaskId());
    }

    public List<Task> searchTasks(String searchQuery,String email) throws Exception{
        List<Task> searchResults = new ArrayList<>();
        String sql = "SELECT * FROM tasks WHERE taskText LIKE=? and email=?";

        try{
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1,searchQuery);
            statement.setString(2,email);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                int taskId = rs.getInt("taskId");
                String taskTitle = rs.getString("taskTitle");
                String taskText = rs.getString("taskText");
                String assignedTo = rs.getString("assignedTo");
                boolean taskCompleted = rs.getBoolean("taskCompleted");
                Task task = new Task(taskId,taskTitle,taskText,assignedTo,taskCompleted);
                searchResults.add(task);
            }
        }
        catch(SQLException e){
            throw new Exception(e.getMessage());
        }
        return searchResults;
    }

    public List<Task> viewAllTasks(String email) throws Exception{
        List<Task> allTasks = new ArrayList<>();

        String sql = "select * from tasks";
        try{
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet rs = statement.executeQuery();
            while(rs.next()){
                int taskId = rs.getInt("taskId");
                String taskTitle = rs.getString("taskTitle");
                String taskText = rs.getString("taskText");
                String assignedTo = rs.getString("assignedTo");
                boolean taskCompleted = rs.getBoolean("taskCompleted");
                Task task = new Task(taskId,taskTitle,taskText,assignedTo,taskCompleted);
                allTasks.add(task);
            }
        }
        catch(SQLException e){
            throw new Exception(e.getMessage());
        }
        return allTasks;
    }
    public List<Task> getAllTasks(boolean completed) throws Exception
    {
        List<Task> tasks = new ArrayList<>();
        String sql= "SELECT * FROM tasks WHERE completed = ?";
        try{
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setBoolean(1, completed);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Task task = new Task(rs.getInt("taskId"), rs.getString("taskTitle"), rs.getString("taskText"),
                        rs.getString("assignedTo"), rs.getBoolean("taskCompleted"));
                tasks.add(task);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return tasks;
    }
    public List<Task> getTaskByStatus(String email,boolean Status) throws Exception
    {
    	List<Task> statusList = new ArrayList<>();
		if(Status) {
			String sql = "SELECT * FROM tasks WHERE completed=? and assignedTo=?";
			try{
	            PreparedStatement statement = conn.prepareStatement(sql);
	            statement.setBoolean(1, Status);
	            statement.setString(2, email);
	            ResultSet rs = statement.executeQuery();
	            while (rs.next()) {
	                Task task = new Task(rs.getInt("taskId"), rs.getString("taskTitle"), rs.getString("taskText"),
	                        rs.getString("assignedTo"), rs.getBoolean("taskCompleted"));
	                statusList.add(task);
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
			
		}else {
			String sql = "SELECT * FROM task WHERE completed=? and assignedTo=?";
			try{
	            PreparedStatement statement = conn.prepareStatement(sql);
	            statement.setBoolean(1, Status);
	            statement.setString(2, email);
	            ResultSet rs = statement.executeQuery();
	            while (rs.next()) {
	                Task task = new Task(rs.getInt("taskId"), rs.getString("taskTitle"), rs.getString("taskText"),
	                        rs.getString("assignedTo"), rs.getBoolean("taskCompleted"));
	                statusList.add(task);
	            }
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
		}
		return statusList;
        //return getAllTasks(true);
    }

	public Task getTaskById(int taskId) throws Exception {
		// TODO Auto-generated method stub
		String sql = "select * from task where id=?";
  	  Task task = null;
	        try{
	            PreparedStatement statement = conn.prepareStatement(sql);
	            statement.setInt(1,taskId);
	            ResultSet rs = statement.executeQuery();

	            if(rs.next()){
	                task = new Task();
	                task.setTaskId(rs.getInt(1));
	                task.setTaskTitle(rs.getString(2));
	                task.setTaskText(rs.getString(3));
	                task.setAssignedTo(rs.getString(4));
	                task.setTaskCompleted(rs.getBoolean(5));
	            }
	            else
	                throw new Exception("No customer with "+taskId+" found");
	        } catch (SQLException e) {
	            throw new Exception(e.getMessage());
	        }
	        
	      return task;
	}
}
