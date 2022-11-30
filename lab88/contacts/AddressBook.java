package lab88.contacts;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.nio.file.*;
import java.nio.file.Paths;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;

/**
 * A class to maintain an arbitrary number of contact details.
 * Details are indexed by both name and phone number.
 * 
 * @author David J. Barnes and Michael Kölling.
 * @version 2016.02.29
 */
public class AddressBook
{
    // Storage for an arbitrary number of details.
    private TreeMap<String, ContactDetails> book;
    private int numberOfEntries;

    /**
     * Perform any initialization for the address book.
     */
    public AddressBook()
    {
        book = new TreeMap<>();
        numberOfEntries = 0;
    }

    /**
     * Look up a name or phone number and return the
     * corresponding contact details.
     * @param key The name or number to be looked up.
     * @return The details corresponding to the key.
     */
    public ContactDetails getDetails(String key)
    {
        return book.get(key);
    }

    /**
     * Return whether or not the current key is in use.
     * @param key The name or number to be looked up.
     * @return true if the key is in use, false otherwise.
     */
    public boolean keyInUse(String key)
    {
        return book.containsKey(key);
    }

    /**
     * Add a new set of details to the address book.
     * @param details The details to associate with the person.
     */
    public void addDetails(ContactDetails details)
    {
        if(details != null) {
            book.put(details.getName(), details);
            book.put(details.getPhone(), details);
            numberOfEntries++;
        }
    }

    /**
     * Change the details previously stored under the given key.
     * @param oldKey One of the keys used to store the details.
    This should be a key that is currently in use.
     * @param details The replacement details.
     */
    public void changeDetails(String oldKey,
    ContactDetails details)
    {
        if(keyInUse(oldKey) && details != null) {
            removeDetails(oldKey);
            addDetails(details);
        }
    }

    /**
     * Search for all details stored under a key that starts with
     * the given prefix.
     * @param keyPrefix The key prefix to search on. This may be
     *                  of zero length, but must not be null.
     * @return An array of those details that have been found.
     */
    public ContactDetails[] search(String keyPrefix)
    {
        // Build a list of the matches.
        List<ContactDetails> matches = new LinkedList<>();
        if(keyPrefix != null) {
            // Find keys that are equal-to or greater-than the prefix.
            SortedMap<String, ContactDetails> tail = book.tailMap(keyPrefix);
            Iterator<String> it = tail.keySet().iterator();
            // Stop when we find a mismatch.
            boolean endOfSearch = false;
            while(!endOfSearch && it.hasNext()) {
                String key = it.next();
                if(key.startsWith(keyPrefix)) {
                    matches.add(book.get(key));
                }
                else {
                    endOfSearch = true;
                }
            }
        }
        ContactDetails[] results = new ContactDetails[matches.size()];
        matches.toArray(results);
        return results;
    }

    /**
     * Return the number of entries currently in the
     * address book.
     * @return The number of entries.
     */
    public int getNumberOfEntries()
    {
        return numberOfEntries;
    }

    /**
     * Remove the entry with the given key from the address book.
     * The key should be one that is currently in use.
     * @param key One of the keys of the entry to be removed.
     */
    public void removeDetails(String key)
    {
        if(keyInUse(key)) {
            ContactDetails details = book.get(key);
            book.remove(details.getName());
            book.remove(details.getPhone());
            numberOfEntries--;
        }
    }

    /**
     * Return all the contact details, sorted according
     * to the sort order of the ContactDetails class.
     * @return A sorted list of the details.
     */
    public String listDetails()
    {
        // Because each entry is stored under two keys, it is
        // necessary to build a set of the ContactDetails. This
        // eliminates duplicates.
        StringBuilder allEntries = new StringBuilder();
        Set<ContactDetails> sortedDetails = new TreeSet<>(book.values());
        for(ContactDetails details : sortedDetails) {
            allEntries.append(details).append("\n\n");
        }
        return allEntries.toString();
    }

    /**
     * Return all the contact details, sorted according
     * to the sort order of the ContactDetails class.
     * @return A sorted list of the details.
     */
    public void loadFromFile(String filename) {
        Path p1 = Paths.get(filename);
        try (BufferedReader reader = Files.newBufferedReader(p1)) {
            String line1 = "";
            while( line1 != null ){    
                line1 = reader.readLine();
                while( line1 != null && line1.isEmpty() )
                    line1 = reader.readLine();
                if ( line1 == null ) break;
                String line2 = reader.readLine();
                String line3 = reader.readLine();
                addDetails(new ContactDetails(line1, line2, line3));
            }
        }
        catch(FileNotFoundException e) {
            System.err.println("Unable to open " + filename);
        }
        catch(IOException e) {
            System.err.println("Error occured reading " + filename);
        }

    }

    /**
     * Return all the contact details, sorted according
     * to the sort order of the ContactDetails class.
     * @return A sorted list of the details.
     */
    public void listToFile(String filename) throws IOException {
        Path p1 = Paths.get(filename);
        try(BufferedWriter writer = Files.newBufferedWriter(p1)){
            writer.write(listDetails());
            writer.close();
        }
        catch(FileNotFoundException e) {
            System.err.println("A problem was encountered writing " +
                filename);
        }
    }
}
