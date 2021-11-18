package com.trello.TrelloScripts;

import org.testng.annotations.Test;

import com.trello.baseutil.BaseTest;
import com.trello.dataproviders.DataProviderFactory;
import com.trello.dataproviders.DataProviderFileRowFilter;
import com.trello.util.Util;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.restassured.response.Response;

/**
 * Description:Script to Verify that whether user is able to create a list.
 * 
 *
 */
@Epic("Create")
public class TC3_CreateAnotherList extends BaseTest{
	
		@Story("Create a Another List")
		@Description("Create List in Trello Board")
		@DataProviderFileRowFilter(file = "./src/main/resources/data/Data.xlsx", sql = "Select * from TrelloData where SlNo ='9'")
		@Test(dataProvider = "data1", dataProviderClass = DataProviderFactory.class, description = "Description: Verify that whether user is able to create a list")
		public void createList(String SlNo,String methodName,String endPointUrl,String fileName,String header,String methodAction,String queryParam,String pathParam,String boardName ,
								String listName , String cardName,String expectedStatusCode) throws InterruptedException
		{
			
			
			/*Sending the POST Request*/
			Response response = Util.executeApi(methodName,endPointUrl,fileName,header,methodAction,queryParam,pathParam,boardName,listName,cardName,prop.getProperty("listId2"));
		
			/*Validating Status Code*/
			Util.assertStatusCode(response, expectedStatusCode);
			
			/*Validating List Name*/
			Util.validateText(response, listName, prop.getProperty("name"));
			
			/*Validating Board Id*/
			Util.validateText(response, Util.getId(prop.getProperty("boardId")), prop.getProperty("actualBoardId"));
			
			/*Validating Schema*/
			Util.schemaValidation(response, prop.getProperty("createList"));
		
			/*Pass Message*/
			Util.logMessage("Another List Created Successfully");
		
		}

}