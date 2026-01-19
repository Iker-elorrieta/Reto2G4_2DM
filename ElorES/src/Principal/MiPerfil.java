package Principal;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class MiPerfil extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DataInputStream dis;
	private DataOutputStream dos;
	private int id;

	// Labels a nivel de clase
	private JLabel lblLogo;
	private JLabel lblEmail;
	private JLabel lblUsername;
	private JLabel lblNombre;
	private JLabel lblApellidos;
	private JLabel lblDNI;
	private JLabel lblDireccion;
	private JLabel lblTelefono;
	private JLabel lblFoto;
	private JLabel lblFondo;

	public MiPerfil(Socket cliente, DataInputStream dis, DataOutputStream dos, int id) {
		this.dis = dis;
		this.dos = dos;
		this.id= id;

		setTitle("Mi Perfil");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 534);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// EL LOGO
		lblLogo = new JLabel();
		lblLogo.setBounds(51, 0, 85, 89);
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
				MenuProfe menu = new MenuProfe(cliente, dis, dos, id);
				menu.setVisible(true);
				try {
					dos.writeUTF("33");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				dispose();
			}
		});
		contentPane.add(btnVolver);
		
		// Botón salir
		JButton btnLogin = new JButton("");
		btnLogin.setIcon(new ImageIcon("fotos/salir.png"));
		btnLogin.setBounds(729, 11, 45, 45);
		btnLogin.setContentAreaFilled(false);
		btnLogin.setBorderPainted(false);
		
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Login login = new Login();
				try {
					dos.writeUTF("0");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				login.setVisible(true);
				dispose();
			}
		});
		contentPane.add(btnLogin);

		// Panel de usuario
		JPanel panel = new JPanel();
		panel.setBounds(227, 36, 311, 428);
		contentPane.add(panel);
		panel.setLayout(null);

		lblFoto = new JLabel("");
		lblFoto.setBounds(118, 42, 60, 60);
		lblFoto.setIcon(new ImageIcon("fotos/profesor1.png"));
		panel.add(lblFoto);

		lblEmail = new JLabel("Email:");
		lblEmail.setBounds(28, 42, 273, 14);
		panel.add(lblEmail);

		lblUsername = new JLabel("Username:");
		lblUsername.setBounds(28, 77, 273, 14);
		panel.add(lblUsername);

		lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(28, 113, 273, 14);
		panel.add(lblNombre);

		lblApellidos = new JLabel("Apellidos:");
		lblApellidos.setBounds(28, 152, 273, 14);
		panel.add(lblApellidos);

		lblDNI = new JLabel("DNI:");
		lblDNI.setBounds(28, 199, 273, 14);
		panel.add(lblDNI);

		lblDireccion = new JLabel("Dirección: ");
		lblDireccion.setBounds(28, 245, 273, 14);
		panel.add(lblDireccion);

		lblTelefono = new JLabel("Teléfono:");
		lblTelefono.setBounds(28, 290, 273, 14);
		panel.add(lblTelefono);
		
		cargarDatos();
		
		
		// Fondo
		lblFondo = new JLabel("");
		lblFondo.setBounds(0, 0, 800, 534);
		lblFondo.setIcon(new ImageIcon("fotos/backgroundGRANDE.png"));
		contentPane.add(lblFondo);
	}

	public void cargarDatos() {
	    try {
	        dos.writeUTF("1");
	        dos.flush();

	        String json = dis.readUTF();

	        Gson gson = new Gson();
	        ArrayList<Map<String, Object>> listaUsuarios = gson.fromJson(
	        	    json,
	        	    new TypeToken<ArrayList<Map<String, Object>>>(){}.getType()
	        	);

	        Map<String, Object> usuarioEncontrado = null;

	        for (Map<String, Object> usuario : listaUsuarios) {
	            double usuarioId = Double.parseDouble(String.valueOf(usuario.get("id")));
	            if (usuarioId == this.id) {
	                usuarioEncontrado = usuario;
	            }
	        }

	        if (usuarioEncontrado != null) {
	            String email = (String) usuarioEncontrado.get("email");
	            String nombre = (String) usuarioEncontrado.get("nombre");
	            String username = (String) usuarioEncontrado.get("username");
	            String apellidos = (String) usuarioEncontrado.get("apellidos");
	            String dni = (String) usuarioEncontrado.get("dni");
	            String direccion = (String) usuarioEncontrado.get("direccion");
	            String telefono = (String) usuarioEncontrado.get("telefono1");

	            // Asignar datos a los labels
	            lblEmail.setText("Email: " + email);
	            lblNombre.setText("Nombre: " + nombre);
	            lblUsername.setText("Username: " + username);
	            lblApellidos.setText("Apellidos: " + apellidos);
	            lblDNI.setText("DNI: " + dni);
	            lblDireccion.setText("Dirección: " + direccion);
	            lblTelefono.setText("Teléfono: " + telefono);

	        } else {
	            System.out.println("No se encontró un usuario con id: " + this.id);
	        }

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
}
