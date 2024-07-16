const { Router } = require("express");
const { addTaskToDriver } = require("../controllers/task.controllers");
const router = Router();

router.post("/addTaskToDriver", addTaskToDriver); // route to add task to driver

module.exports = router;
