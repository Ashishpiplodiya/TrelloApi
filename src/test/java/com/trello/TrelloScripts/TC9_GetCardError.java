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
public class TC9_GetCardError extends BaseTest {

	@Story("Get a Error Card")
	@Description("Get a Error Card from Trello Board")
	@DataProviderFileRowFilter(file = "./src/main/resources/data/Data.xlsx", sql = "Select * from TrelloData where SlNo ='7'")
	@Test(dataProvider = "data1", dataProviderClass = DataProviderFactory.class, description = "Description: Verify that whether user is able to get a card")
	public void getCard(String SlNo,String methodName,String endPointUrl,String fileName,String header,String methodAction,String queryParam,String pathParam,String boardName , String listName , String cardName,String expectedStatusCode) throws InterruptedException
	{
		/*Sending the GET Request*/
		Response response = Util.executeApi(methodName,endPointUrl,fileName,header,methodAction,queryParam,pathParam,boardName,listName,cardName,null);
		
		/*Validating Status Code*/
		Util.assertStatusCode(response, expectedStatusCode);
		
		/*Pass Message*/
		Util.logMessage("Giving Error Message as 404-Not Found");
		Assert.fail("Status code not matched");
	}
}
