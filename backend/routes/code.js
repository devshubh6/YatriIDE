const express = require('express');
const router = express.Router();
const { 
  saveCode, 
  getUserCodes, 
  getCode, 
  deleteCode, 
  compileCode 
} = require('../controllers/codeController');
const { protect } = require('../middleware/auth');

// All routes are protected
router.use(protect);

// Code CRUD operations
router.post('/save', saveCode);
router.get('/all', getUserCodes);
router.get('/:id', getCode);
router.delete('/:id', deleteCode);

// Compilation
router.post('/compile', compileCode);

module.exports = router;
