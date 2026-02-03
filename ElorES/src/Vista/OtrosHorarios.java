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
import modelo.Users;

public class OtrosHorarios extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    private JComboBox<String> cbProfesores;
    private JTable table;
    private DefaultTableModel modelo;

    public OtrosHorarios(Controlador controlador, int idProfe) {

        controlador.setOtrosHorarios(this);

        setTitle("Otros Horarios");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setBounds(100, 100, 808, 568);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // LOGO
        JLabel lblLogo = new JLabel();
        lblLogo.setBounds(340, 11, 140, 140);
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
            MenuProfe menu = new MenuProfe(controlador, idProfe);
            menu.setVisible(true);
            dispose();
        });
        contentPane.add(btnVolver);

        // COMBOBOX PROFESORES
        cbProfesores = new JComboBox<>();
        cbProfesores.setBounds(20, 100, 200, 30);
        contentPane.add(cbProfesores);

        // TABLA
        String[] columnas = {"Hora", "Lunes", "Martes", "Miércoles", "Jueves", "Viernes"};
        modelo = new DefaultTableModel(columnas, 6) {
            private static final long serialVersionUID = 1L;

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Inicializar columna de horas
        for (int i = 0; i < 6; i++) {
            modelo.setValueAt("Hora " + (i + 1), i, 0);
        }

        table = new JTable(modelo);
        table.setEnabled(false);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(20);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        // Centrar primera columna
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);

        final Controlador c = controlador;
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
                label.setHorizontalAlignment(column == 0 ? SwingConstants.CENTER : SwingConstants.LEFT);

                String text = value != null ? value.toString() : "";
                label.setText(text.startsWith("<html>") ? text : "<html>" + text + "</html>");

                // Colores azul-crema alternados
                Color azulClaro = new Color(173, 216, 230);
                Color crema = new Color(255, 253, 208);
                if (row % 2 == 0) {
                    label.setBackground(azulClaro);
                } else {
                    label.setBackground(crema);
                }

                // Colores según estado de reunión 
                if (value != null && value.toString().startsWith("Reunión")) {
                    String id = value.toString().replace("Reunión", "").split("con")[0].trim();
                    String estado = controlador.getEstadoReunion("Reunion" + id);
                    if (estado != null) {
                        switch (estado) {
                            case "pendiente": label.setBackground(new Color(255, 255, 150)); break;
                            case "aceptada":  label.setBackground(new Color(150, 255, 150)); break;
                            case "denegada":  label.setBackground(new Color(255, 120, 120)); break;
                            case "conflicto": label.setBackground(new Color(180, 180, 180)); break;
                        }
                    }
                }

                // Selección
                if (isSelected) {
                    label.setBackground(new Color(100, 149, 237));
                    label.setForeground(Color.WHITE);
                } else {
                    label.setForeground(Color.BLACK);
                }

                return label;
            }
        });

        // Cabecera de la tabla 
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(70, 130, 180)); // azul oscuro
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setReorderingAllowed(false);

        // Scroll pane
        JScrollPane scrollPane = new JScrollPane(table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBounds(10, 162, 772, 356);
        contentPane.add(scrollPane);

        // CARGAR PROFESORES
        controlador.otrosHorarios();

        cbProfesores.addActionListener(e -> {
            if (cbProfesores.getSelectedItem() == null) return;

            String nombreVisible = cbProfesores.getSelectedItem().toString();
            Users profesor = controlador.getMapaProfesores().get(nombreVisible);

            if (profesor != null) {
                controlador.cargarHorarioDeProfesor(profesor);
                ajustarAlturaFilas();
                ajustarAnchoColumnas();
            }
        });

        // BOTÓN SALIR
        JButton btnSalir = new JButton("");
        btnSalir.setIcon(new ImageIcon("fotos/salir.png"));
        btnSalir.setBounds(729, 11, 45, 45);
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
        JLabel lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 800, 534);
        lblFondo.setIcon(new ImageIcon("fotos/backgroundGRANDE.png"));
        contentPane.add(lblFondo);

        // Ajustar dimensiones iniciales
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

    public JComboBox<String> getCbProfesores() { 
        return cbProfesores; 
    }

    public DefaultTableModel getModelo() { 
        return modelo; 
    }

    public JTable getTable() {
        return table;
    }
}
