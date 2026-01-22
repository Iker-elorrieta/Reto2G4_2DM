package Vista;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Color;

import Controlador.Controlador;

public class DetalleAlumno extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JLabel lblEmail, lblNombre, lblUsername, lblApellidos, lblDNI, lblDireccion, lblTelefono;


    public DetalleAlumno(Controlador controlador, int idProfe, int idAlumno) {

        controlador.setDetalleAlumno(this);

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
            ConsultarAlumnos ventana = new ConsultarAlumnos(controlador, idProfe);
            ventana.setVisible(true);
            dispose();
        });
        contentPane.add(btnVolver);

        lblEmail = new JLabel("Email:");
        lblEmail.setForeground(Color.WHITE);
        lblEmail.setBounds(100, 150, 400, 20);
        contentPane.add(lblEmail);

        lblNombre = new JLabel("Nombre:");
        lblNombre.setForeground(Color.WHITE);
        lblNombre.setBounds(100, 180, 400, 20);
        contentPane.add(lblNombre);

        lblUsername = new JLabel("Username:");
        lblUsername.setForeground(Color.WHITE);
        lblUsername.setBounds(100, 210, 400, 20);
        contentPane.add(lblUsername);

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

        controlador.cargarDetalleAlumno(idAlumno);

        JLabel lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 800, 534);
        lblFondo.setIcon(new ImageIcon("fotos/backgroundGRANDE.png"));
        contentPane.add(lblFondo);
    }

    public JLabel getLblEmail() { return lblEmail; }
    public JLabel getLblNombre() { return lblNombre; }
    public JLabel getLblUsername() { return lblUsername; }
    public JLabel getLblApellidos() { return lblApellidos; }
    public JLabel getLblDNI() { return lblDNI; }
    public JLabel getLblDireccion() { return lblDireccion; }
    public JLabel getLblTelefono() { return lblTelefono; }
}
