package zoologia;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;

/**
 * Main GUI class for the Music Genre Explorer application.
 * Handles user interface and delegates business logic to MusicGenreService.
 */
public class MusicGenreGUI extends JFrame {
    
    // Service layer
    private final MusicGenreService musicGenreService;
    
    // Data arrays
    private String[] allGenres;
    private String[] allProperties;
    
    // GUI Components - Genre Explorer Tab
    private JList<String> genreList;
    private final DefaultListModel<String> genreListModel = new DefaultListModel<>();
    private final JTextArea genrePropertiesDisplay = new JTextArea();
    private final JTextArea genreDescriptionDisplay = new JTextArea();
    private final JLabel genreImageLabel = new JLabel();
    
    // GUI Components - Property Search Tab
    private JList<String> availablePropertiesList;
    private final DefaultListModel<String> availablePropertiesModel = new DefaultListModel<>();
    private final DefaultListModel<String> selectedPropertiesModel = new DefaultListModel<>();
    private JList<String> selectedPropertiesList;
    private JButton addPropertyButton;
    private JButton removePropertyButton;
    private JButton clearAllPropertiesButton;
    private JComboBox<String> searchCriteriaComboBox;
    private final JTextArea searchResultsDisplay = new JTextArea();
    
    // UI Color Scheme
    private final Color BACKGROUND_COLOR = new Color(250, 250, 250);
    private final Color CARD_COLOR = Color.WHITE;
    private final Color PRIMARY_COLOR = new Color(59, 130, 246);
    private final Color TEXT_COLOR = new Color(31, 41, 55);
    private final Color SECONDARY_TEXT_COLOR = new Color(107, 114, 128);
    private final Color BORDER_COLOR = new Color(229, 231, 235);
    
    public MusicGenreGUI() {
        super("Sistema de Géneros Musicales");
        this.musicGenreService = new MusicGenreService();
        
        setupWindow();
        initializeComponents();
    }
    
    /**
     * Sets up the main window properties
     */
    private void setupWindow() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        // Modern sizing and centering
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowWidth = Math.min(1200, screenSize.width - 100);
        int windowHeight = Math.min(800, screenSize.height - 100);
        this.setBounds(
            (screenSize.width - windowWidth) / 2, 
            (screenSize.height - windowHeight) / 2, 
            windowWidth, 
            windowHeight
        );
        
        // Set look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Initializes all GUI components and loads data
     */
    private void initializeComponents() {
        this.setBackground(BACKGROUND_COLOR);
        
        JTabbedPane mainTabbedPane = createMainTabbedPane();
        this.add(mainTabbedPane);
        
        // Load knowledge base
        loadKnowledgeBase();
        setupGenreExplorerTab(mainTabbedPane);
        setupPropertySearchTab(mainTabbedPane);
        setupTaxonomyTreeTab(mainTabbedPane);
        
        // Handle window closing
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                handleWindowClose();
            }
        });
    }
    
    /**
     * Creates the main tabbed pane
     */
    private JTabbedPane createMainTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(BACKGROUND_COLOR);
        tabbedPane.setFont(new Font("SF Pro Display", Font.PLAIN, 14));
        tabbedPane.setBorder(new EmptyBorder(20, 20, 20, 20));
        return tabbedPane;
    }
    
    /**
     * Loads the Prolog knowledge base files
     */
    private void loadKnowledgeBase() {
        musicGenreService.loadKnowledgeBase("inference_engine.pl");
        musicGenreService.loadKnowledgeBase("knowledge_base.pl");
        musicGenreService.loadKnowledgeBase("queries.pl");
    }
    
    /**
     * Sets up the genre explorer tab
     */
    private void setupGenreExplorerTab(JTabbedPane parentTabbedPane) {
        JPanel genreExplorerTab = createModernPanel();
        parentTabbedPane.addTab("🎵 Explorar Géneros", genreExplorerTab);
        
        JPanel leftCard = createCard(20, 20, 380, 660);
        JPanel rightCard = createCard(420, 20, 500, 660);
        
        genreExplorerTab.add(leftCard);
        genreExplorerTab.add(rightCard);
        
        setupGenreListSection(leftCard);
        setupGenreImageSection(rightCard);
        
        loadGenreData();
    }
    
    /**
     * Sets up the genre list section on the left side
     */
    private void setupGenreListSection(JPanel parentCard) {
        // Genre list
        JLabel genresLabel = createStyledLabel("Géneros Musicales", 18, true);
        genresLabel.setBounds(0, 0, 300, 30);
        parentCard.add(genresLabel);
        
        genreList = createStyledList(genreListModel);
        JScrollPane genreScrollPane = createScrollPane(genreList, 0, 40, 348, 200);
        parentCard.add(genreScrollPane);
        
        // Properties display
        JLabel propertiesLabel = createStyledLabel("Propiedades", 18, true);
        propertiesLabel.setBounds(0, 260, 300, 30);
        parentCard.add(propertiesLabel);
        
        setupTextArea(genrePropertiesDisplay, 12, false);
        JScrollPane propertiesScrollPane = createScrollPane(genrePropertiesDisplay, 0, 300, 348, 160);
        parentCard.add(propertiesScrollPane);
        
        // Description display
        JLabel descriptionLabel = createStyledLabel("Descripción", 18, true);
        descriptionLabel.setBounds(0, 480, 300, 30);
        parentCard.add(descriptionLabel);
        
        setupTextArea(genreDescriptionDisplay, 13, false);
        JScrollPane descriptionScrollPane = createScrollPane(genreDescriptionDisplay, 0, 520, 348, 120);
        parentCard.add(descriptionScrollPane);
        
        // Add selection listener
        genreList.addListSelectionListener(this::handleGenreSelection);
    }
    
    /**
     * Sets up the image section on the right side
     */
    private void setupGenreImageSection(JPanel parentCard) {
        genreImageLabel.setBounds(16, 16, 468, 628);
        genreImageLabel.setHorizontalAlignment(JLabel.CENTER);
        genreImageLabel.setVerticalAlignment(JLabel.CENTER);
        genreImageLabel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        genreImageLabel.setBackground(new Color(248, 250, 252));
        genreImageLabel.setOpaque(true);
        parentCard.add(genreImageLabel);
    }
    
    /**
     * Sets up the property search tab
     */
    private void setupPropertySearchTab(JTabbedPane parentTabbedPane) {
        JPanel propertySearchTab = createModernPanel();
        parentTabbedPane.addTab("🔍 Buscar por Propiedades", propertySearchTab);
        
        JPanel leftCard = createCard(20, 20, 380, 660);
        JPanel rightCard = createCard(420, 20, 500, 660);
        
        propertySearchTab.add(leftCard);
        propertySearchTab.add(rightCard);
        
        setupPropertySelectionSection(leftCard);
        setupSearchResultsSection(rightCard);
        
        loadPropertyData();
    }
    
    /**
     * Sets up the property selection section
     */
    private void setupPropertySelectionSection(JPanel parentCard) {
        // Available properties list
        JLabel availablePropertiesLabel = createStyledLabel("Todas las Propiedades", 16, true);
        availablePropertiesLabel.setBounds(0, 0, 300, 25);
        parentCard.add(availablePropertiesLabel);
        
        availablePropertiesList = createStyledList(availablePropertiesModel);
        JScrollPane availablePropertiesScrollPane = createScrollPane(availablePropertiesList, 0, 30, 348, 200);
        parentCard.add(availablePropertiesScrollPane);
        
        // Control buttons
        setupPropertyControlButtons(parentCard);
        
        // Selected properties list
        JLabel selectedPropertiesLabel = createStyledLabel("Propiedades Seleccionadas", 16, true);
        selectedPropertiesLabel.setBounds(0, 280, 300, 25);
        parentCard.add(selectedPropertiesLabel);
        
        selectedPropertiesList = createSelectedPropertiesList();
        JScrollPane selectedPropertiesScrollPane = createScrollPane(selectedPropertiesList, 0, 310, 348, 150);
        parentCard.add(selectedPropertiesScrollPane);
        
        // Search criteria selection
        setupSearchCriteriaSection(parentCard);
        
        // Property selection listener
        availablePropertiesList.addListSelectionListener(this::handleSinglePropertySelection);
    }
    
    /**
     * Sets up the property control buttons
     */
    private void setupPropertyControlButtons(JPanel parentCard) {
        addPropertyButton = createStyledButton("Agregar →", 16);
        addPropertyButton.setBounds(0, 240, 110, 30);
        addPropertyButton.addActionListener(e -> addSelectedProperty());
        parentCard.add(addPropertyButton);
        
        removePropertyButton = createStyledButton("← Quitar", 16);
        removePropertyButton.setBounds(120, 240, 110, 30);
        removePropertyButton.addActionListener(e -> removeSelectedProperty());
        parentCard.add(removePropertyButton);
        
        clearAllPropertiesButton = createStyledButton("Limpiar Todo", 16);
        clearAllPropertiesButton.setBounds(240, 240, 108, 30);
        clearAllPropertiesButton.addActionListener(e -> clearAllSelectedProperties());
        parentCard.add(clearAllPropertiesButton);
    }
    
    /**
     * Sets up the search criteria section
     */
    private void setupSearchCriteriaSection(JPanel parentCard) {
        JLabel searchCriteriaLabel = createStyledLabel("Tipo de Búsqueda", 16, true);
        searchCriteriaLabel.setBounds(0, 470, 200, 25);
        parentCard.add(searchCriteriaLabel);
        
        String[] searchOptions = {"Tienen TODAS", "Tienen AL MENOS UNA"};
        searchCriteriaComboBox = new JComboBox<>(searchOptions);
        searchCriteriaComboBox.setBounds(0, 500, 200, 30);
        searchCriteriaComboBox.setFont(new Font("SF Pro Text", Font.PLAIN, 13));
        searchCriteriaComboBox.setBackground(CARD_COLOR);
        parentCard.add(searchCriteriaComboBox);
        
        JButton executeSearchButton = createStyledButton("🔍 Buscar", 16);
        executeSearchButton.setBounds(210, 500, 138, 30);
        executeSearchButton.addActionListener(e -> performMultiplePropertySearch());
        parentCard.add(executeSearchButton);
    }
    
    /**
     * Sets up the search results section
     */
    private void setupSearchResultsSection(JPanel parentCard) {
        JLabel searchResultsLabel = createStyledLabel("Resultados de Búsqueda", 18, true);
        searchResultsLabel.setBounds(16, 16, 400, 30);
        parentCard.add(searchResultsLabel);
        
        setupTextArea(searchResultsDisplay, 13, false);
        JScrollPane searchResultsScrollPane = createScrollPane(searchResultsDisplay, 16, 56, 468, 588);
        parentCard.add(searchResultsScrollPane);
        
        displayInitialSearchInstructions();
    }
    
    /**
     * Sets up the taxonomy tree tab
     */
    private void setupTaxonomyTreeTab(JTabbedPane parentTabbedPane) {
        JPanel taxonomyTab = createModernPanel();
        parentTabbedPane.addTab("🌳 Árbol Taxonómico", taxonomyTab);
        
        JLabel taxonomyLabel = createStyledLabel("Taxonomía de Géneros Musicales", 20, true);
        taxonomyLabel.setBounds(40, 20, 400, 30);
        taxonomyLabel.setForeground(TEXT_COLOR);
        taxonomyTab.add(taxonomyLabel);
        
        JLabel taxonomyImageLabel = createTaxonomyImageLabel();
        JScrollPane taxonomyScrollPane = new JScrollPane(taxonomyImageLabel);
        taxonomyScrollPane.setBounds(20, 60, 880, 600);
        taxonomyScrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        taxonomyScrollPane.getViewport().setBackground(CARD_COLOR);
        taxonomyTab.add(taxonomyScrollPane);
    }
    
    // === EVENT HANDLERS ===
    
    /**
     * Handles genre selection in the genre list
     */
    public void handleGenreSelection(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) return;
        
        String selectedGenre = genreList.getSelectedValue();
        if (selectedGenre == null) return;
        
        displayGenreInformation(selectedGenre);
    }
    
    /**
     * Handles single property selection for simple search
     */
    public void handleSinglePropertySelection(ListSelectionEvent event) {
        if (event.getValueIsAdjusting()) return;
        
        String selectedProperty = availablePropertiesList.getSelectedValue();
        
        // Only show single property results if no multi-properties are selected
        if (selectedProperty == null || !selectedPropertiesModel.isEmpty()) {
            return;
        }
        
        performSinglePropertySearch(selectedProperty);
    }
    
    /**
     * Displays genre information including properties, description, and image
     */
    private void displayGenreInformation(String genreName) {
        StringBuilder propertiesText = new StringBuilder();
        
        // Get and display hierarchy
        if (!genreName.trim().equals("top")) {
            String[] parentGenres = musicGenreService.getGenreHierarchy(genreName);
            if (parentGenres != null) {
                for (String parentGenre : parentGenres) {
                    if (!parentGenre.trim().equals("top")) {
                        propertiesText.append("Es tipo de: ")
                                   .append(formatDisplayText(parentGenre.trim()))
                                   .append("\n");
                    }
                }
            }
        }
        
        // Get and display properties
        String[] genreProperties = musicGenreService.getGenreProperties(genreName);
        if (genreProperties != null) {
            for (String property : genreProperties) {
                propertiesText.append(property.trim()).append("\n");
            }
        }
        
        genrePropertiesDisplay.setText(propertiesText.toString());
        
        // Get and display description
        String genreDescription = musicGenreService.getGenreDescription(genreName);
        genreDescriptionDisplay.setText(genreDescription);
        
        // Load and display image
        loadGenreImage(genreName);
    }
    
    /**
     * Loads and displays the image for a genre
     */
    private void loadGenreImage(String genreName) {
        try {
            ImageIcon genreImage = new ImageIcon("images/generos/" + genreName + ".jpg");
            if (genreImage.getIconWidth() == -1) {
                // Try alternative path
                genreImage = new ImageIcon("images/" + genreName + ".jpg");
                if (genreImage.getIconWidth() == -1) {
                    displayImageNotAvailable(genreName);
                } else {
                    genreImageLabel.setIcon(genreImage);
                    genreImageLabel.setText("");
                }
            } else {
                genreImageLabel.setIcon(genreImage);
                genreImageLabel.setText("");
            }
        } catch (Exception e) {
            genreImageLabel.setIcon(null);
            genreImageLabel.setText("Error cargando imagen");
        }
    }
    
    /**
     * Displays a message when image is not available
     */
    private void displayImageNotAvailable(String genreName) {
        genreImageLabel.setIcon(null);
        genreImageLabel.setText("<html><div style='text-align: center; color: #9CA3AF;'>" +
                              "<p>Imagen no disponible</p>" +
                              "<p>" + formatDisplayText(genreName) + ".jpg</p>" +
                              "</div></html>");
    }
    
    /**
     * Performs a search for a single property
     */
    private void performSinglePropertySearch(String propertyName) {
        String[] matchingGenres = musicGenreService.findGenresWithProperty(propertyName);
        
        StringBuilder resultsText = new StringBuilder();
        resultsText.append("🔍 BÚSQUEDA SIMPLE\n");
        resultsText.append("═".repeat(30)).append("\n\n");
        
        String formattedProperty = formatPropertyText(propertyName);
        resultsText.append("📌 Propiedad: ").append(formattedProperty).append("\n\n");
        
        if (matchingGenres != null && matchingGenres.length > 0) {
            resultsText.append("🎵 Géneros encontrados:\n");
            resultsText.append("─".repeat(20)).append("\n");
            
            for (String genre : matchingGenres) {
                String formattedGenre = formatDisplayText(genre);
                resultsText.append("• ").append(formattedGenre).append("\n");
            }
            resultsText.append("\n✓ Total: ").append(matchingGenres.length).append(" géneros");
            
            double percentage = (matchingGenres.length * 100.0) / musicGenreService.getTotalGenreCount();
            resultsText.append(String.format("\n📊 Representa el %.1f%% del total", percentage));
            
        } else {
            resultsText.append("❌ No se encontraron géneros con esta propiedad.\n\n");
            resultsText.append("💡 Intente seleccionar otra propiedad o use búsqueda múltiple.");
        }
        
        searchResultsDisplay.setText(resultsText.toString());
    }
    
    // === PROPERTY MANAGEMENT METHODS ===
    
    /**
     * Adds the selected property to the search list
     */
    private void addSelectedProperty() {
        String selectedProperty = availablePropertiesList.getSelectedValue();
        
        if (selectedProperty != null && !selectedPropertiesModel.contains(selectedProperty)) {
            selectedPropertiesModel.addElement(selectedProperty);
            updateSearchResults();
        }
    }
    
    /**
     * Removes the selected property from the search list
     */
    private void removeSelectedProperty() {
        String selectedProperty = selectedPropertiesList.getSelectedValue();
        
        if (selectedProperty != null) {
            selectedPropertiesModel.removeElement(selectedProperty);
            updateSearchResults();
        }
    }
    
    /**
     * Clears all selected properties
     */
    private void clearAllSelectedProperties() {
        selectedPropertiesModel.clear();
        updateSearchResults();
    }
    
    /**
     * Updates the search results based on selected properties
     */
    private void updateSearchResults() {
        if (selectedPropertiesModel.isEmpty()) {
            displayInitialSearchInstructions();
        } else {
            performMultiplePropertySearch();
        }
    }
    
    /**
     * Performs a search with multiple properties
     */
    private void performMultiplePropertySearch() {
        if (selectedPropertiesModel.isEmpty()) {
            searchResultsDisplay.setText("Por favor, seleccione al menos una propiedad para buscar.");
            return;
        }
        
        // Convert model to array
        String[] selectedProperties = new String[selectedPropertiesModel.getSize()];
        for (int i = 0; i < selectedPropertiesModel.getSize(); i++) {
            selectedProperties[i] = selectedPropertiesModel.getElementAt(i);
        }
        
        String searchCriteria = getSelectedSearchCriteria();
        String[] searchResults = musicGenreService.findGenresWithMultipleProperties(selectedProperties, searchCriteria);
        
        displayMultiplePropertyResults(selectedProperties, searchCriteria, searchResults);
    }
    
    /**
     * Displays the results of a multiple property search
     */
    private void displayMultiplePropertyResults(String[] searchProperties, String searchCriteria, String[] results) {
        StringBuilder resultsText = new StringBuilder();
        
        resultsText.append("🔍 BÚSQUEDA AVANZADA\n");
        resultsText.append("═".repeat(50)).append("\n\n");
        
        resultsText.append("📋 Propiedades seleccionadas (").append(searchProperties.length).append("):\n");
        for (int i = 0; i < searchProperties.length; i++) {
            String formattedProperty = formatPropertyText(searchProperties[i]);
            resultsText.append(String.format("  %d. %s\n", i + 1, formattedProperty));
        }
        resultsText.append("\n");
        
        String criteriaDescription = searchCriteria.equals("todas") ? 
            "tienen TODAS las propiedades seleccionadas" : 
            "tienen AL MENOS UNA de las propiedades";
        String criteriaIcon = searchCriteria.equals("todas") ? "✅" : "🔸";
        
        resultsText.append(criteriaIcon).append(" Búsqueda: Géneros que ").append(criteriaDescription).append("\n\n");
        
        if (results != null && results.length > 0) {
            resultsText.append("🎵 GÉNEROS ENCONTRADOS:\n");
            resultsText.append("─".repeat(30)).append("\n");
            
            for (int i = 0; i < results.length; i++) {
                String formattedGenre = formatDisplayText(results[i]);
                resultsText.append(String.format("%2d. %s\n", i + 1, formattedGenre));
            }
            
            resultsText.append("\n").append("✓ Total encontrados: ").append(results.length).append(" géneros");
            
            if (searchCriteria.equals("todas") && searchProperties.length > 1) {
                double percentage = (results.length * 100.0) / musicGenreService.getTotalGenreCount();
                resultsText.append(String.format("\n📈 Representa el %.1f%% del total de géneros", percentage));
            }
            
        } else {
            resultsText.append("❌ NINGÚN RESULTADO\n");
            resultsText.append("─".repeat(20)).append("\n");
            resultsText.append("No se encontraron géneros que cumplan con estos criterios.\n\n");
            resultsText.append("💡 Sugerencias:\n");
            resultsText.append("  • Pruebe con menos propiedades\n");
            resultsText.append("  • Cambie a 'AL MENOS UNA' si usó 'TODAS'\n");
            resultsText.append("  • Verifique que las propiedades sean compatibles");
        }
        
        searchResultsDisplay.setText(resultsText.toString());
    }
    
    // === DATA LOADING METHODS ===
    
    /**
     * Loads genre data from the service
     */
    private void loadGenreData() {
        allGenres = musicGenreService.getAllMusicGenres();
        if (allGenres != null) {
            for (String genre : allGenres) {
                genreListModel.addElement(genre.trim());
            }
        }
        
        if (genreListModel.getSize() > 0) {
            genreList.setSelectedIndex(0);
        }
    }
    
    /**
     * Loads property data from the service
     */
    private void loadPropertyData() {
        allProperties = musicGenreService.getAllAvailableProperties();
        if (allProperties != null) {
            for (String property : allProperties) {
                availablePropertiesModel.addElement(property.trim());
            }
        }
    }
    
    // === UTILITY METHODS ===
    
    /**
     * Gets the selected search criteria from the combo box
     */
    private String getSelectedSearchCriteria() {
        int selectedIndex = searchCriteriaComboBox.getSelectedIndex();
        return selectedIndex == 0 ? "todas" : "alguna";
    }
    
    /**
     * Formats property text for display
     */
    private String formatPropertyText(String originalProperty) {
        if (originalProperty == null || originalProperty.trim().isEmpty()) {
            return originalProperty;
        }
        
        if (originalProperty.contains(":")) {
            return originalProperty.replace("_", " ");
        }
        
        String formatted = originalProperty.replace("_", " ");
        return capitalizeWords(formatted);
    }
    
    /**
     * Formats display text by replacing underscores and capitalizing
     */
    private String formatDisplayText(String text) {
        if (text == null) return "";
        
        String formatted = text.replace("_", " ");
        return capitalizeWords(formatted);
    }
    
    /**
     * Capitalizes the first letter of each word
     */
    private String capitalizeWords(String text) {
        if (text == null || text.isEmpty()) return text;
        
        String[] words = text.split("\\s+");
        StringBuilder result = new StringBuilder();
        
        for (int i = 0; i < words.length; i++) {
            if (i > 0) result.append(" ");
            
            if (words[i].length() > 0) {
                result.append(Character.toUpperCase(words[i].charAt(0)));
                if (words[i].length() > 1) {
                    result.append(words[i].substring(1).toLowerCase());
                }
            }
        }
        
        return result.toString();
    }
    
    /**
     * Displays initial search instructions
     */
    private void displayInitialSearchInstructions() {
        searchResultsDisplay.setText(
            "🎯 BÚSQUEDA POR PROPIEDADES\n" +
            "═".repeat(35) + "\n\n" +
            "📋 Instrucciones:\n" +
            "1. Seleccione propiedades de la lista izquierda\n" +
            "2. Haga clic en 'Agregar →' para añadirlas\n" +
            "3. Elija el tipo de búsqueda\n" +
            "4. Los resultados aparecerán automáticamente\n\n" +
            "💡 Puede seleccionar múltiples propiedades para\n" +
            "    búsquedas más específicas y detalladas."
        );
    }
    
    /**
     * Handles window close event
     */
    private void handleWindowClose() {
        int response = JOptionPane.showConfirmDialog(
            rootPane, 
            "¿Está seguro que desea salir?", 
            "Confirmar salida", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        if (response == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }
    
    // === UI COMPONENT CREATION METHODS ===
    
    private JPanel createModernPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND_COLOR);
        panel.setLayout(null);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));
        return panel;
    }
    
    private JPanel createCard(int x, int y, int width, int height) {
        JPanel card = new JPanel();
        card.setBounds(x, y, width, height);
        card.setBackground(CARD_COLOR);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_COLOR, 1),
            new EmptyBorder(16, 16, 16, 16)
        ));
        card.setLayout(null);
        return card;
    }
    
    private JLabel createStyledLabel(String text, int fontSize, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("SF Pro Display", bold ? Font.BOLD : Font.PLAIN, fontSize));
        label.setForeground(bold ? TEXT_COLOR : SECONDARY_TEXT_COLOR);
        return label;
    }
    
    private <T> JList<T> createStyledList(DefaultListModel<T> model) {
        JList<T> list = new JList<>(model);
        list.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        list.setBackground(CARD_COLOR);
        list.setSelectionBackground(PRIMARY_COLOR);
        list.setSelectionForeground(Color.WHITE);
        list.setBorder(new EmptyBorder(8, 8, 8, 8));
        return list;
    }
    
    private JList<String> createSelectedPropertiesList() {
        JList<String> list = new JList<>(selectedPropertiesModel);
        list.setFont(new Font("SF Pro Text", Font.PLAIN, 12));
        list.setBackground(new Color(248, 250, 252));
        list.setSelectionBackground(new Color(239, 68, 68));
        list.setSelectionForeground(Color.WHITE);
        list.setBorder(new EmptyBorder(8, 8, 8, 8));
        return list;
    }
    
    private JScrollPane createScrollPane(Component component, int x, int y, int width, int height) {
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setBounds(x, y, width, height);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        return scrollPane;
    }
    
    private void setupTextArea(JTextArea textArea, int fontSize, boolean editable) {
        textArea.setEditable(editable);
        textArea.setFont(new Font("SF Pro Text", Font.PLAIN, fontSize));
        textArea.setBackground(CARD_COLOR);
        textArea.setBorder(new EmptyBorder(8, 8, 8, 8));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
    }
    
    private JButton createStyledButton(String text, int fontSize) {
        JButton button = new JButton(text);
        button.setFont(new Font("SF Pro Text", Font.PLAIN, fontSize));
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(37, 99, 235));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });
        
        return button;
    }
    
    private JLabel createTaxonomyImageLabel() {
        JLabel taxonomyImage = new JLabel();
        try {
            ImageIcon taxonomyIcon = new ImageIcon("images/arbol_taxonomico.jpg");
            if (taxonomyIcon.getIconWidth() == -1) {
                taxonomyImage.setText("<html><div style='text-align: center; color: #9CA3AF; font-family: SF Pro Text;'>" +
                            "<h2>Árbol Taxonómico</h2>" +
                            "<p>Imagen no encontrada: images/arbol_taxonomico.jpg</p>" +
                            "<p>Por favor, coloque la imagen del árbol taxonómico en la carpeta images/</p>" +
                            "</div></html>");
                taxonomyImage.setHorizontalAlignment(JLabel.CENTER);
            } else {
                taxonomyImage.setIcon(taxonomyIcon);
            }
        } catch (Exception e) {
            taxonomyImage.setText("Error cargando imagen del árbol taxonómico");
            taxonomyImage.setHorizontalAlignment(JLabel.CENTER);
        }
        return taxonomyImage;
    }
}
