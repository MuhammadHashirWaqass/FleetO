const mysql = require("mysql");
const dotenv = require("dotenv");
dotenv.config();

const connection = mysql.createPool({
  waitForConnections: true,
  queueLimit: 0,
  host: process.env.DB_HOST,
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_DATABASE,
  debug: true,
  wait_timeout: 28800,
  connect_timeout: 10,
  port: process.env.DB_PORT,
});

module.exports = connection;
