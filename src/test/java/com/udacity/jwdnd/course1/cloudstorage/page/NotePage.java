package com.udacity.jwdnd.course1.cloudstorage.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NotePage {

    @FindBy(id = "add-note-button")
    private WebElement addNoteButton;

    @FindBy(id = "note-title")
    private WebElement noteTitleInput;

    @FindBy(id = "note-description")
    private WebElement noteDescriptionInput;

    @FindBy(id = "noteSubmit")
    private WebElement noteSubmitButton;

    @FindBy(className = "note-title-print")
    private WebElement noteTitlePrint;

    @FindBy(className = "note-description-print")
    private WebElement noteDescriptionPrint;

    @FindBy(className = "note-edit-button")
    private WebElement noteEditButton;

    @FindBy(className = "note-delete-button")
    private WebElement noteDeleteButton;

    @FindBy(id = "no-notes")
    private WebElement noNotes;

    @FindBy(id = "note-save-success-msg")
    private WebElement noteSaveSuccessMsg;

    @FindBy(id = "note-update-success-msg")
    private WebElement noteUpdateSuccessMsg;

    @FindBy(id = "note-delete-success-msg")
    private WebElement noteDeleteSuccessMsg;

    public NotePage(WebDriver driver) {
        PageFactory.initElements(driver, this);
    }

    public void submitNote(String title, String description) {
        noteTitleInput.sendKeys(title);
        noteDescriptionInput.sendKeys(description);
        noteSubmitButton.submit();
    }

    public String getNoteTitle() {
        return noteTitlePrint.getText();
    }

    public String getNoteDescription() {
        return noteDescriptionPrint.getText();
    }

    public void clickAddNoteButton() {
        addNoteButton.click();
    }

    public void clickEditNoteButton() {
        noteEditButton.click();
    }

    public void clickDeleteNoteButton() {
        noteDeleteButton.click();
    }

    public String getNoNotesMessage() {
        return noNotes.getText();
    }

    public String getNoteSaveSuccessMsg() {
        return noteSaveSuccessMsg.getText();
    }

    public String getNoteUpdateSuccessMsg() {
        return noteUpdateSuccessMsg.getText();
    }

    public String getNoteDeleteSuccessMsg() {
        return noteDeleteSuccessMsg.getText();
    }
}
