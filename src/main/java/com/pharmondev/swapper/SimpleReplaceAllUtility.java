package com.pharmondev.swapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SimpleReplaceAllUtility {

	private static Function<String, Swapper> mapToItem = (line) -> {
		String[] p = line.split(",");// a CSV has comma separated lines
		Swapper item = new Swapper();
		item.setOriginal(p[0]);// <-- this is the first column in the csv file
		item.setAcceptedValue(p[1]);
		// more initialization goes here
		return item;
	};

	public static void main(String[] args) {

		// Take first arg: Path to replace all

		// second arg: param file replace all

		// Take list of csv file paramName -> realParamName
		
//		System.out.println("Arg Size: "+args.length);
//		for(String arg: args) {
//			System.out.println("Arg:"+arg);
//		}
		String CSVFile = args[0];
		String scanDirectory = args[1];
		
		//
		File swapperFile = new File(CSVFile);
		File javaSourcePath = new File(scanDirectory);

		
		try {
			InputStream inputFS = new FileInputStream(swapperFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
			// skip the header of the csv
			final List<Swapper> inputList = br.lines().skip(1).map(mapToItem).collect(Collectors.toList());
			br.close();
			List<File> filesToProcess = new ArrayList<File>();
			//Populate Files
			recurseOnFile(javaSourcePath, filesToProcess);
			//Proceed to processing
			filesToProcess.parallelStream().forEach(file -> {
				replaceInFile(file, inputList);
			});
		} catch (IOException e) {
			e.printStackTrace();

		}
		
		
	}

	private static void recurseOnFile(File passedFile, List<File>filesToProcess) {
		if (passedFile.isFile()) {
			//replaceInFile(passedFile, swappers);
			filesToProcess.add(passedFile);
		} else if (passedFile.isDirectory()) {
			File[] listOfFiles = passedFile.listFiles();
			
			for (File inDir : listOfFiles) {
				recurseOnFile(inDir, filesToProcess);
			}
		}
	}

	private static void replaceInFile(File swapperFile, List<Swapper> swappers) {
		System.out.println("File Started: " + swapperFile.toString());

		try {
			Stream<String> lines = Files.lines(swapperFile.toPath());
			List<String> replaced = lines.map(line -> {
				for (Swapper swap : swappers) {
					//if(line.contains(swap.original)) {
						//System.out.println("Replaced: Original<" +swap.original+">" +"New Value<"+swap.acceptedValue+">");
					//}
					line = line.replaceAll(swap.original, swap.acceptedValue);
					//if(line.contains(swap.original)) {
						//System.out.println("Did not replace");
					//}
				}

				return line;
			}).collect(Collectors.toList());
			Files.write(swapperFile.toPath(), replaced, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
			lines.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("File Completed: " + swapperFile.toString());
	}

}
