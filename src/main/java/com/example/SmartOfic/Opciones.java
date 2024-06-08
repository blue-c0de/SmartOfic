package com.example.SmartOfic;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import com.google.api.core.ApiFuture;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.TileFactoryInfo;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;

import javax.swing.*;
import java.util.HashSet;
import java.util.Set;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.layout.element.Image;

import com.sun.mail.util.MailSSLSocketFactory;

public class Opciones extends JFrame {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPane;
    private final CardLayout cardLayout;
    private double latitude = 0, longitude = 0;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Opciones frame = new Opciones();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Opciones() throws IOException, GeoIp2Exception, InterruptedException, ExecutionException {
        super("Opciones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage("F:/TFG2/SmartOfic/src/main/resources/drawable/logo.png"));
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximiza la ventana
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        contentPane = new JPanel(cardLayout);

        // Crear páginas
        JPanel primerPanel = createFirstPanel();
        JPanel segundoPanel = createSecondPanel();
        JPanel terceroPanel = createThirdPanel();
        JPanel cuartoPanel = createFourthPanel();
        JPanel quintoPanel = createFifthPanel();

        // Agregar páginas al cardPanel
        contentPane.add(primerPanel, "Primera Página");
        contentPane.add(segundoPanel, "Segunda Página");
        contentPane.add(terceroPanel, "Tercera Página");
        contentPane.add(cuartoPanel, "Cuarta Página");
        contentPane.add(quintoPanel, "Quinta Página");

        // Agregar cardPanel al JFrame
        getContentPane().add(contentPane);

        // Hacer visible el JFrame
        setVisible(true);
    }

    private JPanel createFirstPanel() {
        JPanel main = new JPanel(new GridLayout(5, 1, 10, 10));
        main.setBorder(new EmptyBorder(10, 10, 10, 10));
        main.setBackground(new Color(240, 248, 255)); // Light pastel color

        JLabel titulo = new JLabel("Escoge la opción del parte");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        main.add(titulo);

        JButton opcionUno = new JButton("Crear");
        opcionUno.setBackground(new Color(255, 239, 213)); // Light pastel color
        opcionUno.setFocusPainted(false);
        opcionUno.addActionListener(e -> cardLayout.show(contentPane, "Segunda Página"));
        main.add(opcionUno);

        JButton opcionDos = new JButton("Consultar/Modificar");
        opcionDos.setBackground(new Color(255, 239, 213)); // Light pastel color
        opcionDos.setFocusPainted(false);
        opcionDos.addActionListener(e -> cardLayout.show(contentPane, "Tercera Página"));
        main.add(opcionDos);

        JButton opcionTres = new JButton("Exportar Incidencias Resueltas en PDF");
        opcionTres.setBackground(new Color(255, 239, 213)); // Light pastel color
        opcionTres.setFocusPainted(false);
        opcionTres.addActionListener(e -> cardLayout.show(contentPane, "Cuarta Página"));
        main.add(opcionTres);

        JButton opcionCuatro = new JButton("Mapa de Operarios");
        opcionCuatro.setBackground(new Color(255, 239, 213)); // Light pastel color
        opcionCuatro.setFocusPainted(false);
        opcionCuatro.addActionListener(e -> cardLayout.show(contentPane, "Quinta Página"));
        main.add(opcionCuatro);

        return main;
    }

    private JPanel createSecondPanel() throws IOException, ExecutionException, InterruptedException {
        JPanel agregar = new JPanel(new GridLayout(9, 1, 10, 10));
        agregar.setBorder(new EmptyBorder(10, 10, 10, 10));
        agregar.setBackground(new Color(240, 248, 255)); // Light pastel color

        // FORMULARIO
        JLabel label = new JLabel("Rellenar Formulario");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        agregar.add(label);

        // ESTADO
        JPanel estadoPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        estadoPanel.setBackground(new Color(240, 248, 255)); // Light pastel color

        JLabel estadoLabel = new JLabel("Estado");
        estadoLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JRadioButton normal = new JRadioButton("NORMAL");
        JRadioButton grave = new JRadioButton("GRAVE");

        ButtonGroup group = new ButtonGroup();
        group.add(grave);
        group.add(normal);

        estadoPanel.add(estadoLabel);
        estadoPanel.add(normal);
        estadoPanel.add(grave);

        // TITULO
        JPanel tituloPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        tituloPanel.setBackground(new Color(240, 248, 255)); // Light pastel color

        JLabel tituloLabel = new JLabel("Título de Incidencia");
        tituloLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        JTextField textField = new JTextField();

        tituloPanel.add(tituloLabel);
        tituloPanel.add(textField);

        // CATEGORIA
        JPanel categoriaPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        categoriaPanel.setBackground(new Color(240, 248, 255)); // Light pastel color

        JLabel categoriaLabel = new JLabel("Categoría");
        categoriaLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        String[] categorias = {
                "Escoge una categoría",
                "Electricidad",
                "Agua",
                "Gas",
                "Telecomunicaciones",
                "Residuos Sólidos",
                "Transporte Público",
                "Vialidad",
                "Sanidad",
                "Educación",
                "Seguridad"
        };

        JComboBox<String> comboBox = new JComboBox<>(categorias);

        categoriaPanel.add(categoriaLabel);
        categoriaPanel.add(comboBox);

        // OPERARIO
        JPanel operarioPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        operarioPanel.setBackground(new Color(240, 248, 255)); // Light pastel color

        JLabel operarioLabel = new JLabel("Operarios");
        operarioLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        inicializarFirebase();
        Firestore db = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = db.collection("oficinista").get().get();

        List<String> ope = new ArrayList<>();

        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            ope.add(document.getId());
        }

        String[] operarios = new String[ope.size() + 1];
        operarios[0] = "Escoge un operario";

        for (int i = 0; i < ope.size(); i++) {
            operarios[i + 1] = ope.get(i);
        }

        JComboBox<String> comboOpeBox = new JComboBox<>(operarios);

        operarioPanel.add(operarioLabel);
        operarioPanel.add(comboOpeBox);

        // CONTACTO
        JPanel contactoPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        contactoPanel.setBackground(new Color(240, 248, 255)); // Light pastel color

        JLabel contactoLabel = new JLabel("Número de contacto");
        contactoLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JTextField contactoField = new JTextField();

        contactoPanel.add(contactoLabel);
        contactoPanel.add(contactoField);

        // DIRECCION
        JPanel dirPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        dirPanel.setBackground(new Color(240, 248, 255)); // Light pastel color

        JLabel dirLabel = new JLabel("Dirección de domicilio");
        dirLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JTextField dirField = new JTextField();

        dirPanel.add(dirLabel);
        dirPanel.add(dirField);

        // DESCRIPCION
        JPanel descPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        descPanel.setBackground(new Color(240, 248, 255)); // Light pastel color

        JLabel descLabel = new JLabel("Descripción de Incidencia");
        descLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        JTextArea descField = new JTextArea(10, 30);

        descPanel.add(descLabel);
        descPanel.add(descField);

        // BOTONES
        JPanel enviarPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        enviarPanel.setBackground(new Color(240, 248, 255)); // Light pastel color

        JButton volver = new JButton("Volver");
        volver.setFont(new Font("Arial", Font.PLAIN, 12));
        volver.setBackground(new Color(173, 216, 230)); // Light blue color
        volver.setFocusPainted(false);
        volver.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        volver.addActionListener(e -> {
            cardLayout.show(contentPane, "Primera Página");

            try {
                dispose();
                new Opciones().setVisible(true);
            } catch (IOException | GeoIp2Exception | InterruptedException | ExecutionException e1) {
                e1.printStackTrace();
            }
        });

        JButton enviar = new JButton("Enviar");
        enviar.setFont(new Font("Arial", Font.PLAIN, 12));
        enviar.setBackground(new Color(173, 216, 230)); // Light blue color
        enviar.setFocusPainted(false);
        enviar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        enviar.addActionListener(e -> {
            try {
                inicializarFirebase();
                Firestore db1 = FirestoreClient.getFirestore();

                // Obtener los datos de los campos
                String titulo = textField.getText();
                String contacto = contactoField.getText();
                String direccion = dirField.getText();
                String descripcion = descField.getText();
                String estado = normal.isSelected() ? "NORMAL" : grave.isSelected() ? "GRAVE" : "SIN_ESTADO";

                String categoria;
                if (comboBox.getSelectedItem().equals("Escoge una categoría")) {
                    JOptionPane.showMessageDialog(null, "Por favor, escoge una categoría válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    categoria = (String) comboBox.getSelectedItem();
                }

                String oper;
                if (comboOpeBox.getSelectedItem().equals("Escoge un operario")) {
                    JOptionPane.showMessageDialog(null, "Por favor, escoge un operario válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    oper = (String) comboOpeBox.getSelectedItem();
                }

                if (estado.equals("SIN_ESTADO")) {
                    JOptionPane.showMessageDialog(null, "Por favor, escoge un estado válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (contacto.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, introduzca el número de contacto.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (direccion.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, introduzca la dirección del domicilio.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (descripcion.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, introduzca la descripción del problema.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (titulo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, introduzca el título.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Crear un mapa con los datos
                Map<String, Object> incidencia = new HashMap<>();
                incidencia.put("estado", estado);
                incidencia.put("titulo", titulo);
                incidencia.put("categoria", categoria);
                incidencia.put("operario", oper);
                incidencia.put("contacto", contacto);
                incidencia.put("direccion", direccion);
                incidencia.put("descripcion", descripcion);

                // Obtener la referencia a la colección de Firestore usando el estado
                DocumentReference ref = db1.collection(estado).document();

                // Enviar los datos a Firestore
                ApiFuture<WriteResult> result = ref.set(incidencia);

                try {
                    // Confirmar el resultado
                    WriteResult writeResult = result.get();
                    System.out.println("Update time : " + writeResult.getUpdateTime());

                    // Mostrar mensaje de éxito
                    JOptionPane.showMessageDialog(null, "Datos enviados correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    // Limpiar todos los campos
                    textField.setText("");
                    comboBox.setSelectedIndex(0);
                    contactoField.setText("");
                    dirField.setText("");
                    descField.setText("");
                    group.clearSelection();

                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        enviarPanel.add(volver);
        enviarPanel.add(enviar);

        agregar.add(estadoPanel);
        agregar.add(tituloPanel);
        agregar.add(categoriaPanel);
        agregar.add(operarioPanel);
        agregar.add(contactoPanel);
        agregar.add(dirPanel);
        agregar.add(descPanel);
        agregar.add(enviarPanel);

        return agregar;
    }

    private JPanel createThirdPanel() throws IOException {
        JPanel modificar = new JPanel(new BorderLayout());
        modificar.setBorder(new EmptyBorder(10, 10, 10, 10));
        modificar.setBackground(new Color(240, 248, 255)); // Light pastel color

        // LISTA
        CustomListModel listModel = new CustomListModel();
        JList<String> lista = new JList<>(listModel);
        lista.setBackground(new Color(255, 239, 213)); // Light pastel color
        lista.setSelectionBackground(new Color(255, 228, 225)); // Another pastel color

        String[] estados = {"GRAVE", "NORMAL"};

        List<String> states = new ArrayList<>();
        List<String> docs = new ArrayList<>();

        for (String estado : estados) {
            inicializarFirebase();
            CollectionReference collectionRef = FirestoreClient.getFirestore().collection(estado);
            ApiFuture<QuerySnapshot> future = collectionRef.get();

            try {
                QuerySnapshot querySnapshot = future.get();
                List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

                for (DocumentSnapshot document : documents) {
                    // ESTABLECER LISTA
                    String titulo = document.getString("titulo");
                    String estadoDocumento = document.getString("estado");
                    listModel.addElement("Título: " + titulo + ", Estado: " + estadoDocumento);

                    states.add(estadoDocumento);
                    docs.add(document.getId());
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        // SELECCIONAR
        lista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = lista.locationToIndex(e.getPoint());
                JPanel modificarPanel;
                try {
                    modificarPanel = modificarPanel(states.get(index), docs.get(index));
                } catch (IOException | ExecutionException | InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                contentPane.add(modificarPanel, "Modificar Página");
                cardLayout.show(contentPane, "Modificar Página");
            }
        });

        // BOTONES
        JButton volver = new JButton("Volver");
        volver.setFont(new Font("Arial", Font.PLAIN, 12));
        volver.setBackground(new Color(173, 216, 230)); // Light blue color
        volver.setFocusPainted(false);
        volver.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        volver.setPreferredSize(new java.awt.Dimension(80, 25));
        volver.addActionListener(e -> cardLayout.show(contentPane, "Primera Página"));

        modificar.add(new JScrollPane(lista), BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(240, 248, 255)); // Match the main panel background
        bottomPanel.add(volver);
        modificar.add(bottomPanel, BorderLayout.SOUTH);

        return modificar;
    }

    private JPanel modificarPanel(String coleccion, String documento) throws IOException, ExecutionException, InterruptedException {
        JPanel modificar = new JPanel(new GridLayout(9, 1, 10, 10));
        modificar.setBorder(new EmptyBorder(10, 10, 10, 10));
        modificar.setBackground(new Color(240, 248, 255)); // Light pastel color

        // FORMULARIO
        JLabel label = new JLabel("Rellenar Formulario");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        modificar.add(label);

        // ESTADO
        JPanel estadoPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        estadoPanel.setBackground(new Color(240, 248, 255)); // Light pastel color

        JLabel estadoLabel = new JLabel("Estado");
        estadoLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JRadioButton normal = new JRadioButton("NORMAL");
        JRadioButton grave = new JRadioButton("GRAVE");

        ButtonGroup group = new ButtonGroup();
        group.add(grave);
        group.add(normal);

        estadoPanel.add(estadoLabel);
        estadoPanel.add(normal);
        estadoPanel.add(grave);

        // TITULO
        JPanel tituloPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        tituloPanel.setBackground(new Color(240, 248, 255)); // Light pastel color

        JLabel tituloLabel = new JLabel("Título de Incidencia");
        tituloLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        JTextField textField = new JTextField();

        tituloPanel.add(tituloLabel);
        tituloPanel.add(textField);

        // CATEGORIA
        JPanel categoriaPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        categoriaPanel.setBackground(new Color(240, 248, 255)); // Light pastel color

        JLabel categoriaLabel = new JLabel("Categoría");
        categoriaLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        String[] categorias = {
                "Escoge una categoría",
                "Electricidad",
                "Agua",
                "Gas",
                "Telecomunicaciones",
                "Residuos Sólidos",
                "Transporte Público",
                "Vialidad",
                "Sanidad",
                "Educación",
                "Seguridad"
        };

        JComboBox<String> comboBox = new JComboBox<>(categorias);

        categoriaPanel.add(categoriaLabel);
        categoriaPanel.add(comboBox);

        // OPERARIO
        JPanel operarioPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        operarioPanel.setBackground(new Color(240, 248, 255)); // Light pastel color

        JLabel operarioLabel = new JLabel("Operarios");
        categoriaLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        inicializarFirebase();
        Firestore db = FirestoreClient.getFirestore();
        QuerySnapshot querySnapshot = db.collection("oficinista").get().get();

        List<String> ope = new ArrayList<>();

        for (QueryDocumentSnapshot document : querySnapshot.getDocuments()) {
            ope.add(document.getId());
        }

        String[] operarios = new String[ope.size() + 1];
        operarios[0] = "Escoge un operario";

        for (int i = 0; i < ope.size(); i++) {
            operarios[i + 1] = ope.get(i);
        }

        JComboBox<String> comboOpeBox = new JComboBox<>(operarios);

        operarioPanel.add(operarioLabel);
        operarioPanel.add(comboOpeBox);

        // CONTACTO
        JPanel contactoPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        contactoPanel.setBackground(new Color(240, 248, 255)); // Light pastel color

        JLabel contactoLabel = new JLabel("Número de contacto");
        contactoLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JTextField contactoField = new JTextField();

        contactoPanel.add(contactoLabel);
        contactoPanel.add(contactoField);

        // DIRECCION
        JPanel dirPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        dirPanel.setBackground(new Color(240, 248, 255)); // Light pastel color

        JLabel dirLabel = new JLabel("Dirección de domicilio");
        dirLabel.setHorizontalAlignment(SwingConstants.LEFT);

        JTextField dirField = new JTextField();

        dirPanel.add(dirLabel);
        dirPanel.add(dirField);

        // DESCRIPCION
        JPanel descPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        descPanel.setBackground(new Color(240, 248, 255)); // Light pastel color

        JLabel descLabel = new JLabel("Descripción de Incidencia");
        descLabel.setVerticalAlignment(SwingConstants.BOTTOM);

        JTextArea descField = new JTextArea(10, 30);

        descPanel.add(descLabel);
        descPanel.add(descField);

        // ESTABLECER INFO
        DocumentReference docRef = db.collection(coleccion).document(documento);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        try {
            DocumentSnapshot document = future.get();
            if (document.exists()) {
                Map<String, Object> data = document.getData();
                if (data != null) {
                    String estado = (String) data.get("estado");
                    String titulo = (String) data.get("titulo");
                    String categoria = (String) data.get("categoria");
                    String operario = (String) data.get("operario");
                    String contacto = (String) data.get("contacto");
                    String direccion = (String) data.get("direccion");
                    String descripcion = (String) data.get("descripcion");

                    if ("NORMAL".equals(estado)) {
                        normal.setSelected(true);
                    } else if ("GRAVE".equals(estado)) {
                        grave.setSelected(true);
                    }

                    textField.setText(titulo);
                    comboBox.setSelectedItem(categoria);
                    comboOpeBox.setSelectedItem(operario);
                    contactoField.setText(contacto);
                    dirField.setText(direccion);
                    descField.setText(descripcion);
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró el documento.");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        JPanel enviarPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        enviarPanel.setBackground(new Color(240, 248, 255)); // Light pastel color

        JButton volver = new JButton("Volver");
        volver.setFont(new Font("Arial", Font.PLAIN, 12));
        volver.setBackground(new Color(173, 216, 230)); // Light blue color
        volver.setFocusPainted(false);
        volver.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        volver.setPreferredSize(new java.awt.Dimension(80, 25));
        volver.addActionListener(e -> {
            cardLayout.show(contentPane, "Primera Página");

            try {
                dispose();
                new Opciones().setVisible(true);
            } catch (IOException | GeoIp2Exception | InterruptedException | ExecutionException e1) {
                e1.printStackTrace();
            }
        });

        JButton enviar = new JButton("Enviar");
        enviar.setFont(new Font("Arial", Font.PLAIN, 12));
        enviar.setBackground(new Color(173, 216, 230)); // Light blue color
        enviar.setFocusPainted(false);
        enviar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        enviar.addActionListener(e -> {
            try {
                inicializarFirebase();
                Firestore db1 = FirestoreClient.getFirestore();

                // Obtener los datos de los campos
                String titulo = textField.getText();
                String contacto = contactoField.getText();
                String direccion = dirField.getText();
                String descripcion = descField.getText();
                String estado = normal.isSelected() ? "NORMAL" : grave.isSelected() ? "GRAVE" : "SIN_ESTADO";

                String categoria;
                if (comboBox.getSelectedItem().equals("Escoge una categoría")) {
                    JOptionPane.showMessageDialog(null, "Por favor, escoge una categoría válida.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    categoria = (String) comboBox.getSelectedItem();
                }

                String oper;
                if (comboOpeBox.getSelectedItem().equals("Escoge un operario")) {
                    JOptionPane.showMessageDialog(null, "Por favor, escoge un operario válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                } else {
                    oper = (String) comboOpeBox.getSelectedItem();
                }

                if (estado.equals("SIN_ESTADO")) {
                    JOptionPane.showMessageDialog(null, "Por favor, escoge un estado válido.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (contacto.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, introduzca el número de contacto.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (direccion.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, introduzca la dirección del domicilio.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (descripcion.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, introduzca la descripción del problema.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (titulo.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Por favor, introduzca el título.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Crear un mapa con los datos
                Map<String, Object> incidencia = new HashMap<>();
                incidencia.put("estado", estado);
                incidencia.put("titulo", titulo);
                incidencia.put("categoria", categoria);
                incidencia.put("operario", oper);
                incidencia.put("contacto", contacto);
                incidencia.put("direccion", direccion);
                incidencia.put("descripcion", descripcion);

                // Obtener la referencia a la colección de Firestore usando el estado
                DocumentReference ref = db1.collection(estado).document(documento);

                // Enviar los datos a Firestore
                ApiFuture<WriteResult> result = ref.set(incidencia);

                try {
                    // Confirmar el resultado
                    WriteResult writeResult = result.get();
                    System.out.println("Update time : " + writeResult.getUpdateTime());

                    // Mostrar mensaje de éxito
                    JOptionPane.showMessageDialog(null, "Datos enviados correctamente", "Éxito", JOptionPane.INFORMATION_MESSAGE);

                    // Limpiar todos los campos
                    textField.setText("");
                    comboBox.setSelectedIndex(0);
                    contactoField.setText("");
                    dirField.setText("");
                    descField.setText("");
                    group.clearSelection();

                    if (!estado.equals(coleccion)) {
                        db1.collection(coleccion).document(documento).delete();
                    }

                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        enviarPanel.add(volver);
        enviarPanel.add(enviar);

        modificar.add(estadoPanel);
        modificar.add(tituloPanel);
        modificar.add(categoriaPanel);
        modificar.add(operarioPanel);
        modificar.add(contactoPanel);
        modificar.add(dirPanel);
        modificar.add(descPanel);
        modificar.add(enviarPanel);

        return modificar;
    }

    private JPanel createFourthPanel() throws IOException {
        JPanel pdf = new JPanel(new BorderLayout());
        pdf.setBorder(new EmptyBorder(10, 10, 10, 10));
        pdf.setBackground(new Color(240, 248, 255)); // Light pastel color

        // LISTA
        CustomListModel listModel = new CustomListModel();
        JList<String> lista = new JList<>(listModel);
        lista.setBackground(new Color(255, 239, 213)); // Light pastel color
        lista.setSelectionBackground(new Color(255, 228, 225)); // Another pastel color

        List<String> states = new ArrayList<>();
        List<String> docs = new ArrayList<>();
        List<String> descripcion = new ArrayList<>();
        List<String> direccion = new ArrayList<>();
        List<String> categoria = new ArrayList<>();
        List<String> opes = new ArrayList<>();
        List<String> titulo = new ArrayList<>();
        List<String> contacto = new ArrayList<>();
        List<String> horaFin = new ArrayList<>();
        List<String> horaIni = new ArrayList<>();
        List<String> imagen = new ArrayList<>();
        List<String> solucion = new ArrayList<>();

        inicializarFirebase();
        CollectionReference collectionRef = FirestoreClient.getFirestore().collection("resuelto");
        ApiFuture<QuerySnapshot> future = collectionRef.get();

        try {
            QuerySnapshot querySnapshot = future.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();

            for (DocumentSnapshot document : documents) {
                // ESTABLECER LISTA
                String tituloDocumento = document.getString("titulo");
                String estadoDocumento = document.getString("estado");
                listModel.addElement("Título: " + tituloDocumento + ", Estado: " + estadoDocumento);

                states.add(estadoDocumento);
                docs.add(document.getId());
                descripcion.add(document.getString("descripcion"));
                direccion.add(document.getString("direccion"));
                categoria.add(document.getString("categoria"));
                opes.add(document.getString("operario"));
                titulo.add(tituloDocumento);
                contacto.add(document.getString("contacto"));
                horaFin.add(document.getString("horaFin"));
                horaIni.add(document.getString("horaIni"));
                imagen.add(document.getString("imagen"));
                solucion.add(document.getString("solucion"));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // SELECCIONAR
        lista.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int index = lista.locationToIndex(e.getPoint());

                // Ruta del archivo PDF de salida
                String dest = System.getProperty("user.home") + File.separator + "Downloads" + File.separator + categoria.get(index) + "-" + states.get(index) + "-" + docs.get(index) + ".pdf";

                try {
                    // Crear un escritor de PDF
                    PdfWriter writer = new PdfWriter(dest);

                    // Crear un documento PDF
                    PdfDocument pdfDoc = new PdfDocument(writer);

                    // Crear un objeto Document para agregar elementos
                    Document document = new Document(pdfDoc);

                    // TITULO
                    Paragraph title = new Paragraph(titulo.get(index))
                            .setFont(PdfFontFactory.createFont("Helvetica-Bold"))
                            .setFontSize(20)
                            .setTextAlignment(TextAlignment.CENTER)
                            .setBold()
                            .setMarginBottom(20);

                    // ESTADOS
                    Paragraph estTit = new Paragraph("Estados: ").setBold();
                    Paragraph est = new Paragraph(states.get(index));

                    // CATEGORIA
                    Paragraph catTit = new Paragraph("Categoría: ").setBold();
                    Paragraph cat = new Paragraph(categoria.get(index));

                    // OPERARIO
                    Paragraph opeTit = new Paragraph("Operario: ").setBold();
                    Paragraph ope = new Paragraph(opes.get(index));

                    // CONTACTO
                    Paragraph conTit = new Paragraph("Contacto: ").setBold();
                    Paragraph con = new Paragraph(contacto.get(index));

                    // DIRECCION
                    Paragraph dirTit = new Paragraph("Dirección: ").setBold();
                    Paragraph dir = new Paragraph(direccion.get(index));

                    // DESCRIPCION
                    Paragraph desTit = new Paragraph("Descripcion: ").setBold();
                    Paragraph des = new Paragraph(descripcion.get(index));

                    // HORA INICIO
                    Paragraph hIniTit = new Paragraph("Hora Inicio: ").setBold();
                    Paragraph hI = new Paragraph(horaIni.get(index));

                    // HORA FIN
                    Paragraph hFinTit = new Paragraph("Hora Fin: ").setBold();
                    Paragraph hF = new Paragraph(horaFin.get(index));

                    // SOLUCION
                    Paragraph solTit = new Paragraph("Solucion: ").setBold();
                    Paragraph sol = new Paragraph(solucion.get(index));

                    // IMAGEN
                    ImageData imageData = ImageDataFactory.create(imagen.get(index));
                    Image image = new Image(imageData);
                    image.setAutoScale(true);
                    image.setMarginBottom(20);

                    document.add(title);
                    document.add(new Paragraph().add(catTit).add(cat));
                    document.add(new Paragraph().add(opeTit).add(ope));
                    document.add(new Paragraph().add(estTit).add(est));
                    document.add(new Paragraph().add(conTit).add(con));
                    document.add(new Paragraph().add(dirTit).add(dir));
                    document.add(new Paragraph().add(desTit).add(des));
                    document.add(new Paragraph().add(hIniTit).add(hI));
                    document.add(new Paragraph().add(hFinTit).add(hF));
                    document.add(new Paragraph().add(solTit).add(sol));
                    document.add(image);

                    // Cerrar el documento
                    document.close();

                    // Preguntar si se desea enviar por correo electrónico
                    int response = JOptionPane.showConfirmDialog(null, "PDF correctamente descargado. ¿Desea enviarlo por correo electrónico?", "Enviar por correo", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.YES_OPTION) {
                        String email = JOptionPane.showInputDialog("Ingrese el email de destino:");
                        if (email != null && !email.isEmpty()) {
                            enviarCorreoConPDF(email, dest);
                            JOptionPane.showMessageDialog(null, "Correo enviado correctamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } catch (IOException e3) {
                    e3.printStackTrace();
                }
            }
        });

        // BOTONES
        JButton volver = new JButton("Volver");
        volver.setFont(new Font("Arial", Font.PLAIN, 12));
        volver.setBackground(new Color(173, 216, 230)); // Light blue color
        volver.setFocusPainted(false);
        volver.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        volver.setPreferredSize(new java.awt.Dimension(80, 25));
        volver.addActionListener(e -> {
            cardLayout.show(contentPane, "Primera Página");

            try {
                dispose();
                new Opciones().setVisible(true);
            } catch (IOException | GeoIp2Exception | InterruptedException | ExecutionException e1) {
                e1.printStackTrace();
            }
        });

        pdf.add(new JScrollPane(lista), BorderLayout.CENTER);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(new Color(240, 248, 255)); // Match the main panel background
        bottomPanel.add(volver);
        pdf.add(bottomPanel, BorderLayout.SOUTH);

        return pdf;
    }

    public JPanel createFifthPanel() throws IOException, InterruptedException, ExecutionException {
        JPanel mapa = new JPanel(new BorderLayout());
        mapa.setBorder(new EmptyBorder(10, 10, 10, 10));
        mapa.setBackground(new Color(240, 248, 255)); // Light pastel color

        inicializarFirebase();
        Firestore db = FirestoreClient.getFirestore();

        ApiFuture<DocumentSnapshot> future = db.collection("ubicaciones").document("dispositivo1").get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            latitude = document.getDouble("latitude");
            longitude = document.getDouble("longitude");
        }

        // Crear el mapa en el hilo de despacho de eventos de Swing
        SwingUtilities.invokeLater(() -> {

            JXMapViewer mapViewer = new JXMapViewer();

            // Configurar el proveedor de mosaicos (tiles)
            TileFactoryInfo info = new TileFactoryInfo(1, 17, 17, 256, true, true, "https://tile.openstreetmap.org",
                    "x", "y", "z") {
                public String getTileUrl(int x, int y, int zoom) {
                    int z = 17 - zoom;
                    return this.baseURL + "/" + z + "/" + x + "/" + y + ".png";
                }
            };
            DefaultTileFactory tileFactory = new DefaultTileFactory(info);
            mapViewer.setTileFactory(tileFactory);

            // Ubicación obtenida de la IP
            GeoPosition geo = new GeoPosition(latitude, longitude);

            // Añadir marcador
            Set<Waypoint> waypoints = new HashSet<>();
            waypoints.add(new DefaultWaypoint(geo));
            WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();
            waypointPainter.setWaypoints(waypoints);
            mapViewer.setOverlayPainter(waypointPainter);

            // Centrar el mapa en la ubicación
            mapViewer.setZoom(1);
            mapViewer.setAddressLocation(geo);

            // Añadir el mapa al panel
            mapa.add(mapViewer, BorderLayout.CENTER);

            // BOTONES
            JButton volver = new JButton("Volver");
            volver.setFont(new Font("Arial", Font.PLAIN, 12));
            volver.setBackground(new Color(173, 216, 230)); // Light blue color
            volver.setFocusPainted(false);
            volver.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            volver.setPreferredSize(new java.awt.Dimension(80, 25));
            volver.addActionListener(e -> {
                cardLayout.show(contentPane, "Primera Página");

                try {
                    dispose();
                    new Opciones().setVisible(true);
                } catch (IOException | GeoIp2Exception | InterruptedException | ExecutionException e1) {
                    e1.printStackTrace();
                }
            });

            // Añadir el botón de volver al panel
            JPanel bottomPanel = new JPanel();
            bottomPanel.setBackground(new Color(240, 248, 255)); // Match the main panel background
            bottomPanel.add(volver);
            mapa.add(bottomPanel, BorderLayout.SOUTH);

        });

        return mapa;
    }

    private void inicializarFirebase() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            FileInputStream refreshToken = new FileInputStream("F:/TFG2/SmartOfic/src/main/resources/smartoper-d431a-firebase-adminsdk-5obz7-b83ccb291e.json");

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(refreshToken))
                    .setProjectId("smartoper-d431a")
                    .build();

            FirebaseApp.initializeApp(options);
        }
    }

    private void enviarCorreoConPDF(String emailDestino, String rutaArchivoPDF) {
        String remitente = "smartoper@proyectosasr.com";
        String clave = "sasr2024sasr";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.proyectosasr.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");

        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.socketFactory", sf);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(remitente, clave);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remitente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestino));
            message.setSubject("Adjunto encontrarás informe sobre la resolucion de la incidencia.");
            message.setText("Adjunto encontrarás informe sobre la resolucion de la incidencia.");

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            Multipart multipart = new MimeMultipart();

            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(rutaArchivoPDF);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(new File(rutaArchivoPDF).getName());
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            Transport.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

class CustomListModel extends AbstractListModel<String> {
    private static final long serialVersionUID = 1L;
    private final List<String> data;

    public CustomListModel() {
        data = new ArrayList<>();
    }

    public void addElement(String element) {
        data.add(element);
        int index = data.size() - 1;
        fireIntervalAdded(this, index, index);
    }

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public String getElementAt(int index) {
        return data.get(index);
    }
}