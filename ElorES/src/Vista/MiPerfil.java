package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Controlador.Controlador;
import java.awt.Color;

public class MiPerfil extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JLabel lblEmail, lblUsername, lblNombre, lblApellidos, lblDNI, lblDireccion, lblTelefono;


    public MiPerfil(Controlador controlador, int idProfe) {

        controlador.setMiPerfil(this);

        setTitle("Mi Perfil");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 534);
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

        lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setBounds(100, 150, 400, 20);
        contentPane.add(lblEmail);

        lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setBounds(100, 180, 400, 20);
        contentPane.add(lblUsername);

        lblNombre = new JLabel("Nombre:");
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setBounds(100, 210, 400, 20);
        contentPane.add(lblNombre);

        lblApellidos = new JLabel("Apellidos:");
        lblApellidos.setForeground(Color.WHITE);
        lblApellidos.setBounds(100, 240, 400, 20);
        contentPane.add(lblApellidos);

        lblDNI = new JLabel("DNI:");
        lblDNI.setForeground(Color.WHITE);
        lblDNI.setBounds(100, 270, 400, 20);
        contentPane.add(lblDNI);

        lblDireccion = new JLabel("Dirección:");
        lblDireccion.setForeground(Color.WHITE);
        lblDireccion.setBounds(100, 300, 400, 20);
        contentPane.add(lblDireccion);

        lblTelefono = new JLabel("Teléfono:");
        lblTelefono.setForeground(Color.WHITE);
        lblTelefono.setBounds(100, 330, 400, 20);
        contentPane.add(lblTelefono);

        controlador.cargarPerfil(idProfe);

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
}
