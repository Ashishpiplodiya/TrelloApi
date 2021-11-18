package com.trello.TrelloScripts;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;

import com.trello.baseutil.BaseTest;
import com.trello.dataproviders.DataProviderFactory;
import com.trello.dataproviders.DataProviderFileRowFilter;
import com.trello.util.Util;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.restassured.response.Response;

/**
 * Description:Script to Verify that whether user is able to create a board.
 * 
 *
 */
@Epic("Create")
public class TC1_CreateBoard extends BaseTest {

	@Story("Create a Board")
	@Description("Create Board in Trello Board")
	@DataProviderFileRowFilter(file = "./src/main/resources/data/Data.xlsx", sql = "Select * from TrelloData where SlNo ='1'")
	@Test(dataProvider = "data1", dataProviderClass = DataProviderFactory.class, description = "Description: Verify that whether user is able to create a board")
	public void createBoard(String SlNo, String methodName, String endPointUrl, String fileName, String header,
			String methodAction, String queryParam, String pathParam ,String boardName , String listName , String cardName,String expectedStatusCode) throws FileNotFoundException, IOException, ParseException, InterruptedException {

		Thread.sleep(5000);
		/*Sending the POST Request*/
		Response response = Util.executeApi(methodName,endPointUrl,fileName,header,methodAction,queryParam,pathParam,boardName,listName,cardName,null);
		
		/*Validating Status Code*/
		Util.assertStatusCode(response, expectedStatusCode);
		
		/*Validating Board Name*/
		Util.validateText(response, boardName, prop.getProperty("name"));
		
		/*Validating Schema*/
		Util.schemaValidation(response,prop.getProperty("createBoard"));
	
		/*Pass Message*/
		Util.logMessage("Board Created Successfully");
	}
		

	

}
