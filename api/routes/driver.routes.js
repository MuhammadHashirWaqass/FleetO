const { Router } = require("express");
const { getDriversOfOwner } = require("../controllers/driver.controllers");
const router = Router();

router.post("/getDrivers", getDriversOfOwner);
module.exports = router;
