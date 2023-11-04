import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class MenuGUI extends JFrame {
    private Map<Plato, Integer> carrito = new HashMap<>();
    private JLabel infoPlato;  // Nueva JLabel para mostrar la descripción del plato
    private JTextArea facturaTextArea;  // JTextArea para mostrar la factura

    int precioTotal = 0;

    public MenuGUI() {
        // Configuración básica del JFrame
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(650, 400);

        // Crear platos de ejemplo
        Plato platoFuerte1 = new Plato("Bandeja Paisa", "Exquisita bandeja paisa tradicional, el sabor no te decepcionará", "Plato fuerte","19 minutos" ,20000);
        Plato platoFuerte2 = new Plato("Pasta Boloñesa", "Rica pasta a la boloñesa con sus poderosos 100gr" + "<br>" + " de pasta pasaras la mejor exquisitez en tu paladar ", "Plato fuerte", "12 minutos", 15000);
        Plato platoFuerte3 = new Plato("Pollo Teriyaki", "Delicioso pollo hecho con delicadeza" + "<br>" + "y su exquisita salsa un manjar de otro mundo con su receta secreta trasmitida por decadas.", "Plato fuerte","15 minutos" ,18000);
        Plato platoEntrante1 = new Plato("Carpaccio de salmon", "Un manjar de entrante perfecta para las personas" + "<br>" + " amantes de la comida de mar, simplemente un manjar de 8 camarones ", "Entrantes","8 minutos" ,10000);
        Plato platoEntrante2 = new Plato("Gyozas de camarón con salsa acevichada", "Exquisitas gyozas de camarones" + "<br>" + "hechas por nuestro chef chino con 8 años de experiencia y" + "<br>" + " bañados con salsa de ceviche", "Entrante","6 minutos" ,8000);
        Plato platoEntrante3 = new Plato("Pulpo al olivo", "Pulpos seleccionados cuidadosamente por nuestro equipo en japon e" + "<br>" + " importados directamente desde las costas" + "<br>" + "japonesas", "Entradas", "10 minutos",14000);
        Plato platoEntrante4 = new Plato("Roast beef", "Deliciosa carne magra importada directamente desde autralia" + "<br>" + " con un año de maduracion y fermentada en salsa inglesa por mas de 1 año", "Entrada", "11 minutos",16000);
        Plato platoBebida1 = new Plato("Coca Cola", "Deliciosa gaseosa Coca-Cola tradicional, simplemente cocacola ", "Bebida", "2 minutos",4500);
        Plato platoBebida2 = new Plato("Quatro", "Deliciosa gaseosa Quatro, como la fanta pero barata", "Bebida", "2 minutos",4500);
        Plato platoBebida3 = new Plato("Kola Roman", "Deliciosa gaseosa, la copia barata de la cocacola," + "<br>" + " quien chucha compra Kola Roman si hay Coca Cola", "Bebida", "2 minutos",3000);
        Plato platoBebida4 = new Plato("Ginger", "Deliciosa gaseosa ginger," + "<br>" + "pero quien rayos pasa un almuerzo con ginger XD, pidela si quieres", "Bebida", "2 minutos",4500);

        // Agregar platos al menú
        Map<String, Plato> menu = new HashMap<>();
        menu.put("Bandaje Paisa", platoFuerte1);
        menu.put("Pasta Boloñesa", platoFuerte2);
        menu.put("Pollo Teriyaki", platoFuerte3);
        menu.put("Carpaccio de salmon", platoEntrante1);
        menu.put("Gyozas de camarón con salsa acevichada", platoEntrante2);
        menu.put("Pulpo al olivo", platoEntrante3);
        menu.put("Roast beef", platoEntrante4);
        menu.put("Coca Cola", platoBebida1);
        menu.put("Quatro", platoBebida2);
        menu.put("Kola Roman", platoBebida3);
        menu.put("Ginger", platoBebida4);

        // Crear elementos de la interfaz
        JComboBox<String> platosComboBox = new JComboBox<>(menu.keySet().toArray(new String[0]));
        JTextField unidadesTextField = new JTextField(5);
        JButton agregarButton = new JButton("Agregar al Carrito");
        JButton generarFacturaButton = new JButton("Generar Factura");

        // Acción del botón Agregar al Carrito
        agregarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String nombrePlato = platosComboBox.getSelectedItem().toString();
                    Plato platoSeleccionado = menu.get(nombrePlato);
                    int unidades = Integer.parseInt(unidadesTextField.getText());

                    double precioPlato = platoSeleccionado.getPrecio();

                    // Validaciones
                    if (precioTotal + (precioPlato * unidades) > 200000) {
                        throw new PrecioLimiteException("No puede superar el precio límite de almuerzo. ¿O acaso eres millonario?");
                    }

                    precioTotal += precioPlato * unidades;

                    if (carrito.containsKey(platoSeleccionado) && carrito.get(platoSeleccionado) + unidades > 5) {
                        throw new MaximoUnidadesException("No puede pedir más de 5 unidades del mismo plato. Gloton");
                    }

                    if (platoSeleccionado.getTipo().equals("Bebida") && unidades == 0) {
                        throw new BebidaNoSeleccionadaException("Debes seleccionar al menos una unidad de la bebida.");
                    }

                    infoPlato.setText("<html><p>Información del Plato Seleccionado: "
                            + platoSeleccionado.getDescripcion() + "<br>"
                            + "Tipo: " + platoSeleccionado.getTipo() + "<br>"
                            + "Tiempo de preparación: " + platoSeleccionado.getTiempo() + "<br>"
                            + "Precio: " + platoSeleccionado.getPrecio() + "</p></html>"); 

                    // Agregar al carrito
                    carrito.put(platoSeleccionado, carrito.getOrDefault(platoSeleccionado, 0) + unidades);

                    // Limpiar campos
                    unidadesTextField.setText("");
                    JPanel panel = (JPanel) getContentPane().getComponent(0);
                    panel.add(infoPlato);
                    panel.revalidate();
                    panel.repaint();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Ingrese un número válido de unidades. Dale capo no es tan dificil", "Error", JOptionPane.ERROR_MESSAGE);
                } catch (PrecioLimiteException | MaximoUnidadesException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                } catch (BebidaNoSeleccionadaException e1) {
                    JOptionPane.showMessageDialog(null, "¿Por qué no pedís bebida? ¿Quieres que llame a una ambulancia?", "Error", JOptionPane.ERROR_MESSAGE);
                    e1.printStackTrace();
                }
            }
            
        
        });

        generarFacturaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarFactura();
                mostrarVentanaFactura();
            }

            private void mostrarVentanaFactura() {
                JFrame facturaFrame = new JFrame("Factura");
                facturaFrame.getContentPane().add(new JScrollPane(facturaTextArea));
                facturaFrame.setSize(550, 300);
                facturaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                facturaFrame.setVisible(true);
            }
        });

        

        // Acción del botón Generar Factura
        generarFacturaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generarFactura();
            }
        });

        generarFacturaButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (!bebidaSeleccionada()) {
                        throw new BebidaNoSeleccionadaException("¿Por qué no pedís bebida? ¿Quieres que llame a una ambulancia?");
                    }
                    generarFactura();
                    mostrarVentanaFactura();
                } catch (BebidaNoSeleccionadaException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            private void mostrarVentanaFactura() {
                JFrame facturaFrame = new JFrame("Factura");
                facturaFrame.setSize(400, 300);
                facturaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                facturaFrame.setVisible(true);
            }
        });

        // Inicializar la JLabel para la descripción del plato
        infoPlato = new JLabel("Información del Plato Seleccionado:");

        // Inicializar el JTextArea para la factura
        facturaTextArea = new JTextArea(10, 40);
        facturaTextArea.setEditable(false);

        // Agregar elementos al JFrame
        JPanel panel = new JPanel();
        panel.add(new JLabel("Plato:"));
        panel.add(platosComboBox);
        panel.add(new JLabel("Unidades:"));
        panel.add(unidadesTextField);
        panel.add(agregarButton);
        panel.add(generarFacturaButton);
        panel.add(infoPlato);
        panel.add(facturaTextArea);

        getContentPane().add(panel);

        // Mostrar el JFrame
        setVisible(true);
    }

    // Método para generar la factura
    private void generarFactura() {
        facturaTextArea.setText("Factura:\n");
        for (Map.Entry<Plato, Integer> entry : carrito.entrySet()) {
            Plato plato = entry.getKey();
            int unidades = entry.getValue();
            facturaTextArea.append(plato.getNombre() + " - " + unidades + " unidades - Precio unitario: " + plato.getPrecio() +
                    " - Subtotal: " + (plato.getPrecio() * unidades) + "\n");
        }
        facturaTextArea.append("\nTotal a pagar: " + precioTotal);
    }

    // Método para verificar si se ha seleccionado una bebida en el carrito
    private boolean bebidaSeleccionada() {
        for (Plato plato : carrito.keySet()) {
            if (plato.getTipo().equals("Bebida")) {
                return true;
            }
        }
        return false;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MenuGUI();
            }
        });
    }
}

// Excepciones personalizadas
class PrecioLimiteException extends Exception {
    public PrecioLimiteException(String message) {
        super(message);
    }
}

class MaximoUnidadesException extends Exception {
    public MaximoUnidadesException(String message) {
        super(message);
    }
}

class BebidaNoSeleccionadaException extends Exception {
    public BebidaNoSeleccionadaException(String message) {
        super(message);
    }
}
