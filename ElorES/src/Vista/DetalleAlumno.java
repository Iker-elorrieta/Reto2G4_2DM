package Vista;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import Controlador.Controlador;

import java.awt.Color;

public class DetalleAlumno extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;


    private JLabel lblLogo;
    private JLabel lblFondo;

    private JLabel lblEmail;
    private JLabel lblUsername;
    private JLabel lblNombre;
    private JLabel lblApellidos;
    private JLabel lblDNI;
    private JLabel lblDireccion;
    private JLabel lblTelefono;
    
    Controlador controlador = new Controlador(this);

    public DetalleAlumno(Socket cliente, DataInputStream dis, DataOutputStream dos, int idProfe, int idAlumno) {
  
        setTitle("DetalleAlumno");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 534);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // LOGO
        lblLogo = new JLabel();
        lblLogo.setBounds(327, 11, 120, 120);
        ImageIcon icono = new ImageIcon("fotos/logo.png");
        Image imagen = icono.getImage().getScaledInstance(
                lblLogo.getWidth(),
                lblLogo.getHeight(),
                Image.SCALE_SMOOTH
        );
        lblLogo.setIcon(new ImageIcon(imagen));
        contentPane.add(lblLogo);
        
        // Boton Volver 
        JButton btnVolver = new JButton("");
		btnVolver.setIcon(new ImageIcon("fotos/volver.png"));
		btnVolver.setBounds(10, 11, 45, 45);
		btnVolver.setContentAreaFilled(false);
		btnVolver.setBorderPainted(false);
		contentPane.add(btnVolver);
		
		// Botón salir
		JButton btnLogin = new JButton("");
		btnLogin.setIcon(new ImageIcon("fotos/salir.png"));
		btnLogin.setBounds(729, 11, 45, 45);
		btnLogin.setContentAreaFilled(false);
        btnLogin.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Login login = new Login(cliente, dis, dos);
        		login.setVisible(true);   // MOSTRAR login
                dispose();
        	}
        });
		btnLogin.setBorderPainted(false);
		contentPane.add(btnLogin);

        // CAMPOS
        lblEmail = new JLabel("Email:");
        lblEmail.setForeground(new Color(255, 255, 255));
        lblEmail.setBounds(260, 159, 273, 14);
        contentPane.add(lblEmail);

        lblUsername = new JLabel("Username:");
        lblUsername.setForeground(new Color(255, 255, 255));
        lblUsername.setBounds(260, 194, 273, 14);
        contentPane.add(lblUsername);

        lblNombre = new JLabel("Nombre:");
        lblNombre.setForeground(new Color(255, 255, 255));
        lblNombre.setBounds(260, 230, 273, 14);
        contentPane.add(lblNombre);

        lblApellidos = new JLabel("Apellidos:");
        lblApellidos.setForeground(new Color(255, 255, 255));
        lblApellidos.setBounds(260, 269, 273, 14);
        contentPane.add(lblApellidos);

        lblDNI = new JLabel("DNI:");
        lblDNI.setForeground(new Color(255, 255, 255));
        lblDNI.setBounds(260, 316, 273, 14);
        contentPane.add(lblDNI);

        lblDireccion = new JLabel("Dirección:");
        lblDireccion.setForeground(new Color(255, 255, 255));
        lblDireccion.setBounds(260, 362, 273, 14);
        contentPane.add(lblDireccion);

        lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setForeground(new Color(255, 255, 255));
        lblTelefono.setBounds(260, 407, 273, 14);
        contentPane.add(lblTelefono);

        // FONDO
        lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 800, 534);
        lblFondo.setIcon(new ImageIcon("fotos/backgroundGRANDE.png"));
        contentPane.add(lblFondo);

         System.out.println("ID ALUMNO EN DETALLE: " + idAlumno);
        controlador.cargarDatosDetalleAlumno(dis, dos, idAlumno);
        
        btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConsultarAlumnos menu = new ConsultarAlumnos(cliente, dis, dos, idProfe);
				menu.setVisible(true);
				try {
					dos.writeUTF("33");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				dispose();
			}
		});
    }

	public JLabel getLblEmail() {
		return lblEmail;
	}

	public JLabel getLblUsername() {
		return lblUsername;
	}


	public JLabel getLblNombre() {
		return lblNombre;
	}

	public JLabel getLblApellidos() {
		return lblApellidos;
	}



	public JLabel getLblDNI() {
		return lblDNI;
	}


	public JLabel getLblDireccion() {
		return lblDireccion;
	}


	public JLabel getLblTelefono() {
		return lblTelefono;
	}




}
