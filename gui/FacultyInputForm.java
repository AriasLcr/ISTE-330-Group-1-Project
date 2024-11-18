import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FacultyInputForm extends JFrame {
    private JTextField titleField;
    private JFileChooser fileChooser;
    private JButton submitButton;

    public FacultyInputForm() {
        setTitle("Submit Abstract");
        setSize(350, 200);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Input panel setup
        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        add(inputPanel, BorderLayout.NORTH);

        // Title field
        inputPanel.add(new JLabel("Title:"));
        titleField = new JTextField();
        inputPanel.add(titleField);

        // File chooser setup
        fileChooser = new JFileChooser();
        JButton chooseFileButton = new JButton("Choose File");
        inputPanel.add(new JLabel("Abstract File:"));
        inputPanel.add(chooseFileButton);
        chooseFileButton.addActionListener(e -> fileChooser.showOpenDialog(this));

        // Submit button setup
        submitButton = new JButton("Submit Abstract");
        add(submitButton, BorderLayout.SOUTH);
        submitButton.addActionListener(e -> submitAbstract());

        setVisible(true);
    }

    private void submitAbstract() {
        String title = titleField.getText();
        File file = fileChooser.getSelectedFile();
        if (file != null && title != null && !title.isEmpty()) {
            try {
                File destination = new File("ISTE-330-Group-1-Project-main/Abstracts", file.getName());
                Files.copy(file.toPath(), destination.toPath(), StandardCopyOption.REPLACE_EXISTING);
                FacultyService.saveAbstract(title, destination.getAbsolutePath());
                JOptionPane.showMessageDialog(this, "Abstract submitted successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error submitting abstract: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please fill in all fields", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new FacultyInputForm();
    }
}
