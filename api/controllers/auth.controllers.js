const connection = require("../config/db");

const signInOwner = async (req, res) => {
  try {
  } catch (error) {
    res.json(error);
  }
};

const signUpOwner = async (req, res) => {
  const { name, email, password } = req.body;

  // Check if user already exists
  connection.query(
    "SELECT * FROM Owner WHERE email = ?",
    [email],
    (err, result) => {
      if (err) {
        return res.json({ message: err.message });
      }

      if (result.length === 1) {
        return res.json({ message: "User Already Exists" });
      }

      connection.query(
        "INSERT INTO Owner(email, name, password) VALUES (?,?,?)",
        [email, name, password],
        (err, result) => {
          if (err) {
            return res.json({ message: err.message });
          }
          return res.json({ message: "User Added Successfully" });
        }
      );
    }
  );
};

module.exports = { signInOwner, signUpOwner };
