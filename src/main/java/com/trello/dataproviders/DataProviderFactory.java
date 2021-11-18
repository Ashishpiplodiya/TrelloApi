package com.trello.dataproviders;

import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.annotations.DataProvider;

import com.codoid.products.exception.FilloException;
import com.trello.commonutils.ExcelUtil;

/**
 * Description: This class implements data provider logic which fetches the data
 * from the Excel sheet.
 * 
 * @author Sajal, Vikas
 */
public class DataProviderFactory {

	@DataProvider(name = "data1")
	public static Object[][] getFilterRowData(Method method) throws IOException, FilloException {
		DataProviderFileRowFilter file = method.getAnnotation(DataProviderFileRowFilter.class);

		String filepath = System.getProperty("user.dir") + file.file();

		String filterData[][] = ExcelUtil.getRowDataFromExcelUsingFillo(filepath, file.sql());

		return filterData;
	}

}
