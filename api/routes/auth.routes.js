const { Router } = require("express");
const {
  signInOwner,
  signUpOwner,
  signIndriver,
} = require("../controllers/auth.controllers");

const router = Router();

router.post("/signInOwner", signInOwner); // route to sign in owner
router.post("/signUpOwner", signUpOwner); //route to sign up owner
router.post("/signIndriver", signIndriver); // route to sign in driver

module.exports = router;
