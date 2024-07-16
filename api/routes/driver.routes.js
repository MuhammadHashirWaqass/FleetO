const { Router } = require("express");
const {
  getDriversOfOwner,
  addDriverToOwner,
} = require("../controllers/driver.controllers");
const router = Router();

router.post("/getDrivers", getDriversOfOwner);
router.post("/addDriver", addDriverToOwner);

module.exports = router;
