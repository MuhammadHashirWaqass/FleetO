const connection = require("../config/db");

const getDriversOfOwner = async (req, res) => {
  try {
    const { userId } = req.body;

    connection.query(
      "SELECT Driver.* FROM OwnerDrivers JOIN Driver ON Driver.driverId = OwnerDrivers.driverId WHERE OwnerDrivers.ownerId = ?",
      [userId],
      (err, result) => {
        if (err) {
          return res.json({ message: "Error" + err.message });
        }
        return res.json({ data: result });
      }
    );
  } catch (error) {
    return res.json(error);
  }
};

const addDriverToOwner = async (req, res) => {
  try {
    const { name, age, password, vehicle, ownerId, licenseNumber } = req.body;

    connection.query(
      "INSERT INTO Driver(name, password, age, vehicle, licenseNumber) VALUES (?,?,?,?,?)",
      [name, password, age, vehicle, licenseNumber],
      (err, result) => {
        if (err) {
          return res.json({
            message: "Error while inserting into Driver" + err.message,
          });
        }
        const insertedDriverId = result.insertId;
        connection.query(
          "INSERT INTO OwnerDrivers (ownerId, driverId) VALUES (?,?)",
          [ownerId, insertedDriverId],
          (err, result) => {
            if (err) {
              return res.json({
                message:
                  "Error while inserting into OwnerDrivers" + err.message,
              });
            }
            return res.json({ message: "Inserted Driver Successfully" });
          }
        );
      }
    );
  } catch (error) {
    return res.json({ message: error });
  }
};

const deleteDriver = async (req, res) => {
  try {
    const { driverId } = req.body;

    connection.query(
      "DELETE FROM Driver WHERE driverId = ?",
      [driverId],
      (err, result) => {
        if (err) {
          return res.json({ message: "ERROR: " + err });
        }
        return res.json({ message: "Deleted Driver Successfully" });
      }
    );
  } catch (error) {
    return res.json({ message: error });
  }
};

module.exports = { getDriversOfOwner, addDriverToOwner, deleteDriver };
