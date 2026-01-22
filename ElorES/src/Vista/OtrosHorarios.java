package Vista;

import java.awt.Image;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Controlador.Controlador;

public class OtrosHorarios extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JComboBox<String> cbProfesores;
    private JTable tabla;
    private DefaultTableModel modelo;


    public OtrosHorarios(Controlador controlador, int idProfe) {

        controlador.setOtrosHorarios(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 534);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // LOGO
        JLabel lblLogo = new JLabel();
        lblLogo.setBounds(327, 44, 120, 120);
        ImageIcon icono = new ImageIcon("fotos/logo.png");
        Image imagen = icono.getImage().getScaledInstance(
                lblLogo.getWidth(),
                lblLogo.getHeight(),
                Image.SCALE_SMOOTH
        );
        lblLogo.setIcon(new ImageIcon(imagen));
        contentPane.add(lblLogo);

        // BOTÓN SALIR
        JButton btnSalir = new JButton("");
        btnSalir.setIcon(new ImageIcon("fotos/salir.png"));
        btnSalir.setBounds(729, 11, 45, 45);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setBorderPainted(false);
        btnSalir.addActionListener(e -> {
            Login login = new Login(controlador);
            login.setVisible(true);
            dispose();
        });
        contentPane.add(btnSalir);

        // BOTÓN VOLVER
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

        // COMBOBOX PROFESORES
        cbProfesores = new JComboBox<>();
        cbProfesores.setBounds(10, 92, 171, 30);
        contentPane.add(cbProfesores);

        // TABLA
        String[] columnas = {"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        modelo = new DefaultTableModel(columnas, 6);

        for (int i = 0; i < 6; i++) {
            modelo.setValueAt("Hora " + (i + 1), i, 0);
        }

        tabla = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBounds(200, 150, 550, 300);
        contentPane.add(scrollPane);

        // CARGAR PROFESORES
        controlador.otrosHorarios();

        // CUANDO SELECCIONA UN PROFESOR → CARGAR SU HORARIO
        cbProfesores.addActionListener(e -> {
            String nombre = cbProfesores.getSelectedItem().toString();
            controlador.cargarHorarioDeProfesor(nombre);
        });

        // FONDO
        JLabel lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 800, 534);
        lblFondo.setIcon(new ImageIcon("fotos/backgroundGRANDE.png"));
        contentPane.add(lblFondo);
    }

    public JComboBox<String> getCbProfesores() { return cbProfesores; }
    public DefaultTableModel getModelo() { return modelo; }
}
