package com.trello.TrelloScripts;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.trello.baseutil.BaseTest;
import com.trello.dataproviders.DataProviderFactory;
import com.trello.dataproviders.DataProviderFileRowFilter;
import com.trello.util.Util;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/**
 * Description:Script to Verify that whether user is able to get a card
 * 
 *
 */
@Epic("Read")
public class TC5_GetCard extends BaseTest {

	@Story("Get a Card")
	@Description("Get Card from Trello Board")
	@DataProviderFileRowFilter(file = "./src/main/resources/data/Data.xlsx", sql = "Select * from TrelloData where SlNo ='4'")
	@Test(dataProvider = "data1", dataProviderClass = DataProviderFactory.class, description = "Description: Verify that whether user is able to get a card")
	public void getCard(String SlNo,String methodName,String endPointUrl,String fileName,String header,String methodAction,String queryParam,String pathParam,String boardName , String listName , String cardName,String expectedStatusCode) throws InterruptedException
	{
		/*Sending the GET Request*/
		Response response = Util.executeApi(methodName,endPointUrl,fileName,header,methodAction,queryParam,pathParam,boardName,listName,cardName,null);
		
		/*Validating Status Code*/
		Util.assertStatusCode(response, expectedStatusCode);
		
		/*Validating Card Name*/
		Util.validateText(response, cardName,prop.getProperty("name"));
		
		/*Validating Board Id*/
		Util.validateText(response, Util.getId(prop.getProperty("boardId")),prop.getProperty("actualBoardId"));
		
		/*Validating List Id*/
		Util.validateText(response, Util.getId(prop.getProperty("listId")),prop.getProperty("actualListId"));
		
		/*Validating Casrd Id*/
		Util.validateText(response, Util.getId(prop.getProperty("cardId")),prop.getProperty("actualCardId"));
		
		/*Validating Schema*/
		Util.schemaValidation(response, prop.getProperty("getCard"));
	
		
		/*Pass Message*/
		Util.logMessage("Card Fetched Successfully");
	}
}
