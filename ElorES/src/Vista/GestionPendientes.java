package Vista;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import Controlador.Controlador;

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

        setTitle("Gestionar Reuniones Pendientes");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 800, 534);
        setLocationRelativeTo(null);

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
        btnVolver.setBounds(10, 11, 60, 51);
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
        modelo = new DefaultTableModel(columnas, 0) {
  
			private static final long serialVersionUID = 1L;

			@Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(modelo);
        table.setRowSelectionAllowed(true);
        table.setColumnSelectionAllowed(false);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(40);

        // Ocultar columna ID
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);

        // CABECERA
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(70, 130, 180)); // azul suave
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setReorderingAllowed(false);

        // CENTRAR PRIMERA COLUMNA (aunque oculta)
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        // RENDERER GENERAL
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            private static final long serialVersionUID = 1L;

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {

                JLabel label = new JLabel();
                label.setFont(table.getFont());
                label.setOpaque(true);
                label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                label.setVerticalAlignment(SwingConstants.CENTER);

                String text = value != null ? value.toString() : "";
                label.setText(text.startsWith("<html>") ? text : "<html>" + text + "</html>");

                // Alternar colores de filas
                if (row % 2 == 0) {
                    label.setBackground(new Color(245, 245, 245));
                } else {
                    label.setBackground(Color.WHITE);
                }

                // Selección
                if (isSelected) {
                    label.setBackground(new Color(100, 149, 237)); // azul cornflower
                    label.setForeground(Color.WHITE);
                } else {
                    label.setForeground(Color.BLACK);
                }

                return label;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(10, 154, 764, 272);
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
        Boton btnAceptar = new Boton("Aceptar");
        btnAceptar.setBounds(209, 451, 89, 23);
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
        Boton btnRechazar = new Boton("Rechazar");
        btnRechazar.setBounds(475, 451, 89, 23);
        btnRechazar.addActionListener(e -> {
            if (idSeleccionado != 0) {
                controlador.cambiarEstadoReunion(idSeleccionado, "denegada");
                ConsultarReu gestion = new ConsultarReu(controlador, idProfe);
                gestion.setVisible(true);
                this.dispose();
            }
        });
        contentPane.add(btnRechazar);

        // BOTÓN SALIR
        JButton btnSalir = new JButton("");
        btnSalir.setIcon(new ImageIcon("fotos/salir.png"));
        btnSalir.setBounds(729, 17, 45, 45);
        btnSalir.setContentAreaFilled(false);
        btnSalir.setBorderPainted(false);
        btnSalir.addActionListener(e -> {
            Login login = new Login(new Controlador());
            login.setVisible(true);
            controlador.cerrarConexion();
            dispose();
        });
        contentPane.add(btnSalir);

        // FONDO
        lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 784, 495);
        lblFondo.setIcon(new ImageIcon("fotos/backgroundGRANDE.png"));
        contentPane.add(lblFondo);

        // CARGAR DATOS
        controlador.cargarPendientes();

        // AJUSTAR ANCHO Y ALTURA
        ajustarAnchoColumnas();
        ajustarAlturaFilas();
    }

    public void ajustarAlturaFilas() {
        for (int row = 0; row < table.getRowCount(); row++) {
            int maxHeight = 30;
            for (int column = 0; column < table.getColumnCount(); column++) {
                TableCellRenderer renderer = table.getCellRenderer(row, column);
                Component comp = table.prepareRenderer(renderer, row, column);
                maxHeight = Math.max(comp.getPreferredSize().height + 6, maxHeight);
            }
            table.setRowHeight(row, maxHeight);
        }
    }

    public void ajustarAnchoColumnas() {
        for (int col = 0; col < table.getColumnCount(); col++) {
            int maxWidth = 70;
            for (int row = 0; row < table.getRowCount(); row++) {
                TableCellRenderer renderer = table.getCellRenderer(row, col);
                Component comp = table.prepareRenderer(renderer, row, col);
                maxWidth = Math.max(comp.getPreferredSize().width + 15, maxWidth);
            }
            table.getColumnModel().getColumn(col).setPreferredWidth(maxWidth);
        }
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

    public JTable getTable() {
        return table;
    }
}
