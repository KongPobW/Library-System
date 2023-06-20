-- -----------------------------------------------------
-- Schema library
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema library
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `library`;
USE `library`;

-- -----------------------------------------------------
-- Table `library`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`user` (
  `UserID` INT NOT NULL AUTO_INCREMENT,
  `Password` VARCHAR(45) NOT NULL,
  `Email` VARCHAR(255) NOT NULL UNIQUE,
  PRIMARY KEY (`UserID`)
  );


-- -----------------------------------------------------
-- Table `library`.`user_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`user_details` (
  `Email` VARCHAR(255) NOT NULL,
  `FirstName` VARCHAR(45) NOT NULL,
  `LastName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`Email`),
  CONSTRAINT `FK_user_details_Email`
    FOREIGN KEY (`Email`)
    REFERENCES `library`.`user` (`Email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `library`.`publisher`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`publisher` (
  `PublishID` INT NOT NULL AUTO_INCREMENT,
  `pName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`PublishID`)
  );


-- -----------------------------------------------------
-- Table `library`.`author`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`author` (
  `AuthorID` INT NOT NULL AUTO_INCREMENT,
  `aName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`AuthorID`)
  );


-- -----------------------------------------------------
-- Table `library`.`book`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`book` (
  `BookID` INT NOT NULL AUTO_INCREMENT,
  `ISBN` VARCHAR(13) NOT NULL UNIQUE,
  `PublishID` INT NOT NULL,
  `AuthorID` INT NOT NULL,
  PRIMARY KEY (`BookID`),
  CONSTRAINT `FK_book_PublishID`
    FOREIGN KEY (`PublishID`)
    REFERENCES `library`.`publisher` (`PublishID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_book_AuthorID`
    FOREIGN KEY (`AuthorID`)
    REFERENCES `library`.`author` (`AuthorID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `library`.`borrows`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`borrows` (
  `BorrowID` INT NOT NULL AUTO_INCREMENT,
  `BorrowedBookID` INT NOT NULL,
  `BorrowerUserID` INT NOT NULL,
  PRIMARY KEY (`BorrowID`),
  CONSTRAINT `FK_borrows_UserID`
    FOREIGN KEY (`BorrowerUserID`)
    REFERENCES `library`.`user` (`UserID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_borrows_BookID`
    FOREIGN KEY (`BorrowedBookID`)
    REFERENCES `library`.`book` (`BookID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `library`.`borrow_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`borrow_details` (
  `BorrowID` INT NOT NULL,
  `BorrowedDate` DATETIME NOT NULL,
  `BorrowEndDate` DATETIME NOT NULL,
  PRIMARY KEY (`BorrowID`),
  CONSTRAINT `FK_borrow_details_BorrowID`
    FOREIGN KEY (`BorrowID`)
    REFERENCES `library`.`borrows` (`BorrowID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


-- -----------------------------------------------------
-- Table `library`.`book_details`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `library`.`book_details` (
  `ISBN` VARCHAR(13) NOT NULL,
  `Title` VARCHAR(45) NOT NULL,
  `Description` MEDIUMTEXT NOT NULL,
  `Category` VARCHAR(45) NOT NULL,
  `Language` VARCHAR(45) NOT NULL,
  `PublishDate` DATETIME NOT NULL,
  `Length` INT NOT NULL,
  PRIMARY KEY (`ISBN`),
  CONSTRAINT `FK_book_details_ISBN`
    FOREIGN KEY (`ISBN`)
    REFERENCES `library`.`book` (`ISBN`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);