package org.springframework.petmanagement.rest.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BindingErrorsResponse {

    public BindingErrorsResponse() {
        this(null);
    }

    public BindingErrorsResponse(UUID id) {
        this(null, id);
    }

    public BindingErrorsResponse(UUID pathId, UUID bodyId) {
        boolean onlyBodyIdSpecified = pathId == null && bodyId != null;
        if (onlyBodyIdSpecified) {
            addBodyIdError(bodyId, "must not be specified");
        }
        boolean bothIdsSpecified = pathId != null && bodyId != null;
        if (bothIdsSpecified && !java.util.Objects.equals(pathId, bodyId)) {
            addBodyIdError(bodyId, String.format("does not match pathId: %s", pathId));
        }
    }

    private void addBodyIdError(UUID bodyId, String message) {
        BindingError error = new BindingError();
        error.setObjectName("body");
        error.setFieldName("id");
        error.setFieldValue(bodyId.toString());
        error.setErrorMessage(message);
        addError(error);
    }

    private final List<BindingError> bindingErrors = new ArrayList<>();

    public void addError(BindingError bindingError) {
        this.bindingErrors.add(bindingError);
    }

    public void addAllErrors(BindingResult bindingResult) {
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            BindingError error = new BindingError();
            error.setObjectName(fieldError.getObjectName());
            error.setFieldName(fieldError.getField());
            error.setFieldValue(String.valueOf(fieldError.getRejectedValue()));
            error.setErrorMessage(fieldError.getDefaultMessage());
            addError(error);
        }
    }

    public String toJSON() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
        String errorsAsJSON = "";
        try {
            errorsAsJSON = mapper.writeValueAsString(bindingErrors);
        } catch (JsonProcessingException e) {
        }
        return errorsAsJSON;
    }

    @Override
    public String toString() {
        return "BindingErrorsResponse [bindingErrors=" + bindingErrors + "]";
    }

    protected static class BindingError {

        private String objectName;
        private String fieldName;
        private String fieldValue;
        private String errorMessage;

        public BindingError() {
            this.objectName = "";
            this.fieldName = "";
            this.fieldValue = "";
            this.errorMessage = "";
        }

        protected void setObjectName(String objectName) {
            this.objectName = objectName;
        }

        protected void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        protected void setFieldValue(String fieldValue) {
            this.fieldValue = fieldValue;
        }

        protected void setErrorMessage(String error_message) {
            this.errorMessage = error_message;
        }

        @Override
        public String toString() {
            return "BindingError [objectName=" + objectName + ", fieldName=" + fieldName + ", fieldValue=" + fieldValue
                    + ", errorMessage=" + errorMessage + "]";
        }

    }

}