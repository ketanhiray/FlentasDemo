package com.flentas.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class CSVDataProvider {

	public static List<String[]> loadApplicants(String filePath) {
		List<String[]> data = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
			br.readLine(); // skip header
			String line;
			while ((line = br.readLine()) != null) {
				data.add(line.split(","));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	public static String[] getRandomApplicant(String filePath) {
		List<String[]> applicants = loadApplicants(filePath);
		Random rand = new Random();
		return applicants.get(rand.nextInt(applicants.size()));
	}
}
