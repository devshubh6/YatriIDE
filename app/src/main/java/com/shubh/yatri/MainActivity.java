package com.shubh.yatri;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText codeEditor;
    private EditText consoleOutput;
    private Button btnCompile;
    private Button btnSave;
    private Button btnOpen;
    private Button btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        codeEditor = findViewById(R.id.codeEditor);
        consoleOutput = findViewById(R.id.consoleOutput);
        btnCompile = findViewById(R.id.btnCompile);
        btnSave = findViewById(R.id.btnSave);
        btnOpen = findViewById(R.id.btnOpen);
        btnClear = findViewById(R.id.btnClear);

        // Set button click listeners
        btnCompile.setOnClickListener(v -> compileCode());
        btnSave.setOnClickListener(v -> saveCode());
        btnOpen.setOnClickListener(v -> openFile());
        btnClear.setOnClickListener(v -> clearAll());
    }

    /**
     * Compile Java code using javac
     */
    private void compileCode() {
        String code = codeEditor.getText().toString().trim();

        if (code.isEmpty()) {
            consoleOutput.setText("❌ Error: Code editor is empty!");
            Toast.makeText(this, "Please write some code first!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Create temporary Java file
            File cacheDir = getCacheDir();
            File javaFile = new File(cacheDir, "TempCode.java");

            // Write code to file
            FileWriter fw = new FileWriter(javaFile);
            fw.write(code);
            fw.close();

            // Try to compile
            appendConsoleOutput("🔄 Compiling code...\n");
            appendConsoleOutput("📁 File: " + javaFile.getAbsolutePath() + "\n\n");

            // Check if javac is available
            if (isJavacAvailable()) {
                compileWithJavac(javaFile);
            } else {
                // Fallback: Just syntax check
                performSyntaxCheck(code);
            }

        } catch (Exception e) {
            appendConsoleOutput("❌ Compilation Error: " + e.getMessage());
            Toast.makeText(this, "Compilation failed!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Check if javac compiler is available
     */
    private boolean isJavacAvailable() {
        try {
            Process process = Runtime.getRuntime().exec("which javac");
            process.waitFor();
            return process.exitValue() == 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Compile using javac command
     */
    private void compileWithJavac(File javaFile) {
        try {
            Process process = Runtime.getRuntime().exec("javac " + javaFile.getAbsolutePath());
            int exitCode = process.waitFor();

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder errors = new StringBuilder();
            String line;

            while ((line = errorReader.readLine()) != null) {
                errors.append(line).append("\n");
            }

            if (exitCode == 0) {
                appendConsoleOutput("✅ Compilation Successful!\n");
                appendConsoleOutput("📦 Output: TempCode.class generated\n");
                appendConsoleOutput("⏱️  Time: " + getCurrentTime());
            } else {
                appendConsoleOutput("❌ Compilation Failed!\n");
                appendConsoleOutput(errors.toString());
            }

        } catch (Exception e) {
            performSyntaxCheck(codeEditor.getText().toString());
        }
    }

    /**
     * Basic syntax check for Java code
     */
    private void performSyntaxCheck(String code) {
        appendConsoleOutput("⚠️  Javac not available on this device\n");
        appendConsoleOutput("Running basic syntax check...\n\n");

        StringBuilder errors = new StringBuilder();
        boolean hasErrors = false;

        // Check for common Java syntax issues
        if (!code.contains("class ")) {
            errors.append("⚠️  Warning: No class definition found\n");
            hasErrors = true;
        }

        if (code.contains("public static void main")) {
            if (!code.contains("String[] args")) {
                errors.append("❌ Error: Main method signature incorrect\n");
                hasErrors = true;
            }
        }

        // Check bracket matching
        int openBrackets = countChar(code, '{');
        int closeBrackets = countChar(code, '}');
        if (openBrackets != closeBrackets) {
            errors.append("❌ Error: Bracket mismatch! { count: ")
                    .append(openBrackets)
                    .append(", } count: ")
                    .append(closeBrackets)
                    .append("\n");
            hasErrors = true;
        }

        // Check parenthesis matching
        int openParen = countChar(code, '(');
        int closeParen = countChar(code, ')');
        if (openParen != closeParen) {
            errors.append("❌ Error: Parenthesis mismatch! ( count: ")
                    .append(openParen)
                    .append(", ) count: ")
                    .append(closeParen)
                    .append("\n");
            hasErrors = true;
        }

        if (!hasErrors) {
            appendConsoleOutput("✅ Basic syntax check passed!\n");
            appendConsoleOutput("📝 Note: For full compilation, use a server with javac\n");
        } else {
            appendConsoleOutput(errors.toString());
        }

        appendConsoleOutput("\n⏱️  Time: " + getCurrentTime());
    }

    /**
     * Count occurrences of a character in string
     */
    private int countChar(String str, char ch) {
        int count = 0;
        for (char c : str.toCharArray()) {
            if (c == ch) count++;
        }
        return count;
    }

    /**
     * Save code to file
     */
    private void saveCode() {
        String code = codeEditor.getText().toString().trim();

        if (code.isEmpty()) {
            Toast.makeText(this, "Nothing to save!", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
            File cacheDir = getCacheDir();
            File saveFile = new File(cacheDir, "Code_" + timestamp + ".java");

            FileWriter fw = new FileWriter(saveFile);
            fw.write(code);
            fw.close();

            String msg = "✅ File saved:\n" + saveFile.getName() + "\n" + saveFile.getAbsolutePath();
            appendConsoleOutput(msg);
            Toast.makeText(this, "File saved successfully!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            appendConsoleOutput("❌ Save Error: " + e.getMessage());
            Toast.makeText(this, "Failed to save file!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Open file (placeholder for future implementation)
     */
    private void openFile() {
        try {
            File cacheDir = getCacheDir();
            File[] files = cacheDir.listFiles((dir, name) -> name.endsWith(".java"));

            if (files != null && files.length > 0) {
                appendConsoleOutput("📁 Available files:\n");
                for (File file : files) {
                    appendConsoleOutput("  • " + file.getName() + "\n");
                }
                appendConsoleOutput("\n💡 Tip: Long-click on a file to open it\n");
            } else {
                appendConsoleOutput("📭 No saved files found\n");
            }

            Toast.makeText(this, "File browser feature coming soon!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            appendConsoleOutput("❌ Error: " + e.getMessage());
        }
    }

    /**
     * Clear editor and console
     */
    private void clearAll() {
        codeEditor.setText("");
        consoleOutput.setText("");
        Toast.makeText(this, "Editor cleared!", Toast.LENGTH_SHORT).show();
    }

    /**
     * Append text to console output
     */
    private void appendConsoleOutput(String text) {
        consoleOutput.append(text);
    }

    /**
     * Get current time for logging
     */
    private String getCurrentTime() {
        return new SimpleDateFormat("HH:mm:ss", Locale.US).format(new Date());
    }
}
