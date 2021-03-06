package org.gooru.nucleus.handlers.assessment.processors.repositories.activejdbc.entities;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.gooru.nucleus.handlers.assessment.processors.repositories.activejdbc.converters.ConverterRegistry;
import org.gooru.nucleus.handlers.assessment.processors.repositories.activejdbc.converters.FieldConverter;
import org.gooru.nucleus.handlers.assessment.processors.repositories.activejdbc.validators.FieldSelector;
import org.gooru.nucleus.handlers.assessment.processors.repositories.activejdbc.validators.FieldValidator;
import org.gooru.nucleus.handlers.assessment.processors.repositories.activejdbc.validators.ReorderFieldValidator;
import org.gooru.nucleus.handlers.assessment.processors.repositories.activejdbc.validators.ValidatorRegistry;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.Table;

/**
 * Created by ashish on 7/1/16.
 */
@Table("collection")
public class AJEntityAssessment extends Model {
    // Variables used
    public static final String ID = "id";
    public static final String ASSESSMENT_EXTERNAL = "assessment-external";
    public static final String ASSESSMENT = "assessment";
    public static final String CREATOR_ID = "creator_id";
    public static final String PUBLISH_DATE = "publish_date";
    public static final String IS_DELETED = "is_deleted";
    public static final String MODIFIER_ID = "modifier_id";
    public static final String OWNER_ID = "owner_id";
    public static final String TITLE = "title";
    public static final String THUMBNAIL = "thumbnail";
    public static final String LEARNING_OBJECTIVE = "learning_objective";
    public static final String FORMAT = "format";
    public static final String METADATA = "metadata";
    public static final String TAXONOMY = "taxonomy";
    public static final String URL = "url";
    public static final String LOGIN_REQUIRED = "login_required";
    public static final String VISIBLE_ON_PROFILE = "visible_on_profile";
    public static final String COLLABORATOR = "collaborator";
    public static final String COURSE_ID = "course_id";
    public static final String UNIT_ID = "unit_id";
    public static final String LESSON_ID = "lesson_id";
    public static final String GRADING = "grading";
    public static final String TABLE_COURSE = "course";
    public static final String UPDATED_AT = "updated_at";
    public static final String UUID_TYPE = "uuid";
    public static final String JSONB_TYPE = "jsonb";
    public static final String ASSESSMENT_TYPE_NAME = "content_container_type";
    public static final String ASSESSMENT_TYPE_VALUE = "assessment";
    public static final String ASSESSMENT_EX_TYPE_VALUE = "assessment-external";
    public static final String GRADING_TYPE_NAME = "grading_type";
    public static final String GRADING_TYPE_TEACHER = "teacher";
    public static final String GRADING_TYPE_SYSTEM = "system";
    public static final String REORDER_PAYLOAD_KEY = "order";
    public static final String LICENSE = "license";

    // Queries used
    public static final String AUTHORIZER_QUERY =
        "select id, course_id, unit_id, lesson_id, owner_id, creator_id, publish_date, collaborator, grading from collection where format = "
            + "?::content_container_type and id = ?::uuid and is_deleted = ?";

    public static final String AUTH_FILTER = "id = ?::uuid and (owner_id = ?::uuid or collaborator ?? ?);";
    public static final String FETCH_ASSESSMENT_QUERY =
        "select id, title, owner_id, creator_id, original_creator_id, original_collection_id, publish_date, thumbnail, learning_objective, license,"
            + "metadata, taxonomy, setting, grading, visible_on_profile, collaborator, course_id from collection where id = ?::uuid "
            + "and format" + " = 'assessment'::content_container_type and is_deleted = false";
    public static final String FETCH_EXTERNAL_ASSSESSMENT_QUERY =
        "select id, title, owner_id, creator_id, original_creator_id, original_collection_id, thumbnail, learning_objective, "
            + "metadata, taxonomy, visible_on_profile, url, login_required, course_id from collection where id = ?::uuid and format"
            + " = 'assessment-external'::content_container_type and is_deleted = false";
    public static final String COURSE_COLLABORATOR_QUERY =
        "select collaborator from course where id = ?::uuid and is_deleted = false";
    public static final List<String> FETCH_QUERY_FIELD_LIST = Arrays.asList("id", "title", "owner_id", "creator_id",
        "original_creator_id", "original_collection_id", "publish_date", "thumbnail", "learning_objective",
        "license", "metadata", "taxonomy", "setting", "grading", "visible_on_profile");
    public static final List<String> FETCH_EA_QUERY_FIELD_LIST = Arrays.asList("id", "title", "owner_id", "creator_id",
        "original_creator_id", "original_collection_id", "thumbnail", "learning_objective", "metadata",
        "taxonomy", "visible_on_profile", "url", "login_required");

    public static final Set<String> EDITABLE_FIELDS = new HashSet<>(Arrays.asList(TITLE, THUMBNAIL, LEARNING_OBJECTIVE,
        METADATA, TAXONOMY, URL, LOGIN_REQUIRED, VISIBLE_ON_PROFILE));
    public static final Set<String> CREATABLE_FIELDS = EDITABLE_FIELDS;
    public static final Set<String> CREATABLE_EX_FIELDS = EDITABLE_FIELDS;
    public static final Set<String> MANDATORY_EX_FIELDS = new HashSet<>(Arrays.asList(TITLE, URL, LOGIN_REQUIRED));
    public static final Set<String> MANDATORY_FIELDS = new HashSet<>(Arrays.asList(TITLE));
    public static final Set<String> ADD_QUESTION_FIELDS = new HashSet<>(Arrays.asList(ID));
    public static final Set<String> COLLABORATOR_FIELDS = new HashSet<>(Arrays.asList(COLLABORATOR));
    public static final Set<String> REORDER_FIELDS = new HashSet<>(Arrays.asList(REORDER_PAYLOAD_KEY));

    private static final Map<String, FieldValidator> validatorRegistry;
    private static final Map<String, FieldConverter> converterRegistry;

    static {
        validatorRegistry = initializeValidators();
        converterRegistry = initializeConverters();
    }

    private static Map<String, FieldConverter> initializeConverters() {
        Map<String, FieldConverter> converterMap = new HashMap<>();
        converterMap.put(ID, (fieldValue -> FieldConverter.convertFieldToUuid((String) fieldValue)));
        converterMap.put(METADATA, (FieldConverter::convertFieldToJson));
        converterMap.put(TAXONOMY, (FieldConverter::convertFieldToJson));
        converterMap.put(CREATOR_ID, (fieldValue -> FieldConverter.convertFieldToUuid((String) fieldValue)));
        converterMap.put(MODIFIER_ID, (fieldValue -> FieldConverter.convertFieldToUuid((String) fieldValue)));
        converterMap.put(OWNER_ID, (fieldValue -> FieldConverter.convertFieldToUuid((String) fieldValue)));
        converterMap.put(FORMAT,
            (fieldValue -> FieldConverter.convertFieldToNamedType(fieldValue, ASSESSMENT_TYPE_NAME)));
        converterMap.put(COLLABORATOR, (FieldConverter::convertFieldToJson));
        converterMap.put(GRADING,
            (fieldValue -> FieldConverter.convertFieldToNamedType(fieldValue, GRADING_TYPE_NAME)));

        return Collections.unmodifiableMap(converterMap);
    }

    private static Map<String, FieldValidator> initializeValidators() {
        Map<String, FieldValidator> validatorMap = new HashMap<>();
        validatorMap.put(ID, (FieldValidator::validateUuid));
        validatorMap.put(TITLE, (value) -> FieldValidator.validateString(value, 1000));
        validatorMap.put(THUMBNAIL, (value) -> FieldValidator.validateStringIfPresent(value, 2000));
        validatorMap.put(LEARNING_OBJECTIVE, (value) -> FieldValidator.validateStringIfPresent(value, 20000));
        validatorMap.put(METADATA, FieldValidator::validateJsonIfPresent);
        validatorMap.put(TAXONOMY, FieldValidator::validateJsonIfPresent);
        validatorMap.put(URL, (value) -> FieldValidator.validateStringIfPresent(value, 2000));
        validatorMap.put(LOGIN_REQUIRED, FieldValidator::validateBooleanIfPresent);
        validatorMap.put(VISIBLE_ON_PROFILE, FieldValidator::validateBooleanIfPresent);
        validatorMap.put(COLLABORATOR,
            (value) -> FieldValidator.validateDeepJsonArrayIfPresent(value, FieldValidator::validateUuid));
        validatorMap.put(REORDER_PAYLOAD_KEY, new ReorderFieldValidator());
        return Collections.unmodifiableMap(validatorMap);
    }

    public static FieldSelector editFieldSelector() {
        return () -> Collections.unmodifiableSet(EDITABLE_FIELDS);
    }

    public static FieldSelector editExFieldSelector() {
        return () -> Collections.unmodifiableSet(EDITABLE_FIELDS);
    }

    public static FieldSelector reorderFieldSelector() {
        return new FieldSelector() {
            @Override
            public Set<String> allowedFields() {
                return Collections.unmodifiableSet(REORDER_FIELDS);
            }

            @Override
            public Set<String> mandatoryFields() {
                return Collections.unmodifiableSet(REORDER_FIELDS);
            }
        };
    }

    public static FieldSelector createFieldSelector() {
        return new FieldSelector() {
            @Override
            public Set<String> allowedFields() {
                return Collections.unmodifiableSet(CREATABLE_FIELDS);
            }

            @Override
            public Set<String> mandatoryFields() {
                return Collections.unmodifiableSet(MANDATORY_FIELDS);
            }
        };
    }

    public static FieldSelector createExFieldSelector() {
        return new FieldSelector() {
            @Override
            public Set<String> allowedFields() {
                return Collections.unmodifiableSet(CREATABLE_EX_FIELDS);
            }

            @Override
            public Set<String> mandatoryFields() {
                return Collections.unmodifiableSet(MANDATORY_EX_FIELDS);
            }
        };
    }

    public static FieldSelector editCollaboratorFieldSelector() {
        return new FieldSelector() {
            @Override
            public Set<String> mandatoryFields() {
                return Collections.unmodifiableSet(COLLABORATOR_FIELDS);
            }

            @Override
            public Set<String> allowedFields() {
                return Collections.unmodifiableSet(COLLABORATOR_FIELDS);
            }
        };
    }

    public static FieldSelector addQuestionFieldSelector() {
        return () -> Collections.unmodifiableSet(ADD_QUESTION_FIELDS);
    }

    public static ValidatorRegistry getValidatorRegistry() {
        return new AssessmentValidationRegistry();
    }

    public static ConverterRegistry getConverterRegistry() {
        return new AssessmentConverterRegistry();
    }

    public void setModifierId(String modifier) {
        FieldConverter fc = converterRegistry.get(MODIFIER_ID);
        if (fc != null) {
            this.set(MODIFIER_ID, fc.convertField(modifier));
        } else {
            this.set(MODIFIER_ID, modifier);
        }
    }

    public void setCreatorId(String modifier) {
        FieldConverter fc = converterRegistry.get(CREATOR_ID);
        if (fc != null) {
            this.set(CREATOR_ID, fc.convertField(modifier));
        } else {
            this.set(CREATOR_ID, modifier);
        }
    }

    public void setOwnerId(String owner) {
        FieldConverter fc = converterRegistry.get(OWNER_ID);
        if (fc != null) {
            this.set(OWNER_ID, fc.convertField(owner));
        } else {
            this.set(OWNER_ID, owner);
        }
    }

    public void setIdWithConverter(String id) {
        FieldConverter fc = converterRegistry.get(ID);
        if (fc != null) {
            this.set(ID, fc.convertField(id));
        } else {
            this.set(ID, id);
        }
    }

    public void setGrading(String grading) {
        FieldConverter fc = converterRegistry.get(GRADING);
        if (fc != null) {
            this.set(GRADING, fc.convertField(grading));
        } else {
            this.set(ID, grading);
        }
    }

    public void setLicense(Integer code) {
        this.set(LICENSE, code);
    }

    public void setTypeAssessment() {
        FieldConverter fc = converterRegistry.get(FORMAT);
        if (fc != null) {
            this.set(FORMAT, fc.convertField(ASSESSMENT_TYPE_VALUE));
        } else {
            this.set(FORMAT, ASSESSMENT_TYPE_VALUE);
        }
    }

    public void setTypeExAssessment() {
        FieldConverter fc = converterRegistry.get(FORMAT);
        if (fc != null) {
            this.set(FORMAT, fc.convertField(ASSESSMENT_EX_TYPE_VALUE));
        } else {
            this.set(FORMAT, ASSESSMENT_EX_TYPE_VALUE);
        }
    }

    private static class AssessmentValidationRegistry implements ValidatorRegistry {
        @Override
        public FieldValidator lookupValidator(String fieldName) {
            return validatorRegistry.get(fieldName);
        }
    }

    private static class AssessmentConverterRegistry implements ConverterRegistry {
        @Override
        public FieldConverter lookupConverter(String fieldName) {
            return converterRegistry.get(fieldName);
        }
    }

}
