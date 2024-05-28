package com.example.SmartOfic;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractListModel;
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
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;

import java.io.FileNotFoundException;

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

import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;

import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
public class Opciones extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private CardLayout cardLayout;
    private double latitude = 0, longitude = 0;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Opciones frame = new Opciones();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Opciones() throws IOException, GeoIp2Exception, InterruptedException, ExecutionException {
        super("Opciones");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setIconImage(Toolkit.getDefaultToolkit().getImage("F:\\TFG2\\SmartOfic\\src\\main\\resources\\drawable\\logo.png"));
        setSize(600, 500);  
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
        JPanel main =  new JPanel(new GridLayout(5, 1, 10, 10)); 
        main.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel titulo = new JLabel("Escoge la opción del parte");
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        main.add(titulo, BorderLayout.NORTH);

        JButton opcionUno = new JButton("Crear");
        opcionUno.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPane, "Segunda Página");
            }
        });
        main.add(opcionUno);

        JButton opcionDos = new JButton("Modificar");
        opcionDos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPane, "Tercera Página");
            }
        });
        main.add(opcionDos);

        JButton opcionTres = new JButton("Exportar en PDF");
        opcionTres.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPane, "Cuarta Página");
            }
        });
        main.add(opcionTres);
        
        JButton opcionCuatro = new JButton("Mapa de Operarios");
        opcionCuatro.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPane, "Quinta Página");
            }
        });
        main.add(opcionCuatro);

        return main;
    }

    private JPanel createSecondPanel() {
        JPanel agregar =  new JPanel(new GridLayout(8, 1, 10, 10));
        agregar.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // FORMULARIO
        JLabel label = new JLabel("Rellenar Formulario");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        agregar.add(label);
        
        // ESTADO
        JPanel estadoPanel = new JPanel(new GridLayout(1, 3, 10, 10)); 
        
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

        JLabel tituloLabel = new JLabel("Título de Incidencia");
        tituloLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        
        JTextField textField = new JTextField();
        
        tituloPanel.add(tituloLabel);
        tituloPanel.add(textField);
        
        // CATEGORIA
        JPanel categoriaPanel = new JPanel(new GridLayout(2, 1, 10, 10)); 
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
        
        // CONTACTO
        JPanel contactoPanel = new JPanel(new GridLayout(1, 2, 10, 10)); 
        
        JLabel contactoLabel = new JLabel("Número de contacto");
        contactoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        JTextField contactoField = new JTextField();
        
        contactoPanel.add(contactoLabel);
        contactoPanel.add(contactoField);
        
        // DIRECCION
        JPanel dirPanel = new JPanel(new GridLayout(1, 2, 10, 10)); 
        
        JLabel dirLabel = new JLabel("Dirección de domicilio");
        dirLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        JTextField dirField = new JTextField();
        
        dirPanel.add(dirLabel);
        dirPanel.add(dirField);
        
        // DESCRIPCION
        JPanel descPanel = new JPanel(new GridLayout(2, 1, 10, 10)); 

        JLabel descLabel = new JLabel("Descripción de Incidencia");
        descLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        
        JTextArea descField = new JTextArea(10, 30);
        
        descPanel.add(descLabel);
        descPanel.add(descField);

        // BOTONES
        JPanel enviarPanel = new JPanel(new GridLayout(1, 2, 10, 10)); 
        
        JButton volver = new JButton("Volver");
        volver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPane, "Primera Página");

                try {

                    dispose();
					new Opciones().setVisible(true);
				} catch (IOException | GeoIp2Exception | InterruptedException | ExecutionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        
        JButton enviar = new JButton("Enviar");
        enviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					inicializarFirebase();
	                Firestore db = FirestoreClient.getFirestore();
	                
	            	// Obtener los datos de los campos
	                String titulo = textField.getText();
	                String categoria = null;
	                if(comboBox.getSelectedItem().equals("Escoge una categoría")) {
	                	JOptionPane.showMessageDialog(null, "Por favor, escoge una categoría válida.", "Error", JOptionPane.ERROR_MESSAGE);
	                    return;
	                } else {
		                categoria = (String) comboBox.getSelectedItem();
	                }
	                String contacto = contactoField.getText();
	                String direccion = dirField.getText();
	                String descripcion = descField.getText();
	                String estado = normal.isSelected() ? "NORMAL" : grave.isSelected() ? "GRAVE" : "SIN_ESTADO";

	                // Crear un mapa con los datos
	                Map<String, Object> incidencia = new HashMap<>();
	                incidencia.put("estado", estado);
	                incidencia.put("titulo", titulo);
	                incidencia.put("categoria", categoria);
	                incidencia.put("contacto", contacto);
	                incidencia.put("direccion", direccion);
	                incidencia.put("descripcion", descripcion);

	                // Obtener la referencia a la colección de Firestore usando el estado
	                DocumentReference ref = db.collection(estado).document();

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
            }
        });
      
        enviarPanel.add(volver);
        enviarPanel.add(enviar);

        agregar.add(estadoPanel);
        agregar.add(tituloPanel);
        agregar.add(categoriaPanel);        
        agregar.add(contactoPanel);
        agregar.add(dirPanel);
        agregar.add(descPanel);
        agregar.add(enviarPanel);
        
        return agregar;
    }
    
    private JPanel createThirdPanel() throws IOException {
        JPanel modificar =  new JPanel(new GridLayout(2, 1, 10, 10));
        modificar.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // LISTA
        CustomListModel listModel = new CustomListModel();
        JList<String> lista = new JList<>(listModel);

        String[] estados = {"GRAVE", "NORMAL"};
        
        List<String> states = new ArrayList<String>();
    	List<String> docs = new ArrayList<String>();
        
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
        lista.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
		        int index = lista.locationToIndex(e.getPoint());
                JPanel modificarPanel = modificarPanel(states.get(index), docs.get(index));
                contentPane.add(modificarPanel, "Modificar Página");
				cardLayout.show(contentPane, "Modificar Página");
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
        
        
        // BOTONES
        JButton volver = new JButton("Volver");
        volver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPane, "Primera Página");
            }
        });

        modificar.add(new JScrollPane(lista), BorderLayout.CENTER);
        modificar.add(volver);
        
        return modificar;
    }
    
    private JPanel modificarPanel(String coleccion, String documento) {
        JPanel modificar =  new JPanel(new GridLayout(8, 1, 10, 10));
        modificar.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // FORMULARIO
        JLabel label = new JLabel("Rellenar Formulario");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        modificar.add(label);
        
        // ESTADO
        JPanel estadoPanel = new JPanel(new GridLayout(1, 3, 10, 10)); 
        
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

        JLabel tituloLabel = new JLabel("Título de Incidencia");
        tituloLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        
        JTextField textField = new JTextField();
        
        tituloPanel.add(tituloLabel);
        tituloPanel.add(textField);
        
        // CATEGORIA
        JPanel categoriaPanel = new JPanel(new GridLayout(2, 1, 10, 10)); 
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
        
        // CONTACTO
        JPanel contactoPanel = new JPanel(new GridLayout(1, 2, 10, 10)); 
        
        JLabel contactoLabel = new JLabel("Número de contacto");
        contactoLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        JTextField contactoField = new JTextField();
        
        contactoPanel.add(contactoLabel);
        contactoPanel.add(contactoField);
        
        // DIRECCION
        JPanel dirPanel = new JPanel(new GridLayout(1, 2, 10, 10)); 
        
        JLabel dirLabel = new JLabel("Dirección de domicilio");
        dirLabel.setHorizontalAlignment(SwingConstants.LEFT);
        
        JTextField dirField = new JTextField();
        
        dirPanel.add(dirLabel);
        dirPanel.add(dirField);
        
        // DESCRIPCION
        JPanel descPanel = new JPanel(new GridLayout(2, 1, 10, 10)); 

        JLabel descLabel = new JLabel("Descripción de Incidencia");
        descLabel.setVerticalAlignment(SwingConstants.BOTTOM);
        
        JTextArea descField = new JTextArea(10, 30);
        
        descPanel.add(descLabel);
        descPanel.add(descField);
        
        // ESTABLECER INFO
        try {
			inicializarFirebase();
            Firestore db = FirestoreClient.getFirestore();
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
            
        } catch (IOException e1) {
			e1.printStackTrace();
		}
        
        JPanel enviarPanel = new JPanel(new GridLayout(1, 2, 10, 10)); 
        
        JButton volver = new JButton("Volver");
        volver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPane, "Primera Página");

                try {

                    dispose();
					new Opciones().setVisible(true);
				} catch (IOException | GeoIp2Exception | InterruptedException | ExecutionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });
        
        JButton enviar = new JButton("Enviar");
        enviar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
					inicializarFirebase();
	                Firestore db = FirestoreClient.getFirestore();
	                
	            	// Obtener los datos de los campos
	                String titulo = textField.getText();
	                String categoria = null;
	                if(comboBox.getSelectedItem().equals("Escoge una categoría")) {
	                	JOptionPane.showMessageDialog(null, "Por favor, escoge una categoría válida.", "Error", JOptionPane.ERROR_MESSAGE);
	                    return;
	                } else {
		                categoria = (String) comboBox.getSelectedItem();
	                }
	                String contacto = contactoField.getText();
	                String direccion = dirField.getText();
	                String descripcion = descField.getText();
	                String estado = normal.isSelected() ? "NORMAL" : grave.isSelected() ? "GRAVE" : "SIN_ESTADO";

	                // Crear un mapa con los datos
	                Map<String, Object> incidencia = new HashMap<>();
	                incidencia.put("estado", estado);
	                incidencia.put("titulo", titulo);
	                incidencia.put("categoria", categoria);
	                incidencia.put("contacto", contacto);
	                incidencia.put("direccion", direccion);
	                incidencia.put("descripcion", descripcion);

	                // Obtener la referencia a la colección de Firestore usando el estado
	                DocumentReference ref = db.collection(estado).document(documento);

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
	                    	db.collection(coleccion).document(documento).delete();
	                    }

	                } catch (Exception e2) {
	                    e2.printStackTrace();
	                }
				} catch (IOException e1) {
					e1.printStackTrace();
				}
            }
        });
      
        enviarPanel.add(volver);
        enviarPanel.add(enviar);
        
        modificar.add(estadoPanel);
        modificar.add(tituloPanel);
        modificar.add(categoriaPanel);        
        modificar.add(contactoPanel);
        modificar.add(dirPanel);
        modificar.add(descPanel);
        modificar.add(enviarPanel);
        
        return modificar;
    }
    
    private JPanel createFourthPanel() throws IOException {
    	JPanel pdf =  new JPanel(new GridLayout(2, 1, 10, 10));
        pdf.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        // LISTA
        CustomListModel listModel = new CustomListModel();
        JList<String> lista = new JList<>(listModel);

        String[] estados = {"GRAVE", "NORMAL"};
        
        List<String> states = new ArrayList<String>();
    	List<String> docs = new ArrayList<String>();
    	List<String> descripcion = new ArrayList<String>();
    	List<String> direccion = new ArrayList<String>();
    	List<String> categoria = new ArrayList<String>();
    	List<String> titulo = new ArrayList<String>();
    	List<String> contacto = new ArrayList<String>();
        
        for (String estado : estados) {
        	inicializarFirebase();
            CollectionReference collectionRef = FirestoreClient.getFirestore().collection(estado);
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
                	titulo.add(tituloDocumento);
                	contacto.add(document.getString("contacto"));
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        
        // SELECCIONAR
        lista.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
		        int index = lista.locationToIndex(e.getPoint());

		        // Ruta del archivo PDF de salida
		        String dest = System.getProperty("user.home") + File.separator + "Downloads" + File.separator +categoria.get(index) +"-" + states.get(index) +"-" +docs.get(index) + ".pdf";

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
		            
		            // CONTACTO
		            Paragraph conTit = new Paragraph("Contacto: ").setBold();
		            Paragraph con = new Paragraph(contacto.get(index));
		            
		            // DIRECCION
		            Paragraph dirTit = new Paragraph("Dirección: ").setBold();
		            Paragraph dir = new Paragraph(direccion.get(index));
		            
		            // DESCRIPCION
		            Paragraph desTit = new Paragraph("Descripcion: ").setBold();
		            Paragraph des = new Paragraph(descripcion.get(index));
		            
		            document.add(title);		            
		            document.add(new Paragraph().add(catTit).add(cat));
		            document.add(new Paragraph().add(estTit).add(est));
		            document.add(new Paragraph().add(conTit).add(con));
		            document.add(new Paragraph().add(dirTit).add(dir));
		            document.add(desTit);
		            document.add(des);

		            // Cerrar el documento
		            document.close();


                    JOptionPane.showMessageDialog(null, "PDF correctamente descargado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
		        } catch (IOException e3) {
		            e3.printStackTrace();
		        }
		        
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
        });
        
        
        // BOTONES
        JButton volver = new JButton("Volver");
        volver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(contentPane, "Primera Página");

                try {

                    dispose();
					new Opciones().setVisible(true);
				} catch (IOException | GeoIp2Exception | InterruptedException | ExecutionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            }
        });

        pdf.add(new JScrollPane(lista), BorderLayout.CENTER);
        pdf.add(volver);
        
        return pdf;
    }
    
    public JPanel createFifthPanel() throws IOException, GeoIp2Exception, InterruptedException, ExecutionException {
        JPanel mapa = new JPanel(new GridLayout(2, 1, 10, 10));
        mapa.setBorder(new EmptyBorder(5, 5, 5, 5));

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
            mapa.add(mapViewer);
            mapa.revalidate();
            mapa.repaint();
            
            // BOTONES
            JButton volver = new JButton("Volver");
            volver.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(contentPane, "Primera Página");

                    try {

                        dispose();
    					new Opciones().setVisible(true);
    				} catch (IOException | GeoIp2Exception | InterruptedException | ExecutionException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    				}
                }
            });

            mapa.add(volver);
        });
        
        return mapa;
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
}

class CustomListModel extends AbstractListModel<String> {
    private List<String> data;

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