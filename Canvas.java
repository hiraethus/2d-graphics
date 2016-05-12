import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class Canvas extends JPanel {
    private java.util.List<Point> randomPoints;
    private java.util.List<Color> randomColours;
    private String message = "Hello, world";
    private static final int QUANTITY_TOTAL = 50;
    private int quantity = QUANTITY_TOTAL / 2;
    private static final int MAX_SPEED = 10;
    private int speed = 0;


    {
        generateRandomPoints();
        generateRandomColors();
    }

    private double proportion = .1;

    public void setProportion(double prop) {
        proportion = prop;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return this.speed;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int q) {
        this.quantity = q;
    }

    public void generateRandomPoints() {
        randomPoints = new ArrayList<Point>(QUANTITY_TOTAL);
        for (int i = 0; i < QUANTITY_TOTAL; ++i) {
            randomPoints.add(new Point((int) (Math.random() * 500 - 250), (int) (Math.random() * 500 - 250)));
        }

    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return this.message;
    }

    public void generateRandomColors() {
        randomColours = new ArrayList<Color>(500);
        Random rand = new Random();

        for (int i = 0; i < QUANTITY_TOTAL; ++i) {
            float r = rand.nextFloat();
            float g = rand.nextFloat();
            float b = rand.nextFloat();
            randomColours.add(new Color(r,g,b));
        }
    }

    @Override public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setFont(new Font("Arial", Font.PLAIN, 21));

        g2d.translate(this.getWidth() / 2, this.getHeight() / 2);

        g2d.translate(Math.sin(proportion) * 100, 0);

        Font f = Font.getFont(Font.MONOSPACED);
        g2d.setFont(f);

        for (int i = 0; i < quantity; ++i) {
            Point ranPoint = randomPoints.get(i);
            g2d.rotate(proportion * Math.sin(proportion), ranPoint.x, ranPoint.y);

            g2d.setColor(Color.WHITE);
            g2d.drawString(message, ranPoint.x - 1, ranPoint.y - 1);
            g2d.setColor(randomColours.get(i));
            g2d.drawString(message, ranPoint.x, ranPoint.y);

            g2d.rotate(-proportion * Math.sin(proportion), ranPoint.x, ranPoint.y);
        }
    }

    public static void main (String args[]) throws InterruptedException {

        final Canvas canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(500,500));
        canvas.setMinimumSize(new Dimension(500,500));
        canvas.setMaximumSize(new Dimension(500,500));

        JFrame frame = new JFrame();

        JPanel p = new JPanel(new GridBagLayout());
        frame.getContentPane().add(p);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 3;
        gbc.weighty = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.LINE_END;

        p.add(canvas, gbc);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weighty = 0;
        gbc.weightx = 0;

        JLabel resizeLbl = new JLabel("Resize");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        p.add(resizeLbl, gbc);

        final JSlider slider = new JSlider();
        slider.setMinimum(0);
        slider.setMaximum(QUANTITY_TOTAL);
        slider.setValue(canvas.getQuantity());
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                canvas.setQuantity(slider.getValue());
            }
        });

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        p.add(slider, gbc);
        resizeLbl.setLabelFor(slider);


        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        JLabel msgLbl = new JLabel("Message");
        p.add(msgLbl, gbc);

        final JTextField msgField = new JTextField(canvas.getMessage());
        msgLbl.setLabelFor(msgField);
        msgField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                canvas.setMessage(msgField.getText().trim());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                canvas.setMessage(msgField.getText().trim());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                canvas.setMessage(msgField.getText().trim());
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;
        p.add(msgField, gbc);

        final JSlider speedSlider = new JSlider(JSlider.VERTICAL);
        speedSlider.setMinimum(0);
        speedSlider.setMaximum(canvas.MAX_SPEED - 1);
        speedSlider.setValue(canvas.getSpeed());
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                canvas.setSpeed(speedSlider.getValue());
            }
        });

        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        p.add(speedSlider, gbc);

        frame.setPreferredSize(new Dimension(600,600));
        frame.setMinimumSize(new Dimension(600,600));
        frame.setMaximumSize(new Dimension(600,600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setVisible(true);

        //do animation
        new Thread(new Runnable() {
            @Override
            public void run() {
                //To change body of implemented methods use File | Settings | File Templates.
                double proportion = 0;
		boolean goingUp = true;
                while (true) {
		    proportion = (proportion + 0.001);
                    canvas.setProportion(proportion);
                    canvas.repaint();

                    try {
                        Thread.sleep(MAX_SPEED - canvas.getSpeed());
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                    if (proportion >= Math.PI * 2) proportion = 0.;
                }
            }
        }).run();
    }
}
