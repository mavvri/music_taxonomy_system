package zoologia;

import org.jpl7.Query;
import org.jpl7.Term;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * Service class that handles all business logic for music genre queries
 * and operations with the Prolog knowledge base.
 */
public class MusicGenreService {
    
    private Query currentQuery;
    
    /**
     * Loads a Prolog knowledge base file
     * @param fileName the name of the file to load
     * @return true if successful, false otherwise
     */
    public boolean loadKnowledgeBase(String fileName) {
        String consultQuery = "consult('" + "prolog/" + fileName + "')";
        try {
            currentQuery = new Query(consultQuery);
            boolean success = currentQuery.hasSolution();
            System.out.println(consultQuery + " " + (success ? "succeeded" : "failed"));
            return success;
        } catch (Exception e) {
            System.err.println("Error loading knowledge base: " + fileName);
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Retrieves all music genres from the knowledge base
     * @return array of genre names
     */
    public String[] getAllMusicGenres() {
        String query = "obtener_todos_los_generos(GenreList)";
        return executeListQuery(query, "GenreList");
    }
    
    /**
     * Gets all properties for a specific genre
     * @param genreName the name of the genre
     * @return array of formatted properties
     */
    public String[] getGenreProperties(String genreName) {
        String query = "propiedadesc(" + genreName + ", PropertyList)";
        return executeListQuery(query, "PropertyList");
    }
    
    /**
     * Gets the parent classes/genres for a specific genre
     * @param genreName the name of the genre
     * @return array of parent genre names
     */
    public String[] getGenreHierarchy(String genreName) {
        String query = "jerarquia_clase(" + genreName + ", HierarchyList)";
        return executeListQuery(query, "HierarchyList");
    }
    
    /**
     * Gets all available properties in the knowledge base
     * @return array of all property names
     */
    public String[] getAllAvailableProperties() {
        String query = "todas_propiedades(PropertyList)";
        return executeListQuery(query, "PropertyList");
    }
    
    /**
     * Finds all genres that have a specific property
     * @param propertyName the property to search for
     * @return array of genre names that have this property
     */
    public String[] findGenresWithProperty(String propertyName) {
        System.out.println("=== SEARCHING GENRES WITH PROPERTY ===");
        System.out.println("Property: " + propertyName);
        
        // Try standard query first
        String query = "tiene_propiedad(" + propertyName + ", GenreList)";
        String[] results = executeListQuery(query, "GenreList");
        
        // If no results, try formatted property search
        if (results.length == 0) {
            System.out.println("Standard search returned empty, trying formatted search...");
            query = "buscar_propiedad_formateada('" + propertyName + "', GenreList)";
            results = executeListQuery(query, "GenreList");
        }
        
        // If still no results, try manual search
        if (results.length == 0) {
            System.out.println("Formatted search returned empty, trying manual search...");
            results = performManualPropertySearch(propertyName);
        }
        
        System.out.println("Final result count: " + results.length);
        return results;
    }
    
    /**
     * Searches for genres that match multiple properties with specified criteria
     * @param selectedProperties array of properties to search for
     * @param searchCriteria either "todas" (all) or "alguna" (any)
     * @return array of matching genre names
     */
    public String[] findGenresWithMultipleProperties(String[] selectedProperties, String searchCriteria) {
        System.out.println("=== MULTI-PROPERTY SEARCH ===");
        System.out.println("Properties count: " + selectedProperties.length);
        System.out.println("Search criteria: " + searchCriteria);
        
        try {
            String[] allGenres = getAllMusicGenres();
            if (allGenres == null || allGenres.length == 0) {
                System.out.println("No genres found in database");
                return new String[0];
            }
            
            List<String> matchingGenres = new ArrayList<>();
            
            for (String currentGenre : allGenres) {
                int propertyMatches = 0;
                
                for (String currentProperty : selectedProperties) {
                    if (genreHasProperty(currentGenre, currentProperty)) {
                        propertyMatches++;
                    }
                }
                
                boolean shouldInclude = searchCriteria.equals("todas") ? 
                    (propertyMatches == selectedProperties.length) : (propertyMatches > 0);
                
                if (shouldInclude) {
                    matchingGenres.add(currentGenre);
                }
            }
            
            System.out.println("Search completed. Found " + matchingGenres.size() + " matching genres");
            return matchingGenres.toArray(new String[0]);
            
        } catch (Exception e) {
            System.err.println("Error in multi-property search: " + e.getMessage());
            e.printStackTrace();
            return new String[0];
        }
    }
    
    /**
     * Gets the description for a specific genre
     * @param genreName the name of the genre
     * @return the description text
     */
    public String getGenreDescription(String genreName) {
        try {
            String query = "obtiene_descripcion(" + genreName + ", Description)";
            currentQuery = new Query(query);
            if (currentQuery.hasSolution()) {
                Map<String, Term> solutions = currentQuery.nextSolution();
                String description = solutions.get("Description").toString();
                
                // Clean up formatting
                if (description.startsWith("'") && description.endsWith("'")) {
                    description = description.substring(1, description.length() - 1);
                }
                
                description = description.replace("_", " ");
                return description;
            }
        } catch (Exception e) {
            System.err.println("Error getting description for: " + genreName);
            e.printStackTrace();
        }
        return "";
    }
    
    /**
     * Executes a Prolog query that returns a list and parses the results
     * @param queryString the Prolog query to execute
     * @param variableName the variable name to extract from the result
     * @return array of string results
     */
    private String[] executeListQuery(String queryString, String variableName) {
        System.out.println("=== EXECUTING PROLOG QUERY ===");
        System.out.println("Query: " + queryString);
        System.out.println("Variable: " + variableName);
        
        try {
            currentQuery = new Query(queryString);
            System.out.println("Query created successfully");
            
            if (currentQuery.hasSolution()) {
                System.out.println("Query has solution");
                Map<String, Term> solutions = currentQuery.nextSolution();
                String responseString = solutions.get(variableName).toString();
                System.out.println("Raw response: " + responseString);
                
                return parseListResponse(responseString);
            } else {
                System.out.println("Query has no solution");
                return new String[0];
            }
        } catch (Exception e) {
            System.err.println("=== ERROR IN PROLOG QUERY ===");
            System.err.println("Query: " + queryString);
            System.err.println("Error type: " + e.getClass().getSimpleName());
            System.err.println("Error message: " + e.getMessage());
            e.printStackTrace();
            
            // Try alternative approach for property queries
            if (queryString.contains("tiene_propiedad")) {
                System.out.println("Attempting alternative property search...");
                return performAlternativePropertySearch(queryString);
            }
            
            return new String[0];
        }
    }
    
    /**
     * Parses a Prolog list response string into a Java string array
     * @param responseString the raw response from Prolog
     * @return parsed array of strings
     */
    private String[] parseListResponse(String responseString) {
        if (responseString.contains("[") && responseString.contains("]")) {
            String listContent = responseString.substring(
                responseString.indexOf("[") + 1, 
                responseString.lastIndexOf("]")
            );
            System.out.println("Extracted content: " + listContent);
            
            if (listContent.trim().isEmpty()) {
                System.out.println("Empty content - returning empty array");
                return new String[0];
            }
            
            String[] items = listContent.split(",");
            System.out.println("Split into " + items.length + " items");
            
            for (int i = 0; i < items.length; i++) {
                items[i] = items[i].trim();
                // Remove single quotes if present
                if (items[i].startsWith("'") && items[i].endsWith("'")) {
                    items[i] = items[i].substring(1, items[i].length() - 1);
                }
                System.out.println("  Item " + i + ": '" + items[i] + "'");
            }
            
            System.out.println("Successfully processed " + items.length + " items");
            return items;
        } else {
            System.out.println("Response doesn't contain brackets - returning single item");
            return new String[]{responseString};
        }
    }
    
    /**
     * Checks if a specific genre has a specific property
     * @param genreName the name of the genre
     * @param propertyName the property to check
     * @return true if the genre has the property, false otherwise
     */
    private boolean genreHasProperty(String genreName, String propertyName) {
        try {
            String[] genresWithProperty = findGenresWithProperty(propertyName);
            if (genresWithProperty != null) {
                for (String genreWithProperty : genresWithProperty) {
                    if (genreName.equals(genreWithProperty.trim())) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error checking property for genre " + genreName + ": " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Performs a manual search through all genres for a specific property
     * @param targetProperty the property to search for
     * @return array of genre names that have this property
     */
    private String[] performManualPropertySearch(String targetProperty) {
        System.out.println("=== MANUAL PROPERTY SEARCH ===");
        
        try {
            String[] allGenres = getAllMusicGenres();
            if (allGenres == null) {
                return new String[0];
            }
            
            List<String> matchingGenres = new ArrayList<>();
            
            for (String currentGenre : allGenres) {
                try {
                    String propertiesQuery = "frame(" + currentGenre + ", _, Properties, _)";
                    Query query = new Query(propertiesQuery);
                    
                    if (query.hasSolution()) {
                        Map<String, Term> solution = query.nextSolution();
                        Term propertiesTerm = solution.get("Properties");
                        
                        if (propertiesTerm != null) {
                            String propertiesString = propertiesTerm.toString();
                            
                            if (propertyMatchesInString(propertiesString, targetProperty)) {
                                matchingGenres.add(currentGenre);
                                System.out.println("Genre '" + currentGenre + "' contains property '" + targetProperty + "'");
                            }
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error checking properties for " + currentGenre + ": " + e.getMessage());
                }
            }
            
            System.out.println("Manual search found " + matchingGenres.size() + " genres");
            return matchingGenres.toArray(new String[0]);
            
        } catch (Exception e) {
            System.err.println("Error in manual property search: " + e.getMessage());
            return new String[0];
        }
    }
    
    /**
     * Checks if a property matches within a properties string
     * @param propertiesString the string containing all properties
     * @param targetProperty the property to look for
     * @return true if the property is found, false otherwise
     */
    private boolean propertyMatchesInString(String propertiesString, String targetProperty) {
        String normalizedProperty = targetProperty.toLowerCase().replace("'", "");
        String normalizedProperties = propertiesString.toLowerCase();
        
        // Direct match
        if (normalizedProperties.contains(normalizedProperty)) {
            return true;
        }
        
        // Try extracting key parts for compound properties
        if (normalizedProperty.contains("(") && normalizedProperty.contains(")")) {
            int startIndex = normalizedProperty.indexOf("(");
            int endIndex = normalizedProperty.indexOf(")");
            
            if (startIndex > 0 && endIndex > startIndex) {
                String propertyType = normalizedProperty.substring(0, startIndex);
                String propertyValue = normalizedProperty.substring(startIndex + 1, endIndex);
                
                // Check if both type and value are present
                if (normalizedProperties.contains(propertyType) && normalizedProperties.contains(propertyValue)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    /**
     * Performs an alternative property search when the main query fails
     * @param originalQuery the original query that failed
     * @return array of genre names
     */
    private String[] performAlternativePropertySearch(String originalQuery) {
        System.out.println("=== ALTERNATIVE PROPERTY SEARCH ===");
        
        try {
            // Extract property name from query
            if (originalQuery.contains("tiene_propiedad('") && originalQuery.contains("'")) {
                int startIndex = originalQuery.indexOf("tiene_propiedad('") + 17;
                int endIndex = originalQuery.indexOf("'", startIndex);
                String propertyName = originalQuery.substring(startIndex, endIndex);
                
                System.out.println("Extracted property: " + propertyName);
                
                String[] allGenres = getAllMusicGenres();
                if (allGenres == null) {
                    System.out.println("Could not get genres list");
                    return new String[0];
                }
                
                List<String> genresWithProperty = new ArrayList<>();
                
                for (String currentGenre : allGenres) {
                    try {
                        String genreQuery = "frame(" + currentGenre + ", _, Properties, _), member(" + propertyName + ", Properties)";
                        Query query = new Query(genreQuery);
                        if (query.hasSolution()) {
                            genresWithProperty.add(currentGenre);
                            System.out.println("Genre '" + currentGenre + "' has property '" + propertyName + "'");
                        }
                    } catch (Exception e) {
                        System.err.println("Error checking property for genre " + currentGenre + ": " + e.getMessage());
                    }
                }
                
                System.out.println("Alternative search found " + genresWithProperty.size() + " genres");
                return genresWithProperty.toArray(new String[0]);
            }
        } catch (Exception e) {
            System.err.println("Error in alternative property search: " + e.getMessage());
        }
        
        return new String[0];
    }
    
    /**
     * Gets the total count of genres in the knowledge base
     * @return the number of genres
     */
    public int getTotalGenreCount() {
        try {
            String[] allGenres = getAllMusicGenres();
            return allGenres != null ? allGenres.length : 0;
        } catch (Exception e) {
            System.err.println("Error getting total genre count");
            return 100; // fallback value
        }
    }
}
