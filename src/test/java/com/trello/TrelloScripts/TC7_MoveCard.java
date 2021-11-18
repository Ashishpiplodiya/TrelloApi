package com.trello.TrelloScripts;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.trello.baseutil.BaseTest;
import com.trello.dataproviders.DataProviderFactory;
import com.trello.dataproviders.DataProviderFileRowFilter;
import com.trello.util.Util;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Story;
import io.restassured.response.Response;

@Epic("Update")
public class TC7_MoveCard extends BaseTest{
		
		@Story("Move a Card")
		@Description("Move Card in Trello Board")
		@DataProviderFileRowFilter(file = "./src/main/resources/data/Data.xlsx", sql = "Select * from TrelloData where SlNo ='8'")
		@Test(dataProvider = "data1", dataProviderClass = DataProviderFactory.class, description = "Description: Verify that whether user is able to create a list")
		public void createCard(String SlNo,String methodName,String endPointUrl,String fileName,String header,String methodAction,String queryParam,String pathParam,String boardName,String listName,String cardName,String expectedStatusCode) throws InterruptedException
		{
			/*Sending the POST Request*/
			Response response = Util.executeApi(methodName,endPointUrl,fileName,header,methodAction,queryParam,pathParam,boardName,listName,cardName,null);
			
			/*Validating the Status Code*/
			Util.assertStatusCode(response, expectedStatusCode);
			
			/*Pass Message*/
			Util.logMessage("Card Moved Successfully");
		}
}
