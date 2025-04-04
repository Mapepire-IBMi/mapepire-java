package io.github.mapepire_ibmi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

import io.github.mapepire_ibmi.types.ColumnMetadata;
import io.github.mapepire_ibmi.types.DaemonServer;
import io.github.mapepire_ibmi.types.JDBCOptions;
import io.github.mapepire_ibmi.types.QueryMetadata;
import io.github.mapepire_ibmi.types.QueryResult;

public class InteractiveClient {

	private static DaemonServer daemonServer;
    private static SqlJob job; 
	public static void main(String[] args) {

		System.out.println("Running interactive MapepireClient");
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
			String line = input.readLine();
			boolean running = true;
			while (line != null && running) {
				running = processLine(line);
				line = input.readLine();
			}

		} catch (Exception e) {
			System.out.println("Severe failure");
			e.printStackTrace(System.out);
		}
	}

	private static boolean processLine(String line) {

		try { 
		String[] lineElements = splitLine(line);
		if (lineElements.length > 0) {
			if ("connect".equalsIgnoreCase(lineElements[0])) {
				connect(lineElements, line);
			} else if ("runQuery".equalsIgnoreCase(lineElements[0])) {
				runQuery(line);
			} else if ("help".equalsIgnoreCase(lineElements[0])) {
				processHelp(); 
			} else if ("exit".equalsIgnoreCase(lineElements[0])) {
				return false;
			} else {
				System.out.println("Did not recognize command: " + line);
				processHelp();
			}
		}
		} catch (Exception e) { 
			System.out.println("Error processing "+line); 
			e.printStackTrace(System.out); 
		}
		return true;
	}

	private static void runQuery(String line) throws  Exception {
		int runQueryIndex = line.toUpperCase().indexOf("RUNQUERY"); 
		line = line.substring(runQueryIndex+8);
		// Initialize and execute query
		Query query = job.query(line);
		QueryResult<Object> result = query.execute().get();

		String errorString = result.getError();
		if (errorString != null) { 
			System.out.println("Query returned errorString:"+errorString); 
		} else { 
			QueryMetadata metadata = result.getMetadata(); 
			List<ColumnMetadata> columns = metadata.getColumns();
			columns.forEach(col->System.out.print(col.getName()+" ")); 
			System.out.println(); 
			List<Object> data = result.getData();
			ListIterator<Object> it = data.listIterator(); 
			while (it.hasNext()) { 
				HashMap hashmap = (HashMap) it.next(); 
				columns.forEach(col->System.out.print(hashmap.get(col.getName())+" ")); 
				System.out.println(); 
			}
		}
		// Close query and job
		query.close().get();

		
		
	}

	private static void processHelp() {
		System.out.println("Possible commands"); 
		System.out.println("connect host port user password validateCA [CA|NONE] [\"jdbcOption1=value1[;jdbcOption=value]*\"]");
		System.out.println("runQuery QUERY");
		System.out.println("help"); 
		System.out.println("exit"); 
		
	}

	private static void connect(String[] lineElements, String line) throws Exception {

		int elementCount = lineElements.length;
		String host = null;
		int port = 0;
		String user = null;
		String password = null;
		boolean rejectUnauthorized = true;
		String ca = null;
		String stringOptions;
		Properties jdbcProperties = null; 

		if (elementCount > 1)
			host = lineElements[1];
		if (elementCount > 2)
			port = Integer.parseInt(lineElements[2]);
		if (elementCount > 3)
			user = lineElements[3];
		if (elementCount > 4)
			password = lineElements[4];
		if (elementCount > 5)
			rejectUnauthorized = Boolean.parseBoolean(lineElements[5]);
		if (elementCount > 6)
			ca = lineElements[6];
		if (elementCount > 7) {
			stringOptions = lineElements[7]; 
			if (stringOptions.charAt(0) == '"') { 
				String[] quoteSplit = line.split("\""); 
				stringOptions = quoteSplit[1]; 
			}
			line.replace(';','\n'); 
			jdbcProperties = new Properties(); 
			StringReader reader = new StringReader(stringOptions); 
			jdbcProperties.load(reader); 
		}
		
		DaemonServer newDaemonServer = null;
		String successString; 
		if (elementCount <= 5) {
			newDaemonServer = new DaemonServer(host, port, user, password);
			successString  = "connection created using (" + host + "," + port + "," + user + ",*******)";
		} else if (elementCount == 6 || "NONE".equals(ca) ) {
			newDaemonServer = new DaemonServer(host, port, user, password, rejectUnauthorized, null);
			successString  = "connection created using (" + host + "," + port + "," + user + ",*******,"
					+ rejectUnauthorized + ")";
		} else {
			newDaemonServer = new DaemonServer(host, port, user, password, rejectUnauthorized, ca);
			successString  = "connection created using (" + host + "," + port + "," + user + ",*******,"
					+ rejectUnauthorized + "," + ca + ")";
		}
		daemonServer = newDaemonServer;
		SqlJob newJob ; 
		if (jdbcProperties == null) { 
		    newJob = new SqlJob(); 
		} else { 
			JDBCOptions jdbcOptions = new JDBCOptions(jdbcProperties); 
			newJob = new SqlJob(jdbcOptions); 
		}
		newJob.connect(daemonServer).get();
		
		if (job != null) { 
			job.close(); 
		}
		job = newJob; 
		System.out.println(successString); 

	}

	private static String[] splitLine(String line) {
		// For now, keep this simple.
		// If the future, more complex analysis may be needed.
		return line.split(" +");
	}

}
