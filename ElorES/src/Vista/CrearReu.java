package Vista;

import java.awt.Image;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

import com.toedter.calendar.JDateChooser;
import Controlador.Controlador;

public class CrearReu extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JLabel lblLogo;
    private JLabel lblFondo;
    private JLabel lblTema;
    private JLabel lblFechaYHora;
    private JLabel lblAula;
    private JLabel lblUbicacion;
    private JLabel lblMiembros;
    private JComboBox<String> comboUbicacion;
    private JComboBox<String> comboMiembros;
    private JComboBox<String> comboAula;

    private JTextField txtNombre;
    private JSpinner spinnerHora;
    private JTextArea taTema;
    private JDateChooser dateChooser;
    private JLabel lblError;


    public CrearReu(Controlador controlador, int idProfe) {

        controlador.setCrearReu(this); 

        setTitle("Crear Nueva Reunion");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 800, 534);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // LOGO
        lblLogo = new JLabel();
        lblLogo.setBounds(321, 30, 120, 120);
        ImageIcon icono = new ImageIcon("fotos/logo.png");
        Image imagen = icono.getImage().getScaledInstance(
                lblLogo.getWidth(),
                lblLogo.getHeight(),
                Image.SCALE_SMOOTH
        );
        lblLogo.setIcon(new ImageIcon(imagen));
        contentPane.add(lblLogo);

        // BOTÓN VOLVER
        JButton btnVolver = new JButton("");
        btnVolver.setIcon(new ImageIcon("fotos/volver.png"));
        btnVolver.setBounds(10, 11, 45, 45);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.addActionListener(e -> {
            GestionReuniones ventana = new GestionReuniones(controlador, idProfe);
            ventana.setVisible(true);
            dispose();
        });
        contentPane.add(btnVolver);

        // CAMPOS Y LABELS
        JLabel lblNombre = new JLabel("Nombre Reunión:");
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setBounds(27, 209, 128, 14);
        contentPane.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(157, 202, 208, 29);
        contentPane.add(txtNombre);

        lblFechaYHora = new JLabel("Fecha:");
        lblFechaYHora.setForeground(Color.WHITE);
        lblFechaYHora.setBounds(27, 272, 128, 14);
        contentPane.add(lblFechaYHora);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(157, 262, 208, 24);
        contentPane.add(dateChooser);

        lblAula = new JLabel("Aula:");
        lblAula.setForeground(Color.WHITE);
        lblAula.setBounds(448, 202, 128, 14);
        contentPane.add(lblAula);

        comboAula = new JComboBox<>();
        comboAula.setModel(new DefaultComboBoxModel<>(new String[]{
                "", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
        }));
        comboAula.setBounds(586, 194, 171, 30);
        contentPane.add(comboAula);

        lblTema = new JLabel("Tema:");
        lblTema.setForeground(Color.WHITE);
        lblTema.setBounds(27, 381, 128, 14);
        contentPane.add(lblTema);

        taTema = new JTextArea();
        taTema.setBounds(157, 383, 207, 93);
        contentPane.add(taTema);

        lblMiembros = new JLabel("Miembros:");
        lblMiembros.setForeground(Color.WHITE);
        lblMiembros.setBounds(448, 259, 128, 14);
        contentPane.add(lblMiembros);

        comboMiembros = new JComboBox<>();
        comboMiembros.setBounds(586, 251, 171, 30);
        contentPane.add(comboMiembros);

        lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setForeground(Color.WHITE);
        lblUbicacion.setBounds(448, 317, 128, 14);
        contentPane.add(lblUbicacion);

        comboUbicacion = new JComboBox<>();
        comboUbicacion.setBounds(586, 309, 171, 30);
        contentPane.add(comboUbicacion);
        
        Boton btnAñadir = new Boton("Añadir");
        btnAñadir.setBounds(487, 421, 89, 23);
        contentPane.add(btnAñadir);
        
        
        JLabel lblHora = new JLabel("Hora:");
        lblHora.setForeground(Color.WHITE);
        lblHora.setBounds(27, 317, 128, 14);
        contentPane.add(lblHora);
        
        spinnerHora = new JSpinner(new SpinnerDateModel());
        spinnerHora.setBounds(157, 321, 208, 20);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(spinnerHora, "HH:mm:ss");
        spinnerHora.setEditor(editor);
        contentPane.add(spinnerHora);        
        
        lblError = new JLabel("");
        lblError.setForeground(new Color(255, 0, 0));
        lblError.setBounds(451, 455, 306, 14);
        contentPane.add(lblError);
        
        JButton btnSalir = new JButton("");
        btnSalir.setIcon(new ImageIcon("fotos/salir.png"));
        btnSalir.setBounds(729, 11, 45, 45);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setBorderPainted(false);
        btnSalir.addActionListener(e -> {
            Login login = new Login(new Controlador());
            login.setVisible(true);
			controlador.cerrarConexion();
            dispose();
        });
        contentPane.add(btnSalir);

        // FONDO
        lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 800, 534);
        lblFondo.setIcon(new ImageIcon("fotos/backgroundGRANDE.png"));
        contentPane.add(lblFondo);
        
        
        btnAñadir.addActionListener(e -> {
        	controlador.crearReunion();
        });
        
        
        controlador.cargarCentros();
        controlador.cargarAlumnos();
        
    }


	public JComboBox<String> getComboUbicacion() {return comboUbicacion;}
	
	public JComboBox<String> getComboMiembros() {return comboMiembros;}

	public JSpinner getSpinnerHora() {return spinnerHora;}

	public JTextField getTxtNombre() {return txtNombre;}

	public JTextArea getTaTema() {return taTema;}

	public JDateChooser getDateChooser() {return dateChooser;}

	public JComboBox<String> getComboAula() {return comboAula;}

	public JLabel getLblError() {return lblError;}

	
	
}
