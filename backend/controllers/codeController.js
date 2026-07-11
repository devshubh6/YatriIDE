const Code = require('../models/Code');
const { exec } = require('child_process');
const fs = require('fs');
const path = require('path');
const { promisify } = require('util');

const execPromise = promisify(exec);

// Save Code
exports.saveCode = async (req, res) => {
  try {
    const { title, code, language } = req.body;
    const userId = req.user.id;

    if (!title || !code) {
      return res.status(400).json({
        success: false,
        message: 'Please provide title and code'
      });
    }

    let savedCode = await Code.findOne({ userId, title });

    if (savedCode) {
      savedCode.code = code;
      savedCode.language = language || 'java';
      savedCode.updatedAt = new Date();
      await savedCode.save();
    } else {
      savedCode = await Code.create({
        userId,
        title,
        code,
        language: language || 'java'
      });
    }

    res.status(200).json({
      success: true,
      message: '✅ Code saved successfully!',
      data: savedCode
    });

  } catch (error) {
    res.status(500).json({
      success: false,
      message: error.message
    });
  }
};

// Get All Codes for User
exports.getUserCodes = async (req, res) => {
  try {
    const userId = req.user.id;

    const codes = await Code.find({ userId }).sort({ createdAt: -1 });

    res.status(200).json({
      success: true,
      count: codes.length,
      data: codes
    });

  } catch (error) {
    res.status(500).json({
      success: false,
      message: error.message
    });
  }
};

// Get Single Code
exports.getCode = async (req, res) => {
  try {
    const { id } = req.params;
    const userId = req.user.id;

    const code = await Code.findOne({ _id: id, userId });

    if (!code) {
      return res.status(404).json({
        success: false,
        message: 'Code not found'
      });
    }

    res.status(200).json({
      success: true,
      data: code
    });

  } catch (error) {
    res.status(500).json({
      success: false,
      message: error.message
    });
  }
};

// Delete Code
exports.deleteCode = async (req, res) => {
  try {
    const { id } = req.params;
    const userId = req.user.id;

    const code = await Code.findOneAndDelete({ _id: id, userId });

    if (!code) {
      return res.status(404).json({
        success: false,
        message: 'Code not found'
      });
    }

    res.status(200).json({
      success: true,
      message: '✅ Code deleted successfully!'
    });

  } catch (error) {
    res.status(500).json({
      success: false,
      message: error.message
    });
  }
};

// Compile Code
exports.compileCode = async (req, res) => {
  try {
    const { code, language, id } = req.body;
    const userId = req.user.id;

    if (!code || !language) {
      return res.status(400).json({
        success: false,
        message: 'Please provide code and language'
      });
    }

    let output = '';
    let compilationStatus = 'error';

    const tempDir = path.join(__dirname, '../temp');
    if (!fs.existsSync(tempDir)) {
      fs.mkdirSync(tempDir);
    }

    try {
      if (language === 'java') {
        output = await compileJava(code, tempDir);
        compilationStatus = 'success';
      } else if (language === 'python') {
        output = await compilePython(code, tempDir);
        compilationStatus = 'success';
      } else if (language === 'cpp') {
        output = await compileCpp(code, tempDir);
        compilationStatus = 'success';
      } else {
        output = '❌ Language not supported yet';
        compilationStatus = 'error';
      }
    } catch (error) {
      output = `❌ Compilation Error:\n${error.message}`;
      compilationStatus = 'error';
    }

    // Update code in database if id provided
    if (id) {
      await Code.findByIdAndUpdate(id, {
        lastOutput: output,
        compilationStatus
      });
    }

    res.status(200).json({
      success: compilationStatus === 'success',
      status: compilationStatus,
      output,
      timestamp: new Date()
    });

  } catch (error) {
    res.status(500).json({
      success: false,
      message: error.message
    });
  }
};

// Helper: Compile Java
async function compileJava(code, tempDir) {
  const fileName = 'Main.java';
  const filePath = path.join(tempDir, fileName);

  // Write file
  fs.writeFileSync(filePath, code);

  try {
    // Compile
    const { stdout: compileOut, stderr: compileErr } = await execPromise(
      `javac ${filePath}`,
      { timeout: 10000 }
    );

    if (compileErr) {
      throw new Error(compileErr);
    }

    // Run
    const { stdout: runOut, stderr: runErr } = await execPromise(
      `java -cp ${tempDir} Main`,
      { timeout: 10000, cwd: tempDir }
    );

    return runOut || (runErr ? `❌ Runtime Error:\n${runErr}` : '✅ Execution successful!');

  } catch (error) {
    throw error;
  }
}

// Helper: Compile Python
async function compilePython(code, tempDir) {
  const fileName = 'main.py';
  const filePath = path.join(tempDir, fileName);

  fs.writeFileSync(filePath, code);

  try {
    const { stdout, stderr } = await execPromise(
      `python3 ${filePath}`,
      { timeout: 10000 }
    );

    return stdout || (stderr ? `❌ Error:\n${stderr}` : '✅ Execution successful!');

  } catch (error) {
    throw error;
  }
}

// Helper: Compile C++
async function compileCpp(code, tempDir) {
  const fileName = 'main.cpp';
  const filePath = path.join(tempDir, fileName);

  fs.writeFileSync(filePath, code);

  try {
    const { stdout: compileOut, stderr: compileErr } = await execPromise(
      `g++ ${filePath} -o ${path.join(tempDir, 'main')}`,
      { timeout: 10000 }
    );

    if (compileErr) {
      throw new Error(compileErr);
    }

    const { stdout: runOut, stderr: runErr } = await execPromise(
      `${path.join(tempDir, 'main')}`,
      { timeout: 10000 }
    );

    return runOut || (runErr ? `❌ Runtime Error:\n${runErr}` : '✅ Execution successful!');

  } catch (error) {
    throw error;
  }
}
