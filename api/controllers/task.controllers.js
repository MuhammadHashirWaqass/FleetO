const connection = require("../config/db");

const addTaskToDriver = async (req, res) => {
  try {
    const { title, description, address, driverId } = req.body;

    connection.query(
      "INSERT INTO Task (title, description, status, address) VALUES (?,?,'pending',?)",
      [title, description, address],
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

module.exports = { addTaskToDriver };
