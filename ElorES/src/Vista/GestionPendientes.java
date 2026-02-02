package Vista;

import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import Controlador.Controlador;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class GestionPendientes extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel lblLogo;
    private JButton btnVolver;
    private JLabel lblFondo;
    private DefaultTableModel modelo;
    private JTable table;

    // Datos de la fila seleccionada
    private Object[] datosFilaSeleccionada;
    private int idSeleccionado;

    public GestionPendientes(Controlador controlador, int idProfe) {

        controlador.setGestionPendientes(this);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 800, 534);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        // LOGO
        lblLogo = new JLabel();
        lblLogo.setBounds(344, 23, 120, 120);
        ImageIcon icono = new ImageIcon("fotos/logo.png");
        Image imagen = icono.getImage().getScaledInstance(
                lblLogo.getWidth(),
                lblLogo.getHeight(),
                Image.SCALE_SMOOTH
        );
        lblLogo.setIcon(new ImageIcon(imagen));
        contentPane.add(lblLogo);

        // BOTÓN VOLVER
        btnVolver = new JButton("");
        btnVolver.setIcon(new ImageIcon("fotos/volver.png"));
        btnVolver.setBounds(30, 11, 60, 51);
        btnVolver.setContentAreaFilled(false);
        btnVolver.setBorderPainted(false);
        btnVolver.addActionListener(e -> {
            ConsultarReu ventana = new ConsultarReu(controlador, idProfe);
            ventana.setVisible(true);
            dispose();
        });
        contentPane.add(btnVolver);

        // TABLA
        String[] columnas = {"ID", "Profesor", "Alumno", "Centro", "Titulo", "Asunto", "Aula"};
        modelo = new DefaultTableModel(columnas, 0);

        table = new JTable(modelo);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);

        // Ocultar columna ID
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(10, 165, 642, 319);
        contentPane.add(scrollPane);

        // SELECCIÓN DE FILA
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {

                int fila = table.getSelectedRow();
                if (fila == -1) return;

                // Guardar ID
                idSeleccionado = ((Integer) table.getValueAt(fila, 0));

                // Guardar toda la fila en un array
                datosFilaSeleccionada = new Object[table.getColumnCount()];
                for (int col = 0; col < table.getColumnCount(); col++) {
                    datosFilaSeleccionada[col] = table.getValueAt(fila, col);
                }
            }
        });

        // BOTÓN ACEPTAR
        JButton btnAceptar = new JButton("Aceptar");
        btnAceptar.setBounds(662, 257, 89, 23);
        btnAceptar.addActionListener(e -> {
            if (idSeleccionado != 0) {
                controlador.cambiarEstadoReunion(idSeleccionado, "aceptada");
                ConsultarReu gestion = new ConsultarReu(controlador, idProfe);
                gestion.setVisible(true);
                this.dispose();
            }
        });
        contentPane.add(btnAceptar);

        // BOTÓN RECHAZAR
        JButton btnRechazar = new JButton("Rechazar");
        btnRechazar.setBounds(662, 362, 89, 23);
        btnRechazar.addActionListener(e -> {
            if (idSeleccionado != 0) {
                controlador.cambiarEstadoReunion(idSeleccionado, "denegada");
                ConsultarReu gestion = new ConsultarReu(controlador, idProfe);
                gestion.setVisible(true);
                this.dispose();
            }
        });
        contentPane.add(btnRechazar);

        // FONDO (AL FINAL)
        lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 784, 495);
        lblFondo.setIcon(new ImageIcon("fotos/backgroundGRANDE.png"));
        contentPane.add(lblFondo);

        // CARGAR DATOS
        controlador.cargarPendientes();
    }

    public DefaultTableModel getModelo() {
        return modelo;
    }

    public Object[] getDatosFilaSeleccionada() {
        return datosFilaSeleccionada;
    }

    public int getIdSeleccionado() {
        return idSeleccionado;
    }
}
