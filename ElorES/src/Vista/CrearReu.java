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
    private JLabel lblEstado;
    private JComboBox<String> comboUbicacion;

    private JTextField txtNombre;


    public CrearReu(Controlador controlador, int idProfe) {

        controlador.setCrearReu(this); 

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
        lblNombre.setBounds(27, 202, 128, 14);
        contentPane.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(129, 190, 208, 29);
        contentPane.add(txtNombre);

        lblFechaYHora = new JLabel("Fecha y Hora:");
        lblFechaYHora.setForeground(Color.WHITE);
        lblFechaYHora.setBounds(27, 259, 128, 14);
        contentPane.add(lblFechaYHora);

        JDateChooser dateChooser = new JDateChooser();
        dateChooser.setBounds(129, 255, 208, 24);
        contentPane.add(dateChooser);

        lblAula = new JLabel("Aula:");
        lblAula.setForeground(Color.WHITE);
        lblAula.setBounds(27, 317, 128, 14);
        contentPane.add(lblAula);

        JComboBox<String> comboAula = new JComboBox<>();
        comboAula.setModel(new DefaultComboBoxModel<>(new String[]{
                "", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"
        }));
        comboAula.setBounds(129, 302, 208, 30);
        contentPane.add(comboAula);

        lblTema = new JLabel("Tema:");
        lblTema.setForeground(Color.WHITE);
        lblTema.setBounds(27, 381, 128, 14);
        contentPane.add(lblTema);

        JTextArea textAreaTema = new JTextArea();
        textAreaTema.setBounds(129, 376, 207, 93);
        contentPane.add(textAreaTema);

        lblMiembros = new JLabel("Miembros:");
        lblMiembros.setForeground(Color.WHITE);
        lblMiembros.setBounds(439, 202, 128, 14);
        contentPane.add(lblMiembros);

        JComboBox<String> comboMiembros = new JComboBox<>();
        comboMiembros.setModel(new DefaultComboBoxModel<>(new String[]{
                "", "Profesores", "Profesores y Alumnos"
        }));
        comboMiembros.setBounds(589, 189, 171, 30);
        contentPane.add(comboMiembros);

        lblEstado = new JLabel("Estado:");
        lblEstado.setForeground(Color.WHITE);
        lblEstado.setBounds(439, 259, 128, 14);
        contentPane.add(lblEstado);

        JComboBox<String> comboEstado = new JComboBox<>();
        comboEstado.setModel(new DefaultComboBoxModel<>(new String[]{
                "", "Pendiente", "Conflicto", "Aceptada", "Cancelada"
        }));
        comboEstado.setBounds(589, 255, 171, 30);
        contentPane.add(comboEstado);

        lblUbicacion = new JLabel("Ubicación:");
        lblUbicacion.setForeground(Color.WHITE);
        lblUbicacion.setBounds(439, 317, 128, 14);
        contentPane.add(lblUbicacion);

        comboUbicacion = new JComboBox<>();
        comboUbicacion.setBounds(589, 313, 171, 30);
        contentPane.add(comboUbicacion);

        // FONDO
        lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 800, 534);
        lblFondo.setIcon(new ImageIcon("fotos/backgroundGRANDE.png"));
        contentPane.add(lblFondo);
        
        controlador.cargarCentros();
    }


	public JComboBox<String> getComboUbicacion() {return comboUbicacion;}

    
}
