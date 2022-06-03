import javax.swing.*;
import java.awt.*;

public class ErrorWindow extends JFrame{

    private JPanel errorPanel;
    private JLabel errorLabel;

    public ErrorWindow(String error) {

        setTitle("ERROR");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(250, 70);
        setLayout(null);
        setVisible(true);
        setResizable(false);

        errorPanel = new JPanel();
        errorPanel.setLayout(null);
        errorPanel.setSize(300, 200);
        errorPanel.setVisible(true);
        
        errorLabel = new JLabel(error);
        errorLabel.setBounds(10, 0, 300, 20);
        errorLabel.setForeground(Color.red);
        errorLabel.setFont(new Font("Arial", Font.BOLD, 15));

        errorPanel.add(errorLabel);
        add(errorPanel);
    }
}
