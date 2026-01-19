package Principal;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextArea;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;

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
    private JLabel lblEstado;
    private JTextField txtNombre;

	/**
	 * Create the frame.
	 * @param id 
	 * @param dos 
	 * @param dis 
	 * @param cliente 
	 */
	public CrearReu(Socket cliente, DataInputStream dis, DataOutputStream dos, int id) {
		setTitle("Crear Reuniones");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        
        // Botón volver
        JButton btnVolver = new JButton("");
		btnVolver.setIcon(new ImageIcon("fotos/volver.png"));
		btnVolver.setBounds(10, 11, 45, 45);
		btnVolver.setContentAreaFilled(false);
		btnVolver.setBorderPainted(false);
		btnVolver.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		GestionReuniones gestionReuniones = new GestionReuniones(cliente, dis, dos, id);
        		gestionReuniones.setVisible(true);
        		dispose();
        	}
        });
		contentPane.add(btnVolver);
		
		// Botón salir
		JButton btnLogin = new JButton("");
		btnLogin.setIcon(new ImageIcon("fotos/salir.png"));
		btnLogin.setBounds(729, 11, 45, 45);
		btnLogin.setContentAreaFilled(false);
        btnLogin.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Login login = new Login();
        		login.setVisible(true);   // MOSTRAR login
                dispose();
        	}
        });
		btnLogin.setBorderPainted(false);
		contentPane.add(btnLogin);
        
        JLabel lblNombre = new JLabel("Nombre Reunion:");
        lblNombre.setForeground(new Color(255, 255, 255));
        lblNombre.setBounds(27, 202, 128, 14);
        contentPane.add(lblNombre);
        
        lblTema = new JLabel("Tema:");
        lblTema.setForeground(new Color(255, 255, 255));
        lblTema.setBounds(27, 381, 128, 14);
        contentPane.add(lblTema);
        
        lblFechaYHora = new JLabel("Fecha Y Hora:");
        lblFechaYHora.setForeground(new Color(255, 255, 255));
        lblFechaYHora.setBounds(27, 259, 128, 14);
        contentPane.add(lblFechaYHora);
        
        lblAula = new JLabel("Aula:");
        lblAula.setForeground(new Color(255, 255, 255));
        lblAula.setBounds(27, 317, 128, 14);
        contentPane.add(lblAula);
        
        lblUbicacion = new JLabel("Ubicacion:");
        lblUbicacion.setForeground(new Color(255, 255, 255));
        lblUbicacion.setBounds(439, 317, 128, 14);
        contentPane.add(lblUbicacion);
        
        lblMiembros = new JLabel("Miembros:");
        lblMiembros.setForeground(new Color(255, 255, 255));
        lblMiembros.setBounds(439, 202, 128, 14);
        contentPane.add(lblMiembros);
        
        lblEstado = new JLabel("Estado:");
        lblEstado.setForeground(new Color(255, 255, 255));
        lblEstado.setBounds(439, 259, 128, 14);
        contentPane.add(lblEstado);
        
        txtNombre = new JTextField();
        txtNombre.setBounds(129, 190, 208, 29);
        contentPane.add(txtNombre);
        txtNombre.setColumns(10);
        
        JComboBox comboBoxMiembros = new JComboBox();
        comboBoxMiembros.setModel(new DefaultComboBoxModel(new String[] {"", "Profesores", "Profesores Y Alumnos"}));
        comboBoxMiembros.setBounds(589, 189, 171, 30);
        contentPane.add(comboBoxMiembros);
        
        JComboBox comboBoxEstado = new JComboBox();
        comboBoxEstado.setModel(new DefaultComboBoxModel(new String[] {"", "Pendiente", "Conflicto", "Aceptada", "Cancelada"}));
        comboBoxEstado.setBounds(589, 255, 171, 30);
        contentPane.add(comboBoxEstado);
        
        JComboBox comboBoxUbi = new JComboBox();
        comboBoxUbi.setModel(new DefaultComboBoxModel(new String[] {"Elorrieta-Errekamari LHII"}));
        comboBoxUbi.setBounds(589, 313, 171, 30);
        contentPane.add(comboBoxUbi);
        
        JTextArea textArea_Tema = new JTextArea();
        textArea_Tema.setBounds(129, 376, 207, 93);
        contentPane.add(textArea_Tema);
        
        JComboBox comboBoxMiembros_1 = new JComboBox();
        comboBoxMiembros_1.setModel(new DefaultComboBoxModel(new String[] {"", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"}));
        comboBoxMiembros_1.setBounds(129, 302, 208, 30);
        contentPane.add(comboBoxMiembros_1);
        
        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setBounds(129, 255, 208, 24);
        contentPane.add(dateChooser);
        
        // FONDO
        lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 800, 534);
        lblFondo.setIcon(new ImageIcon("fotos/backgroundGRANDE.png"));
        contentPane.add(lblFondo);

	}
}
