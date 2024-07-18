const { Router } = require("express");
const {
  addTaskToDriver,
  getOwnerTasks,
  deleteTask,
} = require("../controllers/task.controllers");
const router = Router();

router.post("/addTaskToDriver", addTaskToDriver); // route to add task to driver
router.post("/getOwnerTasks", getOwnerTasks); // route to get all owner tasks
router.post("/deleteTask", deleteTask); // route to get all owner tasks

module.exports = router;
