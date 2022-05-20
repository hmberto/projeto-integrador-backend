package br.com.pucsp.projetointegrador.order.utils;

public class ReplaceImageNames {
	public static String replaceNames(String name) {
//		String a = "[ \\s]+";
//		name.matches("");
		
		String name1 = name.replace(" ", "");
		String name2 = name1.replace("-", "");
		String name3 = name2.replace(".", "");
		
		String name4 = name3.replace("á", "a");
		String name5 = name4.replace("à", "a");
		String name6 = name5.replace("â", "a");
		String name7 = name6.replace("ã", "a");
		String name8 = name7.replace("é", "e");
		String name9 = name8.replace("è", "e");
		String name10 = name9.replace("ê", "e");
		String name11 = name10.replace("í", "i");
		String name12 = name11.replace("ï", "i");
		String name13 = name12.replace("ó", "o");
		String name14 = name13.replace("ô", "o");
		String name15 = name14.replace("õ", "o");
		String name16 = name15.replace("ö", "o");
		String name17 = name16.replace("ú", "u");
		String name18 = name17.replace("ç", "c");
		String name19 = name18.replace("ñ", "n");
		
//		String name20 = name19.replace("Á", "A");
//		String name21 = name20.replace("À", "A");
//		String name22 = name21.replace("Â", "A");
//		String name23 = name22.replace("Ã", "A");
//		String name24 = name23.replace("É", "E");
//		String name25 = name24.replace("È", "E");
//		String name26 = name25.replace("Ê", "E");
//		String name27 = name26.replace("Í", "I");
//		String name28 = name27.replace("Ï", "I");
//		String name29 = name28.replace("Ó", "O");
//		String name30 = name29.replace("Ô", "O");
//		String name31 = name30.replace("Õ", "O");
//		String name32 = name31.replace("Ö", "O");
//		String name33 = name32.replace("Ú", "U");
//		String name34 = name33.replace("Ç", "C");
//		String name35 = name34.replace("Ñ", "N");
		
		return name19;
	}
}