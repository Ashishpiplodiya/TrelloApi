package com.trello.TrelloScripts;

import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;

import static org.hamcrest.MatcherAssert.assertThat;
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
 * Description:Script to Script to Verify that whether user is able to delete a card
 * 
 *
 */
@Epic("Delete")
public class TC8_DeleteCard extends BaseTest {
	
	@Story("Delete a Card")
	@Description("Delete Card in Trello Board")
	@DataProviderFileRowFilter(file = "./src/main/resources/data/Data.xlsx", sql = "Select * from TrelloData where SlNo ='6'")
	@Test(dataProvider = "data1", dataProviderClass = DataProviderFactory.class, description = "Description: Script to Verify that whether user is able to delete a card")
	public void deleteCard(String SlNo,String methodName,String endPointUrl,String fileName,String header,String methodAction,String queryParam,String pathParam,String boardName , String listName , String cardName,String expectedStatusCode) throws InterruptedException
	{

		
		/*Sending the PUT Request*/
		Response response = Util.executeApi(methodName,endPointUrl,fileName,header,methodAction,queryParam,pathParam,boardName,listName,cardName,null);
		
		/*Validating Status Code*/
		Util.assertStatusCode(response, expectedStatusCode);
		
		/*Validating Response*/
		Util.assertIsEmpty(response);
		
		/*Validating Schema*/
		Util.schemaValidation(response, prop.getProperty("deleteCard"));
		
		/*Pass Message*/
		Util.logMessage("Card Deleted Successfully");
		
	}

}
