const { Router } = require("express");
const { signInOwner, signUpOwner } = require("../controllers/auth.controllers");

const router = Router();

router.post("/signInOwner", signInOwner); // route to sign in owner
router.post("/signUpOwner", signUpOwner); //route to sign up owner

module.exports = router;
