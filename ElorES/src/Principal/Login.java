package Principal;

import java.awt.EventQueue;
import java.awt.Image;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.ImageIcon;
import javax.swing.JButton;


public class Login extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField tfcorreo;
    private JTextField tfcontraseña;
    private JLabel lblError;

    private Socket cliente;
    private DataInputStream dis;
    private DataOutputStream dos;
    
    int id;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Login frame = new Login();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public Login() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 706, 485);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);       

        // EL LOGO
        JLabel lblLogo = new JLabel();
        lblLogo.setBounds(286, 46, 120, 120);
        ImageIcon icono = new ImageIcon("fotos/logo.png");
        Image imagen = icono.getImage().getScaledInstance(
                lblLogo.getWidth(),
                lblLogo.getHeight(),
                Image.SCALE_SMOOTH
        );
        lblLogo.setIcon(new ImageIcon(imagen));
        contentPane.add(lblLogo);

        tfcorreo = new JTextField();
        tfcorreo.setColumns(10);
        tfcorreo.setBounds(182, 224, 361, 31);
        contentPane.add(tfcorreo);

        tfcontraseña = new JTextField();
        tfcontraseña.setColumns(10);
        tfcontraseña.setBounds(182, 285, 361, 31);
        contentPane.add(tfcontraseña);

        JLabel lblCorreo = new JLabel("Usuario:");
        lblCorreo.setForeground(new Color(255, 255, 255));
        lblCorreo.setBounds(80, 232, 59, 14);
        contentPane.add(lblCorreo);

        JLabel lblContraseña = new JLabel("Contraseña:");
        lblContraseña.setForeground(new Color(255, 255, 255));
        lblContraseña.setBounds(80, 293, 92, 14);
        contentPane.add(lblContraseña);

        JButton btnAcceder = new JButton("Acceder");
        btnAcceder.setBounds(319, 345, 89, 23);
        contentPane.add(btnAcceder);
        
        lblError = new JLabel("");
        lblError.setForeground(Color.RED);
        lblError.setBounds(274, 379, 224, 14);
        contentPane.add(lblError);
        
        lblError.setText("");
        
        // EL FONDO
        JLabel lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 724, 458);
        lblFondo.setIcon(new ImageIcon("fotos/background.png"));
        contentPane.add(lblFondo);

        btnAcceder.addActionListener(e -> {
            if (tfcorreo.getText().isEmpty() || tfcontraseña.getText().isEmpty())
            {
            	lblError.setText("Debes completar todos los campos");
            } else {
                validarUsuario();
            }
        });
    }

 
    public void validarUsuario() {
        try {
            // Abrir conexión si no está abierta
            if (cliente == null) {
                cliente = new Socket("localhost", 5000);
                dis = new DataInputStream(cliente.getInputStream());
                dos = new DataOutputStream(cliente.getOutputStream());
            }

            // Cifrar correo y contraseña
            String correoHash = cifrarUsuario(tfcorreo.getText());
            String passHash = cifrarUsuario(tfcontraseña.getText());

            // Enviar datos al servidor
            dos.writeUTF(correoHash);
            dos.writeUTF(passHash);

            // Leer respuesta del servidor
            String respuesta = dis.readUTF();

            if (respuesta.equals("Correcto")) {
                id = Integer.parseInt(dis.readUTF());
                lblError.setText("Login correcto");
                lblError.setForeground(Color.GREEN);
                
                MenuProfe menu = new MenuProfe(cliente, dis, dos, id);
                menu.setVisible(true);  
                dispose();              
               
                
            } else {
                lblError.setText("Correo o contraseña incorrectos");
                lblError.setForeground(Color.RED);
            }

        } catch (IOException e) {
            e.printStackTrace();
            lblError.setText("Error de conexión");
            lblError.setForeground(Color.RED);
        }
    }


    public static String cifrarUsuario(String texto) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] databytes = texto.getBytes();
            md.update(databytes);
            byte[] resumen = md.digest();
            return new String(resumen);
        } catch (NoSuchAlgorithmException e) {
            return "Error al cifrar";
        }
    }
}
