package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controlador.Controlador;
import java.awt.Color;

public class MiPerfil extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JLabel lblEmail, lblUsername, lblNombre, lblApellidos, lblDNI, lblDireccion, lblTelefono, lblFotoPerfil;;


    public MiPerfil(Controlador controlador, int idProfe) {

        controlador.setMiPerfil(this);

        setTitle("Mi Perfil");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 380, 529);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JButton btnVolver = new JButton("");
        btnVolver.setIcon(new ImageIcon("fotos/volver.png"));
        btnVolver.setBounds(10, 11, 45, 45);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.addActionListener(e -> {
            MenuProfe menu = new MenuProfe(controlador, idProfe);
            menu.setVisible(true);
            dispose();
        });
        contentPane.add(btnVolver);
        
        lblFotoPerfil = new JLabel();
        lblFotoPerfil.setBounds(133, 54, 100, 100); 
        contentPane.add(lblFotoPerfil);

        lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setBounds(79, 206, 275, 20);
        contentPane.add(lblEmail);

        lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setBounds(79, 236, 275, 20);
        contentPane.add(lblUsername);

        lblNombre = new JLabel("Nombre:");
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setBounds(79, 266, 275, 20);
        contentPane.add(lblNombre);

        lblApellidos = new JLabel("Apellidos:");
        lblApellidos.setForeground(Color.WHITE);
        lblApellidos.setBounds(79, 296, 275, 20);
        contentPane.add(lblApellidos);

        lblDNI = new JLabel("DNI:");
        lblDNI.setForeground(Color.WHITE);
        lblDNI.setBounds(79, 326, 275, 20);
        contentPane.add(lblDNI);

        lblDireccion = new JLabel("Dirección:");
        lblDireccion.setForeground(Color.WHITE);
        lblDireccion.setBounds(79, 356, 275, 20);
        contentPane.add(lblDireccion);

        lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setForeground(Color.WHITE);
        lblTelefono.setBounds(79, 386, 275, 20);
        contentPane.add(lblTelefono);

        controlador.cargarPerfil(idProfe);

        JButton btnSalir = new JButton("");
        btnSalir.setIcon(new ImageIcon("fotos/salir.png"));
        btnSalir.setBounds(309, 11, 45, 45);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setBorderPainted(false);
        btnSalir.addActionListener(e -> {
            Login login = new Login(new Controlador());
            login.setVisible(true);
			controlador.cerrarConexion();
            dispose();
        });
        contentPane.add(btnSalir);
        
        JLabel lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 800, 534);
        lblFondo.setIcon(new ImageIcon("fotos/backgroundGRANDE.png"));
        contentPane.add(lblFondo);
    }

    public JLabel getLblEmail() { return lblEmail; }
    public JLabel getLblUsername() { return lblUsername; }
    public JLabel getLblNombre() { return lblNombre; }
    public JLabel getLblApellidos() { return lblApellidos; }
    public JLabel getLblDNI() { return lblDNI; }
    public JLabel getLblDireccion() { return lblDireccion; }
    public JLabel getLblTelefono() { return lblTelefono; }
    public JLabel getLblFotoPerfil() { return lblFotoPerfil; }

}
