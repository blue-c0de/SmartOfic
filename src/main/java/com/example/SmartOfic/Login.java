package com.example.SmartOfic;

import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.maxmind.geoip2.exception.GeoIp2Exception;

public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton btnNewButton;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Login frame = new Login();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Login() {
        super("Login Oficinista");
        setResizable(false);
        setIconImage(Toolkit.getDefaultToolkit().getImage("F:\\TFG2\\SmartOfic\\src\\main\\resources\\drawable\\logo.png"));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(323, 169);
        contentPane = new JPanel(new GridLayout(3, 1, 10, 10)); 
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);

        JPanel userPanel = new JPanel(new GridLayout(1, 2, 10, 10)); 
        JLabel usuario = new JLabel("Usuario");
        userPanel.add(usuario);
        textField = new JTextField();
        userPanel.add(textField);

        JPanel passPanel = new JPanel(new GridLayout(1, 2, 10, 10)); 
        JLabel contrasena = new JLabel("Contraseña");
        passPanel.add(contrasena);
        passwordField = new JPasswordField();
        passPanel.add(passwordField);

        JPanel buttonPanel = new JPanel();
        btnNewButton = new JButton("INICIAR SESIÓN");
        btnNewButton.setVerticalAlignment(SwingConstants.BOTTOM);
        buttonPanel.add(btnNewButton);
        
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String usu = textField.getText();
                String con = new String(passwordField.getPassword());

                if (!usu.isEmpty() && !con.isEmpty()) {
                    try {
						loginUser(usu, con);
					} catch (GeoIp2Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                } else {
                	JOptionPane.showMessageDialog(new JFrame(),
                            "No existe",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        contentPane.add(userPanel);
        contentPane.add(passPanel); 
        contentPane.add(buttonPanel); 
        
        setLocationRelativeTo(null);
    }
    
    private void inicializarFirebase() throws IOException {
    	if (FirebaseApp.getApps().isEmpty()) {
	    	FileInputStream refreshToken = new FileInputStream("F:\\TFG2\\SmartOfic\\src\\main\\resources\\smartoper-d431a-firebase-adminsdk-5obz7-b83ccb291e.json");
	
	    	FirebaseOptions options = FirebaseOptions.builder()
	    	    .setCredentials(GoogleCredentials.fromStream(refreshToken))
	    	    .setProjectId("smartoper-d431a")
	    	    .build();
	
	    	FirebaseApp.initializeApp(options);
    	}
    }
    
    private void loginUser(String usu, String password) throws GeoIp2Exception {
    	try {
            inicializarFirebase();
            Firestore db = FirestoreClient.getFirestore();
            
            ApiFuture<DocumentSnapshot> future = db.collection("oficinista").document(usu).get();
            DocumentSnapshot document = future.get();

            if (document.exists()) {
                String contra = document.getString("password");
                
                if (contra.equals(password)) {
                    openMainWindow();
                    dispose();
                } else {
                	JOptionPane.showMessageDialog(new JFrame(),
                            "Usuario/Contraseña incorrecto",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            } 
        } catch (IOException | InterruptedException | ExecutionException e) {}
    }
    
    private void openMainWindow() throws IOException, GeoIp2Exception, InterruptedException, ExecutionException {
        Opciones main = new Opciones();
        main.setVisible(true);
        this.dispose();
    }
}

