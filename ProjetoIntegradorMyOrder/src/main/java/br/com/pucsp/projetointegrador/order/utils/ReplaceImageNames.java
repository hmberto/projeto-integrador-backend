package br.com.pucsp.projetointegrador.order.utils;

public class ReplaceImageNames {
	private ReplaceImageNames () {}
	
	public static String replaceNames(String name) {
		String name0 = name.toLowerCase();
		String name1 = name0.replace(" ", "");
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
		
		return name18.replace("ñ", "n");
	}
}