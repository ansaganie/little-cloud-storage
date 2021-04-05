package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.page.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		this.driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (this.driver != null) {
			driver.quit();
		}
	}
	/**
	 * Tests that "/" and "/home" urls redirects to "/login" page and unauthorized user can only access
	 * "/login" and "/signup" pages
	 */
	@Test
	public void unauthorizedUserTest() {
		driver.get("http://localhost:" + this.port + "/home");
		assertEquals("Login", driver.getTitle());
		driver.get("http://localhost:" + this.port);
		assertEquals("Login", driver.getTitle());
		driver.get("http://localhost:" + this.port + "/signup");
		assertEquals("Sign Up", driver.getTitle());
		driver.get("http://localhost:" + this.port + "/login");
		assertEquals("Login", driver.getTitle());
	}

	/**
	 * Tests user signup, login, if "/home" page access is available after login,
	 * logout and if "/home" page access is restricted after logout
	 */

	@Test
	public void authorizedUserTest() {
		//signup new user
		driver.get("http://localhost:" + this.port);
		LoginPage loginPage = new LoginPage(driver);
		loginPage.clickSignupLink();
		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup("Ansagan", "Islyamgaliyev", "ansagan", "ansagan");
		String successfulSignUp = "You successfully signed up! Please continue to the login page.";
		assertEquals(successfulSignUp, signupPage.getSuccessMessage());

		//login and verify that the home page is accessible
		signupPage.clickLoginLink();
		loginPage.login("ansagan", "ansagan");
		assertEquals("Home", driver.getTitle());

		//sign out returns to Login page
		HomePage homePage = new HomePage(driver);
		homePage.logout();
		assertEquals("Login", driver.getTitle());

		//verify that the home page is no longer accessible
		driver.get("http://localhost:" + this.port + "/home");
		assertEquals("Login", driver.getTitle());
		driver.get("http://localhost:" + this.port);
		assertEquals("Login", driver.getTitle());
	}

	/**
	 * Note CRUD test
	 * A default user was created with the help of DataLoader.java class
	 */

	@Test
	public void notePageTest() {
		HomePage homePage = new HomePage(driver);
		NotePage notePage = new NotePage(driver);
		LoginPage loginPage = new LoginPage(driver);
		driver.get("http://localhost:" + this.port + "/login");
		loginPage.login("admin", "admin");
		homePage.navToNoteTab();

		//add new note
		String title = "note title";
		String description = "note description";
		notePage.clickAddNoteButton();
		notePage.submitNote(title, description);
		String noteTitlePrint = notePage.getNoteTitle();
		String noteDescriptionPrint = notePage.getNoteDescription();
		String noteSaveSuccessMsg = notePage.getNoteSaveSuccessMsg();
		assertEquals("Note was saved.", noteSaveSuccessMsg);
		assertEquals(title, noteTitlePrint);
		assertEquals(description, noteDescriptionPrint);


		//update note
		notePage.clickEditNoteButton();
		String editedTitle = "Testing";
		String editedDescription = "Your tech lead trusts you to do a good job, " +
				"but testing is important whether you're an excel number-cruncher or a full-stack coding superstar!";
		notePage.submitNote(editedTitle, editedDescription);
		String editedNoteTitlePrint = notePage.getNoteTitle();
		String editedNoteDescriptionPrint = notePage.getNoteDescription();
		String noteUpdateSuccessMsg = notePage.getNoteUpdateSuccessMsg();
		assertEquals("Note was updated.", noteUpdateSuccessMsg);
		assertEquals(title + editedTitle, editedNoteTitlePrint);
		assertEquals(description + editedDescription, editedNoteDescriptionPrint);

		//delete note
		notePage.clickDeleteNoteButton();
		String noNotes = notePage.getNoNotesMessage();
		String noteDeleteSuccessMsg = notePage.getNoteDeleteSuccessMsg();
		assertEquals("Note was deleted.", noteDeleteSuccessMsg);
		assertEquals("There is no notes currently, please add some...", noNotes);
	}

	/**
	 * Credential CRUD test
	 * A default user was created with the help of DataLoader.java class
	 */

	@Test
	public void credentialPageTest() {
		HomePage homePage = new HomePage(driver);
		CredentialPage credentialPage = new CredentialPage(driver);
		LoginPage loginPage = new LoginPage(driver);
		driver.get("http://localhost:" + this.port + "/login");
		loginPage.login("admin", "admin");
		homePage.navToCredentialTab();

		//add new note
		String url = "https://classroom.udacity.com";
		String username = "islamgaliev@mail.ru";
		String password = "WhyAlwaysMe202104";
		credentialPage.clickAddCredentialButton();
		credentialPage.submitCredential(url, username, password);
		String urlPrint = credentialPage.getUrlPrint();
		String usernamePrint = credentialPage.getUsernamePrint();
		String passwordPrint = credentialPage.getPasswordPrint();
		String credentialSaveSuccessMsg = credentialPage.getCredentialSaveSuccessMsg();
		assertEquals(url, urlPrint);
		assertEquals(username, usernamePrint);
		assertNotEquals(password, passwordPrint);
		assertEquals("Credential was saved.", credentialSaveSuccessMsg);

		//update
		credentialPage.clickEditButton();
		String passwordInputValue = credentialPage.getPasswordInput();
		assertEquals(password, passwordInputValue);
		String updatedUrl = url + "/me";
		String updatedUsername = "islamgaliev@gmail.com";
		String updatedPassword = "CallMeBaby202005";
		credentialPage.updateCredential(updatedUrl, updatedUsername, updatedPassword);
		urlPrint = credentialPage.getUrlPrint();
		usernamePrint = credentialPage.getUsernamePrint();
		passwordPrint = credentialPage.getPasswordPrint();
		String credentialUpdateSuccessMsg = credentialPage.getCredentialUpdateSuccessMsg();
		assertEquals(updatedUrl, urlPrint);
		assertEquals(updatedUsername, usernamePrint);
		assertNotEquals(updatedPassword, passwordPrint);
		assertEquals("Credential was updated.", credentialUpdateSuccessMsg);

		//delete
		credentialPage.clickDeleteButton();
		String noCredentialsMsg = credentialPage.getNoCredentialsMsg();
		String deleteSuccessMsg = credentialPage.getCredentialDeleteSuccessMsg();
		assertEquals("There is no credentials currently, please add some...", noCredentialsMsg);
		assertEquals("Credential was deleted.", deleteSuccessMsg);
	}

}
