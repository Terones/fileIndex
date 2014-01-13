import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;


public class FileIndex {
	final static String FILE_NAME = "AV1611Bible.txt";
	final static String OUTPUT_FILE_NAME = "C:\\Temp\\output.txt";
	final static Charset ENCODING = StandardCharsets.UTF_8;
	final static int WordsPerPage = 500;
	
	private HashMap<String, ArrayList<ArrayList<Integer>>> indexerList = new HashMap<String, ArrayList<ArrayList<Integer>>>();
	private HashMap<String, Integer> IndexCounter = new HashMap<>(); //convert later to treemap (reversed)
	private int wordCounter = 0;
	private int pageCounter = 0;
	/**
	 * @param args
	 * @throws IOException 
	 */

	public FileIndex(){
		
	}
	
	public void readFile(InputStream inputStream) throws IOException{
		indexerList = new HashMap<String, ArrayList<ArrayList<Integer>>>();
		IndexCounter = new HashMap<>(); //convert later to treemap (switched)
	    try (Scanner scanner =  new Scanner(inputStream, ENCODING.name())){
	      while (scanner.hasNextLine()){
	    	  //process each line in some way
	    	  //log(scanner.nextLine());
	    	  String line = scanner.nextLine().replaceAll("[^A-Za-z ]+", "").toLowerCase(Locale.ENGLISH).trim();
	    	  String[] parts = new String[1];
	    	  if ( line.length() > 0 && !(line.trim().length() == 0) ) { // line has characters;
	    		  if ( line.contains(" ") ) { // line has spaces
	    			  parts = line.split(" ");
	    		  }else{ // just one word
	    			  parts[0] = line;
	    		  }
	    		  
	    		  for( int i = 0; i <= parts.length - 1; i++)
	    		  {
	    			  if ( (wordCounter % WordsPerPage) == 0 ){
	    				  pageCounter++;
	    			  }
	    			  wordCounter++;
	    			  if ( indexerList.containsKey(parts[i]) ){
	    				  this.addToExistingKey(parts[i], pageCounter, wordCounter);
	    				  
	    			  }else{
	    				  this.createNewKey(parts[i], pageCounter, wordCounter);
	    			  } 
	    		  	}
	    	  	}
	      	}      
	    }
	}
	
//	public TreeMap<Integer, String> getWordCount(){
//		TreeMap<Integer, String>  returnMap = new TreeMap<Integer, String>(Collections.reverseOrder());
//		Iterator<?> it = IndexCounter.entrySet().iterator();
//	    while (it.hasNext()) {
//	        Map.Entry<String, Integer> pairs = (Map.Entry<String, Integer>)it.next();
//	        //System.out.println(pairs.getKey() + " = " + pairs.getValue());
//	        returnMap.put(pairs.getValue(), pairs.getKey());
//	    }
//	    return returnMap;
//		
//	}
	
	public LinkedHashMap<String, Integer> getWordCount() {
		   ArrayList<String> mapKeys = new ArrayList<String>(IndexCounter.keySet());
		   ArrayList<Integer> mapValues = new ArrayList<Integer>(IndexCounter.values());
		   Collections.sort(mapValues, Collections.reverseOrder());
		   Collections.sort(mapKeys);

		   LinkedHashMap<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();

		   Iterator<Integer> valueIt = mapValues.iterator();
		   while (valueIt.hasNext()) {
		       Integer val = valueIt.next();
		       Iterator<String> keyIt = mapKeys.iterator();

		       while (keyIt.hasNext()) {
		           String key = keyIt.next();
		           String comp1 = IndexCounter.get(key).toString();
		           String comp2 = val.toString();

		           if (comp1.equals(comp2)){
		               IndexCounter.remove(key);
		               mapKeys.remove(key);
		               sortedMap.put(key, val);
		               break;
		           }

		       }

		   }
		   return sortedMap;
		}
	
	public String getPagesByWord(String key){
		key = key.toLowerCase();
		if (!indexerList.containsKey(key) ) { throw new NullPointerException("key " + key + " not found"); }
		
		return indexerList.get(key).get(0).toString();
		
	}
	public String getOccurenceByWord(String key){
		key = key.toLowerCase();
		if (!indexerList.containsKey(key) ) { throw new NullPointerException("key " + key + " not found"); }
		
		return indexerList.get(key).get(1).toString();
		
	}
	
	private void addToExistingKey(String key, int page, int word) {
		if ( !indexerList.get(key).get(0).contains(page) ) {
			indexerList.get(key).get(0).add(page);
		}
		indexerList.get(key).get(1).add(word);
		
		IndexCounter.put(key, IndexCounter.get(key) + 1);
		
	}
	private void createNewKey(String key, int page, int word) {
		ArrayList<Integer> pageCollector = new ArrayList<>();
		pageCollector.add(page);
		ArrayList<Integer> wordCollector = new ArrayList<>();
		wordCollector.add(word);
		
		ArrayList<ArrayList<Integer>> arrayCollector = new ArrayList<>();
		arrayCollector.add(pageCollector);
		arrayCollector.add(wordCollector);
		
		indexerList.put(key, arrayCollector);
		
		IndexCounter.put(key, 1);
		
	}

	@SuppressWarnings("unused")
	private static void log(Object aMsg){
	    System.out.println(String.valueOf(aMsg));
	  }
	
	
	
	
	
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		FileIndex fileIndexer = new FileIndex();
		
		System.out.println("reading file, please wait");
		fileIndexer.readFile(FileIndex.class.getResourceAsStream(FILE_NAME));
		LinkedHashMap<String, Integer> wordCount = fileIndexer.getWordCount();
		for (Map.Entry<String, Integer> entry : wordCount.entrySet() ) {
		     System.out.println("Key: " + entry.getKey() + " Value: \"" + entry.getValue() + "\"");
		}
		System.out.println( "Jesus" );
		System.out.println( fileIndexer.getOccurenceByWord("Jesus"));
		System.out.println( fileIndexer.getPagesByWord("Jesus"));
		System.out.println( "God" );
		System.out.println( fileIndexer.getOccurenceByWord("God"));
		System.out.println( fileIndexer.getPagesByWord("God"));
		
		
		

	}

}
