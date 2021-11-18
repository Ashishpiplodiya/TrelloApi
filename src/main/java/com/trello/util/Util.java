package com.trello.util;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.testng.Reporter;

import com.trello.baseutil.BaseTest;

import io.qameta.allure.Step;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

/**
 * Description: All the action utilities added in this class e.g
 * 
 * 
 * @author : Sajal, Vikas
 */

public class Util extends BaseTest {

	public long ETO;

	public Util(long ETO) {
		this.ETO = ETO;

	}

	
	/**
	 * Description Method to provide info in the log,extent reports,testNg reports
	 * 
	 * @author Sajal, Vikas
	 * @param message
	 */
	public static void info(String message) {
		BaseTest.logger.info(message);

	}

	

	/**
	 * 
	 * Description Method for the Warning updation in extent Report,Log file,TestNG
	 * Report
	 * 
	 * @author Sajal, Vikas
	 * @param message
	 */

	public static void warn(String message) {

		BaseTest.logger.warn(message);
		Reporter.log(message);
	}

	
	/**
	 * 
	 * Description Method for the error updation in extent Report
	 * 
	 * @author Sajal, Vikas
	 * @param message
	 */
	public static void error(String message) {

		BaseTest.logger.error(message);
		Reporter.log(message);

	}

	
	/**
	 * 
	 * Description Method for fetching of get Current Date Time
	 * 
	 * @author Ashish piplodiya
	 * @param message
	 */
	@Step("{0}")
	public static String logMessage(String message) {
		return message;
	}

	/**
	 * 
	 * Description Method for fetching of get Current Date Time
	 * 
	 * @author Sajal, Vikas
	 * 
	 */

	public static String getCurrentDateTime() {
		DateFormat customFormat = new SimpleDateFormat("MM_dd_yyyy_HH_mm_ss");
		Date currentDate = new Date();
		return customFormat.format(currentDate);
	}

	/**
	 * 
	 * Description Method to delete the directory
	 * 
	 * @author Sajal, Vikas
	 * @param pathToDeleteFolder
	 */
	public static void deleteDir(String pathToDeleteFolder) {
		File extefolder = new File(pathToDeleteFolder);
		if ((extefolder.exists())) {
			deleteFolderDir(extefolder);
		}
	}

	/**
	 * 
	 * Description Method to delete folder directory
	 * 
	 * @author Sajal, Vikas
	 * @param folderToDelete
	 */
	public static void deleteFolderDir(File folderToDelete) {
		File[] folderContents = folderToDelete.listFiles();
		if (folderContents != null) {
			for (File folderfile : folderContents) {
				if (!Files.isSymbolicLink(folderfile.toPath())) {
					deleteFolderDir(folderfile);
				}
			}

		}
		folderToDelete.delete();
	}

	/**
	 * Description To create the duration of the Test Run
	 * 
	 * @author Sajal, Vikas
	 * @param millis
	 */
	public static String formatDuration(final long millis) {
		long seconds = (millis / 1000) % 60;
		long minutes = (millis / (1000 * 60)) % 60;
		long hours = millis / (1000 * 60 * 60);

		StringBuilder b = new StringBuilder();
		b.append(hours == 0 ? "00" : hours < 10 ? String.valueOf("0" + hours) : String.valueOf(hours));
		b.append(":");
		b.append(minutes == 0 ? "00" : minutes < 10 ? String.valueOf("0" + minutes) : String.valueOf(minutes));
		b.append(":");
		b.append(seconds == 0 ? "00" : seconds < 10 ? String.valueOf("0" + seconds) : String.valueOf(seconds));
		return b.toString();
	}

	/**
	 * Description Method to delete excel file from downloads
	 * 
	 * @author Sajal, Vikas
	 * @param downloadspath
	 */
	public synchronized void deleteXlFilesFromDownloads(String downloadspath) {
		// Delete all files from this directory
		String targetDirectory = downloadspath;
		File dir = new File(targetDirectory);

		// Filter out all log files
		String[] xlFiles = dir.list((d, s) -> {
			boolean fileName = s.startsWith("Claim") && s.endsWith(".xlsx") ? true : false;
			return fileName;
		});
		if (xlFiles.length >= 50) {
			for (String xlFile : xlFiles) {
				String tempXLFile = new StringBuffer(targetDirectory).append(File.separator).append(xlFile).toString();
				File fileDelete = new File(tempXLFile);
				boolean isdeleted = fileDelete.delete();

			}
		}
	}

	/**
	 * Description Method to set Token value
	 *
	 * @author Sajal, Vikas
	 * @param tokenValue
	 */
	public synchronized static void setTokenValue(String tokenValue) {
		// Creating a JSONObject object
		JSONObject jsonObject = new JSONObject();
		// Inserting key-value pairs into the json object
		jsonObject.put("token", tokenValue);
		try {
			FileWriter file = new FileWriter(System.getProperty("user.dir") + "./data/token.json");
			file.write(jsonObject.toJSONString());
			file.close();
			info(tokenValue + " written in token.json file");
		} catch (Exception e) {
			e.printStackTrace();
			error("Unable to write " + tokenValue + " in token.json file");
		}
	}

	/**
	 * Description Method to set Token value
	 *
	 * @author Sajal, Vikas
	 * @param id,path
	 */
	public synchronized static void setId(String id, String path) {
		// Creating a JSONObject object
		JSONObject jsonObject = new JSONObject();

		// Inserting key-value pairs into the json object
		jsonObject.put("id", id);
		try {
			FileWriter file = new FileWriter(System.getProperty("user.dir") + path);
			file.write(jsonObject.toJSONString());
			file.close();

			info(id + " written in .json file");
		} catch (Exception e) {
			e.printStackTrace();
			error("Unable to write " + id + " in json file");
		}
	}

	/**
	 * Description Method to get Token value
	 *
	 * @author Sajal, Vikas
	 * @param path
	 */
	public synchronized static String getId(String path) {
		// JSON parser object to parse read file
		JSONParser jsonParser = new JSONParser();
		String id = null;
		try {
			FileReader reader = new FileReader(System.getProperty("user.dir") + path);
			// Read JSON file
			Object obj = jsonParser.parse(reader);
			JSONObject jsonObject = (JSONObject) obj;
			id = (String) jsonObject.get("id");
			if (id.equals("")) {
				id = null;
				info("Token value not available in token.json file");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;

	}

	/**
	 * Description Method to get Token value
	 *
	 * @author Sajal, Vikas
	 */
	public synchronized static String getTokenValue() {
		// JSON parser object to parse read file
		JSONParser jsonParser = new JSONParser();
		String token = null;
		try {
			FileReader reader = new FileReader(System.getProperty("user.dir") + "./data/token.json");
			// Read JSON file
			Object obj = jsonParser.parse(reader);
			JSONObject jsonObject = (JSONObject) obj;
			token = (String) jsonObject.get("token");
			if (token.equals("")) {
				token = null;
				info("Token value not available in token.json file");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;

	}

	/**
	 * 
	 * Description Method to Fetch the value from Json file
	 * 
	 * @author Ashish,Gaurav
	 * @param response,expectedValue
	 */
	public static Object getJsonValue(String path) {
		JSONParser jsonParser = new JSONParser();
		Object environmentValue = null;
		try {
			FileReader reader = new FileReader(System.getProperty("user.dir") + path);
			// Read JSON file
			Object obj = jsonParser.parse(reader);
			JSONObject jsonObject = (JSONObject) obj;
			environmentValue = jsonObject.get("Environment");

			if (environmentValue.equals("")) {
				environmentValue = null;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return environmentValue;

	}

	
	/**
	 * 
	 * Description Method to Execute the API Request
	 * 
	 * @author Ashish
	 * @param methodName,endPointUrl,file,header,methodAction,queryParam,pathParam
	 */

	public synchronized static Response executeApi(String methodName, String endPointUrl, String file, String header,
			String methodAction, String queryParam, String pathParam, String boardName, String listName,
			String CardName,String path) {

		if (methodName.equalsIgnoreCase("get")) {
			return get(endPointUrl, file, header, methodAction, queryParam, pathParam, boardName, listName, CardName);

		}
		if (methodName.equalsIgnoreCase("create")) {
			return post(endPointUrl, file, header, methodAction, queryParam, pathParam, boardName, listName, CardName,path);

		}
		if (methodName.equalsIgnoreCase("update")) {
			return update(endPointUrl, file, header, methodAction, queryParam, pathParam, boardName, listName,
					CardName);

		}
		if (methodName.equalsIgnoreCase("delete")) {
			return delete(endPointUrl, file, header, methodAction, queryParam, pathParam, boardName, listName,
					CardName);

		}

		return null;

	}

	/**
	 * 
	 * Description Method to Add Headers
	 * 
	 * @author Ashish
	 * @param header
	 */

	public static void addHeaders(String header) throws FileNotFoundException, IOException, ParseException {
		try {

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(prop.getProperty("header")));
			String[] a = header.split(",");

			for (int i = 0; i < a.length; i++) {

				requestSpecBuilder.addHeader(a[i], (String) jsonObject.get(a[i]));

			}
			info("Header added to the request");

		} catch (Exception e) {
			error("Unable to add Headers");

			Assert.fail();
		}

	}

	/**
	 * 
	 * Description Method to Add Query Parameter
	 * 
	 * @author Ashish
	 * @param queryParam
	 */
	public static void addQueryParam(String queryParam) throws FileNotFoundException, IOException, ParseException {
		try {

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(prop.getProperty("queryParam")));
			String[] a = queryParam.split(",");

			for (int i = 0; i < a.length; i++) {
				if (a[i].equals("id")) {

					requestSpecBuilder.addQueryParam(a[i], jsonObject.get(a[i]));

				}

				else {
					requestSpecBuilder.addQueryParam(a[i], (String) jsonObject.get(a[i]));

				}
			}

			info("Query Parameter added to the Request");

		} catch (Exception e) {
			error("Unable to add Query Parameter");

			Assert.fail();
		}

	}

	/**
	 * 
	 * Description Method to Add Path Parameter
	 * 
	 * @author Ashish
	 * @param pathParam
	 */
	public static void addPathParam(String pathParam) throws FileNotFoundException, IOException, ParseException {
		try {

			JSONParser jsonParser = new JSONParser();
			JSONObject jsonObject = (JSONObject) jsonParser.parse(new FileReader(prop.getProperty("queryParam")));
			String[] a = pathParam.split(",");

			for (int i = 0; i < a.length; i++) {
				if (a[i].equals("id")) {

					requestSpecBuilder.addPathParam(a[i], jsonObject.get(a[i]));
				}

				else {
					requestSpecBuilder.addPathParam(a[i], (String) jsonObject.get(a[i]));
				}
			}
			info("Path Parameter added to the Request");

		} catch (Exception e) {

			error("Unable to add Path Parameter");

			Assert.fail();
		}

	}

	/**
	 * 
	 * Description Method to Post The Request
	 * 
	 * @author Manikandan A
	 * @param endPointUrl,file,header,methodAction,queryParam,pathParam
	 */
	private synchronized static Response post(String endPointUrl, String file, String header, String methodAction,
			String queryParam, String pathParam, String boardName, String listName, String cardName,String path) {
		Response response;
		try {

			if (methodAction.equalsIgnoreCase("createboard")) {

				try {
					addHeaders(header);
					addQueryParam(queryParam);

					requestSpecBuilder.addQueryParam("name", boardName);
					requestSpecification = requestSpecBuilder.build();

					response = given().spec(requestSpecification).post(endPointUrl).then().log().all().extract()
							.response();
					String id = response.path("id").toString();
					setId(id, prop.getProperty("boardId"));

					return response;

				} catch (Exception e) {
					error("Unable to Create board");

					Assert.fail();

				}

			} else if (methodAction.equalsIgnoreCase("createlist")) {
				try {

					addHeaders(header);
					requestSpecBuilder.addQueryParam("idBoard", getId(prop.getProperty("boardId")));
					addQueryParam(queryParam);

					requestSpecBuilder.addQueryParam("name", listName);
					requestSpecification = requestSpecBuilder.build();

					response = given().spec(requestSpecification).post(endPointUrl).then().log().all().extract()
							.response();

					String id = response.path("id").toString();
					setId(id, path);

					return response;

				} catch (Exception e) {

					error("Unable to Create list");

					Assert.fail();
				}

			} else if (methodAction.equalsIgnoreCase("createcard")) {

				try {

					addHeaders(header);
					requestSpecBuilder.addQueryParam("idList", getId(prop.getProperty("listId")));
					requestSpecBuilder.addQueryParam("name", cardName);

					addQueryParam(queryParam);

					requestSpecification = requestSpecBuilder.build();

					response = given().spec(requestSpecification).post(endPointUrl).then().log().all().extract()
							.response();

					String id = response.path("id").toString();
					setId(id, prop.getProperty("cardId"));

					return response;
				} catch (Exception e) {

					error("Unable to Create Card");
					Assert.fail();

				}
			}
			else if(methodAction.equalsIgnoreCase("moveCard")) {
				
				try {
					
				     
					addHeaders(header);
					requestSpecBuilder.addQueryParam("idBoard",getId(prop.getProperty("boardId")));
					requestSpecBuilder.addQueryParam("idList", getId(prop.getProperty("listId2")));
	
					
					addQueryParam(queryParam);
					
					
					requestSpecification = requestSpecBuilder.build();

					
					
					response=given().spec(requestSpecification).post(endPointUrl+""+getId(prop.getProperty("listId"))+"/moveAllCards").then().log().all().extract().response();

					
					return response;
				} catch (Exception e) {
					
					error("Unable to Move a Card");
					
					Assert.fail();
					
				}
		}
			
			
		} catch (Exception e) {

			error("Unable to send the POST Request");

			Assert.fail();

		}
		




		return null;

	}

	/**
	 * 
	 * Description Method to send the Get Request
	 * 
	 * @author Manikandan A
	 * @param endPointUrl,file,header,methodAction,queryParam,pathParam
	 */
	private synchronized static Response get(String endPointUrl, String file, String header, String methodAction,
			String queryParam, String pathParam, String boardName, String listName, String cardName) {
		Response response;
		try {
			if (methodAction.equalsIgnoreCase("getcard")) {

				try {

					addHeaders(header);

					addQueryParam(queryParam);

					requestSpecification = requestSpecBuilder.build();

					response = given().spec(requestSpecification)
							.get(endPointUrl + "" + getId(prop.getProperty("cardId"))).then().log().all().extract()
							.response();

					return response;
				} catch (Exception e) {

					error("Unable to fetch the Card");
					Assert.fail();
				}
			}
		}

		catch (Exception e) {

			error("Unable to Send the GET Request");

			Assert.fail();

		}
		return null;

	}

	/**
	 * 
	 * Description Method to send the update.
	 * 
	 * @author Manikandan A
	 * @param endPointUrl,file,header,methodAction,queryParam,pathParam
	 */
	private synchronized static Response update(String endPointUrl, String file, String header, String methodAction,
			String queryParam, String pathParam, String boardName, String listName, String cardName) {
		Response response;
		try {
			if (methodAction.equalsIgnoreCase("updatecard")) {

				try {

					addHeaders(header);

					addQueryParam(queryParam);

					requestSpecBuilder.addQueryParam("name", listName);

					requestSpecification = requestSpecBuilder.build();

					response = given().spec(requestSpecification)
							.put(endPointUrl + "" + getId(prop.getProperty("cardId"))).then().log().all().extract()
							.response();

					return response;
				} catch (Exception e) {

					error("Unable to update the Card using PUT Request");

					Assert.fail();
				}

			} else if (methodAction.equalsIgnoreCase("patch")) {

				try {
					requestSpecBuilder.addHeader("Authorization", "Bearer " + Util.getTokenValue());

					addHeaders(header);
					addPathParam(pathParam);
					addQueryParam(queryParam);

					requestSpecification = requestSpecBuilder.build();

					response = given().spec(requestSpecification).patch(endPointUrl).then().log().all().extract()
							.response();

					return response;
				} catch (Exception e) {

					error("Unable to update the Card using PATCH Request");

					Assert.fail();
				}
			}
		} catch (Exception e) {
			error("Unable to Send the Update Request");

			Assert.fail();

		}
		return null;

	}

	/**
	 * 
	 * Description Method to send the Delete Request
	 * 
	 * @author Manikandan A
	 * @param endPointUrl,file,header,methodAction,queryParam,pathParam
	 */
	private synchronized static Response delete(String endPointUrl, String file, String header, String methodAction,
			String queryParam, String pathParam, String boardName, String cardName, String listName) {
		Response response;
		try {
			try {

				addHeaders(header);

				addQueryParam(queryParam);

				requestSpecification = requestSpecBuilder.build();

				response = given().spec(requestSpecification).when()
						.delete(endPointUrl + "" + getId(prop.getProperty("cardId"))).then().log().all().extract()
						.response();

				return response;
			} catch (Exception e) {

				error("Unable to Delete the Card");

				Assert.fail();
			}

		} catch (Exception e) {

			error("Unable to Send the DELETE Request");


			Assert.fail();

		}
		return null;
	}

	/**
	 * 
	 * Description Method to Validate Status Code
	 * 
	 * @author Manikandan A
	 * @param response,expectedStatusCode
	 */
	public static void assertStatusCode(Response response, String expectedStatusCode) {
		int statusCode = 0;
		int actualValue = 0;
		try {
			statusCode = Integer.parseInt(expectedStatusCode);
			actualValue = response.getStatusCode();

			assertThat(actualValue, equalTo(statusCode));
			logMessage("Expected Status Code: " + statusCode + " Matched with Actual Status Code: " + actualValue);
			info("Expected Status Code: " + statusCode + " Matched with Actual Status Code: " + actualValue);

		} catch (Exception e) {
			error("Expected Status Code: " + statusCode + " not matched with Actual Status Code: " + actualValue);
			Assert.fail();
		}
	}

	/**
	 * 
	 * Description Method to Validate Content
	 * 
	 * @author Manikandan A
	 * @param response,expectedStatusCode
	 */
	public static void validateText(Response response, String expectedValue, String value) {
		String actualValue = null;
		try {

			JsonPath js = response.jsonPath();
			actualValue = js.get(value);

			assertThat(actualValue, is(expectedValue));
			logMessage("Expected Value: " + expectedValue + " Matched with Actual Value: " + actualValue);

		} catch (Exception e) {
			error("Expected Value: " + expectedValue + " Matched with Actual Value: " + actualValue);
			Assert.fail();
		}

	}

	/**
	 * 
	 * Description Method to Validate Status Code
	 * 
	 * @author Manikandan A
	 * @param response,expectedValue
	 */
	public static void assertIsEmpty(Response response) {

		try {

			assertThat(prop.getProperty("limit"), is(empty()) != null);

		} catch (Exception e) {
			Assert.fail();
		}

	}

	/**
	 * 
	 * Description Method to Validate Status Code
	 * 
	 * @author Manikandan A
	 * @param response,expectedValue
	 */
	public static void schemaValidation(Response response, String type) {

		try {

			response.then().assertThat().body(matchesJsonSchemaInClasspath(type));

		} catch (Exception e) {
			Assert.fail();
		}

	}

}
