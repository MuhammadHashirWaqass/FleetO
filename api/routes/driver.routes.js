const { Router } = require("express");
const {
  getDriversOfOwner,
  addDriverToOwner,
  deleteDriver,
} = require("../controllers/driver.controllers");
const router = Router();

router.post("/getDrivers", getDriversOfOwner);
router.post("/addDriver", addDriverToOwner);
router.post("/delete", deleteDriver);

module.exports = router;
