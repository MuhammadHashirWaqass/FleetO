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

module.exports = { getDriversOfOwner };
