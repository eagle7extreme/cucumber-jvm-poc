package org.diveintojee.poc.cucumberjvm.business;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.diveintojee.poc.cucumberjvm.TestUtils;
import org.diveintojee.poc.cucumberjvm.domain.Classified;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import static org.junit.Assert.assertEquals;

/**
 * User: lgueye Date: 15/02/12 Time: 17:15
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:cucumber-jvm-poc.xml"})
public class ValidationTestIT {

  @Autowired
  @Qualifier("validator")
  private Validator underTest;

  private Classified classified;

  @Before
  public final void before() {
    classified = TestUtils.validClassified();
  }

  @Test
  public void validationShouldBeLocaleAware() {

    // Variables
    String errorMessage;
    String propertyPath;
    Set<ConstraintViolation<Classified>> violations;
    ConstraintViolation<?> violation;

    // Given
    LocaleContextHolder.setLocale(Locale.ENGLISH);
    errorMessage = "Title max length is " + Classified.CONSTRAINT_TITLE_MAX_SIZE;
    propertyPath = "title";
    classified.setTitle(
        RandomStringUtils.random(Classified.CONSTRAINT_TITLE_MAX_SIZE + 1, TestUtils.CHARS));

    // When
    violations = underTest.validate(classified);

    // Then
    assertEquals(1, CollectionUtils.size(violations));
    violation = violations.iterator().next();
    assertEquals(errorMessage, violation.getMessage());
    assertEquals(propertyPath, violation.getPropertyPath().toString());

    // Given
    LocaleContextHolder.setLocale(Locale.FRENCH);
    errorMessage = "Taille maximale du titre : " + Classified.CONSTRAINT_TITLE_MAX_SIZE;
    propertyPath = "title";
    classified.setTitle(
        RandomStringUtils.random(Classified.CONSTRAINT_TITLE_MAX_SIZE + 1, TestUtils.CHARS));

    // When
    violations = underTest.validate(classified);

    // Then
    assertEquals(1, CollectionUtils.size(violations));
    violation = violations.iterator().next();
    assertEquals(errorMessage, violation.getMessage());
    assertEquals(propertyPath, violation.getPropertyPath().toString());

  }

}
