const express = require("express");
const app = express();
const cors = require("cors");
const PORT = 3000;

const authRoutes = require("../routes/auth.routes.js");
const driverRoutes = require("../routes/driver.routes.js");

app.use(express.json());
app.use(cors());

app.use("/api/auth", authRoutes);
app.use("/api/driver", driverRoutes);

app.listen(PORT, () => {
  console.log(`Listening on port: ${PORT}`);
});
