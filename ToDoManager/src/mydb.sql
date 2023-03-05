create database mydb;
use mydb;
CREATE TABLE users (
  userName VARCHAR(255),
  email VARCHAR(255),
  password VARCHAR(255),
  userId INT PRIMARY KEY
);

CREATE TABLE tasks (
  taskTitle VARCHAR(255),
  taskText VARCHAR(1000),
  assignedTo VARCHAR(255),
  isCompleted BOOLEAN,
  taskId INT PRIMARY KEY
);
INSERT INTO users (userName, email, password,userId) VALUES
('John Doe', 'johndoe@example.com', 'password123',1),
('Jane Smith', 'janesmith@example.com', 'letmein123',2),
('Bob Johnson', 'bjohnson@example.com', 'secret321',3);

INSERT INTO tasks (taskTitle, taskText, assignedTo, isCompleted, taskId) VALUES
('Complete project proposal', 'Write a proposal outlining the scope and objectives of the project', 'johndoe@example.com', false,1),
('Research competitors', 'Conduct research on competitors in the market and document findings', 'janesmith@example.com', true,2),
('Develop prototype', 'Create a functional prototype of the product', 'bjohnson@example.com', false,3);
