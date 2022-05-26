import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileNotFoundException;

public class Login implements ActionListener {
    
    private JFrame WIN;
    private JPanel panel;
    private JPasswordField password;
    private JLabel status, title;

    public Login() {
        
        //Window Configuration
        WIN = new JFrame();
        WIN.setSize(400, 300);
        WIN.setResizable(false);
        WIN.setLocationRelativeTo(null);
        WIN.setTitle("Login");
        WIN.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JLabel enterPass = new JLabel("Enter Password:");
        enterPass.setBounds(90, 75, 100, 20);
        
        password = new JPasswordField(20);
        password.setBounds(200, 75, 100, 20);
        
        JButton login = new JButton("Login");
        login.setBounds(142, 200, 100, 20);
        login.addActionListener(this);

        status = new JLabel("");
        status.setFont(new Font("Arial", Font.BOLD, 20));
        status.setBounds(100, 140, 200, 20);
        status.setForeground(Color.red);

        title = new JLabel("Login Menu");
        title.setFont(new Font("Verdana", Font.BOLD, 20));
        title.setBounds(127, 0, 130, 50);
        
        panel = new JPanel();
        panel.setLayout(null);
        panel.add(enterPass);
        panel.add(password);
        panel.add(login);
        panel.add(status);
        panel.add(title);

        WIN.add(panel);
        WIN.setVisible(true);
        
    }

    public static void main(String [] args) {
        new Login();        
    }

    @Override
    public void actionPerformed(ActionEvent click) {
        try {
            String currPass = "";
            for (int x = 0; x < password.getPassword().length; x++) {
                currPass += password.getPassword()[x];
            }

            if (currPass.equals("Ryan")) {
                new Inventory(WIN, panel);
            } else {
                status.setText("Incorrect Password");
            }
        } catch (FileNotFoundException error) {
            System.out.println(error.getMessage());
        }
    } 
}