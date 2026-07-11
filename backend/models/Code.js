const mongoose = require('mongoose');

const codeSchema = new mongoose.Schema({
  userId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    required: true
  },
  title: {
    type: String,
    required: [true, 'Please provide a title'],
    trim: true
  },
  code: {
    type: String,
    required: [true, 'Please provide code'],
    default: ''
  },
  language: {
    type: String,
    enum: ['java', 'python', 'cpp', 'javascript'],
    default: 'java'
  },
  lastOutput: {
    type: String,
    default: ''
  },
  compilationStatus: {
    type: String,
    enum: ['success', 'error', 'pending', 'none'],
    default: 'none'
  },
  createdAt: {
    type: Date,
    default: Date.now
  },
  updatedAt: {
    type: Date,
    default: Date.now
  }
});

module.exports = mongoose.model('Code', codeSchema);
