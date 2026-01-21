package Vista;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;



import javax.swing.JScrollPane;
import javax.swing.JTable;


public class ConsultarReu extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JLabel lblLogo;
    private JLabel lblFondo;
    private JButton btnVolver;
    private JButton btnLogin;
    private JTable table;

    
	/**
	 * Create the frame.
	 * @param id 
	 * @param dos 
	 * @param dis 
	 * @param cliente 
	 */
	public ConsultarReu(Socket cliente, DataInputStream dis, DataOutputStream dos, int id) {
		setTitle("Consultar mis reuniones");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 534);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		// LOGO
        lblLogo = new JLabel();
        lblLogo.setBounds(327, 44, 120, 120);
        ImageIcon icono = new ImageIcon("fotos/logo.png");
        Image imagen = icono.getImage().getScaledInstance(
                lblLogo.getWidth(),
                lblLogo.getHeight(),
                Image.SCALE_SMOOTH
        );
        lblLogo.setIcon(new ImageIcon(imagen));
        contentPane.add(lblLogo);
        
        // Botón volver
        btnVolver = new JButton("");
		btnVolver.setIcon(new ImageIcon("fotos/volver.png"));
		btnVolver.setBounds(10, 11, 45, 45);
		btnVolver.setContentAreaFilled(false);
		btnVolver.setBorderPainted(false);
		btnVolver.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		GestionReuniones gestionReuniones = new GestionReuniones(cliente, dis, dos, id);
        		gestionReuniones.setVisible(true);
        		dispose();
        	}
        });
		contentPane.add(btnVolver);
		
		// Botón salir
		btnLogin = new JButton("");
		btnLogin.setIcon(new ImageIcon("fotos/salir.png"));
		btnLogin.setBounds(729, 11, 45, 45);
		btnLogin.setContentAreaFilled(false);
        btnLogin.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Login login = new Login(cliente, dis, dos);
        		login.setVisible(true);   // MOSTRAR login
                dispose();
        	}
        });
		btnLogin.setBorderPainted(false);
		contentPane.add(btnLogin);
        
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(198, 114, 435, 283);
        contentPane.add(scrollPane);
        
        table = new JTable();
        scrollPane.setViewportView(table);
        
        // FONDO
        lblFondo = new JLabel("");
        lblFondo.setBounds(0, 0, 800, 534);
        lblFondo.setIcon(new ImageIcon("fotos/backgroundGRANDE.png"));
        contentPane.add(lblFondo);

        //cargarDatos();
        
	}
	
	
	/* private void cargarDatos() {
	        new Thread(() -> {
	            try {
	                dos.writeUTF("3");
	                dos.flush();

	                String json = dis.readUTF();

	                Gson gson = new Gson();
	                ArrayList<Map<String, Object>> listaReuniones = gson.fromJson(
	                        json,
	                        new TypeToken<ArrayList<Map<String, Object>>>(){}.getType()
	                );
	                

	                SwingUtilities.invokeLater(() -> {
	                    rellenarTabla(listaReuniones);
	                });


	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }).start();
	    }
	
	 private void rellenarTabla(ArrayList<Map<String, Object>> lista) {
	    	
		    

	        for (Map<String, Object> horario : lista) {

	            String hora = (String.valueOf(horario.get("hora"))); // 1–6
	            String dia = (String) horario.get("dia");
	            String asignatura = (String) horario.get("modulos");

	            Double horaInt = Double.parseDouble(hora);
	            int fila = horaInt.intValue() - 1;
	            int columna = switch (dia.toLowerCase()) {
	                case "Estado" -> 1;
	                case "Profesor" -> 2;
	                case "Alumno" -> 3;
	                case "Centro" -> 4;
	                case "Fecha" -> 5;
	                default -> -1;
	            };

	            if (columna != -1) {
	                table.setValueAt(asignatura, fila, columna);
	            }
	        }
	    }
	*/
	
}
