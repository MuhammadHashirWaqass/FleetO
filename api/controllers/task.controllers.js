const connection = require("../config/db");

const addTaskToDriver = async (req, res) => {
  try {
    const { title, description, address, driverId } = req.body;

    connection.query(
      "INSERT INTO Task (title, description, status, address, driverId) VALUES (?,?,'pending',?, ?)",
      [title, description, address, driverId],
      (err, result) => {
        if (err) {
          return res.json({
            message: "Error while inserting into Task" + err.message,
          });
        }
        const insertedTaskId = result.insertId;
        connection.query(
          "INSERT INTO DriverTasks (taskId, driverId) VALUES (?,?)",
          [insertedTaskId, driverId],
          (err, result) => {
            if (err) {
              return res.json({
                message: "Error while inserting into DriverTasks" + err.message,
              });
            }
            return res.json({ message: "Inserted Task Successfully" });
          }
        );
      }
    );
  } catch (error) {
    return res.json({ message: error });
  }
};

const getOwnerTasks = async (req, res) => {
  try {
    const { ownerId } = req.body;

    connection.query(
      "SELECT Task.taskId, Task.title, Task.description, Task.status, Task.address, DriverTasks.driverId FROM OwnerDrivers JOIN DriverTasks ON DriverTasks.driverId = OwnerDrivers.driverId JOIN Task ON Task.taskId = DriverTasks.taskId WHERE OwnerDrivers.ownerId = ?",
      [ownerId],
      (err, result) => {
        if (err) {
          return res.json({ message: "ERROR: " + err });
        }
        return res.json({ message: "Loaded Tasks Successfully", data: result });
      }
    );
  } catch (error) {
    return res.json({ message: error });
  }
};

const deleteTask = async (req, res) => {
  try {
    const { taskId } = req.body;

    connection.query(
      "DELETE FROM DriverTasks WHERE taskId = ?",
      [taskId],
      (err, result) => {
        if (err) {
          return res.json({ message: "ERROR: " + err });
        }
        connection.query(
          "DELETE FROM Task WHERE taskId = ?",
          [taskId],
          (err, result) => {
            if (err) {
              return res.json({ message: "ERROR: " + err });
            }
            return res.json({ message: "Task Deleted Successfully" });
          }
        );
      }
    );
  } catch (error) {
    return res.json({ message: error });
  }
};

const getDriverTasks = async (req, res) => {
  try {
    const { driverId } = req.body;

    connection.query(
      "SELECT Task.taskId, Task.title, Task.description, Task.status, Task.address FROM DriverTasks JOIN Task ON Task.taskId = DriverTasks.taskId WHERE DriverTasks.driverId = ?",
      [driverId],
      (err, result) => {
        if (err) {
          return res.json({ message: "ERROR: " + err });
        }
        return res.json({ message: "Loaded Tasks Successfully", data: result });
      }
    );
  } catch (error) {
    return res.json({ message: error });
  }
};

const markTaskAsDone = async (req, res) => {
  try {
    const { taskId } = req.body;

    connection.query(
      "UPDATE Task SET status = 'completed' WHERE taskId = ?",
      [taskId],
      (err, result) => {
        if (err) {
          return res.json({ message: "ERROR: " + err });
        }
        return res.json({ message: "Updated Task Successfully" });
      }
    );
  } catch (error) {
    return res.json({ message: error });
  }
};

const updateTask = async (req, res) => {
  try {
    const { taskId, title, description, address } = req.body;

    connection.query(
      "UPDATE Task SET title = ?, description = ?, address = ? WHERE taskId = ?",
      [title, description, address, taskId],
      (err, result) => {
        if (err) {
          return res.json({ message: "ERROR: " + err });
        }
        return res.json({ message: "Edited Task Successfully" });
      }
    );
  } catch (error) {
    return res.json({ message: error });
  }
};
module.exports = {
  addTaskToDriver,
  getOwnerTasks,
  deleteTask,
  getDriverTasks,
  markTaskAsDone,
  updateTask,
};
