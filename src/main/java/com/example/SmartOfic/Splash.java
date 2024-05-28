package com.example.SmartOfic;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class Splash extends JFrame {
    private BufferedImage logoImage;
    private double scaleFactor = 0.2;
    private URL image;

    public Splash() {
        super("Splash");
        setSize(200, 230);
        setLocationRelativeTo(null); 
        setIconImage(Toolkit.getDefaultToolkit().getImage("F:\\TFG2\\SmartOfic\\src\\main\\resources\\drawable\\logo.png"));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Cargar la imagen del logo
        try {
            image = getClass().getResource("/drawable/logo.png");
            logoImage = ImageIO.read(image);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Crear un panel personalizado para dibujar la imagen
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Calcular la posición y el tamaño de la imagen para el efecto de zoom
                int imageWidth = (int) (logoImage.getWidth() * scaleFactor);
                int imageHeight = (int) (logoImage.getHeight() * scaleFactor);
                int x = (getWidth() - imageWidth) / 2;
                int y = (getHeight() - imageHeight) / 2;

                // Aplicar transformación de escala para el efecto de zoom
                AffineTransform at = AffineTransform.getScaleInstance(scaleFactor, scaleFactor);
                g2d.drawImage(logoImage, at, this);
            }
        };

        // Agregar el panel al marco
        getContentPane().add(panel);

        // Crear un temporizador para el efecto de zoom
        Timer zoomTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scaleFactor += 0.004; // Ajusta la velocidad del zoom según sea necesario
                repaint();
            }
        });
        zoomTimer.start();

        // Temporizador para abrir la ventana de inicio de sesión después de 5 segundos
        Timer loginTimer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Abre la ventana de inicio de sesión
                openLoginWindow();
                dispose();
            }
        });
        loginTimer.setRepeats(false); 
        loginTimer.start();
    }

    private void openLoginWindow() {
        Login loginWindow = new Login();
        loginWindow.setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Splash().setVisible(true);
            }
        });
    }
}
