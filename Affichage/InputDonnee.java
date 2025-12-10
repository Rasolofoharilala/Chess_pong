package Affichage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InputDonnee extends JFrame {
    private JSpinner nombrePiecesSpinner;
    private JTextField pionsHP;
    private JTextField toursHP;
    private JTextField cavaliersHP;
    private JTextField fousHP;
    private JTextField reineHP;
    private JTextField roiHP;
    
    private JButton confirmerButton;
    private ConfigurationListener listener;
    
    // Panels pour afficher/masquer selon le nombre de pièces
    private JPanel toursPanel;
    private JPanel cavaliersPanel;
    private JPanel fousPanel;
    
    public InputDonnee() {
        setTitle("Configuration de l'échiquier");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Nombre de pièces
        JPanel nombrePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        nombrePanel.add(new JLabel("Nombre de colonnes (2-8): "));
        SpinnerNumberModel model = new SpinnerNumberModel(8, 2, 8, 2);
        nombrePiecesSpinner = new JSpinner(model);
        nombrePanel.add(nombrePiecesSpinner);
        mainPanel.add(nombrePanel);
        
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Points de vie des pièces
        JLabel titleLabel = new JLabel("Points de vie des pièces:");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(10));
        
        // Pions (toujours visibles)
        mainPanel.add(createFieldPanel("Pions:", pionsHP = new JTextField("100", 10)));
        
        // Reine et Roi (toujours visibles pour col >= 2)
        mainPanel.add(createFieldPanel("Reine:", reineHP = new JTextField("100", 10)));
        mainPanel.add(createFieldPanel("Roi:", roiHP = new JTextField("100", 10)));
        
        // Fous (visible pour col >= 4)
        fousPanel = createFieldPanel("Fous:", fousHP = new JTextField("100", 10));
        mainPanel.add(fousPanel);
        
        // Cavaliers (visible pour col >= 6)
        cavaliersPanel = createFieldPanel("Cavaliers:", cavaliersHP = new JTextField("100", 10));
        mainPanel.add(cavaliersPanel);
        
        // Tours (visible pour col >= 8)
        toursPanel = createFieldPanel("Tours:", toursHP = new JTextField("100", 10));
        mainPanel.add(toursPanel);
        
        mainPanel.add(Box.createVerticalStrut(20));
        
        // Bouton confirmer
        confirmerButton = new JButton("Confirmer");
        confirmerButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        confirmerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                confirmerConfiguration();
            }
        });
        mainPanel.add(confirmerButton);
        
        // Listener pour le spinner
        nombrePiecesSpinner.addChangeListener(e -> updateVisibility());
        
        // Initialiser la visibilité
        updateVisibility();
        
        add(new JScrollPane(mainPanel));
    }
    
    private JPanel createFieldPanel(String label, JTextField textField) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel(label));
        panel.add(textField);
        return panel;
    }
    
    private void updateVisibility() {
        int nbPieces = (Integer) nombrePiecesSpinner.getValue();
        
        // Fous visibles si >= 4
        fousPanel.setVisible(nbPieces >= 4);
        
        // Cavaliers visibles si >= 6
        cavaliersPanel.setVisible(nbPieces >= 6);
        
        // Tours visibles si >= 8
        toursPanel.setVisible(nbPieces >= 8);
        
        revalidate();
        repaint();
    }
    
    private void confirmerConfiguration() {
        try {
            int nbPieces = (Integer) nombrePiecesSpinner.getValue();
            int pionsHPVal = Integer.parseInt(pionsHP.getText());
            int reineHPVal = Integer.parseInt(reineHP.getText());
            int roiHPVal = Integer.parseInt(roiHP.getText());
            int fousHPVal = nbPieces >= 4 ? Integer.parseInt(fousHP.getText()) : 0;
            int cavaliersHPVal = nbPieces >= 6 ? Integer.parseInt(cavaliersHP.getText()) : 0;
            int toursHPVal = nbPieces >= 8 ? Integer.parseInt(toursHP.getText()) : 0;
            
            if (listener != null) {
                listener.onConfigurationConfirmed(nbPieces, pionsHPVal, reineHPVal, roiHPVal, 
                                                   fousHPVal, cavaliersHPVal, toursHPVal);
            }
            
            dispose(); // Fermer le formulaire
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Veuillez entrer des nombres valides pour les points de vie!", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void setConfigurationListener(ConfigurationListener listener) {
        this.listener = listener;
    }
    
    // Interface pour le callback
    public interface ConfigurationListener {
        void onConfigurationConfirmed(int nbPieces, int pionsHP, int reineHP, int roiHP, 
                                       int fousHP, int cavaliersHP, int toursHP);
    }
}
