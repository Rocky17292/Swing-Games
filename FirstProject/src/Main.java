import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;

public class Main extends JFrame {
    public Main() {
        setTitle("Custom Table Cell Renderer Example");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create table model
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("User");
        tableModel.addColumn("Name");
        tableModel.addColumn("Email");
        tableModel.addColumn("Email status");
        tableModel.addColumn("SMS");
        tableModel.addColumn("SMS status");

        // Add data to the table
        tableModel.addRow(new Object[] { "1", "John Doe", "john@example.com", "Check", "123456", "UnCheck" });
        tableModel.addRow(new Object[] { "2", "Jane Smith", "jane@example.com", "N/A", "789012", "Quota reached" });

        JTable table = new JTable(tableModel);
        CustomTableCellRenderer cellRenderer = new CustomTableCellRenderer(); // Create an instance of the renderer
        table.setDefaultRenderer(Object.class, cellRenderer);

        // Set preferred column widths
        setColumnWidths(table, 10, 150, 200, 100, 100, 100);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(800, 150));
        getContentPane().add(scrollPane, BorderLayout.NORTH);

        // Create panel for icons and their meanings
        JPanel iconPanel = new JPanel();
        iconPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        iconPanel.add(new JLabel(cellRenderer.createCheckIcon())); // Use the renderer instance to access the method
        iconPanel.add(new JLabel(" - Success"));
        iconPanel.add(Box.createHorizontalStrut(30));
        iconPanel.add(new JLabel(cellRenderer.createCrossIcon())); // Use the renderer instance to access the method
        iconPanel.add(new JLabel(" - Failure"));
        iconPanel.add(Box.createHorizontalStrut(30));
        iconPanel.add(new JLabel(cellRenderer.createNotAvailableIcon())); // Use the renderer instance to access the
                                                                          // method
        iconPanel.add(new JLabel(" - N/A"));
        iconPanel.add(Box.createHorizontalStrut(30));
        iconPanel.add(new JLabel(cellRenderer.createQuotaReachedIcon())); // Use the renderer instance to access the
                                                                          // method
        iconPanel.add(new JLabel(" - Limit Reached"));

        getContentPane().add(iconPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }

    private void setColumnWidths(JTable table, int... widths) {
        TableColumn column;
        for (int i = 0; i < widths.length; i++) {
            column = table.getColumnModel().getColumn(i);
            column.setPreferredWidth(widths[i]);
        }
    }

    class CustomTableCellRenderer extends DefaultTableCellRenderer {
        private final int SYMBOL_SIZE = 20;

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
                int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!(value instanceof Icon)) {
                setHorizontalAlignment(JLabel.CENTER); // Center-align text
            }

            if (column == 3 || column == 5) { // Apply rendering only to the "Email status" column
                String status = (String) value;
                if (status.equals("Check")) {
                    setIcon(createCheckIcon());
                    setText("");
                } else if (status.equals("UnCheck")) {
                    setIcon(createCrossIcon());
                    setText("");
                } else if (status.equals("Quota reached")) {
                    setIcon(createQuotaReachedIcon());
                } else if (status.equals("N/A")) {
                    setIcon(createNotAvailableIcon());
                }
                setText(""); // Clear text to display only icon
            } else {
                setIcon(null); // Clear icon if not in the "Email status" column
            }

            return c;
        }

        public Icon createCheckIcon() { // Made public to be accessible from outside the class
            return new Icon() {
                @Override
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    drawCheck(g2d, x + getIconWidth() / 2, y + getIconHeight() / 2, SYMBOL_SIZE);
                    g2d.dispose();
                }

                @Override
                public int getIconWidth() {
                    return SYMBOL_SIZE;
                }

                @Override
                public int getIconHeight() {
                    return SYMBOL_SIZE;
                }
            };
        }

        public Icon createCrossIcon() { // Made public to be accessible from outside the class
            return new Icon() {
                @Override
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    drawCross(g2d, x + getIconWidth() / 2, y + getIconHeight() / 2, SYMBOL_SIZE);
                    g2d.dispose();
                }

                @Override
                public int getIconWidth() {
                    return SYMBOL_SIZE;
                }

                @Override
                public int getIconHeight() {
                    return SYMBOL_SIZE;
                }
            };
        }

        public Icon createNotAvailableIcon() {
            final int SMALL_SYMBOL_SIZE = 13; // Define a smaller size for the icon
        
            return new Icon() {
                @Override
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    int diameter = getIconWidth();
        
                    // Draw black border circle
                    g.setColor(Color.RED);
                    ((Graphics2D) g).setStroke(new BasicStroke(2)); // Set thicker border
                    g.drawOval(x, y, diameter, diameter);
        
                    // Draw black diagonal line
                    g.drawLine(x, y, x + diameter, y + diameter);
                    g.drawLine(x, y + diameter, x + diameter, y);
        
                    // Set color to transparent
                    ((Graphics2D) g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0));
        
                    // Fill the circle with transparent color
                    g.fillOval(x, y, diameter, diameter);
                }
        
                @Override
                public int getIconWidth() {
                    return SMALL_SYMBOL_SIZE; // Return the smaller size
                }
        
                @Override
                public int getIconHeight() {
                    return SMALL_SYMBOL_SIZE; // Return the smaller size
                }
            };
        }
        
        

        public Icon createQuotaReachedIcon() {
            return new Icon() {
                @Override
                public void paintIcon(Component c, Graphics g, int x, int y) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setColor(Color.ORANGE);

                    int[] xPoints = { x + SYMBOL_SIZE / 2, x + 3, x + SYMBOL_SIZE - 3 }; // X coordinates for triangle
                    int[] yPoints = { y + 3, y + SYMBOL_SIZE - 3, y + SYMBOL_SIZE - 3 }; // Y coordinates for triangle
                    int nPoints = 3; // Number of points for the triangle

                    g2d.fillPolygon(xPoints, yPoints, nPoints); // Draw filled triangle

                    g2d.dispose();
                }

                @Override
                public int getIconWidth() {
                    return SYMBOL_SIZE;
                }

                @Override
                public int getIconHeight() {
                    return SYMBOL_SIZE;
                }
            };
        }

        private void drawCross(Graphics2D g2d, int x, int y, int size) {
            int halfSize = size / 2;

            g2d.setColor(Color.RED);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(x - halfSize / 2, y - halfSize / 2, x + halfSize / 2, y
                    + halfSize / 2);
            g2d.drawLine(x + halfSize / 2, y - halfSize / 2, x - halfSize / 2, y + halfSize / 2);
        }

        private void drawCheck(Graphics2D g2d, int x, int y, int size) {
            int halfSize = size / 2;

            g2d.setColor(Color.GREEN);
            g2d.setStroke(new BasicStroke(3));
            g2d.drawLine(x - halfSize / 2, y, x, y + halfSize / 2);
            g2d.drawLine(x, y + halfSize / 2, x + halfSize / 2, y - halfSize / 2);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Main().setVisible(true);
        });
    }
}
