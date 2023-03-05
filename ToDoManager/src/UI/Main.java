package UI;

import java.util.List;
import java.util.Scanner;

import Model.User;
import Model.Task;
import Dao.TaskDao;
import Dao.UserDao;

public class Main {
    private static final Scanner ob = new Scanner(System.in);
    private static TaskDao taskDao = new TaskDao();
    private static UserDao userDao = new UserDao();

    public static void main(String[] args) {
        User currentUser = authenticateUser();

        boolean exit = false;

        while(!exit){
            System.out.println("Please select an option:");
            System.out.println("1. Add a task");
            System.out.println("2. Update a task");
            System.out.println("3. Delete a task");
            System.out.println("4. Search for a task");
            System.out.println("5. View all tasks");
            System.out.println("6. View completed tasks");
            System.out.println("7. View incomplete tasks");
            System.out.println("0. Exit");

            int choice = ob.nextInt();
            switch(choice)
            {
                case 1:
				try {
					addTask(currentUser);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
                    break;
                case 2:
                    updateTask(currentUser);
                    break;
                case 3:
                    deleteTask(currentUser);
                    break;
                case 4:
                    searchTasks(currentUser);
                    break;
                case 5:
                    viewAllTasks(currentUser);
                    break;
                case 6:
                    viewCompletedTasks(currentUser);
                    break;
                case 7:
                    viewIncompleteTasks(currentUser);
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static User authenticateUser() {
        System.out.println("Please enter your email address:");
        String email = ob.nextLine();

        System.out.println("Please enter your password:");
        String password = ob.nextLine();

        User currentUser=null;
		try {
			currentUser = userDao.getUserByEmailAndPassword(email, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (currentUser == null) {
            System.out.println("Invalid email/password. Please try again.");
            return authenticateUser();
        }

        System.out.println("Welcome, " + currentUser.getUserName() + "!");
        return currentUser;
    }

    private static void addTask(User currentUser) throws Exception{
        System.out.println("Enter Task Id:");
        int taskId = ob.nextInt();
        System.out.println("Enter Task Title:");
        String title = ob.next();
        System.out.println("Enter task description:");
        String description = ob.next();

        Task task = new Task();
        task.setTaskId(taskId);
        task.setTaskTitle(title);
        task.setTaskText(description);
        task.setAssignedTo(currentUser.getEmail());

        taskDao.addTask(task);

        System.out.println("Task added successfully!");
    }

    private static void updateTask(User currentUser) {
            System.out.println("Please enter the ID of the task you want to update:");
            int taskId = ob.nextInt();

            ob.nextLine();

            Task taskToUpdate=null;
			try {
				taskToUpdate = taskDao.getTaskById(taskId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            if (taskToUpdate == null)
            {
                System.out.println("Invalid task ID. Please try again.");
                return;
            }

            if (!taskToUpdate.getAssignedTo().equals(currentUser.getEmail())) {

                System.out.println("You are not authorized to update this task.");
                return;
            }

            System.out.println("Please enter the updated task title (or press Enter to skip):");
            String newTaskTitle = ob.nextLine().trim();

            System.out.println("Please enter the updated task description (or press Enter to skip):");
            String newTaskText = ob.nextLine().trim();

            if (!newTaskTitle.isEmpty()) {
                taskToUpdate.setTaskTitle(newTaskTitle);
            }

            if (!newTaskText.isEmpty()) {
                taskToUpdate.setTaskText(newTaskText);
            }

            try {
				taskDao.updateTask(taskToUpdate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            System.out.println("Task updated successfully!");
    }

    private static void deleteTask(User currentUser) {
        System.out.println("Please enter the ID of the task you want to delete:");
        int taskId = ob.nextInt();
        ob.nextLine();

        Task taskToDelete=null;
		try {
			taskToDelete = taskDao.getTaskById(taskId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (taskToDelete == null) {
            System.out.println("Invalid task ID. Please try again.");
            return;
        }

        if (!taskToDelete.getAssignedTo().equals(currentUser.getEmail()))
        {
            System.out.println("You are not authorized to delete this task.");
            return;
        }

        try {
			taskDao.deleteTask(taskToDelete);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        System.out.println("Task deleted successfully!");

    }

    private static void searchTasks(User currentUser) {
        System.out.println("Please enter a keyword to search for in task titles and descriptions:");
        String keyword = ob.nextLine().trim();

        List<Task> searchResults=null;
		try {
			searchResults = taskDao.searchTasks(keyword, currentUser.getEmail());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (searchResults.isEmpty()) {
            System.out.println("No matching tasks found.");
        } else {
            System.out.println("Matching tasks:");

            for (Task task : searchResults)
            {
                System.out.println(task);
            }
        }
    }

    private static void viewAllTasks(User currentUser) {
        List<Task> tasks=null;
		try {
			tasks = taskDao.viewAllTasks(currentUser.getEmail());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            System.out.println("All tasks:");
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
    }

    private static void viewCompletedTasks(User currentUser) {
        List<Task> tasks=null;
		try {
			tasks = taskDao.getTaskByStatus(currentUser.getEmail(), true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (tasks.isEmpty()) {
            System.out.println("No completed tasks found.");
        }
        else
        {
            System.out.println("Completed tasks:");
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
    }

    private static void viewIncompleteTasks(User currentUser) {
        List<Task> tasks=null;
		try {
			tasks = taskDao.getTaskByStatus(currentUser.getEmail(), false);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        if (tasks.isEmpty()) {
            System.out.println("No incomplete tasks found.");
        } else {
            System.out.println("Incomplete tasks:");
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
    }
}
